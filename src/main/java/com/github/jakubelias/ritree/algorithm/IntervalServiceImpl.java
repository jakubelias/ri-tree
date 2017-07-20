package com.github.jakubelias.ritree.algorithm;

import com.github.jakubelias.ritree.dao.IntervalDao;
import com.google.common.math.LongMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IntervalServiceImpl implements IntervalService {

	@Override
	public void reset() {
		leftRoot = 0D;
		rightRoot = 0D;
		minstep = Double.MAX_VALUE;
		offset = null;
	}

	private static final Logger LOG = LoggerFactory.getLogger(IntervalServiceImpl.class);

	private Integer offset;
	private Double leftRoot = 0D;
	private Double rightRoot = 0D;
	private Double minstep = Double.MAX_VALUE;

	@Autowired
	private IntervalDao intervalDao;

	@Override
	public TreeParameters getCurrentTreeParameters() {
		return new TreeParameters(leftRoot, rightRoot, minstep, offset);
	}

	@Override
	public Interval computeInterval(Integer lower, Integer upper, String id) {
		// initialize offset and shift interval
		if (offset == null)
			offset = lower;

		Integer l = lower - offset;
		Integer u = upper - offset;

		// update leftRoot and rightRoot (expand data space)
		if (u < 0 && l <= 2 * leftRoot) {
			leftRoot = Math.pow(-2, LongMath.log2(-l, RoundingMode.UP));
		}

		if (0 < l && u >= 2 * rightRoot) {
			rightRoot = Math.pow(2, LongMath.log2(u, RoundingMode.UP));
		}

		// descend the tree down to the fork node
		Double node;
		Double step;
		if (u < 0) {
			node = leftRoot;
		} else if (0 < l) {
			node = rightRoot;
		} else {
			/* 0 is fork node */
			node = 0D;
		}

		// seek and strike
		for (step = Math.abs(node / 2); step >= 1; step /= 2) {
			if (u < node) {
				node -= step;
			} else if (node < l) {
				node += step;
			} else {
				/* fork reached */
				break;
			}
		}

		if (node != 0 && step < minstep) {
			minstep = step;
		}

		Interval interval = new Interval(node, lower, upper, id);

		LOG.info("interval: {}, tree parameters: {} ", interval, getCurrentTreeParameters());

		return interval;

	}

	public TreeScanResult scanTree(Integer lower, Integer upper) {

		List<Double> leftNodes = new ArrayList<>();
		List<Double> rightNodes = new ArrayList<>();

		Integer l = lower - offset;
		Integer u = upper - offset;

		// descend the tree down to the fork node
		Double node;
		Double step;
		if (u < 0) {
			node = leftRoot;
		} else if (0 < l) {
			node = rightRoot;
		} else {
			/* 0 is fork node */
			node = 0D;
		}

		Double forkNode = null;
		Double forkStep = null;

		// seek and strike
		for (step = Math.abs(node / 2); step >= 1; step /= 2) {
			if (u < node) {
				// upper < node -> node is right to interval, it is member of rigthNodes
				rightNodes.add(node);
				node -= step;
			} else if (node < l) {
				// lower > node -> node is left to interval, it is member of lefthNodes
				leftNodes.add(node);
				node += step;
			} else {
				/* fork reached */
				// inner node, most probably not member of left or right nodes.
				forkNode = node;
				forkStep = step;
				break;
			}

		}

		LOG.info("step: {}, node:", step, node);
		LOG.info("going down for lower bound ...");

		// deep down for l
		// go down until we reach the global min step or ?
		Integer queryPoint = l;

		if (forkNode == null) {
			throw new RuntimeException("should not happen");
		} else {
			node = forkNode;
		}

		if (forkStep == null) {
			throw new RuntimeException("should not happen");
		} else {
			step = forkStep;
		}

		for (step = forkStep; step >= 1; step /= 2) {

			if (queryPoint >= node) {
				// node is left to queryPoint
				leftNodes.add(node);
				node += step;

				if (step <= minstep) {
					LOG.info("Reached minstep");
					break;
				}

			} else {
				// we do add only for left side since we are searching for left boundary
				// rightNodes.add(node);
				node -= step;

				if (step <= minstep) {
					LOG.info("Reached minstep");
					break;
				}

			}

		}

		LOG.info("going down for upper bound ...");

		// deep down for l
		// go down until we reach the global minstep or ?
		queryPoint = u;

		if (forkNode == null) {
			throw new RuntimeException("should not happen");
		} else {
			node = forkNode;
		}

		if (forkStep == null) {
			throw new RuntimeException("should not happen");
		} else {
			step = forkStep;
		}

		for (step = forkStep; step >= 1; step /= 2) {

			if (queryPoint > node) {
				// node is left to queryPoint
				node += step;

				if (step <= minstep) {
					LOG.info("Reached minstep");
					break;
				}

			} else {
				// we do add only for left side since we are searching for left boundary
				// rightNodes.add(node);
				rightNodes.add(node);
				node -= step;
				if (step <= minstep) {
					LOG.info("Reached minstep");
					break;
				}

			}

		}

		return new TreeScanResult(leftNodes, rightNodes);

	}

	public List<Interval> queryForIntersectingIntervals(Integer lower, Integer upper) {

		long startTime = System.nanoTime();

		TreeScanResult treeScanResult = scanTree(lower, upper);

		// original query

		/*
		 * SELECT id FROM Intervals i, leftNodes left, rightNodes right
		 * WHERE
		 * (i.node = left.node AND i.upper >= :lower)
		 * OR (i.node = right.node AND i.lower <= :upper)
		 * OR (i.node BETWEEN :lower – offset AND :upper – offset);
		 */

		// our query without oracle collections
		String queryTemplate =
				// @formatter: off
				"SELECT * FROM Intervals i " +
						" WHERE " +
						"(i.node in :leftNodes and i.upper >= :lower )" +
						" OR "
						+ "(i.node in :rightNodes and i.lower <= :upper) " +
						" OR " +
						"(i.node BETWEEN :lower AND :upper )";
		// @formatter: on

		String query = queryTemplate.replace(":lower", lower.toString());
		query = query.replace(":upper", upper.toString());

		query = query.replace(":leftNodes", createInString(treeScanResult.getLeftNodes()));
		query = query.replace(":rightNodes", createInString(treeScanResult.getRightNodes()));

		LOG.info("query: {}", query);
		List<Interval> result = intervalDao.queryForList(query);

		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		LOG.info("Duration of ri-tree method: {} ms",duration/1000);
		return result;

	}

	@Override
	public List<Interval> queryForIntersectingIntervalsByCollScan(Integer lower, Integer upper) {

		long startTime = System.nanoTime();

		// @formatter: off
		String queryTemplate =
		"SELECT * FROM Intervals i " +
				" WHERE " +
				"(i.lower  <= :upper )" +
				" AND " +
				"(i.upper >=  :lower) ";
		// @formatter: on

		String result = queryTemplate.replace(":upper", upper.toString()).replace(":lower", lower.toString());

		List<Interval> intervals = intervalDao.queryForList(result);

		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		LOG.info("Duration of collscan : {} ms",duration/1000);

		return intervals;

	}

	private String createInString(List<Double> numbers) {
		StringBuffer sb = new StringBuffer();
		sb.append("( ");
		for (Double number : numbers) {
			sb.append(number);
			sb.append(", ");
		}

		String s = sb.toString();
		String result = s.substring(0, s.length() - 2);
		StringBuffer sb2 = new StringBuffer(result);
		sb2.append(" )");

		return sb2.toString();

	}

	public void insertInterval(Integer lower, Integer upper, String id) {
		Interval interval = computeInterval(lower, upper, id);
		intervalDao.insertInterval(interval);
	}

}

package com.github.jakubelias.ritree.dao;

import com.github.jakubelias.ritree.algorithm.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JDBCIntervalDao implements IntervalDao {

	private static final String NODE_COLUMN = "node";
	private static final String LOWER_COLUMN = "lower";
	private static final String UPPER_COLUMN = "upper";
	private static final String ID_COLUMN = "id";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) throws SQLException {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void insertInterval(Interval interval) {
		String sql = "INSERT INTO Intervals " + "(node, lower, upper, id ) " + "VALUES (?, ?, ?, ?)";

		jdbcTemplate.update(sql, ps -> {
			ps.setDouble(1, interval.getNode());
			ps.setDouble(2, interval.getLower());
			ps.setDouble(3, interval.getUpper());
			ps.setString(4, interval.getId());
		});
	}

	@Override
	public List<Interval> getAll() {
		String sql = "SELECT * from Intervals";
		return queryForList(sql);
	}

	@Override
	public void deleteAll() {
		jdbcTemplate.execute("DELETE FROM Intervals");
	}

	public List<Interval> queryForList(String sql) {
		List<Interval> intervals = new ArrayList<>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        if (rows.size() == 0) {
            return intervals;
        }

		for (Map row : rows) {
			Double node = getIntegerValue(row, NODE_COLUMN).doubleValue();
			Integer lower = getIntegerValue(row, LOWER_COLUMN);
			Integer upper = getIntegerValue(row, UPPER_COLUMN);
			String id = row.get(ID_COLUMN).toString();

			Interval interval = new Interval(node, lower, upper, id);
			intervals.add(interval);
		}
		return intervals;
	}

	private Integer getIntegerValue(Map<String, Object> row, String attrName) {
		Object value = row.get(attrName);
		return (Integer) value;

	}
}

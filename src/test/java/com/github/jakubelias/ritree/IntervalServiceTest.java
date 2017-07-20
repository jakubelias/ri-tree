package com.github.jakubelias.ritree;

import com.github.jakubelias.ritree.algorithm.Interval;
import com.github.jakubelias.ritree.algorithm.IntervalService;
import com.github.jakubelias.ritree.algorithm.TreeScanResult;
import com.github.jakubelias.ritree.dao.IntervalDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class IntervalServiceTest {

	@Autowired
	private IntervalService intervalService;


    @Autowired
    private IntervalDao intervalDao;

    @Before
    public void before(){
        //intervalDao.deleteAll();
        intervalService.reset();
    }

	@Test
	public void contextInitTest() {
		org.junit.Assert.assertNotNull(intervalService);
	}

	@Test
	public void firstTest() {
		intervalService.computeInterval(0, 0, "0-1");
		intervalService.computeInterval(5, 100, "5-100");
		intervalService.computeInterval(97, 98, "97-98");
		intervalService.computeInterval(-100, -5, "-100 -5");
	}

	@Test
	public void insertAndQueryTest() {
		intervalService.insertInterval(0, 0, "0-0");
		intervalService.insertInterval(5, 100, "5-100");
		intervalService.insertInterval(97, 98, "97-98");
        TreeScanResult treeScanResult = intervalService.scanTree(3, 11);
    }

    @Test
    public void insertAndQueryTest2() {
        intervalService.insertInterval(0, 0, "0-0");
        intervalService.insertInterval(5, 200, "5-100");
        intervalService.insertInterval(97, 98, "97-98");
		List<Interval> intervals = intervalService.queryForIntersectingIntervals(3, 11);
		Assert.assertEquals(1,intervals.size());
	}


	@Test
	public void moreComplextest() {
		intervalService.insertInterval(0, 0, "0-0");
		intervalService.insertInterval(1, 2, "1-2");
		// should be in
		intervalService.insertInterval(5, 200, "5-200");
		// should be in
		intervalService.insertInterval(13, 18, "13-18");
		// should be in
		intervalService.insertInterval(5, 27, "5-27");
		intervalService.insertInterval(97, 98, "97-98");
		// should be in
		intervalService.insertInterval(36, 89, "36-89");
		// should be in
		intervalService.insertInterval(65, 66, "65-66");
		intervalService.insertInterval(136, 345, "136-345");
		List<Interval> intervals = intervalService.queryForIntersectingIntervals(12, 67);
		Assert.assertEquals(5,intervals.size());
	}

	@Test
	public void batchTest(){
    	//generate of data in range
		generateDataInRange(0,10, 1000);
    	//query for interval
		List<Interval> result2 = intervalService.queryForIntersectingIntervalsByCollScan(330, 350);
		List<Interval> intervals = intervalService.queryForIntersectingIntervals(330, 350);
		Assert.assertEquals(intervals.size(), result2.size());
		//explain
	}


	 private void generateDataInRange(Integer lower, Integer step, Integer size){
		for (int i = 0; i < size; i++) {
			if (i % 2 == 0){
				intervalService.insertInterval(lower+i, lower+i+step,"aa");
			}else{
				intervalService.insertInterval(lower+i, lower+i+(step*2),"aa");
			}
		}
	}

}

package com.github.jakubelias.ritree;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.jakubelias.ritree.algorithm.Interval;
import com.github.jakubelias.ritree.dao.IntervalDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntervalDaoTest {

	@Autowired
	private IntervalDao intervalDao;

	@Test
	public void initTest() {
		Assert.assertNotNull(intervalDao);
	}

	@Test
    @Transactional
	public void insertTest() {
	    Interval interval = new Interval(1D,1,1,"a");
	    intervalDao.insertInterval(interval);
        List<Interval> all = intervalDao.getAll();
        Assert.assertEquals(1, all.size());
    }
}

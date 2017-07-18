package com.github.jakubelias.ritree;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntervalServiceTest {
    
    @Autowired
    private IntervalService intervalService;
    
    @Test
    public void test() {
        org.junit.Assert.assertNotNull(intervalService);
    }


    @Test
    public void sampleTest() {
        intervalService.insertInterval(0,0,"0-1");
        intervalService.insertInterval(5,100,"5-100");
        intervalService.insertInterval(97,98,"97-98");
    }
}

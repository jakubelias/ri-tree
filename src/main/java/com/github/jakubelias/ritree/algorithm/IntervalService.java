package com.github.jakubelias.ritree.algorithm;

import java.util.List;

public interface IntervalService {

    void reset();

    TreeParameters getCurrentTreeParameters();

    Interval computeInterval(Integer lower, Integer upper, String id);

    void insertInterval(Integer lower, Integer upper, String id);

    List<Interval> queryForIntersectingIntervals(Integer lower, Integer upper);

    List<Interval> queryForIntersectingIntervalsByCollScan(Integer lower, Integer upper);

     TreeScanResult scanTree(Integer lower, Integer upper);
}

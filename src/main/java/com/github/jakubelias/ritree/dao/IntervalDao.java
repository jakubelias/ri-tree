package com.github.jakubelias.ritree.dao;

import com.github.jakubelias.ritree.algorithm.Interval;

import java.util.List;

public interface IntervalDao {

    public List<Interval> getAll();

    public void deleteAll();

    void insertInterval(Interval interval);

    List<Interval> queryForList(String sql);
}

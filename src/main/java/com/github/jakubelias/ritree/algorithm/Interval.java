package com.github.jakubelias.ritree.algorithm;

public class Interval {


    public Interval(Double node, Integer lower, Integer upper, String id) {
        this.node = node;
        this.lower = lower;
        this.upper = upper;
        this.id = id;
    }

    private Double node;
    private Integer lower;
    private Integer upper;
    private String id;

    public Double getNode() {
        return node;
    }

    public void setNode(Double node) {
        this.node = node;
    }

    public Integer getLower() {
        return lower;
    }

    public void setLower(Integer lower) {
        this.lower = lower;
    }

    public Integer getUpper() {
        return upper;
    }

    public void setUpper(Integer upper) {
        this.upper = upper;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "node=" + node +
                ", lower=" + lower +
                ", upper=" + upper +
                ", id='" + id + '\'' +
                '}';
    }
}

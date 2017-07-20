package com.github.jakubelias.ritree.algorithm;

public class TreeParameters {

    TreeParameters(Double leftRoot, Double rightRoot, Double minstep, Integer offset) {
        this.leftRoot = leftRoot;
        this.rightRoot = rightRoot;
        this.minstep = minstep;
        this.offset = offset;
    }

    private Double leftRoot;
    private Double rightRoot;
    private Double minstep;
    private Integer offset;


    public Double getLeftRoot() {
        return leftRoot;
    }

    public void setLeftRoot(Double leftRoot) {
        this.leftRoot = leftRoot;
    }

    public Double getRightRoot() {
        return rightRoot;
    }

    public void setRightRoot(Double rightRoot) {
        this.rightRoot = rightRoot;
    }

    public Double getMinstep() {
        return minstep;
    }

    public void setMinstep(Double minstep) {
        this.minstep = minstep;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "TreeParameters{" +
                "leftRoot=" + leftRoot +
                ", rightRoot=" + rightRoot +
                ", minstep=" + minstep +
                ", offset=" + offset +
                '}';
    }
}

package com.github.jakubelias.ritree.algorithm;

import java.util.ArrayList;
import java.util.List;

public class TreeScanResult{
    public TreeScanResult(List<Double> leftNodes, List<Double> rightNodes) {
        this.leftNodes = leftNodes;
        this.rightNodes = rightNodes;
    }

    private List<Double> leftNodes = new ArrayList<>();
    private List<Double> rightNodes = new ArrayList<>();

    public List<Double> getLeftNodes() {
        return leftNodes;
    }

    public void setLeftNodes(List<Double> leftNodes) {
        this.leftNodes = leftNodes;
    }

    public List<Double> getRightNodes() {
        return rightNodes;
    }

    public void setRightNodes(List<Double> rightNodes) {
        this.rightNodes = rightNodes;
    }
}
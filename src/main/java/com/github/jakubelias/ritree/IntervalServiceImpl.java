package com.github.jakubelias.ritree;

import com.google.common.math.LongMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IntervalServiceImpl implements IntervalService {
    
    private static final Logger LOG = LoggerFactory.getLogger(IntervalServiceImpl.class);

    private Integer offset;
    private Double leftRoot =0D;
    private Double rightRoot =0D;
    private Double minstep =Double.MAX_VALUE;


/*    PROCEDURE insertInterval (int lower, int upper, int id) {
// initialize offset and shift interval
        if (offset = NULL) offset = lower;
        int l = lower – offset, u = upper – offset;
// update leftRoot and rightRoot
        if (u < 0 and l <= 2*leftRoot) leftRoot = –2^log2(–l);
        if (0 < l and u >= 2*rightRoot) rightRoot = 2^log2(u);
// descend the tree down to the fork node
        int node, step;
        if (u < 0) node = leftRoot;
        elsif (0 < l) node = rightRoot;
else *//* 0 is fork node *//* node = 0;
        for (step = abs(node/2); step >= 1; step /= 2) {
            if (u < node) node –= step;
            elsif (node < l) node += step;
else *//* fork reached *//* break;
        } // now node is fork node
        if (node != 0 and step < minstep) minstep = step;
        INSERT INTO Intervals VALUES (:node, :lower, :upper, :id);
    }
    */

    @Override
    public void insertInterval(Integer lower, Integer upper, String id) {
        // initialize offset and shift interval
        if (offset == null) offset = lower;
        Integer l = lower - offset;
        Integer u = upper - offset;

        // update leftRoot and rightRoot
        if (u < 0 && l <= 2*leftRoot) {
            leftRoot = Math.pow(-2, LongMath.log2(-l, RoundingMode.UP));
        }

        if (0 < l && u >= 2*rightRoot){
            rightRoot =  Math.pow(2,LongMath.log2(u, RoundingMode.UP));
        }

        // descend the tree down to the fork node
        Double node;
        Double step;
        if (u < 0) {
            node = leftRoot;
        }
        else if (0 < l) {
            node = rightRoot;
        }
        else {
            /* 0 is fork node */
            node = 0D;
        }

        for (step = Math.abs(node/2); step >= 1; step /= 2) {
            if (u < node) {
                node -= step;
            }
            else if (node < l){
                node += step;
            }
            else{
              /* fork reached */
                break;
            }

        }

        // now node is fork node
        LOG.info("forkNode: {}", node);

        if (node != 0 && step < minstep){
            minstep = step;
        }

        LOG.info("node: {}, lower: {}, upper: {}, id: {}, leftRoot: {}, rightRoot: {}, offset: {} ", node,lower,upper,id, leftRoot, rightRoot, offset);


    }
}

package com.broooapps.graphview.models;

import android.util.SparseIntArray;

import java.util.HashMap;

public class PointMap {
    public int maxValue = 0;
    private HashMap<Integer, Integer> pointMap;

    public void addPoint(int spanPos, int value) {
        if (maxValue < value) {
            maxValue = value;
        }
        pointMap.put(spanPos, value);
    }

    public PointMap() {
        pointMap = new HashMap<Integer, Integer>();
    }

    public HashMap<Integer, Integer> getPointMap() {
        return pointMap;
    }

    public GraphPoint get(int spanPos) {
        if (pointMap.containsKey(spanPos)) {
            return new GraphPoint(spanPos, pointMap.get(spanPos));
        } else {
            return new GraphPoint(spanPos, 0);
        }
    }
}
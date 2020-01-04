package com.broooapps.graphview.models;

public class GraphPoint {
    int spanPos;
    int value;

    float x, y;

    public GraphPoint(int spanPos, int value) {
        this.spanPos = spanPos;
        this.value = value;
    }

    public int getSpanPos() {
        return spanPos;
    }

    public int getValue() {
        return value;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

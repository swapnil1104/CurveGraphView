package com.broooapps.graphview.models;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class GraphData {
    private WeakReference<Context> ctxWeakRef;
    private PointMap graphDataPoints;

    private boolean isStraightLine = false;
    private int pointRadius;

    private boolean animateLine = false;

    @ColorInt
    int strokeColor = 0, pointColor = 0;

    @ColorInt
    int gradientStartColor = 0, gradientEndColor = 0;

    int maxValue;

    private GraphData(Builder builder) {
        if (builder.graphDataPoints != null) {
            this.graphDataPoints = builder.graphDataPoints;
        } else {
            this.graphDataPoints = new PointMap();
        }
        if (builder.ctxWeakRef != null) {
            this.ctxWeakRef = new WeakReference<>(builder.ctxWeakRef.get());
        }
        this.strokeColor = builder.strokeColor;
        this.gradientEndColor = builder.gradientEndColor;
        this.gradientStartColor = builder.gradientStartColor;

        this.isStraightLine = builder.isStraightLine;
        this.pointRadius = builder.pointRadius;
        if (builder.pointColor == 0) {
            this.pointColor = this.strokeColor;
        } else {
            this.pointColor = builder.pointColor;
        }
        this.animateLine = builder.animateLine;
    }

    public WeakReference<Context> getCtxWeakRef() {
        return ctxWeakRef;
    }

    public PointMap getGraphDataPoints() {
        return graphDataPoints;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getGradientStartColor() {
        return gradientStartColor;
    }

    public int getGradientEndColor() {
        return gradientEndColor;
    }

    public int getPointColor() {
        return pointColor;
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public boolean isStraightLine() {
        return isStraightLine;
    }

    public boolean isAnimateLine() {
        return animateLine;
    }

    public static IGraphData builder(Context context) {
        return new Builder(context);
    }

    public static class Builder implements IGraphData, IGraphStroke {
        private WeakReference<Context> ctxWeakRef;
        private PointMap graphDataPoints;

        private boolean isStraightLine = false;
        private boolean animateLine = false;
        private int pointRadius = 4;

        @ColorInt
        private int strokeColor = 0, pointColor = 0;
        @ColorInt
        private int gradientStartColor = 0, gradientEndColor = 0;

        private Builder() {
        }

        private Builder(Context context) {
            this.ctxWeakRef = new WeakReference<>(context);
        }

        @Override
        public IGraphStroke setPointMap(PointMap graphPoints) {
            this.graphDataPoints = graphPoints;
            return this;
        }

        @Override
        public Builder setGraphStroke(int colorRes) {
            this.strokeColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setGraphGradient(int start, int end) {
            this.gradientEndColor = ContextCompat.getColor(ctxWeakRef.get(), end);
            this.gradientStartColor = ContextCompat.getColor(ctxWeakRef.get(), start);
            return this;
        }

        public Builder setPointColor(int color) {
            this.pointColor = ContextCompat.getColor(ctxWeakRef.get(), color);
            return this;
        }

        public Builder animateLine(boolean animate) {
            this.animateLine = animate;
            return this;
        }

        public Builder setStraightLine(boolean isStraightLine) {
            this.isStraightLine = isStraightLine;
            return this;
        }

        public Builder setPointRadius(int radius) {
            this.pointRadius = radius;
            return this;
        }

        public GraphData build() {
            return new GraphData(this);
        }
    }

    public interface IGraphData {
        IGraphStroke setPointMap(PointMap graphPoints);
    }

    public interface IGraphStroke {
        Builder setGraphStroke(int colorRes);
    }
}


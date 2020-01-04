package com.broooapps.graphview.models;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class GraphData {
    private WeakReference<Context> ctxWeakRef;
    private PointMap graphDataPoints;
    @ColorInt
    int strokeColor = 0;

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

    public static IGraphData builder(Context context) {
        return new Builder(context);
    }

    public static class Builder implements IGraphData, IGraphStroke {
        private WeakReference<Context> ctxWeakRef;
        private PointMap graphDataPoints;
        @ColorInt
        int strokeColor = 0;
        @ColorInt
        int gradientStartColor = 0, gradientEndColor = 0;

        private Builder() {
        }

        private Builder(Context context) {
            this.ctxWeakRef = new WeakReference<>(context);
        }

        @Override
        public IGraphStroke setData(PointMap graphPoints) {
            this.graphDataPoints = graphPoints;
            return this;
        }

        @Override
        public Builder setStroke(int colorRes) {
            this.strokeColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setGradientColor(int start, int end) {
            this.gradientEndColor = ContextCompat.getColor(ctxWeakRef.get(), end);
            this.gradientStartColor = ContextCompat.getColor(ctxWeakRef.get(), start);
            return this;
        }

        public GraphData build() {
            return new GraphData(this);
        }
    }

    public interface IGraphData {
        IGraphStroke setData(PointMap graphPoints);
    }

    public interface IGraphStroke {
        Builder setStroke(int colorRes);
    }
}


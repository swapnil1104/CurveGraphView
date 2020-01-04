package com.broooapps.graphview;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;


public class CurveGraphConfig {

    private WeakReference<Context> ctxWeakRef;

    @ColorInt
    int maxYValue = 0;

    @ColorInt
    int axisColor = 0;

    @ColorInt
    int strokeColor = 0;

    @ColorInt
    int backgroundColor = 0;

    @ColorInt
    int gradientStartColor = 0, gradientEndColor = 0;

    @ColorInt
    int xAxisScaleColor = 0;

    @ColorInt
    int guidelineColor = 0;

    @ColorInt
    int yAxisScaleColor = 0;

    int guidelineCount = 0;

    int intervalCount = 0;

    String noDataMsg = null;

    public CurveGraphConfig(Builder builder) {
        if (xAxisScaleColor == 0)
            xAxisScaleColor = ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.Black);
        if (strokeColor == 0)
            strokeColor = ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.strokeColor);
        if (backgroundColor == 0)
            backgroundColor = ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.backgroundColor);
        if (gradientStartColor == 0)
            gradientStartColor = ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.gradientStartColor);
        if (gradientEndColor == 0)
            gradientEndColor = ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.gradientEndColor);
        if (guidelineColor == 0)
            guidelineColor = ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.guidelineColor);
        if (yAxisScaleColor == 0)
            yAxisScaleColor = ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.scaleTextColor);
        if (axisColor == 0)
            axisColor = ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.axisColor);


        if (intervalCount == 0) intervalCount = 5;
        if (guidelineCount == 0) guidelineCount = 4;
        if (noDataMsg == null) noDataMsg = "No Data";

        builder.ctxWeakRef.clear();
    }

    public static class Builder implements IMaxValue {
        private WeakReference<Context> ctxWeakRef;

        @ColorInt
        int axisColor = 0;

        @ColorInt
        int strokeColor = 0;

        @ColorInt
        int backgroundColor = 0;

        @ColorInt
        int gradientStartColor = 0, gradientEndColor = 0;

        @ColorInt
        int xAxisScaleColor = 0;

        @ColorInt
        int guidelineColor = 0;

        @ColorInt
        int yAxisScaleColor = 0;

        int guidelineCount = 0;

        int intervalCount = 0;

        String noDataMsg = null;

        private Builder() {}

        public Builder(Context context) {
            ctxWeakRef = new WeakReference<>(context);
        }

        @Override
        public Builder setMaxValue() {
            return null;
        }
        public Builder setNoDataMsg(String message) {
            this.noDataMsg = message;
            return this;
        }

        public Builder getGuidelineCount(int count) {
            this.guidelineCount = count;
            return this;
        }

        public Builder setGuidelineColor(int colorRes) {
            this.guidelineColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setGradientColor(int start, int end) {
            this.gradientEndColor = ContextCompat.getColor(ctxWeakRef.get(), end);
            this.gradientStartColor = ContextCompat.getColor(ctxWeakRef.get(), start);
            return this;
        }

        public Builder setxAxisScaleColor(int colorRes) {
            this.xAxisScaleColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setAxisColor(int colorRes) {
            this.axisColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setBackgroundColor(int colorRes) {
            backgroundColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setyAxisScaleColor(int colorRes) {
            yAxisScaleColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setIntervalDisplayCount(int intervalCount) {
            this.intervalCount = intervalCount;
            return this;
        }

        public Builder setStrokeColor(int colorRes) {
            strokeColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public CurveGraphConfig build() {
            return new CurveGraphConfig(this);
        }
    }

    public interface IMaxValue {
        Builder setMaxValue();
    }
}

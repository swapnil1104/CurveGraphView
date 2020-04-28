package com.broooapps.graphview;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;


public class CurveGraphConfig {

    @ColorInt
    int axisColor;

    @ColorInt
    int xAxisScaleColor;

    @ColorInt
    int guidelineColor;

    @ColorInt
    int yAxisScaleColor;

    int guidelineCount;

    int horizontalGuidelineCount;

    int intervalCount;

    long animationDuration;

    String noDataMsg;

    private CurveGraphConfig(Builder builder) {
        xAxisScaleColor = (builder.xAxisScaleColor == 0) ? ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.Black) : builder.xAxisScaleColor;
        yAxisScaleColor = (builder.yAxisScaleColor == 0) ? ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.scaleTextColor) : builder.yAxisScaleColor;
        guidelineColor = (builder.guidelineColor == 0) ? ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.guidelineColor) : builder.guidelineColor;
        axisColor = (builder.axisColor == 0) ? ContextCompat.getColor(builder.ctxWeakRef.get(), R.color.axisColor) : builder.axisColor;
        intervalCount = builder.intervalCount;
        guidelineCount = builder.guidelineCount;
        horizontalGuidelineCount = builder.horizontalGuidelineCount;
        noDataMsg = (builder.noDataMsg == null) ? "NO DATA" : builder.noDataMsg;
        animationDuration = builder.animationDuration;
        builder.ctxWeakRef.clear();
    }

    public static class Builder {
        private WeakReference<Context> ctxWeakRef;

        @ColorInt
        int axisColor = 0;

        @ColorInt
        int xAxisScaleColor = 0;

        @ColorInt
        int guidelineColor = 0;

        @ColorInt
        int yAxisScaleColor = 0;

        int guidelineCount = 0;

        int intervalCount = 0;

        int horizontalGuidelineCount = 0;

        long animationDuration = 2000;

        String noDataMsg = null;

        private Builder() {
        }

        public Builder(Context context) {
            ctxWeakRef = new WeakReference<>(context);
        }

        public Builder setNoDataMsg(String message) {
            this.noDataMsg = message;
            return this;
        }

        public Builder setVerticalGuideline(int count) {
            this.guidelineCount = count;
            return this;
        }

        public Builder setHorizontalGuideline(int count) {
            this.horizontalGuidelineCount = count;
            return this;
        }

        public Builder setGuidelineColor(int colorRes) {
            this.guidelineColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setxAxisScaleTextColor(int colorRes) {
            this.xAxisScaleColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setAxisColor(int colorRes) {
            this.axisColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setyAxisScaleTextColor(int colorRes) {
            yAxisScaleColor = ContextCompat.getColor(ctxWeakRef.get(), colorRes);
            return this;
        }

        public Builder setIntervalDisplayCount(int intervalCount) {
            this.intervalCount = intervalCount;
            return this;
        }

        public Builder setAnimationDuration(long duration) {
            this.animationDuration = duration;
            return this;
        }

        public CurveGraphConfig build() {
            return new CurveGraphConfig(this);
        }
    }
}

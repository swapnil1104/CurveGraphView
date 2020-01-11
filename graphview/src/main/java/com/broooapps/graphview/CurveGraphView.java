package com.broooapps.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.GraphPoint;
import com.broooapps.graphview.models.PointMap;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurveGraphView extends View {

    DecimalFormat df = new DecimalFormat("#######.##");

    // Builder fields
    private int guidelineCount;
    private int intervalCount;

    private GraphData[] graphDataArray = {};

    int defStyleAttr;
    int viewHeight, viewWidth;
    int graphHeight, graphWidth, graphPadding = 32;

    private Path path = new Path();

    private Paint xAxisScalePaint;
    private Paint yAxisScalePaint;
    private Paint axisLinePaint;
    private Paint graphPointPaint;
    private Paint graphStrokePaint;
    private Paint graphGradientPaint;
    private Paint guidelinePaint;

    private RectF xAxis, yAxis;

    @NonNull
    private WeakReference<Context> contextWeakReference;

    @Nullable
    private AttributeSet attributeSet;
    int xSpan = 0;
    private float maxVal;
    private String noDataMsg;

    public CurveGraphView(Context context) {
        this(context, null);
    }

    public CurveGraphView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurveGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.contextWeakReference = new WeakReference<>(context);
        this.attributeSet = attrs;
        this.defStyleAttr = defStyleAttr;

        initialize();

        configure(new CurveGraphConfig.Builder(getContext()).build());
    }

    private void initialize() {
        xAxisScalePaint = new Paint();
        graphPointPaint = new Paint();
        axisLinePaint = new Paint();
        graphGradientPaint = new Paint();
        graphStrokePaint = new Paint();
        yAxisScalePaint = new Paint();
        guidelinePaint = new Paint();
    }

    public void configure(CurveGraphConfig builder) {
        xAxis = new RectF(0, 0, 0, 0);
        yAxis = new RectF(0, 0, 0, 0);

        this.noDataMsg = builder.noDataMsg;

        this.guidelineCount = builder.guidelineCount;
        this.intervalCount = builder.intervalCount;

        xAxisScalePaint.setTextSize(28f);
        xAxisScalePaint.setAntiAlias(true);
        xAxisScalePaint.setColor(builder.xAxisScaleColor);
        xAxisScalePaint.setStyle(Paint.Style.FILL);

        yAxisScalePaint.setTextSize(28f);
        yAxisScalePaint.setAntiAlias(true);
        yAxisScalePaint.setColor(builder.yAxisScaleColor);
        yAxisScalePaint.setStyle(Paint.Style.FILL);

        graphPointPaint.setAntiAlias(true);
        graphPointPaint.setStyle(Paint.Style.FILL);

        axisLinePaint.setAntiAlias(true);
        axisLinePaint.setColor(builder.axisColor);
        axisLinePaint.setStyle(Paint.Style.FILL);

        guidelinePaint.setStrokeWidth(2);
        guidelinePaint.setAntiAlias(true);
        guidelinePaint.setColor(builder.guidelineColor);
        guidelinePaint.setStyle(Paint.Style.STROKE);
        guidelinePaint.setPathEffect(new DashPathEffect(new float[]{10f, 8f}, 0f));

        graphGradientPaint.setStyle(Paint.Style.FILL);
        graphGradientPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        graphGradientPaint.setAntiAlias(true);

        graphStrokePaint.setStrokeWidth(2);
        graphStrokePaint.setAntiAlias(true);
        graphStrokePaint.setStyle(Paint.Style.STROKE);

    }

    public void setData(int span, int maxVal, GraphData... graphDataArray) {

        this.maxVal = maxVal;
        this.xSpan = span;
        this.graphDataArray = graphDataArray;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGuideline(canvas);
        drawLineGraph(canvas);
        drawScaleText(canvas);
        drawInterval(canvas);
        drawAxis(canvas);
    }

    private void drawAxis(Canvas canvas) {
        canvas.drawRect(xAxis, axisLinePaint);
        canvas.drawRect(yAxis, axisLinePaint);
    }

    private void drawInterval(Canvas canvas) {
        for (int i = 1; i <= intervalCount; i++) {
            String msg = String.valueOf(df.format(i * ((float) xSpan / intervalCount)));
            int xPos = (i * (graphWidth - (graphPadding) * 2)) / (intervalCount + 1);
            int yPos = (int) (viewHeight - xAxisScalePaint.getTextSize());

            canvas.drawText(msg, xPos, yPos, xAxisScalePaint);
        }
    }

    private void drawGuideline(Canvas canvas) {
        for (int i = 1; i <= guidelineCount; i++) {
            path.reset();

            int xPos = (i * (graphWidth - (graphPadding) * 2)) / (guidelineCount + 1);
            path.moveTo(xPos, graphPadding);
            path.lineTo(xPos, yAxis.top);

            canvas.drawPath(path, guidelinePaint);
        }
    }

    private void drawLineGraph(Canvas canvas) {
        int morphedGraphHeight = (int) yAxis.top - 12;
        float f1, f2, f4;
        float scaleFactor = maxVal / (morphedGraphHeight - graphPadding);

        for (int graphDataIndex = 0; graphDataIndex < graphDataArray.length; graphDataIndex++) {
            GraphData graphData = graphDataArray[graphDataIndex];

            updateStyleForGraphData(graphData);
            PointMap pointMap = graphData.getGraphDataPoints();

            GraphPoint prevDataPoint = new GraphPoint(graphPadding, morphedGraphHeight);

            path.reset();
            path.moveTo(graphPadding, yAxis.top);
            path.lineTo(graphPadding, morphedGraphHeight);
            float lastXPoint = graphWidth - graphPadding * 2;

            for (int spanIndex = 0; spanIndex <= xSpan; spanIndex++) {
                GraphPoint graphPoint = pointMap.get(spanIndex);
                if (graphPoint.getSpanPos() == 0) {
                    path.lineTo(graphPadding, morphedGraphHeight - (graphPoint.getValue() / scaleFactor));
                    graphPoint.setX(graphPadding);

                } else {
                    graphPoint.setX(graphPoint.getSpanPos() * (graphWidth - (graphPadding) * 2) / (xSpan));

                }
                if (scaleFactor > 0) {
                    graphPoint.setY(morphedGraphHeight - (graphPoint.getValue() / scaleFactor));
                } else {
                    graphPoint.setY(morphedGraphHeight);
                }
                if (graphData.isStraightLine()) {
                    path.lineTo(graphPoint.getX(), graphPoint.getY());
                } else {
                    if (spanIndex > 0) {
                        f1 = (prevDataPoint.getX() + graphPoint.getX()) / 2;
                        f2 = prevDataPoint.getY();
                        f4 = graphPoint.getY();
                        path.cubicTo(f1, f2, f1, f4, graphPoint.getX(), graphPoint.getY());
                    }
                }

                prevDataPoint = graphPoint;
                if (graphPoint.getY() != morphedGraphHeight) {
                    canvas.drawCircle(graphPoint.getX(), graphPoint.getY(), graphData.getPointRadius(), graphPointPaint);
                }

            }

            path.lineTo(lastXPoint, morphedGraphHeight);
            path.lineTo(lastXPoint, yAxis.top);
            path.moveTo(graphPadding, yAxis.top);
            path.close();

            if (graphData.getGradientStartColor() != 0) {
                canvas.drawPath(path, graphGradientPaint);
            }
            canvas.drawPath(path, graphStrokePaint);
        }
    }

    private void updateStyleForGraphData(GraphData graphData) {
        graphStrokePaint.setColor(graphData.getStrokeColor());
        graphPointPaint.setColor(graphData.getPointColor());

        if (graphData.getGradientStartColor() != 0) {
            graphGradientPaint.setShader(new LinearGradient(0, 0, 0, graphHeight,
                    graphData.getGradientStartColor(),
                    graphData.getGradientEndColor(), Shader.TileMode.MIRROR));
        }

    }

    private void drawScaleText(Canvas canvas) {
        if (maxVal > 0) {
            for (float i = 5; i > 0; i--) {
                float value = maxVal * i / 5;

                float y = ((graphHeight - 32) * (5 - i) / 5f) + graphPadding + yAxisScalePaint.getTextSize() / 2;
                canvas.drawText(String.valueOf((int) value), graphWidth - graphPadding + 8, y, yAxisScalePaint);
            }
        } else {
            canvas.drawText(this.noDataMsg, (graphWidth - graphPadding * 2) / 2, (graphHeight + graphPadding * 2) / 2, yAxisScalePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingTop();
        viewWidth = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();

        graphHeight = viewHeight - graphPadding;
        graphWidth = viewWidth - graphPadding;

        setMeasuredDimension(viewWidth, viewHeight);

        xAxis.left = graphPadding - 4;
        xAxis.top = graphPadding - 4;
        xAxis.right = graphPadding;
        xAxis.bottom = graphHeight;

        yAxis.left = graphPadding;
        yAxis.top = graphHeight - yAxisScalePaint.getTextSize();
        yAxis.right = graphWidth - graphPadding;
        yAxis.bottom = graphHeight - yAxisScalePaint.getTextSize() + 4;

        invalidate();
    }


}

package com.broooapps.graphview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.GraphPoint;
import com.broooapps.graphview.models.PointMap;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Swapnil Tiwari on 2019-08-07.
 * swapniltiwari775@gmail.com
 */
public class CurveGraphView extends View {

    DecimalFormat df = new DecimalFormat("#######.##");

    // Builder fields
    private int verticalGuidelineCount;
    private int intervalCount;
    private int horizontalGuidelineCount;

    private GraphData[] graphDataArray = {};

    int viewHeight, viewWidth;
    int graphHeight, graphWidth, graphPadding = 32;

    private Path vPath = new Path();
    private Path hPath = new Path();

    private Paint xAxisScalePaint;
    private Paint yAxisScalePaint;
    private Paint axisLinePaint;
    private Paint graphPointPaint;
    private Paint graphStrokePaint;
    private Paint graphGradientPaint;
    private Paint guidelinePaint;

    private RectF xAxis, yAxis;

    int xSpan = 0;
    private float maxVal;
    private String noDataMsg;

    // Paint Object for background
    Paint mBgPaint;
    Path boundaryPath;

    ArrayList<Path> pathArrayList;
    float[] length;
    ArrayList<Paint> graphStrokePaintsList;
    ArrayList<Paint> graphGradientPaintsList;
    ArrayList<ArrayList<GraphPoint>> graphPointsList;
    ArrayList<Paint> graphPointPaintsList;

    ValueAnimator valueAnimator;
    ObjectAnimator ob;


    private CurveGraphConfig.Builder builder;
    private boolean isConfigured;

    // viewWidth of the view
    int width;

    // viewHeight of the view
    int height;

    // Context
    private Context context;

    private long animationDuration;

    public CurveGraphView(Context context) {
        this(context, null);
    }

    public CurveGraphView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurveGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        pathArrayList = new ArrayList<>();
        length = new float[]{};
        graphStrokePaintsList = new ArrayList<>();
        graphGradientPaintsList = new ArrayList<>();
        graphPointsList = new ArrayList<>();
        graphPointPaintsList = new ArrayList<>();
    }

    public void configure(CurveGraphConfig builder) {
        isConfigured = true;
        if (xAxis == null) {
            xAxis = new RectF(0, 0, 0, 0);
        }
        if (yAxis == null) {
            yAxis = new RectF(0, 0, 0, 0);
        }
        this.noDataMsg = builder.noDataMsg;
        this.verticalGuidelineCount = builder.guidelineCount;
        this.horizontalGuidelineCount = builder.horizontalGuidelineCount;
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

        animationDuration = builder.animationDuration;
    }

    public void setData(int span, int maxVal, final GraphData... graphDataArray) {
        this.maxVal = maxVal;
        this.xSpan = span;
        this.graphDataArray = graphDataArray;
        graphGradientPaintsList.clear();
        graphStrokePaintsList.clear();
        graphPointPaintsList.clear();
        graphPointsList.clear();
        pathArrayList = constructPaths();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (ob != null) {
            ob.cancel();
        }
        length = getLengths();
        ob = ObjectAnimator.ofFloat(this, "phase", 1f, 0f);
        ob.setDuration(animationDuration);
        ob.setInterpolator(new AccelerateInterpolator());
        ob.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ob.setAutoCancel(true);
        }
        ob.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startGradientAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                for (int i = 0; i < graphGradientPaintsList.size(); i++) {
                    if (graphDataArray[i].isAnimateLine()) {
                        if (graphGradientPaintsList.get(i) != null)
                            graphGradientPaintsList.get(i).setAlpha(0);
                        graphPointPaintsList.get(i).setAlpha(0);
                    }
                }
                invalidate();

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAxis(canvas);
        if (!(graphDataArray.length == 0 || noDataInGraph())) {
            drawVertGuideline(canvas);
            drawHorizontalGuidelines(canvas);
        }
        drawScaleText(canvas);
        drawInterval(canvas);

        drawGraphPaths(canvas);
        drawGraphPoints(canvas);
        drawGradients(canvas);
    }

    private void drawAxis(Canvas canvas) {
        canvas.drawRect(xAxis, axisLinePaint);
        canvas.drawRect(yAxis, axisLinePaint);
    }

    private void drawInterval(Canvas canvas) {
        if (intervalCount == 0 || graphDataArray.length == 0) return;
        for (int i = 1; i <= intervalCount; i++) {
            String msg = df.format(i * ((float) xSpan / intervalCount));
            int xPos = (i * (graphWidth - (graphPadding) * 2)) / (intervalCount + 1);
            int yPos = (int) (viewHeight - xAxisScalePaint.getTextSize());

            canvas.drawText(msg, xPos, yPos, xAxisScalePaint);
        }
    }

    private void drawVertGuideline(Canvas canvas) {
        if (verticalGuidelineCount == 0) return;
        for (int i = 1; i <= verticalGuidelineCount; i++) {
            vPath.reset();

            int xPos = (i * (graphWidth - (graphPadding) * 2)) / (verticalGuidelineCount + 1);
            vPath.moveTo(xPos, graphPadding);
            vPath.lineTo(xPos, yAxis.top);

            canvas.drawPath(vPath, guidelinePaint);
        }
    }


    private void drawHorizontalGuidelines(Canvas canvas) {
        if (horizontalGuidelineCount == 0) return;
        for (int i = 1; i <= horizontalGuidelineCount; i++) {
            hPath.reset();

            int yPos = (i * graphHeight - graphPadding) / (horizontalGuidelineCount + 1);
            hPath.moveTo(graphPadding, yPos);
            hPath.lineTo(graphWidth - graphPadding, yPos);

            canvas.drawPath(hPath, guidelinePaint);
        }
    }

    private boolean noDataInGraph() {
        for (int i = 0; i < graphDataArray.length; i++) {
            GraphData gd = graphDataArray[i];
            if (gd.getGraphDataPoints().getPointMap().isEmpty()) continue;
            return false;
        }
        return true;
    }

    private void startGradientAnimation() {
        valueAnimator = new ValueAnimator();
        PropertyValuesHolder alphaFactor = PropertyValuesHolder.ofInt("PROPERTY_ALPHA", 0, 255);

        valueAnimator.setValues(alphaFactor);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                for (int i = 0; i < graphGradientPaintsList.size(); i++) {

                    if (graphDataArray[i].isAnimateLine()) {
                        if (graphGradientPaintsList.get(i) != null)
                            graphGradientPaintsList.get(i).setAlpha((Integer) animation.getAnimatedValue("PROPERTY_ALPHA"));
                        graphPointPaintsList.get(i).setAlpha((Integer) animation.getAnimatedValue("PROPERTY_ALPHA"));
                    }
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private float[] getLengths() {
        float[] array = new float[pathArrayList.size()];
        int itr = 0;
        for (Path p : pathArrayList) {
            array[itr++] = new PathMeasure(p, false).getLength();
        }
        return array;
    }


    private void drawGraphPoints(Canvas canvas) {
        for (int i = 0; i < graphPointsList.size(); i++) {
            for (GraphPoint gp : graphPointsList.get(i)) {
                Paint p = graphPointPaintsList.get(i);
                p.setStyle(Paint.Style.FILL);
                canvas.drawCircle(gp.getX(), gp.getY(), graphDataArray[i].getPointRadius(), p);
                p.setStyle(Paint.Style.STROKE);
            }
        }
    }

    private ArrayList<Path> constructPaths() {
        int morphedGraphHeight = (int) yAxis.top - 12;
        float f1, f2, f4;
        float scaleFactor = maxVal / (morphedGraphHeight - graphPadding);

        ArrayList<Path> pathList = new ArrayList<>();

        for (int i = 0; i < graphDataArray.length; i++) {
            GraphData graphData = graphDataArray[i];
            updateStyleForGraphData(i, graphData);
            PointMap pointMap = graphData.getGraphDataPoints();
            GraphPoint prevDataPoint = new GraphPoint(graphPadding, morphedGraphHeight);
            Path path = new Path();
            path.moveTo(graphPadding, morphedGraphHeight);
            float lastXPoint = graphWidth - graphPadding * 2;
            ArrayList<GraphPoint> gpList = new ArrayList<>();
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
                    gpList.add(graphPoint);
                }
            }
            graphPointsList.add(gpList);
            path.lineTo(lastXPoint, morphedGraphHeight);
            path.lineTo(lastXPoint, yAxis.top);
            path.lineTo(graphPadding, yAxis.top);
            path.close();
            pathList.add(path);
        }
        return pathList;
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

    private void drawGradients(Canvas canvas) {
        for (int i = 0; i < pathArrayList.size(); i++) {
            if (graphGradientPaintsList.get(i) != null) {
                canvas.drawPath(pathArrayList.get(i), graphGradientPaintsList.get(i));
            }
        }
    }

    private void drawGraphPaths(Canvas canvas) {
        for (int i = 0; i < pathArrayList.size(); i++) {
            canvas.drawPath(pathArrayList.get(i), graphStrokePaintsList.get(i));
        }
    }


    private void updateStyleForGraphData(int pos, GraphData graphData) {

        Paint pointPaint = new Paint();
        pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pointPaint.setColor(graphData.getPointColor());
        graphPointPaintsList.add(pointPaint);

        Paint strokePaint = new Paint();
        strokePaint.setStrokeWidth(2);
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(graphData.getStrokeColor());
        graphStrokePaintsList.add(strokePaint);

        if (graphData.getGradientStartColor() != 0) {
            Paint gradientPaint = new Paint();
            gradientPaint.setStyle(Paint.Style.FILL);
            gradientPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
            gradientPaint.setAntiAlias(true);
            if (graphDataArray[pos].isAnimateLine()) {
                pointPaint.setAlpha(0);
                gradientPaint.setAlpha(0);
            }

            gradientPaint.setShader(new LinearGradient(0, 0, 0, graphHeight,
                    graphData.getGradientStartColor(),
                    graphData.getGradientEndColor(), Shader.TileMode.MIRROR));

            graphGradientPaintsList.add(gradientPaint);
        } else {
            graphGradientPaintsList.add(null);
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

    }

    public void setPhase(float phase) {
        for (int i = 0; i < pathArrayList.size(); i++) {
            if (graphDataArray[i].isAnimateLine()) {
                graphStrokePaintsList.get(i).setPathEffect(createPathEffect(length[i], phase, 0f));
            }
        }
        invalidate();
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[]{pathLength, pathLength},
                Math.max(phase * pathLength, .0f));
    }
}
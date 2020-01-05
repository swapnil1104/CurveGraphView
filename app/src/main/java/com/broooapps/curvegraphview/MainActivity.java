package com.broooapps.curvegraphview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.broooapps.graphview.CurveGraphConfig;
import com.broooapps.graphview.CurveGraphView;
import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.PointMap;

public class MainActivity extends AppCompatActivity {
    CurveGraphView curveGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        curveGraphView = findViewById(R.id.cgv);

        curveGraphView.configure(
                new CurveGraphConfig.Builder(this)
                        .setAxisColor(R.color.Blue)                                             // Set X and Y axis line color stroke.
                        .setIntervalDisplayCount(7)                                             // Set number of values to be displayed in X ax
                        .setGuidelineCount(2)                                                   // Set number of background guidelines to be shown.
                        .setGuidelineColor(R.color.GreenYellow)                                 // Set color of the visible guidelines.
                        .setNoDataMsg(" No Data ")                                              // Message when no data is provided to the view.
                        .setxAxisScaleTextColor(R.color.Black)                                  // Set X axis scale text color.
                        .setyAxisScaleTextColor(R.color.Black)                                  // Set Y axis scale text color
                        .build()
        );


        PointMap pointMap = new PointMap();
        pointMap.addPoint(0, 100);
        pointMap.addPoint(1, 500);
        pointMap.addPoint(4, 600);
        pointMap.addPoint(5, 800);

        GraphData gd = GraphData.builder(this)
                .setPointMap(pointMap)
                .setGraphStroke(R.color.Black)
                .setGraphGradient(R.color.BlueViolet, R.color.RoyalBlue)
                .build();

        PointMap p2 = new PointMap();
        p2.addPoint(0, 140);
        p2.addPoint(1, 700);
        p2.addPoint(2, 100);
        p2.addPoint(3, 0);
        p2.addPoint(4, 190);

        GraphData gd2 = GraphData.builder(this)
                .setPointMap(p2)
                .setGraphStroke(R.color.Green)
                .setGraphGradient(R.color.gradientStartColor, R.color.gradientEndColor)
                .build();


        curveGraphView.setData(5, 1000, gd, gd2);
    }
}

package com.broooapps.curvegraphview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.ConditionVariable;

import com.broooapps.graphview.CurveGraphConfig;
import com.broooapps.graphview.CurveGraphView;
import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.GraphPoint;
import com.broooapps.graphview.models.PointMap;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    CurveGraphView cgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cgv = findViewById(R.id.cgv);

        PointMap pointMap = new PointMap();
        pointMap.addPoint(0, 100);
        pointMap.addPoint(1, 50);
        pointMap.addPoint(2, 200);
        pointMap.addPoint(3, 120);
        pointMap.addPoint(4, 160);

        GraphData gd = GraphData.builder(this)
                .setData(pointMap).setStroke(R.color.Blue)
                .build();

        PointMap pointMap2 = new PointMap();
        pointMap2.addPoint(0, 140);
        pointMap2.addPoint(1, 20);
        pointMap2.addPoint(2, 100);
        pointMap2.addPoint(3, 40);
        pointMap2.addPoint(4, 190);

        GraphData gd2 = GraphData.builder(this)
                .setData(pointMap2).setStroke(R.color.Red).setGradientColor(R.color.gradientStartColor, R.color.gradientEndColor)
                .build();


        cgv.setData(5, 200, gd, gd2);
    }
}

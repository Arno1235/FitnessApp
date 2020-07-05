package com.ArnoVanEetvelde.fitnessapp;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ProgressChart {

    private ArrayList<Double> data;
    private double goal, step, smoothness;
    private int numberOfPoints, startDate, dataType;
    private boolean panX;

    public ProgressChart(ArrayList<Double> data, double goal, double step, double smoothness, int numberOfPoints, boolean panX, int startDate, int dataType){
        this.data = data;
        this.goal = goal;
        this.step = step;
        this.smoothness = smoothness;
        this.numberOfPoints = numberOfPoints;
        this.panX = panX;
        this.startDate = startDate;
        this.dataType = dataType;
    }

    public XYMultipleSeriesRenderer getChartView(){

        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(8);
        renderer.setColor(Color.BLUE);

        XYSeriesRenderer goalRenderer = new XYSeriesRenderer();
        goalRenderer.setLineWidth(2);
        goalRenderer.setColor(Color.GRAY);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.addSeriesRenderer(goalRenderer);

        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        mRenderer.setPanEnabled(panX, false);
        mRenderer.setYAxisMax(Math.round(Collections.max(data)) + 1);
        mRenderer.setYAxisMin(0);
        mRenderer.setXAxisMax(data.size()-1);
        mRenderer.setXAxisMin(data.size()-numberOfPoints-1);
        mRenderer.setLabelsTextSize(32f);
        mRenderer.setYLabels(0);
        //mRenderer.setXLabelsAngle(270f);
        mRenderer.setXLabels(0);
        for (int i = 0; i < data.size(); i++)
        {
            if (dataType == 0) {
                mRenderer.addXTextLabel(i, new DateFormatSymbols().getShortWeekdays()[(i + startDate) - (7 * ((int) (i + startDate) / 7))]);
            } else if (dataType == 1) {
                mRenderer.addXTextLabel(i, Integer.toString(i + startDate));
            } else if (dataType == 2) {
                mRenderer.addXTextLabel(i, new DateFormatSymbols().getShortMonths()[(i + startDate) - (12 * ((int) (i + startDate) / 12))]);
            }
        }
        mRenderer.setShowGrid(false);
        mRenderer.setShowLegend(false);

        return mRenderer;

    }

    public XYMultipleSeriesDataset getDataSet(){
        XYSeries series = new XYSeries("data");
        XYSeries goalSeries = new XYSeries("goal");

        for (int i = 0; i < data.size()-1; i ++){
            if (i == 0){
                double y0 = data.get(0);
                double y1 = data.get(1);
                double y2 = data.get(1);
                double x0 = 0;
                double x1 = 1-smoothness;
                double x2 = 1;
                goalSeries.add(x0, goal);
                for (double t = 0.0; t < 1.0; t+=step){
                    double y = (1-t)*(1-t)*y0 + 2*(1-t)*t*y1 + t*t*y2;
                    double x = (1-t)*(1-t)*x0 + 2*(1-t)*t*x1 + t*t*x2;
                    series.add(x, y);
                }
            } else if (i == data.size()-2){
                double y0 = data.get(data.size()-2);
                double y1 = data.get(data.size()-2);
                double y2 = data.get(data.size()-1);
                double x0 = data.size()-2;
                double x1 = data.size()-2+smoothness;
                double x2 = data.size()-1;
                goalSeries.add(x2, goal);
                for (double t = 0.0; t < 1.0; t+=step){
                    double y = (1-t)*(1-t)*y0 + 2*(1-t)*t*y1 + t*t*y2;
                    double x = (1-t)*(1-t)*x0 + 2*(1-t)*t*x1 + t*t*x2;
                    series.add(x, y);
                }
            } else {
                double y0 = data.get(i);
                double y1 = data.get(i);
                double y2 = data.get(i + 1);
                double y3 = data.get(i + 1);
                double x0 = i;
                double x1 = i + smoothness;
                double x2 = i + 1 - smoothness;
                double x3 = i + 1;
                for (double t = 0.0; t < 1.0; t+=step){
                    double x = (1-t)*(1-t)*(1-t)*x0 + 3*(1-t)*(1-t)*t*x1 + 3*(1-t)*t*t*x2 + t*t*t*x3;
                    double y = (1-t)*(1-t)*(1-t)*y0 + 3*(1-t)*(1-t)*t*y1 + 3*(1-t)*t*t*y2 + t*t*t*y3;
                    series.add(x, y);
                }
            }
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);
        dataset.addSeries(goalSeries);

        return dataset;
    }

}
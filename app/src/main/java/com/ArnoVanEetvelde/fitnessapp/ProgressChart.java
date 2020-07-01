package com.ArnoVanEetvelde.fitnessapp;

import android.graphics.Color;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.HashMap;

public class ProgressChart {

    private ArrayList<Double> data;
    private double step, smoothness;

    public ProgressChart(ArrayList<Double> data, double step, double smoothness){
        this.data = data;
        this.step = step;
        this.smoothness = smoothness;
    }

    public XYMultipleSeriesRenderer getChartView(){

        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(4);
        renderer.setColor(Color.BLUE);
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(5);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(false);
        return mRenderer;

    }

    public XYMultipleSeriesDataset getDataSet(){
        XYSeries series = new XYSeries("");

        for (int i = 0; i < data.size()-1; i ++){
            if (i == 0){
                double y0 = data.get(0);
                double y1 = data.get(1);
                double y2 = data.get(1);
                double x0 = 0;
                double x1 = 1-smoothness;
                double x2 = 1;
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

//        double t = 0.0;
//        while ((1-t)*(1-t)*(data.size()-2) + 2*(1-t)*t*(data.size()-2+smoothness) + t*t*data.size()-1 < data.size()-1){
//
//            if (t <= 1){
//                double y0 = data.get(0);
//                double y1 = data.get(1);
//                double y2 = data.get(1);
//                double x0 = 0;
//                double x1 = 1-smoothness;
//                double x2 = 1;
//                double y = (1-t)*(1-t)*y0 + 2*(1-t)*t*y1 + t*t*y2;
//                double x = (1-t)*(1-t)*x0 + 2*(1-t)*t*x1 + t*t*x2;
//                series.add(x, y);
//            } else if (t >= data.size()-2) {
//                double y0 = data.get(data.size()-2);
//                double y1 = data.get(data.size()-2);
//                double y2 = data.get(data.size()-1);
//                double x0 = data.size()-2;
//                double x1 = data.size()-2+smoothness;
//                double x2 = data.size()-1;
//                double y = (1-t)*(1-t)*y0 + 2*(1-t)*t*y1 + t*t*y2;
//                double x = (1-t)*(1-t)*x0 + 2*(1-t)*t*x1 + t*t*x2;
//                series.add(x, y);
//            } else {
//                double y0 =
//                double y1 =
//                double y2 =
//                double y3 =
//                double x0 =
//                double x1 =
//                double x2 =
//                double x3 =
//                double x = (1-t)*(1-t)*(1-t)*x0 + 3*(1-t)*(1-t)*t*x1 + 3*(1-t)*t*t*x2 + t*t*t*x3;
//                double y = (1-t)*(1-t)*(1-t)*y0 + 3*(1-t)*(1-t)*t*y1 + 3*(1-t)*t*t*y2 + t*t*t*y3;
//                series.add(x, y);
//            }
//
//            t += step;
//        }

//        for (Double dataPoint : data){
//            if (data.indexOf(dataPoint) == 0){
//                double y0 = dataPoint;
//                double y1 = data.get(1)-smoothness;
//                double y2 = data.get(1);
//                for (double i = 0.0; i < 1.0; i += step){
//                    series.add(i, (1-i)*(1-i)*y0 + 2*(1-i)*i*y1 + i*i*y2);
//                }
//            } else if (data.indexOf(dataPoint) == data.size()-2){
//                double y0 = dataPoint;
//                double y1 = dataPoint+smoothness;
//                double y2 = data.get(data.size()-1);
//                for (double i = data.size()-2; i < data.size()-1; i+=step){
//                    series.add(i, (1-i)*(1-i)*y0 + 2*(1-i)*i*y1 + i*i*y2);
//                }
//            } else if (data.indexOf(dataPoint) != data.size()-1){
//                double y0 = dataPoint;
//                double y1 = dataPoint+smoothness;
//                double y2 = data.get(data.indexOf(dataPoint)+1)-smoothness;
//                double y3 = data.get(data.indexOf(dataPoint)+1);
//                for (double i = data.indexOf(dataPoint); i < data.indexOf(dataPoint)+1; i+=step){
//                    series.add(i, (1-i)*(1-i)*(1-i)*y0 + 3*(1-i)*(1-i)*i*y1 + 3*(1-i)*i*i*y2 + i*i*i*y3);
//                }
//            }
//        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);
        return dataset;
    }

}

package com.ArnoVanEetvelde.fitnessapp;

import android.graphics.Color;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.HashMap;

public class ProgressChart {

    private HashMap<Integer, Integer> data;

    public ProgressChart(){
        //this.data = data;
    }

    public XYMultipleSeriesRenderer getChartView(){

        XYSeries series = new XYSeries("Test");
        series.add(0,0);
        series.add(1,1);
        series.add(2,2);
        series.add(3,1);
        series.add(4,0);
        series.add(5,4);
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.RED);
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
// Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(35);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true); // we show the grid

        return mRenderer;
    }

}

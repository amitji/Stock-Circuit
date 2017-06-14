package com.abile2.stockcircuit.charts;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.abile2.stockcircuit.R;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.Arrays;

/**
 * A simple XYPlot
 */
public class SimpleXYPlotActivity extends Activity {

    private XYPlot plot;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_xy_plot_example);

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);
        createCahrt();

    }
    private void createCahrt (){


        // create a couple arrays of y-values to plot:
//        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10, 13, 14};
//        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};
//        Number[] series2Numbers = {5, 2, 10, 5, 20, 10, 40, 20, 80, 55};

        //final String[] domainLabels = {"Q416","Q117","Q217","Q317","Q417" };
        final Number[] domainLabels = {0,3,6,9,12};
        Number[] series1Numbers = {1311.1, 1412.5, 1612.33, 1757.0, 1911.0};
        Number[] series2Numbers = {1311.1, 1211.2, 1288.0, 1344.5, 1433.3};

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Best Price");
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Worst Price");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        // Amit
        LineAndPointFormatter series1Format = new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);
        //LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.CYAN, Color.BLUE, null, null);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2);

        PointLabelFormatter plf = new PointLabelFormatter();
        plf.getTextPaint().setTextSize(PixelUtils.spToPix(20));
        plf.getTextPaint().setColor(this.getResources().getColor(R.color.yellow));
        series1Format.setPointLabelFormatter(plf);

        PointLabelFormatter plf2 = new PointLabelFormatter();
        plf2.getTextPaint().setTextSize(PixelUtils.spToPix(20));
        plf2.getTextPaint().setColor(this.getResources().getColor(R.color.blue_1));
        series2Format.setPointLabelFormatter(plf2);

//        series1Format.getLinePaint().setStrokeWidth(PixelUtils.dpToPix(3));
//        series1Format.getVertexPaint().setStrokeWidth(PixelUtils.dpToPix(6));

        // add an "dash" effect to the series2 line:
        //Amit
        /*
        series2Format.getLinePaint().setPathEffect(new DashPathEffect(new float[] {

                // always use DP when specifying pixel sizes, to keep things consistent across devices:
                PixelUtils.dpToPix(20),
                PixelUtils.dpToPix(15)}, 0));
*/
        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/

        /* Amit
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
*/
        // add a new series' to the xyplot:
        plot.setDomainBoundaries(0,12, BoundaryMode.GROW);
        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);

        //Amit Added below
//        plot.setBorderPaint(null);
//        plot.setPlotMargins(0, 0, 0, 0);
        //This gets rid of the gray grid
        plot.getGraph().setGridBackgroundPaint(new Paint(Color.TRANSPARENT));

//This gets rid of the black border (up to the graph) there is no black border around the labels
        //plot.getBackgroundPaint().setColor(Color.TRANSPARENT);

//This gets rid of the black behind the graph
        //plot.getGraph().getBackgroundPaint().setColor(Color.TRANSPARENT);

//With a new release of AndroidPlot you have to also set the border paint
        plot.getGraph().getDomainGridLinePaint().setColor(Color.TRANSPARENT);
        plot.getGraph().getRangeGridLinePaint().setColor(Color.TRANSPARENT);



/*
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
*/

    }
}
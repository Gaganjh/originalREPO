package com.manulife.pension.bd.web.fap;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.manulife.pension.bd.web.BdProperties;
import com.manulife.pension.platform.web.investment.ChartDataBean;

/**
 * Helper class for Performance Chart
 * 
 * @author Ramkumar
 *
 */
public class PerformanceChartUtil {

    /**
     * Normalize unit values.
     * @param unitValuesArr list of unit values
     * @return none
     */
    @SuppressWarnings("unchecked")
    public static void normalizeUnitValues( List unitValuesArr ){
        int i = 0;
        for( Iterator it = unitValuesArr.iterator(); it.hasNext(); i++ ){

            double[] values = (double[])(it.next());

            double multiplier = 0;

            for( int j=0; j<values.length; j++ ){

                if (multiplier == 0) {
                    //Added by Raghu
                    if ( values[j] != 0 )
                    {
                        multiplier = 1000 / values[j];
                    }
                }

                values[j] = multiplier * values[j];
            }
        }
    }


    /**
     * Formats a double value into a String representation with 2 decimal places, and optionally with
     * commas separating the thousands digits.
     *
     * @param value
     *     The double value to be formatted into a String.
     *
     * @param useCommas
     *     A boolean indicating whether or not to use commas to separate the thousands digits.
     *
     * @return
     *     A String containing the value of the double formatted to 2 decimal places.
     **/

    public static String formatDouble(double value, boolean useCommas) {
        NumberFormat doubleFormatter = NumberFormat.getInstance();
        doubleFormatter.setMinimumFractionDigits(2);
        doubleFormatter.setMaximumFractionDigits(2);
        doubleFormatter.setGroupingUsed(useCommas);
        return doubleFormatter.format(value);
    }


    /**
     * Calculates a value for the y-axis delta for the chart such that we have a "nice" number of
     * labels along the y-axis. By "nice" we mean that the labels are not too close together and
     * not too far apart.
     *
     * @param values
     *     A two-dimensional array of all of the values to be displayed on the chart.
     *
     * @param chartHeight
     *     The height of the chart in pixels.
     *
     * @return
     *     The integer value to be used as the y-axis delta for the chart.
     **/
    public static int calculateYAxisDelta(double[][] values, int chartHeight) {
        double maxValue = BdProperties.getChartMaxValueLowerBound();
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                if (values[i][j] > maxValue) maxValue = values[i][j];
            }
        }
        int maxLines = chartHeight / BdProperties.getChartMinYGridSpacingInPixels();
        int yAxisDelta = BdProperties.getChartYAxisDeltaStart();
        while (((maxValue + yAxisDelta - 1)/ yAxisDelta) > maxLines) yAxisDelta += BdProperties.getChartYAxisDeltaIncrement();
        return yAxisDelta;
    }
    
    /**
     * This method generates the URL parameters for the LineChartServlet
     * @param listEffectiveDates
     * @param unitValues
     * @param bean
     * @param height
     * @param width
     * @return
     *      The URL parameters String for the LineChartServlet
     */
    @SuppressWarnings("unchecked")
    public static String getChartImageUrlParams(
            List listEffectiveDates,
            double[][] unitValues,
            ChartDataBean bean, 
            int height, 
            int width
        ) {
        
        SimpleDateFormat labelDateFormatter = new SimpleDateFormat(BdProperties.getChartXLabelDateFormat());
        
        StringBuffer buff = new StringBuffer();     
        //  ?width=650
        //buff.append("?width=");
        buff.append("width=");
        buff.append(String.valueOf(width));

        //  &height=450
        buff.append("&height=");
        buff.append(String.valueOf(height));

        //  &imageType=GIF
        if(bean.getImageType()!=null) {
            buff.append("&imageType=");
            buff.append(bean.getImageType());
        }

        //  &options=255;255;255
        buff.append("&options=");
        buff.append(BdProperties.getChartBackgroundColor().replace(',',';'));

        //  &gridOptions=240;240;240;0;1500;1;100;$
        buff.append("&gridOptions=");
        buff.append(BdProperties.getChartGridColor().replace(',',';'));
        buff.append(";");
        buff.append(BdProperties.getChartMinValueUpperBound());
        buff.append(";");
        buff.append(BdProperties.getChartMaxValueLowerBound());
        buff.append(";");


        int numPoints = unitValues[0].length;


        int frequency = 1;
        int maxPoints = width / BdProperties.getChartMinXGridSpacingInPixels();
        while (((numPoints + frequency - 1)/ frequency) > maxPoints) frequency++;
        numPoints = numPoints / frequency;
        int xTickFrequency = 1;
        int maxTicks = width / BdProperties.getChartMinXLabelSpacingInPixels();
        while (((numPoints + xTickFrequency - 1)/ xTickFrequency) > maxTicks) xTickFrequency++;
        buff.append(xTickFrequency);
        buff.append(";");


        int yValueDelta = PerformanceChartUtil.calculateYAxisDelta(unitValues, height);


        buff.append(yValueDelta);
        buff.append(";$");

        //  &xLabels=0;0;0;1;1;May-02;Jun-02;Jul-02;Aug-02;Sep-02;Oct-02;Nov-02;Dec-02;Jan-03;Feb-03;Mar-03
        buff.append("&xLabels=");
        buff.append(BdProperties.getChartXLabelColor().replace(',',';'));
        buff.append(";");


        Date[] effectiveDates = (Date[])listEffectiveDates.toArray(new Date[0]);


        int startingPoint = (effectiveDates.length - 1) % frequency;
        if (startingPoint > 0) {
            buff.append(((double)startingPoint)/((double)frequency));
            buff.append(";1;");
            buff.append(labelDateFormatter.format(effectiveDates[0]));
        } else {
            buff.append("1;1");
        }
        for (int i = startingPoint; i < effectiveDates.length; i += frequency) {
            buff.append(";");
            buff.append(labelDateFormatter.format(effectiveDates[i]));
        }
        
        // each line set of values
        for (int i = 0; i < unitValues.length; i++) {
            buff.append("&line");
            buff.append(i + 1);
            buff.append("=");
            buff.append(BdProperties.getChartLineColor(i + 1, false).replace(',',';'));
            if (startingPoint > 0) {
                buff.append(";");
                buff.append(PerformanceChartUtil.formatDouble(unitValues[i][0], false));
            }
            for (int j = startingPoint; j < unitValues[i].length; j += frequency) {
                buff.append(";");
                buff.append(PerformanceChartUtil.formatDouble(unitValues[i][j], false));
            }
        }         

        return buff.toString();
    }
}

package com.manulife.pension.ps.web;

/*
  File: PsProperties.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-01-01   Chris Shin       Initial version.
  CS1.1		2004-04-21	 Chris Shin		  use getResourceAsStream to obtain the properties from ps.properties
*/
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;

import com.manulife.pension.exception.SystemException;


/**
 * This "static" utility class provides access to all of the application properties
 * in the ps.properties file. (Copied from ezk)
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 * @see com.manulife.pension.ezk.web.ezkProperties.java
 **/

public class PsProperties {

	public static final String APPNAME = "PS";

	private static final int UNINITIALIZED_INT_VALUE = -7439281;

	private static final String PROPERTY_CHART_MAX_FUNDS = "MF.ChartMaxFunds";
	private static final String PROPERTY_CHART_MIN_MONTHS = "MF.ChartMinMonths";
	private static final String PROPERTY_CHART_BACKGROUND_COLOR = "MF.ChartBackgroundColor";
	private static final String PROPERTY_CHART_GRID_COLOR = "MF.ChartGridColor";
	private static final String PROPERTY_CHART_X_LABEL_COLOR = "MF.ChartXLabelColor";
	private static final String PROPERTY_CHART_LINE_COLOR_TEMPLATE = "MF.ChartLineColor";
	private static final String PROPERTY_MOCK_PORTFOLIO_FUND_ID = "MF.MockPortfolioFundID";
	private static final String PROPERTY_MOCK_PORTFOLIO_FUND_NAME = "MF.MockPortfolioFundName";
	private static final String PROPERTY_CHART_MIN_VALUE_UPPER_BOUND = "MF.ChartMinValueUpperBound";
	private static final String PROPERTY_CHART_MAX_VALUE_LOWER_BOUND = "MF.ChartMaxValueLowerBound";
	private static final String PROPERTY_CHART_MIN_Y_GRID_SPACING_IN_PIXELS = "MF.ChartMinYGridSpacingInPixels";
	private static final String PROPERTY_CHART_MIN_X_GRID_SPACING_IN_PIXELS = "MF.ChartMinXGridSpacingInPixels";
	private static final String PROPERTY_CHART_MIN_X_LABEL_SPACING_IN_PIXELS = "MF.ChartMinXLabelSpacingInPixels";
	private static final String PROPERTY_CHART_Y_AXIS_DELTA_START = "MF.ChartYAxisDeltaStart";
	private static final String PROPERTY_CHART_Y_AXIS_DELTA_INCREMENT = "MF.ChartYAxisDeltaIncrement";
	private static final String PROPERTY_CHART_RESULTS_PAGE_TITLE_TEMPLATE = "MF.ChartResultsPageTitleTemplate";
	private static final String PROPERTY_CHART_TITLE_DATE_FORMAT = "MF.ChartTitleDateFormat";
	private static final String PROPERTY_CHART_X_LABEL_DATE_FORMAT = "MF.ChartXLabelDateFormat";
	private static final String PROPERTY_SORT_LINK_COLOR = "MF.ColumnSortLinkColor";
	private static final String PROPERTY_NY_STOCK_CLOSURE_TIME_LIMIT = "MF.account.ny_stock_closure_time_limit";
	private static final String PROPERTY_NY_STOCK_CLOSURE_AFTER_TIME_LIMIT = "MF.account.ny_stock_closure_after_time_limit";
	private static final String PROPERTY_ILOANS_LISTENER_PORT_NUMBER = "MF.iloans.listener_port_number";
	private static final String PROPERTY_TEST_URL_PREFIX = "TestURLPrefix";
	
	private static final int DEFAULT_CHART_MAX_FUNDS = 6;
	private static final int DEFAULT_CHART_MIN_MONTHS = 6;
	private static final String DEFAULT_CHART_BACKGROUND_COLOR = "255,255,255";
	private static final String DEFAULT_CHART_GRID_COLOR = "240,240,240";
	private static final String DEFAULT_CHART_X_LABEL_COLOR = "0,0,0";
	private static final String DEFAULT_MOCK_PORTFOLIO_FUND_ID = "Portfolio";
	private static final String DEFAULT_MOCK_PORTFOLIO_FUND_NAME = "Your Hypothetical Portfolio";
	private static final int DEFAULT_CHART_MIN_VALUE_UPPER_BOUND = 0;
	private static final int DEFAULT_CHART_MAX_VALUE_LOWER_BOUND = 1500;
	private static final int DEFAULT_CHART_MIN_Y_GRID_SPACING_IN_PIXELS = 18;
	private static final int DEFAULT_CHART_MIN_X_GRID_SPACING_IN_PIXELS = 10;
	private static final int DEFAULT_CHART_MIN_X_LABEL_SPACING_IN_PIXELS = 50;
	private static final int DEFAULT_CHART_Y_AXIS_DELTA_START = 100;
	private static final int DEFAULT_CHART_Y_AXIS_DELTA_INCREMENT = 50;
	private static final String DEFAULT_CHART_RESULTS_PAGE_TITLE_TEMPLATE = "Fund Performance (% &nbsp;-&nbsp; %)";
	private static final String DEFAULT_CHART_TITLE_DATE_FORMAT = "MMMMMMMMM d, yyyy";
	private static final String DEFAULT_CHART_X_LABEL_DATE_FORMAT = "MMM-yy";
	private static final String DEFAULT_SORT_LINK_COLOR = "#0000FF";	// blue
	private static final int DEFAULT_NY_STOCK_CLOSURE_TIME_LIMIT = 30;
	private static final int DEFAULT_NY_STOCK_CLOSURE_AFTER_TIME_LIMIT = 15;
	private static final int DEFAULT_ILOANS_LISTENER_PORT_NUMBER = 30011;	
	private static final String DEFAULT_TEST_URL_PREFIX = "http://localhost";
	
	private static PsProperties theInstance;
	private static int chartMaxFunds = UNINITIALIZED_INT_VALUE;
	private static int chartMinMonths = UNINITIALIZED_INT_VALUE;
	private static String chartBackgroundColor = null;
	private static String chartGridColor = null;
	private static String chartXLabelColor = null;
	private static String[] chartLineColors = null;
	private static String[] chartLineColorsInHex = null;
	private static String mockPortfolioFundID = null;
	private static String mockPortfolioFundName = null;
	private static int chartMinValueUpperBound = UNINITIALIZED_INT_VALUE;
	private static int chartMaxValueLowerBound = UNINITIALIZED_INT_VALUE;
	private static int chartMinYGridSpacingInPixels = UNINITIALIZED_INT_VALUE;
	private static int chartMinXGridSpacingInPixels = UNINITIALIZED_INT_VALUE;
	private static int chartMinXLabelSpacingInPixels = UNINITIALIZED_INT_VALUE;
	private static int chartYAxisDeltaStart = UNINITIALIZED_INT_VALUE;
	private static int chartYAxisDeltaIncrement = UNINITIALIZED_INT_VALUE;
	private static int nyStockClosureTimeLimit = UNINITIALIZED_INT_VALUE;
	private static int nyStockClosureAfterTimeLimit = UNINITIALIZED_INT_VALUE;
	private static String chartResultsPageTitleTemplate = null;
	private static String chartTitleDateFormat = null;
	private static String chartXLabelDateFormat = null;
	private static String columnSortLinkColor = null;
	private static int iloansListenerPortNumber = UNINITIALIZED_INT_VALUE;
	private static String testURLPrefix;

	private static final String PROPERTIES_FILE_NAME= "/ps.properties";
	
	/** runtime properties */
	private Properties runtimeProperties;
		
	/**
	 * Constructs a PsProperties object.
	 * Since we are implementing this as a Singleton, the constructor is private.
	 **/
	private PsProperties() throws SystemException {
            // read properties
            runtimeProperties = new Properties();
            InputStream in = null;
            try {
				 in = PsProperties.class.getResourceAsStream(PROPERTIES_FILE_NAME);
				runtimeProperties.load(in);
            } catch (FileNotFoundException ex) {
                throw new SystemException(ex, "com.manulife.pension.ps.web.PsProperties", "PsProperties", "Exception when trying to open properties file: ");
            } catch (IOException e) {
               	throw new SystemException(e,"com.manulife.pension.ps.web.PsProperties", "PsProperties", "Exception when trying to open properties file: " + e.toString() );
            }finally{
            	try {
            		if(in != null)
            			in.close();
				} catch (IOException e) {
					throw new SystemException(e,"com.manulife.pension.ps.web.PsProperties", "PsProperties", "Exception when trying to open properties file: " + e.toString() );
				}
            }
	}
	/**
	 * Takes a string of the form 'R,G,B' where each of the R, G or B is between 0 and 255 and
	 * converts it into a string of the form rrggbb where rr is the hex representation of R etc.
	 *
	 * @param colorTriad
	 *     A string of the form 'R,G,B' that is to be converted.
	 *
	 * @return
	 *     A string of the form rrggbb where each character is a hex digit.
	 **/
	
	
	private static String convertColorTriadToHex(String colorTriad) {
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		StringTokenizer tokenizer = new StringTokenizer(colorTriad, ",");
		StringBuffer buf = new StringBuffer();
		while (tokenizer.hasMoreElements()) {
			int value = Integer.parseInt((String)tokenizer.nextElement());
			buf.append(hexDigits[value / 16]);
			buf.append(hexDigits[value % 16]);
		}
		return buf.toString();
	}

	/**
	 * Return a string of the form 'i,j,k' representing the RGB values of the chart background color
	 * specified in the properties file.  If not specified, we return a suitable default.
	 *
	 * @return
	 *     A string of the form 'i,j,k' representing the background RGB color values.
	 **/
	
	
	public static String getChartBackgroundColor() {
		if (chartBackgroundColor == null) {
			chartBackgroundColor = readProperty(PROPERTY_CHART_BACKGROUND_COLOR, DEFAULT_CHART_BACKGROUND_COLOR);
		}
		return chartBackgroundColor;
	}
	/**
	 * Return a string of the form 'i,j,k' representing the RGB values of the chart gridline color
	 * specified in the properties file.  If not specified, we return a suitable default.
	 *
	 * @return
	 *     A string of the form 'i,j,k' representing the gridline RGB color values.
	 **/
	
	
	public static String getChartGridColor() {
		if (chartGridColor == null) {
			chartGridColor = readProperty(PROPERTY_CHART_GRID_COLOR, DEFAULT_CHART_GRID_COLOR);
		}
		return chartGridColor;
	}
	/**
	 * Return a string representing the color of the specified chart line taken from the properties file.
	 * If the value is not specified in the properties file, we return a suitable default.  The string
	 * is either in the form of a color 'R,G,B' triad, or of the form 'xxyyzz' if requested in hex format.
	 *
	 * @return
	 *     If the <code>hex</code> parameter is false, we return a string of the form 'i,j,k' representing
	 *     the RGB color values for the requested line.
	 *     <br>If the <code>hex</code> parameter is true, we return a string of the form 'xxyyzz'.
	 *
	 * @exception IllegalArgumentException
	 *     If the lineNumber requested is larger than the maximum allowed.
	 **/
	
	
	public static String getChartLineColor(int lineNumber, boolean hex) throws IllegalArgumentException {
	
	
		int maxLines = getMaxFunds() + 1;  // we need 1 extra because of the mock portfolio line
	
	
		if (lineNumber > maxLines) throw new IllegalArgumentException();
	
	
		if (chartLineColors == null) {
			chartLineColors = new String[maxLines];
			chartLineColorsInHex = new String[maxLines];
			for (int i = 1; i <= maxLines; i++) {
				String colorTriad = readProperty(PROPERTY_CHART_LINE_COLOR_TEMPLATE + i, getDefaultLineColor(i));
				chartLineColors[i-1] = colorTriad;
				chartLineColorsInHex[i-1] = convertColorTriadToHex(colorTriad);
			}
		}
	
	
		if (hex) {
			return chartLineColorsInHex[lineNumber - 1];
		} else {
			return chartLineColors[lineNumber - 1];
		}
	}
	/**
	 * Return the integer value of the ChartMaxValueLowerBound property from the properties file, or a
	 * suitable default if it's not specified in the file. This value is used to set the default upper
	 * value displayed along the y axis of the chart. The chart is automatically smart enough to make
	 * this value higher if any of the values to be charted are more than this value.
	 *
	 * @return
	 *     An integer value corresponding to the default highest value on the chart.
	 **/
	
	
	public static int getChartMaxValueLowerBound() {
		if (chartMaxValueLowerBound == UNINITIALIZED_INT_VALUE) {
			chartMaxValueLowerBound = readIntProperty(PROPERTY_CHART_MAX_VALUE_LOWER_BOUND, DEFAULT_CHART_MAX_VALUE_LOWER_BOUND);
		}
		return chartMaxValueLowerBound;
	}
	/**
	 * Return the integer value of the ChartMinMonths property from the properties file, or a
	 * suitable default if it's not specified in the file. This value specifies the minimum
	 * number of months worth of data that we are willing to plot. If the requested date range
	 * spans less than this many months, we will reject the input with an error message.
	 *
	 * @return
	 *     An integer value corresponding to the minimum number of months allowed for the chart.
	 **/
	
	
	public static int getChartMinMonths() {
		if (chartMinMonths == UNINITIALIZED_INT_VALUE) {
			chartMinMonths = readIntProperty(PROPERTY_CHART_MIN_MONTHS, DEFAULT_CHART_MIN_MONTHS);
		}
		return chartMinMonths;
	}
	/**
	 * Return the integer value of the ChartMinValueUpperBound property from the properties file, or a
	 * suitable default if it's not specified in the file. This value is used to set the default lower
	 * value displayed along the y axis of the chart. The chart is automatically smart enough to make
	 * this value lower if any of the values to be charted are less than this value.
	 *
	 * @return
	 *     An integer value corresponding to the default lowest value on the chart.
	 **/
	
	
	public static int getChartMinValueUpperBound() {
		if (chartMinValueUpperBound == UNINITIALIZED_INT_VALUE) {
			chartMinValueUpperBound = readIntProperty(PROPERTY_CHART_MIN_VALUE_UPPER_BOUND, DEFAULT_CHART_MIN_VALUE_UPPER_BOUND);
		}
		return chartMinValueUpperBound;
	}


	/**
	 * Return the integer value of the NyStockClosureTimeLimit property from the properties file, or a
	 * suitable default if it's not specified in the file. 
	 *
	 * @return
	 *     An integer value corresponding to time period to NY stock closure time.
	 **/
	
	
	public static int getNYStockClosureTimeLimit() {
		if (nyStockClosureTimeLimit == UNINITIALIZED_INT_VALUE) {
			nyStockClosureTimeLimit = readIntProperty(PROPERTY_NY_STOCK_CLOSURE_TIME_LIMIT, DEFAULT_NY_STOCK_CLOSURE_TIME_LIMIT);
		}
		return nyStockClosureTimeLimit;
	}
	
	/**
	 * Return the integer value of the NyStockClosureAfterTimeLimit property from the properties file, or a
	 * suitable default if it's not specified in the file. 
	 *
	 * @return
	 *     An integer value corresponding to time period to NY stock closure time.
	 **/
	
	
	public static int getNYStockClosureAfterTimeLimit() {
		if (nyStockClosureAfterTimeLimit == UNINITIALIZED_INT_VALUE) {
			nyStockClosureAfterTimeLimit = readIntProperty(PROPERTY_NY_STOCK_CLOSURE_AFTER_TIME_LIMIT, DEFAULT_NY_STOCK_CLOSURE_AFTER_TIME_LIMIT);
		}
		return nyStockClosureAfterTimeLimit;
	}
	/**
	 * Return the integer value of the ChartMinXGridSpacingInPixels property from the properties file, or a
	 * suitable default if it's not specified in the file. This value is used to specify the minimum spacing
	 * required between grid lines on the x axis of the chart.
	 *
	 * @return
	 *     An integer value corresponding to the minimum spacing between grid lines on the x axis.
	 **/
	
	
	public static int getChartMinXGridSpacingInPixels() {
		if (chartMinXGridSpacingInPixels == UNINITIALIZED_INT_VALUE) {
			chartMinXGridSpacingInPixels = readIntProperty(PROPERTY_CHART_MIN_X_GRID_SPACING_IN_PIXELS, DEFAULT_CHART_MIN_X_GRID_SPACING_IN_PIXELS);
		}
		return chartMinXGridSpacingInPixels;
	}
	/**
	 * Return the integer value of the ChartMinXLabelSpacingInPixels property from the properties file, or a
	 * suitable default if it's not specified in the file. This value is used to specify the minimum spacing
	 * required between labels on the x axis of the chart.
	 *
	 * @return
	 *     An integer value corresponding to the minimum spacing between lables on the x axis.
	 **/
	
	
	public static int getChartMinXLabelSpacingInPixels() {
		if (chartMinXLabelSpacingInPixels == UNINITIALIZED_INT_VALUE) {
			chartMinXLabelSpacingInPixels = readIntProperty(PROPERTY_CHART_MIN_X_LABEL_SPACING_IN_PIXELS, DEFAULT_CHART_MIN_X_LABEL_SPACING_IN_PIXELS);
		}
		return chartMinXLabelSpacingInPixels;
	}
	/**
	 * Return the integer value of the ChartMinYGridSpacingInPixels property from the properties file, or a
	 * suitable default if it's not specified in the file. This value is used to specify the minimum spacing
	 * required between grid lines on the y axis of the chart.
	 *
	 * @return
	 *     An integer value corresponding to the minimum spacing between grid lines on the y axis.
	 **/
	
	
	public static int getChartMinYGridSpacingInPixels() {
		if (chartMinYGridSpacingInPixels == UNINITIALIZED_INT_VALUE) {
			chartMinYGridSpacingInPixels = readIntProperty(PROPERTY_CHART_MIN_Y_GRID_SPACING_IN_PIXELS, DEFAULT_CHART_MIN_Y_GRID_SPACING_IN_PIXELS);
		}
		return chartMinYGridSpacingInPixels;
	}
	/**
	 * Returns the string value of the ChartResultsPageTitleTemplate property from the properties file, or a
	 * suitable default if it's not specified. The string is used to build the title for the Chart
	 * Results page. The first '%' in the string will be replaced with the starting date for the
	 * chart and the second '%' in the string will be replaced with the ending date for the chart.
	 *
	 * @return
	 *     The string to be used as the template for the title of the Chart Results page.
	 **/
	
	
	public static String getChartResultsPageTitleTemplate() {
		if (chartResultsPageTitleTemplate == null) {
			chartResultsPageTitleTemplate = readProperty(PROPERTY_CHART_RESULTS_PAGE_TITLE_TEMPLATE, DEFAULT_CHART_RESULTS_PAGE_TITLE_TEMPLATE);
		}
		return chartResultsPageTitleTemplate;
	}
	/**
	 * Returns the string value of the ChartTitleDateFormat property from the properties file, or a
	 * suitable default if it's not specified. The string is used as a format string to the
	 * SimpleDateFormatter class to tell it how to format the dates that show up in the Chart
	 * Results page title.
	 *
	 * @return
	 *     The string to be used as the SimpleDateFormatter format string for dates in the title of the Chart Results page.
	 **/
	
	
	public static String getChartTitleDateFormat() {
		if (chartTitleDateFormat == null) {
			chartTitleDateFormat = readProperty(PROPERTY_CHART_TITLE_DATE_FORMAT, DEFAULT_CHART_TITLE_DATE_FORMAT);
		}
		return chartTitleDateFormat;
	}
	/**
	 * Return a string of the form 'i,j,k' representing the RGB values of the chart xLabel color
	 * specified in the properties file.  If not specified, we return a suitable default.
	 *
	 * @return
	 *     A string of the form 'i,j,k' representing the RGB color values of the labels for the x-axis.
	 **/
	
	
	public static String getChartXLabelColor() {
		if (chartXLabelColor == null) {
			chartXLabelColor = readProperty(PROPERTY_CHART_X_LABEL_COLOR, DEFAULT_CHART_X_LABEL_COLOR);
		}
		return chartXLabelColor;
	}
	/**
	 * Returns the string value of the ChartXLabelDateFormat property from the properties file, or a
	 * suitable default if it's not specified. The string is used as a format string to the
	 * SimpleDateFormatter class to tell it how to format the dates that show up as labels on the
	 * x axis of the chart.
	 *
	 * @return
	 *     The string to be used as the SimpleDateFormatter format string for dates along the x axis.
	 **/
	
	
	public static String getChartXLabelDateFormat() {
		if (chartXLabelDateFormat == null) {
			chartXLabelDateFormat = readProperty(PROPERTY_CHART_X_LABEL_DATE_FORMAT, DEFAULT_CHART_X_LABEL_DATE_FORMAT);
		}
		return chartXLabelDateFormat;
	}
	/**
	 * Return the integer value of the ChartYAxisDeltaIncrement property from the properties file, or a
	 * suitable default if it's not specified in the file. This value, along with the ChartYAxisDeltaStart,
	 * are used to find the lowest y axis value delta so that labels along the y axis are no closer than
	 * the ChartYMinGridSpacing value.
	 *
	 * @return
	 *     An integer value corresponding to the value increment for estimating the y axis delta values.
	 **/
	
	
	public static int getChartYAxisDeltaIncrement() {
		if (chartYAxisDeltaIncrement == UNINITIALIZED_INT_VALUE) {
			chartYAxisDeltaIncrement = readIntProperty(PROPERTY_CHART_Y_AXIS_DELTA_INCREMENT, DEFAULT_CHART_Y_AXIS_DELTA_INCREMENT);
		}
		return chartYAxisDeltaIncrement;
	}
	/**
	 * Return the integer value of the ChartYAxisDeltaStart property from the properties file, or a
	 * suitable default if it's not specified in the file. This value, along with the ChartYAxisDeltaIncrement,
	 * are used to find the lowest y axis value delta so that labels along the y axis are no closer than
	 * the ChartYMinGridSpacing value.
	 *
	 * @return
	 *     An integer value corresponding to the starting value for estimating the y axis delta values.
	 **/
	
	
	public static int getChartYAxisDeltaStart() {
		if (chartYAxisDeltaStart == UNINITIALIZED_INT_VALUE) {
			chartYAxisDeltaStart = readIntProperty(PROPERTY_CHART_Y_AXIS_DELTA_START, DEFAULT_CHART_Y_AXIS_DELTA_START);
		}
		return chartYAxisDeltaStart;
	}
	/**
	 * Return a string of the form 'i,j,k' representing the RGB values of the chart gridline color
	 * specified in the properties file.  If not specified, we return a suitable default.
	 *
	 * @return
	 *     A string of the form 'i,j,k' representing the gridline RGB color values.
	 **/
	
	
	public static String getColumnSortLinkColor() {
		if (columnSortLinkColor == null) {
			columnSortLinkColor = readProperty(PROPERTY_SORT_LINK_COLOR, DEFAULT_SORT_LINK_COLOR);
		}
		return columnSortLinkColor;
	}
	/**
	 * Return a String in the form 'R,G,B' representing the default color of the specified line.
	 * Currently we only support a maximum of 22 lines.
	 **/
	
	
	private static String getDefaultLineColor(int lineNumber) throws IllegalArgumentException {
		String[] colors = {"204,0,0", "0,204,0", "0,0,204", "255,204,0", "255,0,255", "0,204,204",
			"153,153,153", "0,0,0", "153,102,0", "255,0,0", "0,255,0", "0,0,255", "255,255,0", "153,0,204",
			"0,255,255", "255,153,153", "153,255,153", "153,153,255", "255,255,153", "255,153,255",
			"153,255,255", "204,204,204"};
	
	
		if (lineNumber > colors.length) throw new IllegalArgumentException();
	
	
		return colors[lineNumber - 1];
	}
	/**
	 * Return the singleton instance.
	 **/
	
	
	public static PsProperties getInstance() throws SystemException {

		if (theInstance == null) {
			theInstance = new PsProperties();
		}
		return theInstance;
	}
	/**
	 * Return the value of the ChartMaxFunds property which specifies how many dropdown boxes
	 * appear on the performance chart page to allow selection of funds to chart.
	 *
	 * @return
	 *     An integer value corresponding to the number of fund selection dropdown boxes.
	 **/
	
	
	public static int getMaxFunds() {
		if (chartMaxFunds == UNINITIALIZED_INT_VALUE) {
			chartMaxFunds = readIntProperty(PROPERTY_CHART_MAX_FUNDS, DEFAULT_CHART_MAX_FUNDS);
		}
		return chartMaxFunds;
	}
	/**
	 * Returns the string value of the MockPortfolioFundID property from the properties file, or a
	 * suitable default if it's not specified. The string is to be used as the fund investment ID
	 * for the mock portfolio on the performance chart. This string is never seen by the user, but
	 * should uniquely identify the mock portfolio (hence it must not be equal to any real
	 * fund_investmentid for any of our funds).
	 *
	 * @return
	 *     The string to be used as the mock portfolio investmentid.
	 **/
	
	
	public static String getMockPortfolioFundID() {
		if (mockPortfolioFundID == null) {
			mockPortfolioFundID = readProperty(PROPERTY_MOCK_PORTFOLIO_FUND_ID, DEFAULT_MOCK_PORTFOLIO_FUND_ID);
		}
		return mockPortfolioFundID;
	}
	/**
	 * Returns the string value of the MockPortfolioFundName property from the properties file, or a
	 * suitable default if it's not specified. The string is shown in the table on the charting results
	 * page to identify the line used for the mock portfolio on the chart.
	 *
	 * @return
	 *     The string to be shown to the user to identify their mock portfolio.
	 **/
	
	
	public static String getMockPortfolioFundName() {
		if (mockPortfolioFundName == null) {
			mockPortfolioFundName = readProperty(PROPERTY_MOCK_PORTFOLIO_FUND_NAME, DEFAULT_MOCK_PORTFOLIO_FUND_NAME);
		}
		return mockPortfolioFundName;
	}
	
	/**
	 * Read the value of the specified property from the Ezk property file
	 * and interpret it as an Integer value.
	 *
	 * @param name
	 *     The name of the property to read from the properties file.
	 *
	 * @param defaultValue
	 *     The value to be used if the property can't be successfully read from the
	 *     file.
	 *
	 * @return
	 *     A int representing the value of the specified property.
	 *     If the property can't be read from PUBRuntimeParms.properties, or doesn't
	 *     represent a valid int, then the defaultValue parameter is returned.
	 **/
	private static int readIntProperty(String name, int defaultValue) {
		int result = defaultValue;
		String s = readProperty(name, null);
		if (s != null) {
			try {
				result = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				// just return the default value
			}
		}
		return result;
	}
	/**
	 * Read the value of the specified property from the Ezk property file.
	 * If the property can't be read, return the given defaultValue.
	 *
	 * @param name
	 *     The name of the property to read from the properties file.
	 *
	 * @param defaultValue
	 *     The value to assign to the property if it can't be successfully read from
	 *     the properties file.
	 *
	 * @return
	 *     A String representation of the value of the specified property, or the
	 *     specified defaultValue if the property can't be read from Ezk.properties.
	 **/
	
	
	private static String readProperty(String name, String defaultValue) {
		String property = null;
		try {
			property = getInstance().getProperty(name);
		} catch (SystemException e) {
			// just ignore the exception and return null
		}
		if (property == null) property = defaultValue;
		return property;
	}

	/**
	 * Provides the normal getProperty() interface
	 * by delegating to the internal Properties object
	 * NOTE: null will be returned if the key cannot be found.
	 *
	 * @param	java.lang.String - the property name to lookup
	 * @return	java.lang.String - value of the requested property
	 **/
	public String getProperty(String key) {
		return runtimeProperties.getProperty(key);
	}

	/**
	 * Return the integer value of the IloansListenerPortNumber property from the properties file, or a
	 * suitable default if it's not specified in the file. This value is used to establish a port
	 * number that is open on the firewall between the tpa web server and the ezk appserver.
	 * @return
	 *     An integer value corresponding to port Number
	 **/
	
	
	public static int getIloansListenerPortNumber() {
		if (iloansListenerPortNumber == UNINITIALIZED_INT_VALUE) {
			iloansListenerPortNumber = readIntProperty(PROPERTY_ILOANS_LISTENER_PORT_NUMBER, DEFAULT_ILOANS_LISTENER_PORT_NUMBER);
		}
		return iloansListenerPortNumber;
	}

	/**
	 * @return Returns the testURLPrefix.
	 */
	public static String getTestURLPrefix() {
		if (testURLPrefix == null) {
			testURLPrefix = readProperty(PROPERTY_TEST_URL_PREFIX, DEFAULT_TEST_URL_PREFIX);
		}
		return testURLPrefix;
	}
}

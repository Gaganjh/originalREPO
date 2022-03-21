package com.manulife.pension.bd.web.taglib.investment;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BdProperties;
import com.manulife.pension.bd.web.fap.BDPerformanceChartInputForm;
import com.manulife.pension.bd.web.fap.PerformanceChartUtil;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.platform.web.investment.ChartDataBean;
import com.manulife.pension.util.content.helper.ContentUtility;

/**
 * FundPerformanceChart provides html for LineChart applet.
 *
 * @author Anton Grouza
 */
public class FundPerformanceChart extends BodyTagSupport {
	/** formatter for the labels */
	// moved to the methods due to thread access issues
	// private SimpleDateFormat labelDateFormatter = new SimpleDateFormat(BdProperties.getChartXLabelDateFormat());

	/** formatter for the title */
	// removed -- not used
	//private SimpleDateFormat titleDateFormatter = new SimpleDateFormat(BdProperties.getChartTitleDateFormat());

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** formatter for doubles */
	private NumberFormat doubleFormatter = null;

	/** default logger */
	private static Logger logger = Logger.getLogger(FundPerformanceChart.class);

	private String beanName = null;

    private String alt=null;
    
    private String mode=null;

    private String imageType=null;
    
	private int height=450;
	
	private int width=650;

	/**
	 * Called after processing of body content is complete.
  	 *	We use it to output the content we built up during processing
 	 *	of the body content.
	 *
     * @return EVAL_PAGE
     */
    @SuppressWarnings("unchecked")
	public final int doEndTag() throws JspException {
        // BodyContent body = getBodyContent();
        // String bodyText = body.getString();
        HttpSession session = pageContext.getSession();

       BdProperties.getChartGridColor();
		ChartDataBean bean = (ChartDataBean)session.getAttribute(BDConstants.CHART_DATA_BEAN);
		if ( bean == null ){
			return EVAL_PAGE;
		}

		List unitValuesArr = bean.getUnitValues();
		List effectiveDatesArr = bean.getEffectiveDates();
		String[] fundIds = bean.getFundIds();
		List fundNames = bean.getFundNames();

		if ( fundIds == null || fundNames == null || unitValuesArr == null || effectiveDatesArr == null ||
		     ( fundIds.length != fundNames.size() && fundIds.length != fundNames.size() - 1 ) ||
		     ( fundIds.length != unitValuesArr.size() && fundIds.length != unitValuesArr.size() - 1 ) ){
		    throw new JspException("Length of data arrays/lists not compatible.");
		}

		try {

    		List pct = bean.getPctValues();
    		boolean pctExists = false;

    		if ( pct != null && pct.size() > 0 ){
    			pctExists = true;
    		}

			// ... normalizing unit values
			PerformanceChartUtil.normalizeUnitValues( unitValuesArr );

	    	// add mock portfolio if needed
	    	double[] mockValues = null;
	    	int numValues = effectiveDatesArr.size();

	    	if ( pctExists ){
		    	double totalPct = 0;
		    	for( Iterator it=pct.iterator(); it.hasNext(); ){
		    		totalPct += ((Double)it.next()).doubleValue();

		    	}

		    	Double[] pctValues = (Double[])pct.toArray(new Double[0]);

		    	if ( totalPct == 100 ){
		    		mockValues = new double[numValues];
		    		for( int i=0; i<numValues; i++ ){
		    			mockValues[i] = 0;
		    			int j = 0;
			    		for( Iterator it=unitValuesArr.iterator(); it.hasNext(); j++ ){
			    			double[] values = (double[])it.next();
			    			mockValues[i] += values[i] * pctValues[j].doubleValue() / 100.0;
			    		}
		    		}
		    	}else{
		    		pctExists = false;
		    	}
	    	}

	  		int numFunds = (pctExists)?unitValuesArr.size()+1:unitValuesArr.size();
			double[][] unitValues = new double[ numFunds ][];
			int i = 0;
			for( Iterator it=unitValuesArr.iterator(); it.hasNext(); i++ ){
				unitValues[i] = (double[])it.next();
			}

			// add mock portfolio
			if ( pctExists ){
				unitValues[numFunds-1] = mockValues;
				if ( fundNames.size() < numFunds )
					fundNames.add( BdProperties.getMockPortfolioFundName() );
			}

			JspWriter out = pageContext.getOut();// body.getEnclosingWriter();
			String s = getLineChartHTML(effectiveDatesArr, unitValues, bean,session);
			out.println( s );
			s = getTableHTML( bean.getFundFootnotesByFund(), fundNames, unitValues );
			out.println( s );

		} catch (IOException e){
			if (logger.isDebugEnabled()) {
				logger.debug( "Exception:", e );
			}
		    throw new JspException("Error: " + e.toString());
		}

		session.setAttribute("symbolsArray", bean.getFundFootnotes() );
		return EVAL_PAGE;
    }

	/**
	 * Returns a String containing the HTML required to display the fund performance
	 * legend and report.
	 *
	 * @return
	 *     A String containing the HTML to display the fund performance report.
	 **/
    @SuppressWarnings("unchecked")
	public String getTableHTML(List fundFootnotes, List fundNames, double[][] unitValues) {
		StringBuffer buff = new StringBuffer(4000);
		
		buff.append("<div class=\"report_table\"> ");
		buff.append("<table class=\"report_table_content\"> ");
		buff.append("	<thead> ");
		buff.append("	<tr> ");
		buff.append("		<th colspan=\"2\" align=\"left\" valign=\"bottom\" class=\"val_str\"> ");
		buff.append("			<a href=\"#\" >Fund Name</a> </th> ");
		buff.append("		<th width=\"121\" valign=\"bottom\" class=\"val_str\"> ");
		buff.append("			<a href=\"#\">Initial Investment</a></th> ");
		buff.append("		<th width=\"151\" align=\"left\" valign=\"bottom\" class=\"val_str\"> ");
		buff.append("			<a href=\"#\">Ending value</a></th> ");
		buff.append("		<th width=\"207\" align=\"left\" valign=\"bottom\" class=\"val_str\"> ");
		buff.append("			<a href=\"#\">% Change</a></th> ");
		buff.append("	</tr> ");
		buff.append("	</thead> ");
		buff.append("	<tbody> ");
		int i = 0;
		String[] notes = null;
		for (Iterator it=fundNames.iterator(); it.hasNext(); i++) {
			String fundName = (String)it.next();

			/**
			 * Get array of footnote symbols for fund (only uniques values)
			 */

			buff.append("	<tr class=\"spec\"> \n");
			buff.append("		<td width=\"21\" class=\"name\"> \n");
			buff.append("			<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"> \n");
			buff.append("			<tbody> <tr> <td bgcolor=\"#");
			buff.append(BdProperties.getChartLineColor(i+1, true));
			buff.append("\" 			width=\"16\" height=\"1\" valign=\"middle\"> &nbsp;");
			buff.append("			</td></tr></tbody></table> \n");
			buff.append("		</td> \n");
			buff.append("		<td width=\"395\" class=\"name\"> \n");
			buff.append(fundName);
			if ( fundFootnotes.size() > i && (notes=(String[])fundFootnotes.get(i)).length > 0 ){
				buff.append("<sup>");
				for (int k = 0; k < notes.length; k++){
					if (notes[k] != null)
						buff.append(notes[k] + " ");
				}
				buff.append("</sup>");
			}
			buff.append("		</td>\n");

			double startingValue = unitValues[i][0];
			double endingValue = unitValues[i][unitValues[i].length - 1];
            buff.append("		<td align=\"right\" class=\"cur\">$ ");
			buff.append(PerformanceChartUtil.formatDouble(startingValue, true));
			buff.append("		&nbsp;</td>\n");
			
			buff.append("		<td align=\"right\" class=\"cur\">$ ");
			buff.append(PerformanceChartUtil.formatDouble(endingValue, true));
			buff.append("		&nbsp;</td>\n");

			buff.append("		<td align=\"right\" class=\"cur\"> ");
			buff.append(PerformanceChartUtil.formatDouble(100 * (endingValue - startingValue) / startingValue, true));
			buff.append("		%</td>\n");
			buff.append("</tr>\n");
		}
		buff.append("</tbody>");
		buff.append("</table> </div>");

		return buff.toString();
	}


	/**
	 * Returns a String containing the HTML required to draw the chart on the jsp page.
	 *
	 * @param width
	 *     The width in pixels of the chart on the jsp page.
	 *
	 * @param height
	 *     The height in pixels of the chart on the jsp page.
	 *
	 * @return
	 *     A String containing all of the HTML to draw the chart on the page.
	 **/
    @SuppressWarnings("unchecked")
	public String getLineChartHTML(	List listEffectiveDates,
 									double[][] unitValues,
 									ChartDataBean bean,
 									HttpSession session ) throws JspTagException {


		StringBuffer buff = new StringBuffer(4000);
		buff.append("<div class=\"page_section_subheader controls\">");
        buff.append("<h3>");
        if (getBeanName() == null) {
			buff.append("Fund Performance ");
		} else {
	        buff.append(getTitle());
			buff.append(" ");
		}
        buff.append(bean.getStartDate());
        buff.append(" &nbsp;-&nbsp; ");
        buff.append(bean.getEndDate());
        buff.append("</h3>");
        buff.append("<a href=\"javascript://\" onClick=\"doPrintPDF()\" class=\"pdf_btn\" title=\"Create PDF\">Create PDF</a></div>");
        

        buff.append("<br><center>");
		// check some of the optional paramters which override the bean
		if(mode!=null) bean.setMode(mode);

		// check some of the optional paramters which override the bean
		if(imageType!=null) bean.setImageType(imageType);
		if(	bean.getMode()==null||
		    bean.getMode().equalsIgnoreCase(ChartDataBean.PRESENT_IMAGE)
		) {
			// append the servlet tag to the buffer
			buff.append(getImageTags(listEffectiveDates,unitValues,bean,session));
		} else if(bean.getMode().equalsIgnoreCase(ChartDataBean.PRESENT_APPLET)) {
			// append the applet tag to the buffer
			buff.append(getAppletTags(listEffectiveDates,unitValues,bean));
		} else throw new JspTagException("Line Chart Render Mode Invalid");

		
		buff.append("<center> <br>");

		return buff.toString();
	}

	private static final int NUM_SELECTIONS = 6;

	/**
 	 * Filters array of funds and fund history by fund ids we are interested in.
 	 *
 	 *
 	 */
 	private static boolean determinePctValues( BDPerformanceChartInputForm form,
 											List pctValues ) {

		//now find all selected funds
		boolean pctExists = false;

		String[] pctLabels = new String[NUM_SELECTIONS];
		pctLabels[0] = form.getFundPercentage1();
		pctLabels[1] = form.getFundPercentage2();
		pctLabels[2] = form.getFundPercentage3();
		pctLabels[3] = form.getFundPercentage4();
		pctLabels[4] = form.getFundPercentage5();
		pctLabels[5] = form.getFundPercentage6();

		for( int i=0; i<NUM_SELECTIONS; i++ ){
			logger.debug( i+ ">>>" + pctLabels[i] + "<<<" );
		}

		for( int i=0; i<NUM_SELECTIONS; i++ ){
			if ( pctLabels[i] != null ){ 
				pctExists = true;
				double pct = 0;
				try{
					pct = Double.parseDouble(pctLabels[i]);
				}catch(NumberFormatException e){
					//keep it at 0
				}

				pctValues.add(new Double(pct));
			}
		}

		for( int i=0; i<NUM_SELECTIONS; i++ ){
			logger.debug( i+ ">>>" + pctLabels[i] + "<<<" );
		}
		return pctExists;
	}

	/**
	*	The JSP engine will call this method each time the body
	*	content of this tag has been processed. If it returns
	*	SKIP_BODY, the body content will have been processed for the
	*	last time. If it returns EVAL_BODY_TAG, the body will be processed
	*	and this method called at least once more.
	*/
    public int doAfterBody() throws JspTagException {
		return SKIP_BODY;
	}
	
	public String getTitle()  {

		String title = "";

		try {		
			Object bean = TagUtils.getInstance().lookup(pageContext, getBeanName(), null);
			if (bean != null) {
				if (bean instanceof LayoutPage) {
					LayoutPage page = (LayoutPage) bean;
					title = ContentUtility.getContentAttribute(page, "body1Header");
				}
			}
		} catch (JspException e) {
			//do nothing
		}
		
		return title;

	}

	public String getBeanName() {
		return this.beanName;
	}
	
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * Formats a the applet tags based on the bean contents.
	 *
	 *
	 * @param width
	 *     The width in pixels of the chart on the jsp page.
	 *
	 * @param height
	 *     The height in pixels of the chart on the jsp page.
	 *
	 * @param bean
	 *     The java bean which contains chart data.
	 * @return
	 *     A String containing the formatted applet tags.
	 **/
	@SuppressWarnings("unchecked")
	private String getAppletTags(
			List listEffectiveDates,
			double[][] unitValues,
			ChartDataBean bean
		)
	{
		SimpleDateFormat labelDateFormatter = new SimpleDateFormat(BdProperties.getChartXLabelDateFormat());

		StringBuffer buff = new StringBuffer(100);

		buff.append("<applet code=\"com.manulife.util.linechart.applet.LineChartApplet.class\" archive=\"/assets/unmanaged/applets/lineChartApplet.jar\" width=\"");
		buff.append(width);
		buff.append("\" height=\"");
		buff.append(height);
		buff.append("\">\n");
		buff.append("<param name=\"options\" value=\"");
		buff.append(BdProperties.getChartBackgroundColor());
		buff.append("\">\n");
		buff.append("<param name=\"gridOptions\" value=\"");
		buff.append(BdProperties.getChartGridColor());
		buff.append(",");
		buff.append(BdProperties.getChartMinValueUpperBound());
		buff.append(",");
		buff.append(BdProperties.getChartMaxValueLowerBound());
		buff.append(",");

		int numPoints = unitValues[0].length;

		int frequency = 1;
		int maxPoints = width / BdProperties.getChartMinXGridSpacingInPixels();
		while (((numPoints + frequency - 1)/ frequency) > maxPoints) frequency++;
		numPoints = numPoints / frequency;
		int xTickFrequency = 1;
		int maxTicks = width / BdProperties.getChartMinXLabelSpacingInPixels();
		while (((numPoints + xTickFrequency - 1)/ xTickFrequency) > maxTicks) xTickFrequency++;
		buff.append(xTickFrequency);
		buff.append(",");

		int yValueDelta = PerformanceChartUtil.calculateYAxisDelta(unitValues, height);

		buff.append(yValueDelta);
		buff.append(",$\">\n");
		buff.append("<param name=\"xLabels\" value=\"");
		buff.append(BdProperties.getChartXLabelColor());
		buff.append(",");

		Date[] effectiveDates = (Date[])listEffectiveDates.toArray(new Date[0]);

		int startingPoint = (effectiveDates.length - 1) % frequency;
		if (startingPoint > 0) {
			buff.append(((double)startingPoint)/((double)frequency));
			buff.append(",1,");
			buff.append(labelDateFormatter.format(effectiveDates[0]));
		} else {
			buff.append("1,1");
		}
		for (int i = startingPoint; i < effectiveDates.length; i += frequency) {
			buff.append(",");
			buff.append(labelDateFormatter.format(effectiveDates[i]));
		}
		buff.append("\">\n");
		for (int i = 0; i < unitValues.length; i++) {
			buff.append("<param name=\"line");
			buff.append(i + 1);
			buff.append("\" value=\"");
			buff.append(BdProperties.getChartLineColor(i + 1, false));
			if (startingPoint > 0) {
				buff.append(",");
				buff.append(PerformanceChartUtil.formatDouble(unitValues[i][0], false));
			}
			for (int j = startingPoint; j < unitValues[i].length; j += frequency) {
				buff.append(",");
				buff.append(PerformanceChartUtil.formatDouble(unitValues[i][j], false));
			}
			buff.append("\">\n");
		}
		buff.append("</applet>");
		

		return buff.toString();
	}

	/**
	 * Formats a the image tags based on the bean contents.
	 *
	 *
	 * @param width
	 *     The width in pixels of the chart on the jsp page.
	 *
	 * @param height
	 *     The height in pixels of the chart on the jsp page.
	 *
	 * @param bean
	 *     The java bean which contains chart data.
	 * @return
	 *     A String containing the formatted applet tags.
	 **/
	@SuppressWarnings("unchecked")
	private String getImageTags(
			List listEffectiveDates,
			double[][] unitValues,
			ChartDataBean bean,
			HttpSession session
		) {
		StringBuffer buff = new StringBuffer(100);
		ArrayList attrs=new ArrayList();
		attrs.add(new Attribute("src",getChartImageUrl(listEffectiveDates,unitValues,bean,session)));
		attrs.add(new Attribute("border","0"));
		attrs.add(new Attribute("width",String.valueOf(width)));
		attrs.add(new Attribute("height",String.valueOf(height)));
		if(alt!=null) attrs.add(new Attribute("alt",alt));
		if(getTitle()!=null) attrs.add(new Attribute("title",getTitle()));
		addElement(buff,"img",0,attrs,false);
		return buff.toString();
	}

	/**
	 * This method generates the URL for the LineChartServlet
	 * @param listEffectiveDates
	 * @param unitValues
	 * @param bean
	 * @return
	 * 		The URL String for the LineChartServlet
	 */
	@SuppressWarnings("unchecked")
	private String getChartImageUrl(
			List listEffectiveDates,
			double[][] unitValues,
			ChartDataBean bean,
			HttpSession session
		) {
		// <img src="main_chart.gif" name="Fund Performance" border="0" width="650" height="450" />
		// http://localhost:8080/LineChartServlet?width=650&height=650&imageType=GIF&options=QFF;255;255;TITLL&gridOptions=240;240;240;0;1500;1;100;$&yParams=200;200;200;The%20Dollar%20Amount&xParams=200;200;200;The%20Actual%20Date&xLabels=0;0;0;1;1;May-02;Jun-02;Jul-02;Aug-02;Sep-02;Oct-02;Nov-02;Dec-02;Jan-03;Feb-03;Mar-03&line1=204;0;0;1000.00;927.78;876.34;874.46;797.23;864.84;902.69;861.98;847.76;830.96;841.56&line2=0;204;0;1000.00;921.16;800.19;795.41;748.43;784.26;832.05;789.84;768.34;749.22;760.37&line3=0;0;204;1000.00;924.47;838.27;834.94;772.83;824.55;867.37;825.91;808.05;790.09;800.97

		StringBuffer returnURL = new StringBuffer("/LineChartServlet?chartParameterName=");
		returnURL.append(BDConstants.PERFORMANCE_CHART_PARMS);
		
		String params = PerformanceChartUtil.getChartImageUrlParams(listEffectiveDates, unitValues, bean, height, width);
		session.setAttribute(BDConstants.PERFORMANCE_CHART_PARMS, params);			

		return returnURL.toString();
	}


	/**
	 * Gets the alt
	 * @return Returns a String
	 */
	public String getAlt() {
		return alt;
	}
	/**
	 * Sets the alt
	 * @param alt The alt to set
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * Gets the mode
	 * @return Returns a String
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * Sets the mode
	 * @param mode The mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * Gets the imageType
	 * @return Returns a String
	 */
	public String getImageType() {
		return imageType;
	}
	/**
	 * Sets the imageType
	 * @param imageType The imageType to set
	 */
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	/**
	 * Append a whole element tag with the specified indent level
	 * @param buff is the StringBuffer
	 * @param name is the name of the element to add
	 * @param indentLevel is the required indent level for tabbing
	 * @param attributes is a collection of attributes to add
	 * @param complete id a boolean that indicates whether to complete the element or not
	 */
	@SuppressWarnings("unchecked")
	private static void addElement(StringBuffer buff, String name, int indentLevel, Collection attributes, boolean complete) {
		if(name==null||indentLevel<0) return;
		for(int i=0;i<indentLevel;i++) buff.append("\t");
		startElement(buff,name);
		if(attributes!=null) {
			Iterator atIt=attributes.iterator();
			while(atIt.hasNext()) {
				Attribute att=(Attribute)atIt.next();
				appendAttibute(buff,att.getName(),att.getValue());
			}
		}
		if(complete) closeCompleteElement(buff);
		else closeElement(buff);
	}

	private static  void startElement(StringBuffer buff,String name) {
		if(name!=null) {
			buff.append("<");
			buff.append(name);
		}
	}

	private static void closeElement(StringBuffer buff) {
		buff.append(">");
	}


	private static  void closeCompleteElement(StringBuffer buff) {
		buff.append("/>");
	}


	private static  void endElement(StringBuffer buff,String name) {
		if(name!=null) {
			buff.append("</");
			buff.append(name);
			buff.append(">\n");
		}
	}

	private static  void appendAttibute(StringBuffer buff,String name,String value) {
		if(name!=null) {
			buff.append(" ");
			buff.append(name);
			if(value==null) return;
			buff.append("=\"");
			buff.append(value);
			buff.append("\"");
		}
	}

	public final class Attribute
	{
		private String name;
		private String value;
	
		public Attribute(String name, String value)
		{
			this.name = name;
			this.value = value;
		}
	
		public String getName()
		{
			return name;
		}
	
		public String getValue()
		{
			return value;
		}
	}
	/**
	 * Gets the height
	 * @return Returns a int
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * Sets the height
	 * @param height The height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the width
	 * @return Returns a int
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * Sets the width
	 * @param width The width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

}

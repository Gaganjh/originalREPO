package com.manulife.pension.ps.web.taglib.investment;

/*
  File: FundTable.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-01-01   Chris Shin       Initial version.
*/

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.taglib.BaseTagHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.investment.InvestmentReportPresentationValue;
import com.manulife.pension.ps.web.investment.ViewFundGroupPresentation;
import com.manulife.pension.ps.web.investment.ViewFundPresentation;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.account.valueobject.HypotheticalInfo;
import com.manulife.pension.service.contract.valueobject.ContractDetailsOtherVO;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.SvgifFund;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This class is the tag that will build the contract funds table.
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 **/

public class FundTable extends TagSupport {

	private String beanName;
	private boolean highlight = false;
	private boolean printFriendly = false;
	private String productId;
	private String fundSeries;
	private Logger logger = Logger.getLogger(getClass());
	private boolean monthly = true;
	private static final String FORMAT_YEAR_YYYY = "yyyy";
	private static final String FORMAT_MONTH_MM = "MM";
	
	public void setBeanName(String value){
		beanName = value;
	}

	public String getBeanName(){
		return (beanName);
	}

	public void setPrintFriendly(boolean printFriendly){
		this.printFriendly = printFriendly;
	}

	public boolean isPrintFriendly(){
		return this.printFriendly;
	}
	
	/**
	 * @return the monthly
	 */
	public boolean isMonthly() {
		return this.monthly;
	}

	/**
	 * @param monthly the monthly to set
	 */
	public void setMonthly(boolean monthly) {
		this.monthly = monthly;
	}


	public void setHighlight(boolean highlight){
		this.highlight = highlight;
	}

	public boolean isHighlight(){
		return this.highlight;

	}
	/**
	 * @param productId The productId to set.
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return Returns the productId.
	 */
	public String getProductId() {
		return productId;
	}

/**
  *doStartTag is called by the JSP container when the tag is encountered
*/
	public int doStartTag()throws JspException {

		try {
			String svgifDisclosureText =null;
			String svgifFundId=null;
			String fundCreditRate=null;
			Content svgifDisclosureContent = null;
			boolean svgifFundInd = false;
			boolean isUBSContract = false;
			boolean isUBSCoveredFund = false;
			JspWriter out = pageContext.getOut();

			if (BaseTagHelper.isPrintFriendly((HttpServletRequest) pageContext
				.getRequest())) {
				setPrintFriendly(true);
			}
			
			if (BaseTagHelper.isMonthly((HttpServletRequest) pageContext
					.getRequest())) {
					setMonthly(true);
			} else {
				setMonthly(false);
			}

			UserProfile userProfile = SessionHelper.getUserProfile((HttpServletRequest) pageContext
	                .getRequest());
			String defaultRateType = userProfile.getCurrentContract().getDefaultClass();
			String companyCode = userProfile.getCurrentContract().getCompanyCode();
			Date asOfDate = userProfile.getCurrentContract().getContractDates().getAsOfDate();
			isUBSContract = userProfile.getCurrentContract().getDistributionChannel().equals(Constants.UBS_COFID);
			boolean isMerrillContract = false;
			ContractDetailsOtherVO contractDetailsOtherVO = ContractServiceDelegate.getInstance().getContractDetailsOther(userProfile.getCurrentContract().getContractNumber());
			if(contractDetailsOtherVO != null && contractDetailsOtherVO.isMerrillLynch()){
				isMerrillContract = true;
			}
			List<SvgifFund> svgifFundList = FundServiceDelegate.getInstance().getSVGIFDefaultFunds();
			Set<String> merrillFundIds = FundServiceDelegate.getInstance().getMerrillCoveredFundInvestmentIds();
			//UBS changes
			List<String> ubsFundList = FundServiceDelegate.getInstance().getServiceProviderCoveredFunds(asOfDate, companyCode, Constants.UBS_COFID);
			
			// Reterview Disclosure for SVGIF Funds
			if(svgifFundList != null){
			svgifDisclosureContent = ContentCacheManager
					.getInstance().getContentById(ContentConstants.SVGIF_FUND_DISCLOSURE,ContentTypeManager.instance().FEE_DISCLOSURE, Location.US);
			svgifDisclosureText = ContentUtility.getContentAttribute(svgifDisclosureContent, "text");
			}
			/**
			 * Get the name of the InvestmentReportPresentationValue object from the pageContext attribute "investmentReport"
			 * This object passed into the handler class from the JSP page as "beanName"
			 */
			InvestmentReportPresentationValue report = (InvestmentReportPresentationValue)(pageContext.getSession()).getAttribute(beanName);

			if (report != null) {
				String unitValueEffectiveDate = DateRender.formatByPattern(report.getUnitValueEffectiveDate()," ",RenderConstants.MEDIUM_MDY_SLASHED , RenderConstants.EXTRA_LONG_MDY);
				String ferEffectiveDate = DateRender.formatByPattern(report.getFerEffectiveDate()," ",RenderConstants.MEDIUM_MDY_SLASHED , RenderConstants.EXTRA_LONG_MDY);
				String qeEffectiveDate = DateRender.formatByPattern(calculateQuarterEnd(new Date(report.getRateOfReturnEffectiveDate()))," ",RenderConstants.MEDIUM_MDY_SLASHED , RenderConstants.EXTRA_LONG_MDY);				
				String rateOfReturnEffectiveDate = DateRender.formatByPattern(report.getRateOfReturnEffectiveDate()," ",RenderConstants.MEDIUM_MDY_SLASHED , RenderConstants.EXTRA_LONG_MDY);
				

				/**
				 *  Get the Fund Groups + back and fore ground colors
				 *  For each fund groups, get the associated funds
				 *  Set the headings for the dates
				 */
				ViewFundGroupPresentation[] groupfunds = report.getFundGroups();
				
				/**
				 * Column headings
				 */	
				if(isPrintFriendly()){
					out.println("<TABLE CELLSPACING=0 CELLPADDING=0 WIDTH=100% BORDER=0 >");
					out.println("<TR class=tablehead>");
					out.println("<TD WIDTH=\"1%\"</TD>");
					out.println("<TD WIDTH=\"10%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"13%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"4%\"</TD>");
					out.println("<TD WIDTH=\"4%\"</TD>");
					out.println("<TD WIDTH=\"4%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"4%\"</TD>");
					out.println("<TD WIDTH=\"4%\"</TD>");
					out.println("<TD WIDTH=\"4%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"3%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"12%\"</TD>");
					out.println("</TR>");
				} else {
					out.println("<TABLE CELLSPACING=0 CELLPADDING=0 WIDTH=100% BORDER=0 >");
					out.println("<TR class=tablehead>");
					out.println("<TD WIDTH=\"1%\"</TD>");
					out.println("<TD WIDTH=\"9%\"</TD>");
					out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
					out.println("<TD WIDTH=\"16%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"4%\"</TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD WIDTH=\"5%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"4%\"</TD>");
					out.println("<TD class=\"datadivider\" style=\"background:#89A2B3\" width=\"1\"></TD>");
					out.println("<TD WIDTH=\"7%\"</TD>");
					out.println("</TR>");
				}
				for (int i = 0; i < groupfunds.length; i++){					
					String groupName = (groupfunds[i]).getGroupName();
					String backGroundColorCode = (groupfunds[i]).getBackgroundColorCode();
					String foreGroundColorCode = (groupfunds[i]).getForegroundColorCode();
					//Different colspan for print report and jsp page.
					String colspan = isPrintFriendly() ? "31 " : "25";
					out.println("<TR>");
					out.println("<TD  COLSPAN="+colspan+" class=\"greyText\" valign=\"top\" ALIGN=\"LEFT\" WIDTH= \"100%\" BGCOLOR='"+ backGroundColorCode+ "'><FONT COLOR='"+ foreGroundColorCode+ "'><B>&nbsp;"+ groupName + "</B></TD>");
					out.println("</TR>");
					out.println("<TR>");
					out.println("<TD  COLSPAN="+colspan+"><BR/></TD>");
					out.println("</TR>");					
					out.println("<TR>");
					
					if(!isPrintFriendly()){
						
						out.println(createCenteredTableCell(null, "tableTextBold","2%", "&nbsp", ""));
						out.println(createCenteredTableCell(null, "tableTextBold","18%", "&nbsp", ""));
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold","20%", "&nbsp", ""));
						
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold","17%", "Unit Values<BR>as of: " + unitValueEffectiveDate, "3"));
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold", "14%", "Returns* <BR>(%) as of:<BR> "+ rateOfReturnEffectiveDate, "3"));
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						//For monthly and quarterly heading
						if (!isMonthly()) {						
							out.println(createCenteredTableCell(null, "tableTextBold","15%", "Returns* <BR>(%) as of:<BR> "+ rateOfReturnEffectiveDate, "5"));
						}else{
							out.println(createCenteredTableCell(null, "tableTextBold","15%", "Returns* <BR>(%) as of:<BR> "+ qeEffectiveDate, "5"));
						}						
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold","12%", "As of:<BR> "+ ferEffectiveDate ,"1"));
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("</TR>");
						out.println("<TR>");
						out.println("<TD colspan=2></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=1></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=3></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=3></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						if (!isMonthly()) {
							out.println("<TD colspan=5 ALIGN=\"CENTER\"><BR/><strong>Shown</strong>: monthly returns<BR/><A HREF='?&monthly=true'>View quarterly returns</A></TD>");
						} else {							
							out.println("<TD colspan=5 ALIGN=\"CENTER\"><BR/><strong>Shown</strong>: quarterly returns<BR/><A HREF='?&monthly=false'>View monthly returns</A></TD>");
						}
	
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=1></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");	
						out.println("</TR>");
					}else{
						
						out.println(createCenteredTableCell(null, "tableTextBold","0.5%", "&nbsp", ""));
						out.println(createCenteredTableCell(null, "tableTextBold","19.5%", "&nbsp", ""));
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold","15%", "&nbsp", ""));
						
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold",null, "Unit Values<BR>as of: " + unitValueEffectiveDate, "3"));						
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold",null, "Returns* <BR>(%) as of:<BR> "+ rateOfReturnEffectiveDate, "3"));
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold",null, "Returns* <BR>(%) as of:<BR> "+ rateOfReturnEffectiveDate+ " Monthly", "5"));
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold",null, "Returns* <BR>(%) as of:<BR> "+ qeEffectiveDate+ " Quarterly" , "5"));
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println(createCenteredTableCell(null, "tableTextBold",null, "As of:<BR> "+ ferEffectiveDate,"1"));
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD>&nbsp</TD");
						out.println("</TR>");
						out.println("<TR>");
						out.println("<TD colspan=2></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=1></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=3></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=3></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=5></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=5></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=1></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");						
						out.println("</TR>");						
					}					
					
					out.println("<TR VALIGN=\"top\" ALIGN=\"CENTER\">");
					//For print report page.
					if (isPrintFriendly()) {
						out.println("<TD class=\"columnHeading\" COLSPAN = \"2\" ALIGN=\"LEFT\" VALIGN=BOTTOM >Investment<BR>Option</TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >Manager or<BR>Sub-<BR>Adviser<sup>*3</sup></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >Unit<BR>Value<sup>*46</sup></TD>");
						out.println("<TD ALIGN=\"CENTER\" VALIGN=BOTTOM COLSPAN=2 >");
						out.println("<TABLE border=0 WIDTH=\"100%\" style = \"border-collapse: collapse;\"> <TR>");
						out.println("<TD  ALIGN=\"CENTER\"  VALIGN=BOTTOM COLSPAN=2 class=\"tableTextBold\">Daily Change");
						out.println("</TD></TR><TR>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" WIDTH=\"50%\"  VALIGN=BOTTOM >($)</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" WIDTH=\"50%\"  VALIGN=BOTTOM >(%)</TD>");
						out.println("</TR></TABLE></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >1mth</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >3mth</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >YTD</TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >1Yr</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >3Yr</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >5Yr</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >10Yr</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >Since <BR> Inception</TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >1Yr</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >3Yr</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >5Yr</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >10Yr</TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >Since <BR> Inception</TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >Expense<BR>Ratio**(%)</TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM >Morningstar<BR>Category<sup>*7</sup></TD>");						
					} else {
						out.println("<TD class=\"columnHeading\" COLSPAN = \"2\" ALIGN=\"LEFT\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,0)+ " HREF=\"?&sortByType=1&columnNumber=0\">Investment<BR>Option</A></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,2)+ " HREF=\"?&sortByType=1&columnNumber=2\">Manager or<BR>Sub-<BR>Adviser<sup>*3</sup></A></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,3)+ " HREF=\"?&sortByType=1&columnNumber=3\">Unit<BR>Value<sup>*46</sup></A></TD>");
						out.println("<TD ALIGN=\"CENTER\" WIDTH=\"12%\"  VALIGN=BOTTOM COLSPAN=2 >");
						out.println("<TABLE border=0 WIDTH=\"100%\" style = \"border-collapse: collapse;\"> <TR>");
						out.println("<TD ALIGN=\"CENTER\"  VALIGN=BOTTOM COLSPAN=2 class=\"tableTextBold\">Daily Change");
						out.println("</TD></TR><TR>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,4)+ " HREF=\"?&sortByType=1&columnNumber=4\">($)</A></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,5)+ " HREF=\"?&sortByType=1&columnNumber=5\">(%)</A></TD>");
						out.println("</TR></TABLE></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,6)+ " HREF=\"?&sortByType=1&columnNumber=6\">1mth</A></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,7)+ " HREF=\"?&sortByType=1&columnNumber=7\">3mth</A></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,8)+ " HREF=\"?&sortByType=1&columnNumber=8\">YTD</A></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						if(!isMonthly()){
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,9)+ " HREF=\"?&sortByType=1&columnNumber=9\">1Yr</A></TD>");
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,10)+ " HREF=\"?&sortByType=1&columnNumber=10\">3Yr</A></TD>");
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,11)+ " HREF=\"?&sortByType=1&columnNumber=11\">5Yr</A></TD>");
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,12)+ " HREF=\"?&sortByType=1&columnNumber=12\">10Yr</A></TD>");
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,13)+ " HREF=\"?&sortByType=1&columnNumber=13\">Since <BR> Inception</A></TD>");
						}else{
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,24)+ " HREF=\"?&sortByType=1&columnNumber=24\">1Yr</A></TD>");
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,25)+ " HREF=\"?&sortByType=1&columnNumber=25\">3Yr</A></TD>");
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,26)+ " HREF=\"?&sortByType=1&columnNumber=26\">5Yr</A></TD>");
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,27)+ " HREF=\"?&sortByType=1&columnNumber=27\">10Yr</A></TD>");
							out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,28)+ " HREF=\"?&sortByType=1&columnNumber=28\">Since <BR> Inception</A></TD>");
						}
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\"  VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,14)+ " HREF=\"?&sortByType=1&columnNumber=17\"  >Expense<BR>Ratio**(%)</A></TD>");
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD class=\"columnHeading\"  ALIGN=\"CENTER\" VALIGN=BOTTOM ><A"+ createId(Constants.ANCHOR_BASE_ID, i,15)+ " HREF=\"?&sortByType=1&columnNumber=18\">Morningstar<BR>Category<sup>*7</sup></A></TD>");						
					}
					out.println("</TR>");
					out.println("<TR>");
					out.println("<TD colspan=2>&nbsp;</TD>");
					out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
					out.println("<TD colspan=1>&nbsp;</TD>");
					out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
					out.println("<TD colspan=3>&nbsp;</TD>");
					out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
					out.println("<TD colspan=3>&nbsp;</TD>");
					out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
					out.println("<TD colspan=5>&nbsp;</TD>");					
					if(isPrintFriendly()){
						out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						out.println("<TD colspan=5>&nbsp;</TD>");
					}
					out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
					out.println("<TD colspan=1>&nbsp;</TD>");
					out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
					out.println("<TD>&nbsp;</TD>");
					out.println("</TR>");
					if ((groupfunds[i]).getViewFunds() != null) {					   
						ViewFundPresentation[] funds = (groupfunds[i]).getViewFunds();
						
						/**
						 *  For each fund, get related data
						 */
						for (int j = 0; j < funds.length; j++){

							//is the fund selected
							//boolean inParticipantHoldings =  funds[j].getInParticipantHoldings();
							//Supress the non-covered funds (funds that do not have a W on PCIQ screen)
							
							isUBSCoveredFund = ubsFundList.contains(funds[j].getFundId());
							if(isUBSContract){
								if( !isUBSCoveredFund && !funds[j].getSelectedFlag()){
								continue; 
							}
						}
							boolean highlightInvestments =  funds[j].getSelectedFlag() && isHighlight();
							String rateType = null;
							//Fund Name
							String fundName = funds[j].getFundName();
							String fundRateType = funds[j].getRateType();
							
							//Fund Id
							String fundId = funds[j].getFundId();
							svgifFundInd = svgifFundList.stream().filter(o -> o.getFundId().equals(fundId)).findFirst().isPresent();
							
							//Fund Type
							String fundType = funds[j].getFundType();
							//Fund Manager Name
							String fundManagerName = funds[j].getFundManagerName();
							//Unit Value
							BigDecimal unitValue = funds[j].getUnitValue();	
							//$
							BigDecimal  dailyVariance = funds[j].getDailyVariance();
							//% 
							BigDecimal dailyReturn = funds[j].getDailyReturn();
						    BigDecimal oneMonthReturn = funds[j].getOneMonthReturn();
							BigDecimal threeMonthReturn = funds[j].getThreeMonthReturn();
							BigDecimal yearToDateReturn = funds[j].getYearToDateReturn();
							BigDecimal oneYearReturn = funds[j].getOneYearReturn();
							BigDecimal threeYearReturn = funds[j].getThreeYearReturn();
							BigDecimal fiveYearReturn = funds[j].getFiveYearReturn();
							BigDecimal tenYearReturn = funds[j].getTenYearReturn();
							String morningstarCategory = funds[j].getMorningstarCategory();
							morningstarCategory = StringUtils.isNotEmpty(morningstarCategory) ? WordUtils.capitalize(morningstarCategory.toLowerCase()) : morningstarCategory;
							BigDecimal annualInvestmentCharge = funds[j].getAnnualInvestmentCharge();
							BigDecimal sinceInception = funds[j].getSinceInceptionReturn();
							BigDecimal oneYearQuarterlyReturn = funds[j].getOneYearQuarterlyReturn();
							BigDecimal threeYearQuarterlyReturn = funds[j].getThreeYearQuarterlyReturn();
							BigDecimal fiveYearQuarterlyReturn = funds[j].getFiveYearQuarterlyReturn();
							BigDecimal tenYearQuarterlyReturn = funds[j].getTenYearQuarterlyReturn();
							BigDecimal sinceInceptionQuarterly = funds[j].getSinceInceptionQuarterlyReturn();
							HypotheticalInfo hypotheticalInfo = funds[j].getHypotheticalInfo();
							
							/**
							 * Get array of footnote symbols for fund (only uniques values)
							 */
							Set uniquesFootnoteSymbols = new HashSet(Arrays.asList(funds[j].getFundFootnoteSymbols()));
							String[] fundFootnoteSymbols = (String[])uniquesFootnoteSymbols.toArray(new String[] {});
							
							 /* if fund is selected then set the backgound color to yellow
							 */
							String styleName = "tableText";
							if ((highlightInvestments) && (fundId != null)){
								out.println("<TR class=\"selectedFund\">");
								styleName = "tableTextBold";
							} else {
								out.println("<TR>");
								styleName = "tableText";
							}
							
							if (fundRateType != null && (Constants.RATE_TYPE_CX0.equals(fundRateType)|| Constants.RATE_TYPE_CY1.equals(fundRateType)|| Constants.RATE_TYPE_CY2.equals(fundRateType))) {
								rateType = fundRateType;
							}else {
								rateType = defaultRateType;
							}
							//Setting Credit value to SVGIF Funds
							for (SvgifFund svgFunds:svgifFundList) {
								svgifFundId = svgFunds.getFundId();
								fundCreditRate = svgFunds.getFundCreditingRate();
								if(svgifFundId.equals(fundId) && (svgifDisclosureText != null && svgifDisclosureText.contains("{0}"))) {
									svgifDisclosureText = svgifDisclosureText.replace("{0}", fundCreditRate+"%");
								}
							}
							
							if (isPrintFriendly()) {
								if(isMerrillContract){
								if(report.getFeeWaiverFunds().contains(fundId) && report.getRestrictedFunds().containsKey(fundId)) {
									out.println(createTableCell(createId(Constants.CELL_BASE_ID, i, 1, j), "tableTextBold", "RIGHT", null, "• #", ""));
								}else if(report.getFeeWaiverFunds().contains(fundId)){
									out.println(createTableCell(createId(Constants.CELL_BASE_ID, i, 1, j), "tableTextBold", "RIGHT", null, "•", ""));
								}else if(report.getRestrictedFunds().containsKey(fundId) && !(fundId.equals("XX05") || fundId.equals("XX03") || fundId.equals("XX11") ||fundId.equals("XX14"))) {
									out.println(createTableCell(createId(Constants.CELL_BASE_ID, i, 1, j), "tableTextBold", "RIGHT", null, "#", ""));
								}else{
								out.println(createTableCell(createId(Constants.CELL_BASE_ID, i, 1, j),styleName, "RIGHT", null, "", ""));
								}
							}else{
									if(report.getFeeWaiverFunds().contains(fundId)) {
										out.println(createTableCell(createId(Constants.CELL_BASE_ID, i, 1, j), "tableTextBold", "RIGHT", null, "•", ""));
									} else {
										out.println(createTableCell(createId(Constants.CELL_BASE_ID, i, 1, j),styleName, "RIGHT", null, "", ""));
									}
									
								}
							} else {
								if(isMerrillContract){
								if(report.getFeeWaiverFunds().contains(fundId) && report.getRestrictedFunds().containsKey(fundId)){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 1, j), "tableTextBold", null, "• #", ""));
								}else if(report.getFeeWaiverFunds().contains(fundId)) {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 1, j), "tableTextBold", null, "•", ""));
								}else if(report.getRestrictedFunds().containsKey(fundId) && !(fundId.equals("XX05") || fundId.equals("XX03") || fundId.equals("XX11") ||fundId.equals("XX14"))) {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 1, j), "tableTextBold", null, "#", ""));
								}else{
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 1, j),styleName, null, "", ""));
								}
								}else{
									if(report.getFeeWaiverFunds().contains(fundId)) {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 1, j), "tableTextBold", null, "•", ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 1, j),styleName, null, "", ""));
									}
								}
							}
							
							if (!isPrintFriendly() && ((fundId != null) && (!Arrays.asList(Constants.IN_FUND_TYPE).contains(fundType)))){
								out.println("<TD "
                                                + createId(Constants.CELL_BASE_ID, i, 0, j)
                                                + " class=\"fundLink\"  ALIGN=\"LEFT\" ><A HREF='#fundsheet'; return true' NAME='"
                                                + fundId
                                                + "' onClick='FundWindow(\""
                                                + FundServiceDelegate.getInstance()
                                                        .getFundSheetURL(productId, getFundSeries(), fundType,
                                                                fundId, rateType, Environment.getInstance().getSiteLocation())
                                                + "\")'>" + fundName + "</A>");
							} else {
								out.println("<TD " + createId(Constants.CELL_BASE_ID, i, 0, j) + " class=\"tableText\"  ALIGN=\"LEFT\" ><font color=\"#666666\">"+ fundName + "</font>");
							}
							/**
							 * for each footnote symbol - enclose within <sup> tags and a space in between each footnote symbol
							 */

							if (fundFootnoteSymbols.length > 0) {
								out.println("<sup>");
									for (int k = 0; k < fundFootnoteSymbols.length; k++){
										if (fundFootnoteSymbols[k] != null){
											out.println(fundFootnoteSymbols[k] + " ");
										}
									}
								out.println("</sup>");
							}
							out.println("</TD>");

							/**
							 * print out each fund's data
							 */
							out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
							if (fundManagerName != null && fundManagerName.trim().length() > 0){
								out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 1, j),styleName, null, fundManagerName, ""));
							} else {
								out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 1, j),styleName, null, "-", ""));
							}
							
							if (!isPrintFriendly()){
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
								
								//unit value
								if (unitValue != null  && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 2, j),styleName, null, unitValue.toString(),""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 2, j),styleName, null, "-", ""));
								}
		
								//$
								if (dailyVariance != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 3, j),styleName, null, dailyVariance.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 3, j),styleName, null, "-", ""));
								}
	
								//%
								if (dailyReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 4, j),styleName, null,dailyReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 4, j),styleName, null, "-", ""));
								}
								
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
								
								// 1 month
								if (oneMonthReturn != null && !svgifFundInd ){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 5, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor1mthHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+oneMonthReturn.toString()+"</span>" : oneMonthReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 5, j),styleName, null, "-", ""));
								}
	
								// 3 month
								if (threeMonthReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 6, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor3mthHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+threeMonthReturn.toString()+"</span>" : threeMonthReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 6, j),styleName, null, "-", ""));
								}
	
								// YTD
								if (yearToDateReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 7, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRorYtdHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+yearToDateReturn.toString()+"</span>" : yearToDateReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 7, j),styleName, null, "-", ""));
								}
									
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
								
								if (!isMonthly()) {
									// 1 year
									if (oneYearReturn != null && !svgifFundInd){
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 8, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor1yrHypothetical())?"<span style = \"font-weight:bold;font-size:11px; white-space:nowrap;\">"+oneYearReturn.toString()+"</span>" : oneYearReturn.toString(), ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 8, j),styleName, null, "-", ""));
									}
									// 3 years
									if (threeYearReturn != null && !svgifFundInd){
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 9, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor3yrHypothetical())?"<span style = \"font-weight:bold;font-size:11px; white-space:nowrap;\">"+threeYearReturn.toString()+"</span>" : threeYearReturn.toString(), ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 9, j),styleName, null, "-", ""));
									}
									// 5 year
									if (fiveYearReturn != null && !svgifFundInd){
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 10, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor5yrHypothetical())?"<span style = \"font-weight:bold;font-size:11px; white-space:nowrap;\">"+fiveYearReturn.toString()+"</span>" : fiveYearReturn.toString(), ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 10, j),styleName, null, "-", ""));
									}
									// 10 year
									if (tenYearReturn != null && !svgifFundInd){
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 11, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor10yrHypothetical())?"<span style = \"font-weight:bold;font-size:11px; white-space:nowrap;\">"+tenYearReturn.toString()+"</span>" : tenYearReturn.toString(), ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 11, j),styleName, null, "-", ""));
									}
									// Since Inception
									if (sinceInception != null && !svgifFundInd){									
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 12, j),styleName, null,  (hypotheticalInfo!=null && hypotheticalInfo.isRorSinceInceptionHypothetical()) ? "<span style = \"font-weight:bold;font-size:11px \">"+sinceInception.toString()+"</span>" : sinceInception.toString() , ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 12, j),styleName, null, "-", ""));
									}
								} else {
									// 1 year Quarterly
									if (oneYearQuarterlyReturn != null && !svgifFundInd){
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 8, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor1yrQeHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+oneYearQuarterlyReturn.toString()+"</span>" : oneYearQuarterlyReturn.toString(), ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 8, j),styleName, null, "-", ""));
									}
									// 3 years Quarterly
									if (threeYearQuarterlyReturn != null && !svgifFundInd){
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 9, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor3yrQeHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+threeYearQuarterlyReturn.toString()+"</span>" : threeYearQuarterlyReturn.toString(), ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 9, j),styleName, null, "-", ""));
									}
									// 5 year Quarterly
									if (fiveYearQuarterlyReturn != null && !svgifFundInd){
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 10, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor5yrQeHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+fiveYearQuarterlyReturn.toString()+"</span>" : fiveYearQuarterlyReturn.toString(), ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 10, j),styleName, null, "-", ""));
									}
									// 10 year Quarterly
									if (tenYearQuarterlyReturn != null && !svgifFundInd){
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 11, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor10yrQeHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+tenYearQuarterlyReturn.toString()+"</span>" : tenYearQuarterlyReturn.toString(), ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 11, j),styleName, null, "-", ""));
									}
									//Quarterly Since Inception 
									if (sinceInceptionQuarterly != null && !svgifFundInd){									
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 12, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRorSinceInceptionQeHypothetical()) ? "<span style = \"font-weight:bold;font-size:11px \">"+sinceInceptionQuarterly.toString()+"</span>" : sinceInceptionQuarterly.toString(), ""));
									} else {
										out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 12, j),styleName, null, "-", ""));
									}
								}
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
						
								// Expense Ratio**
								if (annualInvestmentCharge != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 13, j),styleName, null, annualInvestmentCharge.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 13, j),styleName, null, "-", ""));
								}						
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
								
								// Morning Star Category**
								if (morningstarCategory != null){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 14, j),styleName, null, morningstarCategory,""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 14, j),styleName, null, "-", ""));
								}
							} else {						
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
								
								//unit value
								if (unitValue != null && !svgifFundInd ){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 2, j),styleName, null, unitValue.toString(),""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 2, j),styleName, null, "-", ""));
								}
								//$
								if (dailyVariance != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 3, j),styleName, null, dailyVariance.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 3, j),styleName, null, "-", ""));
								}
								//%
								if (dailyReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 4, j),styleName, null,dailyReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 4, j),styleName, null, "-", ""));
								}
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
								
								// 1 month
								if (oneMonthReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 5, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor1mthHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+oneMonthReturn.toString()+"</span>" : oneMonthReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 5, j),styleName, null, "-", ""));
								}
								// 3 month
								if (threeMonthReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 6, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor3mthHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+threeMonthReturn.toString()+"</span>" : threeMonthReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 6, j),styleName, null, "-", ""));
								}
								// YTD
								if (yearToDateReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 7, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRorYtdHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+yearToDateReturn.toString()+"</span>" : yearToDateReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 7, j),styleName, null, "-", ""));
								}
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
								// 1 year
								if (oneYearReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 8, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor1yrHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+oneYearReturn.toString()+"</span>" : oneYearReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 8, j),styleName, null, "-", ""));
								}
								// 3 years
								if (threeYearReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 9, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor3yrHypothetical())?"<span style = \"font-weight:bold;font-size:11px \">"+threeYearReturn.toString()+"</span>" : threeYearReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 9, j),styleName, null, "-", ""));
								}
								// 5 year
								if (fiveYearReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 10, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor5yrHypothetical())?"<span style = \"font-weight:bold;font-size:11px; \">"+fiveYearReturn.toString()+"</span>" : fiveYearReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 10, j),styleName, null, "-", ""));
								}
								// 10 year
								if (tenYearReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 11, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor10yrHypothetical())?"<span style = \"font-weight:bold;font-size:11px;\">"+tenYearReturn.toString()+"</span>" : tenYearReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 11, j),styleName, null, "-", ""));
								}
								// Since Inception
								if (sinceInception != null && !svgifFundInd){									
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 12, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRorSinceInceptionHypothetical()) ? "<span style = \"font-weight:bold;font-size:11px \">"+sinceInception.toString()+"</span>" : sinceInception.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 12, j),styleName, null, "-", ""));
								}
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
								// 1 year Quarterly
								if (oneYearQuarterlyReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 24, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor1yrQeHypothetical())?"<span style = \"font-weight:bold;font-size:11px;\">"+oneYearQuarterlyReturn.toString()+"</span>" : oneYearQuarterlyReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 24, j),styleName, null, "-", ""));
								}
								// 3 years Quarterly
								if (threeYearQuarterlyReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 25, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor3yrQeHypothetical())?"<span style = \"font-weight:bold;font-size:11px;\">"+threeYearQuarterlyReturn.toString()+"</span>" : threeYearQuarterlyReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 25, j),styleName, null, "-", ""));
								}
								// 5 year
								if (fiveYearQuarterlyReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 26, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor5yrQeHypothetical())?"<span style = \"font-weight:bold;font-size:11px;\">"+fiveYearQuarterlyReturn.toString()+"</span>" : fiveYearQuarterlyReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 26, j),styleName, null, "-", ""));
								}
								// 10 year Quarterly
								if (tenYearQuarterlyReturn != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 27, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRor10yrQeHypothetical())?"<span style = \"font-weight:bold;font-size:11px;\">"+tenYearQuarterlyReturn.toString()+"</span>" : tenYearQuarterlyReturn.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 27, j),styleName, null, "-", ""));
								}
								// Since Inception Quarterly
								if (sinceInceptionQuarterly != null && !svgifFundInd){									
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 28, j),styleName, null, (hypotheticalInfo!=null && hypotheticalInfo.isRorSinceInceptionQeHypothetical()) ? "<span style = \"font-weight:bold;font-size:11px \">"+sinceInceptionQuarterly.toString()+"</span>" : sinceInceptionQuarterly.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 28, j),styleName, null, "-", ""));
								}
								
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
												
								// Expense Ratio**
								if (annualInvestmentCharge != null && !svgifFundInd){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 18, j),styleName, null, annualInvestmentCharge.toString(), ""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 18, j),styleName, null, "-", ""));
								}
								//vertical line
								out.println("<TD class=\"datadivider\" width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=1 height=1></TD>");
								// Morning Star Category**
								if (morningstarCategory != null){
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 19, j),styleName, null, morningstarCategory,""));
								} else {
									out.println(createCenteredTableCell(createId(Constants.CELL_BASE_ID, i, 19, j),styleName, null, "-", ""));
								}								
							}
							out.println("</TR>");
							
							if (funds[j].getFundDisclosureText() != null
									&& (Constants.MONEY_MARKET_FUND_US.equals(fundId) || Constants.MONEY_MARKET_FUND_NY.equals(fundId))) {
								out.println("<TR>");
								out.println("<TD colspan=29>"
										+ funds[j].getFundDisclosureText() + "</TD>");
								out.println("</TR>");
							}
							if (funds[j].getFundDisclosureText() != null
									&& (Constants.INVESTCO_PREMIER_US_GOVT_MONEY_FUND_US.equals(fundId) || Constants.INVESTCO_PREMIER_US_GOVT_MONEY_FUND_NY.equals(fundId))) {
								out.println("<TR>");
								out.println("<TD colspan=29>"
										+ funds[j].getFundDisclosureText() + "</TD>");
								out.println("</TR>");
							}
							if (svgifDisclosureText != null && (svgifFundInd)) {
								out.println("<TR>");
								out.println("<TD colspan=29>"
										+ svgifDisclosureText + "</TD>");
								out.println("</TR>");
							}
						}out.println("<TR>");
						out.println("<TD colspan=23>&nbsp;</TD>");													
						out.println("</TR>");						
					}
				}out.println("</TABLE>");
				out.println("<BR/>");
			}
		} catch (Exception ex){

			SystemException se = new SystemException(ex,"com.manulife.pension.ps.web.taglib.investment.FundTable", "doStartTag", "Exception when building the table: " + ex.toString() );
			LogUtility.logSystemException(CommonConstants.PS_APPLICATION_ID,se);
			throw new JspException(se.getMessage());
		}
		return SKIP_BODY;
	}

	private String createTableCell(String idString, String classStr,
			String align, String width, String content, String colspan) {
		StringBuffer result = new StringBuffer();
		result.append("<TD ");
		if (idString != null) {
			result.append(idString);
		}
		result.append(" ");
		if (classStr != null) {
			result.append("class=\"").append(classStr).append("\" ");
		}
		if (align != null) {
			result.append("ALIGN=\"").append(align).append("\" ");
		}
		if (width != null) {
			result.append("WIDTH=\"").append(width).append("\" ");
		}
		if (colspan != null) {
			result.append("COLSPAN=\"").append(colspan).append("\" ");
		}
		result.append(">");
		if (content != null) {
			result.append(content);
		}
		result.append("</TD>");
		return result.toString();
	}

	private String createCenteredTableCell(String idString, String classStr,
			String width, String content, String colspan) {
		return createTableCell(idString, classStr, "CENTER", width, content,
				colspan);
	}


/**
 * Generate the id="" string to be placed into links and table cells
 * These ids are used by HttpUnit tests to locate links and content
 * The periods in the ids are used to split out the row and column numbers
 * for the tests themselves
 */
	private String createId(String base, int section, int column, int row) {
		StringBuffer result = new StringBuffer();
		result.append(" id=\"");
		result.append(createBaseId(base));
		if (section > -1) {
			result.append(Constants.ID_SEPARATOR).append(Constants.SECTION_ID);
			result.append(Constants.ID_SEPARATOR).append(String.valueOf(section));
		}
		if (column > -1) {
			result.append(Constants.ID_SEPARATOR).append(Constants.COLUMN_ID);
			result.append(Constants.ID_SEPARATOR).append(String.valueOf(column));
		}
		if (row > -1) {
			result.append(Constants.ID_SEPARATOR).append(Constants.ROW_ID);
			result.append(Constants.ID_SEPARATOR).append(String.valueOf(row));
		}
		result.append("\"");
		return result.toString();
	}

	private String createId(String base, int section, int column) {
		return createId(base, section, column, -1);
	}


	private String createBaseId(String base) {
		if (base != null && !"".equals(base)) {
			return base;
		} else {
			return "id";
		}
	}


	public void setFundSeries(String fundSeries) {
		this.fundSeries = fundSeries;
	}

	public String getFundSeries() {
		return fundSeries;
	}
	
	 /**
     * calculate the previous closest quarter end date
     * <P>
     * <b>input</b> - we always get a date that is the last day of any given
     * month<br>
     * <b>output</b> - closest qe equal or prior to month provided.
     */
    public static Date calculateQuarterEnd(Date inputDate)  {
    	
        SimpleDateFormat sdfMonth = new SimpleDateFormat(FORMAT_MONTH_MM);
        
        SimpleDateFormat sdfYear = new SimpleDateFormat(FORMAT_YEAR_YYYY);
        
        Calendar cal = Calendar.getInstance();
        int i = 0;
        // current month (subtract 1 as month starts at zero in calendar)
        int currentMth;
        synchronized (sdfMonth) {
        	currentMth = Integer.parseInt(sdfMonth.format(inputDate)) - 1;
        }
        // get the current year
        int currentYear;
        synchronized (sdfYear) {
        	currentYear = Integer.parseInt(sdfYear.format(inputDate));
        }
        // quarter months (11 is first and last to make life easy in Jan/Feb)
        int[] qMe = new int[] { 11, 2, 5, 8, 11 };

        for (i = 1; i < qMe.length; i++) {
            if (currentMth < qMe[i]) {
                if (qMe[i] == 2) {
					currentYear = currentYear - 1;
				}
                break;
            }
        }
        cal.clear();
        cal.set(Calendar.YEAR, currentYear);
        cal.set(Calendar.MONTH, qMe[i - 1]);
        cal.set(Calendar.DAY_OF_MONTH, cal
                .getActualMaximum(Calendar.DAY_OF_MONTH)); // get the last day
                                                            // of the month set

        return new Date(cal.getTime().getTime());
    }
}


package com.manulife.pension.ps.web.taglib.resources;

/*
  File: FormsTableTag.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-03-26   Chris Shin       Initial version.
  CS1.1     2004-06-09   Chris Shin       Add logic for forms that have special criteria
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.content.view.ContentFile;
import com.manulife.pension.content.view.MutableForm;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.Constants.Cofid338InvestmentOptionProfile;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.ContractConstants.ContractStatus;
import com.manulife.pension.util.log.LogUtility;

/**
 * This class is the tag that will build the forms table
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 26, 2004)
 **/

public class FormsTableTag extends TagSupport {

	private String formsName;
	private String layoutPage;

	private Logger logger = Logger.getLogger(getClass());	
	private static final String FUND_SERIES_CODE_NA = "NA";
	private Environment env = Environment.getInstance();
	private Map<String, CustomizedFormLinkGenerator> customizedFormLinks = new HashMap<String, FormsTableTag.CustomizedFormLinkGenerator>();
	private Map<String, Cofid338FormLinkGenerator> customizedCofid338FormLinks = new HashMap<String, FormsTableTag.Cofid338FormLinkGenerator>();
	
	private static String CUSTOMIZED_GIFL_ELECTION_FORM = "/assets/pdfs/gifl_election_form.ext";
	private static String CUSTOMIZED_IAT_FORM = "/assets/pdfs/custom_iat.ext";
	private static String CUSTOMIZED_COFID338_ADVANCED_FORM = "/assets/pdfs/Wilshire_338_Advanced.ext";
	private static String CUSTOMIZED_COFID338_BALANCED_FORM = "/assets/pdfs/Wilshire_338_Balanced.ext";
	private static String CUSTOMIZED_COFID338_ADVANTAGE_FORM = "/assets/pdfs/Wilshire_338_Defensive.ext";
	private static String CUSTOMIZED_COFID338_FEE_SENSTIVE_FORM = "/assets/pdfs/Wilshire_338_Fee.ext";
	
	private static Set<String> contractStatusLimittedForm = new HashSet<String>();
	private static Set<String> contractStatusAvailableSet = new HashSet<String>();
	
	static {
		contractStatusLimittedForm.add(CUSTOMIZED_GIFL_ELECTION_FORM);
		contractStatusLimittedForm.add(CUSTOMIZED_IAT_FORM);
		contractStatusAvailableSet.add(ContractStatus.ACTIVE);
		contractStatusAvailableSet.add(ContractStatus.FROZEN);
		contractStatusAvailableSet.add(ContractStatus.APPROVED);
		
	}
	public void setCollection(String value){
		this.formsName = value;
	}

	public String getCollection(){
		return this.formsName;
	}

	public void setLayoutPage(String value){
		this.layoutPage = value;
	}

	public String getLayoutPage(){
		return this.layoutPage;
	}

	
/**
  *doStartTag is called by the JSP container when the tag is encountered
*/
	public int doStartTag()throws JspException {

		LayoutPage layoutPage = null;

		initCustomizedFormLinks();
		
		try {

			Collection formlist = (Collection)(pageContext.getAttribute(this.formsName,PageContext.REQUEST_SCOPE));
			UserProfile userProfile = SessionHelper.getUserProfile((HttpServletRequest)pageContext.getRequest());
			boolean isDefinedBenefitContract = userProfile.getCurrentContract().isDefinedBenefitContract();

			if (!formlist.isEmpty() && userProfile!=null) {

				// the layout page is required for the section titles
			 	Object bean = TagUtils.getInstance().lookup(pageContext, getLayoutPage(), null);
				if (bean != null && bean instanceof LayoutPage) {
					layoutPage = (LayoutPage) bean;
				}

				if (layoutPage == null) {
					throw new IOException();
				}

				Iterator iter = formlist.iterator();

				boolean contractAllowsPBA = userProfile.getCurrentContract().isPBA();
				boolean contractAllowsLoans = userProfile.getCurrentContract().isLoanFeature();
				boolean contractIsNML = userProfile.getCurrentContract().isNml();
				boolean contractIsML = userProfile.getCurrentContract().isMerrillLynch();
				String contractDistChannel = StringUtils.trimToEmpty(userProfile.getCurrentContract().getDistributionChannel());
				if(contractIsML){
					contractDistChannel = "ML";
				}
				//Required to validate the form is GIFL related or not
				boolean hasGatewayIndicator = userProfile.getCurrentContract().getHasContractGatewayInd();
				String giflVersionNo = userProfile.getCurrentContract().getGiflVersion();
				String contractFundSeriesCode = userProfile.getCurrentContract().getFundPackageSeriesCode();
				String contractStatusCode = userProfile.getCurrentContract().getStatus();
				boolean isBundledGaIndicator = userProfile.getCurrentContract().isBundledGaIndicator();
				String productFeatureTypeCode = userProfile.getCurrentContract().getProductFeatureTypeCode();
				boolean installmentsInd = userProfile.getCurrentContract().isInstallmentsInd();
				String contractIssuedStateCode = userProfile.getCurrentContract().getContractIssuedStateCode();
						
				//LS. May 2006 , multiple class phase1
				//3.4.	Appendix - PDF Forms Available For Display on the Plan Sponsor Website
				//Broker Dealer Hybrid (ARABD/ARABDY Hybrid) can potentially use different forms than ARA92 Hybrid.
				String productId = userProfile.getCurrentContract().getProductId();
				if (Constants.FUND_PACKAGE_HYBRID.equals(contractFundSeriesCode) && (Constants.BD_PRODUCT_ID.equals(productId)||Constants.BD_PRODUCT_NY_ID.equals(productId))){
					contractFundSeriesCode =Constants.FUND_PACKAGE_BROKER_HYBRID;
				}
				
                LinkedHashMap<String, ArrayList<MutableForm>> formsMap=new LinkedHashMap<String, ArrayList<MutableForm>>();
                
				while (iter.hasNext()) {
					MutableForm formContent = (MutableForm) iter.next();

					if ((formContent.getFormName()!= null) && (formContent.getFormName().trim().length()>0)) {
						// If BGA Suppress value is 
						//i) Yes - suppress the form for external users if a bundled contract is selected
						//ii) No - show the form for external users if a bundled contract is selected
						
						if(!(isBundledGaIndicator && "Yes".equals(formContent.getBgaSuppressContext()) && userProfile.getRole().isExternalUser())){
							
							if (shouldDisplayForm(formContent, contractAllowsPBA, contractAllowsLoans, contractIsNML, contractIsML, contractDistChannel,
									contractFundSeriesCode, isDefinedBenefitContract,hasGatewayIndicator,giflVersionNo, contractStatusCode, isBundledGaIndicator, productFeatureTypeCode, installmentsInd, contractIssuedStateCode)) {
								ArrayList<MutableForm> formsList = formsMap.get(StringUtils.trimToEmpty(formContent.getCategory()));
								if(formsList==null){
									formsList=new ArrayList<MutableForm>();
									formsMap.put(StringUtils.trimToEmpty(formContent.getCategory()),formsList);
								}
								formsList.add(formContent);
							}
						}
					}
				}

				try {
					generateTable(formsMap, layoutPage);
				} catch (ContentException e) {
					 logger.error(e.getMessage(), e);
				}	
				
			}


		} catch (IOException ex){
			SystemException se = new SystemException(ex,"com.manulife.pension.ps.web.taglib.resources.FormTableTag", "doStartTag", "Exception when building the table: " + ex.toString() );
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw new JspException(se.getMessage());
		} catch (SystemException ex) {
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,ex);
			throw new JspException(ex.getMessage());			
		}

		return SKIP_BODY;

	}

	private boolean shouldDisplayForm(MutableForm formContent, boolean contractAllowsPBA,
			boolean contractAllowsLoans, boolean contractIsNML, boolean contractIsML, String contractDistChannel, String contractFundSeriesCode,
            boolean isDefinedBenefitContract,boolean hasGatewayIndicator,
			String giflVersionNo, String contractStatusCode, boolean isBundledGaIndicator, String productFeatureTypeCode , boolean installmentsInd, String contractIssuedStateCode) {

		boolean displayForm = false;

		if (formContent.getFundPackageRelated() == null ||
			formContent.getFundPackageRelated().equals(FUND_SERIES_CODE_NA) ||
			formContent.getFundPackageRelated().trim().length() == 0) {

			/*
			 * if the contract does not allow PBA, then the form must not be related to PBA
 			 * if the contract does not allow loans, then the form must not be related to loans
 			 */
			if ((!contractAllowsPBA && formContent.getPBARelated().equals("Yes")) ||
                  (!contractAllowsLoans && formContent.getLoanRelated().equals("Yes")) ||
                  //* DB product related are applicable only to Defined Benefit contracts 
                  ( isDefinedBenefitContract && formContent.getProductRelated().equals("DC")) ||
                  ( !isDefinedBenefitContract && formContent.getProductRelated().equals("DB")) ) 
            {
				displayForm=false;
			} else {
				displayForm=true;
			}
		} else {

			/*
			 * Currently only for investment selection and investment change forms
			 */

			//System.out.println("contract " + contractFundSeriesCode);
			//System.out.println("contractPBA " + contractAllowsPBA);
			//System.out.println("contractNML " + contractIsNML);
			
			//System.out.println("fundPackage " + formContent.getFundPackageRelated());
			//System.out.println("PBA " + formContent.getPBARelated());
			//System.out.println("dist " + formContent.getDistributionChannelRelated());
			
			ArrayList<String> formDistChannel = new ArrayList(Arrays.asList(formContent.getDistributionChannelRelated().split(",")));
			if (contractFundSeriesCode.equals(formContent.getFundPackageRelated()) &&
			        (formContent.getPBARelated().equals("No") ||
			                (formContent.getPBARelated().equals("Yes") && contractAllowsPBA)) &&
					(formContent.getLoanRelated().equals("No") ||
					        (formContent.getLoanRelated().equals("Yes") && contractAllowsLoans)) &&
					(formDistChannel.contains(contractDistChannel) || formDistChannel.contains("NA")) && 
	                //* DB product related are applicable only to Defined Benefit contracts 
	                ( isDefinedBenefitContract && formContent.getProductRelated().equals("DB") ||
	                ( !isDefinedBenefitContract && formContent.getProductRelated().equals("DC"))) ) 
	            {
					displayForm=true;
				} else {
					displayForm=false;
				}
		}
		

		// If Gifl version which is associated with FORM PDF links equals to the contract GIFL version, then that form content will be displayed  	
		if(displayForm){
			displayForm = false;
			if(Constants.GIFL_RELATED_BOTH.equals(formContent.getGiflRelated()) || formContent.getGiflRelated()== null || StringUtils.isEmpty(formContent.getGiflRelated())){
				displayForm = true;
			} else if(Constants.GIFL_RELATED_NON_GIFL_ONLY.equals(formContent.getGiflRelated()) && !hasGatewayIndicator){
				displayForm = true;
			} else if(Constants.GIFL_RELATED_GIFL_ONLY.equals(formContent.getGiflRelated()) && hasGatewayIndicator){
					if( (Constants.GIFL_VERSION_01.equals(formContent.getGiflVersion01()) && Constants.GIFL_VERSION_01.equals(giflVersionNo))
						||(Constants.GIFL_VERSION_02.equals(formContent.getGiflVersion02()) && Constants.GIFL_VERSION_02.equals(giflVersionNo)
							||(Constants.GIFL_VERSION_03.equals(formContent.getGiflVersion03()) && Constants.GIFL_VERSION_03.equals(giflVersionNo))))
				displayForm = true;
			}
		}
		if(displayForm){
			displayForm = false;
			if(Constants.BGA_RELATED_BOTH.equals(formContent.getBgaRelated()) || formContent.getBgaRelated()== null || StringUtils.isEmpty(formContent.getBgaRelated())){
				displayForm = true;
			} else if(Constants.BGA_NON_BUNDLED_ONLY.equals(formContent.getBgaRelated()) && !isBundledGaIndicator){
				displayForm = true;
			} else if(Constants.BGA_BUNDLED_ONLY.equals(formContent.getBgaRelated()) && isBundledGaIndicator){
				displayForm = true;
			}
		}
		
		if (displayForm) {
			String location = Environment.getInstance().getSiteLocation();
			ContentFile pdfForm = null;
			if (StringUtils.equals(location, Constants.SITEMODE_USA)) {
				pdfForm = formContent.getEnglishPDFForm();
			} else {
				pdfForm = formContent.getNyEnglishPDFForm();
			}
			if (pdfForm != null) {
				if (contractStatusLimittedForm.contains(pdfForm.getPath())
						&& !contractStatusAvailableSet
								.contains(contractStatusCode)) {
					displayForm = false;
				}
			}
		}
		
		if (displayForm) {
			if (StringUtils.isBlank(formContent.getWithdrawalsRelated())) {
				displayForm = true;
			} else {
				if ("Systematic Withdrawals".equals(formContent.getWithdrawalsRelated())) {
					if ("SYW".equals(productFeatureTypeCode)) {
						displayForm = true;
					} else {
						displayForm = false;
					}
				} else if ("Installment Withdrawals".equals(formContent.getWithdrawalsRelated())) {
					if (installmentsInd && "SYW".equals(productFeatureTypeCode)) {
						displayForm = true;
					} else {
						displayForm = false;
					}
				} else {
					displayForm = false;
				}
			}
		}
		
		if(displayForm){
			if (StringUtils.isBlank(formContent.getPRStateRestriction()) || 
					"".equals(formContent.getPRStateRestriction())) {
				displayForm = true;
			}else{
				if(formContent.getPRStateRestriction().equals("IN")){
					//check PR state, if contract is PR displayform true else false
					if("PR".equals(contractIssuedStateCode)){
						displayForm = true;
					}else{
						displayForm = false;
					}
				}else if(formContent.getPRStateRestriction().equals("EX")){
					//check not PR state, if contract is not PR displayform true else false
					if(!"PR".equals(contractIssuedStateCode)){
						displayForm = true;
					}else{
						displayForm = false;
					}
				}else{
					displayForm = false;
				}
			}
		}
		return displayForm;
	}
	

	/* Common log CL135896
	 * generate the sections for both  participant and employer plan sponsor forms
	 *
	 */
	
	private void generateTable(LinkedHashMap<String, ArrayList<MutableForm>> formsMap,
			LayoutPage layoutPage) throws IOException, SystemException, ContentException	{
		
		JspWriter out = pageContext.getOut();
		
		String s = new String("FORM_CATEGORY");
		HashMap<String, String> formCategoryMap = new LinkedHashMap<String, String>();
 		try {
			formCategoryMap = (BrowseServiceDelegate.getInstance()).getFormCategory(s);
			
		} catch (ContentException e) {
			 logger.error(e.getMessage(), e);
		}
 		
 		
		int count=1;
		for (Map.Entry<String, String> entry : formCategoryMap.entrySet()) {


			ArrayList<MutableForm> formsForThisCategory = formsMap.get(entry.getKey());
			
			/*This is nothing but the LOOK_DESC (Form Category) value from lookup table. 
			If you are looking for any category in the Forms page, make sure that you are having an entry in CMA100.LOOKUP table.*/
			String sectionHeader = entry.getValue(); 
			/*
			 * generate the participant form head section.
			 */
			//TODO : we should enable the sectionHeder condition, once all the forms are placed into some specific category.
			if(formsForThisCategory!=null && !formsForThisCategory.isEmpty() /*&& sectionHeader != null*/){
				out.println("<div id=\"accordion\">");

				out.println("<div class=\"accordion-toggle\">");

				out.println("<table width=\"720\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");

				out.println("<tr height=\"30\">");

				if(count==1){
					out.println("<td class=\"tableheadTD\"  width=\"720\" >");
				}else{
					out.println("<td  class=\"tableheadTD\" width=\"720\" >");
				}

				out.println("<a>");

				out.println("<img class=\"plus_icon1\" src=\"/assets/unmanaged/images/plus_icon.gif\">");
				out.println("<img class=\"minus_icon1\" src=\"/assets/unmanaged/images/minus_icon.gif\"></a> ");
				out.println("&nbsp;<b>");
				out.println(sectionHeader);
				out.println("</b>");
				out.println("</td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div>");


				/*generating participant form body content*/

				out.println("<div class=\"accordion-content\">");

				out.println("<table width=\"720\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
				out.println("<tr>");
				out.println("<td width=\"0\"></td>");
				out.println("<td width=\"20\"></td>");
				out.println("<td width=\"1\"></td>");
				out.println("<td></td>");
				out.println("<td width=\"1\"></td>");
				out.println("</tr>");

				boolean isShade = false;

				if (!formsMap.isEmpty()) {
					isShade = populateDetails(formsForThisCategory, isShade, out, entry.getKey());
				}

				out.println("</table >");

				out.println("</div>");


				/*Break Layer for two accordion sections.*/

				out.println("<div>");
				out.println("<table width=\"720\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"></tbody>");
				out.println("<tr><td>");
				out.println("<img src=\"/assets/unmanaged/images/spacer.gif\" width=\"0\" height=\"1\" border=\"0\">");
				out.println("</td></tr>");
				out.println("</tbody></table>");
				out.println("</div>");

				count++;
			}
		}
	}

	private boolean populateDetails(List<MutableForm> formList, boolean isShade, JspWriter out, String section) throws IOException, SystemException {

		Iterator<MutableForm> iter = formList.iterator();

		String loc = getSite();
		String location = Environment.getInstance().getSiteLocation();

		while (iter.hasNext()) {

			MutableForm formContent = (MutableForm) iter.next();

			if (formContent.getFormName()!= null && formContent.getFormName().length() > 0) {

				// common log 50322
				if (hasPDFForms(location, formContent)) {

					if (isShade) {
						out.println("<tr class=\"datacell2\">");
						isShade = false;
					} else {
						out.println("<tr class=\"datacell1\">");
						isShade = true;
					}

					out.println(getFormattedRow("databorder","1","1"));

					//generate acrobat image and link
					StringBuilder buf = new StringBuilder();
					buf.append("<td class=\"tdContent\" align=\"center\"></td>");
					
					out.println(buf.toString());

					out.println("<td><img src=\"/assets/unmanaged/images/s.gif\" height=\"1\"></td>");

					//generate second column title + description
					
					String englishForm = null;//Spanish Forms are no longer valid. Hence we have removed the relevant code as part of this common log #135896

					if (location.equals(Constants.SITEMODE_USA)) {
						if (formContent.getEnglishPDFForm() != null) {
							englishForm = formContent.getEnglishPDFForm().getPath();
						}
					} else {
						if (formContent.getNyEnglishPDFForm() != null) {
							englishForm = formContent.getNyEnglishPDFForm().getPath();
						}
					}
					buf = new StringBuilder();
					buf.append("<td class=\"tdContent\" align=\"left\" valign=\"top\""
							+ "cmaId = "+formContent.getKey() + ">");
					buf.append(getFormattedPDFLink(englishForm,loc,null,formContent.getFormNumber()));
					buf.append(formContent.getFormName());

					buf.append("</a> (");
					buf.append(formContent.getFormNumber());
					buf.append(")<br>");
					buf.append(formContent.getDescription());
					buf.append("</td>");
					out.println(buf.toString());

					out.println("<td  width=\"0\"><img src=\"/assets/unmanaged/images/s.gif\" height=\"1\" ></td>");
					out.println(getFormattedRow("databorder","1","1"));
					out.println("</tr>");

				}
			}
		}
		out.println("<tr><td class=\"databorder\" colspan=\"7\" width=\"1\" height=\"1\"></td></tr>");

		return isShade;
	}

	private boolean hasPDFForms(String location, MutableForm formContent ) {
		
		boolean hasEnglishPDFForm = false;
		
        if (location.equals(Constants.SITEMODE_USA)) {
        	if (formContent.getEnglishPDFForm() != null) {
        		hasEnglishPDFForm = true;
        	}
        } else {
        	if (formContent.getNyEnglishPDFForm() != null) {
        		hasEnglishPDFForm = true;
        	}
        }
        return (hasEnglishPDFForm ? true : false);
	}
	
	/*
	 * This method will generate the <td></td> row depending on the width and height
	 */
	 private String getFormattedRow(String classStyle, String cellWidth, String cellHeight) {

		StringBuffer buff = new StringBuffer();

		buff.append("<td ");

		if (classStyle != null) {
			buff.append("class=\"");
			buff.append(classStyle);
			buff.append("\" ");
		}

		buff.append(" width=\"");
		buff.append(cellWidth);
		buff.append("\"><img src=\"/assets/unmanaged/images/s.gif\" height=\"");
		buff.append(cellHeight);
		buff.append("\"></td>");

		return buff.toString();
	}

	/*
	 * This method is required to get the domain and protocol as required by the FDF framework
	 */
	private String getSite() {

		StringBuilder loc = new StringBuilder();
		loc.append(env.getSiteProtocol());
		loc.append("://");
		loc.append(env.getSiteDomain());

		return loc.toString();
	}

	private String getFormattedPDFLink(String PDFPath, String loc,
			Miscellaneous adobe, String formNumber) throws SystemException {

		StringBuilder link = new StringBuilder();

		CustomizedFormLinkGenerator customziedForm = customizedCofid338FormLinks
				.get(PDFPath);
		if (customziedForm != null) {
			Cofid338FormLinkGenerator customizedCofidForm = (Cofid338FormLinkGenerator) customziedForm;
			customizedCofidForm.setFormNumber(formNumber);
			link.append(StringEscapeUtils.unescapeJavaScript(customziedForm
					.getFormLink()));

		} else {

			customziedForm = customizedFormLinks.get(StringUtils
					.lowerCase(PDFPath));

			if (customziedForm == null) {
				link.append("<a href=\"javascript:");
				link.append("openFormPDF(\'");
				link.append(PDFPath);
			}

			else {
				link.append("<a href=\"javascript:FormPDFWindow(\'");
				link.append(StringEscapeUtils.unescapeJavaScript(customziedForm
						.getFormLink()));

			}
		}

		link.append("\'); \" onMouseOver='self.status=\"Go to the PDF\"; return true'>");

		if (adobe != null) {
			link.append("<img src=\"");
			link.append(adobe.getImage().getPath());
			link.append("\" border=\"0\">");
		}

		return link.toString();
	}

	private String getStringValue(String text) {

		if (text== null) {
			return "";
		} else {
			return text;
		}
	}

	static interface CustomizedFormLinkGenerator {
		String getFormLink() throws SystemException;		
	}
	
	private void initCustomizedFormLinks() {
		customizedFormLinks.put(CUSTOMIZED_GIFL_ELECTION_FORM, new GIFLParticipantElectionChangeFormLinkGenerator());
		customizedFormLinks.put(CUSTOMIZED_IAT_FORM, new CustomizedIATFormLinkGeneator());
		customizedCofid338FormLinks.put(CUSTOMIZED_COFID338_ADVANCED_FORM,  new Cofid338FormLinkGenerator(Cofid338InvestmentOptionProfile.ADVANCED.getCode()));
		customizedCofid338FormLinks.put(CUSTOMIZED_COFID338_BALANCED_FORM,  new Cofid338FormLinkGenerator(Cofid338InvestmentOptionProfile.BALANCED.getCode()));
		customizedCofid338FormLinks.put(CUSTOMIZED_COFID338_ADVANTAGE_FORM,  new Cofid338FormLinkGenerator(Cofid338InvestmentOptionProfile.ADVANTAGE.getCode()));
		customizedCofid338FormLinks.put(CUSTOMIZED_COFID338_FEE_SENSTIVE_FORM,  new Cofid338FormLinkGenerator(Cofid338InvestmentOptionProfile.FEE_SENSTIVE.getCode()));
	}
	
	abstract class SharpPointFormLinkGenerator implements CustomizedFormLinkGenerator {
		private static final String sharpPointDocumentServiceURL = "sharppointDocumentService.url";

		protected String getContractAccessNumber() throws SystemException {
			return ContractServiceDelegate.getInstance().getContractEnrollmentAccessNumber(getContractNumber());
		}
		
		protected int getContractNumber() {
			UserProfile userProfile = SessionHelper.getUserProfile((HttpServletRequest)pageContext.getRequest());
			return userProfile.getCurrentContract().getContractNumber();
		}
		
		protected boolean hasPBA() {
			UserProfile userProfile = SessionHelper.getUserProfile((HttpServletRequest)pageContext.getRequest());
			return userProfile.getCurrentContract().isPBA();
		}
		protected String getSharpPointDocumentServiceURL() {
			return env.getNamingVariable(sharpPointDocumentServiceURL, null);
		}
	}
	
	class GIFLParticipantElectionChangeFormLinkGenerator extends SharpPointFormLinkGenerator {

		public String getFormLink() throws SystemException {
			return getSharpPointDocumentServiceURL() + "GIFL/"
					+ getContractNumber() + "?IAN=" + getContractAccessNumber()
					+ "&PBA=" + hasPBA();
		}
	}

	class Cofid338FormLinkGenerator implements CustomizedFormLinkGenerator {
		
		private String formType;
		private String formNumber;

		public String getFormType() {
			return formType;
		}
		
		public void setFormNumber(String formNumber) {
			this.formNumber = formNumber;
		}
		
		public String getFormNumber() {
			return formNumber;
		}

		Cofid338FormLinkGenerator(String formType) {
			this.formType = formType;
		}

		@Override
		public String getFormLink() throws SystemException {
			StringBuilder link = new StringBuilder();

			link.append("<a href=\"javascript:downloadCoFid338InvestmentPDF('"
					+ formType + "',' "+getFormNumber()+"");
			

			return link.toString();
		}
	}
	
	class CustomizedIATFormLinkGeneator extends SharpPointFormLinkGenerator {

		public String getFormLink() throws SystemException {
			return getSharpPointDocumentServiceURL() + "IAT/"
					+ getContractNumber() + "?IAN=" + getContractAccessNumber()
					+ "&PBA=" + hasPBA();
		}		
	}
}


<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

String SITEMODE_USA=Constants.SITEMODE_USA;
String SITEMODE_NY=Constants.SITEMODE_NY;
String FUND_PACKAGE_RETAIL=Constants.FUND_PACKAGE_RETAIL;
String FUND_PACKAGE_BROKER=Constants.FUND_PACKAGE_BROKER;
String FUND_PACKAGE_HYBRID=Constants.FUND_PACKAGE_HYBRID;
String FUND_PACKAGE_VENTURE = Constants.FUND_PACKAGE_VENTURE;
String FUND_PACKAGE_MULTICLASS =Constants.FUND_PACKAGE_MULTICLASS;

String EARLY_REDEMPTION_RETAIL_USPDF_URL=Constants.EARLY_REDEMPTION_RETAIL_USPDF_URL;
String EARLY_REDEMPTION_BROKER_USPDF_URL=Constants.EARLY_REDEMPTION_BROKER_USPDF_URL;
String EARLY_REDEMPTION_HYBRID_USPDF_URL=Constants.EARLY_REDEMPTION_HYBRID_USPDF_URL;
String EARLY_REDEMPTION_VENTURE_USPDF_URL = Constants.EARLY_REDEMPTION_VENTURE_USPDF_URL;
String EARLY_REDEMPTION_RETAILNML_USPDF_URL=Constants.EARLY_REDEMPTION_RETAILNML_USPDF_URL;
String EARLY_REDEMPTION_HYBRIDNML_USPDF_URL= Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL;

String EARLY_REDEMPTION_BROKER_NYPDF_URL=Constants.EARLY_REDEMPTION_BROKER_NYPDF_URL;
String EARLY_REDEMPTION_HYBRID_NYPDF_URL=Constants.EARLY_REDEMPTION_HYBRID_NYPDF_URL;
String EARLY_REDEMPTION_RETAIL_NYPDF_URL=Constants.EARLY_REDEMPTION_RETAIL_NYPDF_URL;
String EARLY_REDEMPTION_VENTURE_NYPDF_URL = Constants.EARLY_REDEMPTION_VENTURE_NYPDF_URL;
String EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL=Constants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL;
String EARLY_REDEMPTION_RETAILNML_NYPDF_URL= Constants.EARLY_REDEMPTION_RETAILNML_NYPDF_URL;

pageContext.setAttribute("SITEMODE_USA",SITEMODE_USA,PageContext.PAGE_SCOPE);
pageContext.setAttribute("SITEMODE_NY",SITEMODE_NY,PageContext.PAGE_SCOPE);
pageContext.setAttribute("FUND_PACKAGE_RETAIL",FUND_PACKAGE_RETAIL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("FUND_PACKAGE_BROKER",FUND_PACKAGE_BROKER,PageContext.PAGE_SCOPE);
pageContext.setAttribute("FUND_PACKAGE_HYBRID",FUND_PACKAGE_HYBRID,PageContext.PAGE_SCOPE);
pageContext.setAttribute("FUND_PACKAGE_VENTURE",FUND_PACKAGE_VENTURE,PageContext.PAGE_SCOPE);
pageContext.setAttribute("FUND_PACKAGE_MULTICLASS",FUND_PACKAGE_MULTICLASS,PageContext.PAGE_SCOPE);

pageContext.setAttribute("EARLY_REDEMPTION_RETAIL_USPDF_URL",EARLY_REDEMPTION_RETAIL_USPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_BROKER_USPDF_URL",EARLY_REDEMPTION_BROKER_USPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_HYBRID_USPDF_URL",EARLY_REDEMPTION_HYBRID_USPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_VENTURE_USPDF_URL",EARLY_REDEMPTION_VENTURE_USPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_RETAILNML_USPDF_URL",EARLY_REDEMPTION_RETAILNML_USPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_HYBRIDNML_USPDF_URL",EARLY_REDEMPTION_HYBRIDNML_USPDF_URL,PageContext.PAGE_SCOPE);

pageContext.setAttribute("EARLY_REDEMPTION_BROKER_NYPDF_URL",EARLY_REDEMPTION_BROKER_NYPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_HYBRID_NYPDF_URL",EARLY_REDEMPTION_HYBRID_NYPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_RETAIL_NYPDF_URL",EARLY_REDEMPTION_RETAIL_NYPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_VENTURE_NYPDF_URL",EARLY_REDEMPTION_VENTURE_NYPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL",EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL,PageContext.PAGE_SCOPE);
pageContext.setAttribute("EARLY_REDEMPTION_RETAILNML_NYPDF_URL",EARLY_REDEMPTION_RETAILNML_NYPDF_URL,PageContext.PAGE_SCOPE); 
%>
<mrtl:noCaching/> 
<%-- This jsp includes the following CMA content --%>
<% 
	pageContext.setAttribute("isMerrillContract", userProfile.isMerrillLynchContract());
%>
<content:contentBean contentId="<%=ContentConstants.PERFORMANCE_DISCLOSURE_TEXT%>"
                     type="<%=ContentTypeManager.instance().DISCLAIMER%>"
                     id="performanceDisclosure"/>
                     
<content:contentBean contentId="<%=ContentConstants.FEE_WAIVER_DISCLOSURE_TEXT%>"
                     type="<%=ContentTypeManager.instance().DISCLAIMER%>"
                     id="feeWaiverDisclosure"/>                    
                                      
<content:contentBean contentId="<%=ContentConstants.MERRILL_RESRICTED_FUNDS_CONTENT%>"
                     type="<%=ContentTypeManager.instance().DISCLAIMER%>"
                     id="restrictedFundsDisclosure"/>                                         
                     
                        
<script type="text/javascript" >

        function go(viewBy){
                var url="";
                if (viewBy.selectedIndex == 0) {
                        location.href = "?&sortByType=0&assetRiskOrderBy=0";
                } else {
                        location.href = "?&sortByType=0&assetRiskOrderBy=1";
                }
        }

        function doView(view){

                document.contractFundsForm.selectedView.value = view;
                document.contractFundsForm.submit();

        }
</script>


  <td>
    <table width=100% cellspacing="0" cellpadding=0  border=0>
         
        <td width="5%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

            <td width="91%" valign="top" align="left">
            	<!-- Performace Disclosure -->
				<content:getAttribute id="performanceDisclosure" attribute="text" />
				<p>
				    <content:getAttribute id="feeWaiverDisclosure" attribute="text" />
				</p>
				<c:if test = "${isMerrillContract}">
				<p>
				    <content:getAttribute id="restrictedFundsDisclosure" attribute="text" />
				</p>
				</c:if>
				
                <c:if test="${empty param.printFriendly }" >
                    <table width=100% cellspacing="0" cellpadding="0" border="0">
                          <tr>
                        <td width="52%" valign="top">
                                        <br><strong>Shown</strong>:
<c:if test="${contractFundsForm.selectedView =='selected'}">
                                     Selected investment options<br>
                                          <a href="javascript:doView('available');">View all available investment options</a></p>
</c:if>
<c:if test="${contractFundsForm.selectedView !='selected'}">
                                     All available investment options<br>
                                        <a href="javascript:doView('selected');">View selected investment options</a></p>
</c:if>
                              </td>
                                   <td width="3%"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
                              <td width="45%" align="right">


<c:if test="${environment.siteLocation == SITEMODE_USA}">
<c:if test="${userProfile.currentContract.contractAllocated == true}">
<c:if test="${userProfile.currentContract.nml == false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
			                        	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_RETAIL_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_HYBRID}">
		                            	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRID_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
		                            	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_BROKER_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
		                            	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_VENTURE_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
		                            	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRID_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.nml == true}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_RETAILNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_HYBRID}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_VENTURE_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
			                                <a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.contractAllocated !=true}">
	                        	<a href="javascript:openPDF('${EARLY_REDEMPTION_HYBRIDNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>

<c:if test="${envionment.siteLocation == SITEMODE_NY}">
<c:if test="${userProfile.currentContract.contractAllocated == true}">
<c:if test="${userProfile.currentContract.nml == false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_RETAIL_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_HYBRID}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRID_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_BROKER_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_VENTURE_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRID_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.nml ==true}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_RETAILNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_HYBRID}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_VENTURE_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.contractAllocated !=true}">
                              	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
					
                                </td>
                      </tr>
                      
                        </table>
              
  </c:if>

                <ps:form name="contractFundsForm" modelAttribute="contractFundsForm" method="GET" action="/do/investment/contractFundsReport/" >
                         <input type="hidden" name="selectedView"/>


<c:if test="${not empty param.printFriendly }" >
                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                          <td width="72%" valign="top" >
                                        <br>
                                        <strong>Shown</strong>:
<c:if test="${contractFundsForm.selectedView =='selected'}">
                            Selected investment options<br>
</c:if>
<c:if test="${contractFundsForm.selectedView !='selected'}">
                                          All available investment options<br>
</c:if>
                                  </td>
                          <td width="3%"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
                          <td width="25%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
                  </tr>
                       </table>
</c:if>

         </td>
        <td width="4%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
  </tr>

  <tr>
        <td colspan="3">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr class="tablehead">
                                  <td class="tableheadTD1" width="65%">
                                        <b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> </b>as of <render:date property="contractFundsForm.asOfDate"  patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" />
                                 </td>
                                 <td class="tableheadTDinfo"><img src="/assets/unmanaged/images/s.gif" width="1" height="4">

                                        Option <ps:select name="contractFundsForm" property="selectedViewBy" onchange="go(this);">
                                                <ps:option value="0">View by Investment Category</ps:option>
                                                <ps:option value="1">View by Asset Class</ps:option>
                                    </ps:select>
                                </td>

                         <td width="0%" align="right" class="tableheadTDinfo"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                          </tr>
                </table>

        <!-- Investment Table-->

        <!-- Show errors using struts -->
        <content:errors scope="request" />
        <!-- // end of errors -->

<c:if test="${contractFundsForm.selectedView =='selected'}">

                <ps:fundTable beanName="investmentReport" productId ="<%=userProfile.getCurrentContract().getProductId()%>" fundSeries ="<%=userProfile.getCurrentContract().getFundPackageSeriesCode()%>"/>
</c:if>
<c:if test="${contractFundsForm.selectedView !='selected'}">

                <ps:fundTable beanName="investmentReport" highlight="true" productId = "<%=userProfile.getCurrentContract().getProductId()%>" fundSeries ="<%=userProfile.getCurrentContract().getFundPackageSeriesCode()%>"  />
</c:if>

<c:if test="${empty param.printFriendly }" >
        <p><content:pageFooter beanName="layoutPageBean"/></p>
</c:if>
        <p><content:pageFootnotes beanName="layoutPageBean"/></p>

<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
       	<content:contentBean contentId="<%=ContentConstants.DYNAMIC_FOOTNOTE_MC%>"
               type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>"
        id="classDisclosure"/>
	<p><content:getAttribute beanName="classDisclosure" attribute="text"/></p>


</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode != FUND_PACKAGE_MULTICLASS}">
       	<content:contentBean contentId="<%=ContentConstants.DYNAMIC_FOOTNOTE_INFORCED%>"
               type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>"
        id="inforced"/>
	<p><content:getAttribute beanName="inforced" attribute="text"/></p>


</c:if>


	<ps:fundFootnotes symbols="symbolsArray"/>
	
	<!-- Risks Applicable to All Funds - Disclosure -->
		<content:contentBean
			contentId="<%=ContentConstants.RISK_DISCLOSURES_BY_SITE%>"
			type="<%=ContentConstants.TYPE_DISCLAIMER%>"
			id="risksApplicabletoAllFundsDisclosure" />
		<p><content:getAttribute
			beanName="risksApplicabletoAllFundsDisclosure" attribute="text" /></p>

		<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>

         <br>
        <br>
        </td></tr></table></td>
        </ps:form>

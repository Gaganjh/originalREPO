<%--  Imports  --%>
<%@page import="com.manulife.pension.bd.web.bob.contract.ContractInformationReportHelper"%>
<%@page import="com.manulife.pension.bd.web.bob.contract.ContractIPSHelper"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%--  Tag Libraries  --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%-- Beans used --%>
<%
ContractInformationReportData theReport = (ContractInformationReportData)request.getAttribute(BDConstants.REPORT_BEAN);
  pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
  
  String TRUE=BDConstants.TRUE;
  pageContext.setAttribute("TRUE",TRUE,PageContext.PAGE_SCOPE);
  String SITEMODE_NY=BDConstants.SITEMODE_NY;
  pageContext.setAttribute("SITEMODE_NY",SITEMODE_NY,PageContext.PAGE_SCOPE);
  String SITEMODE_USA=BDConstants.SITEMODE_USA;
  pageContext.setAttribute("SITEMODE_USA",SITEMODE_USA,PageContext.PAGE_SCOPE);
  
  String GIFL_VERSION_03=BDConstants.GIFL_VERSION_03;
  pageContext.setAttribute("GIFL_VERSION_03",GIFL_VERSION_03,PageContext.PAGE_SCOPE);
  
  
  BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
  pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
  
  %>








<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO__PAGE_CONTRACT_OPTIONS_SECTION_TITLE%>"
                            type="<%=BDContentConstants.TYPE_MESSAGE%>"
                            id="contractOptionsSectionTitle"/>
<c:if test="${contractInformationForm.giflFeaturesContentId != 0}">
	<content:contentBean contentId="${contractInformationForm.giflFeaturesContentId}"
                            type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                            id="giflFeatures"/>
</c:if>

<script type="text/javascript">
<!--
/**
 * filename PDF file name
 */
function openFSWPDF(fileName) {
	var isMac = (navigator.userAgent.indexOf("Mac") != -1);
	 
	if (isMac) {
		path = location.protocol + "//" + location.host +  fileName;
	} else {
		path = location.protocol + "//" + location.host + fileName + "#FDF=" + location.protocol + "//" + location.host + "/do/bob/ContractFDFAction.fdf";
	}
	PDFWindow(path);		
}
//-->
</script>
<h4><content:getAttribute beanName="contractOptionsSectionTitle" attribute="text"/></h4>
<h5>&nbsp;</h5>
<h5>Contract Features</h5>
	<ul style="padding-left : 7px">
	<c:set var="isWilshireAvailable" value="false" />
<c:forEach items="${theReport.contractProfileVo.featuresAndServices.contractFeatures}" var="feature">
	 	<c:if test="${fn:length(fn:trim(feature)) >0}" >
		      <c:if test="${not(fn:contains(feature, 'Wilshire 3(21) Adviser Service') or fn:contains(feature, 'Wilshire 3(38) Investment Management Service'))}">
<li>${feature}</li>
		     </c:if>
		     <c:if test="${(fn:contains(feature, 'Wilshire 3(21) Adviser Service') or fn:contains(feature, 'Wilshire 3(38) Investment Management Service'))}">
<c:if test="${theReport.contractProfileVo.featuresAndServices.sendService.enabled == TRUE}">
			    				<li>SEND Service</li>
</c:if>
<li>${feature}</li>
		    	   <c:set var="isWilshireAvailable" value="true" />
	    	</c:if>
		</c:if>
</c:forEach>
	 
	   <c:if test="${not isWilshireAvailable}">
<c:if test="${theReport.contractProfileVo.featuresAndServices.sendService.enabled == TRUE}">
	    	<li>SEND Service</li>
</c:if>
	   </c:if>
	</ul>    

	<c:if test="${bobContext.contractProfile.contract.hasContractGatewayInd}">
<c:if test="${bobContext.contractProfile.contract.giflVersion ==GIFL_VERSION_03}">
			<h5><%=BDConstants.GIFL_SELECT_TITLE%></h5>
</c:if>
			
<c:if test="${bobContext.contractProfile.contract.giflVersion !=GIFL_VERSION_03}">
			<h5><%=BDConstants.GIFL_V1_V2_TITLE%></h5>
</c:if>
		<content:getAttribute beanName="giflFeatures" attribute="text">
		  <content:param><%=ContractInformationReportHelper.getGIFLFeePercentageDisplay(bobContext.getCurrentContract().getContractNumber())%></content:param>
		</content:getAttribute>
	</c:if>

<%-- Display Managed Account --%>
<c:if test="${not empty theReport.managedAccountServiceFeature}">
	<h5>Managed Accounts</h5>
	<ul style="padding-left : 7px">
		<li>${theReport.managedAccountServiceFeature}</li>
	</ul>
</c:if>
<h5>Investments</h5>

    <p>Number of Funds Available: 
    <report:number property="theReport.contractProfileVo.featuresAndServices.availableFundsNumber"
                  defaultValue="0" type="i" />
    </p>
    <p>Number of Funds Selected:
    <report:number property="theReport.contractProfileVo.featuresAndServices.selectedFundsNumber"
                  defaultValue="0" type="i" />
    </p>
    <br/>
    <p>Default Investment Option(s)</p>

<c:if test="${empty theReport.contractProfileVo.defaultInvestments}">
        <p>No default investment option currently selected. 
           Please contact your Client Account Representative 
           to change the default investment option for your contract.
        </p>
        <br/>
</c:if>
<c:if test="${not empty theReport.contractProfileVo.defaultInvestments}">
        <ul style="padding-left : 7px">
<c:forEach items="${theReport.contractProfileVo.defaultInvestments}" var="defaultInvestment">

               <li>
<c:if test="${defaultInvestment.lifeCycleFund ==true}">
${defaultInvestment.fundFamilyDisplayName}
</c:if>
<c:if test="${defaultInvestment.lifeCycleFund ==false}">
<td>${defaultInvestment.fundName}</td>
</c:if>
            <report:number property="defaultInvestment.percentage" type="p" pattern="##0.00" /> %
            </li>
</c:forEach>
        </ul>
</c:if>
    
<c:if test="${contractInformationForm.showFiduciaryWarranty == true}">

        <h5>Fiduciary Standards Warranty (FSW)</h5>
        <c:choose>
        <c:when test="${theReport.contractSummaryVo.fswStatus eq 'QUALIFIED'}">
	        <p>Yes</p>
        
<c:if test="${bobContext.contractSiteLocation ==SITEMODE_USA}">
			<p>
	           <a style="text-decoration: underline;font-weight: normal;color: #002c3d;font-size: 1em;outline-style: none;" href="javascript:openFSWPDF('/assets/pdfs/PS9613_fillable.pdf');" onMouseOver="self.status='Go to the PDF'; return true">View the Warranty Certificate</a>
			 </p>
</c:if>
<c:if test="${bobContext.contractSiteLocation ==SITEMODE_NY}">
			<p>
	           <a style="text-decoration: underline;font-weight: normal;color: #002c3d;font-size: 1em;outline-style: none;" href="javascript:openFSWPDF('/assets/pdfs/PS9614NY_fillable.pdf');" onMouseOver="self.status='Go to the PDF'; return true">View the Warranty Certificate</a>
			</p>
</c:if>
        </c:when>
        <c:when test="${not empty theReport.contractSummaryVo.failedAssetClasses}">
           <p>No</p>
           <p><a style="text-decoration: underline;font-weight: normal;color: #002c3d;font-size: 1em;outline-style: none;" href="#" onclick="openNBDW_FSWWindow()">View outstanding requirement(s)</a></p>
		   <input type="hidden" name="failedAssetClasses" id="contractSummary" value="${theReport.contractSummaryVo.failedAssetClasses}"/>
        </c:when>
        <c:otherwise>
           <p>Not eligible</p>
        </c:otherwise>
        </c:choose>
 	<br/>
</c:if>


<c:if test="${bobContext.currentContract.definedBenefitContract !=true}">
<c:if test="${not empty theReport.contractProfileVo.featuresAndServices.accessChannels}">
        <h5>Access Channels</h5>
        <ul style="padding-left : 7px">
<c:forEach items="${theReport.contractProfileVo.featuresAndServices.accessChannels}" var="accessChannel">

<li>${accessChannel}</li>
</c:forEach>
        </ul>
</c:if>
</c:if>

    <table class="overview_table_alt">
    <tbody>
        <tr>
            <th width="38%">&nbsp;&nbsp;Direct Debit Selected </th>
            <td width="62%">
                <render:yesno property="theReport.contractProfileVo.featuresAndServices.isDirectDebitSelected" />
            </td>
        </tr>
     </tbody>
     </table>
   

   





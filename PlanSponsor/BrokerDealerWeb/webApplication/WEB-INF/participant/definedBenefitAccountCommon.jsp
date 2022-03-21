<%-- Imports used --%>
<%@page import="com.manulife.pension.bd.web.BDErrorCodes"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.participant.ParticipantAccountForm" %>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO" %>

<%-- Tag Libraries used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>

<%-- Constant Files used--%>
<un:useConstants var="bdConstants" className="com.manulife.pension.bd.web.BDConstants" />
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants" className="com.manulife.pension.bd.web.content.BDContentConstants" />
<un:useConstants var="errorCodes" className="com.manulife.pension.bd.web.BDErrorCodes" />

<%-- Beans used --%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
	Integer noParticipant=BDErrorCodes.PARTICIPANT_ACCOUNT_NO_PARTICIPANTS;
	Integer moneyType=BDErrorCodes.PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE;
	pageContext.setAttribute("noParticipant",noParticipant,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("moneyType",moneyType,PageContext.PAGE_SCOPE);
%>

<jsp:useBean id="participantAccountForm" scope="session" type="com.manulife.pension.bd.web.bob.participant.ParticipantAccountForm" />

<c:set var="details" value="${details}" scope="page" />


<content:contentBean contentId="${contentConstants.MESSAGE_DB_PARTICIPANT_ACCOUNT_NO_PARTICIPANTS}"
        type="${contentConstants.TYPE_MESSAGE}"
        id="NoParticipantsMessage"/>

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_DB_PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE}"
        type="${contentConstants.TYPE_MESSAGE}"
        id="moneyTypeReportsNotAvailable"/>

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT}"
        type="${contentConstants.TYPE_MISCELLANEOUS}"
        id="csvIcon"/>

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT}"
        type="${contentConstants.TYPE_MISCELLANEOUS}"
        id="pdfIcon"/>

<script type="text/javascript">
      function doGoToContractDocuments() {
    	  document.getElementById('contractDocumentsLink').value='true';
    	  document.contractDocuments.submit();
      }
</script>


<%-- <form name="contractDocuments" method="post" action="/do/bob/contract/contractDocuments/" > --%>
<bd:form action="/do/bob/contract/contractDocuments/" method="post" modelAttribute="contractDocuments" name="contractDocuments">
      <input id="contractDocumentsLink" type="hidden" name="contractDocuments" value="false"/>
</bd:form>
<%-- </form> --%>

	<%-- 1. Page title and introduction text --%>
	<jsp:include page="/WEB-INF/global/displayContractInfoWithRothMessage.jsp"/>

	<%-- 2. Contract Navigation  --%>
	<navigation:contractReportsTab/>
	
	<%-- 3. Report Section --%>
	<div class="page_section_subheader controls">
		<%--  3.1 Report Title Bar  --%>
		<h3><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></h3>
		
		<%-- 3.2 Report As of Date --%>
		<form method="get" action="/bob/db/definedBenefitAccount/" class="page_section_filter form" >
			<p><label for="investment_allocation_filter">as of: </label></p>
<c:if test="${layoutBean.body =='/WEB-INF/participant/definedBenefitAccount.jsp'}">
				<bd:select property="selectedAsOfDate" name="participantAccountForm" onchange="setFilterFromSelect(this);doFilter();">
	  				<bd:dateOption 
						name="${bdConstants.BOBCONTEXT_KEY}"  
						property="currentContract.contractDates.asOfDate" 
						renderStyle="${renderConstants.MEDIUM_MDY_SLASHED}"/>
					
					<bd:dateOptions 
						name="${bdConstants.BOBCONTEXT_KEY}" 
						property="currentContract.contractDates.monthEndDates" 
						renderStyle="${renderConstants.MEDIUM_MDY_SLASHED}"/>
				</bd:select>
</c:if>
<c:if test="${layoutBean.body !='/WEB-INF/participant/definedBenefitAccount.jsp'}">
            	<p><label for="investment_allocation_filter">
            	   <render:date patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"
                				property="bobContext.currentContract.contractDates.asOfDate"/>
                   </label>
               </p>
</c:if>
		</form>
		
		<%--  3.3 PDF & CSV --%>
		<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
        <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>

	</div>
	<%--#page_section_subheader controls--%>
		
	<%--  4. Report Summary --%>
	<div id="page_section_container">
	
		<%--  4.1 Personal and other details --%>	
		<div class="page_section">
			<table class="overview_table" summary="Customer Account Information">
				<tbody>
					<%-- Name --%>
					<tr>
						<th>Name:</th>
						<td>
${details.firstName}&nbsp;
${details.lastName}
						</td>
					</tr>
					
					<%-- Total Assets --%>
					<tr>
						<th>Total Assets:</th>
						<td><report:number property="details.totalAssets" type="c"/></td>
					</tr>
					
					<%-- Allocated Assets --%>
					<tr>
						<th>Allocated Assets:</th>
						<td><report:number property="details.allocatedAssets" type="c"/></td>
					</tr>

					<%-- Last Contribution Date --%>	
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
					<tr>
						<th>Last Contribution Date:</th>
<c:if test="${empty details.lastContributionDate}">
                        <td>n/a</td>
</c:if>
<c:if test="${not empty details.lastContributionDate}">
						<td><render:date property="details.lastContributionDate" patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" /></td>
</c:if>
                     </tr>
</c:if>
				</tbody>
			</table>
		</div>
		<%--#page_section--%>
		
		<%--  4.2 Asset Allocation by Investment Category PieCharrt--%>	
		<div class="page_section">
			<h4><strong>Asset Allocation by Risk/Return Category</strong></h4>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
				<%-- 4.2.1 Render the Pie Chart --%>
				<tr>
              		<td valign="top">&nbsp;</td>
              		<td colspan="3" valign="top">
              			<div align="center">
              				<bd:pieChart beanName="pieChartBean" 
              				alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." 
              				title="Allocated Assets"/>
              			</div>
              		</td>
            	</tr>
            	
            	<%-- 4.2.2 Pie Chart Legends--%>
            	<%-- LifeCycle --%>
<c:if test="${participantAccountForm.showLifecycle ==true}">
            		<tr>
              			<td width="6%" valign="top">
              				<table border="0" cellpadding="0" cellspacing="0"><tbody><tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_LIFECYCLE %>" height="11" width="11">
                      				&nbsp;
                      			</td>
		                    </tr></tbody></table>
						</td>
              			<td width="29%" valign="top">Target Date</td>
              			<td width="30%" align="right" valign="top">
              				<report:number property="assets.totalAssetsByRisk(LC)" type="c"/>
              			</td>
              			<td width="20%" align="right" valign="top">
              				<report:number property="assets.percentageTotalByRisk(LC)" pattern="##0%" defaultValue="0%"/>
              			</td>
              		</tr>
</c:if>
              	
              	<%-- Aggressive Growth --%>
            	<tr>
              		<td valign="top">
              			<table border="0" cellpadding="0" cellspacing="0">
	                  	<tbody>
                    		<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_AGRESSIVE %>" height="11" width="11">
                      				&nbsp;
                      			</td>
                    		</tr>
                  		</tbody>
              			</table>
              		</td>
              		<td valign="top">Aggressive Growth</td>
              		<td align="right" valign="top">
              			<report:number property="assets.totalAssetsByRisk(AG)" type="c"/>
              		</td>
              		<td align="right" valign="top">
						<report:number property="assets.percentageTotalByRisk(AG)" pattern="##0%" defaultValue="0%"/>
              		</td>
	            </tr>
	            
	            <%-- Growth --%>
    	        <tr>
        			<td valign="top">
        				<table border="0" cellpadding="0" cellspacing="0">
                  		<tbody>
                    		<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_GROWTH %>"  height="11" width="11">
                      				&nbsp;
                      			</td>
                    		</tr>
                  		</tbody>
              			</table>
              		</td>
					<td valign="top">Growth</td>
              		<td align="right" valign="top">
              			<report:number property="assets.totalAssetsByRisk(GR)" type="c"/>
              		</td>
              		<td align="right" valign="top">
						<report:number property="assets.percentageTotalByRisk(GR)" pattern="##0%" defaultValue="0%"/>
              		</td>
            	</tr>
            	
            	<%-- Growth & Income --%>
            	<tr>
              		<td valign="top">
             			<table border="0" cellpadding="0" cellspacing="0">
                 		<tbody>
                   			<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_GROWTH_INCOME %>" height="11" width="11">
                      				&nbsp;
                      			</td>
		                    </tr>
        				</tbody>
              			</table>
              		</td>
              		<td valign="top">Growth &amp; Income</td>
              		<td align="right" valign="top">
              			<report:number property="assets.totalAssetsByRisk(GI)" type="c"/>
              		</td>
              		<td align="right" valign="top">
						<report:number property="assets.percentageTotalByRisk(GI)" pattern="##0%" defaultValue="0%"/>						
              		</td>
            	</tr>
            	
            	<%-- Income --%>
            	<tr>
					<td valign="top">
						<table border="0" cellpadding="0" cellspacing="0">
                  		<tbody>
                    		<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_INCOME %>" height="11" width="11">
                      				&nbsp;
                      			</td>
                    		</tr>
                  		</tbody>
              			</table>
              		</td>
              		<td valign="top">Income</td>
					<td align="right" valign="top">
						<report:number property="assets.totalAssetsByRisk(IN)" type="c"/>
						
					</td>
					<td align="right" valign="top">
						<report:number property="assets.percentageTotalByRisk(IN)" pattern="##0%" defaultValue="0%"/>						
              		</td>
            	</tr>
            	
            	<%-- Conservative --%>
            	<tr>
              		<td valign="top">
              			<table border="0" cellpadding="0" cellspacing="0">
                  		<tbody>
                    		<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_CONSERVATIVE %>" height="11" width="11">
                      				&nbsp;
                      			</td>
                    		</tr>
		                </tbody>
        				</table>
        			</td>
              		<td valign="top">Conservative</td>
              		<td align="right" valign="top">
              			<report:number property="assets.totalAssetsByRisk(CN)" type="c"/>
              		</td>
              		<td align="right" valign="top">
						<report:number property="assets.percentageTotalByRisk(CN)" pattern="##0%" defaultValue="0%"/>						
              		</td>
            	</tr>
			</table>
		</div>
		<%--#page_section--%>
		<div class="clear_footer"></div>
	</div>
	<%--#page_section_container--%>

	<%-- Display Information messages --%>
    <c:if test="${not empty bdMessages}">
	<div class="message message_info">
    <dl>
    <dt>Information Message</dt>
    <c:set var="count" value="1" scope="page"/>
<c:forEach items="${bdMessages}" var="info">
<c:if test="${info.errorCode == noParticipant}">
     <dd>${count}&nbsp;&nbsp;<content:getAttribute id="NoParticipantsMessage" attribute="text"/></dd>
</c:if>
<c:if test="${info.errorCode == moneyType}">
      <dd>${count}&nbsp;&nbsp;
      <content:getAttribute id="moneyTypeReportsNotAvailable" attribute="text">
              <content:param>
				   <render:date patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" property="bobContext.currentContract.contractDates.asOfDate"/>				  
			  </content:param>
	 </content:getAttribute>   
     </dd>
</c:if>
     <c:set var="count" value="${count+1}" scope="page"/>
</c:forEach>
    </dl>
    </div>
</c:if>

	<%-- Participant Tabs to navigate --%>
	<navigation:contractReportsTab attributeName="dbAccountUserNavigation" scope="session" menuID="${layoutBean.dbAccountMenuId}"/>

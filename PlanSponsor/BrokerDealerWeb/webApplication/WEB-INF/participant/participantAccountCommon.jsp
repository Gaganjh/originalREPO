<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.participant.ParticipantAccountForm" %>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">
	function goLoanRepaymentDetails(){
		if ( document.getElementById('selectedLoan').value != -1 )  {
			document.loanRepaymentDetails.loanNumber.value = document.getElementById('selectedLoan').value;
			document.loanRepaymentDetails.submit();
		}
	}

	function selectMoneyReport(actionUrl) {
		document.participantAccountForm.action = actionUrl;
		document.participantAccountForm.submit();
	}
	
	function tooltip(DefInvesValue)
	{
		if(DefInvesValue != null)
		{
			if(DefInvesValue == "TR")
			Tip('Instructions were provided by Trustee - Mapped');
			else if(DefInvesValue == "PR")
			Tip('Instructions prorated - participant instructions incomplete / incorrect');
			else if(DefInvesValue == "PA")
			Tip('Participant Provided');
			else if(DefInvesValue == "DF")
			Tip('Default investment option was used');
			else if(DefInvesValue == "MA")
			Tip('Managed Accounts');
			else			
			UnTip();
		}
		else
		{
			UnTip();
		}
	}	
</script>

<style type="text/css"> 
<!--
	#contract_investment_allocation #page_wrapper #page_content #page_section_container #personal_info a {
		text-decoration:underline !important;
		color:#005B80 !important;
	}
-->
</style>
<%-- Constant Files used--%>
<un:useConstants var="bdConstants" className="com.manulife.pension.bd.web.BDConstants" />
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="contentConstants" className="com.manulife.pension.bd.web.content.BDContentConstants" />
<un:useConstants var="errorCodes" className="com.manulife.pension.bd.web.BDErrorCodes" />

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
	ParticipantAccountDetailsVO details = (ParticipantAccountDetailsVO)request.getAttribute("details");
	pageContext.setAttribute("details",details);
%>
<jsp:useBean id="participantAccountForm" scope="session" type="com.manulife.pension.bd.web.bob.participant.ParticipantAccountForm" />

<%-- <c:set var="details" value="${details.}" scope="page" /> --%>

<content:contentBean contentId="${contentConstants.MESSAGE_PARTICIPANT_ACCOUNT_NO_PARTICIPANTS}"
        type="${contentConstants.TYPE_MESSAGE}"
        id="NoParticipantsMessage"/>

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE}"
        type="${contentConstants.TYPE_MESSAGE}"
        id="moneyTypeReportsNotAvailable"/>

<content:contentBean contentId="${contentConstants.MESSAGE_TECHNICAL_DIFFICULTIES}"
        type="${contentConstants.TYPE_MESSAGE}"
        id="TechnicalDifficulties"/>


<content:contentBean contentId="${contentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT}"
        type="${contentConstants.TYPE_MISCELLANEOUS}"
        id="csvIcon"/>

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT}"
        type="${contentConstants.TYPE_MISCELLANEOUS}"
        id="pdfIcon"/>

  <bd:form name="loanRepaymentDetails" method="post" action="/do/bob/transaction/loanRepaymentDetailsReport/" modelAttribute="loanRepaymentDetailsReportForm" >
  <form:hidden path="loanNumber" value="${participantAccountForm.selectedLoan}"/>
  <form:hidden path="name" value="${details.lastName},${details.firstName}"/>
<form:hidden path="profileId" value="${e:forHtmlAttribute(participantAccountForm.profileId)}"/>
   </bd:form>

	<!-- 1. Page title and introduction text -->
	<jsp:include page="/WEB-INF/global/displayContractInfoWithRothMessage.jsp"/>
	
	<!-- 2. Contract Navigation  -->
	<navigation:contractReportsTab/>
	
	<!-- 3. Report Section -->
	<div class="page_section_subheader controls">
		<!--  3.1 Report Title Bar  -->
		<h3><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></h3>
		
		<!-- 3.2 Report As of Date -->
		<form  method="get" action="/participant/participantAccount/" class="page_section_filter form" >
			<p><label for="investment_allocation_filter">as of: </label></p>
<c:if test="${layoutBean.body =='/WEB-INF/participant/participantAccount.jsp'}">
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
<c:if test="${layoutBean.body !='/WEB-INF/participant/participantAccount.jsp'}">
            	<p><label for="investment_allocation_filter">
            	<render:date patternOut="${renderConstants.MEDIUM_MDY_SLASHED}"
                				property="bobContext.currentContract.contractDates.asOfDate"/>
                 </label>
                </p>
</c:if>
		</form>
		
		<!--  3.3 PDF & CSV -->
		<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
        <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
	</div>
	<!--#page_section_subheader controls-->
		
	<!--  4. Report Summary -->
	<div id="page_section_container">
	
		<!--  4.1 Personal and other details -->	
		<div id="personal_info" class="page_section">
			<table class="overview_table" summary="Customer Account Information">
				<tbody>
					<!-- Name -->
					<tr>
						<th>Name:</th>
						<td>
${details.lastName},
${details.firstName}
						</td>
					</tr>
					
					<!-- SSN -->
					<tr>
						<th class="subheading">SSN:</th>
						<td>
<c:if test="${not empty details.ssn}">
					       	<render:ssn property="details.ssn" />
</c:if>
                        </td>
					</tr>
					
					<!-- Date of Birth -->
					<tr>
						<th class="subheading">Date of Birth:</th>
						<td><render:date property="details.birthDate" defaultValue="1/1/1980" patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" /></td>
					</tr>
					
					<!-- Age -->
<c:if test="${participantAccountForm.showAge ==true}">
					<tr>
						<th class="subheading">Age:</th>
<td>${details.age}</td>
					</tr>
</c:if>
					
					<!-- Contribution Status -->
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
					<%-- CL 110234 Begin --%>
					<tr>
						<th>Employment Status:</th>
<td>${details.employmentStatus}
&nbsp;&nbsp;${details.effectiveDate}
						</td>
					</tr>
					<%-- CL 110234 End --%>					
					<tr>
						<th>Contribution Status:</th>		<%-- CL 110234 --%>
<td>${details.employeeStatus}</td>
					</tr>
</c:if>
					
					<!-- Guaranteed Income for Life -->
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
						<c:if test="${bobContext.currentContract.hasContractGatewayInd}">
		           			<tr>
		          				<th>Guaranteed Income feature</th>
		          				<td>
${details.participantGIFLIndicatorAsSelect}
			          				<c:if test="${bobContext.participantGiflInd != null}">
			          					&nbsp;&nbsp;<a style="text-decoration:underline" href="/do/bob/participant/participantBenefitBaseInformation/?profileId=${e:forHtmlContent(participantAccountForm.profileId)}">View details</a>
			          				</c:if>
		          				</td>
		          			</tr>
						</c:if>
</c:if>
<c:if test="${ participantAccountForm.asOfDateCurrent and participantAccountForm.showManagedAccount }">
					<tr>
						<th>Managed Accounts<sup>&#167;</sup>:</th>
						<td style="padding-top: 7px">${details.managedAccountStatusValue}</td>
					</tr>
</c:if>
				
					
					<!-- Total Assets -->
					<tr class="overview_table_section">
						<th>Total Assets:</th>
						<td><report:number property="details.totalAssets" type="c"/></td>
					</tr>
					
					<!-- Allocated Assets -->
					<tr>
						<th>Allocated Assets:</th>
						<td><report:number property="details.allocatedAssets" type="c"/></td>
					</tr>
					
					<!-- Personal Brokerage Account -->
<c:if test="${participantAccountForm.showPba ==true}">
					<tr>
						<th>Personal brokerage account:</th>
						<td><report:number property="details.personalBrokerageAccount" type="c"/></td>
					</tr>
</c:if>
					
					<!-- Loan assets -->
					
<c:if test="${details.showLoanFeature =='YES'}">
					<tr>	
						<th>Loan assets:</th>
						<td>
							<report:number property="details.loanAssets" type="c"/>
							
<c:if test="${participantAccountForm.showLoans == true}">
                			
                				<c:set var ="loanListSize" value="${participantAccountForm.loanList.size()}"/>
                				<!-- If the Participant has  more than one outstanding loan display a drop down -->
								<c:if test="${loanListSize gt '2'}">
												  <form:select path="participantAccountForm.selectedLoan" id="selectedLoan" onchange="goLoanRepaymentDetails();"  value="-1" style="vertical-align: top">
 <form:options items="${participantAccountForm.loanList}" itemLabel="label" itemValue="value"/> 
</form:select>
				  </c:if>  				
				  				<!-- If the Participant has only one outstanding loan display a link -->
				  				<c:if test="${loanListSize eq '2'}">
<form:hidden path="participantAccountForm.selectedLoan" id="selectedLoan"  value="${participantAccountForm.selectedLoan}" />
					   				<a href="#" onclick="goLoanRepaymentDetails();">View details</a>
</c:if>
</c:if>
						</td>
					</tr>
</c:if>

<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
						<!--  Default date of birth -->
						<tr class="overview_table_section">
							<th>Default Date of Birth:</th>
							<td>
<c:if test="${empty details.birthDate}">Yes</c:if>
<c:if test="${not empty details.birthDate}">No</c:if>
							</td>
						</tr>
					
						<!-- Investment Instruction Type -->
						<tr>
							<th>Investment Instruction Type:</th>
<td><div onmouseover="tooltip('${details.investmentInstructionType}')" onmouseout="UnTip()">${details.investmentInstructionType}</div></td>
						</tr>
						
						<!-- Last Contribution Date -->			
						<tr>
							<th>Last Contribution Date:</th>
							<td>
							<% if(details.getLastContributionDate() != null ){ %>
							<render:date property="details.lastContributionDate" patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" />
							<% } else { %>
							n/a
							<% } %>
							</td>
						</tr>
						
						<!-- Automated Rebalance -->
						<tr>
							<th>Automated Rebalance?:</th>
<td>${details.automaticRebalanceIndicator}</td>
						</tr>
					
						<!-- Year of first Roth contribution -->
<c:if test="${details.rothFirstDepositYear !=9999}">
							<tr>
								<th>Year of first Roth contribution:</th>
<td>${details.rothFirstDepositYear}</td>
							</tr>
</c:if>
</c:if>
				</tbody>
			</table>
		</div>
		<!--#page_section-->
		
		<!--  4.2 Asset Allocation by Investment Category PieCharrt-->	
		<div class="page_section">
			<h4><strong>Asset Allocation by Risk/Return Category</strong></h4>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
				<!-- 4.2.1 Render the Pie Chart -->
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
            	
            	<!-- 4.2.2 Pie Chart Legends-->
            	<!-- LifeCycle -->
<c:if test="${participantAccountForm.showLifecycle ==true}">
            		<tr>
              			<td width="6%" valign="top">
              				<table border="0" cellpadding="0" cellspacing="0"><tbody><tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_LIFECYCLE %> " height="11" width="11">
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
              	
              	<!-- Aggressive Growth -->
            	<tr>
              		<td valign="top">
              			<table border="0" cellpadding="0" cellspacing="0">
	                  	<tbody>
                    		<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_AGRESSIVE %> " height="11" width="11">
                      				&nbsp;
                      			</td>
                    		</tr>
                  		</tbody>
              			</table>
              		</td>
              		<td valign="top">Aggressive growth</td>
              		<td align="right" valign="top">
              			<report:number property="assets.totalAssetsByRisk(AG)" type="c"/>
              		</td>
              		<td align="right" valign="top">
						<report:number property="assets.percentageTotalByRisk(AG)" pattern="##0%" defaultValue="0%"/>
              		</td>
	            </tr>
	            
	            <!-- Growth -->
    	        <tr>
        			<td valign="top">
        				<table border="0" cellpadding="0" cellspacing="0">
                  		<tbody>
                    		<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_GROWTH %> "  height="11" width="11">
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
            	
            	<!-- Growth & Income -->
            	<tr>
              		<td valign="top">
             			<table border="0" cellpadding="0" cellspacing="0">
                 		<tbody>
                   			<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_GROWTH_INCOME %> " height="11" width="11">
                      				&nbsp;
                      			</td>
		                    </tr>
        				</tbody>
              			</table>
              		</td>
              		<td valign="top">Growth &amp; income</td>
              		<td align="right" valign="top">
              			<report:number property="assets.totalAssetsByRisk(GI)" type="c"/>
              		</td>
              		<td align="right" valign="top">
						<report:number property="assets.percentageTotalByRisk(GI)" pattern="##0%" defaultValue="0%"/>						
              		</td>
            	</tr>
            	
            	<!-- Income -->
            	<tr>
					<td valign="top">
						<table border="0" cellpadding="0" cellspacing="0">
                  		<tbody>
                    		<tr>
                      			<td style="background:  <%= BDConstants.AssetAllocationPieChart.COLOR_INCOME %>  " height="11" width="11">
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
            	
            	<!-- Conservative -->
            	<tr>
              		<td valign="top">
              			<table border="0" cellpadding="0" cellspacing="0">
                  		<tbody>
                    		<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_CONSERVATIVE %>  " height="11" width="11">
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
            	
            	<!-- Personal Brokerage Account -->
<c:if test="${participantAccountForm.showPba ==true}">
            	<tr>
              		<td valign="top">
              			<table border="0" cellpadding="0" cellspacing="0">
                  		<tbody>
							<tr>
                      			<td style="background: <%= BDConstants.AssetAllocationPieChart.COLOR_PBA %> " height="11" width="11">
                      				&nbsp;
                      			</td>
                    		</tr>
                  		</tbody>
              			</table>
              		</td>
              		<td valign="top">Personal brokerage account</td>
              		<td align="right" valign="top">
              			<report:number property="assets.totalAssetsByRisk(PB)" type="c"/>
              		</td>
              		<td align="right" valign="top">
						<report:number property="assets.percentageTotalByRisk(PB)" pattern="##0%" defaultValue="0%"/>						
              		</td>
            	</tr>
</c:if>
			</table>
		</div>
		<!--#page_section-->
		<div class="clear_footer"></div>
	</div>
	<!--#page_section_container-->

	<!-- Display Information messages -->
	<c:if test="${not empty bdMessages}">
	<div class="message message_info">
    <dl>
    <dt>Information Message</dt>
    <c:set var="count" value="1" scope="page"/>
<c:forEach items="${bdMessages}" var="info">
<c:if test="${info.errorCode == errorCodes.PARTICIPANT_ACCOUNT_NO_PARTICIPANTS}">
     <dd>${count}.&nbsp;<content:getAttribute id="NoParticipantsMessage" attribute="text"/></dd>
</c:if>
<c:if test="${info.errorCode == errorCodes.PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE}">
      <dd>${count}.&nbsp;<content:getAttribute id="moneyTypeReportsNotAvailable" attribute="text">
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

    <!-- Display Error messages -->
	<c:if test="${not empty bdErrors}">
	<div class="message message_error">
    <dl>
    <dt>Error Message</dt>
    <c:set var="count" value="1" scope="page"/>
<c:forEach items="${bdErrors}" var="info">
<c:if test="${info.errorCode == errorCodes.TECHNICAL_DIFFICULTIES}">
             <dd>${count}.&nbsp;<content:getAttribute id="TechnicalDifficulties" attribute="text"/>&nbsp;&nbsp;[${errorCodes.TECHNICAL_DIFFICULTIES}]</dd>
</c:if>
</c:forEach>
    </dl>
    </div>
</c:if>


	<!-- Participant Tabs to navigate -->
	<navigation:contractReportsTab attributeName="pptAccountUserNavigation" scope="session" menuID="${layoutBean.participantAccountMenuId}"/>

	

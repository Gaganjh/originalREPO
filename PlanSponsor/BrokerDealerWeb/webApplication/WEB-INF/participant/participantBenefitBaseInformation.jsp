<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationReportData"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationReportData"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationReportData" %>
<%@page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantBenefitBaseDetails"%>
<%@ page import="com.manulife.pension.bd.web.bob.participant.WebParticipantGiflData"%>

	
<%
String NA=BDConstants.NA;
pageContext.setAttribute("NA",NA,PageContext.PAGE_SCOPE);
String Date=BDConstants.DEFAULT_DATE;
pageContext.setAttribute("Date",Date,PageContext.PAGE_SCOPE);
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
	ParticipantBenefitBaseDetails benefitDetails =(ParticipantBenefitBaseDetails) request.getAttribute(BDConstants.BENEFIT_DETAILS);
	pageContext.setAttribute("benefitDetails",benefitDetails,PageContext.PAGE_SCOPE);
	WebParticipantGiflData giflDetails =(WebParticipantGiflData)request.getAttribute(BDConstants.ACCOUNT_DETAILS);
	pageContext.setAttribute("giflDetails",giflDetails,PageContext.PAGE_SCOPE);
	String technicalDifficulties =(String)request.getAttribute(BDConstants.TECHNICAL_DIFFICULTIES);
	pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);
%>
<jsp:useBean id="participantBenefitBaseInformationForm" scope="session"
	class="com.manulife.pension.bd.web.bob.participant.ParticipantBenefitBaseInformationForm" />
<%
ParticipantBenefitBaseInformationReportData theReport = (ParticipantBenefitBaseInformationReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>


<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>
<c:if test="${participantBenefitBaseInformationForm.showLIADetailsSection ==true}">
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_LIA_SELECTION_DATE_FIELD_LABEL%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
		id="liaSelectionDateFieldLabel" />
		
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_LIA_SPOUSAL_OPTION_FIELD_LABEL%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
		id="spousalOptionFieldLabel" />
	
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_LIA_PERCENTAGE_FIELD_LABEL%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
		id="liaPercentageFieldLabel" />
		
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_ANNUAL_LIA_AMOUNT_FIELD_LABEL%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
		id="annualLIAAmountFieldLabel" />
		
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_LIA_PAYMENT_FREQUENCY_FIELD_LABEL%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
		id="paymentFrequencyFieldLabel" />
		
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_LIA_ANNIVERSARY_DATE_FIELD_LABEL%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
		id="liaAnniversaryDateFieldLabel" />
	
	<content:contentBean
		contentId="<%=BDContentConstants.MISCELLANEOUS_BENEFIT_BASE_LIA_MESSAGE%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
		id="liaInfoMessage" />
</c:if>
<c:set var="footNotes" value="${layoutBean.layoutPageBean.footnotes}"/>           

<script type="text/javascript" >
   function doReset(nextAction) {
	   	document.participantBenefitBaseInformationForm.task.value = nextAction;
  	 	document.participantBenefitBaseInformationForm.action="/bob/participant/participantBenefitBaseInformation/";
	 		return true;
		}	
   	   	
   function submitFilter() {
   			datesFilterForm = document.getElementById("datesFilterID");
			setFilterFromInput(datesFilterForm.elements['fromDate']);
			setFilterFromInput(datesFilterForm.elements['toDate']);
			doFilter('/bob/participant/participantBenefitBaseInformation');
		}
   function submitDates() {
   			datesFilterForm = document.getElementById("datesFilterID");
			setFilterFromInput(datesFilterForm.elements['fromDate']);
			setFilterFromInput(datesFilterForm.elements['toDate']);
			doFilter('/bob/participant/participantBenefitBaseInformation');
   }
	
   function doOnload() {
			var lastVisited = "<%=request.getParameter("lastVisited")%>";
			var pageNumber = "<%=request.getParameter("pageNumber")%>";
			var sortField = "<%=request.getParameter("sortField")%>";
			if ((lastVisited == "true") || (pageNumber != "null") || (sortField != "null")) {
				location.hash = "participantName";
  			}
  }
</script>

<input type="hidden" name="pdfCapped" /><%-- CCAT: Extra attributes for tag input - name="participantBenefitBaseInformationForm" --%>
       
<h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>

<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
  <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>

<%--Layout/intro1--%>
<c:if test="${not empty layoutPageBean.introduction1}">
    <p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>

<%--Layout/Intro2--%>
<c:if test="${not empty layoutPageBean.introduction2}">
    <p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
</c:if>

<c:if test="${participantBenefitBaseInformationForm.bbBatchDateLessThenETL == 'Y'}">
	<utils:info contentId="<%=BDContentConstants.MISCELLANEOUS_BENEFIT_BASE_BATCH_OUT_OF_DATE%>"/> 
</c:if>

<%--LIA Info message - This should be shown only in the top unlike batch out of date message--%>
<c:if test="${participantBenefitBaseInformationForm.showLIADetailsSection eq true}">
<div class="message message_info">
  <dl>
    <dt>Information Message</dt>
    <dd>1&nbsp;&nbsp; 
       <content:getAttribute attribute="text" beanName="liaInfoMessage"/>
     </dd>
    </dl>
</div>
</c:if>
	
<report:formatMessages scope="session"/>

<navigation:contractReportsTab />

<c:if test="${empty technicalDifficulties}">

	<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>
		<form action="#" class="page_section_filter form">
		  <p> as of <render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED %>" property="participantBenefitBaseInformationForm.asOfDate" /></p>
		</form>
		<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
	    <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
	</div>	
	
	<div id="page_section_container">	
		<div class="page_section">
			<table class="overview_table">
				<tbody>
					<tr>
						<th><strong>Name:</strong></th>
<td>${benefitDetails.name}</td>
					</tr>
					<tr>
						<th class="subheading"><strong>SSN:</strong></th>
						<td><render:ssn property="benefitDetails.ssn" /></td>
					</tr>
<c:if test="${not empty benefitDetails.dateOfBirth}">
						<tr>
							<th class="subheading"><strong>Date of Birth:</strong></th>
						    <td><render:date property="benefitDetails.dateOfBirth" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
						</tr>
</c:if>
					<tr>
<c:if test="${participantBenefitBaseInformationForm.showFootnote == 'Y'}">
							<th class="subheading"><strong>Benefit Base*:</strong></th>
</c:if>
<c:if test="${participantBenefitBaseInformationForm.showFootnote == 'N'}">
						    <th class="subheading"><strong>Benefit Base:</strong></th>
</c:if>
						<td><render:number property="giflDetails.giflBenefitBaseAmt" type="c" /></td>
					</tr>
					<tr>
						<th class="subheading"><strong>Market Value:</strong></th>
						<td>
<c:if test="${giflDetails.webGiflDeselectionDate != Date}">
								<render:number value="0" type="c" />
</c:if>
<c:if test="${giflDetails.webGiflDeselectionDate == Date}">
								<render:number property="benefitDetails.marketValueGoFunds" type="c" />
</c:if>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="page_section">
		 	<table width="100%" border="0" cellspacing="0" cellpadding="0">
			    <tr>
	    	      	<td width="38%" valign="top"><div align="right"><strong>Selection Date:</strong></div></td>
<td width="26%" valign="top">${giflDetails.webGiflSelectionDate}</td>
	           	</tr>
<c:if test="${giflDetails.webGiflDeselectionDate != Date}">
		           	<tr>
		           		<td width="35%" valign="top"><div align="right"><strong>Deactivation Date:</strong></div></td>
		           		<td valign="top"><render:date property="giflDetails.giflDeselectionDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
		           	</tr>
</c:if>
				<tr>
					<td width="35%" valign="top"><div align="right"><strong>Activation Date:</strong></div></td>
<td valign="top">${giflDetails.webGiflActivationDate}</td>
				</tr>
				<!-- This should be hide for valid LIA anniversary date -->
<c:if test="${participantBenefitBaseInformationForm.showLIADetailsSection ==false}">
					<tr>
		               	<td width="35%" valign="top"><div align="right"><strong>Anniversary Date:</strong></div></td>
<td valign="top">${giflDetails.webGiflNextStepUpDate}</td>
		           	</tr>
</c:if>
	           	<tr>
	           		<td width="35%" valign="top"><div align="right"><strong>Minimum Holding Period Expiry Date:</strong></div></td>
<td valign="top">${giflDetails.webGiflHoldingPeriodExpDate}</td>
	           	</tr>
<c:if test="${bobContext.contractProfile.contract.hasContractGatewayInd == true}">
					<%-- Trading Expiration Date will only be displayed  if the trade expiration is in effect and this date should be after GIFL selection activation date--%>
<c:if test="${participantBenefitBaseInformationForm.showTradingExpirationDate == 'Y'}">
							<tr>
								<td width="35%" valign="top"><div align="right"><strong>Trading Restriction Expiry Date:</strong></div></td>
<td valign="top">${participantBenefitBaseInformationForm.displayTradingExpirationDate}</td>
							</tr>	
</c:if>
					<%-- Rate for Last Income Enhancement Fields will be displayed only if the contract has GIFL version 3 --%>
<c:if test="${bobContext.contractProfile.contract.giflVersion == 'G03'}">
						<tr>
							<td width="35%" valign="top"><div align="right"><strong>Rate for Last Income Enhancement:</strong></td>
<td valign="top">${giflDetails.displayRateForLastIncomeEnhancement}</td>
						</tr>	
</c:if>
</c:if>
				<tr>
					<td width="35%" valign="top"><div align="right">
					<%-- Last Income Enhancement will be displayed only if the GIFL version is 3 else Last Step-Up Date will be displayed--%>
<c:if test="${bobContext.contractProfile.contract.hasContractGatewayInd == true}">
<c:if test="${bobContext.contractProfile.contract.giflVersion == 'G03'}">
									<strong>Last Income Enhancement Date:</strong>
</c:if>
<c:if test="${bobContext.contractProfile.contract.giflVersion != 'G03'}">
									<strong>Last Step-Up Date:</strong>
</c:if>
</c:if>
					</td>
<td valign="top">${giflDetails.webGiflLastStepUpDate}</td>
				</tr>
				<tr>
					<td width="35%" valign="top" nowrap="nowrap"><div align="right">
					<%-- "Value changed at last Income Enhancement date" will be displayed only if the GIFL version is 3 else "Value changed at last Step-Up date" will be displayed--%>								
<c:if test="${bobContext.contractProfile.contract.hasContractGatewayInd == true}">
<c:if test="${bobContext.contractProfile.contract.giflVersion == 'G03'}">
								<strong>Value Changed at Last Income Enhancement:</strong>
</c:if>
<c:if test="${bobContext.contractProfile.contract.giflVersion != 'G03'}">
								<strong>Value Changed at Last Step-Up Date:</strong>
</c:if>
</c:if>
					</td>
					<td valign="top">
<c:if test="${giflDetails.webGiflLastStepUpChangeAmt != NA}">
							<render:number property="giflDetails.giflLastStepUpChangeAmt" type="c" />
</c:if>
<c:if test="${giflDetails.webGiflLastStepUpChangeAmt == NA}">
${giflDetails.webGiflLastStepUpChangeAmt}
</c:if>
					</td>
				</tr>
	         </table>
		</div>
		<div class="clear_footer"></div>
	</div>
	
<c:if test="${participantBenefitBaseInformationForm.showLIADetailsSection ==true}">
	<%-- Start LIA information --%>
	<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean" attribute="body3Header"/></h3>
	</div>
	
	<div id="page_section_container">	
		<div class="page_section">
			<table class="overview_table">
				<tbody>
				<tr>
							<th class="subheading"><strong><content:getAttribute id="liaSelectionDateFieldLabel"
										attribute="text" /></strong></th>
						    <td><render:date
									property="participantBenefitBaseInformationForm.liaSelectionDate"
									patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
				</tr>
					<tr>
					<td width="35%" valign="top"><div align="right"><strong><content:getAttribute id="spousalOptionFieldLabel"
										attribute="text" /></strong></div></td>
<td valign="top">${participantBenefitBaseInformationForm.liaIndividualOrSpousalOption}</td>
				</tr>      
					<tr>
					<td width="35%" valign="top"><div align="right"><strong><content:getAttribute id="liaPercentageFieldLabel"
										attribute="text" /></strong></div></td>
<td valign="top">${participantBenefitBaseInformationForm.liaPercentage}</td>
				</tr>      
				</tbody>
			</table>
		</div>
		<div class="page_section">
		 	<table width="100%" border="0" cellspacing="0" cellpadding="0">
			    
				<tr>
					<td width="38%" valign="top"><div align="right"><strong><content:getAttribute id="annualLIAAmountFieldLabel"
										attribute="text" /></strong></div></td>
<td width="40%"> <c:if test="${not empty participantBenefitBaseInformationForm.liaAnnualAmount}"><render:number
										property="participantBenefitBaseInformationForm.liaAnnualAmount" type="c" /></c:if></td>
				</tr>           	
	           	<tr>
	               	<td width="35%" valign="top"><div align="right"><strong><content:getAttribute id="paymentFrequencyFieldLabel"
										attribute="text" /></strong></div></td>
<td valign="top">${participantBenefitBaseInformationForm.liaFrequencyCode}
<c:if test="${not empty participantBenefitBaseInformationForm.liaPeriodicAmt}"> - <render:number
										property="participantBenefitBaseInformationForm.liaPeriodicAmt" type="c" /></c:if></td>
	           	</tr>
	           	<tr>
	           		<td width="35%" valign="top"><div align="right"><strong><content:getAttribute id="liaAnniversaryDateFieldLabel"
										attribute="text" /></strong></div></td>
	           		 <td><render:date
									property="participantBenefitBaseInformationForm.liaAnniversaryDate"
									patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
	           	</tr>
				
	         </table>
		</div>
		<div class="clear_footer"></div>
	</div>
	<%-- End LIA information --%>	
</c:if>
	
	<div class="page_section_subheader controls">
		<h3><content:getAttribute id="layoutPageBean" attribute="body2Header"/></h3>
		<bd:form id="datesFilterID" cssClass="margin-bottom:0;" modelAttribute="participantBenefitBaseInformationForm" name="participantBenefitBaseInformationForm" method="POST" action="/do/bob/participant/participantBenefitBaseInformation/" cssClass="page_section_filter form">
			<p>from  </p>
<input type="text" id="startDate" name="fromDate" size="9" value='${participantBenefitBaseInformationForm.fromDate}' />
			<utils:btnCalendar  dateField="startDate" calendarcontainer="calendarcontainer" datefields="datefields"  calendarpicker="calendarpicker"/>
			<p>  to  </p>
<input type="text" id="endDate" name="toDate" size="9" value='${participantBenefitBaseInformationForm.toDate}'/>
			<utils:btnCalendar  dateField="endDate" calendarcontainer="calendarcontainer1" datefields="datefields1"  calendarpicker="calendarpicker1"/>
			<p>(mm/dd/yyyy)</p>
			<a class="buttonheader" href="javascript:submitDates();"><span>Search</span></a>
		</bd:form>		
	</div>
	
	<bd:form  cssClass="margin-bottom:0;" method="POST"  action="/do/bob/participant/participantBenefitBaseInformation/" modelAttribute="participantBenefitBaseInformationForm" name="participantBenefitBaseInformationForm">
		<div class="report_table">  
			<c:if test="${ empty theReport}">
				<table class="report_table_content" id="participants_table">
					<thead>
						<tr>
							<th class="val_str"><report:sort formName="participantBenefitBaseInformationForm" field="<%=ParticipantBenefitBaseInformationReportData.SORT_FIELD_EFFECTIVE_DATE %>" direction="asc">Transaction Effective Date</report:sort></th>
						    <th class="val_str">Associated Transaction Number</th>
							<th class="val_str">Transaction Type</th>
						    <th class="val_str">Market Value Before Transaction($)</th>
						    <th class="val_str">Transaction Amount($)</th>
							<th class="val_str">Benefit Base Change($)</th>
							<th class="val_str">Resulting Benefit Base ($)</th>
	                        <th class="val_str">MHP Reset</th>
	          			</tr>
					</thead>
				</table>
			</c:if>      
	   		<c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
		        	<div class="table_controls">
	                	<div class="table_action_buttons"> </div>
				    	<div class="table_display_info"><strong><report:recordCounter report="theReport" label="Transactions"/></strong></strong></div>
				    	<div class="table_pagination">
	 						<strong><report:pageCounter formName="participantBenefitBaseInformationForm" arrowColor="black" report="theReport"/> </strong>
	 		      		</div>
						<div class="table_controls_footer"></div>
					</div>
</c:if>
				<table class="report_table_content" id="participants_table">
					<thead>
						<tr>
							<th class="val_str"><report:sort formName="participantBenefitBaseInformationForm" field="<%=ParticipantBenefitBaseInformationReportData.SORT_FIELD_EFFECTIVE_DATE %>" direction="asc">Transaction Effective Date</report:sort></th>
						    <th class="val_str">Associated Transaction Number</th>
							<th class="val_str">Transaction Type</th>
						    <th class="val_str">Market Value Before Transaction($)</th>
						    <th class="val_str">Transaction Amount($)</th>
							<th class="val_str">Benefit Base Change($)</th>
							<th class="val_str">Resulting Benefit Base ($)</th>
	                        <th class="val_str">MHP Reset</th>
	          			</tr>
					</thead>
<c:if test="${not empty theReport.details}">
						<tbody>
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" ><%-- CCAT: Extra attributes for tag c:forEach - type="com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationDataItem" --%>
								<tr class="spec">
									<td class="name"><render:date dateStyle="m" property="theItem.transactionEffectiveDate" /></td>
									<td class="val_str">
<c:if test="${theItem.transactionNumber !=0}">
${theItem.transactionNumber}
</c:if>
<c:if test="${theItem.transactionNumber ==0}">
</c:if>
									</td>
<td class="date">${theItem.transactionType}</td>
									<td class="cur"><utils:replaceString from="<%=BDConstants.NA%>" to="<%=BDConstants.HYPHON_SYMBOL%>" val="${theItem.marketValueBeforeTransaction}"/></td>
									<td class="cur"><utils:replaceString from="<%=BDConstants.NA%>" to="<%=BDConstants.HYPHON_SYMBOL%>" val="${theItem.transactionAmount}"/></td>
									<td class="cur"><utils:replaceString from="<%=BDConstants.NA%>" to="<%=BDConstants.HYPHON_SYMBOL%>" val="${theItem.benefitBaseChangeAmount}"/></td>
									<td class="cur"><render:number property="theItem.benefitBaseAmount" type="d" /></td>
<td class="date">${theItem.holdingPeriodInd}</td>
	                      		</tr>
</c:forEach>
						</tbody>
</c:if>
				</table>
<c:if test="${empty theReport.details}">
				   	<utils:info contentId="<%=BDContentConstants.NO_TRANSACTIONS_MESSAGE_FOR_BENEFIT_BASE_PAGE%>"/>
</c:if>
<c:if test="${not empty theReport.details}">
					<div class="table_controls">
				    	<div class="table_action_buttons"> </div>
				    	<div class="table_display_info"><strong><report:recordCounter report="theReport" label="Transactions"/></strong></div>
				       	<div class="table_pagination"><strong><report:pageCounter formName="participantBenefitBaseInformationForm" arrowColor="black" report="theReport"/> </strong>
				     </div>
</c:if>
			</c:if>			      
			<div class="table_controls_footer"></div>
	  </div>
	</bd:form>		  

</c:if>  
<%-- technical difficulties --%>

<div class="footnotes">
    <div class="footer"><content:pageFooter beanName="layoutPageBean"/></div> 
    <br>    
    
<c:if test="${participantBenefitBaseInformationForm.showFootnote == 'Y'}">
<c:if test="${bobContext.contractProfile.contract.giflVersion != 'G03'}">
			<content:contentBean
				contentId="<%=BDContentConstants.BENEFIT_BASE_PAGE_DYNAMIC_STEP_UP_FOOTNOTE%>"
				type="<%=BDContentConstants.TYPE_DISCLAIMER%>" id="benefitBaseFootnote" />
</c:if>
<c:if test="${bobContext.contractProfile.contract.giflVersion == 'G03'}">
			<content:contentBean
				contentId="<%=BDContentConstants.BENEFIT_BASE_PAGE_DYNAMIC_INCOME_ENHANCEMENT_FOOTNOTE%>"
				type="<%=BDContentConstants.TYPE_DISCLAIMER%>" id="benefitBaseFootnote" />
</c:if>
		<dl>
			<dd><p class="footnote"><content:getAttribute id="benefitBaseFootnote" attribute="text" /></p></dd>
		</dl>
</c:if>
	    
    <c:if test="${not empty footNotes}"> 
	    <dl>
	      <dd><content:pageFootnotes beanName="layoutPageBean"/></dd> 
	    </dl>
	    </c:if>
	<dl>
	<dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd>
	</dl>
	<div class="footnotes_footer"></div>
</div>

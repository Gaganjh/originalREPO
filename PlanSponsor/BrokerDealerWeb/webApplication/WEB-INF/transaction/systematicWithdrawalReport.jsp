<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />

<%@ page import="java.util.Date"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.handler.TransactionType" %>
<%@ page import="com.manulife.pension.bd.web.bob.transaction.SystematicWithdrawReportForm" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.util.content.GenericException"%>
<%@ page import="com.manulife.pension.bd.web.BDErrorCodes"%>
<%@ page import="com.manulife.pension.platform.web.util.CommonEnvironment"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>

<%@ page import="com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawReportData" %>
<%@ page import="java.util.Collection"%>
<%@ page import="com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawDataItem"%>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

		
<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);

	SystematicWithdrawReportData theReport = (SystematicWithdrawReportData)request.getAttribute(BDConstants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
	
	Collection errorBean = (Collection)session.getAttribute(CommonEnvironment.getInstance().getErrorKey());
	request.setAttribute("errorBean", errorBean);
%> 

<input type="hidden" name="pdfCapped" /><%--  input - name="systematicWithdrawReportForm" --%>
			 
<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>
		
		<content:contentBean contentId="<%=BDContentConstants.MESSAGE_STMT_NO_PARTICIPANTS_ON_BASIC_SEARCH%>"
                         	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="noResultForQSearch"/>
<content:contentBean contentId="<%=BDContentConstants.MESSAGE_STMT_NO_PARTICIPANTS_ON_ADV_SEARCH%>"
                         	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="noResultForCustomSearch"/>   

	<style type="text/css">

div#participant_summary_report{
	background-color:#f6f4f1;
	margin-top: 5px;
	margin-right: 0;
	margin-left: 0;
	padding-top: 2px;
	padding-right: 1px;
	padding-bottom: 5px;
	padding-left: 1px;
}



td,
th{
    color:#767676;
    font-size: 0.6875em;
    padding-top: 4px;
    padding-right: 1px;
    padding-bottom: 4px;
    padding-left: 1px;
}
</style>						
							
   <SCRIPT type="text/javascript">
   
			function pptStatemetns(url){
				var vsWin = window.open(url);
				vsWin.focus();
				return true;
			}

		</SCRIPT>		
	
	
	<h2><content:getAttribute id="layoutPageBean" attribute="name"/>
	as of:
	<span style="font-size:14px;"><render:date property="systematicWithdrawReportForm.asOfDate"
				patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" defaultValue="" /></span>
	
</h2>
						
<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
    <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>
<!--Layout/intro1-->
<c:if test="${not empty layoutPageBean.introduction1}">
    <p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>

<!--Layout/Intro2-->
<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>


		<%--#Error- message box--%>
<report:formatMessages scope="request" />
		
		<navigation:contractReportsTab />
		
		<!------ONE Section for Quick Filter  ----------------------------------------------------------->

 <DIV class="page_section_subheader controls">
   <content:errors scope="session"/> 
 <FORM name="quickFilterForm" class="page_section_filter form" style="width: 55%;"
       action="/do/bob/transaction/systematicWithdrawalReport/" method="post">
<input type="hidden" name="task" value="filter"/>
      
      
        <!--   <SELECT name="participantFilter" onChange="setFilterFromSelect(this);selectQuickFilter();"> -->
		<input type="hidden" 
			   name="showCustomizeFilter" 
			   id="showCustomizeFilter"
value="${e:forHtmlAttribute(systematicWithdrawReportForm.showCustomizeFilter)}">


 
      <p><label for="data_Filter_by">Search by: </label></p>
		<bd:select name="systematicWithdrawReportForm"
				   property="quickFilterBy"
				   onchange="setFilterFromSelect(this);selectQuickFilter();">
          <bd:option value="blank_val">&nbsp;</bd:option>
          <bd:option value="wd_status">Withdrawal Status</bd:option>
          <bd:option value="wd_type">Withdrawal Type</bd:option>
          <bd:option value="last_name">Last Name</bd:option>
          <bd:option value="ssn">SSN</bd:option>
        </bd:select>
        <P>
          <LABEL for="participant_search">Looking for: </LABEL>
        </P>
        
         <DIV id="div_blank">
<form:input path="systematicWithdrawReportForm.quickFilterNamePhrase" maxlength="30" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" />





		  
        </DIV>
        <DIV id="div_wdStatus">
<form:select path="systematicWithdrawReportForm.wdStatus" disabled="false" onchange="setFilterFromSelect2(this);" >
    <%-- set the first value of the select --%>
             	<form:option value="All">All</form:option>
<form:options items="${systematicWithdrawReportForm.statusList}" itemLabel="label" itemValue="value"/>
</form:select>
     
        </DIV>
        <DIV id="div_wdType">
        
<form:select path="systematicWithdrawReportForm.wdType" disabled="false" onchange="setFilterFromSelect2(this);" >



             <%-- set the first value of the select --%>
						<form:option value="All">All</form:option>
<form:options items="${systematicWithdrawReportForm.typeList}" itemLabel="label" itemValue="value"/>
</form:select>
       
        </DIV>
        <DIV id="div_namePhrase">
<form:input path="systematicWithdrawReportForm.participantName" maxlength="30" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" />





		  
        </DIV>
        <DIV id="div_ssn">
        
		  
<form:password path="systematicWithdrawReportForm.ssnOne" cssClass="inputField" onchange="setFilterFromInput(this);" maxlength="3" readonly="false" size="2" />





					   
<form:password path="systematicWithdrawReportForm.ssnTwo" cssClass="inputField" onchange="setFilterFromInput(this);" maxlength="2" readonly="false" size="2" />







<form:input path="systematicWithdrawReportForm.ssnThree" maxlength="4" onchange="setFilterFromInput(this);" readonly="false" size="3" cssClass="inputField" />





		
		  
	
    </div>
      </FORM>
	 
	<!------TWO Section for Adv Search and PDF button  -----------------------------------------------------------> 

	<c:if test="${empty requestScope.isError}">
	<a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon" 
	 title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> 
	 <content:image contentfile="image" id="pdfIcon" /> </a>
    <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>

	</c:if>
 <div class="button_header" style="margin-left : 1px; margin-top: 15px; float:right; margin-right : 5px; "><span><input id="customize_report" type="button" value="Advanced Search" style="margin-left : 0px;width:110px;" onclick="openCustomizeFilter()"/></span> </div>
	<div id="quickFilterSubmitDiv" class="button_header" style="margin:0px; margin-left : 1px; margin-right : 4px; margin-top: 15px; float:right;"><span><input id="quick_report" type="button" value="Search" style="margin-left : 0px;width:50px;" onclick="submitData('quickFilter')"/></span> </div>

</div>
	      
	<!------THREE Section for Advance search-----------------------------------------------------------> 	   
 <DIV id="participant_summary_report">
      <DIV id="report_customization_wrapper">		   
		  <FORM name="reportCustomizationForm" id="report_customization" style="margin-bottom: 0px;" action="/bob/transaction/systematicWithdrawalReport/" method="post">
          <TABLE width="100%">
            <TBODY>
              <TR>
                <TD align="right">Withdrawal Status: </TD>
                <TD align="left">
                <div id="div_show_withdrawal_status_option" style="display: block;">
                
<form:select path="systematicWithdrawReportForm.customWDStatus" disabled="false" onchange="setFilterFromSelect2(this);" >



              <form:option value="All">All</form:option>
<form:options items="${systematicWithdrawReportForm.statusList}" itemLabel="label" itemValue="value"/>
</form:select>
				</div>
                </td>               
                <TD align="right">Withdrawal Type: </TD>  
                <TD align="left">
                <div id="div_show_withdrawal_type_option" style="display: block;">
                
<form:select path="systematicWithdrawReportForm.customWDType" disabled="false" onchange="setFilterFromSelect2(this);" >



                          
              <%-- set the first value of the select --%>
					<form:option value="All">All</form:option>
<form:options items="${systematicWithdrawReportForm.typeList}" itemLabel="label" itemValue="value"/>
</form:select>
      
       </div>
                </td>             
                <TD align="right">Last&nbsp;Name: </TD>
                <TD align="left">     
<form:input path="systematicWithdrawReportForm.customparticipantName" maxlength="30" onchange="setFilterFromInput(this);" readonly="false" cssClass="inputField" />





				</td>
				 <TD align="right">SSN: </TD>
                <TD align="left">
<form:password path="systematicWithdrawReportForm.customSsnOne" cssClass="inputField" onchange="setFilterFromInput(this);" maxlength="3" readonly="false" size="2" />





<form:password path="systematicWithdrawReportForm.customSsnTwo" cssClass="inputField" onchange="setFilterFromInput(this);" maxlength="2" readonly="false" size="2" />





<form:input path="systematicWithdrawReportForm.customSsnThree" maxlength="4" onchange="setFilterFromInput(this);" readonly="false" size="2" cssClass="inputField" />





				 
				 </TD>
                <TD width="10">&nbsp;</TD>
				</tr>
			
            </TBODY>
          </TABLE>
          <BR>
           <DIV class="selection_input" style="padding-right: 15px; float: right;">
				   <div class="button_search">
						<input type="button" 
							   value="Reset"
							   id="cancel_customization" 
							   onclick="doCancel()" /></div>
					<div class="button_search">
						<input type="button" 
							   value="Submit"
							   id="apply_customization" 
							   onclick="submitData('customizeFilter')" /></div>
					
				</div>
        </FORM>   
	</div>
	  
</div>	
	<!------FOUR Section for Actual report display----------------------------------------------------------->		   
<bd:form method="post" modelAttribute="systematicWithdrawReportForm" name="systematicWithdrawReportForm" 
		 action="/do/bob/transaction/systematicWithdrawalReport/">
	<div class="report_table">
		 <c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
		 <div class="table_controls">
		 
					<div class="table_action_buttons"></div>
					<div class="table_display_info"><strong><report:recordCounter
						report="theReport" label="Systematic Withdrawals" /></strong></div>
					<div class="table_pagination"><report:pageCounter formName="systematicWithdrawReportForm"
						report="theReport" arrowColor="black" /></div>
					
					<div class="table_controls_footer"></div>
					</div>
</c:if>
		</c:if>
		<div class="table_controls_footer"></div>
			<div id="scrollingTable" style="width:918px;Overflow-x:auto;Overflow-y:hidden">
		<table class="report_table_content" id="participants_table">
	<thead>
	<tr>
		<th rowspan="2" class="val_str">
		<report:sort field="participantName"  formName="systematicWithdrawReportForm"
					 direction="asc">Name</report:sort>&nbsp;&nbsp;&nbsp;&nbsp;</th>
		
				<th rowspan="2" class="val_str">
						<report:sort field="wdStatus" formName="systematicWithdrawReportForm"
									 direction="asc">Withdrawal Status</report:sort>&nbsp;&nbsp;&nbsp;&nbsp;</th>
									 
					<th rowspan="2" class="val_str">
						<report:sort field="wdType" formName="systematicWithdrawReportForm"
					 direction="asc">
						Withdrawal Type
					</report:sort></th>
					<th rowspan="2" class="val_str">
						<report:sort field="setDate" formName="systematicWithdrawReportForm"
					 direction="asc">Setup Date</report:sort>
					</th>
					<th rowspan="2" class="val_str">
						<report:sort field="calcMethod" formName="systematicWithdrawReportForm"
					 direction="asc">Calculation Method</report:sort>
					</th>
					<th rowspan="2" class="val_str">
						<report:sort field="freq" formName="systematicWithdrawReportForm"
					 direction="asc">Frequency</report:sort>
					</th>
					<th rowspan="2" class="val_str">
							<report:sort field="lastPayDate" formName="systematicWithdrawReportForm"
					 direction="asc">Last Payment Date</report:sort>
					</th>
					<th rowspan="2" class="val_str align_center" >
						<report:sort field="lastPayAmt" formName="systematicWithdrawReportForm"
					 direction="asc">Last Payment Amount ($)</report:sort>
					</th>
					<th rowspan="2" class="val_str align_center">
						<report:sort field="totalAsset" formName="systematicWithdrawReportForm"
					 direction="asc">Total Current Assets ($)</report:sort>
					</th>
					
	</tr>
	</thead>
	
	

	
	
	
	<tbody>
		<c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
			
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
 <c:set var="indexValue" value="${theIndex.index}"/>
<% 				
		SystematicWithdrawDataItem theItem = (SystematicWithdrawDataItem)pageContext.getAttribute("theItem");
		String temp = pageContext.getAttribute("indexValue").toString();%>
				<%
						String rowClass = "spec";
							if (Integer.parseInt(temp) % 2 == 1) {
								rowClass = "";
							} else {
								rowClass = "spec";
							}
				%>
				<tr class="<%=rowClass%>">
				<td class="name">
				<bd:link action="/do/bob/participant/participantAccount/?profileId=${theItem.participant.profileId}"> 
										${theItem.participant.lastName},${theItem.participant.firstName}
									</bd:link>
										<br/>
<c:if test="${not empty theItem.participant.ssn}">
										<render:ssn property="theItem.participant.ssn" />
</c:if>
										<br/>
									
				</td>
					<td>
						<div style="color: #000;">
${theItem.wdStatus}
							
								<br/>
								<br/>
							</div>
					</td>
					<td>
${theItem.wdType}
					</td>
					<td class="date">
					
					<render:date property="theItem.wdSetupDate"
											 defaultValue="-"
								patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /> 
					
					</td>
					<td>
${theItem.calculationMethod}
					</td>
					<td>
${theItem.wdfrequency}
					</td>
					<td class="date">
					
					<render:date property="theItem.lastPayDate"
											 defaultValue="-"
								patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" />
					
					</td>
					<td class="cur">
<c:if test="${not empty theItem.lastPaymentAmount}">
					    <report:number property="theItem.lastPaymentAmount" type="c" sign="false"/>
</c:if>
					</td>
					<td class="cur">
<c:if test="${not empty theItem.totalAssets}">
					  <report:number property="theItem.totalAssets" type="c" sign="false"/>
</c:if>
					</td>
				</tr>
</c:forEach>
			
			
			
</c:if>
		</c:if>
		</tbody></table>
		<%-- table_controls --%>
		
		</div>
		<c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
		<div class="table_controls">
		<div class="table_action_buttons"></div>
		<div class="table_display_info"><strong><report:recordCounter
						report="theReport" label="Systematic Withdrawals" /></strong></div>
					<div class="table_pagination"><report:pageCounter formName="systematicWithdrawReportForm"
						report="theReport" arrowColor="black" /></div>
		<div class="table_action_buttons"></div>
		</div>
</c:if>
		</c:if>
	
		
		<c:if test="${empty errorBean}">
<c:if test="${empty theReport.details}">
				  <div class="message message_info">
					    <dl>
					    <dt>Information Message</dt>
<c:if test="${systematicWithdrawReportForm.showCustomizeFilter == 'N' or empty systematicWithdrawReportForm.showCustomizeFilter}">
					     	<dd>1.&nbsp;<content:getAttribute id="noResultForQSearch" attribute="text"/></dd>
</c:if>
					     
<c:if test="${systematicWithdrawReportForm.showCustomizeFilter =='Y'}">
					     	<dd>1.&nbsp;<content:getAttribute id="noResultForCustomSearch" attribute="text"/></dd>
</c:if>
					     
					    </dl>
				  </div>
</c:if> <%-- report table controls --%>
</c:if>
		
		
</div>
			</bd:form>
<div class="footnotes">

<dl>
	<%-- <dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl> --%>
		<dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
</dl>

<dl>
	<dd><content:pageDisclaimer beanName="layoutPageBean" index="-1" /></dd>
</dl>
<div class="footnotes_footer"></div>
</div>

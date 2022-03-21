<%@page import="com.manulife.pension.platform.web.CommonConstants"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.platform.web.report.BaseReportForm" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryItem" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.ps.web.transaction.LoanSummaryReportForm"%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.manulife.pension.delegate.EnvironmentServiceDelegate"%>

<script type="text/javascript" src="/assets/unmanaged/javascript/jquery-ui-1.12.1_PSW.js"></script>
 <div id="errordivcs"><content:errors scope="session"/></div>

 	
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_LOAN_SUMMARY_NO_LOANS%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     beanName="loanSummaryNoLoans"/>
<c:if test="${empty param.printFriendly}" >
<style>

#detailsTable{
	width : 715
}
#itemNewDate {
	width : 50;
}
#itemName {
	width : 130;
}

#itemSSN {
	width : 80
}

#itemLoanNum {
	width : 50
}

#itemBalance {
	width : 100
}

#itemAmt {
	width : 105
}

#itemDate {
	width : 85
}
 
#itemAlert {
	width : 122
}

#errordiv {
	color: red
	
}
</style>
</c:if>

<c:if test="${not empty param.printFriendly}">
<style>

#detailsTable{
	width : 650
}

#itemNewDate {
	width : 50
}

#itemName {
	width : 130;
}

#itemSSN {
	width : 80
}

#itemLoanNum {
	width : 50
}

#itemBalance {
	width : 50
}

#itemAmt {
	width : 105
}

#itemDate {
	width : 85
}
 
#itemAlert {
	width : 72
}

#errordiv {
	color: red
	
}

</style>
</c:if>

  
 
<%
  	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
    pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
    LoanSummaryReportData theReport = (LoanSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);
    pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
    String role=(String)userProfile.getRole().getRoleId();
    pageContext.setAttribute("role",role,PageContext.PAGE_SCOPE);
    String errormessage=(String)request.getAttribute("errorcheck");
    pageContext.setAttribute("errormessage",errormessage,PageContext.PAGE_SCOPE);
    
    String maturityDateCheck = Constants.MATURITY_DATE_CUTTOFF_DATE;
  	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
  	Date utilmaturityDateCheck = sdf1.parse(maturityDateCheck);
  	java.sql.Date maturitydateComaparision = new java.sql.Date(utilmaturityDateCheck.getTime());
  	pageContext.setAttribute("maturitydateComaparision",maturitydateComaparision,PageContext.PAGE_SCOPE);

  	String isDisplayInUI = "false";
  	String loanRemortizationCutOffDate = EnvironmentServiceDelegate.getInstance()
  			.getBusinessParam("LOAN_REAMORTIZATION_REQUEST_CUTOFF_DATE");

  	if (loanRemortizationCutOffDate != null) {
  		Calendar calendar = Calendar.getInstance();
  		java.util.Date currentDate = calendar.getTime();
  		java.sql.Timestamp currentTime = new java.sql.Timestamp(currentDate.getTime());

  		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  		Date date = formatter.parse(loanRemortizationCutOffDate);
  		Calendar cal = Calendar.getInstance();
  		cal.setTime(date);
  		cal.set(Calendar.HOUR_OF_DAY, 0);
  		cal.set(Calendar.MINUTE, 0);
  		cal.set(Calendar.SECOND, 0);

  		Date maturityDate = cal.getTime();
  		Timestamp maturityTime = new Timestamp(maturityDate.getTime());

  		if (currentTime.compareTo(maturityTime) <= 0) {
  			isDisplayInUI = "true";
  		} else {
  			isDisplayInUI = "false";
  		}
  	}

  	boolean checkdisable = true;
  	if ("TPA".equals(role) && "true".equals(isDisplayInUI)) {
  		checkdisable = false;
  	}
  	pageContext.setAttribute("checkdisable", checkdisable, PageContext.PAGE_SCOPE);
  %>
  
  <script type="text/javascript">
  if ( window.history.replaceState ) {
      window.history.replaceState( null, null, window.location.href );
  }
  var isSaveButtonClicked;   
  window.onload=protectLinks;
  
  
  function protectLinks() {
	  <% if ("TPA".equals(role) && "true".equals(isDisplayInUI)) { %>
	    var hrefs  = document.links;
	    if (hrefs != null)
	    { 
	      for (i=0; i<hrefs.length; i++) {
	        if(
	          hrefs[i].onclick != undefined && 
	          (hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("doSignOut") != -1)
	        ) {
	        }
	        else if(
	          hrefs[i].href != undefined && 
	          (hrefs[i].href.indexOf("openWin") != -1 || hrefs[i].href.indexOf("popup") != -1 || hrefs[i].href.indexOf("doSignOut") != -1)
	        ) {
	        }
	        else if(hrefs[i].onclick != undefined) {
	          hrefs[i].onclick = new Function ("var result = confirmDiscardChanges ('Warning! The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
	        }
	        else{
	        	 hrefs[i].onclick = new Function ("return confirmDiscardChanges ('Warning! The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return.');");
	        }
	      }
	    }
	    <% } %>
	   }  
 
 function confirmDiscardChanges(warning) {
      if (isSaveButtonClicked==false) {
          return window.confirm(warning);
      } else {
        isSaveButtonClicked=false;
          return true;
        }
}  
  

function setDirtyFlag(fieldValueChanged){
	isSaveButtonClicked=fieldValueChanged;
	if(fieldValueChanged == true)
	{
		document.loanSummaryReportForm.action="?task=save";
		document.loanSummaryReportForm.submit();	
	}
}
  
	   

</script> 

<jsp:useBean id="loanSummaryReportForm"  class="com.manulife.pension.ps.web.transaction.LoanSummaryReportForm" />
<c:if test="${not empty errormessage}" >
 <div id="errordiv"><b>${errormessage}</b></div>
</c:if>

	<ps:form  method="POST" modelAttribute="loanSummaryReportForm" name="loanSummaryReportForm" action="/do/transaction/loanSummaryReport/">
	  
	  	  
	  <table border="0" cellspacing="0" cellpadding="0" id="detailsTable">
        <tr>
		  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="itemNewDate"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="itemName"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="itemLoanNum"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		   <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		   <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		   <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="itemBalance"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="itemAmt"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="itemDate"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		   <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="itemAlert"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="tablehead">
		  <td class="tableheadTD1" valign="middle" colspan="9">
            <p><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b> as of  <render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theReport.asOfDate"/>
          </td>
          <td class="tableheadTD" align="middle" colspan="8">
		  <b>
   		    <c:if test="${empty param.printFriendly}" >
				<report:recordCounter report="theReport" label="Loans"/>
			</c:if>
		  </b></td>
          <td class="tableheadTD" colspan="8" align="right"><report:pageCounter report="theReport" name="loanSummaryReportForm" formName="loanSummaryReportForm" /></td>
        </tr>

          <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

                <td valign="center" align="left" colspan="5"><b>Total outstanding loan balance:</b></td>
                <td align="right" valign="center" colspan="2"><b><span class="highlight"><render:number property="theReport.outstandingBalance" type="c"/></span></b></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td colspan="6"></td>
					<td valign="top" align="left" colspan="4"><b>Number of loans:</b></td>
					<td align="right" valign="top" colspan="3"><b><span class="highlight" id="numOfLoans"><render:number property="theReport.numLoans" type="i" /></span></b></td>
					<td/>
				<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

			 <tr class="datacell1">
				 <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				 <td colspan="9"></td>
				 <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				 <td colspan="3"></td>
                <td valign="top" align="left" colspan="7"><b>Number of participants with loans:</b></td>
                <td align="right" valign="top" colspan="1"><b><span class="highlight"><render:number property="theReport.numParticipants" type="i" /></span></b></td>
				<td/>
				<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              </tr>  

        </tr>

        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="beigeborder" colspan="20"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr> 
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
          <td colspan="22"><img src="/assets/unmanaged/images/s.gif" height="10"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
        </tr> 
        <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		   <td align="left" valign="top"><b>Extend Loan Maturity</b></td>
		  <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td align="left" valign="top"><report:sort field="<%=LoanSummaryItem.SORT_NAME%>" direction="asc" formName="loanSummaryReportForm"><b>Name</b></report:sort></td>
		  <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td align="left" valign="top"><b><report:sort field="<%=LoanSummaryItem.SORT_LOAN_NUMBER%>" direction="desc" formName="loanSummaryReportForm">Loan number</report:sort></b></td>
		  <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td align="left" valign="top"><b><report:sort field="<%=LoanSummaryItem.SORT_ISSUE_DATE%>" direction="desc" formName="loanSummaryReportForm">Issue date*</report:sort></b></td>
		  <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td align="right" valign="top"><b><report:sort field="<%=LoanSummaryItem.SORT_INTEREST_RATE%>" direction="desc" formName="loanSummaryReportForm">Interest rate</report:sort>(%)</b></td>
		  <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top"><b><report:sort field="<%=LoanSummaryItem.SORT_ORIGINAL_LOAN_AMT%>" direction="desc" formName="loanSummaryReportForm">Original loan amount*</report:sort>($)</b></td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top"><b><report:sort field="<%=LoanSummaryItem.SORT_OUTSTANDING_BALANCE%>" direction="desc" formName="loanSummaryReportForm">Outstanding balance</report:sort>($)</b></td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top"><b><report:sort field="<%=LoanSummaryItem.SORT_LAST_REPAYMENT_AMT%>" direction="desc" formName="loanSummaryReportForm">Last payment</report:sort>($)</b></td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="left" valign="top"><report:sort field="<%=LoanSummaryItem.SORT_LAST_REPAYMENT_DATE%>" direction="desc" formName="loanSummaryReportForm"><b>Last payment date</b></report:sort></td>
		  <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="left" valign="top"><report:sort field="<%=LoanSummaryItem.SORT_MATURITY_DATE%>" direction="desc" formName="loanSummaryReportForm"><b>Maturity date</b></report:sort></td>
          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="left" colspan="2" valign="top"><report:sort field="<%=LoanSummaryItem.SORT_ALERT%>" direction="desc" formName="loanSummaryReportForm"><b>Alert</b></report:sort></td>
		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		 </tr>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >


<c:choose><c:when test="${theIndex.index % 2 == 0}">
 <tr class="datacell1"></c:when><c:otherwise><tr class="datacell2"></c:otherwise>
</c:choose>

	                       
<%--         <% if (theIndex.intValue() % 2 == 0) { %> --%>
<!--           <tr class="datacell1"> -->
<%--         <% } else { %> --%>
<!--           <tr class="datacell2"> -->
<%--         <% } %>         --%>

          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td valign="top">
		  <c:if test="${theItem.maturityDate gt maturitydateComaparision}">
		  <form:checkbox  id ="loanDetails" path="loanDetails"  onclick ="setDirtyFlag(false)" checked="${theItem.maturityDateExtended}" disabled="${checkdisable}" value="${theItem.loanNumber}:${theItem.maturityDate}:${theItem.participantID}"/>
		  </c:if>
		   <c:if test="${theItem.maturityDate le maturitydateComaparision}">
		  <form:checkbox  id ="loanDetails" path="loanDetails"  checked="${theItem.maturityDateExtended}" disabled="true" value="${theItem.loanNumber}:${theItem.maturityDate}:${theItem.participantID}"/>
		  </c:if>
		  </td>
		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top">
				<ps:link action="/do/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="profileId">
${theItem.name}
				</ps:link><br/>
				<render:ssn property="theItem.ssn"/>
		  </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top">
				  <ps:map id="parameterMap">
				  		<ps:param name="task" value="filter"/>
						<ps:param name="loanNumber" valueBeanName="theItem" valueBeanProperty="loanNumber"/>
						<ps:param name="profileId" valueBeanName="theItem" valueBeanProperty="profileId"/>
				  </ps:map> 

  				  <ps:link action="/do/transaction/loanRepaymentDetailsReport/" name="parameterMap">
${theItem.loanNumber}<c:if test="${empty param.printFriendly }" >(details)</c:if>
			</ps:link>
		  </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td align="left" valign="top">
		     <render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theItem.creationDate"/>
		  </td> 
		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		 <td align="right" valign="top">
		   <render:number property="theItem.interestRate" pattern="###.##"/>
		 </td>
		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		 <td align="right" valign="top"> 
		   <render:number property="theItem.loanAmt" type="c" sign="false"/>
		 </td>
		  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top">
<c:if test="${not empty theItem.outstandingBalance}">
	                  <render:number property="theItem.outstandingBalance" type="c" sign="false"/>
</c:if>
		  </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" valign="top">
<c:if test="${not empty theItem.lastRepaymentAmt}">
				  <c:choose><c:when test="${theItem.isNoRepayment()}">n/a</c:when>
				  <c:otherwise>
				  <render:number property="theItem.lastRepaymentAmt" type="c" sign="false"/>
				  </c:otherwise>
				  </c:choose>
				  
<%-- 				  <% if (theItem.isNoRepayment()) { %> --%>
<!--                         n/a -->
<%--                   <% } else { %> --%>
<%--    		  		        <render:number property="theItem.lastRepaymentAmt" type="c" sign="false"/> --%>
<%--    		  		  <% } %> --%>
</c:if>
		  </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="left" valign="top">
<c:if test="${not empty theItem.lastRepaymentDate}">
<c:choose><c:when test="${theItem.isNoRepayment()}"> n/a</c:when>
<c:otherwise>
<%-- 				  <% if (theItem.isNoRepayment()) { %> --%>
<!--                         n/a -->
<%--                   <% } else { %> --%>
   		  		  
				  	<ps:map id="parameterMap">
				  		<ps:param name="task" value="filter"/>
						<ps:param name="transactionNumber" valueBeanName="theItem" valueBeanProperty="lastRepaymentTransactionNo"/>
						<ps:param name="transactionDate" valueBeanName="theItem" valueBeanProperty="lastRepaymentDate"/>
				  	</ps:map> 

  				  	<ps:link action="/do/transaction/loanRepaymentTransactionReport/" name="parameterMap"> 
						<render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theItem.lastRepaymentDate"/>
					</ps:link>
<%-- 				<% } %> --%>
				</c:otherwise>
</c:choose>
</c:if>
		  </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td align="left" valign="top">
				<render:date patternOut= "<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theItem.maturityDate"/>
		  </td>
		   <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="left" valign="top" colspan="2">
<c:forEach items="${theItem.alerts}" var="theAlert" >
${theAlert}<br>
</c:forEach>
		  </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:forEach>
</c:if>

<c:if test="${empty theReport.details}">
          <tr class="datacell1">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			  <td valign="top" colspan="22" id="noLoansCell"><b><span id="noLoans"><content:getAttribute beanName="loanSummaryNoLoans" attribute="text"/></span></b></td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:if>

        <ps:roundedCorner numberOfColumns="24"
                          emptyRowColor="white"
                          oddRowColor="white"
                          evenRowColor="beige"
	                      name="theReport" 
	                      property="details"/>

		<tr>
			<td colspan="22" align="right"><report:pageCounter  name="loanSummaryReportForm" report="theReport" arrowColor="black" formName="loanSummaryReportForm" /></td>
		</tr>
		
		
		<tr>
			<td colspan="22">
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
  			</td>
		</tr>

      </table>
<c:if test="${checkdisable eq 'false' && not empty theReport.details}">
 <div align="right">
 <input type="submit"  class="button100Lg"  name="action" onclick="return setDirtyFlag(true);" value="Save"/>
 </div>
 </c:if>
 
 <c:if test="${checkdisable eq 'false' && empty theReport.details}"> 
 <div align="right">
 <input type="submit"  name="action" value="Save" disabled="true"/>
 </div>
 </c:if>
 

	  </ps:form>

<c:if test="${not empty param.printFriendly}" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>

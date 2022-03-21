<%@taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@page import="com.manulife.pension.ps.web.Constants" %>
<%@page import="com.manulife.util.render.RenderConstants"%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<script type="text/javascript">
	
	function doOnload() {
		var reportType = document.getElementsByName("reportType");
		for (var i = 0; i < reportType.length; i++) {
			if (reportType[i].checked &&
					reportType[i].value != "PPT") {
					hideSSN();
				}
		}
		var infoLevel = document.getElementsByName("infoLevel");
		for (var i = 0; i < infoLevel.length; i++) {
			if (infoLevel[i].checked) {
					disableSSN();
				}
			
		}
	}
	function doSubmit(action) {
		document.partStmtFeesToolForm.task.value=action;
		document.partStmtFeesToolForm.submit();
	}

	function hideSSN() {
		document.getElementById("ssn").style.display = "none";
		document.getElementById("ssnLabel").style.display = "none";
	}

	function hideErrors() {
		document.getElementById("errordivcs").style.display = "none";
	}

	function disableSSN() {
		document.getElementById("ssn1").disabled = true;
		document.getElementById("ssn2").disabled = true;
		document.getElementById("ssn3").disabled = true;
	}

	function displaySSN() {
		
		document.getElementById("ssn").style.display = "block";
		document.getElementById("ssnLabel").style.display = "block";
		document.getElementById("ssn1").value ="";
		document.getElementById("ssn2").value ="";
		document.getElementById("ssn3").value ="";
		document.getElementById("ssn1").disabled = false;
		document.getElementById("ssn2").disabled = false;
		document.getElementById("ssn3").disabled = false;
	}

	function hideSearchParticipantDates() {
		document.getElementById("searchParticipantDates").style.display = "none";
		document.getElementById("searchResult").style.display = "none";
	}

	function hideSearchResult() {
		document.getElementById("searchResult").style.display = "none";
	}

	function searchFocus(input) {
		if(input.value.length == 4) {
			document.getElementById('search').focus();
		}
	}

</script>

<jsp:useBean id="partStmtFeesToolForm" scope="session" type="com.manulife.pension.ps.web.fee.PartStmtFeesToolForm" />



<content:contentBean contentId="<%=ContentConstants.PPT_SEARCH_TITLE%>" 
            type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="searchTitle" />
<content:contentBean contentId="<%=ContentConstants.PPT_SEARCH_RESULT%>" 
            type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="searchResult" />

  
<td width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
<td>
<ps:form method="post"  action="/do/fee/partStmtFeesTool/" modelAttribute="partStmtFeesToolForm" name="partStmtFeesToolForm">
<input type="hidden" name="task"/>
<table width="730" border="0" cellspacing="0" cellpadding="0"> 
  <tr>
    <td width="510" ><img src="/assets/unmanaged/images/s.gif" width="510" height="1"></td>
    <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
    <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
    <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
  </tr>
     
  <tr>
     <td valign="top">
      <img src="/assets/unmanaged/images/s.gif" width="510" height="5"><br>
      <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
      
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="15"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        <td valign="top" width="495">
        <!--Participant Statement Fees Tool Search Request box start -->
        <!-- error section -->
				<div id="errordivcs"><content:errors scope="session" /></div>
				
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        	<tr class="tablehead">
        		<td class="tableheadTD1" colspan="3"><b><content:getAttribute beanName="searchTitle" attribute="text" /></b></td>
        	</tr>
        	<tr>
        		<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
				border="0" width="1" height="1"></td>
        		<td class="databox" width="100%">
        			<table width="100%" border="0" cellspacing="0" cellpadding="0">
        				<tr class="datacell1">
        					<td width="35%"><strong>Report type</strong></td>
        					<td width="35%">
<form:radiobutton onclick="hideSearchParticipantDates();displaySSN();hideErrors();" path="reportType" value="PPT"/>Participant
<form:radiobutton onclick="hideSSN();hideSearchParticipantDates();hideErrors();" path="reportType" value="contract" />Contract
        					</td>
        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        				</tr>
        				
        				<tr class="datacell1">
        					<td width="35%">
        						<div id="ssnLabel" ><strong>Participant SSN</strong> </div>
        					</td>
        					<td width="35%">
        						<div id="ssn" >
<form:password path="ssn1" value="${partStmtFeesToolForm.ssn1}" property="ssn1" id="ssn1" cssClass="inputField"  onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
<form:password path="ssn2" value="${partStmtFeesToolForm.ssn2}" property="ssn2" id="ssn2" cssClass="inputField"  onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2"/>
<form:input path="ssn3" property="ssn3" id="ssn3" autocomplete="off" cssClass="inputField"  onkeyup="return searchFocus(this);"  size="4" maxlength="4"/>
<%-- <ps:password  name="partStmtFeesToolForm" property="ssn1"  styleId="ssn1" style="inputField"  onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
<ps:password  name="partStmtFeesToolForm" property="ssn2"  styleId="ssn2" style="inputField"  onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2"/>
<ps:text  name="partStmtFeesToolForm" property="ssn3" autocomplete="off" styleId="ssn3" style="inputField"  onkeyup="return searchFocus(this);"  size="4" maxlength="4"/>
 --%>								</div>
        					</td>
        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        				</tr>
        				
        				<tr class="datacell1" height="10">
        					<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
        				</tr>
        				
        				<tr class="datacell1">
        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
<td width="35%" align="right"><input type="button" onclick="return doSubmit('reset')" name="button" class="button100Lg" value="reset"/></td>

        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
<td width="27%"><input type="button" onclick="doSubmit('search');" name="button" class="button100Lg" id="search" value="search"/></td>

        				</tr>
        				<tr class="datacell1" height="10">
        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        				</tr>
        			</table>        			
        		</td>
        		<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
				border="0" width="1" height="1"></td>
        	</tr>
        	<tr>
        		<td colspan="3" class="boxborder" width="1"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        	</tr>
        	
        	<tr>
        		<td colspan="3" width="0">
        		<div id="searchParticipantDates" >
<c:if test="${partStmtFeesToolForm.searchResultAllowed ==true}">
        		<table width="100%" border="0" cellspacing="0" cellpadding="0">        			
		        	<tr>
		        		<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
						width="1" height="1"></td>
		        		<td class="databox" width="100%">
		        			<table width="100%" border="0" cellspacing="0" cellpadding="0">
		        				<tr class="datacell1" height="10">
		        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="5"></td>
		        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="5"></td>
		        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        				</tr>
<c:if test="${partStmtFeesToolForm.statementEndDatesAvailable ==false}">
		        				<tr class="datacell1">
		        					<td width="35%"><strong>Name</strong></td>
		        					<td width="35%">
${partStmtFeesToolForm.name}
		        					</td>
		        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        				</tr>
</c:if>
<c:if test="${partStmtFeesToolForm.statementEndDatesAvailable ==true}">
<c:if test="${partStmtFeesToolForm.reportType == 'PPT'}">
		        				<tr class="datacell1">
		        					<td width="35%"><strong>Name</strong></td>
		        					<td width="35%">
${partStmtFeesToolForm.name}
		        					</td>
		        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        				</tr>
</c:if>
		        				<tr class="datacell1">
		        					<td width="35%"><strong>Statement period end date</strong></td>
		        					<td width="35%">
		        						<ps:select name="partStmtFeesToolForm" property="selectedStmtDate" style="width:100px;"
		        							onchange="hideSearchResult();">
											<ps:dateOptions name="partStmtFeesToolForm" property="stmtPeriodEndDates"
														patternOut="MMM dd, yyyy" />
										</ps:select>
		        					</td>
		        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        				</tr>
		        				<tr class="datacell1">
		        					<td width="35%"><strong>Information level</strong></td>
		        					<td width="65%" colspan="3">
 	<form:radiobutton onclick="hideSearchResult();" path="infoLevel" value="Party"/>Services
<form:radiobutton onclick="hideSearchResult();" path="infoLevel" value="Summary"/>Summary
<form:radiobutton onclick="hideSearchResult();" path="infoLevel" value="Detailed"/>Detailed
		        					</td>
		        				</tr>
		        				<tr class="datacell1" height="10">
		        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
		        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
		        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
		        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
		        				</tr>
		        				<tr class="datacell1">
		        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
<td width="27%"><input type="button" onclick="return doSubmit('submit')" name="button" class="button100Lg" value="submit"/></td>

		        				</tr>
</c:if>
		        				<tr class="datacell1" height="10">
		        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
		        					<td width="35%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
		        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
		        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="3"></td>
		        				</tr>
		        			</table>
		        		</td>
		        		<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
						width="1" height="1"></td>
		        	</tr>
		        	<tr>
		        		<td colspan="3" class="boxborder" width="1"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		        	</tr>		        	
        		</table>
</c:if>
        		</div>
				</td>				
        	</tr>
        	
        </table>
        <!--Participant Statement Fees Tool Search Request box end -->
        
        <br/>
        <!--Search Result box start -->
        <div id="searchResult" >
<c:if test="${partStmtFeesToolForm.searchResultAllowed ==true}">
<c:if test="${partStmtFeesToolForm.downloadAllowed ==true}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="box">
        	<tr>
				<td width="1"><img src="/assets/unmanaged/images/s.gif"
					width="1" height="1"></td>
				<td width="100%"><img src="/assets/unmanaged/images/s.gif"
					width="178" height="1"></td>
				<td width="1"><img src="/assets/unmanaged/images/s.gif"
					width="1" height="1"></td>
			</tr>
        	<tr class="tablehead">
        		<td class="tableheadTD1" colspan="3"><b><content:getAttribute beanName="searchResult" attribute="text" /></b></td>
        	</tr>
        	<tr>
        		<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
        		<td class="databox" width="100%">
        			<table width="100%" border="0" cellspacing="0" cellpadding="0">
        				<tr class="datacell1">
        					<td width="70%">
<strong>${partStmtFeesToolForm.requestStatus} <%-- filter="false" --%></strong>
        					</td>
        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        				</tr>
        				<tr class="datacell1">
        					<td width="70%" align="right"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
        					<td width="27%">
<c:if test="${partStmtFeesToolForm.requestSuccess ==true}">
<input type="button" onclick="return doSubmit('download')" name="button" class="button100Lg" value="download"/>

</c:if>
<c:if test="${partStmtFeesToolForm.opsInvestgation ==true}">
        						<c:if test="${userProfile.role.roleId eq 'PSC' || userProfile.role.roleId eq 'PTL'
        							|| userProfile.role.roleId eq 'BGC'}" >
<input type="button" onclick="return doSubmit('download')" name="button" class="button100Lg" value="download"/>

        						</c:if>
</c:if>
        					</td>
        				</tr>
        				<tr class="datacell1" height="10">
        					<td width="70%"><img src="/assets/unmanaged/images/s.gif" width="5" height="5"></td>
        					<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="5" height="5"></td>
        					<td width="27%"><img src="/assets/unmanaged/images/s.gif" width="5" height="5"></td>
        				</tr>
        			</table>        			
        		</td>
        		<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
        	</tr>
        	<tr>
        		<td colspan="3" class="boxborder" width="1"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        	</tr>
        </table>
</c:if>
</c:if>
        </div>
        <!--Search Result box end -->
        
        </td>
      </tr>
    </table>
    
    </td>
  </tr>
  <tr>
   <td>
     <p><content:pageFooter beanName="layoutPageBean"/></p>
     <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
     <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
   </td>
  </tr>
</table>
</ps:form>
</td>
<td valign="top"><img src="/assets/unmanaged/images/s.gif"
  height="59">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
  class="box">
  <tr>
    <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
  </tr>
</table>
<img src="/assets/unmanaged/images/s.gif" height="20"></td>

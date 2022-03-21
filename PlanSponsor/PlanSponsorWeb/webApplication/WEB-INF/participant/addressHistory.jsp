<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantAddressHistoryReportForm" %>
<%@ page import="com.manulife.pension.service.security.Principal" %>
<%@ page import="com.manulife.pension.service.security.role.UserRole" %>
<%@ page import="com.manulife.pension.tools.render.AddressChangeTrackingHelper" %>
<%@page import="com.manulife.pension.service.employee.valueobject.AddressVO" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistoryReportData" %>
 <%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistory" %>       
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%> 
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psMainTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psTabNav.css" type="text/css">


<%boolean errors = false;%>
<%
ParticipantAddressHistoryReportData theReport = (ParticipantAddressHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<jsp:useBean id="participantAddressHistoryReportForm" scope="session" type="com.manulife.pension.ps.web.participant.ParticipantAddressHistoryReportForm" />




<content:contentBean contentId="<%=ContentConstants.MESSAGE_PARTICIPANTS_VIEW_ALL%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="viewAllParticipants"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_PARTICIPANTS_NO_SEARCH_RESULTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="noSearchResults"/>


<c:if test="${empty param.printFriendly}" >

<script type="text/javascript" >

function doReset() {
	document.participantAddressHistoryReportForm.task.value = "default";
	return true;
}

function clearName(evt){

	//IE or browsers that use the getElementById model
	if (document.getElementById('namePhrase')) {
		if (document.getElementById('namePhrase').value) {
			document.getElementById('namePhrase').value = "";
		}
	}


	//Netscape or browsers that use the document model
  	evt = (evt) ? evt : (window.event) ? event : null;
  	if (evt)
  	{
    	var charCode = (evt.charCode) ? evt.charCode :
                   ((evt.keyCode) ? evt.keyCode :
                   ((evt.which) ? evt.which : 0));
    	if (charCode == 9) {
    		return false;
    	}
  	}

	if (document.participantAddressHistoryReportForm.ssnOne) {
			if (document.participantAddressHistoryReportForm.ssnOne.value.length >= 0){
					document.participantAddressHistoryReportForm.namePhrase.value = "";
			}
	}


	if (document.participantAddressHistoryReportForm.ssnTwo) {
			if (document.participantAddressHistoryReportForm.ssnTwo.value.length >= 0){
				document.participantAddressHistoryReportForm.namePhrase.value = "";
			}
	}

	if (document.participantAddressHistoryReportForm.ssnThree) {
			if (document.participantAddressHistoryReportForm.ssnThree.value.length >= 0){
				document.participantAddressHistoryReportForm.namePhrase.value = "";
			}
	}

}

function clearSSN(evt){

	//IE or browsers that use the getElementById model
	if (document.getElementById('ssnOne')) {
		if (document.getElementById('ssnOne').value) {
			document.getElementById('ssnOne').value = "";
		}
	}


	if (document.getElementById('ssnTwo')) {
		if (document.getElementById('ssnTwo').value) {
			document.getElementById('ssnTwo').value = "";
		}
	}

	if (document.getElementById('ssnThree')) {
		if (document.getElementById('ssnThree').value) {
			document.getElementById('ssnThree').value = "";
		}
	}

	//Netscape or browsers that use the document model
	evt = (evt) ? evt : (window.event) ? event : null;
  	if (evt)
  	{
    	var charCode = (evt.charCode) ? evt.charCode :
                   ((evt.keyCode) ? evt.keyCode :
                   ((evt.which) ? evt.which : 0));
    	if (charCode == 9) {
    		return false;
    	}
  	}


	if (document.participantAddressHistoryReportForm.namePhrase) {
			if (document.participantAddressHistoryReportForm.namePhrase.value.length >= 0){
				document.participantAddressHistoryReportForm.ssnOne.value = "";
				document.participantAddressHistoryReportForm.ssnTwo.value = "";
				document.participantAddressHistoryReportForm.ssnThree.value = "";
			}
	}

}

</script>

</c:if>
<c:set var="showDivColumn1" value="${userProfile.getCurrentContract().hasSpecialSortCategoryInd()}"/>
<%
   // optional columns
   boolean readOnly = (request.getParameter("printFriendly") !=null);
  boolean showDivColumn  = userProfile.getCurrentContract().hasSpecialSortCategoryInd();
   int divColumn = (showDivColumn ? 2: 0);
   int columnSize = 18 + divColumn;
   pageContext.setAttribute("readOnlyflag",readOnly,PageContext.PAGE_SCOPE); 
%>

<ps:form method="POST" modelAttribute="participantAddressHistoryReportForm" name="participantAddressHistoryReportForm" action="/do/participant/addressHistory">


<table  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <c:if test="${empty param.printFriendly }" >
      <td>
       <img src="/assets/unmanaged/images/s.gif" width="30" height="1"><br>
       <img src="/assets/unmanaged/images/s.gif" height="1">
      </td>
    </c:if>

    <td  valign="top">
   		 <p>
   		 	<c:if test="${sessionScope.psErrors ne null}">
<c:set var="errorsExist" value="true" scope="page" />
        	<div id="errordivcs"><content:errors scope="session"/></div>
        	<%errors=true;%>
        	</c:if>
        </p>

	  <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">

<input type="hidden" name="task" value="filter"/>

        <tr class="tablehead">
            <td class="filterSearch" colspan="<%=columnSize%>">

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="filterSearch" colspan="3">
                To search for an employee by last name or SSN, make your selection below and click "search" to complete your request.
                </td>
              </tr>
              <tr>
              	<td colspan="3">
                  <table width="100%">
                  	<tr>
	            	  <td colspan=4><b>Segment</b></td>
                      <td><b>Last name</b></td>
                      <td><b>SSN</b></td>
                      <% if (showDivColumn) { %>
	                      <td><b>Division</b></td>
	                  <% } else { %>
	                      <td></td>
	                  <% } %>
                    </tr>
                    <tr>
                      <td colspan=4>
	            	     <form:select path="segment" disabled="${readOnlyflag}">
	            	 			<form:option value="-">All employees</form:option>
	            	 			<form:option value="P">Account holders</form:option>
	            	 			<form:option value="N">Non-account holders</form:option>
</form:select>
                      </td>
                      <td>
<form:input path="namePhrase" onchange="setFilterFromInput(this);" onkeypress="clearSSN(event);" readonly="${readOnlyflag}" cssClass="inputField"/>


                      </td>
                      <td>
                      <form:password path="ssnOne" styleClass="inputField" onkeypress="clearName(event);"
						        readonly="<%=readOnly%>" value="${participantAddressHistoryReportForm.ssnOne}"
								onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3" />
						<form:password path="ssnTwo" styleClass="inputField" onkeypress="clearName(event);"
						        readonly="<%=readOnly%>" value="${participantAddressHistoryReportForm.ssnTwo}"
								onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2" />
						<form:input path="ssnThree" autocomplete="off" styleClass="inputField"  onkeypress="clearName(event);"
						        readonly="<%=readOnly%>"
								onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4" />
                      <td>
                      <% if (showDivColumn) { %>
<form:input path="division" onchange="setFilterFromInput(this);" readonly="${readOnlyflag}" cssClass="inputField"/>


	                  <% } %>
                      </td>
                    </tr>
                    <tr>
                      <td colspan=4><b>Last Updated</b></td>
                      <td><b>Employment status<b></td>
                      <td></td>
                      <td></td>
                    </tr>
                    <tr>
                     <td>From</td>
<td><form:input path="fromDate" maxlength="10" readonly="<%=readOnly%>" size="10" cssClass="inputField"/></td>
                     <td>to</td>
<td><form:input path="toDate" maxlength="10" readonly="<%=readOnly%>" size="10" cssClass="inputField"/></td>
                      <td>
	            	 	  <form:select path="status" disabled="${readOnlyflag}">
	            	 			<form:option value="-">All</form:option>
	            	 			<form:option value="A">Active</form:option>
<% if (userProfile.isInternalUser()) { %>
	            	 			<form:option value="C">Cancelled</form:option>
<% } %>
	            	 			<form:option value="D">Deceased</form:option>
	            	 			<form:option value="P">Disabled</form:option>
	            	 			<form:option value="R">Retired</form:option>
	            	 			<form:option value="T">Terminated</form:option>
</form:select>
                      </td>
                      <td>
                      </td>
                      <td>
                        <c:if test="${empty param.printFriendly }" >
	            	      <input type="submit" name="submit" value="search"  />
                          <input type="submit" name=" reset " value="reset" onclick="return doReset();"/>
                        </c:if>
                      </td>
                    </tr>
                    <tr>
                      <td></td>
                      <td>(mm/dd/yyyy)</td>
                      <td></td>
                      <td>(mm/dd/yyyy)</td>
                    </tr>
                  </table>

              	</td>

              </tr>
            </table>
          </td>
        </tr>
</table>


</br>&nbsp;

<table width="100%" border="0" cellspacing="0" cellpadding="0">

  	   <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="153"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="180"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>

          <!-- new stuff for addr history -->
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="150"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
<% if (showDivColumn) { %>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="153"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
<% } %>
          <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   </tr>


       <tr class="tablehead">
            <td class="tableheadTD1" colspan="<%=columnSize%>">

			<table  border="0" cellspacing="0" cellpadding="0" width="100%">
			          <tr>
			            <%if(errors==false){%>
			              <td class="tableheadTD"></td>
			              <td align="center" class="tableheadTDinfo"><b><report:recordCounter report="theReport" label="Records"/></b></td>
			              <td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="participantAddressHistoryReportForm"/></td>
			            <%} else { %>
			              <td class="tableheadTD"></td>
			              <td align="left" class="tableheadTDinfo">&nbsp;</td>
			              <td align="right" class="tableheadTDinfo"></td>
			            <%} %>
			          </tr>

			</table>
	      </td>
       </tr>

       <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

          <td><b><report:sort field="lastName" direction="asc" formName="participantAddressHistoryReportForm">Name</b></report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b>Address</b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b><report:sort field="city" direction="asc" formName="participantAddressHistoryReportForm">City</b></report:sort></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b><report:sort field="stateCode" direction="asc" formName="participantAddressHistoryReportForm">State</report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b><report:sort field="zip" direction="asc" formName="participantAddressHistoryReportForm">Zip</b></report:sort></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b><report:sort field="country" direction="asc" formName="participantAddressHistoryReportForm">Country</report:sort></b></td>

          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td><b><report:sort field="update" direction="asc" formName="participantAddressHistoryReportForm">Update</b></report:sort></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% if (showDivColumn) { %>
          <td><b><report:sort field="source" direction="asc" formName="participantAddressHistoryReportForm">Source</report:sort></b></td>
          <td class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td colspan="2"><b><report:sort field="division" direction="asc" formName="participantAddressHistoryReportForm">Division</report:sort></b></td>
<% } else { %>
          <td colspan="2"><b><report:sort field="source" direction="asc" formName="participantAddressHistoryReportForm">Source</report:sort></b></td>
<% } %>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>


<%if(errors==false){%>
<c:if test="${empty param.printFriendly}" >
<%
	if (!StringUtils.isEmpty(participantAddressHistoryReportForm.getNamePhrase()) ||
		!participantAddressHistoryReportForm.getSsn().isEmpty() ||
		!StringUtils.isEmpty(participantAddressHistoryReportForm.getFromDate()))
	{
%>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<% if (theReport.getDetails() != null && theReport.getDetails().size() > 0) { // we have search results %>
			<td valign="top" colspan="<%=18+divColumn%>">
		<% } else { // no results %>
		    <td valign="top" colspan="<%=16+divColumn%>">
			<c:if test="${empty errorsExist}">
				There are no employees address history records
		</c:if>
		<% } %>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<% } %>
</c:if>
<% } %>


<% if(errors==false){
   java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM d, yyyy");
   UserRole myRole = userProfile.getPrincipal().getRole();
%>

<c:if test="${not empty theReport.details}">


<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<%
ParticipantAddressHistory theItem = (ParticipantAddressHistory)pageContext.getAttribute("theItem");
%>
<c:set var="indexValue" value="${theIndex.index}"/>
<% String temp = pageContext.getAttribute("indexValue").toString(); 
if (Integer.parseInt(temp)% 2 == 0) { %>
        <tr class="datacell2">
<% } else { %>
        <tr class="datacell1">
<% } %>

<% boolean missingAddress = (
 ((theItem.getAddressLine1() == null) || (theItem.getAddressLine1().trim().length() == 0))
 && ((theItem.getEmployerProvidedEmailAddress() == null) || (theItem.getEmployerProvidedEmailAddress().trim().length() == 0))); %>

          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

          <td valign="top" <% if (missingAddress) { %> class="highlight" <% } %> >
   	    <logicext:if name="theItem" property="accountHolder" op="equal" value="true">
		<logicext:then>
                  <c:if test="${empty param.printFriendly }" >
                    <ps:link action="/do/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="profileId">
${theItem.lastName}, ${theItem.firstName} ${theItem.middleInitial}
	            </ps:link>
	          </c:if>
                  <c:if test="${not empty param.printFriendly }" >
<u>${theItem.lastName}, ${theItem.firstName} ${theItem.middleInitial}</u>
	          </c:if>
	        </logicext:then>
		<logicext:else>
${theItem.lastName}, ${theItem.firstName} ${theItem.middleInitial}
	        </logicext:else>
            </logicext:if>
	        <br>
            <render:ssn property="theItem.ssn"/>
          </td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
          <% if (missingAddress) { %>
              <td valign="top" class="highlight">Address missing</td>
          <% } else { %>
          		<% if (theItem.getEmployerProvidedEmailAddress().length() > 0 ) { %>
              <td valign="top"><%=theItem.getEmployerProvidedEmailAddressNotNull()%></td>
          		<% } else { %>
              <td valign="top"><%=theItem.getAddressLine1()%><br><%=theItem.getAddressLine2NotNull()%></td>
                <% } %>
          <% } %>
          <td class="datadivider" valign="top" width="1" height="1"></td>
          <td valign="top" nowrap><%=theItem.getCity()%></td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
          <td valign="top"><%=theItem.getStateCode()%></td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
<td valign="top" nowrap>${theItem.zipCode}</td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
          <td valign="top"><%=theItem.getCountry()%></td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
          <td valign="top" nowrap><%=dateFormat.format(theItem.getUpdateDate())%></td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
<% if (showDivColumn) { %>
  <% if (myRole.isInternalUser() && AddressVO.INTERNAL_USER.equals(theItem.getUserTypeCode())) { %>
          <td valign="top" title="<%=theItem.getHoverText(myRole.isInternalUser())%>" nowrap>
  <% } else { %>
          <td valign="top" nowrap>
  <% } %>
             <%=AddressChangeTrackingHelper.getRenderedUpdatedSource(theItem.getSource())%>
		     <br/>
             <%=AddressChangeTrackingHelper.getRenderedUpdatedBy(AddressChangeTrackingHelper.PSW_EDIT_ADRESS_OR_ADDRESS_HISTORY,
                                 theItem.getUserTypeCode(), theItem.getSource(),
                                 theItem.getChangedByFirstName(), theItem.getChangedByLastName()) %>

          </td>
          <td class="datadivider" valign="top" width="1" height="1"></td>
<td valign="top" colspan="2">${theItem.division}</td>
<% } else { %>
  <% if (myRole.isInternalUser() && AddressVO.INTERNAL_USER.equals(theItem.getUserTypeCode())) { %>
          <td valign="top" title="<%=theItem.getHoverText(myRole.isInternalUser())%>" colspan="2" nowrap>
  <% } else { %>
          <td valign="top" colspan="2" nowrap>
  <% } %>
		    <%=AddressChangeTrackingHelper.getRenderedUpdatedSource(theItem.getSource())%>
		    <br/>
            <%=AddressChangeTrackingHelper.getRenderedUpdatedBy(AddressChangeTrackingHelper.PSW_EDIT_ADRESS_OR_ADDRESS_HISTORY,
                                 theItem.getUserTypeCode(), theItem.getSource(),
                                 theItem.getChangedByFirstName(), theItem.getChangedByLastName()) %>

		  </td>
<% }  %>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

        </tr>
</c:forEach>
</c:if>
<% } %>

             <ps:roundedCorner numberOfColumns='<%=String.valueOf(columnSize)%>'
                          emptyRowColor="white"
                          oddRowColor="beige"
                          evenRowColor="white"
                          name="theReport"
	                  property="details"/>
	   	  <tr>

	  <%if(errors==false){%>
	  	  <td align="center" colspan="<%=15+divColumn%>"><b><report:recordCounter report="theReport" label="Records"/></b></td>
	      <td align="right"><report:pageCounter report="theReport" arrowColor="black" formName="participantAddressHistoryReportForm"/></td>
	  <% } %>

	</tr>

 		<script type="text/javascript" >
		<!-- // create calendar object(s) just after form tag closed
			var calFromDate = new calendar(document.forms['participantAddressHistoryReportForm'].elements['fromDate']);
			calFromDate.year_scroll = true;
			calFromDate.time_comp = false;
			var calToDate = new calendar(document.forms['participantAddressHistoryReportForm'].elements['toDate']);
			calToDate.year_scroll = true;
			calToDate.time_comp = false;
		//-->
		</script>


		  <tr>
 		    <td colspan="<%=columnSize%>">
			<br>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
 			<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
         </tr>
      </table>
    </td>
    <td><img src="/assets/unmanaged/images/s.gif" height="1" width="15"></td>
  </tr>

</table>

</table>


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

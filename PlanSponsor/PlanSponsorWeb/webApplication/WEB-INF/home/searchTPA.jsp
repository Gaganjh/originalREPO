<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.valueobject.TPAUsersReportData" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.home.SearchTPAForm" %>
<%@ page import="com.manulife.pension.service.security.valueobject.UserInfo" %>
<%-- Builds manage user Profiles  Table ------------------------------%>

<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>

<jsp:useBean class="java.lang.String"
		id="resultsMessageKey"
		scope="request"/>

<jsp:useBean id="tpaBlockOfBusinessEntryForm" 
			scope="session" 
			type="com.manulife.pension.ps.web.tpabob.TPABlockOfBusinessEntryForm" 
			class="com.manulife.pension.ps.web.tpabob.TPABlockOfBusinessEntryForm" />

<script type="text/javascript">

function clearOtherValues(ind){

	if(ind == '1'){
		document.searchTPAForm.tpaFirmName.value = '';
		document.searchTPAForm.tpaFirmId.value = '';
	}else if (ind == '2'){
		document.searchTPAForm.tpaUserName.value = '';
		document.searchTPAForm.tpaFirmId.value = '';
	}else if (ind == '3'){
		document.searchTPAForm.tpaFirmName.value = '';
		document.searchTPAForm.tpaUserName.value = '';
	}

}

/**
 * This method is called when a user clicks on the Profile ID of the TPA user. 
 * This method takes the user to TPA BOB Home page.
 */
function gotoTPABOB(profileID) {
	tpaBobForm = document.tpaBlockOfBusinessEntryForm;
	tpaBobForm.tpaUserIDSelected.value = profileID;
	tpaBobForm.submit();
}
</script>


<%
TPAUsersReportData theReport = (TPAUsersReportData)request.getAttribute(Constants.REPORT_BEAN);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<jsp:useBean id="searchTPAForm"
	class="com.manulife.pension.ps.web.home.SearchTPAForm" scope="session" />

<content:contentBean contentId="<%=ContentConstants.NO_TPA_USERS_FOUND%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="No_Match_Found"/>



<style>

.name_column { width: 100; }
.id_column { width: 80; }
.firm_name_column {width:185;}

</style>

<table cellspacing=0 cellpadding=0 width=525 border=0>
  <tbody>
  	<tr>
  	</tr>
  </tbody>
</table>
<ps:form name="tpaBlockOfBusinessEntryForm"
	modelAttribute="tpaBlockOfBusinessEntryForm"
	action="/do/tpabob/tpaBlockOfBusinessHome/" method="post">
	<form:hidden path="tpaUserIDSelected"/>
</ps:form>

<table width="100%">
<tr>
	<td width="30">
		<img src="/assets/unmanaged/images/s.gif"  height="1"> 
	</td>
	<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		   <tr>
		   	<td width="70%" ><img src="/assets/unmanaged/images/s.gif"  height="3">&nbsp;</td>
        	<td width="3%"><img src="/assets/unmanaged/images/s.gif" height="3"></td>
        	<td width="27%"><img src="/assets/unmanaged/images/s.gif"  height="3"></td>
        </tr>
		<tr><td width="70%" ><img src="/assets/unmanaged/images/s.gif"  height="3">&nbsp;</td>
        	<td width="3%"><img src="/assets/unmanaged/images/s.gif" height="3"></td>
        	<td width="27%"><img src="/assets/unmanaged/images/s.gif"  height="3"></td>
        </tr>
		<tr>
        	<td >
        	
        	<ps:permissionAccess permissions="BUSC">
			    <a href="/do/tools/businessConversionNoNavHeader/?from=searchContract">Security role conversion</a> |
			</ps:permissionAccess>
			
			<ps:isAnyPilotAvailable>
       	      <ps:permissionAccess permissions="PLMN">
			    <a href="/do/pilot/pilotContractNoNavHeader/?from=searchContract">Manage Pilots</a> |
			  </ps:permissionAccess>
		    </ps:isAnyPilotAvailable>
			
			<ps:permissionAccess permissions="CRAP">
			  <a href="/do/tools/controlReports/">Control reports</a> |
			</ps:permissionAccess>
				<a href="/do/home/searchContractDetail/">Search Contract</a> <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >|
					Search TPA 
			</ps:isNotJhtc>
			</td>
		</tr>
		<tr>
        	<td width="70%" ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
        	<td width="3%"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        	<td width="27%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
        </tr>
       	<tr>
       		<td width="70%" ><img src="/assets/unmanaged/images/s.gif"  height="3">&nbsp;</td>
        	<td width="3%"><img src="/assets/unmanaged/images/s.gif" height="3"></td>
        	<td width="27%"><img src="/assets/unmanaged/images/s.gif"  height="3"></td>
        </tr>
        <td><content:errors scope="request"/></td>
		<tr>
		<c:if test="${searchTPAForm.filterYes =='Y'}">
		<c:if test="${empty theReport.details}">
		<c:if test="${searchTPAForm.storedProcExecute =='Y'}">
        	        	<td class="redText">
			        		<content:getAttribute beanName="No_Match_Found" attribute="text"/>
			        	</td>
					</c:if>
					<c:if test="${searchTPAForm.storedProcExecute =='N'}">
						<td><content:errors scope="request"/></td>
					</c:if>
	        	</c:if>
        	</c:if>
        </tr>
	    <tr>
        	<td valign="top" >

		  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
              		<tr>
						<td colspan="24" valign="top">
                            <ps:form  method="GET" action="/home/searchTPAName/"  >
							<table border="0" cellspacing="0" cellpadding="0">
                    			<tr>
                      				<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td width="200"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td width="65"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td width="60"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td width="60"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				      				
                      				<td width="4"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                      				<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>                      				
                    			</tr>
                    			<tr class="tablehead" >
                      					<td class="tableheadTD1" colspan="4"><b>TPA</b></td>
                      				
                      					<td align="left" class="tableheadTD" colspan="12">
                      						<report:recordCounter report="theReport" label="TPA Users"/>
											<b></b>
										</td>
                      					<td align="right" class="tableheadTD" colspan="15">
											<report:pageCounter report="theReport" formName="searchTPAForm"/>
										</td>
																		
               				        	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    			</tr>

                    			<tr class="tablesubhead">
                      				<td rowspan="1" height="28" class="databorder"  >
                      					<img src="/assets/unmanaged/images/s.gif" height="1">
                      				</td>
                      				
                      				<td rowspan="1" colspan="3" >
                      				  <strong>
                      				  <c:if test="${not empty theReport.details}">
	                      					<report:sort field="<%=TPAUsersReportData.SORT_FIELD_LAST_NAME%>" direction="asc" formName="searchTPAForm">TPA Name</report:sort>
										</c:if>
										 <c:if test="${empty theReport.details}">
										  <a href="">TPA Name</a>
										 </c:if>
                      				  </strong>
                      				</td>

                      				<td rowspan="1" class="dataheaddivider" >
                      					<img src="/assets/unmanaged/images/s.gif" height="1">
                      				</td>
                      				<td rowspan="1" colspan="3"  >
                      					<strong>
                      						TPA firm Id(s)
               							</strong>
                      				</td>
									<td rowspan="1" colspan="13" align="left">
                      					<strong>
                      						TPA Firm Name(s)
               							</strong>
                      				</td>
									<td rowspan="1" colspan="10" align="left">

                      				</td>
									<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1">
									</td>
                      				
                      			</tr>
                     			

                    			<tr>
                    				<td colspan="26" align="right"></td>
                    			</tr>
					<c:if test="${not empty theReport.details}">
					<c:forEach var="theItem" items="${theReport.details}" varStatus="theIndex">	
					<%UserInfo theItem=(UserInfo)pageContext.getAttribute("theItem"); %>
					<c:set var="indexValue" value="${theIndex.index}"/>	
					<%Integer theIndex=(Integer)pageContext.getAttribute("indexValue"); %>		
					
						<% if (theIndex.intValue() % 2 == 0) { %>
						<tr class="datacell1">
					    <% } else { %>
						<tr class="datacell2">
					    <% } %>
				
					    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				
					    <td valign="center" >
					    	<a href="javascript://" onClick="gotoTPABOB(${theItem.profileId});return false;"><render:name firstName="<%=theItem.getFirstName()%>" lastName="<%=theItem.getLastName()%>" style="f" defaultValue=""/> </a></td>
					    <td valign="center" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				
						<td  colspan = "28"  valign="center">
							<table border="0" width="100%" cellpadding="2" cellspacing="1">
							<c:forEach items="${theItem.tpaFirms}" var="tpaFirm">
									<tr  valign="center">
										<td class="id_column" colspan = "8" >${tpaFirm.id}</td>
										<td class="firm_name_column" colspan = "14" align="justify">
										${tpaFirm.name}
										</td>
									</tr>
								</c:forEach>
							</table>
					    </td>
				
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					</c:forEach>
						<tr>
							<td class="databorder" colspan="32"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						</tr>
					
					</c:if>
					
					<tr>
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
							
					</table>
						
					<tr>
						<td align="right"><report:pageCounter report="theReport" arrowColor="black" formName="searchTPAForm"/></td>
						<td align="left">&nbsp;</td>
					</tr>	
          			</ps:form>

              			</td>
						
			  		</tr>
					
				</table>
       		</td>
       		
       		<%-- Search Box  Start --%>       		
       		<td width="30" align="right" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        	<td valign="top" align="right">
        	<table border="0" cellspacing="0" cellpadding="0" class="box" vAlign="top" width="175" >
              <tr class="tablehead">
                <td colspan="3" class="tableheadTD1"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
              </tr>
              <tr>
                <td class="boxborder" width="1%"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
               <ps:form modelAttribute="searchTPAForm" name="searchTPAForm"
									method="GET" action="/do/home/searchTPA/">
               <td class="boxbody" width="130">
										<p>
											Enter TPA last name, TPA firm name or TPA firm ID. <br>
											<br> <b>TPA last name</b>
											<form:input path="tpaUserName" name="searchTPAForm"
												maxlength="30" onkeypress="clearOtherValues(1);" />

										</p>
										<p>
											<b>TPA firm name</b>
											<form:input path="tpaFirmName" name="searchTPAForm"
												maxlength="30" onkeypress="clearOtherValues(2);" />

										</p>
										<p>
											<b>TPA firm Id</b>
											<form:input path="tpaFirmId" name="searchTPAForm"
												maxlength="7" onkeypress="clearOtherValues(3);" />
										</p> <input type="hidden" name="filterYes" value="Y">
										<p>
											<input name="search" type="submit" class="button100Lg"
												value="search">
										</p>

									</td>
                </ps:form>
                <td class="boxborder" width="1%"><img src="/assets/unmanaged/images/s.gif" width="1" height="2"></td>
              </tr>
              <tr>
                <td colspan="3" >
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="boxborder" width="1%">
                      	<img src="/assets/unmanaged/images/s.gif" width="1" height="4">
                      </td>
                      <td ><img src="/assets/unmanaged/images/s.gif" width="1%" height="20"></td>
                      <td class="boxborder" width="1%">
                      	<img src="/assets/unmanaged/images/s.gif" width="1%" height="4">
                      </td>
                    </tr>
                    <tr>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                  </table>
                </td>
              </tr>
			  <%-- Search Box  End --%>    
            </table>

			<tr>
			  <td colspan="3">
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			  </td>
			</tr>
			</table>
		</td>
	</tr>
</table>

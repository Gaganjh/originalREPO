<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%> 

<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.ps.web.profiles.ManageInternalUserHelper"%>
<%@page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper"%>
<%@page import="com.manulife.pension.service.security.role.RelationshipManager"%>

<c:set var="RelationshipManager" value ="<%=RelationshipManager.ID %>" scope="page" />
<c:set var="AcessLevelHelper" value ="${AcessLevelHelper.NO_ACCESS }" scope="page" />
<c:set var="BDUserRoleType" value ="${BDUserRoleType.RVP.getUserRoleCode() }" scope="page" />
<script type="text/javascript">
var submitted = false;

function doFinish() {
	if (!submitted) {
		submitted = true;
		var url = new URL(window.location.href);
		url.setParameter('action', 'finish');
		window.location.href = url.encodeURL();
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function doSubmit(href) {
	if (!submitted) {
		submitted = true;
		window.location.href=href;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

</script>

<%--
Defect 007287: Don't highlight changes in add user confirmation screen.
--%>
<logicext:if name="layoutBean" property="param(task)" op="equal" value="add">
  <logicext:then>
<style>
.highlightBold {
	color : #000000;
	font-weight: normal;
}
</style>
  </logicext:then>
</logicext:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	
<tr>
	<td width="525" valign="top">	
      <table width=525 cellSpacing=0 cellPadding=0 border=0>
           <tbody>
              <tr>
                <td width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td width=519><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td width=4><img height=1 src="/assets/unmanaged/images/s.gif" width=4></td>
                <td width=1><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
              </tr> 
            
<%-- table header  --%>
              <tr class=tablehead>
                <td class=tableheadTD1 colSpan=4><strong>
                	<content:getAttribute id="layoutPageBean" attribute="body1Header"/> 
                	</strong></td>
              </tr>
              
           	  <tr class=datacell1>
                <td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td align=middle colSpan=2>
                  <table class=datacell1 cellSpacing=0 cellPadding=0 width=100% border=0>
                    <tbody>
                    <tr class=datacell1>
                      <td class=datacell1 vAlign=top width=200><strong>First name</strong></td>
                      <td class=datacell1 vAlign=top width=319>
                      <ps:highlightIfChanged name="addEditUserForm" property="firstName">
${addEditUserForm.firstName}
					  </ps:highlightIfChanged>
					  </td></tr>
                    <tr class=datacell1>
                      <td class=datacell1 vAlign=top><strong>Last name</strong></td>
                      <td class=datacell1 vAlign=top>
                      <ps:highlightIfChanged name="addEditUserForm" property="lastName">
${addEditUserForm.lastName}
					  </ps:highlightIfChanged>
					  </td></tr>
                    <tr class=datacell1>
                      <td class=datacell1 vAlign=top><strong>Primary Email</strong></td>
                      <td class=datacell1 vAlign=top>
                      <ps:highlightIfChanged name="addEditUserForm" property="email">
${addEditUserForm.email}
					  </ps:highlightIfChanged>
					  </td></tr>
<c:if test="${not empty addEditUserForm.secondaryEmail}">
					<tr class=datacell1>
                      <td class=datacell1 vAlign=top><strong>Secondary Email</strong></td>
                      <td class=datacell1 vAlign=top>
${addEditUserForm.secondaryEmail}
					  </td></tr>
</c:if>
                    <tr class=datacell1>
                      <td class=datacell1><strong>Employee number</strong></td>
                      <td class=datacell1>
                      <ps:highlightIfChanged name="addEditUserForm" property="employeeNumber">
${addEditUserForm.employeeNumber}
					  </ps:highlightIfChanged>
					  </td></tr>
					<logicext:if name="layoutBean" property="param(task)" op="equal" value="add">
					  <logicext:then>
	                    <tr class=datacell1>
	                      <td class=datacell1 vAlign=top width=200><strong>Username</strong></td>
	                      <td class=datacell1 vAlign=top width=319>
	                      <ps:highlightIfChanged name="addEditUserForm" property="userName">
${addEditUserForm.userName}
						  </ps:highlightIfChanged>
						  </td>
						</tr>
					  </logicext:then>
					</logicext:if>
                    <tr class=datacell1>
                      <td class=datacell1><strong>Plan Sponsor access level</strong></td>
                      <td class=datacell1 vAlign=top>
                      <ps:highlightIfChanged name="addEditUserForm" property="planSponsorSiteRole">
                      	<ps:formatUserRole name="addEditUserForm" 
                					   	   property="planSponsorSiteRole" />
					  </ps:highlightIfChanged>
             		  </td></tr>
             		   
<c:if test="${addEditUserForm.planSponsorSiteRole == RelationshipManager}">
   					<tr class=datacell1>
                      <td class=datacell1><strong>Associated RM Block of Business*</strong></td>
                      <td class=datacell1 vAlign=top>
                      <ps:highlightIfChanged name="addEditUserForm" property="rmId">
${addEditUserForm.rmDisplayName}
					  </ps:highlightIfChanged>
					  </td>
					</tr>
</c:if>
<c:if test="${addEditUserForm.planSponsorSiteRole !=AcessLevelHelper}">
<c:if test="${addEditUserForm.planSponsorSiteRole !=PTC}">
                        <tr class=datacell1>
                          <td valign=top><strong>Produce Participant Fee Change Notice</strong></td>
<td valign=top>${addEditUserForm.accessIPIHypotheticalTool}</td></tr>
                        <tr class=datacell1>
                          <td valign=top><strong>Access to 408b2 Supplemental Disclosure Auto Regeneration</strong></td>
<td valign=top>${addEditUserForm.access408DisclosureRegen}</td></tr>
</c:if>
</c:if>
           			<tr class=datacell1>
                      <td class=datacell1><strong>Participant access level</strong></td>
                      <td class=datacell1 vAlign=top>
                      <ps:highlightIfChanged name="addEditUserForm" property="participantSiteRole">
					  	<%--<${addEditUserForm.participantSiteRole}--%>
					  	<ps:formatUserRole name="addEditUserForm" 
                					   	   property="participantSiteRole"
                					   	   site="ezk" />
					  </ps:highlightIfChanged>
					  </td></tr>
           			<tr class=datacell1>
                      <td class=datacell1><strong>Broker Dealer access level</strong></td>
                      <td class=datacell1 vAlign=top>
                      <ps:highlightIfChanged name="addEditUserForm" property="brokerDealerSiteRole">
<c:set var="bdRoleCode" value="${addEditUserForm.brokerDealerSiteRole}" /> 
<% String role=pageContext.getAttribute("bdRoleCode").toString();
pageContext.setAttribute("role",role);%>
					  	<%=ManageInternalUserHelper.getBDWRoleName(role) %>
					  </ps:highlightIfChanged>
					  </td>
					</tr>
<c:if test="${addEditUserForm.brokerDealerSiteRole ==BDUserRoleType}">
   					<tr class=datacell1>
                      <td class=datacell1><strong>RVP</strong></td>
                      <td class=datacell1 vAlign=top>
                      <ps:highlightIfChanged name="addEditUserForm" property="rvpId">
${addEditUserForm.rvpDisplayName}
					  </ps:highlightIfChanged>
					  </td>
					</tr>
</c:if>
				    </tbody>
				  </table>
				</td>
                <td class=databorder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
              </tr>
              
              <tr class=whiteborder>
                <td class=databorder><img height=4 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td class=whiteborder><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
                <td class=whiteborder colSpan=2 rowSpan=2>
					<img height=5 src="/assets/unmanaged/images/box_lr_corner.gif" width=5></td>
              </tr>
              <tr>
                <td class=databorder colSpan=2><img height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
			  </tr>
			</tbody>
	  	</table><br>
		<table cellSpacing=0 cellPadding=0 width=525 border=0>
        	<tbody>
              <tr align=middle>
               <td width=25%>&nbsp;</td>
               <td width="25%">
<c:set var="userName" value="${addEditUserForm.userName}" /> 

<%
String userName= StringEscapeUtils.escapeJavaScript((String)pageContext.getAttribute("userName"));
pageContext.setAttribute("userName", userName);

%>

                	<input type=button class="button100Lg" value="edit this user"
    	 				onclick="doSubmit('/do/profiles/editInternalUser/?userName=${userName}')" >
                </td>
                <td width="25%">
<input type="button" onclick="javascript:window.print()" name="print" class="button100Lg" value="print">


</input>
                </td>
                <td width="25%">
<input type="button" onclick="doFinish()" name="finish" class="button100Lg" value="finish">


</input>
                </td>
              </tr>
            </tbody>
       </table>
    </td>
</tr>
</table>

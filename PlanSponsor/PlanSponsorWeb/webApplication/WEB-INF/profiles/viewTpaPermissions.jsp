<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="com.manulife.pension.ps.web.profiles.UserPermissions" %>       
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ page import="com.manulife.pension.ps.web.profiles.BankAccount" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.security.valueobject.DefaultRolePermissions" %>
<%@ page import="com.manulife.pension.ps.web.profiles.UserPermissionsForm" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<content:contentBean contentId="<%=ContentConstants.USER_MANAGEMENT_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitleUserManagement"/>

<content:contentBean contentId="<%=ContentConstants.REPORTING_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitleReporting"/>

<content:contentBean contentId="<%=ContentConstants.PLAN_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitlePlanServices"/>

<content:contentBean contentId="<%=ContentConstants.CLIENT_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitleClientServices"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_ALL_DIRECT_DEBIT_ACCOUNTS_SELECTED%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningAllDirectDebitAccountsSelected"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_REMOVE_DIRECT_DEBIT_PERMISSION_REMOVE_DIRECT_DEBIT_ACCOUNTS%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningRemoveSelectedDirectDebitAccounts"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_SUBMISSION_ACCESS_ONLY%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningSubmissionAccessOnly"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_SELECTED%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoSelectedDirectDebitAccounts"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_AVAILABLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoAvailableDirectDebitAccounts"/>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

UserPermissions userPermissions = (UserPermissions)session.getAttribute("userPermissionsForm.userPermissions");
pageContext.setAttribute("userPermissions",userPermissions,PageContext.PAGE_SCOPE);
%>

<c:set var="userPermissions" value="${userPermissionsForm.userPermissions}" />

<script type="text/javascript" >
	var submitted = false;

	function doSubmit() {
		if (!submitted) {
			submitted = true;
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}

</script>

<div id="errordivcs"><content:errors scope="session" /></div>

<ps:form method="POST" action="/do/profiles/tpaFirmUserPermissions/" name="userPermissionsForm" modelAttribute="userPermissionsForm">
<!-- <input type="hidden" name="action" value="back"/> -->

<TABLE cellSpacing=0 cellPadding=0 width="715" border=0>
  <TBODY>
  <TR>
    <TD>
      <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR>
          <TD width=500>
          <BR/>

          <jsp:include flush="true" page="userPermissionsHeader.jsp"/>

          <BR/>

<c:if test="${userPermissions.showUserManagementSection ==true}">
           <TABLE class=box style="CURSOR: pointer" onclick="expandSection('sc1')" cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR class=tablehead>
                <TD class=tableheadTD1 colSpan=3><img id="sc1img" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<b><content:getAttribute beanName="groupTitleUserManagement" attribute="text"/></b></TD>
              </TR>
            </TBODY>
           </TABLE>
            <DIV class=switchcontent id=sc1>
            <TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD>
                <TD>
                  <TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
                    <TBODY>

<c:if test="${userPermissions.showManageUsers ==true}">
                      <tr valign="top" class="datacell2">
	                    <td width="219">
							   <strong>Manage users </strong>
	                    </td>
							<td width="75" align="left" nowrap="nowrap">
							  <render:yesno property="userPermissions.manageUsers" defaultValue="no" style="c" />
							</td>
							<td width="50" align="left" nowrap="nowrap"/>
                      </tr>
</c:if>

                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>TPA staff plan access </strong></td>
                        <td width="75" align="left" nowrap="nowrap">
			               <render:yesno property="userPermissions.tpaStaffPlanAccess" defaultValue="no" style="c" />
                        </td>
                        <td width="50" align="left" nowrap="nowrap"/>
                      </tr>

                    </TBODY>
                  </TABLE>
                </TD>
                <TD class=boxborder width=1><IMG height=1src="/assets/unmanaged/images/s.gif" width=1></TD>
              </TR>
              <TR>
                <TD colSpan=3>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                      <TD width="1" class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                  <TD class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                  </TR></TBODY></TABLE></TD></TR></TBODY></TABLE></DIV>&nbsp;<BR/>
</c:if>

<c:if test="${userPermissions.showReportingSection ==true}">
            <TABLE class=box style="CURSOR: pointer" onclick="expandSection('sc2')" cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR class=tablehead>
                <TD class=tableheadTD1 colSpan=3><img id="sc2img" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<B><content:getAttribute beanName="groupTitleReporting" attribute="text"/></B></TD>
              </TR>
             </TBODY>
            </TABLE>
            <DIV class=switchcontent id=sc2>
            <TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD>
                <TD width=644><TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
                  <TBODY>

<c:if test="${userPermissions.showDownloadReports ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>Download reports - full SSN </strong></td>
                        <td width="75" align="left" nowrap="nowrap">
							 <render:yesno property="userPermissions.downloadReports" defaultValue="no" style="c" />
					    </td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>
</c:if>

                  </TBODY>
                </TABLE>
                 </TD>
                <TD width=1 bgcolor="#D9DAE8" class=boxborder><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD></TR>
              <TR>
                <TD colSpan=3>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                      <TD width="1" class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                      <TD class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                      </TR></TBODY></TABLE></TD></TR></TBODY></TABLE></DIV><BR/>
</c:if>


<c:if test="${userPermissions.showClientServicesSection ==true}">
            <TABLE class=box style="CURSOR: pointer" onclick="expandSection('sc4')" cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR class=tablehead>
                <TD class=tableheadTD1 colSpan=3><img id="sc4img" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<B><content:getAttribute beanName="groupTitleClientServices" attribute="text"/></B></TD>
              </TR>
             </TBODY>
            </TABLE>
            <DIV class=switchcontent id=sc4>
            <TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD>
                <TD width=733><TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
                  <TBODY>

<c:if test="${userPermissions.showClientServicesSection ==true}">
					<%-- Signing Authority - Loans - Begin --%>
<c:if test="${userPermissions.showSigningAuthority ==true}">
				        <tr valign="top" class="datacell2">
				            <td width="219"><strong>Signing authority </strong></td>
            				<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.signingAuthority" defaultValue="no" style="c" />
							</td>
							<td width="50" align="left" nowrap="nowrap"/>
                      	</tr>
</c:if>
                    <%-- Signing Authority - Loans - End--%>
                    <tr valign="top">
                      <td colspan="3" class="tablesubhead"><b>Submissions</b></td>
                      </tr>

<c:if test="${userPermissions.showCreateUploadSubmissions ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Create/upload submissions	</strong></td>
						<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.createUploadSubmissions" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>
</c:if>

<c:if test="${userPermissions.showCashAccount ==true}">
                      </tr>
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Cash account </strong></td>
						<td width="75" align="left" nowrap="nowrap">
								<render:yesno property="userPermissions.cashAccount" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>
</c:if>

<c:if test="${userPermissions.showDirectDebit ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Direct debit </strong></td>

						<td width="75" align="left" nowrap="nowrap">
								<render:yesno property="userPermissions.directDebit" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>

<c:if test="${not empty userPermissions.directDebitAccounts}">
	  		<tr valign="top" class="datacell2">
		       		<td align="left" nowrap colspan="3">
		       			<%--
		       			This hidden property is used to ensure that the browser sends back something
		       			for the checkboxes value even when none of the checkboxes is selected. Without
		       			this hidden property, the browser will not send anything back when no checkbox is
		       			selected.
		       			--%>
<input type="hidden" name="selectedDirectDebitAccounts"/>
<input type="hidden" name="userPermissions.selectedDirectDebitAccountIdsAsString" /><%--  input - name="userPermissionsForm" --%>
	 		    		<ps:trackChanges property="userPermissions.selectedDirectDebitAccountIdsAsString"/>

<c:forEach items="${userPermissions.directDebitAccounts}" var="account" >
							&nbsp;&nbsp;&nbsp;&nbsp;
							<%
							BankAccount checkValue=(BankAccount)pageContext.getAttribute("account");
							%>
<c:if test="${account.noAccess ==false}">
<form:checkbox path="selectedDirectDebitAccounts" value="${account.primaryKey}">
${account.primaryKey}
									</form:checkbox>
</c:if>
<c:if test="${account.noAccess !=false}">
<form:checkbox path="selectedDirectDebitAccounts" value="${account.primaryKey}">
${account.primaryKey}
									</form:checkbox>
									 <%
									if (userPermissions.getSelectedDirectDebitAccountsAsList().contains(checkValue)) {
									%>
<input type="hidden" name="selectedDirectDebitAccounts" value="<%= checkValue.getPrimaryKey() %>" />
									<%
									}
									%>
</c:if> --%>
${account.label}<br/>
</c:forEach>

					</td>
	    	</tr>
</c:if>

</c:if>

</c:if>
<c:if test="${userPermissions.showReviewLoans ==true}">
                    <tr valign="top">
                      <td colspan="3" class="tablesubhead"><b>Loans</b></td>
                      </tr>
					
<c:if test="${userPermissions.showViewAllLoans ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>View all loans</strong></td>
						<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.viewAllLoans" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>
</c:if>
                      
<c:if test="${userPermissions.showInitiateLoans ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Initiate loans </strong></td>
						<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.initiateLoans" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                  		</tr>
</c:if>

<c:if test="${userPermissions.showReviewLoans ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review loans </strong></td>
						<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.reviewLoans" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>
</c:if>
</c:if>

<c:if test="${userPermissions.showReviewWithdrawals ==true}">
                    <tr valign="top">
                      <td colspan="3" class="tablesubhead"><b>i:withdrawals</b></td>
                      </tr>

<c:if test="${userPermissions.showViewAllWithdrawals ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>View all i:withdrawals </strong></td>
						<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.viewAllWithdrawals" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                  </tr>
</c:if>

                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Initiate i:withdrawals</strong></td>
						<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.initiateAndViewMyWithdrawals" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>

<c:if test="${userPermissions.showReviewWithdrawals ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review i:withdrawals </strong></td>
						<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.reviewWithdrawals" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>
</c:if>
</c:if>

<c:if test="${userPermissions.showClientServicesSection ==true}">
                    <tr valign="top">
                      <td colspan="3" class="tablesubhead"><strong>Census Management</strong></td>
                    </tr>

<c:if test="${userPermissions.showUpdateCensusData ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>Update census data </strong></td>
						<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.updateCensusData" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>
</c:if>

<c:if test="${userPermissions.showViewSalary ==true}">
                      <tr valign="top" class="datacell2">
                        <td width="219"><strong>View salary </strong></td>
						<td width="75" align="left" nowrap="nowrap">
							<render:yesno property="userPermissions.viewSalary" defaultValue="no" style="c" />
						</td>
						<td width="50" align="left" nowrap="nowrap"/>
                      </tr>
</c:if>

</c:if>

                  </TBODY>
                </TABLE></TD>
                <TD class=boxborder width=1><IMG height=1
                  src="/assets/unmanaged/images/s.gif"
                  width=1></TD></TR>
              <TR>
                <TD colSpan=3>
                  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                    <TBODY>
                    <TR>
                      <TD width="1" class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                  <TD class=boxborder><IMG height=1
                        src="/assets/unmanaged/images/s.gif"
                        width=1></TD>
                  </TR></TBODY></TABLE></TD></TR></TBODY></TABLE></DIV><BR/>
</c:if>

                <BR/>
                <BR/>
            <TABLE cellSpacing=0 cellPadding=0 width=412 border=0>
              <TBODY>
              <TR>
                <TD width=175 align=left>
                   <INPUT class="button134" name="action"
                          type="submit" value="back" onclick="return doSubmit();"/>
                </TD>
                <TD align=right width=175>&nbsp;</TD>
                <TD width=175 align=right><div align="center">
				</TD>
			  </TR>
			  </TBODY>
			</TABLE>
           </A></TD>
          <TD width=260>&nbsp;</TD><!-- end main content table --></TR>
        <TR>
          <TD>&nbsp;</TD>
          <TD colSpan=2>&nbsp;</TD></TR>

        </TBODY></TABLE>

      </td>
      </tr>
      </tbody>
</table>


</ps:form>



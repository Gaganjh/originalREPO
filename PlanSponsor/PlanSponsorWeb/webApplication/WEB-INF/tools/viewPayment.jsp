<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionUploadDetailBean" %>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_UPLOAD_EFFECTIVE_TEXT%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="uploadEffectiveNote"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_VPO_ANOTHER_PAYMENT_BUTTON_TEXT%>"
                        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                        beanName="paymentButtonText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_VPO_SUBMISSION_HISTORY_BUTTON_TEXT%>"
                        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                        beanName="historyButtonText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_VPO_PRINT_BUTTON_TEXT%>"
                        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                        beanName="printButtonText"/>					  
<%-- The first form is needed for the page --%>
<jsp:useBean id="viewPaymentForm" scope="session" type="com.manulife.pension.ps.web.tools.ViewPaymentForm"/>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
SubmissionUploadDetailBean detail = (SubmissionUploadDetailBean)request.getSession(false).getAttribute(Constants.SUBMISSION_UPLOAD_HISTORY_DETAIL_DATA);
pageContext.setAttribute("detail",detail,PageContext.REQUEST_SCOPE);

%>
<content:errors scope="session"/>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">
			<tr>
              <td width="40" class="big"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
              <td height="20">
              <strong><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></strong>
						<table cellspacing="0" cellpadding="0" width="100%" border="0">
                          <tr>
                            <td width="135" valign="top"><!--DWLayoutEmptyCell-->&nbsp;</td>
                            <td width="365" valign="top"><!--DWLayoutEmptyCell-->&nbsp;</td>
                          </tr>
<c:if test="${viewPaymentForm.allowedView ==true}">

                          <tr>
                            <td nowrap><strong>Contract </strong></td>
<td><strong class="highlight"> ${userProfile.currentContract.contractNumber} ${userProfile.currentContract.companyName}</strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Submission number</strong></td>
<td><strong class="highlight">${detail.confirmationNumber} </strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Date received </strong></td>
                            <td ><strong class="highlight"><render:date property="detail.receivedDate" patternOut="MMMM dd, yyyy hh:mm a z" defaultValue=""/></strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Submitted by </strong></td>
								<% if (userProfile.isInternalUser()) { %>
<td title="${detail.submitterId}">
								<% } else { %>
								<td>
								<% } %>
<strong class="highlight">${detail.sender}</strong>
								</td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Status </strong></td>
								<% if (userProfile.isInternalUser()) { %>
<td title="${detail.systemStatus}">
								<% } else { %>
								<td>
								<% } %>
<strong class="highlight">${detail.status}</strong>
								</td>
                          </tr>
                          <tr>
                            <td height="14" colspan="2">&nbsp;</td>
                          </tr>
                          <tr>
                            <td height="14" ><strong>Payment information</strong></td>
							<td height="14" >&nbsp;</td>
                          </tr>
						  <jsp:include page="paymentConfirmSection.jsp" flush="true" />
</c:if>
<c:if test="${viewPaymentForm.allowedView ==false}">
                          <tr>
                            <td nowrap><strong>Note: </strong></td>
                            <td>
                            	<strong class="highlight">You do not have permissions to view this submission.</strong>
                            	<br/><br/>
                            </td>
                          </tr>                    
</c:if>
                      </table>
           	</td>
			<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
			<td width="210" valign="top">
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
			</td>
			<!--// end column 3 -->

		</tr>
		<tr align="center">
			<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>
		<tr align="center">
			<td></td>
			<td colspan="2">
				<p/>
		        <table border="0" cellspacing="0" cellpadding="0" align="center">
		          <tr>
<td><input type="button" onclick="javascript:print(); return false;" name="actionLabel" class="button100Lg" value="print"/></td>
					<td width='15'  ></td>
<td><input type="submit" class="button175" onclick="window.location.href='/do/tools/submissionHistory/'; return false;" value="submission history" /> </td>
					<td width='15'  ></td>
<td><input type="submit" class="button175" onclick="window.location.href='/do/tools/makePayment/'; return false;" value="another payment" /> </td>
		          </tr>
		          <tr>
		            <td colspan="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		          </tr>
		          <tr>
		            <td valign="top"><content:getAttribute beanName="printButtonText" attribute="text" /></td>
					<td width='15'  ></td>
		            <td valign="top"><content:getAttribute beanName="historyButtonText" attribute="text" /></td>
					<td width='15'  ></td>
		            <td valign="top"><content:getAttribute beanName="paymentButtonText" attribute="text" /></td>
		          </tr>
			  
		        </table>
			</td>
			<td></td>
		</tr>
		<tr align="center">
			<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>
	</table>
	<table>	
<c:if test="${detail.displayDebitFootnote ==true}">
		<tr>
		  <td width="22">&nbsp;</td>
		  <td height="20">
			* Note that if values are entered in the bill payment or temporary credit fields, each entry
			will create a separate withdrawal within each account.<br/>
		  </td>
		  <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
		  <td></td>
		</tr>
</c:if>
		<tr>
		  <td width="22">&nbsp;</td>
		  <td height="20"><content:getAttribute beanName="uploadEffectiveNote" attribute="text"/></td>
		  <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
		  <td></td>
		</tr>
	</table>
		



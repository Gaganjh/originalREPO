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
<%@ page import="com.manulife.pension.ps.web.tools.FileUploadDetailBean" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>


<%
FileUploadDetailBean detail = (FileUploadDetailBean)request.getSession().getAttribute(Constants.FILE_UPLOAD_DETAIL_DATA);
pageContext.setAttribute("detail",detail,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_MISC_PRINT_BUTTON_DESCRIPTION%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="printButtonDescription"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_MISC_HISTORY_BUTTON_DESCRIPTION%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="historyButtonDescription"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_MISC_UPLOAD_BUTTON_DESCRIPTION%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="uploadButtonDescription"/>

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
                          <tr>
                            <td nowrap><strong>Contract &nbsp;&nbsp;</strong></td>
<td><strong class="highlight"> ${userProfile.currentContract.contractNumber} ${userProfile.currentContract.companyName}</strong></td>

                          </tr>
                          
                  <!--
                          <tr>
                          	<td nowrap><strong>email &nbsp;&nbsp;</strong></td>
                          	<td><strong class="highlight">${detail.email}</strong></td>
						  </tr>
				  -->
                          <tr>
                          	<td colspan="2">&nbsp;</td>
						  </tr>
						  <tr>
                            <td nowrap><strong>Submission number</strong></td>
<td><strong class="highlight">${detail.confirmationNumber}</strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Date received</strong></td>
                            <td><strong class="highlight"><render:date property="detail.receivedDate" patternOut="MMMM dd, yyyy hh:mm a z" defaultValue=""/></strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Submitted by</strong></td>
<td><strong class="highlight">${detail.sender}</strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Submitter Email</strong></td>
<td><strong class="highlight">${detail.email}</strong></td>
                          </tr>						  
                          <tr>
                            <td height="14" colspan="2"><!--DWLayoutEmptyCell-->&nbsp; </td>
                          </tr>
                          <tr>
                            <td height="14" nowrap><strong>File Information</strong></td>
                            <td height="14">&nbsp;</td>
                          </tr>
<c:if test="${detail.fileUploadExists ==true}">
                          <tr>
                            <td nowrap><strong>File type</strong></td>
<td><strong class="highlight">${detail.submissionTypeName}</strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>File name</strong></td>
<td><strong class="highlight">${detail.fileName}</strong></td>
                          </tr>
</c:if>
<c:if test="${detail.fileUploadExists !=true}">
							<tr> 
							  <td colspan="2">No file was included in this upload.</td>
							</tr>
</c:if>

                          <tr>
                            <td height="14" colspan="2">&nbsp;</td>
                          </tr>
                          <tr>
                            <td height="14" ><strong><content:getAttribute beanName="layoutPageBean" attribute="body1"/></strong></td>
							<td height="14" >&nbsp;</td>
                          </tr>
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
<td><input type="submit" class="button175" onclick="window.location.href='/do/tools/submissionHistory/'; return false;" value="submission history" /> </td>
					<td width='15'></td>
<td><input type="button" onclick="javascript:print(); return false;" name="actionLabel" class="button100Lg" value="print"/></td>
					<td width='15'></td>
<td><input type="submit" class="button175" onclick="window.location.href='/do/tools/uploadLongTermPartTimeInfo/'; return false;" value="upload another file" /> </td>
		          </tr>
		          <tr>
		            <td valign="top"><span class="content"><content:getAttribute beanName="historyButtonDescription" attribute="text"/></span></td>
					<td width='15'></td>
		            <td valign="top"><span class="content"><content:getAttribute beanName="printButtonDescription" attribute="text"/></span></td>
					<td width='15'></td>
		            <td valign="top"><span class="content"><content:getAttribute beanName="uploadButtonDescription" attribute="text"/></span></td>
		          </tr>
		        </table>
			</td>
			<td></td>
		</tr>
		<tr align="center">
			<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>
	</table>
		



<%@ tag body-content="empty" %>
<%@ tag import="com.manulife.pension.util.content.GenericException"
 import="java.util.ArrayList"
 import="java.util.Collection"
 import="java.util.Map"
 import="java.util.HashMap"
 import="com.manulife.pension.ps.web.Constants"
 import="com.manulife.pension.ps.web.content.ContentConstants"
 import="com.manulife.pension.platform.web.util.ContentHelper"
 import="com.manulife.pension.util.content.manager.ContentProperties"
 import="com.manulife.pension.util.content.manager.ContentCacheConstants"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="scope" required="false" %>
<%@ attribute name="removeErrorsFromScopePostUsage" required="false" %>
<%-- 
This tag will be used to show the Error Messages, Warning Messages in the JSP pages in a Box.
The Box will have the Error Count, Warning Count as the Header.
The contents of this box will have the Errors, Warnings numbered 1,2,..etc with a Error Icon / Warning Icon.
Author - Hari Lomte.
--%>
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_ERROR_HEADER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorHeader" />
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_ERROR_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorIntro" />
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_ERROR_INTRO2%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorIntro2" />
<%
	Map<String, Integer> scopes = new HashMap<String, Integer> ();
	scopes.put("page", new Integer(PageContext.PAGE_SCOPE));
	scopes.put("request", new Integer(PageContext.REQUEST_SCOPE));
	scopes.put("session", new Integer(PageContext.SESSION_SCOPE));
	String errorKey = null;
	try {
	    errorKey = ContentProperties.getInstance().getProperty(ContentCacheConstants.ERROR_KEY);
	} catch (Exception e) {
	    throw new JspException(e.getMessage());
	}
	getJspContext().setAttribute("errorKeyVal", errorKey);
	getJspContext().setAttribute("warningKeyVal", Constants.PS_WARNINGS);
	getJspContext().setAttribute("infoKeyVal", Constants.PS_INFO_MESSAGES);
	
%>
<c:if test="${empty scope}">
	<c:set var="scope" value="request"/>
</c:if>
<c:if test="${scope eq 'page'}">
	<c:set var="errorsCollection" value="${pageScope[pageScope['errorKeyVal']]}"/>
	<c:set var="warningCollection" value="${pageScope[pageScope['warningKeyVal']]}"/>
	<c:set var="informationCollection" value="${pageScope[pageScope['infoKeyVal']]}"/>
</c:if>
<c:if test="${scope eq 'request'}">
	<c:set var="errorsCollection" value="${requestScope[pageScope['errorKeyVal']]}"/>
	<c:set var="warningCollection" value="${requestScope[pageScope['warningKeyVal']]}"/>
	<c:set var="informationCollection" value="${requestScope[pageScope['infoKeyVal']]}"/>
</c:if>
<c:if test="${scope eq 'session'}">
	<c:set var="errorsCollection" value="${sessionScope[pageScope['errorKeyVal']]}"/>
	<c:set var="warningCollection" value="${sessionScope[pageScope['warningKeyVal']]}"/>
	<c:set var="informationCollection" value="${sessionScope[pageScope['infoKeyVal']]}"/>
</c:if>
<c:if test="${not empty removeErrorsFromScopePostUsage and removeErrorsFromScopePostUsage eq 'true'}">
	<%
	// Removing the errors / warnings from their scopes so that they do not show up even when there are no errors.
	getJspContext().removeAttribute(errorKey, scopes.get(scope));
	getJspContext().removeAttribute(Constants.PS_WARNINGS, scopes.get(scope));
	getJspContext().removeAttribute(Constants.PS_INFO_MESSAGES, scopes.get(scope));
	%>
</c:if>
<c:set var="errorCount" value="0"/>
<c:set var="warningCount" value="0"/>
<c:set var="infoCount" value="0"/>
<%-- Get the Error / Warning count --%>
<c:if test="${not empty errorsCollection}">
	<c:set var="errorCount" value="${fn:length(errorsCollection)}"/>
	<%-- Get a String[] list of error Messages--%>
	<%getJspContext().setAttribute("errorList", 
	        ContentHelper.getMessagesUsingContentType((
	                Collection<GenericException>)getJspContext().getAttribute("errorsCollection")));%>
</c:if>
<c:if test="${not empty warningCollection}">
	<c:set var="warningCount" value="${fn:length(warningCollection)}"/>
	<%-- Get a String[] list of warning Messages--%>
	<%getJspContext().setAttribute("warningList", 
	        ContentHelper.getMessagesUsingContentType((
	                Collection<GenericException>)getJspContext().getAttribute("warningCollection")));%>
</c:if>
<c:if test="${not empty informationCollection}">
	<c:set var="infoCount" value="${fn:length(informationCollection)}"/>
	<%-- Get a String[] list of information Messages--%>
	<%getJspContext().setAttribute("infoList", 
	        ContentHelper.getMessagesUsingContentType((
	                Collection<GenericException>)getJspContext().getAttribute("informationCollection")));%>
</c:if>
<c:if test="${errorCount gt 0 or warningCount gt 0}">
<table width="500" height="50" border="0" cellPadding="0" cellSpacing="0">
		<tr>
			<td width="1"><img height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
			<td>
				<img height="1" src="/assets/unmanaged/images/s.gif" width="1">
				<img height="1" src="/assets/unmanaged/images/s.gif" width="1">
				<img height="1" src="/assets/unmanaged/images/s.gif" width="1">
				<img height="1" src="/assets/unmanaged/images/s.gif" width="1">
				<img height="1" src="/assets/unmanaged/images/s.gif" width="1">
				<img height="1" src="/assets/unmanaged/images/s.gif" width="1">
			</td>
			<td width="1"><img height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
		</tr>
		<tr class="tablehead">
			<td class="tableheadTD1" colSpan="3">
				<table cellSpacing="0" cellPadding="0" width="100%" border="0">
					<tr>
						<td class="tableheadTD">
							<STRONG>
								<content:getAttribute id='ErrorHeader' attribute='text'>
									<content:param><c:out value="${errorCount}"/></content:param>
									<content:param><c:out value="${warningCount}"/></content:param>
								</content:getAttribute>
							</STRONG>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr class="datacell1">
			<td class="databorder">
				<img height="1" src="/assets/unmanaged/images/s.gif" width="1">
			</td>
			<td class="datacell1" vAlign="top" align="left">
				<c:choose>
					<c:when test="${param.printFriendly == true}">
						<content:getAttribute id='ErrorIntro' attribute='text'/>
						<ol>
						<c:if test="${not empty errorList}">
							<c:forEach var="errorMsg" items="${errorList}">
								<li>
								<img alt='Error' src='/assets/unmanaged/images/error.gif' /> ${errorMsg}
								</li>
							</c:forEach>
						</c:if>
						<c:if test="${not empty warningList}">
							<c:forEach var="warningMsg" items="${warningList}">
								<li>
								<img alt='Warning' src='/assets/unmanaged/images/warning2.gif'> ${warningMsg}
								</li>
							</c:forEach>
						</c:if>
						</ol>
					</c:when>
					<c:otherwise>
						<div class="scroll">			    
							<content:getAttribute id='ErrorIntro' attribute='text'/>	
							<c:if test="${param.readOnly and (not param.printFriendly)}">
								<br><content:getAttribute id='ErrorIntro2' attribute='text'/>	
								<br>
								<table border="0" cellpadding="0" cellspacing="0" width="422">
									<tr valign="top">
										<td width="215">
											<div align="center">
												<input type="button" name="button2" value="edit" 
													class="button100Lg" onclick="return doSubmit('continueEdit')"/>
											</div>
										</td>
										<td width="207">
											<div align="center">
												<input type="button" name="button3" value="accept" 
													class="button100Lg" onclick="return doSubmit('saveIgnoreWarning')"/>          
												<br>
											</div>
											<div align="right"></div>
										</td>
									</tr>
								</table>
							</c:if>
							<ol>
								<c:if test="${not empty errorList}">
									<c:forEach var="errorMsg" items="${errorList}">
										<li>
										<img alt='Error' src='/assets/unmanaged/images/error.gif' /> ${errorMsg}
										</li>
									</c:forEach>
								</c:if>
								<c:if test="${not empty warningList}">
									<c:forEach var="warningMsg" items="${warningList}">
										<li>
										<img alt='Warning' src='/assets/unmanaged/images/warning2.gif'> ${warningMsg}
										</li>
									</c:forEach>
								</c:if>
							</ol>	           
						</div>
					</c:otherwise>
				</c:choose>
			</td>	     
			<td class="databorder"><img height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
		</tr>
		<tr>
			<td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
		</tr>
</table>
</c:if>

<c:if test="${infoCount gt 0}">
	<table width="500" border="0" cellPadding="0" cellSpacing="0">
	<tr><td width="500"> 	
		<c:if test="${not empty infoList}">
			<c:forEach var="infoMsg" items="${infoList}">
				<img alt='Warning' src='/assets/unmanaged/images/icon_info.gif'> ${infoMsg}
			</c:forEach>
		</c:if>
	</td></tr>
	</table>
</c:if>
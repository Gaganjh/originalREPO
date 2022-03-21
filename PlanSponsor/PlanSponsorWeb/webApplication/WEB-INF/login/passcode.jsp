<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.platform.web.util.CommonEnvironment"%>

<%-- This jsp includes the following CMA content --%>
<table width="760" border="0" cellpadding="0" cellspacing="0">
  <tbody>
		<tr>
			<td width="5%">
				<img height="8" src="/assets/unmanaged/images/s.gif" width="10" border="0">
			</td>
			<td width="20%" valign="top">
				<table id="column1" cellspacing="0" cellpadding="0" border="0">
					<tbody>
						<tr valign="top">
							<td><img height="70" src="/assets/unmanaged/images/s.gif" width="100"></td>
						</tr>
						<tr>
							<td class="greyText">&nbsp; </td>
						</tr>
					</tbody>
			    </table>
		    </td>
			<td width="5%">
				<img height="1" src="/assets/unmanaged/images/s.gif" width="15" border="0">
			</td>
			<td width="50%" valign="top" class="greyText">
				<img src="/assets/unmanaged/images/s.gif" width="402" height="23">
				<br>
					<img src="<content:pageImage type="pageTitle" beanName="layoutPageBean"/>" alt="<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>"><br>      <br>
					<table width="394" border="0" cellspacing="0" cellpadding="0">
						<tbody>
							<tr>
								<td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
								<td valign="top" width="495">
									<content:getAttribute attribute="introduction2" beanName="layoutPageBean"/>
								</td>
							</tr>
							<tr>
								<td height="10" align="left" colspan="2"></td>
							</tr>
						</tbody>
					</table>
					<img src="/assets/unmanaged/images/s.gif" width="1" height="5"> <br>
<ps:form method="POST" action="/do/passcodeValidation/" modelAttribute="passcodeForm" name="passcodeForm" onsubmit="return doPreSubmit();">
					<table width="425" border="0" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="400" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>
							<tr class="tablehead">
								<td colspan="5" class="tableheadTD1">&nbsp;<strong> <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> </strong></td>
							</tr>
							<tr class="datacell1">
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td colspan="2" align="center">
									<table>
											<tbody>
												<tr>
													<td  colspan="2">&nbsp;</td>
												</tr>
												<tr>
													<td  colspan="2">
														<content:getAttribute beanName="layoutPageBean" attribute="body1"/>
													</td>
												</tr>
												<tr>
													<td  colspan="2">&nbsp;</td>
												</tr>
												<tr>
													<td  colspan="2">

													
													
													<c:choose>
													
													<c:when test="${sessionScope.PASSCODE_SESSION_KEY.passcodeChannel != 'EMAIL' }">
														<content:contentBean
											contentId="96398"
													type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="dynamicContent" /> 
													
													<content:getAttribute attribute="text" beanName="dynamicContent">
													
													<c:if test="${sessionScope.PASSCODE_SESSION_KEY.passcodeChannel eq 'SMS' }">
													<content:param>text</content:param>
													<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.maskedRecipientMobile}" /></content:param>
													</c:if>	
													
													<c:if test="${sessionScope.PASSCODE_SESSION_KEY.passcodeChannel eq 'VMOBILE' }">
													<content:param>voice message</content:param>
													<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.maskedRecipientMobile}" /></content:param>
													</c:if>
													
													<c:if test="${sessionScope.PASSCODE_SESSION_KEY.passcodeChannel eq 'VPHONE' }">
													<content:param>voice message</content:param>
													<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.maskedRecipientPhone}" /></content:param>
													</c:if>
													
													</content:getAttribute>
													<br/>
													<content:contentBean
											contentId="96399"
													type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="passcodeExpiry" /> 
													<content:getAttribute attribute="text" beanName="passcodeExpiry">
															<content:param><c:out value="${sessionScope.PASSCODE_EXPIRED_TIME}" /></content:param>
													</content:getAttribute>
													</c:when>	
													
													
	<c:when test="${requestScope.PASSCODE_FORWARD_KEY == 'resend'}">
		<content:contentBean contentId="93217" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="dynamicContent"/>
														<content:getAttribute attribute="text" beanName="dynamicContent">
															<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.maskedRecipientEmail}" /></content:param>
															<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.fromAddress}" /></content:param>
														</content:getAttribute>
	</c:when>
	<c:otherwise>
		<content:contentBean contentId="93216" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="dynamicContent"/>
														<content:getAttribute attribute="text" beanName="dynamicContent">
															<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.maskedRecipientEmail}" /></content:param>
															<content:param><c:out value="${sessionScope.PASSCODE_SESSION_KEY.fromAddress}" /></content:param>
														</content:getAttribute>
	</c:otherwise>
</c:choose>
													</td>
												</tr>
												<tr>
													 <td colspan="2">&nbsp;</td>
												</tr>
												<tr>
													<td class="highlight" colspan="2"><ps:label fieldId="passcode" mandatory="false">Enter the security code:</ps:label></td>
												</tr>
												<tr>
													<td>
														<input type="password" name="passcode" size="32" maxlength="32" autocomplete="off" class="inputField" >
													</td>
													<td>
														<input class="button100Lg" type="submit" value="verify" name="action"></TD></TR>
														<script language="javascript">
															var onenter = new OnEnterSubmit('action', 'verify');
															onenter.install();
														</script>
													</td>
 												</tr>
												<tr>
													<td colspan="2" align="left">
														<content:errors scope="request" />
													</td>
												</tr>
											</tbody>
										</table>
								</td>
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>
							<tr class="datacell1">
								<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
								<td colspan="2" class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
							</tr>
							
							<tr>
								<td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>
						</tbody>
					</table>
					<br>
					<c:if test="${sessionScope.PASSCODE_SESSION_KEY.passcodeChannel eq 'EMAIL' }">
					<table width="395" border="0" cellspacing="0" cellpadding="0">
						<tbody>
							<tr>
								<content:contentBean
											contentId="94544"
													type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="resendPassword" /> 
								<td colspan="13" width="100%">
										<content:getAttribute beanName="resendPassword" attribute="text"/>&nbsp;<br>
											<strong><a href="#" onclick="document.forms['passcodeForm'].submit();">
											Resend security code
											</a></strong>

										<input type="hidden" name="action" value="resend"/>
									</td>
								
							</tr>
							<tr>
								<td width="100%" colSpan=13>
									<br>
									<p><content:pageFooter beanName="layoutPageBean"/></p>
									<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
									<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
								</td>
							</tr>							
						</tbody>
					</table>
					</c:if>
</ps:form>
				</td>
				<td width="5%" height="312" valign="top" class="fixedTable">
					<img src="/assets/unmanaged/images/s.gif" width="20" height="1">
				</td>
				<td width="15%" height="312" valign="top" class="fixedTable">
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" >
					<content:param>
						<c:choose> 
							<c:when test="${sessionScope.PASSCODE_CREATED_TIME != null}">
								<c:out value="${sessionScope.PASSCODE_CREATED_TIME}" />
							</c:when>
							<c:otherwise>
							  &nbsp
							</c:otherwise>
						</c:choose>
					</content:param>
				</content:rightHandLayerDisplay>
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer3" />
				</td>
			</tr>
	</tbody>
</table>
<script>
	setFocusOnFirstInputField("passcodeForm");
	if(window.history.replaceState){
		window.history.replaceState(null,null,window.location.href);
	}
	
</script>

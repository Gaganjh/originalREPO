
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>    
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ page import="com.manulife.pension.ps.web.profiles.EditMyProfileForm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<content:contentBean
	contentId="<%=ContentConstants.LAYOUT_EDIT_MY_PROFILE_CONFIRMATION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="layoutPageBean" />
	
<content:contentBean contentId="<%=ContentConstants.NEW_SECTION_TITLE_EDIT_CONFIRM%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
									beanName="securityInfoSectionTitle" />

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<c:set var="theForm" value="${editMyProfileForm}" scope="page" />


<script type="text/javascript">
	var submitted = false;

	var plusIcon = "/assets/unmanaged/images/plus_icon.gif";
	var minusIcon = "/assets/unmanaged/images/minus_icon.gif";

	function expandSection(cid) {
		document.getElementById(cid).style.display = (document
				.getElementById(cid).style.display != "block") ? "block"
				: "none"
		document.getElementById(cid + "img").src = (document
				.getElementById(cid).style.display == "block") ? minusIcon
				: plusIcon;
	}

	function expandAll() {
		expandAllSections();
		setAllIcons(minusIcon);
	}

	function contractAll() {
		contractAllSections();
		setAllIcons(plusIcon);
	}

	function setAllIcons(iconImg) {
		<%int indexValue = ((EditMyProfileForm)pageContext.getAttribute("theForm")).getContractAccesses().size();%>
		var numContractAccesses =indexValue;
		for (var i = 0; i < numContractAccesses; i++) {
			imgElement = document.getElementById("sc" + i + "img");
			if (imgElement != null) {
				imgElement.src = iconImg;
			}
		}
	}

	function doFinish() {
		if (!submitted) {
			submitted = true;
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}

	function doSubmit(href) {
		if (!submitted) {
			submitted = true;
			window.location.href = href;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return;
		}
	}
</script>
<table width="700" border="0" cellspacing="0" cellpadding="0">
   <tr>
      <td>
         <table width="525" border="0" cellspacing="0" cellpadding="0">
            <tr>
               <td width="1"><img src="/assets/unmanaged/images/s.gif"
                  width="1" height="1"></td>
               <td width="169"><img src="/assets/unmanaged/images/s.gif"
                  width="153" height="1"></td>
               <td width="515"><img src="/assets/unmanaged/images/s.gif"
                  width="153" height="1"></td>
               <td width="4"><img src="/assets/unmanaged/images/s.gif"
                  width="4" height="1"></td>
               <td width="1"><img src="/assets/unmanaged/images/s.gif"
                  width="1" height="1"></td>
            </tr>
            <tr class="tablehead">
               <td class="tableheadTD1" colspan="5">
                  <strong>
                     <content:getAttribute
                        id="layoutPageBean" attribute="body1Header" />
                  </strong>
               </td>
            </tr>
            <tr class="datacell1">
               <td rowspan="1" class="databorder"><img
                  src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
               <td colspan="3">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                     <tr class="datacell1">
                        <td width="40%"><strong> First name</strong></td>
                        <td align="left" class="datacell1">
                           <c:if test="${theForm.firstNameChanged ==true}">
                              <span class="highlightBold">
                           </c:if>
                           ${theForm.firstName} <c:if test="${theForm.firstNameChanged ==true}">
                           </span>
                           </c:if>
                        </td>
                     </tr>
                     <tr class="datacell1">
                        <td width="40%"><strong> Last name</strong></td>
                        <td>
                           <c:if test="${theForm.lastNameChanged ==true}">
                              <span class="highlightBold">
                           </c:if>
                           ${theForm.lastName} <c:if test="${theForm.lastNameChanged ==true}">
                           </span>
                           </c:if>
                        </td>
                     </tr>
                     <tr class="datacell1">
                        <td width="40%"><strong>Primary Email</strong><img
                           src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                        <td>
                           <c:if test="${theForm.emailChanged ==true}">
                              <span class="highlightBold">
                           </c:if>
                           ${theForm.email} <c:if test="${theForm.emailChanged ==true}">
                           </span>
                           </c:if>
                        </td>
                     </tr>
                     <c:if test="${not empty theForm.secondaryEmail}">
                        <tr class="datacell1">
                           <td width="40%"><strong>Secondary Email</strong><img
                              src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                           <td>
                              <c:if test="${theForm.secondaryEmailChanged ==true}">
                                 <span class="highlightBold">
                              </c:if>
                              ${theForm.secondaryEmail} <c:if test="${theForm.secondaryEmailChanged ==true}">
                              </span>
                              </c:if>
                           </td>
                        </tr>
                     </c:if>
                     <ps:isExternal name="userProfile" property="role">
                        <tr class="datacell1">
                           <td width="40%"><strong>Telephone number</strong><img
                              src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                           <td>
                              <c:if test="${theForm.telephoneNumber != ''}">
                                 <c:if test="${theForm.phoneNumberChanged ==true}">
                                    <span class="highlightBold">
                                       <render:phone
                                          property="theForm.telephoneNumber"></render:phone>
                                    </span>
                                 </c:if>
                                 <c:if test="${theForm.phoneNumberChanged ==false}">
                                    <render:phone property="theForm.telephoneNumber"></render:phone>
                                 </c:if>
                              </c:if>
                              <c:if test="${not empty theForm.telephoneExtension}">
                                 &nbsp;ext.&nbsp;
                                 <c:if test="${theForm.extensionChanged ==true}">
                                    <span class="highlightBold">${theForm.telephoneExtension}</span>
                                 </c:if>
                                 <c:if test="${theForm.extensionChanged ==false}">
                                    ${theForm.telephoneExtension}
                                 </c:if>
                              </c:if>
                           </td>
                        </tr>
                        <tr class="datacell1">
                           <td width="40%"><strong>Fax number<img
                              src="/assets/unmanaged/images/s.gif" width="1" height="1"></strong></td>
                           <td>
                              <c:if test="${theForm.faxNumber != ''}">
                                 <c:if test="${theForm.faxNumberChanged ==true}">
                                    <span class="highlightBold">
                                       <render:fax
                                          property="theForm.faxNumber"></render:fax>
                                    </span>
                                 </c:if>
                                 <c:if test="${theForm.faxNumberChanged ==false}">
                                    <render:fax property="theForm.faxNumber"></render:fax>
                                 </c:if>
                              </c:if>
                           </td>
                        </tr>
						</ps:isExternal>
						<tr>
							<td class="databorder" colspan="5"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						</tr>
                  </table>
               </td>
               <td rowspan="1" class="databorder"><img
                  src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
         </table>
         <br /> 
         <table width="525" border="0" cellspacing="0" cellpadding="0">
            <tr>
               <td width="1"><img src="/assets/unmanaged/images/s.gif"
                  width="1" height="1"></td>
               <td width="169"><img src="/assets/unmanaged/images/s.gif"
                  width="153" height="1"></td>
               <td width="515"><img src="/assets/unmanaged/images/s.gif"
                  width="153" height="1"></td>
               <td width="4"><img src="/assets/unmanaged/images/s.gif"
                  width="4" height="1"></td>
               <td width="1"><img src="/assets/unmanaged/images/s.gif"
                  width="1" height="1"></td>
            </tr>
            <tr class="tablehead">
               <td colspan="5" class="tableheadTD1">
                  <content:contentBean contentId="<%=ContentConstants.NEW_SECTION_TITLE_EDIT_CONFIRM%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     beanName="NEW_SECTION_TITLE_EDIT_CONFIRM" />
                  <strong>
                     <content:getAttribute beanName="NEW_SECTION_TITLE_EDIT_CONFIRM" attribute="title"/>
                  </strong>
               </td>
            </tr>
            <tr class="datacell1">
               <td rowspan="1" class="databorder"><img
                  src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
               <td colspan="3">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                     <ps:isExternal name="userProfile" property="role">
                        <tr class="datacell1">
                           <td width="40%"><strong>Mobile number </strong></td>
                           <td>
                              <c:if test="${theForm.mobileNumber != ''}">
                                 <c:if test="${theForm.mobileNumberChanged ==true}">
                                    <span class="highlightBold">
                                       <render:phone
                                          property="theForm.mobileNumber"></render:phone>
                                    </span>
                                 </c:if>
                                 <c:if test="${theForm.mobileNumberChanged ==false}">
                                    <render:phone property="theForm.mobileNumber"></render:phone>
                                 </c:if>
                              </c:if>
                           </td>
                        </tr>
                     </ps:isExternal>
                      <tr class="datacell1" align="top">
                        <td width="40%" valign="top"><strong> You have elected to receive your security code by</strong><img
                           src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                        <td>
                           <c:if test="${theForm.passcodeDeliveryPreferenceChanged ==true}">
                              <span class="highlightBold">
                           </c:if>
                           <logicext:if name="theForm" property="passcodeDeliveryPreference" op="equal" value="SMS">
                           <logicext:then>
                           Text message to mobile number
                           </logicext:then>
                           </logicext:if>
                           <logicext:if name="theForm" property="passcodeDeliveryPreference" op="equal" value="VOICE_TO_MOBILE">
                           <logicext:then>
                           Voice message to mobile number
                           </logicext:then>
                           </logicext:if>
                           <logicext:if name="theForm" property="passcodeDeliveryPreference" op="equal" value="VOICE_TO_PHONE">
                           <logicext:then>
                           Voice message to telephone number
                           </logicext:then>
                           </logicext:if>
                           <logicext:if name="theForm" property="passcodeDeliveryPreference" op="equal" value="EMAIL">
                           <logicext:then>
                           Email
                           </logicext:then>
                           </logicext:if>
                           <c:if test="${theForm.passcodeDeliveryPreferenceChanged ==true}"></span></c:if>
                        </td>
                     </tr>
                     <tr class="datacell1">
                        <td width="40%"><strong> Username</strong></td>
                        <td>${userProfile.name}</td>
                     </tr>
                     <tr class="datacell1">
                        <td width="40%"><strong>Password</strong></td>
                        <td>
                           <c:if test="${theForm.passwordChanged ==true}">
                              <span class="highlightBold">
                                 <ps:masked name="theForm"
                                    property="newPassword" />
                           </c:if>
                           <c:if test="${theForm.passwordChanged ==false}">
                           <ps:masked name="theForm" property="currentPassword" />
                           </c:if> <c:if test="${theForm.passwordChanged ==true}">
                           </span>
                           </c:if>
                        </td>
                     </tr>
                     <ps:isNotInternalOrTpa name="userProfile" property="role">
                        <tr class="datacell1">
                           <td width="40%"><strong> Challenge question</strong><img
                              src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                           <td>
                              <c:if test="${theForm.challengeQuestionChanged ==true}">
                                 <span class="highlightBold">
                              </c:if>
                              ${theForm.challengeQuestion} <c:if test="${theForm.challengeQuestionChanged ==true}">
                              </span>
                              </c:if>
                           </td>
                        </tr>
                        <tr class="datacell1">
                           <td width="40%"><strong> Answer</strong></td>
                           <td>
                              <c:if test="${theForm.challengeAnswerChanged ==true}">
                                 <span class="highlightBold">
                                    <ps:masked name="theForm"
                                       property="challengeAnswer" />
                                 </span>
                              </c:if>
                              <c:if test="${theForm.challengeAnswerChanged ==false}">
                                 ***************
                              </c:if>
                           </td>
                        </tr>
                     </ps:isNotInternalOrTpa>
                     	<tr>
							<td class="databorder" colspan="5"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						</tr>
                      </table>
               </td>
               <td rowspan="1" class="databorder"><img
                  src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
         </table>
         <br />
         <c:if test="${editMyProfileForm.emailPreferenceShown ==true}">
            <jsp:include page="myProfileConfirmEmailPreference.jsp"/>
         </c:if>
      </td>
      <td><img src="/assets/unmanaged/images/s.gif" width="20"
         height="1"></td>
      <td valign="top">&nbsp;</td>
   </tr>
</table>
<br>
<c:if test="${empty param.printFriendly}">
	<table width="525" border="0" cellspacing="0" cellpadding="0">
		<tr align="center">
			<td width="131"><img src="/assets/unmanaged/images/s.gif"></td>
<td width="131"><input type="button" onclick="doSubmit('/do/profiles/editMyProfile/')" name="action" class="button100Lg" value="edit my profile"/>


</td>
<td width="131"><input type="button" onclick="doPrintForConfirm();" name="action" class="button100Lg" value="print"/>

</td>
			<ps:form cssClass="margin-bottom:0;" method="POST"
				action="/do/profiles/editMyProfile/" name="editMyProfileForm" modelAttribute="editMyProfileForm">

<td width="132"><input type="submit" class="button100Lg" onclick="return doFinish();" name="action" value="finish" /><%--  - property="actionLabel" --%>

</td>
			</ps:form>

		</tr>
	</table>
</c:if>


<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<style>
.greytext{
	FONT-SIZE: 11px; COLOR: #757575; FONT-FAMILY: Arial, Helvetica, sans-serif;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
    if (${passwordResetAuthenticationForm.smsSwithOn}){
    	$('#sms').addClass("greytext");
    } 
    if (${passwordResetAuthenticationForm.voiceSwithOn}){
    	$('#vmobile').addClass("greytext");
    	$('#vphone').addClass("greytext");
    } 
    if (${passwordResetAuthenticationForm.emailSwithOn}){
    	$('#email').addClass("greytext");
    } 
});
</script> 

<table width="760" border="0" cellpadding="0" cellspacing="0">
    <tbody>
        <tr>
            <td width="5%">
                <img height="8" src="/assets/unmanaged/images/s.gif" width="10" border="0">
            </td>
            <td width="5%">
                <img height="1" src="/assets/unmanaged/images/s.gif" width="15" border="0">
            </td>
            <td width="50%" valign="top" class="greyText">
                <img src="/assets/unmanaged/images/s.gif" width="402" height="23">
                <br>
                <img src="<content:pageImage type=" pageTitle " beanName="layoutPageBean"/>" alt="<content:getAttribute beanName=" layoutPageBean" attribute="body1Header"/>">
               
                <br>
                <ps:form method="POST" action="/do/login/forgotPasswordPasscodeTransition/" modelAttribute="passwordResetAuthenticationForm" name="passwordResetAuthenticationForm" onsubmit="return doPreSubmit();">
                    <c:choose>
                    <c:when test="${(not empty passwordResetAuthenticationForm.maskedMobile) or (not empty passwordResetAuthenticationForm.maskedPhone) }">
                        <br>
                		<br>
                		 <content:errors scope="session" />
                <table width="426" border="0" cellspacing="0" cellpadding="0">
                    <tbody>
                        <tr>
                            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
                            <td valign="top" width="421">
                                <content:getAttribute attribute="introduction2" beanName="layoutPageBean" />
                            </td>
                        </tr>
                        <tr>
                            <td height="10" align="left" colspan="2"></td>
                        </tr>
                        <c:if test="${(passwordResetAuthenticationForm.smsSwithOn eq true) or (passwordResetAuthenticationForm.voiceSwithOn eq true) or (passwordResetAuthenticationForm.emailSwithOn eq true)}">
                        <tr>
                            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
                            <td valign="top" width="421">
                            <content:contentBean
											contentId="96942"
													type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="backOutNote" /> 
													
                            <span style="color: #ff0000;"><content:getAttribute attribute="text" beanName="backOutNote"/></span>
                            </td>
                        </tr>
                        </c:if>
                    </tbody>
                </table>
                <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
                       
                        <table width="426" border="0" cellpadding="0" cellspacing="0">
                            <tbody>
                                <tr>
                                    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
                                    <td><img src="/assets/unmanaged/images/s.gif" width="400" height="1"></td>
                                    <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                                    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                </tr>
                                <tr class="tablehead">
                                    <td colspan="5" class="tableheadTD1">
                                        &nbsp;
                                        <strong>
                                 <content:getAttribute beanName="layoutPageBean"
                                    attribute="body1Header" />
                              </strong>
                                    </td>
                                </tr>
                                <tr class="datacell1">
                                    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                                    <td colspan="2" align="center">
                                        <table>
                                            <tbody>
                                                <tr>
                                                    <td colspan="2">&nbsp;</td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <c:if test="${not empty passwordResetAuthenticationForm.maskedMobile}">
                                                        <div id = "sms">
                                                            <form:radiobutton disabled="${passwordResetAuthenticationForm.smsSwithOn}" path="passcodeDeliveryPreference" value="SMS" />
                                                            <content:getAttribute attribute="body1" beanName="layoutPageBean">
                                                                <content:param>
                                                                    <c:out value="${passwordResetAuthenticationForm.maskedMobile}" />
                                                                </content:param>
                                                            </content:getAttribute>
                                                        </div>    
                                                            <br>
                                                        <div id = "vmobile">
                                                            <form:radiobutton disabled="${passwordResetAuthenticationForm.voiceSwithOn}" path="passcodeDeliveryPreference" value="VOICE_TO_MOBILE" />
                                                            <content:getAttribute attribute="body2" beanName="layoutPageBean">
                                                                <content:param>
                                                                    <c:out value="${passwordResetAuthenticationForm.maskedMobile}" />
                                                                </content:param>
                                                            </content:getAttribute>
                                                        </div>
                                                            <br>
                                                        </c:if>
                                                        <c:if test="${not empty passwordResetAuthenticationForm.maskedPhone}">
                                                        <div id = "vphone">
                                                            <form:radiobutton disabled="${passwordResetAuthenticationForm.voiceSwithOn}" path="passcodeDeliveryPreference" value="VOICE_TO_PHONE" />
                                                            <content:getAttribute attribute="body3" beanName="layoutPageBean">
                                                                <content:param>
                                                                    <c:out value="${passwordResetAuthenticationForm.maskedPhone}" />
                                                                </content:param>
                                                            </content:getAttribute>
                                                        </div>     
                                                            <br>
                                                        </c:if>
                                                        <c:if test="${not empty passwordResetAuthenticationForm.maskedEmail}">
                                                        <div id = "email">
                                                        <content:contentBean contentId="96892" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="email" />
                                                            <form:radiobutton disabled="${passwordResetAuthenticationForm.emailSwithOn}" path="passcodeDeliveryPreference" value="EMAIL" />
                                                            <content:getAttribute attribute="text" beanName="email">
                                                                <content:param>
                                                                    <c:out value="${passwordResetAuthenticationForm.maskedEmail}" />
                                                                </content:param>
                                                            </content:getAttribute>
                                                        </div>   
                                                            <br>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td></td>
                                                    <td>
                                                        <input class="button100Lg" type="submit" value="Send" name="action">
                                                    </td>
                                                </TR>
                                                <script language="javascript">
                                                    var onenter = new OnEnterSubmit('action', 'Send');
                                                    onenter.install();
                                                </script>
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
                    </c:when>
                    <c:otherwise>
                        <img src="/assets/unmanaged/images/s.gif" width="402" height="23">
                        <table width="520" border="0" cellspacing="0" cellpadding="0">
                            <tbody>
                                <tr>
                                    <td>
                                    <content:contentBean contentId="96880" type="<%=ContentConstants.TYPE_LAYOUT_PAGE%>" id="dynamicContent" />
                                    <content:getAttribute attribute="introduction2" beanName="dynamicContent">
                                        <content:param><b>Email</b></content:param>
                                        <content:param>
                                           <b><c:out value="${passwordResetAuthenticationForm.maskedEmail}" /></b>
                                        </content:param>
                                    </content:getAttribute>
                                    </td>
                                </tr>
                                <tr>
                                    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="30"></td>
                                </tr>
                                <tr>
                                    
                                    <td><img src="/assets/unmanaged/images/s.gif" width="400" height="1">
                                        <input type="hidden" name="passcodeDeliveryPreference" value="EMAIL"/>
                                        <input class="button100Lg" type="submit" value="Send" name="action">
                                    </td>
                                </tr>
                                <script language="javascript">
                                    var onenter = new OnEnterSubmit('action',
                                        'Send');
                                    onenter.install();
                                </script>
                            </tbody>
                        </table>
                    </c:otherwise>
                    </c:choose>
                </ps:form>
                <br>
            </td>
				<td width="5%" height="312" valign="top" class="fixedTable">
					<img src="/assets/unmanaged/images/s.gif" width="20" height="1">
				</td>
				<td width="15%" height="312" valign="top" class="fixedTable">
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" >
					
				</content:rightHandLayerDisplay>
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer3" />
				</td>
				
        </tr>
    </tbody>
</table>
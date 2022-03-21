<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>
<%@ page import="com.manulife.pension.platform.web.investment.IPSViewParticiapantNotificationForm"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet/buttons.css" />

<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
	contentId="${contentConstants.IPS_PARTICIPANT_OVERLAY_PAGE_TITLE}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsParticipantOverlayTitle" />
	
<content:contentBean
	contentId="${contentConstants.IPS_CONTACT_NAME_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsContactNameText" />
	
<content:contentBean
	contentId="${contentConstants.IPS_CONTACT_INFO_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsContactInfoText" />
	
<content:contentBean
	contentId="${contentConstants.IPS_CONTACT_COMMENT_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="ipsContactCommentText" />
	
<content:contentBean
	contentId="${contentConstants.LABEL_STREET_ADDRESS}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="street" />
	
<content:contentBean
	contentId="${contentConstants.LABEL_CITY_AND_STATE}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="city" />
	
<content:contentBean
	contentId="${contentConstants.LABEL_ZIP_CODE}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="zipCode" />
	
<mrtl:noCaching/>
<ps:form method="POST" modelAttribute="ipsViewParticiapantNotificationForm" name="ipsViewParticiapantNotificationForm" action="/do/dummy/">
<input type="hidden" name="countdown" id="countdown" size="3" value="360">
<table width="350" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="4" class="tablehead" height="20"><Strong><content:getAttribute attribute="title" beanName="ipsParticipantOverlayTitle"/></Strong>
		</td>		
	</tr>
	<tr>
		<td colspan="4"><div id="errorMessagesDiv" style="display: none;">
				</div>
		</td>		
	</tr>
	<tr>
		<td width="20">
			<img src="/assets/unmanaged/images/s.gif"
				height="20" width="20" />
		</td>
	</tr>
	<tr height="30">
		<td width="20">
			<img src="/assets/unmanaged/images/s.gif"
				height="1" width="5" />
		</td>
		<td width="80">
			<content:getAttribute attribute="text" beanName="ipsContactNameText"/>
		</td>
		<td width="80">
<form:input path="contactName" maxlength="60" size="26"  /> 
		</td>
	</tr>	
	<tr height="30"> 
		<td width="20">
			<img src="/assets/unmanaged/images/s.gif"
				height="1" width="5" />
		</td>
		<td width="80">
			<content:getAttribute attribute="text" beanName="street"/>
		</td>
		<td width="80">
<form:input path="street" maxlength="60" size="26" ></form:input>
		</td>
	</tr>	
	
	<tr height="30">
		<td width="20">
			<img src="/assets/unmanaged/images/s.gif"
				height="1" width="5" />
		</td>
		<td width="80">
			<content:getAttribute attribute="text" beanName="city"/>
		</td>
		<td width="80">
<form:input path="cityAndState" maxlength="60" size="26" ></form:input>
		</td>
	</tr>
	<tr height="30">
		<td width="20">
			<img src="/assets/unmanaged/images/s.gif"
				height="1" width="5" />
		</td>
		<td width="80">
			<content:getAttribute attribute="text" beanName="zipCode"/>
		</td>
		<td width="80">
<form:input path="zipCode" maxlength="5" size="26" ></form:input>
		</td>
	</tr>
	<tr height="30">
		<td width="20" height="20">
			<img src="/assets/unmanaged/images/s.gif"
				height="1" width="5" />
		</td>
		<td width="100">
			<content:getAttribute attribute="text" beanName="ipsContactInfoText"/>
		</td>
		<td width="100">
		
<form:input path="telephoneNumber.areaCode" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" />
-<form:input path="telephoneNumber.phonePrefix" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" />
-<form:input path="telephoneNumber.phoneSuffix" maxlength="4" size="4" />
		
	</td>
			
	</tr>	
	<tr height="30">
		<td>
			<img src="/assets/unmanaged/images/s.gif"
				height="1" width="5" />
		</td>
		<td width="100">
			<content:getAttribute attribute="text" beanName="ipsContactCommentText"/>
		</td>
		<td width="100">
<form:textarea path="comments" onkeydown="limitText(this.form.comments,this.form.countdown,300)" ></form:textarea>
		</td>
	</tr>
</table>


<table width="325" border="0" cellspacing="0" cellpadding="0">	
	<tr>
		<td>
			<img src="/assets/unmanaged/images/s.gif"
				height="20" width="20" />
		</td>
	</tr>
    <tr>
		<td>
			<img src="/assets/unmanaged/images/s.gif"
				height="1" width="50" />
		</td>
        <td >
           	<input class="button100Lg" id="Cancel" type="button" value="Cancel" onclick="doCancelIreport()"/>
            <input class="button100Lg" id="Generate" type="button" value="Generate" onclick="doViewReportsPs('${reviewRequestId}','${isFromLandingPage}','false')"/>
        </td>
    </tr>
</table>
</ps:form>

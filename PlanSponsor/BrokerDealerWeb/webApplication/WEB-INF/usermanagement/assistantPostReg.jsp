<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>


<%@page import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>


<%@page import="com.manulife.pension.bd.web.messagecenter.BDMessageCenterFacade"%>
<%@page import ="com.manulife.pension.bd.web.usermanagement.BrokerAssistantUserManagementForm" %>
<%@page import="com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerAssistantUserProfile" %>

<utils:cancelProtection name="manageAssistantForm" changed="${manageAssistantForm.changed}" 
	exclusion="['action','selectedFirmName']"/>

<c:set var="firms" value="${manageAssistantForm.firms}"/>

 


<content:contentBean contentId="<%=BDContentConstants.USERMANAGEMENT_PROFILE_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="webProfile" />
<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PERSONAL_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="personalInfo" />
<content:contentBean contentId="<%=BDContentConstants.OTHER_PREF_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" 
id="preferencesSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_SECTION_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MESSAGE%>" id="licenseSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_TEXT%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="licenseText" />
<content:contentBean contentId="<%=BDContentConstants.ASSOCIATED_RIA_FIRMS_TITLE%>" 
   type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="riaFirmSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.DELETE_ASSISTANT_WARNING%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"
  id="deleteWarning" />
<content:contentBean contentId="<%=BDContentConstants.NO_PERMISSION_CHECK_WARNING%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"id="permissionCheckWarning" />
  

<script type="text/javascript">
<!--
var firms = [];
var activeFirmIds = [];
var firmError = '<content:getAttribute beanName="invalidFirm" attribute="text" filter="true"/>';  
var deleteWarning = '<content:getAttribute beanName="deleteWarning" attribute="text" filter="true"/>';
var permissionCheckWarning = '<content:getAttribute beanName="permissionCheckWarning" attribute="text" filter="true"/>';
	
	function doPermissionCheckAndSave(form, action, btn) {
		if (firms.length == 0) {
			return doProtectedSubmitBtn(form, action, btn);
		}  
		else{
			var permissionString = form.firmPermissionsListStr.value;
			if (permissionString == null || permissionString == '') {
				if(permissionCheckWarning == null || permissionCheckWarning == ''){
					permissionCheckWarning = 'There are no RIA statement permissions for this user.  If you wish to proceed select the Continue button.  If you wish to adjust the selections use the Cancel button.'
				}
				if (confirm(permissionCheckWarning)) {
					return doProtectedSubmitBtn(form, action, btn);
				} else {
					return false;
				}
			} else {
				return doProtectedSubmitBtn(form, action, btn);
			}
	
		}
	
	}
	
	function doDelete(btn) {
		if (confirm(deleteWarning)) {        
		  return doProtectedSubmitBtn(document.manageAssistantForm, 'delete', btn);
		} else {
		  return false;
		}
	}

	function doResetPassword(btn) {
		  return doProtectedSubmitBtn(document.manageAssistantForm, 'resetPassword', btn); 
	}
	function doPasscodeView(btn) {
		  return doProtectedSubmitBtn(document.manageAssistantForm, 'passcodeView', btn); 
	}
	function doPasscodeExemption(btn) {
		  return doProtectedSubmitBtn(document.manageAssistantForm, 'exemptPasscode', btn); 
	}
	
	function doMimic(btn) {
		  return doProtectedSubmitBtn(document.manageAssistantForm, 'mimic', btn);
	}

	function selectBroker() {
		var linkObj = document.getElementById('brokerLink');
		doProtectedSubmit(document.manageAssistantForm, 'selectBroker', linkObj);
	}
	
	function doSave(btn) {
		  return doProtectedSubmitBtn(document.manageAssistantForm, 'save', btn);
	}
	
	function addFirm(frm) {
		  var lastSelectedRiaFirmName = document.getElementById("lastSelectedRiaFirmName").value;
		  if(lastSelectedRiaFirmName != "" && frm.selectedFirmId.value != "") { // user has selected a firm from drop-down
		  	if(lastSelectedRiaFirmName == frm.selectedFirmName.value) { //After selecting no changes were made
				addNewFirm(frm);
		    }
		    else { //Firm name is modified
		    	verifyFirmName(frm.selectedFirmName.value, frm, firmError);
		    }
		  }
		  else { //User has not selected a firm. Might have copied the entire firm name. So we send another AJAX
		  //request to validate the firm name.
			  verifyFirmName(frm.selectedFirmName.value, frm, firmError);
		  }
	}

	function addNewFirm(frm) {
	  addFirmToList(new bdFirm(frm.selectedFirmId.value, frm.selectedFirmName.value, true));
	  frm.selectedFirmId.value ="";
	  frm.selectedFirmName.value ="";
	  frm.lastSelectedRiaFirmName.value ="";
	  refreshFirms();
	  frm.changed.value=true;
	  changed=true;
	}
	
	function updateFirmPermission(obj,firmId) {
		 var updatedList = [];
		    for (i=0; i < firms.length; i++) {
		        if (firms[i].id == firmId) {
		        	changedfirm = new bdFirm(firms[i].id, firms[i].name, obj.checked);
		        	updatedList.push(changedfirm);
		        }else{
		        	updatedList.push(firms[i]);
		        }
		    }    
		    firms = updatedList;
		    document.manageAssistantForm.changed.value=true;
		    changed=true;
	}

	function bdFirm(id, name, permission) {
	    this.id = id;
	    this.name = name;
	    this.permission = permission;
	}

	function addFirmToList(firm) {
	    for (i=0; i < firms.length; i++) {
	        if (firms[i].id == firm.id) {
	        	alert("The firm has already been added.");            
	            return;
	        }
	    }
	    firms.push(firm);
	    
	}
	
	function addActiveFirmIdsToList(firm) {
	    for (i=0; i < activeFirmIds.length; i++) {
	        if (activeFirmIds[i] == firm.id) {
	        	alert("The firm has already been added.");            
	            return;
	        }
	    }
	    activeFirmIds.push(firm.id);
	}

	function sortFirm(f1, f2) {
		if (f1.name < f2.name) {
			return -1;
		} else {
		 	return 1;
	 	}
	}

	function removeFirm(firmId) {
	    var newList = [];
	    for (i=0; i < firms.length; i++) {
	        if (firms[i].id != firmId) {
	            newList.push(firms[i]);
	        }
	    }    
	    firms = newList;
	    refreshFirms();   
	    document.manageAssistantForm.changed.value=true;
	    changed=true;
	 }
	    
	function refreshFirms() {
        elem = document.getElementById("firms");
        var buf=[];
        buf.push("<table style='width:100%'>");
        if(firms.length)
        	{
        	buf.push("<tr><th></th><th style='width:100%;font-size:12px' valign='middle'><label> Firm</label> </th>");
		     buf.push("<th style='width:20%;font-size:12px'  align='center'> <label>View RIA Statements</label></th> <th style='width:25%'> </th> </tr>");
        	}
	     
		for (i=0; i < firms.length; i++) {
			
			buf.push("<tr><td style='font-weight:normal;font-size:12px'>" + (i+1) + ". " + " </td><td style='font-weight:normal;font-size:12px'>");
            buf.push(firms[i].id + " - " + firms[i].name);
            buf.push("</td>");
            if(activeFirmIds.indexOf(firms[i].id) == -1) {
            	if(firms[i].permission == true){
            		buf.push("</td><td align='center'><input type='checkbox' checked='checked' style='margin-center:80px;' onclick='updateFirmPermission(this, " + firms[i].id + " )'/> </td>");
            	}else{
            		buf.push("</td><td align='center'><input type='checkbox' style='margin-center:80px;' onclick='updateFirmPermission(this, " + firms[i].id + " )'/> </td>");
            	}
            } else {
            	if(firms[i].permission == true){
            		buf.push("</td><td align='center'><input type='checkbox' checked='checked' style='margin-center:80px;' disabled /> </td>");
            	}else{
            		buf.push("</td><td align='center'><input type='checkbox' style='margin-center:80px;' disabled /> </td>");
            	}
            }
            buf.push("<td><img src='/assets/unmanaged/images/buttons/remove_firm.gif' alt='Remove Firm Image' width='87' height='19' onclick='removeFirm(\"");
            buf.push(firms[i].id);
            buf.push("\")'/></td></tr>");
            
        }
        buf.push("<br>");
        buf.pop();
        elem.innerHTML = buf.join("");
        buf.push("</table>");
        
    }

	    function getFirmListAsString() {
	        var buf = "";
	        for (i=0; i < firms.length; i++) {
	            buf += firms[i].id;
	            buf +=",";
	        }
	        return buf;
	    }
	    
	    function getFirmPermissionsString() {
	        var buf = "";
	        for (i=0; i < firms.length; i++) {
	        	if(firms[i].permission == true){
	        		buf += firms[i].id;
	                buf +=",";
	        	}
	        }
	        return buf;
	    }
	    
	    function populateFirms() {
	    	document.manageAssistantForm.firmListStr.value = getFirmListAsString();
	    	document.manageAssistantForm.firmPermissionsListStr.value = getFirmPermissionsString();
	    }
//-->
</script>


<%
BrokerAssistantUserManagementForm manageAssistantForm  = (BrokerAssistantUserManagementForm)request.getAttribute("manageAssistantForm");
ExtendedBrokerAssistantUserProfile assistantProfile= (ExtendedBrokerAssistantUserProfile)manageAssistantForm.getAssistantProfile();
pageContext.setAttribute("assistantProfile", assistantProfile);

%>

<bd:form action="/do/manage/assistant" name="manageAssistantForm" modelAttribute="manageAssistantForm">
	
<form:hidden path="action"/>
<form:hidden path="changed"/>
<form:hidden path="firmListStr" id="firmListStr"/>
<form:hidden path="firmPermissionsListStr" id="firmPermissionsListStr"/>

<div id="contentFull">
<layout:pageHeader nameStyle="h1"/>
<report:formatMessages scope="session"/>
  <c:if test="${manageAssistantForm.updateSuccess}">
  <content:contentBean contentId="<%=BDContentConstants.UPDATE_ASSISTANT_SUCCESS%>" 
   type="<%=BDContentConstants.TYPE_MESSAGE%>" id="assistantSuccessMessage" />
   <div class="message message_info">
  <dl>
    <dt>Information Message</dt>
    <dd>1&nbsp;&nbsp; 
       <content:getAttribute beanName='assistantSuccessMessage' attribute='text'>
       </content:getAttribute>
     </dd>
    </dl>
</div>
</c:if>
	<div class="BottomBorder">
	   <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="webProfile"/></div>   
	</div>
	<div class="label">Role:</div>
	<div class="inputText"><%=BDUserRoleDisplayNameUtil.getInstance().getDisplayName(BDUserRoleType.FinancialRepAssistant) %></div> 
	
    <div class="label">Profile Status:</div>
    <div class="inputText">
     ${manageAssistantForm.profileStatus}
    </div> 
    <div class="label">Password Status:</div>
    <div class="inputText">${manageAssistantForm.passwordStatus}&nbsp;</div> 
	<br/>		
	<div class="BottomBorder">
      <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="personalInfo"/></div>
    </div>
    
    <c:if test="${not empty manageAssistantForm.brokerDealerFirmNames}">    
    <div class="label">BD Firm Name:</div>
    <div class="inputText">
      <c:forEach var="firmName" items="${manageAssistantForm.brokerDealerFirmNames}" varStatus="loopStatus">
         ${firmName} 
         <c:if test="${not loopStatus.last}"> 
           <br/>
         </c:if>
      </c:forEach>&nbsp;     
    </div>
	</c:if>
    <userprofile:extUserPersonalInfo profile="${manageAssistantForm.assistantProfile}"/>
    
    <div class="label">Financial Representative:</div>
    <div class="inputText">
     <label>
     <a id="selectBroker" href="javascript:selectBroker()">${manageAssistantForm.assistantProfile.parentBroker.firstName} ${manageAssistantForm.assistantProfile.parentBroker.lastName}</a>
     </label>
    </div> 
    <br/>
	<div class="BottomBorder"><div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="licenseSectionTitle"/></div>
    </div>
	<p> <content:getAttribute attribute="text" beanName="licenseText"/> </p>
	<div class="BottomBorder">
		<label>
<form:radiobutton disabled="true" path="assistantProfile.producerLicense" id="yes" value="true"/>
			Yes
		</label>
	    <label>
<form:radiobutton disabled="true" path="assistantProfile.producerLicense" id="no" value="false"/>
		    No 
	    </label>
	   <br/>
	</div>
    <br/>
    
    <userprofile:messageCenterPref preferences="<%=BDMessageCenterFacade.getInstance().getMCPreferences(assistantProfile.getProfileId()) %>"/>
    
	<br/>
	<div class="BottomBorder">
	  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="preferencesSectionTitle"/></div>
	</div>
	<div class="label">Default Fund Listing:<br />
	</div>
	<div class="inputText">
	  <label>
<form:radiobutton disabled="true" path="assistantProfile.fundDefaultSite" id="USA" value="USA"/>
		  USA
	  </label>
	  <label>
<form:radiobutton disabled="true" path="assistantProfile.fundDefaultSite" id="NY" value="NY"/>
		  New York
	  </label>
	   <br/>
	</div>
	<c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM') or (not empty manageAssistantForm.firms)}">
	<div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="riaFirmSectionTitle" attribute="body2Header"/></div>
   </div>
   <c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM')}">
	<div class="label"><b>* RIA Firm ID/Name:</b></div>
   <div style="margin-top:0px;float:left;margin-right:15px;margin-left:9px">
	   <utils:riaFirmSearch firmName="selectedFirmName" firmId="selectedFirmId"/>
   </div>
   </c:if>
   <br class="clearFloat" />
   <div id="firms" class="inputText" style="width:435px;position:relative">
   <table style='width: 100%'>
   			<c:if test="${not empty manageAssistantForm.firms}">
					<tr>
						<th></th>
						<th style='width: 100%; font-size: 12px' valign='middle'><label>
								Firm</label></th>
						<th style='width: 20%; font-size: 12px' align='center'><label>View
								RIA Statements</label></th>
						<th style='width: 25%'></th>
					</tr>
			
			</c:if>
					<c:forEach var="firm" items="${firms}" varStatus="loopStatus">
					
						<%--  <div style='width: 335px; float: left'>
							<div align='left'>--%>
							<tr>
							<td style='font-weight:normal;font-size:12px'>${loopStatus.index+1}. </td>
							<td style='font-weight:normal;font-size:12px'>${firm.id} - ${firm.firmName}</td>
							<c:choose>
								<c:when test="${(userProfile.role.roleType.userRoleCode ne 'RUM')}">
									<c:if test="${firm.firmPermission == true}">
										<td align='center'><input type="checkbox" checked='checked' style='margin-center:80px;' disabled/> </td>
									</c:if>
									<c:if test="${firm.firmPermission == false}">
										<td align='center'><input type="checkbox" style='margin-center:80px;' disabled/> </td>
									</c:if>
									 <%-- <c:if test="${not readOnly}"> --%>
										<td>
											<img src='/assets/unmanaged/images/buttons/remove_firm.gif'
												alt='Remove Firm Image' width='87' height='19' 
												onclick=' '/>
										</td>
									<%-- </c:if> --%>
								</c:when>
								<c:otherwise>
										<c:if test="${firm.firmPermission == true}">
											<td align='center'><input type="checkbox" checked='checked' style='margin-center:80px;' onclick='updateFirmPermission(this,"${firm.id}")'/> </td>
										</c:if>
										<c:if test="${firm.firmPermission == false}">
											<td align='center'><input type="checkbox" style='margin-center:80px;' onclick='updateFirmPermission(this,"${firm.id}")'/> </td>
										</c:if>
										<%-- <c:if test="${not readOnly}">--%>
											<td>
												<img src='/assets/unmanaged/images/buttons/remove_firm.gif'
													alt='Remove Firm Image' width='87' height='19' 
													onclick='removeFirm("${firm.id}")'/>
											</td>
										<%-- </c:if>--%>
								</c:otherwise>
							</c:choose>
						</tr>
						
					 
						<script type="text/javascript">
			<!--
					addFirmToList(new bdFirm('${firm.id}', '${firm.firmName}', ${firm.firmPermission}));
			//-->
			</script>
					</c:forEach>
			</table>
    </div>
</c:if>
	<br class="clearFloat"/>
	<div class="formButtons">
	<c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM')}">
	    <div class="formButton">
	      <input type="button" class="blue-btn next" 
	             onmouseover="this.className +=' btn-hover'" 
        onmouseout="this.className='blue-btn next'"
	             name="Save" value="Save"
	             onclick="populateFirms();return doPermissionCheckAndSave(document.manageAssistantForm, 'save', this);">
	    </div>
	    </c:if>
	  <c:if test="${manageAssistantForm.mimicAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="mimic" value="Advisor View"
	        onclick="return doMimic(this)">
        </div> 
	  </c:if>
	    <!--for Passcode exemption -->
	   <c:if test="${manageAssistantForm.passcodeExemptionAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="pcdExc" value="Passcode Exemption"
	        onclick="return doPasscodeExemption(this)">
        </div> 
	  </c:if>	
	  <!--for Passcode -->
	  <c:if test="${manageAssistantForm.passcodeViewAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="resetPcd" value="Passcode View"
	        onclick="return doPasscodeView(this)">
        </div> 
	  </c:if>	
	  <c:if test="${manageAssistantForm.resetPasswordAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="resetPwd" value="Reset Password"
	        onclick="return doResetPassword(this)">
        </div> 
	  </c:if>	  
	  <c:if test="${manageAssistantForm.deleteAllowed}">  
    	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
		onmouseover="this.className +=' btn-hover'" 
        onmouseout="this.className='blue-btn next'"
        name="delete" value="Delete User"
        onclick="return doDelete(this)">
       </div> 
	  </c:if>
  	    <div class="formButton">
	      <input type="button" class="grey-btn back" 
	             onmouseover="this.className +=' btn-hover'" 
	             onmouseout="this.className='grey-btn back'"
	             name="cancel" value="Cancel"
	             onclick="return doCancelBtn(document.manageAssistantForm, this)"> 
	    </div>
      </div>  	  
    </div>
</bd:form>

<layout:pageFooter/>

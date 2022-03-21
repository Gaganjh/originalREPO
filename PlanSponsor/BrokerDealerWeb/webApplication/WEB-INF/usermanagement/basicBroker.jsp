<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.bd.web.util.BDWebCommonUtils"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>

<utils:cancelProtection name="manageBasicBrokerForm" changed="${manageBasicBrokerForm.changed}"
     exclusion="['action','selectedRiaFirmName']"/>

<content:contentBean contentId="<%=BDContentConstants.USERMANAGEMENT_PROFILE_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="webProfile" />
<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PERSONAL_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="personalInfo" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_PREFERENCES_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" 
id="preferencesSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.ASSOCIATED_RIA_FIRMS_TITLE%>" 
   type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="riaFirmSectionTitle" />

<content:contentBean contentId="<%=BDContentConstants.DELETE_BASIC_BROKER_WARNING%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"
  id="deleteWarning" />
  
<content:contentBean contentId="<%=BDContentConstants.NO_PERMISSION_CHECK_WARNING%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"id="permissionCheckWarning" />
  
<c:set var="riafirms" value="${manageBasicBrokerForm.riafirms}"/>

 

<script type="text/javascript">
<!--

var riafirms = [];
var activeRiaFirmIds = [];
var deleteWarning = '<content:getAttribute beanName="deleteWarning" attribute="text" filter="true"/>';
var permissionCheckWarning = '<content:getAttribute beanName="permissionCheckWarning" attribute="text" filter="true"/>';

    function doPermissionCheckAndSave(form, action, btn) {
		if (riafirms.length == 0) {
			return doProtectedSubmitBtn(form, action, btn);
		}  
		else{
			var permissionString = form.riaFirmPermissionsListStr.value;
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

    function doResendInvitation(btn) {
    	return doSubmitCheckChange(document.manageBasicBrokerForm, 'resendActivation', btn);
    }
    
    function doDelete(btn) {
		if (confirm(deleteWarning)) {        
    	  return doProtectedSubmitBtn(document.manageBasicBrokerForm, 'delete', btn);
		} else {
		  return false;
		}
    }

	function doResetPassword(btn) {
		  return doSubmitCheckChange(document.manageBasicBrokerForm, 'resetPassword', btn);
	}
	function doPasscodeExemption(btn) {
		  return doProtectedSubmitBtn(document.manageBasicBrokerForm, 'exemptPasscode', btn); 
	}

	function doPasscodeView(btn) {
		  return doSubmitCheckChange(document.manageBasicBrokerForm, 'passcodeView', btn); 
	}
	
	
	function doSave(btn){
		return doProtectedSubmitBtn(document.manageBasicBrokerForm, 'save', btn);
	}

	function doMimic(btn) {
		return doSubmitCheckChange(document.manageBasicBrokerForm, 'mimic', btn)
	}
	
	function toggleOrg(val) {
		if(val == "O") {
			document.getElementById("firm").style.display = "inline";
			document.getElementById("company").style.display = "none";
		}
		else if(val == "I") {
			document.getElementById("firm").style.display = "none";
			document.getElementById("company").style.display = "inline";
		}
	}
	function addRiaFirm(frm) {
		  var lastSelectedRiaFirmName = document.getElementById("lastSelectedRiaFirmName").value;
		  if(lastSelectedRiaFirmName != "" && frm.selectedRiaFirmId.value != "") { // user has selected a firm from drop-down
		  	if(lastSelectedRiaFirmName == frm.selectedRiaFirmName.value) { //After selecting no changes were made
				addNewRiaFirm(frm);
		    }
		    else { //Firm name is modified
		    	verifyRiaFirmName(frm.selectedRiaFirmName.value, frm, firmError);
		    }
		  }
		  else { //User has not selected a firm. Might have copied the entire firm name. So we send another AJAX
		  //request to validate the firm name.
			  verifyRiaFirmName(frm.selectedRiaFirmName.value, frm, firmError);
		  }
	}

	function addNewRiaFirm(frm) {
		  addRiaFirmToList(new riaFirm(frm.selectedRiaFirmId.value, frm.selectedRiaFirmName.value, true));
		  frm.selectedRiaFirmId.value ="";
		  frm.selectedRiaFirmName.value ="";
		  frm.lastSelectedRiaFirmName.value ="";
		  refreshRiaFirms();
		  frm.changed.value=true;
		  changed=true;
		}


		function riaFirm(id, name, permission) {
		    this.id = id;
		    this.name = name;
		    this.permission = permission;
		}

		function addRiaFirmToList(firm) {
		    for (i=0; i < riafirms.length; i++) {
		        if (riafirms[i].id == firm.id) {
		        	alert("The firm has already been added.");            
		            return;
		        }
		    }
		    riafirms.push(firm);
		    
		}
		function addActiveRiaFirmIdsToList(firm) {
		    for (i=0; i < activeRiaFirmIds.length; i++) {
		        if (activeRiaFirmIds[i] == firm.id) {
		        	alert("The firm has already been added.");            
		            return;
		        }
		    }
		    activeRiaFirmIds.push(firm.id);
		}

		function sortRiaFirm(f1, f2) {
			if (f1.name < f2.name) {
				return -1;
			} else {
			 	return 1;
		 	}
		}
		function removeRiaFirm(firmId) {
		    var newList = [];
		    for (i=0; i < riafirms.length; i++) {
		        if (riafirms[i].id != firmId) {
		            newList.push(riafirms[i]);
		        }
		    }    
		    riafirms = newList;
		    refreshRiaFirms();   
		    document.manageBasicBrokerForm.changed.value=true;
		    changed=true;
		 }
	    
		function refreshRiaFirms() {
	        elem = document.getElementById("riafirms");
	        var buf=[];
	        buf.push("<table style='width:100%'>");
	        if(riafirms.length)
	        	{
	        	buf.push("<tr><th></th><th style='width:100%;font-size:12px' valign='middle'><label> Firm</label> </th>");
			     buf.push("<th style='width:20%;font-size:12px'  align='center'> <label>View RIA Statements</label></th> <th style='width:25%'> </th> </tr>");
	        	}
		     
			for (i=0; i < riafirms.length; i++) {
				
				buf.push("<tr><td style='font-weight:normal;font-size:12px'>" + (i+1) + ". " + " </td><td style='font-weight:normal;font-size:12px'>");
	            buf.push(riafirms[i].id + " - " + riafirms[i].name);
	            buf.push("</td>");
	            if(activeRiaFirmIds.indexOf(riafirms[i].id) == -1) {
	            	if(riafirms[i].permission == true){
	            		buf.push("</td><td align='center'><input type='checkbox' checked='checked' style='margin-center:80px;' onclick='updateRiaFirmPermission(this, " + riafirms[i].id + " )'/> </td>");
	            	}else{
	            		buf.push("</td><td align='center'><input type='checkbox' style='margin-center:80px;' onclick='updateRiaFirmPermission(this, " + riafirms[i].id + " )'/> </td>");
	            	}
	            } else {
	            	if(riafirms[i].permission == true){
	            		buf.push("</td><td align='center'><input type='checkbox' checked='checked' style='margin-center:80px;' disabled /> </td>");
	            	}else{
	            		buf.push("</td><td align='center'><input type='checkbox' style='margin-center:80px;' disabled /> </td>");
	            	}
	            }
	            buf.push("<td><img src='/assets/unmanaged/images/buttons/remove_firm.gif' alt='Remove Firm Image' width='87' height='19' onclick='removeRiaFirm(\"");
	            buf.push(riafirms[i].id);
	            buf.push("\")'/></td></tr>");
	            
	        }
	        buf.push("<br>");
	        buf.pop();
	        elem.innerHTML = buf.join("");
	        buf.push("</table>");
	        
	    }
	    
		function updateRiaFirmPermission(obj,firmId) {
			 var updatedList = [];
			    for (i=0; i < riafirms.length; i++) {
			        if (riafirms[i].id == firmId) {
			        	changedfirm = new riaFirm(riafirms[i].id, riafirms[i].name, obj.checked);
			        	updatedList.push(changedfirm);
			        }else{
			        	updatedList.push(riafirms[i]);
			        }
			    }    
			    riafirms = updatedList;
			    document.manageBasicBrokerForm.changed.value=true;
			    changed=true;
		}

		 function getRiaFirmListAsString() {
		        var buf = "";
		        for (i=0; i < riafirms.length; i++) {
		            buf += riafirms[i].id;
		            buf +=",";
		        }
		        return buf;
		    }
	    
		 function getRiaFirmPermissionsString() {
		        var buf = "";
		        for (i=0; i < riafirms.length; i++) {
		        	if(riafirms[i].permission == true){
		        		buf += riafirms[i].id;
		                buf +=",";
		        	}
		        }
		        return buf;
		    }
	    
		 function populateRiaFirms() {
		    	document.manageBasicBrokerForm.riaFirmListStr.value = getRiaFirmListAsString();
		    	document.manageBasicBrokerForm.riaFirmPermissionsListStr.value = getRiaFirmPermissionsString();
		    }
//-->
</script>


<bd:form action="/do/manage/basicBroker" modelAttribute="manageBasicBrokerForm" name="manageBasicBrokerForm">

<form:hidden path="action"/>
<form:hidden path="changed"/>
<form:hidden path="riaFirmListStr" id="riaFirmListStr"/>
<form:hidden path="riaFirmPermissionsListStr" id="riaFirmPermissionsListStr"/>

	<div id="contentFull">
		<layout:pageHeader nameStyle="h1"/>
		<%
    		BDSessionHelper.moveMessageIntoRequest(request);
		%>
		<report:formatMessages scope="request"/>

		<c:if test="${manageBasicBrokerForm.updateSuccess}">
		  <utils:info contentId="<%=BDContentConstants.UPDATE_BASIC_BROKER_SUCCESS %>"/>
		</c:if>
		
		<c:if test="${manageBasicBrokerForm.resendActivationSuccess}">
		  <utils:info contentId="<%=BDContentConstants.BASIC_BROKER_RESEND_ACTIVATION_SUCCESS %>"/>
		</c:if>

		<div class="BottomBorder">
		  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="webProfile"/></div>
		  <div class="GrayLT Right">* = Required Field</div>
		</div>
		<div class="label">Role:</div>
		<div class="inputText"><%=BDUserRoleDisplayNameUtil.getInstance().getDisplayName(BDUserRoleType.BasicFinancialRep) %></div> 
		<div class="label">Profile Status:</div>
		<div class="inputText">
		 ${manageBasicBrokerForm.profileStatus}
		</div>
		<c:if test="${manageBasicBrokerForm.profileActivated}"> 
		 <div class="label">Password Status:</div>
		 <div class="inputText">${manageBasicBrokerForm.passwordStatus}</div> 
		</c:if>
	   <div class="BottomBorder">
		 <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="personalInfo"/></div>
	   </div>
	   <div class="label">* Affiliated with:</div>
	   <div class="inputText">
		   <label>
<form:radiobutton disabled="${(not manageBasicBrokerForm.updateAllowed)or (userProfile.role.roleType.userRoleCode eq 'RUM')}" onclick="toggleOrg(this.value)" path="partyType" id="org" value="O"/>
				Broker Dealer Firm
		   </label>
		   <label>
<form:radiobutton disabled="${(not manageBasicBrokerForm.updateAllowed) or (userProfile.role.roleType.userRoleCode eq 'RUM')}" onclick="toggleOrg(this.value)" path="partyType" id="indiv" value="I"/>
			   Independent
		   </label>
	   </div>
	   <c:choose>
			<c:when test="${manageBasicBrokerForm.partyType == 'O'}">
				<c:set var="firmStyle" value="display:inline"/>
				<c:set var="companyStyle" value="display:none"/>
			</c:when>
			<c:when test="${manageBasicBrokerForm.partyType == 'I'}">
				<c:set var="firmStyle" value="display:none"/>
				<c:set var="companyStyle" value="display:inline"/>
			</c:when>
			<c:otherwise>
				<c:set var="firmStyle" value="display:none"/>
				<c:set var="companyStyle" value="display:none"/>
			</c:otherwise>
		</c:choose>
		<c:choose>
		  <c:when test="${(manageBasicBrokerForm.updateAllowed) and (userProfile.role.roleType.userRoleCode ne 'RUM')}">				
				<div id="firm" style="${firmStyle}">
					<div class="label" style="margin-top:30px;">
						<label>
						* BD Firm Name:
						</label>
					</div>
					<div class="inputText">
						<label>
							<utils:firmSearch firmName="firmName" firmId="firmId"/>
						</label>
					</div>  
				</div>
				<div id="company" style="${companyStyle}">
					<div class="label">Company Name:</div>
					<div class="inputText">
<form:input path="companyName" maxlength="30" cssClass="input"/>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div id="firm" style="${firmStyle}">
					<div class="label">* BD Firm Name:</div>
					<div class="inputText">
					 ${manageBasicBrokerForm.firmName}
					</div> 
				</div>
				<div id="company" style="${companyStyle}">
					<div class="label">Company Name:</div>
					<div class="inputText">
					 ${manageBasicBrokerForm.companyName} &nbsp;
					</div> 	
				</div>			
			</c:otherwise>
		</c:choose>		
	    <userprofile:extUserPersonalInfo profile="${manageBasicBrokerForm.basicBrokerUserProfile}"/>
		<div class="BottomBorder">
		  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="preferencesSectionTitle"/></div>
		</div>
		<div class="label">Default Fund Listing:<br />
		</div>
		<div class="inputText">
		  <label>
<form:radiobutton disabled="true" path="basicBrokerUserProfile.fundDefaultSite" id="USA" value="USA"/>
			  USA
		  </label>
		  <label>
<form:radiobutton disabled="true" path="basicBrokerUserProfile.fundDefaultSite" id="NY" value="NY"/>
			  New York
		  </label>
		   <br/>
		</div>
		<c:if test="${(((userProfile.role.roleType.userRoleCode eq 'RUM')or (not empty manageBasicBrokerForm.riafirms)) and ( manageBasicBrokerForm.profileStatus ne 'Not Activated'))}">
		<div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="riaFirmSectionTitle" attribute="body2Header"/></div>
   </div>
   <c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM') and (manageBasicBrokerForm.profileStatus ne 'Not Activated')}">
	<div class="label"><b>* RIA Firm ID/Name:</b></div>
   <div style="margin-top:0px;float:left;margin-right:15px;margin-left:9px">
	   <utils:riaFirmSearch firmName="selectedRiaFirmName" firmId="selectedRiaFirmId"/>
   </div>
   </c:if>
   <br class="clearFloat" />
   <div id="riafirms" class="inputText" style="width:435px;position:relative">
   		<table style='width: 100%'>
   		<c:if test="${not empty manageBasicBrokerForm.riafirms}">
					<tr>
						<th></th>
						<th style='width: 100%; font-size: 12px' valign='middle'><label>
								Firm</label></th>
						<th style='width: 20%; font-size: 12px' align='center'><label>View
								RIA Statements</label></th>
						<th style='width: 25%'></th>
					</tr>
</c:if>

					<c:forEach var="firm" items="${riafirms}" varStatus="loopStatus">

						<%--  <div style='width: 335px; float: left'>
							<div align='left'>--%>
						<tr>
							<td style='font-weight: normal; font-size: 12px'>${loopStatus.index+1}.
							</td>
							<td style='font-weight: normal; font-size: 12px'>${firm.id} - ${firm.firmName}</td>
						<c:choose>
						<c:when test="${(userProfile.role.roleType.userRoleCode ne 'RUM') or (manageBasicBrokerForm.profileStatus eq 'Not Activated')}">
							<c:if test="${firm.firmPermission == true}">
								<td align='center'><input type="checkbox" checked='checked' style='margin-center:80px;' disabled/> </td>
							</c:if>
							<c:if test="${firm.firmPermission == false}">
								<td align='center'><input type="checkbox" style='margin-center:80px;' disabled/> </td>
							</c:if>
							<%--  <c:if test="${not readOnly}">--%>
								<td>
									<img src='/assets/unmanaged/images/buttons/remove_firm.gif'
										alt='Remove Firm Image' width='87' height='19' 
										onclick=' '/>
								</td>
								<%-- </c:if>--%>
						</c:when>
						<c:otherwise>
								<c:if test="${firm.firmPermission == true}">
									<td align='center'><input type="checkbox" checked='checked' style='margin-center:80px;' onclick='updateRiaFirmPermission(this,"${firm.id}")'/> </td>
								</c:if>
								<c:if test="${firm.firmPermission == false}">
									<td align='center'><input type="checkbox" style='margin-center:80px;' onclick='updateRiaFirmPermission(this,"${firm.id}")'/> </td>
								</c:if>
								<%-- <c:if test="${not readOnly}">--%>
									<td>
										<img src='/assets/unmanaged/images/buttons/remove_firm.gif'
											alt='Remove Firm Image' width='87' height='19' 
											onclick='removeRiaFirm("${firm.id}")'/>
									</td>
								<%--</c:if>--%>
						</c:otherwise>
					</c:choose>
						</tr>

						<script type="text/javascript">
							<!--
							addRiaFirmToList(new riaFirm('${firm.id}', '${firm.firmName}', ${firm.firmPermission}));
							//-->
					   </script>
				 </c:forEach>
		</table>
 </div>
 </c:if>
	  
        <br class="clearFloat"/>
        <div class="formButtons">
 		 <c:if test="${manageBasicBrokerForm.resendInvitationAllowed}">
	        	<c:choose>
	        		<c:when test="${manageBasicBrokerForm.enableResend}">
				   	<div class="formButton"> 
			        <input type="button" class="blue-btn-medium next" 
						onmouseover="this.className +=' btn-hover'" 
				        onmouseout="this.className='blue-btn-medium next'"
				        name="resend" value="Resend Activation"
				        onclick="return doResendInvitation(this)">
			        </div> 
			        </c:when>
			        <c:otherwise>
					    <div class="formButton">
					      <input type="button" class="disabled-grey-btn next" 
					             name="resend" value="Resend Activation"
					             disabled="disabled">
					    </div> 
			        </c:otherwise>
			    </c:choose>
		 </c:if>
		 <c:if test="${manageBasicBrokerForm.updateAllowed}">
		   	<div class="formButton"> 
	        <input type="button" class="blue-btn next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn next'"
		        name="save" value="Save"
		        onclick="populateRiaFirms();return doPermissionCheckAndSave(document.manageBasicBrokerForm, 'save', this);">
	        </div> 
		 </c:if>
		  <c:if test="${manageBasicBrokerForm.mimicAllowed}">
		   	<div class="formButton"> 
	        <input type="button" class="blue-btn next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn next'"
		        name="mimic" value="Advisor View"
		        onclick="return doMimic(this)">
	        </div> 
		  </c:if>	 
		     <!--for Passcode exemption -->
	   <c:if test="${manageBasicBrokerForm.passcodeExemptionAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="pcdExc" value="Passcode Exemption"
	        onclick="return doPasscodeExemption(this)">
        </div> 
	  </c:if>	
		  
		   <!--for Passcode -->
	  <c:if test="${manageBasicBrokerForm.passcodeViewAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="resetPcd" value="Passcode View"
	        onclick="return doPasscodeView(this)">
        </div> 
	  </c:if>	
		  <c:if test="${manageBasicBrokerForm.resetPasswordAllowed}">
		   	<div class="formButton"> 
	        <input type="button" class="blue-btn-medium next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn-medium next'"
		        name="resetPwd" value="Reset Password"
		        onclick="return doResetPassword(this)">
	        </div> 
		  </c:if>		  
		  <c:if test="${manageBasicBrokerForm.deleteAllowed}">
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
		             onclick="return doCancelBtn(document.manageBasicBrokerForm, this)"> 
		    </div>
		</div>
	 </div>
</bd:form>

<layout:pageFooter/>

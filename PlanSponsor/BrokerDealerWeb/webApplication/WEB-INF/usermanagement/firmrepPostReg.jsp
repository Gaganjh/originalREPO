<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>


<utils:cancelProtection name="manageFirmRepForm" changed="${manageFirmRepForm.changed}"
     exclusion="['action','selectedRiaFirmName']"/>

<content:contentBean contentId="<%=BDContentConstants.USERMANAGEMENT_PROFILE_SECTION_TITLE%>" 
 type="<%=BDContentConstants.TYPE_MESSAGE%>"
 id="webProfile" />
<content:contentBean contentId="<%=BDContentConstants.INTERNAL_USER_PERSONAL_SECTION_TITLE%>" 
 type="<%=BDContentConstants.TYPE_MESSAGE%>"
 id="personalInfo" />
<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SECTION2%>" 
  type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="firmsSection" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_PREFERENCES_SECTION_TITLE%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>" 
  id="preferencesSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_SECTION_TITLE%>" 
   type="<%=BDContentConstants.TYPE_MESSAGE%>" id="licenseSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_TEXT%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="licenseText" />
<content:contentBean contentId="<%=BDContentConstants.ASSOCIATED_RIA_FIRMS_TITLE%>" 
   type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="riaFirmSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.NO_PERMISSION_CHECK_WARNING%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"id="permissionCheckWarning" />
  
<c:set var="readOnly" value="${not manageFirmRepForm.updateAllowed}"/>
<c:set var="firms" value="${manageFirmRepForm.firms}"/>
<c:set var="riafirms" value="${manageFirmRepForm.riafirms}"/>

<jsp:include page="firmrepCommon.jsp" flush="true"/>


<bd:form action="/do/manage/firmrep" method="GET" modelAttribute="manageFirmRepForm" name="manageFirmRepForm">


<form:hidden path="action"/>
<form:hidden path="changed"/>
<form:hidden path="firmListStr"/>
<form:hidden path="riaFirmListStr"/>
<form:hidden path="riaFirmPermissionsListStr"/>

<div id="contentFull">
<layout:pageHeader nameStyle="h1"/>
<c:if test="${manageFirmRepForm.updateSuccess}">
  <utils:info contentId="<%=BDContentConstants.UPDATE_FIRM_REP_SUCCESS %>"/>
</c:if>
<report:formatMessages scope="request"/>

<script type="text/javascript">
<!--
var riafirms = [];
var activeRiaFirmIds = [];
var firmError = '<content:getAttribute beanName="invalidFirm" attribute="text" filter="true"/>';  

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
	    document.manageFirmRepForm.changed.value=true;
	    changed=true;
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
    document.manageFirmRepForm.changed.value=true;
    changed=true;
 }
    
    function refreshRiaFirms() {
        riaelem = document.getElementById("riafirms");
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
        riaelem.innerHTML = buf.join("");
        buf.push("</table>");
        
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
    	document.manageFirmRepForm.riaFirmListStr.value = getRiaFirmListAsString();
    	document.manageFirmRepForm.riaFirmPermissionsListStr.value = getRiaFirmPermissionsString();
    }
  

  //-->
</script>

	<div class="BottomBorder">
	   <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="webProfile"/></div>   
       <div class="GrayLT Right">* = Required Field</div>
	</div>

	<div class="label">Role:</div>
	<div class="inputText"><%=BDUserRoleDisplayNameUtil.getInstance().getDisplayName(BDUserRoleType.FirmRep) %></div> 
	
    <div class="label">Profile Status:</div>
    <div class="inputText">
     ${manageFirmRepForm.profileStatus}
    </div> 

    <div class="label">Password Status:</div>
    <div class="inputText">${manageFirmRepForm.passwordStatus}&nbsp;</div> 

	<br/>		

	<div class="BottomBorder">
      <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="personalInfo"/></div>
    </div>
    
    <div class="label">*First Name:</div>
    <div class="inputText">
     <label>
<form:input path="firstName" disabled="${readOnly}" maxlength="30" size="30" cssClass="input"/>
     </label>
    </div> 
    
    <div class="label">*Last Name:</div>
    <div class="inputText">
     <label>
<form:input path="lastName" disabled="${readOnly}" maxlength="30" size="30" cssClass="input"/>
     </label>
    </div> 
    
    <div class="label">* Email:</div>
    <div class="inputText">
     <label>
<form:input path="emailAddress" disabled="${readOnly}" maxlength="70" size="50" cssClass="input"/>
   	 </label>
    </div>

     <userprofile:address form="${manageFirmRepForm}" readOnly="${readOnly}"/>
   
   <div class="label">* Telephone #:</div>
   <div class="inputText">
        <userprofile:phoneNumInput readOnly="${readOnly}"/>
   </div>
   
   <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="firmsSection" attribute="text"/></div>
   </div>

   <div class="label">* Firm Name:</div>
   <c:if test="${not readOnly}">
   <div style="margin-top:0px;float:left;margin-right:15px;margin-left:9px">
	   <utils:firmSearch firmName="selectedFirmName" firmId="selectedFirmId"/>
   </div>
   <div style="margin-top:15px">
	   <img src="/assets/unmanaged/images/buttons/add_firm.gif" alt="Add Firm Image" width="87" height="19" onclick="addFirm(document.manageFirmRepForm)" />
   </div>
   <br class="clearFloat" />
   
   </c:if>

   <div id="firms" class="inputText" style="width:435px;position:relative">
    <label>
        <c:forEach var="firm" items="${firms}" varStatus="loopStatus">
        			<div style='width:335px;float:left'>
        			  <div align='left'> ${loopStatus.index+1}. ${firm.firmName}</div>
        			</div>
		            <c:if test="${not readOnly}">
                    <div style="float:left">
                      <img src='/assets/unmanaged/images/buttons/remove_firm.gif' alt='Remove Firm Image' width='87' height='19' onclick='removeFirm("${firm.id}")'/> 
                    </div>
                    </c:if>
    		<script type="text/javascript">
			<!--
					addFirmToList(new bdFirm('${firm.id}', '${firm.firmName}'));
			//-->
			</script>
			<br />
			<br />
		</c:forEach>
	</label>
    </div>
   
	<div class="BottomBorder"><div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="licenseSectionTitle"/></div>
    </div>
	<p> <content:getAttribute attribute="text" beanName="licenseText"/> </p>
	<div>
		<label>
<form:radiobutton disabled="true" path="firmRepUserProfile.producerLicense" id="yes" value="true"/>
			Yes
		</label>
	    <label>
<form:radiobutton disabled="true" path="firmRepUserProfile.producerLicense" id="no" value="false"/>
		    No 
	    </label>
	   <br/>
	</div>
    
	<br/>
	<div class="BottomBorder">
	  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="preferencesSectionTitle"/></div>
	</div>
	<div class="label">Default Fund Listing:<br />
	</div>
	<div class="inputText">
	  <label>
<form:radiobutton disabled="${readOnly}" path="defaultSiteLocation" id="USA" value="USA"/>
		  USA
	  </label>
	  <label>
<form:radiobutton disabled="${readOnly}" path="defaultSiteLocation" id="NY" value="NY"/>
		  New York
	  </label>
	   <br/>
	</div>
	<c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM') or (not empty manageFirmRepForm.riafirms)}">
	<div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="riaFirmSectionTitle" attribute="body2Header"/></div>
   </div>
   <c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM')}">
	<div class="label"><b>* RIA Firm ID/Name:</b></div>
   <div style="margin-top:0px;float:left;margin-right:15px;margin-left:9px">
	   <utils:riaFirmSearch firmName="selectedRiaFirmName" firmId="selectedRiaFirmId"/>
   </div>
   </c:if>
   <br class="clearFloat" />
   <div id="riafirms" class="inputText" style="width:435px;position:relative">
   <table style='width: 100%'>
   <c:if test="${not empty manageFirmRepForm.riafirms}">
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
								<%-- </c:if>--%>
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
    <div class="nextButton">
 <c:if test="${(manageFirmRepForm.updateAllowed) || (userProfile.role.roleType.userRoleCode eq 'RUM')}"> 
     <div class="formButton"> 
        <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Save"
	        onclick="populateFirms();populateRiaFirms();return doPermissionCheckAndSave(document.manageFirmRepForm, 'save', this);">
      </div> 
  </c:if> 
     <c:if test="${manageFirmRepForm.mimicAllowed}">
	   	<div class="formButton"> 
        <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="mimic" value="Advisor View"
	        onclick="return doMimic(this)">
        </div> 
	  </c:if>
	  	    <!--for Passcode exemption -->
	   <c:if test="${manageFirmRepForm.passcodeExemptionAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="pcdExc" value="Passcode Exemption"
	        onclick="return doPasscodeExemption(this)">
        </div> 
	  </c:if>	
	  
	  <!--for Passcode -->
	  <c:if test="${manageFirmRepForm.passcodeViewAllowed}">  
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="resetPcd" value="Passcode View"
	        onclick="return doPasscodeView(this)">
        </div> 
	  </c:if>
     <c:if test="${manageFirmRepForm.resetPasswordAllowed}">
	   	<div class="formButton"> 
        <input type="button" class="blue-btn-medium next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn-medium next'"
	        name="resetPwd" value="Reset Password"
	        onclick="return doResetPassword(this)">
        </div> 
	  </c:if>
	  		  
	  <c:if test="${manageFirmRepForm.deleteAllowed}">
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
	             onclick="return doCancelBtn(document.manageFirmRepForm, this)"> 
	    </div>
    </div>
 </div>
</bd:form>

<layout:pageFooter/>

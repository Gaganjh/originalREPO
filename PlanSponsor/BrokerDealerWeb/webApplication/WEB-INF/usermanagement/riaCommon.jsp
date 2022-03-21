<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<content:contentBean contentId="<%=BDContentConstants.INVALID_BD_FIRM_NAME%>" 
     type="<%=BDContentConstants.TYPE_MESSAGE%>" id="invalidFirm" />

<content:contentBean contentId="<%=BDContentConstants.DELETE_RIA_WARNING%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"
  id="deleteRiaWarning" />
 
 
<content:contentBean contentId="<%=BDContentConstants.NO_PERMISSION_CHECK_WARNING%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"
  id="permissionCheckWarning" />
  
<content:contentBean contentId="<%=BDContentConstants.RIA_EMAIL_EXISTS%>" 
   type="<%=BDContentConstants.TYPE_MESSAGE%>" id="emailWarning" />


<script type="text/javascript">
<!--
	var firms = [];
	var activeFirmIds = [];
	var firmError = '<content:getAttribute beanName="invalidFirm" attribute="text" filter="true"/>';  
	
	var deleteRiaWarning = '<content:getAttribute beanName="deleteRiaWarning" attribute="text" filter="true"/>';
	var permissionCheckWarning = '<content:getAttribute beanName="permissionCheckWarning" attribute="text" filter="true"/>';
	
	var emailWarning = '<content:getAttribute beanName="emailWarning" attribute="text" filter="true"/>';
	
	$(document).ready(function(){
		
		var initialEmail = document.forms['manageRiaForm'].elements['emailAddress'].value;
		
		$('#saveMyProfileBtnForPost').click(function(){
			populateFirms();
			checkEmailForRegisteredUser(this);
		});
		
		$('#saveMyProfileBtnForPre').click(function(){
			populateFirms();
			checkEmailForNotRegisteredUser(this);
		});
		
		function checkEmailForRegisteredUser(btn){
			var changedEmail = document.forms['manageRiaForm'].elements['emailAddress'].value;
			if(initialEmail.toLowerCase() == changedEmail.toLowerCase()){
				doPermissionCheckAndSave(document.forms['manageRiaForm'], 'save', btn);
			}else{
				doMailCheckAndSave(document.forms['manageRiaForm'], 'save', btn);
			}
		}
		
		function checkEmailForNotRegisteredUser(btn){
			var changedEmail = document.forms['manageRiaForm'].elements['emailAddress'].value;
			if(initialEmail.toLowerCase() == changedEmail.toLowerCase()){
				doProtectedSubmitBtn(document.forms['manageRiaForm'], 'resendActivation', btn);
			}else{
				doMailCheckSaveAndResend(document.forms['manageRiaForm'], 'resendActivation', btn);
			}
		}
		
	});

	//Common Function for AJAX Calls
	function ajax_getJSON(actionPath, requstParameters, callbackMethod, btn) {
			$.get(actionPath, requstParameters, function(data) {
				// Call back method
					var parsedData = $.parseJSON(data);
					if (parsedData.sessionExpired != undefined) {
						// session expired.... redirecting to login page
					top.location.reload(true);
				} else {
					callbackMethod(parsedData, btn);
				}
			}, "text");
	}
	 //Callback method for Request status
	function save_emailCheck_callbackMethod(parsedData, btn){
		if(parsedData.Status == "exists"){
				alert(emailWarning);
		}
		doPermissionCheckAndSave(document.forms['manageRiaForm'], 'save', btn);
	}

    function doMailCheckAndSave(form, action, btn){
    	var jsonObjparam="";
    	var emailId = document.forms['manageRiaForm'].elements['emailAddress'].value;
        jsonObjparam=$.trim(emailId);
        ajax_getJSON("/do/manage/ria?action=checkDuplicateEmail",
        	{jsonObj:jsonObjparam
        	}, save_emailCheck_callbackMethod, btn);
    }
    
    function saveResend_emailCheck_callbackMethod(parsedData, btn){
    	if(parsedData.Status == "exists"){
    			alert(emailWarning);
    	}
    	doProtectedSubmitBtn(document.manageRiaForm, 'resendActivation', btn);
    }
    
    function doMailCheckSaveAndResend(form, action, btn){
    	var jsonObjparam="";
    	var emailId = document.forms['manageRiaForm'].elements['emailAddress'].value;
        jsonObjparam=$.trim(emailId);
        ajax_getJSON("/do/manage/ria?action=checkDuplicateEmail",
        	{jsonObj:jsonObjparam
        	}, saveResend_emailCheck_callbackMethod, btn);
    }
    
	function doPermissionCheckAndSave(form, action, btn) {
		if (firms.length == 0) {
			return doProtectedSubmitBtn(form, action, btn);
		} else {
			var permissionString = form.firmPermissionsListStr.value;
			 if (permissionString == null || permissionString == '') {
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
		if (confirm(deleteRiaWarning)) {
			return doProtectedSubmitBtn(document.manageRiaForm, 'delete', btn);
		} else {
			return false;
		}
	}

	function doResetPassword(btn) {
		return doSubmitCheckChange(document.manageRiaForm, 'resetPassword', btn);
	}
	
	function doPasscodeExemption(btn) {
		  return doProtectedSubmitBtn(document.manageRiaForm, 'exemptPasscode', btn); 
	}

	function doPasscodeView(btn) {
		  return doSubmitCheckChange(document.manageRiaForm, 'passcodeView', btn); 
	}
	

	function doMimic(btn) {
		return doSubmitCheckChange(document.manageRiaForm, 'mimic', btn);
	}

	function updateFirmPermission(obj, firmId) {
		var updatedList = [];
		for (i = 0; i < firms.length; i++) {
			if (firms[i].id == firmId) {
				changedfirm = new bdFirm(firms[i].id, firms[i].name,
						obj.checked);
				updatedList.push(changedfirm);
			} else {
				updatedList.push(firms[i]);
			}
		}
		firms = updatedList;
		document.manageRiaForm.changed.value = true;
		changed = true;
	}

	function addFirm(frm) {
		var lastSelectedRiaFirmName = document
				.getElementById("lastSelectedRiaFirmName").value;
		if (lastSelectedRiaFirmName != "" && frm.selectedFirmId.value != "") { // user has selected a firm from drop-down
			if (lastSelectedRiaFirmName == frm.selectedFirmName.value) { //After selecting no changes were made
				addNewFirm(frm);
			} else { //Firm name is modified
				verifyFirmName(frm.selectedFirmName.value, frm, firmError);
			}
		} else { //User has not selected a firm. Might have copied the entire firm name. So we send another AJAX
			//request to validate the firm name.
			verifyFirmName(frm.selectedFirmName.value, frm, firmError);
		}
	}

	function addNewFirm(frm) {
		addFirmToList(new bdFirm(frm.selectedFirmId.value,
				frm.selectedFirmName.value, true));
		frm.selectedFirmId.value = "";
		frm.selectedFirmName.value = "";
		frm.lastSelectedRiaFirmName.value = "";
		refreshFirms();
		frm.changed.value = true;
		changed = true;
	}

	function bdFirm(id, name, permission) {
		this.id = id;
		this.name = name;
		this.permission = permission;
	}

	function addFirmToList(firm) {
		for (i = 0; i < firms.length; i++) {
			if (firms[i].id == firm.id) {
				alert("The firm has already been added.");
				return;
			}
		}
		firms.push(firm);
	}

	function addActiveFirmIdsToList(firm) {
		for (i = 0; i < activeFirmIds.length; i++) {
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
		for (i = 0; i < firms.length; i++) {
			if (firms[i].id != firmId) {
				newList.push(firms[i]);
			}
		}
		firms = newList;
		refreshFirms();
		document.manageRiaForm.changed.value = true;
		changed = true;
	}

	function refreshFirms() {
		elem = document.getElementById("firms");
		var buf = [];
		buf.push("<table style='width:100%'>");
		if (firms.length) {
			buf
					.push("<tr><th></th><th style='width:100%;font-size:12px' valign='middle'><label> Firm</label> </th>");
			buf
					.push("<th style='width:20%;font-size:12px'  align='center'> <label>View RIA Statements</label></th> <th style='width:25%'> </th> </tr>");
		}

		for (i = 0; i < firms.length; i++) {

			buf.push("<tr><td style='font-weight:normal;font-size:12px'>"
					+ (i + 1) + ". "
					+ " </td><td style='font-weight:normal;font-size:12px'>");
			buf.push(firms[i].id + " - " + firms[i].name);
			buf.push("</td>");
			if (activeFirmIds.indexOf(firms[i].id) == -1) {
				if (firms[i].permission == true) {
					buf
							.push("</td><td align='center'><input type='checkbox' checked='checked' style='margin-center:80px;' onclick='updateFirmPermission(this, "
									+ firms[i].id + " )'/> </td>");
				} else {
					buf
							.push("</td><td align='center'><input type='checkbox' style='margin-center:80px;' onclick='updateFirmPermission(this, "
									+ firms[i].id + " )'/> </td>");
				}
			} else {
				if (firms[i].permission == true) {
					buf
							.push("</td><td align='center'><input type='checkbox' checked='checked' style='margin-center:80px;' disabled /> </td>");
				} else {
					buf
							.push("</td><td align='center'><input type='checkbox' style='margin-center:80px;' disabled /> </td>");
				}
			}
			buf
					.push("<td><img src='/assets/unmanaged/images/buttons/remove_firm.gif' alt='Remove Firm Image' width='87' height='19' onclick='removeFirm(\"");
			buf.push(firms[i].id);
			buf.push("\")'/></td></tr>");

		}
		buf.push("<br>");
		buf.pop();
		elem.innerHTML = buf.join("");
		buf.push("</table>");

		/* if(firms.length >= 5){
			document.getElementById("selectedFirmName").disabled = true;
		}else{
			document.getElementById("selectedFirmName").disabled = false;
		} */
	}
	
	function doProtectedSubmitWithFirms(frm, action, btn) {
    	frm.firmListStr.value = getFirmListAsString();
		doProtectedSubmitBtn(frm, action, btn);
		return false;
    }

	function getFirmListAsString() {
		var buf = "";
		for (i = 0; i < firms.length; i++) {
			buf += firms[i].id;
			buf += ",";
		}
		return buf;
	}

	function getFirmPermissionsString() {
		var buf = "";
		for (i = 0; i < firms.length; i++) {
			if (firms[i].permission == true) {
				buf += firms[i].id;
				buf += ",";
			}
		}
		return buf;
	}

	function populateFirms() {
		document.manageRiaForm.firmListStr.value = getFirmListAsString();
		document.manageRiaForm.firmPermissionsListStr.value = getFirmPermissionsString();
	}
//-->
</script>
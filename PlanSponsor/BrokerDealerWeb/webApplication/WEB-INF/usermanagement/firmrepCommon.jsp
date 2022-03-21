<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<content:contentBean contentId="<%=BDContentConstants.INVALID_BD_FIRM_NAME%>" 
     type="<%=BDContentConstants.TYPE_MESSAGE%>" id="invalidFirm" />

<content:contentBean contentId="<%=BDContentConstants.DELETE_FIRMREP_WARNING%>" 
  type="<%=BDContentConstants.TYPE_MESSAGE%>"
  id="deleteWarning" />


<script type="text/javascript">
<!--
var firms = [];
var firmError = '<content:getAttribute beanName="invalidFirm" attribute="text" filter="true"/>';  

var deleteWarning = '<content:getAttribute beanName="deleteWarning" attribute="text" filter="true"/>';

function doDelete(btn) {
	if (confirm(deleteWarning)) {  
	  return doProtectedSubmitBtn(document.manageFirmRepForm, 'delete', btn);
	} else {
	  return false;
	}
}

function doResetPassword(btn) {
	  return doSubmitCheckChange(document.manageFirmRepForm, 'resetPassword', btn);
}

function doPasscodeExemption(btn) {
	  return doProtectedSubmitBtn(document.manageFirmRepForm, 'exemptPasscode', btn); 
}

function doPasscodeView(btn) {
	  return doSubmitCheckChange(document.manageFirmRepForm, 'passcodeView', btn); 
}


function doMimic(btn) {
	  return doSubmitCheckChange(document.manageFirmRepForm, 'mimic', btn);
}

function addFirm(frm) {
	  var lastSelectedFirmName = document.getElementById("lastSelectedFirmName").value;
	  if(lastSelectedFirmName != "" && frm.selectedFirmId.value != "") { // user has selected a firm from drop-down
	  	if(lastSelectedFirmName == frm.selectedFirmName.value) { //After selecting no changes were made
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
  addFirmToList(new bdFirm(frm.selectedFirmId.value, frm.selectedFirmName.value));
  frm.selectedFirmId.value ="";
  frm.selectedFirmName.value ="";
  frm.lastSelectedFirmName.value ="";
  refreshFirms();
  frm.changed.value=true;	
}


function bdFirm(id, name) {
    this.id = id;
    this.name = name;
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
    document.manageFirmRepForm.changed.value=true;
 }

function refreshFirms() {
    elem = document.getElementById("firms");
    var buf=[];
    for (i=0; i < firms.length; i++) {
		buf.push("<div style='width:335px;float:left'><div align='left'>" + (i+1) + ". ");
        buf.push(firms[i].name + "</div></div>");
        buf.push("<div><img src='/assets/unmanaged/images/buttons/remove_firm.gif' alt='Remove Firm Image' width='87' height='19' onclick='removeFirm(\"");
        buf.push(firms[i].id);
        buf.push("\")'/></div>");
        buf.push("<br/>");
    }
    buf.push("<br>");
    buf.pop();
    elem.innerHTML = buf.join("");
}

function getFirmListAsString() {
    var buf = "";
    for (i=0; i < firms.length; i++) {
        buf += firms[i].id;
        buf +=",";
    }
    return buf;
}

function populateFirms() {
	document.manageFirmRepForm.firmListStr.value = getFirmListAsString();
}    
//-->
</script>

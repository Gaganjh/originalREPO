<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SECTION1%>" 
     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="section1" />

<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SECTION2%>" 
     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="section2" />
     
<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SECTION3%>" 
     type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="section3" />

<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_ACCESS_CODE_HELP%>" 
     type="<%=BDContentConstants.TYPE_MESSAGE%>" id="accessCodeHelp" />

<content:contentBean contentId="<%=BDContentConstants.INVALID_BD_FIRM_NAME%>" 
     type="<%=BDContentConstants.TYPE_MESSAGE%>" id="invalidFirm" />

<c:set var="firms" value="${createFirmRepForm.firms}"/>

<script type="text/javascript">

var firms = [];
var firmError = '<content:getAttribute beanName="invalidFirm" attribute="text" filter="true"/>';
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
}

function bdFirm(id, name) {
    this.id = id;
    this.name = name;
}

function addFirmToList(firm) {
    for (i=0; i < firms.length; i++) {
        if (firms[i].id == firm.id) {
        	alert("The firm has already been added.");
        	document.getElementById('selectedFirmName').focus();
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

function doProtectedSubmitWithFirms(frm, action, btn) {
	frm.firmListStr.value = getFirmListAsString();
	doProtectedSubmitBtn(frm, action, btn);
	return false;
}    

function doOnload() {
    var lastName=document.forms['createFirmRepForm'].lastName;
    if (lastName) {
        try {
        	lastName.focus();
        } catch (e) {
        }
    }    
}
  		
$(document).ready(function() {
	//
	// Redefine the function from bdutils.js to parent submit upon penetration violation error in firm search.
	// This way firm search penetration violaiton error is shown immediately by the parent.
	//
	callback_verifyFirmName = ({
		success : (
		  function(o) {
				try {
					values = YAHOO.lang.JSON.parse(o.responseText).ResultSet.Result;
				}
				catch (e) {
					return;
				}
	
				if (values != null) {
					$(".message_error").empty();
					//
					// FirmId and FirmName when set to -1 indicate penetration violation error,
					// as set in Json server handler FirmSearchAction.
					//
					if (typeof values[0] != 'undefined' && typeof values[0].firmId != 'undefined' && typeof values[0].firmName != 'undefined' &&
						values[0].firmId == '-1' && values[0].firmName == '-1') {
						return doProtectedSubmitWithFirms(document.createFirmRepForm,'continue', this);
					}
				}
				if (values.length == 0) {
					alert(errMsg);
				}
				else {
					frm.selectedFirmId.value = values[0].firmId;
					frm.selectedFirmName.value = values[0].firmName;
					frm.lastSelectedFirmName.value = values[0].firmName;
					addNewFirm(frm);
				}
		  })
	})
});
</script>

<utils:cancelProtection name="createFirmRepForm" changed="${createFirmRepForm.changed}"
     exclusion="['action']"/>

<div id="contentFull">
<bd:form id="createFirmRepForm" action="/do/createFirmRep/create" modelAttribute="createFirmRepForm" name="createFirmRepForm">

   <input type="hidden" name="action">
<input type="hidden" name="changed"/>
<input type="hidden" name="firmListStr" id="firmListStr"/>
	<layout:pageHeader nameStyle="h1"/>
	<c:if test="${createFirmRepForm.completed}">
	<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SUCCESS%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="successMessage" />
	<div class="message message_info">
	  <dl>
	    <dt>Information Message</dt>
	    <dd>1&nbsp;&nbsp;
	     <content:getAttribute id='successMessage' attribute='text'>
	       <content:param>${createFirmRepForm.firstName} </content:param>
	       <content:param>${createFirmRepForm.lastName}</content:param>
	     </content:getAttribute>
	     </dd>
	    </dl>
	</div>
	</c:if>
	
	<report:formatMessages scope="session"/>
	
   <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="section1" attribute="text"/></div>
     <div class="GrayLT Right">* = Required Field</div>
   </div>

   <div class="label">* Last Name:</div>
   <div class="inputText">
    <label>
<form:input path="lastName" maxlength="30" size="30" cssClass="input"/>
   	</label>
   </div>

   <div class="label">* First Name:</div>
   <div class="inputText">
    <label>
<form:input path="firstName" maxlength="30" size="30" cssClass="input"/>
   	</label>
   </div>
   
   <div class="label">* Email:</div>
   <div class="inputText">
    <label>
<form:input path="emailAddress" maxlength="70" size="50" cssClass="input"/>
   	</label>
   </div>
   <div class="label">* Telephone #:</div>
   <div class="inputText">
        <userprofile:phoneNumInput/>
   </div>

   <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="section2" attribute="text"/></div>
   </div>

   <div class="label">* Firm Name:</div>
   <div style="margin-top:0px;float:left;margin-right:15px;margin-left:9px">
	   <utils:firmSearch firmName="selectedFirmName" firmId="selectedFirmId"/>
   </div>
   <div style="margin-top:15px">
	   <img src="/assets/unmanaged/images/buttons/add_firm.gif" alt="Add Firm Image" width="87" height="19" onclick="addFirm(document.createFirmRepForm)" />
   </div>
   <br class="clearFloat" />
   <div id="firms" class="inputText" style="width:435px">
    <label>
        <c:forEach var="firm" items="${firms}">
            <img src='/assets/unmanaged/images/buttons/remove_firm.gif' alt='Remove Firm Image' width='87' height='19' onclick='removeFirm("${firm.id}")'/>&nbsp; ${firm.firmName}<br>
			<script type="text/javascript">
			<!--
					addFirmToList(new bdFirm('${firm.id}', '${firm.firmName}'));
			//-->
			</script>
		</c:forEach>
	</label>
    </div>
	<script type="text/javascript">
		<!--
			refreshFirms();
		//-->
	</script>    
   <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="section3" attribute="text"/></div>
   </div>
   <div class="label">* Access Code:</div>
   <div class="inputText">
    <label>
<form:input path="passCode" maxlength="25" cssClass="input"/>
   	</label>
	<br />
	<content:getAttribute beanName="accessCodeHelp" attribute="text"/>
   </div>
   
   <br>
  <div id="content">
      <c:if test="${not createFirmRepForm.completed}">
    	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="create" value="Create User"
	        onclick="return doProtectedSubmitWithFirms(document.createFirmRepForm, 'continue', this)">
        </div> 
      </c:if>
      <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Cancel"
             onclick="return doCancelBtn(document.createFirmRepForm, this)"> 
    </div>
   </div>
</bd:form>
</div>

<layout:pageFooter/>

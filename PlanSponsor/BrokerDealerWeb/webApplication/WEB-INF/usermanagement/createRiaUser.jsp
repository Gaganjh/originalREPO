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
     
<content:contentBean contentId="<%=BDContentConstants.RIA_EMAIL_EXISTS%>" 
     type="<%=BDContentConstants.TYPE_MESSAGE%>" id="emailWarning" />

<c:set var="firms" value="${createRiaUserForm.firms}"/>

<script type="text/javascript">
<!--
    var firms = [];
    var firmError = '<content:getAttribute beanName="invalidFirm" attribute="text" filter="true"/>'; 
    var emailWarning = '<content:getAttribute beanName="emailWarning" attribute="text" filter="true"/>';
        
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
	  addFirmToList(new bdFirm(frm.selectedFirmId.value, frm.selectedFirmName.value));
	  frm.selectedFirmId.value ="";
	  frm.selectedFirmName.value ="";
	  frm.lastSelectedRiaFirmName.value ="";
	  refreshFirms();
	  frm.changed.value=true;
	  changed=true;
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
        document.createRiaUserForm.changed.value=true;
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
            buf.push("</td><td align='center'><input type='checkbox' checked='checked' style='margin-center:80px;' disabled/> </td>");
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
    
    function doProtectedSubmitWithFirms(frm, action, btn) {
    	frm.firmListStr.value = getFirmListAsString();
		doProtectedSubmitBtn(frm, action, btn);
		return false;
    }    

    function doOnload() {
        var firstName=document.forms['createRiaUserForm'].firstName;
        if (firstName) {
            try {
            	firstName.focus();
            } catch (e) {
            }
        }    
    }
    
    //Common Function for AJAX Calls
    function ajax_getJSON(actionPath, requstParameters, callbackMethod) {
    		$.get(actionPath, requstParameters, function(data) {
    			// Call back method
    				var parsedData = $.parseJSON(data);
    				if (parsedData.sessionExpired != undefined) {
    					// session expired.... redirecting to login page
    				top.location.reload(true);
    			} else {
    				callbackMethod(parsedData);
    			}
    		}, "text");
    }
    
    //Callback method for Request status
    function emailCheck_callbackMethod(parsedData){
    	if(parsedData.Status == "exists"){
  			alert(emailWarning);
  		}
    	document.forms['createRiaUserForm'].firmListStr.value = getFirmListAsString();
    	document.forms['createRiaUserForm'].action.value="continue";
    	document.forms['createRiaUserForm'].submit();
    }
    
    //Check the email is exists or not if exists display warning messgae with OK button
    $(document).ready(function(){
	    $("#createUser").click(function(){
	    	var jsonObjparam="";
	    	var emailId = document.forms['createRiaUserForm'].elements['emailAddress'].value;
	        jsonObjparam=$.trim(emailId);
	        ajax_getJSON("/do/createRiaUser/create?action=checkDuplicateEmail",
	        	{jsonObj:jsonObjparam
	        	}, emailCheck_callbackMethod);
	    });
	    
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
    						return doProtectedSubmitWithFirms(document.createRiaUserForm,'continue', this);
    					}
    				}
    				if (values.length == 0) {
    					alert(errMsg);
    				}
    				else {
    					frm.selectedFirmId.value = values[0].firmId;
    					frm.selectedFirmName.value = values[0].firmName;
    					frm.lastSelectedRiaFirmName.value = values[0].firmName;
    					addNewFirm(frm);
    				}
    		  })
    	})
    }); 
    

  //-->
</script>

<utils:cancelProtection name="createRiaUserForm" changed="${createRiaUserForm.changed}"
     exclusion="['action']"/>

<div id="contentFull">
<bd:form id="createRiaUserForm" action="/do/createRiaUser/create" modelAttribute="createRiaUserForm" name="createRiaUserForm">

   <input type="hidden" name="action">
<input type="hidden" name="changed"/>
<input type="hidden" name="noRolePartyId"/>
<input type="hidden" name="firmListStr" id="firmListStr"/>
   <c:if test="${createRiaUserForm.noRolePartyId ne 0 }">
<input type="hidden" name="firstName" id="firstName"/>
<input type="hidden" name="lastName" id="lastName"/>
   <c:if test="${not empty createRiaUserForm.emailAddress}">
<input type="hidden" name="emailAddress" id="emailAddress"/>
   </c:if>
   </c:if>
	<layout:pageHeader nameStyle="h1"/>
	<c:if test="${createRiaUserForm.completed}">
	<content:contentBean contentId="<%=BDContentConstants.CREATE_FIRMREP_SUCCESS%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="successMessage" />
	<div class="message message_info">
	  <dl>
	    <dt>Information Message</dt>
	    <dd>1&nbsp;&nbsp;
	     <content:getAttribute id='successMessage' attribute='text'>
	       <content:param>${createRiaUserForm.firstName} </content:param>
	       <content:param>${createRiaUserForm.lastName}</content:param>
	     </content:getAttribute>
	     </dd>
	    </dl>
	</div>
	</c:if>
	
	 <report:formatMessages scope="session"/> 
	
   <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></div>
     <div class="GrayLT Right">* = Required Field</div>
   </div>

   <div class="label"><b>* First Name:</b></div>
   <div class="inputText">
    <label>
    <c:choose>
		 
		<c:when test="${(not empty createRiaUserForm.firstName) and (createRiaUserForm.noRolePartyId ne 0 )}">
<form:input path="firstName" disabled="true" maxlength="30" size="30" cssClass="input"/>
		</c:when>
		<c:otherwise>
<form:input path="firstName" maxlength="30" size="30" cssClass="input"/>
		
		</c:otherwise>
		</c:choose>
   	</label>
   </div>
   
   <div class="label"><b>* Last Name:</b></div>
   <div class="inputText">
    <label>
    	<c:choose>
		 
		<c:when test="${(not empty createRiaUserForm.lastName) and (createRiaUserForm.noRolePartyId ne 0 )}">
<form:input path="lastName" disabled="true" maxlength="30" size="30" cssClass="input"/>
		</c:when>
		<c:otherwise>
<form:input path="lastName" maxlength="30" size="30" cssClass="input"/>
		
		</c:otherwise>
		</c:choose>
   	</label>
   </div>
   
   <div class="label"><b>* Email:</b></div>
   <div class="inputText">
    <label>
    	<c:choose>
		 
		<c:when test="${(not empty createRiaUserForm.emailAddress) and (createRiaUserForm.noRolePartyId ne 0 )}">
		
<form:input path="emailAddress" disabled="true" maxlength="70" size="30" cssClass="input"/>
		    
		</c:when>
		
		<c:otherwise>
<form:input path="emailAddress" maxlength="70" size="30" cssClass="input"/>
		
		</c:otherwise>
		</c:choose>
   	</label>
   </div>
   <div class="label"><b>* Telephone #:</b></div>
   <div class="inputText">
        <userprofile:phoneNumInput/>
   </div>

   <div class="BottomBorder">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></div>
   </div>

   <div class="label"><b>* RIA Firm ID/Name:</b></div>
   <div style="margin-top:0px;float:left;margin-right:15px;margin-left:9px">
	   <utils:riaFirmSearch firmName="selectedFirmName" firmId="selectedFirmId"/>
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
   <div class="BottomBorder" style="display: none;">
	 <div class="SubTitle Gold Left"><content:getAttribute beanName="section3" attribute="text"/></div>
   </div>
   <div class="label" style="display: none;">* Access Code:</div>
   <div class="inputText" style="display: none;">
    <label>
<form:input path="passCode" maxlength="25" cssClass="input"/>
   	</label>
   </div>
   
   <br>
  <div id="content">
      <c:if test="${not createRiaUserForm.completed}">
    	<div class="formButton"> 
       <input type="button" class="blue-btn next" id="createUser"
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="create" value="Create User">
        </div> 
      </c:if>
      <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Cancel"
             onclick="return doCancelBtn(document.createRiaUserForm, this)"> 
    </div>
   </div>
</bd:form>
</div>

<layout:pageFooter/>

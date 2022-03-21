<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.ContentRuleDisplayBean" %>
<%@ page import="com.manulife.pension.bd.web.content.ContentRuleMaintenanceForm" %>

<% 
ContentRuleMaintenanceForm	crm = (ContentRuleMaintenanceForm)session.getAttribute("contentRuleMaintenanceForm");

ContentRuleDisplayBean crb = crm.getContentRule();
pageContext.setAttribute("contentRule",crb,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=BDContentConstants.ATLEAST_ONE_FIRM_ERROR_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="firmErrorMsg" />
<content:contentBean contentId="<%=BDContentConstants.INVALID_FIRM_ERROR_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="invalidFirmErrorMsg" />

<style type="text/css">
.btn-hover {
  color:#CCCC99!important;
}

.formButton input {
    float:right;
    margin-left:20px;
}
	
.formButton  .blue-btn-medium {
      border:none;
      width:140px;
      height:30px;
      color:white;
      background:url('/assets/unmanaged/images/buttons/enabled_blue_button_140px.gif');
	  cursor : pointer;
}
.formButton  .grey-btn-medium {
      border:none;
      width:140px;
      height:30px;
      color:white;
      background:url('/assets/unmanaged/images/buttons/enabled_grey_button_140px.gif');
	  cursor : pointer;
}
</style>

<script type="text/javascript">
    var firms = [];
    var orginalFirmsList = "";
    
	var invalidFirmError = '<content:getAttribute attribute="text" beanName="invalidFirmErrorMsg"/>';
	function addFirm() {
	  if(validateRules()) {
		  var frm = document.forms["contentRuleMaintenanceForm"];
		  var lastSelectedFirmName = document.getElementById("lastSelectedFirmName").value;
		  if(lastSelectedFirmName != "" && frm.selectedFirmId.value != "") { // user has selected a firm from drop-down
		  	if(lastSelectedFirmName == frm.selectedFirmName.value) { //After selecting no changes were made
		 		addNewFirm(frm);
		    }
		    else { //Firm name is modified
		    	verifyFirmName(frm.selectedFirmName.value, frm, invalidFirmError);
		    }
		  }
		  else { //User has not selected a firm. Might have copied the entire firm name. So we send another AJAX
		  //request to validate the firm name.
			  verifyFirmName(frm.selectedFirmName.value, frm, invalidFirmError);
		  }
	  }
    }

    function addNewFirm(frm) {
    	addFirmToList(new bdFirm(frm.selectedFirmId.value, frm.selectedFirmName.value));
		frm.selectedFirmId.value ="";
		frm.selectedFirmName.value ="";
		frm.lastSelectedFirmName.value ="";
	    refreshFirms();
	    disableRules();
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
    
    function removeFirm(firmId) {
        var newList = [];
        for (i=0; i < firms.length; i++) {
            if (firms[i].id != firmId) {
                newList.push(firms[i]);
            }
        }    
        firms = newList;
        refreshFirms(); 
		enableRules();
     }

	function refreshFirms() {
		hideMessages();
        elem = document.getElementById("firms");
        var buf=[];
        for (i=0; i < firms.length; i++) {
			buf.push("<div style='width:360px;float:left'><div align='left'>" + (i+1) + ". ");
            buf.push(firms[i].name + "</div></div>");
            buf.push("<div style='float:left'><label><img src='/assets/unmanaged/images/buttons/remove_firm.gif' alt='Remove Firm Image' width='87' height='19'  onclick='removeFirm(\"");
            buf.push(firms[i].id);
            buf.push("\")'/></label></div>");
            buf.push("<br/>");
            buf.push("<br/>");
        }
		buf.push("<br/>");
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
    
    function doSubmit(frm, action, btn) {
    	frm.firmListStr.value = getFirmListAsString();
		if((document.getElementById("ex").checked || document.getElementById("in").checked) && frm.firmListStr.value == "") {
			document.getElementById("firmErr").style.display="block";
		}
		else {
			frm.action.value = action;
			
			if(document.getElementById("ex").checked) {
				document.getElementById("rule").value = "Exclude";
			}
			else if(document.getElementById("in").checked){
				document.getElementById("rule").value = "Include";
			}
			doProtectedSubmitBtn(frm, action, btn);
		}
    } 
	
	function disableRules() {
		if (firms.length == 1) {
			document.getElementById("ex").disabled = true;
			document.getElementById("in").disabled = true;
		}
	}

	function enableRules() {
		if (firms.length == 0) {
			document.getElementById("ex").disabled = false;
			document.getElementById("in").disabled = false;
			document.getElementById("ex").checked = false;
			document.getElementById("in").checked = false;
			document.getElementById("rule").value = "-"
			document.getElementById("firmsTitle").innerHTML = "Firms";
		}
	}

	function changeTitle() {
		if(document.getElementById("ex").checked) {
			document.getElementById("firmsTitle").innerHTML = "Excluded Firms";
		}
		else {
			document.getElementById("firmsTitle").innerHTML = "Included Firms";
		}
	}

	function validateRules() {
		var returnVal = false;
		if(!document.getElementById("ex").checked && !document.getElementById("in").checked) {
			alert("Please select a rule.");
			returnVal = false;
		}
		else {
			returnVal = true;
		}
		return returnVal;
	}

	function hideMessages() {
		document.getElementById("firmErr").style.display="none";
	}

	function getOriginalFirmList() {
		orginalFirmsList = getFirmListAsString(); 
	}
	
	function doCancel(frm, btn) {		
		var isChanged = checkChange(frm);
		if (isChanged &&
			!confirm("Do you want to abandon the changes?")) {
				return false;
		}
		doProtectedSubmitBtn(frm, 'cancel', btn);
	}

	function checkChange(frm) {
		var firmList = getFirmListAsString();
		var rule = "";
		var originalRule = "${contentRuleMaintenanceForm.contentRule.ruleType}";

		if (originalRule == "-") {
			originalRule = "";
		}
		if (document.getElementById("ex").checked) {
			rule = "Exclude";
		} else if (document.getElementById("in").checked) {
			rule = "Include"
		}
		if (originalRule != rule || orginalFirmsList != firmList) {
			return true;
		} else {
			return false;
		}		
	}	
</script>

<div class="message message_error" id="firmErr" style="display:none">
  <div class="message_close">x</div>
  <dl>
    <dt>Error Message</dt>
    <dd>1&nbsp;&nbsp; 
       <content:getAttribute attribute="text" beanName="firmErrorMsg"/>
     </dd>
    </dl>
</div>

<div id="content">
	<bd:form action="/do/firmRestrictions/maintenance" modelAttribute="contentRuleMaintenanceForm" name="contentRuleMaintenanceForm">
		<%--	CSRF token inserted      --%>
	
<input type="hidden" name="action"/>
<input type="hidden" name="firmListStr"/>
<input type="hidden" name="contentId" value="${contentRule.contentId}"/>
<form:hidden path="task"/>
<input type="hidden" name="ruleType" id="rule"/>
	<h1>Firm Content Rule Maintenance </h1>
	
	<DIV id="errordivcs"><content:errors scope="session"/></DIV><br>
	
	<div class="label">Content Name: </div>  
	<div class="inputText">
<label><strong>${contentRule.name}</strong></label>
	</div>
	<div class="label">Key: </div>
	<div class="inputText">
<label><strong> ${contentRule.contentId} </strong></label>
	</div> 
	<br /> 
	<span class="RadioLabel">Content Rule: </span>
<c:if test="${not empty contentRule.brokerDealerFirms}">
			<span class="RadioValues">
				<label> 
<form:radiobutton disabled="true" onclick="changeTitle()" path="contentRule.ruleType" id="ex" value="Exclude" />
					Exclude
				</label>
				<label>
<form:radiobutton disabled="true" onclick="changeTitle()" path="contentRule.ruleType" id="in" value="Include" />
					Include
				</label>
			<br />
			</span>
</c:if>
<c:if test="${empty contentRule.brokerDealerFirms}">
			<span class="RadioValues">
				<label>
<form:radiobutton onclick="changeTitle()" path="ruleType" id="ex" value="Exclude" />
					Exclude
				</label>
				<label>
<form:radiobutton onclick="changeTitle()" path="ruleType" id="in" value="Include" />
					Include
				</label>
				<br />
			</span>
</c:if>
			<br />
			<br />
	    <div class="BottomBorder">
		<div class="SubTitle Gold Left">BD Firm Search:</div>
	  </div>

	<div class="label" style="width:200px;text-align:left;margin-top:0px;"><utils:firmSearch firmName="selectedFirmName" firmId="selectedFirmId"/> </div>
	<div class="inputText" style="width:210px;">
		<label>
		<img src="/assets/unmanaged/images/buttons/add_firm.gif" alt="Add Firm Image" width="87" height="19" onclick="addFirm()" />
		</label>
	</div>  
	<br/><br />
	<div class="BottomBorder">
		<c:choose>
			<c:when test="${contentRule.ruleType == 'Include' }">
				<div id="firmsTitle" class="SubTitle Gold Left">
			Included Firms
				</div>
			</c:when>
			<c:when test="${contentRule.ruleType == 'Exclude' }">
				<div id="firmsTitle" class="SubTitle Gold Left">
			Excluded Firms
				</div>
			</c:when>
			<c:otherwise>
				<div id="firmsTitle" class="SubTitle Gold Left">
			Firms
				</div>
			</c:otherwise>
		</c:choose>
	</div> 	
	<br />
	<div id="firms" style="width:500px;position:relative">
		<c:forEach var="firm" items="${contentRule.brokerDealerFirms}" varStatus="status">
			<input type="hidden" name="firmId" value="<c:out value='${firm.id}'/>"/>
			<input type="hidden" name="firmName" value="<c:out value='${firm.firmName}'/>"/>
			<script type="text/javascript">
				addFirmToList(new bdFirm(${firm.id}, '${firm.firmName}'));
			</script>
			<div style="width:360px;float:left">
				<div align="left"><c:out value="${status.count}"/>. <c:out value="${firm.firmName}"/></div>
			</div>
			<div style="float:left">
				<label>
					<img src="/assets/unmanaged/images/buttons/remove_firm.gif" alt="Remove Firm Image" width="87" height="19" onclick="removeFirm(<c:out value='${firm.id}'/>)">
				</label>
			</div>
			<br />
			<br />
		</c:forEach>
		<script type="text/javascript">
				getOriginalFirmList();
		</script>
		
	</div>
	<br class="clearFloat">
	<div class="formButtons" style="clear:both">
	<div class="formButton">
      <input type="button" class="grey-btn" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn'"
             name="cancel" value="Cancel"
             onclick="return doCancel(document.contentRuleMaintenanceForm, this)"> 
    </div>
	
	<div class="formButton"> 
       <input type="button" class="blue-btn" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn'"
	        name="continue" value="Save"
	        onclick="return doSubmit(document.contentRuleMaintenanceForm, 'save', this)">
    </div> 
	
	<div class="formButton"> 
       <input type="button" class="blue-btn" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn'"
	        name="continue" value="Save &amp; Exit"
	        onclick="return doSubmit(document.contentRuleMaintenanceForm, 'saveExit', this)">
    </div> 
    </div>
	</bd:form>
	<br class="clearFloat">	
	<layout:pageFooter/>
</div>

<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="mc" tagdir="/WEB-INF/tags/messagecenter"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>
<%@page import="com.manulife.pension.service.message.valueobject.MessageCenterComponent" %>
<%@page import="com.manulife.pension.ps.web.util.SessionHelper"%><style type="text/css">

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);


%>



.style1 {
	font-size: 16px;
	font-weight: bold;
}

.parameter {
	font-style: italic;
}
</style>

<script type="text/javascript">
var priorityOverideMap = new Array();

<!--

// sections list for toggle all
var sections = [];

// Cash Account Messages
var cashAccountTemplates = [];
cashAccountTemplates[0] = 'display(179_8)';
cashAccountTemplates[1] = 'display(178_8)';
cashAccountTemplates[2] = 'display(180_8)';

// the map that holds the templates id for each section id
var sectionMap = [];

  // toggle the expand/collapse of a section
	function toggleSection(frm, sectionId,indId, icon) {
		
		var ele = document.getElementById(sectionId);   
		  	if (typeof ele =="undefined") {
	    	   return;
	    }
	    if (ele.style.display == "none") {
	       ele.style.display = "block";
	   	  // document.forms[frm].elements[indId].value="Y";
	       document.getElementById(icon).src="/assets/unmanaged/images/minus_icon.gif";
	       
	    } else {
	       ele.style.display = "none";
	   	  // document.forms[frm].elements[indId].value="N";
	       document.getElementById(icon).src="/assets/unmanaged/images/plus_icon.gif";
	    }
	}
  
  function toggleAll(expand) {
     for (i = 0; i < sections.length; i++) {
        var sid = sections[i];
		var ele = document.getElementById("section" + sid);
		if (typeof ele =="undefined") {
    	   continue;
	    }
	    var indId = "expand(" + sid + ")";
	    var icon = "expandIcon" + sid;
	    if (expand) {
	       ele.style.display = "block";
	   	   //document.forms['messagePrefForm'].elements[indId].value="Y";
	       document.getElementById(icon).src="/assets/unmanaged/images/minus_icon.gif";	       
	    } else {
	       ele.style.display = "none";
	   	  // document.forms['messagePrefForm'].elements[indId].value="N";
	       document.getElementById(icon).src="/assets/unmanaged/images/plus_icon.gif";
	    }
     }
  }

var sectionsToExpand = [];
function addSectionToExpand(sid) {
   sectionsToExpand.push(sid);
}

function expandErrorSections() {
     for (i = 0; i < sectionsToExpand.length; i++) {
        var sid = sectionsToExpand[i];
		var ele = document.getElementById("section" + sid);
		if (typeof ele =="undefined") {
    	   continue;
	    }
	    var indId = "expand(" + sid + ")";
	    var icon = "expandIcon" + sid;
	       ele.style.display = "block";
	   	   document.forms['messagePrefForm'].elements[indId].value="Y";
	       document.getElementById(icon).src="/assets/unmanaged/images/minus_icon.gif";	       
     }
}

// create the section list  
function addSection(sid) {
      sections.push(sid);
}
  
// the object contains both section id and template id
function sectionTemplates(sid, templates) {
    this.sectionId = sid;
    this.templates = templates;
}

// the object contains both section id and template id
function sectionCount(sid, count) {
    this.sectionId = sid;
    this.count = count;
}


// add a template into a section in the section map
function addTemplateToSection(tid, sid) {
    for (i = 0; i < sectionMap.length; i++) {
        if (sectionMap[i].sectionId == sid) {
           sectionMap[i].templates.push(tid);
           return;
        }
    }
    sectionMap.push(new sectionTemplates(sid, [tid]));
}

// Get the template count in one section
function getTemplateCountInSection(sid) {
   for (i = 0; i < sectionMap.length; i++) {
        if (sectionMap[i].sectionId == sid) {
           return sectionMap[i].templates.length;
     }
   }  
   return 0;
}
 
// add one template count in a section
function addDisplayCount(sectionCounts, sid) {
    for (i = 0; i < sectionCounts.length; i++) {
        if (sectionCounts[i].sectionId == sid) {
           sectionCounts[i].count = sectionCounts[i].count + 1;
           return;
        }
    }
    sectionCounts.push(new sectionCount(sid, 1));
}
// get the selected template in one section
function getDisplayCount(sectionCounts, sid) {
    for (i = 0; i < sectionCounts.length; i++) {
        if (sectionCounts[i].sectionId == sid) {
		  return sectionCounts[i].count;
		}
    }
    return 0;
}

// set the dispaly option change for message template
function displayChange(field,sectionId) {

	/*
	 * display changes specific to cash account messages.
	 * i.e. Among the 3 cash account messages (178,179,180) if one message is set yes, 
	 * other messages which already set to Yes will be set to No
	 */
	//Cash Account Message validation changes - start
	var fieldId = getId(field.name+sectionId);
	var displayId1 = "displayMap1[" + fieldId.templateId + "_" + fieldId.sectionId + "]" ;
	var displayId = "displayMap[" + fieldId+ "]" ;
	
	var prefix = '_8';
	if (displayId1.indexOf(prefix) >= 0) {
	    for (i = 0; i < cashAccountTemplates.length; i++) {
			if (displayId1 == cashAccountTemplates[i]) {
				var selectedTemplate = displayId1; 
				if (field.value == 'Y' && field.checked) {
					for (i = 0; i < cashAccountTemplates.length; i++) {
						if (selectedTemplate != cashAccountTemplates[i]) {
							var otherCashAccountTemplates = document.getElementsByName(cashAccountTemplates[i]);
							otherCashAccountTemplates[1].checked = true; //Defaulting to N ie., No
						}
					}
				}
			}
		}
	}
   //Cash Account Message validation changes - End
   
   setSameTemplate(field, "displayMap[");
   checkSectionDisplay(field.form);
   updatePriority(field);
}

function updatePriority(field) {
	var fieldId = getId(field.name);
	
	
   priorityId = "priorityMap["+fieldId+"]" ;
   
   if(field.id.indexOf('-0') != -1){
    		 var res = field.id.split('-0');
				id = res[0];
		  }else{
			  var res = field.id.split('-1');
				id = res[0];
		  }
	var fieldId1 = getId(id);

   displayId = "displayMap["+ fieldId1.templateId + "_" + fieldId1.sectionId + "]"+"-0";
  
   var displays = document.getElementById(displayId);

   var displayed = false;
   if(displays.checked){
	   
	   displayed = true;
   }
 
   
   var priorities = document.getElementsByName(priorityId);
   
  
	for ( var i = 0 ; i < priorities.length ; i++ ) {
		
	   if ( displayed ) {
	   		//then we re-enabled the priority - unless the override is set to true
	   		//if ( priorityOverideMap[priorityId] == false ) {
				if ( displays.checked == true ) {
	   			priorities[i].disabled = false;
	   		}
	   } else {
		   
			  priorities[i].disabled = true;
	   }
	   
   }
}

// set the priority option change for message template
function priorityChange(field,sectionId) {
   setSameTemplate(field,"priorityMap[");
}

// Set the whole section's msg template to display = No or Yes
function setTemplatesValueInSection(frm, sectionId, value) {

   var prefix = "displayMap[";
   
   //var prefix1 = "sectionDisplay(";
   
	for (var i=0; i < frm.length; i++) {
      var field = frm.elements[i];
	  
      var fieldName = field.name;
	  var fieldId= field.id;
	  var id;
	 
	  if (fieldId.indexOf(prefix) >= 0) {
		  if(fieldId.indexOf('-0') != -1){
    		 var res = fieldId.split('-0');
				id = res[0];
		  }else{
			  var res = fieldId.split('-1');
				id = res[0];
		  }
		
		var sid2 = getId(id).sectionId;
	     if (sectionId == sid2 && field.value== value) { 
		 
			if ( !field.disabled ) {
				
	    		 field.checked = true;
			}
	        
	     }
	   }
   }

   
    


   /*
    * setTemplatesValueInSection specific to cash account messages
    * i.e. among the three cash account messages (178,179,180) in the section 8 
    * only one message will be set to Yes others will be set to No 
    */
    //Cash Account Message validation changes - start
	var sectionDisplayId = "sectionDisplay("+sectionId+")" ;
	var prefix = '8';
  	if (sectionDisplayId.indexOf(prefix) >= 0) {
  		if ( value == 'Y') {  		
			//Set first message as defaulted
			var firstCashAccountTemplates = document.getElementsByName(cashAccountTemplates[0]);
			firstCashAccountTemplates[0].checked = true; //Defaulting to Y ie., Yes
			firstCashAccountTemplates[1].checked = false; 
	  		
	  		//for 2nd and 3rd messages
		    for (i = 1; i < cashAccountTemplates.length; i++) {
				var otherCashAccountTemplates = document.getElementsByName(cashAccountTemplates[i]);
				otherCashAccountTemplates[1].checked = true; //Defaulting to N ie., No
			}
  		}
	}
	//Cash Account Message validation changes - End

    checkSectionDisplay(frm);
}


// check each message template's display option to
// decide the section's display option
function checkSectionDisplay(frm) {
	
   var prefix = "displayMap[";
   var sectionCounts = [];
   for (var i=0; i < frm.length; i++) {
      var field = frm.elements[i];
      var fieldName = field.name;
	  var fieldId = field.id;
	 var id;
	  
      // it is for display
	  if (fieldId.indexOf(prefix) >= 0) {
		  
		   if(fieldId.indexOf('-0') != -1){
    		 var res = fieldId.split('-0');
				id = res[0];
		  }else{
			  var res = fieldId.split('-1');
				id = res[0];
		  }
		
	      var fullId = getId(id);
		  
	      if (field.value == 'Y' && field.checked) {
	         addDisplayCount(sectionCounts, fullId.sectionId);
	      } 
  		  updatePriority(field);
	  }
   } 

   prefix = "sectionDisplay(";
   for (var i=0; i < frm.length; i++) {
      var field = frm.elements[i];
      var fieldName = field.name;
	 
      // it is for display
	  if (fieldName.indexOf(prefix) >= 0) {
		  
	     var sid = getSectionId(fieldName);
		
	     selectedCount = getDisplayCount(sectionCounts, sid);
	     totalCount = getTemplateCountInSection(sid);
	     setSectionInfo(sid, selectedCount, totalCount);
	     var sectionDisplayId = "sectionDisplay(" + sid + ")" ;
		 if (field.value == 'Y' && sectionDisplayId == 'sectionDisplay(8)') {
			 field.checked = (selectedCount == 1);
		 } else if (field.value == 'N') {
	           field.checked = (selectedCount == 0);
	     } else  if (field.value == 'Y') {
	          field.checked = (selectedCount == totalCount);
	      }
	   }
   }
	
   //need to disable the section display if all of its template displays are disabled. 
   for ( var i = 0 ; i < sectionMap.length ; i++ ) {
	  
	   var disabled = "";
	   var allDisabled = true;
	   for ( var j = 0 ; j < sectionMap[i].templates.length  && allDisabled; j++ ) {
		   
		  var objs = document.getElementsByName("displayMap[" + sectionMap[i].templates[j] + "]");
		 
		//  alert("displayMap[" + sectionMap[i].templates[j] + "_" + sectionMap[i].sectionId + "]");
		  
		   	if ( objs.length == 2 ) {
			   	if (!objs[0].disabled) {
			   		allDisabled = false;
			   	}
		   	}
	   } 
	   
	   if ( allDisabled ) {
		   var objs = document.getElementsByName("sectionDisplay(" + sectionMap[i].sectionId + ")");
		  for ( var k = 0 ; k < objs.length ; k++ ) {
		   objs[k].disabled = true;
		  }
	   }
   }
   
}

function setSectionInfo(sid, selectedCount, totalCount) {   
   var sectionSpan = "SectionInfo_" + sid;
   var spEle = document.getElementById(sectionSpan);
   var selectedCountStr = selectedCount;
   spEle.innerHTML = "(You will receive " + selectedCountStr + " of these " + totalCount + " message types)"; 
}
// Find the same template in other section to make it
// the same option
function setSameTemplate(field,prefix) {
	
   var tid = getId(field.name);

   var frm = field.form;
   for (var i=0; i < frm.length; i++) {
      var fieldName = frm.elements[i].name;
      if (fieldName.indexOf(prefix) >= 0) {
        var tid2 = getId(fieldName);
	
        if (tid == tid2 && field.value== frm.elements[i].value && !frm.elements[i].disabled) {
			
          frm.elements[i].checked = true;
        }
      }
   }
}
  
// the object contains both section id and template id
function fullId(sid, tid) {
    this.sectionId = sid;
    this.templateId = tid;
}

var pattern = /\d+/g;

// extract the id from a field name
// i.e. display(1-2)
function getId(name) {
    var res = name.match(pattern);
    if (res.length == 2) {
       return new fullId(res[1], res[0])
    }else{
		return res[0];
	}    
}
function getSectionId(name) {
	var res = name.match(pattern);
	return res[0];
}  

YAHOO.util.Event.onDOMReady(function () {
    expandErrorSections();
    checkSectionDisplay(document.forms['messagePrefForm']);    
 }
)

function copyPreference() {
  document.forms['messagePrefForm'].action.value="copyPreference";
  document.forms['messagePrefForm'].submit();
}  


// submit the form
function doSave(frm, action) {
    // Check if submit is in progress
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    } // fi

    if (frm.elements['applyToAll'].checked) {
        if (!(confirm("Do you really want to apply values to all your contracts?"))) {
            resetSubmitInProgress();
            return false;
        }
    }

    frm.elements['action'].value=action
    frm.submit();
}

<content:contentBean contentId="<%=MCContentConstants.CancelHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="cancelLabel" />

<content:contentBean contentId="<%=MCContentConstants.SaveHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="saveLabel" />

<content:contentBean contentId="<%=MCContentConstants.SaveFinishHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="saveFinishLabel" />

<content:contentBean contentId="<%=MCContentConstants.ReceiveHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="receiveHoverOver" />

<content:contentBean contentId="<%=MCContentConstants.PriorityHoverOver%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="priorityHoverOver" />

var cancelLabel = '<content:getAttribute id="cancelLabel" attribute="text" filter="true"/>'
var saveLabel = '<content:getAttribute id="saveLabel" attribute="text" filter="true"/>'
var saveFinishLabel = '<content:getAttribute id="saveFinishLabel" attribute="text" filter="true"/>'
var receiveHoverOver = '<content:getAttribute id="receiveHoverOver" attribute="text" filter="true"/>'
var priorityHoverOver = '<content:getAttribute id="priorityHoverOver" attribute="text" filter="true"/>' 

function enableButtons() {
    if (document.getElementById('saveAndFinishButton')) {
        document.getElementById('saveAndFinishButton').disabled=false;
    } // fi
    if (document.getElementById('saveButton')) {
        document.getElementById('saveButton').disabled=false;
    } // fi
    if (document.getElementById('cancelButton')) {
        document.getElementById('cancelButton').disabled=false;
    } // fi
}

// Registers our function with the functions that are run on load.
if (typeof(runOnLoad) == "function") {
  runOnLoad( enableButtons );
}

//-->
</script>






<ps:form method="post" action="/do/mcPersonalizeMessage" name="messagePrefForm" modelAttribute="messagePrefForm">

<content:contentBean contentId="<%=MCContentConstants.ApplyToAllContracts%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="applyToAllContractsLabel" />

<content:contentBean contentId="<%=MCContentConstants.MultiContractCopyLine1%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="copyLine1" />
                     
<content:contentBean contentId="<%=MCContentConstants.MultiContractCopyLine2%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="copyLine2" />

<input type="hidden" name="action"/>

<content:errors scope="request"/>

<table border="0" cellpadding="0" cellspacing="0" width="708">
	<tbody>
		<mc:personalizationTab active="message" global="<%=(Boolean)MCUtils.isInGlobalContext(request)%>" tpa="<%=SessionHelper.getUserProfile(request).getRole().isTPA() %>" noticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ALERT_NOITICE_PREFERENCE) %>" enableNoticalertTab="<%= (Boolean)session.getAttribute(MCConstants.ENABLE_ALERT_NOITICE_PREFERENCE) %>" />
		<tr>
			<td width="1" class="boxborder">
			 <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="699">
			<table border="0" cellpadding="1" cellspacing="0" width="710">
				<tbody>
				   <c:if test="<%=MCUtils.hasMoreThanOneMCContracts(userProfile)%>">				
				    <tr>
				      <td colspan="5" class="tableheadTD">
				     
				         <content:getAttribute id="copyLine1" attribute="text"/>
				            <form:select path="fromContract" onchange="copyPreference()">
<form:options items="${messagePrefForm.contracts}" itemLabel="label" itemValue="value"/>
</form:select>
				          <content:getAttribute id="copyLine2" attribute="text"/>
				       </td>
				    </tr>
				  </c:if>		  
				
					<tr height="25">
						<td width="521" valign="middle" class="tableheadTD">
						  <a href="javascript:toggleAll(true)"><img src="/assets/unmanaged/images/plus_icon.gif" border="0"/></a>
						  /
						  <a href="javascript:toggleAll(false)"><img src="/assets/unmanaged/images/minus_icon.gif" border="0"/></a>
						  <b>All</b>
						</td>
						<td width="1" valign="middle" class="dataheaddivider">
						  <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
						</td>
						<td width="100" valign="middle" class="tableheadTD" onmouseover="Tip(receiveHoverOver)" onmouseout="UnTip()">
						 <b>Receive?</b>
						</td>
						<td width="1" valign="middle" class="dataheaddivider">
						  <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
						</td>
						<td width="75" valign="middle" class="tableheadTD" onmouseover="Tip(priorityHoverOver)" onmouseout="UnTip()">
						  <b>Priority</b>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
			<td width="1" class="boxborder">
			 <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
		
		<tr>
		    <td width="1" class="boxborder">
		     <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		    </td>
		    
		    <td>  <!--  start1 -->
<jsp:useBean id="messagePrefForm" scope="session" type="com.manulife.pension.ps.web.messagecenter.personalization.MCMessagePreferenceForm" /> 


    		  
	<c:forEach var="tab" items="${messagePrefForm.messageCenterTop.children}">
	
	

<c:set var="tab" value="${tab}" />

	    <c:if test="${messagePrefForm.tabContainsTemplate(tab)}">
		    <table width="710" border="0" cellspacing="0" cellpadding="1">
                <tr height="30">
                    <td class="tablesubMainhead"><span class="style1"><c:out value="${tab.name}"/></span></td>
                </tr>
            </table>
	     <c:forEach var="section" items="${tab.children}">
	   
<c:set var="section" value="${section}" />

	      <c:if test="${messagePrefForm.sectionContainsTemplate(section)}">		    
  	      <mc:preferencesInSection messagePrefForm="${messagePrefForm}" section="${section}"/> 
  	        <script type="text/javascript">
				<!--
  					addSection(${section.id.value});
				//-->
				</script>
  	     </c:if>   
		 </c:forEach>
		 </c:if>
	 </c:forEach>
		    </td>  <!--  end1 -->
		    
		    <td width="1" class="boxborder">
			 <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		  </tr>			           
          <tr>
            <td class="boxborder" colSpan="3"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          </tr>
	</tbody>
</table>
<br>
<br>
<table width="708" border="0" cellspacing="0" cellpadding="1">
 <c:choose>
  <c:when test="<%=MCUtils.hasMoreThanOneMCContracts(userProfile) %>">
    <tr>
      <td colspan="3" align="left">
<form:checkbox path="applyToAll"/><content:getAttribute id="applyToAllContractsLabel" attribute="text"/>
      </td>
    </tr>
  </c:when>
  <c:otherwise>
<input type="hidden" name="applyToAll" value="off"/>
  </c:otherwise>
 </c:choose> 
    <tr>
      <td width="385"><div align="right">
         <input class="button134" type="button" name="button1" value="cancel"
            id="cancelButton" 
            disabled="disabled" 
            onmouseover="Tip(cancelLabel)" onmouseout="UnTip()"
            onclick="return doConfirmAndSubmit(this.form, 'cancel')">
      </div></td>
      <td width="181">
        <div align="center">
         <input class="button134" type="button" name="button2" value="save" 
            id="saveButton" 
            disabled="disabled" 
            onmouseover="Tip(saveLabel)" onmouseout="UnTip()"
            onclick="return doSave(this.form, 'save')">
        </div>
      </td>
      <td width="136">
        <div align="right">
         <input class="button134" type="button" name="button3" 
            id="saveAndFinishButton" 
            disabled="disabled" 
            onmouseover="Tip(saveFinishLabel)" onmouseout="UnTip()"
            value="save &amp; finish" onclick="return doSave(this.form, 'finish')" >
        </div>
      </td>
    </tr>
</table>	
</ps:form>
   
<script language="javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
   

<%@page import="com.manulife.pension.ps.web.messagecenter.history.MCCarReportController"%>
<%@page import="java.util.Collection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/java-encoder.tld" prefix="e" %>

<%@page import="com.manulife.pension.ps.web.messagecenter.history.MCCarViewUtils"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"%>
<%@page import="com.manulife.pension.ps.web.util.SessionHelper"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCContentConstants"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.history.MCMessageReportForm"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%><style>
<!--
  .advShow {
  }
  .advHide {
     display: none;
  }
-->
</style>
<%@page import="com.manulife.pension.ps.web.messagecenter.history.MCHistoryUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	boolean disabled = !StringUtils.isEmpty(request.getParameter("printFriendly"));
	pageContext.setAttribute("disableflag",disabled,PageContext.PAGE_SCOPE);
	MCMessageReportForm carViewForm = (MCMessageReportForm)session.getAttribute("carViewForm");
	pageContext.setAttribute("carViewForm",carViewForm,PageContext.PAGE_SCOPE);
%>

<script type="text/javascript">
<!--

	String.prototype.trim = function () {
	    return this.replace(/^\s*/, "").replace(/\s*$/, "");
	}

   var recipientMap = new Array();
   var messageStatus = new Array();
   var messageStatusWithRecipient = new Array();
   var defaultMessageStatus = '<c:out value="${carViewForm.messageStatus}"/>';
   var defaultRecipient = '<c:out value="${carViewForm.recipient}"/>';
   var disabledByPrintFriendly = <%=disabled%>;
   	var default_type= '<c:out value="${carViewForm.type}"/>';
   	var default_tab= '<c:out value="${carViewForm.tab}"/>';
   	var default_section= '<c:out value="${carViewForm.section}"/>';
   

   	<%=MCHistoryUtils.getTabsAndSectionMapAsJavaScript(getServletContext(), request)%>
   	<%=MCHistoryUtils.getTypesByTabAsJavaScript(getServletContext(), request)%>
   	<%=MCHistoryUtils.getTypesBySectionAsJavaScript(getServletContext(), request)%>
   	<%=MCCarViewUtils.getMessageStatusAsJavascript(this)%>
    
    
    //update the padding
    var paddingString = "10px"
    for ( i = 0 ; i < messageStatus.length ; i++ ) {
    	if ( i > 2 ) messageStatus[i].padding = paddingString;
    }
    for ( i = 0 ; i < messageStatusWithRecipient.length ; i++ ) {
    	if ( i > 2 ) messageStatusWithRecipient[i].padding = paddingString;
    }
    
   
   var noResultsOption = new Option("No Results","",false,false);
   var workingOption = new Option("Working...","",false,false);
   var clientUserString  = "===== Client User =====";
   var tpaUserString     = "======= TPA ========";
   var internalUserString = "==========Internal User==========";
   
    function option(id, name) {
      this.id = id;
      this.name = name;
      this.padding = "";
      this.toOptionElement = function () {
      		var o = new Option(this.name, this.id, false, false);
      		if ( this.padding != "" ) o.style.paddingLeft = this.padding;
    		return o;
      }
    }
    function Recipient(id, firstName, lastName,  role ) {
    	this.id = id;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.role = role;
    	
    	this.toOptionElement = function () {
    		return new Option(this.lastName + ", " + this.firstName, this.id, false, false);
    	}
    }
    function addOption(select, option) {
    	try {
    		select.add(option,null)
    	} catch (e) {
    		select.add(option) //cause IE sucks.
    	}
    }
    
 
    function optionMap(key, options) {
      this.key = key;
      this.options = options;
    }
   
	function updateSection(tabId) {
	   updateOptionsByKey('section', sections, tabId);
	}
	
	function updateTypesByTab(tabId) {
	   updateOptionsByKey('type', typesByTab, tabId);
	}
	
	function updateTypesBySection(sectionId) {
	   updateOptionsByKey('type', typesBySection, sectionId);
	}

    function updateOptionsByKey(optionFieldName, map, key) {
	   var selectSection = document.forms['carViewForm'].elements[optionFieldName];
	   selectSection.options.length = 0;   
	   for (var i = 0; i < map.length; i++) {
	      if (map[i].key == key) {
	         selectSection.options.length = map[i].options.length;
	         for (var j = 0; j < map[i].options.length; j++) {
	     	  selectSection.options[j].text = map[i].options[j].name;
	       	  selectSection.options[j].value = map[i].options[j].id; 
	    	 }
	    	 break;
	      }
	   }
    }
    
	function selectTab(field) {
	    updateSection(field.value);
	    updateTypesByTab(field.value);
	}
	
	function selectSection(field) {
	  if (field.value == "") {
	    updateTypesByTab(field.form.elements['tab'].value);
	  } else {	  
	    updateTypesBySection(field.value);
	  }
	}
		
	function doCalendar(fieldName) {
	 <c:if test="${empty param.printFriendly}">
		cal = new calendar(document.forms['carViewForm'].elements[fieldName]);
		cal.year_scoll = true;
		cal.time_comp = false;
		cal.popup();
	 </c:if>
	}
	
	function clearSsn(frm) {
	     frm.ssn1.value = "";
	     frm.ssn2.value = "";
	     frm.ssn3.value = "";
	}

	function clearName(frm) {
	     frm.lastName.value = "";
	}
	
    function toggleAdvSearch() {
      <c:if test="${empty param.printFriendly}">
	   var eleLabel = document.getElementById("advSearchAnchor");
	   var frm = document.forms['carViewForm'];
	   var sc;
       if (frm.advancedSearch.value=='true') {
          frm.advancedSearch.value = 'false'
          eleLabel.innerHTML="Advanced search";
          sc = "advHide";
       } else {
          eleLabel.innerHTML="Basic search";
          frm.advancedSearch.value = 'true'
          sc = "advShow";
       }
    
       document.getElementById("adv1").className=sc;
       document.getElementById("adv2").className=sc;
       if (clearAdvanced(document.forms['carViewForm'])) {
         document.forms['carViewForm'].submit();
       }
      </c:if>
    }
    		
    function clearAdvanced(frm) {
      var needReset = false;
      if (frm.lastName.value != "") {
        frm.lastName.value = "";
        needReset = true;
      }
      if (frm.ssn1.value != "") {
        frm.ssn1.value = "";
        needReset = true;
      }
      if (frm.ssn2.value != "") {
        frm.ssn2.value = "";
        needReset = true;
      }
      if (frm.ssn3.value != "") {
        frm.ssn3.value = "";
        needReset = true;
      }
      if (frm.submissionId.value != "") {
        frm.submissionId.value = "";
        needReset = true;
      }
      
      
      return needReset;
    }    		
    
	function doReset(frm) {
	     frm.fromDate.value = '<c:out value="${sessionScope.carViewForm.defaultFromDate}"/>';
	     frm.toDate.value = '<c:out value="${sessionScope.carViewForm.defaultToDate}"/>';
	     frm.tab.value = "";
	     frm.section.value = "";
	     frm.messageId.value="";
	     frm.recipient.value="";
	     frm.messageStatus.value="<%=MCCarViewUtils.MessageStatus.ALL.getValue()%>";
	     frm.type.value = "";
	     frm.lastName.value = "";
	     frm.ssn1.value = "";
	     frm.ssn2.value = "";
	     frm.ssn3.value = "";
	     frm.submissionId.value="";
	     frm.contractId.value = <%if(MCUtils.isInGlobalContext(request)) {%> "" <%} else { out.print("\""); out.print(SessionHelper.getUserProfile(request).getCurrentContract().getContractNumber()); out.print("\"");} %>
	     frm.submit();
	}
	function changeMessageStatus(select, list) {
		var oldValue = select.value;
		if ( select == null ) return;
		while(select.length > 0 ) select.remove(0);
		for ( i = 0 ; i < list.length ; i++ ) {
			addOption(select, list[i].toOptionElement());
		}
		for ( i = 0 ; i < select.options.length ; i++ ) {
			if (select.options[i].value == oldValue ) 
				select.options[i].selected = "selected"; 
		}
		
	}
	//whenever i change the value of the checkbox, i need to also change the value of the hidden field.
	function onAllMessages() {
		document.forms['carViewForm']['allMessages'].value = document.forms['carViewForm']['allMessagesDummy'].checked ?  "true" : "false"; 
	}
	
	function changeRecipient(select) {
	   var selectValue = select.value.trim();
	   form = select.form;
	   if (selectValue == "" || selectValue == tpaUserString || selectValue == clientUserString  || selectValue == internalUserString || selectValue == "All") {
		  document.forms['carViewForm']['allMessages'].value = "false";
		  document.forms['carViewForm']['allMessagesDummy'].checked = false;
		  document.forms['carViewForm']['allMessagesDummy'].disabled = true;
		  document.getElementById('allMessagesSpan').style.color = "gray";
		  document.getElementById('viewPersonalizeSpan').style.display = "";
		  document.getElementById('viewPersonalizeAnchor').style.display = "none";
		  changeMessageStatus(document.forms['carViewForm']['messageStatus'], messageStatus);
		  
	   } else {
	   	   if ( recipientMap[selectValue] )  {
				var getMultipleContractsCallback = { 
				    cache:false,
				    failure: function(response) {},  //TODO get ride of alert
				    argument: null ,
				    success: function(response) {
						//validate the response.
					  var selectValue = document.forms['carViewForm']['recipient'].value.trim();
				      if (selectValue != "" && selectValue != tpaUserString 
							   && selectValue != clientUserString && selectValue != internalUserString  && selectValue != "All") {
					      var result = response.responseText;
					      document.forms['carViewForm']['allMessagesDummy'].disabled = result == "no";
						  document.getElementById('allMessagesSpan').style.color = result == "no" ? "gray" : "black";
						  document.getElementById('viewPersonalizeSpan').style.display = "none"
						  document.getElementById('viewPersonalizeAnchor').style.display = ""
					      if ( result == "no" ) {
					      	document.forms['carViewForm']['allMessagesDummy'].checked = false;
					      	document.forms['carViewForm']['allMessages'].value = "false";
					      }
				      }
				    } 
				  };
				var rec = recipientMap[selectValue]
	   	   		document.forms['carViewForm']['recipientRole'].value  = rec.role; 
		    	var transaction = YAHOO.util.Connect.asyncRequest('GET'
				  		, '/do/mcCarView/getMultipleContracts?userId=' +  rec.id + '&role=' + rec.role
				  		, getMultipleContractsCallback)
   		        changeMessageStatus(document.forms['carViewForm']['messageStatus'], messageStatusWithRecipient);
           }
	   }
	}
	function adjustRecipientWidth() {
		var minWidth = 142;
		var sel = document.forms['carViewForm']['recipient'];
		if ( sel.offsetWidth < minWidth ) {
			sel.style.width = minWidth + "px";
		}
	}
	function onContractIdChange (e) {
		  var getRecipientsCallback = { 
		    cache:false,
		    success: function(response) {
		      /*success handler code*/
		      var result = response.responseText;
		      var recipientSelect = document.forms['carViewForm']['recipient'];
		      while ( recipientSelect.length > 0 ) recipientSelect.remove(0);		  

		      if ( result.substring(0,20) != "GetRecipientsResult:") {
		         if(document.forms['carViewForm']['contractId'].value != "") {
				    addOption(recipientSelect,noResultsOption);		      	
		      	 } 
		      	adjustRecipientWidth()
		      	return;
		      }
		      result = result.substring(20).split("{}");
		      tpas = result[0].split("~~");
		      ps = result[1].split("~~");
		      internals = result[2].split("~~");

		      var option1 = new Option("All","",false,false);
		      var option2 = new Option(clientUserString,clientUserString,false,false);
		      var option3 = new Option(tpaUserString,tpaUserString,false,false);
		      var option4 = new Option(internalUserString,internalUserString,false,false);
		      addOption(recipientSelect,option1);
		      if ( ps[0] != "" ) {
		      	addOption(recipientSelect,option2);
	   		      for ( i = 0 ; i < ps.length ; i++ ) {
			      		var u = ps[i].split("|");
			      		var recipient = new Recipient(u[0],u[1],u[2],u[3]);
			      		recipientMap[u[0]] = new Recipient(u[0],u[1],u[2],u[3]);
			      		addOption(recipientSelect,recipient.toOptionElement());
			      }
		      }
		      if ( tpas[0] != "" ) {
			      addOption(recipientSelect,option3);
			      for ( i = 0 ; i < tpas.length ; i++ ) {
			      		var u = tpas[i].split("|");
			      		var recipient = new Recipient(u[0],u[1],u[2],u[3]);
			      		recipientMap[u[0]] = recipient;
			      		addOption(recipientSelect,recipient.toOptionElement());
			      }
		      }
		      if ( internals[0] != "" ) {
			      addOption(recipientSelect,option4);
			      for ( i = 0 ; i < internals.length ; i++ ) {
			      		var u = internals[i].split("|");
			      		var recipient = new Recipient(u[0],u[1],u[2],u[3]);
			      		recipientMap[u[0]] = recipient;
			      		addOption(recipientSelect,recipient.toOptionElement());
			      }
		      }
			  recipientSelect.disabled = disabledByPrintFriendly ? true: false;
			  recipientSelect.value = defaultRecipient;
			  changeRecipient(recipientSelect);
              setTimeout("adjustRecipientWidth()", 20); //for some reason nobody will ever know, ie doesn't update the width of the select element
              											//immediately.  this super-hack is necessary to so i can determine the width of the select
			  
		    }, 
		    failure: function(response) {}, 
		    argument: null 
		  };
		  var recipientSelect = document.forms['carViewForm']['recipient'];
		  document.forms['carViewForm']['allMessagesDummy'].disabled = true;
		  document.getElementById('allMessagesSpan').style.color = "gray";
		  document.getElementById('viewPersonalizeSpan').style.display = "";
		  document.getElementById('viewPersonalizeAnchor').style.display = "none";
		  recipientSelect.disabled=true;
	      while ( recipientSelect.length > 0 ) recipientSelect.remove(0);		  
          addOption(recipientSelect,workingOption);		      	
	
		  var transaction = YAHOO.util.Connect.asyncRequest('GET'
		  		, '/do/mcCarView/getRecipients?contractId=' + document.getElementById('contractId').value 
		  		, getRecipientsCallback);
	}

	function init() {
        changeMessageStatus(document.forms['carViewForm']['messageStatus'], defaultRecipient == '' ? messageStatus : messageStatusWithRecipient);
		onContractIdChange();
		document.forms['carViewForm']['messageStatus'].value = defaultMessageStatus;
		document.forms['carViewForm']['allMessagesDummy'].checked = document.forms['carViewForm']['allMessages'].value == "true" ? true : false;
		document.forms['carViewForm']['viewPersonalization'].value = "false";
		selectTab(document.forms['carViewForm']['tab']);
    	if ( default_type != '' )
    		document.forms['carViewForm']['type'].value = default_type;
    	if ( default_tab != '' )
    		document.forms['carViewForm']['tab'].value = default_tab;
    	if ( default_section != '' )
    		document.forms['carViewForm']['section'].value = default_section;

		if ( <%=disabled%> ) {
			removeAllLinks()
		}		
	}
	
	function checkLink(url, contractId) {
		  var checkAccessCallBack = { 
		    cache:false,
		    success: function(response) {
		      /*success handler code*/
		      var result = response.responseText;
		      if ( result == "no" ) {
		      	alert('Sorry, you do not have access to that page.')
		      } else {
		      	params = result.split("||");
		      	gotoLink('/do/mcCarView/infoLink?url=' + params[0] + '&contractId=' + params[1]);
	   	      }
		    }, 
		    failure: function(response) { alert('Sorry, you do not have access to that page.')}, 
		    argument: null 
		  };
		  var transaction = YAHOO.util.Connect.asyncRequest('GET'
		  		, '/do/mcCarView/checkAccess?url=' + url + '&contractId=' + contractId
		  		, checkAccessCallBack);	
	}
	function viewPersonalization() {
		document.forms['carViewForm']['viewPersonalization'].value = "true";
		document.forms['carViewForm'].submit();
	}
	YAHOO.util.Event.onDOMReady(init)
	YAHOO.util.Event.addListener("contractId", "change", onContractIdChange); 	

//-->
</script>

<content:contentBean contentId="<%=MCContentConstants.PersonalizationHover%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="personalizationHover" />
                     

<input type="hidden" name="task" value="filter" /><%--  input - disabled="<%=disabled %>" --%>
<input type="hidden" name="advancedSearch" /><%--input - disabled="<%=disabled %>" --%>
<input type="hidden" name="recipientRole"/>
<input type="hidden" name="allMessages"/>
<input type="hidden" name="viewPersonalization"/>


<logicext:if name="carViewForm" property="advancedSearch" value="true" op="equal" scope="session">
 <logicext:then>
    <c:set var="advClass" value="advShow"/>
    <c:set var="searchLabel" value="Basic search"/>
 </logicext:then>
 <logicext:else>
	<c:set var="advClass" value="advHide"/>
	<c:set var="searchLabel" value="Advanced search"/>
 </logicext:else>         
</logicext:if>

<table width="710" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td colspan="4" align="left">
				<content:errors scope="session"/>			
			</td>
		</tr>
		<tr>
			<td colspan="2" class="tablesubhead" style="padding:3px"><b>Message Search </b></td>
			<td colspan="2" class="tablesubhead" align="right">
			  <a id="advSearchAnchor" href="javascript:toggleAdvSearch()">${searchLabel}</a>&nbsp;
			</td>
		</tr>
		<tr>
		  <td class="filterSearch" colspan="4" height="5">
	      </td>
		</tr>
		<tr>
		   <td width="160" valign="top" class="filterSearch" align="left" style="padding-bottom:0px">
			 <b>Contract number</b><br>
			 <% if ( MCUtils.isInGlobalContext(request) ) {%>
<form:input path="contractId" disabled="${disableflag}" maxlength="7" size="7" id="contractId"/>
			 <% } else {  %>
<form:hidden  path="contractId"  id="contractId"/>
			 ${e:forHtmlContent(sessionScope.carViewForm.contractId)}
			 <% } %>
		   </td>
           <td width="160" valign="top" class="filterSearch" align="left" style="padding-bottom:0px">
			 <b>Recipient</b><br>			 
			  
<form:select disabled="true" path="recipient" onchange="changeRecipient(this)" disabled="<%=disabled %>">
			  </form:select>
		   </td>
		   	<td class="filterSearch" style="padding-bottom:0px"><br/>
		   		<input onchange="onAllMessages()" onclick="onAllMessages()" name="allMessagesDummy" type="checkbox" <%if(!MCUtils.isInGlobalContext(request)){ %>style="display:none;"<%} %>>
		   		<span id="allMessagesSpan" <%if(!MCUtils.isInGlobalContext(request)){ %>style="display:none;"<%} %>>All messages for this recipient?</span>
		   	</td>
		   	<td class="filterSearch" style="padding-bottom:0px"><br/>
			  <c:if test="${disabled}">
			    <input disabled="disabled" type=submit value=search name=search >&nbsp;&nbsp;&nbsp; 
			    <input disabled="disabled" type=button value=reset name=reset> 
			  </c:if>  
			  <c:if test="${!disabled}">
			    <input type=submit value=search name=search =>&nbsp;&nbsp;&nbsp; 
			    <input type=button value=reset name=reset onclick="javascript:doReset(this.form)" >
			  </c:if>  
			</td>		   
		</tr>
		<tr><td class="filterSearch"><td colspan="3" class="filterSearch" style="padding-left:5px;padding-top:0px;padding-bottom:0px;">
			<a id="viewPersonalizeAnchor" href="javascript:viewPersonalization()">View Personalization</a>
			<span id="viewPersonalizeSpan" style="color:gray;text-decoration:underline;" onmouseover="Tip('<content:getAttribute beanName="personalizationHover" attribute="text"/>')" onmouseout="UnTip()">View Personalization</a>
		</tr>					
		<tr>
 		     <td colspan="2" class="filterSearch" >
<b>Posted</b> &nbsp; <br>&nbsp;&nbsp;&nbsp;from&nbsp;<form:input path="fromDate" disabled="<%=disabled %>" maxlength="10" size="10"/>
			   <a href="javascript:doCalendar('fromDate')"> 
			    <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
			    <strong> &nbsp;&nbsp;</strong>to 
<form:input path="toDate" disabled="${disableflag}" maxlength="10" size="10"/>
			    <a href="javascript:doCalendar('toDate')" >
			    <img src="/assets/unmanaged/images/cal.gif" border="0"></a><br>
 		     </td>	
 			<td width="160" valign="top" class="filterSearch" align="left">
			  <b>Message status</b><br>
			  <form:select path="messageStatus" disabled="${disabled}" cssStyle="width:165px">
			  	<option value="" selected="selected"></option>
</form:select>
             </td>
		     <td width="140" valign="top" class="filterSearch" align="left">
			  <b>Message ID</b><br>
<form:input path="messageId" disabled="${disableflag}" maxlength="10" size="10"/>
 		     </td>
		</tr>
		<tr>
			<td valign="top" class="filterSearch" width="160" >
			 <b>Message tab </b><br>
			  <form:select path="tab" onchange="javascript:selectTab(this)" cssStyle="width: 150" disabled="${disabled}">
<form:options items="${carViewForm.tabb}" itemLabel="label" itemValue="value"/>
</form:select>
			</td>
			<td valign="top" class="filterSearch" width="160">
			  <b>Section</b><br>
			  <form:select path="section" onchange="javascript:selectSection(this)" cssStyle="width: 150" disabled="${disabled}">
<form:options items="${sections}"/>
</form:select>
			</td>
			<td colspan="2" valign="middle" class="filterSearch" width="368">
			 <b>Message type</b><br>
			 <form:select path="type" cssStyle="width: 360" disabled="${disabled}">
<form:options items="${types}"/>
</form:select>
			</td>
		</tr>
		<tr>
		  <td class="filterSearch" colspan="4" height="5">
	      </td>
		</tr>							
		<tr id="adv1" class="${advClass}">
			<td colspan="4"  valign="top" class="filterSearch" style="background-color:dcdad2;">
			  <b>Message context</b>
			</td>
		</tr>
		<tr id="adv2" class="${advClass}">
 			<td width="160" valign="top" class="filterSearch">
			 <b>Last name </b><br>			 
				<form:input path="lastName" disabled="<%=disabled%>" maxlength="30" onkeyup="clearSsn(this.form);" size="20"/>
			 </td>
			<td width="160" valign="top" class="filterSearch">
			  <b>SSN</b><br>
			  <form:password path="ssn1" size="3" maxlength="3" cssStyle="line-height:20px;height:20px"
	        	     onkeyup="clearName(this.form);autoTab(this, 3, event)" disabled="<%=disabled %>"  value="${carViewForm.ssn1}"/>
	          <form:password path="ssn2" size="2" maxlength="2" cssStyle="line-height:20px;height:20px"
	        	     onkeyup="clearName(this.form);autoTab(this, 2, event)" disabled="<%=disabled %>" value="${carViewForm.ssn2}"/>
	          <form:input path="ssn3" autocomplete="off" style="line-height:20px;height:20px" 
	            size="4" maxlength="4" onkeyup="clearName(this.form)" disabled="<%=disabled %>"/>	        	    	     	     
		   </td>
		   <td valign="top" class="filterSearch" colspan="2">
			  <b>Submission #</b><br>
				  <form:input path="submissionId" disabled="${disableflag}" maxlength="7" size="7"/>
		   </td>	
		</tr>	
		<tr>
		  <td class="filterSearch" colspan="4" height="5"> 
	      </td>
		</tr>							
	</tbody>
</table>



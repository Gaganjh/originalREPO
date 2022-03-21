<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>

<%@page import="com.manulife.pension.ps.web.messagecenter.history.MCHistoryUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCUtils"%>
<%@page import="com.manulife.pension.ps.web.util.SessionHelper"%>
<script type="text/javascript">
   	var default_type= '<c:out value="${messageHistoryForm.type}"/>';
   	var default_tab= '<c:out value="${messageHistoryForm.tab}"/>';
   	var default_section= '<c:out value="${messageHistoryForm.section}"/>';
   	
    <%= MCHistoryUtils.getTabsAndSectionMapAsJavaScript(getServletContext(), request) %>
    <%= MCHistoryUtils.getTypesByTabAsJavaScript(getServletContext(), request) %>
    <%= MCHistoryUtils.getTypesBySectionAsJavaScript(getServletContext(), request) %>  
   
    function option(id, name) {
      this.id = id;
      this.name = name;
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
	   var selectSection = document.forms['messageHistoryForm'].elements[optionFieldName];
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
	
	 function initTabs(optionFieldName, list) {
	 	   var tabSelect = document.forms['messageHistoryForm'].elements[optionFieldName];
	 	   tabSelect.options.length = list.length;
	 	   for (var i = 0; i < list.length; i++) {
	 		  tabSelect.options[i].text = list[i].name;
	 		  tabSelect.options[i].value = list[i].id; 
	 	   }
	 	  
	 }
		
	function doCalendar(fieldName) {
	 <c:if test="${empty param.printFriendly}">
		cal = new calendar(document.forms['messageHistoryForm'].elements[fieldName]);
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
		
	function doReset(frm) {
	     frm.fromDate.value = '<c:out value="${sessionScope.messageHistoryForm.defaultFromDate}"/>';
	     frm.toDate.value = '<c:out value="${sessionScope.messageHistoryForm.defaultToDate}"/>';
	     frm.tab.value = "";
	     frm.section.value = "";
	     frm.type.value = "";
	     frm.lastName.value = "";
	     frm.ssn1.value = "";
	     frm.ssn2.value = "";
	     frm.ssn3.value = "";
	     frm.submissionId.value="";
	     frm.messageStatusForHistory.value = "ALL";
	     frm.contractId.value = <%if(MCUtils.isInGlobalContext(request)) {%> "" <%} else { out.print("\""); out.print(SessionHelper.getUserProfile(request).getCurrentContract().getContractNumber()); out.print("\"");} %>
	     frm.submit();
	}
	
	function switchAdvance() {
	  <c:if test="${empty param.printFriendly}">
	   var ele = document.getElementById("advSearch");
	   var ele2 = document.getElementById("adv1");
	   var eleLabel = document.getElementById("advSearchAnchor");
	   var frm = document.forms['messageHistoryForm'];
       if (ele.style.display == "none") {
            ele.style.display = "";
            ele2.style.display = "";
            eleLabel.innerHTML="Basic search";
            frm.advancedSearch.value="true";
       } else {
            ele.style.display = "none";
            ele2.style.display = "none";
            eleLabel.innerHTML="Advanced search";
            // clear the advanced filters
		    frm.advancedSearch.value="false";
		    if (clearAdvanced(frm)) {
		    	frm.submit();
		    }
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
    function initHistory() {
    	selectTab(document.forms['messageHistoryForm']['tab']);
    	selectSection(document.forms['messageHistoryForm']['section']);
    	initTabs('tab',tabs);
    	if ( default_type != '' )
    		document.forms['messageHistoryForm']['type'].value = default_type;
    	if ( default_tab != '' )
    		document.forms['messageHistoryForm']['tab'].value = default_tab;
    	if ( default_section != '' )
    		document.forms['messageHistoryForm']['section'].value = default_section;

	     if ( <%=!StringUtils.isEmpty(request.getParameter("printFriendly"))%> ) {
	         removeAllLinks()
	     }		
   }
    
    YAHOO.util.Event.onDOMReady(initHistory)
</script>

<input type="hidden" name="task" value="filter"/>
<form:hidden path="advancedSearch"/>

<%
   boolean disabled = !StringUtils.isEmpty(request.getParameter("printFriendly"));
%>

<logicext:if name="messageHistoryForm" property="advancedSearch" value="true" op="equal" scope="session">
 <logicext:then>
    <c:set var="style" value=""/>
    <c:set var="searchLabel" value="Basic search"/>
 </logicext:then>
 <logicext:else>
	<c:set var="style" value="DISPLAY: none"/>
	<c:set var="searchLabel" value="Advanced search"/>
 </logicext:else>         
</logicext:if>
<table width="710" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td colspan="3" align="left">
				<content:errors scope="session"/>			
			</td>
		</tr>
		<tr>
			<td colspan="2" class="tablesubhead"><b>Message Search </b></td>
			<td  class="tablesubhead" align="right" colspan="2">
			   <a id="advSearchAnchor" href="javascript:switchAdvance()">${searchLabel }</a>&nbsp;
			</td>
		</tr>
		<tr>
		  <td class="filterSearch" colspan="4" height="5">
	      </td>
		</tr>	
		<%if(MCUtils.isInGlobalContext(request)) { %>
		<tr>
		  <td class="filterSearch" colspan="4" height="5">
		   <b>Contract number</b><br/>
		   <form:input path="contractId" maxlength="7" size="7"/>
	      </td>
		</tr>	
		<%}%>
		<tr>
			<td colspan="2" height="43" valign="bottom" class="filterSearch">
			<b>Posted</b> <br/>&nbsp;&nbsp;&nbsp; from
			<form:input path="fromDate" disabled="<%=disabled %>" maxlength="10" size="10"/>
			   <a href="javascript:doCalendar('fromDate')"> 
			    <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
			    <strong> &nbsp;&nbsp;</strong>to 
			    <form:input path="toDate"  disabled="<%=disabled %>" maxlength="10" size="10"/>
			    <a href="javascript:doCalendar('toDate')" >
			    <img src="/assets/unmanaged/images/cal.gif" border="0"></a><br>
			    <!--  
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 &nbsp;&nbsp;&nbsp;&nbsp;(mm/dd/yyyy)-->
			</td>
			<td valign="bottom" class="filterSearch">
				<b>Message status</b><br/>
				<form:select path="messageStatusForHistory">
				<form:options items="${messageHistoryForm.messageStatusList}" itemValue="value" itemLabel="label"/>
				</form:select>
			</td>
			<td valign="bottom" class="filterSearch">
			  <c:if test="<%=disabled%>">
			    <input disabled="disabled" type=submit value=search name=search>&nbsp;&nbsp;&nbsp; 
			    <input disabled="disabled" type=button value=reset name=reset > 
			  </c:if>  
			  <c:if test="<%=!disabled%>">
			    <input type=submit value=search name=search>&nbsp;&nbsp;&nbsp; 
			    <input type=submit value=reset name=reset onclick="javascript:doReset(this.form)"> 
			  </c:if>  
			</td>
		</tr>
		<tr>
			<td valign="top" class="filterSearch" width="180" >
			 <b>Message tab </b><br>
			 <form:select path="tab" onchange="javascript:selectTab(this)" style="width: 150" disabled="disabled">
			 <form:options items="${tabs}"/>
			 </form:select>
			</td>
			<td valign="top" class="filterSearch" width="180">
			  <b>Section</b><br>
			   <form:select path="section" onchange="javascript:selectSection(this)" style="width: 150" disabled="disabled">
			 <form:options items="${sections}"/>
			 </form:select>
			</td>
			<td valign="middle" class="filterSearch" width="368" colspan="2">
			 <b>Message type</b><br>
			 <form:select path="type" style="width: 360"disabled="disabled">
			 <form:options items="${types}"/>
			 </form:select>
			</td>
		</tr>

		<tr>
		  <td class="filterSearch" colspan="4" height="5">
	      </td>
		</tr>							

		<tr id="adv1" style="${style}">
			<td colspan="4"  valign="top" class="filterSearch" style="background-color:dcdad2;">
			  <b>Message context	</b>
			</td>
		</tr>
		<tr id="advSearch" style="${style}" >
 			<td width="180" valign="top" class="filterSearch">
			 <b>Last name </b><br>			
			 <form:input path="lastName"  maxlength="30" onkeyup="clearSsn(this.form);" size="20"/> 
			 </td>
			<td width="180" valign="top" class="filterSearch">
			  <b>SSN</b><br>
			  
			   <form:password path="ssn1"   onkeyup="clearName(this.form);autoTab(this, 3, event)" size="3" maxlength="3" value="${messageHistoryForm.ssn1}" disabled="disabled"/>
                <form:password path="ssn2"  onkeyup="clearName(this.form);autoTab(this, 2, event)" size="2" maxlength="2" value="${messageHistoryForm.ssn2}" disabled="disabled"/>
                 <form:input path="ssn3"  autocomplete="off"  onkeyup="clearName(this.form)" size="4" maxlength="4" disabled="disabled"/>
		   </td>
		   <td width="338" valign="top" class="filterSearch" colspan="2">
			  <b>Submission #</b><br>
			  <form:input path="submissionId" disabled="disabled" maxlength="7" size="7"/>
		   </td>			   	
		</tr>
		<tr>
		  <td class="filterSearch" colspan="4" height="5">
	      </td>
		</tr>							
	</tbody>
</table>
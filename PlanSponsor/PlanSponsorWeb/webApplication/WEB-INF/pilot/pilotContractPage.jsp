<!-- taglib used -->
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>




  <%@ page import="java.util.HashMap"%>   
  <%@ page import="java.util.Map"%>    
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%-- <c:set var="pilotView" value="${pilotViewBean.}" /> --%>
<c:set var="pilotView" value="${requestScope.pilotViewBean}" />

<!-- Applied Cancel change general framework from Charles Chan refere to changeTracker.js  -->

<content:contentBean
	contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningDiscardChanges" />
<content:contentBean
	contentId="<%=ContentConstants.PILOT_SEARCH_WARNING%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningSearch" />
<script type="text/javascript" >
function doSignOut(){
	if (discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>")){
		location.href = "/do/home/Signout/";
	}
}
function doCancel() 
{
	//alert();
	return discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>");
}

function doSearch() 
{
	//alert();
	return checkSearchChange("<content:getAttribute beanName="warningSearch" attribute="text" filter="true"/>");
}

function discardChanges(warning) 
{
	var formChanged = false;
	formChanged = changeTracker.hasChanged();
	
	//alert(changeTracker.hasChanged());
	if (formChanged) 
	{
    	if (window.confirm(warning)) 
    	{
      		return true;
    	} else {
	      return false;
    	}
  	}
  return true;
}
function checkSearchChange(warning) 
{
	var formChanged = false;
	formChanged = changeTracker.hasChanged();
	
	
	if (formChanged) 
	{
    	if (confirm(warning)){
    		return true;
    	} else {
    	  	return false;
    	}
  	}
  return true;
}

function doSave()
{
	if (!isSelect()){
		if (!window.confirm("At least one Pilot project must be selected."))
		return false;
	}
	//confirm
	
	if (window.confirm("Are you sure?")){
		 return true;
	} else {
		return false;
	}
}
function isSelect()
{
	
	var flag=false;
	
	for (var i = 0; i < document.forms[0].elements.length; i++) 
	{
     
      var htmlElement = document.forms[0].elements[i];
	  // correct for the special case of checkbox
	  if (htmlElement.type == "checkbox") {
		if (htmlElement.checked)
			flag = true;
	  }
	  
	 }
	 
    return flag;
  }

var keyCode;
document.onkeydown =
    function (evt) {
        keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
    }

function keyPressHandler(buttonName) { 
    if (keyCode == 13) {
		document.getElementsByName(buttonName)[0].click();
		return false;
    }
}
</script>

<%
	String actionStr = null;
	actionStr = (String) request.getAttribute("targetAction");
	actionStr = actionStr + "?from="+request.getParameter("from");
%>

<ps:form method="post" action="<%=actionStr%>" modelAttribute="pilotContractForm" name="pilotContractForm">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tbody>
		<tr>
			<td valign="top" width="30"><img src="/assets/unmanaged/images/s.gif" height="1" width="30"></td>
			<td valign="top" width="700">
	<table border="0" cellpadding="0" cellspacing="0" width="700">
	<tbody>
	<tr>
	<td>
	<table>
		<tr>
			<td width="70%"><img src="/assets/unmanaged/images/s.gif"
				height="1"><content:errors scope="request"/></td>
			<td width="3%"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>
			<td width="27%"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>
		</tr>
	</table>
	<br>
	<table border="0" cellpadding="0" cellspacing="0" class="box"
		valign="top">
		<tbody>
			<tr class="tablehead">
				<td colspan="3" class="tableheadTD1"><b>Enter contract
				number</b></td>
			</tr>
			<tr>
				<td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif"
					height="1" width="1"></td>
<td class="boxbody" width="212"><form:input path="contractNumber" maxlength="10" onkeypress="return keyPressHandler('searchBtn');" size="10"/>

				&nbsp;&nbsp;&nbsp;&nbsp; <input name="searchBtn" class="button100Lg"
					value="search" type="submit" onclick="return doSearch();"></td>
				<td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif"
					height="1" width="1"></td>
			</tr>
			<tr>
				<td colspan="3">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"
								width="1"></td>
						</tr>
					</tbody>
				</table>
				</td>
			</tr>
		</tbody>
	</table>
	<Br>
	<table border="0" cellpadding="0" cellspacing="0">
		<tbody>
			<tr>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
				<td width="50"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
				<td width="200"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
<c:forEach items="${pilotView.pilotNames}" var="colum">
					<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
					<td width="75"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
</c:forEach>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			</tr>
			<tr class="tablehead">
				<td class="tableheadTD1"
					colspan="${5 + 2 * pilotView.pilotTableSize}"><b>Contract</b></td>

			</tr>
			<tr class="tablesubhead">
				<td class="databorder" height="28"><img src="/assets/unmanaged/images/s.gif"
					height="1"></td>
				<td width="50" valign="top"><strong>Contract number </strong></td>
				<td valign="top" class="dataheaddivider"><img
					src="/assets/unmanaged/images/s.gif" height="1"></td>

				<td width="200" valign="top"><strong> Contract name</strong></td>
<c:forEach items="${pilotView.pilotNames}" var="colum">
					<td valign="top" class="dataheaddivider"><img
						src="/assets/unmanaged/images/s.gif" height="1"></td>
					<td width="75" valign="top">
<div align="center"><strong>${colum}</strong></div>
					</td>
</c:forEach>
				<td valign="top" class="databorder"><img src="/assets/unmanaged/images/s.gif"
					height="1"></td>

			</tr>
			<c:if test="${not empty pilotView.goodContractNumber}">
				<tr class="datacell1">
					<td class="databorder" height="28"><img src="/assets/unmanaged/images/s.gif"
						height="1"></td>
<td width="50">${pilotView.contractNumber}</td>

					<td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
<td width="200">${pilotView.contractName}</td>

					<td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
<c:set var="ptable" value="${pilotView.pilotTable}"/>

  <c:forEach items="${pilotView.pilotNames}" var="colum">
						 <td width="75">
						<!--registering form elements in javascript when page is rendering -->
<div align="center">

<%
Map map = (HashMap)pageContext.getAttribute("ptable");
String colum = (String)pageContext.getAttribute("colum");
Boolean val = (Boolean)map.get(colum);
pageContext.setAttribute("pilotValue", val);
%>

<%
Boolean pilotValue = (Boolean)pageContext.getAttribute("pilotValue");

%>

<input type="checkbox" name="${colum}" 
							<% if (pilotValue.booleanValue()){%> value="true" <%} else {%>
							value="false" <%}%> <% if (pilotValue.booleanValue()){%>
							checked="checked" <%}%> >  
							
						</div>
						</td>
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td> 
</c:forEach> 
				</tr>
			</c:if>
			<tr>
				<td align="right" class="boxborder"><img src="/assets/unmanaged/images/s.gif"
					height="1"></td>
				<td colspan="7" align="right" class="boxborder"><img
					src="/assets/unmanaged/images/s.gif" height="1"></td>
				<td align="right" class="boxborder"><img src="/assets/unmanaged/images/s.gif"
					height="1"></td>
			</tr>
		</tbody>
	</table>
	<Br>
	<Br>
	<c:if test="${not empty pilotView.goodContractNumber}">

		<table width="416" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="239"><input name="cancelBtn" class="button134"
					value="cancel" type="submit" onclick="return doCancel();"></td>
				<td width="252">
				<div align="right"><input name="saveBtn" class="button134"
					value="save" type="submit" onclick="return doSave();"></div>
				</td>
			</tr>
		</table>
	</c:if>
		<c:if test="${empty pilotView.goodContractNumber}">

		<table width="416" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="239"></td>
				<td width="252">
				<div align="right"><input name="backBtn" class="button134"
					value="back" type="submit"></div>
				</td>
			</tr>
		</table>
	</c:if>
	</td>
	</tr>
	</tbody>
	</table>
	<br>
	<img src="/assets/unmanaged/images/s.gif" height="1" width="20"></td>
</tr>
</tbody>
</table>
</ps:form>

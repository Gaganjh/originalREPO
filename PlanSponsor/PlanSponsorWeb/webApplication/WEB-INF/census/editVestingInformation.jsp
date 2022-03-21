<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<jsp:include flush="true" page="vestingInformationContent.jsp"/>
	
<SCRIPT language=javascript>

function isFormChanged() {
	return changeTracker.hasChanged();
}

registerTrackChangesFunction(isFormChanged);

if (window.addEventListener) {
	window.addEventListener('load', protectLinksFromCancel, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', protectLinksFromCancel);
}

</SCRIPT>
<content:errors scope="session"/>
<ps:form action="/do/census/editVestingInformation/" modelAttribute="vestingInformationForm" name="vestingInformationForm">
<form:hidden path="source"/>
<form:hidden path="action"/>
<form:hidden path="profileId"/>
<form:hidden path="sourceAsOfDate"/>
<form:hidden path="futurePlanYearEnd"/>
<input type="hidden" name="editMode" value="true"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <c:if test="${not empty param.printFriendly}">
      <tr>
        <td>
      	  <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
      	</td>
      </tr>

      <tr>
        <td>
      	  <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
      	</td>
      </tr>
      </c:if>
      
      <c:if test="${param.source == 'wd'}">
      <tr>
        <td>
      	  <content:getAttribute beanName="WithdrawalWarning" attribute="text"/>
      	</td>
      </tr>
      </c:if>
      <tr>
        <td>&nbsp;</td>
      </tr>
	</table>
					
	<content:getAttribute attribute="text" beanName="employeeSnapshotLink">
	<content:param>/do/census/viewEmployeeSnapshot/
?profileId=${vestingInformationForm.profileId}&source=<%=CensusConstants.VESTING_INFO_PAGE%>
		</content:param>
    </content:getAttribute>
					
	<br><br>
	<jsp:include flush="true" page="vestingInformation.jsp"/>
	<br><br>
	 
	<jsp:include flush="true" page="editVestingParamSection.jsp"/>
    <br><br>
     
	<c:if test="${empty param.printFriendly}">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr vAlign=top>
		<td width="50%">&nbsp;</td>
		<td align="left" width="25%">
			<input type="button" name="button1" value="cancel" class="button134" 
			onclick="doConfirmAndSubmit('cancel')"/>
		</td>
		<td align="right" width="25%">
		    <input type="button" name="button2" value="save" class="button134"  
		    onclick="doSubmit('save')"/>
		</td>
    </tr>
	</table>
	</c:if>


</ps:form>

<c:if test="${not empty param.printFriendly}">
<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
       type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
       id="globalDisclosure"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
	</tr>
</table>
</c:if>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>

<mrtl:noCaching/>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%> 

<!-- Main table for the Custom Query -->
<table>
<tr>
    <td align="left">

    <!-- Custom Query Header Label -->
    <table class="custom-query-criteria-table" style="width:96%!important" cellspacing="0">
        <tr>
            <td style="font-size:12px!important;text-align:left;">Create a Custom Query:</td>
            <td style="width:35px;padding:0px;">
                <a href="#anchor1" style="font-size:11px;" onclick="javascript:openPDF('/assets/pdfs/Custom_Query_help.pdf');">Help</a>
            </td>
        </tr>
    </table>
    
    <br/>

    <c:if test="${fn:length(fapForm.customQueryCriteriaList) == 25}" >
        <span> You have reached the maximum number of query rows </span> 
    </c:if> 

    <!-- this table holds the column header for the custom Query section -->
    <table class="custom-query-criteria-table" cellspacing="0" border="0">
    <!-- ROW 1: Header row -->
    <tr>
        <td class="custom_query_row_button_column"></td>
        <td class="custom_query_logic_column">Command</td>
        <td class="custom_query_bracket_column"><b>"(" </b></td>
        <td class="custom_query_fieldname_column">Field Name</td>
        <td class="custom_query_operator_column">Operator</td>
        <td class="custom_query_value_column">Value</td>
        <td class="custom_query_bracket_column"><b>")"</b></td>
        <td class="custom_query_row_button_column"></td>
    </tr>
    </table>
    <!-- This <DIV> is for the scrolling section -->
    <div class="custom_query_scroll" align="left">
    
        <!-- This is to be iterated to display multiple times -->
        <table class="custom-query-criteria-table" cellspacing="0"  border="0">

        <!-- iterate the ArrayList to create rows -->
<c:forEach items="${fapForm.customQueryCriteriaList}" var="customQueryCriteria" varStatus="rowIndex">
        <tr>
            
            <!-- For the '+' symbol -->
            <td class="custom_query_row_button_column" nowrap="nowrap">
<img class="custom-query-row-button" onclick="insertOrRemoveRows('insert' + ${rowIndex.index})" src="/assets/unmanaged/images/query_plus.png"/>
            </td>
            
            <!-- For the 'Logic' option -->
            <td class="custom_query_logic_column">
            <c:if test="${rowIndex.index  > 0}">
                <c:set var="dropDownList" value="${fapForm.customQueryOptionList['logic']}" />

<form:select path="fapForm.customQueryCriteriaList[${rowIndex.index}].logic" cssClass="custom_query" id="customQueryCriteria" cssStyle="width:64px"><%--  indexed="true"   --%>
                    <form:options items="${dropDownList}" itemValue="value" itemLabel="label"/>
</form:select>
            </c:if>
            </td>
            
            <!-- For the '(' -->
            <td class="custom_query_bracket_column">
<form:input path="fapForm.customQueryCriteriaList[${rowIndex.index}].leftBracket"  onchange="restrictCharacters(this, '(')" cssStyle="width:20px" id="leftBracket" /><%-- form:input - indexed="true" name="customQueryCriteria" --%>
            </td>
            
            <!-- For the 'Filed Name' -->
            <td class="custom_query_fieldname_column">
                <!-- The Field selection drop-down has values grouped by Performance, Fees, Portfolio Statistics, Fund Identifier -->
<form:select path="fapForm.customQueryCriteriaList[${rowIndex.index}].fieldId" cssClass="custom_query" onchange="toggleCustomQueryValue(this);" cssStyle="width:211px"><%--  indexed="true"  --%>
                <c:forEach var="fieldOption" items="${fapForm.customQueryOptionList['fields']}">
                    <c:choose>
                        <c:when test="${fieldOption.value == -2}">
                            <form:option style="background:black;color:white;" value="${fieldOption.value}" >
                           		${fieldOption.label}
                            </form:option>
                        </c:when>
                        <c:when test="${fieldOption.value != 'N/A'}">
                            <form:option value="${fieldOption.value}">${fieldOption.label}</form:option>
                        </c:when>
                        <c:otherwise>
                            <form:option value="${fieldOption.value}">${fieldOption.label}</form:option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
</form:select>
            </td>
            
            <!-- For the 'Operator' -->
            <td class="custom_query_operator_column">
                
                <c:set var="datType" value="${customQueryCriteria.dataTypeForSelectedField}"/>
<form:select path="fapForm.customQueryCriteriaList[${rowIndex.index}].operator" cssClass="custom_query" cssStyle="width:80px"><%--  indexed="true"   --%>
                <c:choose>
                    <c:when test="${!empty datType}">
                        <c:set var="dropDownList" value="${fapForm.customQueryOptionList[datType]}" />
                        <form:options items="${dropDownList}" itemValue="value" itemLabel="label"/>
                    </c:when>
                    <c:otherwise>
                        <form:option value="N/A">-</form:option>
                    </c:otherwise>
                </c:choose>
</form:select>
                
            </td>

            <!-- For the 'Value' -->
            <td class="custom_query_value_column">
                <!-- Text box to enter the value related to the field chosen -->
                <!-- Drop-down to be populated dynamically, if the field chosen is 'Risk category' -->
                <c:choose>
                    <c:when test="${customQueryCriteria.fieldId == '49'}" >
						<form:select path="fapForm.customQueryCriteriaList[${rowIndex.index}].value" cssStyle="width:235px">
							<form:options items="${fapForm.riskCategoryList}" itemLabel="label" itemValue="value"/>
						</form:select>
                    </c:when>
                    <c:when test="${customQueryCriteria.fieldId == '50'}" >
						<form:select path="fapForm.customQueryCriteriaList[${rowIndex.index}].value" cssStyle="width:235px">
							<form:options items="${fapForm.assetClassList}" itemLabel="label" itemValue="value"/>
						</form:select>
                    </c:when>
                    <c:when test="${customQueryCriteria.fieldId == '19'}" >
						<form:select path="fapForm.customQueryCriteriaList[${rowIndex.index}].value" cssStyle="width:235px">
							<form:options items="${fapForm.overallstarRatingList}" itemLabel="label" itemValue="value"/>
						</form:select>
                    </c:when>
                   <c:when test="${customQueryCriteria.fieldId == '20'}" >
						<form:select path="fapForm.customQueryCriteriaList[${rowIndex.index}].value" cssStyle="width:235px">
							<form:options items="${fapForm.morningStarRatingEvaluationList}" itemLabel="label" itemValue="value"/>
						</form:select>
                    </c:when>
                    <c:when test="${customQueryCriteria.fieldId == '21'}" >
						<form:select path="fapForm.customQueryCriteriaList[${rowIndex.index}].value" cssStyle="width:235px">
							<form:options items="${fapForm.fi360EvaluationList}" itemLabel="label" itemValue="value"/>
					</form:select>
                    </c:when>
                    <c:when test="${customQueryCriteria.fieldId == '22'}" >
						<form:select path="fapForm.customQueryCriteriaList[${rowIndex.index}].value" cssStyle="width:235px">
							<form:options items="${fapForm.rpagEvaluationList}" itemLabel="label" itemValue="value"/>
						</form:select>
                    </c:when>
                    <c:otherwise>
<form:input path="fapForm.customQueryCriteriaList[${rowIndex.index}].value" maxlength="30" cssStyle="width:235px" id="value" />
                    </c:otherwise>
                </c:choose>
            </td>
            
            <!-- For the ')' -->
            <td class="custom_query_bracket_column">
<form:input path="fapForm.customQueryCriteriaList[${rowIndex.index}].rightBracket" onchange="restrictCharacters(this, ')')" cssStyle="width:20px" id="rightBracket" /><%--  form:input - indexed="true" name="customQueryCriteria" --%>
            </td>

            <!-- For the '-' symbol -->
            <td class="custom_query_row_button_column" nowrap="nowrap">
<img class="custom-query-row-button" onclick="insertOrRemoveRows('remove' + ${rowIndex.index})" src="/assets/unmanaged/images/query_minus.png"/>
            </td>
        </tr>
</c:forEach>

        <tr>
            
            <!-- For the '+' symbol -->
            <td class="custom_query_row_button_column" nowrap="nowrap">
                <span style="width:9px;" onclick="insertOrRemoveRows('insertAtLast')">
                    <img style="padding:0px;" src="/assets/unmanaged/images/query_plus.png"/>
                </span>
            </td>

        </tr>
        </table>
    </div>
	<br/>
	
    <div style="width:220px;float:right;">
		<!-- Clear Button -->
		<input type="button" value="Clear" onclick="loadOptionsList('saveQueryName')"/>

		<!-- Cancel Button -->
		<input type="button" value="Cancel" onclick="startNewQuery()"/>

		<!-- View Results Button -->
		<input type="button" value="View Results" onclick="doCustomQuery()"/>
    </div>
    <br/><br/><br/>
    
    <!-- This <DIV> is for the 'save' option -->
<c:if test="${userProfile.internalUser !=true}">
	<div style="background-color:#E0E0E0;width:700px;height:30px;padding-top:4px;font-size:11px;border:1px solid #606060;">
		<table>
		  <tr>
			<td>(Optional) Save the above query as: </td>
<td><form:input path="fapForm.saveQueryName" maxlength="60" cssStyle="font-size:11px;width:100px;" id="saveQueryName" /></td>
			<td>
				<span class="button_search">
					<input type="button" id="saveCustomQuery" value="Save" style="font-size:11px;width:50px;" onclick="doSaveUserData(false)"/>
				</span>
				Saved query parameters will appear under "My Custom Queries".
			</td>
		 </tr>
	   </table>
	</div>
</c:if>
    </td>
</tr>
</table>

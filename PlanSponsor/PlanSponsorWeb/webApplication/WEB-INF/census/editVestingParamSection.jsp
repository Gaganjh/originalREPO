<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups" %>
<%@ page import="com.manulife.pension.ps.web.census.util.VestingHelper" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVYOSInfo"%>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  <%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeFullyVestedIndicatorInfo"%>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>





<c:if test="${vestingInformationForm.fullyVestedInd =='Y'}">
<c:set var="fullyVestedEffDateStyle" value="DISPLAY:block" />
</c:if>
<c:if test="${vestingInformationForm.fullyVestedInd =='N'}">
<c:set var="fullyVestedEffDateStyle" value="DISPLAY:none" />
</c:if>

	
	
	<!----------------- Section 3 begins ------------------------->
	 <table class="box" cellSpacing="0" cellPadding="0" border="0" width="100%">  			
	  <tr class="tablehead">
    	<td class="tableheadTD1">
      		<A HREF="javascript:toggleSection('param')"><IMG ID="paramIcon" SRC="/assets/unmanaged/images/minus_icon.gif" BORDER="0"/></A>&nbsp;
      		<B>Provided vesting information and employment status history</B><!-- <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>  -->
    	</td>
      </tr>    			
    </table>
    
	 <DIV id="param" style="DISPLAY: block">
     <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td width="138" class="tablesubhead"><b>Plan year end</b></td>
          <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td width="400" class="tablesubhead"><b>Provided vested years of service</b></td>
          <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td width="50">&nbsp;</td>
          <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td class="tablesubhead" nowrap="nowrap"><b>Employment status history</b></td>
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        </tr>
        <tr>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td colspan="3" align="left" valign="top">
          
          <jsp:setProperty name="rowStyle" property="start" value="1"/>
		  <jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>
		  
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
  <c:set var="vyosParam" value="${requestScope.vestingInfo}"/>  
<c:set var="vyosParamList" value="${vyosParam.vestingParamVYOS.vyosList}" />

          
          <c:if test="${not empty vyosParamList}">
<c:forEach items="${vyosParamList}" var="vyosParam" varStatus="paramIndex">
<c:set var="paramIndex" value="${paramIndex.index}"/>
<%String paramIndex = pageContext.getAttribute("paramIndex").toString(); %>
            <tr class="<c:out value="${rowStyle.next}"/>">
              <td width="135"><fmt:formatDate value="${vyosParam.effectiveDate}" pattern="MMMMM dd, yyyy"/></td>
              <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="400">
              	<table width="100%" border="0" cellpadding="0" cellspacing="0">
              	<tr>
              		<td width="10%">
<form:input path="vyos[${paramIndex}]" maxlength="2" onchange="return validateVestedYrsOfService(this);" size="3" cssStyle="boxbody"/>


<form:hidden path="vyosDate[${paramIndex}]" />
<form:hidden path="clonedForm.vyos[${paramIndex}]" />
					</td>
					<td width="5%"></td>
              		<td width="85%">${VestingHelper.getVestingAuditInfo(vyosParam,userProfile,VestingType.VYOS)}</td>
              		<ps:trackChanges name="vestingInformationForm" escape="true" property='<%= "vyos[" + paramIndex + "]" %>'/>
              	</tr>
              	</table>
              </td>
            </tr>
</c:forEach>
</c:if>
          
              <tr class="datacell1">
                <td width="135" class="tablesubhead"><b>Apply 100% vesting?</b></td>
                <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="400" class="tablesubhead"><b>Effective Date</b></td>
              </tr>
      
<c:set var="fullyVestedVO" value="${requestScope.vestingInfo.vestingParamFullyVested}" />


<c:set var="fullyVestedParam" value="${fullyVestedVO.fullyVestedIndicator}" />


              <c:if test="${not empty fullyVestedParam}">
              <tr class="datacell1">
                <td class="highlight">  
<form:radiobutton onclick="fullyVestedChanged('Y')" path="fullyVestedInd" value="Y"/>
					Yes
<form:radiobutton onclick="fullyVestedChanged('N')" path="fullyVestedInd" value="N"/>
					No

	           	    <ps:trackChanges name="vestingInformationForm" escape="false" property='fullyVestedInd'/>
                </td>
                <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="400">
	                <table width="100%" border="0" cellpadding="0" cellspacing="0">
	              	<tr>
	              		<td width="30%">
	              			<div id="fullyVestedEffDate" style="<c:out value='${fullyVestedEffDateStyle}'/>" >
<form:input path="fullyVestedEffectiveDate" maxlength="10" onblur="handleFullyVestedEffectiveDateBlur()" onchange="validateFullyVestedEffDate(this)" onfocus="handleFullyVestedEffectiveDateFocus()" size="10" cssClass="inputAmount" id="fullyVestedEffectiveDate"/>
&nbsp;<a
				              href="javascript:handleFullyVestedEffectiveDateCalendar()"
				              onfocus="handleFullyVestedEffectiveDateFocus();"
				              onblur="handleFullyVestedEffectiveDateCalendarBlur();"
				            ><img src="/assets/unmanaged/images/cal.gif" border="0"></a>
						  	</div>
						</td>
						<td width="5%"></td>
	              		<td width="65%">
	              			<div id="fullyVestedAudit" style="<c:out value='${fullyVestedEffDateStyle}'/>" >
	              			${VestingHelper.getVestingAuditInfo(fullyVestedParam,userProfile,VestingType.FULLY_VESTED_IND)}
	              			</div>
	              		</td>
	              		<ps:trackChanges name="vestingInformationForm" escape="true" property='fullyVestedEffectiveDate'/>
	              	</tr>
	              	
	              	</table>
				</td>
              </tr>
              <c:if test="${vestingInformationForm.displayApplyLTPTCreditingField eq true}">
              	<tr class="datacell1">
                	<td width="135" class="tablesubhead"><b>Apply LTPT crediting?</b></td>
               		<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                	<td width="400" class="tablesubhead"></td>
              	</tr>
              	<tr class="datacell1">
                <td class="highlight">  
					<form:radiobutton path="ltptCrediting" value="Y"/>
						Yes
						<form:radiobutton path="ltptCrediting" value="N"/>
						No
                </td>
                <td> </td>
                <td> </td>
                </tr>
              </c:if>
</c:if>
          </table>
          </td>
          <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td width="50">&nbsp;</td>
          <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td align="left" valign="top" class="datacell1">
          
          <jsp:setProperty name="rowStyle" property="start" value="1"/>
		  <jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>
		  
          <table width="100%" border="0" cellpadding="0" cellspacing="0">   
            <tr class="datacell1">
              <td width="120"><strong>Status</strong></td>
              <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="69"><strong>Effective</strong></td>
            </tr>

          	
<c:set var="statusParamList" value="${requestScope.vestingInfo}"/>
<c:set var="statusParamList" value="${statusParamList.vestingParamStatus.fullStatusList}"/>

            <c:if test="${not empty statusParamList}">
<c:forEach items="${statusParamList}" var="statusParam" varStatus="idx">
          	<c:if test="${idx.index le '4'}">
            <tr class="<c:out value="${rowStyle.next}"/>">
              <td width="120"><span class="highlight">
              	<ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="statusParam" property="status"/>
              </span></td>
              <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="69"><span class="highlight">
              	<c:if test="${not empty statusParam.effectiveDate}">
                	<fmt:formatDate value="${statusParam.effectiveDate}" pattern="MM/dd/yyyy"/>
                </c:if></span></td>
            </tr>
            </c:if>
</c:forEach>
</c:if>
            
            <tr class="datacell2">
              <td colspan="3" class="boxborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
            </tr>
          </table>
          </td>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        </tr>
        <tr class="datacell1">
          <td colspan="8" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
        </tr>
    </table>
	</div>
    

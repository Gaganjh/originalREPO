<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups" %>
<%@ page import="com.manulife.pension.ps.web.census.util.VestingHelper" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo" %>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVYOSInfo" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeFullyVestedIndicatorInfo" %>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeStatusInfo" %>
<%@ page import="com.manulife.pension.service.employee.valueobject.ApplyLTPTCreditingInfo" %>
	<c:choose>
	  <c:when test="${param.printFriendly == true}">
	    <c:set var="expandStyle" value="DISPLAY:block"/>
	    <c:set var="expandIcon" value="/assets/unmanaged/images/minus_icon.gif"/>    
	  </c:when>
	  <c:otherwise>
	    <c:set var="expandStyle" value="DISPLAY:none"/>
	    <c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif"/>
	  </c:otherwise>
	</c:choose>

	<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
		<jsp:setProperty name="rowStyle" property="start" value="1"/>
		<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
	</jsp:useBean>

	<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>

	<!----------------- Section 3 begins ------------------------->
	  <table class="box" cellSpacing="0" cellPadding="0" border="0" width="100%">  			
	  <tr class="tablehead">
    	<td class="tableheadTD1">
    		<c:choose>
    		<c:when test="${!param.printFriendly}">
      		<A HREF="javascript:toggleSection('param')"><IMG ID="paramIcon" SRC="<c:out value='${expandIcon}'/>" BORDER="0"/></A>&nbsp;
      		</c:when>
      		</c:choose>
      		<B>Provided vesting information and employment status history</B><!-- <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>  -->
    	</td>
      </tr>    			
      </table>
    
	 <DIV id="param" style="<c:out value='${expandStyle}'/>">
     <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td width="135" class="tablesubhead"><b>Plan year end</b></td>
          <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td width="400" class="tablesubhead"><b>Provided vested years of service</b></td>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td width="50" class="datacell1">&nbsp;</td>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td class="tablesubhead" nowrap="nowrap"><b>Employment status history</b></td>
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
        </tr>
        <tr>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td colspan="3" align="left" valign="top">
          
          <jsp:setProperty name="rowStyle" property="start" value="1"/>
		  <jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>
		  
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <c:set var="vyosParamList" value="${vestingInfo.vestingParamVYOS.vyosList}"/>
          <c:if test="${not empty vyosParamList}">
          <c:forEach items="${vyosParamList}" var="vyosParam">
          <%EmployeeVYOSInfo vyosParam=(EmployeeVYOSInfo)pageContext.getAttribute("vyosParam"); %>
            <tr class="<c:out value="${rowStyle.next}"/>">
              <td width="135"><fmt:formatDate value="${vyosParam.effectiveDate}" pattern="MMMMM dd, yyyy"/></td>
              <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
              <td width="400">
              	<table width="100%" border="0" cellpadding="0" cellspacing="0">
              	<tr>
              		<td width="10%"><c:out value="${vyosParam.vyos}"/></td>
              		<td width="90%"><%=VestingHelper.getVestingAuditInfo(vyosParam,userProfile, VestingType.VYOS)%></td>
              	</tr>
              	</table>
              </td>
            </tr>
          </c:forEach>
          </c:if>
          
              <tr>
                <td width="135" class="tablesubhead"><b>Apply 100% vesting?</b></td>
                <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="400" class="tablesubhead"><b>Effective Date</b></td>
              </tr>
             
             <c:set var="fullyVestedVO" value="${vestingInfo.vestingParamFullyVested}"/> 
             <c:set var="fullyVestedParam" value="${fullyVestedVO.fullyVestedIndicator}"/>
          
              <c:if test="${not empty fullyVestedParam}">
              <%EmployeeFullyVestedIndicatorInfo fullyVestedParam=(EmployeeFullyVestedIndicatorInfo)pageContext.getAttribute("fullyVestedParam");%>
              <tr class="datacell1">
                <td class="highlight">  
                <c:if test="${fullyVestedParam.fullyVested}">Yes</c:if>
                <c:if test="${!fullyVestedParam.fullyVested}">No</c:if>
                </td>
                <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="400">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
              	<tr>
              		<td width="20%">
              			<c:if test="${fullyVestedParam.fullyVested and not empty fullyVestedParam.effectiveDate}">
                		<fmt:formatDate value="${fullyVestedParam.effectiveDate}" pattern="MM/dd/yyyy"/>&nbsp;
                		</c:if>
                	</td>
              		<td width="80%">
              			<c:if test="${fullyVestedParam.fullyVested}">
              			<%=VestingHelper.getVestingAuditInfo(fullyVestedParam,userProfile,VestingType.FULLY_VESTED_IND)%>
              			</c:if>
              		</td>
              	</tr>
              	</table>
                </td>
              </tr>
              </c:if>
			<c:set var="applyLTPTCreditingInfo" value="${vestingInfo.applyLTPTCreditingInfo}"/>
			<%ApplyLTPTCreditingInfo applyLTPTCreditingInfo=(ApplyLTPTCreditingInfo)pageContext.getAttribute("applyLTPTCreditingInfo");  %>
			<c:if test="${vestingInformationForm.displayApplyLTPTCreditingField eq true}">
              <tr class="datacell1">
				<td width="135" class="tablesubhead"><b>Apply LTPT crediting?</b></td>
               	<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="400" class="tablesubhead"></td>
			  </tr>
			  <tr class="datacell1">
	              <td class="highlight">
						<c:if test="${requestScope.vestingInfo.applyLTPTCreditingInfo != null}">	
							<c:if test="${vestingInfo.applyLTPTCreditingInfo.applyLTPTCrediting == 'Y'}">Yes</c:if>	
							<c:if test="${vestingInfo.applyLTPTCreditingInfo.applyLTPTCrediting == 'N'}">No</c:if>
						</c:if>
						<c:if test="${empty applyLTPTCreditingInfo}">No</c:if>
				  </td>
				  <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                  <td width="400">
                  	<table width="100%" border="0" cellpadding="0" cellspacing="0">
              			<tr>
              				<td width="10%"></td>
              				<td width="90%">
								<%=VestingHelper.getVestingAuditInfoForLTPTCrediting(applyLTPTCreditingInfo, userProfile,VestingType.APPLY_LTPT_CREDITING)%>
              				</td>
              			</tr>
              		</table>
                  </td>
              </tr>
            </c:if>  
          </table>
          </td>
          <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
          <td width="50" class="datacell1">&nbsp;</td>
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
            
            <c:set var="statusParamList" value="${vestingInfo.vestingParamStatus.fullStatusList}"/>
            
          <c:if test="${not empty statusParamList}">
          <c:forEach items="${statusParamList}" var="statusParam" varStatus="idx">
          <c:set var="indexValue" value="${idx.index}"/> 
<%Integer temp = (Integer)pageContext.getAttribute("indexValue");
pageContext.setAttribute("temp",temp);
EmployeeStatusInfo statusParam =(EmployeeStatusInfo)pageContext.getAttribute("statusParam");
%>
          <c:if test="${temp le 4}">
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
          <td colspan="9" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
        </tr>
    </table>
	</div>
    
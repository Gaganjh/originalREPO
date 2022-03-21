<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.manulife.pension.ps.web.sendservice.NoticePlanDataForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.sendservice.util.PlanDataWebUtility"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="noticePlanCommonVO" value="${sessionScope.noticePlanCommonVO}" />


<div id="generalTabDivId" class="borderedDataBox">
  <div class="subhead">
    <c:if test="${not param.printFriendly}">
      <div class="expandCollapseIcons" id="generalShowIconId" onclick="expandDataDiv('general');">
        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
      </div>
      <div class="expandCollapseIcons" id="generalHideIconId" onclick="collapseDataDiv('general');">
        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
      </div>
    </c:if>
    <div class="sectionTitle">Summary</div>
    <div class="sectionHighlightIcon" id="generalSectionHighlightIconId">
      <ps:sectionHilight name="summary" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
    </div>
    <div class="endDataRowAndClearFloats"></div>
  </div>
 <div id="generalDataDivId">
	<div class="evenDataRow">
		<TABLE class=dataTable>
			<TR>
				<TD class=generalLabelColumn><ps:fieldHilight
						name="noticePlanCommonVO.planName" singleDisplay="true"
						className="errorIcon" displayToolTip="true" /> Plan Name:</TD>
<TD class=dataColumn><c:if test="${not empty noticePlanCommonVO.planName}">

						<p>
${noticePlanCommonVO.planName}
						</p>
</c:if>
 <c:if test="${empty noticePlanCommonVO.planName}">
						<p>Pending Plan Information Completion</p>
</c:if></TD>
			</TR>
		</table>
	</DIV>
	<DIV class=oddDataRow>
		<TABLE class=dataTable>
			<TR>
				<TD class=generalLabelColumn><ps:fieldHilight
						name="noticePlanCommonVO.planYearEnd.data" singleDisplay="true"
						className="errorIcon" displayToolTip="true" /> Plan Year End:
				</TD>
				<TD class=dataColumn>
				<c:choose>
					<c:when test="${noticePlanCommonVO.planYearEnd == null or noticePlanCommonVO.planYearEnd==''}">
						<p>Pending Plan Information Completion</p>
					</c:when>
					<c:otherwise>
<p>${noticePlanCommonVO.planYearEnd.data}</p>
					</c:otherwise>
				</c:choose>					  				
				</TD>
			</TR>
		</table>
	</DIV>
	<DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
			 <ps:fieldHilight name="noticePlanCommonVO.contractNumber" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Contract number:  
			</TD>
			<TD class=dataColumn>
<c:if test="${not empty noticePlanCommonVO.contractNumber}">
<p>${noticePlanCommonVO.contractNumber}</p>
</c:if>
<c:if test="${empty noticePlanCommonVO.contractNumber}">
				  <p>Pending Plan Information Completion</p>
</c:if>
			</TD>
        </TR>
       </table>
      </DIV>
      <DIV class=oddDataRow>
	     <TABLE class=dataTable>
	       <TR>
				<TD class=generalLabelColumn>				 
					Enrollment Access Number (EAN):
				</TD>
				<TD class=dataColumn>
<c:if test="${not empty noticePlanCommonVO.enrollmentAccessNumber}">
<p>${noticePlanCommonVO.enrollmentAccessNumber}</p>
</c:if>
<c:if test="${empty noticePlanCommonVO.contractNumber}">
						  <p/>
</c:if>
				</TD>
	       </TR>	       
	     </TABLE>
	  </DIV> 
	  <DIV class=evenDataRow>
      	<TABLE class=dataTable>
	        <TR>
				<TD class=generalLabelColumn>
					The contract has selected the following Notice: 
				</TD>
				<TD class=dataColumn>
<c:if test="${not empty noticePlanCommonVO.noticeType}">
<p>${noticePlanCommonVO.noticeType}</p>
</c:if>
<c:if test="${empty noticePlanCommonVO.noticeType}">
						  <p>SEND Service has not been selected.</p>
</c:if>
				</TD>
	        </TR>
        </table>
      </DIV>     
  </div>
         
</div>



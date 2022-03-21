<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.bob.valueobject.RiaFeeDetailsVO"%>
<%@ page import="com.manulife.pension.ps.service.report.bob.valueobject.RiaFeeRangeVO"%>
<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/security" prefix="security"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<content:contentBean contentId="<%=BDContentConstants.RIA_BLEND_FEE_TILTLE%>" 
   type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="riaBlendFeeTitle" />
<div id="riaFeeDeatilsdiv" class="ui-helper-hidden ui-state-highlight">
	<div style="background-color: #455660; border-top: #febe10 4px solid;">
		<ul class="proposal_nav_menu">
			<li>
				<a id="Requested_Printed_copies_sec" class="selected_link">
					<span style="padding-left: 10px">
					<content:getAttribute id="layoutPageBean" attribute="name" beanName="riaBlendFeeTitle" /> 
					</span>
				</a>
			</li>
			<div
				style="text-align: right; padding-right: 15px; padding-top: 10px;">
				<a href="javascript:doCloseOverlay()"><img class="cancel"
					height="15" width="15" src="/assets/unmanaged/images/close.gif" /></a>
			</div>
		</ul>
	</div>
	<table class="report_table_content" style="border-bottom: 0px;">

		<tbody>
<c:forEach items="${blockOfBusinessForm.riaFeeDetailsVO}" var="riaFeeDetailsList" varStatus="rowIndex" >


				<tr class="spec">
					<td colspan="4" class="name"><strong>Blended Rate
							(previous month end)</strong></td>
					<c:choose>
						<c:when test="${(empty riaFeeDetailsList.riaBlendRate) or (riaFeeDetailsList.riaBlendRate lt 0)}">
							<td colspan="5" class="name"> - </td>
						</c:when>
						<c:otherwise>
							<td colspan="5" class="name"><report:number
									property="riaFeeDetailsList.riaBlendRate"
									pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3"
									type="c" sign="false" />%</td>
						</c:otherwise>
					</c:choose>
				</tr>
</c:forEach>
			<tr>
				<td colspan="4" class="name">&nbsp;</td>
				<td colspan="5" class="name">&nbsp;</td>
			</tr>
			
			<tr class="spec">
				<td colspan="4" class="name"><strong>Scale Information</strong></td>
				<td colspan="5" class="name">&nbsp;</td>
			</tr>


			<tr class="spec">
				<td colspan="2" class="name"><strong>Band Start ($)</strong></td>
				<td colspan="2" class="name"><strong>Band End ($)</strong></td>
				<td colspan="5" class="name"><strong>Band Rate (%)</strong></td>
			</tr>
<c:if test="${empty blockOfBusinessForm.riaFeeRangeVO}">

				<tr> <td colspan="5">No Blend Information available</td> </tr>
</c:if>

<c:if test="${not empty blockOfBusinessForm.riaFeeRangeVO}">
<c:forEach items="${blockOfBusinessForm.riaFeeRangeVO}" var="riaFeeRangeList" varStatus="rowIndex" >



					<tr>
						<c:choose>
							<c:when test="${rowIndex % 2 == 0}">
								<tr class="">
							</c:when>
							<c:otherwise>
								<tr class="spec">
							</c:otherwise>
						</c:choose>
						<td colspan="2"><report:number
								property="riaFeeRangeList.minAmt"
								pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>" scale="3"
								type="c" sign="false" /></td>
						<td colspan="2"><report:number
								property="riaFeeRangeList.maxAmt"
								pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>" scale="3"
								type="c" sign="false" />
						<td colspan="5"><report:number
								property="riaFeeRangeList.bandRate"
								pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3"
								type="c" sign="false" /></td>
					</tr>
</c:forEach>
</c:if>
		</tbody>
	</table>
</div>

<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ page import="com.manulife.pension.ps.service.report.bob.valueobject.RiaFeeDetailsVO"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/security" prefix="security"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<content:contentBean contentId="<%=BDContentConstants.RIA_BPS_FEE_TILTLE%>" 
   type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>" id="riaBpsFeeTitle" />
<jsp:useBean id="blockOfBusinessForm" scope="session" type="com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm" /> 
<div id="riaFeeDeatilsdiv" class="ui-helper-hidden ui-state-highlight">
	<div style="background-color: #455660; border-top: #febe10 4px solid;">
		<ul class="proposal_nav_menu">
			<li>
				<a id="Requested_Printed_copies_sec" class="selected_link">
					<span style="padding-left: 10px"> 
							<content:getAttribute id="layoutPageBean" attribute="name" beanName="riaBpsFeeTitle" />
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

<c:forEach items="${blockOfBusinessForm.riaFeeDetailsVO}" var="riaFeeDetailsList" >

		<table class="report_table_content" style="border-bottom: 0px;">


			<tr class="spec">
				<td width="25%" class="name"><strong>Annualized
						Percentage</strong></td>
				<c:choose>
					<c:when test="${riaFeeDetailsList.riaBpsPercentage < 0}">
						<td width="35%" class="name"> - </td>
					</c:when>
					<c:otherwise>
						<td width="35%" class="name"><report:number
								property="riaFeeDetailsList.riaBpsPercentage"
								pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3"
								type="c" sign="false" />%</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td width="25%" class="name"><strong>Annual Contract
						Dollar Cap</strong></td>
				<c:choose>
					<c:when test="${riaFeeDetailsList.riaBpsMaxAmt < 0}">
						<td width="35%" class="name"> - </td>
					</c:when>
					<c:otherwise>
						<td width="35%" class="name">$<report:number
								property="riaFeeDetailsList.riaBpsMaxAmt"
								pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>" scale="3"
								type="c" sign="false" />
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
</c:forEach>
</div>

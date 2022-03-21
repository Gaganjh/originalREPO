<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO" %>
        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>


<%@page import="com.manulife.util.render.RenderConstants"%><un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>

<c:set var="details" value="${requestScope.details}" scope="page" />



<content:contentBean contentId="<%=BDContentConstants.FIXED_FOOTNOTE_PBA_FOR_PARTICIPANT_REPORTS%>" type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />
<report:formatMessages scope="request"/>
<!-- Include common -->
<jsp:include page="/WEB-INF/participant/participantAccountCommon.jsp"
	flush="true" />

<script type="text/javascript">
	// This function is implementedto be executed during onLoad.
	function doOnload() {
		scroll(0, 650);
	}
</script>

<input type="hidden" name="pdfCapped" /><%--  input - name="participantAccountForm" --%>
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
<c:if test="${participantAccountForm.hasInvestments ==true}">

		<div  class="report_table">
<c:if test="${details.netEEDeferralContributionsAvailable ==true}">
			<table class="report_table_content">
				<tbody>
					<tr class="spec">
						<td colspan="2" width="918" class="sub"><content:getAttribute
							id="layoutPageBean" attribute="body1" /> <br>
						<br>
						</td>
					</tr>
					<tr>
						<td width="300"><strong>Net employee contribution</strong><br>
						</td>
						<td width="618">$<report:number
							property="details.netEEDeferralContributions" defaultValue=""
							pattern="###,###,##0.00" /><br>
						</td>
					</tr>
				
			<tr class="spec">
						<td width="300"><strong>Maximum hardship amount</strong><br>
						</td>
						<td width="618">$<report:number
							property="details.maximumHardshipAmount" defaultValue=""
							pattern="###,###,##0.00" /><br>
						</td>
					</tr>
			<!-- Deferrals section -->
			
					<tr class="spec">
						<td width="300"><strong>Current Deferral on File</strong><br>
						</td>
<td width="618">${participantAccountForm.deferralContributionText} <%-- filter="false" --%>

						<br>
						</td>
					</tr>
			


			<!-- EZincrease Section -->
			
			
			<logicext:if name="details" property="participantDeferralVO.auto" op="equal" value="true">
	     		<logicext:or name="details" property="participantDeferralVO.signUp" op="equal" value="true" />
	      		<logicext:then>
	       	
	      		
				<!-- If EZincrease is OFF in CSF, then display the status is OFF and suppress the other attributes-->
<c:if test="${details.participantDeferralVO.participantACIOn ==false}">
								<tr >
									<td width="300" class="name"><strong>Scheduled deferral increase</strong></td>
									<td width="618" class="name">Off</td>
								</tr>
							
</c:if>
				
				<!-- If EZincrease is ON in CSF, then display the attributes -->
<c:if test="${details.participantDeferralVO.participantACIOn !=false}">
					
							<!--  EZincrease label -->
							<tr >
								<td class="name" colspan="2"><strong><u>
								 Scheduled deferral increase
								 </u></strong></td>
							</tr>

							<!--  Date of next increase -->
							<tr class="spec">
								<td width="300" class="name"><strong>Date of Next
								Increase</strong></td>
								<td width="618" class="name">
								<render:date property="details.participantDeferralVO.dateOfNextIncrease" patternOut="${renderConstants.MEDIUM_MDY_SLASHED}" patternIn="${renderConstants.EXTRA_LONG_MDY}" />
								</td>
							</tr>

							<!--  Next increase-->
							<tr >
								<td width="300" class="name"><strong>Next Increase</strong></td>
<td width="618" class="name">${participantAccountForm.nextIncreaseValue} <%-- filter="false" --%></td>

							</tr>

							<!--  Personal contribution limit -->
							<tr class="spec">
								<td width="300" class="name"><strong>Personal rate
								limit</strong></td>
<td width="618" class="name">${participantAccountForm.personalRateLimit} <%-- filter="false" --%></td>

							</tr>
</c:if>

			    </logicext:then>
	      
	   		 </logicext:if>
             
           </tbody>
         </table>
</c:if></div>
</c:if>
</c:if>

<!-- FootNotes and Disclaimer -->
<div class="footnotes">

<c:if test="${participantAccountForm.asOfDateCurrent and participantAccountForm.showManagedAccount }">
			<content:contentBean
				contentId="<%=BDContentConstants.MA_FOOTNOTE%>"
				type="<%=BDContentConstants.TYPE_DISCLAIMER%>" id="participantMAFootnote" />
			<p class="footnote"><content:getAttribute id="participantMAFootnote" attribute="text" /></p>
</c:if>

<c:if test="${participantAccountForm.showPba ==true}">
			  <dl><dd><content:getAttribute beanName="footnotePBA" attribute="text"/></dd></dl>
</c:if>
		<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
        <dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
		<div class="footnotes_footer"></div>
</div> 
<!--end of footnotes-->

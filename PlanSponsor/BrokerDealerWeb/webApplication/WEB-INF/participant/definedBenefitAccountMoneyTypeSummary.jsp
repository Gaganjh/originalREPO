<%-- Tag Libraries used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<report:formatMessages scope="request"/>
<%-- Constant Files used--%>
<un:useConstants var="bdConstants" className="com.manulife.pension.bd.web.BDConstants" />

<%-- Java scripts used --%>
<script type="text/javascript" >
	// This function is implemented to be executed during onLoad.
	function doOnload() {
		scroll(0, 650);
	}
</script>

<input type="hidden" name="pdfCapped" /><%--  tag input - name="participantAccountForm" --%>

<%-- Include common --%>
<jsp:include page="/WEB-INF/participant/definedBenefitAccountCommon.jsp" flush="true" />

<c:if test="${participantAccountForm.asOfDateCurrent ==true}">

	<%-- Report Table Column Headers --%>
	<table width="918" class="report_table_content">
		<thead>
			<tr>
				<th width="304" class="val_str">Money Types</th>
				<th width="612" class="val_str">
				<div align="right">Total Assets($)</div>
				</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>

<c:if test="${participantAccountForm.hasInvestments ==false}">

		<div class="report_table">
		<table width="918" class="report_table_content">
			<tr class="spec">
				<td colspan="3"><content:getAttribute
					id="NoParticipantsMessage" attribute="text" /></td>
			</tr>
		</table>
		</div>
</c:if>

<c:if test="${participantAccountForm.hasInvestments ==true}">
		<div class="report_table">
		<table width="918" class="report_table_content">
			<tbody>
			
			    <tr class="spec">
					<td  colspan="2">&nbsp;</td>
				</tr>
			
				<%-- Employer contributions  --%>
				<tr class="">
					<td width="306" class="val_num_cnt">
                        <strong>Employer contributions</strong>
                    </td>
					<td width="612" class="cur">
					     <report:number property="account.totalEmployerContributionsAssets"
						                defaultValue=""
						                pattern="${bdConstants.AMOUNT_FORMAT_TWO_DECIMALS}" />
                    </td>
				</tr>

<%-- <c:set var="theEmployerMoneyTypeTotals" value="${account.employerMoneyTypeTotals}"/>



				<c:set var="emloyerMoneyTypeSize" value="account.employerMoneyTypeTotals"/> --%>
				<c:if test="${not empty account.employerMoneyTypeTotals}">
<% int theIndexs=0; %>
<c:forEach items="${account.employerMoneyTypeTotals}" var="employerMoneyTypeTotals" varStatus="theIndex" >




                       <%
					if (theIndexs++ % 2 == 0) {
				%>
						 <tr class="spec">
					  <%
						} else {
					%>
						 <tr>
					  <%
						}
					%>	
							<td width="306" class="val_num_cnt">
${employerMoneyTypeTotals.moneyTypeName}
                            </td>
							<td width="612" class="cur">
							    <report:number property="employerMoneyTypeTotals.balance"
							                   defaultValue=""
								               pattern="${bdConstants.AMOUNT_FORMAT_TWO_DECIMALS}" />
                            </td>
						</tr>
</c:forEach>
</c:if>

                 <c:if test="account.employerMoneyTypeTotals % 2 eq 0"> 
                      <c:set var="classNextRow" value="" scope="page"/>
                      <c:set var="classFirstRow" value="spec" scope="page"/>
                </c:if>
                <c:if test="account.employerMoneyTypeTotals % 2 ne 0"> 
                      <c:set var="classNextRow" value="spec" scope="page"/>
                      <c:set var="classFirstRow" value="" scope="page"/>
                </c:if> 

				<tr class="${classFirstRow}">
					<td  colspan="2">&nbsp;</td>
				</tr>

				<tr class="${classNextRow}">
					<td width="306" class="val_num_cnt">
                        <strong>Total</strong>
                    </td>
					<td width="612" class="cur">
					    <report:number property="account.totalContributionsAssets" 
					                   defaultValue=""
						               pattern="${bdConstants.AMOUNT_FORMAT_TWO_DECIMALS}" />
                     </td>
				</tr>
			</tbody>
		</table>
		</div>
</c:if>
</c:if>

<!-- FootNotes and Disclaimer -->
<layout:pageFooter/>
<!--end of footnotes-->

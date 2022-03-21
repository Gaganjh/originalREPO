<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_ACCOUNT_BALANCE_PBA_AND_LOAN_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="accountBalanceFootnotePbaAndLoan"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_ACCOUNT_BALANCE_PBA_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="accountBalanceFootnotePbaOnly"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_1_2_ACCOUNT_BALANCE_LOAN_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="accountBalanceFootnoteLoanOnly"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<c:forEach items="${layoutPageBean.footnotes}" var="footnote" varStatus="footnoteStatus">
  	<tr>
  		<td><content:getAttribute beanName="footnote" attribute="text"/></td>
  	</tr>
 	</c:forEach>
 	<tr>
 		<td>
 			<c:if test="${withdrawalForm.withdrawalRequestUi.hasBothPbaAndLoans}">
 				<content:getAttribute beanName="accountBalanceFootnotePbaAndLoan" attribute="text" />
 			</c:if>
 			<c:if test="${withdrawalForm.withdrawalRequestUi.hasPbaOnly}">
 				<content:getAttribute beanName="accountBalanceFootnotePbaOnly" attribute="text" />
 			</c:if>
 			<c:if test="${withdrawalForm.withdrawalRequestUi.hasLoansOnly}">
 				<content:getAttribute beanName="accountBalanceFootnoteLoanOnly" attribute="text" />
 			</c:if>
 		</td>
 	</tr>
	<c:forEach items="${layoutPageBean.disclaimer}" var="disclaimer" varStatus="disclaimerStatus">
  	<tr>
  		<td><content:getAttribute beanName="disclaimer" attribute="text"/></td>
  	</tr>
 	</c:forEach>
</table>
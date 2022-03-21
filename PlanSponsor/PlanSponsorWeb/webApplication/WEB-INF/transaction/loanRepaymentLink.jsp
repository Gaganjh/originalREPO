<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- This page is included by the Loan Repayment Details page 
     It is already guarded by the printfriendly tag. --%>
<a href="/do/transaction/loanSummaryReport/">Current loan summary</a><br>

<ps:map id="parameterMap">
	<ps:param name="profileId" valueBeanName="loanRepaymentDetailsReportForm" valueBeanProperty="profileId"/>
</ps:map>
<ps:link action="/do/transaction/participantTransactionHistory/" name="parameterMap">Participant Transaction History
</ps:link><br>			

<a href="/do/transaction/transactionHistoryReport/">Transaction History</a>

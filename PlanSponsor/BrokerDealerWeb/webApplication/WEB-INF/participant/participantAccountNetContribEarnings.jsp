<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<content:contentBean contentId="<%=BDContentConstants.FIXED_FOOTNOTE_PBA%>" 
					 type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" 
					 id="footnotePBA" />

<content:contentBean contentId="<%=BDContentConstants.PS_PARTICIPANT_ACCOUNT_EARNINGS_FOOTNOTE%>"
                     type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>"
                     id="footnoteEarnings"/>

<content:contentBean contentId="<%=BDContentConstants.SHOW_NONROTH_HEADER%>" 
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="showNonRothHeadertext" />
					 
<content:contentBean contentId="<%=BDContentConstants.SHOW_ROTH_HEADER%>" 
					 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="showRothHeadertext" />
					 
<content:contentBean contentId="<%=BDContentConstants.MULTIPLE_ROTH_FOOTNOTES%>" 
					 type="<%=BDContentConstants.TYPE_PAGEFOOTNOTE%>" 
					 id="multipleRoth" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>
<report:formatMessages scope="request"/>
<!-- Include common -->  
<jsp:include page="/WEB-INF/participant/participantAccountCommon.jsp" flush="true" />

<script type="text/javascript">   
		// This function is implementedto be executed during onLoad.
	function doOnload() {
		scroll(0, 650);
	}
</script>
 
<input type="hidden" name="pdfCapped" /><%--  input - name="participantAccountForm" --%>

<c:if test="${participantAccountForm.asOfDateCurrent ==true}">

<div class="report_table">
<table width="918" class="report_table_content">
 	<thead>
   	<tr>
   		<th width="304" class="val_str">After Tax Money Types</th>
   		<th width="309" class="cur align_center">Net Contributions($)</th>
   		<th width="309" class="cur align_center">Earnings*($)</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>

<c:set var="theNetContribEarningsDetails" value="${account.netContribEarningsDetailsCollection}"/>
<c:if test="${participantAccountForm.showNonRothHeader ==true}">
<table width="918" class="report_table_content">
    <tbody>

   	    <c:if test="${theIndex % 2 eq 0}"> 
	          <tr class="spec">
	    </c:if>
		<c:if test="${theIndex % 2 ne 0}"> 
			  <tr>
		</c:if>	
			<tr><th class="val_str" colspan="3">
		<content:getAttribute beanName="showNonRothHeadertext" attribute="text"/></th></tr>
		
<c:forEach items="${theNetContribEarningsDetails}" var="theItem" varStatus="theIndex" >

<c:if test="${theItem.nonRothMoneyTypeInd ==true}">
   		<td width="300" class="val_str">
${theItem.moneyTypeName}
   		</td>
   		
   		<td width="309" class="cur">
   			<report:number property="theItem.netContributions" defaultValue = "" pattern="###,###,##0.00;(###,###,##0.00)"/>
   		</td>
   		
   		<td width="309" class="cur">
   			<report:number property="theItem.earnings" defaultValue = "" pattern="###,###,##0.00;(###,###,##0.00)"/>
   		</td>
</c:if>
    </tr>
</c:forEach>
    </tbody>
</table>
</c:if>

<c:if test="${participantAccountForm.showRothHeader ==true}">
<table width="918" class="report_table_content">
    <tbody>

   	    <c:if test="${theIndex % 2 eq 0}"> 
	          <tr class="spec">
	    </c:if>
		<c:if test="${theIndex % 2 ne 0}"> 
			  <tr>
		</c:if>
		<tr> <th class="val_str" colspan="3"> 
		<content:getAttribute beanName="showRothHeadertext" attribute="text"/></th></tr>
		
<c:forEach items="${theNetContribEarningsDetails}" var="theItem" varStatus="theIndex" >

<c:if test="${theItem.rothMoneyTypeInd ==true}">
   		<td width="300" class="val_str">
${theItem.moneyTypeName}
   		</td>
   		
   		<td width="309" class="cur">
   			<report:number property="theItem.netContributions" defaultValue = "" pattern="###,###,##0.00;(###,###,##0.00)"/>
   		</td>
   		
   		<td width="309" class="cur">
   			<report:number property="theItem.earnings" defaultValue = "" pattern="###,###,##0.00;(###,###,##0.00)"/>
   		</td>
</c:if>
    </tr>
</c:forEach>
    </tbody>
</table>


</c:if>
</div>
</c:if>

<!-- FootNotes and Disclaimer 90052-->
<div class="footnotes">
<c:if test="${participantAccountForm.asOfDateCurrent and participantAccountForm.showManagedAccount }">
			<content:contentBean
				contentId="<%=BDContentConstants.MA_FOOTNOTE%>"
				type="<%=BDContentConstants.TYPE_DISCLAIMER%>" id="participantMAFootnote" />
			<p class="footnote"><content:getAttribute id="participantMAFootnote" attribute="text" /></p>
</c:if>
<c:if test="${participantAccountForm.showMultileRothFootnote ==true}">
		<dl><dd><content:getAttribute beanName="multipleRoth" attribute="text"/></dd></dl>
</c:if>
		<dl><dd><content:getAttribute beanName="footnoteEarnings" attribute="text"/></dd></dl>
<c:if test="${participantAccountForm.showPba ==true}">
			  <dl><dd><content:getAttribute beanName="footnotePBA" attribute="text"/></dd></dl>
</c:if>
		<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
        <dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
		<div class="footnotes_footer"></div>
</div> 
<!--end of footnotes-->

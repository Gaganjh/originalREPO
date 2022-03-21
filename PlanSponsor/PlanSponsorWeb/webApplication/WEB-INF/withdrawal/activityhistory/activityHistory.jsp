<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<script  type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<content:errors scope="session"/>
<div style="margin-left: 10px;width: 660px;"><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></div>
<br/><br/>
<div style="margin-left: 10px;width: 660px;"><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></div>
<br/>
<br/>
<div style="padding-left: 10px;font-weight:bold;">Submission number <c:out value="${withdrawalActivityHistoryForm.submissionId}"/></div>
<br/>
<br/>
<jsp:include flush="true" page="activitysummary.jsp"/>
<br/>
<jsp:include flush="true" page="activities.jsp"/>
<table style="margin-left:5px">
	<tr>
		<td>
<input type="button" onclick="javascript:window.print()" name="abc" class="button100Lg" value="print"/>
	    </td>
    	<td>
<input type="button" onclick="javascript:window.close()" name="abc" class="button100Lg" value="close"/>
	    </td>
	</tr>
</table>
<table>
<c:forEach items="${layoutPageBean.footnotes}" var="footnote" varStatus="footnoteStatus">
	<tr>
		<td><content:getAttribute beanName="footnote" attribute="text"/></td>
	</tr>
</c:forEach>
<c:forEach items="${layoutPageBean.disclaimer}" var="disclaimer" varStatus="disclaimerStatus">
	<tr>
	 	<td><content:getAttribute beanName="disclaimer" attribute="text"/></td>
	</tr>
</c:forEach>
</table>


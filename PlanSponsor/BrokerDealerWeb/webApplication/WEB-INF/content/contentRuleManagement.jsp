<%@ page import="com.manulife.pension.bd.web.content.ContentRuleDisplayBean" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>

<script type="text/javascript">

    function doSort(frm, sortProperty) {
    	frm.action.value = 'sort';
		frm.sortProperty.value = sortProperty;
    	frm.submit();
    } 

</script>
<bd:form action="/do/firmRestrictions/management" modelAttribute="contentRuleManagementForm" name="contentRuleManagementForm">

	<h2>Content Rule Management</h2>

	<p></p>


	<div class="page_section_subheader controls">
	  <h3>Content Rule Management</h3>
	</div>

	<DIV id="errordivcs"><report:formatMessages scope="session"/></DIV>

	<div class="report_table">
<input type="hidden" name="action"/>
<form:hidden path="sortProperty"/>
<input type="hidden" name="contentIdSortOrder"/>
<input type="hidden" name="ruleSortOrder"/>
		<table class="report_table_content">
			<thead>
				<tr>
<c:if test="${contentRuleManagementForm.sortProperty !=contentId}">
	<th valign="bottom" nowrap="nowrap" class="val_str"><a href="javascript:doSort(document.contentRuleManagementForm, 'contentId')" class="">Key</a></th>
</c:if>
<c:if test="${contentRuleManagementForm.sortProperty == contentId}">
<th valign="bottom" nowrap="nowrap" class="val_str"><a href="javascript:doSort(document.contentRuleManagementForm, 'contentId')" class="${contentRuleManagementForm.sortClass}">Key</a></th>
</c:if>
					<th valign="bottom" class="val_str">Name</th>
					<th valign="bottom" class="val_str">Category</th>
<c:if test="${contentRuleManagementForm.sortProperty !=ruleType}">
						<th valign="bottom" class="val_str"><a href="javascript:doSort(document.contentRuleManagementForm, 'ruleType')" class="">Rule</a></th>
</c:if>
<c:if test="${contentRuleManagementForm.sortProperty ==ruleType}">
<th valign="bottom" class="val_str"><a href="javascript:doSort(document.contentRuleManagementForm, 'ruleType')" class="${contentRuleManagementForm.sortClass}">Rule</a></th>
</c:if>
					<th colspan="2" valign="bottom" class="val_str">Action</th>
				</tr>
			</thead>
<c:if test="${not empty contentRuleManagementForm.contentRules}">
<c:forEach items="${contentRuleManagementForm.contentRules}" var="rule">
					<tbody>
						<tr class="spec">
<td class="name">${rule.contentId}</td>
<td class="val_num_cnt">${rule.name}</td>
<td class="val_num_cnt">${rule.category}</td>
<td class="date">${rule.ruleType}</td>
							<td class="val_str">
<c:if test="${rule.ruleType == '-'}">
									<div align="center">
<a href="/do/firmRestrictions/maintenance?task=add&amp;contentId=${rule.contentId}"><img src="/assets/unmanaged/images/buttons/cma_edit_icon.gif" width="16" height="16" border="0"/></a>
									</div>
</c:if>
<c:if test="${rule.ruleType != '-'}">
									<div align="center">
<a href="/do/firmRestrictions/maintenance?task=edit&amp;contentId=${rule.contentId}"><img src="/assets/unmanaged/images/buttons/cma_edit_icon.gif" width="16" height="16" border="0"/></a>
									</div>
</c:if>
							</td>
<c:if test="${rule.ruleType != '-'}">
<td class="val_str"><div align="center"><a href="/do/firmRestrictions/maintenance?action=view&amp;contentId=${rule.contentId}"><img src="/assets/unmanaged/images/buttons/cma_view_icon.gif" width="16" height="16" border="0" /></a></div></td>
</c:if>
<c:if test="${rule.ruleType == '-'}">
								<td class="val_str"><div align="center"><img src="/assets/unmanaged/images/buttons/cma_view_icon.gif" width="16" height="16" border="0" /></div></td>
</c:if>
						</tr>
					</tbody>
</c:forEach>
</c:if>
		  </table>
	</div>

	<div class="footnotes">
		<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
		<div class="footnotes_footer"></div>
	</div> 
</bd:form>

<layout:pageFooter/>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<link rel="stylesheet" href="/assets/unmanaged/stylesheet/tpaFandp.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/manulife.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/plansponsor.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/buttons.css" type="text/css">


<table width="300" border="0" cellspacing="0" cellpadding="0"> <!--- Table 1 -->
<tr>
	<td valign="top" align="left">
		<img src="/assets/unmanaged/images/s.gif" width="10" height="1">
	</td>
	<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"><!--- Table 2 -->
		<tr>
			<td><img src="/assets/unmanaged/images/s.gif" width="1" height="10"></td>
		</tr>
		<tr>
			<td class="tableheadTD1" colspan="3"><b>Asset Class Legend</b></td>
		</tr>
		<tr>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td>
				<table width="298" border="0" cellspacing="0" cellpadding="0"><!--- Table 3 -->
				<tr>
					<td width="2"></td>
					<td width="42"></td>
					<td width="20"></td>
					<td width="234"></td>
				</tr>
				<tr class="tablesubhead">
					<td width="2"></td>
					<td><strong>Code</strong></td>
					<td width="20"></td>
					<td><strong>Asset Class</strong></td>
				</tr>
<c:forEach items="${fapForm.assetClassList}" var="assetClass" varStatus="rowIndex">
<c:set var="indexValue" value="${rowIndex.index}"/>
					<c:choose>
						<c:when test="${(indexValue + 1) % 2 == 0}">
							<tr class="datacell1">
						</c:when>
						<c:otherwise>
							<tr class="datacell2">
						</c:otherwise>
					</c:choose>
						<td></td>
						<td>${assetClass.value}</td>
						<td>-</td>
						<td>${assetClass.label}</td>			
					</tr>
</c:forEach>
				<tr>
					<td colspan="4" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>
				</table><!---########### Table 3 -->
			</td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		</table><!---####### Table 2 -->
	</td>
</tr>
</table> <!---### Table 1 -->

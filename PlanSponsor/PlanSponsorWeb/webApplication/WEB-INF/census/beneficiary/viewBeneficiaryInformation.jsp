<%@ page
	import="com.manulife.pension.ps.web.census.beneficiary.BeneficiaryForm"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@ taglib uri="manulife/tags/render" prefix="render"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>

<jsp:useBean id="beneficiaryForm" scope="session" type="com.manulife.pension.ps.web.census.beneficiary.BeneficiaryForm" />
<c:set var="relation" value="${beneficiaryForm.relationship}" />  

<html>
<head>

<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/beneficiary.js"></script>

</head>
<body>
<content:errors scope="request"/>
<ps:form method="post" action="/do/census/beneficiary/viewBeneficiaryInformation/" name="beneficiaryForm" modelAttribute="beneficiaryForm">

		

<form:hidden  path=="action"/>
<form:hidden  path="profileId"/>

	<!-- start Employee information -->

	<table width="760" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="30" />
			<td width="500" colspan="2">
			<!-- First Name and Last Name display -->
<b>${beneficiaryForm.employeeFirstName}

${beneficiaryForm.employeeLastName} | <render:ssn

				property="beneficiaryForm.employeeSSN" defaultValue="" /> </b></td>
		</tr>
		<!-- Last updated date display -->
		<tr>
			<td width="30" />
			<td><b>Last updated date :</b> <render:date dateStyle="l"
				property="beneficiaryForm.employeelastUpdatedDate" /></td>
			<td />
		</tr>
		<!-- Source of last update display -->
		<tr>
			<td width="30" />
<td><b>Source of last update : </b> ${beneficiaryForm.employeelastUpdatedBy}</td>

			<td />
		</tr>
	</table>
	<table width="760" border="0" cellpadding="0" cellspacing="0">
		<tbody>
			<tr>
				<td>
				<table width="500" border="0" cellpadding="0" cellspacing="0">
					<tbody>

<c:if test="${beneficiaryForm.primaryBeneficiaryExists ==true}">

<c:forEach items="${beneficiaryForm.primaryBeneficiaries}" var="primaryBeneficiaryIterate" varStatus="theIndex">


								<tr>
									<td width="15"><img src="/assets/unmanaged/images/s.gif"
										width="30" border="0" height="0"></td>
									<td valign="top" width="609">
									<br />
									<table class="box" border="0" cellSpacing="0" cellPadding="0"
										width="500">
										<!-- start primary beneficiary information -->
										<tbody>
											<tr class="tablehead">
												<td class="tableheadTD1" width="475" nowrap="nowrap"><B>
												${theIndex.index +1 }. Primary beneficiary</B></td>
											</tr>
										</tbody>
									</table>
									<DIV style="DISPLAY: block" id="basic">
									<table class="greyborder" border="0" cellSpacing="0"
										cellPadding="1" width="500">
										<tbody>
											<!-- First Name -->
											<tr class="dataCell2">
												<td class="databorder" width="1"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
												<td width="123">First name</td>
												<td width="26" align="right"></td>
												<td class="greyborder" width="1"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
<td class="highlight" width="347">${primaryBeneficiaryIterate.firstName}</td>

												<td class="databorder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
											</tr>

											<!-- Last Name -->
											<tr class="dataCell1">
												<td class="databorder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
												<td width="123">Last name</td>
												<td width="26" align="right"></td>
												<td class="greyborder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
<td class="highlight">${primaryBeneficiaryIterate.lastName}</td>

												<td class="databorder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
											</tr>

											<!-- Date of Birth -->
											<tr class="dataCell2">
												<td class="databorder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
												<td width="123">Date of birth</td>
												<td width="26" align="right"></td>
												<td class="greyborder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
												<td class="highlight"><render:date
													patternOut="MMMM dd, yyyy"
													property="primaryBeneficiaryIterate.birthDate" /></td>
												<td class="databorder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
											</tr>

											<!-- Relationship -->
											<tr class="dataCell1">
												<td class="databorder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
												<td width="123">Relationship</td>
												<td width="26" align="right"></td>
												<td class="greyborder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
												<td class="highlight">
												${beneficiaryForm.relationship[fn:trim(primaryBeneficiaryIterate.relationshipCode)]} <!-- display only if other relation ship is selected -->

<c:if test="${not empty primaryBeneficiaryIterate.otherRelationshipDesc}"><img border="0" src="/assets/unmanaged/images/spacer.gif" width="80" height="1">



<font color="#000000">If other </font>${primaryBeneficiaryIterate.otherRelationshipDesc}

</c:if></td>
												<td class="databorder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
											</tr>

											<!-- Percent Share -->
											<tr class="dataCell2">
												<td class="databorder" width="1"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
												<td width="123">Percent share</td>
												<td width="26" align="right"></td>
												<td class="greyborder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1"
													height="1"></td>
<td class="highlight"><c:if test="${not empty primaryBeneficiaryIterate.sharePct}">

													<render:number
														property="primaryBeneficiaryIterate.sharePct" />%
</c:if></td>
												<td class="databorder"><img border="0"
													src="/EmployeeSnapshotView_files/spacer.gif" width="1"
													height="1"></td>
											</tr>
											<tr>
												<td class="databorder" height="1" colSpan="6"></td>
											</tr>
										</tbody>
									</table>
									</DIV>
</c:forEach>
</c:if>

						<br/>
						<br/>
						<!-- Contingent Beneficiary Starts here. -->
<c:if test="${beneficiaryForm.contingentBeneficiaryExists eq true}">

<c:forEach items="${beneficiaryForm.contingentBeneficiaries}" var="contingentBeneficiaryIterate" varStatus="theIndex">


								<br/>

								<table class="box" border="0" cellSpacing="0" cellPadding="0"
									width="500">
									<!-- start Contingent beneficiary info -->
									<tbody>
										<tr class="tablehead">
											<td class="tableheadTD1"  width="475" nowrap="nowrap">
											<B>${theIndex.index + 1}. Contingent beneficiary</B></td>
										</tr>
									</tbody>
								</table>

								<DIV style="DISPLAY: block" id="basic">
								<table class="greyborder" border="0" cellSpacing="0"
									cellPadding="1" width="500">
									<tbody>
										<!-- First Name -->
										<tr class="dataCell2">
											<td class="databorder" width="1"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="0"></td>
											<td width="123">First name</td>
											<td width="26" align="right"></td>
											<td class="greyborder" width="1"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
<td class="highlight" width="347">${contingentBeneficiaryIterate.firstName}</td>

											<td class="databorder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
										</tr>

										<!-- Last Name -->
										<tr class="dataCell1">
											<td class="databorder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
											<td width="123">Last name</td>
											<td width="26" align="right"></td>
											<td class="greyborder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
<td class="highlight">${contingentBeneficiaryIterate.lastName}</td>

											<td class="databorder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
										</tr>

										<!-- Date of Birth -->
										<tr class="dataCell2">
											<td class="databorder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
											<td width="123">Date of birth</td>
											<td width="26" align="right"></td>
											<td class="greyborder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
											<td class="highlight"><render:date
												patternOut="MMMM dd, yyyy"
												property="contingentBeneficiaryIterate.birthDate" /></td>
											<td class="databorder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
										</tr>

										<!-- Relationship -->
										<tr class="dataCell1">
											<td class="databorder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
											<td width="123">Relationship</td>
											<td width="26" align="right"></td>
											<td class="greyborder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
											<td class="highlight">${beneficiaryForm.relationship[fn:trim(contingentBeneficiaryIterate.relationshipCode)]}
											 <c:if test="${not empty contingentBeneficiaryIterate.otherRelationshipDesc}"><img border="0" src="/assets/unmanaged/images/spacer.gif" width="80" height="1">





<font color="#000000">If other </font> ${contingentBeneficiaryIterate.otherRelationshipDesc}


</c:if></td>
											<td class="databorder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
										</tr>

										<!-- Percent Share -->
										<tr class="dataCell2">
											<td class="databorder" width="1"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
											<td width="123">Percent share</td>
											<td width="26" align="right"></td>
											<td class="greyborder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
<td class="highlight"><c:if test="${not empty contingentBeneficiaryIterate.sharePct}">

												<render:number
													property="contingentBeneficiaryIterate.sharePct" />%
</c:if></td>
											<td class="databorder"><img border="0"
												src="/assets/unmanaged/images/spacer.gif" width="1"
												height="1"></td>
										</tr>
										<tr>
											<td class="databorder" height="1" colSpan="6"></td>
										</tr>
									</tbody>
								</table>
								</DIV>
</c:forEach>

</c:if>

						<Br/>
						<Br/>
						<c:if test="${empty param.printFriendly}">
							<table width="450">
								<!-- start back and edit buttons -->
								<tr>
									<td><img border="0"
										src="/assets/unmanaged/images/spacer.gif" width="30"
										height="0"></td>
									<td>
									<table border="0" cellSpacing="0" cellPadding="0" width="450">
										<tr vAlign="top">
<td width="278" align="left"><input type="button" onclick="doSubmit('back')" name="button" class="button134" id="back" value="back"/></TD>

<c:if test="${beneficiaryForm.allowedToEdit ==true}">

<td width="206" align="right"><input type="submit" onclick="doSubmit('edit')" name="button" class="button134" id="edit" value="edit"/>&nbsp;</td>

</c:if>
										</tr>
									</table>
									</td>
								</tr>
							</table>
						</c:if>
					</tbody>
				</table>
				</td>
			</tr>
		</tbody>
	</table>

</ps:form>

<!-- footer table -->
<BR/>
<table height="25" cellSpacing="0" cellPadding="0" width="500"
	border="0">
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="30" valign="top"><img
					src="/assets/unmanaged/images/s.gif" width="30" height="0"></td>
				<td><br>
				<p><content:pageFooter beanName="layoutPageBean" /></p>
				<p class="footnote"><content:pageFootnotes
					beanName="layoutPageBean" /></p>
				<p class="disclaimer"><content:pageDisclaimer
					beanName="layoutPageBean" index="-1" /></p>
				</td>
			</tr>
		</table>
		</td>
		<td width="3%"><img src="/assets/unmanaged/images/s.gif"
			height="1"></td>
		<td width="15%"><img src="/assets/unmanaged/images/s.gif"
			height="1"></td>
	</tr>
</table>
<c:if test="${not empty param.printFriendly}">
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />

	<table width="650" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute
				beanName="globalDisclosure" attribute="text" /></td>
		</tr>
	</table>
</c:if>
</body>
</html>

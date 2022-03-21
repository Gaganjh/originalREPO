<!-- Imports -->
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page
	import="com.manulife.pension.service.security.role.InternalUser"%>
<%@ page
	import="com.manulife.pension.service.security.role.ThirdPartyAdministrator"%>
<%@ page
	import="com.manulife.pension.service.security.role.PayrollAdministrator"%>
<%@ page
	import="com.manulife.pension.service.security.valueobject.UserInfo"%>
<%@ page
	import="com.manulife.pension.service.security.valueobject.ContractPermission"%>
<%@ page
	import="com.manulife.pension.service.contract.report.valueobject.SelectContractReportData"%>
	<%@ page
	import="com.manulife.pension.service.contract.report.valueobject.SelectContract"%>
<%@page import="java.util.Map"%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="java.util.Set"%>

<!-- taglib used -->
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<script type="text/javascript">
	function doSubmit(contract){
		document.selectContractForm.contractNumber.value = contract;
		document.selectContractForm.submit();
	}
</script>


<%!
public boolean isRestricted(UserInfo userInfo, int contractNumber) {

	if (userInfo.getRole() instanceof InternalUser ||
		userInfo.getRole() instanceof ThirdPartyAdministrator) {
		return false;
	}
	ContractPermission permission = userInfo.getContractPermission(contractNumber);
	return permission == null || permission.getRole() instanceof PayrollAdministrator;
}

public boolean isMCAvailable(UserProfile userProfile) {
	Set<Integer> contracts = userProfile.getMessageCenterAccessibleContracts();
	return contracts != null && !contracts.isEmpty();
}
%>
<%

                SelectContractReportData theReport = (SelectContractReportData)request.getAttribute(Constants.REPORT_BEAN);
                UserInfo userInfo = (UserInfo)request.getAttribute(Constants.USERINFO_KEY);
                UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
                pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
                pageContext.setAttribute("userInfo",userInfo,PageContext.PAGE_SCOPE);
                pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
                
%>

<!-- Beans used -->



<c:set var="mcNotAvailable"
	value="<%=new Boolean(!isMCAvailable(userProfile)) %>" />
<c:choose>

	<c:when test="${mcNotAvailable}">
		<c:set var="headerCols" value="9" />
		<c:set var="tagCols" value="12" />
		<c:set var="investedCols" value="2" />
		<c:set var="nameWidth" value="200" />
		<c:set var="rowspan" value="1" />
	</c:when>
	<c:otherwise>
		<c:set var="headerCols" value="15" />
		<c:set var="tagCols" value="18" />
		<c:set var="investedCols" value="1" />
		<c:set var="nameWidth" value="165" />
		<c:set var="rowspan" value="2" />
	</c:otherwise>
</c:choose>

<td width="30"><img src="/assets/unmanaged/images/s.gif" height="1">
</td>
<td>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="82%"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>
			<td width="1%"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>
			<td width="1%"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>
		</tr>
		<tr>
			<td width="80%" valign="top">
				<p>
					<content:errors scope="session" />
				</p>

				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>

						<td colspan="3" valign="top"><ps:form method="GET" name="selectContractForm"
							modelAttribute="selectContractForm"	
								action="/do/home/selectContract/">

								<table border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="1"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
										<td width="${nameWidth}"><img
											src="/assets/unmanaged/images/s.gif" height="1"></td>
										<td width="1"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
										<td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
										<td width="1"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
										<td width="65"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
										<td width="1"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
										<td width="60"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
										<td width="1"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
										<td width="60"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
										<c:if test="${!mcNotAvailable}">
											<td width="1"><img src="/assets/unmanaged/images/s.gif"
												height="1"></td>
											<td width="40"><img src="/assets/unmanaged/images/s.gif"
												height="1"></td>
											<td width="1"><img src="/assets/unmanaged/images/s.gif"
												height="1"></td>
											<td width="35"><img src="/assets/unmanaged/images/s.gif"
												height="1"></td>
											<td width="1"><img src="/assets/unmanaged/images/s.gif"
												height="1"></td>
											<td width="30"><img src="/assets/unmanaged/images/s.gif"
												height="1"></td>
										</c:if>
										<td width="4"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
										<td width="1"><img src="/assets/unmanaged/images/s.gif"
											height="1"></td>
									</tr>
									<tr class="tablehead">
										<td class="tableheadTD1" colspan="2"><b>Contract</b></td>
										<td colspan="${headerCols}">
											<table cellspacing="0" cellpadding="0" border="0"
												width="100%">
												<tr>
													<td align="center" class="tableheadTD"><b><report:recordCounter
																label="Contracts" report="theReport" /></b></td>
												</tr>
											</table>
										</td>
										<td class="databorder"><img
											src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									</tr>

									<tr class="tablesubhead">
										<td rowspan="${rowspan}" height="28" class="databorder"><img
											src="/assets/unmanaged/images/s.gif" height="1"></td>
										<td rowspan="${rowspan}"><strong> <report:sort
													field="contractName"
													direction="<%=ReportSort.ASC_DIRECTION%>" formName ="selectContractForm">Contract name
                      				</report:sort>
										</strong></td>

										<td rowspan="${rowspan}" class="dataheaddivider"><img
											src="/assets/unmanaged/images/s.gif" height="1"></td>
										<td rowspan="${rowspan}"><strong> <report:sort
													field="contractNumber"
													direction="<%=ReportSort.ASC_DIRECTION%>" formName ="selectContractForm">Contract number
                      				</report:sort>
										</strong></td>

										<td rowspan="${rowspan}" class="dataheaddivider"><img
											src="/assets/unmanaged/images/s.gif" height="1"></td>
										<td rowspan="${rowspan}"><strong> <report:sort
													field="lastPayrollAllocationAmount"
													direction="<%=ReportSort.ASC_DIRECTION%>" formName ="selectContractForm">
										Last payroll allocation
                      				</report:sort>
										</strong></td>

										<td rowspan="${rowspan}" class="dataheaddivider"><img
											src="/assets/unmanaged/images/s.gif" height="1"></td>
										<td rowspan="${rowspan}"><strong> <report:sort
													field="forPayrollEndingDate"
													direction="<%=ReportSort.ASC_DIRECTION%>" formName ="selectContractForm">
									For payroll ending
                      				</report:sort>
										</strong></td>

										<td rowspan="${rowspan}" class="dataheaddivider"><img
											src="/assets/unmanaged/images/s.gif" height="1"></td>
										<td rowspan="${rowspan}" colspan="${investedCols}"><strong>
												<report:sort field="investedDate"
													direction="<%=ReportSort.ASC_DIRECTION%>" formName ="selectContractForm">
									Invested
                      				</report:sort>
										</strong></td>
										<c:if test="${!mcNotAvailable}">
											<td rowspan="${rowspan}" class="dataheaddivider"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>
											<td colspan="6" align="center"><strong>Message</strong>
											</td>
										</c:if>
										<td class="databorder"><img
											src="/assets/unmanaged/images/s.gif" height="1"></td>

									</tr>
									<c:if test="${!mcNotAvailable}">
										<tr class="tablesubhead">
											<td align="center"><strong>Urgent</strong></td>
											<td class="dataheaddivider"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>
											<td align="center"><strong>Action</td>
											<td class="dataheaddivider"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>
											<td colspan="2" align="center"><strong>FYI</strong></td>
											<td class="databorder"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>
										</tr>
									</c:if>
									<% String cellRecordClass="datacell1";%>
									<input type=hidden name="contractNumber" />
									<c:forEach items="${theReport.details}" var="theRecord">
										<%
										SelectContract record = (SelectContract)(pageContext.getAttribute("theRecord"));
                				boolean restricted = false;//TODO isRestricted(userInfo, record.getContractNumber());
                				request.setAttribute("RESTRICTED", new Boolean(restricted));
                				%>
										<tr class="<%=cellRecordClass%>">
											<td height="15" class="databorder"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>
											<td>${theRecord.contractName}</td>
											<td class="datadivider"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>
											<td><a
												href="javascript:doSubmit(<%=record.getContractNumber()%>)">
													${theRecord.contractNumber} </a> <c:if
													test="${theRecord.oldIloanIndicator ==true}">
                    	 				*
</c:if></td>
											<td class="datadivider"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>
											<td align="right"><logicext:if name="RESTRICTED"
													op="equal" value="true">
													<logicext:then>
    	                	 				Restricted
                    	 				</logicext:then>
													<logicext:else>
														<render:number property="theRecord.lastPayrollAllocation"
															type="c" defaultValue="n/a" />
													</logicext:else>
												</logicext:if></td>
											<td class="datadivider"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>
											<td align="center"><logicext:if name="RESTRICTED"
													op="equal" value="true">
													<logicext:then>
    	                	 				-
                    	 				</logicext:then>
													<logicext:else>
														<render:date
															patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>"
															property="theRecord.forPayrollEndingDate"
															defaultValue="n/a" />
													</logicext:else>
												</logicext:if></td>
											<td class="datadivider"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>
											<td align="center" colspan="${investedCols}"><logicext:if
													name="RESTRICTED" op="equal" value="true">
													<logicext:then>
    	                	 				-
                    	 				</logicext:then>
													<logicext:else>
														<render:date
															patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>"
															property="theRecord.investedDate" defaultValue="n/a" />
													</logicext:else>
												</logicext:if></td>
											<c:if test="${!mcNotAvailable}">
												<c:set var="counts"
													value="${requestScope.contractMessageCounts[theRecord.contractNumber]}" />
												<td class="datadivider"><img
													src="/assets/unmanaged/images/s.gif" height="1"></td>
												<td align="center"><c:choose>
														<c:when test="${empty counts}">
                    	 			      -
                    	 			    </c:when>
														<c:otherwise>
                    	 			      ${counts[0]}
                    	 			    </c:otherwise>
													</c:choose></td>
												<td class="datadivider"><img
													src="/assets/unmanaged/images/s.gif" height="1"></td>
												<td align="center"><c:choose>
														<c:when test="${empty counts}">
                    	 			      -
                    	 			    </c:when>
														<c:otherwise>
                    	 			      ${counts[1]}
                    	 			    </c:otherwise>
													</c:choose></td>
												<td class="datadivider"><img
													src="/assets/unmanaged/images/s.gif" height="1"></td>
												<td align="center" colspan="2"><c:choose>
														<c:when test="${empty counts}">
                    	 			      -
                    	 			    </c:when>
														<c:otherwise>
                    	 			      ${counts[2]}
                    	 			    </c:otherwise>
													</c:choose></td>
											</c:if>
											<td class="databorder"><img
												src="/assets/unmanaged/images/s.gif" height="1"></td>

										</tr>
										<%
                       				cellRecordClass = (cellRecordClass.equals("datacell1"))?"datacell2":"datacell1";
                       			%>
									</c:forEach>
									<c:if test="${not empty theReport.details}">

										<ps:roundedCorner numberOfColumns="${tagCols}"
											emptyRowColor="white" oddRowColor="white"
											evenRowColor="beige" name="theReport" property="details" />

									</c:if>
								</table></ps:form>
							</td>
					</tr>
				</table>

			</td>
			<td width="1%"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>
			<td width="1%"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>

		</tr>
		<tr>
			<td colspan="3"><br>
				<p>
					<content:pageFooter beanName="layoutPageBean" />
				</p>
				<p class="footnote">
					<content:pageFootnotes beanName="layoutPageBean" />
				</p>
				<p class="disclaimer">
					<content:pageDisclaimer beanName="layoutPageBean" index="-1" />
				</p></td>
		</tr>
	</table>
</td>


<td valign="top" width="185"></td>








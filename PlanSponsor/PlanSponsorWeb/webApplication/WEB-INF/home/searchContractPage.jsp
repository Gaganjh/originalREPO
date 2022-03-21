<!-- Imports -->
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page
	import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.pension.ps.web.home.SearchCompanyNameForm"%>
<%@ page import="com.manulife.pension.ps.web.ErrorCodes"%>
<%@ page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
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
<%@page import="java.util.Set"%>
<%@page import="org.owasp.encoder.Encode"%>

<!-- taglib used -->
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%%>

<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script language="javascript">
                function doSubmit(contract)
                {
                                document.searchContractForm.contractNumber.value = contract;
                                document.searchContractForm.submit();
                }

                function clearContractName()
                {
                                document.searchCompanyNameForm.companyName.value = "";
                }

                function clearContractNumber()
                {
                                document.searchCompanyNameForm.contractNumber.value = "";
                }


</script>

<%
                String sCompanyName = request.getParameter("companyName");
                String test="test";
                String sContractNumber = request.getParameter("contractNumber");
                if (sContractNumber == null) sContractNumber = "";
                if (sCompanyName == null || sContractNumber.length() != 0) sCompanyName = "";

                SelectContractReportData theReport = (SelectContractReportData)request.getAttribute(Constants.REPORT_BEAN);
                UserInfo userInfo = (UserInfo)request.getAttribute(Constants.USERINFO_KEY);
                UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
                pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
                pageContext.setAttribute("userInfo",userInfo,PageContext.PAGE_SCOPE);
                pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
                
%>

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

<ps:map id="parameterMap">
	<ps:param name="contractNumber" value="<%=Encode.forHtml(sContractNumber)%>" />
	<ps:param name="companyName" value="<%=Encode.forHtml(sCompanyName)%>" />
</ps:map>

<!-- Beans used -->


<c:if test="${empty theReport.details}">
	<%
                                if ( sContractNumber.length() > 0 ||
                                                               sCompanyName.length() > 2 )
                                {
                                                Object obj = request.getAttribute(Environment.getInstance().getErrorKey());
                                                java.util.Collection errors = null;
                                                if ( obj != null )
                                                    errors = (java.util.Collection) obj;
                                                else
                                                errors = new java.util.ArrayList();

                                     if ( theReport.isContractExistsOnTheOtherSite() )
                                     {                                 
                                                                String [] contractExistOnTheOtherSiteErrorMsgArgs = new String[2];
                                                                if ( userProfile.getRole() instanceof ThirdPartyAdministrator )
                                                                                contractExistOnTheOtherSiteErrorMsgArgs[0] = Environment.getInstance().getTPAOtherSiteMarketingURL();
                                                                else
                                                                                contractExistOnTheOtherSiteErrorMsgArgs[0] = Environment.getInstance().getPSOtherSiteMarketingURL();

                                                                contractExistOnTheOtherSiteErrorMsgArgs[1] = contractExistOnTheOtherSiteErrorMsgArgs[0];

                                                                errors.add(new com.manulife.pension.util.content.GenericException(
                                                                                                ErrorCodes.CONTRACT_EXIST_ON_THE_OTHER_SITE,
                                                                                                contractExistOnTheOtherSiteErrorMsgArgs));
                                                }
                                     else
                                                    errors.add(new com.manulife.pension.util.content.GenericException(ErrorCodes.SEARCH_CONTRACT_NO_RESULT));

                                                 request.setAttribute(Environment.getInstance().getErrorKey(), errors);
                                }
                %>
</c:if>

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
			<td width="70%"><img src="/assets/unmanaged/images/s.gif"
				height="3">&nbsp;</td>
			<td width="3%"><img src="/assets/unmanaged/images/s.gif"
				height="3"></td>
			<td width="27%"><img src="/assets/unmanaged/images/s.gif"
				height="3"></td>
		</tr>
		<tr>
			<td width="70%"><img src="/assets/unmanaged/images/s.gif"
				height="3">&nbsp;</td>
			<td width="3%"><img src="/assets/unmanaged/images/s.gif"
				height="3"></td>
			<td width="27%"><img src="/assets/unmanaged/images/s.gif"
				height="3"></td>
		</tr>
		<tr>


			<td><ps:permissionAccess permissions="BUSC">
					<a
						href="/do/tools/businessConversionNoNavHeader/?from=searchContract">Security
						role conversion</a> |
                                                </ps:permissionAccess> <ps:isAnyPilotAvailable>
					<ps:permissionAccess permissions="PLMN">
						<a href="/do/pilot/pilotContractNoNavHeader/?from=searchContract">Manage
							Pilots</a> |
                                                  </ps:permissionAccess>
				</ps:isAnyPilotAvailable> <ps:permissionAccess permissions="CRAP">
					<a href="/do/tools/controlReports/">Control reports</a> |
                                                </ps:permissionAccess> <% if (userInfo.getRole() instanceof InternalUser ){ %>
				Search Contract <ps:isNotJhtc
					name="<%= Constants.USERPROFILE_KEY %>" property="role">|
                                                                <a
						href="/do/home/searchTPA/?first=Y">Search TPA</a>
				</ps:isNotJhtc> <%}%></td>

		</tr>

		<tr>
			<td width="70%"><img src="/assets/unmanaged/images/s.gif"
				height="1">
			<content:errors scope="request" />
				<content:errors scope="session" /></td>
			<td width="3%"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>
			<td width="27%"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>
		</tr>
		<tr>
			<td width="70%"><img src="/assets/unmanaged/images/s.gif"
				height="3">&nbsp;</td>
			<td width="3%"><img src="/assets/unmanaged/images/s.gif"
				height="3"></td>
			<td width="27%"><img src="/assets/unmanaged/images/s.gif"
				height="3"></td>
		</tr>
		<tr>
			<td valign="top">

				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>

						<td colspan="7" valign="top"><ps:form method="GET" name="searchContractForm"
							modelAttribute="searchContractForm"	action="/do/home/searchContract/">

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
													<td align="right" class="tableheadTD"><report:pageCounter formName="searchContractForm"
															report="theReport" name="parameterMap" /></td>
												</tr>
											</table>
										</td>
										<td class="databorder"><img
											src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									</tr>

									  <tr class="tablesubhead">
                                                                                <td rowspan="${rowspan}" height="28" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td rowspan="${rowspan}">
                                                                                <strong>
<c:if test="${searchContractForm.runStoredProc ==true}">
                                                                                <report:sort field="contractName" direction="<%=ReportSort.ASC_DIRECTION%>" name="parameterMap" formName="searchContractForm">Contract name
                                                                                </report:sort>
</c:if>
<c:if test="${searchContractForm.runStoredProc ==false}">
                                                                                <a href="">Contract name</a>
</c:if>
                                                                                </strong>
                                                                                </td>

                                                                                <td rowspan="${rowspan}" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td rowspan="${rowspan}">
                                                                                <strong>
<c:if test="${searchContractForm.runStoredProc ==true}">
                                                                                <report:sort field="contractNumber" direction="<%=ReportSort.ASC_DIRECTION%>" name="parameterMap" formName="searchContractForm">Contract number
                                                                                </report:sort>
</c:if>
<c:if test="${searchContractForm.runStoredProc ==false}">
                                                                                <a href="">Contract number</a>
</c:if>
                                                                                </strong>
                                                                                </td>

                                                                                <td rowspan="${rowspan}" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td rowspan="${rowspan}">
                                                                                <strong>
<c:if test="${searchContractForm.runStoredProc ==true}">
                                                                                <report:sort field="lastPayrollAllocationAmount" direction="<%=ReportSort.ASC_DIRECTION%>" name="parameterMap" formName="searchContractForm">
                                                                                                                                                                Last payroll allocation
                                                                                </report:sort>
</c:if>
<c:if test="${searchContractForm.runStoredProc ==false}">
                                                                                <a href="">Last payroll allocation</a>
</c:if>
                                                                                </strong>
                                                                                </td>

                                                                                <td rowspan="${rowspan}" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td rowspan="${rowspan}">
                                                                                <strong>
<c:if test="${searchContractForm.runStoredProc ==true}">
                                                                                <report:sort field="forPayrollEndingDate" direction="<%=ReportSort.ASC_DIRECTION%>" name="parameterMap" formName="searchContractForm">
                                                                                                                                                For payroll ending
                                                                                </report:sort>
</c:if>
<c:if test="${searchContractForm.runStoredProc ==false}">
                                                                                <a href="">For payroll ending</a>
</c:if>
                                                                                </strong>
                                                                                </td>

                                                                                <td rowspan="${rowspan}" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td rowspan="${rowspan}" colspan="${investedCols}">
                                                                                <strong>
<c:if test="${searchContractForm.runStoredProc ==true}">
                                                                                <report:sort field="investedDate" direction="<%=ReportSort.ASC_DIRECTION%>" name="parameterMap" formName="searchContractForm">
                                                                                                                                                Invested
                                                                                </report:sort>
</c:if>
<c:if test="${searchContractForm.runStoredProc ==false}">
                                                                                <a href="">Invested</a>
</c:if>
                                                                                </strong>
                                                                                </td>
                                                                                <c:if test="${!mcNotAvailable}">                                                                              
                                                                                <td rowspan="${rowspan}" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td colspan="6" align="center">
                                                                                  <strong>Message</strong>
                                                                                </td>                                                                                    
                                                                                </c:if>
                                                                                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                
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
									<%-- <form:hidden path="contractNumber"/> --%>
									<input type=hidden name="contractNumber" />

									<c:forEach items="${theReport.details}" var="theRecord1">
                                      <% SelectContract theRecord = (SelectContract)(pageContext.getAttribute("theRecord1")); %>
									<%
                                                                               boolean restricted = isRestricted(userInfo, theRecord.getContractNumber());
                                                                               request.setAttribute("RESTRICTED", new Boolean(restricted));
                                                                                %>
									  <tr class="<%=cellRecordClass%>">
                                                                                <td class="databorder"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
												<td>${theRecord1.contractName}</td>
                                                                                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
				<td><a href="javascript:doSubmit(<%=theRecord.getContractNumber()%>)">${theRecord1.contractNumber}</a>
<c:if test="${theRecord1.oldIloanIndicator ==true}">
                                                                                                *
</c:if></td>
                                                                                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td align="right">
                                                                                <logicext:if name="RESTRICTED" op="equal" value="true">
                                                                                                <logicext:then>
                                                                                                                Restricted
                                                                                                </logicext:then>
                                                                                                <logicext:else>
                                                                                                                <render:number property="theRecord1.lastPayrollAllocation" type="c" defaultValue="n/a"/>
                                                                                                </logicext:else>
                                                                                </logicext:if>
                                                                                </td>
                                                                                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td align="center">
                                                                                <logicext:if name="RESTRICTED" op="equal" value="true">
                                                                                                <logicext:then>
                                                                                                                -
                                                                                                </logicext:then>
                                                                                                <logicext:else>
                                                                                                                <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theRecord1.forPayrollEndingDate" defaultValue="n/a"/>
                                                                                                </logicext:else>
                                                                                </logicext:if>
                                                                                </td>
                                                                                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td align="center" colspan="${investedCols}">
                                                                                <logicext:if name="RESTRICTED" op="equal" value="true">
                                                                                                <logicext:then>
                                                                                                                -
                                                                                                </logicext:then>
                                                                                                <logicext:else>
                                                                                                                <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theRecord1.investedDate" defaultValue="n/a"/>
                                                                                                </logicext:else>
                                                                                </logicext:if>
                                                                                </td>                                    
                                                                                <c:if test="${!mcNotAvailable}">
                                                                                <c:set var="counts" value="${requestScope.contractMessageCounts[theRecord1.contractNumber]}"/>                         
                                                                                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td align="center" >
                                                                                  <c:choose>
                                                                                    <c:when test="${empty counts}">
                                                                                      -
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                      ${counts[0]}
                                                                                    </c:otherwise>
                                                                                  </c:choose>
                                                                                </td>                                                                    
                                                                                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td align="center" >
                                                                                  <c:choose>
                                                                                    <c:when test="${empty counts}">
                                                                                      -
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                      ${counts[1]}
                                                                                    </c:otherwise>
                                                                                  </c:choose>
                                                                                </td>
                                                                                                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                                                                                <td align="center" colspan="2" >
                                                                                  <c:choose>
                                                                                    <c:when test="${empty counts}">
                                                                                      -
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                      ${counts[2]}
                                                                                    </c:otherwise>
                                                                                  </c:choose>
                                                                                </td>
                                                                                </c:if>
                                                                                                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>

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

									<tr>
										<td colspan="${tagCols}" align="right"><report:pageCounter formName="searchContractForm"
												report="theReport" arrowColor="black" name="parameterMap" /></td>
									</tr>
								</table>
							</ps:form>
							</td>
					</tr>
				</table>
			</td>
			<td width="30"><img src="/assets/unmanaged/images/s.gif"
				height="1"></td>
			<td valign="top" align="right">
				<table border="0" cellspacing="0" cellpadding="0" class="box"
					vAlign="top" width="175">
					<tr class="tablehead">
						<td colspan="3" class="tableheadTD1"><b>Search for a contract</b></td>
					</tr>
					<tr>
						<td class="boxborder" width="1%"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<ps:form name="searchCompanyNameForm" modelAttribute="searchCompanyNameForm" method="GET"
							action="/do/home/searchCompanyName/" >
							<td class="boxbody" width="130">
								<p>
									Enter company name or contract number.<br>
									<br> <b>Contract name</b> <input name="companyName"
										type="text" value="<%=Encode.forHtmlAttribute(sCompanyName)%>"
										onkeypress="clearContractNumber();">
								</p>
								<p>
									<b>Contract number</b> <input name="contractNumber" type="text"
										value="<%=Encode.forHtmlAttribute(sContractNumber)%>" onkeypress="clearContractName();">
								</p>
								<p>
									<input name="search" type="submit" class="button100Lg"
										value="search">
									<c:if test="${searchContractForm.showButton ==true}">
										<br>
										<br>
										<input type="button" class="button100Lg" value="show all"
											onClick="window.location='/do/home/searchContractDetail/?showAll=';">
									</c:if>
								</p>
							</td>
</ps:form>						<td class="boxborder" width="1%"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					<tr>
						<td colspan="3">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="boxborder" width="1"><img
										src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
									<td><img src="/assets/unmanaged/images/s.gif" width="1"
										height="4"></td>
									<td rowspan="2" width="5"><img
										src="/assets/unmanaged/images/box_lr_corner.gif" width="5"
										height="5"></td>
								</tr>
								<tr>
									<td class="boxborder"><img
										src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td class="boxborder"><img
										src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
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
<script>
                setFocusOnFirstInputField("searchCompanyNameForm");
</script>

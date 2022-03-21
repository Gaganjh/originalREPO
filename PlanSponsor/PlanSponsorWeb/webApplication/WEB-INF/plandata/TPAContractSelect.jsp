<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContract"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContractSearchReportData" %>
<%

TPAPlanDataContractSearchReportData theReport = (TPAPlanDataContractSearchReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%> 


 
<c:if test="${empty param.printFriendly }" >
	<script language="javascript" type="text/javascript"
		src="/assets/unmanaged/javascript/calendar.js">
	</script>

	<script type="text/javascript" >
		function doBack() {
	    	document.forms['tpaPlanDataContractSearchForm'].action = "/do/viewTpaStandardFeeSchedule/?tpaFirmId="+document.forms['tpaFeeScheduleContractSearchForm'].tpaId.value;
	    	document.tpaPlanDataContractSearchForm.submit();
		}
		
		function doCustomize(contractNumber, contractName) {
			document.forms['tpaPlanDataContractSearchForm'].task.value = "customizeContract";
			document.getElementById("selectedContractNumber").value = contractNumber;
			document.getElementById("selectedContractName").value = contractName;
			document.tpaPlanDataContractSearchForm.submit();
			
		}
	</script>
</c:if>
<input type="hidden" name="task"  value="filter"/>
<form:hidden path="selectedContractNumber"/> 
<form:hidden path="selectedContractName"/> 

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		
		<td width="100%" valign="top">
		<br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="7" valign="top">
					<table width="735" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"height="1"></td>
							<td width="260"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
									height="1"></td>
							<td><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="155"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="120"></td>
							
							
						</tr>
						<tr class="tablehead">
                			<td class="tableheadTD1" colspan="2"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
                			</b></td>
                      		<td colspan="9">
                      			<table cellspacing="0" cellpadding="0" border="0" width="100%">
                      				<tr>
                      					<td align="center" class="tableheadTD">
											<b><report:recordCounter label="Contracts" report="theReport"/></b>
										</td>
                      					<td align="right" class="tableheadTD"><report:pageCounter report="theReport"  formName="tpaPlanDataContractSearchForm" name="parameterMap" /></td>
									</tr>
								</table>
                      		</td>
               				
                    	</tr>
						<tr class="tablesubhead">
							<td rowspan="1" height="28" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD class=payrollDate vAlign=middle height=25 align=left>	
                      			<strong>
                      				<report:sort    field="contractName" formName="tpaPlanDataContractSearchForm" direction="<%=ReportSort.DESC_DIRECTION%>">Contract name
                      				</report:sort>
                      			</strong>  
                    		</TD>
							<td rowspan="1" class="dataheaddivider"><imgsrc="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD class=payrollDate vAlign=middle height=25 align=left>
           				       	<strong>
                      				<report:sort field="contractId"   formName="tpaPlanDataContractSearchForm" direction="<%=ReportSort.DESC_DIRECTION%>">Contract number
                      				</report:sort>
                      		   </strong>
                      		</TD>

							

							<td rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							
							<TD class=payrollDate vAlign=middle height=25 align=left>
           				          <strong>
                      				<report:sort field="createdTS"   formName="tpaPlanDataContractSearchForm"  direction="<%=ReportSort.DESC_DIRECTION%>">Last updated 
                      				</report:sort>
                      		   </strong>
                      		</TD>

							<td rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD align=left> 
           				          <strong>
                      				<report:sort  formName="tpaPlanDataContractSearchForm"  field="createdUser" direction="<%=ReportSort.ASC_DIRECTION%>">Last updated by 
                      				</report:sort>
                      		   </strong>
                      		</TD>
							<td rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                            <TD class=payrollDate vAlign=middle height=25 align=left> 
                                  <strong>
                                    <report:sort field="serviceSelected"   formName="tpaPlanDataContractSearchForm"   direction="<%=ReportSort.ASC_DIRECTION%>">Service Selected 
                                    </report:sort>
                               </strong>
                            </TD>
							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width=1></td>
						</tr>

						<c:if test="${not empty theReport}">
<c:forEach items="${theReport.details}" var="theRecord" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/> 
<%String temp = pageContext.getAttribute("indexValue").toString();
TPAPlanDataContract theRecord = (TPAPlanDataContract)pageContext.getAttribute("theRecord");

%>
								<% if (Integer.parseInt(temp) % 2 != 0) { %>
			                   <tr class="datacell1" height="25">
						             <% } else {%>
				               <tr class="datacell2" height="25">
						            <% } %>
									<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td align="left">
									<c:choose>
										<c:when test='${param.printFriendly}'>
${theRecord.contractName}
										</c:when>
									<c:otherwise>
									<% String contractNameLink = theRecord.getContractName().replace("'","\\\'");
										request.setAttribute("contractNameLink", contractNameLink); 
										
										%>
										
									<a href="javascript:doCustomize(${theRecord.contractId}, '${contractNameLink}')" />
${theRecord.contractName}
										
									</c:otherwise>
									</c:choose>
										
									</td>
									<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td align="left"> 
									<c:choose>
										<c:when test='${param.printFriendly}'>
${theRecord.contractId}
										</c:when>
									<c:otherwise>
									<a href="javascript:doCustomize(${theRecord.contractId},'${contractNameLink}')" />
${theRecord.contractId}
										
									</c:otherwise>
									</c:choose>
									</td>
									<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									
									<td align="left"><render:date patternOut="MM/dd/yyyy"
																			property="theRecord.createdTS" />
									</td>
									<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
<td align="left" >${theRecord.createdUser}</td>
									<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td align="left">
${theRecord.serviceSelected}
                                    </td>
                                    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
								</tr>
</c:forEach>

</c:if>
						<tr>
							<td class="databorder" colspan="12"><img src="/assets/unmanaged/images/s.gif" width="2" height="1"></td>
						</tr>
						<tr>
							<td colspan="12" align="right"><report:pageCounter report="theReport" formName="tpaPlanDataContractSearchForm" arrowColor="black" name="parameterMap" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>				
	</td>
</tr>
</table>

<c:if test="${not empty param.printFriendly}" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute id="globalDisclosure"
				attribute="text" /></td>
		</tr>
	</table>
</c:if>

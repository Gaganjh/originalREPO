<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.service.report.valueobject.ReportSort"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContractSearchReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>



<%
TPAFeeScheduleContractSearchReportData theReport = (TPAFeeScheduleContractSearchReportData)request.getAttribute(Constants.REPORT_BEAN);
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
	    	document.forms['tpaFeeScheduleContractSearchForm'].action = "/do/viewTpaStandardFeeSchedule/?tpaFirmId="+document.forms['tpaFeeScheduleContractSearchForm'].tpaId.value;
	    	document.tpaFeeScheduleContractSearchForm.submit();
		}
		
		function doCustomize(contractNumber) {
			document.forms['tpaFeeScheduleContractSearchForm'].task.value = "customizeContract";
			document.getElementById("selectedContractNumber").value = contractNumber;
			document.tpaFeeScheduleContractSearchForm.submit();
		}
	</script>
</c:if>
<input type="hidden" name="task"/>
<form:hidden path="selectedContractNumber" id="selectedContractNumber" /><%--  input - name="tpaFeeScheduleContractSearchForm" --%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="30"
			height="1"></td>
		<td width="100%" valign="top">
		<br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="7" valign="top">
					<table width="735" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"height="1"></td>
							<td width="210"><img src="/assets/unmanaged/images/s.gif"
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
							<td width="120"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="120"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="4"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif"
										height="1"></td>
						</tr>
						<tr class="tablehead">
                			<td class="tableheadTD1" colspan="2"><b>Contract</b></td>
                      		<td colspan="9">
                      			<table cellspacing="0" cellpadding="0" border="0" width="100%">
                      				<tr>
                      					<td align="center" class="tableheadTD">
											<b><report:recordCounter label="Contracts" report="theReport"/></b>
										</td>
                      					<td align="right" class="tableheadTD"><report:pageCounter
													report="theReport" name="parameterMap" formName="tpaFeeScheduleContractSearchForm" /></td>
									</tr>
								</table>
                      		</td>
               				<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    	</tr>
						<tr class="tablesubhead">
							<td rowspan="1" height="28" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD class=payrollDate vAlign=middle height=25 align=left>	
                      			<strong>
                      				<report:sort field="contractName" direction="<%=ReportSort.DESC_DIRECTION%>" formName="tpaFeeScheduleContractSearchForm">Contract name
                      				</report:sort>
                      			</strong>  
                    		</TD>
							<td rowspan="1" class="dataheaddivider"><imgsrc="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD class=payrollDate vAlign=middle height=25 align=left>
           				       	<strong>
                      				<report:sort field="contractId" direction="<%=ReportSort.DESC_DIRECTION%>" formName="tpaFeeScheduleContractSearchForm">Contract number
                      				</report:sort>
                      		   </strong>
                      		</TD>

							<td rowspan="1" class="dataheaddivider"><imgsrc="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD class=payrollDate vAlign=middle height=25 align=left>
           				          <strong>
                      				<report:sort field="feeSchedule" direction="<%=ReportSort.ASC_DIRECTION%>" formName="tpaFeeScheduleContractSearchForm">Fee schedule
                      				</report:sort>
                      		   </strong>
                      		</TD>

							<td rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							
							<TD class=payrollDate vAlign=middle height=25 align=left>
           				          <strong>
                      				<report:sort field="createdTS" direction="<%=ReportSort.DESC_DIRECTION%>" formName="tpaFeeScheduleContractSearchForm">Last updated 
                      				</report:sort>
                      		   </strong>
                      		</TD>

							<td rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<TD colSpan=2 align=left> 
           				          <strong>
                      				<report:sort field="createdUser" direction="<%=ReportSort.ASC_DIRECTION%>" formName="tpaFeeScheduleContractSearchForm">Last updated by 
                      				</report:sort>
                      		   </strong>
                      		</TD>

							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width=1></td>
						</tr>

						<c:if test="${not empty theReport}">
<c:forEach items="${theReport.details}" var="theRecord" varStatus="theIndex" >

								<c:choose>
								<c:when test="${theIndex.index % 2 == 0}">
			                   	<tr class="datacell1" height="25">
						           </c:when>
								<c:otherwise>
				               <tr class="datacell2" height="25">
						          </c:otherwise>
								</c:choose>
									<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
<td align="left">${theRecord.contractName}</td>
									<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td align="left"> 
										<a href="javascript:doCustomize(${theRecord.contractId})" />  
${theRecord.contractId}
										</a>
									</td>
									<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td align="left">
${theRecord.feeSchedule}
									</td>
									<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
									<td align="left"><render:date patternOut="MM/dd/yyyy HH:mm:ss"
																			property="theRecord.createdTS" />
									</td>
									<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
<td align="left" colspan="2">${theRecord.createdUser}</td>
									<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
								</tr>
</c:forEach>

</c:if>
						<tr>
							<td class="databorder" colspan="12"><img src="/assets/unmanaged/images/s.gif" width="2" height="1"></td>
						</tr>
						<tr>
							<td colspan="12" align="right"><report:pageCounter report="theReport" arrowColor="black" name="parameterMap" formName="tpaFeeScheduleContractSearchForm" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>				
	</td>
</tr>
</table>

<c:if test="${not empty param.printFriendly }" >
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

<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.pif.report.valueobject.PIFSubmissionReportData" %>
<%@ page import="com.manulife.pension.service.pif.valueobject.PlanInfoSubmissionHistoryVO" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%-- Define static constants --%>
<un:useConstants scope="request" var="lookupConstants" className="com.manulife.pension.cache.CodeLookupCache"/>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="globalConstants" className="com.manulife.pension.common.GlobalConstants"/>

<%
if(request.getAttribute(Constants.REPORT_BEAN) != null){
	
	PIFSubmissionReportData theReport = (PIFSubmissionReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
}
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>

<content:contentBean contentId="${contentConstants.ADD_PLAN_INFO_LINK}" type="${contentConstants.TYPE_MISCELLANEOUS}"  id="addPlanInfoLink"/>
                    		
<content:contentBean contentId="${contentConstants.PIF_NO_SUBMISSIONS_AVAILABLE}"
                    		type="${contentConstants.TYPE_MESSAGE}"  id="submissionMessage"/>
                    		
<content:contentBean contentId="${contentConstants.DELETE_ICON_INTERNAL_USERS}"
							type="${contentConstants.TYPE_MISCELLANEOUS}"  id="deleteIconInternalUsers"/>
<%-- Load IE 6 specific Styles to handle width issues --%>
<!--[if lt IE 7]>
  <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
<![endif]-->                    		
<html>
<body>
<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
  <TBODY>
  <TR>
    <TD width=30>&nbsp;</TD>
    <TD width=700>
<SCRIPT language=JavaScript1.2 type=text/javascript>

function doAddPlanInfo(){
	var contractId = document.forms['pifSubmissionReportForm'].contractId.value;
	document.forms['pifDataForm'].contractId.value = contractId;	
	document.forms['pifDataForm'].tpaFirmName.value = "<c:out value="${theReport.tpaFirmName}" escapeXml="false" />";	
	document.forms['pifDataForm'].tpaFirmId.value = "<c:out value="${theReport.tpaFirmId}" escapeXml="false" />";
	document.forms['pifDataForm'].contractName.value = "<c:out value="${theReport.contractName}" escapeXml="false" />";
	document.pifDataForm.action="/do/contract/pic/edit/?action=addPlanInfo";
	document.pifDataForm.submit();
}

function doEditPlanInfo(submissionId){
	if(submissionId != ""){
		doValidateLock(submissionId);
	} else {
		return false;
	}	
}

function resetFilters() {
	
  document.forms['pifSubmissionReportForm'].contractName.value  = ""; 		
  document.forms['pifSubmissionReportForm'].contractNumber.value  = "";
  document.forms['pifSubmissionReportForm'].submissionStatus.value  = "";
  document.pifSubmissionReportForm.submit();
}

function doDelete(contractId,submissionId,lastUpdatedDate,deleteUserName){
	
	if(submissionId != ""){
		doValidateDeleteLock(contractId,submissionId,lastUpdatedDate,deleteUserName);
	} else {
		return false;
	}
}
</SCRIPT>
<%-- This form is used for add plan information button --%>
<ps:form modelAttribute="pifDataForm" name="pifDataForm" method="POST" action="/do/contract/pic/edit/">
	<input type="hidden" name="contractId" id="contractId" />
	<input type="hidden" name="submissionId" id="submissionId" />
	<input type="hidden" name="tpaFirmName" id="tpaFirmNameId" />
	<input type="hidden" name="tpaFirmId" id="tpaFirmId" />
	<input type="hidden" name="contractName" id="contractNameId" />
</ps:form>

<%-- This form is used for delete plan information button --%>
<ps:form name="deletePIFDataForm" modelAttribute="deletePIFDataForm" method="POST" action="/do/contract/pic/delete/">
	<input type="hidden" name="contractNumber" id="deleteContractNumber" />
	<input type="hidden" name="contractName" id="deleteContractName" />
	<input type="hidden" name="submissionId" id="deleteSubmissionId" />
	<input type="hidden" name="lastUpdatedDate" id="deleteLastUpdatedDate" />
	<input type="hidden" name="userName" id="deleteUserName" />
</ps:form>

<%-- Error Table --%>
<div id="messagesBox" class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
	<ps:messages scope="request" maxHeight="${param.printFriendly ? '1000px' : '100px'}" suppressDuplicateMessages="true" />
</div>
<DIV style="DISPLAY: block" id=basic>
<%--start table content --%>

<ps:form method="POST" cssStyle="margin-bottom:0;" action="/do/contract/pic/plansubmission/" modelAttribute="pifSubmissionReportForm" name="pifSubmissionReportForm">
	<form:hidden path="contractId"  />
            <%--------------  body  -----------------------%>
			<% Integer contractId = null;%>
			<%
				if(request.getAttribute("conId") != null){
					contractId = (Integer)request.getAttribute("conId");					
				}				
			%>
			<input type="hidden" name="IdCon" id="IdCon" value="<%=contractId%>" />
            <TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
              <TBODY>
              <TR>
                <TD>
                  <P><B></B></P></TD>
                <TD width=10><IMG border=0 
                  src="/assets/unmanaged/images/spacer.gif" 
                  width=15 height=1></TD>
                <TD>&nbsp;</TD></TR>
              <TR>
                <TD vAlign=top width=700>
                  <TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
                    <TBODY>
					
					<tr><td colspan="12"><b>Legend</b>: <img src="/assets/unmanaged/images/edit_icon.gif" /> Edit <img src="/assets/unmanaged/images/delete_icon.gif" /> Delete</td></tr>
					
                    <TR>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=actions><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=30 height=1></TD>
                      <TD class=submissionDate><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=35 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=type><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=130 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=payrollDate><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=150 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD align="left" class=contributionTotal><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=120 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=paymentTotal><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=120 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=130 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>                        
					  <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD></TR>
                    <TR class=tablehead>
                      <TD class=tableheadTD1 height=25 vAlign=center 
                        colSpan=13></TD>
                      <TD class=databorder><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD></TR>
					
					<TR class=datacell1>
						<TD class=databorder>
							<IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
						</TD>
						<TD colSpan=12>
							<TABLE border="0" width="100%">
								<TR class=datacell1>
									<TD valign=top width="17%">
										<B>Contract name</B>
									</TD>
									<TD width="18%">
									<form:input path="contractName" 
		                           		disabled="${param.printFriendly}" 
		                           	    id="contractNmId"
		                           		maxlength="30" style="width:95%"/>
									</TD>
									
									<TD></TD>
								</TR>							
								<TR class=datacell1>
									<TD valign=top width="17%">
										<B>Contract number</B>
									</TD>
									<TD width="18%">
		                          <form:input	path="contractNumber" 	
		                          disabled="${param.printFriendly}"  
		                          id="contractNumberId " 	
		                          maxlength="7" style="width:95%"/>  
									</TD>
									
									<TD></TD>
								</TR>
								<TR class=datacell1>
								    <TD valign=top width="17%">
									    <b>Submission status</b> 
								    </TD>
									<TD width="18%">
                                  <form:select path="submissionStatus" 
                                     disabled="${param.printFriendly}" style="width:95% ">
				                      <form:option value="">All Statuses </form:option>
				                      <form:option value="14">Draft</form:option>
				                      <form:option value="I1">Submitted</form:option>
				                  </form:select> 
		               					
									</TD>
									<TD valign=bottom >
									<span style="FLOAT: left">
								    <c:if test="${empty param.printFriendly}">
					    				<input type="submit" name="Submit" value="search">
		                				<input type="submit" name="reset" value="reset" onclick="resetFilters();" />
					                </c:if>
									</span>									  
									</TD>									
								</TR>	
													
							</TABLE>
						</TD>
						<TD class=databorder>
							<IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
						</TD>						
					</TR>
					<tr class="tablehead">
						<c:if test="${empty theReport.details}">
							<td class="tableheadTD" colspan="14" height=25><B><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></td>
						</c:if>
						<c:if test="${not empty theReport.details}">
						
						<td class="tableheadTD" colspan="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="1">
						<B><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></B></td>
						<td class="tableheadTD" valign="middle" colspan="4">
		          		<b><report:recordCounter report="theReport" label="Submissions "/></b>
          				</td>          
          				<td align="right" colspan="4" style="color: white;"><report:pageCounter report="theReport" formName="pifSubmissionReportForm" /></td>
						<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						
						<c:if test="${param.printFriendly}">
							<td class="databorder" colspan="14" height=25></td>
						</c:if>
						</c:if>
        			</tr>	
                    <TR class=tablesubhead>
                      <TD class=databorder height=25><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=actions></TD>
                      <TD class=submissionNumber vAlign=top height=25 align=left> </TD>
                      <TD class=dataheaddivider height=25><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=type valign=middle align=left height=25>
                      	 <report:sort field="submissionStatus" direction="desc" formName="pifSubmissionReportForm"> 
                      	<B>Submission Status</B>
                      	</report:sort> 
                      </TD>
                      <TD class=dataheaddivider height=25><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=payrollDate vAlign=middle height=25 align=left>	
                       <report:sort field="contractName" direction="desc" formName="pifSubmissionReportForm">
                      	<B>Contract Name</B>
                       </report:sort>  		  
                      							
                      </TD>
                      <TD class=dataheaddivider height=25><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=payrollDate vAlign=middle height=25 align=left>
                       <report:sort field="contractNumber" direction="desc" formName="pifSubmissionReportForm">
                      	<B>Contract Number</B>
                       </report:sort>
                      	</TD>
                      <TD class=dataheaddivider height=25><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=contributionTotal height=25 valign=middle align=left>
                        <report:sort field="dateUpdated" direction="asc" formName="pifSubmissionReportForm">
                        <B>Date Updated</B>
                        </report:sort>
                        </TD>
                      <TD class=dataheaddivider height=25><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=status valign=middle align=left height=25><B>User Name</B> 
                      </TD>
                      <TD class=databorder height=25><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD></TR>
					<c:set var="addLinkFlag" value="false"/>
					<c:set var="row1" value="datacell1"/>
					<c:set var="row2" value="datacell2"/>
					<c:if test="${empty isError && showLink == 'true'}" >
						<c:set var="addLinkFlag" value="true"/>
					</c:if>
					<c:if test="${addLinkFlag == 'true'}" >
					<c:set var="row1" value="datacell2"/>
					<c:set var="row2" value="datacell1"/>
					<tr class="datacell1" height=25 >
						<TD class=databorder height=25 ><IMG src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
						<td colspan="2" ></td>
						<td colspan="10" align=left>
                    	<a href="javascript:doAddPlanInfo();" >
						<content:getAttribute beanName="addPlanInfoLink" attribute="text"> 
                    		<content:param>${pifSubmissionReportForm.contractId}</content:param>
                    	</content:getAttribute>                    	</a>
						</td>
						<TD class=databorder height=25><IMG src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
					</tr>						
                    </c:if>
                  <c:if test="${empty theReport.details}">
					<tr class="datacell1" height=25 >
						<TD class=databorder height=25><IMG src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
						<td colspan="2" ></td>
						<td colspan="10" align=left>	
 						<content:getAttribute beanName="submissionMessage" attribute="text"/>		                    		
						</td>
						<TD class=databorder height=25><IMG src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
					</tr>		
                    </c:if>   
                    <c:if test="${not empty theReport.details}">
                    <%-- report data iteration part --%>
                    
                    <c:forEach varStatus="details" items="${theReport.details}" var="submission"> 
                    
                    <c:set var="indexValue" value="${details.index}"/>
                    <TR 
                    <c:if test="${indexValue % 2 == 0}">
					class=<c:out value="${row1}" />
					</c:if>
					<c:if test="${indexValue % 2 == 1}">
					class=<c:out value="${row2}" />
					</c:if>
					height=25>
                      <TD class=databorder><IMG src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=actions align=middle>
                      	<c:if test="${empty param.printFriendly}">
                        <TABLE border=0 cellSpacing=0 cellPadding=1>
                          <TBODY>
                          <TR>
                            <TD>
                            <c:if test="${(submission.processStatusCode eq '14') and (submission.contractStatusCode ne 'DI')}">
                            <A href="#" onclick='doEditPlanInfo(${submission.submissionId});' ><IMG 
                              title=Edit border=0 alt=Edit src="/assets/unmanaged/images/edit_icon.gif"></A>
                            </c:if>  
                            </TD>
                            <TD>
                            <c:if test="${(submission.processStatusCode eq '14') and (submission.contractStatusCode ne 'DI')}">
                            <ps:isInternalUser name="userProfile" property="role">
                            	<IMG alt=Delete border=0 src='/assets/unmanaged/images/delete_icon.gif' title="<content:getAttribute beanName="deleteIconInternalUsers" attribute="text"/>"
                    			 />
                            </ps:isInternalUser>  
                            <c:if test="${submission.contractStatusCode ne 'DI'}">                          
                            <ps:isExternal name="userProfile" property="role">
                            <c:set var="temp" value= "${StringEscapeUtils.escapeJavaScript(submission.getUserName())}" />
							<A onclick="doDelete('${submission.contractNumber}', '${submission.submissionId}', '<render:date property="submission.lastSubmittedTS"/>', '${submission.userName}');" 
                              href="#"><IMG 
                              title=Delete border=0 alt=Delete src="/assets/unmanaged/images/delete_icon.gif"></A>
                             
                            </ps:isExternal> 
                            
  
                            
                            </c:if> 
                             </c:if> 
                             </TD>
                          </TR></TBODY></TABLE>
                          </c:if>
                      </TD>
                      <TD class=submissionNumber noWrap align=left> </TD>
                      <TD class=datadivider><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=type align=left>
                       ${submission.processStatusDesc}
                      </TD>
                      <TD class=datadivider><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=payrollDate align=left>
                       ${submission.contractName}
                      </TD>                      
                      <TD class=datadivider><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=payrollDate align=left>
						${submission.contractNumber}
                      </TD>
                      <TD class=datadivider><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=paymentTotal valign=center align=left>  
                        <render:date property="submission.lastSubmittedTS"/>
                      </TD>
                      <TD class=datadivider><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=status align=left>
                      ${submission.userName}

                      </TD>
                      <TD class=databorder><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                    </TR>
                    </c:forEach>
                    </c:if>
                    <TR>
                      <TD class=databorder colSpan=14><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                    </TR>
                    <c:if test="${not empty theReport.details}">
						<c:if test="${empty param.printFriendly}">
						<tr>
						<td colspan="5"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="dark_grey_color" valign="middle" colspan="4">
		          		<b><report:recordCounter report="theReport" label="Submissions "/></b>
          				</td>          
          				<td align="right" class="dark_grey_color" colspan="4"><report:pageCounter report="theReport" arrowColor="black" formName="pifSubmissionReportForm" /></td>
						<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
						</c:if>
					</c:if>
                </TBODY></TABLE></TD>
                <TD width=10><IMG border=0 
                  src="/assets/unmanaged/images/spacer.gif" 
                  width=15 height=1></TD>
                <TD vAlign=top width=190 align=middle><IMG 
                  src="/assets/unmanaged/images/s.gif" 
                  width=1 height=25> <IMG 
                  src="/assets/unmanaged/images/s.gif" 
                  width=1 height=5> <IMG 
                  src="/assets/unmanaged/images/s.gif" 
                  width=1 height=5> </TD></TR>
              <TR>
                <TD></TD>
                <TD width=10><IMG border=0 
                  src="/assets/unmanaged/images/spacer.gif" 
                  width=15 height=1></TD>
                <TD>&nbsp;</TD>				
              </TR>
                </TBODY></TABLE>
	</ps:form><BR>
	<TR>
	   <TD><IMG border=0 
	     src="/assets/unmanaged/images/s.gif" 
	     width=30 height=1></TD>
	   <TD> </TD>
	   <TD width=10><IMG border=0 
	     src="/assets/unmanaged/images/spacer.gif" 
	     width=15 height=1></TD>
	   <TD>&nbsp;</TD>
	</TR>

    <c:if test="${not empty param.printFriendly}" >
	<content:contentBean contentId="${contentConstants.GLOBAL_DISCLOSURE}"  type="${contentConstants.TYPE_MISCELLANEOUS}"
        id="globalDisclosure"/>
	<tr>
		<td width="100%" colspan="2"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
	</tr>
	</c:if>
    </TD></TR></TBODY></TABLE>
<%--end table content --%>

</body>
</html>
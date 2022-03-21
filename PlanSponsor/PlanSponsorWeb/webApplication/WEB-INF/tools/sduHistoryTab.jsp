<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>       
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%@page import="com.manulife.pension.ps.web.tools.SDUHistoryTabController"%>
<%@page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUHistoryTabForm" %>
<%@page import="com.manulife.pension.platform.web.secureDocumentUpload.SDUHistoryTabReportData" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Collections"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collection"%>
<%@page import="org.springframework.hateoas.PagedResources.PageMetadata"%>


<%
	SDUHistoryTabReportData theReport = (SDUHistoryTabReportData)request.getAttribute(SDUHistoryTabController.HISTORY_RESULTS);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

<jsp:useBean id="sduHistoryTabForm"
             scope="session" 
             class="com.manulife.pension.platform.web.secureDocumentUpload.SDUHistoryTabForm" />

<content:contentBean contentId="<%=ContentConstants.SDU_HISTORY_TAB_INTRO%>" 
					 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="SDUHistoryTabIntro" />
<content:contentBean contentId="<%=ContentConstants.SDU_HISTORY_NOT_AVAILABLE_MESSAGE%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="NoResultsMessage"/>

<script type="text/javascript">

function filterSubmit(){
	document.forms['sduHistoryTabForm'].elements['task'].value = "filter";
	document.forms['sduHistoryTabForm'].elements['pageNumber'].value = 1;
	if(document.getElementsByName('justMine').checked){
		setFilterFromInput(document.getElementsByName('justMine').value);
	}
	else {
		document.getElementsByName('justMine').value = false;
		setFilterFromInput(document.getElementsByName('justMine').value);
	}
	}

function sortSubmit(sortField, sortDirection) {
	document.forms['sduHistoryTabForm'].elements['task'].value = "sort";
	document.forms['sduHistoryTabForm'].elements['pageNumber'].value = 1;
	document.forms['sduHistoryTabForm'].elements['sortField'].value = sortField;
	document.forms['sduHistoryTabForm'].elements['sortDirection'].value = sortDirection;
	document.forms['sduHistoryTabForm'].submit();
}

function pagingSubmit(pageNumber){
	if (document.forms['sduHistoryTabForm']) {
		document.forms['sduHistoryTabForm'].elements['task'].value = "page";
		document.forms['sduHistoryTabForm'].elements['pageNumber'].value = pageNumber;
		document.forms['sduHistoryTabForm'].submit();
	}
}

function validateCalInput(event) { 
    var key = event.which;
	//47 to 57 : /0123456789
	//0 : all non printable chars (del,right arrow, left arrow)
	//8 : Backspace
	//3 & 22 : ctrl+c ctrl+v	
    if ((key>=47 && key<=57) || key === 0 || key === 3 || key === 22 || key === 8){
		 return true;	
	}
	else{
		event.preventDefault();
        return false;	
	}
} 

</script>


<p>
	<content:errors scope="request" />
</p>

<table width="700">
<tbody>
	<tr>
		<td width="10"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
        <td>&nbsp;</td>
    </tr>
</tbody>
</table>

    <!-- Tab selection -->
    <c:if test="${empty param.printFriendly}">
    <jsp:include page="sduNavigationBar.jsp" flush="true">
        <jsp:param name="selectedTab" value="HistoryTab" />
    </jsp:include>
    </c:if>
    <c:if test="${not empty param.printFriendly}">
    <jsp:include page="sduNavigationBar.jsp" flush="true">
        <jsp:param name="selectedTab" value="HistoryPrintTab" />
    </jsp:include>
    </c:if>
    
    <!-- Tab selection -->


<ps:form cssStyle="margin-bottom:0;" method="POST" modelAttribute="sduHistoryTabForm" name="sduHistoryTabForm" action="/do/tools/secureDocumentUpload/history/">

<input type="hidden" name="task" value="default"/>
<form:hidden path="pageNumber"/>
<form:hidden path="sortField"/>
<form:hidden path="sortDirection"/>

    <table width="700" class="tableBorder" style="table-layout:fixed; padding: 1px; background-color: rgb(204, 204, 204);" border="0" cellspacing="0" cellpadding="0">
        <tbody>
            <tr>
                <td height="16" class="tablesubhead" colspan="11"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
            </tr>
            <tr>
                <td class="submissionId" width="80"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td class="submissionTs" width="80"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td class="fileName" width="150"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td class="clientUserName" width="110"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td class="clientUserRole" width="120"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td width="1"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td class="submissionStatus" width="127"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
            </tr>
            <!-- SDUHistoryTabIntro -->
            <tr class="datacell1">
                <td colspan="11">
                    <table border="0" cellspacing="0" cellpadding="5">
                        <tbody>
                            <tr>
                                <td width="700" height="30" class="datacell1" style="padding: 4px;">
                                    <content:getAttribute beanName="SDUHistoryTabIntro" attribute="text" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            <!-- SDUHistoryTabIntro -->
            <!-- Table records for pagination -->
            <tr class="tablehead">
                <td height="25" class="tableheadTD" colspan="11">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tbody>
                            <tr>
                                <td width="300" class="tableheadTD" style="padding-left: 2px;"><b>Document submissions</b></td>
                                <td class="tableheadTDinfo"><b><report:recordCounter report="theReport" label=""/></b></td>
                                <td class="tableheadTDinfo"></td>
								<c:if test="${empty param.printFriendly}">
	                                <td align="right" class="tableheadTD">
	                                	<report:pageCounterViaSubmit report="theReport" arrowColor="white" name="parameterMap" formName="sduHistoryTabForm"/>
	                                </td>
								</c:if>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            <!-- Table records total section for pagination -->

            <!--  selection fields for data-->
            <tr class="datacell1">
                <td colspan="11">
                    <table border="0" cellspacing="0" cellpadding="0">
                        <tbody>
                            <tr>
                                <td width="25" class="datacell1"  style="padding-bottom: 15px; padding-left: 4px;" align="right">
                                    <strong>from</strong>
                                </td>
                                <td width="115" class="datacell1">
                                    <table>
                                        <tbody>
                                            <tr>
                                            <c:if test="${not empty param.printFriendly}">
                                              <td width="85%" colspan="2" valign="middle">
                                                    <form:input path="filterStartDate" maxlength="10" size="10" tabindex="8" onkeypress="return validateCalInput(event)" readonly="true"/> 
                                                        <a tabindex="9"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date" valign="top"></a>
                                                </td>

                                            </c:if>
                                            <c:if test="${empty param.printFriendly}">
                                              <td width="85%" colspan="2" valign="middle">
                                                    <form:input path="filterStartDate" maxlength="10" size="10" tabindex="8" onkeypress="return validateCalInput(event)"/> 
                                                        <a href="javascript:calFromDate.popup();" tabindex="9"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date" valign="top"></a>
                                                </td>

                                            </c:if>
                                               
                                            </tr>
                                            <tr>
                                                <td class="datacell1" valign="top">(mm/dd/yyyy)</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </td>
                                <td width="12" class="datacell1"  style="padding-bottom: 15px; padding-left: 4px;" align="right">
                                    <strong>to</strong>
                                </td>
                                <td width="115" class="datacell1">
                                    <table>
                                        <tbody>
                                            <tr>
                                             <c:if test="${not empty param.printFriendly}">
                                                <td width="85%" colspan="2" valign="midlle">                    
													<form:input path="filterEndDate" maxlength="10" size="10" tabindex="10" onkeypress="return validateCalInput(event)" readonly="true"/> 
                                                    <a tabindex="11"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date" valign="top"></a>
                                                </td>
                                              </c:if>
                                               <c:if test="${empty param.printFriendly}">
                                                <td width="85%" colspan="2" valign="midlle">                    
													<form:input path="filterEndDate" maxlength="10" size="10" tabindex="10" onkeypress="return validateCalInput(event)" /> 
                                                    <a href="javascript:calToDate.popup();" tabindex="11"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date" valign="top"></a>
                                                </td>
                                              </c:if>
                                            </tr>
                                            <tr>
                                                <td class="datacell1" valign="top">(mm/dd/yyyy)</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </td>
                                 <td width="100" class="datacell1" style="padding-bottom: 15px; padding-left: 10px;" align="left">
                               		<c:if test="${sduHistoryTabForm.justMineFilter == true }">
										<form:checkbox path="justMine" value="true" disabled="true"/>&nbsp;Just mine
									</c:if>	
                                 	<c:if test="${not empty param.printFriendly}">
										<c:if test="${sduHistoryTabForm.justMineFilter == false }">
											<form:checkbox path="justMine" value="true" disabled="true"/>&nbsp;Just mine
										</c:if>		
									</c:if>	
									<c:if test="${empty param.printFriendly}">
										<c:if test="${sduHistoryTabForm.justMineFilter == false }">
											<form:checkbox path="justMine" value="true"/>&nbsp;Just mine
										</c:if>		
									</c:if>	
																													
                                </td>
                                 <c:if test="${not empty param.printFriendly}">
                                <td width="70" class="datacell1" style="padding-bottom: 15px;">
                                    <input type="submit" onclick="javascript:filterSubmit();" name="submitButton" class="button89x21" value="search" disabled="disabled"/>
                                </td>
                                </c:if>
                                <c:if test="${empty param.printFriendly}">
                                <td width="70" class="datacell1" style="padding-bottom: 15px;">
                                    <input type="submit" onclick="javascript:filterSubmit();" name="submitButton" class="button89x21" value="search"/>
                                </td>
                                </c:if>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
           
            <!--  Header section  for fields -->
            <tr class="tablesubhead">

                <table width="700" class="tableBorder" style="table-layout:fixed; padding: 1px; background-color: rgb(204, 204, 204);" border="0" cellspacing="0" cellpadding="0">
                    <tr class="tablesubhead">
                        <td valign="top" align="left" class="submissionId" style="padding-left: 4px;" width="71">
                            <b>
							<report:sortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMISSION_ID%>" 
							direction="desc">Submission<br/>number</report:sortLinkViaSubmit>							
							</b>
                        </td>
                        <td class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                        <td valign="top" align="left" class="submissionTs" style="padding-left: 4px;" width="71">
                            <b>
							<report:sortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMISSION_DATE%>" 
							direction="desc">Submission<br/>date/time</report:sortLinkViaSubmit>							
							</b>
                        </td>

                        <td class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                        <td valign="top" align="left" class="fileName" style="padding-left: 4px;" width="206">
                            <b>
							<report:sortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_DOCUMENT_NAME%>" 
							direction="asc">Document name</report:sortLinkViaSubmit>
							</b>
                        </td>
                        <td class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                        <td valign="top" align="left" class="clientUserName" style="padding-left: 4px;" width="126">
                            <b>
							<report:sortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMITTER_NAME%>" 
							direction="asc">Submitted by</report:sortLinkViaSubmit>
							</b>
                        </td>
                        <td class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                        <td valign="top" align="left" class="clientUserRole" style="padding-left: 4px;" width="121">
                            <b>
							<report:sortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMITTER_ROLE%>" 
							direction="asc">Role </report:sortLinkViaSubmit>
							</b>
                        </td>
                        <td class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                        <td valign="top" align="left" class="submissionStatus" style="padding-left: 4px;" width="72">
                            <b>
							<report:sortLinkViaSubmit formName="sduHistoryTabForm" field="<%=SDUHistoryTabReportData.SORT_SUBMISSION_STATUS%>" 
							direction="asc">Upload<br/>status </report:sortLinkViaSubmit>
							</b>
                        </td>
                    </tr>
                  
     <!--  Report data display -->
                <c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
                <c:if test="${ theIndex.index % 2 == 0}">
                	<tr class="datacell1">                
                </c:if>
                <c:if test="${ theIndex.index % 2 != 0}">
                	<tr class="datacell2">                
                </c:if>					
				<td align="left" class="submissionId" nowrap="nowrap" style="padding-left: 4px;">&nbsp;${theItem.submissionId} </td>
                <td class="datadivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td align="left" class="submissionTs" style="padding-left: 4px;">
                	<render:date property="theItem.submissionTs" patternOut="MM/dd/yyyy'<br>'hh:mm a" defaultValue=""/>
                </td>                  
                <td class="datadivider"><img width="1" height="0" src="/assets/unmanaged/images/s.gif"></td>
                <td align="left" class="fileName" style=" word-wrap: break-word; padding-left: 4px;"> ${theItem.fileName} </td>
                <td class="datadivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td align="left" class="clientUserName" style="padding-left: 4px;">${theItem.clientUserName} </td>
                <td class="datadivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td align="left" class="clientUserRole" style="padding-left: 4px;">${theItem.clientUserRole} </td>
                <td class="datadivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                <td align="left" class="submissionStatus" style="padding-left: 4px;" colspan="2"> ${theItem.submissionStatus} </td>
					
					</tr>
					</c:forEach>					
					<!--  report data end -->
					<!--  Display message when the result row count is 0 -->
					<c:if test="${theReport.totalCount == 0}">
					<tr class="datacell1">
						   <td valign="top" colspan="11">
						  	<b><content:getAttribute beanName="NoResultsMessage" attribute="text" /></b>
						  </td>
					</tr>
				   </c:if>
      
                </table>
            </tr>
        </tbody>
        <tr>
            <td width="10"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
            <td>&nbsp;</td>
        </tr>
    </table>
</ps:form>

<c:if test="${not empty param.printFriendly}">
						<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
							type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
							id="globalDisclosure" />

						<table width="760" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="760"><content:getAttribute beanName="globalDisclosure" attribute="text" /></td>
							</tr>
						</table>
</c:if>
<script type="text/javascript">
    // create calendar object(s) just after form tag closed
    var calFromDate = new calendar(document.forms['sduHistoryTabForm'].elements['filterStartDate']);
    calFromDate.year_scroll = true;
    calFromDate.time_comp = false;
    var calToDate = new calendar(document.forms['sduHistoryTabForm'].elements['filterEndDate']);
    calToDate.year_scroll = true;
    calToDate.time_comp = false;
</script>	
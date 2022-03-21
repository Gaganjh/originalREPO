<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="manulife/tags/content" prefix="content" %>
<%@ taglib uri="manulife/tags/ps" prefix="ps" %> 
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.util.SessionHelper"%>
<%@ page import="com.manulife.pension.ps.web.sendservice.NoticeActiveMailReportForm"%>
<%@ page import="com.manulife.pension.service.request.valueobject.NoticeRequest"%>
<%@ page import="com.manulife.pension.service.request.valueobject.NoticeRequestHistory"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.sendservice.valueobject.SendServiceReportData" %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <STYLE>
				.datacell2 {
					padding-top: 5px;
					padding-bottom: 5px;
					background-color: #efefef;
				}
				.on{
					padding-top: 2px !important;
				}
				.enabledButton{
					cursor: pointer !important;
				}
				.buttonDisable p{
					cursor: default;
				}
				.enabledButton p{
					cursor: pointer !important;
				}
				</STYLE>

<script language="JavaScript" type="text/javascript"
				src="/assets/unmanaged/javascript/tooltip.js"></script>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<content:contentBean contentId="91895" type="${contentConstants.TYPE_MISCELLANEOUS}" id="SEND_SERVICE_REGENERATE_WARNING" />
<content:contentBean contentId="91896" type="${contentConstants.TYPE_MISCELLANEOUS}" id="SEND_SERVICE_CANCEL_WARNING" />
<content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_NO_ACTIVE_MAILING%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="noActiveMail"/>
 <content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_ACTIVE_MAILING_INTRO%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
                     id="activeMailIntro"/>
 <content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_ACTIVE_MAILING_HEADER%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
                     id="activeMailHeader"/>
 <content:contentBean
		contentId="<%=ContentConstants.SEND_SERVICE_FOOTER%>"
		type="<%=ContentConstants.TYPE_LAYOUT_PAGE%>" id="sendServiceFooter" />

<%

SendServiceReportData theReport = (SendServiceReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 
 <c:if test="${not empty theReport}">
 
</c:if>
<ps:form cssClass="margin-bottom:0;" method="POST" action="/do/sendservice/activeMail/" modelAttribute="noticeActiveMailReportForm" name="noticeActiveMailReportForm" enctype="multipart/form-data">
<input  type="hidden" name="task">
<input  type="hidden" name="selectedTrackingNumber">
<input  type="hidden" name="sourceStatus">

	<table border="0" cellspacing="0" cellpadding="0" width="730px">
		<tbody>
			<tr>
				<td class="" colspan="6">
				<table>
					<tr>
						<td colspan="2">
						<div class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
						<ps:messages scope="session" 
							maxHeight="${param.printFriendly ? '1000px' : '100px'}"
							suppressDuplicateMessages="true" />
						</div>
						</td>
					</tr><tr><td colspan="2">&nbsp;</td></tr>
				</table></td>											
		    </tr>
			<tr>
				<jsp:include page="noticedistributiontab.jsp" flush="true">
					<jsp:param value="1" name="tabValue" />
				</jsp:include>
		</tr>
		<tr>
		<TD colspan="3">
		 <TABLE width="700" class="tableBorder" style="padding: 1px; background-color: #002D62;" border="0" cellspacing="0" cellpadding="0">
            <TBODY>
              <TR>
                <TD height="16" class="tableheadTD1" colspan="3"><IMG width="1" 
            height="1" src="/assets/unmanaged/images/s.gif"></TD>
              </TR>
              <TR>
                <TD width="60"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="1"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="410"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="1"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="60"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="1"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="170"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="1"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              </TR>
              <TR class="datacell1">
                <TD colspan="12"><TABLE border="0" cellspacing="0" cellpadding="5">
                    <TBODY>
                      <TR>
                        <TD style="padding-left: 7px;"> <content:getAttribute beanName="activeMailIntro" attribute="text" /> </TD>
                      </TR>
                    </TBODY>
                  </TABLE>
                </TD>
              </TR>
<c:if test="${userProfile.internalUser ==true}">
              <TR class="datacell1">
                <TD align="left" colspan="12"><DIV style="padding-left: 7px; margin-top: 6px; float: left;"><STRONG>Legend</STRONG>:&nbsp;<IMG width="12" height="12" src="/assets/unmanaged/images/regenerate_icon.gif">&nbsp;Regenerate&nbsp;&nbsp;<IMG src="/assets/unmanaged/images/cancel_icon.gif" border="0">&nbsp;Cancel&nbsp;&nbsp; </DIV>
				</TD>
              </TR>
</c:if>
              <!-- Start of body title -->
              <TR class="tablehead">
                <TD class="tableheadTD1" colspan="12">
                <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                    <TBODY>
                      <TR>
                        <TD class="tableheadTD" colspan="1" style="width: 258px; height: 14px;"><content:getAttribute beanName="activeMailHeader" attribute="text" /></TD>
                        <TD align="left" style="padding-right: 10px;" colspan="9" class="tableheadTDinfo"><b><report:sendServiceRecordCounter report="theReport"
											label="Active Mailings" /></b></TD>
						<td align="right" colspan="2" class="tableheadTDinfo"><b> <report:sendServicePageCounter formName="noticeActiveMailReportForm" name="noticeActiveMailReportForm" report="theReport" /></b></td>
                      </TR>
                    </TBODY>
                  </TABLE></TD>
              </TR>
              <!-- End of body title -->
              <TR>
                <TD colspan="12">
                	<TABLE width="100%" border="0" cellspacing="0" cellpadding="2">
                    <THEAD>
                      <TR class="tablesubhead">
<c:if test="${userProfile.internalUser ==true}">
                         <TD width="34"><B>Action</B></TD>
</c:if>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="100"> <B>Mailing Name</B></TD>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="100"><p title="It's important to provide complete address information for all employees listed in the Employee File. Employees with a missing address will not receive a mailing. Plan Sponsors will be responsible for providing the notice to those employees."><B>Employee File</B></p></TD>
	                    <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="100" onmouseover="Tip(applicableDtMouseOver)" onmouseout="UnTip()"><report:sort formName="noticeActiveMailReportForm" field="<%=NoticeRequest.SORT_FIELD_APPL_DATE%>" direction="desc"><B>Applicable Date</B></report:sort></TD>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="65"><report:sort formName="noticeActiveMailReportForm" field="<%=NoticeRequest.SORT_FIELD_STATUS%>" direction="desc"><B>Status</B></report:sort></TD>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="80"><report:sort formName="noticeActiveMailReportForm" field="<%=NoticeRequest.SORT_FIELD_STATUS_DATE%>" direction="desc"><B>Status Date</B></report:sort></TD>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="80"><B>Order #</B></TD>
                      </TR>
                    </THEAD>
                    <TBODY>
                    <c:if test="${not empty noticeActiveMailReportForm.noticeRequestList }">
<c:forEach items="${noticeActiveMailReportForm.noticeRequestList}" var="noticeRequest" varStatus="index">
<c:if test="${noticeRequest.noticeStatus == 'PN'}">
                    	  		<TR class="datacell2" style="vertical-align: top;">
<c:if test="${userProfile.internalUser ==true}">
	                        	<TD width="34"><IMG width="3" height="12" src="/assets/unmanaged/images/s.gif">&nbsp;</TD>
</c:if>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
		                    <TD>
<c:if test="${noticeRequest.noticeTypeCode =='ANNUAL'}">Annual</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='ELGBLE'}">Newly Eligible</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CNVRSN'}">Conversion</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CHANGE'}">Admin Expense Change Notice</c:if></TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>Employee File</TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
<c:if test="${noticeRequest.noticeTypeCode =='ANNUAL'}"> <p title="Plan year start date"></c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CNVRSN'}"> <p title="Blackout start date"></c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CHANGE'}"> <p title="Fee effective date"></c:if>
<fmt:formatDate pattern="MM/dd/yyyy" value="${noticeRequest.noticeEffectiveDate}" />
 <%-- format="MM/dd/yyyy" --%>
	                        	 <c:if test="${(noticeRequest.noticeTypeCode eq 'ANNUAL' or noticeRequest.noticeTypeCode eq 'ELGBLE'  or noticeRequest.noticeTypeCode eq 'CNVRSN')}">
	                         	   </p>
	                        	 </c:if>
	                        </TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>	
<c:if test="${noticeRequest.noticeStatus =='PN'}">Upcoming</c:if>
	                   		</TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
<TD><p title="Preview period begins:
<fmt:formatDate pattern="MM/dd/yyyy" value="${noticeRequest.previewStartDate}" />
 <%-- format="MM/dd/yyyy" --%>">
 <fmt:formatDate pattern="MM/dd/yyyy" value="${noticeRequest.upcomingDisplayDate}" />
 <%-- format="MM/dd/yyyy" --%></p></TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
<c:if test="${noticeRequest.merrillOrderNo ==0}">
	                        		 <p title="JH Tracking Number: ${noticeRequest.orderNo}">Pending</p> 
</c:if>
	                        </TD>
	                      	</TR>
</c:if>
</c:forEach>
                   </c:if>
                   
              	<c:if test="${not empty noticeActiveMailReportForm.noticeRequestList }">
<c:forEach items="${noticeActiveMailReportForm.noticeRequestList}" var="noticeRequest" varStatus="theIndex">
 <c:set var="indexValue" value="${theIndex.index}"/> 
 <%String temp = pageContext.getAttribute("indexValue").toString(); %>
<c:if test="${noticeRequest.noticeStatus !='PN'}">
                       	 <c:if test="${not (noticeRequest.noticeStatus eq 'RG')}">
                       		 <c:if test="${indexValue %2==0}">
   							<TR class="datacell3" style="vertical-align: top;">
   						</c:if>
						 <c:if test="${indexValue % 2 == 1}">
    						<TR class="datacell1" style="vertical-align: top;">
						</c:if> 
<c:if test="${userProfile.internalUser ==true}">
                     			<TD width="34"><IMG width="3" height="12" src="/assets/unmanaged/images/s.gif">
                     			 
	                        		<c:if test="${noticeRequest.noticeStatus eq 'FE' or noticeRequest.noticeStatus eq 'ER' or noticeRequest.noticeStatus eq 'CN'}">
	                        		<c:if test="${empty param.printFriendly }" >
<a href="#" id = '${noticeRequest.orderNo}' onclick="doRegenerateNotice('regenerateNotice','${noticeRequest.orderNo}','${noticeRequest.noticeStatus}'); return false;"></c:if>
	                						<IMG width="12" height="12" src="/assets/unmanaged/images/regenerate_icon.gif">
	                       				</a>
	                       			</c:if>        	
	                       	 	<c:if test="${noticeRequest.noticeStatus eq 'PR' or noticeRequest.noticeStatus eq 'GN' and userProfile.internalUser eq true}">
	                       	 	<c:if test="${empty param.printFriendly }" >
<a href="#" id = '${noticeRequest.orderNo}' onclick="doCancelNotice('cancelNotice','${noticeRequest.orderNo}','${noticeRequest.noticeStatus}'); return false;"></c:if>
	                         			<IMG src="/assets/unmanaged/images/cancel_icon.gif" border="0"></a>
	                         	</c:if> 	 
	                        </TD>
</c:if>
	                       	
	                        <TD width="1" class="datadivider">
	                        	<IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
	                        
	                        	<c:if test="${noticeRequest.noticeStatus eq 'CN' or noticeRequest.noticeStatus eq 'RG'}">
<c:if test="${noticeRequest.noticeTypeCode =='ANNUAL'}">Annual</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='ELGBLE'}">Newly Eligible</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CNVRSN'}">Conversion</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CHANGE'}">Admin Expense Change Notice</c:if>
	                        	</c:if>
	                        	
	                        	<c:if test="${not (noticeRequest.noticeStatus eq 'CN' or noticeRequest.noticeStatus eq 'RG')}">
	                        	<c:if test="${empty param.printFriendly }" >
<a href="#" onclick="doViewPDF('previewNoticeDocument','${noticeRequest.orderNo}'); return false;"></c:if>
<c:if test="${noticeRequest.noticeTypeCode =='ANNUAL'}">Annual</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='ELGBLE'}">Newly Eligible</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CNVRSN'}">Conversion</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CHANGE'}">Admin Expense Change Notice</c:if>
	                        	</a>
	                        	</c:if>
	                        	
	                        </TD>
	                        <TD width="1" class="datadivider">
	                        	<IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
	                        	<c:if test="${noticeRequest.noticeStatus eq 'CN' or noticeRequest.noticeStatus eq 'RG'}">
	                        	Employee File
	                        	</c:if>
	                        	<c:if test="${not (noticeRequest.noticeStatus eq 'CN' or noticeRequest.noticeStatus eq 'RG')}">
	                        	<c:if test="${empty param.printFriendly }" >
<a href="#" onclick="doViewCSV('previewNoticeDocument','${noticeRequest.orderNo}'); return false;"></c:if>
	                        	Employee File
<c:if test="${(noticeRequest.noticeStatus eq 'IN' or noticeRequest.noticeStatus eq 'SN'or noticeRequest.noticeStatus eq 'IP'or noticeRequest.noticeStatus eq 'FE' or noticeRequest.noticeStatus eq 'RS')}">(${noticeRequest.participantCount})</c:if>
	                        	</a>
	                        	
	                        	</c:if>
	                        	
	                        </TD>
	                      	
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
<c:if test="${noticeRequest.noticeTypeCode =='ANNUAL'}"> <p title="Plan year start date"></c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CNVRSN'}"> <p title="Blackout start date"></c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CHANGE'}"> <p title="Fee effective date"></c:if>
	                        

<fmt:formatDate pattern="MM/dd/yyyy" value="${noticeRequest.noticeEffectiveDate}" />
	                        	 <c:if test="${(noticeRequest.noticeTypeCode eq 'ANNUAL' or noticeRequest.noticeTypeCode eq 'ELGBLE'  or noticeRequest.noticeTypeCode eq 'CNVRSN')}">
	                         		</p>
	                        	 </c:if>
	                        </TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD> 
	                        	<c:if test="${(noticeRequest.noticeStatus eq 'PR')}">Preview</c:if>
	                          	<c:if test="${(noticeRequest.noticeStatus eq 'IN')}">Initiated</c:if>
	                           	<c:if test="${(noticeRequest.noticeStatus eq 'SN')}">Initiated</c:if>
	                           	<c:if test="${(noticeRequest.noticeStatus eq 'RS')}">Initiated</c:if>
	                           	<c:if test="${(noticeRequest.noticeStatus eq 'IP')}">In Progress</c:if>
	                           	<c:if test="${(noticeRequest.noticeStatus eq 'GN')}">Preview </c:if>
	                           	<c:if test="${(noticeRequest.noticeStatus eq 'CN')}">
<c:if test="${noticeRequest.merrillOrderNo ==0}">
	                           <p title="Requested by plan. ${noticeRequest.profileId} cancelled at John Hancock.">Cancelled
	                           </p>
</c:if>
<c:if test="${noticeRequest.merrillOrderNo !=0}">
	                           <p title="Requested by plan. John Hancock cancelled at Merrill.">Cancelled
	                           </p>
</c:if>
	                           </c:if>
	                           
<c:if test="${userProfile.internalUser ==true}">
<c:if test="${noticeRequest.noticeStatus =='FE'}">File Error </c:if>
<c:if test="${noticeRequest.noticeStatus =='ER'}">File Error</c:if>
</c:if>
<c:if test="${userProfile.internalUser ==false}">
<c:if test="${noticeRequest.noticeStatus =='FE'}">Initiated</c:if>
<c:if test="${noticeRequest.noticeStatus =='ER'}">Preview</c:if>
</c:if>
	                        </TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
	                        <c:if test="${not((noticeRequest.noticeStatus eq 'ER'))}">
<fmt:formatDate pattern="MM/dd/yyyy" value="${noticeRequest.noticeRequestHistory.noticeStatusDate}" />	                        
 <%-- format="MM/dd/yyyy" --%>
	                        </c:if>
	                         <c:if test="${(noticeRequest.noticeStatus eq 'ER') and (userProfile.internalUser eq true)}">
<fmt:formatDate pattern="MM/dd/yyyy" value="${noticeRequest.noticeRequestHistory.noticeStatusDate}" />
 <%-- format="MM/dd/yyyy" --%>
	                        </c:if>
	                        <c:if test="${(noticeRequest.noticeStatus eq 'ER') and (userProfile.internalUser eq false)}">
<fmt:formatDate pattern="MM/dd/yyyy" value="${noticeRequest.previewStartDate}" />
 <%-- format="MM/dd/yyyy" --%>
	                        </c:if>
	                        </TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
<c:if test="${userProfile.internalUser ==true}">
	                        	<c:if test="${not(noticeRequest.noticeStatus eq 'CN' or noticeRequest.noticeStatus eq 'IP')}">
<c:if test="${noticeRequest.merrillOrderNo ==0}">
		                        		 <p title="JH Tracking Number: ${noticeRequest.orderNo}">Pending</p> 
</c:if>
		                        	</c:if>
		                        	<c:if test="${(noticeRequest.noticeStatus eq 'CN')}">
<c:if test="${noticeRequest.merrillOrderNo ==0}">
		                        		 <p title="JH Tracking Number: ${noticeRequest.orderNo}">N/A</p> 
</c:if>
		                        	<c:if test="${(noticeRequest.noticeStatus eq 'CN')}">
<c:if test="${noticeRequest.merrillOrderNo !=0}">
		                        		 <p title="JH Tracking Number: ${noticeRequest.orderNo}">N/A</p> 
</c:if>
		                        	</c:if>
		                        	</c:if>
		                        	<c:if test="${(noticeRequest.noticeStatus eq 'IP')}">
<c:if test="${noticeRequest.merrillOrderNo !=0}">
<p title="JH Tracking Number: ${noticeRequest.orderNo}">${noticeRequest.merrillOrderNo}</p>
</c:if>
	                        	</c:if>
</c:if>
<c:if test="${userProfile.internalUser ==false}">
	                        	<c:if test="${not(noticeRequest.noticeStatus eq 'CN' or noticeRequest.noticeStatus eq 'IP')}">
<c:if test="${noticeRequest.merrillOrderNo ==0}">
		                        		 <p>Pending</p> 
</c:if>
		                        	</c:if>
		                        	<c:if test="${(noticeRequest.noticeStatus eq 'CN')}">
<c:if test="${noticeRequest.merrillOrderNo ==0}">
		                        		 <p>N/A</p> 
</c:if>
		                        	<c:if test="${(noticeRequest.noticeStatus eq 'CN')}">
<c:if test="${noticeRequest.merrillOrderNo !=0}">
		                        		 <p>N/A</p> 
</c:if>
		                        	</c:if>
		                        	</c:if>
		                        	<c:if test="${(noticeRequest.noticeStatus eq 'IP')}">
<c:if test="${noticeRequest.merrillOrderNo !=0}">
${noticeRequest.merrillOrderNo}
</c:if>
	                        	</c:if>
</c:if>
	                       </TD> 
	                       </c:if>
</c:if>
</c:forEach>
                      </c:if>
<c:if test="${empty noticeActiveMailReportForm.noticeRequestList}">
                         <TR class="datacell1" style="vertical-align: top;">
<c:if test="${userProfile.internalUser ==true}">
	                        <TD colspan="15"><content:getAttribute beanName="noActiveMail" attribute="text" /></TD>
</c:if>
<c:if test="${userProfile.internalUser ==false}">
	                        <TD colspan="14"><content:getAttribute beanName="noActiveMail" attribute="text" /></TD>
</c:if>
	                        </TR>
</c:if>
                    </TBODY>
                  </TABLE>
                  </TD>
                  </TR> 
            </TBODY>
          </TABLE>
          <table width="700" border="0" cellspacing="0" cellpadding="0">
          <tr class="">
          		<td class="" colSpan="1"></td>
	        	<td class="" colspan="9" style="padding-left: 264px;"><b> <report:sendServiceRecordCounter report="theReport" label="Active Mailings" /></b></TD> 
						<td align="right" colspan="2"  class=""><b><report:sendServicePageCounter formName="noticeActiveMailReportForm" name="noticeActiveMailReportForm" report="theReport" arrowColor="black" /></b></td>
		    </tr> 
		    </table>
		</TD>
		</tr>
		</tbody> 
	</table>
	<br/>
	<table cellpadding="0" cellspacing="0" border="0" width="730px">
			<tr>				
				<td width="100%"><content:getAttribute beanName="sendServiceFooter" attribute="footer1"/></td>
			</tr>
	</table>
</ps:form>
<c:if test="${not empty param.printFriendly}" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<br/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute
				beanName="globalDisclosure" attribute="text" /></td>
		</tr>
	</table>
</c:if>



<script>
	  var utilities = {
	      
	      // Asynchronous request call to the server. 
	      doAsyncRequest : function(actionPath, callbackFunction, data) {
	          YAHOO.util.Connect.setForm(document.noticeActiveMailReportForm);
	          // Make a request
	          var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction, data);
	      },
	      
	      // Generic function to handle a failure in the server response  
	      handleFailure : function(o){ 
	          o.argument = null;
	          utilities.hideWaitPanel();
	  		clearInterval(intervalId);
	      },
	      
	      // Shows loading panel message
	      showWaitPanel : function() {
	          waitPanel = document.getElementById("wait_c");
	          if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
	              loadingPanel = new YAHOO.widget.Panel("wait",  
	                                  {   width: "250px", 
	                                      height:"50px",
	                                      fixedcenter: true, 
	                                      close: false, 
	                                      draggable: false, 
	                                      zindex:4,
	                                      modal: true,
	                                      visible: false,
	                                      constraintoviewport: true
	                                  } 
	                              );
	              loadingPanel.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
	              loadingPanel.render(document.body);
	              loadingPanel.show();
	          }       
	      },

	 

	      /**
	      * hides the loading panel
	      */
	      hideWaitPanel: function () {	
	          loadingPanel.hide();	
	      }
	          
	   };

	function doViewPDF(action, trackingNumber) {
		
		document.getElementsByName("selectedTrackingNumber")[0].value = trackingNumber;
		document.getElementsByName("task")[0].value= action;
		utilities.showWaitPanel();
  		utilities.doAsyncRequest("/do/sendservice/activeMail/?task=previewNoticeDocument&selectedTrackingNumber="+trackingNumber, callback_checkCustomPlanNoticeGenerated);
  	}
    // Call back handler to Check whether Custom plan Report Generation is complete.
	var callback_checkCustomPlanNoticeGenerated =    {
		success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/sendservice/activeMail/?task=fetchPdf";
				utilities.hideWaitPanel();
			}else{
				window.location.href = "/do/sendservice/activeMail/?task=errorReport";
				utilities.hideWaitPanel();
			}
			},
	    cache : false,
	    failure : utilities.handleFailure
	};

	
	
	function doViewCSV(action,trackingNumber) {
		utilities.showWaitPanel();
		document.getElementsByName("task")[0].value= action;
			utilities.doAsyncRequest("/do/sendservice/activeMail/?task=previewEmployeeList&selectedTrackingNumber="+trackingNumber, callback_checkCSVGenerated);
		
	    
	}
	
	var callback_checkCSVGenerated =    {
		success:  function(o) { 
			if(o.responseText == 'csvGenerated'){
				window.location.href = "/do/sendservice/activeMail/?task=fetchCSV";
				utilities.hideWaitPanel();
			}else{
				window.location.href = "/do/sendservice/activeMail/?task=errorReport";
				utilities.hideWaitPanel();
			}
			},
	    cache : false,
	    failure : utilities.handleFailure
	};

	 
	function doRegenerateNotice(action,trackingNumber,sourceStatusCode) {
	      var reportURL = new URL("/do/sendservice/activeMail/");
	   	  var response = confirm(SEND_SERVICE_REGENERATE_WARNING);
	      if (response == true) {
				document.getElementById(trackingNumber).style.display='none';
	    		document.noticeActiveMailReportForm.task.value="regenerateNotice";
	    		document.noticeActiveMailReportForm.sourceStatus.value = sourceStatusCode;
	    		document.noticeActiveMailReportForm.selectedTrackingNumber.value=trackingNumber;
	    		document.noticeActiveMailReportForm.submit();
	     }
	    }     
	
	function doCancelNotice(action,trackingNumber,sourceStatusCode) {
	      var reportURL = new URL("/do/sendservice/activeMail/");
	   	  var response = confirm(SEND_SERVICE_CANCEL_WARNING);
	      if (response == true) {
				document.getElementById(trackingNumber).style.display='none';
	    		document.noticeActiveMailReportForm.task.value="cancelNotice";
	    		document.noticeActiveMailReportForm.sourceStatus.value = sourceStatusCode;
	    		document.noticeActiveMailReportForm.selectedTrackingNumber.value=trackingNumber;
	    		document.noticeActiveMailReportForm.submit();
	     }
	    } 
	var SEND_SERVICE_REGENERATE_WARNING = "<content:getAttribute id='SEND_SERVICE_REGENERATE_WARNING' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
	var SEND_SERVICE_CANCEL_WARNING = "<content:getAttribute id='SEND_SERVICE_CANCEL_WARNING' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
	var applicableDtMouseOver = 'Applicable dates for each mailing: <br><b style="font-size:11px">Annual:</b>  Plan year start date <br><b style="font-size:11px">Conversion:</b> Blackout start date <br><b style="font-size:11px">Change:</b> Fee effective date';	
</script>

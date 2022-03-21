<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="manulife/tags/content" prefix="content" %>
<%@ taglib uri="manulife/tags/ps" prefix="ps" %> 
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.util.SessionHelper"%>
<%@ page import="com.manulife.pension.ps.web.sendservice.NoticeCompletedMailReportForm"%>
<%@ page import="com.manulife.pension.service.request.valueobject.NoticeRequest"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%@ page import="com.manulife.pension.ps.service.report.sendservice.valueobject.SendServiceReportData" %>
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<content:contentBean contentId="91970" type="${contentConstants.TYPE_MISCELLANEOUS}" id="SEND_SERVICE_UPDATE_WARNING" />
<content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_NO_ACTIVE_MAILING%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="noActiveMail"/>
  <content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_COMPLETED_MAILING_INTRO%>"
 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
 id="completedMailIntro"/>
 <content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_ACTIVE_MAILING_HEADER%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
                     id="activeMailHeader"/>
 <content:contentBean
		contentId="<%=ContentConstants.SEND_SERVICE_FOOTER%>"
		type="<%=ContentConstants.TYPE_LAYOUT_PAGE%>" id="sendServiceFooter" />
<script language="JavaScript" type="text/javascript"
				src="/assets/unmanaged/javascript/tooltip.js"></script>
<script>

	  var utilities = {
	      
	      // Asynchronous request call to the server. 
	      doAsyncRequest : function(actionPath, callbackFunction, data) {
	          YAHOO.util.Connect.setForm(document.noticeCompletedMailReportForm);
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
  		utilities.doAsyncRequest("/do/sendservice/completedMail/?task=previewNoticeDocument&selectedTrackingNumber="+trackingNumber, callback_checkCustomPlanNoticeGenerated);
  	}
    // Call back handler to Check whether Custom plan Report Generation is complete.
	var callback_checkCustomPlanNoticeGenerated =    {
		success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/sendservice/completedMail/?task=fetchPdf";
				utilities.hideWaitPanel();
			}else{
				window.location.href = "/do/sendservice/completedMail/?task=errorReport";
				utilities.hideWaitPanel();
			}
			},
	    cache : false,
	    failure : utilities.handleFailure
	};

	
	
	function doViewCSV(action,trackingNumber) {
		utilities.showWaitPanel();
		document.getElementsByName("task")[0].value= action;
			utilities.doAsyncRequest("/do/sendservice/completedMail/?task=previewEmployeeList&selectedTrackingNumber="+trackingNumber, callback_checkCSVGenerated);
		
	    
	}
	
	var callback_checkCSVGenerated =    {
		success:  function(o) { 
			if(o.responseText == 'csvGenerated'){
				window.location.href = "/do/sendservice/completedMail/?task=fetchCSV";
				utilities.hideWaitPanel();
			}else{
				window.location.href = "/do/sendservice/completedMail/?task=errorReport";
				utilities.hideWaitPanel();
			}
			},
	    cache : false,
	    failure : utilities.handleFailure
	};

	function doUpdateNotice(action,trackingNumber,sourceStatusCode) {
	      var reportURL = new URL("/do/sendservice/completedMail/");
	   	  var response = confirm(SEND_SERVICE_UPDATE_WARNING);
	      if (response == true) {
				document.getElementById(trackingNumber).style.display='none';
	    		document.noticeCompletedMailReportForm.task.value="updateNotice";
	    		document.noticeCompletedMailReportForm.sourceStatus.value = sourceStatusCode;
	    		document.noticeCompletedMailReportForm.selectedTrackingNumber.value=trackingNumber;
	    		document.noticeCompletedMailReportForm.submit();
	     }
	    } 
	
	var SEND_SERVICE_UPDATE_WARNING = "<content:getAttribute id='SEND_SERVICE_UPDATE_WARNING' attribute='text'  filter='true' escapeJavaScript='true'></content:getAttribute>";
	var applicableDtMouseOver = 'Applicable dates for each mailing: <br><b style="font-size:11px">Annual:</b>  Plan year start date <br><b style="font-size:11px">Conversion:</b> Blackout start date <br><b style="font-size:11px">Change:</b> Fee effective date';
</script>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
SendServiceReportData theReport = (SendServiceReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

%> 
<c:if test="${not empty theReport}">
</c:if>
<ps:form cssClass="margin-bottom:0;" method="POST" action="/do/sendservice/completedMail/" modelAttribute="noticeCompletedMailReportForm" name="noticeCompletedMailReportForm" enctype="multipart/form-data">
<input  type="hidden" name="task">
<input  type="hidden" name="selectedTrackingNumber">
<input  type="hidden" name="sourceStatus">
<%
NoticeCompletedMailReportForm noticeCompletedMailReportForm = (NoticeCompletedMailReportForm)pageContext.getAttribute("noticeCompletedMailReportForm");
%>
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
				 <TD colspan="3"><DIV class="nav_Main_display" id="div">
                  <UL>
                    <LI id="tab1" style="color: black !important;" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    <c:choose>
                    	<c:when test="${param.printFriendly}">
                    		<SPAN>Active Mailings</SPAN>
                    	</c:when>
                    	<c:otherwise>
                    		<A href="/do/sendservice/activeMail/"><SPAN>Active Mailings</SPAN></a>
                    	</c:otherwise>
                   	</c:choose>
                   	</LI>
                    <LI class="on" id="tab2" style="color: black !important;"><SPAN>Completed Mailings</SPAN></LI>
                    <LI id="tab3" style="color: black !important;" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    <c:choose>
                    	<c:when test="${param.printFriendly}">
                    		<SPAN>Plan Details</SPAN>
                    	</c:when>
                    	<c:otherwise>
                    		<A href="/do/sendservice/planData/"><SPAN>Plan Details</SPAN></A>
                    	</c:otherwise>
                   	</c:choose>
                   	</LI>
                  </UL>
                </DIV></TD>

		</tr>
		<tr>
		<TD colspan="3">
		 <TABLE width="700" class="tableBorder" style="padding: 1px; background-color: #002D62" border="0" cellspacing="0" cellpadding="0">
            <TBODY>
              <TR>
                <TD height="16" class="tableheadTD1" colspan="12"><IMG width="1" 
            height="1" src="/assets/unmanaged/images/s.gif"></TD>
              </TR>
              <TR>
                <TD width="30"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="1"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="390"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="1"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="50"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="1"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="150"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="1"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                <TD width="70"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
              </TR>
              <TR class="datacell1">
                <TD colspan="12"><TABLE border="0" cellspacing="0" cellpadding="5">
                    <TBODY>
                      <TR>
                        <TD style="padding-left: 7px;"> <content:getAttribute beanName="completedMailIntro" attribute="text" /></TD>
                      </TR>
                    </TBODY>
                  </TABLE>
                </TD>
              </TR>
              <TR class="datacell1">
<c:if test="${userProfile.internalUser ==true}">
                <TD align="left" colspan="12"><DIV style="padding-left: 7px; margin-top: 6px; float: left;"><STRONG>Legend</STRONG>:&nbsp;<IMG width="12" height="12" src="/assets/unmanaged/images/update_icon.gif">&nbsp;Update</DIV>
                </TD>
</c:if>
              </TR>
              <!-- Start of body title -->
              <TR class="tablehead">
                <TD class="tableheadTD1" colspan="12"><TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                    <TBODY>
                      <TR>
                        <TD class="tableheadTD" colspan="1" style="width: 258px; height: 14px;"><content:getAttribute beanName="activeMailHeader" attribute="text" />
                        <TD align="left" style="padding-right: 10px;" colspan="9" class="tableheadTDinfo"><b>  <report:sendServiceRecordCounter report="theReport" 
											label="Completed Mailings" /></b> </TD>
						<td align="right" colspan="2" class="tableheadTDinfo"><b> <report:sendServicePageCounter  formName="noticeCompletedMailReportForm" name="noticeCompletedMailReportForm" report="theReport" /></b>
                        </TD>
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
                        <TD width="100" ><B>Mailing Name</B></TD>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="100"><p title="It's important to provide complete address information for all employees listed in the Employee File. Employees with a missing address will not receive a mailing. Plan Sponsors will be responsible for providing the notice to those employees"><B>Employee File</B></p></TD>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="100" onmouseover="Tip(applicableDtMouseOver)" onmouseout="UnTip()"><report:sort formName="noticeCompletedMailReportForm" field="<%=NoticeRequest.SORT_FIELD_APPL_DATE%>" direction="desc"><B>Applicable Date</B></report:sort></TD>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="65"><report:sort formName="noticeCompletedMailReportForm" field="<%=NoticeRequest.SORT_FIELD_STATUS%>" direction="desc"><B>Status</B></report:sort></TD>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="80"><report:sort formName="noticeCompletedMailReportForm" field="<%=NoticeRequest.SORT_FIELD_STATUS_DATE%>" direction="desc"><B>Status Date</B></report:sort></TD>
                        <TD width="1" class="dataheaddivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
                        <TD width="94"><B>Order #</B></TD>
                      </TR>
                    </THEAD>
                   
                    <TBODY>
                     <c:if test="${not empty noticeCompletedMailReportForm.noticeRequestList }">
<c:forEach items="${noticeCompletedMailReportForm.noticeRequestList}" var="noticeRequest" varStatus="index">
    <c:set var="indexValue" value="${index.index}"/> 
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
 
 %>
 
   
                      <c:if test="${not(userProfile.internalUser eq 'false' and (noticeRequest.noticeStatus eq 'UP' or noticeRequest.noticeStatus eq 'UN' or  noticeRequest.noticeStatus eq 'UE'))}">
                       	 	 <% if (Integer.parseInt(indexVal) % 2 == 0) { %>
    						<TR class="datacell1" style="vertical-align: top;">
    						<% } else { %>
	    						<TR class="datacell2" style="vertical-align: top;">
							 <% } %>
<c:if test="${userProfile.internalUser ==true}">
	                      	  		<TD width="34"><IMG width="3" height="12" src="/assets/unmanaged/images/s.gif">	    
	                      	  			<c:if test="${noticeRequest.noticeStatus eq 'CM'}">
	                      	  			 	 <c:if test="${not(noticeRequest.updateInd eq 'false' or noticeRequest.noticeTypeCode eq 'CNVRSN')}">
<a href="#" id = '${noticeRequest.orderNo}' onclick="doUpdateNotice('updateNotice','${noticeRequest.orderNo}','${noticeRequest.noticeStatus}'); return false;">
	                      	  					<IMG width="12" height="12" src="/assets/unmanaged/images/update_icon.gif"></a>
	                      	  				</c:if>
	                      	  		   </c:if>
	                      	  	  </TD>
</c:if>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
	                        <c:if test="${not(noticeRequest.noticeStatus eq 'CM' or noticeRequest.noticeStatus eq 'NM')}">
<c:if test="${noticeRequest.noticeTypeCode =='ANNUAL'}">Annual</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='ELGBLE'}">Newly Eligible</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CNVRSN'}">Conversion</c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CHANGE'}">Admin Expense Change Notice</c:if>
	                        	</c:if>
	                        	
	                        	<c:if test="${noticeRequest.noticeStatus eq 'CM' or noticeRequest.noticeStatus eq 'NM'}">
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
	                        	<c:if test="${(noticeRequest.noticeStatus eq 'CM' or noticeRequest.noticeStatus eq 'NM')}">
	                        	<c:if test="${empty param.printFriendly }" >
<a href="#" onclick="doViewCSV('previewEmployeeList','${noticeRequest.orderNo}'); return false;"></c:if>
Employee File(${noticeRequest.participantCount})
	                        	</a>
	                        	</c:if>
	                        	
	                        	<c:if test="${not(noticeRequest.noticeStatus eq 'CM' or noticeRequest.noticeStatus eq 'NM')}">
	                        	Employee File
<c:if test="${(noticeRequest.noticeStatus eq 'NE')}">(${noticeRequest.participantCount})</c:if>
	                        		</c:if>
	                        	
	                        </TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
<c:if test="${noticeRequest.noticeTypeCode =='ANNUAL'}"> <p title="Plan year start date"></c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CNVRSN'}"> <p title="Blackout start date"></c:if>
<c:if test="${noticeRequest.noticeTypeCode =='CHANGE'}"> <p title="Fee effective date"></c:if>
<fmt:formatDate pattern="MM/dd/yyyy" value="${noticeRequest.noticeEffectiveDate}" />
	                        </TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD> 
<c:if test="${noticeRequest.noticeStatus =='CM'}">
<c:if test="${noticeRequest.originalOrderNumber !=0}"> <p title="This mailing was regenerated."></c:if>
	                       	Completed</c:if>
<c:if test="${noticeRequest.noticeStatus =='CN'}">
<c:if test="${noticeRequest.merrillOrderNo ==0}">
<c:if test="${noticeRequest.previousStatusCode =='FE'}">
			                           		<p title="Cancelled at John Hancock due to System Error.">Cancelled</p>
</c:if>
<c:if test="${noticeRequest.previousStatusCode =='ER'}">
			                           		<p title="Cancelled at John Hancock due to System Error.">Cancelled</p>
</c:if>
<c:if test="${noticeRequest.previousStatusCode =='PR'}">
			                           		<p title="Requested by plan. ${noticeRequest.profileId} cancelled at John Hancock.">Cancelled</p>
</c:if>
<c:if test="${noticeRequest.previousStatusCode =='IP'}">
			                               	 <p title="Requested by plan. John Hancock cancelled at Merrill.">Cancelled
			                         	 	 </p>
</c:if>
</c:if>
<c:if test="${noticeRequest.merrillOrderNo !=0}">
			                          	 <p title="Requested by plan. John Hancock cancelled at Merrill.">Cancelled
			                         	  </p>
</c:if>
</c:if>
<c:if test="${noticeRequest.noticeStatus =='NE'}">No Mail Recipients</c:if>
<c:if test="${noticeRequest.noticeStatus =='NM'}">Updated</c:if>
	                           	                           
<c:if test="${userProfile.internalUser ==true}">
<c:if test="${noticeRequest.noticeStatus =='UP'}">Updated Preview</c:if>
<c:if test="${noticeRequest.noticeStatus =='UN'}">Updated Preview</c:if>
<c:if test="${noticeRequest.noticeStatus =='UE'}">Update Error</c:if>
</c:if>
	                           </TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
<TD><fmt:formatDate pattern="MM/dd/yyyy" value="${noticeRequest.noticeRequestHistory.noticeStatusDate}" />
 </TD>
	                        <TD width="1" class="datadivider"><IMG width="1" height="1" src="/assets/unmanaged/images/s.gif"></TD>
	                        <TD>
		                        <c:if test="${not(noticeRequest.noticeStatus eq 'CM')}">
<c:if test="${userProfile.internalUser ==true}">
<c:if test="${noticeRequest.merrillOrderNo ==0}">
			                        		 <p title="JH Tracking Number: ${noticeRequest.orderNo}">N/A</p> 
</c:if>
</c:if>
<c:if test="${userProfile.internalUser ==false}">
<c:if test="${noticeRequest.merrillOrderNo ==0}">
			                        		 <p>N/A</p> 
</c:if>
</c:if>
		                        </c:if>
		                        <c:if test="${noticeRequest.noticeStatus eq 'CM'}">
<c:if test="${noticeRequest.merrillOrderNo !=0}">
<c:if test="${userProfile.internalUser ==true}">
<p title="JH Tracking Number: ${noticeRequest.orderNo}">${noticeRequest.merrillOrderNo}</p>
</c:if>
<c:if test="${userProfile.internalUser ==false}">
${noticeRequest.merrillOrderNo}
</c:if>
</c:if>
		                        </c:if>
	                        </TD>
	                      </TR>
                       	 	
                       </c:if>
                       		
</c:forEach>
                      </c:if>
<c:if test="${empty noticeCompletedMailReportForm.noticeRequestList}">
                         <TR class="datacell1" style="vertical-align: top;">
<c:if test="${userProfile.internalUser ==true}">
	                        <TD colspan="15"><content:getAttribute beanName="noActiveMail" attribute="text" /></TD>
</c:if>
<c:if test="${userProfile.internalUser ==false}">
	                        <TD colspan="14"><content:getAttribute beanName="noActiveMail" attribute="text" /></TD>
</c:if>
</c:if>
                    </TBODY>
                  </TABLE></TD>
              </TR>
            </TBODY>
          </TABLE>
           <table width="700" border="0" cellspacing="0" cellpadding="0">
          <tr class="">
          		<td class="" colSpan="1"></td>
	        	<td class="" colspan="9" style="padding-left: 264px;"><b> <report:sendServiceRecordCounter report="theReport"  label="Completed Mailings" /></b></TD>
						<td align="right" colspan="2"  class=""><b> <report:sendServicePageCounter formName="noticeCompletedMailReportForm" name="noticeCompletedMailReportForm" report="theReport" arrowColor="black" /></b></td>
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
<c:if test="${not empty param.printFriendly }" >
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

 <style>
 DIV.nav_Main_display LI.on {
 	color: black !important;
    font-size: 12px !important;
}
DIV.nav_Main_display LI {
    font-size: 12px !important;
    color: black !important;
    line-height: 13px;
}
DIV.nav_Main_display #tab1.on {
    height: 33px;    
}
DIV.nav_Main_display A:hover {
    color: black !important;
}
DIV.nav_Main_display A:visited {
    color: black !important;
}
DIV.nav_Main_display SPAN {
    color: black !important;
}
</style>	

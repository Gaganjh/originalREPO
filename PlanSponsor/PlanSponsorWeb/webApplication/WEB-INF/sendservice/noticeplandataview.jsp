<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %> 
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.sendservice.NoticePlanDataForm"%>
<%@ page import="com.manulife.pension.service.request.valueobject.NoticeRequest" %>

<c:set var="disableFields" scope="request" value="true"/>
<c:set var="disableFieldsForContributions" scope="request" value="true"/>
 <content:contentBean contentId="91990"    type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"   id="intro"/>
 <content:contentBean
		contentId="<%=ContentConstants.SEND_SERVICE_FOOTER%>"
		type="<%=ContentConstants.TYPE_LAYOUT_PAGE%>" id="sendServiceFooter" />
<content:contentBean contentId="<%=ContentConstants.SEND_SERVICE_NOTICE_GENERATION_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="sendServiceLink" />

<c:set var="form" scope="request" value="noticePlanDataForm"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<ps:form cssClass="margin-bottom:0;" method="POST" action="/do/sendservice/planData/" modelAttribute="noticePlanDataForm" name="noticePlanDataForm" enctype="multipart/form-data">
<input type="hidden" name="action" /><%--  input - name="noticePlanDataForm" --%>
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
							disableWarningPopup = "${param.printFriendly ? false : true}"
							suppressDuplicateMessages="true" />
						</div>
						</td>
					</tr><tr><td colspan="2">&nbsp;</td></tr>
				</table>
				</td>											
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
                    <LI id="tab2" style="color: black !important;" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    <c:choose>
                    	<c:when test="${param.printFriendly}">
                    		<SPAN>Completed Mailings</SPAN>
                    	</c:when>
                    	<c:otherwise>
                    		<A href="/do/sendservice/completedMail/"><SPAN>Completed Mailings</SPAN></A>
                    	</c:otherwise>
                   	</c:choose>
                   	</LI>
                    <LI class="on" id="tab3" style="color: black !important;"><SPAN>Plan Details</SPAN></LI>
                  </UL>
                </DIV></TD>
			</tr>
			<tr>
				<TD colspan="3">
				 <TABLE width="700" class="tableBorder" style="padding: 1px; background-color: #002D62;" border="0" cellspacing="0" cellpadding="0">
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
                        <TD style="padding-left: 7px;"> <content:getAttribute beanName="intro" attribute="text" /> </TD>
                      </TR>
                    </TBODY>
                  </TABLE>
                </TD>
              </TR>
              
              <c:if test="${noticePlanDataForm.eligibleNotice}">
	              <TR class="datacell1">
	                <TD colspan="12">
	                	<TABLE border="0" cellspacing="0" cellpadding="5">
	                    <TBODY>
	                      <TR>
							<TD style="padding-left: 7px;">
								<content:getAttribute beanName="sendServiceLink" attribute="text">
									<% String sendServiceLinkParam = "doViewPDF('previewNoticeDocument','1'); return false;"; %>
					  				<content:param><%=sendServiceLinkParam%></content:param>
								</content:getAttribute>
							</TD> 
						 </TR>
	                    </TBODY>
	                  </TABLE>
	                </TD>
	              </TR>	
              </c:if>
              
              <TR class="datacell1">
                <TD align="left" colspan="12">
                </TD>
              </TR>
              <!-- Start of body title -->
              <TR >
                <TD colspan="12">
					  <c:if test="${not param.printFriendly}">
      						<jsp:include flush="true" page="expandCollapseAllSections.jsp"></jsp:include>
   					</c:if>
   						  <jsp:include flush="true" page="plandatasummary.jsp"></jsp:include>
   						  <jsp:include flush="true" page="contributionAndDistribution.jsp"></jsp:include>
   						   <jsp:include flush="true" page="safeHarbor.jsp"></jsp:include>
   						   <jsp:include flush="true" page="automaticContribution.jsp"></jsp:include>
   						     <jsp:include flush="true" page="investmentInformation.jsp"></jsp:include> 
   						       <jsp:include flush="true" page="contactInformation.jsp"></jsp:include>
   						  <jsp:include flush="true" page="commonHandlers.jsp"></jsp:include>
   						  <jsp:include flush="true" page="viewUpdates.jsp"></jsp:include>
				</TD>
				
				
				
			</tr>
		</tbody>
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
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">initViewPlanData();</script>

<script type="text/javascript">
var utilities = {
	      
	      // Asynchronous request call to the server. 
	      doAsyncRequest : function(actionPath, callbackFunction, data) {
	          YAHOO.util.Connect.setForm(document.getElementById("noticePlanDataForm"));
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
			document.getElementsByName("selectedTrackingNumber").value = trackingNumber;
			document.getElementsByName("action").value= action;
			utilities.showWaitPanel();
			utilities.doAsyncRequest("/do/sendservice/planData/?action=previewNoticeDocument&selectedTrackingNumber="+trackingNumber, callback_checkCustomPlanNoticeGenerated);
		}
		
		// Call back handler to Check whether Custom plan Report Generation is complete.
		var callback_checkCustomPlanNoticeGenerated =    {
			success:  function(o) { 
				if(o.responseText == 'pdfGenerated'){
					window.location.href = "/do/sendservice/planData/?action=fetchPdf";
					utilities.hideWaitPanel();
				}else{
					window.location.href = "/do/sendservice/planData/?action=errorReport";
					utilities.hideWaitPanel();
				}
				},
		    cache : false,
		    failure : utilities.handleFailure
		};
		
</script>

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

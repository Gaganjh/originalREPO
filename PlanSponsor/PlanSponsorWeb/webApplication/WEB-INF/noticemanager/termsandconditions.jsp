<!-- Author: Yeshwanth -->
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.noticemanager.TermsAndConditionsNoticeManagerForm" %>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<c:set var="printFriendly" value="${param.printFriendly}" />
  
    
    
<content:contentBean
  contentId="<%=ContentConstants.NMC_TERMS_OF_USE_FOOTER_TEXT%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="termsAndConditionsMessage" />
    
    <script>


    /**
     * Opens up a new window and perform the same request again (with printFriendly
     * parameter.
     */
    function doPrint()
    {
      var reportURL = new URL();
      reportURL.setParameter("task", "print");
      reportURL.setParameter("action", "print");
      reportURL.setParameter("printFriendly", "true");
      window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
    }
     
    /**
    * Method to set the action in the form and submit the from.
    * Called when the user clicks the button	
    */
   function doSubmit(action) {
	document.getElementsByName("action")[0].value= action;
    document.termsAndConditionsForm.submit();
   }
   
    
    </script>
    	<ps:form action="/do/noticemanager/termsandconditions/" method="POST" modelAttribute="termsAndConditionsForm" name="termsAndConditionsForm">
<form:hidden path="action" />
<form:hidden path="fromPage" />
		
		<content:errors scope="session"/>
		<table border="0" cellpadding="0" cellspacing="0" width="715">
				<tbody>	
					<tr>
						<td width="580"><img src="/assets/unmanaged/images/s.gif" height="1" width="580"></td>
						<td width="20"><img src="/assets/unmanaged/images/s.gif" height="1" width="20"></td>
						<td width="100"><img src="/assets/unmanaged/images/s.gif" height="1" width="100"></td>
					</tr>					
					<tr>
						<td colspan="3" width="580"><img src="/assets/unmanaged/images/s.gif" height="23" width="580"><br>
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td width="5"><img src="/assets/unmanaged/images/s.gif" height="1" width="5"></td>
										<td valign="top">
											<img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
										 </td>
										 
										 
										 <c:if test="${empty param.printFriendly }" >
									      <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
									      <td valign="top" class="right">
<c:set var="href" value="#"/>
<c:set var="onclick" value="return true;" />
											<table width="180" border="0" cellspacing="0" cellpadding="0" class="beigeBox">
											  <tr>
											    <td class="beigeBoxTD1">
											      <table width="100%" border="0" cellspacing="0" cellpadding="0">
											          <c:if test="${empty layoutBean.params['skipPrint']}">
												          <content:contentBean contentId="<%=ContentConstants.COMMON_PRINT_REPORT_TEXT%>"
												                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
												                               id="contentBean" override="true"/>
												          <tr>
												            <td width="17">
												              <a href="javascript:doPrint()">
															    <content:image id="contentBean" contentfile="image"/></a>
															</td>
												            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
												            <td>
												              <a href="javascript:doPrint()">
												                <content:getAttribute id="contentBean" attribute="text"/>
												              </a>
												            </td>
												          </tr>  
											          </c:if>
											      </table>
											    </td>
											  </tr>
											  <tr>
											    <td align="right"><img src="/assets/unmanaged/images/box_lr_corner_E9E2C3.gif" width="5" height="5"></td>
											   </tr>
											 </table>
								 	      </td>
								</c:if>
									</tr>
									<tr>
										<td width="5"><img src="/assets/unmanaged/images/s.gif" height="1" width="5"></td>
										<td valign="top" colspan="3" style="margin-top:4px;"><content:getAttribute beanName="layoutPageBean" attribute="body1"/></td>
									</tr>
									<c:if test="${empty param.printFriendly }" >
									<tr>
										<td width="580" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="580" height="23"><br>
											<table border="0" cellspacing="0" cellpadding="0" width="100%">
												<tbody>
													<tr>
														<td width="160">
															<div align="left" style="width: 160px;">
<input type="button" onclick="doSubmit('cancel')" name="button" class="button134" id="disagreeButton" value="I disagree"/>


															</div>										
														</td>
														<td width="160">
															<div align="left">
<input type="button" onclick="doSubmit('agree')" name="button" class="button134" id="agreeButton" value="I agree"/>


															</div>
														</td>
														<td width="260">
														</td>
														
													</tr>
												</tbody>
											</table>
										</td>
									</tr>	
									</c:if>				
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="3" width="580"><img src="/assets/unmanaged/images/s.gif" height="23" width="580"><br>
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td width="160">
											<div style="width: 160px;" align="left">
												
											</div>										
										</td>
										<td width="160">
											<div align="left">
												
											</div>
										</td>
										<td width="260">
										</td>
										
									</tr>
								</tbody>
							</table>
						</td>

					</tr>
				</tbody>
			</table>
		</ps:form>
		
<c:if test="${not empty param.printFriendly }" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute
				beanName="globalDisclosure" attribute="text" /></td>
		</tr>
	</table>
</c:if>	

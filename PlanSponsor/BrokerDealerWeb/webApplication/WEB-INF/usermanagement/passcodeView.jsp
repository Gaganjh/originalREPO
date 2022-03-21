<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/security" prefix="security" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<div id="contentFull">
<layout:pageHeader nameStyle="h1"/>
	<div class="BottomBorder">
      <div class="SubTitle Gold Left"><content:getAttribute attribute="subHeader" beanName="layoutPageBean" /></div>
    </div> 
	<c:if test="${requestScope.passcodeViewForm.success}">
<content:contentBean contentId="<%=BDContentConstants.RESET_PASSCODE_SUCCESS_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="successMessage" />
<div class="message message_info">
  <dl>
    <dt>Information Message</dt>
    <dd>1&nbsp;&nbsp;
      <content:getAttribute id='successMessage' attribute='text'>
       <content:param>${requestScope.passcodeViewForm.firstName} ${requestScope.passcodeViewForm.lastName}</content:param>
           </content:getAttribute> 
     </dd>
    </dl>
</div>

</c:if>

<report:formatMessages scope="request"/>
<bd:form action="/do/usermanagement/passcodeView" modelAttribute="passcodeViewForm" name="passcodeViewForm">

		
  <div>&nbsp;</div>
   <div class="label">First Name:</div>
  <div class="inputText">
    ${requestScope.passcodeViewForm.firstName} &nbsp;
  </div>
   <div class="label">Last Name:</div>
  <div class="inputText">
    ${requestScope.passcodeViewForm.lastName} &nbsp;
  </div>
  <div class="label">Current Email Address:</div>
  <div class="inputText">
    ${requestScope.passcodeViewForm.emailAddress} &nbsp;
  </div>
  <div class="label">Last Passcode Timestamp:</div>
  <div class="inputText">
      ${requestScope.passcodeViewForm.lastPasscodeTimestamp} &nbsp;
  </div>
  <div class="label">Last Passcode Destination:</div>
  <div class="inputText">
      ${requestScope.passcodeViewForm.lastPasscodeDestination} &nbsp;
    </div>
  
      <div class="label">Last Passcode Success:</div>
  <div class="inputText">
    ${requestScope.passcodeViewForm.lastPasscodeSuccess} &nbsp;
  </div>
    
  <div class="label">Last Passcode Failure:</div>
	  <div class="inputText">
          ${requestScope.passcodeViewForm.lastPasscodeFailure} &nbsp;
      </div>
      
  <div class="label">Passcode Status:</div>
  <div class="inputText">
   ${requestScope.passcodeViewForm.passcodeStatus} &nbsp;
  </div>
 
  <br class="clearFloat"/>
<div id="content">
    <c:choose>
     <c:when test="${not requestScope.passcodeViewForm.disabled}">
       <c:if test="${requestScope.passcodeViewForm.resetButton}"> 
       <div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Reset Passcode"
	        onclick="return doProtectedSubmitBtn(document.passcodeViewForm, 'unlockPasscode', this)">
        </div>  
        </c:if> 
         <c:if test="${not requestScope.passcodeViewForm.resetButton}">
         <div class="formButton">
		  <input type="button" class="disabled-grey-btn next" 
				 name="save" value="Reset Passcode"
				 disabled="disabled">
		</div> 
		</c:if>
     </c:when>
	 <c:otherwise>      
		<div class="formButton">
		  <input type="button" class="disabled-grey-btn next" 
				 name="save" value="Reset Passcode"
				 disabled="disabled">
		</div> 
      </c:otherwise>
     </c:choose>
     <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="back" value="Back"
             onclick="return doProtectedSubmitBtn(document.passcodeViewForm, 'cancel', this)"> 
    </div> 
   <input type="hidden" name="action">   
</div>
</bd:form>
</div>

<layout:pageFooter/>
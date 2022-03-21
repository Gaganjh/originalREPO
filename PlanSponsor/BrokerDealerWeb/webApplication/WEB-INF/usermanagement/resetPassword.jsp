<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags/security" prefix="security" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<content:contentBean contentId="<%=BDContentConstants.RESET_PWD_ACCESS_CODE_TEXT%>" 
     type="<%=BDContentConstants.TYPE_MESSAGE%>" id="accessCodeHelp" />

<div id="contentFull">
<layout:pageHeader nameStyle="h1"/>
<c:if test="${requestScope.resetPasswordForm.success}">
<content:contentBean contentId="<%=BDContentConstants.RESET_PWD_SUCCESS_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="successMessage" />
<div class="message message_info">
  <dl>
    <dt>Information Message</dt>
    <dd>1&nbsp;&nbsp;
     <content:getAttribute id='successMessage' attribute='text'>
       <content:param>${requestScope.resetPasswordForm.firstName} ${requestScope.resetPasswordForm.lastName}</content:param>
       <content:param>${requestScope.resetPasswordForm.emailAddress} </content:param>
     </content:getAttribute>
     </dd>
    </dl>
</div>

</c:if>

<report:formatMessages scope="request"/>
<bd:form action="/do/usermanagement/resetPassword" modelAttribute="resetPasswordForm" name="resetPasswordForm">

  <div>&nbsp;</div>
  <div class="label">First Name:</div>
  <div class="inputText">
    ${requestScope.resetPasswordForm.firstName} &nbsp;
  </div>
  <div class="label">Last Name:</div>
  <div class="inputText">
      ${requestScope.resetPasswordForm.lastName} &nbsp;
  </div>
  <div class="label">Email Address:</div>
  <div class="inputText">
      ${requestScope.resetPasswordForm.emailAddress} &nbsp;
    </div>
  
      <div class="label">User Role:</div>
  <div class="inputText">
    ${requestScope.resetPasswordForm.userRole} &nbsp;
  </div>
  
  <!-- It is done like this to fix an IE6 alignment issue. -->
   <c:choose>
      <c:when test="${not empty requestScope.resetPasswordForm.firms}">
	    <c:forEach var="firm" items="${requestScope.resetPasswordForm.firms}" varStatus="loopStatus">
	    	<c:choose>
		    	<c:when test="${loopStatus.first }">
		    	  <div class="label">Firm Name:</div>
				  <div class="inputText">
		       		${firm.firmName}
		       	  </div> 
		       	</c:when>     
		       	<c:otherwise>
		    	  <div class="label" style="margin-top:0px">&nbsp;</div>
				  <div class="inputText" style="margin-top:0px">
		       		${firm.firmName}
		       	  </div> 
		       	</c:otherwise>
		   </c:choose>
	    </c:forEach>
    </c:when>
    <c:otherwise>
      <div class="label">Firm Name:</div>
	  <div class="inputText">
          n/a
      </div>
    </c:otherwise>
 </c:choose>
 <c:choose>
      <c:when test="${not empty requestScope.resetPasswordForm.riaFirms}">
	    <c:forEach var="riaFirm" items="${requestScope.resetPasswordForm.riaFirms}" varStatus="loopStatus">
	    	<c:choose>
		    	<c:when test="${loopStatus.first }">
		    	  <div class="label">RIA Firm Name:</div>
				  <div class="inputText">
		       		${riaFirm.firmName}
		       	  </div> 
		       	</c:when>     
		       	<c:otherwise>
		    	  <div class="label" style="margin-top:0px">&nbsp;</div>
				  <div class="inputText" style="margin-top:0px">
		       		${riaFirm.firmName}
		       	  </div> 
		       	</c:otherwise>
		   </c:choose>
	    </c:forEach>
    </c:when>
    <c:otherwise>
      <div class="label">RIA Firm Name:</div>
	  <div class="inputText">
          n/a
      </div>
    </c:otherwise>
 </c:choose>
  
  <div class="label">Assistant to:</div>
  <div class="inputText">
   <c:choose>
       <c:when test="${not empty requestScope.resetPasswordForm.parentUserName}">
         ${requestScope.resetPasswordForm.parentUserName}
       </c:when>
       <c:otherwise>
         n/a
       </c:otherwise>
    </c:choose>
  </div>
  
  <div class="label">Producer Code:</div>
  <div class="inputText">
   <c:choose>
       <c:when test="${not empty requestScope.resetPasswordForm.producerCodes}">
	    <c:forEach var="pc" items="${requestScope.resetPasswordForm.producerCodes}" varStatus="loopStatus">
	       ${pc.id}
	       <c:if test="${not loopStatus.last}">
	       <br>
	       </c:if>       
	    </c:forEach>
	   </c:when>
	   <c:otherwise>
	     n/a
	   </c:otherwise>
	</c:choose>
  </div>

  <div class="label">* Access Code:</div>
  <div class="inputText">
<form:input path="accessCode" disabled="${requestScope.resetPasswordForm.disabled}" maxlength="25"/>
    	<br />
	<content:getAttribute beanName="accessCodeHelp" attribute="text"/>    
  </div>
  <br class="clearFloat"/>
<div id="content">
    <c:choose>
     <c:when test="${not requestScope.resetPasswordForm.disabled}">
       <div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Confirm Reset"
	        onclick="return doProtectedSubmitBtn(document.resetPasswordForm, 'resetPassword', this)">
        </div>     
     </c:when>
	 <c:otherwise>      
		<div class="formButton">
		  <input type="button" class="disabled-grey-btn next" 
				 name="save" value="Confirm Reset"
				 disabled="disabled">
		</div> 
      </c:otherwise>
     </c:choose>
     <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Cancel"
             onclick="return doProtectedSubmitBtn(document.resetPasswordForm, 'cancel', this)"> 
    </div> 
   <input type="hidden" name="action">   
</div>
</bd:form>
</div>

<layout:pageFooter/>

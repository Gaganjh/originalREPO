<%@tag import="com.manulife.pension.bd.web.util.BDWebCommonUtils"%>
<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@tag import="com.manulife.pension.bd.web.usermanagement.UserManagementHelper"%>
<%@tag import="com.manulife.pension.service.broker.valueobject.BrokerEntityExtendedProfile"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ attribute name="brokerEntityAssoc" type="com.manulife.pension.service.security.bd.valueobject.BrokerEntityAssoc" 
required="true" rtexprvalue="true"%>
<%@ attribute name="allowRemove" type="java.lang.Boolean" required="true" rtexprvalue="true"%>
<%@ attribute name="allowResend" type="java.lang.Boolean" required="true" rtexprvalue="true"%>
<%@ attribute name="enableResend" type="java.lang.Boolean" required="true" rtexprvalue="true"%>

<content:contentBean contentId="<%=BDContentConstants.PERSONAL_INFO_PRIMARY_CONTACT_INFO_TEXT%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="primaryContactInfoText" />

  <c:set var="brokerEntity" value="${brokerEntityAssoc.brokerEntity}"/>
<% 
BrokerEntityExtendedProfile brokerEntity = (BrokerEntityExtendedProfile)jspContext.getAttribute("brokerEntity");
%>
  

   <c:set var="isSsn" value="<%=BDWebCommonUtils.isSsn(brokerEntity.getSsnTaxId()) %>"/>
   <c:set var="maskedId" value="<%=BDWebCommonUtils.fullMaskSsn(brokerEntity.getSsnTaxId())%>"/>
   <c:set var="showCompanyName" value="<%=UserManagementHelper.showCompanyName(brokerEntity)%>"/>
   <c:set var="showPersonalName" value="<%=UserManagementHelper.showPersonalName(brokerEntity)%>"/>
   <c:set var="usaIndicator" value="<%=UserManagementHelper.getUSAIndicatorValue(brokerEntity)%>"/>	
   <c:set var="address" value="<%=UserManagementHelper.getAddress(brokerEntity)%>"/>
   <div class="table_controls" style="height:auto">
   <table width="100%">
     <tr>
       <td width="33%">
         <h1 class="SubTitle">
          <c:choose>
            <c:when test="${isSsn}">
              SSN:
            </c:when>
            <c:otherwise>
              Tax ID:
            </c:otherwise>
          </c:choose>
           ${maskedId}         
         </h1>
       </td>
       <td width="26%">
         <%-- Show as primary only when it is active and allow to be removed --%>
         <c:if test="${allowRemove && brokerEntityAssoc.primaryInd && brokerEntityAssoc.active}">
          <h1><content:getAttribute attribute="text" beanName="primaryContactInfoText"/> </h1>
         </c:if>
       </td>
       <td width="17%">
         <c:choose>         
         	<c:when test="${brokerEntityAssoc.active}">
          	<h1>Activated</h1>
          	</c:when>
          	<c:otherwise>
	          	 <c:if test="${allowResend}">
	          	 	<c:choose>
	          	 		<c:when test="${enableResend }">
				          	<div class="button_login">
				          	 <input type="button" style="width:120px; font-size:12px;" value="Resend activation" 
				          	      onclick="resendActivation(${brokerEntity.id},this)"/>
				          	</div>
		          		</c:when>
		          		<c:otherwise>
				          	<div class="button_disabled">
				          	 <input type="button" style="width:120px; font-size:12px;" value="Resend activation" 
				          	      disabled="disabled"/>
				          	</div>
		          		</c:otherwise>
		          	</c:choose>
	          	 </c:if>     
          	</c:otherwise>
         </c:choose>
       </td>
       <td width="24%">   
          <c:if test="${allowRemove}">
	       <div class="button_login">
	             <input name="button2" type="button" id="button" style="width:110px; font-size:12px;" 
	             onclick="removeParty(${brokerEntity.id}, this)" value="Remove" />
	        </div>
	      </c:if>
       </td>
     </tr>
   </table>
   </div>
   
   <c:if test="${not empty brokerEntity.brokerDealerFirms}">
   <div class="label">BD Firm Name:</div>
   <div class="inputText">
      <c:forEach var="firmName" items="<%=UserManagementHelper.getSortedBDFirmNames(brokerEntity.getBrokerDealerFirms()) %>" varStatus="loopStatus">
         ${firmName}
         <c:if test="${not loopStatus.end}"> 
           <br/>
         </c:if>
      </c:forEach>
    </div> 
   </c:if>
   <c:if test="${showCompanyName}">
     <div class="label">Company Name:</div>
     <div class="inputText">
       <label>${brokerEntity.orgName} &nbsp;</label>
     </div>
   </c:if>
   <c:if test="${showPersonalName}">
     <div class="label">First Name:</div>
     <div class="inputText">
       <label>${brokerEntity.firstName} &nbsp;</label>
     </div>
     <div class="label">Middle Initial:</div>
     <div class="inputText">
       <label>${brokerEntity.middleInit} &nbsp;</label>
     </div>
     <div class="label">Last Name:</div>
     <div class="inputText">
       <label>${brokerEntity.lastName} &nbsp;</label>
     </div>
   </c:if>
    <div class="label">Email:</div>
     <div class="inputText">
       <label>${brokerEntity.emailAddress}&nbsp;</label>
     </div>
    <div class="label">Mailing Address 1:</div>
     <div class="inputText">
       <label>${address.addressLine1}&nbsp;</label>
     </div>  
    <div class="label">Address 2:</div>
    <div class="inputText">
       <label>${address.addressLine2}&nbsp;</label>
    </div>
    <c:if test="${not empty address.addressLine3}">
    <div class="label">Address 3:</div>
     <div class="inputText">
       <label>${address.addressLine3}&nbsp;</label>
     </div>
    </c:if> 
    <div class="label">City:</div>
     <div class="inputText">
       <label>${address.city} &nbsp;</label>
     </div> 
    <div class="label">State:</div>
     <div class="inputText">
       <label>${address.state}&nbsp;</label>
     </div> 
    <div class="label">Zip Code:</div>
     <div class="inputText">
       <label>${address.zipCode}&nbsp;</label>
     </div> 
    <div class="label">U.S. Address:</div>
     <div class="inputText">
       <label>${usaIndicator}&nbsp;</label>
     </div>      
    <div class="label">Telephone #:</div>
     <div class="inputText">
       <label>${brokerEntity.phoneNum}&nbsp;</label>
     </div>  
    <div class="label">Mobile #:</div>
     <div class="inputText">
       <label>${brokerEntity.cellPhoneNum}&nbsp;</label>
     </div>  
    <div class="label">Fax #:</div>
     <div class="inputText">
       <label>${brokerEntity.faxNum}&nbsp;</label>
     </div>  

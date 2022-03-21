<%@tag import="com.manulife.pension.bd.web.util.BDWebCommonUtils"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<%@attribute name="profile" type="com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile" 
   required="true"%>

	   
<c:set var="zipCode" value="${profile.address.zipCode}"/>
<c:set var="country" value="${profile.address.country}"/>
<c:set var="phoneNumber" value="${profile.phoneNum}"/>

<%
String zipCode =  (String)jspContext.getAttribute("zipCode");
String country =  (String)jspContext.getAttribute("country");
String phoneNumber =  (String)jspContext.getAttribute("phoneNumber");

%>

   <div class="label">First Name:</div>
   <div class="inputText">
	<label>${profile.firstName}&nbsp;</label>
   </div>
   <div class="label">Last Name:</div>
   <div class="inputText">
	<label>${profile.lastName}&nbsp;</label>
   </div>
	<div class="label">Email:</div>
   <div class="inputText">
	<label>${profile.emailAddress}&nbsp;</label>
   </div>
	<div class="label">Mailing Address 1:</div>
   <div class="inputText">
	<label>${profile.address.addrLine1}&nbsp;</label>
   </div>
   <div class="label">Address 2:</div>
   <div class="inputText">
     <label>${profile.address.addrLine2}&nbsp;</label>
   </div>
      <div class="label">City:</div>
	<div class="inputText">
		<label>${profile.address.city}&nbsp;</label>
	</div>
	      <div class="label">State:</div>
	<div class="inputText">
		<label>${profile.address.state}&nbsp;</label>
	</div>
	      <div class="label">Zip Code:</div>
	<div class="inputText">
		<label><%=BDWebCommonUtils.formatZipCode(zipCode,country) %>&nbsp;</label>
	</div>
	      <div class="label">Country:</div>
	<div class="inputText">
		<label><%=BDWebCommonUtils.getCountryName(country)%>&nbsp;</label>
	</div>
      <div class="label">Telephone #:</div>
	<div class="inputText">
		<label><%=BDWebCommonUtils.formatPhoneNumber(phoneNumber) %>&nbsp;</label>
	</div>

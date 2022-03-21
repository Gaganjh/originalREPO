<%@ attribute name="name" type="java.lang.String" required="true" rtexprvalue="true"%>
<%@ attribute name="form" type="com.manulife.pension.ezk.web.ActionForm" required="true" rtexprvalue="true"%>
<%@ attribute name="formName" type="java.lang.String" required="true" rtexprvalue="true"%>
<%@ attribute name="renderSection" type="java.lang.String" required="false"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@tag import="com.manulife.pension.bd.web.BDConstants"%>
<%@tag import="com.manulife.pension.service.broker.valueobject.BrokerEntityExtendedProfile" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- 
   This tag is used to display the list of broker entities.
--%>

<%@tag import="com.manulife.pension.bd.web.usermanagement.UserManagementHelper"%>
<content:contentBean contentId="<%=BDContentConstants.PERSONAL_INFO_PRIMARY_CONTACT_INFO_TEXT%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="primaryContactInfoText" />

<script type="text/javascript">
	function setPrimaryContact(obj,primaryContactId) {
		obj.form.primaryBrokerPartyId.value = primaryContactId;
		obj.checked = true;
	}

	function toggleMandatoryFields(obj) {
		usaIndicator = obj.value;
		index = obj.id;
		stateDiv = 'stateDiv'+index;
		mandatoryStateDiv = 'mandatoryStateDiv'+index;
		zipDiv = 'zipDiv'+index;
		mandatoryZipDiv = 'mandatoryZipDiv'+index;
		if(usaIndicator == 'true') {
			document.getElementById(stateDiv).style.display= "none";
			document.getElementById(mandatoryStateDiv).style.display= "block";
			document.getElementById(zipDiv).style.display= "none";
			document.getElementById(mandatoryZipDiv).style.display= "block";
		}
		else {
			document.getElementById(stateDiv).style.display= "block";
			document.getElementById(mandatoryStateDiv).style.display= "none";
			document.getElementById(zipDiv).style.display= "block";
			document.getElementById(mandatoryZipDiv).style.display= "none";
		}
	}

</script>

  <c:forEach items="${form.brokerEntityProfilesList}" var="brokerEntityProfile" varStatus="loopStatus" >

 <c:set var="index" value="${loopStatus.index}"/> 
 <% 
  String index = jspContext.getAttribute("index").toString();
 
 %>
 <% BrokerEntityExtendedProfile brokerEntityProfile = (BrokerEntityExtendedProfile)jspContext.getAttribute("brokerEntityProfile"); %>
 

		<c:set var="maskedId" value="<%=com.manulife.pension.bd.web.util.BDWebCommonUtils.fullMaskSsn(brokerEntityProfile.getSsnTaxId())%>"/>
		<div class="BottomBorder">
		    <c:choose>		      
				<c:when test="${brokerEntityProfile.ssn}">
					<div class="SubTitle Gold Left">Profile for SSN: 
				</c:when>
				<c:otherwise>
					<div class="SubTitle Gold Left">Profile for Tax ID: 
				</c:otherwise>
			</c:choose>
					<span class="style2"><c:out value="${maskedId}"/></span>
					<strong>
						<span class="style2"><br /></span>
					</strong>
				</div>
				<div class="SubTitle Gold Left"></div>
				<c:if test="${fn:length(form.brokerEntityProfilesList) gt 1}">
					<label>
							<c:if test="${brokerEntityProfile.id==form.primaryBrokerPartyId}">
								<input id="primaryContact" name="primaryContact" type="radio" value="true" checked onclick="setPrimaryContact(this,'${brokerEntityProfile.id}')" />
							</c:if>
							<c:if test="${brokerEntityProfile.id!=form.primaryBrokerPartyId}">
								<input id="primaryContact" name="primaryContact" type="radio" value="false" onclick="setPrimaryContact(this,'${brokerEntityProfile.id}')" />
							</c:if>
						<content:getAttribute attribute="text" beanName="primaryContactInfoText"/>
					</label>
				</c:if>
			</div>
				<c:if test="${not empty renderSection}">
				 <div class="regSection">
				</c:if>
				<div style="height: 1px"></div>
			<c:forEach var="firmName" items="<%=UserManagementHelper.getSortedBDFirmNames(brokerEntityProfile.getBrokerDealerFirms()) %>" varStatus="status">
				<c:if test="${status.first}">
					<div class="label">BD Firm Name:</div>
				</c:if>
				<div class="inputText">${firmName}</div> 
			</c:forEach>
			<c:forEach var="riaFirmName" items="<%=UserManagementHelper.getSortedRiaFirmNamesWithPermissions(brokerEntityProfile.getRiaFirms()) %>" varStatus="status">
				<c:if test="${status.first}">
					<div class="label">View RIA Statements For:</div>
				</c:if>
				<div class="inputText">${riaFirmName}</div> 
			</c:forEach>
			<c:if test="${brokerEntityProfile.showOrgName}">
				<div class="label">* Company Name:</div>
				<div class="inputText">
					<form:input cssClass="input" path="brokerEntityProfilesList[${index}].orgName" size="40" maxlength="40" />
				</div>
			</c:if>
			<c:if test="${brokerEntityProfile.showPersonalName}">
				<div class="label">* First Name:</div>
				<div class="inputText">
					<form:input cssClass="input" path="brokerEntityProfilesList[${index}].firstName" size="30" maxlength="20" />
				</div>
				<div class="label"> Middle Initial:</div>
				<div class="inputText">
					<form:input cssClass="input"path="brokerEntityProfilesList[${index}].middleInit" size="3" maxlength="1" />
				</div>
				<div class="label">* Last Name:</div>
				<div class="inputText">
					<form:input cssClass="input"  path="brokerEntityProfilesList[${index}].lastName" size="30" maxlength="20" />
				</div>
			</c:if>
			<div class="label">* Email:</div>
			<div class="inputText">
				<form:input cssClass="input" path="brokerEntityProfilesList[${index}].emailAddress" size="50" maxlength="99" />
			</div>
			<c:set var="mailingAddress" value="<%=BDConstants.MAILING_ADDRESS%>" />
			<c:choose>
				<c:when test='${form.addressFlagMap[brokerEntityProfile.ssnTaxId] == mailingAddress}'>
					<div class="label">* Mailing Address 1:</div>
					<div class="inputText">
						<form:input cssClass="input" path="brokerEntityProfilesList[${index}].mailingAddress.addressLine1" size="40" maxlength="40" />
					</div>  
					<div class="label"> Address 2:</div>
					<div class="inputText">
						<form:input cssClass="input"  path="brokerEntityProfilesList[${index}].mailingAddress.addressLine2" size="40" maxlength="40" />
					</div>
					<c:if test="${not empty brokerEntityProfile.mailingAddress.addressLine3}">
						<div class="label"> Address 3:</div>
						<div class="inputText">
							<form:input cssClass="input" path="brokerEntityProfilesList[${index}].mailingAddress.addressLine3" size="40" maxlength="40" />
						</div>
					</c:if>
					<c:choose>
						<c:when test="${brokerEntityProfile.mailingAddress.usaIndicator}">
							<c:set var="stateStyle" value="display:none"/>
							<c:set var="mandatoryStateStyle" value="display:block"/>
							<c:set var="zipStyle" value="display:none"/>
							<c:set var="mandatoryZipStyle" value="display:block"/>
						</c:when>
						<c:otherwise>
							<c:set var="stateStyle" value="display:block"/>
							<c:set var="mandatoryStateStyle" value="display:none"/>
							<c:set var="zipStyle" value="display:block"/>
							<c:set var="mandatoryZipStyle" value="display:none"/>
						</c:otherwise>
					</c:choose>
					<div class="label">* City:</div>
					<div class="inputText">
						<form:input cssClass="input" path="brokerEntityProfilesList[${index}].mailingAddress.city" size="25" maxlength="25" />
					</div> 
					
					<div class="label" id="<%="stateDiv"+index%>" style="${stateStyle}"> State:</div>
					<div class="label" id="<%="mandatoryStateDiv"+index%>" style="${mandatoryStateStyle}">* State:</div>
					<div class="inputText">
						<form:input cssClass="input" path="brokerEntityProfilesList[${index}].mailingAddress.state" size="2" maxlength="2" />
					</div>
					<div class="label" id="<%="zipDiv"+index%>" style="${zipStyle}"> Zip Code:</div>
					<div class="label" id="<%="mandatoryZipDiv"+index%>" style="${mandatoryZipStyle}">* Zip Code:</div>
					<div class="inputText">
						<form:input cssClass="input" path="brokerEntityProfilesList[${index}].mailingAddress.zipCode" size="10" maxlength="10" />
					</div>
					<div class="label">*  U.S. Address:</div>
					<div class="inputText">
						<label>
							<form:radiobutton id="${index}" path="brokerEntityProfilesList[${index}].mailingAddress.usaIndicator" value="true"  onclick="toggleMandatoryFields(this)"/>
							Yes
						</label>
						<label>
							<form:radiobutton id="${index}" path="brokerEntityProfilesList[${index}].mailingAddress.usaIndicator" value="false"  onclick="toggleMandatoryFields(this)"/>
							No
						</label>
					</div>
				</c:when>
				<c:otherwise>
					<div class="label">* Mailing Address 1:</div>
					<div class="inputText">
						<form:input cssClass="input" path="brokerEntityProfilesList[${index}].homeAddress.addressLine1" size="40" maxlength="40" />
					</div>  
					<div class="label"> Address 2:</div>
					<div class="inputText">
						<form:input cssClass="input" path="brokerEntityProfilesList[${index}].homeAddress.addressLine2" size="40" maxlength="40" />
					</div>
					<c:if test="${not empty brokerEntityProfile.homeAddress.addressLine3}">
						<div class="label"> Address 3:</div>
						<div class="inputText">
							<form:input cssClass="input" name="brokerEntityProfile" path="homeAddress.addressLine3" size="40" maxlength="40" />
						</div>
					</c:if>
					<c:choose>
						<c:when test="${brokerEntityProfile.homeAddress.usaIndicator}">
							<c:set var="stateStyle" value="display:none"/>
							<c:set var="mandatoryStateStyle" value="display:block"/>
							<c:set var="zipStyle" value="display:none"/>
							<c:set var="mandatoryZipStyle" value="display:block"/>
						</c:when>
						<c:otherwise>
							<c:set var="stateStyle" value="display:block"/>
							<c:set var="mandatoryStateStyle" value="display:none"/>
							<c:set var="zipStyle" value="display:block"/>
							<c:set var="mandatoryZipStyle" value="display:none"/>
						</c:otherwise>
					</c:choose>
					<div class="label">* City:</div>
					<div class="inputText">
						<form:input cssClass="input" path="brokerEntityProfilesList[${index}].homeAddress.city" size="25" maxlength="25" />
					</div> 
					<div class="label" id="<%="stateDiv"+index%>" style="${stateStyle}"> State:</div>
					<div class="label" id="<%="mandatoryStateDiv"+index%>" style="${mandatoryStateStyle}">* State:</div>
					<div class="inputText">
						<form:input cssClass="input" path="brokerEntityProfilesList[${index}].homeAddress.state" size="2" maxlength="2" />
					</div>
					<div class="label" id="<%="zipDiv"+index%>" style="${zipStyle}"> Zip Code:</div>
					<div class="label" id="<%="mandatoryZipDiv"+index%>" style="${mandatoryZipStyle}">* Zip Code:</div>
					<div class="inputText">
						<form:input cssClass="input" path="brokerEntityProfilesList[${index}].homeAddress.zipCode" size="10" maxlength="10" />
					</div>
					<div class="label">*  U.S. Address:</div>
					<div class="inputText">
						<label>
							<form:radiobutton id="${index}" path="brokerEntityProfilesList[${index}].homeAddress.usaIndicator" value="true"  onclick="toggleMandatoryFields(this)"/>
							Yes
						</label>
						<label>
							<form:radiobutton id="${index}" path="brokerEntityProfilesList[${index}].homeAddress.usaIndicator" value="false"  onclick="toggleMandatoryFields(this)"/>
							No
						</label>
					</div>
				</c:otherwise>
			</c:choose>
			<div class="label"> Telephone #:</div>
			<div class="inputText" style="vertical-align:top">
				<form:input cssClass="input" path="brokerEntityProfilesList[${index}].phoneNum" size="12" maxlength="12"  /><span>(###-###-####)<br></span>
			</div>
			<div class="label"> Mobile #:</div>
			<div class="inputText" style="vertical-align:top">
				<form:input cssClass="input" path="brokerEntityProfilesList[${index}].cellPhoneNum" size="12" maxlength="12" />(###-###-####)
			</div> 
			<div class="label"> Fax #:</div>
			<div class="inputText" style="vertical-align:top">
				<form:input cssClass="input" path="brokerEntityProfilesList[${index}].faxNum" size="12" maxlength="12" />(###-###-####)
			</div>
			<c:if test="${not empty renderSection}">
				 </div>
			</c:if>
			
	</c:forEach>

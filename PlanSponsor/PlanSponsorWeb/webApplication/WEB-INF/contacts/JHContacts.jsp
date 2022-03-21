<%-- Author: Venkatesh Kasiraj - This JSP was added for Contact Management Project June 2010
 JH Contact tab displays the contact information for a particular contract and user profile   
--%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="manulife/tags/content" prefix="content"%>

<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.service.security.role.InternalUser"%>
<%@page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.service.contract.valueobject.ContactVO"%>

<%-- This jsp includes the following CMA content --%>


<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_SECTION_TITLE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Section_Title" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_MORE_INFORMATION_LINK%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_More_Information_Link" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_YOUR_MANULIFE_ACCOUNT_REPRESENTATIVE_LABEL%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Your_Manulife_Account_Representative_Label" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_ACCOUNT_REPRESENTATIVE_PHONE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Account_Representative_Phone" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_CUSTOMER_TECHNOLOGY_SERVICES_LABEL%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Customer_Technology_Services_Label" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_MANULINE_LABEL%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Manuline_Label" />
<content:contentBean
	contentId="<%=ContentConstants.PS_DEFINED_BENEFIT_CUSTOMER_SERVICE_FAX%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Defined_Benefit_Customer_Service_Fax" />
<content:contentBean
	contentId="<%=ContentConstants.PS_DEFINED_BENEFIT_ACCOUNT_REP_PHONE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Defined_Benefit_Account_Rep_Phone" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACT_OTHER_FAX_NO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Customer_Technology_Services_Phone" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACT_PARTICIPANT_PHONE_NO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Manuline_Phone" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACT_PARTICIPANT_SPANISH_PHONE_NO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Manuline_Spanish_Phone" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACT_GENERAL_CAR_NO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Manuline_General_Car_Phone" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACT_ENROLLMENT_FAX_NO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Manuline_Enrollment_Fax_No" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACT_PLAN_CONTACT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Manuline_Plan_Contact" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACT_DB_OTHER_FAX_NO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Manuline_Plan_DB_Other_Fax" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_SERVICE_REPRESENTATIVE_LABEL%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Service_Rep_Label" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_ROLLOVER_EDUCATION_LABEL%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Rollover_Education_Label" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_ROLLOVER_EDUCATION_PHONE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Rollover_Education_Phone" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_CONSOLIDATION_SPECIALIST_LABEL%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Consolidation_Specialist_Label" />
<content:contentBean
	contentId="<%=ContentConstants.PS_CONTACTS_CONSOLIDATION_SPECIALIST_PHONE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="Contacts_Consolidation_Specialist_Phone" />
<content:contentBean 
	contentId="<%=ContentConstants.KRD_CONTACT_TEXT %>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	beanName="KRD_Contact_Text"/>
	
	
	<jsp:useBean class="java.lang.String" id="resultsMessageKey" scope="request"/>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

	<jsp:useBean class="com.manulife.pension.ps.web.contacts.JHContactsForm" id="jhContactsForm" scope="session"/>
	
	
<content:contentBean contentId="<%=ContentConstants.EMAIL_SUBJECT_TEXT %>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Email_Subject_Text" />


<c:set var="contractStatus" value="${userProfile.currentContract.status}"/>

<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}"/>

<c:set var="contactVO" value="${jhContactsForm.contactVO}" />


	<div id="errordivcs"><content:errors scope="session"/></div>
<%-- TAB section --%>
<jsp:include page="ContactsTab.jsp" flush="true">
	<jsp:param value="1" name="tabValue" />
	<jsp:param value="${tpaFirmAccessForContract}"
		name="tpaFirmAccessForContract" />
</jsp:include>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><table class="addressTable" border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td colspan="5" class="tableheadTD" style="border-right-color: 002D62;"><b><content:getAttribute
					beanName="layoutPageBean" attribute="body1Header" /></b></td>
			</tr>
		
		
			<%-- Business rule:
		SPR.93.	In the Contact section, if the contract is in DI status the system will display "Discontinued Contract"
		instead of "Manulife Account Representative" and "For Assistance" instead of CAR name.
		  System will not display the CAR's phone number, phone extension or email address.
		   System will display general 1-800 # .  Message should be content managed.
		--%>
		
			<c:if test="${contractStatus eq 'DI'}">
				<%-- CMA managed--%>
				<tr>
					<td class="pgNumBack" colspan="5"><b>Discontinued Contract</b></td>
				</tr>
				<tr class="datacell1">
					<td width="35%" colspan="1">For Assistance</td>
					<td colspan="4"><content:getAttribute
						beanName="Contacts_Manuline_General_Car_Phone" attribute="text" /></td>
				</tr>
</c:if>
			<c:if test="${contractStatus ne 'DI'}">
				<%-- CMA managed--%>
				<tr>
					<td class="pgNumBack" colspan="5"><b><content:getAttribute
						beanName="Contacts_Your_Manulife_Account_Representative_Label"
						attribute="text" /></b></td>
				</tr>
				<tr class="datacell1">
<td width="35%" valign="top">${jhContactsForm.contactVO.name}</td>

<td colspan="4" nowrap="nowrap"> <c:if test="${definedBenefit eq 'true'}">

						<content:getAttribute beanName="Defined_Benefit_Account_Rep_Phone"
							attribute="text" />
</c:if> <c:if test="${definedBenefit ne 'true'}">
						<content:getAttribute
							beanName="Contacts_Account_Representative_Phone" attribute="text" />
</c:if> <%-- Business rule:
								SPR.89.	If the site is NY then Account representative NY extension must be displayed,
								 if the contract is US then Account representative US extension must be displayed.
--%> ext. ${jhContactsForm.contactVO.extension}</td>

				</tr>
				<tr class="datacell2">
					<td width="35%" valign="top">Email</td>
					<td colspan="4">
					
					<c:set var="emailSubjectText"><content:getAttribute id="Email_Subject_Text" attribute="text"><content:param><%= userProfile.getCurrentContract().getContractNumber()%></content:param></content:getAttribute></c:set>
					<% 
                      String mailTo = "";
                      if(jhContactsForm.getContactVO().getName()!=null && !jhContactsForm.getContactVO().getName().equals("")){
                    	  mailTo = jhContactsForm.getContactVO().getName().trim() +" <"+jhContactsForm.getContactVO().getEmail() + ">?subject=";%>
                    	  <a href="mailto:<%=mailTo%>${emailSubjectText}">Email</a>
                     <%}%></td>
				</tr>
		
</c:if>
			<%-- customer service fax line --%>
			<tr>
				<td class="pgNumBack" colspan="5"><b><content:getAttribute
					beanName="Contacts_Customer_Technology_Services_Label"
					attribute="text" /></b></td>
			</tr>
			<c:if test="${definedBenefit ne 'true'}">
				<tr class="datacell1">
					<td valign="top" width="35%" colspan="1">For enrollment forms:</td>
					<td colspan="4"><content:getAttribute
						beanName="Contacts_Manuline_Enrollment_Fax_No" attribute="text" /></td>
				</tr>
</c:if>
			<c:choose>
				<c:when test="${!definedBenefit}">
					<tr class="datacell2">
				</c:when>
				<c:otherwise>
					<tr class="datacell1">
				</c:otherwise>
			</c:choose>
			<td width="35%" valign="top">For other documents:</td>
			<td colspan="4"><c:if test="${definedBenefit eq 'true'}">
				<%-- CMA managed--%>
				<content:getAttribute beanName="Contacts_Manuline_Plan_DB_Other_Fax"
					attribute="text" >
					<content:param>/do/tools/secureDocumentUpload/submit/</content:param>
				</content:getAttribute>
			</td>
</c:if>
			<c:if test="${definedBenefit ne 'true'}">
				<%-- CMA managed--%>
				<content:getAttribute
					beanName="Contacts_Customer_Technology_Services_Phone"
					attribute="text" >
					<content:param>/do/tools/secureDocumentUpload/submit/</content:param>
				</content:getAttribute>
				</td>
</c:if>
			</tr>
			<%-- participant toll-free service line not applicable for Defined Benefit contracts --%>
			<c:if test="${definedBenefit ne 'true'}">
				<tr>
					<td class="pgNumBack" colspan="5"><b><content:getAttribute
						beanName="Contacts_Manuline_Label" attribute="text" /></b></td>
				</tr>
				<tr class="datacell1">
					<td width="35%" valign="top"><content:getAttribute
						beanName="Contacts_Service_Rep_Label" attribute="text" /></td>
					<td colspan="4"><content:getAttribute
						beanName="Contacts_Manuline_Phone" attribute="text" /></td>
				</tr>
				
				<tr class="datacell1">
					<td width="35%" valign="top">&nbsp;</td>
					<td colspan="4"><content:getAttribute
						beanName="Contacts_Manuline_Spanish_Phone" attribute="text" /></td>
				</tr>
	
				
				<tr class="datacell2">
					<td width="35%" valign="top"><content:getAttribute
						beanName="Contacts_Rollover_Education_Label" attribute="text" /></td>
					<td colspan="4"><content:getAttribute
						beanName="Contacts_Rollover_Education_Phone" attribute="text" /></td>
				</tr>
				
				<tr class="datacell1">
					<td width="35%" valign="top"><content:getAttribute
						beanName="Contacts_Consolidation_Specialist_Label" attribute="text" /></td>
					<td colspan="4"><content:getAttribute
						beanName="Contacts_Consolidation_Specialist_Phone" attribute="text" /></td>
				</tr>
</c:if>
		</table></td>
	</tr>
	<tr>
		<td><content:getAttribute beanName="layoutPageBean" attribute="body1" /></td>
	</tr>
	<tr>
		<td style="border-top: 1px solid #002D62;"><img 
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>
</table>	

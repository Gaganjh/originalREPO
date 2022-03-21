
<%-- This jsp is included as part of the secureHomePage.jsp --%>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="notifications" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContactVO" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractSummaryVO" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContactVO" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Section_Title"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_MORE_INFORMATION_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_More_Information_Link"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_YOUR_MANULIFE_ACCOUNT_REPRESENTATIVE_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Your_Manulife_Account_Representative_Label"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_ACCOUNT_REPRESENTATIVE_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Account_Representative_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_CUSTOMER_TECHNOLOGY_SERVICES_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Customer_Technology_Services_Label"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_CUSTOMER_TECHNOLOGY_SERVICES_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Customer_Technology_Services_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_MANULINE_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Manuline_Label"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_MANULINE_ENGLISH_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Manuline_EN_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_MANULINE_SPANISH_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Manuline_SP_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_DEFINED_BENEFIT_CUSTOMER_SERVICE_FAX%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Defined_Benefit_Customer_Service_Fax"/>
<content:contentBean contentId="<%=ContentConstants.PS_DEFINED_BENEFIT_ACCOUNT_REP_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Defined_Benefit_Account_Rep_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONSOLIDATION_SERVICE_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Consolidation_Service_Label"/>
<content:contentBean contentId="<%=ContentConstants.PS_CONSOLIDATION_SERVICE_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Consolidation_Service_Phone"/>
<content:contentBean contentId="<%=ContentConstants.PS_ROLL_OVER_EDU_SERVICE_LABEL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Roll_Over_Edu_Service_Label"/>
<content:contentBean contentId="<%=ContentConstants.PS_ROLL_OVER_EDU_SERVICE_PHONE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Roll_Over_Edu_Service_Phone"/>
<content:contentBean contentId="<%=ContentConstants.KRD_CONTACT_TEXT %>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="KRD_Contact_Text"/>
<content:contentBean contentId="<%=ContentConstants.EMAIL_SUBJECT_TEXT %>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Email_Subject_Text" />

<c:set var="contractSummary" value="${requestScope.contractSummary}" />
<c:set var="carContact" value="${contractSummary.carContact}" />
<c:set var="participantList" value="${contractSummary.participants}" />
<c:if test="${not empty userProfile.currentContract}">
<c:set var="contractStatus" value="${userProfile.currentContract.status}"/>
<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}"/>
</c:if>

<c:if test="${not empty contractSummary.carContact}">
<c:set var="carContact" value="${contractSummary.carContact}"/>
<%!
ContactVO contactVO = null;
%>
<%
contactVO = (ContactVO)pageContext.getAttribute("carContact");
%>
</c:if>


 <c:if test="${empty userProfile.currentContract}">
<c:set var="contractStatus" value="null" />
<c:set var="definedBenefit" value="null" />
</c:if>    
    

<table width="240" border="0" cellspacing="0" cellpadding="0" class="box">
              <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="238"><img src="/assets/unmanaged/images/s.gif" width="238" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              </tr>
              <tr class="tablehead">
                <td colspan="3" class="tableheadTD1">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="tableheadTD"><b><%-- CMA managed--%><content:getAttribute beanName="Contacts_Section_Title" attribute="title"/></b></td>
                      <td align="right"><%-- CMA managed--%><content:getAttribute beanName="Contacts_More_Information_Link" attribute="text"/></td>
                    </tr>
                </table></td>
              </tr>

<%-- Business rule:
SPR.93.	In the Contact section, if the contract is in DI status the system will display "Discontinued Contract"
instead of "Manulife Account Representative" and "For Assistance" instead of CAR name.
  System will not display the CAR's phone number, phone extension or email address.
   System will display general 1-800 # .  Message should be content managed.
--%>

              <tr>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="boxbody" valign="top">
	                <c:if test = "${contractStatus=='DI'}">
    	                  <%-- CMA managed--%><b>Discontinued Contract
		                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		                   <tr>
		                      <td valign="top"><br><b>For Assistance</td>
		                    </tr>
		                  </table>
</c:if>
  				<c:if test = "${contractStatus ne'DI'}">
 	                      <%-- CMA managed--%><b><content:getAttribute beanName="Contacts_Your_Manulife_Account_Representative_Label" attribute="text"/></b><br>
		                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		                   <tr>
<td valign="top">${contractSummary.carContact.name}<br>
		                      <c:set var="emailSubjectText"><content:getAttribute id="Email_Subject_Text" attribute="text"><content:param>${contractSummary.contractNumber}</content:param></content:getAttribute></c:set> 
		                      <% 
		                      String mailTo = "";
		                      if(contactVO.getName()!=null && !contactVO.getName().equals("")){
		                    	  mailTo = contactVO.getName().trim() +" <"+contactVO.getEmail() + ">?subject=";%>
		                    	  <a href="mailto:<%=mailTo%>${emailSubjectText}">Email</a>
		                      <%}%>		                      
		                       </td>
		                      <td align="right" valign="top">
		                        <c:if test = "${definedBenefit==true}">
		                      		<p><content:getAttribute beanName="Defined_Benefit_Account_Rep_Phone" attribute="text"/><br>
</c:if>
		                  <c:if test = "${definedBenefit ne true}">
		                      		<p><content:getAttribute beanName="Contacts_Account_Representative_Phone" attribute="text"/><br>
</c:if>
		                      	  
								<%-- Business rule:
									SPR.89.	If the site is NY then Account representative NY extension must be displayed,
									 if the contract is US then Account representative US extension must be displayed.
								--%>

ext. ${contractSummary.carContact.extension} <%-- scope="request" --%></p></td>
		                    </tr>
		                  </table>
  	                      <br>
	                  	  
</c:if>

       <%-- customer service fax line --%>                 
                  <b><%-- CMA managed--%><content:getAttribute beanName="Contacts_Customer_Technology_Services_Label" attribute="text"/></b><br>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="65%" height="18" valign="top">&nbsp; </td>
                      <td width="35%" align="right" valign="top">
                      <c:if test = "${definedBenefit==true }">
		                <%-- CMA managed--%><content:getAttribute beanName="Defined_Benefit_Customer_Service_Fax" attribute="text"/></td>
</c:if><c:if test = "${definedBenefit==false }">
                          	<%-- CMA managed--%><content:getAttribute beanName="Contacts_Customer_Technology_Services_Phone" attribute="text"/></td>
</c:if>
                    </tr>
                  </table>
                  <Br>
       
       <%-- participant toll-free service line not applicable for Defined Benefit contracts --%>
	 		  <c:if test = "${definedBenefit==false }">    	
                  <b><%-- CMA managed--%><content:getAttribute beanName="Contacts_Manuline_Label" attribute="text"/></b><br>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="65%" height="18" valign="top">&nbsp; </td>
                      <td width="35%" align="right" valign="top"><%-- CMA managed--%><content:getAttribute beanName="Contacts_Manuline_EN_Phone" attribute="text"/></td>
                    </tr>
                    <tr>
                      <td width="65%" height="18" valign="top">&nbsp; </td>
                      <td width="35%" align="right" valign="top"><%-- CMA managed--%><content:getAttribute beanName="Contacts_Manuline_SP_Phone" attribute="text"/></td>
                    </tr>
                  </table>
                  <Br> 
         	     
        <%-- "Participant consolidation services toll free line" Label --%>
         	   	 <b><%-- CMA managed--%><content:getAttribute beanName="Consolidation_Service_Label" attribute="text"/></b><br>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="65%" height="18" valign="top">&nbsp; </td>
                      <td width="35%" align="right" valign="top"><%-- CMA managed--%><content:getAttribute beanName="Consolidation_Service_Phone" attribute="text"/></td>
                    </tr>
                  </table>
                  <Br> 
        <%-- "Participant rollover education services toll free line" Label --%>
         	   	 <b><%-- CMA managed--%><content:getAttribute beanName="Roll_Over_Edu_Service_Label" attribute="text"/></b><br>
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="65%" height="18" valign="top">&nbsp; </td>
                      <td width="35%" align="right" valign="top"><%-- CMA managed--%><content:getAttribute beanName="Roll_Over_Edu_Service_Phone" attribute="text"/></td>
                    </tr>
                  </table>
                  <Br> 
</c:if>
                  
               </td>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              </tr>
              <tr>
                <td colspan="3">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td rowspan="2"  width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
                    </tr>
                    <tr>
                      <td height="2" class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                </table></td>
              </tr>
            </table>

<%--  Imports  --%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.platform.web.util.DataUtility"%>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContactVO" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractSummaryVO" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%--  Tag Libraries  --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- Beans used --%>
<%
ContractInformationReportData theReport = (ContractInformationReportData)request.getAttribute(BDConstants.REPORT_BEAN);
  pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
  
  String TRUE=BDConstants.TRUE;
  pageContext.setAttribute("TRUE",TRUE,PageContext.PAGE_SCOPE);
  
  ContractInformationReportData contractSummary = (ContractInformationReportData)request.getAttribute(BDConstants.REPORT_BEAN);
  pageContext.setAttribute("contractSummary",contractSummary,PageContext.PAGE_SCOPE);
  
  ContactVO carContact = contractSummary.getContractSummaryVo().getCarContact(); 
  pageContext.setAttribute("carContact",carContact,PageContext.PAGE_SCOPE);
  BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
  pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
  
  %>
  


<content:contentBean contentId="<%=BDContentConstants.PS_CONTACTS_ACCOUNT_REPRESENTATIVE_PHONE%>" 
                            type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
                            id="carPhoneNumber"/>

<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_CONTACT_INFO_SECTION_TITLE%>"
                           	type="<%=BDContentConstants.TYPE_MESSAGE%>"
                          	id="contactSectionTitle"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PARTICIPANT_TOLL_FREE_NUMBER%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="participantTollFreeNumber"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_ENROLLMENT_FORM_FAX_NUMBER%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="enrollmentFormFaxNumber"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_OTHER_FORM_FAX_NUMBER%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="otherDCFaxNumber"/>
                          	
<content:contentBean contentId="<%=BDContentConstants.DB_FAX_NUMBER%>"
                            type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                            id="otherDBFaxNumber"/>
                            
<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_DB_CONTRACT_PHONE_NUMBER%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="dbContractCarPhoneNumber"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_GENERAL_PHONE_NUMBER%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="generalPhoneumber"/>
                          	
<content:contentBean contentId="<%=BDContentConstants.CONTACT_SERVICE_REP_LABEL%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="serviceRepLabel"/>
                            	
<content:contentBean contentId="<%=BDContentConstants.CONTACT_SERVICE_REP_NUMBER_SPANISH%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="serviceRepSpanishNumber"/>
                          	
<content:contentBean contentId="<%=BDContentConstants.CONTACT_ROLLOVER_EDUCATION_SPECIALIST_LABEL%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="rolloverEduSpecialistLabel"/>
                          	
<content:contentBean contentId="<%=BDContentConstants.CONTACT_ROLLOVER_EDUCATION_SPECIALIST_PHONE%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="rolloverEduSpecialistPhone"/>
                          	
<content:contentBean contentId="<%=BDContentConstants.CONTACT_CONSOLIDATION_SPECIALIST_LABEL%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="consolidationSpecialistLabel"/>                          	

<content:contentBean contentId="<%=BDContentConstants.CONTACT_CONSOLIDATION_SPECIALIST_PHONE%>"
                           	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="consolidationSpecialistPhone"/>  
<content:contentBean contentId="<%=BDContentConstants.EMAIL_SUBJECT_TEXT %>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" beanName="Email_Subject_Text" />
                          	
<h4><content:getAttribute beanName="contactSectionTitle" attribute="text"/></h4>
<table class="overview_table" >
	<tbody>
		<tr>
			<th>Mailing Address:</th>
			<td width="166"><c:if
					test="${not empty theReport.contractProfileVo.address.line1}">
			       ${theReport.contractProfileVo.address.line1}<br />
				</c:if> <c:if test="${not empty theReport.contractProfileVo.address.line2}">
                   ${theReport.contractProfileVo.address.line2}<br />
				</c:if> <c:if test="${not empty theReport.contractProfileVo.address.city}">
                   ${theReport.contractProfileVo.address.city}
<c:if
						test="${theReport.contractProfileVo.address.completeStateCode ==true}">,</c:if>
				</c:if> <c:if
					test="${not empty theReport.contractProfileVo.address.stateCode}">
                   ${theReport.contractProfileVo.address.stateCode}
<c:if test="${not empty theReport.contractProfileVo.address.zipCode}">,</c:if>
				</c:if> <c:if test="${not empty theReport.contractProfileVo.address.zipCode}">
                   ${theReport.contractProfileVo.address.zipCode}
</c:if> <br /></td>
		</tr>

		<c:if test="${bobContext.currentContract.status =='DI'}">
			<tr>
				<th>Discontinued Contract:</th>
				<td>For assistance
				<br/><span class="contact_no"><content:getAttribute beanName="generalPhoneumber" attribute="text"/></span></td>
			</tr>
</c:if>
<c:if test="${bobContext.currentContract.status !='DI'}">
			<tr>
				<th>Client Account Rep.:</th>
				<td>
				<c:if test="${not empty theReport.contractSummaryVo.carContact.name}">
${theReport.contractSummaryVo.carContact.name}

		        <br/>
		        </c:if>
<c:if test="${bobContext.currentContract.definedBenefitContract ==true}">
					<span class="contact_no"><content:getAttribute beanName="dbContractCarPhoneNumber" attribute="text"/></span>
</c:if>
<c:if test="${bobContext.currentContract.definedBenefitContract !=true}">
					<%--BASESD ON US OR NY NEED TO SHOW THE PHONE NUMBER --%>
					<span class="contact_no"><content:getAttribute beanName="carPhoneNumber" attribute="text"/></span>
</c:if>
				<c:if test="${not empty theReport.contractSummaryVo.carContact.extension}">
ext. ${theReport.contractSummaryVo.carContact.extension}
				</c:if>
				<c:if test="${not empty theReport.contractSummaryVo.carContact.name}">
				<br/>
				<c:set var="emailSubjectText"><content:getAttribute id="Email_Subject_Text" attribute="text"><content:param><%= bobContext.getContractProfile().getContract().getContractNumber()%></content:param></content:getAttribute></c:set>
				<% String mailTo = carContact.getName().trim() +" <"+carContact.getEmail() + ">?subject=";%> 
				<a style="text-decoration: underline;" href="mailto:<%=mailTo%>${emailSubjectText}">Email</a>
		        </c:if>
		         </td>
			</tr>
</c:if>

		<tr>
			<th>Relationship Manager Contact:</th>
			<td>
			<c:if
					test="${not empty theReport.contractSummaryVo.rmUserName}">
			       ${theReport.contractSummaryVo.rmUserName}<br />
					</c:if> 
				
				<c:if test="${(not empty theReport.contractSummaryVo.rmUserPhoneNumber) and (empty theReport.contractSummaryVo.rmUserPhoneExtension)}">
                   ${theReport.contractSummaryVo.rmUserPhoneNumber} <br />
				</c:if>
				
				<c:if test="${(not empty theReport.contractSummaryVo.rmUserPhoneNumber) and (not empty theReport.contractSummaryVo.rmUserPhoneExtension)}">
                   ${theReport.contractSummaryVo.rmUserPhoneNumber} ext. ${theReport.contractSummaryVo.rmUserPhoneExtension} <br />
				</c:if>
				
				<c:if test="${not empty theReport.contractSummaryVo.rmUserEmail}">
					<a style="text-decoration: underline;"
						href="mailto:${theReport.contractSummaryVo.rmUserEmail}">
						Email</a>
				</c:if>
				
			</td>
		</tr>
		
		<tr>
			<th>TPA Contact:</th>
			<td><c:choose>
				<c:when test='${not empty theReport.tpaPrimaryContactDetails}'>


<c:forEach items="${theReport.tpaPrimaryContactDetails}" var="theItem" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/>


						<c:if test="${indexValue ne 0}"> 
						 ,
					   </c:if>
						${theItem.name} 
</c:forEach>
				</c:when>
				<c:when test='${not empty fn:trim(theReport.tpaContactName)}'>
${theReport.tpaContactName}
				</c:when>
				<c:otherwise>
			       Not available
			    </c:otherwise>
			</c:choose> <br />
${theReport.tpaFirmName}
<c:if test="${not empty theReport.tpaPrimaryContactDetails}">

				
<c:forEach items="${theReport.tpaPrimaryContactDetails}" var="theItem" >

					<c:if test="${not empty theItem.phone}">
						<c:set var="tpaPhone" value="true" />
					</c:if>
					<c:if test="${not empty theItem.email}">
						<c:set var="tpaEmail" value="true" />
					</c:if>

</c:forEach>
				<c:if test="${tpaPhone eq 'true'}">
				<br />
<c:forEach items="${theReport.tpaPrimaryContactDetails}" var="theItem" varStatus="indexValue" >
<c:set var="indexValue" value="${indexValue.index}"/>


						<c:if test="${indexValue ne 0}"> 
						 ,
					   </c:if>

						<c:if test="${not empty theItem.phone}">
					${theItem.phone} 
				</c:if>
						<c:if test="${empty theItem.phone}">
					Not available
				</c:if>


</c:forEach>
				</c:if>
				<c:if test="${tpaEmail eq 'true'}">
				<br />
<c:forEach items="${theReport.tpaPrimaryContactDetails}" var="theItem" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/>


						<c:if test="${indexValue ne 0}"> 
						 ,
					   </c:if>
						<c:if test="${not empty theItem.email}">
							<a style="text-decoration: underline;"
								href="mailto:${theItem.email}">${theItem.email}</a>
						</c:if>
						<c:if test="${empty theItem.email}">
					Not available
						</c:if>
</c:forEach>
				</c:if>
</c:if></td>
		</tr>
		<tr>
			<th>Plan Sponsor Contact:</th>
<td><c:if test="${not empty theReport.planSponsorPrimaryContactDetails}">


<c:forEach items="${theReport.planSponsorPrimaryContactDetails}" var="theItem" >


					<c:if test="${not empty theItem.phone}">
						<c:set var="psPhone" value="true" />
					</c:if>
					<c:if test="${not empty theItem.email}">
						<c:set var="psEmail" value="true" />
					</c:if>
</c:forEach>


<c:forEach items="${theReport.planSponsorPrimaryContactDetails}" var="theItem" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/>


					<c:if test="${indexValue ne 0}"> 
						 ,
					   </c:if>
						${theItem.name} 
</c:forEach>
				<br />
				<c:if test="${psPhone eq 'true'}">
<c:forEach items="${theReport.planSponsorPrimaryContactDetails}" var="theItem" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/>

						<c:if test="${indexValue ne 0}"> 
						 ,
					   </c:if>
						<c:if test="${not empty theItem.phone}">
					${theItem.phone} 
				</c:if>
						<c:if test="${empty theItem.phone}">
					Not available
				</c:if>
</c:forEach>
					<br />
				</c:if>
				<c:if test="${psEmail eq 'true'}">
<c:forEach items="${theReport.planSponsorPrimaryContactDetails}" var="theItem" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/>


						<c:if test="${indexValue ne 0}"> 
						 ,
					   </c:if>

						<c:if test="${not empty theItem.email}">
							<a style="text-decoration: underline;"
								href="mailto:${theItem.email}">${theItem.email}</a>
						</c:if>
						<c:if test="${empty theItem.email}">
					Not available
						</c:if>

</c:forEach>
				</c:if>


</c:if></td>
		</tr>

<c:if test="${bobContext.currentContract.definedBenefitContract !=true}">
			<tr>
				<th>Participant Toll Free Services:</th>
				<td>&nbsp;</td>
			</tr>	
               <%-- CMA DEFINED --%>
                 
			<tr>	
			    <td class="contact_names"><content:getAttribute beanName="serviceRepLabel" attribute="text" />
			 
			    </td> 
			    
			    <td class="contact_no"><content:getAttribute beanName="participantTollFreeNumber" attribute="text" /></td>
			</tr>
			
			<tr>
			    <td>&nbsp;</td> 
			    <td><content:getAttribute beanName="serviceRepSpanishNumber" attribute="text" /></td>
			</tr> 
			
			<tr>
			    <td class="contact_names"><content:getAttribute beanName="rolloverEduSpecialistLabel" attribute="text" />
			    	</td>
			    <td class="contact_no"><content:getAttribute beanName="rolloverEduSpecialistPhone" attribute="text" /></td>
			</tr>
			
			<tr>
			    <td class="contact_names"><content:getAttribute beanName="consolidationSpecialistLabel" attribute="text" /> 
			    </td> 
			    <td class="contact_no"><content:getAttribute beanName="consolidationSpecialistPhone" attribute="text" /></td>
			</tr>    
			<tr>
				<th>Enrollment Form Fax:</th>
                 <%-- CMA DEFINED --%>
				<td class="contact_no"><content:getAttribute beanName="enrollmentFormFaxNumber" attribute="text"/></td>
			</tr>
</c:if>
		<tr>
			<th>Other Form Fax:</th>
            <%-- CMA DEFINED --%>
<c:if test="${bobContext.currentContract.definedBenefitContract ==true}">
			       <td class="contact_no"><content:getAttribute beanName="otherDBFaxNumber" attribute="text"/></td>
</c:if>
<c:if test="${bobContext.currentContract.definedBenefitContract !=true}">
			       <td class="contact_no"><content:getAttribute beanName="otherDCFaxNumber" attribute="text"/></td>
</c:if>
		</tr>
	</tbody>
</table>

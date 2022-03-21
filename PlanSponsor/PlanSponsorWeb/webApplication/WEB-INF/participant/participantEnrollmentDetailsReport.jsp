<%-- taglib used --%>

<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantEnrollmentDetailsReportForm" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.util.render.DateRender" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummary" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummaryReportData" %>

<jsp:useBean id="participantEnrollmentDetailsReportForm" scope="session" type="com.manulife.pension.ps.web.participant.ParticipantEnrollmentDetailsReportForm" />
<%
ParticipantEnrollmentSummaryReportData theReport = (ParticipantEnrollmentSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 



<!-- Remove the extra column at the before the report -->
<c:if test="${empty param.printFriendly}" >
	<td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	<td >
</c:if>
		<p>
	    	<content:errors scope="session" />
		</p>
<c:if test="${not empty param.printFriendly }" >
	<td>
</c:if>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<%ParticipantEnrollmentSummary theItem=(ParticipantEnrollmentSummary)pageContext.getAttribute("theItem"); %>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
         		<tr>
          			<td width="1"></td>
          			<td width="69"></td><!--Name-->
          			<td width="1"></td>
          			<td width="105"></td><!--SSN-->
          			<td width="1"></td>
          			<td width="66"></td><!--Birth Date-->
          			<td width="1"></td>
          			<td width="60"></td><!--Payroll number-->
          			<td width="1"></td>
           			<td width="75"></td><!--Division-->
          			<td width="1"></td>
          			<td width="60"></td><!--Enrollment Method-->
          			<td width="1"></td>
          			<td width="60"></td><!--Enrollment Processing Date-->
          			<td width="1"></td>
          			<td width="60"></td><!--Eligible to Defer-->
          			<td width="1"></td>   
          			<td width="65"></td><!--Deferral at enrollment-->
          			<td width="1"></td>   
          			<td width="65"></td><!--Deferral at enrollment-->
          			<td width="4"></td>
          			<td width="1"></td>
        		</tr>


        <tr>
          <td class="tableheadTD1" colspan="22">
	      		<table border="0" cellspacing="0" cellpadding="0">
              		<tr>
                		<td class="tableheadTD"><b><%-- CMA managed--%><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/> </b>
                		</td>
             		</tr>
              	</table>
          </td>
        </tr>

        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="10" align="left" valign="top" class="datacell1">
	    	<table width="100%" border="0" cellpadding="2" cellspacing="0">
            	<tr>
                	<td width="45%" align="top"><b>Name</b></td>
                	<td class="highlight" width="55%" align="left">
${theItem.lastName},
${theItem.firstName}
					</td>
            	</tr>
          		<tr>
                	<td width="45%" align="top"><b>SSN</b></td>
                	<td class="highlight" width="55%">
						<NOBR><render:ssn property="theItem.ssn" /></NOBR>  
					</td>
            	</tr>
 				<tr>
					<td width="45%" align="top"><b>Date of birth</b></td>
         	<td class="highlight" width="55%">				
						<render:date property="theItem.birthDate" defaultValue="Not entered" patternOut="MM/dd/yyyy"/>
					</td>
       	</tr>
				<tr>
                	<td width="45%" align="top"><b>Residence state</b></td>
                	<td class="highlight" width="55%">
<c:if test="${not empty theItem.residenceStateCode}">
						<%if(theItem.getResidenceStateCode() == null || "".equals(theItem.getResidenceStateCode().trim())){ %>
							Not entered
						<% }else{%> 	
${theItem.residenceStateCode}
						<%}%>						
</c:if>
					</td>
            	</tr>
				
<c:if test="${not empty theItem.employerDesignatedID}">
				<%if(theItem.getEmployerDesignatedID() == null || "".equals(theItem.getEmployerDesignatedID().trim())){ %>
							
				<% }else{%> 				
					<tr>
                		<td width="45%" align="top"><b>Payroll number</b></td>
                		<td class="highlight" width="55%">
${theItem.employerDesignatedID}
						</td>
            		</tr>    
				<%}%>	
</c:if>

<c:if test="${not empty theItem.organizationUnitID}">
				<%if(theItem.getOrganizationUnitID() != null && 
				  !"".equals(theItem.getOrganizationUnitID().trim()) &&
				  participantEnrollmentDetailsReportForm.hasSpecialSortCategoryInd()) { %> 					
					<tr>
                		<td width="45%" align="top"><b>Division</b></td>
                		<td class="highlight" width="55%">
${theItem.organizationUnitID}
						</td>
            		</tr>
				<%}%>	
</c:if>
          	</table>
	  	  </td>
         <td colspan="10" align="left" valign="top" class="datacell1">
	    	<table width="100%" border="0" cellpadding="2" cellspacing="0">
            	<tr>
                	<td width="60%" align="top"><b>Enrollment method</b></td>
                	<td class="highlight" width="40%" align="left">
${theItem.enrollmentMethod}
					</td>
            	</tr>
          		<tr>
                	<td width="60%" align="top"><b>Enrollment processed date</b></td>
                	<td class="highlight" width="40%">
						<render:date property="theItem.enrollmentProcessedDate" patternOut="MM/dd/yyyy"/>						
					</td>
            	</tr>
 				<tr>
                	<td width="60%" align="top"><b>Normal retirement date</b></td>
                	<td class="highlight" width=40%">
                		<% if(theItem.getNormalRetirementDate() != null && 
                		!theItem.getNormalRetirementDate().equals("")) { %>
							<render:date property="theItem.normalRetirementDate" patternOut="MM/dd/yyyy"/>
						<% } else { %>
						Not entered
						<% } %>
					</td>
            	</tr>
				<tr>
                	<td width="60%" align="top"><b>Contribution status</b></td>
                	<td class="highlight" width="40%">
${theItem.contributionStatus}
					</td>
            	</tr>
		
				
				<% if(theItem.getEnrollmentMethod() != null && 
				(theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Internet") ||
				  theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Auto") ||
					theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Was auto"))) { %>	
					<tr>
        		<td width="60%" align="top"><b>Eligible to defer</b></td>
        		<td class="highlight" width="40%">
        		  <%if(theItem.getEligibleToDeferInd() == null || theItem.getEligibleToDeferInd().trim().equals("N/A")){ %>	
        		    <% if(!theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Internet")) { %>
        		      Yes
        		    <% }else{%>
					        N/A
        		    <% } %>
					    <% }else{%>
${theItem.eligibleToDeferInd}
					    <% } %>
 					  </td>
          </tr>
			  <% } %>							

				<% if (theItem.getDeferralComment().equals("N/A")){%>
				<% }else{%> 				
					<% if (!theItem.getDeferralComment().equals("Entered")){%>
						<tr>
                			<td width="60%" align="top"><b>Deferral at Enrollment</b></td>
                			<td class="highlight" width="40%">
${theItem.deferralComment}
							</td>
            			</tr>											
					<%} else {%>
					<% if(theItem.getEnrollmentMethod() != null && 
				        (theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Internet") ||
				        theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Auto") ||
					      theItem.getEnrollmentMethod().trim().equalsIgnoreCase("Was auto"))) { %>	
						<% if (theItem.hasTradDeferral()) { %>					
							<tr>					
								<td width=60%" align="top"><b>Deferral to Traditional 401(k)</b></td>
									<%if (theItem.getContributionAmt() > 0){%>
          								<td class="highlight" width="40%">
											<render:number property='theItem.contributionAmt' defaultValue = ""  pattern="$###,##0.00"/>
										</td>         							
									<%} else if (theItem.getContributionPct() > 0){%>
    									<td class="highlight" width="40%">    								
											<render:number property='theItem.displayContributionPct' defaultValue = "" scale='6' pattern="###.###%"/>	
										</td> 									
									<%} else {%>
    									<td class="highlight" width="40%">   										
											None
										</td>
									<%}%>	
            				</tr>
						<%}%>
						<%if (theItem.hasRothDeferral()){%>					
							<tr>					
								<td width=60%" align="top"><b>Deferral to Roth 401(k)</b></td>
									<%if (theItem.getContributionAmtRoth() > 0){%>
										<td class="highlight" width="40%">          								
											<render:number property='theItem.contributionAmtRoth' defaultValue = ""  pattern="$###,##0.00"/>
										</td>         							
									<%} else if (theItem.getContributionPctRoth() > 0){%>
			         					<td class="highlight" width="40%">  			
											<render:number property='theItem.displayContributionPctRoth' defaultValue = "" scale='6' pattern="###.###%"/>	
										</td>
									<%} else {%>
			         					<td class="highlight" width="40%">  
											None
										</td>
									<%}%>	
            				</tr>
            	<%}%>
						<%}%>
					<%}%>    
				<%}%>
				
		</table>
		</td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>     
	</tr>
	<tr>
		<td class="databorder" colspan="22"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td> 
	</tr>
		<tr>
	   		<td colspan="22">
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
  		 </tr>	

</table>
</c:forEach>
</c:if> 

     <br>
    </td>

    <!-- column to the right of the report -->
    <td width="15"></td>

<c:if test="${not empty param.printFriendly }" >
	</td>
</c:if>

<c:if test="${not empty param.printFriendly }" >
		<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
            type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
            id="globalDisclosure"/>

		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
			</tr>
		</table>
</c:if>

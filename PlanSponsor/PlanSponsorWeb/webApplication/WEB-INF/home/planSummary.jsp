<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="notifications" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<jsp:useBean id="today" class="java.util.Date" />
<%-- This jsp is included as part of the secureHomePage.jsp --%>
<c:set var="contractSummary" value="${requestScope.contractSummary}" /> 
<c:set var="uncashedChecks" value="${requestScope.uncashedChecks}" /> 
<c:set var="participantList" value="${contractSummary.participants}" /> 
<c:if test="${not empty userProfile.currentContract}">
<c:set var="contractStatus" value="${userProfile.currentContract.status}"/>
</c:if>

<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="<%=ContentConstants.PS_YOUR_PLAN_SUMMARY_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Your_Plan_Summary_Section_Title"/>
<content:contentBean contentId="<%=ContentConstants.PS_BGA_PLAN_SUMMARY_SECTION_TITLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="BGA_Plan_Summary_Section_Title"/>
<content:contentBean contentId= "<%=ContentConstants.MISCELLANEOUS_BATCH_LATE_WARNING %>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="LateBatchWarning"/>
<table cellpadding="0" cellspacing="0" border="0" width="500">

	     <c:if test ="${lateBatch=='true' }" >
	    
	            <tr>
	                <td colspan ="2"><content:getAttribute beanName="LateBatchWarning" attribute="text"/>
			</td>
				</tr>
			 <tr>
	                <td colspan ="2">&nbsp;&nbsp</td>
				</tr>
				<br>
</c:if>
	            <tr>
	                <td><b><img src="/assets/unmanaged/images/s.gif" width="5" height="1">
${contractSummary.contractName}</b> |
	                <b>Contract:</b> 
${contractSummary.contractNumber}

					<%-- Business rule:
						SPR.91.	If the user does not have more than one Contract associated with their User ID
						 then the Change Contract page element will not display.
						 
						 SPR.78.	If the user is associated with more than one but less than or equal to 20 contracts, selecting "Change contract" will display the "select contract" page.  
						 
						 SPR.79.	If the user is associated with more than 20 contracts, selecting the "Change contract" element will navigate the user to the Search Contract Page. 
					--%> 
					<c:if test="${userProfile.numberOfContracts gt '1'}">
					<c:if test="${userProfile.numberOfContracts gt '20'}">
						(<a href="/do/home/ChangeContract/">change</a>)</td>
					</c:if>
					<c:if test="${userProfile.numberOfContracts lt '20'}" >
						(<a href="/do/home/ChangeContract/">change</a>)</td>
					</c:if></c:if>
				
					<%-- This is a new requirement from TPA rewrite --%>
<c:if test="${userProfile.numberOfContracts ==1}">
					  <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" > 
  					    <%-- Link to the select contracts page --%>
					    (<a href="/do/home/ChangeContract/">change</a>)</td>
					  </ps:isTpa> 					    
</c:if>
                    <c:if test="${not empty userProfile.currentContract}">
	                <td align="right">As of <render:date property="userProfile.currentContract.contractDates.asOfDate" patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" defaultValue = "" /></td>
	                </c:if>
	            </tr>
	        </table>

	        <table width="508" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	                <td height="3"></td>
	            </tr>
	            <tr class="tablehead">
	                <td class="tableheadTD1" colspan="2">
	                <b> <%-- CMA managed--%>
	                <c:if test="${not empty userProfile.currentContract}">
<c:if test="${currentContract.bundledGaIndicator ==true}">
		                <ps:isInternalUser name="userProfile" property="role">
		                	<content:getAttribute beanName="BGA_Plan_Summary_Section_Title" attribute="title"/>
		                </ps:isInternalUser>
		                <ps:isExternal name="userProfile" property="role">
		                	<content:getAttribute beanName="Your_Plan_Summary_Section_Title" attribute="title"/>
		                </ps:isExternal>		                
</c:if>
		               </c:if>					   
		               <c:if test="${not empty userProfile.currentContract}">
<c:if test="${currentContract.bundledGaIndicator !=true}">
		                	<content:getAttribute beanName="Your_Plan_Summary_Section_Title" attribute="title"/>
</c:if>
     					</c:if>		               
		            </b>
	                </td>
	            </tr>
	        </table>
	        <table cellpadding="0" cellspacing="0" border="0" width="508">
	            <tr class="datacell1" style="padding: 0px" height="42">
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td></td>
	                <td><b>Total contract assets</b><br>
	                <c:if test="${not empty contractPeraDetailsVO}">
	              <c:if test="${contractPeraDetailsVO.contractIsPera == true}">
		                
		                (excludes uncashed checks and PERA)
		               </c:if>
		                <c:if test="${contractPeraDetailsVO.contractIsPera == false}">
		               
		                (excludes uncashed checks)
		                </c:if>
	              </c:if>
	              <c:if test="${ empty contractPeraDetailsVO}">
	                
	                (excludes uncashed checks)
	              </c:if> </td>
	              
	                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td align="center">
	                <div align="right" style="padding-right: 5"><b class="highlight"><render:number property="contractSummary.totalContractAssets" type="c" defaultValue="10000.00"/></b></div>
	                </td>
	                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td align="right">
	                	<a class="b11" href="/do/contract/contractSnapshot/">Contract snapshot</a><br>
	                </td>
	                <td></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            
	        <c:if test="${not empty contractPeraDetailsVO}">    

			       <c:if test="${contractPeraDetailsVO.contractIsPera == true}">
		            <tr class="datacell1" style="padding: 0px" height="42">
		                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                <td></td>
		                <td><b>PERA Balance as of <fmt:formatDate value="${today}" pattern="MM/dd/yyyy"/></b></td>
		                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                <td align="center">
		                <div align="right" style="padding-right: 5"><b class="highlight"><render:number property="contractPeraDetailsVO.availablePeraBalance" type="c" defaultValue="0.00"/></b></div>
		                </td>
		                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		                <td></td>
		                <td></td>
		                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		            </tr>
		            </c:if>
	            </c:if>
	            <tr class="datacell2" style="padding: 0px" height="42">
	                <td height="15" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td></td>
	                <td><b>Cash account balance</b><br>
	                (amount included in total contract assets)</td>
	                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td align="center">
	                <div align="right" style="padding-right: 5"><b class="highlight"><render:number property="contractSummary.cashAccountBalanace" type="c" defaultValue="20000.00"/></b></div>
	                </td>
	                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" ><td align="right"><a class="b11" href="/do/transaction/cashAccountReport/">Cash account / <br>payment history</a></td></ps:isNotJhtc>
	                <td></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <%-- CL 134735: June 2016 Release ME --%>
	            <tr class="datacell1" style="padding: 0px" height="42">
	                <td height="15" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td></td>
	                <td><b>Uncashed checks as of <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="uncashedChecks.asOfDate"/></b><br>
	                (amount not included in total contract assets)</td>
	                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td align="center">
	                <div align="right" style="padding-right: 5"><b class="highlight"><render:number property="uncashedChecks.uncashedChecksValue" type="c" defaultValue="20000.00"/></b></div>
	                </td>
	                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" ><td align="right"><a class="b11" href="/do/transaction/uncashedChecksReport/">Uncashed checks</a></td></ps:isNotJhtc>
	                <td></td>
	                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
<% int theIndex = 0; %>
			<%-- Business Rule:
			SPR.88.	If a contract does not have Loans as a product feature associated with the contract
			 the following page elements will not be displayed
			--%>
			<%-- Show outstanding loans section if outstandingLoansCount is greater than 0 --%>
			<c:if test ="${showLoans==true }" >
					<% if (theIndex++ % 2 == 0) { %>
			            <tr class="datacell2" style="padding:0px" height="42">
			        <% } else { %>
			            <tr class="datacell1" style="padding:0px" height="42">
                    <% } %>
			        	  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			        	  <td></td>
			        	  <td><b>Loans outstanding</b></td>
			        	  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			        	  <td align="center">
			                  <div align="right" style="padding-right: 5">
			                  	<b><render:number property="contractSummary.outstandingLoansCount" type="i" pattern="######"/>
<c:if test="${contractSummary.outstandingLoansCount ==1}">
			                  	loan:
</c:if>
<c:if test="${contractSummary.outstandingLoansCount !=1}">
			                  	loans: 
</c:if>
			                  	<render:number property ="contractSummary.outstandingLoans" type="c" defaultValue="30000.00"/>
			            		<span class="highlight"></b></span>
			            	  </div>
			                </td>
			        	  <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			        	  <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" ><td align="right"><a class="b11" href="/do/transaction/loanSummaryReport/">Loan summary</a></td></ps:isNotJhtc>
			        	  <td></td>
			        	  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			        	</tr>
</c:if>
	            
			<tr class="datacell1">
				<td colspan="9" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</tr>	            
			
        	<tr height="1">
        	<td width="1" nowrap></td>
        	<td width="9" nowrap></td>
        	<td width="161" nowrap></td>
        	<td width="1" nowrap></td>
        	<td width="160" nowrap></td>
        	<td width="1" nowrap></td>
        	<td width="161" nowrap></td>
        	<td width="9" nowrap></td>
        	<td width="1" nowrap></td>
			</tr>
			</table>

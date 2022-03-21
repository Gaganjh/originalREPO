<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<content:contentBean contentId="<%=ContentConstants.REPORTING_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="reportingGroupTitle"/>
<content:contentBean contentId="<%=ContentConstants.PLAN_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="planGroupTitle"/>
<content:contentBean contentId="<%=ContentConstants.CLIENT_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="clientGroupTitle"/>
<content:contentBean contentId="<%=ContentConstants.PARTICIPANT_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="participantGroupTitle"/>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.profiles.TPAUserContractAccess" %>
<STYLE type="text/css">
   .borderLeft {border-left: 1px solid #89A2B3;}
   .borderRight {border-right: 1px solid #89A2B3;}
   .borderBottom {border-bottom: 1px solid #89A2B3;}
   .dataDividerRight {border-right: 1px solid #CCCCCC;}
   .boldEntry { font-weight:bold;}
   .subsubHeader { background-color: #E6E6E6; }
</STYLE>

 
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



             
<c:set var="addEditUserForm" value="${tpaFirmForm}" scope="request"/>

<c:forEach items="${addEditUserForm.contractAccesses}" var="contractAccess" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/> 
<c:set var="contractAccess" value="${contractAccess}"/>




	      	<logicext:if name="contractAccess" property="hasAccess" op="equal" value="false">
				<logicext:then>
					<content:getAttribute beanName="messageNoPermissions" attribute="text" />
				</logicext:then>
			</logicext:if>
	<%
		Integer value=Integer.parseInt(pageContext.getAttribute("indexValue").toString());
		String uploadSubmissionsIniValName = "uploadSubmissionsIniVal" + value;
		String viewAllSubmissionsIniValName = "viewAllSubmissionsIniVal" + value;
		String cashAccountIniValName = "cashAccountIniVal" + value;
		String directDebitIniValName = "directDebitIniVal" + value;
	%>
	<%-- The submissionAccessUserCount is the number of users who have access to View submissions
	     minus the current user (if the current user has that permission). --%>
<form:hidden path="contractAccesses[${theIndex.index}].numberOfSelectedDirectDebitAccounts" /><%--  input - indexed="true" name="contractAccess" --%>
<form:hidden path="contractAccesses[${theIndex.index}].permissionsNotShown" /><%--  input - indexed="true" name="contractAccess" --%>
<form:hidden path="contractAccesses[${theIndex.index}].accountsNotShown" /><%--  input - indexed="true" name="contractAccess" --%>
<form:hidden path="contractAccesses[${theIndex.index}].contractNumber" /><%--  input - indexed="true" name="contractAccess" --%>
	<table border="0" cellpadding="2" cellspacing="0" width="412" class="borderBottom">
		<tbody>
		 <tr class="tablehead">
		     <td class="tableheadTD1" colspan="2" style="padding:4px"><strong><content:getAttribute id="layoutPageBean" attribute="body1Header"/> </strong></td>
		 </tr>
		 <tr>
		     <td  width="60%" class="borderLeft " ><strong>TPA firm ID </strong></td>
<td class="highlight borderRight">${tpaFirmForm.id}</td>
		   </tr>
		<tr class="datacell1">
		   	<td  class="borderLeft " width="127"><strong>TPA firm name </strong></td>
<td class="highlight borderRight ">${tpaFirmForm.name}</td>
		</tr>
		<c:if test="${addEditUserForm.showClientServicesSection}" >
        <tr>
       		<td colspan="2" class="tablesubhead borderLeft borderRight" style="padding:0px">&nbsp;<b><content:getAttribute beanName="clientGroupTitle" attribute="text"/></b></td>
	    </tr>

    	<c:if test="${contractAccess.showSigningAuthority}" >
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >Signing authority </td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].signingAuthority">
		    		<render:yesno property="contractAccess.signingAuthority"/>
		    	</ps:highlightIfChanged>
		    </td>
		</tr>
		</c:if>
		
		<c:if test="${addEditUserForm.showTPAUserList}">
			<c:if test="${contractAccess.signingAuthority}">
		  		<tr valign="top">
		       		<td class="datacell2 borderLeft borderRight" align="left" nowrap colspan="3">
						<c:forEach items="${addEditUserForm.selectedTPAUsersAsList}" var="user">
&nbsp;&nbsp;&nbsp;${user.firstName} ${user.lastName}</br>
						</c:forEach>
					</td>
		    	</tr>
			</c:if>
		</c:if>
	    
    	<c:if test="${addEditUserForm.showSubmissionsSection}" >
    	<tr>
    		<td colspan="2" class="subsubHeader borderRight borderLeft boldEntry" >Submissions </td>
		</tr>
   		<c:if  test="${contractAccess.showUploadSubmissions}">
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >&nbsp;&nbsp;Create/upload submissions </td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].uploadSubmissions">
		    		<render:yesno property="contractAccess.uploadSubmissions"/>
		    	</ps:highlightIfChanged>
			</td>
		</tr>
		</c:if>
   		<c:if  test="${contractAccess.showCashAccount}">
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >&nbsp;&nbsp;Cash Account </td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].cashAccount">
		    		<render:yesno property="contractAccess.cashAccount"/>
		    	</ps:highlightIfChanged>
			</td>
		</tr>	
		</c:if>
   		<c:if  test="${contractAccess.showDirectDebit}">
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >&nbsp;&nbsp;Direct debit </td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].directDebit">
		    		<render:yesno property="contractAccess.directDebit"/>
		    	</ps:highlightIfChanged>
			</td>
		</tr>
<c:if test="${not empty contractAccess.directDebitAccounts}">
  		<tr valign="top">
		       		<td class="datacell2 borderLeft borderRight" align="left" nowrap colspan="3">
<c:forEach items="${contractAccess.selectedDirectDebitAccountsAsList}" var="account" >

&nbsp;&nbsp;&nbsp;&nbsp;${account.label}<br/>
</c:forEach>
					</td>
		    	</tr>
</c:if>
        </c:if>			
		</c:if>		
		<c:if test="${addEditUserForm.showLoansSection}">		
    	<tr>
    		<td colspan="2" class=" subsubHeader borderRight borderLeft boldEntry" >Loans</td>
		</tr>
		<c:if test="${contractAccess.showInitiateLoans}">		
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >Initiate loans </td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].initiateLoans">
		    		<render:yesno property="contractAccess.initiateLoans"/>
		    	</ps:highlightIfChanged>
		    </td>

		</tr>
		</c:if>
		<c:if test="${contractAccess.showReviewLoans}">		
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >Review loans</td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].reviewLoans">
		    		<render:yesno property="contractAccess.reviewLoans"/>
		    	</ps:highlightIfChanged>
		    </td>
		</tr>
		</c:if>
	</c:if>
		<c:if test="${addEditUserForm.showiWithdrawalsSection}">		
    	<tr>
    		<td colspan="2" class=" subsubHeader borderRight borderLeft boldEntry" >i:withdrawals</td>
		</tr>
		<c:if test="${contractAccess.showInitiateIWithdrawals}">		
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >Initiate i:withdrawals </td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].initiateIWithdrawals">
		    		<render:yesno property="contractAccess.initiateIWithdrawals"/>
		    	</ps:highlightIfChanged>
		    </td>

		</tr>
		</c:if>
		<c:if test="${contractAccess.showReviewIWithdrawals}">		
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >Review i:withdrawals</td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].reviewIWithdrawals">
		    		<render:yesno property="contractAccess.reviewIWithdrawals"/>
		    	</ps:highlightIfChanged>
		    </td>
		</tr>
		</c:if>

		<c:if test="${addEditUserForm.showCensusManagementSection}">		
    	<tr>
    		<td colspan="2" class="subsubHeader tablesubhead borderRight borderLeft boldEntry" >Census Management</td>
		</tr>
 		<c:if test="${contractAccess.showUpdateCensusData}">		
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >Update census data</td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].updateCensusData">
		    		<render:yesno property="contractAccess.updateCensusData"/>
		    	</ps:highlightIfChanged>
		    </td>

		</tr>																													
		</c:if>
		</c:if>	
		</c:if>	
		</c:if>
		
		<c:if test="${addEditUserForm.showPlanServicesSection}" >
        <tr>
       		<td colspan="2" class="tablesubhead borderLeft borderRight" style="padding:0px">&nbsp;<b><content:getAttribute beanName="planGroupTitle" attribute="text"/></b></td>
	    </tr>
	    <c:if test="${contractAccess.showFeeAccess404A5}">		
    	<tr>
    		<td class="datacell2  borderLeft boldEntry" >404a-5 individual expense management</td>
		    <td class="datacell2  borderRight" >
		    	<ps:highlightIfChanged name="theForm" property="contractAccess[0].feeAccess404A5">
		    		<logicext:if name="theForm" property="contractAccess[0].feeAccess404A5" op="equal" value="false">
						<logicext:then>Yes</logicext:then>
						<logicext:else>No</logicext:else>
					</logicext:if>
		    	</ps:highlightIfChanged>
		    </td>

		</tr>																													
		</c:if>
		</c:if>
	</table>
	<br/>
	<br/>
</c:forEach>

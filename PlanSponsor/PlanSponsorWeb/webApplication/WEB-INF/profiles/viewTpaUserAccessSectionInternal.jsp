<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>  
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<content:contentBean contentId="<%=ContentConstants.USER_MANAGEMENT_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitleUserManagement"/>

<content:contentBean contentId="<%=ContentConstants.REPORTING_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitleReporting"/>

<content:contentBean contentId="<%=ContentConstants.PLAN_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitlePlanServices"/>

<content:contentBean contentId="<%=ContentConstants.CLIENT_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="groupTitleClientServices"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_ALL_DIRECT_DEBIT_ACCOUNTS_SELECTED%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningAllDirectDebitAccountsSelected"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_REMOVE_DIRECT_DEBIT_PERMISSION_REMOVE_DIRECT_DEBIT_ACCOUNTS%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningRemoveSelectedDirectDebitAccounts"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_SUBMISSION_ACCESS_ONLY%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningSubmissionAccessOnly"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_SELECTED%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoSelectedDirectDebitAccounts"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_AVAILABLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoAvailableDirectDebitAccounts"/>



<table >

<c:forEach items="${addEditUserForm.tpaFirms}" var="tpaFirm" varStatus="tpaIndex" >





<c:if test="${tpaFirm.removed !=true}">
<c:if test="${tpaFirm.hidden !=true}">
<c:set var="indexValue" value="${tpaIndex.index}"/>
	<%
	String indexvalue = pageContext.getAttribute("indexValue").toString();
	String uploadSubmissionsIniValName = "uploadSubmissionsIniVal" + indexvalue;
	String viewAllSubmissionsIniValName = "viewAllSubmissionsIniVal" + indexvalue;
	String cashAccountIniValName = "cashAccountIniVal" + indexvalue;
	String directDebitIniValName = "directDebitIniVal" + indexvalue;	
	%>

<tr>
	<td>
	<TABLE class=box style="CURSOR: pointer"
onclick="expandSection('${tpaFirm.id}')" cellSpacing=0 cellPadding=3
		width=400 border=0>
			<TR class=tablehead>
				<TD class=tableheadTD1>
<img id="${tpaFirm.id}img" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;
<B>${tpaFirm.name},&nbsp;${tpaFirm.id} permissions</B>
				</TD>
			</TR>
	</TABLE>
	
<DIV class=switchcontent id=${tpaFirm.id}>
	<TABLE width=400 class=box cellSpacing=0 cellPadding=3 border=0>
	  <TR>
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td width="200"><strong>TPA firm ID</strong></td>
<td width="200">${tpaFirm.id}</td>
        <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  <tr>
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td><strong>TPA firm name</strong></td>
<td>${tpaFirm.name}</td>
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  <!-- Permissions will be displayed only when WebAccess = Yes -->
	  <c:if test="${addEditUserForm.webAccess}">
      <TR class=tablesubhead>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   <td colspan="2">
	     <strong><content:getAttribute beanName="groupTitleUserManagement" attribute="text"/><strong>
	   </td>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;Manage users </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].manageUsers">
			    <render:yesno property="tpaFirm.contractAccesses[0].manageUsers" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  <tr class="datacell2">
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;TPA staff plan access </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].tpaStaffPlanAccess">
			    <render:yesno property="tpaFirm.contractAccesses[0].tpaStaffPlanAccess" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  
      <TR class=tablesubhead>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   <td colspan="2">
	     <strong><content:getAttribute beanName="groupTitleReporting" attribute="text"/><strong>
	   </td>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;Download reports - full SSN </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].reportDownload">
			    <render:yesno property="tpaFirm.contractAccesses[0].reportDownload" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
<!--	  
<c:if test="${tpaFirm.contractAccesses[0].userPermissions.showPlanServicesSection==true}"> 
      <TR class=tablesubhead>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   <td colspan="2">
	     <strong><content:getAttribute beanName="groupTitlePlanServices" attribute="text"/><strong>
	   </td>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;Plan data management </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].submitUpdateVesting">
			    <render:yesno property="tpaFirm.contractAccesses[0].submitUpdateVesting" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  	  	  
</c:if>
-->
      <TR class=tablesubhead>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   <td colspan="2">
	     <strong><content:getAttribute beanName="groupTitleClientServices" attribute="text"/><strong>
	   </td>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	
	<%-- Signing Authority - Loans - Begin --%>
<c:if test="${tpaFirm.contractAccesses[0].userPermissions.showSigningAuthority ==true}">
	      	<TR class="datacell2">
    		  	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   			<td><strong>Signing authority<strong></td>
     			<td>   
		        	<ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].userPermissions.signingAuthority">
						<render:yesno property="tpaFirm.contractAccesses[0].userPermissions.signingAuthority" defaultValue="no" style="c" />	  
					</ps:highlightIfChanged>
			 	</td>
             	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  	</tr>	  
</c:if>
	<%-- Signing Authority - Loans - Begin --%>
      <TR bgcolor="#E6E6E6">
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   <td colspan="2">
	     <strong>Submissions<strong>
	   </td>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;&nbsp;&nbsp;&nbsp;Create/Upload submissions </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].userPermissions.createUploadSubmissions">
			    <render:yesno property="tpaFirm.contractAccesses[0].userPermissions.createUploadSubmissions" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;&nbsp;&nbsp;&nbsp;Cash account </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].userPermissions.cashAccount">
			    <render:yesno property="tpaFirm.contractAccesses[0].userPermissions.cashAccount" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;&nbsp;&nbsp;&nbsp;Direct debit </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].userPermissions.directDebit">
			    <render:yesno property="tpaFirm.contractAccesses[0].userPermissions.directDebit" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  
<c:if test="${tpaFirm.contractAccesses[0].userPermissions.showReviewLoans ==true}">
      <TR bgcolor="#E6E6E6">
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   <td colspan="2">
	     <strong>Loans<strong>
	   </td>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;View all loans </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].userPermissions.viewAllLoans">
			    <render:yesno property="tpaFirm.contractAccesses[0].userPermissions.viewAllLoans" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;&nbsp;&nbsp;&nbsp;Initiate loans </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].userPermissions.initiateLoans">
			    <render:yesno property="tpaFirm.contractAccesses[0].userPermissions.initiateLoans" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  	  
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review loans </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].reviewLoans">
			    <render:yesno property="tpaFirm.contractAccesses[0].reviewLoans" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  	  	  
</c:if>

<c:if test="${tpaFirm.contractAccesses[0].userPermissions.showReviewWithdrawals ==true}">
      <TR bgcolor="#E6E6E6">
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   <td colspan="2">
	     <strong>i:withdrawals<strong>
	   </td>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;View all i:withdrawals </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].userPermissions.viewAllWithdrawals">
			    <render:yesno property="tpaFirm.contractAccesses[0].userPermissions.viewAllWithdrawals" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;&nbsp;&nbsp;&nbsp;Initiate i:withdrawals </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].userPermissions.initiateAndViewMyWithdrawals">
			    <render:yesno property="tpaFirm.contractAccesses[0].userPermissions.initiateAndViewMyWithdrawals" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  	  
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review i:withdrawals </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].reviewIWithdrawals">
			    <render:yesno property="tpaFirm.contractAccesses[0].reviewIWithdrawals" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  	  	  
</c:if>
	  
      <TR bgcolor="#E6E6E6">
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	   <td colspan="2">
	     <strong>Census Management<strong>
	   </td>
       <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;Update census data </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].updateCensusData">
			    <render:yesno property="tpaFirm.contractAccesses[0].updateCensusData" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  <tr class="datacell2">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>&nbsp;View salary </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].viewSalary">
			    <render:yesno property="tpaFirm.contractAccesses[0].viewSalary" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
	  
	  </c:if>
	  
	  <!-- Contract Information section : only for Single TPA firm view in edit mode -->
<c:if test="${addEditUserForm.fromTPAContactsTab ==true}">
		<logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
			<logicext:then>
				<tr class="datacell1">
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td colspan="2">
						<table width="100%" border="0" cellpadding="3" cellspacing="0">
							<tr valign="top">
						 		<td width="200"><strong>Contract Number</strong></td>
						    	<td width="200">${userProfile.currentContract.contractNumber}</strong></td>
						  	</tr>
						
					        <tr valign="top">
					     		<td><strong>Contract name</strong></td>
					        	<td>${userProfile.currentContract.companyName}</strong>
					            </td>
					  		</tr>
					  		
					  		<tr valign="center">
						 		<td><strong>Signature Received - Authorized Signer</strong></td>
						    	<td align="left" nowrap>
						    	 <ps:highlightIfChanged name="addEditUserForm" property="signatureReceivedAuthSigner">
							    	<render:yesno property="addEditUserForm.signatureReceivedAuthSigner" defaultValue="no" style="c" />
							     </ps:highlightIfChanged>
						    	</td>
						  	</tr>
						
					        <tr valign="top">
					     		<td><strong>TPA Contact</strong></td>
					        	<td align="left" nowrap>
					        	 <ps:highlightIfChanged name="addEditUserForm" property="primaryContact">
							    	<render:yesno property="addEditUserForm.primaryContact" defaultValue="no" style="c" />
							     </ps:highlightIfChanged>
					        	</td>
					  		</tr> 
						</table>
					</td>
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>
			</logicext:then>
		</logicext:if>
</c:if>
	
	  <tr>
		  <td colspan="4" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  
	</table>
	</div>
	</td>
	</tr>
	
	<tr><td><br/></td></tr>
</c:if> <%-- TPA firm not hidden --%>
</c:if> <%-- TPA firm not removed --%>
	
</c:forEach>

</table>	

<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditUserForm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<content:contentBean contentId="<%=ContentConstants.WARNING_REMOVE_ALL_SUBMISSION_PERMISSION%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningRemoveAllSubmissionPermission"/>

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%
int rowIndex = 0;
%>

<c:forEach items="${addEditUserForm.tpaFirms}" var="tpaFirm" varStatus="tpaIndex" >





<c:if test="${tpaFirm.removed !=true}">
<c:if test="${tpaFirm.hidden !=true}">
	
    <% if (rowIndex % 2 == 0) { %>
	    <tr class="datacell2">
	<% } else { %>
	    <tr class="datacell1">
	<% }
	
	   rowIndex++;
	
	%>
	
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<td colspan="6" align="center">
	
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
	
<form:hidden  path="tpaFirms[${tpaIndex.index}].contractAccesses[0].contractNumber" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].originalReviewIWithdrawals" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastRegisteredUser" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].id" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].name" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithManageUsers" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithReceiveILoansEmail" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithReceiveILoansEmailAndTPAStaffPlan" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithSigningAuthorityContracts" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithReviewIWithdrawalsContracts" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].permissionsNotShown" /><%--  input - indexed="true" name="tpaFirm" --%>

<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].userPermissions.createUploadSubmissions" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].userPermissions.cashAccount" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].userPermissions.directDebit" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].userPermissions.initiateAndViewMyWithdrawals" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].userPermissions.signingAuthority" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].userPermissions.initiateLoans" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].userPermissions.viewAllLoans" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].userPermissions.reviewLoans" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithReviewLoansContracts" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].originalReviewLoans" /><%--  input - indexed="true" name="tpaFirm" --%>
	

	<ps:trackChanges  name="addEditUserForm" indexPrefix="tpaFirms" index="${tpaIndex.index}" property="id"/>
	<c:set var="count" value="${tpaIndex.index}"/> 
	<c:set var="id" value="${tpaFirm.id}"/> 
	<c:set var="contractAccess" value="${tpaFirm.getContractAccess(0)}"/> 
	
	<% int indexValue = Integer.parseInt(pageContext.getAttribute("count").toString()); 
	    int indexId = Integer.parseInt(pageContext.getAttribute("id").toString()); 
	    String jsFunction = "javascript:removeTpa('" + indexId + "', '" + indexValue + "')";
	    %>
	    <tr valign="top">
	 		<td width="46%"><strong>TPA firm ID</strong></td>
<td colspan="3"><strong class="highlight">${tpaFirm.id}</strong>
	            </td>
	  	</tr>
	
        <tr valign="top">
     		<td width="46%"><strong>TPA firm name</strong></td>
<td colspan="3"><strong class="highlight">${tpaFirm.name}</strong>
            </td>
  		</tr>

 <c:if test="${userProfile.internalUser ==true}">
		<tr valign="top">
		    <td width="46%"><strong>Manage users</strong></td>
       		<td align="left" nowrap>
 <form:radiobutton onclick="doRefresh();" indexed="true" path="tpaFirms[${tpaIndex.index}].contractAccesses[0].manageUsers" value="true" /> 
           		 		yes</td>
  			<td align="left" nowrap>
  			<form:radiobutton onclick="doRefresh();" indexed="true" path="tpaFirms[${tpaIndex.index}].contractAccesses[0].manageUsers" value="false"/>
  						no</td>
        	<td align="left" nowrap>&nbsp;</td>
 		    <ps:trackChanges indexPrefix="tpaFirms" name="addEditUserForm" index="${tpaIndex.index}" property="contractAccesses[0].manageUsers"/>     
        </tr>
</c:if>
<c:if test="${userProfile.internalUser !=true}">
<form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].manageUsers"/>
</c:if>

	
	
		<logicext:if name="tpaFirm" property="contractAccesses[0].showTpaStaffPlanAccess" op="equal" value="true">
			<logicext:then>
			
		  		<tr valign="top">
		       		<td>
		       			<strong>TPA staff plan access</strong>
		       		</td>
		       		<td align="left" nowrap>
		       		<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].tpaStaffPlanAccess" indexed="true" value="true"/>
		           		 		yes</td>
		  			<td align="left" nowrap>
		  				<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].tpaStaffPlanAccess" indexed="true" value="false"/>
		  						no</td>
		        	<td align="left" nowrap>&nbsp;</td>
		 		    <ps:trackChanges indexPrefix="tpaFirms" index="${tpaIndex.index}" name="addEditUserForm" property="contractAccesses[0].tpaStaffPlanAccess"/>
		     	</tr>
	        </logicext:then>
	        <logicext:else>
	        <form:hidden indexed="true" path="tpaFirms[${tpaIndex.index}].contractAccesses[0].tpaStaffPlanAccess" />
	        </logicext:else>
		</logicext:if>
		
		<logicext:if name="tpaFirm" property="contractAccesses[0].showUploadSubmissions" op="equal" value="true">			
			<logicext:then>
            <tr valign="top">
           		<td> <strong>Create submissions</strong></td>
                <td align="left" nowrap>
                <form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].uploadSubmissions" indexed="true" value="true"/>&nbsp;yes
                </td>
                <td align="left" nowrap>
<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].uploadSubmissions" indexed="true" value="false"/>&nbsp;no
  			    </td>
         	    <td align="left" nowrap>&nbsp;</td>
                <ps:trackChanges indexPrefix="tpaFirms" index="${tpaIndex.index}" name="addEditUserForm" property="contractAccesses[0].uploadSubmissions"/>
            </tr>
           </logicext:then> 
	        <logicext:else>
	        <form:hidden indexed="true" path="tpaFirms[${tpaIndex.index}].contractAccesses[0].uploadSubmissions"/>
	        </logicext:else>
 		</logicext:if>   

		<logicext:if name="tpaFirm" property="contractAccesses[0].showReportDownload" op="equal" value="true">
			<logicext:then>
	  		<tr valign="top">
	       		<td>
	       			<strong>Download reports - full SSN</strong>
	       		</td>
	       		<td align="left" nowrap>
	       		<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].reportDownload" indexed="true" value="true"/>&nbsp;yes</td>
	  			<td align="left" nowrap>
<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].reportDownload" indexed="true" value="false"/>&nbsp;no</td>
	        	<td align="left" nowrap>&nbsp;</td>
	 		    <ps:trackChanges indexPrefix="tpaFirms" index="${tpaIndex.index}" name="addEditUserForm"  property="contractAccesses[0].reportDownload"/>
	     	</tr>
	        </logicext:then>
	        <logicext:else>
	        <form:hidden indexed="true" path="tpaFirms[${tpaIndex.index}].contractAccesses[0].reportDownload"/>
	        </logicext:else>
		</logicext:if>

		<logicext:if name="tpaFirm" property="contractAccesses[0].showUpdateCensusData" op="equal" value="true">
			<logicext:then>
	  		<tr valign="top">
	       		<td>
	       			<strong>Update census data</strong>
	       		</td>
	       		<td align="left" nowrap>
	       		<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].updateCensusData" indexed="true" value="true"/>&nbsp;yes</td>
	  			<td align="left" nowrap>
	<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].updateCensusData" indexed="true" value="false"/>&nbsp;no</td>
	        	<td align="left" nowrap>&nbsp;</td>
	 		    <ps:trackChanges name="addEditUserForm" indexPrefix="tpaFirms" index="${tpaIndex.index}" property="contractAccesses[0].updateCensusData"/>
	     	</tr>
	        </logicext:then>
	        <logicext:else>
	        <form:hidden indexed="true" path="tpaFirms[${tpaIndex.index}].contractAccesses[0].updateCensusData"/>
	        </logicext:else>
		</logicext:if>

		<logicext:if name="tpaFirm" property="contractAccesses[0].showViewSalary" op="equal" value="true">
			<logicext:then>		
	  		<tr valign="top">
	       		<td>
	       			<strong>View salary</strong>
	       		</td>
	       		<td align="left" nowrap>
<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].viewSalary" indexed="true" value="true"/>&nbsp;yes
	       		</td>
	  			<td align="left" nowrap>
<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].viewSalary" indexed="true" value="false"/>&nbsp;no
	  			</td>
	        	<td align="left" nowrap>&nbsp;</td>
	 		    <ps:trackChanges name="addEditUserForm" indexPrefix="tpaFirms" index="${tpaIndex.index}" property="contractAccesses[0].viewSalary"/>
	     	</tr>
	        </logicext:then>
	        <logicext:else>
	        <form:hidden indexed="true" path="tpaFirms[${tpaIndex.index}].contractAccesses[0].viewSalary" />
	        </logicext:else>
		</logicext:if>

		<logicext:if name="tpaFirm" property="contractAccesses[0].showReviewLoans" op="equal" value="true">
			<logicext:then>		
	  		<tr valign="top">
	       		<td>
	       			<strong>Initiate/review loans</strong>
	       		</td>
	       		<td align="left" nowrap>
<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].reviewLoans" indexed="true" value="true"/>&nbsp;yes
	       		</td>
	  			<td align="left" nowrap>
<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].reviewLoans" indexed="true" value="false"/>&nbsp;no
	  			</td>
	        	<td align="left" nowrap>&nbsp;</td>
	 		    <ps:trackChanges name="addEditUserForm" indexPrefix="tpaFirms" index="${tpaIndex.index}" property="contractAccesses[0].reviewLoans"/>
	     	</tr>
	        </logicext:then>
	        <logicext:else>
	        <form:hidden indexed="true" path="tpaFirms[${tpaIndex.index}].contractAccesses[0].reviewLoans"/>
	        </logicext:else>
		</logicext:if>

		<logicext:if name="tpaFirm" property="contractAccesses[0].showReviewIWithdrawals" op="equal" value="true">
			<logicext:then>		
	  		<tr valign="top">
	       		<td>
	       			<strong>Initiate/review i:withdrawals</strong>
	       		</td>
	       		<td align="left" nowrap>
<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].reviewIWithdrawals" indexed="true" value="true"/>&nbsp;yes
	       		</td>
	  			<td align="left" nowrap>
<form:radiobutton path="tpaFirms[${tpaIndex.index}].contractAccesses[0].reviewIWithdrawals" indexed="true" value="false"/>&nbsp;no
	  			</td>
	        	<td align="left" nowrap>&nbsp;</td>
	 		    <ps:trackChanges name="addEditUserForm" indexPrefix="tpaFirms" index="${tpaIndex.index}" property="contractAccesses[0].reviewIWithdrawals"/>
	     	</tr>
	        </logicext:then>
	        <logicext:else>
	        <form:hidden path="tpaFirms[${tpaIndex.index}].contractAccesses[0].reviewIWithdrawals"/>
	        </logicext:else>
		</logicext:if>
		        
		<tr>
			<td align="right" colspan="4">
<% if (rowIndex % 2 == 0) { %>			
<c:if test="${addEditUserForm.fromTPAContactsTab !=true}">
		       <input type="button" name="actionLabel" 
		         value="delete" 
		         onclick="<%=jsFunction%>" class="button134Cell1">	  
</c:if>

<c:if test="${addEditUserForm.webAccess}">
<c:if test="${userProfile.internalUser ==true}">
		       &nbsp;&nbsp;
		       <input type="submit" name="actionLabel" 
		         value="change Permissions" 
		         onclick="return doChangePermissions('${tpaFirm.id}');" class="button134Cell1" id="changePermsId">			       		  	 
</c:if>
</c:if>			

		         
<% } else { %>
<c:if test="${addEditUserForm.fromTPAContactsTab !=true}">
		       <input type="button" name="actionLabel" 
		         value="delete" 
		         onclick="<%=jsFunction%>" class="button134Cell2">			      
</c:if>

<c:if test="${addEditUserForm.webAccess}">      	         
<c:if test="${userProfile.internalUser ==true}">
		       &nbsp;&nbsp;
		       <input type="submit" name="actionLabel" 
		         value="change Permissions" 
		         onclick="return doChangePermissions('${tpaFirm.id}');" class="button134Cell2" id="changePermsId">		
			 
</c:if>
</c:if>

<% } %>

		    </td>
		</tr>        
	   </table>
	  
    </td>
    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  </tr>
</c:if> <%-- TPA firm not hidden --%>
</c:if> <%-- TPA firm not removed --%>
	
</c:forEach>

<logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
<logicext:then>
	<jsp:include page="tpaUserContractInfoSection.jsp" flush="true" />
</logicext:then>
</logicext:if>

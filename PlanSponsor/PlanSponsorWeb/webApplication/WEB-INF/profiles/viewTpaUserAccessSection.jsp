<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


<table>

<c:forEach items="${addEditUserForm.tpaFirms}" var="tpaFirm" varStatus="tpaIndex" >

<c:if test="${tpaFirm.removed !=true}">
<c:if test="${tpaFirm.hidden !=true}">
<c:set var="indexValue" value="${tpaIndex.index}"/> 
<input type="hidden" name="tpaFirms[${tpaIndex.index}].id" /><%--  input - indexed="true" name="tpaFirm" --%>
<input type="hidden" name="tpaFirms[${tpaIndex.index}].lastRegisteredUser" /><%--  input - indexed="true" name="tpaFirm" --%>
<input type="hidden" name="tpaFirms[${tpaIndex.index}].lastUserWithManageUsers" /><%--  input - indexed="true" name="tpaFirm" --%>
<input type="hidden" name="tpaFirms[${tpaIndex.index}].lastUserWithReceiveILoansEmail" /><%--  input - indexed="true" name="tpaFirm" --%>
<input type="hidden" name="tpaFirms[${tpaIndex.index}].lastUserWithReceiveILoansEmailAndTPAStaffPlan" /><%--  input - indexed="true" name="tpaFirm" --%>
<input type="hidden" name="tpaFirms[${tpaIndex.index}].lastUserWithSigningAuthorityContracts" /><%--  input - indexed="true" name="tpaFirm" --%>
<input type="hidden" name="tpaFirms[${tpaIndex.index}].lastUserWithReviewIWithdrawalsContracts" /><%--  input - indexed="true" name="tpaFirm" --%>
	
   
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
 
 
 
	String uploadSubmissionsIniValName = "uploadSubmissionsIniVal" + indexVal;
	String viewAllSubmissionsIniValName = "viewAllSubmissionsIniVal" + indexVal;
	String cashAccountIniValName = "cashAccountIniVal" + indexVal;
	String directDebitIniValName = "directDebitIniVal" + indexVal;	
	%> 
<tr>
	<td>
		<TABLE class=box style="CURSOR: pointer"
		onclick="expandSection('${tpaFirm.id}')" cellSpacing=0 cellPadding=3 width=400 border=0>
			<TR class=tablehead>
				<TD class=tableheadTD1 colSpan=3>
				 <img id="${tpaFirm.id}img" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;
				 <B>${tpaFirm.name},&nbsp;${tpaFirm.id} permissions</B>
				</TD>
			</TR>
	</TABLE>
	<div class=switchcontent id=${tpaFirm.id}>
	<table class=box cellSpacing=0 cellPadding=3 width=400 border=0>

	  <tr>
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
<logicext:if name="tpaFirm" property="contractAccesses[0].showTpaStaffPlanAccess" op="equal" value="true">
	<logicext:then>
	  <tr bgcolor="#E6E6E6">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>TPA staff plan access </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].tpaStaffPlanAccess">
			    <render:yesno property="tpaFirm.contractAccesses[0].tpaStaffPlanAccess"   defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
    </logicext:then>
</logicext:if>
	  
<logicext:if name="tpaFirm" property="contractAccesses[0].showUploadSubmissions" op="equal" value="true">			
	<logicext:then>	  
	  <tr bgcolor="#E6E6E6">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>Create submissions </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].uploadSubmissions">
			    <render:yesno property="tpaFirm.contractAccesses[0].uploadSubmissions" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
    </logicext:then>
</logicext:if>

<logicext:if name="tpaFirm" property="contractAccesses[0].showReportDownload" op="equal" value="true">
	<logicext:then>	  
	  <tr bgcolor="#E6E6E6">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>Download reports - full SSN </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].reportDownload">
			    <render:yesno property="tpaFirm.contractAccesses[0].reportDownload" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
    </logicext:then>
</logicext:if>
	
<logicext:if name="tpaFirm" property="contractAccesses[0].showUpdateCensusData" op="equal" value="true">
	<logicext:then>	  
	  <tr bgcolor="#E6E6E6">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>Update census data </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].updateCensusData">
			    <render:yesno property="tpaFirm.contractAccesses[0].updateCensusData" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
    </logicext:then>
</logicext:if>

<logicext:if name="tpaFirm" property="contractAccesses[0].showViewSalary" op="equal" value="true">
	<logicext:then>			  
	  <tr bgcolor="#E6E6E6">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>View salary </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].viewSalary">
			    <render:yesno property="tpaFirm.contractAccesses[0].viewSalary" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  
    </logicext:then>
</logicext:if>

<logicext:if name="tpaFirm" property="contractAccesses[0].showReviewLoans" op="equal" value="true">
	<logicext:then>			  
	  <tr bgcolor="#E6E6E6">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>Initiate/review Loans </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].reviewLoans">
			    <render:yesno property="tpaFirm.contractAccesses[0].reviewLoans" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  	  	  
    </logicext:then>
</logicext:if>

<logicext:if name="tpaFirm" property="contractAccesses[0].showReviewIWithdrawals" op="equal" value="true">
	<logicext:then>			  
	  <tr bgcolor="#E6E6E6">
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	     <td><strong>Initiate/review i:withdrawals </strong></td>
	     <td>   
	        <ps:highlightIfChanged name="tpaFirm" property="contractAccesses[0].reviewIWithdrawals">
			    <render:yesno property="tpaFirm.contractAccesses[0].reviewIWithdrawals" defaultValue="no" style="c" />	  
			</ps:highlightIfChanged>
		 </td>
         <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>	  	  	  
    </logicext:then>
</logicext:if>
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

    </td></tr>
	<tr><td><br/></td></tr>
</c:if> <%-- TPA firm not hidden --%>
</c:if> <%-- TPA firm not removed --%>
	
</c:forEach>

</table>	

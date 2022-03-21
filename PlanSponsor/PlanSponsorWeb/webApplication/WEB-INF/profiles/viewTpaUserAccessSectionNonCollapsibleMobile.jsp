<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

 <%@ page import="com.manulife.pension.ps.web.profiles.TpaFirm" %>     
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<content:contentBean contentId="<%=ContentConstants.WARNING_REMOVE_ALL_SUBMISSION_PERMISSION%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningRemoveAllSubmissionPermission"/>
<% 
UserProfile userProfile =(UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<script type="text/javascript" >


</script>

<%
int rowIndex = 0;
%>
<c:forEach items="${addEditUserForm.tpaFirms}" var="tpaFirm" varStatus="tpaIndex" >
<% String index=pageContext.getAttribute("tpaIndex").toString();
pageContext.setAttribute("index",index);
%>

<c:if test="${tpaFirm.removed !=true}">
<c:if test="${tpaFirm.hidden !=true}">

<form:hidden path="tpaFirms[${tpaIndex.index}].id" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastRegisteredUser" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithManageUsers" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithReceiveILoansEmail" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithReceiveILoansEmailAndTPAStaffPlan" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithSigningAuthorityContracts" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithReviewIWithdrawalsContracts" /><%--  input - indexed="true" name="tpaFirm" --%>
<form:hidden path="tpaFirms[${tpaIndex.index}].lastUserWithReviewLoansContracts" /><%--  input - indexed="true" name="tpaFirm" --%>

    <% if (rowIndex % 2 == 0) { %>
	    <tr class="datacell2">
	<% } else { %>
	    <tr class="datacell1">
	<% }
	   rowIndex++;
	String uploadSubmissionsIniValName = "uploadSubmissionsIniVal" + index;
	String cashAccountIniValName = "cashAccountIniVal" + index;
	String directDebitIniValName = "directDebitIniVal" + index;
	
	%>
	
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<td colspan="6" align="center">
	
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		
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
  		
<c:if test="${addEditUserForm.webAccess}">  		
<c:if test="${userProfile.internalUser ==true}">
		<tr valign="top">
		    <td width="46%"><strong>Manage users</strong></td>
       		<td align="left" nowrap>
  	    		<render:yesno property="tpaFirm.contractAccesses[0].manageUsers" defaultValue="no" style="c" />            		 		
       		</td>
  			<td align="left" nowrap>
  			</td>
        	<td align="left" nowrap>&nbsp;</td>
        </tr>
</c:if>
	
		<logicext:if name="tpaFirm" property="contractAccesses[0].showTpaStaffPlanAccess" op="equal" value="true">
			<logicext:then>
		  		<tr valign="top">
		       		<td>
		       			<strong>TPA staff plan access</strong>
		       		</td>
		       		<td align="left" nowrap>
		   	    		<render:yesno property="tpaFirm.contractAccesses[0].tpaStaffPlanAccess" defaultValue="no" style="c" />
		            </td>
		  			<td align="left" nowrap></td>
		        	<td align="left" nowrap>&nbsp;</td>
		     	</tr>
	        </logicext:then>
		</logicext:if>

        <tr valign="top">
       		<td> <strong>Create submissions</strong></td>
            <td align="left" nowrap>
	    	<render:yesno property="tpaFirm.contractAccesses[0].uploadSubmissions" defaultValue="no" style="c" />
            </td>
            <td align="left" nowrap></td>
     	    <td align="left" nowrap>&nbsp;</td>
        </tr>
 		   
		<logicext:if name="tpaFirm" property="contractAccesses[0].showReportDownload" op="equal" value="true">
			<logicext:then>
		  		<tr valign="top">
		       		<td>
		       			<strong>Download reports - full SSN</strong>
		       		</td>
		       		<td align="left" nowrap>
		    			<render:yesno property="tpaFirm.contractAccesses[0].reportDownload" defaultValue="no" style="c" />
		  			<td align="left" nowrap>
		        	<td align="left" nowrap>&nbsp;</td>
		     	</tr>
	        </logicext:then>
		</logicext:if>

		<logicext:if name="tpaFirm" property="contractAccesses[0].showUpdateCensusData" op="equal" value="true">
			<logicext:then>	
	  		<tr valign="top">
	       		<td>
	       			<strong>Update census data</strong>
	       		</td>
	       		<td align="left" nowrap>
	       		<render:yesno property="tpaFirm.contractAccesses[0].updateCensusData" defaultValue="no" style="c" />
	    	    </td>
	  			<td align="left" nowrap></td>
	        	<td align="left" nowrap>&nbsp;</td>
	     	</tr>
	        </logicext:then>
		</logicext:if>
		
		<logicext:if name="tpaFirm" property="contractAccesses[0].showViewSalary" op="equal" value="true">
			<logicext:then>			
	  		<tr valign="top">
	       		<td>
	       			<strong>View salary</strong>
	       		</td>
	       		<td align="left" nowrap>
	       		 	<render:yesno property="tpaFirm.contractAccesses[0].viewSalary" defaultValue="no" style="c" />	  	  
	       		</td>
	  			<td align="left" nowrap></td>
	        	<td align="left" nowrap>&nbsp;</td>
	     	</tr>
	        </logicext:then>
		</logicext:if>

		<logicext:if name="tpaFirm" property="contractAccesses[0].showReviewLoans" op="equal" value="true">
			<logicext:then>	
	  		<tr valign="top">
	       		<td>
	       			<strong>Initiate/review Loans</strong>
	       		</td>
	       		<td align="left" nowrap>
	       		<render:yesno property="tpaFirm.contractAccesses[0].reviewLoans" defaultValue="no" style="c" />	  
	       		</td>
	  			<td align="left" nowrap>
	  			</td>
	        	<td align="left" nowrap>&nbsp;</td>
	     	</tr>
	        </logicext:then>
		</logicext:if>

		<logicext:if name="tpaFirm" property="contractAccesses[0].showReviewIWithdrawals" op="equal" value="true">
			<logicext:then>	
	  		<tr valign="top">
	       		<td>
	       			<strong>Initiate/review i:withdrawals</strong>
	       		</td>
	       		<td align="left" nowrap>
	       		<render:yesno property="tpaFirm.contractAccesses[0].reviewIWithdrawals" defaultValue="no" style="c" />	
	       		</td>
	  			<td align="left" nowrap>
	  			</td>
	        	<td align="left" nowrap>&nbsp;</td>
	     	</tr>
	        </logicext:then>
		</logicext:if>
		        
		      </c:if>  
	   </table>
	  
    </td>
    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  </tr>
</c:if> <%-- TPA firm not hidden --%>
</c:if> <%-- TPA firm not removed --%>
	
</c:forEach>

		<jsp:include page="tpaUserContractInfoSectionMobile.jsp" flush="true" />

<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.census.CensusConstants"%>
<%@ page import="com.manulife.pension.ps.web.census.EditEmployeeSnapshotForm.PageErrorType"%>
<c:if test="${not empty validationErrors and empty ssnWarnings}">
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_ERROR_HEADER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorHeader" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_ERROR_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorIntro" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_ERROR_INTRO2%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorIntro2" />

<content:contentBean contentId = "<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_ERROR_DUPLICATE_SUBMITTED_CSDB_EMAIL_ACTION_REQUIRED_TEXT%>"  
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="duplicateCSDBEmailAddressText" />
	
<script type="text/javascript">

   function showSimilarRecord(profileId) {
       printURL = "/do/census/viewEmployeeSnapshot/?profileId=" + profileId + "&printFriendly=true";      
       window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
   }
   
   function continueFlow(frm, accept) {
        return doSubmit('save');
   }

</script>
<%
String duplicateemail=PageErrorType.duplicateEmail;
%>
<TABLE width=500 height="50" border=0 cellPadding=0 cellSpacing=0>
     <TBODY>
        <TR>
           <TD width=1>
	          <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	       </TD>
           <TD>
		       <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
		       <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
		       <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
		       <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
		       <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
		       <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	       </TD>
           <TD width=1>
	            <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	       </TD>
          </TR>
          <TR class=tablehead>
            <TD class=tableheadTD1 colSpan=3>
			    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	              <TBODY>
		                <TR>
		                    <TD class=tableheadTD>
		                    <STRONG>
			                    <content:getAttribute id='ErrorHeader' attribute='text'>
			                      <content:param><c:out value="${validationErrors.numOfErrors}"/></content:param>
			                      <content:param><c:out value="${validationErrors.numOfWarnings}"/></content:param>
			                    </content:getAttribute>
		                    </STRONG></TD>
		                </TR>
	              </TBODY>
	            </TABLE>
           </TD>
         </TR>
         <TR class=datacell1>
            <TD class="databorder">
	          <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	        </TD>
         <TD class=datacell1 vAlign=top align=left>
         <c:choose>
	          <c:when test="${param.printFriendly == true}">
 			   <content:getAttribute id='ErrorIntro' attribute='text'/>
	           <ol>
	             <c:forEach var="error" items="${validationErrors.errors}">
	               <li>
	                 <ps:employeeSnapshotErrorMsg name="error"/>
			
	               </li>
	             </c:forEach>
	           </ol>
		  </c:when>
	          <c:otherwise>
              <DIV class=scroll >			    
 			   <content:getAttribute id='ErrorIntro' attribute='text'/>	
			  <c:if test="${param.readOnly and (not param.printFriendly)}">
			      <br><content:getAttribute id='ErrorIntro2' attribute='text'/>	
				  <br>
		           <table border="0" cellpadding="0" cellspacing="0" width="422">
		             <tbody>
		               <tr valign="top">
		                 <td width="215"><div align="center">
		                     <input type="button" name="button2" value="edit" class="button100Lg" onclick="return doEnableAndSubmit('continueEdit')"/>
		                 </div></td>
		                 <td width="207"><div align="center">
				              <input type="button" name="button3" value="accept" class="button100Lg" onclick="return doEnableAndSubmit('saveIgnoreWarning')"/>          
		
		                     <br>
		                   </div>
		                     <div align="right"></div></td>
		               </tr>
		             </tbody>
		           </table>
		          </c:if>

               <ol>
             	 <c:forEach var="error" items="${validationErrors.errors}">
	               <li>
	                 <ps:employeeSnapshotErrorMsg name="error"/>
	                 <c:if test="${error.errorCode eq 1416}">
<c:set var="employeeData" value="${sessionScope.employeeData}"/>
	                 <br>
	                 <br>
	                 <content:getAttribute beanName="duplicateCSDBEmailAddressText" attribute="text">
			           <content:param>javascript:showSimilarRecord(<c:out value='${employeeData.profileId}'/>)</content:param>
					 </content:getAttribute>
<input type="hidden" name="specialPageType" value="${duplicateemail}"/>
	                 </c:if>
	               </li>
	             </c:forEach>
	           </ol>
 	          </DIV>
	          </c:otherwise>
          </c:choose>
	</TD>	     
         <TD class=databorder>
	      <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1> 
	     </TD>
        </TR>
        <TR>
          <TD class="databorder" colspan="3" height="1"><IMG src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></TD>
         </TR>
    </TBODY>
</TABLE>

<br><br>
</c:if>

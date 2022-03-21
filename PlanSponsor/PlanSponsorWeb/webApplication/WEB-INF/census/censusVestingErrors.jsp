<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<c:if test="${not empty vestingErrors}">
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_ERROR_HEADER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorHeader" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_ERROR_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorIntro" />

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
			                      <content:param><c:out value="${vestingErrors.numOfErrors}"/></content:param>
			                      <content:param><c:out value="${vestingErrors.numOfWarnings}"/></content:param>
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
	             <c:forEach var="error" items="${vestingErrors.errors}">
	               <li>
	                 <ps:employeeSnapshotErrorMsg name="error"/>
	               </li>
	             </c:forEach>
	           </ol>
	          </c:when>
	          <c:otherwise>
			    <DIV class=scroll >
	 			   <content:getAttribute id='ErrorIntro' attribute='text'/>	
		           <ol>
		             <c:forEach var="error" items="${vestingErrors.errors}">
		               <li>
		                 <ps:employeeSnapshotErrorMsg name="error"/>
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

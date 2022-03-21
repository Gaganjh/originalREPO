<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty validationErrors}">
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
		                    <TD class=tableheadTD><STRONG>Errors (<c:out value="${validationErrors.numOfErrors}"/>) / Warnings (<c:out value="${validationErrors.numOfWarnings}"/>)</STRONG></TD>
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
		    <div style="height: 70px; overflow-y: auto">
		       All errors have 
		        <IMG src="/assets/unmanaged/images/error.gif"> next to them.  
	              All warnings have 
	           <IMG alt=Warning src="/assets/unmanaged/images/warning2.gif"> next to them.
	           <ol>
	             <c:forEach var="error" items="${validationErrors.errors}">
	               <li>
	                 <ps:employeeSnapshotErrorMsg name="error"/>
	               </li>
	             </c:forEach>
	           </ol>
	       </DIV>
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

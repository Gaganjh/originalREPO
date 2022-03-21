<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<c:if test="${employeeForm.showConfirmationToDo}">
<content:contentBean
	contentId="<%=ContentConstants.EMPLOYEE_SNAPSHOT_CONFIRMATION_TO_DO_BOX%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="confirmationToDoBox" />

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
		                    <content:getAttribute id='confirmationToDoBox' attribute='title'/>
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
        		<content:getAttribute id='confirmationToDoBox' attribute='text'/>
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

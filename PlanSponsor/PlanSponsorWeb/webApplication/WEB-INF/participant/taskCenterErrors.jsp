<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@page import="com.manulife.pension.ps.web.participant.TaskCenterTasksReportForm" %>
<%@page import="org.owasp.encoder.Encode"%>

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_ERROR_HEADER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorHeader" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_ERROR_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ErrorIntro" />



<jsp:useBean id="taskCenterTasksReportForm" scope="session" type="com.manulife.pension.ps.web.participant.TaskCenterTasksReportForm" />


<c:if test="${not empty validationErrors}">
</br>
<table>
<tr><td>

<TABLE width=370 height="50" border=0 cellPadding=0 cellSpacing=0>
          <TR class=tablehead>
            <TD colspan="3" class=tableheadTD1 >
			    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		                <TR>
		                    <TD class=tableheadTD>
		                    <STRONG>
			                    <content:getAttribute id='ErrorHeader' attribute='text'>
			                      <content:param><c:out value="${validationErrors.numOfErrors}"/></content:param>
			                      <content:param><c:out value="${validationErrors.numOfWarnings}"/></content:param>
			                    </content:getAttribute>
		                    </STRONG></TD>
		                </TR>
	            </TABLE>
           </TD>
         </TR>
         
         <tr> 
            <TD class="databorder">
	          <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	        </TD>
            <TD class=datacell1 vAlign=top align=left>
			   <DIV class=scroll >
 			   <content:getAttribute id='ErrorIntro' attribute='text'/>	
	           <ol>
	             <c:forEach var="error" items="${validationErrors.errors}">
	               <li>
	                 <ps:employeeSnapshotErrorMsg name="error"/>
	               </li>
	             </c:forEach>
	           </ol>	           
			    </DIV>
            </td>
          
            <TD class=databorder>
	          <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1> 
	        </TD>
         </TR>
        <TR>
          <TD class="databorder" colspan="3" height="1"><IMG src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></TD>
         </TR>
         
         
         <TR>
           <TD width=1>
	            <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	       </TD>
         </TR>   
</table>

</td>
<td><img src="/assets/unmanaged/images/s.gif" width="1" height="20"></td>
<td valign="top">

<c:if test="${taskCenterTasksReportForm.showRemarks}">

<TABLE width=350 height="50" border=0 cellPadding=0 cellSpacing=0>
          <tr><td colspan="3">
                <table><td><img src="/assets/unmanaged/images/error.gif" /></td>
                   <td>Please enter a reason for declining the deferral change request(s). Please note, this reason will apply to all selected requests.</td>
                </table>                   
              </td>
          </tr>
          <TR class=tablehead>
            <TD colspan="3" class=tableheadTD1 >
			    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		                <TR>
		                    <TD class=tableheadTD>
		                    <STRONG>Remarks
		                    </STRONG>
		                    </TD>
		                </TR>
	            </TABLE>
           </TD>
         </TR>
         <tr> 
            <TD class="databorder">
	          <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	        </TD>
            <TD class=datacell1 >
 		       <textarea rows="6" cols="45" name="remarks"><%=Encode.forHtmlContent(taskCenterTasksReportForm.getRemarks())%></textarea>
            </td>
          
            <TD class=databorder>
	          <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1> 
	        </TD>
         </TR>
        <TR>
          <TD class="databorder" colspan="3" height="1"><IMG src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></TD>
         </TR>
         
         
         <TR>
           <TD width=1>
	            <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	       </TD>
         </TR>   
         
</table>

</c:if>

</td></tr></table>
</c:if>


<c:if test="${taskCenterTasksReportForm.showRemarks and empty validationErrors}">
</br>

<table>
<tr><td>

<TABLE width=370 height="50" border=0 cellPadding=0 cellSpacing=0>
   <tr><td></td></tr>
</table>

</td>
<td><img src="/assets/unmanaged/images/s.gif" width="1" height="20"></td>
<td valign="top">



<TABLE width=350 height="50" border=0 cellPadding=0 cellSpacing=0>
          <tr><td colspan="3">
                <table><td><img src="/assets/unmanaged/images/error.gif" /></td>
                   <td>Please enter a reason for declining the deferral change request(s). Please note, this reason will apply to all selected requests.</td>
                </table>                   
              </td>
          </tr>
          <TR class=tablehead>
            <TD colspan="3" class=tableheadTD1 >
			    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		                <TR>
		                    <TD class=tableheadTD>
		                    <STRONG>Remarks
		                    </STRONG>
		                    </TD>
		                </TR>
	            </TABLE>
           </TD>
         </TR>
         <tr> 
            <TD class="databorder">
	          <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	        </TD>
            <TD class=datacell1 >
 		       <textarea rows="6" cols="45" name="remarks"><%=Encode.forHtmlContent(taskCenterTasksReportForm.getRemarks())%></textarea>
            </td>
          
            <TD class=databorder>
	          <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1> 
	        </TD>
         </TR>
        <TR>
          <TD class="databorder" colspan="3" height="1"><IMG src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></TD>
         </TR>
         
         
         <TR>
           <TD width=1>
	            <IMG height=1 src="/assets/unmanaged/images/spacer.gif" width=1>
	       </TD>
         </TR>   
         
</table>

</td></tr></table>

</c:if>

<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<style type="text/css">
DIV.scrollSimilarSsn {
	OVERFLOW: auto;
	WIDTH: auto;
	BORDER-TOP-STYLE: none;
	BORDER-RIGHT-STYLE: none;
	BORDER-LEFT-STYLE: none;
	HEIGHT: 175px;
	BACKGROUND-COLOR: #fff;
	BORDER-BOTTOM-STYLE: none;
	padding: 8px;
	visibility: visible;
}
</style>

<script type="text/javascript">
<!--
   function showSimilarRecord(profileId) {
       printURL = "/do/census/viewEmployeeSnapshot/?profileId=" + profileId + "&printFriendly=true";      
       window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
   }
   
   function continueFlow(frm, accept) {
       frm.elements['acceptSimilarSsn'].value = accept;
       var submitInProgress = isSubmitInProgress();	   
	   if (submitInProgress) {
		  return false;
	   }
       if (accept) {
           return doSubmit('save');
       } else {
		   return doSubmit('continueEdit');
	   }
   }
//-->
</script>

<c:if test="${not empty ssnWarnings}">
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_WARNING_HEADER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="WarningHeader" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_SSN_WARNING_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="WarningIntro" />

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
		                    <content:getAttribute attribute="text" beanName="WarningHeader">
		                     <content:param><c:out value="${fn:length(ssnWarnings.employees)}"/></content:param>
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
		    <DIV class="scrollSimilarSsn">
		      <content:getAttribute beanName="WarningIntro" attribute="text"/>
	           <ol>
	               <li>
	                 <ps:employeeSnapshotErrorMsg name="ssnWarningMsg" />
	                 <br>
	                 <br>

			           <strong>Similar Records: </strong> <br>
			             <c:forEach var="employee" items="${ssnWarnings.employees}">
<c:set var="ssn" value="${employee.ssn}" /> 
	                   		 <c:out value="${employee.firstName}"/> <c:out value="${employee.lastName}"/> 
	                   		 ${ssn.substring(0,3)} ${ssn.substring(3,5)} ${ssn.substring(5)}
			                 <a href="javascript:showSimilarRecord(<c:out value='${employee.profileId}'/>)">
			                 View similar record 
			                 </a><br>
			             </c:forEach>
	               </li>
	           </ol>
	           <br>
	           <br>
                  <table border="0" cellpadding="0" cellspacing="0" width="422">
                    <tbody>
                      <tr valign="top">
                        <td width="215"><div align="left">
                            <input name="task" type="button" class="button134" onClick="return continueFlow(this.form, false)" value="edit">
                        </div></td>
                        <td width="207"><div align="right">
                            <input name="task" type="button" class="button134" onClick="return continueFlow(this.form, true)" value="accept">
                            <br>
                          </div>
                            <div align="right"></div></td>
                      </tr>
                    </tbody>
                  </table>
	           
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

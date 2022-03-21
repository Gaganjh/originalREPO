<%@ page
  import="com.manulife.pension.ps.web.tools.CensusErrorCorrectionForm"%>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<%
  CensusErrorCorrectionForm form = (CensusErrorCorrectionForm)request.getSession(false).getAttribute("errorCorrectionForm");
  if (form.getErrors().isEmpty()) {
%> 
  <c:set var="cleanRecord" value="true"/>
<% } else { %>
  <c:set var="cleanRecord" value="false"/>
<% } %>

<c:set var="warningPage" value="${errorCorrectionForm.warningPage}" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_GENERAL_HEADER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="GeneralHeader" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_GENERAL_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="GeneralIntro" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_WARNING_ONLY_HEADER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="WarningOnlyHeader" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_WARNING_ONLY_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="WarningOnlyIntro" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_WARNING_PAGE_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="WarningPageMessage" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_CLEAN_RECORD_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="CleanRecordMessage" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_CENSUS_ERROR_CORRECTION_SUBMISSION_ALREADY_PROCESSED%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="SubmissionAlreadyProcessedMessage" />

<style type="text/css">
DIV.scrollDiv {
	OVERFLOW: auto;
	WIDTH: auto;
	BORDER-TOP-STYLE: none;
	BORDER-RIGHT-STYLE: none;
	BORDER-LEFT-STYLE: none;
	HEIGHT: 100px;
	BACKGROUND-COLOR: #fff;
	BORDER-BOTTOM-STYLE: none;
	padding: 3px;
	visibility: visible;
}
</style>

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
		                      <c:choose>
			                      <c:when test="${cleanRecord}">
									<content:getAttribute id='GeneralHeader' attribute='text'>
				                      <content:param>0</content:param>
				                      <content:param>0</content:param>
				                    </content:getAttribute>
				                  </c:when>
				                  <c:otherwise>
						              <c:choose>
						                  <c:when test="${warningPage}">
						                    <content:getAttribute id='WarningOnlyHeader' attribute='text'>
						                      <content:param><c:out value="${validationErrors.numOfWarnings}"/></content:param>
						                    </content:getAttribute>
						                  </c:when>
						                  <c:otherwise>
						                    <content:getAttribute id='GeneralHeader' attribute='text'>
						                      <content:param><c:out value="${validationErrors.numOfErrors}"/></content:param>
						                      <content:param><c:out value="${validationErrors.numOfWarnings}"/></content:param>
						                    </content:getAttribute>
						                  </c:otherwise>
						               </c:choose>
				                  </c:otherwise>
			                  </c:choose>
		                    </STRONG></TD>
		                </TR>
	              </TBODY>
	            </TABLE>
           </TD>
         </TR>
         <TR class=datacell1>
            <TD class="databorder">
	          <IMG height="1" src="/assets/unmanaged/images/spacer.gif" width="1">
	        </TD>
        <TD class=datacell1 vAlign=top align=left>
		    <DIV class="scrollDiv" style="height:100">
              <c:choose>
                  <c:when test="${cleanRecord}">
		              <c:choose>
		                  <c:when test="${errorCorrectionForm.submissionProcessed}">
							<content:getAttribute id='SubmissionAlreadyProcessedMessage' attribute='text'/>
						  </c:when>
						  <c:otherwise>
							<content:getAttribute id='CleanRecordMessage' attribute='text'/>
						  </c:otherwise>
					  </c:choose>   
                  </c:when>
                  <c:otherwise>
	                <c:choose>
	                  <c:when test="${warningPage}">
 	 		  	        <content:getAttribute id='WarningOnlyIntro' attribute='text'/>
 	 		  	        <br/>	
	 	 		  	    <content:getAttribute id='WarningPageMessage' attribute='text'/>	
			          </c:when>
			          <c:otherwise>
 	 		  	        <content:getAttribute id='GeneralIntro' attribute='text'/>	
			          </c:otherwise>
			        </c:choose>	           
 		            <ol>
		            <c:forEach var="error" items="${validationErrors.errors}">
		               <li>
		                 <ps:employeeSnapshotErrorMsg name="error"/>
		               </li>
		            </c:forEach>
		            </ol>
                  </c:otherwise>
              </c:choose>
	        </DIV>
	     </TD>
         <TD class=databorder>
	      <IMG height="1" src="/assets/unmanaged/images/spacer.gif" width="1"> 
	     </TD>
        </TR>
        <TR>
          <TD class="databorder" colspan="3" height="1"><IMG src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></TD>
         </TR>
    </TBODY>
</TABLE>

<br><br>

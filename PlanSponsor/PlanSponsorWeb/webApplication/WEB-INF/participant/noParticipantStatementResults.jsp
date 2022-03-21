<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantStatementSearchForm" %>
<%@ page import="com.manulife.pension.service.statement.valueobject.ParticipantStatementDocumentVO" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<un:useConstants var="psWebConstants" className="com.manulife.pension.ps.web.Constants"/>
<un:useConstants var="commonConstants" className="com.manulife.pension.platform.web.CommonConstants"/>

<c:set var="statementsList" value="${statementsList}" /> 


			 

<jsp:useBean id="ParticipantStatementSearchForm" scope="session" type="com.manulife.pension.ps.web.participant.ParticipantStatementSearchForm" /> 

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_PARTICIPANT_STATEMENTS_AVAILABLE%>"
                         	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="participantStatementsNoResultMessage"/>
<%
String tableWidth = ParticipantStatementSearchForm.getTableWidth();
String columnSpan = "8";

%>

<%
String ua = request.getHeader( "User-Agent" );
boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
request.setAttribute("isIE",isMSIE);
%>
  <!-- Remove the extra column before the report -->
  <c:if test="${empty param.printFriendly }" >
    <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    <td>
  </c:if>

  <c:if test="${not empty param.printFriendly }" >
    <td>
  </c:if>

      <p>
      <c:if test="${not empty sessionScope.psErrors}">
      <c:if test="${empty param.printFriendly }" ><!-- QC Defect fix #6887-->
<c:set var="errorsExist" value="${true}" scope="page" />
        <div id="errordivcs"><content:errors scope="session"/></div>
      </c:if>
      <c:if test="${not empty param.printFriendly }" ><!-- QC Defect fix #6887 starts-->
          <%pageContext.removeAttribute("psErrors") ;%>
      </c:if><!-- QC Defect fix #6887 ends-->
      </c:if>
      </p>

    <!-- Beginning of Participant Summary report body -->
      <ps:form method="GET" action="/participant/participantStatementResults" >
<input type="hidden" name="task" value="filter"/>
      <table width="<%=tableWidth%>" style="width:525px;Overflow-x:auto;" border="0" cellspacing="0" cellpadding="0">   
      	<!-- Set the column spacing for the report -->
        <tr>
        </br>
        </br>
        <td class = "redText">
        	<content:getAttribute id="participantStatementsNoResultMessage" attribute="text"/>
		</td>
        </tr>

	</table>

		<c:if test="${isIE eq true}">
		<table>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
		</table>
		</c:if>
		</br>
		<p>
           <input class="button100Lg" type=button value="back" onClick="history.back()">
         </p>
         </br>
         </br>
         </br>
         </br>
         </br>
        <tr>
          <td colspan="<%=columnSpan%>">
			<table>
			<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td>         
            <br>
            <p><content:pageFooter beanName="layoutPageBean"/></p>
            <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
            <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
			</td>
			</tr>
			</table>
          </td>
        </tr>	
      </ps:form>
 

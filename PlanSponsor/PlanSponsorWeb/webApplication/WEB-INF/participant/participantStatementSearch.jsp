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
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementsReportData" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<un:useConstants var="psWebConstants" className="com.manulife.pension.ps.web.Constants"/>
<un:useConstants var="commonConstants" className="com.manulife.pension.platform.web.CommonConstants"/>
<%
ParticipantStatementsReportData theReport = (ParticipantStatementsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>
<%
ParticipantStatementSearchForm ParticipantStatementSearchForm=(ParticipantStatementSearchForm)session.getAttribute("ParticipantStatementSearchForm");
pageContext.setAttribute("ParticipantStatementSearchForm",ParticipantStatementSearchForm,PageContext.PAGE_SCOPE);
%>
<%
boolean isReportCurrent = true;

String tableWidth = ParticipantStatementSearchForm.getTableWidth();
String columnSpan = ParticipantStatementSearchForm.getColumnSpan();

%>
 





<content:contentBean contentId="<%=ContentConstants.MESSAGE_PARTICIPANTS_STMT_NO_SEARCH_RESULTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="noSearchResults"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_SEARCH_FOR_PARTICIPANTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="searchForParticipants"/>								   




<%
String ua = request.getHeader( "User-Agent" );
boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
request.setAttribute("isIE",isMSIE);
%>

<c:if test="${empty param.printFriendly }" >
<script type="text/javaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript" >
if (window.addEventListener) {
	window.addEventListener('load', init, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', init);
} else if (document.getElementById)
	window.onload=init;
	
function init() {
  
}

//Called when reset is clicked
function doReset() {
	document.ParticipantStatementSearchForm.task.value = "default";
	return true;
}
</script>
</c:if>


  <!-- Remove the extra column before the report -->
  <c:if test="${empty param.printFriendly }" >
    <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    <td>
  </c:if>

  <c:if test="${not empty param.printFriendly }" >
    <td>
  </c:if>

      <p>
  		<c:if test="${sessionScope.psErrors ne null}">
<c:set var="errorsExist" value="true" scope="page" />
			<div id="errordivcs"><content:errors scope="session"/></div>
      <c:if test="${printFriendly ne null}" >
          <%pageContext.removeAttribute("psErrors") ;%>
      </c:if>
      </c:if>
      </p>

    <!-- Beginning of Participant Statement report body -->
      <ps:form method="GET" modelAttribute="ParticipantStatementSearchForm" name="ParticipantStatementSearchForm" action="/do/participant/participantStatementSearch" >
 <input type="hidden" name="task" value="filter"/> 


      <table width="730" height="160px" cellspacing="0" cellpadding="0" bgcolor="Beige">

        <!-- Start of body title -->
        <tr class="tablehead" >
          <td class="tablesubhead" colspan="8"><b>Participant Statement Search</b></td>
        </tr>
        <!-- End of body title -->
        <tr class=filterSearch>
          <td colspan="4">	
            <!--<content:getAttribute id="searchForParticipants" attribute="text"></content:getAttribute> -->
            To narrow your search, please enter information into one or more of the fields below and click "search" to complete your request.
            <!--   <c:if test="${not empty layoutPageBean.introduction2}">
              <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
              <br>
            </c:if>   --> 	
          </td>
        </tr>
        <tr class=filterSearch>
          <td width="220"><b>Last name </b><br>
            <c:if test="${not empty param.printFriendly }" >
<form:input path="namePhrase" onchange="setFilterFromInput(this);" readonly="true" cssClass="inputField" />

            </c:if>
            <c:if test="${empty param.printFriendly }" >
<form:input path="namePhrase" maxlength="30" onchange="setFilterFromInput(this);" cssClass="inputField" tabindex="20" />

            </c:if>	            	  
          </td>
          <td width="220"><b>First name </b><br>
            <c:if test="${not empty param.printFriendly }" >
<form:input path="firstName" onchange="setFilterFromInput(this);" readonly="true" cssClass="inputField" />

            </c:if>
            <c:if test="${empty param.printFriendly }" >
<form:input path="firstName" maxlength="30" onchange="setFilterFromInput(this);" cssClass="inputField" tabindex="20" />

            </c:if>
	      </td>
          <td width="220"><b>SSN</b><br>  
						<c:if test="${not empty param.printFriendly}" >
						<form:password path="ssnOne" readonly="true" styleClass="inputField"  onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3" value="${ParticipantStatementSearchForm.ssnOne}"/> 
						<form:password path="ssnTwo"  readonly="true"  styleClass="inputField"  onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2" value="${ParticipantStatementSearchForm.ssnTwo}"/>
						<form:input path="ssnThree" autocomplete="off" readonly="true"  styleClass="inputField"  onkeyup="return autoTab(this, 4, event);"  size="4" maxlength="4" />
						</c:if>
						<c:if test="${empty param.printFriendly}" >
						<form:password path="ssnOne" styleClass="inputField"  onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3" value="${ParticipantStatementSearchForm.ssnOne}"/> 
						<form:password path="ssnTwo"   styleClass="inputField"  onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2" value="${ParticipantStatementSearchForm.ssnTwo}"/>
						<form:input path="ssnThree" autocomplete="off"   styleClass="inputField"  onkeyup="return autoTab(this, 4, event);"  size="4" maxlength="4" />						</c:if>	            	  

          </td>        
          <td width="70" valign="bottom" class="filterSearch">
            &nbsp;
          </td>
        </tr>
        <tr class=filterSearch>
		  <td>
       	  </td>	 
		  <td>
       	  </td>	 
	      <td valign="middle"><div align="left">
            <c:if test="${empty param.printFriendly}" >
              <input type="submit" name="	" value="search" tabindex="70"/>
              <input type="submit" name="reset" value="reset" tabindex="80" onclick="return doReset();"/>
            </c:if>
            &nbsp;</div>
          </td>
          <td>
          </td>	
        </tr>
        <tr>
          <td colspan="4" class="filterSearch"><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td>
        </tr>	
      </table>
      <br>
      
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>
          <c:if test="${printFriendly eq null}" >
            <span class="b11 style1"><img src="/assets/unmanaged/images/view_icon.gif" width="12" height="12"> View Participant Statement </span>
          </c:if>	            	  
          </td>
        </tr>
      </table>
      <table width="<%=tableWidth%>" style="width:730px;Overflow-x:auto;" border="0" cellspacing="0" cellpadding="0">   
      	<!-- Set the column spacing for the report -->
        <tr>
          <td width="1"></td>
          <td width="20"></td>  	<!--View icon-->
          <td width="1"></td>
          <td width="220"></td>  	<!--Last Name-->
          <td width="1"></td>
          <td width="220"></td>		<!--First-->
          <td width="1"></td>
          <td width="222"></td> 	<!--ssn-->
          <td width="1"></td>
        </tr>

        <tr class="tablehead">
          <td class="tablehead" colspan="<%=columnSpan%>">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="tableheadTD" width="50%">
                	&nbsp;
                </td>
                <td class="tableheadTDinfo"><b><report:recordCounter report="theReport" label="Participants"/></b></td>
                <td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="ParticipantStatementSearchForm"/></td>
              </tr>
            </table>
          </td>
        </tr>
	</table>

    <div style="width:730px;Overflow-x:auto;Overflow-y:hidden">
		<table border="0" cellspacing="0" width="100%" cellpadding="0">         
        <!-- table details starts -->
        <tr class="tablesubhead">
          
        <!-- View icon -->
          <td rowspan="1" class="databorder" width="1"></td>
          <td rowspan="1" width="20"></td>
          
        <!-- Last Name -->
          <td rowspan="1" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<c:if test="${empty param.printFriendly }" >
          <td style='width: 300px' rowspan="1" valign="bottom"><report:sort field="PARTICIPANT_LAST_NAME" direction="asc"><b>Last Name</b></report:sort>
		  </td>
		</c:if>
		<c:if test="${not empty param.printFriendly }" >
          <td rowspan="1"><b>Last Name</b></td>
		</c:if>
		
        <!-- First Name -->
          <td rowspan="1" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td style='width: 300px' rowspan="1" valign="bottom"><report:sort field="PARTICIPANT_FIRST_NAME" direction="asc"><b>First Name</b></report:sort></td>

        <!-- SSN -->
          <td rowspan="1" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td style='width: 300px' rowspan="1" valign="bottom"><report:sort field="SOCIAL_SECURITY_NO" direction="asc"><b>SSN</b></report:sort></td>
          
        <!-- spacer -->
          <td rowspan="1" class="databox"></td>
          <td rowspan="1" class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<%-- CL 110234 Begin--%>
		<tr class="tablesubhead">
			  <td class="dataheaddivider" colspan="1" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		</tr>
		
		
<!-- Start of Details -->
<% boolean beigeBackgroundCls = false; // used to determine if the cell should use the background style
   boolean lastLineBkgrdClass = false; // used to determine if the last line should be beige or not
   boolean highlight = false;	// used to determine if the style class has to change
   boolean highlightBirthDate = false;	// used to determine if the style class has to change
   boolean highlightGateway = false;  // used to determine if the style class has to change
%>

<c:if test="${empty param.printFriendly }" >

        

		<% if (theReport.getDetails() != null && theReport.getDetails().size() > 0) { // we have search results %>
		
		<% } else { // no results %>
			<!-- no results -->
			<tr class="datacell1">
	          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          <td width="20">&nbsp;</td>
	          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	          <td valign="top" colspan="6">
			<c:if test="${empty pageScope.errorsExist}">
			<content:getAttribute id="noSearchResults" attribute="text">
			<!-- no errors and no results -->
				<content:param>/do/participant/participantStatementSearch</content:param>
			</content:getAttribute>
			</c:if>
			</td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<% 
		   }
		   lastLineBkgrdClass = true;
		 %>
          

</c:if>


<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:set var="Index" value="${theIndex.index}"/>

<%String temp = pageContext.getAttribute("Index").toString();
if (Integer.parseInt(temp) % 2 != 0) {
	 	beigeBackgroundCls = true;
	 	lastLineBkgrdClass = true; %>
        <tr class="datacell1">
<% } else {
		beigeBackgroundCls = false;
		lastLineBkgrdClass = false; %>
    	<tr class="datacell2">
<% } %>


        <!-- history icon --> 
          <td class="databorder" width="1"></td>
		<c:if test="${empty param.printFriendly }" >
          <td width="20px">
            <ps:link action="/do/participant/participantStatementResults?task=fetchStatements"  paramId="profileId" paramName="theItem" paramProperty="profileId">
              <img src="/assets/unmanaged/images/view_icon.gif" alt="Participant Statements Summary" width="12" height="12" border="0">
            </ps:link>
          </td>
        </c:if>
        <c:if test="${not empty param.printFriendly }" >
          <td>&nbsp;</td>
        </c:if>
        
        <!-- Last Name -->
          <td class="datadivider" width="1"></td>
          <td style='width: 300px'>
${theItem.lastName}
          </td>

        <!-- First Name -->
          <td class="datadivider" width="1"></td>
<td nowrap="nowrap" style='width: 250px'>${theItem.firstName}</td>
        
        <!-- SSN -->	
          <td class="datadivider" width="1"></td>
          <td style='width: 300px'><render:ssn property="theItem.ssn"  /></td>


        <!-- spacer -->
        <% if (beigeBackgroundCls) { %>
          <td class="dataheaddivider">
        <% } else { %>
          <td class="beigeborder">
        <% } %>
            <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
          </td>
          <td class="databorder" width="1"></td>
        </tr>
</c:forEach>
</c:if>

<!-- End of Details -->

<!-- Start of last line -->
<!-- let the last line have the same background colour as the previous line -->
<% if (lastLineBkgrdClass) { %>
	<tr class="datacell1">
<% } else { %>
	<tr class="datacell2">
<% } %>
        <!-- View icon -->
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>    
          <td class="lastrow"></td>
          
        <!-- Last Name -->
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          
        <!-- First Name -->
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          
        <!-- SSN -->
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          
        

        <!-- spacer -->
          <td class="datadivider"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>

        </tr>

		<!-- End of Last line -->
        <tr><td class="databorder" colspan="<%=columnSpan%>"></td></tr>   
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
	</div>         
        <tr><td colspan="<%=columnSpan%>" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="ParticipantStatementSearchForm"/></td></tr>
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
 

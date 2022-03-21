<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="manulife/tags/ps" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantForfeituresReportForm" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantForfeituresReportData" %>

<%
ParticipantForfeituresReportData theReport = (ParticipantForfeituresReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
Boolean moneyTypeFlag = (Boolean)userProfile.getCurrentContract().gethasEmployerMoneyTypeOnly();
pageContext.setAttribute("moneyTypeFlag",moneyTypeFlag,PageContext.PAGE_SCOPE);
%>




<jsp:useBean id="participantForfeituresReportForm" scope="session" type="com.manulife.pension.ps.web.participant.ParticipantForfeituresReportForm" />


<content:contentBean contentId="<%=ContentConstants.MESSAGE_PARTICIPANTS_VIEW_ALL%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="viewAllParticipants"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_PARTICIPANTS_NO_SEARCH_RESULTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="noSearchResults"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_SEARCH_FOR_PARTICIPANTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="searchForParticipants"/>								   

<content:contentBean contentId="<%=ContentConstants.NOTIFICATION_MESSAGE_PARTICIPANT_FORFEITURES%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="accountsPageNotification"/>

<%
	String ua = request.getHeader( "User-Agent" );
	boolean isMSIE = (ua != null && (ua.indexOf("MSIE") != -1 || ua.indexOf("Trident") != -1));
	request.setAttribute("isIE",isMSIE);
%>

<c:if test="${empty param.printFriendly }" >
<script type="text/javascript" >

	/**
	 * Sets the filter using the given HTML select object.
	 */
	function setFilterFromSelect(theSelect)	{
	  var newValue = theSelect.options[theSelect.selectedIndex].value;
	  filterMap[theSelect.name] = newValue;
	}
	
	//Called when reset is clicked
	function doReset() {
		document.participantForfeituresReportForm.task.value = "default";
		return true;
	}

	/**
	*   Function to clear the Name field when SSN provided
	*/
	function clearName(evt) {
	
		//IE or browsers that use the getElementById model
		if (document.getElementById('namePhrase')) {
			if (document.getElementById('namePhrase').value) {
				document.getElementById('namePhrase').value = "";
			}
		}
		
		//Netscape or browsers that use the document model
	  	evt = (evt) ? evt : (window.event) ? event : null;
	  	if (evt)
	  	{	
	    	var charCode = (evt.charCode) ? evt.charCode :
	                   ((evt.keyCode) ? evt.keyCode :
	                   ((evt.which) ? evt.which : 0));
	    	if (charCode == 9) {
	    		return false;
	    	}
	  	}    

	  	if (document.participantForfeituresReportForm.ssnOne) {
				if (document.participantForfeituresReportForm.ssnOne.value.length >= 0){
						document.participantForfeituresReportForm.namePhrase.value = "";
				}
		}

		if (document.participantForfeituresReportForm.ssnTwo) {
				if (document.participantForfeituresReportForm.ssnTwo.value.length >= 0){	
					document.participantForfeituresReportForm.namePhrase.value = "";
				}
		}
	
		if (document.participantForfeituresReportForm.ssnThree) {
				if (document.participantForfeituresReportForm.ssnThree.value.length >= 0){	
					document.participantForfeituresReportForm.namePhrase.value = "";
				}
		}
	
	}

	/**
	*   Function to clear the SSN field when Name field provided
	*/	
	function clearSSN(evt){
	
		//IE or browsers that use the getElementById model
		if (document.getElementById('ssnOne')) {
			if (document.getElementById('ssnOne').value) {
				document.getElementById('ssnOne').value = "";
			}
		} 
	
		if (document.getElementById('ssnTwo')) {
			if (document.getElementById('ssnTwo').value) {
				document.getElementById('ssnTwo').value = "";
			}
		} 
	
		if (document.getElementById('ssnThree')) {
			if (document.getElementById('ssnThree').value) {
				document.getElementById('ssnThree').value = "";
			}
		} 	
		
		//Netscape or browsers that use the document model
		evt = (evt) ? evt : (window.event) ? event : null;
	  	if (evt)
	  	{	
	    	var charCode = (evt.charCode) ? evt.charCode :
	                   ((evt.keyCode) ? evt.keyCode :
	                   ((evt.which) ? evt.which : 0));
	    	if (charCode == 9) {
	    		return false;
	    	}
	  	}    
		
		if (document.participantForfeituresReportForm.namePhrase) {
				if (document.participantForfeituresReportForm.namePhrase.value.length >= 0){
					document.participantForfeituresReportForm.ssnOne.value = "";
					document.participantForfeituresReportForm.ssnTwo.value = "";
					document.participantForfeituresReportForm.ssnThree.value = "";
				}	
		}
	}
	
	/**
	 * Sets the filter using the given HTML select object.
	 */	
	var previousSelectionIndex = 0;
	
	function setFilterFromSelect2(theSelect) {
	  var newValue = theSelect.options[theSelect.selectedIndex].value;
	  if ( newValue != "##" ) {
		  filterMap[theSelect.name] = newValue;
	  }
	  else {
		  theSelect.selectedIndex = previousSelectionIndex;
	  }
	}
</script>
</c:if>

	<c:if test="${empty param.printFriendly }" >
		<td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
		<td>
	</c:if>
	
	<c:if test="${not empty param.printFriendly }" >
		<td width="10"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
		<td>
	</c:if>

	<p>
		<c:if test="${sessionScope.psErrors ne null}">
<c:set var="errorsExist" value="true" scope="page" />
			<div id="errordivcs"><content:errors scope="session"/></div>
		</c:if>
	</p>

	<%-- Form - start --%>
	<ps:form method="POST" modelAttribute="participantForfeituresReportForm" name="participantForfeituresReportForm" action="/do/participant/participantForfeiture" >
    
<input type="hidden" name="task" value="filter"/>

	<%-- Evaluate the blank row colspan values based on Division field availability --%>
<c:if test="${participantForfeituresReportForm.showDivision ==true}">
		<c:set var="BlankRowColSpan" value="18"></c:set>
</c:if>
<c:if test="${participantForfeituresReportForm.showDivision !=true}">
		<c:set var="BlankRowColSpan" value="16"></c:set>
</c:if>

	<table width="730" border="0" height="160px" cellspacing="0" cellpadding="0" bgcolor="Beige">
	<tr>
		<td width="100%" colspan="4">
			<%-- TAB section - starts --%>
			<c:if test="${not empty param.printFriendly }" >
				<jsp:include flush="true" page="accountsPageNavigationTabBar.jsp">
					<jsp:param name="selectedTab" value="AccountsForfeituresPrintTab"/>
				</jsp:include>
			</c:if>
			<c:if test="${empty param.printFriendly }" >
				<jsp:include flush="true" page="accountsPageNavigationTabBar.jsp">
					<jsp:param name="selectedTab" value="AccountsForfeituresTab"/>
				</jsp:include>
			</c:if>
			<%-- TAB section - ends --%>
		</td>
	</tr>

	<%-- Search Criteria fields --%>
	<tr class="tablehead" >
		<td class="tablesubhead" colspan="8"><b>Participant Forfeitures Search</b></td>
	</tr>
	
	<tr class=filterSearch>
		<td colspan="4">
			<content:getAttribute id="searchForParticipants" attribute="text"></content:getAttribute>
			<c:if test="${not empty layoutPageBean.introduction2}">
				<content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
				<br>
</c:if>
		</td>
	</tr>
	


	
	<tr class=filterSearch>
		<td width="220"><b>Summary as of </b><br>
		<form:hidden path="baseAsOfDate"/>
			<render:date property="userProfile.currentContract.contractDates.asOfDate" 
						 dateStyle="<%=RenderConstants.LONG_STYLE%>"/>
		</td>
		<td width="150"><b>Last name</b><br>
			<c:if test="${not empty param.printFriendly }" >
<form:input path="namePhrase" disabled="true" onchange="setFilterFromInput(this);" readonly="true" cssClass="inputField" />

			</c:if>
			<c:if test="${empty param.printFriendly }" >
<form:input path="namePhrase" onchange="setFilterFromInput(this);" onkeypress="clearSSN(event);" cssClass="inputField" tabindex="20" />

			</c:if>
		</td>
		<td width="190"><b>SSN</b><br>  
			<c:if test="${not empty param.printFriendly}" >
			<form:password path="ssnOne" value="${participantForfeituresReportForm.ssnOne}"  readonly="true" disabled="true" styleClass="inputField"  onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
			<form:password path="ssnTwo" value="${participantForfeituresReportForm.ssnTwo}" readonly="true" disabled="true" styleClass="inputField"  onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2"/>
			<form:input path="ssnThree"  autocomplete="off" readonly="true"   disabled="true" styleClass="inputField"  onkeyup="return autoTab(this, 4, event);"  size="4" maxlength="4"/>
			</c:if>
			<c:if test="${empty param.printFriendly}">
			<form:password path="ssnOne" value="${participantForfeituresReportForm.ssnOne}" styleClass="inputField" onkeypress="clearName(event);"  onkeyup="return autoTab(this, 3, event);"  size="3" maxlength="3"  tabindex="30"/>
			<form:password path="ssnTwo" value="${participantForfeituresReportForm.ssnTwo}" styleClass="inputField" onkeypress="clearName(event);"  onkeyup ="return autoTab(this, 2, event);" size="2" maxlength="2"  tabindex="31"/>
			<form:input path="ssnThree" autocomplete="off" styleClass="inputField"  onkeypress="clearName(event);"   onkeyup ="return autoTab(this, 4, event);" size="4" maxlength="4"  tabindex="32"/>
			</c:if>	            	  
		</td>        
		<td width="170" valign="bottom" class="filterSearch">
<c:if test="${participantForfeituresReportForm.showDivision ==true}">
				<b>Division</b><br>
				<c:if test="${not empty param.printFriendly }" >
<form:input path="division" disabled="true" readonly="true" cssClass="inputField"/>
				</c:if>
				<c:if test="${empty param.printFriendly }" >
<form:input path="division" maxlength="25" cssClass="inputField" tabindex="40"/>
				</c:if>
</c:if>&nbsp;
		</td>
	</tr>

	<tr class=filterSearch>
		<td>
			<DIV id="contributionStatus"> <b>Contribution status</b>	<br>
				<c:if test="${not empty param.printFriendly }" >
<form:select path="status" disabled="true" onchange="setFilterFromSelect2(this);" >
					<form:option value="All">All</form:option>
					<form:option value="Active">Active</form:option>
<c:if test="${moneyTypeFlag ==false}">
						<form:option value="Active no balance">Active no balance</form:option>
						<form:option value="Active non contributing">Active non contributing</form:option>
						<form:option value="Active opted out">Active opted out</form:option>
</c:if>
					<form:option value="Inactive not vested">Inactive not vested</form:option>
					<form:option value="Inactive with balance">Inactive with balance</form:option>
<c:if test="${moneyTypeFlag ==false}">
						<form:option value="Opted out not vested">Opted out not vested</form:option>
</c:if>
</form:select>
				</c:if>
				<c:if test="${empty param.printFriendly }" >
<form:select path="status" onchange="setFilterFromSelect2(this);" tabindex="50" >
					<form:option value="All">All</form:option>
					<form:option value="Active">Active</form:option>
<c:if test="${moneyTypeFlag ==false}">
						<form:option value="Active no balance">Active no balance</form:option>
						<form:option value="Active non contributing">Active non contributing</form:option>
						<form:option value="Active opted out">Active opted out</form:option>
</c:if>
					<form:option value="Inactive not vested">Inactive not vested</form:option>
					<form:option value="Inactive with balance">Inactive with balance</form:option>
<c:if test="${moneyTypeFlag ==false}">
						<form:option value="Opted out not vested">Opted out not vested</form:option>
</c:if>
</form:select>
				</c:if>
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</DIV>
		</td>
		<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>	 
		<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td valign="middle">
			<div align="left">
				<c:if test="${empty param.printFriendly }" >
					<input type="submit" name="submit" value="search" tabindex="70"/>
					<input type="submit" name="reset" value="reset" tabindex="80" onclick="return doReset();"/>
				</c:if>
				<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
			</div>
		</td>
	</tr>
	
	<tr>
		<td colspan="4" class="filterSearch">
			<img src="/assets/unmanaged/images/s.gif" width="1" height="5">
		</td>
	</tr>
	</table>
	
	<br>

	<%-- Accounts Forfeitures Details --%>
	<table width="730" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="1"></td>
			<td width="25"></td>  		<%--History icon --%>
			<td width="1"></td>
			<td width="150"></td>  		<%--Name/SSN--%>
<c:if test="${participantForfeituresReportForm.showDivision ==true}">
				<td width="1"></td>
				<td width="75"></td>	<%--Division--%>
</c:if>
			<td width="1"></td>
			<td width="125"></td> 		<%--Status--%>
			<td width="1"></td>
			<td width="85"></td> 		<%--Withdrawal transaction date--%>
			<td width="1"></td>          
			<td width="85"></td>		<%--Employee assets--%>
			<td width="1"></td>
			<td width="85"></td>		<%--Employer assets--%>
			<td width="1"></td>
			<td width="85"></td>		<%--Total assets--%>
			<td width="1"></td>
			<td width="75"></td>		<%--Forfeitures--%>
			<td width="1"></td>		
		</tr>
		
		<tr class="tablehead">
			<td class="tableheadTD1" colspan="27">
				<b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b>
			</td>
		</tr>
	
		<%-- Participant Summary Total details --%>
		<c:if test="${not empty theReport.participantForfeituresTotals}">
			<tr class="datacell1">
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td colspan="15" align="center">
					<b class="copyBig">Total participants:<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
						<span class="highlight">
${theReport.participantForfeituresTotals.totalParticipants}
						</span>
					</b>
				</td>
				<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" width="85"><b>Employee<br><NOBR>assets($)</NOBR></b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" width="85"><b>Employer<br><NOBR>assets($)</NOBR></b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" width="85"><b>Total<br><NOBR>assets($)</NOBR></b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" width="85"><b><NOBR>Forfeitures($)<NOBR></b></td>
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>  
			</tr>
			
			<tr class="datacell1">
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td colspan="15" rowspan="3" align="center">
					<table width="100%" align="center" border="0"
						cellpadding="0" cellspacing="1">
						<tbody>
							<tr>
								<td>
									<span class="notice">Notification:</span>
									<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
									<content:getAttribute beanName="accountsPageNotification" attribute="text">
										<content:param>/do/transaction/cashAccountForfeituresReport/</content:param>
									</content:getAttribute>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
				<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="85" class="datacell2"><b>Totals</b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" class="datacell2"><b><span class="highlight"><render:number property='theReport.participantForfeituresTotals.employeeAssetsTotal' defaultValue = "0.00" pattern="###,###,##0.00"/></span></b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" class="datacell2"><b><span class="highlight"><render:number property='theReport.participantForfeituresTotals.employerAssetsTotal' defaultValue = "0.00" pattern="###,###,##0.00"/></span></b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" class="datacell2"><b><span class="highlight"><render:number property='theReport.participantForfeituresTotals.totalAssets' defaultValue = "0.00" pattern="###,###,##0.00"/></span></b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" class="datacell2"><b><span class="highlight"><render:number property='theReport.participantForfeituresTotals.forfeituresTotal' defaultValue = "0.00" pattern="###,###,##0.00"/></span></b></td>
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</tr>
			
			<tr class="datacell1">
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="85"><b>Averages</b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" class="datacell1"><b><span class="highlight"><render:number property='theReport.participantForfeituresTotals.employeeAssetsAverage' defaultValue = "0.00" pattern="###,###,##0.00"/></span></b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" class="datacell1"><b><span class="highlight"><render:number property='theReport.participantForfeituresTotals.employerAssetsAverage' defaultValue = "0.00" pattern="###,###,##0.00"/></span></b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" class="datacell1"><b><span class="highlight"><render:number property='theReport.participantForfeituresTotals.totalAssetsAverage' defaultValue = "0.00" pattern="###,###,##0.00"/></span></b></td>
				<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td align="right" class="datacell1"><b><span class="highlight"><render:number property='theReport.participantForfeituresTotals.forfeituresAverage' defaultValue = "0.00" pattern="###,###,##0.00"/></span></b></td>		
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</tr>
			
			<tr class="datacell1">
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td colspan="9" class="beigeborder"></td>
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</tr>
		</c:if>
	
		<%-- Participant Transaction History Icon --%>
		<tr class="datacell1">
			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td colspan="25">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<c:if test="${empty param.printFriendly }" >
								<span class="b11 style1">Participant transaction history <img src="/assets/unmanaged/images/history_icon.gif" width="12" height="12"></span>
							</c:if>	            	  
						</td>
					</tr>
				</table>
			</td>
			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
	
		<%-- Participant Transaction Count details with Pagination --%>
		<tr class="tablehead">
			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td class="tablehead" colspan="25">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="tableheadTD" width="40%"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td class="tableheadTDinfo"><b><report:recordCounter report="theReport" label="Participants"/></b></td>
						<td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="participantForfeituresReportForm"/></td>
					</tr>
				</table>
			</td>
			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
	</table>

	<%-- Participant Forfeiture Details --%>
	<div style="width:730px;">
	<table border="0" cellspacing="0" width="100%" cellpadding="0">         
		<tr class="tablesubhead">
		 
			<%-- History icon --%>
			<td class="databorder" width="1"></td>
			<td width="25"></td>
		
			<%-- Name/SSN --%>
			<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="bottom" width="125">
				<report:sort field="lastName" direction="asc" formName="participantForfeituresReportForm"><b>Name/SSN</b></report:sort>
			</td>
		<!-- Start of Details -->
	<% boolean beigeBackgroundClsFixed = false; // used to determine if the cell should use the background style
   boolean lastLineBkgrdClassFixed = false; // used to determine if the last line should be beige or not
   %>
			<%-- Division --%>
<c:if test="${participantForfeituresReportForm.showDivision ==true}">
				<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td valign="bottom"><report:sort field="division" direction="asc" formName="participantForfeituresReportForm"><b>Division</b></report:sort></td>
</c:if>
		
			<%-- Status --%>
		    <td class="dataheaddivider" valign="bottom"  width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="bottom"><report:sort field="status" direction="asc" formName="participantForfeituresReportForm"><b>Contribution status</b></report:sort></td>         

			<%-- Withdrawal transaction date --%>
		    <td class="dataheaddivider" valign="bottom"  width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="bottom"><report:sort field="terminationDate" direction="desc" formName="participantForfeituresReportForm"><b>Withdrawal transaction date</b></report:sort></td>         
		
			<%-- Employee assets --%>
			<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="bottom" align="right"><b><NOBR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<report:sort field="employeeAssets" direction="desc" formName="participantForfeituresReportForm"><b>Employee<br>assets($)</b></report:sort></NOBR></td>
			
			<%-- Employer assets --%>
			<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="bottom" align="right"><b><NOBR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<report:sort field="employerAssets" direction="desc" formName="participantForfeituresReportForm"><b>Employer<br>assets($)</b></report:sort></NOBR></td>
			
			<%-- Total assets --%>
			<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="bottom" align="right"><b><NOBR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<report:sort field="totalAssets" direction="desc" formName="participantForfeituresReportForm"><b>Total<br>assets($)</b></report:sort></NOBR></td>
			
			<%-- Forfeitures --%>
			<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			<td valign="bottom" align="right"><b><NOBR>&nbsp;&nbsp;&nbsp;<report:sort field="forfeitures" direction="desc" formName="participantForfeituresReportForm"><b>Forfeitures($)</b></report:sort></NOBR></td>
		
			<%-- spacer --%>
			<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		
		<tr class="tablesubhead">
			<td class="dataheaddivider" colspan="${BlankRowColSpan}" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
		</tr>
	
		<c:if test="${empty param.printFriendly }" >
			<%
			if (!StringUtils.isEmpty(participantForfeituresReportForm.getNamePhrase()) ||
				!StringUtils.isEmpty(participantForfeituresReportForm.getStatus()) ||
				!StringUtils.isEmpty(participantForfeituresReportForm.getDivision()) ||
				!participantForfeituresReportForm.getSsn().isEmpty())  
			{
			%>
				<tr class="datacell1" height="15">
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td width="25"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${participantForfeituresReportForm.showDivision ==true}">
						<td valign="top" colspan="13" height="4">
</c:if>
<c:if test="${participantForfeituresReportForm.showDivision !=true}">
						<td valign="top" colspan="11" height="4">	
</c:if>
						<c:choose>
							<c:when test="${theReport.details != null && !empty theReport.details}"> 
								<content:getAttribute id="viewAllParticipants" attribute="text">
									<content:param>/do/participant/participantForfeiture</content:param>
								</content:getAttribute>
							</c:when>
							<c:otherwise>
								<c:if test="${ pageScope.errorsExist eq null}">
									<content:getAttribute id="noSearchResults" attribute="text">
										<content:param>/do/participant/participantForfeiture</content:param>
									</content:getAttribute>
								</c:if>
							</c:otherwise>
						</c:choose>					
					</td>
					<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>
			<% } %>
		</c:if>
	<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >


 <c:set var="indexValue" value="${theIndex.index}"/> 
				
		<c:set var="rowId" value="${ indexValue % 2 }" />
			<c:choose>
				<c:when test="${rowId == 0}"> 
					<c:set var="rowClass" value="datacell2" />
				</c:when>
				<c:otherwise>
					<c:set var="rowClass" value="datacell1" />
				</c:otherwise>
			</c:choose>
		
		
		
		
			<tr class="${rowClass}">
			 <c:set var="prefixURL" value="/do/participant/participantAccount/?selectedAsOfDate=" />
<c:set var="suffixURLAsOfDate" value="${participantForfeituresReportForm.getAsOfDate()}"/>
				<c:set var="actionURL" value="${prefixURL}${suffixURLAsOfDate}"/>
			
				<%-- history icon --%> 
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<c:if test="${empty param.printFriendly }" >
					<td width="25">
						<ps:link action="/do/transaction/participantTransactionHistory/" paramId="profileId" paramName="theItem" paramProperty="profileId">
						<img src="/assets/unmanaged/images/history_icon.gif" alt="Participant transaction history" width="12" height="12" border="0">
						</ps:link>
					</td>
				</c:if>
				<c:if test="${not empty param.printFriendly }" >
					<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</c:if>
					
				<%-- Name/SSN --%>
				<td class="datadivider" width="1"></td>
				<td>
					<ps:link action="${actionURL}" paramId="profileId" paramName="theItem" paramProperty="profileId">
${theItem.lastName},
${theItem.firstName}<br>
						<render:ssn property="theItem.ssn" />
					</ps:link>
				</td>
				
				<%-- Division --%>
<c:if test="${participantForfeituresReportForm.showDivision ==true}">
					<td class="datadivider" width="1"></td>
<td>${theItem.division}</td>
</c:if>
				
				<%-- Status --%>
				<td class="datadivider" width="1"></td>
<td>${theItem.status}</td>

				<%-- Withdrawal transaction date --%>
				<td class="datadivider" width="1"></td>
				<td><render:date property="theItem.terminationDate" patternIn="yyyy-MM-dd" patternOut="MM/dd/yyyy"/></td>
			
				<%-- Employee assets --%>
				<td class="datadivider" width="1"></td>
				<td align="right">
					<c:choose>
						<c:when test="${theItem.employeeAssets < 0}"> * </c:when>
						<c:otherwise>
							<render:number property='theItem.employeeAssets' defaultValue = "0.00"  pattern="###,###,##0.00"/>
						</c:otherwise>
					</c:choose>
				</td>
				<%-- Employer assets --%>
				<td class="datadivider" width="1"></td>
				<td align="right">
					<c:choose>
						<c:when test="${theItem.employerAssets < 0}"> * </c:when>
						<c:otherwise>
							<render:number property='theItem.employerAssets' defaultValue = "0.00"  pattern="###,###,##0.00"/>
						</c:otherwise>
					</c:choose>	
				</td>
				
				<%-- Total assets --%>
				<td class="datadivider" width="1"></td>
				<td align="right">
					<c:choose>
						<c:when test="${theItem.totalAssets < 0}"> * </c:when>
						<c:otherwise>
							<render:number property='theItem.totalAssets' defaultValue = "0.00"  pattern="###,###,##0.00"/>
						</c:otherwise>
					</c:choose>
				</td>
			
				<%-- Forfeitures --%>
				<td class="datadivider" width="1"></td>
				<td align="right">
					<c:choose>
						<c:when test="${theItem.forfeitures < 0}"> * </c:when>
						<c:otherwise>
							<render:number property='theItem.forfeitures' defaultValue = "0.00"  pattern="###,###,##0.00"/>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			
			</tr>
</c:forEach>
		</c:if>
	
		<tr>
			<td class="databorder" colspan="${BlankRowColSpan}"></td>
		</tr>
	<c:if test="${empty param.printFriendly }" >
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
	</c:if>	
	<tr>
		<td colspan="${BlankRowColSpan}" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="participantForfeituresReportForm"/></td>
	</tr>
	<tr>
		<td colspan="${BlankRowColSpan}">
			<c:if test="${empty param.printFriendly }" >
				<table>
					<tr>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>
			</c:if>	
						<br>
						<p><content:pageFooter beanName="layoutPageBean"/></p>
						<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
						<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
			<c:if test="${empty param.printFriendly}" >
						</td>
						</tr>
				</table>
			</c:if>	
		</td>
	</tr>	
	</ps:form>
	</table>
	<br>
	</td>

	<%-- Column to the right of the report --%>
	<td width="15"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	<c:if test="${not empty param.printFriendly}" >
		<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
		id="globalDisclosure"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
			</tr>
		</table>
	</c:if>

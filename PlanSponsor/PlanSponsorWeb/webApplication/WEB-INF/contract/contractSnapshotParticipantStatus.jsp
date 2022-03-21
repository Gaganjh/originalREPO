<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractSnapshotVO" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="psweb" %>


<%-- Beans used --%>
<%-- <c:set var="contractSnapshot" value="${ContractSnapshotVO}" /> --%>

<jsp:useBean id="contractSnapshot" scope="session" 
	class="com.manulife.pension.service.contract.valueobject.ContractSnapshotVO" />

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_SNAPSHOT_ENCOURAGE_ENROLLMENT_LINK%>"
                           	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="encourageEnrollmentLink"/>

<script type="text/javascript" >
function calculateParticipationRate()
{
var error1 = '${contractSnapshotForm.error1} <%-- filter="false" --%>';
var error2 = '${contractSnapshotForm.error2} <%-- filter="true" --%>';
var error3 = '${contractSnapshotForm.error3} <%-- filter="true" --%>';

	// Put some default text in all the div's
	var divObjErrorText = document.getElementById("ErrorText");
	divObjErrorText.innerHTML= "";

	var divObjBlankText = document.getElementById("BlankText");
	divObjBlankText.innerHTML = "";

	var divObjParticipationRate = document.getElementById("ParticipationRate");
	divObjParticipationRate.innerHTML = "&nbsp;0%";

	var totalEmployees = document.eligibleToEnrollForm.eligibleToEnrollText.value;
var activeEmployees = '${contractSnapshot.contractAssetsByRisk.totalActiveParticipantsNumber}';

	// Check that is not blank
	if (totalEmployees == "")
	{
		divObjErrorText.innerHTML= "<ul><li>"+ error1 +"</li></ul>";
		divObjBlankText.innerHTML = "&nbsp;";
		return;
	}

	// Check for valid characters
	for (var i = 0; i < totalEmployees.length; i++)
	{
		if (totalEmployees.charAt(i) < "0" || totalEmployees.charAt(i) > "9")
		{
			divObjErrorText.innerHTML= "<ul><li>"+ error3 + "</li></ul>";
			divObjBlankText.innerHTML = "&nbsp;";
			return;
		}
	}

	// Participation rate cannot be bigger than 100%
	if (parseInt(totalEmployees) < parseInt(activeEmployees))
	{
		divObjErrorText.innerHTML= "<ul><li>"+ error2 +"</li></ul>";
		divObjBlankText.innerHTML = "&nbsp;";
		return;
	}

	var rate = activeEmployees/totalEmployees * 100;

	divObjParticipationRate.innerHTML = "&nbsp;" + Math.round(rate) + "%";
}


</script>

<a name="#thisPage"/>
  <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><b>Participant status</b></td>
                <td align="right">&nbsp;</td>
              </tr>
            </table>
          </td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>
<%--                 If the contract is discontinued do not display the participation rate sections (PPR.32)
                	<c:if test="${userProfile.currentContract.status!=Contract.STATUS_CONTRACT_DISCONTINUED}">
 	               <b>Participation rate</b>
                </c:if> --%>
                
   			 <% String DISCONT = Contract.STATUS_CONTRACT_DISCONTINUED; 
				String.valueOf(Contract.STATUS_CONTRACT_DISCONTINUED);
				pageContext.setAttribute("DISCONT", DISCONT,PageContext.PAGE_SCOPE); %> 
			<c:if test="${userProfile.currentContract.status != DISCONT}">
 	               <b>Participation rate</b>
			</c:if>
                </td>
                <td align="right">&nbsp;</td>
              </tr>
            </table>
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top">
          	<table width="100%">
          	<tr>

          	<td>
<c:if test="${contractSnapshot.contractAssetsByRisk.hasEmployeeMoneyType ==true}">
          	<table width = "100%">
<c:if test="${contractSnapshot.contractAssetsByRisk.activeParticipantsNumber !=0}">
	          	<tr>
 		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_ACTIVE%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>
  		        	<td>Active</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.activeParticipantsNumber}</td>
  		        </tr>
</c:if>

<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveWithBalanceParticipantsNumber !=0}">
          		<tr>
 		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>

          			<td>Inactive with balance</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveWithBalanceParticipantsNumber}</td>
       			</tr>
</c:if>

<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveWithUMParticipantsNumber !=0}">
	      		<tr>
  		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>

          			<td>Inactive unvested money only</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveWithUMParticipantsNumber}</td>
       			</tr>
</c:if>

          		<tr>
 		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background:#FFFFFF"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>

          			<td class="highlight"><b>Total</b></td>
<td align="right" class="highlight"><b>${contractSnapshot.contractAssetsByRisk.totalParticipantsNumber}</b></td>
       			</tr>

          	</table>
</c:if>

<c:if test="${contractSnapshot.contractAssetsByRisk.hasEmployeeMoneyType ==false}">
          	<table width = "100%">

<c:if test="${contractSnapshot.contractAssetsByRisk.activeContributingParticipantsNumber !=0}">
	          	<tr>
 		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_ACTIVE%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>
  		        	<td>Active</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.activeContributingParticipantsNumber}</td>
  		        </tr>
</c:if>

<c:if test="${contractSnapshot.contractAssetsByRisk.activeNoBalanceParticipantsNumber !=0}">
  		        <tr>
 		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_ACTIVE_NO_BALANCE%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>
  		        	<td>Active with no balance</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.activeNoBalanceParticipantsNumber}</td>
  		        </tr>
</c:if>

<c:if test="${contractSnapshot.contractAssetsByRisk.activeNotContributingParticipantsNumber !=0}">
  		        <tr>
 		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_ACTIVE_NOT_CONTRIBUTING%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>
  		        	<td>Active but not contributing</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.activeNotContributingParticipantsNumber}</td>
  		        </tr>
</c:if>
  		        
<c:if test="${contractSnapshot.contractAssetsByRisk.activeOptedOutParticipantsNumber !=0}">
		        <tr>
		         	<td>
				       	<table border="0" cellpadding="0" cellspacing="0">
   	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_ACTIVE_OPTED_OUT%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
    			   	</table>
	           	</td>
		        	<td>Active opted out</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.activeOptedOutParticipantsNumber}</td>
		        </tr>
</c:if>

<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveWithBalanceParticipantsNumber !=0}">
          		<tr>
 		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>

          			<td>Inactive with balance</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveWithBalanceParticipantsNumber}</td>
       			</tr>
</c:if>

<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveWithUMParticipantsNumber !=0}">
	      		<tr>
  		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>

          			<td>Inactive unvested money only</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveWithUMParticipantsNumber}</td>
       			</tr>
</c:if>
       			
<c:if test="${contractSnapshot.contractAssetsByRisk.optedOutNotVestedParticipantsNumber !=0}">
		        <tr>
		         	<td>
				       	<table border="0" cellpadding="0" cellspacing="0">
 	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_OPTED_OUT_NOT_VESTED%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
  			   	</table>
	           	</td>
		        	<td>Opted out not vested</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.optedOutNotVestedParticipantsNumber}</td>
		        </tr>
</c:if>
		        
<c:if test="${contractSnapshot.contractAssetsByRisk.optedOutZeroBalanceParticipantsNumber !=0}">
		        <tr>
		         	<td>
				       	<table border="0" cellpadding="0" cellspacing="0">
   	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_OPTED_OUT_ZERO_BALANCE%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
    			   	</table>
	           	</td>
		        	<td>Opted out zero balance</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.optedOutZeroBalanceParticipantsNumber}</td>
		        </tr>
</c:if>

<c:if test="${contractSnapshot.contractAssetsByRisk.inactiveParticipantsNumber !=0}">
	      		<tr>
  		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background: <%=Constants.ParticipantStatusPieChart.COLOR_INACTIVE%>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>

          			<td>Inactive</td>
<td align="right">${contractSnapshot.contractAssetsByRisk.inactiveParticipantsNumber}</td>
       			</tr>
</c:if>


          		<tr>
 		         	<td>
   				       	<table border="0" cellpadding="0" cellspacing="0">
       	    	          <tr><td height="11" width="11" style="background:#FFFFFF"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
        			   	</table>
		           	</td>

          			<td class="highlight"><b>Total</b></td>
<td align="right" class="highlight"><b>${contractSnapshot.contractAssetsByRisk.totalParticipantsNumber}</b></td>
       			</tr>

          	</table>
</c:if>

          	</td>

          	<td> </td>
       		<td align="center">
            <%--img src="/assets/unmanaged/images/snapshot_participant_status.gif" width="295" height="96"--%>
			<psweb:pieChart scope="<%=pageContext.REQUEST_SCOPE%>" beanName="<%=Constants.CONTRACT_SNAPSHOT_STATUS_PIECHART%>" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Participant Status"/>
			</td>
			</tr>
			<tr>
			
				<psweb:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
				<td colspan="3">
	              <a href="/do/participant/participantSummary">Go to participant summary</a>
	            </td>
	            </psweb:isNotJhtc>
	       		</tr>
	       		</table>
			

          </td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="2" valign="top">
          <%-- If the contract is discontinued do not display the participation rate sections (PPR.32)--%>
		<c:if test="${userProfile.currentContract.status != DISCONT}">
			<content:getAttribute beanName="layoutPageBean" attribute="body1" />
			<br>
			<br>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="210">Employees in the Contract as of <render:date
							property="userProfile.currentContract.contractDates.asOfDate"
							defaultValue="" />
					</td>
					<td>&nbsp;<b>${contractSnapshot.contractAssetsByRisk.totalActiveParticipantsNumber}</b>
					</td>
				</tr>
				<tr>
					<td width="210" height="32">Employees eligible to enroll</td>
					<td height="32">
						<form name="eligibleToEnrollForm">
							<input type="text" name="eligibleToEnrollText" size="5"
								maxlength="6" />&nbsp;<a href="#thisPage"
								onclick="calculateParticipationRate()">calculate</a>
						</form>
					</td>
				</tr>
				<tr>
					<td class="redText"><div id="ErrorText"></div></td>
				</tr>
				<tr>
					<td><div id="BlankText"></div></td>
				</tr>
				<tr>
					<td width="210" class="highlight"><b>Participation rate</b></td>
					<td><b class="highlight"><div id="ParticipationRate">&nbsp;0%</div></b></td>
				</tr>
			</table>
<%-- 					    <c:if test="${Constants.USERPROFILE_KEY.currentContract.mta=='false'}">
 --%>			
			 <c:if test="${userProfile.currentContract.mta == false}">	<br>
            	<content:getAttribute beanName="encourageEnrollmentLink" attribute="text"/>
</c:if>


            <p><br>
          </p>
</c:if>
	       </td>

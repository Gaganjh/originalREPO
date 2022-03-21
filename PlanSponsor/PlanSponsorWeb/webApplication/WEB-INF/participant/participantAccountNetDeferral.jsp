<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.ErrorCodes" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%> 
        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>


<c:set var="details" value="${requestScope.details}" scope="page" />



  <!-- Inlcude common -->        
    <jsp:include page="/WEB-INF/participant/participantAccountCommon.jsp" flush="true" /> 
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
	 <c:if test="${empty requestScope.errors}">
         
		<script type="text/javascript">   
		// This function is implemented to be executed during onLoad.
		// PPR. 
		function doOnload() {
		  //var tabs = document.getElementById("tabs");
		  //alert("tabs="+tabs.style.top);
		  scroll(0,450);
		}
		</script>   
		<% int style=1; %>   

		<tr>
		  <td height="1" colspan="1" rowspan="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td height="1" colspan="4" rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td height="1" colspan="1" rowspan="1" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td height="10" colspan="5" rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td height="1" colspan="1" rowspan="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td height="1" colspan="5" rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td height="1" colspan="1" rowspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>        
		<tr>
		  <td colspan="18" height="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		<tr>
		  <td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${participantAccountForm.showNetContribEarnings ==true}">
			<!--if after tax money - display the after tax money tab-->  			
				<c:if test="${empty param.printFriendly }" >
			  		<td colspan="16" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccount/');"><img src="/assets/unmanaged/images/participant_account_1.gif" width="117" height="30" border ="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeSummary/');"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeDetails/');"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountNetContribEarnings/');"><img src="/assets/unmanaged/images/after_tax_tab_off.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountNetDeferral/');"></a><img src="/assets/unmanaged/images/net_EE_deferrals_on.gif" width="118" height="30" border="0"></a></td>
				</c:if>
				<c:if test="${not empty param.printFriendly }" >
			  		<td colspan="16" valign="bottom" class="box"><img src="/assets/unmanaged/images/participant_account_1.gif" width="117" height="30"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/after_tax_tab_off.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/net_EE_deferrals_on.gif" width="118" height="30" border="0"></td>
				</c:if>
</c:if>
<c:if test="${participantAccountForm.showNetContribEarnings ==false}">
				<!--if no after tax money - suppress the after tax money tab-->       		
				<c:if test="${empty param.printFriendly }" >
			  		<td colspan="16" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccount/');"><img src="/assets/unmanaged/images/participant_account_1.gif" width="117" height="30" border ="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeSummary/');"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeDetails/');"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountNetDeferral/');"></a><img src="/assets/unmanaged/images/net_EE_deferrals_on.gif" width="118" height="30" border="0"></td>
				</c:if>
				<c:if test="${not empty param.printFriendly }" >
			  		<td colspan="16" valign="bottom" class="box"><img src="/assets/unmanaged/images/participant_account_1.gif" width="117" height="30"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/net_EE_deferrals_on.gif" width="118" height="30" border="0"></td>
				</c:if>
</c:if>
		  <td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		<tr>
		<td class="datadivider" colspan = "18" ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		<tr class ="datacell1">
		<td class="databorder" width="1" cellpading="0" ></td>
		<td colspan ="16">
		  <table width ="100%" border ="0" cellpadding="0" cellspacing="0">
			  <tr>
				<td width = "1%"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width = "30%"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width = "65%"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>		
				<td width = "4%"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>		
			  </tr>		
<c:if test="${participantAccountForm.hasInvestments ==false}">
			   <tr>
				<td ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td "datacell1" colspan="3"><content:getAttribute id="NoParticipantsMessage" attribute="text"/></td>
			   </tr>
</c:if>
<c:if test="${participantAccountForm.hasInvestments ==true}">
<c:if test="${details.netEEDeferralContributionsAvailable ==false}">
					<tr>
						<td ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td "datacell1" colspan="3">
							<content:getAttribute id="TechnicalDifficulties" attribute="text"/>
							&nbsp;&nbsp;[<%= ErrorCodes.TECHNICAL_DIFFICULTIES %>]
						</td>	
					</tr>
</c:if>
<c:if test="${details.netEEDeferralContributionsAvailable ==true}">
					 <tr>
						<td ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="3" valign="bottom"><content:getAttribute id="layoutPageBean" attribute="body1"/></td>
					 </tr>
					<tr>
					  <td colspan="4" ><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td>	
					</tr>
					<tr>
					    <td></td>
					    <td><b>Net employee contribution</b></td>
						<td align="left" class="highlight" ><b>$<render:number property="details.netEEDeferralContributions" defaultValue = ""  pattern="###,###,##0.00"/></b></td>
						<td></td>
				    </tr>
				    <tr><td colspan="4"><hr/></td></tr>
					<tr>
					    <td></td>
					    <td><strong>Maximum hardship amount</strong></td>
						<td align="left" class="highlight" ><b>$<render:number property="details.maximumHardshipAmount" defaultValue = ""  pattern="###,###,##0.00"/></b></td>
						<td></td>
				    </tr>
				    <tr><td colspan="4"><hr/></td></tr>
				    <tr>
				        <td></td>
				        <td><strong>Current deferral on file</strong></td>
<td class="highlight" nowrap>${participantAccountForm.deferralContributionText} <%-- filter="false" --%>
				            <%		ParticipantAccountDetailsVO details = (ParticipantAccountDetailsVO)pageContext.getAttribute("details");
				            if (details.getParticipantDeferralVO().getOutstandingRequests() >0 ) { %>
				                   <a href="/do/participant/taskCenterTasks?profileId=${details.getProfileId()}"><IMG alt ="Click here to go to Deferral Tasks" height=16 src="/assets/unmanaged/images/alert.gif" width=16 border=0></a>
				            <% } %>
						 </td>
						 <td></td>													     
					</tr>					
				<logicext:if name="details" property="participantDeferralVO.auto" op="equal" value="true">
	     		<logicext:or name="details" property="participantDeferralVO.signUp" op="equal" value="true" />
	      		<logicext:then>
	      				
				<!-- If EZincrease is OFF in CSF, then display the status is OFF and suppress the other attributes-->
<c:if test="${details.participantDeferralVO.participantACIOn ==false}">
						<tr>
						  <td colspan="4"><hr/></td>
						</tr>
						<tr>
	                       <td></td>
						   <td><strong><u>Scheduled deferral increase</u></strong></td>
						   <td class="highlight">Off</td>
						   <td></td>
						</tr>
</c:if>
				
				<!-- If EZincrease is ON in CSF, then display the attributes -->
<c:if test="${details.participantDeferralVO.participantACIOn !=false}">
					
							<!--  EZincrease label -->
					<tr>
					  <td colspan="4"><hr/></td>
					</tr>
					<tr>
                       <td></td>
					   <td><strong><u>
						Scheduled deferral increase
						</u></strong></td>
					   <td></td>
					   <td></td>
					</tr>
					 <tr>
					   <td></td>							 
					   <td><strong>Date of next increase</strong></td>
					   <td class="highlight"><%=details.getParticipantDeferralVO().getDateOfNextIncrease()%></td>
					   <td></td>
					 </tr>							 
					 <tr>
					   <td></td>							 							 
					   <td><strong>Next increase</strong></td>
<td class="highlight">${participantAccountForm.nextIncreaseValue} <%-- filter="false" --%></td>
					   <td></td>
					 </tr>							 
					 <tr>
					   <td></td>							 							 							 
					   <td><strong>Personal rate limit</strong></td>
<td class="highlight">${participantAccountForm.personalRateLimit} <%-- filter="false" --%></td>
					   <td></td>   
					 </tr>							 							 
</c:if>

			    </logicext:then>
	      
	   		 </logicext:if>			
					<tr >
						<td colspan="4" ><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td>	
					</tr>
					<tr class ="datacell1">
						<td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan= "2"><content:getAttribute id="layoutPageBean" attribute="body2"/></td>
					</tr>	
</c:if>
</c:if>
			</table>
			</td>
			<td class="databorder" width="1"></td>		
        	<tr>
          <td class="databorder" colspan="18"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table>
      <br>
    </td>
    <td><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	</tr>
    <tr>
			<td width="1%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
			<td >
				<br>
			    <c:if test="${participantAccountForm.asOfDateCurrent and participantAccountForm.showManagedAccount }">
	                 <content:contentBean contentId="<%=ContentConstants.MA_FOOTNOTE%>"
					                      type="<%=ContentConstants.TYPE_DISCLAIMER%>"
					                      id="participantMAFootnote" />
		             <p class="footnote"><content:getAttribute id="participantMAFootnote" attribute="text" /></p>
                </c:if>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
				<p class="footnote">
					<content:pageFootnotes beanName="layoutPageBean"/><br>
<c:if test="${participantAccountForm.showPba ==true}">
						<content:getAttribute beanName="footnotePBA" attribute="text"/><br>
</c:if>
				</p>
				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
			</td>
			<td  ></td> 			
		</tr>
		</table>
	  <c:if test="${not empty param.printFriendly }" >
		 <content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        	id="globalDisclosure"/>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="5%">&nbsp;</td>
					<td width="95%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
				</tr>
		    </table>
      </c:if>
</c:if>
</c:if>
    <c:if test="${not empty requestScope.errors}">
    	<table width="715" border="0" cellspacing="0" cellpadding="0">
    	<tr>	
    		<td>
    		&nbsp;
    		</td>
    	</tr>
    	<tr>
    		<td> 
    		  <content:errors scope="session"/>
    		</td>
    	</tr>
    	<tr>	
    		<td>
    		&nbsp;
    		</td>
    	</tr>
    	</table>
</c:if>
 
  

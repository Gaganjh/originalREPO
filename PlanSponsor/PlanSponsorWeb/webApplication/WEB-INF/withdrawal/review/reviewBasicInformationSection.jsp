<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_BASIC_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="basicInformationSectionTitle"/>
<content:contentBean
  contentId="${contentConstants.LAST_PROCESSED_CONTRIBUTION_DATE_COMMENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="lastProcessedDateCommentText"/>
<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_1}" 
  beanName="step1PageBean" /> 
<form:hidden path="withdrawalRequestUi.withdrawalRequest.robustDateChangedAfterVesting" id="robustDateChangedAfterVestingIndicatorId"/>
<div style="padding-top:10px;padding-bottom:10px;">
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
 	  <tr>
      <td class="tableheadTD1" colspan="3">
       	<div style="padding-top:5px;padding-bottom:5px">
       		<span style="padding-right:2px" id="basicInformationShowIcon" onclick="showBasicInformationSection();">
       			<img src="/assets/unmanaged/images/plus_icon.gif" width="12" height="12" border="0">
       		</span>
       		<span style="padding-right:2px" id="basicInformationHideIcon" onclick="hideBasicInformationSection();">
       			<img src="/assets/unmanaged/images/minus_icon.gif" width="12" height="12" border="0">
       		</span>
       		<b><content:getAttribute beanName="basicInformationSectionTitle" attribute="text"/></b>
       	</div>
      </td>
   	</tr>
    <tr>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="basicInformationTable">
          <tr class="datacell1" valign="top">
 				    <td class="longSectionNameColumn"><strong>Request date</strong></td>
	          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				    <td class="shortIndentedValueColumn">${withdrawalForm.withdrawalRequestUi.requestDate}</td>
					</tr>
          <tr class="datacell1" valign="top">
					   <td class="longSectionNameColumn">
					   	 <strong>
					   	 	 	 Expiration date
					   	 </strong>
					   </td>
	           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					   <td class="shortIndentedValueColumn">
					   <ps:fieldHilight name="withdrawalRequestUi.expirationDate" singleDisplay="true"><ps:activityHistory itemNumber="${activityConstants.EXPIRATION_DATE.id}"/></ps:fieldHilight>
					   <c:choose>
			          	  		<c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
			          	  			
			          	  			<fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.expirationDate}" 
			          	  					type="DATE" pattern="MM/dd/yyyy"/>
			          	  		</c:when>
			          	  		<c:otherwise>
<form:input path="withdrawalRequestUi.expirationDate" onchange="return handleExpirationDateChanged();" cssClass="mandatory" id="expirationDateId"/>



               <img onclick="return handleDateIconClicked(event, 'expirationDateId', true, false);" src="/assets/unmanaged/images/cal.gif" border="0">
               (mm/dd/yyyy)
               </c:otherwise>
               </c:choose>
						 </td>
					</tr>
          <tr class="datacell1" valign="top">
            <td class="longSectionNameColumn"><strong>Type of withdrawal</strong></td>
	          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="shortIndentedValueColumn">
              <ps:displayDescription collection="${withdrawalReasons}"
                                     keyName="code" 
                                     keyValue="description" 
                                     key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}"/>
            </td>
          </tr>
          <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE}">
            <tr class="datacell1" valign="top">
              <td class="longSectionNameColumn"><strong>Hardship reason</strong></td>
  	          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td class="shortIndentedValueColumn">
              <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.reasonDetailCode" singleDisplay="true"><ps:activityHistory itemNumber="${activityConstants.HARDSHIP_REASON.id}"/></ps:fieldHilight>
                		<c:choose>
                	<c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
                	<ps:displayDescription collection="${hardshipTypes}" keyName="code" keyValue="description" 
				                  key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonDetailCode}"/>
                	</c:when>
			    	<c:otherwise>
<form:select path="withdrawalRequestUi.withdrawalRequest.reasonDetailCode" onchange="return handleHardshipReasonChanged();" id="hardshipReasonId">



                  			<form:option value="">- select -</form:option>
                  			<form:options items="${hardshipTypes}" itemValue="code" itemLabel="description"/>               
</form:select>
                	</c:otherwise>
			   </c:choose>
              </td>
            </tr>
            <tr class="datacell1" valign="top">
              <td class="longSectionNameColumn"><strong>Hardship Explanation</strong></td>
  	          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td class="shortIndentedValueColumn">${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonDescriptionForDisplay}</td>
            </tr>
          </c:if>
          <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}">
            <tr class="datacell1" valign="top">
              <td class="longSectionNameColumn" style="vertical-align: top;padding-top: 3px;">
              	<strong>
              		IRA provider
              	</strong></td>
              <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td class="shortIndentedValueColumn">
                <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" singleDisplay="true"><ps:activityHistory itemNumber="${activityConstants.IRA_PROVIDER.id}"/></ps:fieldHilight>
                <c:choose>
					<c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
						 <c:choose>
			          	   	  	<c:when test = "${withdrawalForm.withdrawalRequestUi.
			          	   	  					withdrawalRequest.iraServiceProviderCode == requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}">
<form:radiobutton disabled="${withdrawalForm.withdrawalRequestUi.viewOnly}" path="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" value="${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}"/>PenChecks



				          	  	</c:when>
			          	   	  	<c:when test = "${withdrawalForm.withdrawalRequestUi.
			          	   	  					withdrawalRequest.iraServiceProviderCode == requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}">
<form:radiobutton disabled="${withdrawalForm.withdrawalRequestUi.viewOnly}" path="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" value="${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}"/>Wealth Management Systems, Inc.


				          	  	</c:when>
				          	  	<c:when test = "${withdrawalForm.withdrawalRequestUi.
			          	   	  					withdrawalRequest.iraServiceProviderCode == requestConstants.IRA_SERVICE_PROVIDER_NEITHER_CODE}">
<form:radiobutton disabled="${withdrawalForm.withdrawalRequestUi.viewOnly}" path="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" value="${requestConstants.IRA_SERVICE_PROVIDER_NEITHER_CODE}"/>Other


				          	  	</c:when>
			          	  </c:choose>
			       	  </c:when>
			          <c:otherwise>
<form:radiobutton onclick="handleWmsiPaychecksChanged();" path="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" cssClass="${(isPendingApproval or withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" value="${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}"/>PenChecks



			                <br/>
<form:radiobutton onclick="handleWmsiPaychecksChanged();" path="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" cssClass="${(isPendingApproval or withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" value="${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}"/>Wealth Management Systems, Inc.



			                <br/>
<form:radiobutton onclick="handleWmsiPaychecksChanged();" path="withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode" cssClass="${(isPendingApproval or withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" value="${requestConstants.IRA_SERVICE_PROVIDER_NEITHER_CODE}"/>Other



               		</c:otherwise>
               </c:choose>
              </td>
            </tr>
          </c:if>
          <tr class="datacell1" valign="top">
            <td class="longSectionNameColumn"><strong>Participant leaving plan?<a href="javascript:openChangeWindow()"></a></strong></td>
	          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="shortIndentedValueColumn">${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantLeavingPlanInd ? 'Yes' : 'No'}</td>
          </tr>
          <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE || withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}">
            <tr class="datacell1" valign="top">
               <td class="longSectionNameColumn">
                 <strong>
                   Termination date
                 </strong>
               </td>
               <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               <td class="shortIndentedValueColumn"><ps:fieldHilight name="withdrawalRequestUi.terminationDate" singleDisplay="true">
					<ps:activityHistory itemNumber="${activityConstants.EVENT_DATE.id}"/>
				</ps:fieldHilight>
				 <c:choose>
                	<c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
                	<fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.terminationDate}" 
			          	  					type="DATE" pattern="MM/dd/yyyy"/>
                	</c:when>
			    	<c:otherwise>
<form:input path="withdrawalRequestUi.terminationDate" onchange="return handleTerminationDateChanged();" cssClass="mandatory" id="terminationDateId"/>



                 <img onclick="return handleDateIconClicked(event, 'terminationDateId', true, true);" src="/assets/unmanaged/images/cal.gif" border="0">
                 (mm/dd/yyyy)
                 </c:otherwise>
			   </c:choose>
               </td>
            </tr>
          </c:if>
          <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}">
            <tr class="datacell1" valign="top">
               <td class="longSectionNameColumn">
                 <strong>
                  Retirement date
                 </strong>
               </td>
               <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               <td class="shortIndentedValueColumn">
               <ps:fieldHilight name="withdrawalRequestUi.retirementDate" singleDisplay="true">
			      <ps:activityHistory itemNumber="${activityConstants.EVENT_DATE.id}"/>
  				 </ps:fieldHilight>
                 <c:choose>
                	<c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
                	<fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.retirementDate}" 
			          	  					type="DATE" pattern="MM/dd/yyyy"/>
                	</c:when>
			    	<c:otherwise>
<form:input path="withdrawalRequestUi.retirementDate" onchange="return handleRetirementDateChanged();" cssClass="mandatory" id="retirementDateId"/>



                 <img onclick="return handleDateIconClicked(event, 'retirementDateId', true, true);" src="/assets/unmanaged/images/cal.gif" border="0">
                 (mm/dd/yyyy)
                 </c:otherwise>
			   </c:choose>
               </td>
            </tr>
          </c:if>
          <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_DISABILITY_CODE}">
            <tr class="datacell1" valign="top">
               <td class="longSectionNameColumn">
                 <strong>
                   Disability date
                 </strong>
               </td>
               <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
               <td class="shortIndentedValueColumn">
               <ps:fieldHilight name="withdrawalRequestUi.disabilityDate" singleDisplay="true">
                      <ps:activityHistory itemNumber="${activityConstants.EVENT_DATE.id}"/>
				   </ps:fieldHilight>
                 <c:choose>
                	<c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
                	<fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.disabilityDate}" 
			          	  					type="DATE" pattern="MM/dd/yyyy"/>
                	</c:when>
			    	<c:otherwise>
<form:input path="withdrawalRequestUi.disabilityDate" onchange="return handleDisabilityDateChanged();" cssClass="mandatory" id="disabilityDateId"/>



                 <img onclick="return handleDateIconClicked(event, 'disabilityDateId', true, true);" src="/assets/unmanaged/images/cal.gif" border="0">
                 (mm/dd/yyyy)
                 </c:otherwise>
			   </c:choose>
               </td>
            </tr>
          </c:if>
          <c:if test="${withdrawalForm.withdrawalRequestUi.showFinalContributionDate}">
            <tr class="datacell1" valign="top">
              <td>
                <span id="basicLastContributionPayrollEndingDateCol1Id" class="longSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                  <strong>Last contribution payroll ending date</strong>
                </span>    
              </td>
              <td class="datadivider">
                <span id="basicLastContributionPayrollEndingDateCol2Id">
                  <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
                </span>
              </td>
              <td>
                <span id="basicLastContributionPayrollEndingDateCol3Id" class="shortIndentedValueColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                  ${withdrawalForm.withdrawalRequestUi.mostRecentPriorContributionDate}
                </span>
              </td>
  					</tr>
            <tr class="datacell1" valign="top">
              <td>
                <span id="basicFinalContributionDateCol1Id" class="longSectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
				   	<strong>
				   	 	Final contribution date
				    </strong>
                </span>
			  </td>
              <td class="datadivider">
                <span id="basicFinalContributionDateCol2Id">
                  <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
                </span>
              </td>
              <td>
                <span id="basicFinalContributionDateCol3Id" class="shortIndentedValueColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
					<ps:fieldHilight name="withdrawalRequestUi.finalContributionDate" singleDisplay="true">
						<ps:activityHistory itemNumber="${activityConstants.FINAL_CONTRIBUTION_DATE.id}"/>
					</ps:fieldHilight>
					<c:choose>
	                	<c:when test="${withdrawalForm.withdrawalRequestUi.viewOnly}">
	                	<form:hidden path="withdrawalRequestUi.finalContributionDate" id="finalContributionDateId"/>
<!-- <input type="hidden" name="withdrawalRequestUi.finalContributionDate" id="finalContributionDateId"/> -->

		                	<fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.finalContributionDate}" 
					          	  					type="DATE" pattern="MM/dd/yyyy"/>
	                	</c:when>
				    	<c:otherwise>
<form:input path="withdrawalRequestUi.finalContributionDate" onchange="return handleFinalContributionDateChanged();" cssClass="${(isPendingApproval or withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" id="finalContributionDateId"/>



		                   <img onclick="return handleDateIconClicked(event, 'finalContributionDateId', true, false);" src="/assets/unmanaged/images/cal.gif" border="0">
		                   (mm/dd/yyyy)
	                   </c:otherwise>
                   </c:choose>
                </span>
    				</td>
    			</tr>
          <tr class="datacell1" valign="top">
            <td colspan="3">
              <span id="basicFinalContributionDateCommentId" class="indentedValue" style="padding-left: ${isIE ? '2' : '4'}px;">
                <content:getAttribute beanName="lastProcessedDateCommentText" attribute="text"/>
              </span>
            </td>
          </tr>
          </c:if>
          <tr class="datacell1" valign="top">
						<td class="longSectionNameColumn"><strong>Payment to</strong></td>
	          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						<td class="shortIndentedValueColumn">
							<ps:displayDescription collection="${paymentToTypes}"
																		 keyName="code" 
																		 keyValue="description" 
																		 key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo}"/>
						</td>
					</tr>
          <tr class="datacell1" valign="top">
            <td colspan="3" class="indentedValue">
              <content:getAttribute beanName="step1PageBean" attribute="body2" escapeHtml="true" escapeJavaScript="true"/>
            </td>
          </tr>
        </table>
      </td>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3">
        <div id="basicInformationFooter">
          <table border="0" cellspacing="0" width="100%">
            <tr>
              <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
              <td><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
              <td rowspan="2" width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" height="5" width="5"></td>
            </tr>
            <tr>
              <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
  </table>
</div>					


<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>


<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />

<un:useConstants var="scheduleConstants"
	className="com.manulife.pension.service.contract.valueobject.VestingSchedule" />
<un:useConstants var="uiConstants"
	className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="planDataConstants"
	className="com.manulife.pension.service.contract.valueobject.PlanData" />

<c:set var="moneyTypes" scope="request"
	value="${loanFeaturesForm.lookupData['MONEY_TYPES_BY_CONTRACT']}" />
<c:set var="vestingServiceCreditMethods" scope="request" value="${loanFeaturesForm.lookupData['VESTING_SERVICE_CREDIT_METHOD']}"/>

<content:contentBean
	contentId="${contentConstants.LOAN_FEATURE_GLANCE_PLAN_PROVISIONS}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="planProvisions" />
<content:contentBean
	contentId="${contentConstants.LOAN_FEATURE_GLANCE_PERMISSIONS}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="loanPermissions" />
<content:contentBean
	contentId="${contentConstants.LOAN_FEATURE_GLANCE_VESTING}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="vesting" />
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_NO_LOAN_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noLoanMoneyTypesText"/>	
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_NO_VESTING_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noMoneyTypesText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_CALCULATE_VESTING_SELECTED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="calculateVestingSelected"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_CALCULATE_VESTING_NOT_SELECTED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="calculateVestingNotSelected"/>
<content:contentBean contentId="${contentConstants.GLOBAL_DISCLOSURE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="globalDisclosure"/>


<content:errors scope="request"/>
<table border="0" cellpadding="0" cellspacing="0" width="760">
	<tr>
		<td>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">

			<tr>
				<td width="30"><img src="/assets/unmanaged/images/s.gif"
					height="1" width="30"><br>
				<img src="/assets/unmanaged/images/s.gif" height="1"></td>
				
				<td width="501">
				
				<p><content:getAttribute beanName="layoutPageBean"
					attribute="introduction1" /><br>
				
				<content:getAttribute beanName="layoutPageBean"
					attribute="introduction2" /></p><br>
				<br>
				<table class="box" border="0" cellpadding="0" cellspacing="0"
					width="500">
					<tr>
						<td class="tableheadTD1"><b>&nbsp;<content:getAttribute
							beanName="planProvisions" attribute="text" /> </b></td>
						<td width="132" align="right" class="tablehead">&nbsp;</td>
					</tr>
				</table>
				<table class="box" border="0" cellpadding="0" cellspacing="0"
					width="500">
					<tr>

						<td width="1" class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						<td>
						<table border="0" cellpadding="0" cellspacing="0" width="498">
							<tr valign="top">
								<td colspan="3" class="tablesubhead"><b>Minimum and
								maximum amounts </b></td>
							</tr>
							<tr valign="top">
								<td class="datacell1">Minimum loan amount</td>
								<td width="94" align="right" class="datacell1"><strong>
								<fmt:formatNumber value="${loanFeaturesForm.planDataUi.planData.minimumLoanAmount}" type="CURRENCY" currencyCode="USD" />
								 </strong></td>

								<td width="182" class="datacell1">&nbsp;</td>
							</tr>
							<tr valign="top">
								<td class="datacell1">Maximum loan amount</td>
								<td align="right" class="datacell1"><strong> <fmt:formatNumber 	value="${loanFeaturesForm.planDataUi.planData.maximumLoanAmount}" type="CURRENCY" currencyCode="USD" />
								</strong></td>
								<td class="datacell1">&nbsp;</td>
							</tr>
							<tr valign="top">
								<td class="datacell1">Maximum loan percentage</td>
								<td align="right" class="datacell1"><strong>
								<fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_LOAN_PERCENTAGE_SCALE}" value="${loanFeaturesForm.planDataUi.planData.maximumLoanPercentage}" />${empty loanFeaturesForm.planDataUi.planData.maximumLoanPercentage ? '' : '%'}
								</strong></td>								
								<td class="datacell1">&nbsp;</td>
							</tr>
							<tr valign="top">
								<td class="datacell1">Maximum number of outstanding loans</td>
								<td align="right" class="datacell1">
								<strong>
								<c:choose>
				                    <c:when test="${planDataConstants.UNLIMITED_LOANS_ALLOWED == loanFeaturesForm.planDataUi.planData.maximumNumberofOutstandingLoans}">
                      					${planDataConstants.UNLIMITED}
                    				</c:when>
                    				<c:when test="${planDataConstants.UNSELECTED_LOANS_ALLOWED == loanFeaturesForm.planDataUi.planData.maximumNumberofOutstandingLoans}">
                      					<%-- Unspecified --%>
                    				</c:when>
                    				<c:otherwise>
                      					${loanFeaturesForm.planDataUi.planData.maximumNumberofOutstandingLoans}
                    				</c:otherwise>
                  				</c:choose>
								</strong></td>
								<td class="datacell1">&nbsp;</td> 
							</tr> 
							<tr valign="top">
								<td class="datacell1">Loan interest rate above prime</td>
								<td align="right" class="datacell1"><strong><fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${planDataConstants.MAXIMUM_INTEREST_RATE_SCALE}" value="${loanFeaturesForm.planDataUi.planData.loanInterestRateAbovePrime}"/>${empty loanFeaturesForm.planDataUi.planData.loanInterestRateAbovePrime ? '' : '%'}</strong></td>
								<td class="datacell1">&nbsp;</td>
							</tr>



							<tr valign="top">
								<td colspan="3" class="tablesubhead"><b>Allowable money
								types</b></td>
							</tr>
							<tr valign="top">
								<td colspan="3">
								<div><c:choose>
									<c:when
										test="${empty loanFeaturesForm.planDataUi.planData.allowableMoneyTypesForLoans}">
										<div class="data"><content:getAttribute
											beanName="noLoanMoneyTypesText" attribute="text" /></div>
									</c:when>
									<c:otherwise>
										<c:forEach
											items="${loanFeaturesForm.planDataUi.planData.allowableMoneyTypesForLoans}"
											var="moneyType" 
											varStatus="moneyTypeStatus">
											<div
												class="${(moneyTypeStatus.count % 2 == 0) ? 'rightColData' : 'leftColData'}">
											<div class="data"><ps:displayDescription
												collection="${moneyTypes}" 
												keyName="id"
												keyValue="contractLongName" 
												key="${moneyType}" />&nbsp;
												(<ps:displayDescription collection="${moneyTypes}" keyName="id" 												
												keyValue="contractShortName" key="${moneyType}" />)</div>
											</div>
											<c:if
												test="${moneyTypeStatus.count % 2 == 0 or moneyTypeStatus.last}">
												<div class="endDataRowAndClearFloats"></div>
											</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose></div>
								</td>
							</tr>

						</table>
						</td>
						<td width="1" class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					</tr>
					<tr>
						<td colspan="3" class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					</tr>
				</table>
                
                <p>
                <table border="0" cellpadding="0" cellspacing="0"
                    width="500">
                <tr><td>
				<content:getAttribute beanName="layoutPageBean"
					attribute="body1" />
				</td></tr>
				</table>
				</p>
				
				<table class="box" border="0" cellpadding="0" cellspacing="0"
					width="500">
					<tr>
						<td class="tableheadTD1"><b>&nbsp;<content:getAttribute
							beanName="loanPermissions" attribute="text" /> </b></td>
						<td width="132" align="right" class="tablehead">&nbsp;</td>
					</tr>
				</table>

				<table class="box" border="0" cellpadding="0" cellspacing="0"
					width="500">
					<tr>
						<td width="1" class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						<td>
						<table border="0" cellpadding="0" cellspacing="0" width="100%">

							<tr valign="top">
								<td colspan="7" class="datacell1"></td>
							</tr>
							<tr valign="top">
								<td width="296" class="datacell1">&nbsp;</td>
								<td width="1" class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td width="64" align="center" class="datacell1"><b>Initiate</b></td>
								<td width="1" class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td width="66" align="center" valign="top" class="datacell1"><b>Review</b></td>
								<td width="1" class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td width="69" align="center" class="datacell1"><b>Signing
								Authority </b></td>
							</tr>
							<tr valign="top">
								<td class="datacell2">Participating employees</td>
								<td width="1" class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td align="center" class="datacell2">
								<c:if test="${loanFeaturesForm.participantCanInitiate}">
								<img src="/assets/unmanaged/images/CheckMark.gif" width="16" height="18">
								</c:if>
								</td>
								<td width="1" class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td align="center" valign="top" class="datacell2">&nbsp;</td>
								<td width="1" class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td align="center" class="datacell2">&nbsp;</td>
							</tr>
							<c:if test="${not empty loanFeaturesForm.userInfoCollection}">	
								<tr valign="top">
									<td class="datacell1"><b>Plan Sponsor Website users</b></td>
									<td width="1" class="homedataborder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td align="center" class="datacell1">&nbsp;</td>
									<td width="1" class="homedataborder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td align="center" valign="top" class="datacell1">&nbsp;</td>
									<td width="1" class="homedataborder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td align="center" class="datacell1">&nbsp;</td>
								</tr>
							</c:if>
							<c:forEach items="${loanFeaturesForm.userInfoCollection}" var="planSponsorUser" varStatus="planSponsorUserStatus">
								<c:forEach items="${planSponsorUser.contractPermissions}" var="permissions" varStatus="perstatus"> 
								<tr valign="top">												
									<td class = "${(planSponsorUserStatus.count % 2 == 0) ? 'datacell1' : 'datacell2'}">&nbsp;&nbsp;${planSponsorUser.lastName} &nbsp;${planSponsorUser.firstName}&nbsp;(${permissions.role.displayName})</td>
								</c:forEach>
									<td width="1" class="homedataborder">
									<img src="/assets/unmanaged/images/s.gif" height="1" width="1">
									</td>									
									<c:forEach items="${planSponsorUser.contractPermissions}" var="permissions" varStatus="perstatus"> 
									<td align="center" class = "${(planSponsorUserStatus.count % 2 == 0) ? 'datacell1' : 'datacell2'}">								
										<c:if test="${permissions.initiateLoans}">																
										<img src="/assets/unmanaged/images/CheckMark.gif" width="16" height="18">									
										</c:if>
									</td>
									<td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							
									<td align="center" valign="top" class = "${(planSponsorUserStatus.count % 2 == 0) ? 'datacell1' : 'datacell2'}">
										<c:if test="${permissions.reviewLoans}">
										<img src="/assets/unmanaged/images/CheckMark.gif" width="16" height="18">
										</c:if>
									</td>
									<td width="1" class="homedataborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								
									<td align="center" class = "${(planSponsorUserStatus.count % 2 == 0) ? 'datacell1' : 'datacell2'}">
										<c:if test="${permissions.signingAuthority}">
										<img src="/assets/unmanaged/images/CheckMark.gif" width="16" height="18">
										</c:if>
									</td>
									</c:forEach>
								</tr>
							</c:forEach>
							
							
						<c:if test="${not empty loanFeaturesForm.tpaFirmId}">
							<tr valign="top">
								<td class="datacell1"><b>TPA Firm(${loanFeaturesForm.tpaFirmName})</b></td>
								<td class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td align="center" class="datacell1">
								<c:if test="${loanFeaturesForm.tpaFirmCanInitiate}">
								<img src="/assets/unmanaged/images/CheckMark.gif" width="16" height="18">
								</c:if>
								</td>
								
								<td class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td align="center" valign="top" class="datacell1">
								<c:if test="${loanFeaturesForm.tpaFirmCanReview}">
								<img src="/assets/unmanaged/images/CheckMark.gif" width="16" height="18">
								</c:if>
								</td>
								<td class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td align="center" class="datacell1">
								<c:if test="${loanFeaturesForm.tpaFirmCanSign}">
								<img src="/assets/unmanaged/images/CheckMark.gif" width="16" height="18">
								</c:if>
								</td>
							</tr>
						</c:if>

						</table>
						</td>
						<td width="1" class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					</tr>
					<tr>
						<td colspan="3" class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					</tr>
				</table>
				<p>
                <table border="0" cellpadding="0" cellspacing="0"
                    width="500">
				<tr><td><content:getAttribute beanName="layoutPageBean"
					attribute="body2" /></td></tr>
				</table>
				</p>
				<table class="box" border="0" cellpadding="0" cellspacing="0"
					width="500">
					<tr>

						<td class="tableheadTD1"><b>&nbsp;<content:getAttribute
							beanName="vesting" attribute="text" /> </b></td>
						<td width="132" align="right" class="tablehead">&nbsp;</td>
					</tr>
				</table>
				<table class="box" border="0" cellpadding="0" cellspacing="0"
					width="500">
					<tr>
						<td width="1" class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						<td>
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr valign="top">
								<td colspan="17" class="datacell1"></td>
							</tr>
							<tr valign="top">
								<td colspan="17" class="tablesubhead"><b>General
								vesting provisions </b></td>
								<c:if test="${(!(loanFeaturesForm.planDataUi.planData.isCalculateVestingEnabled)) or 
	    							(loanFeaturesForm.planDataUi.planData.isCalculateVestingEnabled and loanFeaturesForm.planDataUi.planData.isAnyFieldRequiredToCalculateVestingMissing)}">
        							<div class="evenDataRow">
         							<div class="data">
            						<c:choose>
              							<c:when test="${loanFeaturesForm.planDataUi.planData.isCalculateVestingEnabled}">
                						<c:if test="${loanFeaturesForm.planDataUi.planData.isAnyFieldRequiredToCalculateVestingMissing}">
                  						<content:getAttribute beanName="calculateVestingSelected" attribute="text"/>
                						</c:if>
              							</c:when>
              						<c:otherwise>
               				 			<content:getAttribute beanName="calculateVestingNotSelected" attribute="text"/>
             				 		</c:otherwise>
            						</c:choose>          
          							</div>
       							 </div>
     						 </c:if>
							</tr>
							<div class="evenDataRow">     
					          <tr >
    					        <td colspan="3" class="datacell1">Vesting service crediting
								method</td>
								<td colspan="13" class = "datacell1">
								 <ps:displayDescription collection="${vestingServiceCreditMethods}" keyName="code" keyValue="description" key="${loanFeaturesForm.planDataUi.planData.vestingServiceCreditMethod}"/>
								 </td>
					          </tr>
      
					      </div>												
							
							<tr valign="top">
								<td colspan="3" class="datacell2">Vesting hours of service
								(if applicable)</td>
								<td width="1" class="homedataborder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td colspan="13" class="datacell2">
								<fmt:formatNumber groupingUsed="true" value="${loanFeaturesForm.planDataUi.planData.hoursOfService}"/>
								</td>
							</tr>
							<tr valign="top">
								<td colspan="17" class="tablesubhead"><b>Vesting
								schedule </b></td>
							</tr>
							<tr>
							<td colspan="17">
							<div>
								<table class="vestingSchedule">
								<thead class="evenDataRow">
									<th style="border-left-width: 0;" />
									<th />
									<th colspan="8" style="border-right-width: 0;">Completed years of service</th>									
								</thead>
								<thead class="oddDataRow">
									<th style="border-left-width: 0; border-top-width: 1px;">Money Type</th>									
									<th style="border-top-width: 1px;">Vesting Schedule</th>
									<c:forEach begin="0" end="${scheduleConstants.YEARS_OF_SERVICE}" var="year" varStatus="yearStatus">																				
										<th style="border-right-width: ${yearStatus.last ? '0' : '1px'}; border-top-width: 1px;">${year}</th>
									</c:forEach>
								 </thead>
								 <c:choose>
								 	<c:when test="${empty  loanFeaturesForm.planDataUi.planData.vestingSchedules}">										
										<tr class="oddDataRow"> 
											<td class="textData" colspan="${scheduleConstants.YEARS_OF_SERVICE + 3}" style="border-left-width: 0; border-right-width: 0;">									
												<content:getAttribute beanName="noMoneyTypesText" attribute="text"/> 
											</td> 
										</tr> 
									</c:when> 
									<c:otherwise>
										<c:forEach items="${loanFeaturesForm.planDataUi.planData.vestingSchedules}" var="vestingSchedule" varStatus="vestingScheduleStatus"> 										
											<tr class="${(vestingScheduleStatus.count % 2 == 0) ? 'oddDataRow' : 'evenDataRow'}"> 
												<td class="textData" style="border-left-width: 0;">
												 	<ps:fieldHilight name="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].vestingSchedule.vestingScheduleType" singleDisplay="true" displayToolTip="true" className="errorIcon"/>
											 		 <span onmouseover="Tip('${vestingSchedule.moneyTypeLongName}&nbsp;(${vestingSchedule.moneyTypeShortName})')" 	onmouseout="UnTip()"> ${vestingSchedule.moneyTypeShortName} 
													</span>
												</td>
											<td class="textData" nowrap="nowrap">
											${vestingSchedule.vestingScheduleDescription}
											</td>
											<c:forEach items="${vestingSchedule.schedules}" var="vestedAmount" varStatus="vestedAmountStatus">											
												<td class="numericData" style="border-right-width: ${vestedAmountStatus.last ? '0' : '1px'}" nowrap="nowrap">
													<ps:fieldHilight name="planDataUi.vestingSchedules[${vestingScheduleStatus.index}].schedules[${vestedAmountStatus.index}].amount" singleDisplay="true" displayToolTip="true" />
														 <c:if test="${not empty vestingSchedule.vestingScheduleDescription}">
															<fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${scheduleConstants.PERCENTAGE_SCALE}"value="${vestedAmount.amount}" />${empty vestedAmount.amount ? '' : '%'}
	                        							</c:if>
                        						</td>
											</c:forEach>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</table>
							</div>
							</td>
							</tr>
						</table>
						</td>
						<td width="1" class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						<!-- end main content table -->
					</tr>

					<tr>
						<td colspan="3" class="boxborder"><img
							src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					</tr>

				</table>

				</td>
			</tr>
		</table>		
		</body>
		
		<br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="5%">&nbsp;</td>
				<td width="95%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
			</tr>
		</table>
		<br>
	</html>

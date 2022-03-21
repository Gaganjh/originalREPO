<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>
<un:useConstants var="scheduleConstants" className="com.manulife.pension.service.contract.valueobject.VestingSchedule" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<c:set var="noticePlanCommonVO" value="${sessionScope.noticePlanCommonVO}" />


<c:set var="noticePlanDataVO" value="${sessionScope.noticePlanDataVO}" />

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<content:contentBean contentId="<%=ContentConstants.NO_VESTING_SCHEDULE_AVAILABLE%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="noVestingScheduleAvailable" />
<content:contentBean contentId="<%=ContentConstants.VESTING_TABLE_INTRODUCTION%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="vestingTableIntro" />

<hr style="margin-top: 0px;" />
<table class="datatable" style="width: 729px;">
							<tr>
                              <td colspan="10" width="100%" class="datacell1">                              
                                  <b>Employer Contributions and Vesting</b><br/>
                                <content:getAttribute id="vestingTableIntro" attribute="text"/>
<c:if test="${not empty noticePlanCommonVO.vestingSchedules}">
                                <div class="borderedDataBox" id="vestingSchedulData" style="width: 722px;">
                                  <%-- We are using inline styles to remove leftmost and rightmost borders to prevent double width (containing div has a border) --%>
                                  <table class="vestingSchedule">
                                    <thead class="evenDataRow">
                                      <th style="border-left-width: 0;"/>
                                      <th/>
                                      <th/>
                                      <th colspan="7" style="border-right-width: 0;">Completed years of service</th>
                                    </thead>
                                    <thead class="evenDataRow">
                                      <th style="border-left-width: 0; border-top-width: 1px;">Money Type</th>
                                      <th style="border-left-width: 0; border-top-width: 1px;">Exclude</th>
                                      <th style="border-top-width: 1px;">Vesting Schedule</th>
                                      <c:forEach begin="0" end="${scheduleConstants.YEARS_OF_SERVICE}" var="year" varStatus="yearStatus">
                                        <th style="border-right-width: ${yearStatus.last ? '0' : '1px'}; border-top-width: 1px;">${year}</th>
                                      </c:forEach>
                                    </thead>
<input type="hidden" name="noticePlanDataForm.excludeCount" class="excludeCount" value="${noticePlanDataForm.excludeCount}"/>

									
                                        <c:forEach items="${noticePlanCommonVO.vestingSchedules}"  var="vestingSchedule" varStatus="vestingScheduleStatus" >

                                         <tr class="${(vestingScheduleStatus.count % 2 == 0) ? 'evenDataRow' : 'oddDataRow'}  forIterate">
                                            <td class="textData" styleId="moneyTypeExcludeObject[${vestingScheduleStatus.count-1}].moneyTypeShortName style="border-left-width: 0;" onmouseover="Tip('${vestingSchedule.moneyTypeLongName}&nbsp;(${vestingSchedule.moneyTypeShortName})')" onmouseout="UnTip()">
                                              ${vestingSchedule.moneyTypeShortName}

<input type="hidden" name="noticePlanDataForm.noticePlanDataVO.noticePlanVestingMTExcludeVOList[${vestingScheduleStatus.count-1}].moneyTypeId" class="moneyTypeName" value="${vestingSchedule.moneyTypeShortName}"/>



                                            </td>
<input type="hidden" name="noticePlanDataForm.noticePlanDataVO.noticePlanVestingMTExcludeVOList[${vestingScheduleStatus.count-1}].excludeInd" class="excludeIndVal" id="excludeIndValue${vestingScheduleStatus.count-1}" value="noticePlanDataForm.noticePlanDataVO.noticePlanVestingMTExcludeVOList[${vestingScheduleStatus.count-1}].excludeInd"/>


                                          <!-- Code to get the exclude flag for the Money types -->
                                             <td class="textData" align ="center">

				<!-- Code to get the SH exclude flag for the Money types START-->
									
 
									<c:set var="isExcluded" value="false" />
									
									 <c:forEach
										items="${noticePlanDataVO.noticePlanVestingMTExcludeVOShList}"
										var="noticePlanVestingMTExclude"
										varStatus="vestingScheduleStatus1">
											
										<c:if
											test="${fn:trim(noticePlanVestingMTExclude. moneyTypeId) eq fn:trim(vestingSchedule.moneyTypeShortName)}">
											<input type="checkbox"
												id='moneyTypeExcludeCheckBox${vestingScheduleStatus.count-1}'
												disabled="${disableFields}"
												name="excludedMoneyTypename"
												value='${fn:trim(vestingSchedule.moneyTypeShortName)}'
												Class="moneyTypeIDIndval" checked />
											<c:set var="isExcluded" value="true" />
										</c:if>
									</c:forEach>
									
									 <c:if test="${not isExcluded}">
										<input type="checkbox"
												id='moneyTypeExcludeCheckBox${vestingScheduleStatus.count-1}'
												disabled="${disableFields}"
												name="excludedMoneyTypename"
												value='${fn:trim(vestingSchedule.moneyTypeShortName)}'
												Class="moneyTypeIDIndval" />
									</c:if>
								
									
								<!-- Code to get the SH exclude flag for the Money types END-->
								
								  	 	</td>
										  					  	
                                            <td class="textData">
                                              ${vestingSchedule.vestingScheduleDescription}
                                            </td>
                                            <c:forEach items="${vestingSchedule.schedules}" var="vestedAmount" varStatus="vestedAmountStatus">
                                              <td class="numericData" style="border-right-width: ${vestedAmountStatus.last ? '0' : '1px'}">
                                                <c:if test="${not empty vestingSchedule.vestingScheduleDescription}">
                                                  <fmt:formatNumber type="NUMBER" minFractionDigits="0" maxFractionDigits="${scheduleConstants.PERCENTAGE_SCALE}" value="${vestedAmount.amount}"/>${empty vestedAmount.amount ? '' : '%'}
                                                </c:if>
                                              </td>
                                             
                                            </c:forEach>
                                          </tr>
                                        </c:forEach>
                                         	
                                            
                                       
                                  </table>
                                </div> 
</c:if>
                               <%-- Need to include the content from CMA for this message--%>
<c:if test="${empty noticePlanCommonVO.vestingSchedules}">
                                <p><img src="/assets/unmanaged/images/warning2.gif"> <content:getAttribute id="noVestingScheduleAvailable" attribute="text"/></p>
</c:if>
                              </td>
                            </tr>
                            </table>

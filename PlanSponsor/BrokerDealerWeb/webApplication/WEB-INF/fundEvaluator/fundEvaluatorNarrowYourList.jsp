<%-- tag libraries --%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- imports --%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.platform.web.content.CommonContentConstants"%>
  <%@ page import="com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorForm"%>
<jsp:useBean id="fundEvaluatorForm" scope="request" type="com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorForm" />

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<un:useConstants var="fundEvalConstants" className="com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorConstants"/>
		         
<content:contentBean contentId="<%=BDContentConstants.WARRANTY_TOPFUNDS_DESC%>"
					 type="<%=CommonContentConstants.TYPE_MESSAGE%>"
					 id="warrantyTopFundsDesc"/>

<content:contentBean contentId="<%=BDContentConstants.TOPFUNDS_DESC%>"
					 type="<%=CommonContentConstants.TYPE_MESSAGE%>"
					 id="topFundsDesc"/>

<content:contentBean contentId="<%=BDContentConstants.NO_SELECTION_DESC%>"
					 type="<%=CommonContentConstants.TYPE_MESSAGE%>"
					 id="noSelectionDesc"/>

<content:contentBean contentId="<%=BDContentConstants.WARNING_NAVIGATES_AWAY_FROM_FUNDEVALUATOR%>"
			        type="<%=CommonContentConstants.TYPE_MESSAGE%>"
			        id="navigateAwayWarningContent"/>
			        			
<c:set var="intro1" value="${layoutPageBean.introduction1}"/>
<c:set var="intro2" value="${layoutPageBean.introduction2}"/>		 
<c:set var="benifits" value="${layoutPageBean.layer1.text}"/>
<SCRIPT type=text/javascript>

   function doNext() {
			document.fundEvaluatorForm.action="?action=" + investmentOptionsSelection + "&" + "page=";
			document.fundEvaluatorForm.submit();
			
	}

	function doPrevious() {
		document.fundEvaluatorForm.action="?action=" + selectCriteria + "&" + "page=" + navigateToPrevious; 
		document.fundEvaluatorForm.submit();
	}


  $(document).ready(function() {
	  $("select.multiselect-plugin").multiselect({ selectedList : 0});
	 	$("button.ui-multiselect").css('width','230px');
	 	
	 	if( document.fundEvaluatorForm.contractNumber != undefined && document.fundEvaluatorForm.compulsoryFunds != undefined){
	 		if(document.fundEvaluatorForm.compulsoryFunds.value =='mmf'){
	 			$("#svfDropdown").css({display : "none"});
	 		}else if(document.fundEvaluatorForm.compulsoryFunds.value =='svf'){
	 			$("#mmfDropdown").css({display : "none"});
	 		}
	 	}else if( document.fundEvaluatorForm.compulsoryFunds != undefined){
	 		if($("input[name='compulsoryFunds']:checked").val() =='mmf'){
	 			$("#svfDropdown").css({display : "none"});
	 		}else if($("input[name='compulsoryFunds']:checked").val() =='svf'){
	 			$("#mmfDropdown").css({display : "none"});
	 		}else if($("input[name='compulsoryFunds']:checked").val() === undefined){
	 			$("#mmfDropdown").css({display : "none"});
	 			$("#svfDropdown").css({display : "none"});
	 		}
	 	}
	 	
	$("#mmfRadio").click(function() {

			if ($("#mmfDropdown").css("display") == 'block') {
				
			} else {
				$("#mmfDropdown").css({display : "block"});
				$("#svfDropdown").css({display : "none"});
				$("#svfFunds").val("");
			}

		});

		$("#svfRadio").click(function() {
			if ($("#svfDropdown").css("display") == 'block') {

			} else {
				$("#svfDropdown").css({display : "block"});
				$("#mmfDropdown").css({display : "none"});
				 $("#mmfFunds").multiselect("uncheckAll");
			}
		});

	});
</SCRIPT>


<bd:form action="/do/fundEvaluator/" modelAttribute="fundEvaluatorForm" name="fundEvaluatorForm" >

	<div id="contentOuterWrapper">
			<div id="contentWrapper"> 
				<%--Display the benefit section if the content is not empty --%>     
				<c:if test="${!empty benifits}">
					<div id="rightColumn1">
						<%-- FundEvaluator benefits layer --%>
						${layoutPageBean.layer1.text}		
					</div>
				</c:if>
				<div id="contentTitle">
					<content:getAttribute id="layoutPageBean" attribute="name"/>					
				</div>				
				<c:if test="${!empty intro1}">
					<p id="contentIntro">
						<content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
					</p>
				</c:if>
				<c:if test="${!empty intro2}">
					<p id="contentIntro">
						<content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
					</p>
				</c:if>		
				<c:if test="${!empty benifits}">
					<br class="clearFloat" /><br class="clearFloat" />
				</c:if>		
				<!-- error message -->		
				<report:formatMessages scope="session"/>
			
				<div id="contentievalfull">
					<div id="fundEvaluatorButtons">
						<input type="button" class="blue-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='blue-btn next'" name="Next" value="Next" onclick="doNext()">
						<input type="button" class="grey-btn back" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn back'" name="Back" value="Back" onclick="doPrevious()">
					</div>
					
				<div style="CLEAR: both">
				
					<ul class="proposal_nav_menu">
						<li><a id="step1">Step 1</a></li>
						<li><a id="step2">Step 2</a></li>
						<li><a  class="selected_link"><span class="step_number">Step 3:</span><span class="step_caption"> Select evaluation framework</span>
							</a>
						</li>         
						<li><a id="step4">Step 4</a></li>
						<li><a id="step5">Step 5</a></li>
					</ul>

					<div class="selection_mode_step3"> 
						<div id="right">
						
							<div id="choose2">
								<h1><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></h1>
								<div id="box1">

                  					<div class="asset-allocation-grouping">
                    				Target Date Portfolios:<br/>
                    					<form:select cssClass="multiselect-plugin" name="fundEvaluatorForm" path="lifecycleFundSuites" multiple="multiple">
                    						
                    						<c:choose>
												<c:when test="${empty fundEvaluatorForm.contractNumber}">
                    								<c:if test="${not empty fundEvaluatorForm.fundFamilyList}">
                    									<c:forEach items="${fundEvaluatorForm.fundFamilyList}" var="fundFamilyId" varStatus="fundFamilyIndex">
															<form:option value="${fundFamilyId.familyCode}"> ${fundFamilyId.familyDescription}</form:option>
														</c:forEach>
													</c:if>
												</c:when>
											<c:otherwise>
												<c:if test="${not empty fundEvaluatorForm.fundFamilyList}">
													<c:forEach items="${fundEvaluatorForm.fundFamilyList}" var="fundFamilyId" varStatus="fundFamilyIndex">
														<form:option value="${fundFamilyId.familyCode}" disabled="${fundFamilyId.contractSelected}"> ${fundFamilyId.familyDescription}</form:option>
													</c:forEach>
												</c:if>
											</c:otherwise>
											</c:choose>				
                    					</form:select>
                  					</div>
                  					<div class="asset-allocation-grouping">
                    				Target Risk Portfolios:<br/>
                    					<form:select cssClass="multiselect-plugin" name="fundEvaluatorForm" path="lifestyleFundSuites" multiple="multiple">
                    						<c:if test="${not empty fundEvaluatorForm.lifestyleFamilyList}">
                    							<c:forEach items="${fundEvaluatorForm.lifestyleFamilyList}" var="fundFamilyId" varStatus="fundFamilyIndex">
                    								<c:choose>
														<c:when test="${empty fundEvaluatorForm.contractNumber}">
															<c:if test="${fundEvaluatorForm.nml or fundFamilyId.familyCode != fundEvalConstants.RUSSELL_FUND_FAMILY_CODE}">
																<form:option  value="${fundFamilyId.familyCode}"> ${fundFamilyId.familyDescription}</form:option>
															</c:if>
														</c:when>
													<c:otherwise>
														<c:if test="${fundEvaluatorForm.nml or fundFamilyId.familyCode != fundEvalConstants.RUSSELL_FUND_FAMILY_CODE}">
															<form:option  value="${fundFamilyId.familyCode}" disabled="${fundFamilyId.contractSelected}"> ${fundFamilyId.familyDescription}</form:option>
														</c:if>
													</c:otherwise>
												</c:choose>	
												</c:forEach>
											</c:if>
                    						</form:select>
                  						</div>
                 					</div>
							</div>
							
							<div id="choose3">
							<h1><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></h1>
								<div id="box1">							
									<c:choose>
										<c:when test="${empty fundEvaluatorForm.contractNumber}">
											<p style="margin-bottom: 0px;padding-bottom: 10px;padding-top: 0px;">
												<label class="rightBoxLabel"><form:radiobutton
														name="fundEvaluatorForm" path="compulsoryFunds"
														value="mmf" id="mmfRadio" />Select your Money Market Fund(s): </label>
											</p>
											<p style="display: block; margin-top: 0px;" id="mmfDropdown">
												<form:select cssClass="multiselect-plugin" name="fundEvaluatorForm" path="moneyMarketFunds" multiple="multiple" id="mmfFunds">
													<c:forEach items="${fundEvaluatorForm.mmfFundCodeList}" var="fundFamilyId" varStatus="fundFamilyIndex">
														<c:choose>
															<c:when test="${empty fundEvaluatorForm.contractNumber}">
																<form:option value="${fundFamilyId}"> ${fundEvaluatorForm.mmfFundList[fundFamilyId].fundName}</form:option>
															</c:when>
															<c:otherwise>
																<form:option value="${fundFamilyId}" disabled="${fundEvaluatorForm.contractSelectedSVFAndMMFFunds[fundFamilyId]}"> ${fundEvaluatorForm.mmfFundList[fundFamilyId].fundName}</form:option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</form:select>
											</p>

											<p style="margin-bottom: 0px;padding-bottom: 10px;padding-top: 0px;">
												<label class="rightBoxLabel" > <form:radiobutton
														name="fundEvaluatorForm" path="compulsoryFunds"
														value="svf" id="svfRadio" /> Select one Stable Value Fund: 
												</label>
											</p>
											<p style="display: block; margin-top: 0px" id="svfDropdown">
												
												<c:choose>
												
												<c:when test="${empty fundEvaluatorForm.contractNumber}">
														 <form:select   path="stableValueFunds"  id="svfFunds" cssStyle="width: 230px; " cssClass="ui-multiselect ui-widget" >
														<option value="">Select option</option>
														
														  <c:forEach items="${fundEvaluatorForm.svfFundCodeList}" var="fundFamilyId" varStatus="fundFamilyIndex">
																<form:option value="${fundFamilyId}"> ${fundEvaluatorForm.svfFundList[fundFamilyId].fundName}  </form:option>
													    </c:forEach>  
														</form:select> 
												</c:when>
												<c:otherwise>
														<form:select name="fundEvaluatorForm" path="stableValueFunds"  id="svfFunds" disabled="true" cssStyle="width: 230px;" cssClass="ui-multiselect ui-widget" >
														<option value="">Select option</option>
														<c:forEach items="${fundEvaluatorForm.svfFundCodeList}" var="fundFamilyId" varStatus="fundFamilyIndex">
																	<form:option value="${fundFamilyId}"> ${fundEvaluatorForm.svfFundList[fundFamilyId].fundName}</form:option>
														</c:forEach>
												</form:select>
												</c:otherwise>
												</c:choose>
											</p>
										</c:when>

										<c:otherwise>										
											<c:if test="${!fundEvaluatorForm.SVFSelected && !fundEvaluatorForm.MMFSelected}">	
												<p style="margin-bottom: 0px;padding-bottom: 10px;padding-top: 0px;">
													<label class="rightBoxLabel">
														<form:radiobutton  name="fundEvaluatorForm" path = "compulsoryFunds" value="mmf" id="mmfRadio" />			
														 Select your Money Market Fund(s):
													</label>
													
												</p>	
												<p style="margin-top: 0px" id="mmfDropdown">
												<form:select cssClass="multiselect-plugin" name="fundEvaluatorForm" path="moneyMarketFunds" multiple="multiple" id="mmfFunds">
													<c:forEach items="${fundEvaluatorForm.mmfFundCodeList}" var="fundFamilyId" varStatus="fundFamilyIndex">
														<c:choose>
															<c:when test="${empty fundEvaluatorForm.contractNumber}">
																<form:option value="${fundFamilyId}"> ${fundEvaluatorForm.mmfFundList[fundFamilyId].fundName}</form:option>
															</c:when>
															<c:otherwise>
																<form:option value="${fundFamilyId}" disabled="${fundEvaluatorForm.contractSelectedSVFAndMMFFunds[fundFamilyId]}"> ${fundEvaluatorForm.mmfFundList[fundFamilyId].fundName}</form:option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</form:select>
											</p>			
												<p style="margin-bottom: 0px;padding-bottom: 10px;padding-top: 0px;">
													<label class="rightBoxLabel">
														<form:radiobutton   name="fundEvaluatorForm" path = "compulsoryFunds" value="svf"  id="svfRadio" />
														Select one Stable Value Fund:
													</label>
												</p>	
												<p style="margin-top: 0px" id="svfDropdown">
												<c:choose>
												<c:when test="${empty fundEvaluatorForm.contractNumber}">
														<form:select name="fundEvaluatorForm" path="stableValueFunds"  id="svfFunds" cssStyle="width: 230px;" cssClass="ui-multiselect ui-widget" >
														<option value="">Select option</option>
														<c:forEach items="${fundEvaluatorForm.svfFundCodeList}" var="fundFamilyId" varStatus="fundFamilyIndex">
																<form:option value="${fundFamilyId}"> ${fundEvaluatorForm.svfFundList[fundFamilyId].fundName}</form:option>
													</c:forEach>
														</form:select>
												</c:when>
												<c:otherwise>
														<form:select name="fundEvaluatorForm" path="stableValueFunds"  id="svfFunds" disabled="true" cssStyle="width: 230px;" cssClass="ui-multiselect ui-widget" >
														<option value="">Select option</option>
														<c:forEach items="${fundEvaluatorForm.svfFundCodeList}" var="fundFamilyId" varStatus="fundFamilyIndex">
																	<form:option value="${fundFamilyId}"> ${fundEvaluatorForm.svfFundList[fundFamilyId].fundName}</form:option>
														</c:forEach>
												</form:select>
												</c:otherwise>
												</c:choose>
											</p>													
											</c:if>
											<c:if test="${fundEvaluatorForm.SVFSelected || fundEvaluatorForm.MMFSelected}">																		
												<p style="margin-bottom: 0px;padding-bottom: 10px;padding-top: 0px;">
													<label class="rightBoxLabel">
														<form:radiobutton name="fundEvaluatorForm" path = "compulsoryFunds" disabled = "true" value="mmf" />			
														 Select your Money Market Fund(s):
													</label>
												</p>	
												<p style="margin-top: 0px" id="mmfDropdown">
												<form:select cssClass="multiselect-plugin" name="fundEvaluatorForm" path="moneyMarketFunds" multiple="multiple" id="mmfFunds">
													<c:forEach items="${fundEvaluatorForm.mmfFundCodeList}" var="fundFamilyId" varStatus="fundFamilyIndex">
														<c:choose>
															<c:when test="${empty fundEvaluatorForm.contractNumber}">
																<form:option value="${fundFamilyId}" > ${fundEvaluatorForm.mmfFundList[fundFamilyId].fundName}</form:option>
															</c:when>
															<c:otherwise>
																<form:option value="${fundFamilyId}" disabled="${fundEvaluatorForm.contractSelectedSVFAndMMFFunds[fundFamilyId]}"> ${fundEvaluatorForm.mmfFundList[fundFamilyId].fundName}</form:option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</form:select>
											</p>				
												<p style="margin-bottom: 0px; padding-bottom: 10px; padding-top: 0px;">
													<label class="rightBoxLabel">
														<form:radiobutton  name="fundEvaluatorForm" path = "compulsoryFunds" disabled = "true" value="svf"  />													
														Select one Stable Value Fund:
													</label>
												</p>
												<p style="margin-top: 0px" id="svfDropdown">
												<c:choose>
												<c:when test="${empty fundEvaluatorForm.contractNumber}">
														<form:select  name="fundEvaluatorForm" path="stableValueFunds"  id="svfFunds" cssStyle="width: 230px;" cssClass="ui-multiselect ui-widget" >
														<option value="">Select option</option>
														<c:forEach items="${fundEvaluatorForm.svfFundCodeList}" var="fundFamilyId" varStatus="fundFamilyIndex">
																<form:option value="${fundFamilyId}"> ${fundEvaluatorForm.svfFundList[fundFamilyId].fundName}</form:option>
													    </c:forEach>
														</form:select>
												</c:when>
												<c:otherwise>
														<form:select name="fundEvaluatorForm" path="stableValueFunds"  id="svfFunds" disabled="true"  cssStyle="width: 230px;" cssClass="ui-multiselect ui-widget" >
														<option value="">Select option</option>
														<c:forEach items="${fundEvaluatorForm.svfFundCodeList}" var="fundFamilyId" varStatus="fundFamilyIndex">
																	<form:option value="${fundFamilyId}"> ${fundEvaluatorForm.svfFundList[fundFamilyId].fundName}</form:option>
														</c:forEach>
												</form:select>
												</c:otherwise>
												</c:choose>
											</p>	
											</c:if>								
										</c:otherwise>
									</c:choose>						
								</div>
							</div>
						</div>
						
					<div id="choose1">
						<h1><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></h1>
						<div id="box1" >
							<p><label class="leftboxHead">
							  <form:radiobutton name="fundEvaluatorForm" path="preSelectFunds" value="1" />	
							  <strong> Top Fund in every asset class</strong><br /></label>
							 <span class="leftBoxContent"><content:getAttribute beanName="topFundsDesc" attribute="text" filter="true"/></span>
							</p>
							<p>
							  <label class="leftboxHead"><form:radiobutton name="fundEvaluatorForm" path="preSelectFunds" value="2" />	
								<strong>Do not pre-select any Funds</strong>
							  </label>
							  <br/>
							  <span class="leftBoxContent">
								<content:getAttribute beanName="noSelectionDesc" attribute="text" filter="true"/>
							  </span>
							</p>
						</div>
					</div>	 
				</div>
			
				<BR/>
				<div id="fundEvaluatorButtons">
					<input type="button" class="blue-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='blue-btn next'" name="Next" value="Next" onclick="doNext()">
					<input type="button" class="grey-btn back" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn back'" name="Back" value="Back" onclick="doPrevious()">
				</div>
			</div>	
			</div>
		</div>
	</div>	
<!-- To retain some of  the step 5 values  -->
<form:hidden path="previousAction" value="${fundEvalConstants.FORWARD_NARROW_YOUR_LIST}" /><%--  input - name="fundEvaluatorForm" --%>
<input type="hidden" name="navigateAwayWarning" value="${navigateAwayWarningContent.text}"/>
<form:hidden path="includedOptItem1" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem3" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem4" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem5" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem6" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem7" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="nml" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="newPlanClosedFund" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="existingClientClosedFund" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includeGIFLSelectFunds" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="dataModified" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="edwardJones" /><%--  input - name="fundEvaluatorForm" --%>
<input type="hidden" name=stableValueFunds" />
<form:hidden path="merrillFirmFilter"/>
</bd:form>

<%-- Page specific footer elements --%>
<layout:pageFooter/>
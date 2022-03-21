<%-- tag libraries --%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%-- imports --%>
<%@ page import="com.manulife.pension.platform.web.content.CommonContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<un:useConstants var="fundEvalConstants" className="com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorConstants"/>
			        
<content:contentBean contentId="<%=BDContentConstants.WARNING_NAVIGATES_AWAY_FROM_FUNDEVALUATOR%>"
        type="<%=CommonContentConstants.TYPE_MESSAGE%>"
        id="navigateAwayWarningContent"/>			    


<c:set var="intro1" value="${layoutPageBean.introduction1}"/>
<c:set var="intro2" value="${layoutPageBean.introduction2}"/>
<c:set var="benifits" value="${layoutPageBean.layer1.text}"/>

<bd:form method="POST" action="/do/fundEvaluator/" modelAttribute="fundEvaluatorForm" name="fundEvaluatorForm">

	
	
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
		<%-- 
		Removing the following br tags to reduce the space and assume that the benefit section
		content will never be populated
		--%>
		<%--<br class="clearFloat" /><br class="clearFloat" />--%>
		
		<c:if test="${!empty benifits}">
			<br class="clearFloat" /><br class="clearFloat" />
		</c:if>
		<%-- display error messages --%>		
		<report:formatMessages scope="session"/>
		
		<div id="contentievalfull">
		
			<div class="nextButton" style="DISPLAY:none;" id="enabledNextButton">
				<div id="fundEvaluatorButtons">
					<input type="button" class="blue-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='blue-btn next'" name="Next" value="Next" onclick="doNext()">
				</div>
			</div>
		
			<div class="nextButton" style="DISPLAY:none;" id ="disabledNextButton">
				<div id="fundEvaluatorButtons">
					<input type="button" class="disabled-grey-btn back" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='disabled-grey-btn back'" name="Next" value="Next" onclick="doNext()" disabled>
				</div>     
			</div>
			
			<div style="CLEAR: both">
				<ul class="proposal_nav_menu">
					<li><a  class="selected_link"><span class="step_number">Step 1:</span><span class="step_caption"> Select your client</span></a> </li>
					<li><a id="step2">Step 2</a></li>
					<li><a id="step3">Step 3</a></li>
					<li><a id= "step4">Step 4</a></li>
					<li><a id="step5"> Step 5</a></li>
				 </ul>
				 
				<div class="selection_mode">
					<div class="centerTitleStep1">I want to evaluate investments for... </div>
					<div id="TabbedPanels1" class="TabbedPanels"> 
						 <ul class="TabbedPanelsTabGroup">
							<li class="TabbedPanelsTab" tabindex="0">A New Plan</li>
							<li class="TabbedPanelsTabSelectedOr">or</li>
							<li class="TabbedPanelsTab" tabindex="0">An Existing Client</li>
						</ul>
						<div class="TabbedPanelsContentGroup">
							<div class="TabbedPanelsContent">
								<div id="newplaninputArrow"></div>
								<div id="newplaninput">			
										<table width="100%" border="0" cellspacing="0" cellpadding="3">
										<!-- 1st row fund menu / class -->
											<tr>
												<td width="45%"><label class="coversheet">
													<strong>Fund Menu</strong><br>
<form:select path="fundMenuSelected" disabled="true" >
														<form:option value="ALL" >All Funds</form:option> 
														 
</form:select>
													</label>
												</td>
												<td width="55%"><strong class="coversheet">Class</strong>
												<br />
													<c:set var="fundClasses" value="${fundEvaluatorForm.fundClassesList}" />
<form:select path="fundClassSelected" >
														 
														<form:options items="${fundClasses}" itemValue="value" itemLabel="label" />
														
</form:select>
												</td>
											</tr>
											
											<tr>
											  <td>&nbsp;</td>
											  <td>&nbsp;</td>
											</tr>
											
											<tr>
											<!-- 2nd row left column usa/ny funds -->
												<td valign="top">
													<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td valign="top">						  
															<label>
<form:radiobutton onclick="showHide('show');" path="fundUsa" value="USA" />
																<span style= "font-size: 12px; color:#000;">USA Funds</span>
															</label>
														</td>
													</tr>
													
													<tr id="stateRow" <c:if test="${fundEvaluatorForm.fundUsa == 'NY'}"> style="display:none;" </c:if> >
														<td valign="top"><span style= "font-size: 12px; color:#000;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;State</span>
														<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

														<c:set var="states" value="${fundEvaluatorForm.statesList}" />
<form:select path="stateSelected" >
<form:options items="${states}" itemValue="value" itemLabel="label" />
															
</form:select>
														</td>
													</tr>
													
													<tr>
														<td valign="bottom">						  
<form:radiobutton onclick="showHide('hide');" path="fundUsa" value="NY" />
															<span style= "font-size: 12px; color:#000;">NY Funds</span>
														</td>
													</tr>
													</table>
												</td>

									<!-- right column closed/GIFL/EDJ/NML  -->
												<td valign="top">
													<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<!-- display include closed funds -->
													<tr><td>
<form:checkbox path="newPlanClosedFund" />
															<span style= "font-size: 12px; color:#000;">Include closed Funds</span>
														</td>
													</tr>
													
													<!-- display G.I.F.L check box -->
													<tr>
														<td valign="bottom">
<form:checkbox path="includeGIFLSelectFunds" />
																<span style= "font-size: 12px; color:#000;">Include G.I.F.L. Select Funds</span>
														</td>
													</tr>
													
													<tr>
														<td>&nbsp;</td>
													</tr>
													<!-- Display firm filter dropdown -->
<c:if test="${fundEvaluatorForm.showFirmFilter ==true}">
													<tr>
														<td><label class="coversheet">
															<strong>BD Firm Filter</strong><br>
<form:select path="firmFilterSelected" >
																<form:option value="" ></form:option> 
																<%--<form:option value="EDJ" >Edward Jones</html:option> --%> 
																<form:option value="NML">NML</form:option> 
																<form:option value="ML">ML</form:option>
</form:select>
															</label>
														</td>
													</tr>
</c:if>
<c:if test="${fundEvaluatorForm.showFirmFilter !=true}">
<form:hidden path="nml" />
</c:if>
													</table>
												</td>																								
											
											</tr>										
	
											<tr>											  
											  <td colspan="2"><content:getAttribute beanName="layoutPageBean" attribute="body1"/></td>
											</tr>
									  </table>		  
								</div>
							</div>					
							<div class="TabbedPanelsContent"><br /><br /></div>				
							<div class="TabbedPanelsContent">
								<div id="existingplaninputArrow"></div>   <br /><br />                  
								<div id="existingplaninput">
									<table width="100%" border="0" cellspacing="0" cellpadding="3">
										<tr>
											<td width="60%">
												<label class="coversheet">
													<strong>Enter your client's contract number</strong>
													<!-- onChangeContract is used to track the change in contract number -->
<form:input path="contractNumber" maxlength="7" onkeyup="javascript:enableOrDisableNextButton('existingclient');" />
												</label>
											</td>
											<td>&nbsp;</td>
										</tr>	
										<tr>
											<td colspan="2">&nbsp;</td>
										</tr>										
										<tr>
											<td colspan="2">
<form:checkbox path="existingClientClosedFund" />
<span style= "font-size: 12px; color:#000;">&nbsp;Include closed Funds not already selected by the contract</span>

											</td>
										</tr>										
										<tr>
											<td colspan="2"><content:getAttribute beanName="layoutPageBean" attribute="body2"/></td>
										</tr>
									</table>
								</div>    
								<br class="clearFloat" /> 					
							</div>
						</div>
					</div>
				</div>            
				
				<BR/>
				<div class="nextButton" style="DISPLAY:none;" id="enabledBottomNextButton">
					<div id="fundEvaluatorButtons">
						<input type="button" class="blue-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='blue-btn next'" name="Next" value="Next" onclick="doNext()">
					</div>
				</div>
			
				<div class="nextButton" style="DISPLAY:none;" id ="disabledBottomNextButton">
					<div id="fundEvaluatorButtons">
						<input type="button" class="disabled-grey-btn back" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='disabled-grey-btn back'" name="Next" value="Next" onclick="doNext()" disabled>
					</div>     
				</div>
				
			</div>
		</div>
	</div>
	</div>
	
	<!-- To retain the step 3 & step 5 values  -->
<form:hidden path="previousAction" value="${fundEvalConstants.FORWARD_SELECT_YOUR_CLIENT}" /><%--  input - name="fundEvaluatorForm" --%>
<input type="hidden" name="navigateAwayWarning" value="${navigateAwayWarningContent.text}"/>
<form:hidden path="lifeCyclePortfolios" />
<form:hidden path="lifeStylePortfolios" />
		<!-- To track whether contract selected funds or not -->
<form:hidden path="lifecycleFundSuites" />
<form:hidden path="lifestyleFundSuites" />
<form:hidden path="compulsoryFunds" />
<form:hidden path="preSelectFunds" />
<form:hidden path="clientType" />
<form:hidden path="defaultSiteLocation" />
<form:hidden path="companyName" />
	
<form:hidden path="includedOptItem1" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem3" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem4" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem5" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem6" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includedOptItem7" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="dataModified" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="showFirmFilter" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="edwardJones" /><%--  input - name="fundEvaluatorForm" --%>
		
</bd:form>		

<%-- Page specific footer elements --%>
<layout:pageFooter/>

<script type="text/javascript">
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1", {defaultTab:1});
retainSelectedValues();

</script>




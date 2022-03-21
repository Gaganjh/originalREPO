<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.MessageFormat" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm"%>
<%@ page import="com.manulife.pension.util.content.GenericException" %>
<%@ page import="com.manulife.pension.util.content.manager.ContentProperties" %>
<%@ page import="com.manulife.pension.util.content.manager.ContentCacheConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.bd.web.bob.blockOfBusiness.util.BOBColumnsApplicableToTab"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@ page import="com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportVO"%>
 <%@ page import="com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<content:contentBean contentId="<%=BDContentConstants.HOVER_BPS_FEE_ANNUAL_MAX_AMT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="annualMaxHoverText"/>
<content:contentBean contentId="<%=BDContentConstants.HOVER_BPS_FEE_MONTHLY_MIN_AMT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="monthlyMinHoverText"/>        
<c:set var = "annualMaxHoverText"><content:getAttribute beanName='annualMaxHoverText' attribute='text'/></c:set>  
<c:set var = "monthlyMinHoverText"><content:getAttribute beanName='monthlyMinHoverText' attribute='text'/></c:set>  
<c:set var = "replaceParam" value="{0}"/>   

<c:set var = "contractStatusCodePS" value="<%=BDConstants.CONTRACT_STATUS_PS%>"/> 
<c:set var = "contractStatusCodeDC" value="<%=BDConstants.CONTRACT_STATUS_DC%>"/> 
<c:set var = "contractStatusCodePC" value="<%=BDConstants.CONTRACT_STATUS_PC%>"/> 
<c:set var = "contractStatusCodeCA" value="<%=BDConstants.CONTRACT_STATUS_CA%>"/> 
    
 <% BOBColumnsApplicableToTab applicableColumns = (BOBColumnsApplicableToTab)request.getAttribute(BDConstants.APPLICABLE_COLUMNS);
pageContext.setAttribute("applicableColumns",applicableColumns,PageContext.PAGE_SCOPE);
%>
  
<jsp:useBean id="blockOfBusinessForm" scope="session" type="com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm" />


	<script>
			$(document).ready(function(){
				
				var riaTabInd = false;
				var compensationTabInd= false;
				var fiducairyTabInd = false;
				
				// check if any column is sorted
				if($(".riatab a[class^='sort']").length > 0) {
					riaTabInd = true;
				}
				if($(".compensationtab a[class^='sort']").length > 0) {
					compensationTabInd = true;
				}
				if($(".fiduciaryServicesTab a[class^='sort']").length > 0) {
					fiducairyTabInd = true;
				}
				
				// if no columns are sorted, show tab in following order  - Compensation , RIA, Fiduciary
				if(!riaTabInd &&  !compensationTabInd && !fiducairyTabInd) {
					if($("#compensation").length > 0) {
						compensationTabInd = true;
					} else if($("#riafee").length > 0) {
						riaTabInd = true;
					} else if($("#fiduciaryServices").length > 0) {
						fiducairyTabInd = true;
					}
				}
								
				if(compensationTabInd) {
					showCompensationTab();
				} else if (riaTabInd) {
					showRiaTab();
				} else if (fiducairyTabInd) {
					showFiduciaryServicesTab();
				} else {
					$("#subtabfield").attr('colSpan',riafldcnt); 
					$(".legendCls").hide();
					$(".riaLegendCls").hide();
					$(".fiduciarylegendCls").hide();
				}
				
				$("#compensation").click(function(){
					showCompensationTab();
				});
				$("#riafee").click(function(){
					showRiaTab();
				});
				$("#fiduciaryServices").click(function(){
					showFiduciaryServicesTab();
				});
				
				function showCompensationTab() {
					$('#compensation').css('cursor', 'default');
					$('#riafee').css('cursor', 'pointer');
					$("#subtabfield").attr('colSpan',compfldcnt); 
					$(".riatab").hide();
					$(".riaLegendCls").hide();
					$(".compensationtab").show();
					$(".legendCls").show();
					$('#compensation').css('background-color','white');
					$("#riafee").css('background-color','#DEDED8');
					$('#fiduciaryServices').css('cursor','pointer');
					$("#fiduciaryServices").css('background-color','#DEDED8');
					$(".fiduciaryServicesTab").hide();
					$(".fiduciarylegendCls").hide();
				}
				
				function showRiaTab() {
					$('#riafee').css('cursor', 'default');
					$('#compensation').css('cursor', 'pointer');
					$("#subtabfield").attr('colSpan',riafldcnt); 
					$(".compensationtab").hide();
					$(".legendCls").hide();
					$(".riatab").show();
					$(".riaLegendCls").show();
					$('#riafee').css('background-color','white');
					$("#compensation").css('background-color','#DEDED8');
					$("#fiduciaryServices").css('background-color','#DEDED8');
					$('#fiduciaryServices').css('cursor','pointer');
					$(".fiduciaryServicesTab").hide();
					$(".fiduciarylegendCls").hide();
				}
				
				function showFiduciaryServicesTab() {
					$('#riafee').css('cursor', 'pointer');
					$('#compensation').css('cursor', 'pointer');
					$("#subtabfield").attr('colSpan', fiduciaryfldcnt); 
					$(".compensationtab").hide();
					$(".legendCls").hide();
					$(".riatab").hide();
					$(".riaLegendCls").hide();
					$('#fiduciaryServices').css('background-color','white');
					$("#riafee").css('background-color','#DEDED8');
					$("#compensation").css('background-color','#DEDED8');
					$('#fiduciaryServices').css('cursor','default');
					$(".fiduciaryServicesTab").show();
					$(".fiduciarylegendCls").show();
				}
				
				$('#subtabfield').hover(
					function(){ 
						$(".compensationtab").addClass('hover');
						$(".riatab").addClass('hover');
						$(".fiduciaryServicesTab").addClass('hover');
					},
					function(){
						$(".compensationtab").removeClass('hover');
						$(".riatab").removeClass('hover');
						$(".fiduciaryServicesTab").removeClass('hover');
					}
				);
				$('#desc338hdrfield').hover(
					function(){
						//$(".desc338datafld").addClass('hover');
						$(".compensationtab").addClass('hover');
						$(".riatab").addClass('hover');
						$(".fiduciaryServicesTab").addClass('hover');
					},
					function(){
						//$(".desc338datafld").removeClass('hover');
						$(".compensationtab").removeClass('hover');
						$(".riatab").removeClass('hover');
						$(".fiduciaryServicesTab").removeClass('hover');
					}
				);
			});
	</script>
	
	<bd:form method="post" action="/do/bob/blockOfBusiness/Active/" modelAttribute="blockOfBusinessForm" name="blockOfBusinessForm">
	
		<% 
			boolean riadesgflg = false; 
		    boolean compfeeflg = false; 
		%>
		<%
		BlockOfBusinessReportData theReport = (BlockOfBusinessReportData)request.getAttribute(BDConstants.REPORT_BEAN);
                pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
		%>
			 <c:if test="${not empty theReport.details}">
				<div class="table_controls">
					<div class="table_action_buttons"></div>
					<div class="table_display_info_abs">
						<strong><report:recordCounter report="theReport" label="Contracts"/></strong>
					</div>
					<div class="table_pagination">
				  		<strong><report:pageCounter arrowColor="black" report="theReport"  formName="blockOfBusinessForm"/> </strong>
					</div>
				</div>
			</c:if>

		<table class="report_table_content">
			<thead>
				<tr> 
					<c:if test="${applicableColumns.applicableColumnsForTab['contractName'].enabled}">
						<th  valign="bottom" nowrap="nowrap" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractNumber'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['proposalName'].enabled}">
						<th  valign="bottom" nowrap="nowrap" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['proposalNumber'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractEffDt'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['proposalDt'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractPlanYearEndMMDD'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractState'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['numOfLives'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['totalAssets'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['transferredOutAssetsPriorToCharges'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['assetCharge'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['expectedFirstYearAssets'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							&nbsp;
						</th>
					</c:if>
					<c:set var="count" value="0"/>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionDepositTr1yr'].enabled}">
						<c:set var="count" value="${count+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionDepositReg1Yr'].enabled}">
						<c:set var="count" value="${count+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionDepositRegRen'].enabled}">
						<c:set var="count" value="${count+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionAsset1Year'].enabled}">
						<c:set var="count" value="${count+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionAssetRen'].enabled}">
						<c:set var="count" value="${count+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionAssetAllYrs'].enabled}">
						<c:set var="count" value="${count+1}"/>
					</c:if>
					
					<c:set var="riacount" value="0" />
					<c:if test="${applicableColumns.applicableColumnsForTab['riaBps'].enabled}">
						<c:set var="riacount" value="${riacount+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['riaBpsMax'].enabled}">
						<c:set var="riacount" value="${riacount+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['riaBpsMin'].enabled}">
						<c:set var="riacount" value="${riacount+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['riaAcBlend'].enabled}">
						<c:set var="riacount" value="${riacount+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['riaAcTiered'].enabled}">
						<c:set var="riacount" value="${riacount+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['riaFlatFeePerHead'].enabled}">
						<c:set var="riacount" value="${riacount+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['riaFlatFeeProrata'].enabled}">
						<c:set var="riacount" value="${riacount+1}"/>
					</c:if>					
					<c:set var="fiduciaryColCount" value="0" />
					<c:if test="${applicableColumns.applicableColumnsForTab['cofid321ABFee'].enabled}">
						<c:set var="fiduciaryColCount" value="${fiduciaryColCount+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['cofid321DBFee'].enabled}">
						<c:set var="fiduciaryColCount" value="${fiduciaryColCount+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['cofid338ABFee'].enabled}">
						<c:set var="fiduciaryColCount" value="${fiduciaryColCount+1}"/>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['cofid338DBFee'].enabled}">
						<c:set var="fiduciaryColCount" value="${fiduciaryColCount+1}"/>
					</c:if>					
			    	<c:if test="${not empty theReport.details}">
			    	<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex">
						<c:if test="${(not empty theItem.riaFlatFeeProrata or 
																		not empty theItem.riaFlatFeePerHead or 
																		not empty theItem.riaBps or 
																		not empty theItem.riaBpsMin or 
																		not empty theItem.riaBpsMax or 
																		not empty theItem.riaAcBlend or 
																		not empty theItem.riaAcTiered)}">
							 <c:set var="showRiaTabInd" value="true"/>
						</c:if>
						<c:if test="${(not empty theItem.cofidFeeFeatureCode)}">
							 <c:set var="showFiduciaryTabInd" value="true"/>
						</c:if>
				</c:forEach>
					</c:if>
					
					<c:if test="${count gt 0 or showRiaTabInd eq true or showFiduciaryTabInd eq true }">
					  <th id = "subtabfield" colspan="${count}" valign="bottom" class="val_str" style="padding: 0px;">
						<table id = "subtabs">
							<tr>
							<c:if test="${count gt 0 and blockOfBusinessForm.level1User eq true }">
								<td  id="compensation" style="border: 1px solid; border-bottom:0px; text-align: center;width: 130px;">
							    	<span><strong>Compensation</strong></span>
							    </td>
							</c:if>
							<c:if test="${showRiaTabInd eq true and riacount gt 0  and blockOfBusinessForm.level1User eq true}">
									<td  nowrap id="riafee" style="border: 1px solid; border-bottom:0px; text-align: center;width: 110px;;">
								    	<span><strong>RIA fees</strong></span>
								    </td>
							</c:if>
							<c:if test="${showFiduciaryTabInd eq true and fiduciaryColCount gt 0 and blockOfBusinessForm.level1User eq true}">
								<td nowrap id="fiduciaryServices" style="border: 1px solid; border-bottom:0px; text-align: center;width: 170px;">
							    	<span><strong>Fiduciary Services</strong></span>
							    </td>
							</c:if>
							<c:if test="${blockOfBusinessForm.level1User eq false }">
								<td>
							    	<span><strong>Commissions</strong></span>
							    </td>
							</c:if>									
							</tr>
						</table>
						</th>
					</c:if>
									
					<c:if test="${applicableColumns.applicableColumnsForTab['producerCodesOfBrokers'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['namesOfTheBrokers'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['firmName'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['rvpName'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['productType'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['usOrNy'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['class'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							&nbsp;
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['discontinuedDate'].enabled}">
						<th  valign="bottom" class="val_str">
							&nbsp;
						</th>
					</c:if>
					<% boolean riaFlag = true; %>
			    	<c:if test="${not empty theReport.details}">
			    	<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex">
						<c:if test="${(applicableColumns.applicableColumnsForTab['des338Ind'].enabled) and (theItem.des338Ind eq 'Yes')}">
							<%
								if(riaFlag){
									riaFlag = false;
									riadesgflg = true;
							%> 
								<th  id = "desc338hdrfield" valign="bottom" class="val_str align_center">
									&nbsp;
								</th>
							<% } %>
						</c:if>
					</c:forEach>
					</c:if>
				</tr>
				<tr class="block_of_business">				
					<c:if test="${applicableColumns.applicableColumnsForTab['contractName'].enabled}">
						<th  valign="bottom" nowrap="nowrap" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['contractName'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['contractName'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractNumber'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['contractNumber'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['contractNumber'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['proposalName'].enabled}">
						<th  valign="bottom" nowrap="nowrap" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['proposalName'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['proposalName'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['proposalNumber'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['proposalNumber'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['proposalNumber'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractEffDt'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['contractEffDt'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['contractEffDt'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['proposalDt'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['proposalDt'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['proposalDt'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractPlanYearEndMMDD'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['contractPlanYearEndMMDD'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['contractPlanYearEndMMDD'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractState'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['contractState'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['contractState'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['numOfLives'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['numOfLives'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['numOfLives'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['totalAssets'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['totalAssets'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['totalAssets'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['transferredOutAssetsPriorToCharges'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['transferredOutAssetsPriorToCharges'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['transferredOutAssetsPriorToCharges'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['assetCharge'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['assetCharge'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['assetCharge'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['expectedFirstYearAssets'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['expectedFirstYearAssets'].id}" direction="desc" formName="blockOfBusinessForm" >
								${applicableColumns.applicableColumnsForTab['expectedFirstYearAssets'].title}
							</report:sort>
						</th>
					</c:if>				
				    <c:if test="${applicableColumns.applicableColumnsForTab['commissionDepositTr1yr'].enabled}">
						<th class="compensationtab" style="border-left : 1px solid #cac8c4;" valign="bottom" class="sub align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['commissionDepositTr1yr'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['commissionDepositTr1yr'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionDepositReg1Yr'].enabled}">
						<th class="compensationtab" valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['commissionDepositReg1Yr'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['commissionDepositReg1Yr'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionDepositRegRen'].enabled}">
						<th class="compensationtab" valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['commissionDepositRegRen'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['commissionDepositRegRen'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionAsset1Year'].enabled}">
						<th class="compensationtab" valign="bottom" class="sub align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['commissionAsset1Year'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['commissionAsset1Year'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionAssetRen'].enabled}">
						<th class="compensationtab" valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['commissionAssetRen'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['commissionAssetRen'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionAssetAllYrs'].enabled}">
						<th class="compensationtab" valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['commissionAssetAllYrs'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['commissionAssetAllYrs'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${showRiaTabInd eq true}">
						<c:if test="${applicableColumns.applicableColumnsForTab['riaBps'].enabled}">
							<th class="riatab" valign="bottom" class="val_str align_center">
								<report:sort field="${applicableColumns.applicableColumnsForTab['riaBps'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['riaBps'].title}
								</report:sort>
							</th>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaBpsMin'].enabled}">
							<th class="riatab" valign="bottom" class="val_str align_center" style="min-width: 45px;">
								<report:sort field="${applicableColumns.applicableColumnsForTab['riaBpsMin'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['riaBpsMin'].title}
								</report:sort>
							</th>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaBpsMax'].enabled}">
							<th class="riatab" valign="bottom" class="val_str align_center" style="min-width: 45px;">
								<report:sort field="${applicableColumns.applicableColumnsForTab['riaBpsMax'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['riaBpsMax'].title}
								</report:sort>
							</th>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaAcBlend'].enabled}">
							<th class="riatab" valign="bottom" class="val_str align_center" style="min-width: 45px;">
								<report:sort field="${applicableColumns.applicableColumnsForTab['riaAcBlend'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['riaAcBlend'].title}
								</report:sort>
							</th>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaAcTiered'].enabled}">
							<th class="riatab" valign="bottom" class="val_str align_center" style="min-width: 45px;">
								<report:sort field="${applicableColumns.applicableColumnsForTab['riaAcTiered'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['riaAcTiered'].title}
								</report:sort>
							</th>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaFlatFeePerHead'].enabled}">
							<th class="riatab" valign="bottom" class="val_str align_center">
								<report:sort field="${applicableColumns.applicableColumnsForTab['riaFlatFeePerHead'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['riaFlatFeePerHead'].title}
								</report:sort>
							</th>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaFlatFeeProrata'].enabled}">
							<th class="riatab" valign="bottom" class="val_str align_center">
								<report:sort field="${applicableColumns.applicableColumnsForTab['riaFlatFeeProrata'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['riaFlatFeeProrata'].title}
								</report:sort>
							</th>
						</c:if>
					</c:if>	
					<c:if test="${showFiduciaryTabInd eq true}">				
						<c:if test="${applicableColumns.applicableColumnsForTab['cofid321ABFee'].enabled}">
							<th class="fiduciaryServicesTab" nowrap valign="bottom" class="val_str align_center">
								<report:sort field="${applicableColumns.applicableColumnsForTab['cofid321ABFee'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['cofid321ABFee'].title}
								</report:sort>
							</th>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['cofid321DBFee'].enabled}">
							<th class="fiduciaryServicesTab" nowrap valign="bottom" class="val_str align_center">
								<report:sort field="${applicableColumns.applicableColumnsForTab['cofid321DBFee'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['cofid321DBFee'].title}
								</report:sort>
							</th>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['cofid338ABFee'].enabled}">
							<th class="fiduciaryServicesTab" nowrap valign="bottom" class="val_str align_center">
								<report:sort field="${applicableColumns.applicableColumnsForTab['cofid338ABFee'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['cofid338ABFee'].title}
								</report:sort>
							</th>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['cofid338DBFee'].enabled}">
							<th class="fiduciaryServicesTab" nowrap valign="bottom" class="val_str align_center"> 
								<report:sort field="${applicableColumns.applicableColumnsForTab['cofid338DBFee'].id}" direction="desc" formName="blockOfBusinessForm">
									${applicableColumns.applicableColumnsForTab['cofid338DBFee'].title}
								</report:sort>
							</th>
						</c:if>		
					</c:if>						
					<c:if test="${applicableColumns.applicableColumnsForTab['producerCodesOfBrokers'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['producerCodesOfBrokers'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['producerCodesOfBrokers'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['namesOfTheBrokers'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['namesOfTheBrokers'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['namesOfTheBrokers'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['firmName'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['firmName'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['firmName'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['rvpName'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['rvpName'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['rvpName'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['productType'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['productType'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['productType'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['usOrNy'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['usOrNy'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['usOrNy'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['class'].enabled}">
						<th  valign="bottom" class="val_str align_center">
							<report:sort field="${applicableColumns.applicableColumnsForTab['class'].id}" direction="asc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['class'].title}
							</report:sort>
						</th>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['discontinuedDate'].enabled}">
						<th  valign="bottom" class="val_str">
							<report:sort field="${applicableColumns.applicableColumnsForTab['discontinuedDate'].id}" direction="desc" formName="blockOfBusinessForm">
								${applicableColumns.applicableColumnsForTab['discontinuedDate'].title}
							</report:sort>
						</th>
					</c:if>
					<% riaFlag = true; %>
			    	<c:if test="${not empty theReport.details}">
			        <c:forEach items="${theReport.details}" var="theItem"  varStatus="theIndex" >
						<c:if test="${(applicableColumns.applicableColumnsForTab['des338Ind'].enabled) and (theItem.des338Ind eq 'Yes')}">
							<%
								if(riaFlag){
									riaFlag = false;
									riadesgflg = true;
							%> 
								<th  valign="bottom" class="val_str align_center desc338datafld">
									<report:sort field="${applicableColumns.applicableColumnsForTab['des338Ind'].id}" direction="desc" formName="blockOfBusinessForm">
										${applicableColumns.applicableColumnsForTab['des338Ind'].title}
									</report:sort>
								</th>
							<% } %>
						</c:if>
					</c:forEach>
					</c:if>
				</tr>
			</thead>
			<tbody>
	 <c:if test="${not empty theReport.details}">
							<c:forEach items="${theReport.details}" var="theItem"  varStatus="theIndex" >

													<c:set var="indexValue" value="${theIndex.index}"/>
													<c:if test="${indexValue}% 2 == 0">
														<tr >
														
													</c:if>
													<c:if test="${indexValue}% 2 != 0">
														<tr class="spec">
													</c:if>
			    
					<c:if test="${applicableColumns.applicableColumnsForTab['contractName'].enabled}">
						<td class="name">
							<%if (BDConstants.ACTIVE_TAB.equals(blockOfBusinessForm.getCurrentTab()) || 
							        BDConstants.DISCONTINUED_TAB.equals(blockOfBusinessForm.getCurrentTab())) { %>
								<a href="javascript://" onclick="goToContractInformationPage(${theItem.contractNumber}); return false;">${theItem.contractName}</a>
								<%} else if(BDConstants.PENDING_TAB.equals(blockOfBusinessForm.getCurrentTab())){ %>						
								<c:choose>
									<c:when test="${theItem.contractStatusCode eq contractStatusCodePS or theItem.contractStatusCode eq contractStatusCodeDC or theItem.contractStatusCode eq contractStatusCodePC or theItem.contractStatusCode eq contractStatusCodeCA}">
										<a href="javascript://" onclick="goToSecureDocumentUploadPage(${theItem.contractNumber}); return false;">${theItem.contractName}</a>
									</c:when>
									<c:otherwise>
										${theItem.contractName}
									</c:otherwise>
								</c:choose>
							<%} else { %>
								${theItem.contractName}
							<%} %>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractNumber'].enabled}">
						<td class="val_num_cnt">${theItem.contractNumber}
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['proposalName'].enabled}">
						<td class="name">${theItem.proposalName}</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['proposalNumber'].enabled}">
						<td class="val_num_cnt">${theItem.proposalNumber}</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractEffDt'].enabled}">
						<td class="date"><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>"	property="theItem.contractEffectiveDate"/></td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['proposalDt'].enabled}">
						<td class="date"><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>"	property="theItem.proposalDate"/></td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractPlanYearEndMMDD'].enabled}">
						<td class="date"><render:date patternOut="<%=BDConstants.DATE_MM_DD_SLASHED%>" property="theItem.contractPlanYearEnd"/></td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['contractState'].enabled}">
						<td class="val_str">${theItem.contractState}</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['numOfLives'].enabled}">
						<c:if test="${empty theItem.numOfLives}">
							<td>-</td>
						</c:if>
						<c:if test="${not empty theItem.numOfLives}">
							<td class="val_num_cnt">${theItem.numOfLives}
							</td>
						</c:if>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['totalAssets'].enabled}">
						<td class="cur">
							<c:if test="${empty theItem.totalAssets}">
								-
							</c:if>
							<c:if test="${not empty theItem.totalAssets}">
								<report:number property="theItem.totalAssets" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['transferredOutAssetsPriorToCharges'].enabled}">
						<td class="cur">
							<c:if test="${empty theItem.transferredAssetsPriorToCharges}">
								-
							</c:if>
							<c:if test="${not empty theItem.transferredAssetsPriorToCharges}">
					              <report:number property="theItem.transferredAssetsPriorToCharges" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['assetCharge'].enabled}">
						<td class="cur">
							<c:if test="${empty theItem.assetCharge}">
								-
							</c:if>
							<c:if test="${not empty theItem.assetCharge}">
					              <report:number property="theItem.assetCharge" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['expectedFirstYearAssets'].enabled}">
						<td class="cur">
							<c:if test="${empty theItem.expectedFirstYearAssets}">
								-
							</c:if>
							<c:if test="${not empty theItem.expectedFirstYearAssets}">
					              <report:number property="theItem.expectedFirstYearAssets" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionDepositTr1yr'].enabled}">
						<td class="compensationtab">
							<c:if test="${empty theItem.commissionDepositTr1yr}">
								-
							</c:if>
							<c:if test="${not empty theItem.commissionDepositTr1yr}">
								<report:number property="theItem.commissionDepositTr1yr" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionDepositReg1Yr'].enabled}">
						<td class="compensationtab">
							<c:if test="${empty theItem.commissionDepositReg1Yr}">
								-
							</c:if>
							<c:if test="${not empty theItem.commissionDepositReg1Yr}">
								<report:number property="theItem.commissionDepositReg1Yr" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionDepositRegRen'].enabled}">
						<td class="compensationtab">
							<c:if test="${empty theItem.commissionDepositRegRen}">
								-
							</c:if>
							<c:if test="${not empty theItem.commissionDepositRegRen}">
								<report:number property="theItem.commissionDepositRegRen" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionAsset1Year'].enabled}">
						<td class="compensationtab">
							<c:if test="${empty theItem.commissionAsset1Year}">
								-
							</c:if>
							<c:if test="${not empty theItem.commissionAsset1Year}">
								<report:number property="theItem.commissionAsset1Year" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionAssetRen'].enabled}">
						<td class="compensationtab">
							<c:if test="${empty theItem.commissionAssetRen}">
								-
							</c:if>
							<c:if test="${not empty theItem.commissionAssetRen}">
								<report:number property="theItem.commissionAssetRen" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['commissionAssetAllYrs'].enabled}">
						<td class="compensationtab">
							<c:if test="${empty theItem.commissionAssetAllYrs}">
								-
							</c:if>
							<c:if test="${not empty theItem.commissionAssetAllYrs}">
								<report:number property="theItem.commissionAssetAllYrs" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
							</c:if>
						</td>
					</c:if>					
					<c:if test="${showRiaTabInd eq true}">
						<c:if test="${applicableColumns.applicableColumnsForTab['riaBps'].enabled}">
							<td class="riatab">
								<c:choose>
									<c:when test="${theItem.riaFirmName eq '*' and theItem.isRiaBpsAvailable()}">
									    ${theItem.riaFirmName}
									</c:when>
									<c:otherwise>
										<c:if test="${empty theItem.riaFirmName or not theItem.isRiaBpsAvailable()}">
											-
										</c:if>
										<c:if test="${theItem.isRiaBpsAvailable() and not empty theItem.riaFirmName}">
											<report:number property="theItem.riaBps" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
										</c:if>
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaBpsMin'].enabled}">
							<c:choose>
								<c:when test="${theItem.riaFirmName eq '*' and not empty theItem.riaBpsMin}">
								   <td class="riatab">
								     ${theItem.riaFirmName}
								   </td> 
								</c:when>
								<c:otherwise>
									<c:if test="${empty theItem.riaFirmName or empty theItem.riaBpsMin}">
									  <td class="riatab">
										-
									  </td> 
									</c:if>
									<c:if test="${not empty theItem.riaBpsMin and not empty theItem.riaFirmName}">
									    <c:set var="monthlyMinAmt"><fmt:formatNumber value="${theItem.riaBpsMonthlyMinAmount}" type="currency"/></c:set>
									    <c:set var="hoverText">${fn:replace(monthlyMinHoverText, replaceParam, monthlyMinAmt)}</c:set>
									    <td class="riatab" title = "${hoverText}">
										    <report:number property="theItem.riaBpsMin" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
										</td>
									</c:if>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaBpsMax'].enabled}">
								<c:choose>
									<c:when test="${theItem.riaFirmName eq '*' and not empty theItem.riaBpsMax}">
									<td class="riatab">
									    ${theItem.riaFirmName}
									</td>
									</c:when>
									<c:otherwise>
										<c:if test="${empty theItem.riaFirmName or empty theItem.riaBpsMax}">
										<td class="riatab">
											-
										</td>
										</c:if>
										<c:if test="${not empty theItem.riaBpsMax and not empty theItem.riaFirmName}">
										<c:set var="annualAmt"><fmt:formatNumber value="${theItem.riaBpsAnnualMaxAmount}" type="currency"/></c:set>
									    <c:set var="hoverText">${fn:replace(annualMaxHoverText, replaceParam, annualAmt)}</c:set>
									    <td class="riatab" title = "${hoverText}">
											<report:number property="theItem.riaBpsMax" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
										</td>
										</c:if>
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaAcBlend'].enabled}">
							<td class="riatab">
								<c:choose>
									<c:when test="${theItem.riaFirmName eq '*' and not empty theItem.riaAcBlend}">
									    ${theItem.riaFirmName}
									</c:when>
									<c:otherwise>
										<c:if test="${empty theItem.riaFirmName or empty theItem.riaAcBlend}">
											-
										</c:if>
										<c:if test="${not empty theItem.riaAcBlend and not empty theItem.riaFirmName}">
											<c:if test="${theItem.riaAcBlend eq '999.000000'}">
												NEW
											</c:if>
											<c:if test="${theItem.riaAcBlend ne '999.000000'}">
												<report:number property="theItem.riaAcBlend" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
											</c:if>
											<a class="trigger exempt"
											href="javascript:viewBlendFeeOverlay(${theItem.contractNumber},${theIndex},${theItem.proposalNumber});"><img src="/assets/unmanaged/images/planreview_preview_icon.png" width="14" height="14" border="0" style="position: absolute;padding-left: 2px;"></a>
										</c:if>
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaAcTiered'].enabled}">
							<td class="riatab">
								<c:choose>
									<c:when test="${theItem.riaFirmName eq '*' and not empty theItem.riaAcTiered}">
									    ${theItem.riaFirmName}
									</c:when>
									<c:otherwise>
										<c:if test="${empty theItem.riaFirmName or empty theItem.riaAcTiered}">
											-
										</c:if>
										<c:if test="${not empty theItem.riaAcTiered and not empty theItem.riaFirmName}">
											<c:if test="${theItem.riaAcTiered eq '999.000000'}">
												NEW
											</c:if>
											<c:if test="${theItem.riaAcTiered ne '999.000000'}">
												<report:number property="theItem.riaAcTiered" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
											</c:if>
											<a class="trigger exempt"
											href="javascript:viewTieredFeeOverlay(${theItem.contractNumber},${theIndex},${theItem.proposalNumber});"><img src="/assets/unmanaged/images/planreview_preview_icon.png" width="14" height="14" border="0" style="position: absolute;padding-left: 2px;"></a>
										</c:if>
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaFlatFeePerHead'].enabled}">
							<td class="riatab cur">
								<c:choose>
									<c:when test="${theItem.riaFirmName eq '*' and not empty theItem.riaFlatFeePerHead}">
									    ${theItem.riaFirmName}
									</c:when>
									<c:otherwise>
										<c:if test="${empty theItem.riaFirmName or empty theItem.riaFlatFeePerHead}">
											-
										</c:if>
										<c:if test="${not empty theItem.riaFlatFeePerHead and not empty theItem.riaFirmName}">
											<report:number property="theItem.riaFlatFeePerHead" scale="3" type="c" sign="false"/>
										</c:if>
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['riaFlatFeeProrata'].enabled}">
							<td class="riatab cur">
								<c:choose>
									<c:when test="${theItem.riaFirmName eq '*' and not empty theItem.riaFlatFeeProrata}">
									    ${theItem.riaFirmName}
									</c:when>
									<c:otherwise>
										<c:if test="${empty theItem.riaFirmName or empty theItem.riaFlatFeeProrata}">
											-
										</c:if>
										<c:if test="${not empty theItem.riaFlatFeeProrata and not empty theItem.riaFirmName}">
											<report:number property="theItem.riaFlatFeeProrata" scale="3" type="c" sign="false"/>
										</c:if>
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>
					</c:if>	
					<c:if test="${showFiduciaryTabInd eq true}">				
						<c:if test="${applicableColumns.applicableColumnsForTab['cofid321ABFee'].enabled}">
							<c:if test="${empty theItem.cofid321ABFee}">
								<td class="fiduciaryServicesTab">
									-
								</td>
							</c:if>
							<c:if test="${not empty theItem.cofid321ABFee}">
							    <c:if test="${not empty theItem.cofidBPSFeeMonthlyMinAmt and theItem.cofidBPSFeeMonthlyMinAmt > 0}">
							        <c:set var="annualAmt"><fmt:formatNumber value="${theItem.cofidBPSFeeMonthlyMinAmt}" type="currency"/></c:set>
								    <c:set var="hoverText">${fn:replace(monthlyMinHoverText, replaceParam, annualAmt)}</c:set>
							        <td class="fiduciaryServicesTab" title="${hoverText}" >
							        <report:number property="theItem.cofid321ABFee" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
								  </td>
							    </c:if>
							     <c:if test="${ empty theItem.cofidBPSFeeMonthlyMinAmt or theItem.cofidBPSFeeMonthlyMinAmt <= 0 }">
							      <td class="fiduciaryServicesTab">
							        <report:number property="theItem.cofid321ABFee" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
								  </td>
							    </c:if>
							</c:if>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['cofid321DBFee'].enabled}">
							<td class="fiduciaryServicesTab cur">
								<c:if test="${empty theItem.cofid321DBFee}">
									-
								</c:if>
								<c:if test="${not empty theItem.cofid321DBFee}">
									<report:number property="theItem.cofid321DBFee" pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>" scale="3" type="c" sign="false"/>
								</c:if>
							</td>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['cofid338ABFee'].enabled}">
							<c:if test="${empty theItem.cofid338ABFee}">
								<td class="fiduciaryServicesTab">
									-
								</td>
							</c:if>
							<c:if test="${not empty theItem.cofid338ABFee}">
							    <c:if test="${not empty theItem.cofidBPSFeeMonthlyMinAmt and theItem.cofidBPSFeeMonthlyMinAmt > 0 }">
							      <c:set var="annualAmt"><fmt:formatNumber value="${theItem.cofidBPSFeeMonthlyMinAmt}" type="currency"/></c:set>
								  <c:set var="hoverText">${fn:replace(monthlyMinHoverText, replaceParam, annualAmt)}</c:set>
							      <td class="fiduciaryServicesTab" title="${hoverText}" >
							        <report:number property="theItem.cofid338ABFee" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
								  </td>
							    </c:if>
							    <c:if test="${ empty theItem.cofidBPSFeeMonthlyMinAmt or theItem.cofidBPSFeeMonthlyMinAmt <= 0 }">
							      <td class="fiduciaryServicesTab">
							        <report:number property="theItem.cofid338ABFee" pattern="<%=BDConstants.AMOUNT_3DECIMAL_FORMAT%>" scale="3" type="c" sign="false"/>
								  </td>
							    </c:if>
							</c:if>
						</c:if>
						<c:if test="${applicableColumns.applicableColumnsForTab['cofid338DBFee'].enabled}">
							<td class="fiduciaryServicesTab cur">
								<c:if test="${empty theItem.cofid338DBFee}">
									-
								</c:if>
								<c:if test="${not empty theItem.cofid338DBFee}">
									<report:number property="theItem.cofid338DBFee" pattern="<%=BDConstants.AMOUNT_FORMAT_TWO_DECIMALS%>" scale="3" type="c" sign="false"/>
								</c:if>
							</td>
						</c:if>
					</c:if>		
					<c:if test="${applicableColumns.applicableColumnsForTab['des338Ind'].enabled}">
						<c:if test="<%=riadesgflg%>">
							<td class="val_str desc338datafld">
								<c:if test="${empty theItem.des338Ind}">
									-
								</c:if>
								<c:if test="${not empty theItem.des338Ind}">
									${theItem.des338Ind}
								</c:if>
							</td>
						</c:if>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['producerCodesOfBrokers'].enabled}">
						<td class="val_num_cnt">${theItem.producerCodes}</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['namesOfTheBrokers'].enabled}">
						<td class="val_str">${theItem.financialRepNameAndFirmName}</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['firmName'].enabled}">
						<td class="name">firmName place holder</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['rvpName'].enabled}">
						<td class="name">${theItem.rvpName}</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['productType'].enabled}">
						<td class="name">${theItem.productType}</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['usOrNy'].enabled}">
						<td class="name align_center">${theItem.usOrNy}</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['class'].enabled}">
						<td class="name align_center">${theItem.fundClass}</td>
					</c:if>
					<c:if test="${applicableColumns.applicableColumnsForTab['discontinuedDate'].enabled}">
						<c:if test="${theItem.contractStatusCode ne 'DI'}">
							<td>-</td>
						</c:if>
						<c:if test="${theItem.contractStatusCode eq 'DI'}">
							<td class="date"><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.discontinuanceDate"/></td>
						</c:if>
					</c:if>
					</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
		<script>
			var compfldcnt = '<c:out value="${count}"/>';
			var riafldcnt = '<c:out value="${riacount}"/>';
			var fiduciaryfldcnt = '<c:out value="${fiduciaryColCount}"/>';
		</script>
		<%--.report_table_content--%>
		<%
		ArrayList<GenericException> infoMsgDisplayedUnderColHeader = (ArrayList<GenericException>) 
						request.getAttribute(BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);
		ArrayList<GenericException> errorMsgDisplayedUnderColHeader = (ArrayList<GenericException>) 
		request.getAttribute(BDConstants.ERROR_MSG_DISPLAY_UNDER_COLUMN_HEADER);
		
		if (infoMsgDisplayedUnderColHeader != null && !infoMsgDisplayedUnderColHeader.isEmpty()) {
			request.setAttribute(BDConstants.INFO_MESSAGES, infoMsgDisplayedUnderColHeader);
		}
		
		if (errorMsgDisplayedUnderColHeader != null && !errorMsgDisplayedUnderColHeader.isEmpty()) {
		    String ERROR_KEY = ContentProperties.getInstance().getProperty(ContentCacheConstants.ERROR_KEY);
			request.setAttribute(ERROR_KEY, errorMsgDisplayedUnderColHeader);
		}
		%>
		<report:formatMessages scope="request"/>
		 <c:if test="${not  empty theReport.details}">
			<div class="table_controls">
				<div class="table_action_buttons"></div>
				<div class="table_display_info_abs">
					<strong><report:recordCounter report="theReport" label="Contracts" /></strong>
				</div>
				<div class="table_pagination">
			  		<strong><report:pageCounter arrowColor="black" report="theReport"  formName="blockOfBusinessForm"/> </strong>
				</div>
			</div>
			</c:if>
	</bd:form>
<div id="viewOverlayPanel"
	style="visibility: hidden; background-color: #FBF9EE; font: 16px verdana, arial, helvetica, sans-serif;">
	<%-- Dynamic --%>
</div>
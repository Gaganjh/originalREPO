<!-- taglibs -->
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.platform.web.content.CommonContentConstants"%>

<un:useConstants var="ievalConstants" className="com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorConstants"/>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<content:contentBean contentId="<%=BDContentConstants.WARNING_NAVIGATES_AWAY_FROM_FUNDEVALUATOR%>"
				     type="<%=CommonContentConstants.TYPE_MESSAGE%>"
				     id="navigateAwayWarningContent"/>			         
				     
<c:set var="intro1" value="${layoutPageBean.introduction1}"/>
<c:set var="intro2" value="${layoutPageBean.introduction2}"/>		
<c:set var="benifits" value="${layoutPageBean.layer1.text}"/>		     
					 
<script type="text/javascript">

var intervalId;

var utilities = {
    
    // Asynchronous request call to the server. 
    doAsyncRequest : function(actionPath, callbackFunction) {
        YAHOO.util.Connect.setForm(document.fundEvaluatorForm);
        // Make a request
        var request = YAHOO.util.Connect.asyncRequest('GET', actionPath, callbackFunction);
    },
    
    // Generic function to handle a failure in the server response  
    handleFailure : function(o){ 
        o.argument = null;
        utilities.hideWaitPanel();
		clearInterval(intervalId);
    },
    
    // Shows loading panel message
    showWaitPanel : function() {
        waitPanel = document.getElementById("wait_c");
        if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
            loadingPanel = new YAHOO.widget.Panel("wait",  
                                {   width: "250px", 
                                    height:"50px",
                                    fixedcenter: true, 
                                    close: false, 
                                    draggable: false, 
                                    zindex:4,
                                    modal: true,
                                    visible: false,
                                    constraintoviewport: true
                                } 
                            );
            loadingPanel.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
            loadingPanel.render(document.body);
            loadingPanel.show();
        }       
    },

    /**
    * hides the loading panel
    */
    hideWaitPanel: function () {	
        loadingPanel.hide();	
    }
        
 }

    function showAssetClass(assetBoxIndex) {
       document.getElementById("ieval_fundlist_outerlayer").style.display="";
       document.getElementById("ieval_fundlist_innerlayer").style.display="";
    }
	
    function closeList() {
       document.getElementById("ieval_fundlist_outerlayer").style.display="none";
       document.getElementById("ieval_fundlist_innerlayer").style.display="none";
    }
	//This function is used to move the optional items to included list 
	//when the user selects the item. And hides the optional item and display the item in 
	//included list.
	var count = 0;	// count of selected Optional items
	var optionalItemsCount = 3;	//Total optional items availlable	
	<c:if test="${fundEvaluatorForm.includeGIFLSelectFunds}">
		optionalItemsCount++;
	</c:if>
	
	function moveOptionalItems(i){	
		count = count + 1;		
		//for(var i=1;i<=optionalItemsCount;i++){				
			if(document.getElementById("optItem"+i) && document.getElementById("optItem"+i).checked == true){
				document.getElementById("optItemDiv"+i).style.display="none";
				document.getElementById("optItem"+i).checked = false;
				document.getElementById("includedOptItemDiv"+i).style.display="";
				document.getElementById("includedOptItem"+i).checked = true;	
				
			}
		
			//If "Fund rankings and methodology" is selected , make the 
			//Fund Ranking Selected RadioButton as selected
			if(document.getElementById("includedOptItemDiv"+i).id == 'includedOptItemDiv4'){
				//If "selected funds" is selected
				if(document.getElementById("fundRankingMethodology1").checked)
					document.getElementById("fundRankingMethodology1").checked = true;
					//if "All available funds"  is selected
				else if(document.getElementById("fundRankingMethodology2").checked ){
					document.getElementById("fundRankingMethodology2").checked = true;
				}//If both the radio buttons are not selected. default will be "selected funds"
				else{
					document.getElementById("fundRankingMethodology1").checked = true;
				}
				
			}
			//If all the optional items are removed , then display the "All selected" label.
			if(count == optionalItemsCount){
				document.getElementById("allSelectedLabel").style.display = "block";				
			}
			
		//}
	}
	//This function is used to remove the items from the included list and show the items again
	// in the optional list.	
	function removeOptionalItems(i){	
		count = count -1;	
		//for(var i=1;i<=optionalItemsCount;i++){	
			if(document.getElementById("optItemDiv"+i) && !document.getElementById("includedOptItem"+i).checked ){		
				document.getElementById("optItemDiv"+i).style.display="";
				//If Fund ranking and methodology is deselected from the included list 
				//asset class  and risk/return   should be cleared.
				if(document.getElementById("includedOptItemDiv"+i).id == 'includedOptItemDiv4'){
					document.getElementById("fundRankingMethodology1").checked = false;
					document.getElementById("fundRankingMethodology2").checked = false;
					
				}
				//clear the value when the item is removed the included list			
				document.getElementById("includedOptItemDiv"+i).style.display="none";				
				document.getElementById("optItem"+i).checked = false;	
				
				//To hide the "All selected" label displayed in the optional section.
				document.getElementById("allSelectedLabel").style.display = "none";					
			}
		//}		
	}
	//This function is used to navigate to step 4.
	function doPrevious() {
		document.fundEvaluatorForm.action="?action=" + investmentOptionsSelection + "&" + "page=" + "previous";		
		document.fundEvaluatorForm.submit();
	}
	
	function doGenerateReport(){
		utilities.showWaitPanel();
		document.fundEvaluatorForm.action="?action=generateReport&page=";	
		document.getElementById("errorMsgs").style.display="none";
		document.fundEvaluatorForm.submit();
		
		intervalId = setInterval("doCheckPdfReportGenerated()", 5000);    	
	}
	
	function doCheckPdfReportGenerated(){
		utilities.doAsyncRequest("/do/fundEvaluator/?action=checkPdfReportGenerated&page=", callback_checkPdfReportGenerated);
	}
	
	// Call back handler to Check whether FundEvaluator Report Generation is complete.
var callback_checkPdfReportGenerated =    {
    cache : false,
    failure : utilities.handleFailure
    };
	
	//This method identifies and loads the included and optional items, if the user has
	//selected them previously.
	function loadSelectedValues(){					
			
			if('${fundEvaluatorForm.includedOptItem1}'=='on'){
				count = count + 1;
				document.getElementById("includedOptItemDiv1").style.display="";	
				document.getElementById("optItemDiv1").style.display="none";				
			}
			if("${e:forJavaScriptBlock(fundEvaluatorForm.includedOptItem3)}"=='on'){
				count = count + 1;
				document.getElementById("includedOptItemDiv3").style.display="";	
				document.getElementById("optItemDiv3").style.display="none";				
			}
			if('${fundEvaluatorForm.includedOptItem4}'=='on'){
				count = count + 1;
				document.getElementById("includedOptItemDiv4").style.display="";	
				document.getElementById("optItemDiv4").style.display="none";
				
			}
			if("${e:forJavaScriptBlock(fundEvaluatorForm.includedOptItem5)}"=='on'){
				count = count + 1;
				document.getElementById("includedOptItemDiv5").style.display="";	
				document.getElementById("optItemDiv5").style.display="none";
				
			}
			if("${e:forJavaScriptBlock(fundEvaluatorForm.includedOptItem6)}"=='on'){
				count = count + 1;
				document.getElementById("includedOptItemDiv6").style.display="";	
				document.getElementById("optItemDiv6").style.display="none";
				
			}
			if("${e:forJavaScriptBlock(fundEvaluatorForm.includedOptItem7)}"=='on'){
				count = count + 1;
				document.getElementById("includedOptItemDiv7").style.display="";	
				document.getElementById("optItemDiv7").style.display="none";
				
			}
			//To display the text "All options have been selected" based on the count.
			if(count == optionalItemsCount){
				document.getElementById("allSelectedLabel").style.display = "block";				
			}
	}	
	
</script>

<bd:form action="/do/fundEvaluator/" modelAttribute="fundEvaluatorForm" name="fundEvaluatorForm">
	
	
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
		<div id="errorMsgs" style="DISPLAY:block";>
 			<report:formatMessages scope="session"/>
		</div>
			<div id="contentievalfull">
					<div id="fundEvaluatorButtons">
						<input type="button" class="grey-btn back" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn back'" name="Back" value="Back" onclick="doPrevious()">
					</div>
					<div style="CLEAR: both">
						<ul class="proposal_nav_menu">
							<li><a id="step1">Step 1</a></li>
							<li><a id="step2">Step 2</a></li>				
							<li><a id="step3">Step 3</a></li>
							<li><a id="step4">Step 4</a></li>				
							<li><a  class="selected_link"><span class="step_number">Step 5:</span><span class="step_caption"> Customize and generate report</span></a></li>         
						</ul>		

 
				<div class="selection_mode">
				<!-- Cover sheet info start-->
					<div id="coversheetInfo">
					<div class="coversheetInformationHeadingLeft">FundEvaluator cover sheet</div>											
					<div class="coversheetInformationHeadingRight">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>	

							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td align="right" valign="top" nowrap="nowrap" class="coversheet"><strong>*Company name</strong></td>
									<td valign="top">
										<label>
<form:input path="companyName" maxlength="60" size="40" />
										</label>
									</td>
									<td align="right">&nbsp;</td>
									<td align="right">&nbsp;</td>
									<td class="coversheet"><strong>*Select cover image</strong></td>								
							</tr>
							<tr valign="top">
								<td align="right" nowrap="nowrap" class="coversheet"><strong>*Your name</strong></td>
								<td>
									<label>
<form:input path="yourName" maxlength="61" size="40" />
									</label>
								</td>
								<td colspan="5" rowspan="5">
									<table border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td align="right" width="60px">&nbsp;</td>											
											<td align="center" valign="top"><img src="/assets/unmanaged/images/Cover-Plain.gif" alt="" width="74" height="60" /></td>
											<td align="center" valign="top"><img src="/assets/unmanaged/images/Cover-Boardroom.gif" alt="" width="74" height="60" /></td>
											<td align="center" valign="top"><img src="/assets/unmanaged/images/Cover-Factory.gif" alt="" width="74" height="60" /></td>
											<td align="center" valign="top"><img src="/assets/unmanaged/images/Cover-Grocery.gif" alt="" width="74" height="60" /></td>
										</tr>
									
										<tr>
											<td align="right">&nbsp;</td>
											<td align="center"><label>
<form:radiobutton path="yourCoverSheet" value="${ievalConstants.COVER_SHEET_STANDARD}" />
												 </label>										  
											 </td>
											  
											<td align="center">
<form:radiobutton path="yourCoverSheet" value="${ievalConstants.COVER_SHEET_TYPE1}" />
											</td>						  
											  
											<td align="center">
<form:radiobutton path="yourCoverSheet" value="${ievalConstants.COVER_SHEET_TYPE2}" />
											</td>						  
											
											<td align="center">
<form:radiobutton path="yourCoverSheet" value="${ievalConstants.COVER_SHEET_TYPE3}" />
											</td>						  
										</tr>
									</table>
								</td>
							</tr>
							<tr valign="top">
								<td align="right" nowrap="nowrap" class="coversheet"><strong>&nbsp;Your firm</strong></td>
								<td><label>
<form:input path="yourFirm" maxlength="60" size="40" />
								</label></td>
							</tr>
							<tr valign="top">
								<td align="right" nowrap="nowrap">&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
							<tr valign="top">
								<td align="right" nowrap="nowrap" class="coversheet"><strong>*required fields</strong></td>
								<td>&nbsp;</td>
							</tr>
						</table>
 
					</div>
					<!-- Cover sheet info end-->
			  
				<div id="optionalItems">
					<h1><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></h1>
					<ul>						
						<li id = "allSelectedLabel" style="DISPLAY:none";>
							<span class="list">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;						
								<label>All optional items have been selected</label>
							</span>
						</li>
						<c:if test="${fundEvaluatorForm.includeGIFLSelectFunds}">
							<li id="optItemDiv7"><img src="/assets/unmanaged/images/ICO-GIFLSel-Not-Inc.gif" width="35" height="40" />                                    
								<span class="list">						
									<input type="checkbox" name="optItemChkBox7" id="optItem7"  onclick="moveOptionalItems(7);"/> 
									G.I.F.L. Select Overview
								</span>
							</li>
						</c:if>
						
						<li id="optItemDiv3">
							<img src="/assets/unmanaged/images/ICO-IPS-Not-Inc.gif" width="30" height="40" />
							<span class="list">
								&nbsp;<input type="checkbox" name="optItemChkBox3" id="optItem3"  onclick="moveOptionalItems(3);"/>
								Investment Policy Statement and &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;overview
							</span>
						</li>
						
						<!--  <li id="optItemDiv4">
							<img src="/assets/unmanaged/images/ICO-Ranking-Not-Inc.gif" width="35" height="40" />
							<span class="list">									
								<input type="checkbox" name="optItemChkBox4" id="optItem4" onclick="moveOptionalItems(4);" />
								Fund rankings and methodology<br /><br />			
							</span> 
						</li> -->
						
						<li id="optItemDiv5"><img src="/assets/unmanaged/images/ICO-Glossary-Not-Inc.gif" width="35" height="40" />
							<span class="list">
							<input type="checkbox" name="optItemChkBox5" id="optItem5" onclick="moveOptionalItems(5);"/>
							Glossary</span>
						</li>
						<li id = "optItemDiv6"><img src="/assets/unmanaged/images/ICO-InvSelection-Not-Inc.gif" width="31" height="40" />
							<span class="list">
							&nbsp;<input type="checkbox" name="optItemChkBox6" id="optItem6" onclick="moveOptionalItems(6);"/>
							Contract investment administration &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;form</span>
						</li>
					</ul>				
				</div>
				<!-- included items -->
				<div id="itemsIncluded">
					<h1><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></h1>
					<ul>					  
						<li><img src="/assets/unmanaged/images/ICO-Cover-Inc.gif" width="35" height="40" /><span class="list">Cover Sheet</span></li>
						<li><img src="/assets/unmanaged/images/ICO-Measurement-Inc.gif" width="35" height="40" /><span class="list">Measurement criteria and results</span></li>
						 <!-- Enabling the Fund ranking Methodology as a Mandatory feild -->
						<!-- Changing the position of Fund ranking and methodology -->
						
						<li class="listMoreSpace"><img src="/assets/unmanaged/images/ICO-Ranking-Inc.gif" width="35" height="40" /><span class="list">Fund ranking and methodology<br />
								<br>
									Include:<br>
								<label>
<form:radiobutton path="fundRankingMethodology" id="fundRankingMethodology1" value="selected" />
									Advisor Selected Funds						
								</label>
								<label>
<form:radiobutton path="fundRankingMethodology" id="fundRankingMethodology2" value="available" />
									All Available Funds							
								</label>
							</span>
						</li> 
				
						<li><img src="/assets/unmanaged/images/ICO-FullFund-Inc.gif" width="35" height="40" /><span class="list" style="height: 35px;">Full fund listing with performance and fees*<br />
							List by: 
							  <label>
<form:radiobutton path="fundListRiskOrderOrAssetClass" value="assetclass" />
								asset class</label>
						  <label>
<form:radiobutton path="fundListRiskOrderOrAssetClass" value="riskreturn" />
							  risk category</label><br/>
						  </span> 
						
						<span class="list" style="padding-left: 45px;">Average expense ratio method:<br/>
						  <label>
<form:radiobutton path="averageExpenceRatioMethod" value="simpleAverage" />
								simple</label>
						  <label>
<form:radiobutton path="averageExpenceRatioMethod" value="weightedAverage" />
							  weighted</label>
						  <label>
<form:radiobutton path="averageExpenceRatioMethod" value="bothAverages" />
								  both</label>  
						  </span> 
						  
						  
						
					 
					  		
						<li id="includedOptItemDiv7" style="DISPLAY:none";><img src="/assets/unmanaged/images/ICO-GIFLSel-Inc.gif" width="35" height="40" />                                    
							<span class="list">						
&nbsp;<input type="checkbox" name="includedOptItem7" id="includedOptItem7" onclick="removeOptionalItems(7);" />
								G.I.F.L. Select Overview
							</span>
						</li>
						
						<!-- Changing the position of Glossary -->
						
						<li id="includedOptItemDiv5" style="DISPLAY:none";>
							<img src="/assets/unmanaged/images/ICO-Glossary-Inc.gif" width="35" height="40" />
							<span class="list">

<input type="checkbox" name="includedOptItem5" id="includedOptItem5" onclick="removeOptionalItems(5);" />
								Glossary
							</span>
						</li>
				
						<li id="includedOptItemDiv3" style="DISPLAY:none";>							
							<span class="list">
							<img src="/assets/unmanaged/images/ICO-IPS-Inc.gif" width="30" height="40" />
&nbsp;<input type="checkbox" name="includedOptItem3" id="includedOptItem3" onclick="removeOptionalItems(3);" />
								Investment Policy Statement and <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;overview<br/><br/><br/>								
							</span>
						</li>
			
					<%-- 	<li id="includedOptItemDiv4" style="DISPLAY:none";>
							<img src="/assets/unmanaged/images/ICO-Ranking-Inc.gif" width="35" height="40" />
							<span class="listMoreSpace">										
								<form:checkbox name="fundEvaluatorForm" path="includedOptItem4" styleId = "includedOptItem4"  onclick="removeOptionalItems(4);"/>
									Fund ranking and methodology<br /><br/>
									Include:<br>
								<label>
									<form:radiobutton  name="fundEvaluatorForm" path="fundRankingMethodology"  value="selected" styleId="fundRankingMethodology1"/>
									Advisor selected Funds										
								</label>
								<label>
									<form:radiobutton name="fundEvaluatorForm" property="fundRankingMethodology"  value="available" styleId="fundRankingMethodology2"/>all available Funds										
								</label>
							</span>
						</li> 
                        --%>			
											
					
						<li id="includedOptItemDiv6" style="DISPLAY:none";><img src="/assets/unmanaged/images/ICO-InvSelection-Inc.gif" width="31" height="40" />
							<span class="list">
&nbsp; 
<input type="checkbox" name="includedOptItem6" id="includedOptItem6" onclick="removeOptionalItems(6);" />
							Contract investment administration form</span>
						</li>		
														
					</ul>
					<script>loadSelectedValues();</script>
				</div>           
              
					<div id="itemsArrow"></div>
					<br class="clearFloat" />
					<br class="clearFloat" />            
					<br/>
					<div>					
						<input type="button" class="generateButton"  onmouseover="this.className +=' hover'" onmouseout="this.className='generateButton'" name="Generate Report" value="Generate Report" onclick="doGenerateReport()">											
					</div>
				
				<BR/>	
 				</div>
			<br>
			<div id="fundEvaluatorButtons">
				<input type="button" class="grey-btn back" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn back'" name="Back" value="Back" onclick="doPrevious()">
			</div>	 
      </div>       
    </div>
</div>  
</div>
<form:hidden path="previousAction" value="${ievalConstants.FORWARD_CUSTOMIZE_REPORT}" /><%--  input - name="fundEvaluatorForm" --%>
<input type="hidden" name="navigateAwayWarning" value="${navigateAwayWarningContent.text}"/>
<form:hidden path="lifeStylePortfolios" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="lifeCyclePortfolios" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="nml" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="newPlanClosedFund" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="existingClientClosedFund" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="includeGIFLSelectFunds" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="dataModified" /><%--  input - name="fundEvaluatorForm" --%>
<form:hidden path="edwardJones" /><%--  input - name="fundEvaluatorForm" --%>
</bd:form>

<%-- Page specific footer elements --%>
<layout:pageFooter/>
	

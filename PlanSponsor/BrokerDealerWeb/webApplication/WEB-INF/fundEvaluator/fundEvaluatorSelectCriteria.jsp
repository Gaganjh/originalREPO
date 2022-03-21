<%-- tag libraries --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.fundEvaluator.CriteriaVO"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.platform.web.content.CommonContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<script type="text/javascript" src="/assets/unmanaged/javascript/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/assets/unmanaged/javascript/chart.min.js"></script>


<un:useConstants var="fundEvalConstants" className="com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorConstants"/>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
 <c:set var="criteriaDetails" value="${criteriaDetails}" />

 

     
<content:contentBean contentId="<%=BDContentConstants.WARNING_NAVIGATES_AWAY_FROM_FUNDEVALUATOR%>"
        type="<%=CommonContentConstants.TYPE_MESSAGE%>"
        id="navigateAwayWarningContent"/>                 

<c:set var="intro1" value="${layoutPageBean.introduction1}"/>
<c:set var="intro2" value="${layoutPageBean.introduction2}"/>
<c:set var="benifits" value="${layoutPageBean.layer1.text}"/>

<head>

    <!-- Dependency --> 
    <script type="text/javascript" src="/assets/unmanaged/javascript/yui-2.5.1/yahoo/yahoo-min.js" ></script>

    <!-- Event source file -->
    <script type="text/javascript" src="/assets/unmanaged/javascript/yui-2.5.1/event/event-min.js" ></script>
    <!-- TODO: New Chart Bar (domReadyOnLoad; testPieChartApplet) -->
    <script type="text/javascript">
    
        function doOnload() {
            if(document.getElementById) {
                for (i=0;i<15;i++) {
                    var divSlider = document.getElementById("slider"+i);                    
                    if (divSlider) {                        
                        val = parseInt(document.getElementById("slider-converted-value"+i).value);                        
                        document.getElementById("slider-converted-value"+i).focus();
                        if (val <= 0) {
                            divSlider.style.display = "none";
                        }
                        
                    }                    
                }
                
                var checkedcount=0;
                var checkgroup = document.fundEvaluatorForm.criteriaSelectedStyle;
                for (var i=0; i<checkgroup.length; i++)
                checkedcount+=(checkgroup[i].checked)? 1 : 0    
                if(checkedcount > 6 ) {
                 	$('#measurementCriteria').css('height', 90 * checkedcount + 'px');
               	    $('#slidersPie').css('height', 90 * checkedcount + 'px');
               	    $('.selection_mode_slider').css('height', 90 * checkedcount + 'px');
                } 	else {
                	$('#measurementCriteria').css('height', '540px');
               	    $('#slidersPie').css('height', '540px');
               	    $('.selection_mode_slider').css('height', '540px');
                }	
            }
        }
        function drawPie(selected_sliders_array, selected_piechart_legendbox_color) {
			
			//var slide_0 = 0; var slide_1 = 0; var slide_2 = 0; var slide_3 = 0; var slide_4 = 0;
			//var color_0 = "#E0E0E0"; var color_1 = "#E0E0E0"; var color_2 = "#E0E0E0"; var color_3 = "#E0E0E0"; var color_4 = "#E0E0E0";
			
			var slide = new Array(0,0,0,0,0,0,0,0,0,0,0,0);
			var color = new Array("#E0E0E0","#E0E0E0","#E0E0E0","#E0E0E0","#E0E0E0","#E0E0E0","#E0E0E0","#E0E0E0","#E0E0E0","#E0E0E0","#E0E0E0","#E0E0E0");
			var total = 0;
			
			if (selected_sliders_array != null) {
				for(var i=0; i<selected_sliders_array.length; i++){
					if (selected_sliders_array[i] != null) {
						slide[i] = selected_sliders_array[i];
						total = total + slide[i];
					}
				}
			}
			if (selected_piechart_legendbox_color != null) {
				for(var i=0; i<selected_piechart_legendbox_color.length; i++){
					if (selected_piechart_legendbox_color[i] != null) {
						color[i] = selected_piechart_legendbox_color[i];
					}
				}
			}
			
			if(total>100) {
				for(var i=0; i<slide.length; i++){
					slide[i] = 0;
				}
			}
			
			
			
			function PieChartDataObject() {            
				this.value = "";
				this.color = ""; 
				this.highlight = "";
				this.label = "0%";
				this.labelColor = 'white';
				this.labelFontSize = '16';
		    }
			
			var pieChartData = new Array();
			for(i=0;i<slide.length;i++) {
				pieChartData[i] = new PieChartDataObject();
				pieChartData[i].value = slide[i];
				pieChartData[i].color = color[i]; 
				pieChartData[i].highlight = color[i];
				pieChartData[i].label = slide[i] + "%";
				pieChartData[i].labelColor = 'white';
				pieChartData[i].labelFontSize = '16';
			}
			
			pieChartData[slide.length]=new PieChartDataObject();
			pieChartData[slide.length].value = 100-total;
			pieChartData[slide.length].color ="#E0E0E0";
			pieChartData[slide.length].highlight ="#E0E0E0";
			pieChartData[slide.length].label = "";
			
			var options = {
				animation: false,
				segmentShowStroke: true,
				segmentStrokeWidth: 0,
				tooltipTemplate: "<\%= value \%>\%",
				showTooltips: true,
				responsive : true
			};

			if (window.pieChart == null) {		
				window.pieChart = new Chart(document.getElementById("pieChartCanvas").getContext("2d")).Pie(pieChartData, options);
			}
			else {
				
				var total = 0;
				for(i=0;i<slide.length;i++) {
					
					window.pieChart.segments[i].value = slide[i];
					window.pieChart.segments[i].fillColor = color[i];
					window.pieChart.segments[i].highlightColor = color[i];
					window.pieChart.segments[i].label = slide[i] + "%";
					
					total = total + slide[i];
				}
				

				window.pieChart.segments[slide.length].value = 100 - total;
				window.pieChart.update();

			}
        }
        
         function getJava() {
            window.open("http://www.java.com/en/");
        }
        
        function showHint(measurement,description){
            if(measurement == '3 Year Return'){
                measurement = 'Total_Return';
            }
            if(measurement == '5 Year Return'){
                measurement = '5_Year_Return';
            }
            if(measurement == '10 Year Return'){
                measurement = '10_Year_Return';
            }
            if(measurement == 'Sharpe Ratio'){
                measurement = 'Sharpe_Ratio';
            }
            if(measurement == 'Information Ratio'){
                measurement = 'Information_Ratio';
            }
            if(measurement == 'Upside Capture'){
                measurement = 'Upside_Capture';
            }
            if(measurement == 'Downside Capture'){
                measurement = 'Downside_Capture';
            }
            if(measurement == 'Standard Deviation'){
                measurement = 'Standard_Deviation';
            }
            if(measurement == 'Expense Ratio'){
                measurement = 'Expense_Ratio';
            }
            if(measurement == 'R-Squared'){
                measurement = 'R_Squared';
            }
            var glossaryUrl  = "/do/fundEvaluator/?action=showGlossary" + "#" + measurement;            
            window.open(glossaryUrl);
        }
</script>
    
</head>

<body class = "yui-skin-sam">
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
                <c:if test="${!empty benifits}">
                    <br class="clearFloat" /><br class="clearFloat" />
                </c:if> 
                <%-- Display error message --%>                    
                <report:formatMessages scope="session"/>
                
                <div id="javaMessage" class='message message_info' style="display:none"><dl><dt>Information Message</dt><dd>This page uses Java<sup>TM</sup> to display an interactive pie chart. We have detected that your browser is unable to display the pie chart correctly. To install a new version of Java<sup>TM</sup> please click <span style="color:#002C3D;cursor:pointer;text-decoration:underline" onclick="getJava()">here</span>.</dd></dl></div>
                <div id="contentievalfull">
                    <div id="fundEvaluatorButtons">
                        <input type="button" class="blue-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='blue-btn next'" name="Next" value="Next" onclick="doNext()">
                        <input type="button" class="grey-btn back" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn back'" name="Back" value="Back" onclick="doPrevious()">
                    </div>
                    
                    <div style="CLEAR: both">
                      <ul class="proposal_nav_menu">
                            <li><a id="step1">Step 1</a></li>                   
                            <li> <a  class="selected_link"><span class="step_number">Step 2:</span><span class="step_caption"> Select evaluation criteria</span></a></li>                                                          
                            <li><a id= "step3">Step 3</a></li>
                            <li><a id="step4">Step 4</a></li>
                            <li><a id="step5">Step 5</a></li>
                      </ul>
                        
                      <div class="selection_mode_slider">
                          <div id="slidersPie">
                            <table class="slider-table">
                                <tr>
                                    <td colspan="2" style="height:15px;">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td style="width:265px;" valign="top">


<c:forEach items="${criteriaDetails}" var="theItem" varStatus="criteriaIndex" >
<c:set var="indexValue" value="${criteriaIndex.index}"/> 

                                    <DIV class="slider-housing" id="slider${indexValue}">
                                     <table class="slider-table">
                                        <tr>
                                      <td style="font-size:11px;vertical-align:bottom;offset:0px;">
                                                <div id="measurement">
                                                 &nbsp;&nbsp;${theItem.measuredBy}
                                               </div>        
                                                    </td>
                                                    <td style="font-size:11px;" align="right">

                                                        <div>
                                                  <c:if test="${theItem.criteriaSelected==''}">
                                          <span id="colorId${criteriaIndex.index}" style="border:1px solid black; background-color:#c54211; width:20px; height:20px; text-align:right;">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;                                                           
                                                            </c:if>

                                             <c:if test="${theItem.criteriaSelected!=''}">
                                               <span id="colorId${criteriaIndex.index}" style="border:1px solid black; background-color:#c54211; width:20px; height:20px; text-align:right;">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp; 

                                              <script>loadUpdatedValues("colorId${criteriaIndex.index}");</script>
                                                </c:if>

                              <form:input  path="theItemList[${criteriaIndex.index }].criteriaValue" maxlength="3" size="3" value="${theItem.criteriaValue}"   id="slider-converted-value${criteriaIndex.index}" />% 
<%--  <html:text name ="theItem" property="criteriaValue" indexed="true"              
                                                                styleId="slider-converted-value${criteriaIndex}"
                                                                style="FONT-WEIGHT: bold; WIDTH: 30px; COLOR: #666666"
                                                                maxlength="3" size="3"  />%  --%>

                                                        </div>                                  
                                                     </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2">
                                                        <div id="slider-bg${criteriaIndex.index}" class="yui-h-slider" tabindex="-1" title="Slider">
                                                            <div id="slider-thumb${criteriaIndex.index}" class="yui-slider-thumb">
                                                                <div class="ieval-sliderbtn-text" style="font-size:11px!important;" id="sliderbtn${criteriaIndex.index}">0%</div>
                                                                <img src="/assets/unmanaged/images/thumb-n.gif"/>                                                                
                                                            </div>
                                                        </div>
                                                    </td>
                                                </TR>
                                            </TABLE>
                                            </DIV>
                                      </c:forEach>        




                                    </td>
                                    
                                     <td style="width:159px;" valign="center"><p> 
                                    <div class="piechart-canvas">
                                    <canvas width="120" height="120" title="Evaluation Criteria" id="pieChartCanvas" style="width: 120px; height: 120px;">
                                    </canvas>
                                    </div>
									<script>drawPie(null,null);</script>
                                      </p>                                    
                                      <span style="font-size:14px;">&nbsp;&nbsp;
                                        <strong>Remaining:&nbsp;&nbsp;
                                            <span id="total_percentage" style="font-size:14px;" >0%</span>
                                        </strong><br>
                                        <div class="remainingPercentageText"  id="negative_percentage"></div>
                                      </span>                                   
                                    </td>                      
                                </tr>           

                            </table>
                        </div>
                         
                        <div id="measurementCriteria">        
                            <div id="measurementHeader">Measurement</div> <div id="criteriaHeader">Criteria</div>               
 
                         
<c:forEach items="${criteriaDetails}" var="theItem" varStatus="criteriaIndex">

                                <div id="measurement">
										<c:if test="${not empty theItem.criteriaSelected}">
											<c:set var="checkedFlag" value="checked" />
										</c:if>
										<c:if test="${empty theItem.criteriaSelected}">
											<c:set var="checkedFlag" value="" />
										</c:if>
										<form:checkbox path="theItemList[${criteriaIndex.index }].criteriaSelected" id="criteriaSelectedStyle" onclick="chkCriteriaSelectionSize('${theItem.measuredBy}','${theItem.description}', '${criteriaIndex.index}' );reposition_Slider_thumb('${criteriaIndex.index}')" disabled="false" value="${theItem.criteriaName}" checked="${checkedFlag}" />

<%-- <input type="checkbox" name="criteriaDetails[${criteriaIndex.index }].criteriaSelected" id="criteriaSelectedStyle" onclick="chkCriteriaSelectionSize('${theItem.measuredBy}','${theItem.description}', '${criteriaIndex.index}' );reposition_Slider_thumb('${criteriaIndex.index}')" value="${theItem.criteriaName}" />
 --%>



                                                        <a id="measuredByIndex${criteriaIndex.index}"
href="javascript:showHint('${theItem.measuredBy}', '${theItem.description}')">
${theItem.measuredBy}
</a>
                                     
                                </div> 
                                <div id="criteria"><span class="ieval_criteria_box">
                                    ${theItem.criteriaName}</span>
                                </div>
</c:forEach>
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

<form:hidden path="criteriaSelected" />
<form:hidden path="remainingPercentage" />
<form:hidden path="pieChartcolors" />

 <%-- To retain the checkbox properties when the user navigating to some other screens and come back.
Common to all the pages. --%>
<form:hidden path="previousAction" value="${fundEvalConstants.FORWARD_SELECT_CRITERIA}" />
<input type="hidden" name="navigateAwayWarning" value="${navigateAwayWarningContent.text}"/>
<form:hidden path="includedOptItem1" />
<form:hidden path="includedOptItem3" />
<form:hidden path="includedOptItem4" />
<form:hidden path="includedOptItem5" />
<form:hidden path="includedOptItem6" />
<form:hidden path="includedOptItem7" />
<form:hidden path="newPlanClosedFund" />
<form:hidden path="existingClientClosedFund" />
<form:hidden path="includeGIFLSelectFunds" />
<form:hidden path="lifeStylePortfolios" />
<form:hidden path="lifeCyclePortfolios" />
<form:hidden path="nml" />
<form:hidden path="dataModified" />
<form:hidden path="edwardJones" />
<form:hidden path="merrillFirmFilter" />
</bd:form>
<%-- Page specific footer elements --%>
<layout:pageFooter/>
</body>

<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.ErrorCodes"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



  <content:contentBean
  contentId="<%=ContentConstants.INVALID_NEW_BAND_ENDS%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidNewBanEnds" />
  
  <content:contentBean
  contentId="<%=ContentConstants.NEW_BAND_ENDS_ZERO%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidValueBAC" />
  
  <content:contentBean
  contentId="<%=ContentConstants.INVALID_LAST_BAND_ENDS%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidLastBandValue" />
  
  <content:contentBean
  contentId="<%=ContentConstants.ONE_OR_MORE_INVALID_NEW_BAND_ENDS%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="oneOrMoreInvalidBAC" />
  
  <content:contentBean
  contentId="<%=ContentConstants.INVALID_BAND_STARTS_AND_ENDS%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidBandStartsEnds" />
  
  <content:contentBean
  contentId="<%=ContentConstants.INVALID_ASSET_CHARGE_RATE%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidRate" />
  
  <content:contentBean
  contentId="<%=ContentConstants.EXTRA_ASSET_CHARGE_RATE_ENTRY%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="extraRateEntry" />
  
  <content:contentBean
  contentId="<%=ContentConstants.ONE_OR_MORE_INVALID_ASSET_CHARGE_RATE%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="oneOrMoreInvalidRate" />
  
  <content:contentBean
  contentId="<%=ContentConstants.INVALID_NEW_DI_CHARGE%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidDiCharge" />
  
  <content:contentBean
  contentId="<%=ContentConstants.BLANK_NEW_DI_CHARGE%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="blankDiCharge" />

  <content:contentBean
  contentId="<%=ContentConstants.ASSET_CHARGE_AND_OTHER_CHAGE_MUST_BE_SAME%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidAssetCharge_OtherCharge" />
  
  <content:contentBean
  contentId="<%=ContentConstants.INVALID_NEW_PRICING_REQUEST%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidNewPricingRequest" />

  <content:contentBean
  contentId="<%=ContentConstants.ERROR_CALCLLATE_BAC%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="bacRateNotCalculated" />
  
  <content:contentBean
  contentId="<%=ContentConstants.ERROR_CALCULATE_DI%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="diChargeNotCalculated" />
  
  <content:contentBean
  contentId="<%=ContentConstants.NONZERO_ANNUALIZED_TPA_ABF%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="invalidAnnualizedTpaABF" />
			 
			 
<html>
<head>
<title></title>

<script type="text/javascript" >

$(document).ready(function() {

		var plusIcon = "/assets/unmanaged/images/plus_icon.gif";
		var minusIcon = "/assets/unmanaged/images/minus_icon.gif";
		$('#changeBACSectionImg').attr('src',plusIcon);
		$('#changeBACSection').hide();
		$('#changeDCSectionImg').attr('src',plusIcon);
		$('#changeDCSection').hide();
		var errors = $('#errorKeys');
		for (var i = 0; i < errors.length; i++) {
			$('#error_'+errors[i]).addClass("redTextBold");
			if(errors[i] == 'blendedAssetCharge'){
				$('#changeBACSection').clicktoggle();
				$('#changeBACSectionImg').attr('src',minusIcon);
			}
			if(errors[i] == 'discontinuanceFee'){
				$('#changeDCSection').clicktoggle();
				$('#changeDCSectionImg').attr('src',minusIcon);
			}
		} 

		$('input[type="text"]').on("focus",function(){
		$(this).removeClass("redTextBold");
		});
		
		$('select').on("focus",function(){
		$(this).removeClass("redTextBold");
		});
		
		var bacRowIndex = $('#changeBACScale tr').length - 5 ;
		$('.changeBACScale_0').removeAttr("disabled");
		var j;
		for(i = 1; i < bacRowIndex; i++) {
		j=i-1;
			if($('.changeBACScale_'+i).attr("value") == null && $('.changeBACScale_'+j).attr("value") == null) {
				$('.changeBACScale_'+i).attr("disabled", "disabled");	
			} else if (Number($('.changeBACScale_'+i).attr("value")) == 999999999.99) {
				$('.changeBACScale_'+i).removeAttr("disabled");
				for(j = i+1;  j <= bacRowIndex ; j++) {
					$('.changeBACScale_'+j).attr("disabled", "disabled");	
				}
				break;
			} else if( $('.changeBACScale_'+i).attr("value") != null ){
			
				for(j = i+1;  j >= 0; j--) {
					$('.changeBACScale_'+j).removeAttr("disabled");
			}
			}
		}
		
		
		$('#changeBACSectionImg').on("click",function() {

			$('#changeBACSection').clicktoggle();
			var newsrc=$(this).attr('src') == plusIcon ? minusIcon : plusIcon ;
			$(this).attr('src',newsrc);
  
		});
	
		$('#addBACRow').on("click",function() {
				var newBandStartsAt;
		        var rowIndex = $('#changeBACScale tr').length - 5 ;
		        var regExp = /^[0-9]\d*(\.\d{1,2})?$/;
				var previousBandEndsAt = $('#changeBACScale_'+(Number(rowIndex)-1)+'_0').attr('value');
				if( rowIndex > 0 && previousBandEndsAt > 0 && previousBandEndsAt < 999999999.99 && regExp.test(previousBandEndsAt)) {
				newBandStartsAt = Number(previousBandEndsAt) + 0.01;
				} else {
				newBandStartsAt=" ";
				}
				$('#newband_'+(rowIndex)).html(newBandStartsAt);
				var newrow ='<tr class="datacell1" id="newBac'+(rowIndex)+'">'+
									'<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
									'<td vAlign="middle" height="25" align="center" colspan="2"><div id="cbs_'+(rowIndex)+'">  </div></td>'+
									'<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
									'<td valign="middle" align="center" height="25"><div id="cbe_'+(rowIndex)+'">  </div></td>'+
									'<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
									'<td valign="middle" align="center" height="25"><div id="newband_'+(rowIndex)+'">' +newBandStartsAt+ '</div></td>'+
									'<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
									'<td valign="middle" align="center" height="25">'+
									'<input class="changeBACScale_'+rowIndex+'" id="changeBACScale_'+rowIndex+'_0" type="text" onblur="onblurevent(this)"/></td>'+
									'<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
									'<td vAlign="middle" height="25" align="center"><div id="cbac_'+(rowIndex)+'">  </div></td>'+
									'<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
									'<td vAlign="middle" height="25" align="center" colspan="2"><input id="changeBACScale_'+rowIndex+'_1" type="text"/></td>'+
									'<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
							  '</tr>';
				if( newBandStartsAt > 0 
							&& newBandStartsAt < 999999999.99 
							&& rowIndex < 10 ) {
							
					$('#changeBACScale tr:last').before(newrow);
					
				} else if (previousBandEndsAt == null 
							|| previousBandEndsAt == 999999999.99 || rowIndex > 9){
				
					//disable 'Add new row' icon
					
				} else if (newBandStartsAt==" " && rowIndex < 10){
				
				$('#changeBACScale tr:last').before(newrow);
				}
			});
					

		$('#addDCRow').on("click",function() {
			var rowIndex = $('#changeDCScale tr').length - 4 ;
			var year = Number(rowIndex) + 1 ;
			var newrow='<tr class="datacell1" id="newDi'+rowIndex+'">'+
								'<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
								'<td vAlign="middle" height="25" align="center" colspan="2">'+ year +'</td>'+
								'<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
								'<td valign="middle" align="center" height="25"></td>'+
								'<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
								'<td vAlign="middle" height="25" align="center" colspan="2"><input id="changeDCScale_'+rowIndex+'" type="text"/></td>'+
								'<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>'+
						'</tr>';
						if (rowIndex < 18 ) {
							$('#changeDCScale tr:last').before(newrow);
						}
							
		});	

			$('#changeDCSectionImg').on("click",function() {

				$('#changeDCSection').clicktoggle();
				var newsrc=$(this).attr('src') == plusIcon ? minusIcon : plusIcon ;
				$(this).attr('src',newsrc);
				  
			});
		});

		function onblurevent(inputObject) {
			var rowIndex = $('#changeBACScale tr').length - 5 ;
			var index = Number($(inputObject).attr('class').substring(15));
			var regExp = /^[0-9]\d*(\.\d{1,2})?$/;
			var newband='';
			if ( regExp.test($(inputObject).attr('value')) && Number($(inputObject).attr('value')) > 0 && 
								Number($(inputObject).attr('value')) < 999999999.99){
				newband = Number($(inputObject).attr('value'))+0.01;
				newband = newband.toFixed(2)
				//$('.changeBACScale_'+index).attr("disabled", "disabled");	
				$('.changeBACScale_'+(index +1)).removeAttr("disabled");
				$("#newband_"+(index +1)).html(newband);
			} else if(Number($(inputObject).attr('value')) == 999999999.99 || $(inputObject).attr('value') == null){
				$("#newband_"+(index +1)).html('');
			}else if ($(inputObject).attr('value').length > 0 ){
				$("#newband_"+(index +1)).html('');
				//$('.changeBACScale_'+(index +1)).removeAttr("disabled");
			}
		}

		function calculateBAC(csrf) {
		var rowIndex = $('#changeBACScale tr').length - 5 ;
		var newVal,arr,temp;
		var arr = [];
		for(i = 0; i < rowIndex; i++) {
			arr[i] = [];
			for(j = 0; j < 2; j++) {
			temp = "changeBACScale_"+i+"_"+j;
			  newVal = $("#changeBACScale_"+i+"_"+j).attr('value');
			  if (newVal != null) {
				
					arr[i][j] = newVal ;
					
				}else {
					arr[i][j] = ' ' ;
					}
						
					}     	 
				}
			$.ajax({
				  url:"/do/fee/hypotheticalTool/?action=calculateBACharge"+"&_csrf="+csrf,
				  type:"post",
				  data: {bacValues : arr},
				  
				  success: function success(data){	
					if(data == 'failure'){
						//$('#bacErrors').html(' ');
						$('#errorDiv').html(' ');
							$('#errorDiv').hide();
						var reportURL = new URL("/do/fee/hypotheticalTool/?action=feeCalculationError&_csrf="+csrf);
						PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
						window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
						}else if(data.length < 12 ) {
						
							$('#errorDiv').html(' ');
							$('#errorDiv').hide();
							$('#changeBACSection').clicktoggle();
							$('#changeBACSectionImg').attr('src',"/assets/unmanaged/images/plus_icon.gif");
							$('#blendedAssetCharge').val(data) ;
							$('table tbody tr td').removeClass("redTextBold");
							$('.changeBACScale_0').removeAttr("disabled");
							var j;
							for(i = 1; i < rowIndex; i++) {
							j=i-1;
								if($('.changeBACScale_'+i).attr("value") == null && $('.changeBACScale_'+j).attr("value") == null) {
									$('.changeBACScale_'+i).attr("disabled", "disabled");	
								} else if (Number($('.changeBACScale_'+i).attr("value")) == 999999999.99) {
									$('.changeBACScale_'+i).removeAttr("disabled");
									for(j = i+1;  j <= rowIndex ; j++) {
										$('.changeBACScale_'+j).attr("disabled", "disabled");	
									}
									break;
								} else if( $('.changeBACScale_'+i).attr("value") != null ){
								
									for(j = i+1;  j >= 0; j--) {
										$('.changeBACScale_'+j).removeAttr("disabled");
								}
								}
							}
							
						} else { 
						var message = '<table id="bac_error_div"><tbody><tr><td class="redText" valign="top"><ul> ';
						var myData = JSON.parse(data);
						$('#errorDiv').show();
						for (x in myData)
						{
						var errorKey = myData[x];
						if(errorKey == <%=ErrorCodes.INVALID_NEW_BAND_ENDS%>){
							message = message+"<li>"+ '<content:getAttribute beanName="invalidNewBanEnds" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.NEW_BAND_ENDS_ZERO%>){
							message = message+"<li>"+ '<content:getAttribute beanName="invalidValueBAC" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.INVALID_LAST_BAND_ENDS%>){
							message = message+"<li>"+ '<content:getAttribute beanName="invalidLastBandValue" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.ONE_OR_MORE_INVALID_NEW_BAND_ENDS%>){
							message = message+"<li>"+ '<content:getAttribute beanName="oneOrMoreInvalidBAC" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.INVALID_BAND_STARTS_AND_ENDS%>){
							message = message+"<li>"+ '<content:getAttribute beanName="invalidBandStartsEnds" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.INVALID_ASSET_CHARGE_RATE%>){
							message = message+"<li>"+ '<content:getAttribute beanName="invalidRate" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
															
						}else if (errorKey == <%=ErrorCodes.EXTRA_ASSET_CHARGE_RATE_ENTRY%>){
							message = message+"<li>"+ '<content:getAttribute beanName="extraRateEntry" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.ONE_OR_MORE_INVALID_ASSET_CHARGE_RATE%>){
							message = message+"<li>"+ '<content:getAttribute beanName="oneOrMoreInvalidRate" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.ASSET_CHARGE_AND_OTHER_CHAGE_MUST_BE_SAME%>){
							message = message+"<li>"+ '<content:getAttribute beanName="invalidAssetCharge_OtherCharge" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.INVALID_NEW_PRICING_REQUEST%>){
							message = message+"<li>"+ '<content:getAttribute beanName="invalidNewPricingRequest" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.ERROR_CALCLLATE_BAC%>){
							message = message+"<li>"+ '<content:getAttribute beanName="bacRateNotCalculated" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.ERROR_CALCULATE_DI%>){
							message = message+"<li>"+ '<content:getAttribute beanName="diChargeNotCalculated" attribute="text" filter="false" escapeJavaScript="true"/> ['+errorKey+']</li>';
							
						}
						}
						message = message+'</ul></td></tr></tbody></table>';
						$('#errorDiv').html(message);
					  }
				  error:function error(data){
				  
							$('#errorDiv').html(' ');
							$('#errorDiv').hide();
						var reportURL = new URL("/do/fee/hypotheticalTool/?action=feeCalculationError&_csrf="+csrf);
						PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
						window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
				  }  
				}	
			}); 
		}
		
		function calculateDC(csrf) {
			var rowIndex = $('#changeDCScale tr').length - 4 ;
			var newVal,arr,temp;
			var arr = [];
			for(i = 0; i < rowIndex; i++) {
				arr[i] = [];
				temp = "changeDCScale_"+i;
				newVal = $("#changeDCScale_"+i).attr('value');
				if (newVal != null) {
				
					arr[i]= newVal ;
					
				}else {
					
					arr[i]= '' ;
					}
					
				} 
				$.ajax({
					  url:"/do/fee/hypotheticalTool/?action=calculateDICharge&_csrf="+csrf,
					  type:"post",
					  data: {dcValues : arr, rowCount : rowIndex },
					  
					  success: function success(data){
						if(data == 'failure'){ 
							$('#errorDiv').html(' ');
							$('#errorDiv').hide();
							var reportURL = new URL("/do/fee/hypotheticalTool/?action=feeCalculationError&_csrf="+csrf);
							PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
							window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
						}else if (data.length < 12) {
						
							$('#changeDCSection').clicktoggle();
							$('#changeDCSectionImg').attr('src',"/assets/unmanaged/images/plus_icon.gif");
							$('#discontinuanceFee').val(data);
							$('table tbody tr td').removeClass("redTextBold");
							$('#errorDiv').html(' ');
							$('#errorDiv').hide();
						} else {
						var message = '<table id="dc_error_div"><tbody><tr><td class="redText"  valign="top"><ul> ';
						var myData = JSON.parse(data);
						$('#errorDiv').show();
						for (x in myData)
						{
						var errorKey = myData[x];
						
						if(errorKey == <%=ErrorCodes.INVALID_NEW_DI_CHARGE%>){
							message = message+"<li>"+ '<content:getAttribute beanName="invalidDiCharge" attribute="text" filter="false" escapeJavaScript="true"/>['+errorKey+']</li>';
							
						}else if (errorKey == <%=ErrorCodes.BLANK_NEW_DI_CHARGE%>){
							message = message+"<li>"+ '<content:getAttribute beanName="blankDiCharge" attribute="text" filter="false" escapeJavaScript="true"/>['+errorKey+']</li>';
							
						}
						}
						message = message+'</ul></td></tr></tbody></table>';
						$('#errorDiv').html(message);
						}
					error:function error(data){
							$('#errorDiv').html(' ');
							$('#errorDiv').hide();
							var reportURL = new URL("/do/fee/hypotheticalTool/?action=feeCalculationError&_csrf="+csrf);
							PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
							window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
					  }  
					}
				}); 
				
		}
		
	function doClearBACValues(csrf) {

		var rowIndex = $('#changeBACScale tr').length - 5 ;
			$.ajax({
				  url:"/do/fee/hypotheticalTool/?action=clearBaCharge&_csrf="+csrf,
				  type:"post",
				  
				  success: function success(data){	
					if(data == 'failure'){
						var reportURL = new URL("/do/fee/hypotheticalTool/?action=feeCalculationError&_csrf="+csrf);
						PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
						window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
						} else {
							$('#bac_error_div').html(' ');
							$('#bac_error_div').hide();
						}
					
				  error:function error(data){
						var reportURL = new URL("/do/fee/hypotheticalTool/?action=feeCalculationError&_csrf="+csrf);
						PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
						window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
				  }  
				}	
			}); 
			for( i = 0; i <  rowIndex ; i++ ) {
				$('#newBac'+i).remove();
			}
			
		$('#changeBACScale :input').val('');
		$('#changeBACScale :input').removeAttr("disabled"); 
		
			for( i = 1; i <  rowIndex ; i++ )
			{
				$('#newband_'+i).html(' ');
				$('.changeBACScale_'+i).attr("disabled", "disabled");	
			}
			$('#blendedAssetCharge').val('') ;
		} 
	
	function doClearDCValues(csrf) {
		var rowIndex = $('#changeDCScale tr').length - 4 ;
		var newVal,arr,temp;
		var arr = [];
		for(i = 0; i < rowIndex; i++) {
				arr[i]= '' ;
				$('#newDi'+i).remove();
			} 
		$.ajax({
			  url:"/do/fee/hypotheticalTool/?action=clearDiCharge&_csrf="+csrf,
			  type:"post",
			  data: {dcValues : arr, rowCount : rowIndex },
			  
			  success: function success(data){
				if(data == 'failure'){ 

					var reportURL = new URL("/do/fee/hypotheticalTool/?action=feeCalculationError&_csrf="+csrf);
					PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
					window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
				} else {
							$('#dc_error_div').html(' ');
							$('#dc_error_div').hide();
						}

			error:function error(data){
					var reportURL = new URL("/do/fee/hypotheticalTool/?action=feeCalculationError&_csrf="+csrf);
					PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
					window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
			  }  
			}
		}); 
		$('#changeDCScale :input').val('');
		$('#discontinuanceFee').val(" ");
	} 
	
	function resolveCsrf(csrf,url) {
		var form = document.createElement("form");
		form.action = url;
		form.method = 'POST';
		form.target = "_blank";

		var csrfInput = document.createElement("input");
		csrfInput.setAttribute("type", "hidden");
		csrfInput.setAttribute("name", "_csrf");
		csrfInput.setAttribute("value", csrf);
		form.appendChild(csrfInput);
		
		document.body.appendChild(form);
		form.submit();
	}
	
	function compareBAC_DC(csrf) {
		var BacRowCount = $('#changeBACScale tr').length - 5 ;
		var newVal,temp;
		var arr = [];
		
		var csrfInput = document.createElement("input");
		csrfInput.setAttribute("type", "hidden");
		csrfInput.setAttribute("name", "_csrf");
		csrfInput.setAttribute("value", csrf);
		document.forms['ipiToolForm'].action.value="compareBAC_DC";
		document.forms['ipiToolForm'].appendChild(csrfInput);
		
		for(i = 0; i < BacRowCount; i++) {
			arr[i] = [];
			for(j = 0; j < 2; j++) {
			temp = "changeBACScale_"+i+"_"+j;
			  newVal = $("#changeBACScale_"+i+"_"+j).attr('value');
			  if (newVal != null) {
				
					arr[i][j] = newVal ;
					
				}else {
					arr[i][j] = ' ' ;
					}
						
					}     	 
				}
				
			var DcRowCount = $('#changeDCScale tr').length - 4 ;
			var newVal,temp;
			var dcArr = [];
			for(i = 0; i < DcRowCount; i++) {
				dcArr[i] = [];
				temp = "changeDCScale_"+i;
				newVal = $("#changeDCScale_"+i).attr('value');
				if (newVal != null) {
				
					dcArr[i]= newVal ;
					
				}else {
					
					dcArr[i]= '' ;
					}
					
				} 				
			$.ajax({
				  url:"/do/fee/hypotheticalTool/?action=compareBAC_DC&_csrf="+csrf,
				  type:"post",
				  data: {bacValues : arr, dcValues : dcArr, BacRowCount :BacRowCount, DcRowCount : DcRowCount },
				  
				  success: function success(data){
					  //resolveCsrf(csrf,"/do/fee/hypotheticalTool/?action=compareBAC_DC");
					document.forms['ipiToolForm'].submit();
				  
				  error:function error(data){
				  //Do Nothing...
				  }
				}				  
			});
		}
	
</script>
</head>
<body>
<c:set var="ipiToolForm1" value="${requestScope.ipiTool}" scope="request"/>
<ps:form action="/do/fee/hypotheticalTool/" modelAttribute="ipiToolForm" name="ipiToolForm">
<form:hidden path="myAction" value="submit"/>
	<table border="0" cellSpacing="0" cellPadding="0" width=760>
		<tbody >
		  <tr>
		    <td width=30>&nbsp;</td>
			  <td>	<br/><br/>		     
					<table border="0" cellSpacing="0" cellPadding="0" width="730">
							<tbody>
								<tr>
									<td vAlign="top" width="730">
										<table border="0" cellSpacing="0" cellPadding="0" width="730">
											<tbody>
												<tr>
													<td colspan="8">
													<b class="redText">
														<div id="errorDiv">
															<content:errors scope="session" />
														</div>
														</b>
													</td>
												</tr>
												<tr class="tablehead">
													<td class="tableheadTD1" height="25" vAlign="middle" colspan="7"><b>Contract Overview</b></td>
													<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												<tr class="datacell1">
													<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colspan="6">
													<table border="0" width="100%" style= "table-layout: fixed;">
															<tbody>
																<tr class="datacell1">
																	<td vAlign="top" width="20%"><b>Contract Number</b></td>
																	<td vAlign="top" width="40%">${userProfile.currentContract.contractNumber}</td>
																	<td vAlign="top" width="20%"><b>Contract Status</b></td>
																	<td vAlign="top" width="20%">${userProfile.currentContract.status}</td>
																</tr>
																<tr class="datacell1">
																	<td vAlign="top" width="20%"><b>Contract Name</b></td>
																	<td vAlign="top" width="40%">${userProfile.currentContract.contractLegalName}</td>
																	<td vAlign="top" width="20%"><b>Effective Date</b></td>
																	<td vAlign="top" width="20%">
																	<c:choose>
																		<c:when test="${userProfile.currentContract.status == 'AC' || userProfile.currentContract.status == 'CF'}">
																			<fmt:formatDate value="${userProfile.currentContract.effectiveDate}" pattern="MMMM dd, yyyy" />
																		</c:when>
																		<c:otherwise>
																			TBD
																		</c:otherwise>
																	</c:choose>
																	</td>
																</tr>
																<tr class="datacell1">
																	<td vAlign="top" width="20%"><b>PIM/CAR Name</b></td>
																	<td vAlign="top" width="40%">${userProfile.currentContract.contractCarName}</td>
																	<td vAlign="top" width="20%"><b>Total Assets</b></td>
																	<td vAlign="top" width="20%">$<fmt:formatNumber type="number" pattern="###,###,###.##" value="${userProfile.currentContract.totalAssets}"/></td>
																</tr>
															</tbody>
														</table></td>
													<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>  
										 <c:forEach var="ipiTool" items="${ipiToolForm1.fieldAttributeValue}">
												<c:if test="${ipiTool.sectionName != 'Summary of New IPI Charges'}">
													<tr class="subsubhead">
														<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
														<td colspan="6"><b>${ipiTool.sectionName}</b></td>
														<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													</tr>
														<c:if test="${ipiTool.sectionName == 'Recovery Methods'}">
															<tr class="datacell1">
																<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																<td colSpan="6">
																	<table border="0" width="100%">
																	<tbody>
																	<c:forEach var="fields" items="${ipiTool.fields}"> 
																			<tr class="datacell1">
																				<td vAlign="middle" width="20%" id="error_${fields.formName}">													
																						${fields.labelText}	
																				</td>
																				<td vAlign="middle" width="10%">${fields.currentValue}</td>
																				<c:choose>
																				<c:when test="${fields.formName == 'contractStmtRm'}">
																				<td vAlign="top" width="70%">
<form:select path="${fields.formName}"><%-- property="${fields.formName}" --%>
																					    <form:option value="">- - Select - -</form:option>
																					    <c:forEach var="recoveryValue" items="${fields.recoveryValues}">
																						<form:option value="${recoveryValue}">${recoveryValue}</form:option>
																						</c:forEach>
</form:select>
																				</td>
																				</c:when>
																				<c:otherwise>
																				<td vAlign="top" width="70%">
<form:select path="${fields.formName}" ><%-- property="${fields.formName}" --%>
																					    <form:option value="">- - Select - -</form:option>
																						<c:forEach var="recoveryValue" items="${fields.recoveryValues}">
																						<form:option value="${recoveryValue}">${recoveryValue}</form:option>
																						</c:forEach>
</form:select>
																				</td>
																				</c:otherwise>
																				</c:choose>
																			</tr>
																		</c:forEach>
																	</tbody>
																	</table>
																</td>
																<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															</tr>
															<tr class="tablesubhead">
																<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																<td vAlign="top" height="25" colspan="2"  width="43%"></td>
																<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																<td valign="middle"  height="25" align="center"><b>Current Pricing</b></td>
																<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																<td vAlign="middle" height="25" align="center" width="35%"><b>New Pricing Request</b></td>
																<td class="databorder" height="25" ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															</tr> 
														</c:if>
														<c:if test="${ipiTool.sectionName != 'Recovery Methods'}">
															<c:forEach var="fields" items="${ipiTool.fields}" varStatus="loop"> 
																<c:choose>
																		<c:when test="${fields.formName == 'dioDiscount'}">
																			<tr style="display:none">
																		</c:when>
																		<c:otherwise>
																			<tr class="datacell1">												
																		</c:otherwise>
																	</c:choose>	
																	<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																	<td vAlign="middle" height="25" align="left" colspan="2" id="error_${fields.formName}">								
																			${fields.labelText}	
																	</td>
																	<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																	<td vAlign="middle" align=center height="25">
																			<c:if test="${fields.unit == '$'}">
																				${fields.unit}<fmt:formatNumber type="number" pattern="###,###,###.##" value="${fields.currentValue}" maxFractionDigits="${fields.scale}"  minFractionDigits="${fields.scale}"/>
																			</c:if>
																			<c:if test="${fields.unit == '%'}">
																				<fmt:formatNumber value="${fields.currentValue}" maxFractionDigits="${fields.scale}"/>${fields.unit}
																			</c:if>
																	</td>
																	<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																	<td vAlign="middle" height="25" align=center>
																		<c:choose>
																				<c:when test="${fields.formName == 'discontinuanceFee' || fields.formName == 'blendedAssetCharge' }">
																					<form:input path="${fields.formName}" disabled="true" id="${fields.formName}" />
																				</c:when>
																				<c:when test="${fields.formName == 'annualizedTPAAssetBasedFee'}">
																					<form:input path="${fields.formName}" />
																				</c:when>
																				<c:otherwise>
																					<form:input path="${fields.formName}" />
																				</c:otherwise>
																		</c:choose>
																	</td>
																	<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																</tr>
																<c:if test="${loop.first && ipiTool.sectionName== 'John Hancock Charges'}">
																<tr class="datacell1" valign="top">
																	<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"> </td>
																	<td colspan="6">
																	<div id="bacSectionImg">
											                       		<img id="changeBACSectionImg" class="icon" src="/assets/unmanaged/images/plus_icon.gif">&nbsp;Change BAC Scale
											                        </div>
																	<div id="changeBACSection">	
																		<jsp:include flush="true" page="changeBACScale.jsp"></jsp:include>					
																	</div>	
																	</td>
																	<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"> </td>
																</tr>
																</c:if>
																<c:if test="${loop.last && ipiTool.sectionName== 'John Hancock Charges'}">
																<tr class="datacell1" valign="top">
																<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																<td colspan="6">
																	<div>
										                               	<img id="changeDCSectionImg" class="icon">&nbsp;Change Discontinuance Charge Scale
										                            </div>	
																	<div id="changeDCSection">	
																		<jsp:include flush="true" page="changeDCScale.jsp"></jsp:include>					
																	</div>
																</td>
																<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															</tr>
															</c:if>
															</c:forEach>
														</c:if>
														</c:if>
												</c:forEach>
												<tr>
													<td class="databorder" colSpan="8"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												<tr>
													<td colSpan=5><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td class="dark_grey_color" vAlign=middle colSpan="4"></td>
													<td class="dark_grey_color" colSpan="2" align="right"></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
						<br/>
						<c:if test="${not param.printFriendly}">
							<table border="0" cellSpacing="0" cellPadding="0">
								<tr>
									<td width="400" align="right"> </td>
									<td width="140" align="right"> </td>
									<td width="140" align="right">
<input type="button" onclick="compareBAC_DC('${_csrf.token}');" class="button134" value="Calculate"/>
									</td>
								</tr>
							</table>
    					</c:if>
			 	 </td>
	     	</tr>
			<tr>
				<td width=30>
					&nbsp;
				</td>
			   <td>
				 <p><content:pageFooter beanName="layoutPageBean"/></p>
				 <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
				 <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
			   </td>
			</tr>
		</tbody>
	</table>
	</ps:form>
</body>
</html>

<!-- Author: Yeshwanth -->
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %> 

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.noticemanager.BuildYourPackageNoticeManagerForm"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 


<c:set var="printFriendly" value="${param.printFriendly}" />
<%-- CMA Contents  --%>
<content:contentBean contentId="<%=ContentConstants.ICC_YEAR_END_WARNING_MESSAGE_KEY%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="yearEndWarningMessage" />
<content:contentBean contentId="<%=ContentConstants.ICC_WARNING_MESSAGE_KEY%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningMessage" />
<content:contentBean contentId="<%=ContentConstants.NMC_BUILD_TABLE_TITLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="tableTitle"/>
<content:contentBean contentId="<%=ContentConstants.NMC_MAILING_NAME_LABLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="mailingNameLable"/>
<content:contentBean contentId="<%=ContentConstants.NMC_SELECTION_INDICATOR_LABLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="selectionIndicatorLable"/>
<content:contentBean contentId="<%=ContentConstants.NMC_DOCUMENT_SELECTION_LABLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="documentSelectionILable"/>
<content:contentBean contentId="<%=ContentConstants.NMC_ADDRESS_FILE_LABLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="addressFileLable"/>
<content:contentBean contentId="<%=ContentConstants.NMC_PLACE_ORDER_LABLE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="placeOrderLable"/>
<content:contentBean contentId="<%=ContentConstants.NMC_SECTION_INTRO%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="sectionIntro"/>
<content:contentBean contentId="<%=ContentConstants.NMC_404_DESCRIPTION%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="description404"/>
<content:contentBean contentId="<%=ContentConstants.NMC_ICC_DESCRIPTION%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="descriptionICC"/>
<content:contentBean contentId="<%=ContentConstants.NMC_DOCUMENT_NAME_DESCRIPTION%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="descriptionDocument"/>
<content:contentBean contentId="<%=ContentConstants.NMC_ORDER_BUTTON_HOVER_CONTENT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="placeOrder"/>
<content:contentBean contentId="<%=ContentConstants.NMC_SORT_TOOL_TIP%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="sortToolTip"/>
<content:contentBean contentId="<%=ContentConstants.NMC_CENSUS_LINK_TOOL_TIP%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="censusLinkToolTip"/>
<content:contentBean contentId="<%=ContentConstants.NMC_CENSUS_DOWNLOAD_LINK_TOOL_TIP%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="censusDownloadLinkToolTip"/>
<content:contentBean contentId="<%=ContentConstants.NMC_MAILING_SELECTION_THANKYOU%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="mailingSelectionThankyou"/>
<content:contentBean contentId="<%=ContentConstants.NMC_RESET_BUTTON_TOOL_TIP%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="resetToolTip"/>
<content:contentBean contentId="<%=ContentConstants.NMC_ORDER_BUTTON_TOOL_TIP%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="orderToolTip"/>
<content:contentBean contentId="<%=ContentConstants.NMC_PLACE_ORDER_WAIT_MESSAGE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="orderWaitMessage"/>


	<script>

	  var utilities = {
	      
	      // Asynchronous request call to the server. 
	      doAsyncRequest : function(actionPath, callbackFunction, data) {
	          YAHOO.util.Connect.setForm(document.buildYourPackageForm);
	          // Make a request
	          var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction, data);
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

	   // Shows loading panel message for order
	      showWaitPanelOrder : function() {
	          waitPanel = document.getElementById("wait_c");
	          if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
	              loadingPanel = new YAHOO.widget.Panel("wait",  
	                                  {   width: "250px", 
	                                      height:"67px",
	                                      fixedcenter: true, 
	                                      close: false, 
	                                      draggable: false, 
	                                      zindex:4,
	                                      modal: true,
	                                      visible: false,
	                                      constraintoviewport: true
	                                  } 
	                              );
	              loadingPanel.setBody("<span style='float: right; width: 161px; height: 100%; padding: 0px 5px 5px;'>"+ orderWaitMessage +"</span><div style='margin:0 auto;'><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/></div>");
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
	          
	   };

	function catchEnter(){
		if($('#mailingIndicatorYes').is(':checked') || $('#mailingIndicatorEmail').is(':checked') || $('#mailingIndicatorMailAndEmail').is(':checked')) { 
			doSubmit('order');
		}else{
			doDownload('download');
		}
	}   
	function doView(action, plandocument, documentId, contractId, selectedDocumentName) {
		document.forms['buildYourPackageForm'].selectedDocumentName.value = selectedDocumentName;
		document.forms['buildYourPackageForm'].planNoticedocument.value = plandocument;
		document.forms['buildYourPackageForm'].contractId.value = contractId;
		document.forms['buildYourPackageForm'].documentId.value = documentId;
		document.getElementsByName("task")[0].value= action;
		utilities.showWaitPanel();
  		utilities.doAsyncRequest("/do/noticemanager/buildyourpackage/?task=viewNotice&documentId=" + documentId, callback_checkCustomPlanNoticeGenerated);
  	}
    // Call back handler to Check whether Custom plan Report Generation is complete.
	var callback_checkCustomPlanNoticeGenerated =    {
		success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/noticemanager/buildyourpackage/?task=fetchPdf";
			}
			if(o.responseText.substring(0, 15) == 'censusGenerated' || o.responseText.substring(0, 15) == 'employGenerated'){
				var split = o.responseText.split("&");
				document.forms['buildYourPackageForm'].countOfParticipantInCensusGenerated.value = split[1];	   	
				document.forms['buildYourPackageForm'].planNoticedocument.value = "customPlanDocument";	   	
				// For subbmitting the order and selected documents list, implimented just before the submit every time.
			    selecteddocuments = "";
				orderofdocuments = "";
				selectedJHdocuments = "";
				$(".selectedcheckbox:checked").each(function(){
					selecteddocuments += $(this).attr( "value" ) + ",";
				});	
				$(".selectedJHcheckbox:checked").each(function(){
					selectedJHdocuments += $(this).attr( "value" ) + ",";
				});	
				$(".ordercheckbox").each(function(){
					orderofdocuments += $(this).attr( "value" ) + ",";
				});	
				orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
				//alert("doc: "+ selecteddocuments + " --- jh ---" + selectedJHdocuments);
				if(selectedJHdocuments !=""){
					selectedJHdocuments = selectedJHdocuments.substring(0, selectedJHdocuments.length - 1);
				}
				if(selecteddocuments !=""){
					selecteddocuments = selecteddocuments.substring(0, selecteddocuments.length - 1);
				}
				document.forms['buildYourPackageForm'].documentsSelectedJH.value = selectedJHdocuments;
				document.forms['buildYourPackageForm'].documentsSelected.value = selecteddocuments;
				document.forms['buildYourPackageForm'].censusInfoPresent.value = "true";	
				//alert("doc: "+ selecteddocuments + " --- jh ---" + selectedJHdocuments);
				document.forms['buildYourPackageForm'].documentsOrder.value = orderofdocuments;
				// Submit the form
			   	document.buildYourPackageForm.submit();
			   	return;
			}

			if(o.responseText == 'noCensusInfo' || o.responseText =='noEmployInfo'){
				
				document.forms['buildYourPackageForm'].planNoticedocument.value = "customPlanDocument";	   	
				document.forms['buildYourPackageForm'].censusInfoPresent.value = "false";	   	
				// For subbmitting the order and selected documents list, implimented just before the submit every time.
			    selecteddocuments = "";
				orderofdocuments = "";
				selectedJHdocuments = "";
				$(".selectedcheckbox:checked").each(function(){
					selecteddocuments += $(this).attr( "value" ) + ",";
				});	
				$(".selectedJHcheckbox:checked").each(function(){
					selectedJHdocuments += $(this).attr( "value" ) + ",";
				});	
				$(".ordercheckbox").each(function(){
					orderofdocuments += $(this).attr( "value" ) + ",";
				});	
				orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
				//alert("doc: "+ selecteddocuments + " --- jh ---" + selectedJHdocuments);
				if(selectedJHdocuments !=""){
					selectedJHdocuments = selectedJHdocuments.substring(0, selectedJHdocuments.length - 1);
				}
				if(selecteddocuments !=""){
					selecteddocuments = selecteddocuments.substring(0, selecteddocuments.length - 1);
				}
				document.forms['buildYourPackageForm'].documentsSelectedJH.value = selectedJHdocuments;
				document.forms['buildYourPackageForm'].documentsSelected.value = selecteddocuments;
				//alert("doc: "+ selecteddocuments + " --- jh ---" + selectedJHdocuments);
				document.forms['buildYourPackageForm'].documentsOrder.value = orderofdocuments;
				// Submit the form
			   	document.buildYourPackageForm.submit();
			}else{
				utilities.hideWaitPanel();
			}
			},
	    cache : false,
	    failure : utilities.handleFailure
	};

	var callback_checkpdfGenerated =    {
			success:  function(o) { 
				if(o.responseText == 'pdfGenerated'){
					window.location.href = "/do/noticemanager/buildyourpackage/?task=fetchPdf";
				}
				else if(o.responseText == 'ereportsFailure' || o.responseText == 'mergeFailure'){
					window.location.href = "/do/noticemanager/buildyourpackage/?task=populateErrors";
					//var reportURL = new URL("/do/planAndInvestmentNotice/?action=openErrorPdf");
					//PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
		        	//window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
				}
				//TODO what to do is pdf not generated
				else{
					window.location.href = "/do/noticemanager/buildyourpackage/?task=populateErrors";
				}
				utilities.hideWaitPanel();
			},
		    cache : false,
		    failure : utilities.handleFailure
		};

	function doterms(action){
	   	document.getElementsByName("task")[0].value= "termsOfUse";
	   	if(action == "download"){
	   		// For subbmitting the order and selected documents list, implimented just before the submit every time.
		    selecteddocuments = "";
			orderofdocuments = "";
			selectedJHdocuments = "";
			$(".selectedcheckbox:checked").each(function(){
				selecteddocuments += $(this).attr( "value" ) + ",";
			});	
			$(".selectedJHcheckbox:checked").each(function(){
				selectedJHdocuments += $(this).attr( "value" ) + ",";
			});	
			$(".ordercheckbox").each(function(){
				orderofdocuments += $(this).attr( "value" ) + ",";
			});	
			orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
			
			if(selectedJHdocuments !=""){
				selectedJHdocuments = selectedJHdocuments.substring(0, selectedJHdocuments.length - 1);
			}
			if(selecteddocuments !=""){
				selecteddocuments = selecteddocuments.substring(0, selecteddocuments.length - 1);
			}
			orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);

			document.forms['buildYourPackageForm'].documentsSelectedJH.value = selectedJHdocuments;
			document.forms['buildYourPackageForm'].documentsSelected.value = selecteddocuments;
			document.forms['buildYourPackageForm'].documentsOrder.value = orderofdocuments;
			document.forms['buildYourPackageForm'].script.value = "download";
				
	   	}
	   	if(action == "order"){
	   		// For subbmitting the order and selected documents list, implimented just before the submit every time.
		    selecteddocuments = "";
			orderofdocuments = "";
			selectedJHdocuments = "";
			$(".selectedcheckbox:checked").each(function(){
				selecteddocuments += $(this).attr( "value" ) + ",";
			});	
			$(".selectedJHcheckbox:checked").each(function(){
				selectedJHdocuments += $(this).attr( "value" ) + ",";
			});	
			//selecteddocuments = selecteddocuments.substring(0, selecteddocuments.length - 1);
			$(".ordercheckbox").each(function(){
				orderofdocuments += $(this).attr( "value" ) + ",";
			});	
			orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
			
			if(selectedJHdocuments !=""){
				selectedJHdocuments = selectedJHdocuments.substring(0, selectedJHdocuments.length - 1);
			}
			if(selecteddocuments !=""){
				selecteddocuments = selecteddocuments.substring(0, selecteddocuments.length - 1);
			}
			orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
			document.forms['buildYourPackageForm'].documentsSelectedJH.value = selectedJHdocuments;
			document.forms['buildYourPackageForm'].documentsSelected.value = selecteddocuments;
			document.forms['buildYourPackageForm'].documentsOrder.value = orderofdocuments;
			document.forms['buildYourPackageForm'].script.value = "order";
	   	}
	   	document.buildYourPackageForm.submit();
	}
	
	function doDownload(action){
		document.forms['buildYourPackageForm'].planNoticedocument.value = "customPlanDocument";
	   	document.getElementsByName("task")[0].value= action;
	    
		// For subbmitting the order and selected documents list, implimented just before the submit every time.
	    selecteddocuments = "";
		orderofdocuments = "";
		selectedJHdocuments = "";
		$(".selectedcheckbox:checked").each(function(){
			selecteddocuments += $(this).attr( "value" ) + ",";
		});	
		$(".selectedJHcheckbox:checked").each(function(){
			selectedJHdocuments += $(this).attr( "value" ) + ",";
		});	
		// for validation when no documents are selected
		if(selecteddocuments == "" && selectedJHdocuments == ""){
			document.forms['buildYourPackageForm'].documentsSelectedJH.value = "";
			document.forms['buildYourPackageForm'].documentsSelected.value = "";
			//document.buildYourPackageForm.submit();
	    }
		$(".ordercheckbox").each(function(){
			orderofdocuments += $(this).attr( "value" ) + ",";
		});	
		orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
		
		if(selectedJHdocuments !=""){
			selectedJHdocuments = selectedJHdocuments.substring(0, selectedJHdocuments.length - 1);
		}
		if(selecteddocuments !=""){
			selecteddocuments = selecteddocuments.substring(0, selecteddocuments.length - 1);
		}
		orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);

		document.forms['buildYourPackageForm'].documentsSelectedJH.value = selectedJHdocuments;
		document.forms['buildYourPackageForm'].documentsSelected.value = selecteddocuments;
		document.forms['buildYourPackageForm'].documentsOrder.value = orderofdocuments;

		document.getElementsByName("task")[0].value= action;
		utilities.showWaitPanel();
  		utilities.doAsyncRequest("/do/noticemanager/buildyourpackage/", callback_checkpdfGenerated);
	}

	
	function doSubmit(action) {
		utilities.showWaitPanelOrder();
		document.getElementsByName("task")[0].value= action;
		var selectedCensusVal = "";
		var selected = $("input[name='fileType']:checked");
		if (selected.length > 0) {
			selectedCensusVal = selected.val();
		}
		if(selectedCensusVal == "employee"){
			utilities.doAsyncRequest("/do/participant/participantAddresses?task=downloadEligibleEmployeeAdressFile&ext=.csv", callback_checkCustomPlanNoticeGenerated);
		}
		else if(selectedCensusVal == "existing"){
			utilities.doAsyncRequest("/do/participant/participantAddresses?task=fetchParticipantAddress&ext=.csv", callback_checkCustomPlanNoticeGenerated);
		}
		else{

			document.forms['buildYourPackageForm'].planNoticedocument.value = "customPlanDocument";	   	
			// For subbmitting the order and selected documents list, implimented just before the submit every time.
		    selecteddocuments = "";
			orderofdocuments = "";
			selectedJHdocuments = "";
			$(".selectedcheckbox:checked").each(function(){
				selecteddocuments += $(this).attr( "value" ) + ",";
			});	
			$(".selectedJHcheckbox:checked").each(function(){
				selectedJHdocuments += $(this).attr( "value" ) + ",";
			});	
			//selecteddocuments = selecteddocuments.substring(0, selecteddocuments.length - 1);
			$(".ordercheckbox").each(function(){
				orderofdocuments += $(this).attr( "value" ) + ",";
			});	
			orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
			
			if(selectedJHdocuments !=""){
				selectedJHdocuments = selectedJHdocuments.substring(0, selectedJHdocuments.length - 1);
			}
			if(selecteddocuments !=""){
				selecteddocuments = selecteddocuments.substring(0, selecteddocuments.length - 1);
			}
			orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
			document.forms['buildYourPackageForm'].documentsSelectedJH.value = selectedJHdocuments;
			document.forms['buildYourPackageForm'].documentsSelected.value = selecteddocuments;
			document.forms['buildYourPackageForm'].documentsOrder.value = orderofdocuments;
			// Submit the form
		   	document.buildYourPackageForm.submit();
		}
		
	    
	}
	// showing a pop up when ereports or merging fails in placing order
	$(document).ready(function(){
		var ereportError = document.forms['buildYourPackageForm'].ereportError.value;
		var mergeError = document.forms['buildYourPackageForm'].mergeError.value;
		// setting the values to false so whjen it is submitted again the pop wont show
		// nothing but clearing error indicators
		document.forms['buildYourPackageForm'].ereportError.value = false;
		document.forms['buildYourPackageForm'].mergeError.value = false;
		if(ereportError == "true" || mergeError == "true"){
			//var reportURL = new URL("/do/planAndInvestmentNotice/?action=openErrorPdf");
			//PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
        	//window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
		}

	});
	var inputsChanged = false;
	$(document).ready(function(){
		$("input").on("change",function(){
			inputsChanged = true;
			$('#resetContainer .twodisabled').hide();$('#resetContainer').addClass("enabledButton");
			disabledInd = false;
		});
		if($("#mailingIndicatorNo:checked").length > 0){
			$(".mailingyes").hide();
			$(".mailingSelectionThankyou").show();
			$(".downloadbutton").show();
			$(".orderbutton").hide();
		}
		$("tr.sort").last().find("td.up a").hide();
		
	});
	var disabledInd = true;
	$(document).ready(function(){
		var sortAppliedInd = document.forms['buildYourPackageForm'].sortAppliedInd.value;
		var errorsInd = document.forms['buildYourPackageForm'].errorsInd.value;
		var change = isDefaultChanged();
		if(sortAppliedInd == "true" || errorsInd == "true" || change == true ){
			$("#cancel").attr("disabled",false);
			$('#resetContainer .twodisabled').hide();$('#resetContainer').addClass("enabledButton");
			disabledInd = false;
		}else{
			$("#cancel").attr("disabled","true");
			disabledInd = true;
		}
		$("input").on("change",function(){
			$("#cancel").attr("disabled",false);
			$('#resetContainer .twodisabled').hide();$('#resetContainer').addClass("enabledButton");
			disabledInd = false;
		});
		if (${buildYourPackageForm.internalUser ==true}){			
			$('#resetContainer').removeClass("enabledButton");
		}
	});
	
	function doReset(){
		if(disabledInd) return;
		var sortAppliedInd = document.forms['buildYourPackageForm'].sortAppliedInd.value;
		var errorsInd = document.forms['buildYourPackageForm'].errorsInd.value;
		if(sortAppliedInd == "true" || errorsInd == "true"){
			utilities.showWaitPanel();
			document.getElementsByName("task")[0].value= "reset";
			document.buildYourPackageForm.submit();
		}
		var change = isDefaultChanged();
		if(change){
			utilities.showWaitPanel();
			document.getElementsByName("task")[0].value= "reset";
			document.buildYourPackageForm.submit();
		}
		
	}
	  

	// Call back handler to Check whether ICC Report Generation is complete.
	var callback_checkIccGenerated =    {
		success:  function(o) { 
			//if(true){
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/iccReport/";
			}else{
				var reportURL = new URL("/do/iccReport/?action=openErrorPdf");
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
			}
			utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};
		
    

	  
	$(document).ready(function(){
		// By default the section to show the mailing name and census address will be disabled
		if (document.forms['buildYourPackageForm'].mailingSelectionIndicator.value == ""){
			$(".mailingyes").hide();
		}
		
		selectedJHdocuments = document.forms['buildYourPackageForm'].documentsSelectedJH.value;
		selecteddocuments = document.forms['buildYourPackageForm'].documentsSelected.value;
		if(selecteddocuments !=""){
			var individual = selecteddocuments.split(",");
			for (i = 0; i < individual.length; i++) {
			    $( "input[value='"+individual[i]+"']" ).prop( "checked", true );
			}
		}
		if(selectedJHdocuments !=""){
			var individual = selectedJHdocuments.split(",");
			for (i = 0; i < individual.length; i++) {
				$( "input[value='"+individual[i]+"']" ).prop( "checked", true );
			}
			var planhighlight = false;
			var planinvestment = false;
			var icc = false;
			$(".selectedJHcheckbox:checked").each(function(){
				if($(this).attr( "value" ) == "planhighlight")  planhighlight = true;
				if($(this).attr( "value" ) == "planinvestment")  planinvestment = true;
				if($(this).attr( "value" ) == "icc")  icc = true;
			});
			
			if(planinvestment){
				$( "input[value='icc']" ).prop( "checked", false ).attr('disabled','disabled');
			}else{
				$( "input[value='icc']" ).removeAttr('disabled');
			}
			
			if(icc){
				$( "input[value='planinvestment']" ).prop( "checked", false ).attr('disabled','disabled');
			}
			else{
				$( "input[value='planinvestment']" ).removeAttr('disabled');
			}
		}
		$(".selectedJHcheckbox").on("click",function(){
			var planhighlight = false;
			var planinvestment = false;
			var icc = false;
			$(".selectedJHcheckbox:checked").each(function(){
				if($(this).attr( "value" ) == "planhighlight")  planhighlight = true;
				if($(this).attr( "value" ) == "planinvestment")  planinvestment = true;
				if($(this).attr( "value" ) == "icc")  icc = true;
			});
			
			if(planinvestment){
				$( "input[value='icc']" ).prop( "checked", false ).attr('disabled','disabled');
			}else{
				$( "input[value='icc']" ).removeAttr('disabled');
			}
			
			if(icc){
				$( "input[value='planinvestment']" ).prop( "checked", false ).attr('disabled','disabled');
			}
			else{
				$( "input[value='planinvestment']" ).removeAttr('disabled');
			}
		});	
		$("#mailingIndicatorYes,#mailingIndicatorEmail,#mailingIndicatorMailAndEmail").on("click",function(){
				$(".mailingyes").show();
				$(".downloadbutton").hide();
				$(".orderbutton").show();
				$(".mailingSelectionThankyou").hide();
				$( ".w14two" ).removeClass("w14no").addClass("w14yes");
		});
		
		$("#mailingIndicatorNo").on("click",function(){
			$(".mailingyes").hide();
			$(".downloadbutton").show();
			$(".orderbutton").hide();
			$( ".w14two" ).removeClass("w14yes").addClass("w14no");
			$(".mailingSelectionThankyou").show();			
		});
		
		$(".viewDocument").on("click",function(e){
			e.preventDefault();
		});

		//dragging functionality
		//$("#sortableTable").tableDnD();

		
		// sorting functionality
		//$("#TableID").dataTable({"paging":   false,"ordering": false, "info":     false,"bFilter": false, "bInfo": false,"bPaginate": false}).rowReordering();

		// functionality for storing thedocument order
		var selecteddocuments ="--";
		var orderofdocuments ="--";
				
		$(".datacell1").on("click",function(){
			//alert($( ".selectedcheckbox:checked" ).length);
			
		}); 
		var LinkProtectionEnabled = false;
		// Alert users if they make any cahges in the sort and tryning to navigate
		$("a").not(".exempt").not('[target="_blank"]').not('[href^="#"]').not('a:contains("Print report")').on("click",function(e){
			e.preventDefault();
			if(LinkProtectionEnabled === true){
				return;
			}
			LinkProtectionEnabled = true;
			var sortAppliedInd = document.forms['buildYourPackageForm'].sortAppliedInd.value;
			var DefaultChanged = isDefaultChanged();
			link = $(this).attr('href');
			if(sortAppliedInd == "true"){
				var message = "The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return."; 
				if(confirm(message)){
					//window.location.href = link;
					collectDataTosession();
				}
			}	
			else{
				collectDataTosession();
			}
		});
		function collectDataTosession(){
			document.forms['buildYourPackageForm'].planNoticedocument.value = "customPlanDocument";
		    
			// For subbmitting the order and selected documents list, implimented just before the submit every time.
		    selecteddocuments = "";
			orderofdocuments = "";
			selectedJHdocuments = "";
			$(".selectedcheckbox:checked").each(function(){
				selecteddocuments += $(this).attr( "value" ) + ",";
			});	
			$(".selectedJHcheckbox:checked").each(function(){
				selectedJHdocuments += $(this).attr( "value" ) + ",";
			});	
			// for validation when no documents are selected
			if(selecteddocuments == "" && selectedJHdocuments == ""){
				document.forms['buildYourPackageForm'].documentsSelectedJH.value = "";
				document.forms['buildYourPackageForm'].documentsSelected.value = "";
				//document.buildYourPackageForm.submit();
		    }
			$(".ordercheckbox").each(function(){
				orderofdocuments += $(this).attr( "value" ) + ",";
			});	
			orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);
			
			if(selectedJHdocuments !=""){
				selectedJHdocuments = selectedJHdocuments.substring(0, selectedJHdocuments.length - 1);
			}
			if(selecteddocuments !=""){
				selecteddocuments = selecteddocuments.substring(0, selecteddocuments.length - 1);
			}
			orderofdocuments = orderofdocuments.substring(0, orderofdocuments.length - 1);

			document.forms['buildYourPackageForm'].documentsSelectedJH.value = selectedJHdocuments;
			document.forms['buildYourPackageForm'].documentsSelected.value = selecteddocuments;
			document.forms['buildYourPackageForm'].documentsOrder.value = orderofdocuments;

			document.getElementsByName("task")[0].value= "submitToSession";
	  		utilities.doAsyncRequest("/do/noticemanager/buildyourpackage/?task=submitToSession", callback_session);
		}

		var callback_session =    {
				success:  function(o) { 
					LinkProtectionEnabled = false;
					window.location.href = link;
				},
			    cache : false,
			    failure : function() { 
					LinkProtectionEnabled = false;
				    window.location.href = link;
				}
			};
			
		$("form[name='QuickLinksForm'] select").attr("onchange",'');
		$("form[name='QuickLinksForm'] select").on("change",function(){
			if(LinkProtectionEnabled === true){
				return;
			}
			LinkProtectionEnabled = true;
			var sortAppliedInd = document.forms['buildYourPackageForm'].sortAppliedInd.value;
			var DefaultChanged = isDefaultChanged();
			var value = $(this).val();
			link = value;
			if(sortAppliedInd == "true"){
				var message = "The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return."; 
				if(confirm(message)){	
					if(value != "#" && value != ""){
						collectDataTosession();
				  	}	
				}
			}
			else{
				if(value != "#" && value != ""){
					collectDataTosession();
			  	}
			}
		});
		
	});

	function isDefaultChanged(){
		if($("input[type=checkbox]:checked").length != 0){
			return true;
		}
		if($("input[value=existing]:checked").length != 0 || $("input[value=new]:checked").length != 0){
			return true;
		}
		if($("input[value=true]:checked").length != 1){
			return true;
		}
		if($("input[name=mailingName]").val() != ""){
			return true;
		}
		return false;
	}
	
	function doCustomSort(methodname,sortTypeArrow,documentId){
		 document.forms['buildYourPackageForm'].documentId.value = documentId;
		 document.forms['buildYourPackageForm'].sortTypeArrow.value = sortTypeArrow+","+documentId;
		 document.getElementsByName("task")[0].value= methodname; 
		 isCustomSort=true;
		 document.forms['buildYourPackageForm'].submit();
		
	}

	   
	// Call back handler to Check whether ICC Report Generation is complete.
	var callback_checkPiNoticeGenerated =    {
			success:  function(o) { 
			if(o.responseText == 'pdfGenerated'){
				window.location.href = "/do/planAndInvestmentNotice/";
			}else{
				var reportURL = new URL("/do/planAndInvestmentNotice/?action=openErrorPdf");
				PDFWindowFeatures = "channelmode=no,directories=no,fullscreen=no,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,titlebar=yes,toolbar=no";
	        	window.open(reportURL.encodeURL(),"_blank",PDFWindowFeatures+",height=600,width=800,left=0,top=0",false);
			}
			utilities.hideWaitPanel();
			},
	    cache : false,
	    failure : utilities.handleFailure
	};

</script>
<c:if test="${buildYourPackageForm.script =='download'}">
<script>
	var fromPage = "";
	$(document).ready(function(){
		//alert("download");
		fromPage =document.forms['buildYourPackageForm'].fromPage.value; 
		document.forms['buildYourPackageForm'].script.value = ''; 
		if(fromPage == "terms"){
			doDownload('download');
			//alert("download");
		}
	});
</script>
</c:if>
<c:if test="${buildYourPackageForm.script =='order'}">
<script>
	$(document).ready(function(){
		//alert("order");
		fromPage =document.forms['buildYourPackageForm'].fromPage.value; 
		document.forms['buildYourPackageForm'].script.value = '';
		if(fromPage == "terms"){
			doSubmit('order');
			//alert("order");
		}
	});
</script>
</c:if>
	<style>
		.downloadbutton{
			display: none;
		}
		.datacell2 {
		    padding: 2px;
		    font-size: 11px;
		    background: none;
		}
		.JHDoc {
			background-color: #DCECF1;
		}		
	</style>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<ps:form cssStyle="margin-bottom:0;" method="POST" onsubmit="catchEnter(); return false;"
	action="/do/noticemanager/buildyourpackage/"  enctype="multipart/form-data" modelAttribute="buildYourPackageForm" name="buildYourPackageForm" >
	<input  type="hidden" name="task">
<form:hidden path="documentId" />
<form:hidden path="planNoticedocument" />
<form:hidden path="documentId" />
<form:hidden path="contractId" />
<form:hidden path="documentsSelected" />
<form:hidden path="documentsSelectedJH" />
<form:hidden path="documentsOrder" />
<form:hidden path="documentId" />
<form:hidden path="sortTypeArrow" />
<form:hidden path="sortAppliedInd" />
<form:hidden path="uploadAndShareTab" />
<form:hidden path="buildYourPackageTab" />
<form:hidden path="buildYourPackageNPTab" />
<form:hidden path="orderStatusTab" />
<form:hidden path="selectedDocumentName" />
<form:hidden path="censusInfoPresent" />
<form:hidden path="countOfParticipantInCensusGenerated" />
<form:hidden path="errorsInd" />
<form:hidden path="script" />
<form:hidden path="fromPage" />
<form:hidden path="ereportError" />
<form:hidden path="mergeError" />

	<table border="0" cellspacing="0" cellpadding="0" width="730px">
		<tbody>
			<tr>
				<td class="" colspan="6"><DIV id=errordivcs><content:errors scope="request"/></DIV><br></td>											
		    </tr>
			<tr>
				<jsp:include page="planNoticeTabs.jsp" flush="true">
					<jsp:param value="2" name="tabValue" />
				</jsp:include>
			</tr>
			<tr>
				<td class="tableheadTD" height="25" colspan="13"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</tr>
			<tr>
				<td width="30"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="270"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="50"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="50"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="50"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="40"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td width="55"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			</tr>
			<tr class="datacell1">
				<td colspan="13">
					<table border="0" cellspacing="0" cellpadding="5">
						<tbody>
							<tr>
								<td><content:getAttribute attribute="text" beanName="sectionIntro"/></td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr class="datacell1" height="10">
				<td colspan="13"></td>
			</tr>
			<tr class="tablehead">
				<td class="tableheadTD1" colspan="13"><b><content:getAttribute attribute="text" beanName="tableTitle"/></b></td>
			</tr>								
			<tr class="datacell1" height="10">
				<td colspan="13"></td>
			</tr>
			<tr class="datacell1">
				<td colspan="13">
					<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<tbody><tr>
							<td width="15"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td width="30"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td width="350"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td width="300"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
						<tr>
							<td height="25" colspan="7"><strong><content:getAttribute attribute="text" beanName="documentSelectionILable"/></strong></td>
						</tr>
						<tr height="10">
							<td colspan="7"></td>
						</tr>
						
						</tbody>
					</table>	
					<style>
						.w14{
							width: 13px !important;
						}
						.w41{
							width:41px !important;
						}
						.w648{
							width: 650px !important;
						}
						.w50 {
							width: 50px !important;
						}
						.span {
							width: 54px !important;
							padding: 4 27;
							border-right: 1px solid #FFFFFF;
							border-left: 1px solid #FFFFFF;
							margin-left: 27px;
						}
						.w14two{
							width: 13px !important;
						}
						.w500 {
							width: 18px !important;
						}
						.selectedJHcheckbox {
							margin-left: 17px;
						}
						.span {
							width: 54px !important;
							padding: 4px 27px;
							border-right: 1px solid #FFF;
							border-left: 1px solid #FFF;
							margin-left: 10;
							margin-right: 2px;
						}
						@-moz-document url-prefix() {
						    .w294{
								width: 592px !important;
							}
						}
						.w294{
							width: 591px !important;
						}
						.enabledButton{
							cursor: pointer !important;
						}
						.buttonDisable p{
							cursor: default;
						}
						.enabledButton p{
							cursor: pointer !important;
						}
					</style>
					<!--[if IE]>
						<style>
							.w41{
								width:42px !important;
							}
							.w648{
								width:631px !important;
							}
							.w1{
								width:1px !important;
							}
							.w1two{
								width:2px !important;
							}
							.w50{
								width: 51px !important;
							}
							.selectedJHcheckbox{
								margin-left: 10px;
								margin-right: 2px;
							}
							.selectedcheckbox {
								margin-left: -1px;
							}
							.w500{
								width: 11px !important;
							}
							.w450{
								width: 15px !important;
							}
							.span {
								width: 55px !important;
								padding: 4 27;
								border-right: 1px solid #FFFFFF;
								border-left: 1px solid #FFFFFF;
								margin-left: 9px;
								margin-right: 2px;
							}
							.w294{
								width: 590px;
							}
							.cell678{
								width:678 !important;
							}
						</style>
					<![endif]-->
					<c:if test="${printFriendly == null}" >
						<!--[if IE]>
							<style>
								.w14{
									width:13px !important
								}
								.w14yes{
									width:13px !important;
								}
								
								.w14two{
									width:13px !important
								}
								.w14no{
									width:9px !important;
								}
							</style>
						<![endif]-->
					</c:if>
					<c:if test="${printFriendly != null}" >
						<!--[if IE]>
							<style>
								.w14two{
									width:5px !important
								}
							</style>
						<![endif]-->
						<script>
							$(document).ready(function(){
								$("a").on("click",function(e){
									e.preventDefault();
								});
							});
						</script>
					</c:if>
					<script>
						var icctoolmessage = "<content:getAttribute attribute="text" beanName="description404"/>";
						var censusLinkToolTip = "<content:getAttribute attribute="text" beanName="censusLinkToolTip"/>";
						var descriptionICC = "<content:getAttribute attribute="text" beanName="description404"/>";
						var orderToolTip = "<content:getAttribute attribute="text" beanName="orderToolTip"/>";
						var resetToolTip = "<content:getAttribute attribute="text" beanName="resetToolTip"/>";
						var orderWaitMessage = "<content:getAttribute attribute="text" beanName="orderWaitMessage"/>";
					</script>	
					<table id="sortableTable" border="0" cellspacing="0" cellpadding="0" width="100%">
						<thead>
						<tr>
							<td class="w14">&nbsp;</td>
							<td class="pgNumBack " width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="tablesubhead w41" align="center" valign="top"><b>Select</b></td>
							<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<c:if test="${requestScope.planVoListLenght == 0 }"> 
								<td class="tablesubhead w50" align="center" valign="top"><b>Sort</b></td>
</c:if>
							<c:if test="${requestScope.planVoListLenght != 0 }">
							<td class="tablesubhead w50" align="center" valign="top" 
								onmouseover="Tip('<content:getAttribute attribute="text" beanName="sortToolTip"/>')" onmouseout="UnTip()">
								<b>Sort</b>
							</td>
</c:if>
							<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="tablesubhead w294" valign="top"><b>Document name</b> <!--<IMG src="assets/arrow_triangle_up.gif">--></td>
							<td class="pgNumBack" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<c:if test="${ printFriendly == null }" >
							<td>&nbsp;</td>
							</c:if>
						</tr>
						</thead>
						<tbody>
						<c:if test="${sessionScope.buildReportData != null}">
<%
PlanDocumentReportData theReport = (PlanDocumentReportData)session.getAttribute("buildReportData");
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 
<c:if test="${empty theReport.customPlanNoticeDocuments}">
								<!--[if IE]>
								<style>
									.w14{
										width:13px !important
									}
									.w294{
										width: 574px !important;
									}
								</style>
							<![endif]-->
</c:if>
<c:if test="${not empty theReport.customPlanNoticeDocuments}">
<c:forEach items="${theReport.customPlanNoticeDocuments}" var="theItem" varStatus="theIndex" >


										<tr class="datacellReplace">
											<td class="w14" style="background: none repeat scroll 0% 0% #FFF;">&nbsp;</td>
											<td class="datadivider " width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											<td class="w41" width="30" align="center">
<c:if test="${buildYourPackageForm.internalUser ==true}">
												<input type="checkbox" class ="selectedcheckbox ordercheckbox" 
value="${theItem.documentId}" name="selectFile" disabled>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==false}">
												<c:if test="${ printFriendly == null }" >
												<input type="checkbox" class ="selectedcheckbox ordercheckbox" 
value="${theItem.documentId}" name="selectFile">
												</c:if>
												<c:if test="${ printFriendly != null }" >
												<input type="checkbox" class ="selectedcheckbox ordercheckbox" 
value="${theItem.documentId}" name="selectFile" disabled>
												</c:if>
</c:if>
											</td>
											<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											<td class="w50" align="center">
<c:if test="${buildYourPackageForm.internalUser !=true}">
												<c:if test="${ printFriendly == null }" >
													<table cellspacing="0" cellpadding="0">
														<tbody>
															<tr class="sort">
<c:if test="${buildYourPackageForm.customSortDisplayInd ==true}">
																	<c:if test="${requestScope.planVoListLenght != 0 }">
																		<td width="25px" align="center" class="up">
																			<c:if test="${theIndex.index != fn:length(theReport.customPlanNoticeDocuments) - 1 }">
																				<a href="#" 
onclick="doCustomSort('customSort','down','${theItem.documentId}'); return false;">
																					<img border="0" src="/assets/unmanaged/images/arrow_triangle_down.gif"></a>
</c:if>
																		</td>
																		<td width="25px" align="center">
																			<c:if test="${theIndex.index !=0}">
																				<a href="#" 
onclick="doCustomSort('customSort','up','${theItem.documentId}'); return false;">
																				<img border="0" src="/assets/unmanaged/images/arrow_triangle_up.gif"></a>
</c:if>
																		</td>
</c:if>
</c:if>
															</tr>
														</tbody>
													</table>
												</c:if>
</c:if>
											</td>
											<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											<td class="w294"> 
<span style="padding-left: 4px;">${theItem.docNameAndUpdatedDate}</span>
											</td>
											<td class="datadivider " width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											<c:if test="${ printFriendly == null }" >
											<td class="w450" style="background: none repeat scroll 0% 0% #FFF;">&nbsp;</td>
											</c:if>
										</tr>
</c:forEach>
</c:if>
						</c:if>	
	
<c:if test="${theReport.jhPlanNoticeDocuments != null}">
<c:forEach items="${theReport.jhPlanNoticeDocuments}" var="theItem" varStatus="theIndex" >
<c:if test="${(theItem.documentName =='planHighlights') || (theItem.documentName =='icc')}">
							<tr class="datacell2" >
							<td class=" w14two w14yes">&nbsp;</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="JHDoc"> 
							
<c:if test="${buildYourPackageForm.internalUser ==true}">
								<input  class="selectedJHcheckbox" type="checkbox" 
<c:if test="${theItem.documentName == 'planHighlights'}">
										value="planhighlight"
</c:if>
<c:if test="${theItem.documentName =='icc'}">
						               value="icc"  
						               onmouseover="Tip(icctoolmessage)" onmouseout="UnTip()"
</c:if>
								name="selectFile" disabled>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==false}">
								<c:if test="${ printFriendly != null }" >
								<input  class="selectedJHcheckbox" type="checkbox" 
<c:if test="${theItem.documentName =='planHighlights'}">
										value="planhighlight"
</c:if>
<c:if test="${theItem.documentName =='icc'}">
						               value="icc"  
</c:if>
								name="selectFile" disabled="disabled">
								</c:if>
								<c:if test="${ printFriendly == null }" >
								<input  class="selectedJHcheckbox" type="checkbox" 
<c:if test="${theItem.documentName =='planHighlights'}">
										value="planhighlight"
</c:if>
<c:if test="${theItem.documentName =='icc'}">
						               value="icc"  
						               onmouseover="Tip(icctoolmessage)" onmouseout="UnTip()"
</c:if>
								name="selectFile">
								</c:if>
</c:if>
							</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="w50 JHDoc" align="center"></td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="w294 JHDoc"> 
							<span style="padding-left: 4px;">
<c:if test="${theItem.documentName =='planHighlights'}">
									Plan Highlights
</c:if>
<c:if test="${theItem.documentName =='icc'}">
					               Investment Comparative Chart
</c:if>
				            </span>
							</td>
							<td class="datadivider " width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<c:if test="${ printFriendly == null }" >
								<td colspan="1 JHDoc" class="w500">&nbsp;</td>
							</c:if>
							</tr>
</c:if>
</c:forEach>
</c:if>
						</tbody>
					</table>								
					
										
					
					<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<tbody>					
						
						
						<tr height="10">
							<td colspan="10"></td>
						</tr>
						<tr>							
							<td height="25" width="150"><strong><content:getAttribute attribute="text" beanName="selectionIndicatorLable"/></strong></td>
							<td class="datacell1" width="15" style="padding-left: 10px;">
								<span class="content mailingselectionblock">
									Package
								</span>
							</td>	
							<td class="datacell1" width="15" style="padding-left: 10px;">
								<span class="content mailingselectionblock">	
									Mail
								</span>
							</td>	
							<td class="datacell1" width="15" style="padding-left: 10px;">
								<span class="content mailingselectionblock">	
									Email
								</span>
							</td>
							<td class="datacell1" colspan="2" style="padding-left: 10px;">
								<span class="content mailingselectionblock">	
									Email & Mail									
								</span>
							</td>	
						</tr>						
						<tr>
							<td width="150"></td>
							<td class="datacell1"  style="padding-left: 20px;">
								<span class="content mailingselectionblock">
									<c:if test="${buildYourPackageForm.internalUser ==false}">
										<c:if test="${ printFriendly != null }" >
	                                          <form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorNo" value="package"/>
										</c:if>
										<c:if test="${ printFriendly == null }" >
	                                         <form:radiobutton path="mailingSelectionIndicator" id="mailingIndicatorNo" value="package"/>
										</c:if>
									</c:if>
									<c:if test="${buildYourPackageForm.internalUser ==true}">
										<form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorNo" value="package"/>
									</c:if>	
								</span>
							</td>	
							<td class="datacell1"  style="padding-left: 8px;">
								<span class="content mailingselectionblock">
								<c:if test="${buildYourPackageForm.internalUser ==false}">
									<c:if test="${ printFriendly != null }" >
                                          <form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorYes" value="mail"/>
									</c:if>
									<c:if test="${ printFriendly == null }" >
                                         <form:radiobutton path="mailingSelectionIndicator" id="mailingIndicatorYes" value="mail"/>
									</c:if>
								</c:if>
								<c:if test="${buildYourPackageForm.internalUser ==true}">
									<form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorYes" value="mail"/>
								</c:if>		
							   </span>
							</td>
							<td class="datacell1"  style="padding-left: 12px;">
								<span class="content mailingselectionblock">
								<c:if test="${buildYourPackageForm.internalUser ==false}">
									<c:if test="${ printFriendly != null }" >
                                          <form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorEmail" value="email"/>
									</c:if>
									<c:if test="${ printFriendly == null }" >
                                         <form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorEmail" value="email"/>
									</c:if>
								</c:if>
								<c:if test="${buildYourPackageForm.internalUser ==true}">
									<form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorEmail" value="email"/>
								</c:if>	
							  </span>
							</td>	
							<td class="datacell1" colspan="4" style="padding-left: 30px;">
								<span class="content mailingselectionblock">
								<c:if test="${buildYourPackageForm.internalUser ==false}">
									<c:if test="${ printFriendly != null }" >
                                          <form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorMailAndEmail" value="mailAndEmail"/>
									</c:if>
									<c:if test="${ printFriendly == null }" >
                                         <form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorMailAndEmail" value="mailAndEmail"/>
									</c:if>
								</c:if>
								<c:if test="${buildYourPackageForm.internalUser ==true}">
									<form:radiobutton disabled="true" path="mailingSelectionIndicator" id="mailingIndicatorMailAndEmail" value="mailAndEmail"/>
								</c:if>								
								</span>
							</td>
						</tr>
						</tbody>
					</table>
					<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<tbody>							
						<tr height="10">
							<td colspan="7"></td>
						</tr>
						<tr class="mailingyes">
							<td height="25" colspan="7"><strong><content:getAttribute attribute="text" beanName="mailingNameLable"/></strong></td>
						</tr>
						<tr height="10" class="mailingyes">
							<td colspan="7"></td>
						</tr>
						<tr class="mailingyes">
							<td></td>	
							<td class="datacell1" colspan="6">
<c:if test="${buildYourPackageForm.internalUser ==false}">
								<c:if test="${ printFriendly != null }" >
<form:input path="mailingName" disabled="true" maxlength="60" size="60"/>
								</c:if>
								<c:if test="${ printFriendly == null }" >
<form:input path="mailingName" maxlength="60" size="60"/>
								</c:if>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==true}">
<form:input path="mailingName" disabled="true" maxlength="60" size="60"/>
</c:if>
							</td>
						</tr>
						<tr height="10" class="mailingyes">
							<td colspan="7"></td>
						</tr>
						<tr class="mailingyes">
							<td height="25" colspan="7"><strong><content:getAttribute attribute="text" beanName="addressFileLable"/></strong></td>
						</tr>
						<tr height="10" class="mailingyes">
							<td colspan="9"></td>
						</tr>
						<tr class="mailingyes">
							<td></td>
							<td class="databorder" colspan="5"></td>
							<td></td>
						</tr>
						<tr class="mailingyes">
							<td>&nbsp;</td>
							<td class="pgNumBack" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="tablesubhead" align="center" valign="top"><b>Select</b></td>
							<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="tablesubhead" valign="top"><b>Address file</b> <!--<IMG src="assets/arrow_triangle_up.gif">--></td>
							<td class="pgNumBack" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td>&nbsp;</td>
						</tr>
						<tr class="mailingyes">
							<td>&nbsp;</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="datacell1" width="30" align="center">
<c:if test="${buildYourPackageForm.internalUser ==true}">
<form:radiobutton disabled="true" path="fileType" value="employee"/>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==false}">
								<c:if test="${ printFriendly != null }" >
<form:radiobutton disabled="true" path="fileType" value="employee"/>
								</c:if>
								<c:if test="${ printFriendly == null }" >
<form:radiobutton path="fileType" value="employee"/>
								</c:if>
</c:if>
							</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="datacell1">
								<c:if test="${ printFriendly != null }" >
									Eligible Employee
								</c:if>
								<c:if test="${ printFriendly == null }" >
									<a href="/do/noticemanager/buildyourpackage/?task=downloadEligibleEmployeeAdressFiles" onmouseover="Tip(censusLinkToolTip)" onmouseout="UnTip()">
									Eligible Employee</a>
								</c:if>
								 address file &nbsp;&nbsp;(This option includes emails, if available.)</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td>&nbsp;</td>
						</tr>
						
						<tr class="mailingyes">
							<td>&nbsp;</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="datacell3" width="30" align="center">
<c:if test="${buildYourPackageForm.internalUser ==true}">
<form:radiobutton disabled="true" path="fileType" value="new"/>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==false}">
								<c:if test="${ printFriendly != null }" >
<form:radiobutton disabled="true" path="fileType" value="new"/>
								</c:if>
								<c:if test="${ printFriendly == null }" >
<form:radiobutton path="fileType" value="new"/>
								</c:if>
</c:if>
							</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="datacell3"> 
								<table cellspacing="0" cellpadding="0">
									<tbody>
										<tr>
											<td width="70px" valign="top">Upload custom address file:</td>
											<td width="280px">
<c:if test="${buildYourPackageForm.internalUser ==false}">
												<c:if test="${ printFriendly != null }" >
												<input type="file" name="censusFile" size="58" disabled="true"/>
												</c:if>
												<c:if test="${ printFriendly == null }" >
												<input type="file" name="censusFile" size="58" />
												</c:if>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==true}">
												<input type="file" name="censusFile" size="58" disabled="true"/>
</c:if>
											<br>
												(Be sure to use this 
												<c:if test="${ printFriendly == null }" >
												<a class="exempt" href="/assets/pdfs/Participant_Address_Template_Notice_Manager.xlsx"
													onmouseover="Tip('<content:getAttribute attribute="text" beanName="censusDownloadLinkToolTip"/>')" onmouseout="UnTip()">
												template</a>.)
												</c:if>	
												<c:if test="${ printFriendly != null }" >
												template.)
												</c:if>	
												
											</td>
										</tr>
									</tbody>
								</table>
							</td>														
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td>&nbsp;</td>
						</tr>
						<tr class="mailingyes">
							<td>&nbsp;</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="datacell1" width="30" align="center">
<c:if test="${buildYourPackageForm.internalUser ==true}">
<form:radiobutton disabled="true" path="fileType" value="existing"/>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==false}">
								<c:if test="${ printFriendly != null }" >
<form:radiobutton disabled="true" path="fileType" value="existing"/>
								</c:if>
								<c:if test="${ printFriendly == null }" >
<form:radiobutton path="fileType" value="existing"/>
								</c:if>
</c:if>
							</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td class="datacell1">Address file from 
								<c:if test="${ printFriendly != null }" >
									Census Information
								</c:if>
								<c:if test="${ printFriendly == null }" >
									<a href="/do/participant/participantAddresses" onmouseover="Tip(censusLinkToolTip)" onmouseout="UnTip()">
									Census Information</a>
								</c:if>
								 page.</td>
							<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td>&nbsp;</td>
						</tr>
					
						
						<tr class="mailingyes">
							<td></td>
							<td class="databorder" colspan="5"></td>
							<td></td>
						</tr>
						<tr height="10" class="mailingyes">
							<td colspan="7"></td>
						</tr>
						<tr class="mailingyes">
							<td height="25" colspan="7"><strong><content:getAttribute attribute="text" beanName="placeOrderLable"/></strong></td>
						</tr>
						<tr height="10" class="mailingSelectionThankyou" style="display:none;">
							<td height="25" colspan="7"><strong><content:getAttribute attribute="text" beanName="mailingSelectionThankyou"/></strong></td>
						</tr>
						<tr height="10">
							<td colspan="7"></td>
						</tr>
					</tbody></table>
				</td>
			</tr>	
		</tbody>
	</table>
	<br></br>
	<br></br>
	<style>
		
		.buttonDisable {
			background-image: url("/assets/unmanaged/images/134_button.gif");
			width: 134px;
			height: 25px;
			margin-top: 16px;
			postion: relative;
		}
		.buttonText {
			position: absolute;
			text-align: center;
			width: 134px;
			color: white;
			font-size: 13px;
			margin-top: 5px;
			left: 0px;
		}
		.twodisabled {
			display: none;
		}
	</style>
	<!--[if IE]>
		<style>
			.twodisabled{
				color: #B1AFA2;
				width: 132px;
				margin-top: 6px;
				display: block;
			}
		</style>
	<![endif]-->
	<table border="0" cellspacing="0" cellpadding="1" width="708">
		<tbody>
			<tr>
				<c:if test="${ printFriendly == null }" >
				<td width="385"> </td>
				<td width="181" style="">
<c:if test="${buildYourPackageForm.internalUser ==false}">
						<div align="center">
							<div style="postion:relative;" class="resetButton" id="resetContainer"
								onclick="return doReset()" onmouseover="Tip(resetToolTip)" onmouseout="UnTip('')">
								<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >reset</p><p class="buttonText twodisabled">reset</p></h5>
							</div>
						</div>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==true}">
						<div align="center">
							<div style="postion:relative;"  class="resetButton" id="resetContainer"
								 onmouseover="Tip(resetToolTip)" onmouseout="UnTip('')">
								<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >reset</p><p class="buttonText twodisabled">reset</p></h5>
							</div>
						</div>
</c:if>
				</td>
				<td width="136" class="orderbutton">												
<c:if test="${buildYourPackageForm.internalUser ==false}">
						<c:if test="${empty buildYourPackageForm.termsOfUse}">
							<div align="right">
								<div style="postion:relative;"  class="resetButton enabledButton"
									onclick="return doterms('order')" onmouseover="Tip(orderToolTip)" onmouseout="UnTip()"/>
									<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >place order</p></h5>
								</div>
							</div>
</c:if>
<c:if test="${buildYourPackageForm.termsOfUse =='Y'}">
							<div align="right">
								<div style="postion:relative;"  class="resetButton enabledButton"
									onclick="return doSubmit('order')" onmouseover="Tip(orderToolTip)" onmouseout="UnTip()"/>
									<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >place order</p></h5>
								</div>
							</div>
</c:if>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==true}">
						<div align="right">
							<div style="postion:relative;"  class="resetButton" id=""
								 onmouseover="Tip(orderToolTip)" onmouseout="UnTip('')">
								<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >place order</p><p class="buttonText twodisabled">place order</p></h5>
							</div>
						</div>
</c:if>
				</td>
				<td width="136" class="downloadbutton">
<c:if test="${buildYourPackageForm.internalUser ==false}">
						<c:if test="${buildYourPackageForm.termsOfUse == '' }">
							<div align="right">
								<div style="postion:relative;"  class="resetButton enabledButton"
									onclick="return doterms('download')" />
									<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >download</p></h5>
								</div>
							</div>
</c:if>
<c:if test="${buildYourPackageForm.termsOfUse == 'Y'}">
							<div align="right">
								<div style="postion:relative;"  class="resetButton enabledButton"
									onclick="return doDownload('download')" />
									<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >download</p></h5>
								</div>
							</div>
</c:if>
</c:if>
<c:if test="${buildYourPackageForm.internalUser ==true}">
						<div align="right">
							<div style="postion:relative;"  class="resetButton" id="" >
								<h5 style="position: relative;" class="buttonDisable"><p class="buttonText" >download</p><p class="buttonText twodisabled">download</p></h5>
							</div>
						</div>
</c:if>
				 </td>	
				</c:if>
			</tr>
		</tbody>
	</table>
</ps:form>
<c:if test="${ printFriendly != null }" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute
				beanName="globalDisclosure" attribute="text" /></td>
		</tr>
	</table>
</c:if>
<script language="javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>

<script>
$(document).ready(function(){
	// This will impliment custom table rows with zebra crossing
	$(".datacellReplace:even").addClass("datacell1");
	$(".datacellReplace:odd").addClass("datacell3");
});	
</script>

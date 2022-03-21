
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<!-- BEGIN FLOATING LAYER CODE //-->

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>

<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanContentConstants"
	className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />

<content:contentBean
	contentId="${loanContentConstants.SECTION_TITLE_BANK_INFORMATION}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="sectionTitleBankInformation" />

<content:contentBean
	contentId="${loanContentConstants.BANK_DIALOG_FOOTER}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="bankDialogFooter" />

<content:contentBean
	contentId="${loanContentConstants.BANK_LOOKUP_NO_DATA_FOUND}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="bankLookupNoDataFound" />

<content:contentBean
	contentId="${loanContentConstants.BANK_LOOKUP_MORE_THAN_ONE_ENTRY_FOUND}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="bankLookupMoreThanOneEntryFound" />

<content:contentBean
	contentName="${loanContentConstants.BANK_LOOKUP_BANK_NAME_INVALID_CHARACTER}"
	type="${contentConstants.TYPE_MESSAGE}"
	beanName="bankLookupBankNameInvalidCharacter" />
<style type="text/css">
  /* Change style per style user story */
.bankPopUpModal {
	display: none;
	z-index: 100000;
	top: 593;
	left: 277;
	position: absolute;
	overflow: auto;
	width: 100%;
	height: 100%;
	margin-left: 0px;
	margin-top: 0px;
	overflow: auto;
	
}

@media ( min-width : 992px) {
	.bankPopUpModalContent {
		background-color: #f3f1f1;
		padding: 0px;
		border: 1px solid #999;
		width: 460px;
		height: 169.5px
		top: 30%;
		text-align: left;
		z-index: 100000;
		margin-top: 25%;
		margin-right: 52%;
		margin-left: 13%;
	}
}

@media ( max-width : 991px) {
	.bankPopUpModalContent {
		background-color: #f3f1f1;
		padding: 0px;
		border: 1px solid #999;
		width: auto;
		height: auto;
		left: 30%;
		top: 30%;
		text-align: left;
		z-index: 100000;
		#border-radius: 6px;
	}
}


.modal-content {
	margin-top: -18.7px;
	background-color: #f3f1f1;
}

.modal-content a {
	text-decoration: underline;
}

.modal-header {
	display: -ms-flexbox;
	display: flex;
	-ms-flex-align: start;
	align-items: flex-start;
	-ms-flex-pack: justify;
	justify-content: space-between;
	padding: 0;
}

.modal-title {
	font-size: 18.5px;
	padding-bottom: 10px;
	padding-left: 12px;
	padding-top: 6px;
	position: relative;
	display: none;
	border-bottom: 1px solid #eee;
	background-color: #002D62;
	color: #FFF;
	font-family: Arial, Helvetica, sans-serif;
}

.modal-body {
	position: relative;
	padding: $modal-inner-padding;
}

.col-xs-12, .col-md-12 {
	clear: both;
}

.modal-bg {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	opacity: 0;
	z-index: 10;
	visibility: hidden;
	transition: background-color$base-duration/2 linear;
}

.modal-footer {
	position: relative;
	display: flex;
	align-items: center;
	justify-content: flex-end;
	width: 100%;
	margin: 0;
	padding: 10px 0 0;
}
.divider{
    height:auto;
    display:inline-block;
    padding: 8.1px;
}
</style>
<%-- Defines handlers for all editable fields --%>
<script type="text/javascript">

 function onClickForSelectBankName() {
	 
	 var recipientIndex = document.getElementById('recipientIndex').value;
	 var payeeIndex = document.getElementById('payeeIndex').value;	  
    $("#bankSelectionErrorMsg").html('');
	var elements = document.getElementsByName('bank');
    var selectedBankName = '';
	var ele = document.getElementsByName('bank');
              
     for(i = 0; i < ele.length; i++) {
       if(ele[i].checked)
               selectedBankName   = ele[i].value;
     }
		   
	if(selectedBankName == ''){
		selectedBankName = document.getElementById('bankNameOtherInput').value;
		
		var invalidCharacter = "";
	      for (var i = 0; i < selectedBankName.length; i++) {
	        var c = selectedBankName.charAt(i);
	        if (c == '"' || c == '&' || c == '<') {
	          invalidCharacter += c;
	        }
	      }
	      if (invalidCharacter.length > 0) {
            var invalidCharacterMessage = '<content:getAttribute escapeJavaScript="true" beanName="bankLookupBankNameInvalidCharacter" attribute="text"/>';
            invalidCharacterMessage = invalidCharacterMessage.replace(/\{0\}/, invalidCharacter);
		    $("#bankSelectionErrorMsg").html(invalidCharacterMessage);
           return false;
         }
	}
	
	var txtName = document.getElementById("eftPayeeBankNameId[" + recipientIndex + "][" + payeeIndex + "]");
    txtName.value = selectedBankName;
    hideBankLookPopup();
  }
 
 
  
  
 function hideBankLookPopup(){
   var sessionWarningModal = document.getElementById('bankPopUpModal');
	sessionWarningModal.style.display = "none";
	document.documentElement.style.overflow = 'scroll';
	document.body.scroll = "yes";	
 }
 

 function callbackBankLookupPopUp(response,recipientIndex,payeeIndex){
	 
	var h = 0;
	var banks = response;
  if (banks.length == 0) {
    $("#bankNameOtherRadio").prop("checked", true);
    $("#bankSelectionDiv").hide();
    $("#bankSelectionInitialMsg").html('<content:getAttribute escapeJavaScript="true" attribute="text" beanName="bankLookupNoDataFound"/>');
  } else {
	 $("#bankSelectionInitialMsg").html('<content:getAttribute escapeJavaScript="true" attribute="text" beanName="bankLookupMoreThanOneEntryFound"/>');

	 var html = '';
    for (var i = 0; i < 4 && i < banks.length; i++) {
      var bank = banks[i];
      html += '<tr><td style = "width: 5px;"><input type="radio" name="bank_hidden" value="1">';
      html += '</td><td >' + bank.name + '</td></tr>';
    }
    $("#bankSelectionTableHidden").html(html);
    
    html = '';
	if(banks.length>10){
		h = 80;
	}
	else{
		h = 40;
	}
    for (var i = 0; i < banks.length; i++) {
      var bank = banks[i];
      html += '<tr><td style = "width: 5px;"><input name="bank" type="radio" value="' + bank.name + '"';    
      html += '/></td><td >' + bank.name + '</td></tr>';
    }
  }
  html += '<tr><td style = "width: 5px;"><input type="hidden" id="recipientIndex" value="' + recipientIndex + '">';
  html += '<tr><td style = "width: 5px;"><input type="hidden" id="payeeIndex" value="' + payeeIndex + '">';
   
    if(payeeIndex == 0){
	   $(".bankPopUpModal").css("top", "756");
   }
   else if(payeeIndex == 1){
	   $(".bankPopUpModal").css("top", "190%");
   }
   else if(payeeIndex == 2){
	   $(".bankPopUpModal").css("top", "260%");
   }
   else if(payeeIndex == 3){
	   $(".bankPopUpModal").css("top", "330%");
   }
    $("#bankSelectionDivScrollView").height($("#bankSelectionTableHidden").height() + h);
    $("#bankSelectionTable").html(html);
    $("#bankSelectionDiv").show();
	var bankPopUpModal = document.getElementById('bankPopUpModal');
	bankPopUpModal.style.display = "block";	
			
 }
 
</script>


<div class="bankPopUpModal" id="bankPopUpModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true"  data-keyboard="false" data-backdrop="static"
	>

	<div class="bankPopUpModalContent">
		<div class="modal-content" style="">
			
			
			<h3 class="modal-title">Bank Information</h3>
				
			
			<div class="modal-body" id="getCode"
				style="">

				<table width="450" border="0" cellspacing="0" cellpadding="0"
					bgcolor="#FFFFFF">
					<tr>
						<td colspan="2">
							<table border="0" cellpadding="0" cellspacing="0" width="440">
								<tr>
									<td width="3"><img src="/assets/unmanaged/images/s.gif"
										height="1" width="3" /></td>

									<td width="250">

										<div id="bankSelectionErrorMsg">&nbsp;</div>

										<div id="bankSelectionInitialMsg" style="color: red;">
											&nbsp;</div>

										<table style="display: none" id="bankSelectionTableHidden"
											border="0" cellpadding="0" cellspacing="0" width="440">
											&nbsp;
										</table>
										<div id="bankSelectionDiv">
											<div id="bankSelectionDivScrollView"
												style="width: 440;  overflow: auto;">
												<table id="bankSelectionTable" border="0" cellpadding="0"
													cellspacing="0" width="440" >
												</table>
											</div>
										</div>

										<table border="0" cellpadding="0" cellspacing="0" height="80"
											width="440">
											<tr>
												<td><input id="bankNameOtherRadio" name="bank"
													type="radio" value="" />
													<input id="bankNameOtherInput" name="bankOther"
													type="text" maxlength="70"
													 /></td>
											</tr>

											<tr>
												<td>&nbsp;</td>

												<td>&nbsp;</td>
											</tr>

											<tr>
												<td><input id="bankLookupDialogCancel" type="button"
													class="button100Lg" value="cancel"
													onclick="hideBankLookPopup(); return false;" />
													&nbsp;&nbsp; <input id="bankLookupDialogSelect"
													type="button" class="button100Lg" value="select"
													onclick="onClickForSelectBankName(); return false;" /></td>
											</tr>

											<tr>
												<td>&nbsp;</td>

												<td>&nbsp;</td>
											</tr>
										</table>
									</td>

									<td width="3"><img src="/assets/unmanaged/images/s.gif"
										height="1" width="3" /></td>
								</tr>

								<tr>
									<td colspan="3"><img src="/assets/unmanaged/images/s.gif"
										height="1" width="1" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionPaymentForm" %>
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionUploadForm" %>

<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_PAYMENT_TEXT%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="fileUploadPaymentNote"/>

<content:contentBean contentId="<%=ContentConstants.SUBMISSION_NEGATIVE_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageNegativeAmount"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxAmount"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_VALID_EFFECTIVE_DATE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidDate"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_CASH_VALUE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxCashValue"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_CASH_FUTURE_DATED%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageFutureDated"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_BILL_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxBillAmount"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_TEMP_CREDIT_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxTempCreditAmount"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_VALID_FILE_TYPE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidFileType"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_USE_PAYMENT_ONLY%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageUsePaymentOnly"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_VALID_GENERATE_STMT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidGenerateStmt"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_VALID_ADDRESS_SUBM%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidAddressSubmission"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidUploadInProgress"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_PAYMENT_AUTH%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageNoPaymentAuth"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_BILL_NO_CONTRIBUTION%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageBillAmountNoContributionAmount"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_TEMPORARY_CREDIT_NO_CONTRIBUTION%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageTempCreditAmountNoContributionAmount"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NOT_AUTHORIZED_UPLOAD_CONTRIB%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageContractStatusNotAuth"/>

<jsp:useBean id="submissionUploadForm"
                         scope="session" 
                         class="com.manulife.pension.ps.web.tools.SubmissionUploadForm" />

<%
SubmissionUploadForm requestForm = (SubmissionUploadForm)session.getAttribute("submissionUploadForm");
pageContext.setAttribute("requestForm",requestForm,PageContext.REQUEST_SCOPE);

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String paymentHeight = String.valueOf(SubmissionPaymentForm.MAX_PAYMENT_TABLE_HEIGHT);
pageContext.setAttribute("paymentHeight",paymentHeight,PageContext.PAGE_SCOPE);
%>
<c:set var="ifileConfig" value="${userProfile.contractProfile.ifileConfig}"/>
<c:set var="contract" value="${userProfile.currentContract}" scope="request" />
<c:set var="trackChanges" value="false" scope="request" />

<script type="text/javascript" >
	//AG: formName == "submissionUploadForm"
	// lastpayroll => lastPayroll

	var selectedDate;

	function isFormChanged() {

	    <% if (submissionUploadForm.isDisplayFileUploadSection()) { %>

		var uploadFormObj = getFormByName("submissionUploadForm");

		if (isFileSectionShown) {
			var filename = uploadFormObj.uploadFile.value;
			if (!isNullInput(filename)==true) {
				return true;
			}
		}

		if (calculateTotalPaymentInstructions() != 0.00) {
			return true;
		}

		if (uploadFormObj.requestEffectiveDate != undefined) {
			var str_date = uploadFormObj.requestEffectiveDate.value;

			var arr_date = str_date.split('/');
			selectedDate = new Date(arr_date[2],parseInt(arr_date[0],10)-1,arr_date[1]);

			if (defaultEffectiveDate.valueOf() != selectedDate.valueOf()) {
				return true;
			}
		}

		
		if (uploadFormObj.lastPayroll != undefined) {
			if (uploadFormObj.lastPayroll[0].checked==true || uploadFormObj.lastPayroll[1].checked==true) {
				return true;
			}
		}

		<% } %>

		return false;
	}

	var errorContent="";

	var isNetscape=eval(navigator.appName == "Netscape");
	var isExplorer=eval(navigator.appVersion.indexOf("MSIE")!=-1);
 	function getFormByName(formName) {
	 	if (navigator.appName == "Netscape" && parseInt(navigator.appVersion) == 4) {
			return getFormByNameNav4(formName);
	   	} else {
			return document.forms[formName];
 	  	}
  	}

 	function getFormByNameNav4(formName, parent) {
	   	var objForm;
	  	var parentObj= (parent) ? parent : document;
	    	objForm= parentObj.forms[formName];
	   	if (!objForm) {
			for (var i= 0; i < parentObj.layers.length && !objForm; i++) {
				objForm= getFormByNameNav4(formName, parentObj.layers[i].document);
			}
	  	}
	      return objForm;
        }

	function writeError(text) {
		//alert('>>> Write error' + text + ' isNetscape = ' + isNetscape);

		var contentString;

		if (text=="")
			errorContent = "";
		else
			errorContent = errorContent + "<li>" + text + "</li>";

		var contentString;
		if (text.length > 0 )
			contentString = '<table id="psErrors"><tr><td class="redText"><ul>' +
				errorContent + '</ul></td></tr></table>';
		else
			contentString = '';

		if ( document.getElementById ) {
			document.getElementById('errordivcs').innerHTML = contentString;
		}else if (isExplorer) {
			document.all.errordivcs.innerHTML = contentString;
		} else if (isNetscape) {
			//this is old netscape
			document.errordivcs.document.open();
			document.errordivcs.document.write(contentString);
			document.errordivcs.document.close();
		}

		if (text!="" && text!=" " &&text.length>0) {
			location.href='#TopOfPage';
		}

		//alert('Write error' + text);
	}
	function y2k(number) {
		if (isExplorer) {
			return (number < 1000) ? number + 1900 : number;
		} else {
			return (number + 1900);
		}
	}
	var months = new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");

	function formatDate(date) {
		return months[date.getMonth()] + " " + date.getDate() + ", " + y2k(date.getYear());
	}
	function formatCurrency(num) {
		num = num.toString().replace(/\$|\,/g,'');
		if(isNaN(num)) {
			num = "0";
		}
		sign = (num == (num = Math.abs(num)));
		num = Math.floor(num*100+0.50000000001);
		cents = num%100;
		num = Math.floor(num/100).toString();
		if(cents<10) {
			cents = "0" + cents;
		}
		for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++) {
			num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
		}
		return (((sign)?'':'-') + '$' + num + '.' + cents);
	}

	function formatCurrencyForInputTextBox(num) {
		num = Math.round(num*100);
		cents = num%100;
		num = Math.floor(num/100).toString();
		if(cents<10) {
			cents = "0" + cents;
		}
		for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++) {
			num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
		}
		return num+"."+cents;
	}
	function parseCurrencyInput(num) {
		return Math.round(num.replace(/\$|\,/g,'')*100)/100;
	}

	function validatePaymentInstructionInput(object) {

		//alert(object.name);
		var validColumn = true;
		var validRow = true;
		var fieldArray = null;
		var row = object.name.substring(object.name.indexOf("[")+1,object.name.indexOf("]"));

		if (object.value!="0.00") {
			var num = object.value.replace(/\$|\,/g,'');
			if(isNaN(num)) {
				alert("Invalid entry. Please enter only numeric values.");
				object.value="0.00";
			} else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) {
				alert("Invalid payment amount. Only two decimal places are permitted");
				object.value="0.00";
			} else if (num != Math.abs(num)) {
				alert('<content:getAttribute beanName="messageNegativeAmount" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_NEGATIVE_AMOUNT%> + ']');
				object.value="0.00";
			} else if (num > 99999999.99) {
				alert('<content:getAttribute beanName="messageMaxAmount" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_MAX_AMOUNT%> + ']');
				object.value="0.00";
			} else {
				object.value=formatCurrencyForInputTextBox(num);
			}
		}

		if(object.name.indexOf("bill")!=-1) {
			fieldArray = billFields;
		} else if(object.name.indexOf("credit")!=-1) {
			fieldArray = creditFields;
		} else {
			fieldArray = contributionFields;
		}

		for (var i=0; i<fieldArray.length && validColumn; i++) {
			var arrayObject = eval(fieldArray[i]);
			if (arrayObject.value!="0.00") {
					var num = arrayObject.value.replace(/\$|\,/g,'');
					if(isNaN(num) ||
					   (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) ||
					   (num != Math.abs(num)) ||
					   (num > 99999999.99)) {
						validColumn = false;
					} else {
						arrayObject.value=formatCurrencyForInputTextBox(num);
					}
				}
		}

		fieldArray = eval("accountsRow"+row);
		for (var i=0; i<fieldArray.length && validRow; i++) {
			var arrayObject = eval(fieldArray[i]);
			if (arrayObject.value!="0.00") {
					var num = arrayObject.value.replace(/\$|\,/g,'');
					if(isNaN(num) ||
					   (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) ||
					   (num != Math.abs(num)) ||
					   (num > 99999999.99)) {
						validRow = false;
					} else {
						arrayObject.value=formatCurrencyForInputTextBox(num);
					}
				}
		}

		// grand total
		if(!validColumn || !validRow) {
				var grandTotal = document.getElementById('grandTotal');
				if(grandTotal != null)  grandTotal.innerHTML ="NaN";
		} else {
				var grandTotal = document.getElementById('grandTotal');
				if(grandTotal != null)  grandTotal.innerHTML = '$'+formatCurrencyForInputTextBox(calculateTotal(contributionFields) + calculateTotal(billFields) +calculateTotal(creditFields));
		}

		// column totals
		if(!validColumn) {
				if(object.name.indexOf("bill")!=-1) {
					document.getElementById('billPaymentTotal').innerHTML="NaN";
				} else if(object.name.indexOf("credit")!=-1) {
					document.getElementById('temporaryCreditTotal').innerHTML="NaN";
				} else {
					document.getElementById('contributionTotal').innerHTML="NaN";
				}
		} else {
				if(object.name.indexOf("bill")!=-1) {
					document.getElementById('billPaymentTotal').innerHTML = '$'+formatCurrencyForInputTextBox(calculateTotal(billFields));
				} else if(object.name.indexOf("credit")!=-1) {
					document.getElementById('temporaryCreditTotal').innerHTML = '$'+formatCurrencyForInputTextBox(calculateTotal(creditFields));
				} else {
					document.getElementById('contributionTotal').innerHTML = '$'+formatCurrencyForInputTextBox(calculateTotal(contributionFields));
				}
		}

		// row totals
	    if (!validRow) {
				var rowTotal = document.getElementById('accountsRow'+row+'Total');
				if(rowTotal != null) rowTotal.innerHTML="NaN";
		} else {
				var rowTotal = document.getElementById('accountsRow'+row+'Total');
				if(rowTotal != null) rowTotal.innerHTML='$'+formatCurrencyForInputTextBox(calculateTotal(eval("accountsRow"+row)));
		}
	}

	<% if (submissionUploadForm.isDisplayFileUploadSection()) { %>
		var isFileSectionShown=true;
	<% } else { %>
		var isFileSectionShown=false;
	<% } %>


	<% if (submissionUploadForm.isDisplayPaymentInstructionSection() && submissionUploadForm.getAccounts() != null && submissionUploadForm.getAccounts().size() > 0) {%>
		var isPaymentSectionShown=true;
	<%} else {%>
		var isPaymentSectionShown=false;
	<%}%>

	<% if (submissionUploadForm.isCashAccountPresent()) {%>
		var isCashAccountPresent=true;
	<%} else {%>
		var isCashAccountPresent=false;
	<%}%>

	var uploadcredit=1;

var contributionFields = ${submissionUploadForm.getPaymentContributionInputObjectsNamesForJavascript()} <%-- filter="false" --%>;

var billFields = ${submissionUploadForm.getPaymentBillInputObjectsNamesForJavascript()} <%-- filter="false" --%>;
var creditFields = ${submissionUploadForm.getPaymentCreditInputObjectsNamesForJavascript()} <%-- filter="false" --%>;

var validDates = ${submissionUploadForm.allowedMarketDatesJavaScript} <%-- filter="false" --%>;

<c:if test="${not empty submissionUploadForm.paymentInfo}">
var outstandingBillPayment = ${submissionUploadForm.paymentInfo.outstandingBillPayment};
var outstandingTemporaryCredit = ${submissionUploadForm.paymentInfo.outstandingTemporaryCredit};
var cashAccountAvailableBalance = ${submissionUploadForm.paymentInfo.cashAccountAvailableBalance};
var cashAccountTotalBalance = ${submissionUploadForm.paymentInfo.cashAccountTotalBalance};
</c:if>

var isBillPaymentSectionShown = ${submissionUploadForm.displayBillPaymentSection};
var isTemporaryCreditSectionShown = ${submissionUploadForm.displayTemporaryCreditSection};


	var cashAccountRow;
<c:forEach items="${submissionUploadForm.accountsRowsObjectsNamesForJavascript}" var="theItem" varStatus="theIndex">
var accountsRow${theIndex.index} = ${theItem};
	// the last account is the cash account if its present
cashAccountRow = accountsRow${theIndex.index};
</c:forEach>

	function calculateTotal(fields) {
		if(fields==null) return 0;
		var uploadFormObj = getFormByName("submissionUploadForm");
		//alert('calculate total');
		var totalValue = 0;
		for (var i=0; i<fields.length; i++) {
			totalValue = totalValue + parseCurrencyInput((eval(fields[i])).value);
		}
		return totalValue;
	}

	function calculateTotalPaymentInstructions() {
		var fieldArray = contributionFields;
		if(billFields!=null) fieldArray = fieldArray.concat(billFields);
		if(creditFields!=null) fieldArray = fieldArray.concat(creditFields);
		return calculateTotal(fieldArray);
	}

	function validatePaymentInstructionInputs() {
		var uploadFormObj = getFormByName("submissionUploadForm");

		var fieldArray = contributionFields;
		if(billFields!=null) fieldArray = fieldArray.concat(billFields);
		if(creditFields!=null) fieldArray = fieldArray.concat(creditFields);

		for (var i=0; i<fieldArray.length; i++) {
			var object = eval(fieldArray[i]);
		    if (object.value!="0.00") {
			    var num = object.value.replace(/\$|\,/g,'');
				if(isNaN(num)) {
					writeError("Invalid entry. Please enter only numeric values.");
					object.value="0.00";
					return false;
			    } else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) {
					writeError("Invalid payment amount. Only two decimal places are permitted.");
					object.value="0.00";
					return false;
			    } else if (num != Math.abs(num)) {
					writeError('<content:getAttribute beanName="messageNegativeAmount" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_NEGATIVE_AMOUNT%> + ']');
					object.value="0.00";
					return false;
				} else if (num > 99999999.99) {
					writeError('<content:getAttribute beanName="messageMaxAmount" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_MAX_AMOUNT%> + ']');
					object.value="0.00";
					return false;
				} else {
					object.value=formatCurrencyForInputTextBox(num);
		    	}
			}
		}
		//alert("All Fields are valid = " + isValid);
		return true;
	}


	var currentDate=<%=submissionUploadForm.getCurrentDateJavascriptObject()%>;
	var defaultEffectiveDate=<%=submissionUploadForm.getDefaultEffectiveDateJavascriptObject()%>;

	var errorMessage="";

	var paymentDateChecked = 'no';
	function validatePaymentDateSelection() {

		if (isPaymentSectionShown) {
			//alert('Validating Date');

			var uploadFormObj = getFormByName("submissionUploadForm");
			var str_date = uploadFormObj.requestEffectiveDate.value;
			if (str_date.length == 0) {
				alert('<content:getAttribute beanName="messageValidDate" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_VALID_EFFECTIVE_DATE%> + ']');
				uploadFormObj.requestEffectiveDate.value = cal.gen_date(dt_default);
				paymentDateChecked = 'bad';
				return false;
			}

			var arr_date = str_date.split('/');
			if (isNaN(arr_date[2]) || arr_date[2].length != 4) {
				alert('<content:getAttribute beanName="messageValidDate" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_VALID_EFFECTIVE_DATE%> + ']');
				uploadFormObj.requestEffectiveDate.value = cal.gen_date(dt_default);
				paymentDateChecked = 'bad';
				return false;
			}

			selectedDate = new Date(arr_date[2],parseInt(arr_date[0],10)-1,arr_date[1]);
			if (arr_date[2] < 2000) {
				alert ("Invalid date selected: '" + str_date + "'.\nAllowed range is " +(this.dt_start.getMonth() +1) + "/" + this.dt_start.getDate() + "/" + this.dt_start.getFullYear()	+" to "	+(this.dt_end.getMonth() +1) + "/" + this.dt_end.getDate() + "/" + this.dt_end.getFullYear()	+ ".");
				uploadFormObj.requestEffectiveDate.value = cal.gen_date(dt_default);
				paymentDateChecked = 'bad';
				return false;
			}

			if(typeof(cal) != 'undefined') {
				var result = cal.prs_date(str_date, true);
				if (result == null) {
					uploadFormObj.requestEffectiveDate.value = cal.gen_date(dt_default);
					paymentDateChecked = 'bad';
					return false;
				}
			}

		}
		paymentDateChecked = 'ok';
		return true;
	}

	function disableLastquaterSelection(o){

		var uploadFormObj = getFormByName("submissionUploadForm");
		if (uploadFormObj.lastPayroll != undefined) {
			if (o==true) {
				uploadFormObj.lastPayroll[0].checked=false;
				uploadFormObj.lastPayroll[1].checked=false;
			}
			uploadFormObj.lastPayroll[0].disabled=o;
			uploadFormObj.lastPayroll[1].disabled=o;
		}
	}

	function selectFileType(selectedtype) {
		/** regular */
		if ("C"==selectedtype) {
			disableLastquaterSelection(false);
		} else {
			disableLastquaterSelection(true);
		}
	}
	function resetValues(){
		if (confirm('You have selected to clear all entries on this submission. Are you sure you want to proceed and discard your changes?')) {
			selectedDate="";
			paymentDateChecked = 'no';
			writeError("");
			var uploadFormObj = getFormByName("submissionUploadForm");
			uploadFormObj.reset();
			location.href='#TopOfPage';


			//reset all the total row and column fields
			var totalElements = new Array(
					document.getElementById('billPaymentTotal'),
					document.getElementById('temporaryCreditTotal'),
					document.getElementById('contributionTotal'),
					document.getElementById('grandTotal')
			);

			var numRows = contributionFields.length;

			for (var i = 0; i < numRows; i++) {
				totalElements.push(document.getElementById('accountsRow'+i+'Total'));
			}

			for ( i = 0; i < totalElements.length; i++) {
				if(totalElements[i]!=null) totalElements[i].innerHTML = "$0.00";
			}
		}
	}
	function isNullInput(str) {

		b = ((str==null) || (""==str));
		//alert("isNullInput returns " + b + "for '" + str + "'" );
		return b;
	}

	function getFileType(uploadFormObj) {
		return "C";
	}

	function validateFileSelectionSection() {
		var isValid = true;

		if (isFileSectionShown) {

			//alert('validate file');

			var uploadFormObj = getFormByName("submissionUploadForm");

			var filename = uploadFormObj.uploadFile.value;
			var filetype = getFileType(uploadFormObj);
			//alert('filetype = ' + filetype + ' filename = ' + filename);

			if (isNullInput(filetype)==true ) {
				writeError('<content:getAttribute beanName="messageValidFileType" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_VALID_FILE_TYPE%> + ']');
				//alert('Please select a file type');
				isValid = false;
			}

			if (isNullInput(filename)==true) {
				writeError('<content:getAttribute beanName="messageUsePaymentOnly" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_USE_PAYMENT_ONLY%> + ']');
				//alert("Please select a file to submit. If you would like to make a payment only, select the \n\'Make a Payment\' link in the top right hand side of the page.");
				isValid = false;
			}
			
			if (isExplorer && filename.indexOf('\\') == -1) {
				writeError("Please enter the directory where the file is located");
				isValid = false;
			}

			if (filetype=="C" && uploadFormObj.lastPayroll != undefined) {
				if (uploadFormObj.lastPayroll[0].checked==false && uploadFormObj.lastPayroll[1].checked==false) {
					writeError('<content:getAttribute beanName="messageValidGenerateStmt" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_VALID_GENERATE_STMT%> + ']');

					isValid = false;
				}
			}
		}
		return isValid;
	}

	function validatePaymentInstructions() {
		var isValid = true;
		var uploadFormObj = getFormByName("submissionUploadForm");
		var totalValue = calculateTotalPaymentInstructions();

		var filetype = getFileType(uploadFormObj);
		if (totalValue > 0.0) {
			if (filetype=="A") {
				writeError('<content:getAttribute beanName="messageValidAddressSubmission" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_VALID_ADDRESS_SUBM%> + ']');
				isValid = false;
			}
		}

		if (isCashAccountPresent) {

			if(typeof(cashAccountTotalBalance) != 'undefined' && calculateTotal(cashAccountRow) > cashAccountTotalBalance) {
				writeError('<content:getAttribute beanName="messageMaxCashValue" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_MAX_CASH_VALUE%> + ']');
				isValid = false;
			}

			if(totalValue > 0.0 && calculateTotal(cashAccountRow) == totalValue && selectedDate.valueOf() > defaultEffectiveDate.valueOf()) {
				writeError('<content:getAttribute beanName="messageFutureDated" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_CASH_FUTURE_DATED%> + ']');
				isValid = false;
			}

		}

		if(isBillPaymentSectionShown) {
			if(typeof(outstandingBillPayment) != 'undefined' && calculateTotal(billFields) > outstandingBillPayment) {
				writeError('<content:getAttribute beanName="messageMaxBillAmount" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_MAX_BILL_AMOUNT%> + ']');
				isValid = false;
			}
		}

		if(isTemporaryCreditSectionShown) {
			if(typeof(outstandingTemporaryCredit) != 'undefined' && calculateTotal(creditFields) > outstandingTemporaryCredit) {
				writeError('<content:getAttribute beanName="messageMaxTempCreditAmount" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_MAX_TEMP_CREDIT_AMOUNT%> + ']');
				isValid = false;
			}
		}

		if (calculateTotal(contributionFields) == 0.0 && totalValue != 0.0 ) {
			if(typeof(outstandingBillPayment) != 'undefined' && calculateTotal(billFields) > 0.0) {
				writeError('<content:getAttribute beanName="messageBillAmountNoContributionAmount" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_UPLOAD_BILL_NO_CONTRIBUTION%> + ']');
			}
			if(typeof(outstandingTemporaryCredit) != 'undefined' && calculateTotal(creditFields) > 0.0) {
				writeError('<content:getAttribute beanName="messageTempCreditAmountNoContributionAmount" attribute="text"/>' + ' [' + <%=ContentConstants.SUBMISSION_UPLOAD_TEMPORARY_CREDIT_NO_CONTRIBUTION%> + ']');
			}
			isValid = false;
		}


		return isValid;
	}
	function validatePaymentSection() {
		if (isPaymentSectionShown) {
			//alert('payment section');
			var b1 = true;
			if (paymentDateChecked == 'bad') {
				b1 = false;
				paymentDateChecked = 'no';
			} else if (paymentDateChecked == 'no') {
				b1 = validatePaymentDateSelection();
			} else {
				paymentDateChecked = 'no';
			}
			//alert('validate date selection returns ' + b1);
			var b2 = validatePaymentInstructionInputs();
			//alert('validate payment instruction inputs returns ' + b2);
			var b3 = validatePaymentInstructions();
			//alert('validate payment instrutions returns ' + b3);
			return b1 && b2 && b3;
		} else {
			return true;
		}
	}
	function validateInputs() {
		writeError("");
		var b1 = validateFileSelectionSection();
		//alert('validate file returns ' + b1);
		var b2 = validatePaymentSection();
		//alert('validate payment returns ' + b2);
		var b = b1 && b2;
		//alert('validate inputs: ' + b);

		return b;
	}
	function checklastpayrolloption(obj){

		var uploadFormObj = getFormByName("submissionUploadForm");
		var filetype = getFileType(uploadFormObj);
		if (filetype!="C") {
			obj.checked=false;
		}
	}
	var uploadCanceld=false;
	function cancelUpload(){
		uploadCanceld=true;
	}

	function confirmSend() {

		//alert('Confirm');

		var uploadFormObj = getFormByName("submissionUploadForm");
		var filetype = getFileType(uploadFormObj);

		if (uploadcredit != 1 ) {
			//alert('uploadcredit');
			writeError("");
			hideInProgressDivision();
			writeError("<content:getAttribute beanName="messageValidUploadInProgress" attribute="text"/>" + ' [' + <%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%> + ']' + "<br>If you have trouble with your upload click <a href=\"/do/tools/submissionUpload/\">here</a> to start over.");
		   	return false;
	 	} else if (validateInputs() == true) {
		   	var message;
			message = "";
			var totalValue = 0.00;

			//alert('Confirm - valid');
			//if (isFileSectionShown) {

			if (isPaymentSectionShown) {
				totalValue = calculateTotalPaymentInstructions();

				if (isCashAccountPresent) {
					if ((totalValue - Math.abs(parseCurrencyInput((eval(contributionFields[contributionFields.length-1])).value))) > 250000.0 ) {
						message = message+"WARNING:  A direct debit for more than $250,000.00 has been "
										 +"requested and will be processed. If this was done in error, "
										 +"contact your client account representative immediately.\n\n";
					}

					if(typeof(cashAccountAvailableBalance) != 'undefined' && calculateTotal(cashAccountRow) > cashAccountAvailableBalance) {
						message = message+"WARNING:  Cash account amount entered is greater than the balance available for allocations.\n\n";
					}


					if (totalValue - calculateTotal(cashAccountRow) > 0.0
							&& calculateTotal(cashAccountRow) > 0.0) {
						message = message+"You are using funds from your cash account. Contact your client account representative to provide details regarding which transactions to use.\n\n";
					}

				} else {
					if (totalValue > 250000.0 ) {
						message = message+"WARNING:  A direct debit for more than $250,000.00 has been "
										 +"requested and will be processed. If this was done in error, "
										 +"contact your client account representative immediately.\n\n";
					}
				}

				if (calculateTotal(contributionFields) > 0 && calculateTotal(contributionFields) < 1) {
					message = message+"WARNING:  Total payment towards contribution is less than $1.00. Are you sure you want to proceed?\n\n";
				}

				if (filetype != "A" && contributionFields.length > 1 && calculateTotal(cashAccountRow) == totalValue) {
					message = message+"WARNING:  You have access to direct debit accounts but have not entered any payments. Are you sure you want to proceed?\n\n";
				}

			}

			//alert('Confirm - valid 1');
			var filename = null;
			if (isFileSectionShown) {
				filename = uploadFormObj.uploadFile.value;
				if (isNullInput(filename)==false) {
					message = message + "Upload file: "+filename;
				}
			}

			//alert('Confirm - valid 2');

		   	if (totalValue>0.001) {
				if (isNullInput(filename)==false) {
					message = message+" and total";
				} else {
					message = message+"Upload a total";
				}
				message = message+" payment of "+formatCurrency(totalValue)+" with a requested payment effective date of "+formatDate(selectedDate);


				if(isBillPaymentSectionShown) {
					if(calculateTotal(billFields) > 0.0) {
						message = message+";\nBill payment of "+formatCurrency(calculateTotal(billFields))+ " will be made";
					}
				}
				if(isTemporaryCreditSectionShown) {
					if(calculateTotal(creditFields) > 0.0) {
						message = message+";\nTemporary Credit payment of "+formatCurrency(calculateTotal(creditFields))+ " will be made";
					}
				}

		  	}
         	message = message +"?";

			//alert('Confirm - valid 3 '+message);


		   	if (confirm(message)) {
				uploadcredit = 0;
				writeError("");
				showInProgressDivision();
			 	location.href='#TopOfPage';
				return true;
		   	} else {
				return false;
		  	}
	   	} else {
			return false;
	    }
	}
	function initDocument() {
		writeError(errorMessage);
	}
	function showInProgressDivision() {
		if ( document.getElementById ) {
			document.getElementById('inProgressDivision').style.display = 'block';
		}else if (isExplorer) {
			document.all['inProgressDivision'].style.display = 'block';
		} else if (isNetscape) {
			document.layers['inProgressDivision'].display = 'block';
		}
		return true;
	}
	function hideInProgressDivision() {
		if ( document.getElementById ) {
			document.getElementById('inProgressDivision').style.display = 'none';
		}else if (isExplorer) {
			document.all['inProgressDivision'].style.display = 'none';
		} else if (isNetscape) {
			document.layers['inProgressDivision'].display = 'none';
		}
		return true;
	}

	function protectLinks() {

			var hrefs  = document.links;
			if (hrefs != null)
			{
				for (i=0; i<hrefs.length; i++) {
					if(
						hrefs[i].onclick != undefined &&
						(hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("doSignOut") != -1)
					) {
						// don't replace window open or popups as they won't loose there changes with those
					}
					else if(
						hrefs[i].href != undefined &&
						(hrefs[i].href.indexOf("openWin") != -1 || hrefs[i].href.indexOf("popup") != -1 || hrefs[i].href.indexOf("doSignOut") != -1)
					) {
						// don't replace window open or popups as they won't loose there changes with those
					}
					else if(hrefs[i].onclick != undefined) {
						hrefs[i].onclick = new Function ("var result = discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
					}
					else {
						hrefs[i].onclick = new Function ("return discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');");
					}
					//alert (hrefs[i].onclick);
				}
			}
	 }

	registerTrackChangesFunction(isFormChanged);
	if (window.addEventListener) {
		window.addEventListener('load', protectLinks, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', protectLinks);
	}
</SCRIPT>
<style type="text/css">
<!--
div.paymentScroll {
	height:${submissionUploadForm.paymentTableHeight} px;
	width: 100%;
	<c:if test="${submissionUploadForm.paymentTableHeight == paymentHeight}">
	overflow: auto;
	</c:if>
	border-style: none;
	background-color: #fff;
	padding: 0px;}
-->
</style>

	<A NAME="TopOfPage"></A>&nbsp;
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">

			<tr>
				<td>
					<DIV id="inProgressDivision" style="display: none;";>
						<TABLE BORDER=0 CELLPADDING=1 CELLSPACING=0>
							<TR><TD ALIGN="left"><b>Upload in progress.</b></TD>
							</TR>
							<TR><TD ALIGN="left">Please wait for the Upload confirmation page.
							Navigating away from this page before the upload is complete will cancel your upload and end your secure session.
							<br>If you have trouble with your upload click <a href="/do/tools/submissionComboUpload/">here</a> to start over.
							</TD>
							</TR>
						</TABLE>
					</DIV>
					<DIV id=errordivcs><content:errors scope="session"/></DIV><br>
				</td>
				<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
				<td><img src="/assets/unmanaged/images/spacer.gif" width="1	" height="1" border="0"></td>
			</tr>

            <tr>
            <td height="20">
    	 		   <ps:form action="/do/tools/submissionComboUpload/" modelAttribute="submissionUploadForm" name="submissionUploadForm" enctype="multipart/form-data" method="POST" onsubmit="return confirmSend();" >

						  <table width="500" border="0" cellpadding="0" cellspacing="0">
						<tr>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="80" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="420" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
                    <tr class="tablehead" height="25">
                      <td class="tableheadTD1" colspan="5"> <b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
                    </tr>
                    <tr class="datacell1">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td colspan="3" class="whiteborder">
                      <table width="100%" border="0" cellpadding="0" cellspacing="0">
                          <tr>
                            <td class="datacell1"><b>Contract&nbsp; </b>
									<strong class="highlight">
${userProfile.currentContract.contractNumber}

${userProfile.currentContract.companyName}

									</strong>
                            </td>
                          </tr>
                          <tr>
                            <td colspan="1" width="100%" align="right" class="datacell1">&nbsp;</td>
                          </tr>
                          <tr>
                            <td class="datacell1" valign="top" width="250"><span class="content"><b>File Information</b></span></td>
                          </tr>

						  <% if (submissionUploadForm.isDisplayFileUploadSection()) { %>
							  <tr>
								<td class="datacell1"><span class="content"><content:getAttribute beanName="layoutPageBean" attribute="body1"/> </span></td>
							  </tr>
							  <tr>
								<td>&nbsp;</td>
							  </tr>
							  <tr>
								<td>
									<table cellspacing="0" cellpadding="0" width="500" border="0">
									<!--DWLayoutTable-->
									<tbody>
									  <tr>
										<td width="78" align="left" valign="top" class="datacell1"><span class="content"><strong>File type </strong></span>
										</td>
										<td width="178" align="left" valign="top" class="datacell1">
										  <span class="content">
<input type="hidden" name="fileType" value="C"/>
										 Regular Combination
										</td>
										<td width="244" align="left" valign="top" class="datacell1">
<c:if test="${submissionUploadForm.displayFileUploadSection ==true}">
<c:if test="${submissionUploadForm.displayGenerateStatementSection ==true}">

											<table align="left" >
<c:if test="${not empty submissionUploadForm.statementDates}">
												<tr>
													<td colspan="2" align="left"><b>Participant Statements</b></td>
										                </tr>
												<tr>
													<td colspan="2" align="left">Statements for the following quarters have not been created for participants.</td>
										                </tr>
<c:forEach items="${submissionUploadForm.statementDates}" var="item" varStatus="theIndex" >
												<tr>
													<td colspan="2" align="left">&nbsp;
													<span class="highlightBold"><render:date property="item.startDate" patternOut="MMM-d-yyyy" defaultValue=""/> to
													<render:date property="item.endDate" patternOut="MMM-d-yyyy" defaultValue=""/>
													</span>
													</td>
												</tr>
</c:forEach>
</c:if>
												<tr>
												<td colspan="2" align="left"><b style="color:blue">Is this your last contribution for the above quarter?</b><br><br>
													<table>
														<tr>
<td><form:radiobutton path="lastPayroll" value="C"/>Yes&nbsp;&nbsp;&nbsp;</td>
															<td><b>If "Yes", statements will run after this allocation is complete.</b></td>
														</tr>
														<tr><td><br></td></tr>
														<tr>
<td><form:radiobutton path="lastPayroll" value="S"/>No&nbsp;&nbsp;&nbsp;</td>
															<td><b>If "No", we will continue to wait for your last contribution for the quarter.</b></td>
														</tr>
													</table>
												</td>
												</tr>

											</table>


</c:if>
</c:if>
											&nbsp;
										</td>
									  </tr>
									  <tr>
										<td valign="top" class="datacell1" celspan="2"><!--DWLayoutEmptyCell-->&nbsp;</td>
										<td height="15" align="left" valign="top" class="datacell1" celspan="2"><!--DWLayoutEmptyCell-->&nbsp;</td>
										<td height="15" valign="top" class="datacell1" celspan="2"><!--DWLayoutEmptyCell-->&nbsp;</td>
									  </tr>
									  <tr>
										<td celspan="2" valign="top" class="datacell1">
											<span class="content">
												<strong>File name</strong>
											</span>
										</td>
										<td height="15" colspan="2" valign="top" class="datacell1" celspan="2"><span class="content">
										  <!-- input type="file" size="60" name="uploadfile" / -->
										  <input type="file" name="uploadFile" size="58"/>
										  <!-- size="<=(isIE? 60 : 30)>" -->
										  <br />
										  (Maximum file size accepted is 5 MB)</span>
										</td>
									  </tr>
									</tbody>
								</table>
								</td>
							  </tr>

						  <%} else {%>
						  		<tr>
						  		<td class="datacell1"><span class="content">
<c:if test="${userProfile.beforeCAStatusAccessOnly ==true}">
									<content:getAttribute beanName="messageContractStatusNotAuth" attribute="text"/>
</c:if>
<c:if test="${userProfile.beforeCAStatusAccessOnly ==false}">
									You are not authorized to send a file.
</c:if>
								</span></td>
								</tr>
						  <%}%>


                          <tr>
                            <td>&nbsp;</td>
                          </tr>
<c:set var="lastRowStyle" value="${datacell1}" scope="request" />
						  <% if (submissionUploadForm.isDisplayFileUploadSection()) { %>
                          <tr class="datacell1">
                            <td><span class="content"><b>Payment Information</b> </span></td>
                          </tr>
                          <tr class="datacell1">
                            <td>
                            	<span class="content"><content:getAttribute beanName="layoutPageBean" attribute="body2"/></span>
								</td>
                          </tr>
						    <%if (submissionUploadForm.isDisplayPaymentInstructionSection()) {%>
							  <% if (submissionUploadForm.getAccounts() != null && submissionUploadForm.getAccounts().size() > 0) { %>
								  <jsp:include page="paymentEnterSection.jsp" flush="true" />
							  <%} else {%>
							    <tr>
								  <td class="datacell1">
									  <span class="content">You are not authorized to use the checking accounts on this contract. Please contact your user manager or client account representative.</span>
								  </td>
							  </tr>
							  <%}%>


						    <%} else {%>
							  <tr>
								<td class="datacell1">
									<span class="content"><content:getAttribute beanName="messageNoPaymentAuth" attribute="text"/></span>
								</td>
							  </tr>
						  <%}%>
						<% } %>

                      </table>
                      </td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
<tr class="${lastRowStyle}">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="lastrow" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
<tr class="${lastRowStyle}">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
						  <td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td  colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
						</tr>
						<tr>
						  <td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
                </table>
					  <% if ( submissionUploadForm.isDisplayFileUploadSection() ) { %>
					  		<br/><br/>
							<table width="455" border="0" cellpadding="0" cellspacing="0">
								<tbody>
								  <tr>
									<td width="143">&nbsp;</td>
									<td width="146"><span class="content">
									  <input type="button" class="button134" onclick="resetValues();" value="clear" />
									</span></td>
									<td width="166"><span class="content">
<input type="submit" class="button134" value="send" name="actionLabel" />
									</span></td>
								  </tr>
								</tbody>
							</table>
					<% } %>

				</ps:form>
				<%if (submissionUploadForm.isDisplayFileUploadSection() && submissionUploadForm.isDisplayPaymentInstructionSection() && submissionUploadForm.getAccounts() != null && submissionUploadForm.getAccounts().size() > 0) { %>
<c:if test="${contract.status != 'CA'}">
<c:if test="${submissionUploadForm.viewMode ==false}">
							<script type="text/javascript" >
							<!-- // create calendar object(s) just after form tag closed
								 // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
								 // note: you can have as many calendar objects as you need for your application
var dt_start = new Date("${submissionUploadForm.calendarStartDate}");
var dt_end = new Date("${submissionUploadForm.calendarEndDate}");
								//todo: where do we start
var dt_default = new Date("${submissionUploadForm.requestEffectiveDate}");
								if (dt_default.getYear() < 2000) {
									if (dt_default.getYear() > 1899) {
										dt_default.setYear(dt_default.getYear() + 100);
									} else if (dt_default.getYear() < 100) {
										dt_default.setYear(dt_default.getYear() + 2000);
									}
								}
								dt_end.setHours(23);
								dt_end.setMinutes(59);
								dt_end.setSeconds(59);
								dt_end.setMilliseconds(999);
								var cal = new calendar(document.forms['submissionUploadForm'].elements['requestEffectiveDate'],dt_start.valueOf(),dt_end.valueOf(),validDates, dt_default);
								cal.year_scroll = false;
								cal.time_comp = false;
							//-->
							</script>
</c:if>
</c:if>
				<% } %>
				</td>

				<!--// emd column 2 -->

				<!-- column 3 HELPFUL HINT -->

				<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
				<td width="210" align="center" valign="top">
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="25">

					<logicext:if name="userProfile" property="allowedUploadSubmissions" op="equal" value="true">
						<logicext:then>
						   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
						   <!-- file upload helpful hint -->
						   <content:rightHandLayerDisplay layerName="layer1" beanName="layoutPageBean" />
						</logicext:then>
					</logicext:if>

					<logicext:if name="userProfile" property="allowedCashAccount" op="equal" value="true">
						<logicext:or name="userProfile" property="allowedDirectDebit" op="equal" value="true"/>

						<logicext:then>
						   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
						   <!-- file upload helpful hint -->
						   <content:rightHandLayerDisplay layerName="layer2" beanName="layoutPageBean" />
						</logicext:then>

					</logicext:if>

				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer3" beanName="layoutPageBean" />

				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer4" beanName="layoutPageBean" />
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">

				</td>

				<!--// end column 3 -->


			</tr>

			<tr>
              <td height="20">&nbsp;</td>
 			  <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
			  <td></td>
            </tr>

 			<tr>
               <td height="20" width="30">&nbsp;</td>
               <td colspan="2">
					<p><content:pageFooter id="layoutPageBean"/></p>
				</td>
 			</tr>
			<% if (submissionUploadForm.isDisplayFileUploadSection()) { %>
<c:if test="${ifileConfig.directDebitAccountPresent ==true}">
<c:if test="${submissionUploadForm.displayMoreSections ==true}">
						<tr>
              				<td height="20">&nbsp;</td>
 			  				<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
 			  				<td></td>
			  			</tr>
						<tr>
  			  				<td height="20"><span class="disclaimer"><content:getAttribute beanName="fileUploadPaymentNote" attribute="text"/></span>
  			  				<td></td>
			  			</tr>
</c:if>
</c:if>
			<% } %>
<c:if test="${not empty submissionUploadForm.marketClose}">
			<tr>
              <td height="20">&nbsp;</td>
 			  <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
			  <td></td>
            </tr>
			<tr>
 			  <td><span class="disclaimer"><content:getAttribute beanName="submissionUploadForm" attribute="marketClose"/></span></td>
			  <td></td>
            </tr>
</c:if>

			<%if (submissionUploadForm.isDisplayFileUploadSection() && submissionUploadForm.isDisplayPaymentInstructionSection() && submissionUploadForm.getAccounts() != null && submissionUploadForm.getAccounts().size() > 0) { %>
			<tr>
  				<td height="20">&nbsp;</td>
	  				<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
	  				<td></td>
  			</tr>

			<tr>

 				<td height="20"  colspan="2">
					<p class="disclaimer"><content:pageFootnotes id="layoutPageBean"/></p>
					<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>
				</td>
 			</tr>
 			<% } %>

		</table>
<script>
	var uploadFormObj = getFormByName("submissionUploadForm");
	if (typeof(uploadFormObj.elements['amounts[0]']) != "undefined" )
		validatePaymentInstructionInput(uploadFormObj.elements['amounts[0]']);
</script>
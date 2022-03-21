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
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_PAYMENT_INFO%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messagePaymentInfo"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_CASH_CONT_ONLY%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageCashContOnly"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_PAYMENT_CASH_ACCOUNT_EXCEEDS_BALANCE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxCashValue"/>							
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_CASH_FUTURE_DATED%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageFutureDated"/>							
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_BILL_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxBillAmount"/>							
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_MAX_TEMP_CREDIT_AMOUNT%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageMaxTempCreditAmount"/>							
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_PAYMENT_AUTH%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageNoPaymentAuth"/>
<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageValidUploadInProgress"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NOT_AUTHORIZED_MAKE_PAYMENT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageContractStatusNotAuth"/>

<jsp:useBean id="submissionPaymentForm"
                         scope="session" 
                         class="com.manulife.pension.ps.web.tools.SubmissionUploadForm" />
<%
SubmissionUploadForm requestForm = (SubmissionUploadForm)session.getAttribute("submissionPaymentForm");
pageContext.setAttribute("requestForm",requestForm,PageContext.REQUEST_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String paymentHeight = String.valueOf(SubmissionPaymentForm.MAX_PAYMENT_TABLE_HEIGHT);
pageContext.setAttribute("paymentHeight",paymentHeight,PageContext.PAGE_SCOPE);
%>
<c:set var="ifileConfig" value="${userProfile.contractProfile.ifileConfig}"/>
<c:set var="contract" value="${userProfile.currentContract}" scope="request" />
<c:set var="trackChanges" value="false" scope="request" />

<%

	if(submissionPaymentForm != null && submissionPaymentForm.getAccounts() != null && submissionPaymentForm.getAccounts().size()>2){
		session.removeAttribute("totalContribution");
	}
%>

<script type="text/javascript" > 
	//AG: formName == submissionPaymentForm
	var selectedDate;
	
	var uploadcredit=1;
	
	function isFormChanged() {
	
	    <%-- if (form.isDisplayFileUploadSection()) { --%>
	
		var uploadFormObj = getFormByName("submissionPaymentForm");
		if (calculateTotalPaymentInstructions() != 0.00) {
			return true;
		}
		
		if (uploadFormObj.requestEffectiveDate == null) {
			return false;
		}	
		var str_date = uploadFormObj.requestEffectiveDate.value;

		var arr_date = str_date.split('/');
		selectedDate = new Date(arr_date[2],parseInt(arr_date[0],10)-1,arr_date[1]);
		
		if (defaultEffectiveDate.valueOf() != selectedDate.valueOf()) {
			return true;
		}
	
		<%-- } --%>
	
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
				alert("Invalid payment amount. Only two decimal places are permitted.");
				object.value="0.00";
			} else if (num != Math.abs(num)) {
				alert('<content:getAttribute beanName="messageNegativeAmount" attribute="text"/>');
				object.value="0.00";
			} else if (num > 99999999.99) {
				alert('<content:getAttribute beanName="messageMaxAmount" attribute="text"/>');
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
										
					var divisionValue=formatCurrencyForInputTextBox(calculateTotal(contributionFields));	
					var totalContribution="0.00";
					if(divisionValue=="0.00"){
					/** session value comes from 
					  * com.manulife.pension.ps.web.tools.ViewContributionDetailsNewAction
					**/
					totalContribution=<%=session.getAttribute("totalContribution")%>;
					}  	
								
					if(totalContribution==""||totalContribution==null||totalContribution=="0.00"){
						totalContribution=formatCurrencyForInputTextBox(calculateTotal(contributionFields));
						
					}
                     
					document.getElementById('contributionTotal').innerHTML = '$'+totalContribution;
					//+formatCurrencyForInputTextBox(calculateTotal(contributionFields));
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
	
	<% if (submissionPaymentForm.isDisplayFileUploadSection()) { %>
		var isFileSectionShown=true;
	<% } else { %>
		var isFileSectionShown=false;
	<% } %>

	
	<% if (submissionPaymentForm.isDisplayPaymentInstructionSection() && submissionPaymentForm.getAccounts() != null && submissionPaymentForm.getAccounts().size() > 0) {%>
		var isPaymentSectionShown=true;
	<%} else {%>
		var isPaymentSectionShown=false;
	<%}%>

	<% if (submissionPaymentForm.isCashAccountPresent()) {%>
		var isCashAccountPresent=true;
	<%} else {%>
		var isCashAccountPresent=false;
	<%}%>

var contributionFields = ${submissionPaymentForm.getPaymentContributionInputObjectsNamesForJavascript()} <%-- filter="false" --%>;

var billFields = ${submissionPaymentForm.getPaymentBillInputObjectsNamesForJavascript()} <%-- filter="false" --%>;
var creditFields = ${submissionPaymentForm.getPaymentCreditInputObjectsNamesForJavascript()} <%-- filter="false" --%>;

var validDates = ${submissionPaymentForm.allowedMarketDatesJavaScript} <%-- filter="false" --%>;
	
<c:if test="${not empty submissionPaymentForm.paymentInfo}">
var outstandingBillPayment = ${submissionPaymentForm.paymentInfo.outstandingBillPayment};
var outstandingTemporaryCredit = ${submissionPaymentForm.paymentInfo.outstandingTemporaryCredit};
var cashAccountAvailableBalance = ${submissionPaymentForm.paymentInfo.cashAccountAvailableBalance};
var cashAccountTotalBalance = ${submissionPaymentForm.paymentInfo.cashAccountTotalBalance};
</c:if>

var isBillPaymentSectionShown = ${submissionPaymentForm.displayBillPaymentSection};
var isTemporaryCreditSectionShown = ${submissionPaymentForm.displayTemporaryCreditSection};
	
	
	var cashAccountRow;
<c:forEach items="${submissionPaymentForm.accountsRowsObjectsNamesForJavascript}" var="theItem" varStatus="theIndex">
var accountsRow${theIndex.index} = ${theItem};
	// the last account is the cash account if its present
cashAccountRow = accountsRow${theIndex.index};
</c:forEach>
	
	function calculateTotal(fields) {
		if(fields==null) return 0;
		var uploadFormObj = getFormByName("submissionPaymentForm");
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
		var uploadFormObj = getFormByName("submissionPaymentForm");
		
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
					writeError('<content:getAttribute beanName="messageNegativeAmount" attribute="text"/>');
					object.value="0.00";
					return false;
				} else if (num > 99999999.99) {
					writeError('<content:getAttribute beanName="messageMaxAmount" attribute="text"/>');
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


	var currentDate=<%=submissionPaymentForm.getCurrentDateJavascriptObject()%>;
	var defaultEffectiveDate=<%=submissionPaymentForm.getDefaultEffectiveDateJavascriptObject()%>;
	
	var errorMessage="";

	var paymentDateChecked = 'no';
	function validatePaymentDateSelection() {

		if (isPaymentSectionShown) {
			//alert('Validating Date');

			var uploadFormObj = getFormByName("submissionPaymentForm");
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

	function resetValues(){
		if (!isFormChanged()
				|| confirm('Warning!  The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.')) {
			selectedDate="";
			paymentDateChecked = 'no';
			writeError("");
			var uploadFormObj = getFormByName("submissionPaymentForm");
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

	function validatePaymentInstructions() {
		var isValid = true;
		var uploadFormObj = getFormByName("submissionPaymentForm");
		var totalValue = calculateTotalPaymentInstructions();

		if (totalValue<0.001) {
			writeError('<content:getAttribute beanName="messagePaymentInfo" attribute="text"/>');
			isValid = false;
		}

		if (isCashAccountPresent) { 

			if(typeof(cashAccountTotalBalance) != 'undefined' && calculateTotal(cashAccountRow) > cashAccountTotalBalance) {
				writeError('<content:getAttribute beanName="messageMaxCashValue" attribute="text"/>');
				isValid = false;
			}
			
			if(calculateTotal(contributionFields) != 0 && calculateTotal(contributionFields) == parseCurrencyInput((eval(contributionFields[contributionFields.length-1])).value)) {
				writeError('<content:getAttribute beanName="messageCashContOnly" attribute="text"/>');
				isValid = false;
			}

			if(totalValue > 0.0 && calculateTotal(cashAccountRow) == totalValue && selectedDate.valueOf() > defaultEffectiveDate.valueOf()) {
				writeError('<content:getAttribute beanName="messageFutureDated" attribute="text"/>');
				isValid = false;
			}
			
		}

		if(isBillPaymentSectionShown) {
			if(typeof(outstandingBillPayment) != 'undefined' && calculateTotal(billFields) > outstandingBillPayment) {
				writeError('<content:getAttribute beanName="messageMaxBillAmount" attribute="text"/>');
				isValid = false;
			}
		}
		if(isTemporaryCreditSectionShown) {
			if(typeof(outstandingTemporaryCredit) != 'undefined' && calculateTotal(creditFields) > outstandingTemporaryCredit) {
				writeError('<content:getAttribute beanName="messageMaxTempCreditAmount" attribute="text"/>');
				isValid = false;
			}
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
		var b1 = validatePaymentSection();
		//alert('validate payment returns ' + b2);
		var b = b1;
		//alert('validate inputs: ' + b);

		return b;
	}

	var uploadCanceld=false;
	function cancelUpload(){
		uploadCanceld=true;
	}

	function confirmSend() {

		//alert('Confirm');

		var uploadFormObj = getFormByName("submissionPaymentForm");

	 	 if (uploadCanceld==true) {
			//alert('uploadcanceld');
		   	window.location="LPAction.jsp?Page=<%/**=pageBean.getPageName()**/%>&cancel=y";
		   	return false;
		} else if (uploadcredit != 1 ) {
			//alert('uploadcredit');
			writeError("");
			writeError("<content:getAttribute beanName="messageValidUploadInProgress" attribute="text"/>" + ' [' + <%=ContentConstants.SUBMISSION_UPLOAD_IN_PROGRESS%> + ']' + "<br>If you have trouble with your upload click <a href=\"/do/tools/submissionPayment/\">here</a> to start over.");
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
					var checkTotalValue = totalValue - Math.abs(parseCurrencyInput((eval(contributionFields[contributionFields.length-1])).value));
					if(creditFields!=null) {
						checkTotalValue = checkTotalValue - Math.abs(parseCurrencyInput((eval(creditFields[creditFields.length-1])).value));
					}
					if (checkTotalValue > 250000.0 ) {
						message = message+"WARNING:  A direct debit for more than $250,000.00 has been requested and will be processed.\n\n";
					}
					
					if(typeof(cashAccountAvailableBalance) != 'undefined' && calculateTotal(cashAccountRow) > cashAccountAvailableBalance) {
						message = message+"WARNING:  Cash account amount entered is greater than the balance available for allocations.\n\n";
					}
					
				} else {
					if (totalValue > 250000.0 ) {
						message = message+"WARNING:  A direct debit for more than $250,000.00 has been requested and will be processed.\n\n";
					}
				}
				if (calculateTotal(contributionFields) > 0 && calculateTotal(contributionFields) < 1) {
					message = message+"WARNING:  Total payment towards contribution is less than $1.00. Are you sure you want to proceed?\n\n";
				}

				if (contributionFields.length > 1 && calculateTotal(cashAccountRow) == totalValue) {
					message = message+"WARNING:  You have access to direct debit accounts but have not enterered any payments. Are you sure you want to proceed?\n\n";
				}
			}

			//alert('Confirm - valid 1');
		   	if (totalValue>0.001) {
				message = message+"Make a total payment of "+formatCurrency(totalValue)+" with a requested payment effective date of "+formatDate(selectedDate);

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
						hrefs[i].onclick = new Function ("var result = discardChanges('Warning!  The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
					}
					else {
						hrefs[i].onclick = new Function ("return discardChanges('Warning!  The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');");
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
	height: ${submissionPaymentForm.paymentTableHeight}px;
	width: 100%;
	<c:if test="${submissionPaymentForm.paymentTableHeight == paymentHeight}">
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
					<DIV id=errordivcs><content:errors scope="session"/></DIV><br>
				</td>
				<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
				<td><img src="/assets/unmanaged/images/spacer.gif" width="1	" height="1" border="0"></td>
			</tr>

            <tr>
              <td height="20">
    	 		   <ps:form action="/do/tools/makePayment/" modelAttribute="submissionPaymentForm" name="submissionPaymentForm" method="POST" onsubmit="return confirmSend();">

				  <table width="500" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td width="500"><img src="/assets/unmanaged/images/s.gif" width="500" height="1" /></td>
                      <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                    <tr class="tablehead" height="25">
                      <td class="tableheadTD1" colspan="4"> <b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
                    </tr>
                    <tr class="whiteborder">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td colspan="2">
                      <table width="100%" border="0" cellpadding="0" cellspacing="0">
                          <tr>
                            <td class="datacell1"><b>Contract:</b>
									<strong class="highlight">
${userProfile.currentContract.contractNumber}

${userProfile.currentContract.companyName}

									</strong>
                            </td>
                          </tr>
                          <tr>
                            <td width="100%" align="right" class="datacell1">	&nbsp;</td>
                          </tr>

                          <tr>
                            <td>&nbsp;</td>
                          </tr>
                          <tr class="datacell1">
                            <td><span class="content"><b>Payment Information</b> </span></td>
                          </tr>
                          <tr class="datacell1">
									<td>
										<span class="content"><content:getAttribute beanName="layoutPageBean" attribute="body1"/></span>
									</td>
                          </tr>
<c:set var="lastRowStyle" value="datacell1" scope="request" />
						<%if (submissionPaymentForm.isDisplayPaymentInstructionSection()) {%>
							<% if (submissionPaymentForm.getAccounts() != null && submissionPaymentForm.getAccounts().size() > 0) {	%>					
								<jsp:include page="paymentEnterSection.jsp" flush="true" />
								
							<%} else {%>
							<tr>
								<td class="datacell1">
									<span class="content">You are not authorized to use the checking accounts on this contract. Please contanct your user manager or client account representative.</span>
								</td>
							</tr>
							<%}%>

						<%} else {%>
							<tr>
						  		<td class="datacell1"><span class="content">
<c:if test="${userProfile.beforeCAStatusAccessOnly ==true}">
									<content:getAttribute beanName="messageContractStatusNotAuth" attribute="text"/>
</c:if>
<c:if test="${userProfile.beforeCAStatusAccessOnly ==false}">
									<content:getAttribute beanName="messageNoPaymentAuth" attribute="text"/>
</c:if>
								</span></td>
							</tr>
						<%}%>

                      </table>
                      </td>
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>


<tr class="${lastRowStyle}">
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
                      <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                      <td  colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
                    </tr>
                    <tr>
                      <td class="databorder" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                    </tr>
                </table>
<c:if test="${submissionPaymentForm.viewMode ==false}">
					  <% if (submissionPaymentForm.isDisplayPaymentInstructionSection() && submissionPaymentForm.getAccounts() != null && submissionPaymentForm.getAccounts().size() > 0) { %>
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
</c:if>
				</ps:form>
				<%if (submissionPaymentForm.isDisplayPaymentInstructionSection() && submissionPaymentForm.getAccounts() != null && submissionPaymentForm.getAccounts().size() > 0) { %>
<c:if test="${contract.status != 'CA'}">
<c:if test="${submissionPaymentForm.viewMode ==false}">
							<script type="text/javascript" >
							<!-- // create calendar object(s) just after form tag closed
								 // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
								 // note: you can have as many calendar objects as you need for your application
var dt_start = new Date("${submissionPaymentForm.calendarStartDate}");
var dt_end = new Date("${submissionPaymentForm.calendarEndDate}");
								//todo: where do we start
var dt_default = new Date("${submissionPaymentForm.requestEffectiveDate}");
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
								var cal = new calendar(document.forms['submissionPaymentForm'].elements['requestEffectiveDate'],dt_start.valueOf(),dt_end.valueOf(),validDates, dt_default);
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
				   
					<logicext:if name="userProfile" property="allowedCashAccount" op="equal" value="true">
						<logicext:or name="userProfile" property="allowedDirectDebit" op="equal" value="true"/>
				
						<logicext:then>
						   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
						   <!-- file upload helpful hint -->
						   <content:rightHandLayerDisplay layerName="layer2" beanName="layoutPageBean" /> 
						</logicext:then>
						
					</logicext:if>
				  
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer2" beanName="layoutPageBean" />   
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer3" beanName="layoutPageBean" />   
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
				   <content:rightHandLayerDisplay layerName="layer4" beanName="layoutPageBean" />   
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
		  		  
				</td>

				<!--// end column 3 -->


			</tr>
            <!--<tr>
              <td height="20">&nbsp;</td>
 			  <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
			  <td></td>
            </tr>-->
            </table>

 			<tr>
               <td height="20" width="30">&nbsp;</td>
               <td colspan="2">		 	   
					<p><content:pageFooter id="layoutPageBean"/></p>
				</td>
 			</tr>

			<%if (submissionPaymentForm.isDisplayPaymentInstructionSection() && submissionPaymentForm.getAccounts() != null && submissionPaymentForm.getAccounts().size() > 0) { %>
 			<tr>
               <td height="20">&nbsp;</td>
               <td colspan="2">		 	   
					<p class="footnote"><content:pageFootnotes id="layoutPageBean"/></p>
					<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>
				</td>
 			</tr>
 			<% } %>
			
<c:if test="${ifileConfig.directDebitAccountPresent ==true}">
<c:if test="${submissionPaymentForm.displayMoreSections ==true}">
			<tr>
              <td height="20">&nbsp;</td>
 			  <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
			  <td></td>
            </tr>
 			<tr>
               <td height="20">&nbsp;</td>
               <td colspan="2">		 	   
				<span class="disclaimer"><content:getAttribute beanName="fileUploadPaymentNote" attribute="text"/></span>
				</td>
 			</tr>
</c:if>
</c:if>

<c:if test="${not empty submissionPaymentForm.marketClose}">
<br>${submissionPaymentForm.marketClose}
</c:if>

<script>
	var uploadFormObj = getFormByName("submissionPaymentForm");
	if (typeof(uploadFormObj.elements['amounts[0]']) != "undefined" ) {		
		validatePaymentInstructionInput(uploadFormObj.elements['amounts[0]']);
	}
		
</script>
<% session.removeAttribute("totalContribution");%>

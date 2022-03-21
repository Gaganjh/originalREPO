// Used to prevent multiple submit
var submitInd = false;

// Called when Save is clicked
function doSave() { 
  // Used to prevent multiple submit
  if(submitInd) {
    return false;
  }
  
  submitInd = true;
  
  if (document.forms['employeeEnrollmentSummaryReportForm']) {       
    document.forms['employeeEnrollmentSummaryReportForm'].elements['task'].value = "save"; 
    //alert(document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[0].profileId'].value);
    document.forms['employeeEnrollmentSummaryReportForm'].submit();
  }
}

function clearNameAndPED(evt){

    //IE or browsers that use the getElementById model
    if (document.getElementById('namePhrase')) {
      if (document.getElementById('namePhrase').value) {
        document.getElementById('namePhrase').value = "";
      }
    }
    
    if (document.getElementById('fromPED')) {
      if (document.getElementById('fromPED').value) {
        document.getElementById('fromPED').value = "";
      }
    }
    
    if (document.getElementById('toPED')) {
      if (document.getElementById('toPED').value) {
        document.getElementById('toPED').value = "";
      }
    }
 
    if (document.getElementById('enrolledFrom')) {
      if (document.getElementById('enrolledFrom').value) {
        document.getElementById('enrolledFrom').value = "";
      }
    }
    
    if (document.getElementById('enrolledTo')) {
      if (document.getElementById('enrolledTo').value) {
        document.getElementById('enrolledTo').value = "";
      }
    }
    
  
    //Netscape or browsers that use the document model
      evt = (evt) ? evt : (window.event) ? event : null;
      if (evt)
      { 
        var charCode = (evt.charCode) ? evt.charCode :
                     ((evt.keyCode) ? evt.keyCode :
                     ((evt.which) ? evt.which : 0));
        if (charCode == 9) {
          return false;
        }
      } 
    
    if (document.employeeEnrollmentSummaryReportForm.ssnOne) {
        if (document.employeeEnrollmentSummaryReportForm.ssnOne.value.length >= 0){
            document.employeeEnrollmentSummaryReportForm.namePhrase.value = "";

            if (document.employeeEnrollmentSummaryReportForm.fromPED != null ||
                    document.employeeEnrollmentSummaryReportForm.toPED != null) {
                    
            	document.employeeEnrollmentSummaryReportForm.fromPED.value = "";
            	document.employeeEnrollmentSummaryReportForm.toPED.value = "";            
            }   
            
            if (document.employeeEnrollmentSummaryReportForm.enrolledFrom != null ||
                document.employeeEnrollmentSummaryReportForm.enrolledTo != null) {
                
	            document.employeeEnrollmentSummaryReportForm.enrolledFrom.value = "";
	            document.employeeEnrollmentSummaryReportForm.enrolledTo.value = "";            
            }
        }
    }
    
    if (document.employeeEnrollmentSummaryReportForm.ssnTwo) {
        if (document.employeeEnrollmentSummaryReportForm.ssnTwo.value.length >= 0){ 
          document.employeeEnrollmentSummaryReportForm.namePhrase.value = "";

          if (document.employeeEnrollmentSummaryReportForm.fromPED != null ||
                  document.employeeEnrollmentSummaryReportForm.toPED != null) {
                  
          	document.employeeEnrollmentSummaryReportForm.fromPED.value = "";
          	document.employeeEnrollmentSummaryReportForm.toPED.value = "";            
          }   
	      
          if (document.employeeEnrollmentSummaryReportForm.enrolledFrom != null ||
                document.employeeEnrollmentSummaryReportForm.enrolledTo != null) {
                
	            document.employeeEnrollmentSummaryReportForm.enrolledFrom.value = "";
	            document.employeeEnrollmentSummaryReportForm.enrolledTo.value = "";            
          }	      
        }
    }
  
    if (document.employeeEnrollmentSummaryReportForm.ssnThree) {
        if (document.employeeEnrollmentSummaryReportForm.ssnThree.value.length >= 0){ 
          document.employeeEnrollmentSummaryReportForm.namePhrase.value = "";
          
          if (document.employeeEnrollmentSummaryReportForm.fromPED != null ||
                  document.employeeEnrollmentSummaryReportForm.toPED != null) {
                  
          	document.employeeEnrollmentSummaryReportForm.fromPED.value = "";
          	document.employeeEnrollmentSummaryReportForm.toPED.value = "";            
          }   
          if (document.employeeEnrollmentSummaryReportForm.enrolledFrom != null ||
                document.employeeEnrollmentSummaryReportForm.enrolledTo != null) {
                
              document.employeeEnrollmentSummaryReportForm.enrolledFrom.value = "";
              document.employeeEnrollmentSummaryReportForm.enrolledTo.value = "";            
          }      
        }
    } 
  }

function clearSSNAndPED(evt){

  //IE or browsers that use the getElementById model
  if (document.getElementById('ssnOne')) {
    if (document.getElementById('ssnOne').value) {
      document.getElementById('ssnOne').value = "";
    }
  } 

  if (document.getElementById('ssnTwo')) {
    if (document.getElementById('ssnTwo').value) {
      document.getElementById('ssnTwo').value = "";
    }
  } 

  if (document.getElementById('ssnThree')) {
    if (document.getElementById('ssnThree').value) {
      document.getElementById('ssnThree').value = "";
    }
  }   
  
  if (document.getElementById('fromPED') !=null) {
    if (document.getElementById('fromPED').value) {
      document.getElementById('fromPED').value = "";
    }
  }
  
  if (document.getElementById('toPED') !=null) {
    if (document.getElementById('toPED').value) {
      document.getElementById('toPED').value = "";
    }
  }
  
  if (document.getElementById('enrolledFrom') !=null) {
    if (document.getElementById('enrolledFrom').value) {
      document.getElementById('enrolledFrom').value = "";
    }
  }
  
  if (document.getElementById('enrolledTo') !=null) {
    if (document.getElementById('enrolledTo').value) {
      document.getElementById('enrolledTo').value = "";
    }
  }
  
  
  //Netscape or browsers that use the document model
  evt = (evt) ? evt : (window.event) ? event : null;
    if (evt)
    { 
      var charCode = (evt.charCode) ? evt.charCode :
                   ((evt.keyCode) ? evt.keyCode :
                   ((evt.which) ? evt.which : 0));
      if (charCode == 9) {
        return false;
      }
    }    
  
  if (document.employeeEnrollmentSummaryReportForm.namePhrase) {
      if (document.employeeEnrollmentSummaryReportForm.namePhrase.value.length >= 0){
        document.employeeEnrollmentSummaryReportForm.ssnOne.value = "";
        document.employeeEnrollmentSummaryReportForm.ssnTwo.value = "";
        document.employeeEnrollmentSummaryReportForm.ssnThree.value = "";
        
        if (document.employeeEnrollmentSummaryReportForm.fromPED != null ||
                document.employeeEnrollmentSummaryReportForm.toPED != null) {
                
        	document.employeeEnrollmentSummaryReportForm.fromPED.value = "";
        	document.employeeEnrollmentSummaryReportForm.toPED.value = "";            
        }      
        if (document.employeeEnrollmentSummaryReportForm.enrolledFrom != null ||
                document.employeeEnrollmentSummaryReportForm.enrolledTo != null) {
                
            document.employeeEnrollmentSummaryReportForm.enrolledFrom.value = "";
            document.employeeEnrollmentSummaryReportForm.enrolledTo.value = "";            
        }      
      } 
  }
  
}

function clearSSNAndNameAndPED(evt){

  //IE or browsers that use the getElementById model
  if (document.getElementById('ssnOne')) {
    if (document.getElementById('ssnOne').value) {
      document.getElementById('ssnOne').value = "";
    }
  } 


  if (document.getElementById('ssnTwo')) {
    if (document.getElementById('ssnTwo').value) {
      document.getElementById('ssnTwo').value = "";
    }
  } 

  if (document.getElementById('ssnThree')) {
    if (document.getElementById('ssnThree').value) {
      document.getElementById('ssnThree').value = "";
    }
  }   
  
  if (document.getElementById('fromPED') !=null) {
	    if (document.getElementById('fromPED').value) {
	      document.getElementById('fromPED').value = "";
	    }
	  }
	  
  if (document.getElementById('toPED') !=null) {
    if (document.getElementById('toPED').value) {
      document.getElementById('toPED').value = "";
    }
  }
  
  //Netscape or browsers that use the document model
  evt = (evt) ? evt : (window.event) ? event : null;
    if (evt)
    { 
      var charCode = (evt.charCode) ? evt.charCode :
                   ((evt.keyCode) ? evt.keyCode :
                   ((evt.which) ? evt.which : 0));
      if (charCode == 9) {
        return false;
      }
    }    
  
  
  if (document.employeeEnrollmentSummaryReportForm.namePhrase) {
      if (document.employeeEnrollmentSummaryReportForm.namePhrase.value.length >= 0){
        document.employeeEnrollmentSummaryReportForm.ssnOne.value = "";
        document.employeeEnrollmentSummaryReportForm.ssnTwo.value = "";
        document.employeeEnrollmentSummaryReportForm.ssnThree.value = "";
      } 
  }

  //IE or browsers that use the getElementById model
    if (document.getElementById('namePhrase')) {
      if (document.getElementById('namePhrase').value) {
        document.getElementById('namePhrase').value = "";
      }
    }  
    
    if (document.employeeEnrollmentSummaryReportForm.ssnOne) {
        if (document.employeeEnrollmentSummaryReportForm.ssnOne.value.length >= 0){
            document.employeeEnrollmentSummaryReportForm.namePhrase.value = "";
        }
    }
  
  
    if (document.employeeEnrollmentSummaryReportForm.ssnTwo) {
        if (document.employeeEnrollmentSummaryReportForm.ssnTwo.value.length >= 0){ 
          document.employeeEnrollmentSummaryReportForm.namePhrase.value = "";
        }
    }
  
    if (document.employeeEnrollmentSummaryReportForm.ssnThree) {
        if (document.employeeEnrollmentSummaryReportForm.ssnThree.value.length >= 0){ 
          document.employeeEnrollmentSummaryReportForm.namePhrase.value = "";
        }
    }
    
    if (document.employeeEnrollmentSummaryReportForm.fromPED != null ||
            document.employeeEnrollmentSummaryReportForm.toPED != null) {
            
    	document.employeeEnrollmentSummaryReportForm.fromPED.value = "";
    	document.employeeEnrollmentSummaryReportForm.toPED.value = "";            
    } 
}

function clearSSNAndNameAndEPD(evt){

	  //IE or browsers that use the getElementById model
	  if (document.getElementById('ssnOne')) {
	    if (document.getElementById('ssnOne').value) {
	      document.getElementById('ssnOne').value = "";
	    }
	  } 


	  if (document.getElementById('ssnTwo')) {
	    if (document.getElementById('ssnTwo').value) {
	      document.getElementById('ssnTwo').value = "";
	    }
	  } 

	  if (document.getElementById('ssnThree')) {
	    if (document.getElementById('ssnThree').value) {
	      document.getElementById('ssnThree').value = "";
	    }
	  }   
	  
	  if (document.getElementById('enrolledFrom') !=null) {
		    if (document.getElementById('enrolledFrom').value) {
		      document.getElementById('enrolledFrom').value = "";
		    }
		  }
		  
	  if (document.getElementById('enrolledTo') !=null) {
	    if (document.getElementById('enrolledTo').value) {
	      document.getElementById('enrolledTo').value = "";
	    }
	  }
	  
	  //Netscape or browsers that use the document model
	  evt = (evt) ? evt : (window.event) ? event : null;
	    if (evt)
	    { 
	      var charCode = (evt.charCode) ? evt.charCode :
	                   ((evt.keyCode) ? evt.keyCode :
	                   ((evt.which) ? evt.which : 0));
	      if (charCode == 9) {
	        return false;
	      }
	    }    
	  
	  
	  if (document.employeeEnrollmentSummaryReportForm.namePhrase) {
	      if (document.employeeEnrollmentSummaryReportForm.namePhrase.value.length >= 0){
	        document.employeeEnrollmentSummaryReportForm.ssnOne.value = "";
	        document.employeeEnrollmentSummaryReportForm.ssnTwo.value = "";
	        document.employeeEnrollmentSummaryReportForm.ssnThree.value = "";
	      } 
	  }

	  //IE or browsers that use the getElementById model
	    if (document.getElementById('namePhrase')) {
	      if (document.getElementById('namePhrase').value) {
	        document.getElementById('namePhrase').value = "";
	      }
	    }  
	    
	    if (document.employeeEnrollmentSummaryReportForm.ssnOne) {
	        if (document.employeeEnrollmentSummaryReportForm.ssnOne.value.length >= 0){
	            document.employeeEnrollmentSummaryReportForm.namePhrase.value = "";
	        }
	    }
	  
	  
	    if (document.employeeEnrollmentSummaryReportForm.ssnTwo) {
	        if (document.employeeEnrollmentSummaryReportForm.ssnTwo.value.length >= 0){ 
	          document.employeeEnrollmentSummaryReportForm.namePhrase.value = "";
	        }
	    }
	  
	    if (document.employeeEnrollmentSummaryReportForm.ssnThree) {
	        if (document.employeeEnrollmentSummaryReportForm.ssnThree.value.length >= 0){ 
	          document.employeeEnrollmentSummaryReportForm.namePhrase.value = "";
	        }
	    }
	    
	    if (document.employeeEnrollmentSummaryReportForm.enrolledFrom != null ||
	            document.employeeEnrollmentSummaryReportForm.enrolledTo != null) {
	            
	    	document.employeeEnrollmentSummaryReportForm.enrolledFrom.value = "";
	    	document.employeeEnrollmentSummaryReportForm.enrolledTo.value = "";            
	    } 
	}

/**
 * Enables the submit buttons on first change
 */
function enableSubmit() {
  var x=document.getElementById("saveButton");
  x.innerHTML='<input name="button" type="submit" class="button134" value="SAVE" onclick="return doSave();" />';
  
  x=document.getElementById("cancelButton");
  x.innerHTML='<input name="button" type="submit" class="button134" value="CANCEL" onclick="return doCancel();" />';
   
  return false;
}

/**
 * Flips the hidden field value between Y and N when the checkbox is clicked
 */
function flipIndicator(indexValue) { 
  var autoEnrollOptOut = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].autoEnrollOptOutInd'];
  //var contributionPct = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].contributionPct'];

  if(autoEnrollOptOut != null) {
    if(autoEnrollOptOut.value == 'Y') {
      autoEnrollOptOut.value = 'N';
      //contributionPct.readOnly = false;
    } else {
      autoEnrollOptOut.value = 'Y';
      //contributionPct.readOnly = true;
    }
  }
  
  return true;
}
/**
 * Prevents from choosing blank option; povides little delay
 */
//function languageIndReinstate (indexValue)
//{
//    var languageInd = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].languageInd'];
//    	  if (languageInd.options[languageInd.selectedIndex] && languageInd.options[languageInd.selectedIndex].value != "Y") { 
//	//
//	}
//     return true;
//}
/**
 * Makes the fields available
 */
function removeReadOnly(indexValue) { 
  // var optOut = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].optOut'];
  var eligibilityDate = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].eligibilityDate'];
  //var contributionPct = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].contributionPct'];
  var eligibleToEnroll = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].eligibleToEnroll'];
  // var autoEnrollOptOut = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].autoEnrollOptOutInd'];
  // var isOptOutEditable = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].optOutEditable'];
  
  if (eligibleToEnroll != null) {
	  if (eligibleToEnroll.options[eligibleToEnroll.selectedIndex] && eligibleToEnroll.options[eligibleToEnroll.selectedIndex].value != "Y") { 
      // Make them read only
      eligibilityDate.readOnly = true;
     // if(optOut != null )
    //	  optOut.disabled = true;
      //contributionPct.readOnly = true;  
    } else {
      // Make them editable
      eligibilityDate.readOnly = false;
    /*  if(isOptOutEditable != null && isOptOutEditable.value == "true")
      {
    	  if(optOut != null )
    		  optOut.disabled = false;
      } */
      
      // Still read only if opt-out
      //if(autoEnrollOptOut && autoEnrollOptOut.value == "Y") {
      //  contributionPct.readOnly = true;  
      //} else {
      //  contributionPct.readOnly = false;
      //}
        
    } 
  } 
  
  return true;
}

function removeNotSelectedOptionLang(indexValue) { 

	  var languageInd = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItemList[' + indexValue + '].languageInd'];
	 	  if (languageInd != null) {
	     for (var i = 0; i < 3; i++) {
			if (languageInd.options[i] && languageInd.options[i].value == "NS") {
	        languageInd.options[i] = null; // Remove the option
	       // languageIndReinstate (indexValue);
	        enableSubmit();
	      }
	     }  
	  }
	   
	  return true;
	}

function languageIndReinstate (indexValue){	

	var showInitialLangDIV = document.getElementById("showInitialLangDIV"+indexValue);
	var showNextLangDIV = document.getElementById("showNextLangDIV"+indexValue);
   	showNextLangDIV.style.display = "block";
	showInitialLangDIV.style.display = "none";	
}

//function languageIndReinstate (indexValue)
//{
//    var languageInd = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].languageInd'];
//    	  if (languageInd.options[languageInd.selectedIndex] && languageInd.options[languageInd.selectedIndex].value != "Y") { 
//	//
//	}
//     return true;
//}
//function languageIndReinstate (indexValue,languageInd)
//{   
//	alert("languageIndReinstate"+indexValue+"   languagerind : "+languageInd);
//	document.forms.employeeEnrollmentSummaryReportForm.elements['theItem[' + indexValue + ']'].value=languageInd;
//
//
//     return true;
//}
//function removeNotSelectedOptionLang(indexValue) { 
//	alert("removeNotSelectedOptionLang.....");
//  var languageInd = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].languageInd'];
//	 var languageInd = document.employeeEnrollmentSummaryReportForm.elements['theItem[' + indexValue + '].languageInd'];
//  alert("languageInd....."+languageInd);
//  if (languageInd != null) {
//     for (var i = 0; i < 3; i++) {
//    	 alert("languageIndval....."+languageInd.options[i].value);
//      if (languageInd.options[i] && languageInd.options[i].value == "") {
//        languageInd.options[i] = null; // Remove the option
//        languageIndReinstate (indexValue);
//        enableSubmit();
//      }
//     }  
//  }
//  
//  return true;
//}
function removeNotSelectedOption(indexValue) { 
  var eligibleToEnroll = document.forms['employeeEnrollmentSummaryReportForm'].elements['theItem[' + indexValue + '].eligibleToEnroll'];
  
  if (eligibleToEnroll != null) {
     for (var i = 0; i < 3; i++) {
      if (eligibleToEnroll.options[i] && eligibleToEnroll.options[i].value == "") { 
        eligibleToEnroll.options[i] = null; // Remove the option
        removeReadOnly(indexValue);
        enableSubmit();
      }
     }  
  }
  
  return true;
}
   
function openStatisticsWindow(){
  windowOptions = "'toolbar=yes,location=no,scrollbars=yes,resizable=no,width=536,height=203,left=250,top=250'"
  changeWindow = window.open('/do/census/enrollmentStatsSnapshot/','changeWin',windowOptions)
}

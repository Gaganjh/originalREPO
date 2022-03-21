function clearSSN(event){
  if (document.getElementById('ssnFirstGroup')) {
    if (document.getElementById('ssnFirstGroup').value) {
      document.getElementById('ssnFirstGroup').value = "";
    }
  } 

  if (document.getElementById('ssnSecondGroup')) {
    if (document.getElementById('ssnSecondGroup').value) {
      document.getElementById('ssnSecondGroup').value = "";
    }
  } 

  if (document.getElementById('ssnThirdGroup')) {
    if (document.getElementById('ssnThirdGroup').value) {
      document.getElementById('ssnThirdGroup').value = "";
    }
  } 
}

function clearName(event){
  if (document.getElementById('lastName')) {
    if (document.getElementById('lastName').value) {
      document.getElementById('lastName').value = "";
    }
  } 
}

function doSearch() {		
  if (discardChanges("Are you sure you want to search and discard changes?")==false) {
        return false;
    }
	document.forms['payrollSelfServiceChangesForm'].elements['task'].value = "filter";
	return true;
}

function doReset() {
  if (discardChanges("Are you sure you want to reset and discard changes?")==false) {
      return false;
  }
  document.forms['payrollSelfServiceChangesForm'].elements['task'].value = "reset";
  return true;
}

function sortSubmit(sortField, sortDirection) {
	document.forms['payrollSelfServiceChangesForm'].elements['task'].value = "sort";
	document.forms['payrollSelfServiceChangesForm'].elements['pageNumber'].value = 1;
	document.forms['payrollSelfServiceChangesForm'].elements['sortField'].value = sortField;
	document.forms['payrollSelfServiceChangesForm'].elements['sortDirection'].value = sortDirection;
	document.forms['payrollSelfServiceChangesForm'].submit();
}
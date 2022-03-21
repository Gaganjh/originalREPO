var DEBUG = false;
var isSubmitted = false;

/* Helper Functions */
function getCheckBoxValue(checkBoxArray)
{
  if (checkBoxArray != null) {
    if (checkBoxArray.length == 1) {
      return getBooleanValue(checkBoxArray.value);
    }
    for (var i =0; i < checkBoxArray.length; i++) {
      if (checkBoxArray[i].checked) {
        return getBooleanValue(checkBoxArray[i].value);
      }
    }
  }
  return null;
}

/*
 * Returns the boolean value of the given parameter. Anything
 * other than "yes", "true" and 1 are considered false.
 */
function getBooleanValue(theValue)
{
  if (theValue == "yes" || theValue == 1 || theValue == "true")
    return true;
  else
    return false;
}

function getNamedElement(theForm, theName)
{
  var elements = document.getElementsByName(theName);
  if (elements == null) {
    return null;
  } else {
    if (elements.length == 1) {
      return elements[0];
    } else {
      return elements;
    }
  }
}

function enable(theForm, theName)
{
  var elements = document.getElementsByName(theName);
  if (elements == null) {
    return null;
  } else {
    for (var i = 0; i < elements.length; i++) {
	   elements[i].disabled = false;
    }
  }
}

/* end of helper functions */

/**
 * doSubmit creates objects from the form, calls doValidate and returns true if validation is ok
 */
function doSubmit(theForm)
{
  if(DEBUG)
    alert("inside doSubmit");

  // create a ueer profile object
  var userProfile = new UserProfile();
  // userProfile.firstName = theForm.firstName.value;
  // userProfile.lastName = theForm.lastName.value;
  // userProfile.email = theForm.email.value;

  // if(DEBUG)
  //   alert(userProfile.toString());

  for(var i = 0; i < theForm.elements.length; i++) {
    var element = theForm.elements[i];
    // if this is the start of a contract then create one
    if (element.name.indexOf("contractNumber") != -1) {
      if (DEBUG)
        alert(element.name);
        
      var indexOfDot = element.name.indexOf(".");
      var prefix = element.name.substring(0, indexOfDot);

      /*
       * This is for the Add/Edit TPA user page. We need to obtain the
       * TPA firm level values before we change the prefix.
       */
      var tpaFirmIdInput = getNamedElement(theForm, prefix + ".id");
      if (prefix.indexOf("tpaFirm[") != -1) {
        prefix = prefix + ".contractAccesses[0]";
      }

      var contractNumberInput = getNamedElement(theForm, prefix + ".contractNumber");

      var contractPerm = new ContractPermission(contractNumberInput.value);
      var planSponsorSiteRoleInput = getNamedElement(theForm, prefix + ".planSponsorSiteRole");

      if (tpaFirmIdInput != null) {
          contractPerm.tpaFirmId = tpaFirmIdInput.value;
      }
       
      contractPerm.planSponsorSiteRole = planSponsorSiteRoleInput.value;
      contractPerm.oldPlanSponsorSiteRole = changeTracker.getOldValue(prefix + ".planSponsorSiteRole");

      var submissionAccessUserCountInput = getNamedElement(theForm, prefix + ".submissionAccessUserCount");
      contractPerm.submissionAccessUserCount = submissionAccessUserCountInput.value;

      var pseumCountInput = getNamedElement(theForm, prefix + ".pseumCount");
      contractPerm.pseumCount = pseumCountInput.value;

      contractPerm.iFileAccess = getCheckBoxValue(getNamedElement(theForm, prefix + ".ifileAccessAvailable"));
      if (contractPerm.iFileAccess != null) {
        contractPerm.oldIFileAccess = getBooleanValue(changeTracker.getOldValue(prefix + ".ifileAccessAvailable"));
      }
      contractPerm.submissionAccess = getCheckBoxValue(getNamedElement(theForm, prefix + ".submissionAccess"));
      if (contractPerm.submissionAccess != null) {
        contractPerm.oldSubmissionAccess = getBooleanValue(changeTracker.getOldValue(prefix + ".submissionAccess"));
      }
      contractPerm.uploadAccess = getCheckBoxValue(getNamedElement(theForm, prefix + ".uploadSubmissions"));

      contractPerm.directDebitAccess = getCheckBoxValue(getNamedElement(theForm, prefix + ".directDebit"));
      contractPerm.cashAccountAccess = getCheckBoxValue(getNamedElement(theForm, prefix + ".cashAccount"));
      contractPerm.viewAllSubmissions = getCheckBoxValue(getNamedElement(theForm, prefix + ".viewAllSubmissions"));

      var oldUploadAccess = getBooleanValue(changeTracker.getOldValue(prefix + ".uploadPermissions"));
      var oldDirectDebitAccess = getBooleanValue(changeTracker.getOldValue(prefix + ".directDebit"));
      var oldCashAccountAccess = getBooleanValue(changeTracker.getOldValue(prefix + ".cashAccount"));
      var oldViewAllSubmissions = getBooleanValue(changeTracker.getOldValue(prefix + ".viewAllSubmissions"));
      contractPerm.oldHasIfileRelatedAccess = oldUploadAccess || oldDirectDebitAccess || oldCashAccountAccess;
      contractPerm.oldDirectDebitAccess = oldDirectDebitAccess;
      contractPerm.oldHasViewSubmissionRelatedAccess = contractPerm.oldHasIfileRelatedAccess || oldViewAllSubmissions;

      var numberOfSelectedDebitAccountsInput = getNamedElement(theForm, prefix + ".numberOfSelectedDirectDebitAccounts");
      if (numberOfSelectedDebitAccountsInput != null) {
        contractPerm.oldNumberOfSelectedDirectDebitAccounts = numberOfSelectedDebitAccountsInput.value;
      }

	  var directDebitAccounts = document.getElementsByName(prefix + ".selectedDirectDebitAccounts");
	  if (directDebitAccounts != null && directDebitAccounts.length != 0) {
	    var directDebitAccountsChecked = false;
	  	for (var accountIndex = 0; accountIndex < directDebitAccounts.length; accountIndex++) {
	  	  if (directDebitAccounts[accountIndex].value != "none" && directDebitAccounts[accountIndex].checked) {
	  	    directDebitAccountsChecked = true;
	  	    break;
	  	  }
	  	}
	    contractPerm.hasDirectDebitAccount = true;
	  	contractPerm.noDirectDebitAccountsSelected = !directDebitAccountsChecked;
	  } else {
	  	contractPerm.hasDirectDebitAccount = false;
	  	contractPerm.noDirectDebitAccountsSelected = true;
	  }

      contractPerm.statementAccess = getCheckBoxValue(getNamedElement(theForm, prefix + ".statementsAccessAvailable"));
      contractPerm.participantAddressDownloadAccess = getCheckBoxValue(getNamedElement(theForm, prefix + ".participantAddressDownloadAccessAvailable"));
      if (DEBUG)
        alert(contractPerm.toString());

      userProfile.contractPermissions.push(contractPerm);
    }
  }
  if (doValidate(userProfile)) {
     if (!isSubmitted)  {
        isSubmitted = true;

/*
		 for(var i = 0; i < theForm.elements.length; i++) {
 
		    var element = theForm.elements[i];
	
		    // if this is the start of a contract then create one
		    if (element.name.indexOf("contractNumber") != -1) {
		        
		      var indexOfDot = element.name.indexOf(".");
		      var prefix = element.name.substring(0, indexOfDot);

		      // This is for the Add/Edit TPA user page. We need to obtain the
		      // TPA firm level values before we change the prefix.
		      var tpaFirmIdInput = getNamedElement(theForm, prefix + ".id");
		      if (prefix.indexOf("tpaFirm[") != -1) {
		        prefix = prefix + ".contractAccesses[0]";
		      }  
	
		      enable(theForm, prefix + ".ifileAccessAvailable");
		      enable(theForm, prefix + ".submissionAccess");
		      enable(theForm, prefix + ".uploadSubmissions");
		      enable(theForm, prefix + ".directDebit");
		      enable(theForm, prefix + ".cashAccount");
		      enable(theForm, prefix + ".viewAllSubmissions");
		    }
		 }
*/
	    return true;
	 } else {
	    window.status = "Transaction already in progress.  Please wait.";
	 }
  }
  return false;
}


/*
 * UserProfile is a represntation of the form used for user add/edit
 * the user has personal info and an array of ContractPermission objects
 */
function UserProfile()
{
  this.firstName = null;
  this.lastName = null;
  this.email = null;
  this.contractPermissions = new Array();
  this.hasNoAccessToAnyContract = userProfileHasNoAccessToAnyContract;
  this.toString = userProfileToString;
}

/*
 * for a userProfile returns true if an access level is selected and it is not "no access"
 */
function userProfileHasNoAccessToAnyContract()
{
  if (this.contractPermissions.length != 0) {
    for (var i = 0; i < this.contractPermissions.length; i++) {
      if (this.contractPermissions[i].planSponsorSiteRole != null &&
          this.contractPermissions[i].planSponsorSiteRole != "" &&
          this.contractPermissions[i].planSponsorSiteRole != ContractPermission.ACCESS_LEVEL_NONE)
         return false;
    }
  }
  return true;
}

function userProfileToString()
{
  var result = "User Profile: \n";
  result += "firstName = " + this.firstName;
  result += "; lastName = " + this.lastName;
  result += "; email = " + this.email;

  return result;

}

/*
 * ContractPermission is an object representation of the contract flags in the form
 */
function ContractPermission(theContractNumber)
{
  if (DEBUG)
    alert("a new ContractPermission is being created with: " + theContractNumber);

  /* CONSTANTS */
  ContractPermission.ACCESS_LEVEL_NONE = "NA";
  ContractPermission.ACCESS_LEVEL_PLAN_ADMIN = "PA";
  ContractPermission.ACCESS_LEVEL_PAYROLL_CLERK = "PYA";
  ContractPermission.ACCESS_LEVEL_PS_USER_MANAGER = "PSEUM";


  /* variables */
  this.contractNumber = theContractNumber;
  this.planSponsorSiteRole = ContractPermission.ACCESS_LEVEL_NONE;
  this.oldPlanSponsorSiteRole = ContractPermission.ACCESS_LEVEL_NONE;
  this.iFileAccess = false;
  this.oldIFileAccess = false;
  this.uploadAccess =false;
  this.directDebitAccess = false;
  this.cashAccountAccess = false;
  this.oldCashAccountAccess = false;
  this.statementAccess = false;
  this.participantAddressDownloadAccess = false;
  this.iFileUserCount = 0;
  this.oldHasIfileRelatedAccess = false;
  this.oldHasViewSubmissionRelatedAccess = false;
  this.submissionAccess = false;
  this.oldSubmissionAccess = false;
  this.oldDirectDebitAccess = false;
  this.hasDirectDebitAccounts = false;
  this.noDirectDebitAccountsSelected = false;
  this.oldNumberOfSelectedDirectDebitAccounts = 0;
  this.pseumCount = 0;

  /* methods() */
  this.isNoAccess = contractPermissionIsNoAccess;
  this.hasNoiFileAccess  = conrtactPermissionHasNoiFileAccess;
  this.hasiFileRelatedAccessButNoiFile  = contractPermissionHasiFileRelatedAccessButNoiFile;
  this.noUserWithiFileAccessForContract = contractPermissionNoUserWithiFileAccessForContract;
  this.noUserWithPSEUMAccessForContract = contractPermissionNoUserWithPSEUMAccessForContract;
  this.toString = contractPermissionToString;
  this.hasSubmissionAccessOnly = contractPermissionHasSubmissionAccessOnly;
  this.hasNoSubmissionAccess  = conrtactPermissionHasNoSubmissionAccess;
  this.noUserWithSubmissionAccessForContract = contractPermissionNoUserWithSubmissionAccessForContract;
  this.noSelectedDirectDebitAccounts = contractPermissionHasNoSelectedDirectDebitAccounts;
}

function contractPermissionHasSubmissionAccessOnly()
{
  if (this.submissionAccess != null && this.submissionAccess) {
    if (this.oldSubmissionAccess == false || this.oldHasViewSubmissionRelatedAccess) {
        var hasOtherAccess = false;
        if (this.uploadAccess != null && this.uploadAccess) {
        	hasOtherAccess = true;
        }
        if (this.directDebitAccess != null && this.directDebitAccess) {
        	hasOtherAccess = true;
        }
        if (this.cashAccountAccess != null && this.cashAccountAccess) {
        	hasOtherAccess = true;
        }
        if (this.viewAllSubmissions != null && this.viewAllSubmissions) {
        	hasOtherAccess = true;
        }
        return !hasOtherAccess;
    }
  }
  return false;
}

/*
 * Checks whether the contract permission has access level = No Access
 */
function contractPermissionIsNoAccess()
{
  return (this.planSponsorSiteRole == ContractPermission.ACCESS_LEVEL_NONE);
}

/*
 * for a contract permission returns true if the role is "payroll clerk" and no iFile is selected
 */
function conrtactPermissionHasNoiFileAccess()
{
  if (this.iFileAccess != null) {
    if (this.oldIFileAccess == true && !this.iFileAccess) {
      return this.planSponsorSiteRole == ContractPermission.ACCESS_LEVEL_PAYROLL_CLERK;
    } else if (this.planSponsorSiteRole == ContractPermission.ACCESS_LEVEL_PAYROLL_CLERK &&
           this.oldPlanSponsorSiteRole != ContractPermission.ACCESS_LEVEL_PAYROLL_CLERK) {
      return !this.iFileAccess;
    }
    }
  return false;
}

/*
 * for a contract permission returns true if the role is "payroll clerk" and no iFile is selected
 */
function conrtactPermissionHasNoSubmissionAccess()
{
  if (this.submissionAccess != null) {
    if (this.oldSubmissionAccess == true && !this.submissionAccess) {
      return this.planSponsorSiteRole == ContractPermission.ACCESS_LEVEL_PAYROLL_CLERK;
    } else if (this.planSponsorSiteRole == ContractPermission.ACCESS_LEVEL_PAYROLL_CLERK &&
           this.oldPlanSponsorSiteRole != ContractPermission.ACCESS_LEVEL_PAYROLL_CLERK) {
      return !this.submissionAccess;
    }
    }
  return false;
}

/*
 * Assume direct debit access is already checked.
 */
function contractPermissionHasNoSelectedDirectDebitAccounts()
{
	if (this.oldDirectDebitAccess &&
		this.oldNumberOfSelectedDirectDebitAccounts == '0') {
	  return false;
	}
	return this.noDirectDebitAccountsSelected;
}

/*
 * for a contract permission returns true if the cash account, direct debt, ... are selected
 * and no ifile access is given
 */
function contractPermissionHasiFileRelatedAccessButNoiFile()
{
  if (this.planSponsorSiteRole != ContractPermission.ACCESS_LEVEL_NONE) {
    if (this.iFileAccess != null && !this.iFileAccess) {
      /*
       * Check flag only if someone changed ifile access to false or
       * the original related permissions are all false.
       */
      if (this.oldIFileAccess || !this.oldHasIfileRelatedAccess) {
        if (this.uploadAccess || this.directDebitAccess || this.cashAccountAccess) {
          return true;
        }
      }
    }
  }

  return false;
}

/*
 * for a contract permission returns true if no more users are left with iFile access to this contract
 */
function contractPermissionNoUserWithiFileAccessForContract() {
  if (this.planSponsorSiteRole != ContractPermission.ACCESS_LEVEL_NONE && this.iFileAccess != null) {
  	 if ((this.submissionAccessUserCount == 0 && !this.iFileAccess) || 
  	 	 (this.submissionAccessUserCount == 1  && !this.iFileAccess && this.oldIFileAccess)) {
         return true;
     }
  }  
  return false;
}

/*
 * for a contract permission returns true no more users are left with submission access to this contract
 */
function contractPermissionNoUserWithSubmissionAccessForContract()
{
  if (this.submissionAccessUserCount == "0") {
     if (this.oldSubmissionAccess == true) {
        if ((this.submissionAccess != null && !this.submissionAccess) ||
           (this.planSponsorSiteRole == ContractPermission.ACCESS_LEVEL_NONE)) {
         return true;
        }
     }
  }
  return false;
}

/*
 * for a contract permission returns true if no more users are left with PSEUM access to this contract
 */
function contractPermissionNoUserWithPSEUMAccessForContract()
{
  if (this.pseumCount == "1" && 
  		this.oldPlanSponsorSiteRole == ContractPermission.ACCESS_LEVEL_PS_USER_MANAGER &&
  		this.planSponsorSiteRole != ContractPermission.ACCESS_LEVEL_PS_USER_MANAGER )
  {
     return true;
  }  
  return false;
}

function contractPermissionToString()
{
  var result = "Contract Permission: \n";
  result += "contractNumber =" + this.contractNumber;
  result += "; planSponsorSiteRole =" + this.planSponsorSiteRole;
  result += "; oldPlanSponsorSiteRole =" + this.oldPlanSponsorSiteRole;
  result += "; iFileAccess =" + this.iFileAccess;
  result += "; submissionAccess =" + this.submissionAccess;
  result += "; oldIFileAccess =" + this.oldIFileAccess;
  result += "; uploadAccess =" + this.uploadAccess;
  result += "; directDebitAccess =" + this.directDebitAccess;
  result += "; cashAccountAccess =" + this.cashAccountAccess;
  result += "; statementAccess =" + this.statementAccess;
  result += "; participantAddressDownloadAccess =" + this.participantAddressDownloadAccess;
  result += "; iFileUserCount =" + this.iFileUserCount;
  result += "; pseumCount =" + this.pseumCount;
  return result;
}

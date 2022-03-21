<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper" %>

<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningDiscardChanges"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_NO_ACCESS_TO_ANY_CONTRACT%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoAccessToAnyContract"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_CONTRACT_ACCESS_IS_NO_ACCESS%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="contractAccessIsNoAccess"/>
<content:contentBean contentId="<%=ContentConstants.ERROR_ADD_USER_NO_ACCESS_TO_ANY_CONTRACT %>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="errorNoAccessToAnyContract"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_NO_I_FILE_PERMISSION%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoIfilePermission"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_NO_I_FILE_ACCESS%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoIfileAccess"/> 
<content:contentBean contentId="<%=ContentConstants.WARNING_NO_I_FILE_USERS%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoIfileUsers"/>                           	
<content:contentBean contentId="<%=ContentConstants.WARNING_SUBMISSION_ACCESS_ONLY%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningSubmissionAccessOnly"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_NO_SUBMISSION_ACCESS%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoSubmissionAccess"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_NO_SUBMISSION_ACCESS_FOR_CONTRACT%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoSubmissionAccessUsers"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_AVAILABLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoDirectDebitAccountAvailable"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_NO_DIRECT_DEBIT_ACCOUNT_SELECTED%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningNoDirectDebitAccountSelected"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_LAST_PSEUM%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                   	 id="warningLastPSEUM"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_TPA_LAST_USER_RECEIVE_ILOANS_EMAIL%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningLastUserReceiveIloansEmail"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_TPA_LAST_USER_STAFF_PLAN_AND_RECEIVE_ILOANS_EMAIL%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningLastUserStaffPlanAndReceiveIloansEmail"/> 
	
<content:contentBean contentId="<%=ContentConstants.WARNING_LAST_TPAUM%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningLastTPAUM"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_TPA_NO_MORE_REGISTERED_USER%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningTpaNoMoreRegisteredUser"/>




<script type="text/javascript" >
function writeError(text) {
	var contentString;
	if (text.length > 0 )
		contentString = '<table id="psErrors"><tr><td class="redText"><ul><li>' +
			text + '</li></ul></td></tr></table>';
	else 
		contentString = '';
	
	document.getElementById('errordivcs').innerHTML = contentString;

	if (text!="" && text!=" " &&text.length>0) {
		location.href='#TopOfPage';
	}
}

function doCancel(theForm) {
	return discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>");
}

/**
 * doValidate checks for all business rules, displays confirmations for warnings
 * and if all is OK returns true
 */
function doValidate(userProfile) {
	if (DEBUG) { 
	alert("inside doValidate");
	}
	<c:if test="${empty showTpaPermissionsOnly }" >
	<logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
      isAdd = false;
	  <logicext:then>
	// MPR 38 & 230. System must display a warning message if profile has no remaining contracts associated with the user.
	if (userProfile.hasNoAccessToAnyContract()) {
          return confirm("<content:getAttribute beanName="warningNoAccessToAnyContract" attribute="text" filter="true"/>");
    }
	  </logicext:then>
	</logicext:if>

	</c:if>

	var warningMessage = "";
	var tpaFirmsLastUserReceiveIloansEmail = "";
	var tpaFirmsLastUserStaffPlanAndReceiveIloansEmail = "";
	var tpaFirmsLastTPAUM ="";
	var warningMessagePrefix = "";
	var tpaFirmsNoMoreRegisteredUser = "";

	// the following are per contractPermission
	for (var i = 0; i < userProfile.contractPermissions.length; i++) {
		var contractPermission = userProfile.contractPermissions[i];

	    <logicext:if name="layoutBean" property="id" op="equal" value="/profiles/addTpaUser.jsp">
	        <logicext:or name="layoutBean" property="id" op="equal" value="/profiles/editTpaUser.jsp"/>
	        <logicext:then>
			warningMessagePrefix = "\nTPA firm " + contractPermission.tpaFirmId + " ";
	        </logicext:then>
	        <logicext:else>
			warningMessagePrefix = "\nContract " + contractPermission.contractNumber + " ";
			</logicext:else>
		</logicext:if>

	<c:if test="${empty showTpaPermissionsOnly }" >

// MPR.216 - System would display warning if any of the Access levels is "No Access" (includes both changed access levels and access levels for newly added contracts).
		if (contractPermission.isNoAccess()) {
			warningMessage += warningMessagePrefix + "<content:getAttribute beanName="contractAccessIsNoAccess" attribute="text" filter="true"/>";
        }

// PIR7. System must display a warning if access level selected is Payroll administrator but submission permissions were not added.
		if (contractPermission.submissionAccess != null && contractPermission.hasNoSubmissionAccess() ) {
			warningMessage += warningMessagePrefix + "<content:getAttribute beanName="warningNoSubmissionAccess" attribute="text" filter="true"/>";
		}

// MPR 42. System must display a warning if role selected is Payroll Administrator but  I:File permissions was  not added.User will be able to save and continue even though Payroll Administrator does not have I:file permission.
		if (contractPermission.submissionAccess == null && contractPermission.hasNoiFileAccess() ) {
			warningMessage += warningMessagePrefix + "<content:getAttribute beanName="warningNoIfilePermission" attribute="text" filter="true"/>";
		}

// MPR 43. System must display a warning if user does not select iFile access BUT does select one or more of the following Upload File, Cash Account, Direct Debit Account. Permissions for the user.
		if (contractPermission.hasiFileRelatedAccessButNoiFile() ) {
			warningMessage += warningMessagePrefix + "<content:getAttribute beanName="warningNoIfileAccess" attribute="text" filter="true"/>";
		}

// MPR 231. System must display a warning message if profile contract that was removed has no remaining users with I:file access.
		if (!isAdd && contractPermission.noUserWithiFileAccessForContract() ) {
			warningMessage += warningMessagePrefix + "<content:getAttribute beanName="warningNoIfileUsers" attribute="text" filter="true"/>";
		}

// PIR4.2. A warning message will be displayed on Save/Edit informing the user that the user will only have access to the view
// submission tool but will not be albe to perform any functions.
		if (contractPermission.hasSubmissionAccessOnly()) {
			warningMessage += warningMessagePrefix + "<content:getAttribute beanName="warningSubmissionAccessOnly" attribute="text" filter="true"/>";
		}

// MPR.606 that if as a result of saving on Edit page, there are NO LONGER any PSEUMs for the contract, a warning should display.
		if (!isAdd && contractPermission.noUserWithPSEUMAccessForContract() ) {
			warningMessage += warningMessagePrefix + "<content:getAttribute beanName="warningLastPSEUM" attribute="text" filter="true"/>";
		}
	</c:if>

	<c:if test="${not empty showTpaPermissionsOnly }" >
// TPA10. A warning message will be displayed on Save/Edit informing the user that the user will only have access to the view
// submission tool but will not be albe to perform any functions.

		if (contractPermission.hasSubmissionAccessOnly()) {
		    /*
		     * TPA firm ID would be null if we're coming from Manage TPA firm permission page.
		     */
		    if (contractPermission.tpaFirmId != null) {
		      warningMessage += "\nTPA firm " + contractPermission.tpaFirmId + " ";
		    } else {
		      warningMessage += "\n";
		    }
			warningMessage += "<content:getAttribute beanName="warningSubmissionAccessOnly" attribute="text" filter="true"/>";
		}
	</c:if>

    <logicext:if name="layoutBean" property="id" op="equal" value="/profiles/addTpaUser.jsp">
        <logicext:or name="layoutBean" property="id" op="equal" value="/profiles/editTpaUser.jsp"/>
        <logicext:then>

          var lastUserWithReceiveILoansEmailPermissionElement = document.getElementsByName("tpaFirm[" + i + "].lastUserWithReceiveILoansEmailPermission")[0];
          var lastUserWithReceiveILoansEmailPermission = lastUserWithReceiveILoansEmailPermissionElement.value;
	     // alert ("lastUserWithReceiveILoansEmailPermission " +lastUserWithReceiveILoansEmailPermission);	  
          var lastUserWithTpaStaffPlanPermissionElement = document.getElementsByName("tpaFirm[" + i + "].lastUserWithTpaStaffPlanPermission")[0];
          var lastUserWithTpaStaffPlanPermission = lastUserWithTpaStaffPlanPermissionElement.value;
		//  alert ("lastUserWithTpaStaffPlanPermission " +lastUserWithTpaStaffPlanPermission);
          var tpaFirmIdElement = document.getElementsByName("tpaFirm[" + i + "].id")[0];
          var tpaFirmIdValue = tpaFirmIdElement.value;
	      var receiveIloansEmailElement = document.getElementsByName("tpaFirm[" + i + "].contractAccesses[0].receiveIloansEmail")[0];
	      var receiveIloansEmailChecked = receiveIloansEmailElement.checked;	  
	      var receiveIloansEmailOldValue = changeTracker.getOldValue("tpaFirm[" + i + "].contractAccesses[0].receiveIloansEmail");
	    
	      var tpaStaffPlanElelement = null;
		  tpaStaffPlanElelement = document.getElementsByName("tpaFirm[" + i + "].contractAccesses[0].tpaStaffPlanAccess")[0];
	      var tpaStaffPlanChecked = false;
		  var tpaStaffPlanOldValue = "false";
		  if(tpaStaffPlanElelement !=null){
		  //	alert("tpaStaffPlan " + tpaStaffPlanElelement.value);
		  	tpaStaffPlanChecked =tpaStaffPlanElelement.checked;
	      	tpaStaffPlanOldValue = changeTracker.getOldValue("tpaFirm[" + i + "].contractAccesses[0].tpaStaffPlanAccess");
		  }
 		  var planSponsorSiteRoleInput =document.getElementsByName("tpaFirm[" + i + "].contractAccesses[0].planSponsorSiteRole")[0];
          var planSponsorSiteRole =planSponsorSiteRoleInput.value; 
		 // alert(" planSponsorSiteRole " +planSponsorSiteRole);

   		  var oldPlanSponsorSiteRole =changeTracker.getOldValue("tpaFirm[" + i + "].contractAccesses[0].planSponsorSiteRole");
      	// alert("  old planSponsorSiteRole " + oldPlanSponsorSiteRole);	 
		 
		 var lastRegisteredUserElement = document.getElementsByName("tpaFirm[" + i + "].lastRegisteredUser")[0];
		 var lastRegisteredUser = lastRegisteredUserElement.value;
		 
		 if(lastRegisteredUser == "true" && planSponsorSiteRole == "NA"){
			if ( tpaFirmsNoMoreRegisteredUser.length > 0 ) {
				tpaFirmsNoMoreRegisteredUser += ", ";
			}
			tpaFirmsNoMoreRegisteredUser += tpaFirmIdValue; 
		}
		 
		 
		 
		 
		 var lastTPAUMElement =document.getElementsByName("tpaFirm[" + i + "].lastTPAUM")[0];	  
		  var lastTPAUM = lastTPAUMElement.value;		
		  //alert("last tpaum " +  lastTPAUM);
		 	if(lastTPAUM == "true" && planSponsorSiteRole !="TUM"){
				if ( tpaFirmsLastTPAUM.length > 0 ) {
					tpaFirmsLastTPAUM += ", ";
				}
				tpaFirmsLastTPAUM += tpaFirmIdValue; 
			}

		 
		  if ( planSponsorSiteRole=="NA" ||( !receiveIloansEmailChecked && receiveIloansEmailOldValue) ) {
			  if ( lastUserWithReceiveILoansEmailPermission == "true" ) {
				if ( tpaFirmsLastUserReceiveIloansEmail.length > 0 ) {
				  tpaFirmsLastUserReceiveIloansEmail += ", ";
				}
				tpaFirmsLastUserReceiveIloansEmail += tpaFirmIdValue; 
			  }
		  }

		  if (tpaStaffPlanOldValue =="true"  && receiveIloansEmailOldValue =="true")
		  {
		  if(!receiveIloansEmailChecked ||!tpaStaffPlanChecked ||planSponsorSiteRole=="NA")
		  {
				if(lastUserWithTpaStaffPlanPermission == "true" || lastUserWithReceiveILoansEmailPermission == "true" )
				{				
					if ( tpaFirmsLastUserStaffPlanAndReceiveIloansEmail.length > 0 ) {
					tpaFirmsLastUserStaffPlanAndReceiveIloansEmail += ", ";
					}   			    
					tpaFirmsLastUserStaffPlanAndReceiveIloansEmail += tpaFirmIdValue;
				}
		  }
		}
	  

        </logicext:then>
        <logicext:else>
// ICE14. System must display a warning message if profile contract that was removed has no remaining users with submission access.
		if (!isAdd && contractPermission.noUserWithSubmissionAccessForContract() ) {
				warningMessage += warningMessagePrefix + "<content:getAttribute beanName="warningNoSubmissionAccessUsers" attribute="text" filter="true"/>";
		}
		
		if (contractPermission.directDebitAccess != null &&
			contractPermission.submissionAccess != null) {
// PIR4.1.5.6 If contract does not have any available direct debit accounts, on save, the system will display a message.
			if (contractPermission.directDebitAccess && !contractPermission.hasDirectDebitAccount) {
				warningMessage += warningMessagePrefix + "<content:getAttribute beanName="warningNoDirectDebitAccountAvailable" attribute="text" filter="true"/>";
			} else {
// PIR4.1.5.4 If user deselect all direct debit accounts
				if (contractPermission.directDebitAccess &&
				    contractPermission.noSelectedDirectDebitAccounts()) {
					warningMessage += warningMessagePrefix + "<content:getAttribute beanName="warningNoDirectDebitAccountSelected" attribute="text" filter="true"/>";				    
				}
			}
		}
       </logicext:else>
    </logicext:if>
    }
    <logicext:if name="layoutBean" property="id" op="equal" value="/profiles/addTpaUser.jsp">
        <logicext:or name="layoutBean" property="id" op="equal" value="/profiles/editTpaUser.jsp"/>
        <logicext:then>
//TTP.1 if, as a result of editing the profile one or more of the firms on the user's profile  will have no remaining users with Receive I:loans Email permission.
          if ( tpaFirmsLastUserReceiveIloansEmail.length > 0 ) {
			warningMessage += '\nFirm(s): ' + tpaFirmsLastUserReceiveIloansEmail + ' <content:getAttribute beanName="warningLastUserReceiveIloansEmail" attribute="text" filter="true"/>';          
          }
//TTP.8 if, as a result of editing the profile one or more of the firms on the user's profile  will have no remaining users with both Receive I:loans Email permission and Staff Plan permission.
          if ( tpaFirmsLastUserStaffPlanAndReceiveIloansEmail.length > 0 ) {
  	        warningMessage += '\nFirm(s): ' + tpaFirmsLastUserStaffPlanAndReceiveIloansEmail + ' <content:getAttribute beanName="warningLastUserStaffPlanAndReceiveIloansEmail" attribute="text" filter="true"/>';          
          }
  //TTP.26 If as a result of saving the updated profile, a firm will no longer have any TPAUMs 
		  if (tpaFirmsLastTPAUM.length >0){
	  	        warningMessage += '\nFirm(s): ' + tpaFirmsLastTPAUM + ' <content:getAttribute beanName="warningLastTPAUM" attribute="text" filter="true"/>';          	  
		  }


  //MPR250	Save - Warning If No Remaining Users for the Firm
		  if (tpaFirmsNoMoreRegisteredUser.length >0){
	  	        warningMessage += '\nFirm(s): ' + tpaFirmsNoMoreRegisteredUser + ' <content:getAttribute beanName="warningTpaNoMoreRegisteredUser" attribute="text" filter="true"/>';          	  
		  }




		  if ( warningMessage.length > 0) {
		    warningMessage += '\n\nAre you sure you would like to continue?';
		  }
        </logicext:then>
   </logicext:if>       

	if (warningMessage.length <= 0) {
		return true;
	} else {
		return confirm(warningMessage);
	}
}

</script>

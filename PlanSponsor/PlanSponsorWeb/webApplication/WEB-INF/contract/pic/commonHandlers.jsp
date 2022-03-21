<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%-- Defines page level event handlers --%>
<script type="text/javascript">
/**
 * Defines an object to hold section meta information.
 */ 
function SectionMetaInfo(dataDivId) {
  this.dataDivId=dataDivId;
} 

/**
 * Collection of section meta information.
 */
var sectionMetaInfoArray = new Array(); 
    sectionMetaInfoArray['general'] 
      = new SectionMetaInfo('generalTabDivId');
    sectionMetaInfoArray['money'] 
      = new SectionMetaInfo('moneyTabDivId');	  
    sectionMetaInfoArray['eligibility'] 
      = new SectionMetaInfo('eligibilityTabDivId');
    sectionMetaInfoArray['contribution'] 
      = new SectionMetaInfo('contributionTabDivId');
    sectionMetaInfoArray['withdrawals'] 
      = new SectionMetaInfo('withdrawalsTabDivId');
    sectionMetaInfoArray['loans'] 
      = new SectionMetaInfo('loansTabDivId');
    sectionMetaInfoArray['vesting'] 
      = new SectionMetaInfo('vestingTabDivId');
    sectionMetaInfoArray['forfeitures'] 
      = new SectionMetaInfo('forfeituresTabDivId');
    sectionMetaInfoArray['otherPlanInformation'] 
      = new SectionMetaInfo('otherPlanInformationTabDivId');

/**
 * Function that handles expansion of a data tab.
 */
function expandDataDiv(section) {

   var sectionInfo = sectionMetaInfoArray[section];
	//alert('expandDataDiv section :'+section+', sectionInfo: '+sectionInfo+', sectionInfo.dataDivId: '+sectionInfo.dataDivId);
   <%-- Expand the data div --%>
   showNodeById(sectionInfo.dataDivId);

}

// Shows the specified element (assumes we are dealing with block elements)
function showNode(node) {
  try {
    //alert('showNode'+ node);
	//alert('showNode'+ node.style.value);
    node.style.display='block';
  }
  catch(E){}
}

// Shows the specified element by id
function showNodeById(id) {
//alert('id:'+id+'document.getElementById(id):'+document.getElementById(id));
  showNode(document.getElementById(id));
}

/**
 * Function that handles expansion of a data tab.
 */
function collapseDataDiv(section) {
 //alert('collapseDataDiv');
  //alert(section);
   var sectionInfo = sectionMetaInfoArray[section];
   
   <%-- Hide the data div --%>
    hideNodeById(sectionInfo.dataDivId);
    
}

// Hides the specified element
function hideNode(node) {
  try {
   //alert('hideNode'+ node);
    node.style.display='none';
  }
  catch(E){}
}

// Hides the specified element by id
function hideNodeById(id) {
 //alert('hideNodeById');
  hideNode(document.getElementById(id));
}

/**
 * Function that handles collapse of all the data sections.
 */
 function collapseAllDataDivs() {
 //alert('collapseAllDataDivs');
   collapseDataDiv('general')
   collapseDataDiv('money')
   collapseDataDiv('eligibility')
   collapseDataDiv('contribution')
   collapseDataDiv('withdrawals')
   collapseDataDiv('loans')
   collapseDataDiv('vesting')
   collapseDataDiv('forfeitures')
   collapseDataDiv('otherPlanInformation')
 }

/**
 * Initialization function that is called when the page first loads.
 */
function initEditPlanData(section) {
  collapseAllDataDivs();
  <c:if test="${selectedTab == 'general'}">
	document.pifDataForm.fromTab.value = 'general';  
    expandDataDiv('general');
  </c:if>
  <c:if test="${selectedTab == 'moneyType'}">
	document.pifDataForm.fromTab.value = 'moneyType';
    expandDataDiv('money');
  </c:if>  
  <c:if test="${selectedTab == 'eligibility'}">
  	document.pifDataForm.fromTab.value = 'eligibility';
    expandDataDiv('eligibility');
  </c:if> 
  <c:if test="${selectedTab == 'contribution'}">
  	document.pifDataForm.fromTab.value = 'contribution';
    expandDataDiv('contribution');
  </c:if> 
  <c:if test="${selectedTab == 'vesting'}">
  	document.pifDataForm.fromTab.value = 'vesting';
    expandDataDiv('vesting');
  </c:if>
  <c:if test="${selectedTab == 'forfeitures'}">
  	document.pifDataForm.fromTab.value = 'forfeitures';
    expandDataDiv('forfeitures');
  </c:if>  
  <c:if test="${selectedTab == 'withdrawals'}">
  	document.pifDataForm.fromTab.value = 'withdrawals';
    expandDataDiv('withdrawals');
  </c:if> 
  <c:if test="${selectedTab == 'loans'}">
  	document.pifDataForm.fromTab.value = 'loans';
    expandDataDiv('loans');
  </c:if>   
  <c:if test="${selectedTab == 'otherPlanInformation'}">
  	document.pifDataForm.fromTab.value = 'otherPlanInformation';
    expandDataDiv('otherPlanInformation');
  </c:if>  

  
}

function showTabData(section) {
	  collapseAllDataDivs();
	  expandDataDiv(section);

}

function getTabData(selectedTab) {

	//document.pifDataForm.fromTab.value = ${pifDataForm.selectedTab};
	document.pifDataForm.toTab.value = selectedTab;
	document.pifDataForm.action="/do/contract/pic/edit/?action=getSelectedTabData&contractId=70300";
	document.pifDataForm.submit();	

}

function doCancelAndExit(){
	alert("CancelAndExit");
	//TODO: Need to update the warning message with the CMA specific content.
    var message = 'Your changes will be lost if you proceed with the cancel & exit';
   	var response = confirm(message);
	if (response == true) {
		document.pifDataForm.action="?action=cancelAndExit&contractId=70300";
		document.pifDataForm.submit();
	}	
}

function doSaveAndExit(){
	alert("SaveAndExit");
	document.pifDataForm.action="?action=saveAndExit&contractId=70300";
	document.pifDataForm.submit();	
}

function doSave(){
	alert("Save");
	document.pifDataForm.action="?action=save&contractId=70300";
	document.pifDataForm.submit();	
}

function doSubmit(){
	alert("Submit");
	//TODO : Need to change the action once PIFConfirmAction class is created.
	//document.pifDataForm.action="?action=submit&contractId=70300";
	//document.pifDataForm.submit();	
}

</script>
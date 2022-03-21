<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%-- Defines page level event handlers --%>
<script type="text/javascript">

/**
 * Function that handles suppression of all the expand/collapse tabs.
 */
 function hideAllExpandCollapseDivs() {
   hideNodeById('generalShowIconId');
   hideNodeById('generalHideIconId');
   hideNodeById('eligibilityAndParticipationShowIconId');
   hideNodeById('eligibilityAndParticipationHideIconId');
   hideNodeById('contributionsShowIconId');
   hideNodeById('contributionsHideIconId');
   hideNodeById('withdrawalsShowIconId');
   hideNodeById('withdrawalsHideIconId');
   hideNodeById('loansShowIconId');
   hideNodeById('loansHideIconId');
   hideNodeById('vestingAndForfeituresShowIconId');
   hideNodeById('vestingAndForfeituresHideIconId');
   hideNodeById('forfeituresShowIconId');
   hideNodeById('forfeituresHideIconId');
   hideNodeById('otherPlanInformationShowIconId');
   hideNodeById('otherPlanInformationShowIconId');
 }

/**
 * Defines an object to hold section meta information.
 */ 
function SectionMetaInfo(sectionDivId, dataDivId, showIconId, hideIconId, sectionHighlightIconId) {
  this.sectionDivId=sectionDivId;
  this.dataDivId=dataDivId;
  this.showIconId=showIconId;
  this.hideIconId=hideIconId;
  this.sectionHighlightIconId=sectionHighlightIconId;
} 

/**
 * Collection of section meta information.
 */
var sectionMetaInfoArray = new Array(); 
    sectionMetaInfoArray['general'] 
      = new SectionMetaInfo('generalTabDivId', 'generalDataDivId', 'generalShowIconId', 'generalHideIconId', 'generalSectionHighlightIconId');
    sectionMetaInfoArray['eligibilityAndParticipation'] 
      = new SectionMetaInfo('eligibilityAndParticipationTabDivId', 'eligibilityAndParticipationDataDivId', 'eligibilityAndParticipationShowIconId', 'eligibilityAndParticipationHideIconId', 'eligibilityAndParticipationSectionHighlightIconId');
    sectionMetaInfoArray['contributions'] 
      = new SectionMetaInfo('contributionsTabDivId', 'contributionsDataDivId', 'contributionsShowIconId', 'contributionsHideIconId', 'contributionsSectionHighlightIconId');
    sectionMetaInfoArray['withdrawals'] 
      = new SectionMetaInfo('withdrawalsTabDivId', 'withdrawalsDataDivId', 'withdrawalsShowIconId', 'withdrawalsHideIconId', 'withdrawalsSectionHighlightIconId');
    sectionMetaInfoArray['loans'] 
      = new SectionMetaInfo('loansTabDivId', 'loansDataDivId', 'loansShowIconId', 'loansHideIconId', 'loansSectionHighlightIconId');
    sectionMetaInfoArray['vestingAndForfeitures'] 
      = new SectionMetaInfo('vestingAndForfeituresTabDivId', 'vestingAndForfeituresDataDivId', 'vestingAndForfeituresShowIconId', 'vestingAndForfeituresHideIconId', 'vestingAndForfeituresSectionHighlightIconId');
    sectionMetaInfoArray['forfeitures'] 
      = new SectionMetaInfo('forfeituresTabDivId', 'forfeituresDataDivId', 'forfeituresShowIconId', 'forfeituresHideIconId', 'forfeituresSectionHighlightIconId');
    sectionMetaInfoArray['otherPlanInformation'] 
      = new SectionMetaInfo('otherPlanInformationTabDivId', 'otherPlanInformationDataDivId', 'otherPlanInformationShowIconId', 'otherPlanInformationHideIconId', 'otherPlanInformationSectionHighlightIconId');
 
/**
 * Function that handles expansion of all the data sections.
 */
 function expandAllDataDivs() {
   expandDataDiv('general')
   expandDataDiv('eligibilityAndParticipation')
   expandDataDiv('contributions')
   expandDataDiv('withdrawals')
   expandDataDiv('loans')
   expandDataDiv('vestingAndForfeitures')
   expandDataDiv('forfeitures')
   expandDataDiv('otherPlanInformation')
 }
 
/**
 * Function that handles collapse of all the data sections.
 */
 function collapseAllDataDivs() {
   collapseDataDiv('general')
   collapseDataDiv('eligibilityAndParticipation')
   collapseDataDiv('contributions')
   collapseDataDiv('withdrawals')
   collapseDataDiv('loans')
   collapseDataDiv('vestingAndForfeitures')
   collapseDataDiv('forfeitures')
   collapseDataDiv('otherPlanInformation')
 }

/**
 * Function that handles expansion of a data tab.
 */
 function expandDataDiv(section) {
 
    var sectionInfo = sectionMetaInfoArray[section];
 
    <%-- Expand the data div --%>
    showNodeById(sectionInfo.dataDivId);
    
    <%-- Show the hide icon --%>
    showNodeById(sectionInfo.hideIconId);
    
    <%-- Hide the show icon --%>
    hideNodeById(sectionInfo.showIconId);
    
    <%-- Hide the section warning icon --%>
    hideNodeById(sectionInfo.sectionHighlightIconId);
    
    <%-- Replace bottom border (removed during collapse to reduce vertical alignment offset) --%>
    document.getElementById(sectionInfo.sectionDivId).style.borderTopWidth = '0px';
 }
 
/**
 * Function that handles expansion of a data tab.
 */
 function collapseDataDiv(section) {
 
    var sectionInfo = sectionMetaInfoArray[section];
    
    <%-- Hide the data div --%>
    hideNodeById(sectionInfo.dataDivId);
    
    <%-- Show the show icon --%>
    showNodeById(sectionInfo.showIconId);
    
    <%-- Hide the hide icon --%>
    hideNodeById(sectionInfo.hideIconId);

    <%-- Show the section warning icon --%>
    showNodeById(sectionInfo.sectionHighlightIconId);
    
    <%-- Remove bottom border to reduce vertical alignment offset --%>
    document.getElementById(sectionInfo.sectionDivId).style.borderTopWidth = '0px';
    
    <%-- Scroll slightly to reset our positioning for tooltips --%>
    window.scrollBy(0,-1);
 }
 
/**
 * Function that handles click of the print report link.
 */
  function doPrint() {
    var urlquery=location.href.split("?");

    var printURL;
    if (urlquery.length > 1) {
        printURL = location.href+"&printFriendly=true";
    } else {
        printURL = location.href+"?printFriendly=true";
    }
    window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
  }
</script>
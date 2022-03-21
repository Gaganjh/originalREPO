<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%-- Defines page level event handlers --%>
<script type="text/javascript">

/**
 * Function that handles suppression of all the expand/collapse tabs.
 */
 function hideAllExpandCollapseDivs() {
   hideNodeById('generalShowIconId');
   hideNodeById('generalHideIconId');
   hideNodeById('contributionShowIconId');
   hideNodeById('contributionHideIconId');
   hideNodeById('safeHarbourShowIconId');
   hideNodeById('safeHarbourHideIconId');
   hideNodeById('autoContributionShowIconId');
   hideNodeById('autoContributionHideIconId');
   hideNodeById('contactinfoShowIconId');
   hideNodeById('contactinfoHideIconId');
   hideNodeById('investmentinfoShowIconId');
   hideNodeById('investmentinfoHideIconId');
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
    sectionMetaInfoArray['contribution'] 
      = new SectionMetaInfo('contributionTabDivId', 'contributionDataDivId', 'contributionShowIconId', 'contributionHideIconId', 'contributionSectionHighlightIconId');
    sectionMetaInfoArray['safeHarbour'] 
      = new SectionMetaInfo('safeHarbourTabDivId', 'safeHarbourDataDivId', 'safeHarbourShowIconId', 'safeHarbourHideIconId', 'safeHarbourSectionHighlightIconId');
    sectionMetaInfoArray['autoContribution'] 
      = new SectionMetaInfo('autoContributionTabDivId', 'autoContributionDataDivId', 'autoContributionShowIconId', 'autoContributionHideIconId', 'autoContributionSectionHighlightIconId');
    sectionMetaInfoArray['contactinfo'] 
      = new SectionMetaInfo('contactinfoTabDivId', 'contactinfoDataDivId', 'contactinfoShowIconId', 'contactinfoHideIconId', 'contactinfoSectionHighlightIconId');
    sectionMetaInfoArray['investmentinfo'] 
      = new SectionMetaInfo('investmentinfoTabDivId', 'investmentinfoDataDivId', 'investmentinfoShowIconId', 'investmentinfoHideIconId', 'investmentinfoSectionHighlightIconId');
   
 
/**
 * Function that handles expansion of all the data sections.
 */
 function expandAllDataDivs() {
   expandDataDiv('general')
   expandDataDiv('contribution')
   expandDataDiv('safeHarbour')
   expandDataDiv('autoContribution')
   expandDataDiv('contactinfo')
   expandDataDiv('investmentinfo')
 }
 
/**
 * Function that handles collapse of all the data sections.
 */
 function collapseAllDataDivs() {
   collapseDataDiv('general')
   collapseDataDiv('contribution')
   collapseDataDiv('safeHarbour')
   collapseDataDiv('autoContribution')
   collapseDataDiv('contactinfo')
   collapseDataDiv('investmentinfo')
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
    document.getElementById(sectionInfo.sectionDivId).style.borderBottomWidth = '1px';
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
    document.getElementById(sectionInfo.sectionDivId).style.borderBottomWidth = '0px';
    
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
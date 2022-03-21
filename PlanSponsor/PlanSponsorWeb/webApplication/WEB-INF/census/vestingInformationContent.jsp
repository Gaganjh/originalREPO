<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<!-- Bean Definition for CMA Content -->

<content:contentBean 
	contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_WITHDRAWAL_WARNING%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="WithdrawalWarning" />

<content:contentBean 
	contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_WITHDRAWAL_REASON%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="WithdrawalReason" />

<content:contentBean 
	contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_EE_MONEYTYPE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="EEMoneyType" />
	
<content:contentBean 
	contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_ASOFDATE_FORMAT_ERROR%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="AsOfDateFormatError" />
	
<content:contentBean 
	contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_MINIMUM_ASOFDATE_ERROR%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="MinimumAsOfDate" />
	
<content:contentBean
	contentId="56126"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="InvalidNumeric" />
	
<content:contentBean
	contentId="58133"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="FullyVestedEffectiveFormat" />
	
<content:contentBean 
	contentId="59658" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="MinimumFullyVestedEffDate"/>
	
<content:contentBean 
	contentId="58132" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="FullyVestedEffDateInTheFuture"/>
	
<content:contentBean 
	contentId="<%=ContentConstants.MESSAGE_MISSING_FULLY_VESTED_EFFECTIVE_DATE%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="FullyVestedEffDateIndDiscrepancy"/>

<content:contentBean
	contentId="56556"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="InvalidPYOSRange" />

<content:contentBean
	contentId="58250"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="PYOSBeBlank" />
	
<content:contentBean
	contentId="60462"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="OnlyPriorVYOSUpdated" />
	
<content:contentBean
	contentId="60463"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="UpdatedVYOSLowerThanPriorVYOS" />
	
<content:contentBean
	contentId="60709"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="PlanLastUpdated" />


<script type="text/javascript" >
<!--

var asOfDateFormatError = "<content:getAttribute id='AsOfDateFormatError' attribute='text'  filter='true' escapeJavaScript='true'><content:param>As Of Date</content:param></content:getAttribute>";
var minimumAsOfDate = "<content:getAttribute id='MinimumAsOfDate' attribute='text'  filter='true' escapeJavaScript='true'/>";
var previousYearsOfServiceFormatError = "<content:getAttribute id='InvalidNumeric' attribute='text'  filter='true' escapeJavaScript='true'><content:param>Vested years of service</content:param></content:getAttribute>";
var fullyVestedEffDateFormatError = "<content:getAttribute id='FullyVestedEffectiveFormat' attribute='text'  filter='true' escapeJavaScript='true'/>";
var minimumFullyVestedEffDate = "<content:getAttribute id='MinimumFullyVestedEffDate' attribute='text'  filter='true' escapeJavaScript='true'/>";
var fullyVestedEffDateInTheFuture = "<content:getAttribute id='FullyVestedEffDateInTheFuture' attribute='text' filter='true' escapeJavaScript='true'/>";
var fullyVestedEffDateIndDiscrepancy = "<content:getAttribute id='FullyVestedEffDateIndDiscrepancy' attribute='text'  filter='true' escapeJavaScript='true'/>";
var invalidPYOSRange = "<content:getAttribute id='InvalidPYOSRange' attribute='text'  filter='true' escapeJavaScript='true'/>";
var pyosBeBlank = "<content:getAttribute id='PYOSBeBlank' attribute='text'  filter='true' escapeJavaScript='true'/>";
var onlyPriorVYOSUpdated = "<content:getAttribute id='OnlyPriorVYOSUpdated' attribute='text' filter='true' escapeJavaScript='true'/>";
var updatedVYOSLowerThanPriorVYOS = "<content:getAttribute id='UpdatedVYOSLowerThanPriorVYOS' attribute='text' filter='true' escapeJavaScript='true'/>";

//-->
</SCRIPT>

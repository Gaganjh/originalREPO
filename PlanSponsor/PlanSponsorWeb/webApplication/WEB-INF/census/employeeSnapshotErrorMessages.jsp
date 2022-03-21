<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<content:contentBean
	contentId="56124"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="NonPrintable" />

<content:contentBean
	contentId="56126"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="InvalidNumeric" />

<content:contentBean
	contentId="56133"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="InvalidSSN" />

<content:contentBean
	contentId="56278"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="BirthDateFormat" />

<content:contentBean
	contentId="56282"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="HireDateFormat" />

<content:contentBean
	contentId="56279"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="EligibilityDateFormat" />

<content:contentBean
	contentId="56281"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="EmploymentStatusEffDateFormat" />

<content:contentBean
	contentId="56273"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="YTDHoursWorkedEffDateFormat" />

<content:contentBean
	contentId="56201"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DesignatedRothDefPerPositive" />

<content:contentBean
	contentId="59654"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DesigRothDefPerNotInRange" />

<content:contentBean
	contentId="56204"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="BeforeTaxDefPerPositive" />

<content:contentBean
	contentId="59653"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="BeforeTaxDefPerNotInRange" />

<content:contentBean
	contentId="56187"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="EmailAddress" />

<content:contentBean
	contentId="56185"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ZipCodeFormat" />

<content:contentBean
	contentId="56572"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="InvalidYTDHoursWorked" />

<content:contentBean
	contentId="56618"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="YTDHoursWorkedOutOfRange" />

<content:contentBean
	contentId="56571"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="AnnualBaseSalaryOutOfRange" />

<content:contentBean
	contentId="56573"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="AnnualBaseSalaryDecimal" />

<content:contentBean
	contentId="57201"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="YTDCompensationOutOfRange" />

<content:contentBean
	contentId="56573"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="YTDCompensationDecimal" />

<content:contentBean
	contentId="56203"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DesignatedRothDefAmtOutOfRange" />

<content:contentBean
	contentId="56203"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DesignatedRothDefAmtPositive" />

<content:contentBean
	contentId="56573"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DesignatedRothDefAmtDecimal" />

<content:contentBean
	contentId="56207"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="BeforeTaxDefOutOfRange" />

<content:contentBean
	contentId="56207"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="BeforeTaxDefPositive" />

<content:contentBean
	contentId="56573"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="BeforeTaxDefDecimal" />

<content:contentBean
	contentId="56574"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="DesignatedRothDefPerDecimal" />

<content:contentBean
	contentId="56574"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="BeforeTaxDefPerDecimal" />

<content:contentBean
	contentId="58133"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="FullyVestedEffectiveFormat" />

<content:contentBean
	contentId="56556"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="InvalidPYOSRange" />

<content:contentBean
	contentId="58250"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="PYOSBeBlank" />

<script type="text/javascript" >
<!--
   var nonPrintableLastName= "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>Last name</content:param></content:getAttribute>";
   var nonPrintableFirstName = "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>First name</content:param></content:getAttribute>";
   var nonPrintableMiddleInit = "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>Middle initial</content:param></content:getAttribute>";   
   var nonPrintableEmployeeId = "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>Employee ID</content:param></content:getAttribute>";
   var nonPrintableDivision = "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>Division</content:param></content:getAttribute>";

   var nonPrintableAddressLine1= "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>Address Line 1</content:param></content:getAttribute>";
   var nonPrintableAddressLine2 = "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>Address Line 2</content:param></content:getAttribute>";
   var nonPrintableCity = "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>City </content:param></content:getAttribute>";
   var nonPrintableState = "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>State</content:param></content:getAttribute>";   
   var nonPrintableZipCode= "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>Zip code</content:param></content:getAttribute>";
   var nonPrintableEmail = "<content:getAttribute id='NonPrintable' attribute='text'  filter='true'><content:param>Employer provided email address</content:param></content:getAttribute>";

   
   var invalidSsnError = "<content:getAttribute id='InvalidSSN' attribute='text'  filter='true'/>";
   var birthDateFormatError = "<content:getAttribute id='BirthDateFormat' attribute='text'  filter='true'/>";
   var hireDateFormatError = "<content:getAttribute id='HireDateFormat' attribute='text'  filter='true'/>";
   var employmentEffDateFormatError = "<content:getAttribute id='EmploymentStatusEffDateFormat' attribute='text'  filter='true'/>";
   var annualSalaryFormatError = "<content:getAttribute id='InvalidNumeric' attribute='text'  filter='true'><content:param>Annual base salary</content:param></content:getAttribute>";
   var annualSalaryDecimalError = "<content:getAttribute id='AnnualBaseSalaryDecimal' attribute='text'  filter='true'></content:getAttribute>";
   var annualSalaryOutOfRangeError = "<content:getAttribute id='AnnualBaseSalaryOutOfRange' attribute='text'  filter='true'></content:getAttribute>";
   
   var ytdCompensationFormatError = "<content:getAttribute id='InvalidNumeric' attribute='text'  filter='true'><content:param>Eligible YTD Compensation</content:param></content:getAttribute>";
   var ytdCompensationDecimalError = "<content:getAttribute id='YTDCompensationDecimal' attribute='text'  filter='true'></content:getAttribute>";
   var ytdCompensationOutOfRangeError = "<content:getAttribute id='YTDCompensationOutOfRange' attribute='text'  filter='true'></content:getAttribute>";
   var ytdCompEffDateFormatError = "<content:getAttribute id='YTDHoursWorkedEffDateFormat' attribute='text'  filter='true'/>";
   var eligibleDateFormatError = "<content:getAttribute id='EligibilityDateFormat' attribute='text'  filter='true'><content:param>Eligibility date</content:param></content:getAttribute>";
   var ytdHoursWorkedFormatError = "<content:getAttribute id='InvalidNumeric' attribute='text'  filter='true'><content:param>Plan YTD hours worked</content:param></content:getAttribute>";
   var previousYearsOfServiceFormatError = "<content:getAttribute id='InvalidNumeric' attribute='text'  filter='true'><content:param>Vested years of service</content:param></content:getAttribute>";
   var designatedRothDefPerFormatError = "<content:getAttribute id='InvalidNumeric' attribute='text'  filter='true'><content:param>Designated roth deferral percentage</content:param></content:getAttribute>";
   var beforeTaxDefPerFormatError = "<content:getAttribute id='InvalidNumeric' attribute='text'  filter='true'><content:param>Before tax deferral percentage</content:param></content:getAttribute>";
   var designatedRothDefPerPositive = "<content:getAttribute id='DesignatedRothDefPerPositive' attribute='text'  filter='true'/>";
   var desigRothDefPerNotInRange = "<content:getAttribute id='DesigRothDefPerNotInRange' attribute='text'  filter='true'/>";
   var beforeTaxDefPerPositive = "<content:getAttribute id='BeforeTaxDefPerPositive' attribute='text'  filter='true'/>";
   var beforeTaxDefPerNotInRange = "<content:getAttribute id='BeforeTaxDefPerNotInRange' attribute='text'  filter='true'/>";

   var designatedRothDefAmtFormatError = "<content:getAttribute id='InvalidNumeric' attribute='text'  filter='true'><content:param>Designated roth flat dollar deferral</content:param></content:getAttribute>";
   var designatedRothDefAmtDecimalError = "<content:getAttribute id='DesignatedRothDefAmtDecimal' attribute='text'  filter='true'></content:getAttribute>";
   var designatedRothDefAmtOutOfRangeError = "<content:getAttribute id='DesignatedRothDefAmtOutOfRange' attribute='text'  filter='true'></content:getAttribute>";
   var designatedRothDefAmtPositive = "<content:getAttribute id='DesignatedRothDefAmtPositive' attribute='text'  filter='true'/>";
   
   var beforeTaxDefFormatError = "<content:getAttribute id='InvalidNumeric' attribute='text'  filter='true'><content:param>Before tax flat dollar deferral</content:param></content:getAttribute>";
   var beforeTaxDefDecimalError = "<content:getAttribute id='BeforeTaxDefDecimal' attribute='text'  filter='true'></content:getAttribute>";
   var beforeTaxDefOutOfRangeError = "<content:getAttribute id='BeforeTaxDefOutOfRange' attribute='text'  filter='true'></content:getAttribute>";
   var beforeTaxDefPositiveError = "<content:getAttribute id='BeforeTaxDefPositive' attribute='text'  filter='true'></content:getAttribute>";
   
   var designatedRothDefPerDecimalError = "<content:getAttribute id='DesignatedRothDefPerDecimal' attribute='text'  filter='true'></content:getAttribute>";
   var beforeTaxDefPerDecimalError = "<content:getAttribute id='BeforeTaxDefPerDecimal' attribute='text'  filter='true'></content:getAttribute>";
   
   var zipCode1FormatError = "<content:getAttribute id='ZipCodeFormat' attribute='text'  filter='true'/>";
   var zipCode2FormatError = "<content:getAttribute id='ZipCodeFormat' attribute='text'  filter='true'/>";
   var emailAddrError = "<content:getAttribute id='EmailAddress' attribute='text'  filter='true'/>";
   var invalidYTDHoursWorkd = "<content:getAttribute id='InvalidYTDHoursWorked' attribute='text'  filter='true'/>";
   var ytdHoursWorkedOutOfRangeError = "<content:getAttribute id='YTDHoursWorkedOutOfRange' attribute='text'  filter='true'/>";
   
   var fullyVestedEffDateFormatError = "<content:getAttribute id='FullyVestedEffectiveFormat' attribute='text'  filter='true'/>";
   var invalidPYOSRange = "<content:getAttribute id='InvalidPYOSRange' attribute='text'  filter='true'/>";
   
   var pyosBeBlank = "<content:getAttribute id='PYOSBeBlank' attribute='text'  filter='true'/>";
//-->
</SCRIPT>
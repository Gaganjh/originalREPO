  PLUS_ICON = "/assets/unmanaged/images/plus_icon.gif";
  MINUS_ICON = "/assets/unmanaged/images/minus_icon.gif";

  //toggle functions
  function toggleSection(sectionDivId) {
    document.getElementById(sectionDivId + 'Icon').src = (document.getElementById(sectionDivId).style.display != "block")? MINUS_ICON : PLUS_ICON;
    expandcontent(sectionDivId);
  }

  function toggleExpandAll() {
    expandAllSections();
    changeAllIcons(MINUS_ICON);
  }

  function toggleContractAll() {
    contractAllSections();
    changeAllIcons(PLUS_ICON);
  }

  function changeAllIcons(iconSrc) {
    for (var i = 0; i < 6; i++) {
      iconId = 'sc' + i + 'Icon';
      document.getElementById(iconId).src = iconSrc;
    }
  }

  function doCalendar(fieldName) {
    cal = new calendar(document.forms['planData'].elements[fieldName]);
  	cal.year_scoll = true;
  	cal.time_comp = false;
  	cal.popup();
  }

  function onChangeSchedule(sel, count, moneyType) {
    scheduleCode = sel.options[sel.selectedIndex].value;
    key = moneyType + "_" + scheduleCode;

    hidLabel = document.forms["planData"]["schedule[" + count + "].label"];  hidLabel.value = sel.options[sel.selectedIndex].text;
    
    txtYear0 = document.getElementById("schedule[" + count + "].year0");  txtYear0.value = vs[key][0];
    txtYear1 = document.getElementById("schedule[" + count + "].year1");  txtYear1.value = vs[key][1];
    txtYear2 = document.getElementById("schedule[" + count + "].year2");  txtYear2.value = vs[key][2];
    txtYear3 = document.getElementById("schedule[" + count + "].year3");  txtYear3.value = vs[key][3];
    txtYear4 = document.getElementById("schedule[" + count + "].year4");  txtYear4.value = vs[key][4];
    txtYear5 = document.getElementById("schedule[" + count + "].year5");  txtYear5.value = vs[key][5];
    txtYear6 = document.getElementById("schedule[" + count + "].year6");  txtYear6.value = vs[key][6];
    txtYear7 = document.getElementById("schedule[" + count + "].year7");  txtYear7.value = vs[key][7];

    hidYear0 = document.getElementById("hid_schedule[" + count + "].year0");  hidYear0.value = vs[key][0];
    hidYear1 = document.getElementById("hid_schedule[" + count + "].year1");  hidYear1.value = vs[key][1];
    hidYear2 = document.getElementById("hid_schedule[" + count + "].year2");  hidYear2.value = vs[key][2];
    hidYear3 = document.getElementById("hid_schedule[" + count + "].year3");  hidYear3.value = vs[key][3];
    hidYear4 = document.getElementById("hid_schedule[" + count + "].year4");  hidYear4.value = vs[key][4];
    hidYear5 = document.getElementById("hid_schedule[" + count + "].year5");  hidYear5.value = vs[key][5];
    hidYear6 = document.getElementById("hid_schedule[" + count + "].year6");  hidYear6.value = vs[key][6];
    hidYear7 = document.getElementById("hid_schedule[" + count + "].year7");  hidYear7.value = vs[key][7];

    if (scheduleCode != 'CUS') {
      txtYear0.disabled = true;  hidYear0.disabled = false;
      txtYear1.disabled = true;  hidYear1.disabled = false;
      txtYear2.disabled = true;  hidYear2.disabled = false;
      txtYear3.disabled = true;  hidYear3.disabled = false;
      txtYear4.disabled = true;  hidYear4.disabled = false;
      txtYear5.disabled = true;  hidYear5.disabled = false;
      txtYear6.disabled = true;  hidYear6.disabled = false;
      txtYear7.disabled = true;  hidYear7.disabled = false;
    } else {
      txtYear0.disabled = false; hidYear0.disabled = true;
      txtYear1.disabled = false; hidYear1.disabled = true;
      txtYear2.disabled = false; hidYear2.disabled = true;
      txtYear3.disabled = false; hidYear3.disabled = true;
      txtYear4.disabled = false; hidYear4.disabled = true;
      txtYear5.disabled = false; hidYear5.disabled = true;
      txtYear6.disabled = false; hidYear6.disabled = true;
      
      txtYear7.value = 100;      hidYear7.value = 100;
      txtYear7.disabled = true;  hidYear7.disabled = false;
    }
  }

  function onClickMoneyOption(optionBox) {
    defaultOptions = document.getElementsByName("planDataDWithdrawalsVO.defaultMoneyOption");

    for (var i = 0; i < defaultOptions.length; i++) {
      if (defaultOptions[i].checked) {
        if (optionBox.value == defaultOptions[i].value && optionBox.checked == false) {
          return false;
        }
      } else {
        if (optionBox.value == defaultOptions[i].value) {
          defaultOptions[i].disabled = !optionBox.checked;
        }
      }
    }
  }

  function onClickDefaultOption(radio) {
    moneyOptions = document.getElementsByName("planDataDWithdrawalsVO.selectedMoneyOptions");

    for (var i = 0; i < moneyOptions.length; i++) {
      if (moneyOptions[i].value == radio.value) {
        moneyOptions[i].checked = true;
      }
    }
  }

  function onClickPrototypeUsed(protoRadio) {
    dropdownProto = document.forms["planData"]["planDataAInfoVO.prototypeId"];
    baseRadios = document.getElementsByName("planDataDWithdrawalsVO.hardshipWithdrawalBase");
    baseHid = document.getElementById("hid_planDataDWithdrawalsVO.hardshipWithdrawalBase");

    if (protoRadio.value == 'N' || protoRadio.value == 'U') {
      dropdownProto.disabled = true;

      baseRadios[0].disabled = false; baseRadios[1].disabled = false; baseRadios[2].disabled = false;
      baseHid.disabled = true;
    } else {
      dropdownProto.disabled = false;

      baseRadios[1].checked = true; baseRadios[0].disabled = true; baseRadios[1].disabled = true; baseRadios[2].disabled = true;
      baseHid.value = "S";          baseHid.disabled = false;
    }
  }

  function onClickMethod(methodRadio) {
    var hoursField = document.getElementById("planDataFVestingVO.hours");
    if (methodRadio.value == 'H') {
      hoursField.disabled = false;
      if (trim(hoursField.value) == '') {
      	hoursField.value = '1000';
      }
    } else {
      hoursField.disabled = true;
    }
  }

  function validatePlanName(field) {
    return validateField(field, new Array(validateNonPrintableAscii), new Array(ERR_INVALID_PLAN_NAME), true)
  }

  function validateEin1(field) {
    return validateField(field, new Array(validate2Digits), new Array(ERR_INVALID_EIN), true)
  }
 
  function validateEin(field) {
	    return validateField(field, new Array(validate9Digits), new Array(ERR_INVALID_EIN_TPA), true)
  }

  function validatePlanNumberTpa(field) {
	    return validateField(field, new Array(validate3DigitsTpa), new Array(ERR_INVALID_PLAN_NUMBER_TPA), true)
  }
  
  function validateEin2(field) {
    return validateField(field, new Array(validate7Digits), new Array(ERR_INVALID_EIN), true)
  }

  function validatePlanNumber(field) {
    return validateField(field, new Array(validate3Digits), new Array(ERR_INVALID_PLAN_NUMBER), true)
  }

  function validatePlanEffectiveDate(field) {
    return validateField(field, new Array(validateDate), new Array(ERR_INVALID_PLAN_EFFECTIVE_DATE), true)
  }

  function validateAciEffectiveDate(field) {
    return validateField(field, new Array(validateDate), new Array(ERR_INVALID_ACI_EFFECTIVE_DATE), true)
  }
  function validateAciAppliesToEffectiveDate(field) {
    return validateField(field, new Array(validateDate), new Array(ERR_INVALID_ACI_APPLIES_EFFECTIVE_DATE), true)
  }
  function validateAutomaticEnrollmentEffectiveDate(field) {
    return validateField(field, new Array(validateDate), new Array(ERR_INVALID_AE_EFFECTIVE_DATE), true)
  } 
  
  function validateOtherEntityType(field) {
    return validateField(field, new Array(validateNonPrintableAscii), new Array(ERR_INVALID_OTHER), true)
  }
  
  function validatePlanEntryDate(field) {
    return validateField(field, new Array(validateMonthDay), new Array(ERR_INVALID_PLAN_ENTRY_DATE), true)
  }
  function validateAciApplyDate(field) {
    return validateField(field, new Array(validateMonthDay), new Array(ERR_INVALID_ACI_ANNUAL_APPLY_DATE), true)
  }

  function validateNormalRetireAge(field) {
    if(validateNumberField(field, 0.1, 99.9, ERR_INVALID_NORMAL_RETIRE_AGE, ERR_DECIMAL_1_normalAge, 1, ERR_AGE_RANGE_normalAge, ERR_AGE_RANGE_normalAge)) {
      return validateField(field, new Array(validateWholeOrHalf), new Array(ERR_INVALID_NORMAL_RETIRE_AGE), true)
    } else {
      return false;
    }
  }

  function validateEarlyRetireAge(field) {
    if (validateNumberField(field, 0.1, 99.9, ERR_INVALID_EARLY_RETIRE_AGE, ERR_DECIMAL_1_earlyAge, 1, ERR_AGE_RANGE_earlyAge, ERR_AGE_RANGE_earlyAge)) {
      return validateField(field, new Array(validateWholeOrHalf), new Array(ERR_INVALID_EARLY_RETIRE_AGE), true)
    } else {
      return false;
    }
  }
  function validateNonElectiveContributionPercent(field) {
	    return validateNumberField(field, 1, 100, ERR_NON_ELECTIVE_CONTRIBUTION_PERCENT_FORMAT, ERR_NON_ELECTIVE_CONTRIBUTION_PERCENT_FORMAT, 2, ERR_NON_ELECTIVE_CONTRIBUTION_PERCENT_FORMAT, ERR_NON_ELECTIVE_CONTRIBUTION_PERCENT_FORMAT);	  
  }
  function validateFirstPercent(field) {
	    return validateNumberField(field, 10, 500, ERR_FIRST_PERCENT_FORMAT, ERR_FIRST_PERCENT_FORMAT, 2, ERR_FIRST_PERCENT_FORMAT, ERR_FIRST_PERCENT_FORMAT);	  
  }
  function validateNextPercent(field) {
	    return validateNumberField(field, 10, 500, ERR_NEXT_PERCENT_FORMAT, ERR_NEXT_PERCENT_FORMAT, 2, ERR_NEXT_PERCENT_FORMAT, ERR_NEXT_PERCENT_FORMAT);	  
  }
  function validateNormalRetireYear(field) {
    return validateNumberField(field, 1, 100, ERR_INVALID_NORMAL_RETIRE_SERVICE_MIN, ERR_DECIMAL_0, 0, ERR_SERVICE_MIN_YEAR_RANGE_normalYear, ERR_SERVICE_MIN_YEAR_RANGE_normalYear);
  }
  function validateDeferralAnnualLimit(field) {
	    return validateNumberField(field, 1, irsMaximum, ERR_INVALID_DEFERRAL_ANNUAL_LIMIT, ERR_INVALID_DEFERRAL_ANNUAL_LIMIT, 0, ERR_INVALID_DEFERRAL_ANNUAL_LIMIT, ERR_INVALID_DEFERRAL_ANNUAL_LIMIT);
  }
  function validateFirstOfFirstPercent(field) {
	    return validateNumberField(field, 1, 25, ERR_FIRST_OF_FIRST_PERCENT, ERR_FIRST_OF_FIRST_PERCENT, 2, ERR_FIRST_OF_FIRST_PERCENT, ERR_FIRST_OF_FIRST_PERCENT);
  }
  function validateFirstOfFirstAmount(field) {
	    return validateNumberField(field, 500, 1000000, ERR_FIRST_OF_FIRST_AMOUNT, ERR_FIRST_OF_FIRST_AMOUNT, 0, ERR_FIRST_OF_FIRST_AMOUNT, ERR_FIRST_OF_FIRST_AMOUNT);
  }
  function validateNextOfPercent(field) {
	    return validateNumberField(field, 1, 25, ERR_NEXT_PERCENT, ERR_NEXT_PERCENT, 2, ERR_NEXT_PERCENT, ERR_NEXT_PERCENT);
  }
  function validateNextOfAmount(field) {
	    return validateNumberField(field, 500, 1000000, ERR_NEXT_AMOUNT, ERR_NEXT_AMOUNT, 0, ERR_NEXT_AMOUNT, ERR_NEXT_AMOUNT);
  }
  function validateMaxMatchPercent(field) {
	    return validateNumberField(field, 1, 100, ERR_MAX_MATCH_PERCENT, ERR_MAX_MATCH_PERCENT, 2, ERR_MAX_MATCH_PERCENT, ERR_MAX_MATCH_PERCENT);
  }
  function validateMaxMatchAmount(field) {
	    return validateNumberField(field, 500, 1000000, ERR_MAX_MATCH_AMOUNT, ERR_MAX_MATCH_AMOUNT, 0, ERR_MAX_MATCH_AMOUNT, ERR_MAX_MATCH_AMOUNT);
  }
  function validateDeferralLimitMax(field) {
	    return validateNumberField(field, 1, 100, ERR_INVALID_DEFERRAL_LIMIT_MAX, ERR_INVALID_DEFERRAL_LIMIT_MAX, 3, ERR_INVALID_DEFERRAL_LIMIT_MAX, ERR_INVALID_DEFERRAL_LIMIT_MAX);
  }
  function validateDeferralLimitMin(field) {
	    return validateNumberField(field, 1, 100, ERR_INVALID_DEFERRAL_LIMIT_MIN, ERR_INVALID_DEFERRAL_LIMIT_MIN, 3, ERR_INVALID_DEFERRAL_LIMIT_MIN, ERR_INVALID_DEFERRAL_LIMIT_MIN);
  }
  function validateDefaultAnnualIncrease(field, ind) {
	  if(ind == 'Y') {
		  return validateNumberField(field, 1, 100, ERR_INVALID_DEFUALT_ANNUAL_INCREASE, ERR_INVALID_DEFUALT_ANNUAL_INCREASE, 0, ERR_INVALID_DEFUALT_ANNUAL_INCREASE, ERR_INVALID_DEFUALT_ANNUAL_INCREASE);
	  } else {
		  return validateNumberField(field, 1, 100, null, null, 0, null, null);
	  }
  }
  function validateDefaultAnnualIncreaseWithDecimal(field, ind) {
	  if(ind == 'Y') {
	    return validateNumberField(field, 1, 100, ERR_INVALID_DEFUALT_ANNUAL_INCREASE_WITH_DECIMAL, ERR_INVALID_DEFUALT_ANNUAL_INCREASE_WITH_DECIMAL, 2, ERR_INVALID_DEFUALT_ANNUAL_INCREASE_WITH_DECIMAL, ERR_INVALID_DEFUALT_ANNUAL_INCREASE_WITH_DECIMAL);
	  } else {
		return validateNumberField(field, 1, 100, null, null, 2, null, null);
	  }
  }
  function validateDefaultMaximumAutoIncrease(field, ind) {
	if(ind == 'Y') {  
	  return validateNumberField(field, 1, 100, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE, 0, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE);
	} else {
		  return validateNumberField(field, 1, 100, null, null, 0, null, null);
	}
  }
  function validateDefaultMaximumAutoIncreaseWithDecimal(field, ind) {
	if(ind == 'Y') {     
	  return validateNumberField(field, 1, 100, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE_WITH_DECIMAL, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE_WITH_DECIMAL, 2, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE_WITH_DECIMAL, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE_WITH_DECIMAL);
	} else {
	  return validateNumberField(field, 1, 100, null, null, 2, null, null);
    }
  }

  function validateEarlyRetireYear(field) {
    return validateNumberField(field, 1, 100, ERR_INVALID_EARLY_RETIRE_SERVICE_MIN, ERR_DECIMAL_0, 0, ERR_SERVICE_MIN_YEAR_RANGE_earlyYear, ERR_SERVICE_MIN_YEAR_RANGE_earlyYear);
  }

  function validateMinWithdrawalAmount(field) {
    return validateCurrencyField(field, 0, 999999999.99, ERR_INVALID_MIN_HARDSHIP, ERR_DECIMAL_2_minWithdrawal, ERR_MIN_HARDSHIP_RANGE, ERR_OUT_OF_RANGE_minHardship);
  }

  function validateMaxWithdrawalAmount(field) {
    return validateCurrencyField(field, 0, 999999999.99, ERR_INVALID_MAX_HARDSHIP, ERR_DECIMAL_2_maxWithdrawal, ERR_MAX_HARDSHIP_RANGE, ERR_OUT_OF_RANGE_maxHardship);
  }

  function validateMinLoanAmount(field) {
    return validateCurrencyField(field, 0.00, 999999999.99, ERR_INVALID_MIN_LOAN_AMOUNT, ERR_DECIMAL_2_minLoan, ERR_MIN_LOAN_AMOUNT_MUST_GT_500, ERR_OUT_OF_RANGE_minLoanAmt);
  }

  function validateMaxLoanAmount(field) {
    return validateCurrencyField(field, 0, 50000.00, ERR_INVALID_MAX_LOAN_AMOUNT, ERR_DECIMAL_2_maxLoan, ERR_MAX_LOAN_AMOUNT_MUST_LT_50000, ERR_MAX_LOAN_AMOUNT_MUST_LT_50000);
  }

  function validateMaxLoanPercent(field) {
	var res = validateField(field, new Array(validatePercentage, validatePerDecimal, validate0to50), new Array(ERR_INVALID_MAX_LOAN_PERCENT, ERR_DECIMAL_3_maxLoanPerc, ERR_MAX_LOAN_PERCENT_MUST_LT_50), true);
	formatPercentage(field);
	return res;
  }

  function validateLoanInterestRate(field) {
	var res = validateField(field, new Array(validatePercentage, validatePerDecimal, validate0to9), new Array(ERR_INVALID_LOAD_INTEREST_RATE, ERR_DECIMAL_3_loanInterestRate, ERR_LOAN_INTEREST_RATE_MUST_BETWEEN_0_2), true);
	formatPercentage(field);
	return res;
  }

  function validateEligibiliyCriteriaMinimumAge(field) {
	if(validateNumberField(field, 5, 21, ERR_INVALID_MINIMUM_AGE, ERR_INVALID_MINIMUM_AGE_WHOLE_OR_HALF, 1, ERR_INVALID_MINIMUM_AGE, ERR_INVALID_MINIMUM_AGE)) {
      return validateField(field, new Array(validateWholeOrHalf), new Array(ERR_INVALID_MINIMUM_AGE_WHOLE_OR_HALF), true);
	} else {
	  return false;
	}
  }
  
  function validateEligibiliyCriteriaHoursOfService(field) {
	return validateNumberField(field, 1, 2000, ERR_INVALID_HOURS_OF_SERVICE, ERR_INVALID_HOURS_OF_SERVICE_WHOLE, 0, ERR_INVALID_HOURS_OF_SERVICE_LESS_THAN_ZERO, ERR_INVALID_HOURS_OF_SERVICE);
  }
  
  function validateEligibiliyCriteriaPeriodOfService(field, basis) {
	if (basis == 'W') {
	  return validateNumberField(field, 1, 104, ERR_INVALID_PERIOD_OF_SERVICE, ERR_INVALID_PERIOD_OF_SERVICE_WHOLE, 0, ERR_INVALID_PERIOD_OF_SERVICE_INVAID_RANGE, ERR_INVALID_PERIOD_OF_SERVICE_INVAID_RANGE);
	} else if (basis == 'D') {
	  return validateNumberField(field, 1, 730, ERR_INVALID_PERIOD_OF_SERVICE, ERR_INVALID_PERIOD_OF_SERVICE_WHOLE, 0, ERR_INVALID_PERIOD_OF_SERVICE_INVAID_RANGE, ERR_INVALID_PERIOD_OF_SERVICE_INVAID_RANGE);
	} else if (basis == 'M') {
      return validateNumberField(field, 1, 24, ERR_INVALID_PERIOD_OF_SERVICE, ERR_INVALID_PERIOD_OF_SERVICE_WHOLE, 0, ERR_INVALID_PERIOD_OF_SERVICE_INVAID_RANGE, ERR_INVALID_PERIOD_OF_SERVICE_INVAID_RANGE);
	}
  }
  
  function validateDefaultDeferralPercentageForAutomaticEnrollment(field, isQaca) {
	if (isQaca) {
      if (! validateNumberField(field, 3, 10, ERR_INVALID_DEFAULT_DEFERRAL_FOR_AE, ERR_INVALID_DEFAULT_DEFERRAL_FOR_AE_WHOLE_OR_HALF, 1, ERR_INVALID_DEFAULT_DEFERRAL_FOR_AE_RANGE, ERR_INVALID_DEFAULT_DEFERRAL_FOR_AE_RANGE)) {
	    return false;
	  }
	} else {
      if (! validateNumberField(field, 1, 100, ERR_INVALID_DEFAULT_DEFERRAL_FOR_AE, ERR_INVALID_DEFAULT_DEFERRAL_FOR_AE_WHOLE_OR_HALF, 1, ERR_INVALID_DEFAULT_DEFERRAL_FOR_AE_RANGE_NO_QACA, ERR_INVALID_DEFAULT_DEFERRAL_FOR_AE_RANGE_NO_QACA)) {
  	    return false;
  	  }
	}
    return validateField(field, new Array(validateWholeOrHalf), new Array(ERR_INVALID_DEFAULT_DEFERRAL_FOR_AE_WHOLE_OR_HALF), true);
  }

  function validatePartialWithdrawalMinimumAmount(field) {
    return validateNumberField(field, 1, 50000, ERR_INVALID_PARTIAL_WITHDRAWAL_MIN_AMOUNT, ERR_INVALID_PARTIAL_WITHDRAWAL_MIN_AMOUNT, 0, ERR_INVALID_PARTIAL_WITHDRAWAL_MIN_AMOUNT, ERR_INVALID_PARTIAL_WITHDRAWAL_MIN_AMOUNT);
  }
  function validateHoursOfService(field) {
    return validateNumberField(field, 1, 1000, ERR_INVALID_HOURS, ERR_DECIMAL_0_vestingHours, 0, ERR_HOURS_MUST_BETWEEN_1_1000, ERR_HOURS_MUST_BETWEEN_1_1000);
  }
  function validateMaximumAmortizationGeneralPurpose(field) {
    return validateNumberField(field, 1, 5, ERR_INVALID_GENERAL_PURPOSE_AMORTIZATION, ERR_DECIMAL_0, 0, ERR_INVALID_GENERAL_PURPOSE_AMORTIZATION, ERR_INVALID_GENERAL_PURPOSE_AMORTIZATION);
  }
  function validateMaximumAmortizationHardship(field) {
    return validateNumberField(field, 1, 5, ERR_INVALID_HARDSHIP_AMORTIZATION, ERR_DECIMAL_0, 0, ERR_INVALID_HARDSHIP_AMORTIZATION, ERR_INVALID_HARDSHIP_AMORTIZATION);
  }
  function validateMaximumAmortizationPrimaryResidence(field) {
    return validateNumberField(field, 1, 30, ERR_INVALID_PRIMARY_RESIDENCE_AMORTIZATION, ERR_DECIMAL_0_primaryResidence, 0, ERR_INVALID_PRIMARY_RESIDENCE_AMORTIZATION, ERR_INVALID_PRIMARY_RESIDENCE_AMORTIZATION);
  }

  function validateMaxNumberofOutstandingLoans(field){
    return validateNumberField(field, 0, 99, ERR_INVALID_MAXIMUM_NUMBER_OF_LOANS, ERR_DECIMAL_0_primaryResidence, 0, ERR_INVALID_MAXIMUM_NUMBER_OF_LOANS, ERR_INVALID_MAXIMUM_NUMBER_OF_LOANS);  
  }



  function validateVestingSchedulePercent(field, moneyTypeId, year) {
	var res = validateField(field,
	        new Array(validateNotEmpty, validatePercentage, validatePerDecimal, validate0to100),
	        new Array(
	          eval("ERR_EMPTY_PERCENTAGE_" + moneyTypeId + "_" + year),
	          eval("ERR_INVALID_PERCENTAGE_" + moneyTypeId + "_" + year),
	          eval("ERR_DECIMAL_3_PERCENTAGE_"+ moneyTypeId + "_" + year),
	          eval("ERR_PERCENTAGE_MUST_BETWEEN_0_100_" + moneyTypeId + "_" + year)), true);
	formatPercentage(field);
	return res;
  }

  function validateFromDate(field) {
    return validateField(field, new Array(validateDate), new Array(ERR_DATE_FORMAT_from), true)
  }

  function validateToDate(field) {
    return validateField(field, new Array(validateDate), new Array(ERR_DATE_FORMAT_to), true)
  }
  
  function validate9Digits(value) {
    if (value.length == 0) {
      return true;
    }

    return isAllDigits(value) && value.length == 9;
  }
  
  function validate3DigitsTpa(value) {
    if (value.length == 0) {
      return true;
    }

    return isAllDigits(value) && value.length == 3;
  }

  function validate2Digits(value) {
    if (value.length == 0) {
      return true;
    }

    return isAllDigits(value) && value.length == 2;
  }

  function validate7Digits(value) {
    if (value.length == 0) {
      return true;
    }

    return isAllDigits(value) && value.length == 7;
  }

  function validate3Digits(value) {
    if (value.length == 0) {
      return true;
    }

    return isAllDigits(value) && value.length == 3;
  }

  function validateNumberField(field, min, max, invalidMsg, decimalMsg, lowDigitNumber, minMsg, maxMsg) {
    if (field.value.length == 0) {
      return true;
    }

	var num = field.value;
	
	/*
	 * Remove trailing zeros first.
	 */
	if(! isNaN(num)) {
		var decimalIndex = num.indexOf(".");
		if (decimalIndex != -1) {
			for (var i = num.length - 1; i > decimalIndex; i--) {
				if (num.charAt(i) != '0') {
					break;
				}
			}
			/*
			 * if we reach the decimal place, we just remove the dot.
			 */
			if (i != decimalIndex) {
				i++;
			}
			
			if (i != num.length) {
				num = num.substring(0, i);
				field.value = num;
			}
		}
	}
	
	if(isNaN(num)) {
        alert(invalidMsg);
        field.value="";
        field.select();
        return false;
	} else if (num.indexOf(".") != -1 && (lowDigitNumber == 0 || num.substring(num.indexOf(".")+1,num.length).length > lowDigitNumber)) {
        alert(decimalMsg);
        field.value="";
        field.select();
        return false;
	} else if (min != null && num < min) {
        alert(minMsg);
        field.value="";
        field.select();
        return false;
	} else if (max != null && num > max) {
        alert(maxMsg);
        field.value="";
        field.select();
        return false;
	} else {
	    return true;
	}
  }

  function validate0to50(value) {
    if (value.length == 0) {
      return true;
    }

    var iValue = parseFloat(value);
    if (isNaN(iValue) || iValue > 50 || iValue < 0) {
      return false;
    } else {
      return true;
    }
  }

  function validate3to10(value) {
    if (value.length == 0) {
      return true;
    }

    var iValue = parseFloat(value);
    if (isNaN(iValue) || iValue > 10 || iValue < 3) {
      return false;
    } else {
      return true;
    }
  }

  function validate0to100(value) {
    if (value.length == 0) {
      return true;
    }

    var iValue = parseFloat(value);
    if (isNaN(iValue) || iValue > 100 || iValue < 0) {
      return false;
    } else {
      return true;
    }
  }
  
  function validateWholeOrHalf(value) {
    if (value.length == 0) {
      return true;
    }

    var iValue = parseFloat(value);
    if (isNaN(iValue) || iValue % 0.5 != 0) {
      return false;
    } else {
      return true;
    }
  }

  function validate0to9(value) {
    if (value.length == 0) {
      return true;
    }

    var iValue = parseFloat(value);
    if (isNaN(iValue) || iValue > 9.999 || iValue < 0) {
      return false;
    } else {
      return true;
    } 
  }
  
  function validateMonthDay(value) {
    if (value.length==0) {
      return true;
    }

    return getDate(value + "/2000") != null;
  }

  function validateNotEmpty(value) {
    return trim(value) != '';
  }

  function trim(s) {
    for(var i = 0; i < s.length; i++) {
      if(s.charAt(i) != ' ') {
        s = s.substr(i);
        break;
      }
    }

    for(var i = s.length - 1; i > -1; i--) {
      if(s.charAt(i) != ' ') {
        return s = s.substr(0, i + 1);
      }
    }

    return '';
  }
  
// New functions below here
function setDirtyFlag() {
  document.getElementById('dirtyFlagId').value = 'true';
}

function isFormDirty() {
	return (document.getElementById('dirtyFlagId').value == 'true');
}

function protectLinks() {
		
		var hrefs  = document.links;
		if (hrefs != null)
		{
			for (i=0; i<hrefs.length; i++) { 
				if(
					hrefs[i].onclick != undefined && 
					(hrefs[i].onclick.toString().indexOf("openWin") != -1 || 
					 hrefs[i].onclick.toString().indexOf("popup") != -1 ||
					 hrefs[i].onclick.toString().indexOf("PDFWindow") != -1 ||
					 hrefs[i].onclick.toString().indexOf("doSignOut") != -1 ||
					 hrefs[i].onclick.toString().indexOf("doPrint") != -1 ||
					 hrefs[i].onclick.toString().indexOf("showPopupGuide") != -1 ||
					 hrefs[i].onclick.toString().indexOf("#") != -1 ||
					 hrefs[i].onclick.toString().indexOf("expand") != -1 ||
					 hrefs[i].onclick.toString().indexOf("collapse") != -1
					 )
				) {
					// Don't replace window open or popups as they won't lose their changes with those
				}
				else if(
					hrefs[i].href != undefined && 
					(hrefs[i].href.indexOf("openWin") != -1 ||
					 hrefs[i].href.indexOf("popup") != -1 ||
					 hrefs[i].href.indexOf("PDFWindow") != -1 ||
					 hrefs[i].href.indexOf("doSignOut") != -1 ||
					 hrefs[i].href.indexOf("doPrint") != -1 ||
					 hrefs[i].href.indexOf("showPopupGuide") != -1 ||
					 hrefs[i].href.indexOf("#") != -1 ||
					 hrefs[i].href.indexOf("expand") != -1 ||
					 hrefs[i].href.indexOf("collapse") != -1
					 )
				) {
					// Don't replace window open or popups as they won't lose their changes with those
				}
				else {
					if (hrefs[i].href.indexOf("task=download") != -1) {
						hrefs[i].onclick = new Function ("return confirmDiscardChanges('Warning! You have unsaved changes that will not be included in the download. Do you want to continue with the download?');");
					} else {
						hrefs[i].onclick = new Function ("return confirmDiscardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');");
					}
				}
			}
		}
 }	
 
/**
 * Determines if a discard confirmation should be displayed.
 */ 
function confirmDiscardChanges(warning) {
  if (document.getElementById('dirtyFlagId').value == 'true') {
  	return window.confirm(warning);
  } else {
	  return true;
	}
}
 

  
  
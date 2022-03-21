function calculateRepaymentAmount(P, numberOfYears, nominalAnnualInterestRate, 
                                  numberOfPeriodsPerYear,
                                  daysPerPeriod) {
  if (nominalAnnualInterestRate <= 0) {
    return 0;
  }
  var i = calculateInterestPerPaymentPeriod(nominalAnnualInterestRate, numberOfPeriodsPerYear);
  var N = Math.floor(numberOfYears * numberOfPeriodsPerYear);
  var repaymentAmount = calculateRepaymentPerPeriod(P, N, i);
  return repaymentAmount;
}

function calculateLoanAmount(repaymentAmount, numberOfYears,
                             nominalAnnualInterestRate,
                             numberOfPeriodsPerYear,
                             daysPerPeriod) {
  if (nominalAnnualInterestRate <= 0) {
    return 0;
  }
  var i = calculateInterestPerPaymentPeriod(nominalAnnualInterestRate, numberOfPeriodsPerYear);
  var N = Math.floor(numberOfYears * numberOfPeriodsPerYear);
  var loanAmount = repaymentAmount * (1 - 1/(Math.pow(1+i, N)))/i;

  // return a round down version of the result.
  loanAmount = Math.floor(loanAmount * 100) / 100;
  return loanAmount;
}

function calculateInterestPerPaymentPeriod(nominalAnnualInterestRate, numberOfPeriodsPerYear) {
  return (nominalAnnualInterestRate / numberOfPeriodsPerYear) / 100.0;
}

function calculateRepaymentPerPeriod(P, N, i) {
  // The formula is x = P * i/(1 - 1/(1 + i)^N) 
  // where:
  // x = repayment amount
  // P = principal (or loan amount)
  // i = interest rate per period
  // N = number of periods
  // ^ stands for "power of"
  // http://en.wikipedia.org/wiki/Amortization_calculator
  
  var x = P * i /(1 - 1/(Math.pow(1+i, N)));
  return x.toFixed(2);
}

function calculateVestedBalanceForMoneyType(loanMoneyType) {
    if (! isNaN(loanMoneyType.vestingPercentage)) {
      var balance = loanMoneyType.accountBalance * loanMoneyType.vestingPercentage / 100.0;
      return balance;
    } else {
      return NaN;
    }
}

function calculateAvailableBalanceForMoneyType(loanMoneyType) {
  if (! loanMoneyType.excludeIndicator) {
    return calculateVestedBalanceForMoneyType(loanMoneyType);
  } else {
    return 0;
  }
}

function calculateVestedAccountBalance(loanParticipantData) {
  var vestedAccountBalance = 0;
  for (var i = 0; i < loanParticipantData.moneyTypes.length; i++) {
      var balance = calculateVestedBalanceForMoneyType(loanParticipantData.moneyTypes[i]);
      if (! isNaN(balance)) {
        vestedAccountBalance += balance;
      } else {
        return NaN;
      }
  }
  return vestedAccountBalance;
}

function calculateVestedBalanceForMoneyTypeWithLoans(loanMoneyType) {
    if (! isNaN(loanMoneyType.vestingPercentage)) {
      var balance = (loanMoneyType.accountBalance + loanMoneyType.loanBalance) * loanMoneyType.vestingPercentage / 100.0;
      return balance;
    } else {
      return NaN;
    }
}

function calculateVestedAccountBalanceWithLoans(loanParticipantData) {
  var vestedAccountBalance = 0;
  for (var i = 0; i < loanParticipantData.moneyTypes.length; i++) {
      var balance = calculateVestedBalanceForMoneyTypeWithLoans(loanParticipantData.moneyTypes[i]);
      if (! isNaN(balance)) {
        vestedAccountBalance += balance;
      } else {
        return NaN;
      }
  }
  for (var i = 0; i < loanParticipantData.moneyTypesWithoutAccountBalance.length; i++) {
      var balance = calculateVestedBalanceForMoneyTypeWithLoans(loanParticipantData.moneyTypesWithoutAccountBalance[i]);
      if (! isNaN(balance)) {
        vestedAccountBalance += balance;
      } else {
        return NaN;
      }
  }
  return vestedAccountBalance;
}

function calculateAvailableAccountBalance(loanParticipantData) {
  var availableAccountBalance = 0;
  for (var i = 0; i < loanParticipantData.moneyTypes.length; i++) {
      var balance = calculateAvailableBalanceForMoneyType(loanParticipantData.moneyTypes[i]);
      if (! isNaN(balance)) {
        availableAccountBalance += balance;
      } else {
        return NaN;
      }
  }
  return availableAccountBalance;
}

function calculateMaximumLoanAvailable(loanPlanData, loanParticipantData) {

  var maxLoanAmount = loanPlanData.maximumLoanAmount;
  var IRSLoanAmount = 10000;
  
  if (isNaN(maxLoanAmount) ||
      isNaN(loanParticipantData.highestOutstandingLoanBalanceWithin12Months) ||
      isNaN(loanParticipantData.currentOutstandingLoanBalance)) {
    return NaN;
  }

  maxLoanAmount -= loanParticipantData.highestOutstandingLoanBalanceWithin12Months;
  maxLoanAmount += loanParticipantData.currentOutstandingLoanBalance;
  
  var vestedAccountBalance = calculateVestedAccountBalanceWithLoans(loanParticipantData);
  
  if (isNaN(vestedAccountBalance)) {
    return NaN;
  }
  
  var maxPercentLoanAmount = vestedAccountBalance *  loanPlanData.maximumLoanPercentage / 100.0;
  
  if (loanParticipantData.applyIrs10KDollarRuleInd) {
    if (maxPercentLoanAmount < IRSLoanAmount) {
      maxPercentLoanAmount = IRSLoanAmount;
    }
  }
  
  var maxResult;
  
  //lesser of maxLoanAmount or maxPercentLoanAmount
  if (maxLoanAmount < maxPercentLoanAmount) {
    maxResult = maxLoanAmount;
  } else {
    maxResult = maxPercentLoanAmount;
  }
  
  maxResult -= loanParticipantData.currentOutstandingLoanBalance;
  
  if (maxResult < loanPlanData.minimumLoanAmount) {
    maxResult = 0;
  }
  if (maxResult > loanPlanData.maximumLoanAmount) {
    maxResult = loanPlanData.maximumLoanAmount;
  }

  // return a round down version of the result.
  maxResult = Math.floor(maxResult * 100) / 100;
  
  return maxResult;  
}


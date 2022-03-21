/**
 * Calculates and updates the maximum loan available balance and
 * all vested balances and available balances along the way.
 *
 * Requires NumberUtils.js
 */
function recalculateBalances(limitMaxLoanAmtToAvailableAccountBalance) {
  
  var totalAccountBalance = 0;
  var totalVestedBalance = 0;
  var totalAvailableBalance = 0;
  var vestedBalanceNaN = false;
  var availableBalanceNaN = false;
  
  for (var i = 0; i < loan.moneyTypes.length; i++) {
    var loanMoneyType = loan.moneyTypes[i];
    var vestedBalance = calculateVestedBalanceForMoneyType(loanMoneyType);
    var availableBalance = calculateAvailableBalanceForMoneyType(loanMoneyType);
    var vestedBalanceSpanId = "#vestedBalance_" + loanMoneyType.moneyTypeId;
    var vestedBalanceSpanId1 = ".vestedBalance1_" + loanMoneyType.moneyTypeId;
    var availableBalanceSpanId = "#availableBalance_" + loanMoneyType.moneyTypeId;
    var availableBalanceSpanId1 = ".availableBalance1_" + loanMoneyType.moneyTypeId;
    
    
    if (isNaN(vestedBalance)) {
      $(vestedBalanceSpanId).text('');
      $(vestedBalanceSpanId1).text('');
      vestedBalanceNaN = true;
    } else {
      $(vestedBalanceSpanId).text(numberUtils.formatAmount(vestedBalance, false, false));
      $(vestedBalanceSpanId1).text(numberUtils.formatAmount(vestedBalance, false, false));
      totalVestedBalance += vestedBalance;
    }
    if (isNaN(availableBalance)) {
      $(availableBalanceSpanId).text('');
      $(availableBalanceSpanId1).text('');
      availableBalanceNaN = true;
    } else {
      $(availableBalanceSpanId).text(numberUtils.formatAmount(availableBalance, false, false));
      $(availableBalanceSpanId1).text(numberUtils.formatAmount(availableBalance, false, false));
      totalAvailableBalance += availableBalance;
    }
    
    totalAccountBalance += loanMoneyType.accountBalance;
  } 
  var maxLoanAvailable = calculateMaximumLoanAvailable(loanPlanData, loan);

  if (limitMaxLoanAmtToAvailableAccountBalance) {
	if (! isNaN(maxLoanAvailable) && ! availableBalanceNaN && maxLoanAvailable > totalAvailableBalance) {
	  maxLoanAvailable = totalAvailableBalance;
	}
  }
  
  if (isNaN(maxLoanAvailable)) {
    $("#maxLoanAvailableSpan").text('');
    $("#calculatorMaxLoanAvailableSpan").text('');
    $("input[name='maximumLoanAvailable']").val('');
  } else {
    formattedAmount = numberUtils.formatAmount(maxLoanAvailable, true, false);
    $("#maxLoanAvailableSpan").text(formattedAmount);
    $("#calculatorMaxLoanAvailableSpan").text(formattedAmount);
    $("input[name='maximumLoanAvailable']").val(numberUtils.deformatNumber(formattedAmount));
  }

  if (vestedBalanceNaN) {
    $("#totalVestedBalance").text('');
    $(".totalVestedBalance").text('');
  } else {
    $("#totalVestedBalance").text(numberUtils.formatAmount(totalVestedBalance, false, false));
    $(".totalVestedBalance").text(numberUtils.formatAmount(totalVestedBalance, false, false));
  }

  if (availableBalanceNaN) {
    $("#totalAvailableBalance").text('');
    $(".totalAvailableBalance").text('');
  } else {
    $("#totalAvailableBalance").text(numberUtils.formatAmount(totalAvailableBalance, false, false));
    $(".totalAvailableBalance").text(numberUtils.formatAmount(totalAvailableBalance, false, false));
  }
  
  $("#totalAccountBalance").text(numberUtils.formatAmount(totalAccountBalance, false, false));
   $(".totalAccountBalance").text(numberUtils.formatAmount(totalAccountBalance, false, false));
}

function setMoneyTypeVestingPercentage(field, moneyTypeIndex, limitMaxLoanAmtToAvailableAccountBalance) {
  var loanMoneyType = loan.moneyTypes[moneyTypeIndex];
  loanMoneyType.vestingPercentage = numberUtils.deformatNumber(field.value);
  recalculateBalances(limitMaxLoanAmtToAvailableAccountBalance);
}

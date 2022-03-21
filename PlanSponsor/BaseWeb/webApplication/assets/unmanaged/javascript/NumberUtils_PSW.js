function NumberUtils() {

  this.parseNumber = function(numString, nanValue) {
     var deformattedNum = this.deformatNumber(numString);
     if (isNaN(deformattedNum) || deformattedNum.trim() == '') {
       if (nanValue != null) {
         return nanValue;
       } else {
         return Number.NaN;
       }
     }       
     return parseFloat(deformattedNum);
  }

  /**
   * Strips all occurances of the group separator ','.
   */
  this.deformatNumber = function(num) {
    // Remove grouping separators
    return num.toString().replace(/[,\$]/g, '');
  }

  this.formatPercentage = function(num, decimal_places, showPercentageSign, truncateZeros) {
    var percentage;
    if (truncateZeros) {
        percentage = parseFloat(this.deformatNumber(num.toString()));
    } else {
        percentage = parseFloat(this.deformatNumber(num.toString())).toFixed(decimal_places);
    }
    if (showPercentageSign) {
      return percentage + "%";
    } else {
      return percentage;
    }
  }

  /**
   * Formats the specified amount to use grouping separators, and optionally a 
   * dollar sign and two different negative formats.
   *
   * @param num The amount to format.
   * @param showDollarSign True if the number should use a dollar sign.
   * @param formatForInput True if the number should use the negative format -<num> rather than (<num>).
   * @param truncateCents True if no decimal digits are desired 
   * @param truncateZeroCents True if no zero decimal digits are desired
   */
  this.formatAmount = function(num, showDollarSign, formatForInput, truncateCents, truncateZeroCents) {

    num = num.toString().replace(/\(|\)|\$|\,/g,'');
    if(isNaN(num)) {
        num = "0";
    }
    // Limit of 15 digits for JavaScript numbers
    if (num.length > 15 && num.toString().indexOf('.') == -1) {
        return num;
    }
    var sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    var cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10) {
        cents = "0" + cents;
    }
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++) {
        num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
    }
    
    return (((sign)?'':((formatForInput)?'-':'(')) 
                 + (showDollarSign?'$':'') 
                 + num 
                 + (truncateCents ? '' : ((truncateZeroCents && parseFloat(cents)==0) ? '' : '.' + cents))
                 + ((sign)?'':((formatForInput)?'':')')));
  }
}

var numberUtils = new NumberUtils();

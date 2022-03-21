function StringUtils() {

  /**
   * Checks if 'value' contains any characters that are not represented by the 
   * specified regular expression 'validCharacterRegEx'.
   *
   * @param value The input string to check for invalid characters.
   * @param validCharacterRegEx A regular expression of the characters that are considered valid. 
   * @param rc Return code: true = invalid characters detected, false = no invalid characters detected.
   */
  this.isInvalidCharactersExist = function (value, validCharacterRegEx) {
    var rc = false;
    var strippedValue = value.replace(validCharacterRegEx, '');
    if (strippedValue.length > 0) {
      rc = true;
    } else {
      rc = false;
    }
    return rc;
  }

}

var stringUtils = new StringUtils();

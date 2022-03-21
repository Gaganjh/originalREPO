/**
 * This class represents a URL object. It can be initialized with a URL string.
 *
 * @author Charles Chan
 */
function URL(url)
{
  /**
   * This method records the fact that a param's value is changed.
   */
  this.setParameter = function(paramName, newValue) {
    this.paramMap[paramName] = newValue;
  }

  this.unsetParameter = function(paramName) {
    this.paramMap[paramName] = null;
  }

  /**
   * This method returns the number of param already set.
   */
  this.paramCount = function() {
    var count = 0;
    for (var paramKey in this.paramMap) {
      count++;
    }
    return count;  
  }

  /**
   * Parses the given URL and initialize the internal parameters.
   */
  this.parseURL = function(url) {
    var qsIndex = url.indexOf('?');
    this.paramMap = new Array();
    
    if (qsIndex != -1) {
      this.requestURL = url.substring(0, qsIndex);
      var queryString = url.substring(qsIndex + 1);
      var nv = queryString.split('&');
      var url = new Object();
      for(i = 0; i < nv.length; i++) {
        var equalIndex = nv[i].indexOf('=');
        var key = nv[i].substring(0,equalIndex);
        var value = unescape(nv[i].substring(equalIndex + 1));
        this.setParameter(key, value);
      }
    } else {
      this.requestURL = url;
    }
  }

  /*
   * This method encodes the given URL by including all the report
   * specific parameters in it.
   */ 
  this.encodeURL = function(url) {
    var params = "";
    var paramCount = this.paramCount();
    var count = 0;
    for (var paramKey in this.paramMap) {
      count++;
      if (this.paramMap[paramKey] != null) {
        params += paramKey + "=" + escape(this.paramMap[paramKey]);
        if (count < paramCount) {
          params += "&";
        }
      }
    }

    if (params.length > 0) {
      params = "?" + params;
    }
    
    if (url != null) {
      returnURL = url + params
    } else {
      if (this.requestURL == null) {
        returnURL = params;
      } else {
        returnURL = this.requestURL + params;
      }
    }
    return returnURL;
  }

  /**
   * An associative array of param values.
   */
  this.paramMap = new Array();
  
  /**
   * A request URL. This URL does not contain any parameter list.
   */
  this.requestURL = "";

  if (url != null) {
    this.parseURL(url);
  }
}

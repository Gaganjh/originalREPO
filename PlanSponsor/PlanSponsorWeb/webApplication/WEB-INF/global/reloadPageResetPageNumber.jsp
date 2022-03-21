<html>
  <head><title>reload the page</title>
  	<jsp:include page="/WEB-INF/global/digitalsurvey.jsp" flush="true" />
  </head>
   
  <script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script> 

  <script language="javascript" type="text/javascript" src="/assets/unmanaged/javascript/URL.js"></script>
  <script type="text/javascript" >
  function resetPageNumber() {
    var newURL = new URL(window.top.location.href);
    newURL.setParameter("pageNumber", "1");
    window.top.location.href = newURL.encodeURL();
  }
  </script>  
  <body onload="resetPageNumber();">
  </body>
</html>

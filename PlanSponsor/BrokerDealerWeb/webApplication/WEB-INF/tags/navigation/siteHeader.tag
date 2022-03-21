<%@tag import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>

<c:choose>
  <c:when test="<%=BDSessionHelper.isInSecuredMode(request) %>">
	<div id="secure_header">
			<navigation:headerRenderer/>
	</div> <!--#secure_header-->
	<div id="site_header">
		<a href="/do/home/" style="cursor: pointer" ><h1>John Hancock: Broker Dealer<img alt="John Hancock logo" src="/assets/unmanaged/images/jh_logo_print.gif" /></h1></a>
	</div> <!--#site_header-->
	
  </c:when>
  <c:otherwise>
 	  <div id="site_header">
		<a href="/do/home/" style="cursor: pointer" ><h1>John Hancock: Broker Dealer<img alt="John Hancock logo" src="/assets/unmanaged/images/jh_logo_print.gif" /></h1>
 	        </a>
       <div id="contactus_link">
          <a href="/do/contactUs/" class="contactus">Contact Us</a>
       </div>
      </div>
  </c:otherwise>
</c:choose>		   

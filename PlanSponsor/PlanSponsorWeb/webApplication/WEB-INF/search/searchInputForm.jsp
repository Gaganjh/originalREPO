
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@ page import="com.manulife.pension.service.searchengine.SearchResultsByCategory" %>
<%@ page import="com.manulife.pension.ps.web.search.SearchForm" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.reference.ContentReference"%>

<jsp:useBean id="psContext" scope="page"  class="com.manulife.pension.ps.web.taglib.search.PSContext"/>

<script type="text/javascript" >
<!--
var ie4 = (document.all != null);
var ns4 = (document.layers != null); // not supported
var ns6 = ((document.getElementById) && (navigator.appName.indexOf('Netscape') != -1));
var isMac = (navigator.appVersion.indexOf("Mac") != -1);

function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
</script>

<ps:form  method="GET" modelAttribute="searchCriteriaForm" name="searchCriteriaForm" action="/do/search/searchInputAction" cssClass="form-horizontal">

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="30" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>
      <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    <td width="700">
    
      <table width="700" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
          <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
        </tr>
        
        <tr>
        
          <td valign="top">
		  <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
		  <img src='<content:pageImage type="pageTitle" id="layoutPageBean"/>'><br>		  <br>
		  	<content:errors scope="session"/>															 
		    <table width="500" border="0" cellpadding="0" cellspacing="0">
		      <tr>
		        <td>
		              <!--Layout/intro1-->
		              <c:if test="${ layoutPageBean.introduction1 != null }">
	                    <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
	                    <br>
					  </c:if>
	  				  <!--Layout/Intro2-->
		              <c:if test="${ layoutPageBean.introduction2 != null }">
					    <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
	         		    <br>
					</c:if>
	            </td>
	          </tr>
              <tr>
                <td valign="top"><!--DWLayoutEmptyCell-->&nbsp;</td>
              </tr>
              
              <tr>
                <td valign="top" >
                	<input type="text" value="" size="40" name="searchCriteriaForm.searchCriteria">
					<!-- Number of results to be displayed for each category on the first result page -->
					<input type="hidden" value="3"  name="numberResultsDisplayInit">
					<!-- Number of results for a specific category to be displayed on the second result page and onward -->
					<input type="hidden" value="10"  name="numberResultsDisplayCategory">
					<!-- Indicates the next page that will be displayed, in this case, the first result page-->
					<input type="hidden" value="initResultPage"  name="nextPage">
					<input type="hidden" value="non_enrolled_tall" name="requestedHeaderType">
					<input type="hidden" value="footer" name="requestedFooterType">
                    <input name="Submit" type="submit" class="button100Lg" value="search"></td>
              </tr>
            </table></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          <td valign="top">
		    <img src="/assets/unmanaged/images/s.gif" width="1" height="85"></td>
        </tr>
      </table>
	  <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br><br>
	  <p><content:pageFooter beanName="layoutPageBean"/></p>
   	  <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 	  <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
	</td>
    <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
  </tr>
</table>
</ps:form >
<script>
	setFocusOnFirstInputField("searchCriteriaForm");
</script>

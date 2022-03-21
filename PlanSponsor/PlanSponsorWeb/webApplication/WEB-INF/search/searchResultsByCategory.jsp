
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
<%@ page import="org.owasp.encoder.Encode"%>

<jsp:useBean id="psContext" scope="page"  class="com.manulife.pension.ps.web.taglib.search.PSContext"/>
<script
    language="javascript">
function doSubmit(){

	document.forms.searchCriteriaForm.submit();
}

</script>
<% 	/* 	Obtain all information necessary to process a "Next" or "Previous" request by the user and send it
		as parameters in the URL.
	*/
	
	String category	= ((SearchForm)request.getAttribute("searchForm")).getSearchResultsForm().getCategory();
	String categoryPrintName = Encode.forHtmlContent(((SearchForm)request.getAttribute("searchForm")).getSearchResultsForm().getCategoryPrintName());

	// *******
	// the following is a hack to replace the word "Testimonial" with "Real-life stories"
	// the real fix is to change the categoryPrintName itself, but at this point (1 day from code freeze)
	// we are not sure what other things such a change would impact.
	// *******
	String displayName = categoryPrintName;
	//if ( "Testimonial".equals(categoryPrintName) ) {
	//	displayName = "Real-life stories";
	//}

	String paramCategory = "category=" + Encode.forHtmlContent(category);
	String paramCategoryPrintName = "categoryPrintName=" + categoryPrintName;
	String currentSearchCriteria = ((SearchForm)request.getAttribute("searchForm")).getSearchCriteriaForm().getSearchCriteria();
	String paramCurrentSearchCriteria = "currentSearchCriteria=" + Encode.forHtmlContent(currentSearchCriteria);
	int numValueResultsDisplayCategory = ((SearchForm)request.getAttribute("searchForm")).getNumberResultsDisplayCategory();
	String numberResultsDisplayCategory = "numberResultsDisplayCategory=" + Integer.toString(numValueResultsDisplayCategory);
	int currentIndex = ((SearchForm)request.getAttribute("searchForm")).getSearchResultsForm().getCurrentIndex();
	String requestedHeaderType = "requestedHeaderType=" + ((SearchForm)request.getAttribute("searchForm")).getRequestedHeaderType();
	String requestedFooterType = "requestedFooterType=" + ((SearchForm)request.getAttribute("searchForm")).getRequestedFooterType();	
	String numberResultsDisplayInit = "numberResultsDisplayInit=" + ((SearchForm)request.getAttribute("searchForm")).getNumberResultsDisplayInit();	
	
	String parameters = paramCategory + "&" + paramCategoryPrintName + "&" + paramCurrentSearchCriteria + "&" + numberResultsDisplayCategory + "&" + 
						requestedHeaderType + "&" + requestedFooterType + "&nextPage=categoryResultPage" + "&" + numberResultsDisplayInit;
	
	/* 
		Link to obtain the previous or next set of results for the category
		Parameters needed are as follow:
		 	- category:						The category of the serach results to be displayed in the next page
		 	- currentSearchCriteria:		The search criteria as entered by the user in the first page
		 	- numberResultsDisplayCategory:	The number of search results displayed per page for a specific category
			- newIndex:						The starting index from which results will be displayed (first result will be index * nbr results per page)
			- nextPage:						The page where the results are to be returned (either initResultPage or categoryResultPage)
			- requestedHeaderType			The type of header to be displayed on this and subsequent results pages
			- requestedFooterType			The type of footer to be displayed on this and subsequent results pages
	*/
	String parametersPrev = parameters + "&newIndex=" + (currentIndex-1);
	String parametersNext = parameters + "&newIndex=" + (currentIndex+1);
	int numberResultsDisplayInitInt = ((SearchForm)request.getAttribute("searchForm")).getNumberResultsDisplayInit();	
	int beginningResultNumber = (currentIndex * numValueResultsDisplayCategory) + 1 + numberResultsDisplayInitInt;
%>


<table width="765" border="0" cellspacing="0" cellpadding="0" height="80%">
  <tr>
  	<!-- vertical spacer, 1st column -->
    <td width="35"><img src="/assets/unmanaged/images/spacer.gif" width="35" height="1" border="0"></td>
	
	<!-- column 1, center -->
    <td width="490" valign="top" class="greyText"><br>
		<img src='<content:pageImage type="pageTitle" id="layoutPageBean"/>'><br><br>
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
		<br>
		<br>
		<ps:form method="GET" modelAttribute="searchCriteriaForm" name="searchCriteriaForm" action="/do/search/searchInputAction">
	       	<input type="hidden" value="<%=Encode.forHtmlAttribute(currentSearchCriteria)%>"  name="searchCriteriaForm.searchCriteria">
			<!-- Number of results to be displayed for each category on the first result page -->
			<input type="hidden" value="3"  name="numberResultsDisplayInit">
			<!-- Number of results for a specific category to be displayed on the second result page and onward -->
			<input type="hidden" value="10"  name="numberResultsDisplayCategory">
			<!-- Indicates the next page that will be displayed, in this case, the first result page-->
			<input type="hidden" value="initResultPage"  name="nextPage">
			<input type="hidden" value="non_enrolled_tall" name="requestedHeaderType">
			<input type="hidden" value="footer" name="requestedFooterType">
            <input type="hidden" name="Submit" value="search">
			<a href="#" onclick="javascript:doSubmit();return true;" >Back to Search </a>
		</ps:form >
		
		
	<!-- search results (search parameter and number of records) header table -->
	<table border="0" cellpadding="0" cellspacing="0" width="510">
	    <tr>
		    <td align="left">
			<span class="big">Your search:</span>&nbsp;&nbsp;<%= Encode.forHtmlContent(currentSearchCriteria) %>
			</td>
		<%
			int regularInterval = beginningResultNumber + (numValueResultsDisplayCategory-1);
			int maxInterval = ((SearchForm)request.getAttribute("searchForm")).getSearchResultsForm().getNbrResults();
			int currentInterval = regularInterval<maxInterval?regularInterval:maxInterval;
		%>
			<td align="center">
		    <span class="big">Results:</span>
			&nbsp;<%= beginningResultNumber %> - <%= currentInterval %>
			&nbsp;<span class="big">of</span>&nbsp;
${searchForm.searchResultsForm.nbrResults}
			</td>
			<td align="right">
			  <ps:searchPaging currentPageNumber="<%=currentIndex%>" 
				                 startAfterResultNumber="<%=numberResultsDisplayInitInt%>" 
				                 parameter="<%=parameters%>" 
				                 numberOfResults="<%=maxInterval%>" 
				                 url="/do/search/searchInputAction" 
				                 groupInterval="5" 
				                 numberOfResultsPerPage="<%=numValueResultsDisplayCategory%>"/>

			</td>
	    </tr>
	</table>

	<img src="/assets/unmanaged/images/horizonal_line.gif" width="510" height="7">
	<!-- end search results header section -->
    </p>
	<p>
	<table cellspacing="0" cellpadding="5" border="0" width="85%">

       	<tr>
			<td>
	        <span class="redText">
			<!-- category title -->
			<%= displayName %>
			</span>
			<br>
			
			<%-- Loop to iterate through search results provided--%>
<c:forEach items="${searchForm.searchResultsForm.searchResults}" var="item" >
			<%-- Print out title, content and URL of each result --%>
			<p>
			<!-- url and title -->

  			  <ps:urlGenerator beanId="item" property="reference" titleProperty="title"><ps:titleGenerator beanId="item"/></ps:urlGenerator>
<c:if test="${not empty item.content}">
<br>${item.content} <%-- filter="false" --%>
</c:if>

			</p>
</c:forEach>
			<%-- end category iterator --%>
	        </td>
        </tr>
		
	</table>
	<br>
	<img src="/assets/unmanaged/images/horizonal_line.gif" width="510" height="7">
	<br>
	<!-- results table -->
	<table border="0" cellpadding="0" cellspacing="0" width="510">
	    <tr>
	    	<td align="left">&nbsp;</td>
			<td align="right">
		    	<ps:searchPaging currentPageNumber="<%=currentIndex%>" startAfterResultNumber="<%=numberResultsDisplayInitInt%>" parameter="<%=parameters%>" numberOfResults="<%=maxInterval%>" url="/do/search/searchInputAction" groupInterval="5" numberOfResultsPerPage="<%=numValueResultsDisplayCategory%>"/>
			</td>
	    </tr>
	</table>	
	<br>
	
 	  <p><content:pageFooter beanName="layoutPageBean"/></p>
   	  <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 	  <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>

	</td>
	<!--// end column 1 -->
	
	<!-- vertical spacer 2nd column -->
    <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
	<!-- column 2 -->
	
    <td width="210" valign="top" ><img src="/assets/unmanaged/images/s.gif" width="8" height="8" border="0"><br></td>
	<!--// column 3 -->
	
  </tr>
</table>
<!-- //end main content table -->	

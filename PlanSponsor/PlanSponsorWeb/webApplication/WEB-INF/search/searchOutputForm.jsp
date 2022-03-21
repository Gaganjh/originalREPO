
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/java-encoder.tld" prefix="e" %>

<%@ page import="com.manulife.pension.service.searchengine.SearchResultsByCategory" %>
<%@ page import="com.manulife.pension.ps.web.search.SearchForm" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.reference.ContentReference"%>


<jsp:useBean id="psContext" scope="page"  class="com.manulife.pension.ps.web.taglib.search.PSContext"/>

<%

String query = ((SearchForm)request.getAttribute("searchForm")).getSearchCriteriaForm().getSearchCriteriaFormatted();

%>

<!-- start main table -->
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

		  <table width="500" border="0" cellpadding="0" cellspacing="0"><!--DWLayoutTable-->
              <tr>
                <td colspan="3" valign="top">
                <ps:form method="POST" modelAttribute="searchCriteriaForm" name="searchCriteriaForm" action="/do/search/searchInputAction" cssClass="form-horizontal">
                	<input type="text" value="" size="40" name="searchCriteriaForm.searchCriteria">
					<!-- Number of results to be displayed for each category on the first result page -->
					<input type="hidden" value="3"  name="numberResultsDisplayInit">
					<!-- Number of results for a specific category to be displayed on the second result page and onward -->
					<input type="hidden" value="10"  name="numberResultsDisplayCategory">
					<!-- Indicates the next page that will be displayed, in this case, the first result page-->
					<input type="hidden" value="initResultPage"  name="nextPage">
					<input type="hidden" value="non_enrolled_tall" name="requestedHeaderType">
					<input type="hidden" value="footer" name="requestedFooterType">
                    <input name="Submit" type="submit" class="button100Lg" value="search">
                </ps:form >
                </td>
              </tr>
              <tr>
                <td height="20" colspan="2" valign="top">&nbsp;</td>
              <td width="1" valign="top">&nbsp;</td>
              </tr>
              <tr>
<td width="276" height="20" valign="top"> <strong>Your Search:</strong> <span class="highlightBold">${e:forHtmlContent(searchForm.searchCriteriaForm.searchCriteria)} </span></td>
<td colspan="2" align="right" valign="top"><strong>Search Results:</strong> <span class="highlightBold">${searchForm.searchResultsForm.nbrResults} </span></td>
              </tr>
              <tr>
                <td height="14" colspan="3" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <!--DWLayoutTable-->
                  <tr>
                    <td  rowspan="2" valign="top"><img src="/assets/unmanaged/images/horizontal_ang.gif" width="6" height="3"></td>
                    <td width="99%" valign="top" class="boxborder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
                  </tr>
                  <tr>
                    <td height="2"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
                  </tr>
                </table></td>
              </tr><tr><td height="0"></td>
              <td width="209"></td>
              <td></td>
              </tr>
              <tr>
                <td height="37" valign="top">
                <strong>
  
<c:if test="${searchForm.searchResultsForm.nbrResults ==0}">
				<content:getAttribute id="layoutPageBean" attribute="body2Header"/>
</c:if>

<c:if test="${searchForm.searchResultsForm.nbrResults !=0}">
				<content:getAttribute id="layoutPageBean" attribute="body1Header"/>
</c:if>

        
                </strong>
                </td>
              </tr>


                <%-- Loop through all categories where results exist --%>
      
<c:forEach items="${searchForm.searchResultsForm.searchResults}" var="currentSearchResultByCategory">

		<%  /* 	Obtain all information necessary to process a "More" request by the user for a specific category, and send it

			as parameters in the URL.

			*/

			String category = "category=" + ((SearchResultsByCategory)pageContext.getAttribute("currentSearchResultByCategory")).getCategory();

			String categoryPrintName = ((SearchResultsByCategory)pageContext.getAttribute("currentSearchResultByCategory")).getCategoryPrintName();

			String displayName = categoryPrintName;

			String currentSearchCriteria = "currentSearchCriteria=" + query;

			String paramCategoryPrintName = "categoryPrintName=" + categoryPrintName;				

			String numberResultsDisplayCategory = "numberResultsDisplayCategory=" + ((SearchForm)request.getAttribute("searchForm")).getNumberResultsDisplayCategory();
			
			String numberResultsDisplayInit = "numberResultsDisplayInit=" + ((SearchForm)request.getAttribute("searchForm")).getNumberResultsDisplayInit();

			String parameters = category + "&" + currentSearchCriteria + "&" + numberResultsDisplayCategory + "&newIndex=0" + "&nextPage=categoryResultPage" + "&" + paramCategoryPrintName + "&" + numberResultsDisplayInit;		

		%>

        <tr><td>

<c:if test="${not empty currentSearchResultByCategory.categoryPrintName}">

        <span class="highlightBold">

		<!-- category title -->

		<%=displayName%>

		</span>

		<br>

</c:if>

		

		<!-- CATEGORY NAME: (<%=category%>) -->

		

		<%-- Loop through the results for each category --%>

<c:forEach items="${currentSearchResultByCategory.resultsForCategory}" var="item">

		<%-- Print out title, content and URL of each result --%>


		<!-- url and title -->


		<p>
		  <ps:urlGenerator beanId="item" property="reference" titleProperty="title"><ps:titleGenerator beanId="item"/></ps:urlGenerator>

<c:if test="${not empty item.excerpt}">

<br>${item.excerpt} <%-- filter="false" --%>

</c:if>
		</p>

</c:forEach>
		
				
		</br>

		<%-- end category iterator --%>



		<%-- Link to obtain more results for that specific category

			 Parameters needed are as follow:

			 	- category:						The category of the serach results to be displayed in the next page

			 	- currentSearchCriteria:		The search criteria as entered by the user in the first page

			 	- numberResultsDisplayCategory:	The number of search results displayed per page for a specific category

				- newIndex:						The starting index from which results will be displayed (first result will be index * nbr results per page)

				- nextPage:						The page where the results are to be returned (either initResultPage or categoryResultPage)

		--%>



<c:if test="${currentSearchResultByCategory.hasMore ==true}">
			
<a href="<%= psContext.makeCURL("/do/search/searchInputAction?","",parameters)%>">More ${currentSearchResultByCategory.categoryPrintName}...</a>
			<br><br><br>
</c:if>

        </td>

        </tr>

</c:forEach>

		<%-- end iteration of all categories with results --%>
	  </table>
	  <script>
		setFocusOnFirstInputField("searchCriteriaForm");
	  </script>
	  <br>
	  <br>
 	  <p><content:pageFooter beanName="layoutPageBean"/></p>
   	  <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 	  <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
	</td>
   </tr>
  </table>
 </td>
 </tr>

</table>


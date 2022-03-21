<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<script type="text/javascript"
	src="/assets/unmanaged/javascript/find_literature_search_results_thumbs_psw.js"></script>
<script type="text/javascript"
	src="/assets/unmanaged/javascript/SpryTabbedPanelsFL.js"></script>
<script type="text/javascript"
	src="/assets/unmanaged/javascript/SpryTabbedPanelsThumbListFL.js"></script>

<link rel="stylesheet" type="text/css" media="all"
	href="/assets/stylesheets/find_literature_search_browse_psw.css" />
<link rel="stylesheet" type="text/css" media="all"
	href="/assets/stylesheets/find_literature_search_elements_psw.css" />
<link rel="stylesheet" type="text/css" media="all"
	href="/assets/stylesheets/SpryTabbedPanelsFL_psw.css" />
	    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/SpryTabbedPanelsThumbListFL_psw.css" />

<%
String Browser="IE";
String userAgent = request.getHeader("user-agent").toUpperCase();

if (userAgent.indexOf("TRIDENT") > -1) {

	Browser="IE";
}
else if (userAgent.indexOf("CHROME") > -1) {

	Browser="Other";
}
else if (userAgent.indexOf("FIREFOX") > -1) {

	Browser="Others";
}
pageContext.setAttribute("Browser",Browser,PageContext.PAGE_SCOPE);
%>
	<div id="searchContentTitle">
		<h1>Search below to find material you can provide to participants that can educate them about key topics.  Enter the name of the topic or keyword you are looking for, then select a piece from the results to view, email or download.  </h1>
			</div>

<a id="searchTop" name="searchTop"></a>
<div id="TabbedPanels1" class="TabbedPanels"style="width:740px; margin-left:20px;">
	<ul class="TabbedPanelsTabGroup">
		<li class="TabbedPanelsTab" tabindex="0"
			onClick="javascript:searchTabClicked();">Search Literature</li>
		<!--<li class="TabbedPanelsTab" tabindex="0" onClick="favesTabClicked();">Your Favorites</li>-->
		<!--<li class="TabbedPanelsTab" tabindex="0" onClick="cartTabClicked();">Your Cart</li>-->
	</ul>
	<div class="TabbedPanelsContentGroup">
		<div id="tabSearch" class="TabbedPanelsContent">
			<form id="form1" name="form1" method="" action="" style="padding-left: 10px; display:flex;">
				Search: <input name="keyword" type="text" id="keyword" size="50" />

				<label style="white-space: no-wrap"> <input type="radio" name="keywordSearchType"
					value="any" checked="checked" /> Any Word
				</label style="white-space: no-wrap"> <label> <input type="radio" name="keywordSearchType"
					value="exact" /> Exact Phrase
				</label> <label style="white-space: no-wrap"> <input name="btnSearch" id="btnSearch" class="button100Lg" type="button" value="search" onClick="return javascript:searchButtonClicked();" style="margin-left: 10px; background-color:#DEE0DF;margin-right:5px;" />
				</label>
			</form>
			
			<c:if test="${Browser eq 'IE'}">
			
			<label style="padding-left:316px;padding-right:15px"> <input id="english" type="radio" name="keywordSearchTypeforLanguage"
					value="english" checked="checked" /> English
				</label> <label> <input id="spanish" type="radio" name="keywordSearchTypeforLanguage"
					value="spanish" /> Spanish
				</label>
			</c:if>
			<c:if test="${Browser eq 'Other'}">
			<label style="padding-left:349px;padding-right:12px"> <input id="english" type="radio" name="keywordSearchTypeforLanguage"
					value="english" checked="checked" /> English
				</label> <label> <input id="spanish" type="radio" name="keywordSearchTypeforLanguage"
					value="spanish" /> Spanish
				</label>
			</c:if>
			<c:if test="${Browser eq 'Others'}">
			<label style="padding-left:341px;padding-right:15px"> <input id="english" type="radio" name="keywordSearchTypeforLanguage"
					value="english" checked="checked" /> English
				</label> <label> <input id="spanish" type="radio" name="keywordSearchTypeforLanguage"
					value="spanish" /> Spanish
				</label>
			</c:if>
		</div>
	</div>

</div>
<div id="resultsHidden">
<div id="results">
		<div id="titleBar">
			<div id="pgNavTop">Show
				<select id="maxResults" onchange="javascript: generalFilterClicked();" name="maxResults">
					<option selected value="15">15</option>
					<option value="30">30</option>
					<option value="60">60</option>
					<option value="0">All</option>
				</select>
				Results</div>
			<label id="numResults" name="numResults"></label>
		</div>
		<div id="subNav">
			<div id="TabbedPanels2" class="TabbedThumbsListPanels">
				<ul class="TabbedThumbsListPanelsTabGroup">
					<li class="TabbedThumbsListPanelsTab " tabindex="0">Thumbnail View</li>
					<li class="TabbedThumbsListPanelsTab" tabindex="0">List View</li>
				</ul>
				<div class="TabbedThumbsListPanelsContentGroup">
					<div class="TabbedThumbsListPanelsContent">
						<div id="thumbs_list">
							<div id="instructions">Click on a thumbnail for more details and to download.<!--To view a description of the thumbnail please hover your mouse over the image. To view additional details and features please click the thumbnail image below. --></div>
							<div id="thumbImgs"></div>
							<br class="clearBoth">
							<div id="pgNavBot"></div>
							<br class="clearBoth">
						</div>
					</div>
					<div class="TabbedThumbsListPanelsContent">
						<div id="thumbs_list">
							<div id="instructions"><!--To view a description of the thumbnail please hover your mouse over the image. To view additional details and features please click the thumbnail image below. --></div>
							<div id="listview"></div>
							<br class="clearBoth">
							<div id="pgNavBot"></div>
						</div>
						<br class="clearBoth">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var TabbedPanels2 = new Spry.Widget.TabbedPanels("TabbedPanels2", {defaultTab:0});
var TabbedPanels1= new Spry.Widget.TabbedPanels("TabbedPanels1", {defaultTab:0});
</script>

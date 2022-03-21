var J = jQuery.noConflict();
J(document).ready
(
	function()
	{
		J('input#btnSearch').click
		(
		 	function($e)
			{
				$e.preventDefault();
				searchButtonClicked();
			}
		)
		J('input').keypress
		(
		 	function(e)
			{
				if (e.which == 13)
				{
					e.preventDefault();
					searchButtonClicked();
				}
			}
		)
	}
)

function updateBrowse(btnVal)
{
	hideAllBrowseSubFilters();
	resetPrimeFilters();
	document.getElementById('keyword').value = "";
	var urlParams = enumerateFilters();
	document.getElementById('primeSubs' + btnVal).style.display = "block";
	document.getElementById('thumbImgs').innerHTML = "";
	document.getElementById('resultsHidden').style.display = "block";
	var numResults = document.getElementById('maxResults').value;
	//J.getJSON("/do/jhNavigator?keyWord="+searchq, function(data){J.each(data.items,function(i,item){J("<img/>").attr({src:"http://www.jhnavigator.com/managed_assets/thumbnails/"+item.thumb,height:100,width:129,border:0,alt:""}).appendTo("#thumbImgs").wrap("<div id='thumbnail'><a href='http://www.jhnavigator.com/pages/catalog/view_item.cfm?itemID=" + item.itemID + "'></a></div>");})});
	J.getJSON("/do/jhNavigator?proxyURL=0&browse="+btnVal+urlParams+"&maxResults="+numResults, function(data){parseResults(data);});
	J.get("/do/jhNavigator?proxyURL=2&browse="+btnVal+urlParams+"&maxResults="+numResults, function(data){parseListResults(data);});

}

function updateBrowseSubPrimes(primeElementID)
{
	var urlParams = enumerateFilters();
	var primeParams = enumeratePrimeFilters();
	clearResults();
	var numResults = document.getElementById('maxResults').value;
	J.getJSON("/do/jhNavigator?proxyURL=0&browse="+primeElementID+urlParams+primeParams+"&maxResults="+numResults, function(data){parseResults(data);});
	J.get("/do/jhNavigator?proxyURL=2&browse="+primeElementID+urlParams+primeParams+"&maxResults="+numResults, function(data){parseListResults(data);});
}

function enumeratePrimeFilters()
{
	var frm = document.forms.form3;
	var formVals = frm['subPrimeFilter'];
	var subPrimes = "0";
	for (i = 0; i < formVals.length; i++ )
	{
		if (formVals[i].checked)
		{
			subPrimes = subPrimes + "," + formVals[i].value;
		}
	}
	var browseParams = "&subPrimes=" + subPrimes;
	return browseParams;
}

function enumerateFilters()
{
	var audienceList = "3000,3001,3003";
	var marketList = "1";

	if (document.getElementById('FR').checked == 1)
	{
		audienceList = "3003";
	}
	if (document.getElementById('PS').checked == 1)
	{
		if (audienceList != "3000,3001,3003")
		{
			audienceList = audienceList + ",3001";
		}
		else
		{
			audienceList = "3001";
		}
	}
	if (document.getElementById('PT').checked == 1)
	{
		if (audienceList != "3000,3001,3003")
		{
			audienceList = audienceList + ",3000";
		}
		else
		{
			audienceList = "3000";
		}
	}

	if (document.getElementById('USA').checked == 1)
	{
		marketList = marketList + ",2";
	}
	if (document.getElementById('NY').checked == 1)
	{
		marketList = marketList + ",3";
	}

	// Get National Firm Account ID from cookie to pass to Web Service as Firm Name
	if (cookieValues.NFA == "0")
	{
		var currentFirm = "ZZ";
	}
	else
	{
		var currentFirm = cookieValues.NFA;
	}
	
	var srchParams = "&audience="+audienceList+"&markets="+marketList+"&firm="+currentFirm;
	return srchParams;
}

function parseResults(jsonResults)
{
	// Thumbnail view
	if (jsonResults.numResults == 1)
	{
		document.getElementById('numResults').innerHTML = " "+jsonResults.numResults+" Result returned";
	}
	else
	{
		document.getElementById('numResults').innerHTML = " "+jsonResults.numResults+" Results returned";
	}
	J.each(jsonResults.items,function(i,item){J("<img/>").attr({src:"https://www.jhnavigator.com/managed_assets/bd_thumbs/"+item.thumb,height:133,width:100,border:0,alt:""}).appendTo("#thumbImgs").wrap("<div id='thumbnail'><a onClick='javascript: openItem(" + item.itemVersionID + ");'></a><br>" + item.name + "</div>");})
}

function parseListResults(htmlResults)
{
	// List view
	document.getElementById('listview').innerHTML = htmlResults;
}

function hideAllBrowseSubFilters()
{
	for (x = 1; x <= 6; x++)
	{
		var y = document.getElementById('primeSubs' + x);
		y.style.display = "none";
	}
}

function resetGeneralFilters()
{
	document.getElementById('FR').checked = 0;
	document.getElementById('PS').checked = 0;
	document.getElementById('PT').checked = 0;
	document.getElementById('NY').checked = 0;
	document.getElementById('USA').checked = 1;
	TabbedPanels2.showPanel(0); // Switch back to thumbnail view if in list view
}

function searchTabClicked()
{
	var objResults = document.getElementById('resultsHidden');
	if (objResults.style.display == "block")
	{
		// Filters and results visible - clear results, hide PRIME filters, show general filters, reset to default
		hideAllBrowseSubFilters();
		clearResults();
		document.getElementById('resultsHidden').style.display = "block";
		resetGeneralFilters();
	}
}

function browseTabClicked()
{
	document.getElementById('keyword').value = "";
	var objResults = document.getElementById('resultsHidden');
	if (objResults.style.display == "block")
	{
		// Filters and results visible - clear results, show general filters, reset to default
		clearResults();
		document.getElementById('resultsHidden').style.display = "block";
		resetGeneralFilters();
	}
}

function generalFilterClicked()
{
	// Enumerate values of all filters and send updated query string to Web Service
	var urlParams = enumerateFilters();
	// Poll PRIME sub-filter DIV's to see if one of them is visible - if so, change type of query
	var primeFlag = 0;
	for (x = 1; x <= 6; x++)
	{
		var y = document.getElementById('primeSubs' + x);
		if (y.style.display == "block")
		{
			primeFlag = x;
			// In Browse mode, refresh all PRIME sub-filters too
			var primeParams = enumeratePrimeFilters();
		}
	}

	clearResults();
	var numResults = document.getElementById('maxResults').value;

	if (primeFlag == 0)
	{
		// Search mode
		var myString = new String();
		myString = document.getElementById('keyword').value;
		var keywordType = "";
		for (i = 0; i < document.form1.keywordSearchType.length; i++)
		{
			if (document.form1.keywordSearchType[i].checked == true)
			{
				keywordType = document.form1.keywordSearchType[i].value;
				break;
			}
		}
		var myRegExp = new RegExp(" ","g");
		var myResult = myString.replace(myRegExp, ",");
		searchq = encodeURI(myResult);
		J.getJSON("/do/jhNavigator?proxyURL=0&keyWord="+searchq+urlParams+"&maxResults="+numResults+"&keywordType="+keywordType, function(data){parseResults(data);});
		J.get("/do/jhNavigator?proxyURL=2&keyWord="+searchq+urlParams+"&maxResults="+numResults+"&keywordType="+keywordType, function(data){parseListResults(data);});
	}
	else
	{
		// Browse mode
		J.getJSON("/do/jhNavigator?proxyURL=0&browse="+primeFlag+urlParams+primeParams+"&maxResults="+numResults, function(data){parseResults(data);});
		J.get("/do/jhNavigator?proxyURL=2&browse="+primeFlag+urlParams+primeParams+"&maxResults="+numResults, function(data){parseListResults(data);});
	}

}

function searchButtonClicked()
{
	hideAllBrowseSubFilters();
	var urlParams = enumerateFilters();
	clearResults();
	var keywordType = "";
	for (i = 0; i < document.form1.keywordSearchType.length; i++)
	{
		if (document.form1.keywordSearchType[i].checked == true)
		{
			keywordType = document.form1.keywordSearchType[i].value;
			break;
		}
	}
	document.getElementById('resultsHidden').style.display = "block";
	var myString = new String();
	myString = document.getElementById('keyword').value;
	var myRegExp = new RegExp(" ","g");
	var myResult = myString.replace(myRegExp, ",");
	searchq = encodeURI(myResult);
	var numResults = document.getElementById('maxResults').value;
	J.getJSON("/do/jhNavigator?proxyURL=0&keyWord="+searchq+urlParams+"&maxResults="+numResults+"&keywordType="+keywordType, function(data){parseResults(data);});
	J.get("/do/jhNavigator?proxyURL=2&keyWord="+searchq+urlParams+"&maxResults="+numResults+"&keywordType="+keywordType, function(data){parseListResults(data);});
}

function clearResults()
{
	document.getElementById('thumbImgs').innerHTML = "";
	document.getElementById('listview').innerHTML = "";
	document.getElementById('numResults').innerHTML = "No Results returned";
}

function openItem(id)
{
	vURL = '/do/jhNavigator?proxyURL=1&itemVersionID='+id;
	window.open(vURL, 'Details', 'left=150,top=150,height=580,width=730,resizable=no,scrollbars=yes,toolbar=no,location=no,directories=no');
}

function resetPrimeFilters()
{
	var frm = document.forms.form3;
	var formVals = frm['subPrimeFilter'];
	for (i = 0; i < formVals.length; i++ )
	{
		formVals[i].checked = false;
	}
}

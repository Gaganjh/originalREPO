 <!--[if IE 6]>
    <STYLE>
	/*
	Added the below entries so that the sort icon gets displayed for center-aligned / right-aligned columns.
	IE has a problem that it does not show the css background-image when the link wraps. 
	*/
	a.sort_descending {
		display:inline-block !important;
	}
	
	a.sort_ascending {
		display:inline-block !important;
	}
    </STYLE>
 <![endif]--> 

<div id="tpaFandp_body">
	<jsp:include page="/WEB-INF/fap/tpaFandpBodyHeader.jsp"></jsp:include>
	<div id="messages"></div><br/>
	<jsp:include page="/WEB-INF/fap/tpaFandpFilter.jsp"></jsp:include>
	<div id="fapTabContainer"><script>applyDefaultFilter(); </script> </div>
	<div id="page_wrapper_footer"></div> 
</div>
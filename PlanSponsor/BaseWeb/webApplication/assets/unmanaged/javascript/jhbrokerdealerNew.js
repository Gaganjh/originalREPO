/*******************************************************************************
*	jhborkerdealer.js
*	Copyright © 2008 John Hancock All Rights Reserved. Designated trademarks and brands are the property of their respective owners.
*
*	JavaScript used throughout the John Hancock Broker Dealer website.
********************************************************************************/


$(document).ready(function(){
	//This turns on the row/column highlight effect for any table with the class of 'report_table_content'
	
	$("#site_search_input").trigger('select');
	$("#site_search_input_lower").trigger('select');
	
	$("table#participants_table").on('mouseout',function(){
		$("a.transaction_history").css({display:"none"});
	});
	
	$("table#participants_table td").on('mouseover',function(){
		$("a.transaction_history").css({display:"none"});
			
	});
	
	$("table#participants_table td.name").on('mouseover',function(){
		$("a.transaction_history").css({display:"none"});
		$("a.transaction_history", this).css({display:"block", float:"right"});
		
	});
	
	//$(".report_table_content").tableHover({colClass: "hover", cellClass: "hovercell", clickClass: "click"});
	
	//Opens the advanced filtering control
	$("#advanced_filter_btn").ready(function(){
		$("#advanced_filter_btn").on('click',		
			function(){
				$("#advanced_filters").css("display","block");
				$("#filter ul").html('<li id="advanced_filter_btn" class="active"><em>Advanced Filter...</em></li>');
				return false;
			}	
		);
	});	
	
	//Code for displaying the "Filters Have Been Applied" message, this will probably need to be removed if the filters are applied on page refresh or added to if applied via AJAX
	$("#filter_report").ready(function(){
		$("#filter_report").on('click',
			function(){
				//Ajax function call would go here
				$("#filters_applied_message").css("display","block");
				return false;
			}
		);
	});	
	
	$("#customize_funds").ready(function(){
		$("#customize_funds").on('click',
			function(){
				//Ajax function call would go here
				$("#filters_applied_message").css("display","block");
				return false;
			}
		);
	});	
	
	//will work for any message close button
	$("#customize_report").ready(function(){
		$("#customize_report").on("click",
			function(){
			//This function was making "Advance Filter" to always appear. Hence, removed it.
			//	$("#report_customization_wrapper").css("display","block");
				return false;
			}
		);
	});	
	
	//Opens the advanced filtering control
	$("#advanced_filter_btn").ready(function(){
		$("#advanced_filter_btn").on("click",		
			function(){
				$("#advanced_filters").css("display","block");
				$("#filter ul").html('<li><a href="#">All</a></li><li><a href="#">Active</a></li><li><a href="#">Outstanding Proposals</a></li><li><a href="#">Pending Contracts</a></li><li><a href="#">Discontinued</a></li><li id="advanced_filter_btn" class="active"><em>Advanced Filter...</em></li>');
				return false;
			}	
		);
	});

});


//Protoyping code for hiding and showing the advanced filter controls
// toggle the expand/collapse of a section
$(document).ready(function() {
	$('#accordion').find('.accordion-toggle').on("click",function() {
		var count = $(".accordion-content:visible").length;
		//Expand or collapse this panel
		$(this).next().toggle();
		
		if(count < 2 ){
			
			//Hide the other panels
			$(".accordion-content").not($(this).next()).slideUp('fast');
			
			//toggle plus and minus icons for the other panels
			$(".accordion-toggle").not($(this)).find('.plus_icon1').show();
			$(".accordion-toggle").not($(this)).find('.minus_icon1').hide();
			
		}

		//toggle plus and minus icons for this panel
		$(this).find('.plus_icon1').toggle();
		$(this).find('.minus_icon1').toggle();


	});

});

function toggleAll(expand) {
	
    if (expand) {
    	$(".accordion-content").show();
    	$(".accordion-toggle").find('.plus_icon1').hide();
		$(".accordion-toggle").find('.minus_icon1').show();	       
    } else {
    	$(".accordion-content").hide();
    	$(".accordion-toggle").find('.plus_icon1').show();
		$(".accordion-toggle").find('.minus_icon1').hide();
		
    }
}

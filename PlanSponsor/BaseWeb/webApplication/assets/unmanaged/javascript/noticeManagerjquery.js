var defaultext = "";
var defaultdropdown1 = "Select";
var defaultradio="N";
var changeHappened = false;
// disable the delete candy button when row has Default values
$(document).ready(function(){
	$("tr.alertrows").each(function(){

		var alertName= $(this).children( 'td:nth-child(3)' ).children('input').val();
		var urgencyCode= $(this).children( 'td:nth-child(12)' ).children('input[id=mode]:checked,#noticePrefForm').val();
		var Date=$(this).children( 'td:nth-child(5)' ).children('TABLE').children('TBODY').children("TR.alertrows1").children('td:nth-child(1)').children('input').val();
		var frequencyCode = $(this).children( 'td:nth-child(7)' ).children('select').children('option:selected').text();
		var alertTiming = $(this).children( 'td:nth-child(9)' ).children('input').val();
		if(defaultext == alertName && defaultext == Date && urgencyCode == defaultradio && defaultdropdown1 == frequencyCode && defaultext == alertTiming ){
			$("#delete").click(function(e){
				e.preventDefault();
			});
			if($("#delete img[title='Delete']").length == 1){
				doSubmitDeletefunctionInd = true;
				$("#delete").css("cursor","default");
			}
		} else
		{
			doSubmitDeletefunctionInd = false;
			$("#delete").css("cursor","pointer");
			$(this).children( 'td:nth-child(1)' ).children('A').show();
		}

	}); 

	$(".values").change(function(){
		changeHappened = true;
		$("tr.alertrows").each(function(){ 

			var alertName= $(this).children( 'td:nth-child(3)' ).children('input').val();
			var urgencyCode= $(this).children( 'td:nth-child(12)' ).children('input[id=mode]:checked,#noticePrefForm').val();
			var Date=$(this).children( 'td:nth-child(5)' ).children('TABLE').children('TBODY').children("TR.alertrows1").children('td:nth-child(1)').children('input').val();
			var frequencyCode = $(this).children( 'td:nth-child(7)' ).children('select').children('option:selected').text();
			var alertTiming = $(this).children( 'td:nth-child(9)' ).children('input').val();

			if(defaultext == alertName && defaultext == Date && urgencyCode == defaultradio && defaultdropdown1 == frequencyCode && defaultext == alertTiming )
			{
				$("#delete").click(function(e){
					e.preventDefault();
				});
				if($("#delete img[title='Delete']").length == 1){
					doSubmitDeletefunctionInd = true;
					$("#delete").css("cursor","default");
				}
			}else
			{
				doSubmitDeletefunctionInd = false;
				$("#delete").css("cursor","pointer");
				$(this).children( 'td:nth-child(1)' ).children('A').show();
			}

		}); 
	});

	$("a").not('[href^="#"]').not(".noLinkAction").not('a:contains("Print report")').click(function(e){
		e.preventDefault();
		if(changeHappened){
			var message = "The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return."; 
			if(confirm(message)){
				var link = $(this).attr('href');
				window.location.href = link;
			}
		}	
		else{
			var link = $(this).attr('href');
			window.location.href = link;
		}
	});

	});
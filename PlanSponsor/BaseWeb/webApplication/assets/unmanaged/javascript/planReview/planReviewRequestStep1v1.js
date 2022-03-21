//This javascript file is used to scrpiting common functionality in ContractReviewReport Step1 Page
//To deactivate the row if contract status code is no AC or CF and if Contract Effective Date is less than 3 months prior to the most recent Report Month End Date

function submit(action, path) {
	document.forms['planReviewReportForm'].action = path + action;
	navigate("planReviewReportForm");
}

$(document)
		.ready(
				function() {
					$('tr.forIterate').each(
							function() {
							
								var contractDisabledMsg = $(this).find(
										'.contractDisabledMsg').val()
								var isContractDisabled = $(this).find(
										'.isContractDisabled').val();
								
								if (isContractDisabled == "true") {
									if(contractDisabledMsg !=""){
										$(this).find('input, select').prop(
												'disabled', true);
									}
									else if(true){
										$(this).find('select:first-child').not("[class$='selectedReportMonthEndDate']").prop(
										'disabled', true);
									}
								}
								

							});
					//To disable select all check box when no contracts are selected.
					if ($('.bob_results').val() == 'bob') {
					if ($(".selectContracts:checked").length == 0) {
						$("#select_all").prop('checked', false);
					} 
					}
					// Enables the Selectall check box based on the conditions
					if ($('.bob_results').val() == 'bob') {
					if ($(".selectContracts:checked").length > 0) {
						$(".blue-btn").prop('disabled', false).removeClass("disabled-grey-btn");
					} else {
						if ($(".selectContracts").prop('checked') == false) {
							if ($('.bob_results').val() == 'contract') {
								if($(".blue-btn").prop('disabled') == true){
									$(".blue-btn").prop('disabled', true).addClass("disabled-grey-btn");
									}else {
								$(".blue-btn").prop('disabled', false).removeClass("disabled-grey-btn");
									}
							} else {

								$(".blue-btn").prop('disabled', true).addClass("disabled-grey-btn");
							}
						}
					}
					}
	
					if ($(".selectContracts:enabled").length > 0
							&& $('.displayContractReviewReportsSize').val() > 1) {
						$("#select_all").prop('disabled', false);

					} else {
						$("#select_all").prop('disabled', true);
						if ($(".selectContracts:checked").length > 0) {
							$(".blue-btn").prop('disabled', false).removeClass("disabled-grey-btn");
						} else {
							if ($(".selectContracts").prop('checked') == false) {
								if ($('.bob_results').val() == 'contract') {
									if($(".blue-btn").prop('disabled') == true){
										$(".blue-btn").prop('disabled', true).addClass("disabled-grey-btn");
										}else {
									$(".blue-btn").prop('disabled', false).removeClass("disabled-grey-btn");
										}
								} else {

									$(".blue-btn").prop('disabled', true).addClass("disabled-grey-btn");
								}
							}
						}
						
						}
					
					$("#select_all")
							.on('click',
									function() {
										var checked_status = this.checked;
										if (checked_status) {
											$(".blue-btn").prop('disabled',
													false).removeClass("disabled-grey-btn");
										} else {
											$(".blue-btn").prop('disabled',
													true).addClass("disabled-grey-btn");
										}

										var index = 0;
										$(".selectContracts")
												.each(
														function() {
															if ($(this).prop(
																	'disabled') != 'disabled'
																	) {
																this.checked = checked_status;
																this.value = true;
																$(
																		"#selectContracts"
																				+ index)
																		.val(
																				this.checked);
																$(
																		"#allContractSelected")
																		.val(
																				this.checked);

															}
															index = index + 1;
														});
									});
					$(".selectContracts").on("click",function() {

						if (this.checked) {
							$(".blue-btn").prop('disabled', false).removeClass("disabled-grey-btn");
						} else {
							if ($(".selectContracts:checked").length == 0) {
								$(".blue-btn").prop('disabled', true).addClass("disabled-grey-btn");
							}
						}
						if (this.checked != $("#select_all").prop('checked')) {
							$("#select_all").prop('checked', false);
							$("#allContractSelected").val(false);
						}
						var index = this.id;
						$("#selectContracts" + index).val(this.checked);

					});

					
					$(".selectedperformanceAndExpenseRatio").on("click",
							function() {
								/*if (this.checked != $("#iReportAll").prop(
										'checked')) {
									$("#iReportAll").prop('checked', false);
									$("#allRatioReportSelected").val(false);
								}*/
								var index = this.id;
								$("#selectedperformanceAndExpense" + index)
										.val(this.checked);
							});
					if($('.selectContracts').length == 1)
			    	  {
			    	  $("#select_all").prop('disabled', true);
			    	  }
				
				});

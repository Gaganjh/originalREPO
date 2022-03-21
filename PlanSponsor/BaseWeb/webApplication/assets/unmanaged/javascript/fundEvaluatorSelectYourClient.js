	/**
	* This function is used to trim the blank spaces.
	*/
	String.prototype.trim = function(){ 
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	/*
	  This function is used to store the tabIndex of the tab clicked, and used to open the tab 
	  when the control comes from next page.
	*/
	function tabSelection(tabIndex){	
		if(tabIndex == 0){
			document.fundEvaluatorForm.clientType.value = 'newplan';
		}
		else if(tabIndex == 2){
			document.fundEvaluatorForm.clientType.value = 'existingclient';
		}
		else{
			document.fundEvaluatorForm.clientType.value = '';
		}	
	}
	
	/**
 	* This method is called when next button is clicked. 	
 	*/	
	
	function doNext() {
		defaultValue = document.fundEvaluatorForm.contractNumber.defaultValue;
		newValue = document.fundEvaluatorForm.contractNumber.value;
		if(document.fundEvaluatorForm.clientType.value == 'existingclient' && defaultValue != newValue){		
			clearPreviousContractSelections();
		}
			document.fundEvaluatorForm.action="?action=" + selectCriteria + "&" + "page=" ;		
			document.fundEvaluatorForm.submit();
	}
	
	/**
 	* This method is called when next button is clicked. 	 
 	* Clears System selected contract information for previously provided contract.
 	*/	
	function clearPreviousContractSelections(){			
		document.fundEvaluatorForm.includedOptItem7.value = '';
		document.fundEvaluatorForm.lifeCyclePortfolios.value = '';
		document.fundEvaluatorForm.lifecycleFundSuites.value == '';			
		document.fundEvaluatorForm.lifeStylePortfolios.value = '';
		document.fundEvaluatorForm.lifestyleFundSuites.value == '';	
		document.fundEvaluatorForm.compulsoryFunds.value='';			
		document.fundEvaluatorForm.companyName.value='';
		
	}
	
	/**
 	* This method is called when new plan tab is selected. 	
 	* Clears existing client information and set to default values.
 	*/	
	function clearExitingClientInformation(tabIndex,tabClicked){		
		
		document.fundEvaluatorForm.contractNumber.value = '';								
		document.fundEvaluatorForm.existingClientClosedFund.checked = false;	
		
		//reset the values in step 3 when the user selects the existing plan information tab 
		//(assume that the user has selected the new plan information first time , then using 
		//the previous button from step 3 to come back to step 1  and selected existing plan information  
		//then the datas populated based on the existing plan information should be cleared and reset the values.)		
		if(tabClicked == 'yes'){	
			// Resetting of includedOptItem7 value should be done only when user switches from existing plan to new
			// plan or vice versa. Otherwise that the value that would have been 'on' if GIFL check box selected in step5 
			// should get retained if the user checks includeGIFLSelectFunds in step1.
			document.fundEvaluatorForm.includedOptItem7.value = '';
			document.fundEvaluatorForm.lifeStylePortfolios.value = '';
			document.fundEvaluatorForm.lifeCyclePortfolios.value = '';
			document.fundEvaluatorForm.compulsoryFunds.value='';			
			document.fundEvaluatorForm.companyName.value='';			
		}
		//document.fundEvaluatorForm.preSelectFunds.value='';	
		document.fundEvaluatorForm.clientType.value = 'newplan';
		enableOrDisableNextButton('newplan');
		
	}

	/**
	 * Show or hide state
	 */
	function showHide(option) {
	    var targetElement = document.getElementById("stateRow");
	    if (option=="show") {
	    	targetElement.style.visibility='visible';
	    	targetElement.style.display='inline';
	   	} else {
	    	targetElement.style.visibility='hidden';
	    	targetElement.style.display='none';
	    	document.getElementById('stateSelected').value = '';
	   	}  
	}
	
	/**
 	* This method is called when existing client tab is selected. 	
 	* Clears new plan informations and set to default values.
 	*/
	
	function clearFundMenuItems(tabIndex,tabClicked){
			document.fundEvaluatorForm.fundMenuSelected.selectedIndex = 0;		
			document.fundEvaluatorForm.fundClassSelected.selectedIndex = 0;		
			document.fundEvaluatorForm.stateSelected.selectedIndex = 0;		
			
			//set "usa" or "ny" funds based on the site location. 
			if(document.fundEvaluatorForm.defaultSiteLocation.value == 'NY'){//if "ny"			
				document.fundEvaluatorForm.fundUsa[0].checked = false;
				document.fundEvaluatorForm.fundUsa[1].checked = true;	
				showHide("hide");
			}
			else {//if "usa"
				document.fundEvaluatorForm.fundUsa[0].checked = true;
				document.fundEvaluatorForm.fundUsa[1].checked = false;	
				showHide("show");
			}
			document.fundEvaluatorForm.newPlanClosedFund.checked = false;
			document.fundEvaluatorForm.includeGIFLSelectFunds.checked = false;

			if(document.fundEvaluatorForm.showFirmFilter.value == "true"){	
				document.fundEvaluatorForm.firmFilterSelected.selectedIndex = 0;		
			}
			
			//reset the step 3 values 
			if(tabClicked == 'yes'){	
				// Resetting of includedOptItem7 value should be done only when user switches from existing plan to new
				// plan or vice versa. Otherwise that the value that would have been 'on' if GIFL check box selected in step5 
				// should get retained if the user chooses the same GIFL v3 contract in step1.
				document.fundEvaluatorForm.includedOptItem7.value = '';
				document.fundEvaluatorForm.lifeStylePortfolios.value = '';
				document.fundEvaluatorForm.lifeCyclePortfolios.value = '';				
			}
			//document.fundEvaluatorForm.preSelectFunds.value='';			
			document.fundEvaluatorForm.clientType.value = 'existingclient';
			enableOrDisableNextButton('existingclient');
			
	}
	/**
	* This method is to identify and open the previously selected tab when the user comes
	* to the screen 2nd time or etc.
	*/
	function retainSelectedValues(){		
			var clientType = document.fundEvaluatorForm.clientType.value;		
			if(clientType == 'newplan'){
				var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1", {defaultTab:0});		
			}else if(clientType == 'existingclient'){
				var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1", {defaultTab:2});
			}else{
				var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1", {defaultTab:1});
			}			
			enableOrDisableNextButton(clientType);
		}	
		
	/**
	*	This method identifies and enable or disable the next button based on the selected
	*	tabs
	*/	
	function enableOrDisableNextButton(clientType){	
		if(clientType == 'newplan'){
			document.getElementById('enabledNextButton').style.display = "";
			document.getElementById('disabledNextButton').style.display = "none";
			document.getElementById('enabledBottomNextButton').style.display = "";
			document.getElementById('disabledBottomNextButton').style.display = "none";		
		}
		else if(clientType == 'existingclient'){		
			if( (document.fundEvaluatorForm.contractNumber.value).trim()!= ''){			
				document.getElementById('disabledNextButton').style.display = "none";
				document.getElementById('enabledNextButton').style.display = "";
				document.getElementById('enabledBottomNextButton').style.display = "";
				document.getElementById('disabledBottomNextButton').style.display = "none";		
			}
			else{			
				document.getElementById('disabledNextButton').style.display = "";
				document.getElementById('enabledNextButton').style.display = "none";
				document.getElementById('enabledBottomNextButton').style.display = "none";
				document.getElementById('disabledBottomNextButton').style.display = "";
			}
		}
		else{
			document.getElementById('disabledNextButton').style.display = "";
			document.getElementById('enabledNextButton').style.display = "none";
			document.getElementById('enabledBottomNextButton').style.display = "none";
			document.getElementById('disabledBottomNextButton').style.display = "";
		}
	}

//The following javascript methods are used to assign the colors dynamically to the specific slider bars.
//This will allocates the color from the pool when the user selects the check  box. 
//If the user deselect the check //box the color will be returned to the pool again for use.

/**
 *  This function is used to assign the color to the slider. If the user selects a specific criteria 
 *  a new color will be assigned from the pool. If the the criteria is deselected then the color will
 *  be returned back to the pool again. 

*/
    function Color() {            
        this.available = true;
    }
    var colors = new Array();       
    colors['#CD5806'] = new Color();
    colors['#6F732D'] = new Color();
    colors['#7C2230'] = new Color();
    colors['#003E51'] = new Color();
    colors['#EBAB00'] = new Color();

    colors['#b69650'] = new Color();
    colors['#3b5a2a'] = new Color();
    colors['#9a7fba'] = new Color();
    colors['#4d2669'] = new Color();
    colors['#bc80a8'] = new Color();
    colors['#9ec5c5'] = new Color();
    colors['#28748c'] = new Color();

    
    function getColorFromPool() {                               
            for (var color in  colors) {
            if (this.colors[color].available == true) {
            this.colors[color].available = false;
            return color;
            }
        }
    }
    
    function returnColorToPool(hexColorString) {
        this.colors[hexColorString].available = true;
    }
    
    var colorPool = new Object();
    colorPool.colors = colors;
    colorPool.getColorFromPool = getColorFromPool;
    colorPool.returnColorToPool = returnColorToPool;    
    var sliderRowsIdNamesStartWith = "slider";
    var colorChooser = "colorId";
    
/**
 *  This function is called when the user clicks on Next button
*/
    function doNext() {
        doValidateNext();       
            document.fundEvaluatorForm.action="?action=" + narrowYourList + "&" + "page=" + "";
            document.fundEvaluatorForm.submit();
        
    }
/**
 *  This function is called when the user clicks on Previous button
*/
    
    function doPrevious() {
    	validateSelectedCriteriaWithZeroPercentage();
        document.fundEvaluatorForm.action="?action=" + selectYourClient + "&" + "page=" + navigateToPrevious;
        document.fundEvaluatorForm.submit();
    }
    
/**
 *  This function is used to check whether the selected criteria value is 0% and 
 *  , deslect the criteria if it is zero .  
*/    
    function validateSelectedCriteriaWithZeroPercentage(){
		var checkBoxgroup = document.fundEvaluatorForm.criteriaSelectedStyle;
			for(var checkboxIndex = 0 ; checkboxIndex < checkBoxgroup.length ; checkboxIndex++) {
				 if(checkBoxgroup[checkboxIndex].checked){
					if(parseInt(document.getElementById("slider-converted-value"+checkboxIndex).value) == 0){
						 checkBoxgroup[checkboxIndex].checked = false;      
					}
					
				 }
				  
			}
	}
    
    
/**
 *  This function is used to check whether the user has selected atleast one criteria
*/  
    function criteriaSelected(){
        var checkgroup = document.fundEvaluatorForm.criteriaSelectedStyle;
        var count = 0;
        
        for(var i=0; i<checkgroup.length; i++){         
            if(checkgroup[i].checked == true){
                count = count+1;
                break;
            }
        }           
        if(count > 0) {return true;}
        else {return false;}        
    }
/**
 *  This function is called when the user selected next button and used to validate before 
 *  navigating to next screen.
*/      
    function doValidateNext(){
        //check if any one of the criteria is selected.
        if(!criteriaSelected()){                
            document.fundEvaluatorForm.criteriaSelected.value = false;         
            
        }else{//check if the remaining percentage is zero
            document.fundEvaluatorForm.criteriaSelected.value = true;
            var remainingPercentage = document.getElementById('total_percentage').innerHTML;            
            document.fundEvaluatorForm.remainingPercentage.value = remainingPercentage;
            if(remainingPercentage == '0%'){                
            }
            else{
                document.fundEvaluatorForm.remainingPercentage.value = "nonzero";      
                                
            }   
        }
        validateSelectedCriteriaWithZeroPercentage();
        //Populate the piechart segment colors/legend box colors for the selected criteiras into form bean.
        var checkBoxgroup = document.fundEvaluatorForm.criteriaSelectedStyle;        
        var pieChartColors="";
        for(var checkboxIndex = 0 ; checkboxIndex < checkBoxgroup.length ; checkboxIndex++) {     
                if(checkBoxgroup[checkboxIndex].checked){   
                    pieChartColors = pieChartColors+document.getElementById("colorId"+checkboxIndex).tag+",";
                    }
                    else{
                        pieChartColors = pieChartColors+" ,";//set blank color if criteria not selected
                    }
                }
        document.fundEvaluatorForm.pieChartcolors.value = pieChartColors;
    }


/**
 *  This function is used to set the focus to the percentage text box to move the slider
*/  
    
    function reposition_Slider_thumb(checkboxIndex){
        
        if (document.getElementById('slider'+checkboxIndex).style.display != 'none') {
            document.getElementById("slider-converted-value"+checkboxIndex).focus();
            }
        }
        
    
/**
 *  This function is called when the check box is clicked.
*/  
    function chkCriteriaSelectionSize(criteriaName, criteriaDescription, checkboxIndex) { 
    
    
        var checkgroup = document.fundEvaluatorForm.criteriaSelectedStyle;
        var criteriaBindedToSlider = document.fundEvaluatorForm.criteriaSelectedSlider;
        var valueSlider = "slider-converted-value";
        var measuredById = 'measuredByIndex'+checkboxIndex;
        var selectedmeasuredByName = document.getElementById(measuredById).innerHTML;
    
        for (var i=0; i<checkgroup.length; i++){            
        //to set the value to zero when the user deselect the check box.
        //assume that the user move the slider and again he wants to select some other criteria instead of the current one
        //then the selected vlaue has to be cleared and set to zero. so that the pie chart will be refreshed with proper values.
        //document.getElementById("slider-converted-value"+checkboxIndex).value = 0;        
    
            var checkedcount=0;
            for (var i=0; i<checkgroup.length; i++)
            checkedcount+=(checkgroup[i].checked)? 1 : 0      
            		
            		
    		if(checkedcount > 6 ) {
             	$('#measurementCriteria').css('height', 90 * checkedcount + 'px');
           	    $('#slidersPie').css('height', 90 * checkedcount + 'px');
           	    $('.selection_mode_slider').css('height', 90 * checkedcount + 'px');
            } 	else {
            	$('#measurementCriteria').css('height', '540px');
           	    $('#slidersPie').css('height', '540px');
           	    $('.selection_mode_slider').css('height', '540px');
            }	
                  
            if(checkgroup[checkboxIndex].checked == true) {                 
                for(var slider = 1; slider <= checkgroup.length ; slider++){
                //choose the color if the check box is selected.
                    var sliderRow1ElementId = sliderRowsIdNamesStartWith + checkboxIndex;            
                    var sliderColor = colorChooser+checkboxIndex;
                    if (document.getElementById(sliderRow1ElementId).style.display != "") {
                        document.getElementById(sliderRow1ElementId).style.display = "";
                        document.getElementById("slider-converted-value"+checkboxIndex).value=0;
                        var nextAvailableColor = getColorFromPool();
                        document.getElementById(sliderColor).style.backgroundColor = nextAvailableColor;
                        document.getElementById(sliderColor).tag = nextAvailableColor;
                        return ;
                        }
                } 
            }else{              
                var sliderRow1ElementId = sliderRowsIdNamesStartWith + checkboxIndex;
                var sliderColor = colorChooser+checkboxIndex;
                //return the color to pool if the check box is deselected.  
                document.getElementById("slider-converted-value"+checkboxIndex).value=0;
                returnColorToPool(document.getElementById(sliderColor).tag);
                recalculateTotal();
                document.getElementById(sliderRow1ElementId).style.display = "none";                                                                                            
                
                }
            }           
        }
    
    
    
/**
 *  This function is used to set the legend box color when the user comes to the screen second time.
*/  
    function loadUpdatedValues(sliderColor){
        //set the pie chart legend box color in edit mode
        nextAvailableColor = getColorFromPool();
        document.getElementById(sliderColor).style.backgroundColor = nextAvailableColor;   
        document.getElementById(sliderColor).tag = nextAvailableColor;   
    }
    
    var total=0;
    var unAllocatedWedgeExists=true;
    
    function goNext() {
        if (total == 100) {
        top.location.href='ievaluator_step3.html';
        } else {
        alert("Your total sums to: "+total+"%.\nThe total must be 100% before you can continue");
        }
    }
    
    //This function is used to calculate the total percentage.
    function recalculateTotal() {               
        total = 100;
        var indexArray = 0;
        var selected_sliders_array = new Array(0,0,0,0,0,0,0,0,0,0,0,0);
        var selected_piechart_legendbox_color = new Array(0,0,0,0,0,0,0,0,0,0,0,0);
        var selectedSliderCount = 0;

        var sumSliders = 0;
        for(var i=0; i<selected_sliders_array.length; i++) {            
            sliderValue = parseInt(Math.round(document.getElementById("slider-converted-value"+i).value));         
            sumSliders += sliderValue; 
                        
            if(sliderValue !=0 ) {
                            selected_sliders_array[indexArray] = sliderValue;
                            
                            selected_piechart_legendbox_color[indexArray] = document.getElementById("colorId"+i).tag;         
                            indexArray++;
            }
            
        }
        
        var total = 100-sumSliders;
   
        if (total < 0) {
            document.getElementById('negative_percentage').innerHTML='Remaining must be 0% to continue';
			document.getElementById('total_percentage').className = 'remainingPercentageValue';
            strTotal = total +"%";          
        } else {
            document.getElementById('negative_percentage').innerHTML='';
			document.getElementById('total_percentage').className = '';
            strTotal = total + "%";
        }
        
        document.getElementById('total_percentage').innerHTML=strTotal;    
		// function drawPie should be defined in jsp page WHERE Chart.min.js get imported
		drawPie(selected_sliders_array, selected_piechart_legendbox_color);
    }
        
    (function() {       
            for(var sliderNumber = 0;sliderNumber < 12 ; sliderNumber++) {
        (function() {
            var Event = YAHOO.util.Event,
            Dom   = YAHOO.util.Dom,
            lang  = YAHOO.lang,
            slider,
            bg="slider-bg" + sliderNumber,
            thumb="slider-thumb" + sliderNumber,
            valuearea1="slider-value" + sliderNumber,
            textfield="slider-converted-value" + sliderNumber,
            sliderButtonId = "sliderbtn" + sliderNumber
    
            // The slider can move 0 pixels left
            var leftConstraint = 0;
            // The slider can move 200 pixels right
            var rightConstraint = 200;
            // Custom scale factor for converting the pixel offset into a real value
            var scaleFactor = .5;
            // The amount the slider moves when the value is changed with the arrow
            // keys 
            var keyIncrement = 10; 
            var tickSize = 2;
            
    
            Event.onDOMReady(function() {                         
                    slider = YAHOO.widget.Slider.getHorizSlider(bg, thumb, leftConstraint, rightConstraint, tickSize);                  
                    slider.animate=false;
                    slider.keyIncrement=10;
                    slider.getRealValue = function() {                                          
                        return Math.round(this.getValue() * scaleFactor);
                    }

                    slider.subscribe("change", function(offsetFromStart) {
                        var fld = Dom.get(textfield);
                        // Display the pixel value of the control use the scale factor to convert the pixel offset into a 
                        // real value                           
                        var actualValue = slider.getRealValue();
                        
                        document.getElementById(sliderButtonId).innerHTML = actualValue +"%";
                        // update the text box with the actual value                                                                
                        fld.value = actualValue;
                        // Update the title attribute on the background.  This // // helps assistive                                
                        // technology to communicate the state change                                                   
                        Dom.get(bg).title = "slider value = " + actualValue;
                        recalculateTotal();                                                                                         
                        });
    
                    slider.subscribe("slideStart", function() {                        
                        YAHOO.log("slideStart fired", "warn");
                    });
    
                    slider.subscribe("slideEnd", function() {
                        YAHOO.log("slideEnd fired", "warn");
                        recalculateTotal();
                    });
    
        
                // Listen for keystrokes on the form field that displays the
                // control's value.  While not provided by default, having a
                // form field with the slider is a good way to help keep your
                // application accessible.
                
                Event.on(textfield, "focus", function(e) {        
                		if(document.getElementById(textfield).value ==''){
							document.getElementById(textfield).value = 0;
						}                  
                        var v = parseFloat(Math.round(this.value));                                                         
                        slider.setValue(Math.round(v / scaleFactor));                        					

                });
				Event.on(textfield, "blur", function(e) {                     		
                        //In case of decimal values given in the text box will not trigger the 
                        //slider movement when both slider and text box are having the same values
                        //eg(2.3 in text box and slider value is already 2). 
                        //In this type of scenarios the text box will be updated with the slider value 
                        //explicitly. 
                        //setting the text box value with slider value after slider movement						
						//alert("before");						
                        document.getElementById(textfield).value = slider.getRealValue();						

                });
                Event.on(textfield, "change", function(e) {   
                		if(document.getElementById(textfield).value ==''){
							document.getElementById(textfield).value = 0;
						}                        
                        var v = parseFloat(Math.round(this.value));                                                         
                        slider.setValue(Math.round(v / scaleFactor));
                        //In case of decimal values given in the text box will not trigger the 
                        //slider movement when both slider and text box are having the same values
                        //eg(2.3 in text box and slider value is already 2). 
                        //In this type of scenarios the text box will be updated with the slider value 
                        //explicitly. 
                        //setting the text box value with slider value after slider movement
                        document.getElementById(textfield).value = slider.getRealValue();

                });
                Event.on(textfield, "keydown", function(e) {                            
                // set the value when the 'return' key is detected  
                if ((Event.getCharCode(e) === 13) || (Event.getCharCode(e) === 9)) { 
                				if(document.getElementById(textfield).value ==''){
									document.getElementById(textfield).value = 0;
								}                                    
                                var percentageTextBoxValue = parseFloat(Math.round(document.getElementById(textfield).value));

                                if(!lang.isNumber(percentageTextBoxValue)) {                                                         
                                    alert("You must enter a number between 1 and 100");                                                        
                                    percentageTextBoxValue =  0;
                                    document.getElementById(textfield).value = 0;
                                } else if(percentageTextBoxValue > 100) {
                                    percentageTextBoxValue = 100;
                                    document.getElementById(textfield).value = 100;
                                } else if(percentageTextBoxValue < 0) {
                                    //if negative values are entered in the percentage text box.                                    
                                    percentageTextBoxValue = 0;                                
                                    document.getElementById(textfield).value = 0;
                                }
                                
                                //slider movement                                                   
                                
                                // convert the real value into a pixel offset                                           
                                slider.setValue(Math.round(percentageTextBoxValue/scaleFactor));
                                //In case of decimal values given in the text box will not trigger the 
                        		//slider movement when both slider and text box are having the same values
                        		//eg(2.3 in text box and slider value is already 2). 
                        		//In this type of scenarios the text box will be updated with the slider value 
                        		//explicitly. 
                        		//setting the text box value with slider value after slider movement
                                document.getElementById(textfield).value = slider.getRealValue();
    
                        }           
                });
    

            // Use the "get" method to get the current offset from the slider's start
            // position in pixels.  By applying the scale factor, we can translate this
            // into a "real value
            Event.on("getval", "click", function(e) {
                YAHOO.log("Current value: "   + slider.getValue() + "\n" +"Converted value: " + slider.getRealValue(), "info", "example");
            });
    
    
        });
    
    })();
    
    
    }
    
    }) ();

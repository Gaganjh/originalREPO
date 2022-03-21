
<%@ tag body-content="empty"%>
<%@ attribute name="dateField" required="true"%>
<%@ attribute name="calendarcontainer" required="true"%>
<%@ attribute name="datefields" required="true"%>
<%@ attribute name="calendarpicker" required="true"%>


<STYLE type="text/css">
DIV.yuimenu .bd {
    ZOOM: normal;
}
#${calendarcontainer} {
    PADDING-RIGHT:10px; PADDING-LEFT:10px; PADDING-BOTTOM:10px; PADDING-TOP:10px;
    
}
#calendarmenu {
    POSITION:absolute;
    z-index: 100;
}
#${calendarpicker} BUTTON {
    PADDING-RIGHT:1px; PADDING-LEFT:1px; BACKGROUND:url("/assets/unmanaged/images/calendar.gif") no-repeat center center; PADDING-BOTTOM: 0px; OVERFLOW: hidden; PADDING-TOP:0px; WHITE-SPACE:nowrap; TEXT-ALIGN:left; width:20px; height:18px; min-height: 1em;
}

#${datefields} {
    BORDER-RIGHT:#666 1px none; BORDER-TOP:#666 1px none; BORDER-LEFT:#666 1px none; BORDER-BOTTOM:#666 1px none;
	width: 20px; height: 18px; margin-top: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px;PADDING-RIGHT:0px; PADDING-LEFT:0px; PADDING-BOTTOM:0px; PADDING-TOP:0px;
}
#${calendarpicker} {
    VERTICAL-ALIGN: baseline;
}
</STYLE>

 <!--[if IE 6]>
    <STYLE>
    #buttoncalendar {
        width:175px!important;
    }
    </STYLE>
 <![endif]--> 


<%%>

<div id="calendar_yui">
    <div class="yui-skin-sam">
        <fieldset id="${datefields}" />
    </div>
</div>

<SCRIPT type="text/javascript">
    YAHOO.util.Event.onDOMReady(function () {
        function onButtonClick() {
            /*
                 Create an empty body element for the Overlay instance in order 
                 to reserve space to render the Calendar instance into.
            */
            oCalendarMenu.setBody("&#32;");
            oCalendarMenu.body.id = "${calendarcontainer}";
            // Render the Overlay instance into the Button's parent element
            oCalendarMenu.render(this.get("container"));
            // Align the Overlay to the Button instance
            oCalendarMenu.align();
            /*
                 Create a Calendar instance and render it into the body 
                 element of the Overlay.
            */
            var oCalendar = new YAHOO.widget.Calendar("buttoncalendar", oCalendarMenu.body.id);
            oCalendar.render();
         oCalendarMenu.show();
            /* 
                Subscribe to the Calendar instance's "changePage" event to 
                keep the Overlay visible when either the previous or next page
                controls are clicked.
            */
            oCalendar.changePageEvent.subscribe(function () {
                
                window.setTimeout(function () {

                    oCalendarMenu.show();
                
                }, 0);
            
            });

            /*
                Subscribe to the Calendar instance's "select" event to 
                update the month, day, year form fields when the user
                selects a date.
            */
            oCalendar.selectEvent.subscribe(function (p_sType, p_aArgs) {
                var aDate;
                if (p_aArgs) {
                        
                    aDate = p_aArgs[0][0];
                    var myDate = (Number(aDate[1]) < 10 ? '0' : '') + aDate[1] + '/' + 
                     (Number(aDate[2]) < 10 ? '0' : '') + aDate[2] + '/' + aDate[0];                        
                    YAHOO.util.Dom.get("${dateField}").value = myDate;
                }
                oCalendarMenu.hide();
            });

            /*
                This method will be called just before the Calendar menu is shown. This method picks up the date
                entered by the user in the Date Input field and shows that date as Selected in the caledar menu.
                For example: if the input field has 12/28/2007, and if the user clicks on calendar button, the
                calendar menu will show 28th December 2007 selected.
            */
            function updateCal() {
                var txtDate1 = document.getElementById("${dateField}");
                if (txtDate1.value != "") {
                    oCalendar.select(txtDate1.value);
                    var selectedDates = oCalendar.getSelectedDates();
                    if (selectedDates.length > 0) {
                        var firstDate = selectedDates[0];
                        oCalendar.cfg.setProperty("pagedate", (firstDate.getMonth()+1) + "/" + firstDate.getFullYear());
                        oCalendar.render();
                        oCalendarMenu.show();
                    }
                    
                }
            }
            
            //Subscribing the updateCal() method for beforeRenderEvent.
            oCalendar.beforeRenderEvent.subscribe(updateCal())
            

        /*
                 Unsubscribe from the "click" event so that this code is 
                 only executed once
            */
            this.unsubscribe("click", onButtonClick);
        }

        // Create an Overlay instance to house the Calendar instance
        var oCalendarMenu = new YAHOO.widget.Overlay("calendarmenu");
        // Create a Button instance of type "menu"
        var oButton = new YAHOO.widget.Button({ 
                                            type: "menu", 
                                            id: "${calendarpicker}", 
                                            label: "", 
                                            menu: oCalendarMenu, 
                                            container: "${datefields}" });
        /*
            Add a "click" event listener that will render the Overlay, and 
            instantiate the Calendar the first time the Button instance is 
            clicked.
        */
        oButton.on("click", onButtonClick);
    });
</SCRIPT>

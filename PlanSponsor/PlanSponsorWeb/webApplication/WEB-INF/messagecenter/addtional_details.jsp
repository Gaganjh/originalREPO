<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
YAHOO.namespace("johnhancock.psw");

var messageId = null;

function moreDetails(messageId) {
 	var o = document.getElementById("moreDetailsDiv" + messageId);
 	if ( o != null ) o.style.display = ""; 
 	o = document.getElementById("moreDetailsAnchor" + messageId);
 	if ( o != null ) o.style.display = "none"; 
 	
}
function lessDetails(messageId) {
	var o = document.getElementById("moreDetailsDiv" + messageId);
 	if ( o != null ) o.style.display = "none"; 
 	o = document.getElementById("moreDetailsAnchor" + messageId);
 	if ( o != null ) o.style.display = ""; 
 	
}
function showDetails(contextElement, mId, text) {
  <c:if test="${empty param.printFriendly}"> 
	messageId = mId;
	document.getElementById('messageInfo').innerHTML = text;
	YAHOO.johnhancock.psw.mcAdditionalInfoPanel.cfg.setProperty("context", [contextElement, "tl", "bl"]);
    YAHOO.johnhancock.psw.mcAdditionalInfoPanel.show();
  </c:if>
}

<c:if test="${empty param.printFriendly}">

// Close the additional information panel
// set the async request to set the message status as VISITED
function closePanel() {
  var visitMessageCallback = { 
    cache:false,
    success: function(response) {
      /*success handler code*/
      var result = response.responseText;
      if (result=="success") {
         if (messageId != null) {
           showMessageVisited(sectionIds, messageId);
         }
      }
    }, 
    failure: function(response) {/*failure handler code*/}, 
    argument: null 
  };
  if (messageId != null) {
	  var transaction = YAHOO.util.Connect.asyncRequest(
	    'GET', '/do/messagecenter/visitMessage?messageId=' + messageId,
	    visitMessageCallback);
   }
    
   YAHOO.johnhancock.psw.mcAdditionalInfoPanel.hide();
}

// Show the message as visited style
function showMessageVisited(sections, messageId) {
   for (var i = 0; i < sections.length; i++) {
      var rowId = "Message_" + sections[i] + "_" + messageId;
      var row = document.getElementById(rowId);
      if (row != null) {
         var imgTd = row.cells[0];
         setVisitedImage(imgTd);
         for (var j = 1; j < row.cells.length; j++) {
             row.cells[j].style.color = "${visitedMsgColor}";
         }
      }
   }
}

// set the image to be visited
function setVisitedImage(cell) {
  for (var i=0; i<cell.childNodes.length; i++) {
     if (cell.childNodes[i].nodeName=="A") {
	     var link = cell.childNodes[i];
		 for (var j = 0; j < link.childNodes.length; j++) {
		   var c= link.childNodes[j];
		   if (c.nodeName=='IMG') {
				var imgName = c.getAttribute("src");
				var newImgName = imgName.replace("${newActIcon}", "${visitedActIcon}");
				c.setAttribute("src", newImgName);
				return;
 		   }
	     }
	     return;
      }
  }
}

YAHOO.util.Event.onDOMReady(function () { 
    YAHOO.johnhancock.psw.mcAdditionalInfoPanel = new YAHOO.widget.Panel("AdditionInfoPanel", {
    constraintoviewport: true,
    width: "360px",
    modal: true,
    close: false,
    underlay: "none",  
    zIndex: 4,
    visible:false} );
    YAHOO.util.Event.addListener("mcAdditionalInfoPanelClose", "click", closePanel);
    YAHOO.johnhancock.psw.mcAdditionalInfoPanel.render(document.body);    
 }
)
</c:if>
</script>

<c:if test="${empty param.printFriendly}">
<div id="AdditionInfoPanel">
	<div class="hd"
		style="padding: 0px; background: none; background-color: #002D62">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%" style="padding: 5px">
				   <font style="font-size: 15px; font-weight: bold; color: #ffffff">
					More Details
					</font>
				 </td>
			</tr>
		</table>
		<div class="bd" style="background-color: #ffffff; padding: 0px">
			<table border="0" cellpadding="3" cellspacing="0" width="350">
				<tr>
					<td width="3">
					  <img src="/assets/unmanaged/images/s.gif" height="1" width="3" />
					</td>
					<td width="350">
					  <div id="messageInfo"></div>
						<table border="0" cellpadding="3" cellspacing="0" width="100%">
							<tr>							 
								<td align="center">
								  <input id="mcAdditionalInfoPanelClose" type="button" class="button100Lg" value="close" />
								</td>
							</tr>
						</table>
				 	 </td>
	
				  	 <td width="3">
					  <img src="/assets/unmanaged/images/s.gif" height="1" width="3" />
					 </td>
				  </tr>			
				  <tr>
					  <td colspan="3">
						<img src="/assets/unmanaged/images/s.gif"	height="1" width="1" />
				      </td>
				  </tr>
			 </table>
		</div>
	</div>
</div>
</c:if>
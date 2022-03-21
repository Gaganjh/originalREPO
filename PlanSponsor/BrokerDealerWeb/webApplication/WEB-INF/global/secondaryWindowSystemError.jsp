
<br/>

<%--Error- message box--%> 
<div class='message message_error'>
<dl>
<dt>Error Message</dt>
<dd>1.&nbsp;&nbsp;System Error occurred.<br>
		Please try again. If you are still experiencing the same problem
		please call your client account representative. <b>[<%= request.getAttribute("errorCode") %> - <%= request.getAttribute("uniqueErrorId") %>]
		</dd>
		</dl></div>
	
<p><a href="javascript:window.close();">close</a></p>
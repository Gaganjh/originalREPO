<%@ page import="org.owasp.encoder.Encode"%>
        <script language="javascript" type="text/javascript">
            <!--            
            window.onload = initializePrintWindow;

            function initializePrintWindow() {
                try {
                    disableInputElements();
                    document.getElementById("fundsInfo").style.display = "block";
                } catch (e) {
                }
                try {
                    setTimeout("window.print()", 2000);
                } catch (e) {
                }
            }

            // Disable the input elements for printer friendly page.
            function disableInputElements() {
                try {
                    document.getElementById("invOptionsList").className = "";
                    var inputElements = document.getElementsByTagName("input"); 
                    for(var i=0; i < inputElements.length; i++) {
                        if(inputElements[i].getAttribute("type") == "checkbox") {
                            inputElements[i].disabled = true;
                        }
                    }
                } catch (e) {
                }
            }
            -->
        </script>
    
    
<div id="fundsInfo" style="display:none">
<% String previewContent = request.getParameter("fundEvalHtmlPrintPreviewString");%>
<%=previewContent%>
</div>
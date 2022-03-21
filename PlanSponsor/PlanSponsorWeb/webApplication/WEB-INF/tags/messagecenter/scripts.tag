<%@ tag
	import="com.manulife.pension.ps.web.messagecenter.model.MCAbstractReportModel"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="model"
	type="com.manulife.pension.ps.web.messagecenter.model.MCAbstractReportModel"
	required="true" rtexprvalue="true"%>

<script type="text/javascript">
<!--
   function gotoLink(url) {
   	 <c:if test="${model.navigatable}">
   	    window.location=url;
   	 </c:if>
   	 return;
   }
   
	function doPrint()
	{
	  var printURL = '<%=model.getUrlGenerator().getTabPrintFriendlyUrl(model.getSelectedTab())%>';
	  window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
	}
	
	function openWin(url) {
		options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=800,height=450,left=10,top=10";
		newwindow=window.open(url, "general", options);
		if (navigator.appName=="Netscape") {
			newwindow.focus();
		}
	}
//-->
</script>	
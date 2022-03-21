<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<script type="text/javascript" >
function doSubmit(button){

	document.forms.performanceChartResultsForm.button.value=button;
	document.forms.performanceChartResultsForm.submit();
}

function doPrint()
{
  var reportURL = new URL("/do/investment/performanceChartResult/");
  reportURL.setParameter("task", "print");
  reportURL.setParameter("printFriendly", "true");
  window.open(reportURL.encodeURL(),"","width=740,height=480,resizable,toolbar,scrollbars,");
}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  	<td>
   <c:if test="${empty param.printFriendly }" >  
	<br><ps:fundPerformanceChart beanName="layoutPageBean"  imageType="GIF" mode="image" width="715" height="450" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." />	  
   </c:if>  
   <c:if test="${not empty param.printFriendly }" >  
	<br><ps:fundPerformanceChart beanName="layoutPageBean"  imageType="GIF" mode="image" width="665" height="450" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." />	  
   </c:if>  

	<br>
            
   <c:if test="${empty param.printFriendly }" >            
      <table width="699" border="0" cellpadding="0" cellspacing="0">
        <tr align="right">
          <ps:form modelAttribute="performanceChartInputForm" name="performanceChartResultsForm" method="GET" action="/do/investment/performanceChartResult/">
<form:hidden path="button" />
          </ps:form>

          <td width="553"><input name="Submit" type="submit" class="button100Lg" onClick="javascript:doSubmit('reset');" value="new chart"></td>
          <td width="146"><input name="Submit" type="submit" class="button100Lg" onClick="javascript:doSubmit('next');" value="modify chart"></td>
        </tr>
      </table>

  	  <br>
	  <p><content:pageFooter beanName="layoutPageBean"/></p>
	</c:if>
 	  <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
      <p class="footnote"><ps:fundFootnotes symbols="symbolsArray"/></p>
 	  <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>

 	</td>
  </tr>
</table>
<c:if test="${not empty param.printFriendly}" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>

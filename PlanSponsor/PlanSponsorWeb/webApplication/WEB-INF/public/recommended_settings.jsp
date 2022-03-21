<%-- --------------------------------------------------------------------------
Use case    : Recommended browser settings
Description : describes the recommended browser settings to view the manulife web site.
File: recommended_settings.jsp
-------------------------------------------------------------------------- --%>
<%@ page session="false" %>

<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<script type="text/javascript" >
<!--
function openWin(url) {
	options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=785,height=400,left=0,top=0";
	newwindow=window.open(url, "general", options);
	if (navigator.appName=="Netscape") {
		newwindow.focus();
	}
}
//-->
</script>

<br>
<table width="700" border="0" cellspacing="0" cellpadding="0" class="fixedTable">
	<tr>
	<td width="50">&nbsp;</td>
	<td class="greyText" align="left">
	<p>
	<img src='<content:pageImage beanName="layoutPageBean" type="pagetitle"/>' alt="Recommended Settings">
	</p>
	</td>
	</tr>
	
	<tr>
	<td width="50">&nbsp;</td>
	<td class="greyText" align="left">
	<p>
     <!--Layout/intro1-->
        <c:if test="${not empty layoutPageBean.introduction1}">
        	<content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
  	    	<br><br>
</c:if>
  	 <!--Layout/Intro2-->
	   	<c:if test="${not empty layoutPageBean.introduction2}">
			<content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
   		    <br><br>
</c:if>
	<content:getAttribute attribute="body1" beanName="layoutPageBean"/>
	</p>
	</td>
	</tr>

	<tr>
	<td width="50">&nbsp;</td>
	<td>
	<br> 
	<br>
<input type="button" onclick="javascript:history.back();" name="button" class="button100Lg" value="previous"/>
	<br> 
	<br>
    <p><content:pageFooter beanName="layoutPageBean"/></p>
    <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
    <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>

	</td>
	</tr>	
</table>
<br><br>

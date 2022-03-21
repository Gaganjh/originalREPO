<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>


<script type="text/javascript" >
//This function is implemented to be executed during onLoad.
function doOnload() {
	if(document.getElementById("region").value == 'USA'){
	    document.getElementById("companySelect").value="usa";
	} else {
		document.getElementById("companySelect").value="ny";
	}
}
</script>


<div id="contentOuterWrapper">
  <div id="contentWrapper">
<report:formatMessages scope="request"/> 
    <div id="rightColumn1">
         <h1><content:getAttribute id="layoutPageBean" attribute="body3Header"/></h1>
         
         <c:if test="${not empty layoutPageBean.body3}">
		        <p><content:getAttribute attribute="body3" beanName="layoutPageBean"/></p>
</c:if>
     </div>  

    <div id="contentTitle"><content:getAttribute id="layoutPageBean" attribute="name"/></div>

         <div id="content3">

         <%-- Layout/intro1 --%>
         <c:if test="${not empty layoutPageBean.introduction1}">
     	        <p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>

         <%-- Layout/intro2 --%>
         <c:if test="${not empty layoutPageBean.introduction2}">
                <p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
</c:if>

         <br/>
         <br/>
         <br/>
         <br/>

<input type="hidden" name="region" id="region" />

<input type="hidden" name="includeNml" id="showNml" value="CHOICE" />

<c:if test="${historicalFapPdfPageForm.includeNml =='NO'}">
                <content:getAttribute beanName="layoutPageBean" attribute="body1"/>
</c:if>
<c:if test="${historicalFapPdfPageForm.includeNml !='NO'}">
                 <content:getAttribute beanName="layoutPageBean" attribute="body2"/>
</c:if>

         </div>

    <br class="clearFloat" />
  </div>
</div>

<!-- FootNotes and Disclaimer -->
<div class="footnotes">
		<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
		<div class="footnotes_footer"></div>
</div> 
			

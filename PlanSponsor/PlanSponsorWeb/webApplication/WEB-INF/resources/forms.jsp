<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager" %>

<script type="text/javascript">
	function downloadCoFid338InvestmentPDF(formType,docketFormNo) {
		var url = "/do/resources/forms/?action=downloadFundReplacementForm&formType="+formType+"&docketFormNo="+docketFormNo;
		var form = document.createElement("form");
		var csrfInput = document.createElement("input");
		csrfInput.setAttribute("type", "hidden");
		csrfInput.setAttribute("name", "_csrf");
		csrfInput.setAttribute("value", "${_csrf.token}");
		form.appendChild(csrfInput);
		
		form.action = url;
		form.method = 'POST';
		form.style.display = 'none';
		document.body.appendChild(form);
		form.submit();
	}
</script>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />
<%-- This jsp includes the following CMA content --%>
<content:contentByType id="forms" type="<%=ContentTypeManager.instance().FORM %>" />

	<td width="23%" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	<td width="54%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
          <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
        </tr>
        <tr>
          <td colspan="3" >
		  <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
		  <img src='<content:pageImage type="pageTitle" beanName="layoutPageBean"/>' alt="Forms" width="110" height="35"><br>
	<table width="720" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td><img src="/assets/unmanaged/images/s.gif" width="500"
								height="5"></td>
						</tr>
						<tr>
							<td valign="top"><c:if test="${layoutPageBean.introduction1 != ''}">
									<content:getAttribute attribute="introduction1"
										beanName="layoutPageBean" />
									
								</c:if> <content:getAttribute attribute="introduction2"
									beanName="layoutPageBean">
									<content:param>/do/tools/secureDocumentUpload/submit/</content:param>
									<content:param>/do/tools/fundChangePortal/</content:param>
								</content:getAttribute></td>

						</tr>
							
					<tr><td><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td>
				    <!-- Added to display XSS validation error message -->
          			<div id="errordivcs"><content:errors scope="session"/></div>
				</tr>
        		<tr>
					<td>
						<br>
						  <a href="javascript:toggleAll(true)"><img src="/assets/unmanaged/images/plus_icon_all.gif" border="0"/></a>
						  /
						  <a href="javascript:toggleAll(false)"><img src="/assets/unmanaged/images/minus_icon_all.gif" border="0"/></a>
						  <b>All</b>
				</td>
        		</tr>
				<tr><td><img src="/assets/unmanaged/images/s.gif" width="500" height="15"></td></tr>
        		<tr>
				
        		
         			<td>
         				<ps:formsTable collection="forms" layoutPage="layoutPageBean" />
          			</td>
          		</tr>
          		
          		<tr>
          			<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          			<td valign="top"> <img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
        		</tr>
      	  </table>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
			<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 		  </td>
 		</tr>
 	  </table>
 	
 	<td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    

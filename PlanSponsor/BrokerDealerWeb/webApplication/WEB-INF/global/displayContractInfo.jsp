<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

       <%@ page import="com.manulife.pension.bd.web.bob.BobContext"%> 

<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<%
  BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
  pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
  
  %>
  
  


<h2><content:getAttribute id="layoutPageBean" attribute="name"/>
  
</h2>
						
<p class="record_info"><strong>${bobContext.contractProfile.contract.companyName} (${bobContext.contractProfile.contract.contractNumber})</strong> 
    <input class="btn-change-contract" type="button" onmouseover="this.className +=' btn-change-contract-hover'" onmouseout="this.className='btn-change-contract'" onclick="top.location.href='/do/bob/blockOfBusiness/Active/'" value="Change contract">
</p>
<!--Layout/intro1-->
<c:if test="${not empty layoutPageBean.introduction1}">
    <p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>

<!--Layout/Intro2-->
<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>

<div class="table_controls_footer"></div>

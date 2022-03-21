<%@taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>

<un:useConstants var="bdContentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />
	
<mrtl:noCaching/>
<content:contentBean contentId="${bdContentConstants.FUND_AND_PERFORMANCE_LAYOUT_LAYER1}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="Layer1"/>

<content:contentBean contentId="${bdContentConstants.FUND_INFORMATION_TAB}" 
type="${bdContentConstants.TYPE_MISCELLANEOUS}" beanName="FundInfoTab"/>

<content:contentBean contentId="${bdContentConstants.FUND_ADMINISTRATION_TAB}" 
type="${bdContentConstants.TYPE_LAYOUT_PAGE}" beanName="FundAdminTab"/>


<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" />
<c:set var="fapLayoutPageBean" value="${layoutBean.layoutPageBean}" scope="session" />

<%-- Additional Information Section--%>
  <a name="anchor1"></a>
  <div id="rightColumn1">
     <h1><content:getAttribute attribute="name" beanName="Layer1"/></h1>
     <p><content:getAttribute attribute="text" beanName="Layer1"/></p>
  </div>

<%-- Page header and intro text --%>
<div id="content">
   <h1><content:getAttribute attribute="name" beanName="fapLayoutPageBean"/>: <span style="font-size:14px;">${fapForm.asOfDate}</span></h1>
   
   <div>
       <p><content:getAttribute attribute="text" beanName="FundInfoTab"/></p>
   </div>
   
   
</div>
 <br/>   
<navigation:contractReportsTab/>        
<br/>

<c:if test="${fapForm.fcpContent eq true}">         
<table>
<tr>
<td>
<content:getAttribute attribute="body1"	beanName="FundAdminTab">
<content:param>/do/tools/fundChangePortal/</content:param>
</content:getAttribute>
</td>
</tr>
</table>
</c:if>


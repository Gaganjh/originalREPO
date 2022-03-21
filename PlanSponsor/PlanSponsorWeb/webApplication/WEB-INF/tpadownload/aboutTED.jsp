<%@ page session="false" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>

<SCRIPT>
var framesetQuery = "";
if ( window.top.location.search != 0 ) {
    framesetQuery = window.top.location.search;
} else {
    framesetQuery = "?OpenFrameset";
}

function getParameter ( queryString, parameterName ) {
	var parameterName = parameterName + "=";
	if ( queryString.length > 0 ) {
	    begin = queryString.indexOf ( parameterName );
	    if ( begin != -1 ) {
		begin += parameterName.length;
		end = queryString.indexOf ( "&" , begin );
		if ( end == -1 ) {
		    end = queryString.length
		}
		return unescape ( queryString.substring ( begin, end ) );
	    }
	    return "null";
	}
}

function previous() {
	if (getParameter(framesetQuery, 'from') == "history") {
		top.location.href = '/do/tpadownload/tedHistoryFilesReport/';
	} else {
		top.location.href = '/do/tpadownload/tedCurrentFilesReport/';
	}
}
</SCRIPT>


<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />


<c:if test="${not empty layoutBean.getParam(titleImageFallback)}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam(titleImageFallback)}" />

<img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>" 
alt="${layoutBean.getParam(titleImageAltText)}"/>

    <br>
</c:if>
<c:if test="${empty layoutBean.getParam(titleImageFallback)}">
    <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
    <br>
</c:if>

<A href="javascript:previous();">Previous</a>
<BR><BR>


<c:if test="${layoutPageBean.introduction1 != '' }">
     <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
</c:if>


<c:if test="${ layoutPageBean.introduction2 !=' '}">
     <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
</c:if>
<p><content:errors scope="request"/></p>
<table cellpadding="0" cellspacing="0" border="0" class="box"> 
  <tr class="tablehead">
    <td colspan="5" class="tableheadTD1"><b> <content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
  </tr>

  <tr>
    <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    <td width="10"><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
    <td width="640">
	
	<c:if test="${ layoutPageBean.body1 !=' ' }">
	    <content:getAttribute beanName="layoutPageBean" attribute="body1"/>
    </c:if>

	<c:if test="${ layoutPageBean.body2 != ' '}">
	    <content:getAttribute beanName="layoutPageBean" attribute="body2"/>
    </c:if>
	
    <c:if test="${ layoutPageBean.body3 !=' '}">
	   <content:getAttribute beanName="layoutPageBean" attribute="body3"/>
    </c:if>
    </td>
    <td width="10"><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
    <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
  </tr>
  <tr>
    <td class="databorder" colspan="6"></td>
  </tr>
</TABLE>

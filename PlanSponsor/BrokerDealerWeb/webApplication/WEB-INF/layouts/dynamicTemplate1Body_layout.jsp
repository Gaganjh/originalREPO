<%@page session="false" %>
<%@page contentType="text/html; charset=utf-8" %>
<%@page import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.pagelayout.StyleSheet" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.manulife.pension.content.valueobject.LayoutPage" %>



<%@taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mrtl:noCaching/>

<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" />
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<%
    BDLayoutBean layout = (BDLayoutBean) request.getAttribute(BDConstants.LAYOUT_BEAN);
    LayoutPage layoutPage = (LayoutPage)layout.getLayoutPageBean();
%>

<c:set var="strIntroduction1" value="${layoutPageBean.introduction1}"/>
<c:set var="strIntroduction2" value="${layoutPageBean.introduction2}"/>
<c:set var="strBody1Header" value="${layoutPageBean.body1Header}"/>
<c:set var="strBody1" value="${layoutPageBean.body1}"/>
<div id="contentOuterWrapper">
    <div id="contentWrapper">
            <%if(!StringUtils.isBlank(layoutPage.getName())){%>
                <div id="contentTitle"><content:getAttribute attribute="name" beanName="layoutPageBean"/></div>
            <%}%>
            <c:if test="${!empty strIntroduction1}">
                <p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
            </c:if>
            <c:if test="${!empty strIntroduction2}">
                <p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
            </c:if>
            <c:if test="${!empty strBody1Header || !empty strBody1}">
                    <c:if test="${!empty strBody1Header}">
                        <strong><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></strong>
                    </c:if>
                    <c:if test="${!empty strBody1}">
                        <content:getAttribute beanName="layoutPageBean" attribute="body1"/>
                    </c:if>
            </c:if>
    </div><%-- End of contentWrapper --%>
</div><%-- End of contentOuterWrapper --%>
<div class="footnotes">
    <dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
    <dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
    <dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
    <div class="footnotes_footer"></div>
</div>  <!--#footnotes-->

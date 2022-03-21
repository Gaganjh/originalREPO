<%@page session="false" %>
<%@page contentType="text/html; charset=utf-8" %>
<%@page import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.pagelayout.StyleSheet" %>

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
%>

<c:set var="strIntroduction1" value="${layoutPageBean.introduction1}"/>
<c:set var="strIntroduction2" value="${layoutPageBean.introduction2}"/>
<c:set var="strBody1Header" value="${layoutPageBean.body1Header}"/>
<c:set var="strBody1" value="${layoutPageBean.body1}"/>
<c:set var="strBody2Header" value="${layoutPageBean.body2Header}"/>
<c:set var="strBody2" value="${layoutPageBean.body2}"/>
<c:set var="strBody3Header" value="${layoutPageBean.body3Header}"/>
<c:set var="strBody3" value="${layoutPageBean.body3}"/> 

<div id="contentOuterWrapper">
    <div id="contentWrapper">
        <c:if test="${!empty strBody2Header || !empty strBody2}">
            <div id="rightColumn1">
                <c:if test="${!empty strBody2Header}">
                    <h2><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></h2>
                </c:if>
                <c:if test="${!empty strBody2}">
                   <p><content:getAttribute beanName="layoutPageBean" attribute="body2"/></p> 
                </c:if>
            </div> 
        </c:if>
        <c:if test="${!empty strBody3Header || !empty strBody3}">
            <div id="rightColumn1">
                <c:if test="${!empty strBody3Header}">
                    <h2><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></h2>
                </c:if>
                <c:if test="${!empty strBody3}">
                   <p><content:getAttribute beanName="layoutPageBean" attribute="body3"/></p>
                </c:if>
            </div> 
        </c:if>
        <div id="contentTitle"><content:getAttribute attribute="name" beanName="layoutPageBean"/></div>
        <div id="content3">
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
        </div><%--end of content3--%>
    </div><%-- End of contentWrapper --%>
</div><%-- End of contentOuterWrapper --%>
<div class="footnotes">
    <dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
    <dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
    <dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
    <div class="footnotes_footer"></div>
</div>  <!--#footnotes-->

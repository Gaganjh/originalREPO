<%-- Prevent the creation of a session --%>
 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%-- avoid duplication of header code --%> 
<jsp:include page="standardheader.jsp" flush="true"/>
<jsp:include page="commonheaderforinputforms.jsp" flush="true"/>

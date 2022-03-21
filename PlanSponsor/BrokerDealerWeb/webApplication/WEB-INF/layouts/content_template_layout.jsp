<%@page contentType="text/html; charset=utf-8" %>
<%@page import="com.manulife.pension.bd.web.pagelayout.BDLayoutBean"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.BDPdfConstants"%>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>
<%@page import="com.manulife.pension.bd.web.ApplicationHelper"%>
<%@page import="com.manulife.pension.bd.web.pagelayout.StyleSheet" %>
<%@page import="com.manulife.pension.platform.web.util.ReportsXSLProperties" %>
<%@page import="org.apache.commons.lang.StringUtils" %>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>

<mrtl:noCaching/>

<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.bd.web.pagelayout.BDLayoutBean" />
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script type="text/javascript" src="/assets/unmanaged/javascript/MainDropDownMenu.js"></script> 

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%
    BDLayoutBean layout = (BDLayoutBean) request
    .getAttribute(BDConstants.LAYOUT_BEAN);
    BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
    request.setAttribute("userProfile", userProfile);
    String contentIdParam = request.getParameter("id");
    String menuName = request.getParameter("menuName");
    String PARTNERING_WITH_US = "partnering";
    String FIND_LITERATURE_MENU_ID = "literature";
    String PRIME_ELEMENTS_MENU_ID = "prime";
%>
    <!-- CMA <%=layout.getContentId()%> -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE"/>
        <meta http-equiv="PRAGMA" content="NO-CACHE"/>
        
	<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>

        <script type="text/javascript"> 
            function doOnload() {
                // This function is not implemented. It will will implement in the body pages 
                // if anything required to be executed during onLoad
            }
        </script>
            
        <%if (layout.getJavascripts() != null) {
            for (String javaScriptV : layout.getJavascripts()) {
        %>
            <script type="text/javascript" src="<%=javaScriptV %>"></script>
        <%  }
          }
        %>
        <script type="text/javascript"> 
            function doPrintPDF() {
                doOpenPDF(<%=ReportsXSLProperties.get(BDConstants.MAX_CAPPED_ROWS_IN_PDF)%>)
            }
        </script>       
        <%if(layout.getStylesheets() != null) {
            for (StyleSheet styleSheet: layout.getStylesheets()) {
        %>  
            <link rel="stylesheet" type="text/css" 
                <%if (styleSheet.getMedia() != null) { %>
                    media="<%=styleSheet.getMedia()%>" 
                <%}%> href="<%=styleSheet.getHref()%>" />
        <%  }
          }
        %>
        <!--[if lte IE 6]><link rel="stylesheet" type="text/css" media="screen" href="/assets/unmanaged/stylesheet_nbdw/ie6.css" /><![endif]-->
        <!--[if gte IE 7]><link rel="stylesheet" type="text/css" media="screen" href="/assets/unmanaged/stylesheet_nbdw/ie7.css" /><![endif]-->
    <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/print.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/content.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/screen.css" />
<%
    if(StringUtils.isBlank(contentIdParam)){
        if (StringUtils.equalsIgnoreCase(menuName, PARTNERING_WITH_US)) {
            // user select the Partnering with us tab
%>
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/landing_page_partnering.css" />
<%
        } else if (StringUtils.equalsIgnoreCase(menuName,
                FIND_LITERATURE_MENU_ID)) {
            // user select the Find Literature tab
%>    
    <script type="text/javascript" src="/assets/unmanaged/javascript/find_literature_jquery-1.3.2.min.js"></script>
    <script type="text/javascript" src="/assets/unmanaged/javascript/find_literature_search_results_thumbs.js"></script>
    <script type="text/javascript" src="/assets/unmanaged/javascript/SpryTabbedPanelsFL.js"></script>                   
    <script type="text/javascript" src="/assets/unmanaged/javascript/SpryTabbedPanelsThumbListFL.js"></script>
    <script type="text/javascript" src="/assets/unmanaged/javascript/AJS.js"></script>
    <script type="text/javascript" src="/assets/unmanaged/javascript/AJS_fx.js"></script>
    <script type="text/javascript" src="/assets/unmanaged/javascript/gb_scripts.js"></script>

    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/find_literature_comm_buttons.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/find_literature_comm_pg_elements.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/find_literature_gen_table_cont.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/find_literature_html_elements.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/find_literature_search_browse.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/find_literature_search_elements.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/SpryTabbedPanelsFL.css" /> 
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/SpryTabbedPanelsThumbListFL.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/screen.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/gb_styles.css"/> 
    
    <script type="text/javascript">
    <!--
    function MM_goToURL() { //v3.0
      var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
      for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
    }
    // Grey Box variable
    var GB_ROOT_DIR = "/assets/generalimages/";
    //-->
    </script>
    
    
<%
        } else if (StringUtils.equalsIgnoreCase(menuName,
                PRIME_ELEMENTS_MENU_ID)) {
            // user select the Prime Elements tab
%>
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/landing_page_prime.css" />    
<%
            // end of landing page
        }
%>
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/all_landing_pages.css" />
<%
    }else if(StringUtils.isNumeric(contentIdParam)){
%>
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/all_dynamic_content.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/landing_page_partnering.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/assets/stylesheets/all_landing_pages.css" />      
<%
    }
%>
    <script type="text/javascript" src="/assets/unmanaged/javascript/BDUserCookieReader.js"></script>
    <title><content:pageTitle beanName="layoutPageBean" /></title>
    </head>
    <body id="common_elements" onload="doOnload();">
        <div id="page_wrapper">
            <navigation:siteHeader/>
            <%-- Main navigation menu --%>
            <navigation:menuRenderer/>
            <report:formatMessages scope="request"/>
            <div id="page_content" class="background-img">
                 <jsp:include page="<%=layout.getBody()%>"/>
                 <jsp:include page="/WEB-INF/global/standardfooter.jsp"/>
             </div>  <!-- page_content -->
         </div> <!-- page_wrapper -->
         <div id="page_wrapper_footer">&nbsp;</div> <!--NOTE: To end the page at the bottom--> 
        <%
        if (StringUtils.equalsIgnoreCase(menuName,
                    FIND_LITERATURE_MENU_ID)) {
                // user select the Find Literature tab
        %>
            <script type="text/javascript"> 
            <!-- 
                var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
                var TabbedPanels2 = new Spry.Widget.TabbedPanels("TabbedPanels2");
            //-->
            </script>
        <%
        }
        %>
    </body>
</html>

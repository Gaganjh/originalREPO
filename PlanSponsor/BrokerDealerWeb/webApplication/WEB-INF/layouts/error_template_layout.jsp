<%@page contentType="text/html; charset=utf-8" %>
<%@page import="com.manulife.pension.bd.web.pagelayout.StyleSheet" %>


<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/mrtl.tld" prefix="mrtl" %>

<mrtl:noCaching/>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    
<script type="text/javascript" src="/assets/unmanaged/javascript/MainDropDownMenu.js"></script> 

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

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
        
        <!--[if lte IE 6]><link rel="stylesheet" type="text/css" media="screen" href="/assets/unmanaged/stylesheet_nbdw/ie6.css" /><![endif]-->
        <!--[if gte IE 7]><link rel="stylesheet" type="text/css" media="screen" href="/assets/unmanaged/stylesheet_nbdw/ie7.css" /><![endif]-->
            <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/contract_funds.css" />
            <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/print.css" />
            <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/content.css" />
            <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/business_building_tools.css" />
            <link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/public_home.css" />
        
        
        <title>Error Page</title>
    </head>
    <body id="contract_funds">
        <div id="page_wrapper">
            <navigation:siteHeader/>
        
            <div id="page_content" class="background-img">
                 <span>&nbsp;Error occurred.</br></span> <%-- This will the error box shows the margin --%>  
                 <span>&nbsp;The requested page could not be found.<a href="/do/home/">&nbsp;Home Page</a></br></span>          
             </div>  <!-- page_content -->
         </div> <!-- page_wrapper -->
         <div id="page_wrapper_footer">&nbsp;</div> <!--NOTE: To end the page at the bottom--> 
    </body>
</html>

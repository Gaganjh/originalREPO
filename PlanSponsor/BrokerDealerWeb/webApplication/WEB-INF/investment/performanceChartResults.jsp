<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!--[if lte IE 6]><link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/ie6.css" /><![endif]-->
    <!--[if gte IE 7]><link rel="stylesheet" type="text/css" media="all" href="/assets/unmanaged/stylesheet_nbdw/ie7.css" /><![endif]-->
    
    <script type="text/javascript" >
        function doSubmit(button){
            document.forms.performanceChartResultsForm.button.value=button;
            document.forms.performanceChartResultsForm.submit();
        }
    </script>
</head>

<body id="contract_funds">
<report:formatMessages scope="request"/> 
<!--#secondary_nav-->
    <form name="performanceChartResultsForm" method="get" action="/do/fap/performanceChartResult/">    
<input type="hidden" name="button" /><%-- input - name="bdPerformanceChartInputForm" --%>
<input type="hidden" name="pdfCapped" value="false" /><%--  input - name="bdPerformanceChartInputForm" --%>
    </form>
    
    <h2><content:getAttribute id="layoutPageBean" attribute="name"/></h2>
    
    <table>
            <tr>
                <td>
                    <content:getAttribute id="layoutPageBean" attribute="introduction1"/>
                </td>
            </tr>
            <tr>
                <td>
                    <content:getAttribute id="layoutPageBean" attribute="introduction2"/>
                </td>
            </tr>
        </table>
	
    <div class="clear_footer">&nbsp;</div>
    <center>    
        <bd:fundPerformanceChart beanName="layoutPageBean"  imageType="GIF" mode="image" width="715" height="450" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." />
    </center>
    
    <div class="clear_footer">&nbsp;</div>
            
    <div class="button_regular"><a href="#" onclick="javascript:doSubmit('next');">Modify Chart</a></div>
    <div class="button_regular"><a href="#" onclick="javascript:doSubmit('reset');">New Chart</a></div>
    
    <br><br>
    
    <div align="left">
         <dl>
             <dd><content:pageFooter beanName="layoutPageBean"/></dd> 
         </dl>     
        <dl>
             <dd><content:pageFootnotes beanName="layoutPageBean"/></dd> 
        </dl>
        <dl>
            <dd><bd:fundFootnotes symbols="symbolsArray"/></dd>
        </dl>
        <dl>
            <dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd>
        </dl>
        <div class="footnotes_footer"></div>
    </div> <!--#footnotes-->
    
</body>
</html>

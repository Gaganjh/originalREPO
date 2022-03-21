<%-- Prevent the creation of a session --%>
 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<c:set var="bodyPage" value="${layoutBean.bodyPage}" scope="page" />
<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<table width="765" border="0" cellspacing="0" cellpadding="0" >
  <TR valign="top">
    <td>
	  <table width="550" border="0" cellspacing="0" cellpadding="0" >
	    <tr>
        <!-- column 1 -->
          <TD width="10"><IMG height=8 src="/assets/unmanaged/images/s.gif" width=10 border=0></TD>
          <TD width="100" vAlign=top>

            <TABLE id=column1 cellSpacing=0 cellPadding=0 border=0>
              <TBODY>
                <TR vAlign=top>
                  <TD><IMG src="/assets/unmanaged/images/s.gif" width=100 height=1></TD>
                </TR>
                <TR>
                  <TD class=greyText>&nbsp;</TD>
                </TR>
              </TBODY>
            </TABLE>

          </TD>

    	  <TD width="15"><IMG src="/assets/unmanaged/images/s.gif" width=15 border=0 height=1></TD>
    	  <!-- end column 1 -->
    	  <!-- column 2  -->

    	  <TD width="425" vAlign=top class=greyText> 
    	  	<img src="/assets/unmanaged/images/s.gif" width="402" height="1"><br>
		    <IMG src='<content:pageImage type="pageTitle" beanName="layoutPageBean"/>'><br>      
      	    <br>
      		<table width="425" border="0" cellspacing="0" cellpadding="0">
        	  <tr>
                <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
          	 	<td valign="top" width="425"> 
       	          <c:if test ="${not empty  layoutPageBean.subHeader}" >
          		     <b><content:getAttribute beanName="layoutPageBean" attribute="subHeader" /></b><br>
</c:if>
       
              <!--Layout/intro1-->
	              <c:if test="${not empty layoutPageBean.introduction1}">
                    <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
                    <br><br>
</c:if>
  				  <!--Layout/Intro2-->
	              <c:if test="${not empty layoutPageBean.introduction2}">
				    <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
         		    <br><br>
</c:if>
<c:if test="${not empty layoutBean.getParam('additionalIntroJsp')}">
<c:set var="additionalJSP" value="${layoutBean.getParam('additionalIntroJsp')}" /><



                    <jsp:include page="${additionalJSP}" flush="true"/>
</c:if>
           	    </td>
              </tr>
            </table>
          </td>

        </tr>
	    <tr>

<%-- body page will go here --%>
      	  <td colspan="4"><jsp:include page="${bodyPage}" flush="true" /> 
           <img src="/assets/unmanaged/images/s.gif" width="1" height="20"> <br>
          </td>
    	</tr>
	</table>
  </td>    
  <td width="20" height="312" valign="top" class="fixedTable"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
  <td width="195" height="312" valign="top" class="fixedTable">
  	<div align="left"><img src="/assets/unmanaged/images/s.gif" width="8" height="8" border="0" align="top"> </div>
  	<center>
	<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
	<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
	<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer3" />
	</center>
  </td>

 </tr>
</table>

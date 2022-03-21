<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.util.content.taglib.ImageRotatorTag" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>


<content:getImageSets layoutPageName="layoutPageBean" imageSetListName="imageSetListName"/>

      <!-- start publicheader.jsp area -->
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="fixedTable">
        <tr>
          <td width="330" rowspan="3" valign="top" bgcolor="#002d62" style="padding-bottom: 10px;" >
            <table width="300" border="0" cellspacing="0" cellpadding="0">
              <tr valign="top">
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
					<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO_PUBLIC%>"
   	  	                type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
    	                id="companyLogo"/>


	                <td ><img src='<content:getAttribute id="companyLogo" attribute="image.path" />' border="0"></td>
</c:if>
<c:if test="${applicationScope.environment.siteLocation=='ny'}" >
					<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO_NY_PUBLIC %>"
   		                type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
     	                id="companyLogoNY"/>

	                <td ><img src='<content:getAttribute id="companyLogoNY" attribute="image.path"/>' border="0"></td>
</c:if>
              </tr>
            </table>
          </td>
          <td valign="bottom" bgcolor="#002D62"></td>          
          <td width="1" valign="top"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
          <td width="209" bgcolor="#002D62">
            <TABLE class="fixedTable" cellSpacing="0" cellPadding="0"
                  width="132" height=27 border=0>
              <TBODY>
                <TR>
                  <TD width="7">&nbsp;</TD>
                  <TD class=whiteMenu vAlign=top width="81">&nbsp;&nbsp;&nbsp;&nbsp;
                     <!--A href="/public/contactUs.jsp"><SPAN class=whiteMenuBold>Contact us</SPAN></A-->
                  </TD>
                  <TD class=whiteMenu vAlign=bottom
                      width="44">&nbsp;&nbsp;&nbsp;</TD>
                </TR>
              </TBODY>
            </TABLE>
          </td>
        </tr>
        
        <tr>
          <td valign="bottom" bgcolor="#002D62"></td>
          <td width="1" valign="top"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
          <td width="209" valign="top" bgcolor="#002D62">&nbsp;</td>          
        </tr>
      </table>
      <table width="100%" height="29" border="0" cellpadding="0" cellspacing="0" bgcolor="#628297" class="fixedTable">
        <tr>
          <td width="16" height="29" valign="top"></td>
          <td width="539" class="whiteMenu" valign="middle" align="left">
            <FONT color=#efefce>&nbsp;&nbsp;<render:date value="<%= Calendar.getInstance().getTime().toString() %>" dateStyle="l" /></FONT>            
          </td>
          <td bgcolor="#FFFFFF" width="1"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
          <td width="209" valign="middle">&nbsp;</td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="fixedTable">
        <tr>
          <td width="2%" valign="top"></td>
          <td width="98%">&nbsp;</td>
        </tr>
      </table>
      <!-- end publicheader.jsp area -->

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentTypeManager" %>

<%-- This jsp includes the following CMA content --%>
<content:contentByType id="tpaforms" type="<%=ContentTypeManager.instance().TPAFORM %>" />

<content:contentBean contentId="<%=ContentConstants.COMMON_ADOBE_ACROBAT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="adobeAcrobat"/> 
<content:contentBean contentId="<%=ContentConstants.COMMON_HOWTO_USE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="howToUse"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td width="23%" valign="top"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	<td width="54%">
	  
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
		    	<td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
				<td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
				<td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
		    </tr>
		   	<tr>
				<td><img src="/assets/unmanaged/images/s.gif" width="500" height="10"></td>
			</tr>
			<tr>
		    	<td valign="top" align="left" class="greyText">
							<p><c:if test="${layoutPageBean.body1 != '' }">
								<p><content:getAttribute attribute="body1" beanName="layoutPageBean"/></p>
</c:if>
							
							<c:if test="${layoutPageBean.body2 != '' }">
								<p><content:getAttribute attribute="body2" beanName="layoutPageBean"/></p>
</c:if>
							
							<c:if test="${layoutPageBean.body3 != '' }">
								<p><content:getAttribute attribute="body3" beanName="layoutPageBean"/><br></p>
</c:if>
			    </td>
				<td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
			</tr>
			
			<tr>
				<td><a href="#Administration">Administration</a>&nbsp;&nbsp; | &nbsp;&nbsp;<a href="#Contract installation">Contract installation</a></a></td>
			</tr>
			<tr>
				<td><img src="/assets/unmanaged/images/s.gif" width="500" height="10"></td>
			</tr>
		    <tr>
		   		<td>
			   		<table width="509" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td  width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td  width="80"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td  width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td  width="341"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td  width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td  width="80"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td  width="4"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
							<td  width="1"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
						</tr>
						<tr class="tablehead">
							<td class="tableheadTD1" colspan="4"><b><a href="/do/resources/tpaGaForms/">Group Annuity</a> | John Hancock Retirement Select</b></td>
							<td colspan="4" class="tableheadTD" align="center"></td>
						</tr>
											
					<ps:tpaformsTable collection="tpaforms" layoutPage="layoutPageBean" adobe="adobeAcrobat" howTo="howToUse" typeOfForm="mf"/>
					</table>
		   </tr>
	    </table>
				 
		<p><content:pageFooter beanName="layoutPageBean"/></p><br><br>
		<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
		<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
	</td>
	<td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
</tr>
</table>

<%-- Prevent the creation of a session --%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>


<div id="errordivcs"><content:errors scope="session"/></div>
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
			    	<td valign="top" align="left" class="greyText">
								<p><c:if test="${layoutPageBean.body1!=''}">
									<p><content:getAttribute attribute="body1" beanName="layoutPageBean"/></p>
</c:if>
								
								<c:if test="${layoutPageBean.body2!=''}">
									<p><content:getAttribute attribute="body2" beanName="layoutPageBean"/></p>
</c:if>
								
								<c:if test="${layoutPageBean.body3!=''}">
									<p><content:getAttribute attribute="body3" beanName="layoutPageBean"/><br></p>
</c:if>
				    </td>
					<td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
					<td width="180"  valign="top" >
				       <center>
							<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
							<img src="/assets/unmanaged/images/s.gif" width="1" height="5">
							<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
						</center>
				    </td>
			   </tr>
		    </table>
					 
		    <p class="footnote"><content:getAttribute attribute="pageFooter" beanName="layoutPageBean"/></p>
			<p class="footnote"><content:getAttribute attribute="pageFootnotes" beanName="layoutPageBean"/></p><br>
			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/><br><br></p>
		</td>
		<td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	</tr>
</table>

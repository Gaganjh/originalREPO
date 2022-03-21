<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>

<%@ page import="com.manulife.pension.content.view.MutableUpdate"%>

<%-- get the content beans for updates (archived and current) --%>
<content:contentUpdates id="allArchivedUpdates" isArchived="true"/>
<content:contentUpdates id="allCurrentUpdates" isArchived="false"/>

<content:contentUpdatesByGroup id="currentUpdates" collection="allCurrentUpdates" groupId="<%=ContentConstants.TPA_UPDATES_GROUP%>"/>
<content:contentUpdatesByGroup id="archivedUpdates" collection="allArchivedUpdates" groupId="<%=ContentConstants.TPA_UPDATES_GROUP%>"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_NOCUR_MANULIFE_UPDATES%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningNoCurrentUpdates"/>

<content:errors scope="session"/>

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
          <td colspan="3" > 
		  <table width="500" border="0" cellspacing="0" cellpadding="0">
	<c:if test="${empty currentUpdates}">
			<tr>
			  	<td align="left"><b><content:getAttribute beanName="warningNoCurrentUpdates" attribute="text"/></b>
					<br><br>
			  	</td>
			</tr>
</c:if>
	
	<c:if test="${not empty currentUpdates}">
			<tr>
				<td align="left"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b><br><br></td>
			</tr>
			
<c:forEach items="${currentUpdates}" var="update" >
			<tr>
				<td class="boldGreyText">
<a name="X${update.id}"></a>
					<b><content:getAttribute id="update" attribute="title" /></b>
					<br>
					(<render:date property="update.date" patternOut="<%= RenderConstants.EXTRA_LONG_MDY%>"/>)
					<br><br>
				</td>
			</tr>
			
			<tr>
				<td class="greyText">
					<table width="100%" border="0" cellspacing="0" cellpadding="0" >
					  <tr>
					  	<td width="5%">
		    			  &nbsp;
		    			</td>
					  	<td width="95%">
		    			  <content:getAttribute id="update" attribute="text"/>
		    			</td>
		    	  	  </tr>
		    	    </table>
	  				<br><br>
				</td>
			</tr>
</c:forEach>

</c:if>
	<c:if test="${not empty archivedUpdates}">
			<tr>
			    <td align="left"><b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b><br><br></td>
			</tr>
			
<c:forEach items="${archivedUpdates}" var="archUpdate" >
			<tr>

				<td class="boldGreyText">
					<b><content:getAttribute id="archUpdate" attribute="teaser"/></b>
					&nbsp;
					(<render:date property="archUpdate.date" patternOut="<%= RenderConstants.EXTRA_LONG_MDY%>"/>)
<a href="/do/news/archivedTpaUpdates/#X${archUpdate.id}">
					more&gt;&gt;</a>
					<br><br>
				</td>

			</tr>
</c:forEach>
</c:if>
          	<tr>
          		<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          		<td valign="top"> <img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
        	</tr>
      	</table>
		<p><content:pageFooter beanName="layoutPageBean"/></p><br><br>
		<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 		<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 	</td>
    <td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	</tr>
  </table>

<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

					<TR>
					  <TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD class=name_column1 width=60><IMG height=1 src="/assets/unmanaged/images/s.gif" width=75></TD>
					  <TD width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD class=access_level_column1 width=250><IMG height=1 src="/assets/unmanaged/images/s.gif" width=330></TD>
					  <TD><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD width=150><IMG height=1 src="/assets/unmanaged/images/s.gif" width=76></TD>
					  <TD width=2><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>
					<TR class=tablehead>
					  <TD class=tableheadTD1 colSpan=8><SPAN class=tableheadTD><B>Contract Access</B></SPAN></TD>
					  <TD class=databorder width=2><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>
					<TR class=tablesubhead>
					  <TD class=databorder><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD vAlign=top width=75><B>Contract number </B></TD>
					  <TD class=datadivider vAlign=top><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD vAlign=top noWrap width=250><B>Contract name </B></TD>
					  <TD class=datadivider vAlign=top width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD width=150 vAlign=top><B>Role </B></TD>
					  <TD class=datadivider vAlign=top width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD vAlign=top width=50><B>Manage users</B></TD>
					  <TD class=databorder width=2><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>

<%
int rowIndex = 0;
%>
<c:forEach items="${registerForm.contractAccess}" var="contractAccess" varStatus="theIndex" >





				 <% if (rowIndex % 2 == 0) { %>
				<tr class="datacell1">
				<% } else { %> 
				<tr class="datacell2">
				 <% }
				 rowIndex++;
				 %> 

					  <TD class=databorder><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD vAlign=center width=60>${contractAccess.contractNumber}</TD>
					  <TD class=datadivider vAlign=center><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
<TD vAlign=center width=250>${contractAccess.companyName}</TD>
					  <TD class=datadivider vAlign=center width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
					  <TD width=150 vAlign=center>
<c:if test="${not empty contractAccess.planSponsorSiteRole}">
							  <ps:formatUserRole name="contractAccess" property="planSponsorSiteRole" />
<c:if test="${not empty contractAccess.roleType}">
(${contractAccess.roleType})
</c:if>
</c:if>
					  </TD>
					  <TD class=datadivider vAlign=center width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
                	  <TD vAlign=center width=150><render:yesno property="contractAccess.externalUserManager" defaultValue="no" style="l" /></TD>
					  <TD class=databorder vAlign=top width=2><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
			</tr>
</c:forEach>

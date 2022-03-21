<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.pension.service.security.valueobject.ContactInfo"%>

<c:set var="stagingContactsList" value="${stagingContactList}" scope="page" />


<c:set var="colspan_staging" value="9"/>		
<c:if test="${not empty param.printFriendly }" >

	<c:set var="colspan_staging" value="7"/>
</c:if>			
<tr class="datacell1">
	<td class="beigeborder2" colspan="13">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tbody>
				<tr>
					<td width="300" height="25"><hr size="1"></td>
					<td width="169" height="25"><div align="center"><b>INTERNAL USERS ONLY</b></div></td>
					<td width="300" height="25"><hr size="1"></td>
				</tr>
			</tbody>
		</table>
	</td>
</tr>

<tr>
	<td colspan="13">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tbody>
				<tr>
					<td colspan="${colspan_staging}" class="tableheadTD1">
						<B><content:getAttribute id="layoutPageBean" attribute="body2Header"/></B>
					</td>
				</tr>
						  
				<tr >
				<c:if test="${empty param.printFriendly }" >
					<td width="40" class="pgNumBack"><B>Action</B></td>
					<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				</c:if>
					<td width="142" class="pgNumBack"><b>Name</b></td>
					<td class="dataheaddivider" width="1">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="142" class="pgNumBack"><b>Title</b></td>
					<td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
					<td width="160" class="pgNumBack"><B>Phone </B></td>
					<td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
					<td width="85" class="pgNumBack"><B>Fax</B></td>
				</tr>
<c:forEach items="${stagingContactsList}" var="stagingContact" varStatus="stagingContactInd" >

<c:set var="stagingContactVar" value="${stagingContactInd.index}"/>

				<%String theIndex = pageContext.getAttribute("stagingContactVar").toString();
				  if (Integer.parseInt(theIndex) % 2 == 0) {  %>
				
				<tr class="datacell2">
				<% } else { %>
				<tr class="datacell1">
				<% } %> 
				<c:if test="${empty param.printFriendly }" >
					<td width="40" >
						<%-- Staging contact actions should be displayed for internal user with Manage External user permissions --%>
						<ps:permissionAccess permissions="EXMN">
						<table width="25" border="0" cellspacing="0" cellpadding="2">
							<tr>
								<td>
									<a href="javascript://" id="addStaggingLink${stagingContact.contactId}" onClick="return doAddContact(this, ${stagingContact.contactId});">
										<img src="/assets/unmanaged/images/aIcon.gif" alt="Add" title="Add" width="12" height="12"  border="0">
									</a>
								</td>
								<td>
									<a href="javascript://" id="removeStaggingLink${stagingContact.contactId}" onClick="return doDeleteStagingContact(this, ${stagingContact.contactId});">
										<img src="/assets/unmanaged/images/rIcon.gif" alt="Remove" title="Remove" width="12" height="12"  border="0">
									</a>
								</td>
							</tr>
						</table>
						</ps:permissionAccess>
					</td>
					<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				</c:if>					
					<td width="142" >
						<render:name firstName="${stagingContact.firstName}" lastName="${stagingContact.lastName}" style="f" defaultValue=""/>
					</td>
					<td width="1" class="greyborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="142" >
						${stagingContact.title}
					</td>
					<td width="1" class="greyborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="125" >
						<c:if test="${ not empty stagingContact.phone}">
							${stagingContact.phone.areaCode}-${stagingContact.phone.phonePrefix}-${stagingContact.phone.phoneSuffix}
							<c:if test="${not empty stagingContact.extension}" >
								<br/>ext. ${stagingContact.extension}
							</c:if>
						</c:if>
					</td>
					<td width="1" class="greyborder">
						<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
					</td>
					<td width="85" >
						<c:if test="${ not empty stagingContact.fax}">
							${stagingContact.fax.areaCode}-${stagingContact.fax.faxPrefix}-${stagingContact.fax.faxSuffix}
						</c:if>
					</td>
				</tr>
				<tr>
		  			<td class="datadivider" colspan="${colspan_staging}"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
				</tr>
</c:forEach>
			</tbody>
		</table>
	</td>
</tr>

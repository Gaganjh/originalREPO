<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>


 <%--- start key dates --%>
	<tr>
 	   <td valign="top">
       <table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr class="datacell1" valign="top" width="50%">
            	<td width="50%"><b>Contract effective</b></td>
             	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="50%"><b class="highlight">
                	<render:date property = "contractProfile.contractEffectiveDate" defaultValue = "" />
				</b></td>
            </tr>
            <tr class="datacell2" valign="top">
            	<td><b>Contract year end</b></td>
            	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             	<td><b class="highlight">
             		<render:date property = "contractProfile.contractYearEndDate" defaultValue = "" patternOut="MMMM dd"/>
             	</b></td>
            </tr>
<c:if test="${not empty contractProfile.guaranteedAccountTransferDates}">
            <tr class="datacell1" valign="top">
            	<td><b>Guaranteed account transfer dates<sup>1</b></sup></td>
            	<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             	<td><b class="highlight">
<c:forEach items="${contractProfile.guaranteedAccountTransferDates}" var="transferDate">
             			<render:date property = "transferDate" defaultValue = "" patternOut="MMMM dd"/><br>
</c:forEach>
                 </td>
            </tr>
</c:if>
            <tr>
            	<td colspan="3" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
          </table>
<%--- end key dates --%>


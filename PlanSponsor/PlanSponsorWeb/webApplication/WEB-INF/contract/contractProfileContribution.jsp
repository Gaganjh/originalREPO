<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>


 <%--- start contribution types --%>
	<tr class="tablesubhead">
		<td><b>Contribution types</b></td>
    </tr>
    <tr>
    	<td class="boxbody">
    	
    		<!---CMA managed -->
            <content:getAttribute beanName="layoutPageBean" attribute="body2"/>
    	
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
<c:forEach items="${contractProfile.contributionTypes}" var="contribution">
             	<tr >
<td width="70%"><ul class="noindent"><li> ${contribution.longName}</li></ul></td>
<td width="30%">${contribution.moneyType} </td>
             					
 				</tr>
</c:forEach>
		   </table>      
         </td>
     </tr>
<%--- end contribution types --%>

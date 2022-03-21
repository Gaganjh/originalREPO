<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="manulife/tags/content" prefix="content" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="4" bgcolor="#FFF9F2">
			<DIV class="nav_Main_display">
				<UL class="">
				<c:choose>
					<c:when test="${param['selectedTab'] == 'AccountsSummaryTab'}">
						<LI class="on">Summary</LI>
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/participant/participantForfeiture?fromPage=anotherTab">Forfeitures</a></LI>
					</c:when>
					
					<c:when test="${param['selectedTab'] == 'AccountsForfeituresTab'}">
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/participant/participantSummary?fromPage=anotherTab">Summary</a></LI>
						<LI class="on">Forfeitures</LI>
					</c:when>
					
					<c:when test="${param['selectedTab'] == 'AccountsSummaryPrintTab'}">
						<LI class="on">Summary</LI>
						<LI>Forfeitures</LI>
					</c:when>
					
					<c:when test="${param['selectedTab'] == 'AccountsForfeituresPrintTab'}">
						<LI>Summary</LI>
						<LI class="on">Forfeitures</LI>	
					</c:when>					
				</c:choose>
				</UL> 
			</DIV>
		</td>
	</tr>
</table>

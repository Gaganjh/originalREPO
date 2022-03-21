<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="manulife/tags/content" prefix="content" %>
<table width="700" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="4" bgcolor="#FFF9F2">
			<DIV class="nav_Main_display">
				<UL class="">
				<c:choose>
					<c:when test="${param['selectedTab'] == 'SubmitTab'}">
						<LI class="on">Submit</LI>
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/tools/secureDocumentUpload/history/">History</a></LI>
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/tools/secureDocumentUpload/view/">View</a></LI>						
					</c:when>
					<c:when test="${param['selectedTab'] == 'HistoryTab'}">
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/tools/secureDocumentUpload/submit/">Submit</a></LI>
						<LI class="on">History</LI>
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/tools/secureDocumentUpload/view/">View</a></LI>						
					</c:when>
					<c:when test="${param['selectedTab'] == 'ViewTab'}">
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/tools/secureDocumentUpload/submit/">Submit</a></LI>						
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/tools/secureDocumentUpload/history/">History</a></LI>
						<LI class="on">View</LI>
					</c:when>
					
					<c:when test="${param['selectedTab'] == 'SubmitPrintTab'}">
						<LI class="on">Submit</LI>
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">History</LI>
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">View</LI>						
					</c:when>					
					<c:when test="${param['selectedTab'] == 'HistoryPrintTab'}">
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">Submit</LI>
						<LI class="on">History</LI>						
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">View</LI>						
					</c:when>
					<c:when test="${param['selectedTab'] == 'ViewPrintTab'}">
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">Submit</LI>
						<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">History</LI>
						<LI class="on">View</LI>
					</c:when>
				</c:choose>
				</UL> 
			</DIV>
		</td>
	</tr>
</table>
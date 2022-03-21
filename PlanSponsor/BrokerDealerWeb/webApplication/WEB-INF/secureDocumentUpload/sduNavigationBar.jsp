<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="manulife/tags/content" prefix="content" %>
<div id="page_primary_nav" class="page_nav">	
	<ul>
		<li class="active"><em>Submit a Document</em></li>
	</ul>
</div>
<div id="page_primary_nav_footer"></div>
		<DIV id="page_secondary_nav">
			<UL class="">
			<c:choose>
				<c:when test="${param['selectedTab'] == 'SubmitTab'}">
					<LI class="active"><strong><a href="/do/bob/secureDocumentUpload/submit/">Submit a Document</a></strong></LI>
					<LI ><a href="/do/bob/secureDocumentUpload/history/">Submission History</a></LI>
					<LI ><a href="/do/bob/secureDocumentUpload/view/">View a Document</a></LI>						
				</c:when>
				<c:when test="${param['selectedTab'] == 'HistoryTab'}">
					<LI ><a href="/do/bob/secureDocumentUpload/submit/">Submit a Document</a></LI>
					<LI class="active"><strong><a href="/do/bob/secureDocumentUpload/history/">Submission History</a></strong></LI>
					<LI ><a href="/do/bob/secureDocumentUpload/view/">View a Document</a></LI>						
				</c:when>
				<c:when test="${param['selectedTab'] == 'ViewTab'}">
					<LI ><a href="/do/bob/secureDocumentUpload/submit/">Submit a Document</a></LI>						
					<LI ><a href="/do/bob/secureDocumentUpload/history/">Submission History</a></LI>
					<LI class="active"><strong><a href="/do/bob/secureDocumentUpload/view/">View a Document</a></strong></LI>
				</c:when>
				
				<c:when test="${param['selectedTab'] == 'SubmitPrintTab'}">
					<LI class="active">Submit a Document</LI>
					<LI >Submission History</LI>
					<LI >View a Document</LI>						
				</c:when>					
				<c:when test="${param['selectedTab'] == 'HistoryPrintTab'}">
					<LI >Submit a Document</LI>
					<LI class="active">Submission History</LI>						
					<LI >View a Document</LI>						
				</c:when>
				<c:when test="${param['selectedTab'] == 'ViewPrintTab'}">
					<LI >Submit a Document</LI>
					<LI >Submission History</LI>
					<LI class="active">View a Document</LI>
				</c:when>
			</c:choose>
			</UL> 
		</DIV>
 

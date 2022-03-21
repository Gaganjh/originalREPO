<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

       <%@page import="com.manulife.pension.ps.web.controller.UserProfile" %> 


<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
	<div id="n1" style="position:absolute; left:30px; top:100px; width:425px; height:25px; z-index:1; visibility: visible;" >
	  <a class="n1" href="#" onMouseOver="navOn(0);" onMouseOut="navOff(0);" id="Sec1" name="Sec1">
	    <b>TPA report view</b>
	  </a>
	  <span class="nPipe" id="pipe1">
	    <img src="/assets/unmanaged/images/white.gif" width="1" height="10" border="0" align="middle" hspace="0" name="pipe_img1">
	  </span>
	  <a class="n1" href="#" onMouseOver="navOn(1)" onMouseOut="navOff(1)" id="Sec2" name="Sec2">
	    <b>Toolkit</b>
	  </a>
	  <span class="nPipe" id="pipe2">
	    <img src="/assets/unmanaged/images/white.gif" width="1" height="10" border="0" align="middle" hspace="0" name="pipe_img2">
	  </span>
	  <a class="n1" href="#" onMouseOver="navOn(2)" onMouseOut="navOff(2)" id="Sec3" name="Sec3">
	    <b>Forms and investment options</b>
	  </a>
	  <span class="nPipe" id="pipe3">
	    <img src="/assets/unmanaged/images/white.gif" width="1" height="10" border="0" align="middle" hspace="0" name="pipe_img3">
	  </span>
	  <a class="n1" href="#" onMouseOver="navOn(3)" onMouseOut="navOff(3)" id="Sec4" name="Sec4">
	    <b>Updates</b>
	  </a>
	</div>
	
	<div id="n2Sec1" style="white-space: nowrap; position:absolute; left:25px; top:125px; width:730px; height:25px; z-index:2; visibility: hidden;" class="n2">
	  <span>
		<a href="/do/tpabob/tpaBlockOfBusinessHome/" id="Sub1Sec1" name="Sub1Sec1">TPA Block Of Business</a> 
	     | <a href="/do/tpadownload/eStatements/" id="Sub2Sec1" name="Sub2Sec1"> eStatements</a> 
	     | <a href="/do/tpadownload/tedCurrentFilesReport/" id="Sub3Sec1" name="Sub3Sec1">TPA e-download</a> 
	     | <ps:withdrawalLink contractId = '' linkType='<%=Constants.LINKTYPE_LIST_TPA%>' > 
	       <a href="/do/withdrawal/loanAndWithdrawalRequestsInit/" id="Sub4Sec1" name="Sub4Sec1">Loans and Withdrawals</a>
	       </ps:withdrawalLink> 
	     | <a href="/do/contract/pic/plansubmission/?id=refresh" id="Sub5Sec1" name="Sub5Sec1">Plan Information</a>
<c:if test="${userProfile.tpaFeeScheduleAccessAllowed ==true}">
         | <a href="/do/plandata/viewTpaPlanDataContractsView/" id="Sub6Sec1" name="Sub6Sec1">SEND Service</a>
        	|<a href="/do/feeSchedule/selectTpaFirm/" id="Sub7Sec1" name="Sub7Sec1">TPA fee schedule</a>        	
</c:if>
         	
	   </span>
	</div>
<c:if test="${applicationScope.environment.siteLocation == 'usa'}" >
	<div id="n2Sec2" style="position:absolute; left:130px; top:125px; width:500px; height:25px; z-index:2; visibility: hidden;" class="n2">
	  <span>
	    <a href="/do/tpatoolkit/tpaAdministrationGuide/" id="Sub1Sec2" name="Sub1Sec2">
	      Webcasts and Recordings</a> | <a href="/do/tpatoolkit/tpaAuditPackage/" id="Sub3Sec2" name="Sub3Sec2">
	      Audit package</a> |<a href="/do/tpatoolkit/tpaOtherGuideTools/" id="Sub4Sec2" name="Sub4Sec2">Other guides and tools</a>
	  </span>
	</div>
</c:if>
<c:if test="${applicationScope.environment.siteLocation != 'usa'}" >
	
	<div id="n2Sec2" style="position:absolute; left:130px; top:125px; width:500px; height:25px; z-index:2; visibility: hidden;" class="n2">
	  <span>
	    <a href="/do/tpatoolkit/tpaAdministrationGuide/" id="Sub1Sec2" name="Sub1Sec2">
	      Webcasts and Recordings</a> | <a href="/do/tpatoolkit/tpaAuditPackage/" id="Sub2Sec2" name="Sub2Sec2">
	      Audit package</a> |<a href="/do/tpatoolkit/tpaOtherGuideTools/" id="Sub3Sec2" name="Sub3Sec2">Other guides and tools</a>
	  </span>
	</div>
</c:if>
	<div id="n2Sec3" style="position:absolute; left:180px; top:125px; width:400px; height:25px; z-index:2; visibility: hidden;" class="n2">
	  <span>
	    <a href="/do/resources/tpaGaForms/" id="Sub1Sec3" name="Sub1Sec3">
	      Forms</a> | <a href="/do/fap/tpaFandp/" id="Sub2Sec3" name="Sub2Sec3">
	      Funds &amp; Performance</a>
	   </span>
	</div>
		  
<c:if test="${applicationScope.environment.siteLocation =='usa'}">
		<div id="n2Sec4" style="position:absolute; left:289px; top:125px; width:468px; height:25px; z-index:2; visibility: hidden;" class="n2">
		  <span>
		    <a href="/do/news/tpaForumNewsletter/" id="Sub1Sec4" name="Sub1Sec4">
		      TPA newsletter</a> | <a href="/do/news/currentTpaUpdates/" id="Sub2Sec4" name="Sub2Sec4">
		      <ps:companyName/> updates</a>  | <a href="/do/news/currentTpaLegislativeUpdates/" id="Sub3Sec4" name="Sub3Sec4">
		      Legislative updates</a> | <a href="/do/news/currentTpaIndustryUpdates/" id="Sub4Sec4" name="Sub4Sec4">
		      Industry updates</a>
		   </span>
		</div>
</c:if>
		  

<c:if test="${applicationScope.environment.siteLocation !='usa'}" >
		<div id="n2Sec4" style="position:absolute; left:361px; top:125px; width:400px; height:25px; z-index:2; visibility: hidden;" class="n2">
		  <span>
		    <a href="/do/news/currentTpaUpdates/" id="Sub1Sec4" name="Sub1Sec4">
		      <ps:companyName/> updates</a> | <a href="/do/news/currentTpaLegislativeUpdates/" id="Sub2Sec4" name="Sub2Sec4">
		      Legislative updates</a>| <a href="/do/news/currentTpaIndustryUpdates/" id="Sub3Sec4" name="Sub3Sec4">
		      Industry updates</a>
		   </span>
		</div>
</c:if>


<script type="text/javascript" >
init('${layoutBean.tpaMenu}','${layoutBean.tpaSubmenu}');
</script>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

        
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%@page import="com.manulife.pension.service.security.role.BundledGaCAR"%>
<div id="n1" style="position:absolute; left:30px; top:100px; width:473px; height:25px; z-index:1; visibility: visible;" >
	<a class="n1" href="#" onMouseOver="navOn(0);" onMouseOut="navOff(0);" id="Sec1" name="Sec1">
		<b>TPA report view</b>
	</a>
	
	<span class="nPipe" id="pipe1">
		<img src="/assets/unmanaged/images/white.gif" width="1" height="10" border="0" align="middle" hspace="0" name="pipe_img1">
	</span>
	<a class="n1" href="#" onMouseOver="navOn(1)" onMouseOut="navOff(1)" id="Sec2" name="Sec2">
		<b>Forms and investment options</b>
	</a>

	<a class="n1" href="#" id="Sec3" name="Sec3"></a>
	<a class="n1" href="#" id="Sec4" name="Sec4"></a>
</div>
	
<div id="n2Sec1" style="position:absolute; left:30px; top:125px; width:600px; height:25px; z-index:2; visibility: hidden;" class="n2">
	<span>
		<a href="/do/tpabob/tpaBlockOfBusinessHome/" id="Sub1Sec1" name="Sub1Sec1">TPA Block Of Business</a>
		| <a href="/do/contract/pic/plansubmission/?id=refresh" id="Sub2Sec1" name="Sub2Sec1">Plan Information</a>
<c:if test="${userProfile.tpaFeeScheduleAccessAllowed == true}">
		| <a href="/do/plandata/viewTpaPlanDataContractsView/" id="Sub3Sec1" name="Sub3Sec1">SEND Service</a>
        	|<a href="/do/feeSchedule/selectTpaFirm/" id="Sub4Sec1" name="Sub4Sec1">TPA fee schedule</a>
        	
</c:if>
	</span>
</div>
<div id="n2Sec2" style="position:absolute; left:135px; top:125px; width:400px; height:25px; z-index:2; visibility: hidden;" class="n2">
	<span>
		<% if(userProfile.getRole() instanceof BundledGaCAR ) {%>
			<a href="/do/resources/tpaGaForms/" id="Sub1Sec2" name="Sub1Sec2">Forms</a> |
		<%} else {%>
			<a href="javascript://" id="Sub1Sec2" name="Sub1Sec2"></a>
		<%} %>
		<a href="/do/fap/tpaFandp/" id="Sub2Sec2" name="Sub2Sec2">Funds &amp; Performance</a>
	</span>
</div>

<div id="n2Sec3" style="visibility: hidden;" class="n2"></div>		  
<div id="n2Sec4" style="visibility: hidden;" class="n2"></div>


<script type="text/javascript" >
init("${layoutBean.getParam('tpaInternalUserMenu')}","${layoutBean.getParam('tpaInternalUserSubMenu')}");
</script>

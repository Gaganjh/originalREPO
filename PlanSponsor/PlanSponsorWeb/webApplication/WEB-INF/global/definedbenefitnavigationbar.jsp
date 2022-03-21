<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>


<%
Environment environment = (Environment)session.getAttribute("environment");
pageContext.setAttribute("ENVIRONMENT_KEY",environment,PageContext.SESSION_SCOPE); 

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	
 %>

<c:if test="${userProfile.navBarFullAvailable ==true}">
<!--Full navigation menu-->
<div id="n1" style="position:absolute; left:25px; top:100px; width:365px; height:25px; z-index:1;"><a
       class="n1" href="#" onMouseOver="navOn(0);" onMouseOut="navOff(0);"
       id="Sec1" name="Sec1"><b>Your contract reports</b></a><span class="nPipe"
       id="pipe1"><img src="/assets/unmanaged/images/white.gif" width="1" height="10" border="0"
       align="middle" hspace="0" name="pipe_img1"></span><a class="n1"
       href="#" onMouseOver="navOn(1)"
       onMouseOut="navOff(1)" id="Sec2" name="Sec2"><b>Your resources</b></a><span class="nPipe"
       id="pipe2"><img src="/assets/unmanaged/images/white.gif" width="1" height="10" border="0"
       align="middle" hspace="0" name="pipe_img2"></span><a class="n1" href="#" onMouseOver="navOn(2)"
       onMouseOut="navOff(2)" id="Sec3" name="Sec3"><b>Getting help</b></a><span class="nPipe"
       id="pipe3"><img src="/assets/unmanaged/images/white.gif" width="1" height="10" border="0"
       align="middle" hspace="0" name="pipe_img3"></span><a class="n1" href="#" onMouseOver="navOn(3)"
       onMouseOut="navOff(3)" id="Sec4" name="Sec4"><b>News</b></a></div>

<div id="n2Sec1" style="position:absolute; left:30px; top:125px; width:700px; height:25px; z-index:2;visibility: hidden" class="n2">
  <span>
    <a href="/do/contract/contractDetails/" id="Sub1Sec1" name="Sub1Sec1">Contract details</a> | 
<!--    <a href="/do/participant/participantMenu" id="Sub2Sec1" name="Sub2Sec1">Employees</a> |  -->
    <a href="/do/transaction/transactionMenu/" id="Sub3Sec1" name="Sub3Sec1">Transactions</a> | 
    <a href="/do/investment/investments/" id="Sub4Sec1" name="Sub4Sec1">Investments</a>
  </span>
</div>

<div id="n2Sec2" style="position:absolute; left:163px; top:125px; width:562px; height:25px; z-index:2; visibility: hidden" class="n2">
  <span>
    <a href="/do/contentpages/userguide/landingpage/?parentId=97" id="Sub1Sec2" name="Sub1Sec2">Administration guide</a> | 
    <a href="/do/resources/forms/" id="Sub2Sec2" name="Sub2Sec2">Forms</a> | 
    <a href="/do/tools/toolsMenu/" id="Sub3Sec2" name="Sub3Sec2">Tools</a>
    	<ps:linkAccessible path="/do/fee/disclosure/">
	    	<c:set var="isRegulatoryDisclosureAvailable" value="false"/>
<c:if test="${userProfile.feeDisclsoureAccessAllowed ==true}">
	    		<c:set var="isRegulatoryDisclosureAvailable" value="true"/>
	    		 | <a href="/do/fee/disclosure/" id="Sub6Sec2" name="Sub6Sec2">Regulatory Disclosures</a> 
</c:if>
	    	<c:if test="${!isRegulatoryDisclosureAvailable}" >
<c:if test="${userProfile.participantDisclosureAllowed ==true}">
	    		 | <a href="/do/fee/disclosure/" id="Sub6Sec2" name="Sub6Sec2">Regulatory Disclosures</a>
</c:if>
	        </c:if> 	
	    </ps:linkAccessible>
<!--    <a href="/do/contentpages/userguide/landingpage/?parentId=75" id="Sub4Sec2" name="Sub4Sec2">Fiduciary resources</a> | -->
<!--    <a href="/do/resources/quarterlyInvestmentGuide/" id="Sub5Sec2" name="Sub5Sec2">Quarterly investment guide</a> -->
  </span>
</div>

<div id="n2Sec3" style="position:absolute; left:265px; top:125px; width:455px; height:25px; z-index:2; visibility: hidden" class="n2">
  <span>
    <a href="/do/contacts/?parm=contactUs" id="Sub1Sec3" name="Sub1Sec3">Contact us</a> |
    <a href="/do/contentpages/glossary" id="Sub2Sec3" name="Sub2Sec3">Glossary</a>
  </span>
</div>

<div id="n2Sec4" style="position:absolute; left:340px; top:125px; width:372px; height:25px; z-index:2; visibility: hidden" class="n2">
  <span>
    <a href="/do/news/currentUpdates/" id="Sub1Sec4" name="Sub1Sec4"><ps:companyName/> updates</a> | 
<c:if test="${ENVIRONMENT_KEY.CMANewsLetterAvailable ==true}" >
    	<a href="/do/news/newsletterIssueIndex" id="Sub2Sec4" name="Sub2Sec4">Newsletter</a> |
</c:if>
    <a href="/do/news/currentLegislativeUpdates/" id="Sub3Sec4" name="Sub3Sec4">Legislative updates</a>
  </span>
</div>
</c:if>


<c:if test="${userProfile.navBarFullAvailable !=true}">
<c:if test="${userProfile.role !='PYA'}">
<c:if test="${userProfile.currentContract.mta !=true}">
<c:if test="${userProfile.currentContract.contractAllocated !=false}">
<!--Partial navigation menu - CA contract and allocated -->
    <div id="n1"  style="position:absolute; left:30px; top:100px; width:500px; height:25px; z-index:1;" >
   	<a class="n1" href="#" id="Sec1" name="Sec1"></a>
    <a class="n1" href="#" onMouseOver="navOn(1)" onMouseOut="navOff(1)" id="Sec2" name="Sec2"><b>Your resources</b></a>
    <a class="n1" href="#" id="Sec3" name="Sec3"></a>
    <a class="n1" href="#" id="Sec4" name="Sec4"></a>
</div>

<div id="n2Sec1" style="position:absolute; left:30px; top:125px; width:700px; height:25px; z-index:2;visibility: hidden" class="n2">
</div>

<div id="n2Sec2" style="position:absolute; left:30px; top:125px; width:562px; height:25px; z-index:2; visibility: hidden" class="n2">
  <span>
    <a href="/do/contentpages/userguide/landingpage/?parentId=97" id="Sub1Sec2" name="Sub1Sec2">Administration guide</a> | 
    <a href="/do/resources/forms/" id="Sub2Sec2" name="Sub2Sec2">Forms</a> | 
    <a href="/do/tools/toolsMenu/" id="Sub3Sec2" name="Sub3Sec2">Tools</a>
    <ps:linkAccessible path="/do/fee/disclosure/">
    	<c:set var="isRegulatoryDisclosureAvailable" value="false"/>
<c:if test="${userProfile.feeDisclsoureAccessAllowed ==true}">
    		<c:set var="isRegulatoryDisclosureAvailable" value="true"/>
    		 | <a href="/do/fee/disclosure/" id="Sub6Sec2" name="Sub6Sec2">Regulatory Disclosures</a> 
</c:if>
    	<c:if test="${!isRegulatoryDisclosureAvailable}" >
<c:if test="${userProfile.participantDisclosureAllowed ==true}">
    		 | <a href="/do/fee/disclosure/" id="Sub6Sec2" name="Sub6Sec2">Regulatory Disclosures</a>
</c:if>
        </c:if> 	
    </ps:linkAccessible>
  </span>
</div>

<div id="n2Sec3" style="position:absolute; left:265px; top:125px; width:455px; height:25px; z-index:2; visibility: hidden" class="n2">
</div>
<div id="n2Sec4" style="position:absolute; left:340px; top:125px; width:372px; height:25px; z-index:2; visibility: hidden" class="n2">
</div>
</c:if>
</c:if>
</c:if>
</c:if>


<c:set var="dbMenu" value="${layoutBean.definedBenefitMenu}"/>
<c:set var="dbSubMenu" value="${layoutBean.definedBenefitSubmenu}"/>

<c:if test="${userProfile.currentContract.contractAllocated ==true}">

	<script type="text/javascript" >	
init('${dbMenu}','${dbSubMenu}');
	</script>

</c:if>

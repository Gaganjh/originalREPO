<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractSnapshotVO" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractAssetsByRiskAndParticipantStatusVO" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<jsp:useBean id="contractSnapshot" scope="request"  
	class="com.manulife.pension.service.contract.valueobject.ContractSnapshotVO" />


<c:set var="contractAssetsByRisk" value="${contractSnapshot.contractAssetsByRisk}" scope= "request"/>
<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}" scope = "page"/>
      

        <tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="4" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><b>Asset allocation by investment category</b></td>
                <td align="right">&nbsp;</td>
              </tr>
            </table>
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="4" valign="top" width="698" height="123">
          	<table border="0">
            <tr>
            <c:if test="${definedBenefit == true}">
              <td colspan="2"><b>Allocated Contract Assets</b></td>
	      <td>&nbsp;</td>	      
	</c:if>
		<c:if test="${definedBenefit != true}">
              <td colspan="2"><b>Allocated Contract Assets<sup>3</sup></b></td>
              <td><b>Asset allocation by age group</b></td>
</c:if>
             </tr>
             <tr>
             <td>
             <table border="0" cellpadding="0" cellspacing="2">
<c:if test="${contractSnapshotForm.displayLifecycle ==true}">
	    <tr valign="top">
		     <td>
			 <table border="0" cellpadding="0" cellspacing="0">
			   <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_LIFECYCLE %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
			 </table>
		     </td>
		     <td>Target Date</td>
 		     <td align="right"><render:number property = "contractAssetsByRisk.totalAssetsByRisk(LC)" type="c"/></td> 
              </tr>
</c:if>
              <tr valign="top">
               <td>
           	 <table border="0" cellpadding="0" cellspacing="0">
           	   <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_AGRESSIVE %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
           	 </table>
               </td>
               <td>Aggressive Growth</td>
               <td align="right"><render:number property="contractAssetsByRisk.totalAssetsByRisk(AG)" type="c"/></td> 
              </tr>
              <tr valign="top">
           	<td>
           	  <table border="0" cellpadding="0" cellspacing="0">
           	     <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_GROWTH %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
           	  </table>
           	</td>
           	<td>Growth</td>
           	<td align="right"><render:number property="contractAssetsByRisk.totalAssetsByRisk(GR)" type="c"/></td>
              </tr>
              <tr valign="top">
           	 <td>
           	   <table border="0" cellpadding="0" cellspacing="0">
           	      <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_GROWTH_INCOME %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
           	   </table>
           	 </td>
           	 <td>Growth & Income</td>
           	 <td align="right"><render:number property="contractAssetsByRisk.totalAssetsByRisk(GI)" type="c"/></td>
               </tr>
               <tr valign="top">
           	  <td>
           	    <table border="0" cellpadding="0" cellspacing="0">
           	       <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_INCOME %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
           	    </table>
           	  </td>
           	  <td>Income</td>
           	  <td align="right"><render:number property="contractAssetsByRisk.totalAssetsByRisk(IN)" type="c"/></td>
           	</tr>
           	<tr valign="top">
           	  <td>
           	    <table border="0" cellpadding="0" cellspacing="0">
           	       <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_CONSERVATIVE %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
           	    </table>
           	  </td>
           	  <td>Conservative</td>
           	  <td align="right"><render:number property="contractAssetsByRisk.totalAssetsByRisk(CN)" type="c"/></td>
           	</tr>

<c:if test="${contractSnapshotForm.displayPba ==true}">
           	<tr valign="top">
           	  <td>
           	        <table border="0" cellpadding="0" cellspacing="0">
           	          <tr><td height="11" width="11" style="background: <%= Constants.AssetAllocationPieChart.COLOR_PBA %>"><img src="/assets/unmanaged/images/s.gif" width="11" height="11"></td></tr>
           	        </table>
           	  </td>
           	  <td>Personal Brokerage Account</td>
           	  <td align="right"><render:number property="contractAssetsByRisk.totalAssetsByRisk(PB)" type="c"/></td>
           	</tr>
</c:if>

            </table>
            </td>
            <td>
                <ps:pieChart beanName="<%= Constants.CONTRACT_SNAPSHOT_RISK_PIECHART %>" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Allocation Chart"/>
            </td>
              <c:if test="${definedBenefit == true}">
            <td>&nbsp;</td>
</c:if>

        <c:if test="${definedBenefit != true}">
            <td>
            <table cellpadding="0" cellspacing="0" border="0">
                  <tr>
                    <td colspan="11" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  </tr>

                  <tr>
              
 <c:if test="${contractAssetsByRisk.getAgeGroup('<30').numberOfParticipants != 0}">
                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="center" valign="middle" height="80" width="82">
					  <ps:pieChart beanName="<%= Constants.CONTRACT_SNAPSHOT_AGE_BELOW_30_PIECHART %>" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Allocation Chart"/>
                      <br/>&lt;30 years old<br/>
${contractAssetsByRisk.getAgeGroup('<30').numberOfParticipants} Participant(s)
                    </td>
 </c:if> 

<c:if test="${contractAssetsByRisk.getAgeGroup('30-39').numberOfParticipants != 0}">
                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="center" valign="middle" height="80" width="82">
					  <ps:pieChart beanName="<%= Constants.CONTRACT_SNAPSHOT_AGE_30_39_PIECHART %>" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Allocation Chart"/>
                      <br/>30-39 years old<br/>
${contractAssetsByRisk.getAgeGroup('30-39').numberOfParticipants} Participant(s)
                    </td>
</c:if>


<c:if test="${contractAssetsByRisk.getAgeGroup('40-49').numberOfParticipants != 0}">
                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="center" valign="middle" height="80" width="82">
                      <ps:pieChart beanName="<%= Constants.CONTRACT_SNAPSHOT_AGE_40_49_PIECHART %>" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Allocation Chart"/>
                      <br/>40-49 years old<br/>
${contractAssetsByRisk.getAgeGroup('40-49').numberOfParticipants} Participant(s)
                    </td>
</c:if>

<c:if test="${contractAssetsByRisk.getAgeGroup('50-59').numberOfParticipants != 0}">
                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="center" valign="middle" height="80" width="82">
                      <ps:pieChart beanName="<%= Constants.CONTRACT_SNAPSHOT_AGE_50_59_PIECHART %>" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Allocation Chart"/>
                      <br/>50-59 years old<br/>
${contractAssetsByRisk.getAgeGroup('50-59').numberOfParticipants} Participant(s)
                    </td>
</c:if>
<c:if test="${contractAssetsByRisk.getAgeGroup('60+').numberOfParticipants != 0}">
                    <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    <td align="center" valign="middle" height="80" width="82">
                      <ps:pieChart beanName="<%= Constants.CONTRACT_SNAPSHOT_AGE_60_ABOVE_PIECHART %>" alt="If you have trouble seeing this chart image, please try again later.  If the problem persists, please contact your Client Account Representative." title="Asset Allocation Chart"/>
                      <br/>60+ years old<br/>
${contractAssetsByRisk.getAgeGroup('60+').numberOfParticipants} Participant(s)
                    </td>
</c:if>


                    <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  </tr>

                  <tr>
                    <td colspan="11" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                  </tr>

                 <tr>
                <p>
                <c:if test="${contractAssetsByRisk.getNumberOfDefaultBirthDateParticipants() > 0}">
                  <td colspan=10>
	                  Allocations by age group will not be accurate if complete and correct birth dates have not been provided.
	                  In these instances, default birth dates are assumed.
	                  <c:if test="${contractAssetsByRisk.getAgeGroup('DEFAULT1980').numberOfParticipants > 0}">
Your contract currently has ${contractAssetsByRisk.getAgeGroup('DEFAULT1980').numberOfParticipants}
	 	                  participant(s) with a default birth date of January 1, 1980.
	 		              </c:if>
                  </td>
                </c:if>
                </p>
                </tr>

             </table>
            </td>
</c:if>

              </td>
            </table>
            <p></p>
            
          <c:if test="${definedBenefit != true}"> 
            <p><a href="/do/investment/investmentAllocationReport/">Go to investment allocation report</a></p>
            <p>&nbsp;</p>
</c:if>
           
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

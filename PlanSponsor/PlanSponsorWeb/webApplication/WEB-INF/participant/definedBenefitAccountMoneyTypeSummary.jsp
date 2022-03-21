<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantFundMoneyTypeTotalsVO" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

   <!-- Inlcude common -->  

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
   <jsp:include page="/WEB-INF/participant/definedBenefitAccountCommon.jsp" flush="true" />  
  
<c:if test="${participantAccountForm.asOfDateCurrent ==true}">
     <c:if test="${empty requestScope.errors}">
		<script languange="javascript">   
		// This function is implementedto be executed during onLoad.
		// PPR. 
		function doOnload() {
		  //var tabs = document.getElementById("tabs");
		  //alert("tabs="+tabs.style.top);
		  scroll(0,450);
		}
		</script>   
     
        <% int style=1; %> 
        
        <tr>
          <td colspan="18" height="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>


        <tr>
		 <ps:isInternalOrTpa name ="userProfile" property ="principal.role">
          <td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${participantAccountForm.showNetContribEarnings ==true}">
				<!--if after tax money - display the after tax money tab-->            
				<c:if test="${empty param.printFriendly }" >          
            		<td colspan="16" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccount/');"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeSummary/');"></a><img src="/assets/unmanaged/images/money_type_summary_2.gif" width="118" height="30"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeDetails/');"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></a></td>
          		</c:if>
          		<c:if test="${not empty param.printFriendly }" >  
            		<td colspan="16" valign="bottom" class="box"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_2.gif" width="118" height="30"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></td>          
          		</c:if>
</c:if>
<c:if test="${participantAccountForm.showNetContribEarnings ==false}">
				<!--if no after tax money - suppress the after tax money tab-->
				<c:if test="${empty param.printFriendly }" >          
            		<td colspan="16" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccount/');"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeSummary/');"></a><img src="/assets/unmanaged/images/money_type_summary_2.gif" width="118" height="30"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeDetails/');"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></a></td>
          		</c:if>
          		<c:if test="${not empty param.printFriendly }" >  
            		<td colspan="16" valign="bottom" class="box"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_2.gif" width="118" height="30"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></td>          
          		</c:if>
</c:if>
          <td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
       </ps:isInternalOrTpa>
	   
	   <ps:isNotInternalOrTpa name ="userProfile" property ="principal.role">
	   	<td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${participantAccountForm.showNetContribEarnings ==true}">
				<!--if after tax money - display the after tax money tab-->  	
				<c:if test="${empty param.printFriendly }" >          
	        		<td colspan="16" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccount/');"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeSummary/');"></a><img src="/assets/unmanaged/images/money_type_summary_2.gif" width="118" height="30"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeDetails/');"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountNetContribEarnings/');"><img src="/assets/unmanaged/images/after_tax_tab_off.gif" width="118" height="30" border="0"></a></td>
	        	</c:if>
	        	<c:if test="${not empty param.printFriendly }" >  
	        		<td colspan="16" valign="bottom" class="box"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_2.gif" width="118" height="30"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/after_tax_tab_off.gif" width="118" height="30" border="0"></td>          
	        	</c:if>
</c:if>
<c:if test="${participantAccountForm.showNetContribEarnings ==false}">
				<!--if no after tax money - suppress the after tax money tab-->	
				<c:if test="${empty param.printFriendly }" >          
	        		<td colspan="16" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccount/');"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeSummary/');"></a><img src="/assets/unmanaged/images/money_type_summary_2.gif" width="118" height="30"></a><a href="javascript:selectMoneyReport('/do/db/definedBenefitAccountMoneyTypeDetails/');"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></a></td>
	        	</c:if>
	        	<c:if test="${not empty param.printFriendly }" >  
	        		<td colspan="16" valign="bottom" class="box"><img src="/assets/unmanaged/images/defined_benefit_off.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_2.gif" width="118" height="30"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></td>          
	        	</c:if>
</c:if>
	   	<td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
       </ps:isNotInternalOrTpa>
	   
	   </tr>  



        <tr>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="209" colspan="4" valign="bottom" class="tablesubhead"><b>Money types</b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="1" valign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <%-- Requirement PPR.254 --%>
<c:if test="${details.showLoanFeature =='YES'}">
		    <td width="244" colspan="5" align="right" valign="bottom" class="tablesubhead"><b>Total assets (which will exclude loans($))</b></td>    	  
            <td width="1" align="right" valign="bottom" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>  	    
            <td width="244" colspan="5" align="right" valign="bottom" class="tablesubhead"><b>Loan assets($)</b></td>
</c:if>
<c:if test="${details.showLoanFeature !='YES'}">
		    <td width="244" colspan="5" align="right" valign="bottom" class="tablesubhead">&nbsp;</td>    	  
            <td width="244" colspan="6" align="right" valign="bottom" class="tablesubhead"><b>Total assets($)</b></td>
</c:if>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

<c:if test="${participantAccountForm.hasInvestments ==false}">
 <tr>		
   <td class="databorder" width="1"></td>
   <td colspan="16">
    <content:getAttribute id="NoParticipantsMessage" attribute="text"/>
   </td>
   <td class="databorder" width="1"></td>
 </tr>
</c:if>

<c:if test="${participantAccountForm.hasInvestments ==true}">
<% if(style==1) style=2;else style=1; %>
        <tr>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="209" colspan="4" class="datacell<%=style%>">&nbsp;</td>
          <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${details.showLoanFeature =='YES'}">
            <td width="244" align="right" colspan="5" class="datacell<%=style%>">&nbsp;</td>
            <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="244" align="right" colspan="5" class="datacell<%=style%>">
  			   &nbsp;             
            </td>
</c:if>
<c:if test="${details.showLoanFeature !='YES'}">
		    <td width="244" colspan="5" align="right" class="datacell<%=style%>">&nbsp;</td>    	  
            <td width="245" colspan="6" align="right" class="datacell<%=style%>">&nbsp;</td>
</c:if>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

<% if(style==1) style=2;else style=1; %>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="4" class="datacell<%=style%>"><b>Employer contributions<img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${details.showLoanFeature =='YES'}">
            <td align="right" colspan="5" class="datacell<%=style%>">
  			  <render:number property="account.totalEmployerContributionsAssets" defaultValue = ""  pattern="###,###,##0.00"/>
            </td>
            <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="right" colspan="5" class="datacell<%=style%>">
  			  <render:number property="account.totalEmployerContributionsLoanAssets" defaultValue = ""  pattern="###,###,##0.00"/>              
            </td>
</c:if>
<c:if test="${details.showLoanFeature !='YES'}">
		    <td colspan="5" align="right" class="datacell<%=style%>">&nbsp;</td>    	  
            <td colspan="6" align="right" class="datacell<%=style%>">
              <render:number property="account.totalEmployerContributionsAssets" defaultValue = ""  pattern="###,###,##0.00"/>
  			</td>
</c:if>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>    
  
<c:set var="theEmployerMoneyTypeTotals" value="${account.employerMoneyTypeTotals}"/>

<c:set var= "emloyerMoneyTypeSize" value="${account.employerMoneyTypeTotals}"/>
<c:if test="${ employerMoneyTypeSize!=0}">
<c:forEach items="${theEmployerMoneyTypeTotals}" var="employerMoneyTypeTotals" >
  <% if(style==1) style=2;else style=1; %>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="4" class="datacell<%=style%>">
<span class="highlight">${employerMoneyTypeTotals.moneyTypeName}</span>
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${details.showLoanFeature =='YES'}">
            <td align="right" colspan="5" class="datacell<%=style%>">
              <span class="highlight">
                <render:number property="employerMoneyTypeTotals.balance" defaultValue = ""  pattern="###,###,##0.00"/>
              </span>  
            </td>
            <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="right" colspan="5" class="datacell<%=style%>">
              <span class="highlight">
                <render:number property="employerMoneyTypeTotals.loanBalance" defaultValue = ""  pattern="###,###,##0.00"/>
              </span>
            </td>
</c:if>
<c:if test="${details.showLoanFeature !='YES'}">
		    <td colspan="5" align="right" class="datacell<%=style%>">&nbsp;</td>    	  
            <td colspan="6" align="right" class="datacell<%=style%>">
              <span class="highlight">  
                <render:number property="employerMoneyTypeTotals.balance" defaultValue = ""  pattern="###,###,##0.00"/>
              </span>
  			</td>
</c:if>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>     
        </tr>     
</c:forEach>
</c:if>

<% if(style==1) style=2;else style=1; %>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="4" class="datacell<%=style%>">
            &nbsp;
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${details.showLoanFeature =='YES'}">
            <td align="right" colspan="5" class="datacell<%=style%>">
  			   &nbsp;
            </td>
            <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="right" colspan="5" class="datacell<%=style%>">
  			   &nbsp;             
            </td>
</c:if>
<c:if test="${details.showLoanFeature !='YES'}">
		    <td colspan="5" align="right" class="datacell<%=style%>">&nbsp;</td>    	  
            <td colspan="6" align="right" class="datacell<%=style%>">
  			  &nbsp;
  			</td>
</c:if>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

<% if(style==1) style=2;else style=1; %>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="4" class="datacell<%=style%>"><b>Total<img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${details.showLoanFeature =='YES'}">
            <td align="right" colspan="5" class="datacell<%=style%>">
  			  <render:number property="account.totalContributionsAssets" defaultValue = ""  pattern="###,###,##0.00"/>
            </td>
            <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="right" colspan="5" class="datacell<%=style%>">
  			  <render:number property="account.totalContributionsLoanAssets" defaultValue = ""  pattern="###,###,##0.00"/>              
            </td>
</c:if>
<c:if test="${details.showLoanFeature !='YES'}">
		    <td colspan="5" align="right" class="datacell<%=style%>">&nbsp;</td>    	  
            <td colspan="6" align="right" class="datacell<%=style%>">
  			  <render:number property="account.totalContributionsAssets" defaultValue = ""  pattern="###,###,##0.00"/>
  			</td>
</c:if>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>   

</c:if>

        <tr>
          <td class="databorder" height="1"></td>
          <td width="24%" height="1" colspan="4" valign="bottom" class="datacell<%=style%>"></td>
          <td width="1" height="1" valign="bottom" class="datadivider"></td>

          <td width="80" height="1" align="right" valign="bottom" class="datacell<%=style%>"></td>
          <td width="1" height="1" align="right" valign="bottom" class="datacell<%=style%>"></td>
          <td width="70" height="1" align="right" valign="bottom" class="datacell<%=style%>"></td>
          <td width="1" height="1" align="right" valign="bottom" class="datacell<%=style%>"></td>
          <td width="102" height="1" align="right" valign="bottom" class="datacell<%=style%>"></td>

<c:if test="${details.showLoanFeature =='YES'}">
            <td width="1" height="1" align="right" valign="bottom" class="datadivider"></td>
            <td colspan="5" width="244" align="right" valign="bottom" class="datacell<%=style%>"></td>	  
</c:if>

<c:if test="${details.showLoanFeature !='YES'}">
            <td colspan="6" width="244" align="right" valign="bottom" class="datacell<%=style%>"></td>	  
</c:if>
          
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        
        <tr>
          <td class="databorder" colspan="18"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table>
      <br>
    </td>
    <td><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	</tr>
    <tr>
	   		<td width="1%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
	   		<td >
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote">
 					<content:pageFootnotes beanName="layoutPageBean"/><br>
<c:if test="${participantAccountForm.showPba ==true}">
						<content:getAttribute beanName="footnotePBA" attribute="text"/><br>
</c:if>
				</p>
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
	   		<td  ></td> 			
	</tr>
</table>
<c:if test="${not empty param.printFriendly }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="5%">&nbsp;</td>
			<td width="95%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
</c:if>
</c:if>
<c:if test="${not empty requestScope.errors}">
	<table width="715" border="0" cellspacing="0" cellpadding="0">
	<tr>	
		<td>
		&nbsp;
		</td>
	</tr>
	<tr>
		<td> 
		  <content:errors scope="session"/>
		</td>
	</tr>
	<tr>	
		<td>
		&nbsp;
		</td>
	</tr>
	</table>
</c:if>

   

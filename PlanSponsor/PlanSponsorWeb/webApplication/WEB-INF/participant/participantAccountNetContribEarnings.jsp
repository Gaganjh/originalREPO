<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.ps.service.participant.valueobject.ParticipantNetContribEarningsVO"%>

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
   <jsp:include page="/WEB-INF/participant/participantAccountCommon.jsp" flush="true" /> 


	<content:contentBean contentId="<%=ContentConstants.PS_PARTICIPANT_ACCOUNT_EARNINGS_FOOTNOTE%>"
                           	type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>"
                          	id="footnoteEarnings"/>
                          	
   <content:contentBean contentId="<%=ContentConstants.PS_SHOW_NONROTH_HEADER%>" 
					 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="showNonRothHeadertext" />
					 
   <content:contentBean contentId="<%=ContentConstants.PS_SHOW_ROTH_HEADER%>" 
					 type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
					 id="showRothHeadertext" />
					 
	<content:contentBean contentId="<%=ContentConstants.PS_MULTIPLE_ROTH_FOOTNOTES%>" 
					 type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>" 
					 id="multipleRoth" />
					 
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
          <td height="1" colspan="1" rowspan="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="1" colspan="4" rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="1" colspan="1" rowspan="1" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="10" colspan="5" rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="1" colspan="1" rowspan="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="1" colspan="5" rowspan="1" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td height="1" colspan="1" rowspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>        

        <tr>
          <td colspan="18" height="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>


        <tr>
		 
		<ps:isInternalOrTpa name ="userProfile" property ="principal.role">
		<!--display Net EE Deferrals tab-->
          <td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          		<c:if test="${empty param.printFriendly }" >          
            		<td colspan="16" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccount/');"><img src="/assets/unmanaged/images/participant_account_1.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeSummary/');"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeDetails/');"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountNetContribEarnings/');"></a><img src="/assets/unmanaged/images/after_tax_tab_on.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountNetDeferral/');"><img src="/assets/unmanaged/images/net_EE_deferrals_off.gif" width="118" height="30" border="0"></a></td>
          		</c:if>
          		<c:if test="${not empty param.printFriendly }" >  
            		<td colspan="16" valign="bottom" class="box"><img src="/assets/unmanaged/images/participant_account_1.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/after_tax_tab_on.gif" width="118" height="30" border="0"></a><img src="/assets/unmanaged/images/net_EE_deferrals_off.gif" width="118" height="30"></td>          
          		</c:if>
          <td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
       </ps:isInternalOrTpa>
	   
	   <ps:isNotInternalOrTpa name ="userProfile" property ="principal.role">
		<!--suppress Net EE Deferrals tab-->
		<td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	             <c:if test="${empty param.printFriendly }" >          
	               <td colspan="16" valign="bottom" class="box"><a name="columns"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccount/');"><img src="/assets/unmanaged/images/participant_account_1.gif" width="117" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeSummary/');"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountMoneyTypeDetails/');"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"></a><a href="javascript:selectMoneyReport('/do/participant/participantAccountNetContribEarnings/');"></a><img src="/assets/unmanaged/images/after_tax_tab_on.gif" width="118" height="30" border="0"></a></td>
	             </c:if>
	             <c:if test="${not empty param.printFriendly }" >  
	               <td colspan="16" valign="bottom" class="box"><img src="/assets/unmanaged/images/participant_account_1.gif" width="117" height="30" border="0"><img src="/assets/unmanaged/images/money_type_summary_1.gif" width="118" height="30"><img src="/assets/unmanaged/images/money_type_details_1.gif" width="118" height="30" border="0"><img src="/assets/unmanaged/images/after_tax_tab_on.gif" width="118" height="30" border="0"></a></td>          
	             </c:if>
		<td height="30" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
       </ps:isNotInternalOrTpa>
	   
	   </tr>  






        <tr>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="209" colspan="4" valign="bottom" class="tablesubhead"><b>After tax money types</b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="1" valign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
     	  <td width="244" colspan="5" align="right" valign="bottom" class="tablesubhead"><b>Net contributions($)</b></td>    	  
          <td width="1" align="right" valign="bottom" class="dataheaddivider"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>  	    
          <td width="244" colspan="5" align="right" valign="bottom" class="tablesubhead"><b>Earnings*($)</b></td>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>


<c:set var="theNetContribEarningsDetails" value="${account.netContribEarningsDetailsCollection}"/>
<c:if test="${participantAccountForm.showNonRothHeader ==true}">
  <% if(style==1) style=2;else style=1; %>
		<tr><td class="dataheaddivider" colspan="18"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"> </td></tr>
		<tr>
  		<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  		<td colspan="16" class="tablesubhead" align="left" width="244">
		<content:getAttribute beanName="showNonRothHeadertext" attribute="text"/>
		</td><td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>    
		</tr>
		<tr><td class="dataheaddivider" colspan="18"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"> </td></tr>
		
		
<c:forEach items="${theNetContribEarningsDetails}" var="theItem" >
<c:if test="${theItem.nonRothMoneyTypeInd ==true}">
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="4" class="datacell<%=style%>">
<span class="highlight">${theItem.moneyTypeName}</span>
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" colspan="5" class="datacell<%=style%>">
             <span class="highlight">
				<render:number property="theItem.netContributions" defaultValue = ""  pattern="###,###,##0.00;(###,###,##0.00)"/>
			</span>  
            </td>
            <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="right" colspan="5" class="datacell<%=style%>">
              <span class="highlight">
					<render:number property="theItem.earnings" defaultValue = ""  pattern="###,###,##0.00;(###,###,##0.00)"/>
              </span>
            </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>     
        </tr>  
</c:if>
</c:forEach>
</c:if>
  
<c:if test="${participantAccountForm.showRothHeader ==true}">
  <% if(style==1) style=2;else style=2; %>
		
		<tr><td class="dataheaddivider" colspan="18"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"> </td></tr>
		<tr>
  		<td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  		<td colspan="16" class="tablesubhead" align="left" width="244">
		<content:getAttribute beanName="showRothHeadertext" attribute="text"/>
		</td><td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>    
		</tr>
  		
<c:forEach items="${theNetContribEarningsDetails}" var="theItem" >
<c:if test="${theItem.rothMoneyTypeInd ==true}">
        <tr><td class="dataheaddivider" colspan="18"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"> </td></tr>
        <tr>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="4" class="datacell<%=style%>">
<span class="highlight">${theItem.moneyTypeName}</span>
          </td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right" colspan="5" class="datacell<%=style%>">
             <span class="highlight">
				<render:number property="theItem.netContributions" defaultValue = ""  pattern="###,###,##0.00;(###,###,##0.00)"/>
			</span>  
            </td>
            <td width="1" align="right" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td align="right" colspan="5" class="datacell<%=style%>">
              <span class="highlight">
					<render:number property="theItem.earnings" defaultValue = ""  pattern="###,###,##0.00;(###,###,##0.00)"/>
              </span>
            </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>     
        </tr> 
</c:if>
</c:forEach>
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

        
            <td width="1" height="1" align="right" valign="bottom" class="datadivider"></td>
            <td colspan="5" width="244" align="right" valign="bottom" class="datacell<%=style%>"></td>	  

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
	   			<c:if test="${participantAccountForm.asOfDateCurrent and participantAccountForm.showManagedAccount }">
	               <content:contentBean contentId="<%=ContentConstants.MA_FOOTNOTE%>"
					                    type="<%=ContentConstants.TYPE_DISCLAIMER%>"
					                    id="participantMAFootnote" />
		           <p class="footnote"><content:getAttribute id="participantMAFootnote" attribute="text" /></p>
                </c:if>
	   			<p class="footnote">
<c:if test="${participantAccountForm.showMultileRothFootnote ==true}">
						<content:getAttribute beanName="multipleRoth" attribute="text"/><br>
</c:if>
				</p>
				<p class="footnote">
<c:if test="${participantAccountForm.showNetContribEarnings ==true}">
						<content:getAttribute beanName="footnoteEarnings" attribute="text"/><br>
</c:if>
				</p>				
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

   

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_FUNDCHECK_INVESTMENT_MENU_LINK_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="fundCheckLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_FUNDCHECK_INVESTMENT_MENU_DESC_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="fundCheckDesc"/>                     

<content:contentBean contentId="<%=ContentConstants.IPS_ASSIST_SERVICE_LINK_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="ipsAssisServiceLinkText"/>

<content:contentBean contentId="<%=ContentConstants.IPS_ASSIST_SERVICE_DESC%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="ipsAssisServiceDesc"/>
                     
<content:contentBean contentId="<%=ContentConstants.COFID321_ASSIST_SERVICE_LINK_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="cofid321QtrlyReviewAssisServiceLinkText"/>
                     
<content:contentBean contentId="<%=ContentConstants.COFID321_ASSIST_SERVICE_DESC%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="cofid321QtrlyReviewAssisServiceDesc"/> 
<content:contentBean contentId="<%=ContentConstants.COFID_FUND_RECOMMENDATION_REVIEW_LINT_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="cofidFundRecommendationReviewLinkText"/> 
<content:contentBean contentId="<%=ContentConstants.COFID_FUND_RECOMMENDATION_REVIEW_DESC_TEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="cofidFundRecommendationReviewLinkDesc"/> 
 
                     

<td width="23%" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>
      <img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    <td width="54%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
          <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
        </tr>
        <tr>
          <td >
		  <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
		  <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
		  <img src='<content:pageImage type="pageTitle" beanName="layoutPageBean"/>' alt="Investments" ><br>
		    <table width="500" border="0" cellspacing="0" cellpadding="0">
			  <tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td></tr>
              <tr>
				<td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				<td width="495" valign="top">
					<c:if test="${layoutPageBean.introduction1 !=' '}">	   
  
  <content:getAttribute attribute="introduction1" beanName="layoutPageBean"/>
</c:if>
					<br>
					<p><content:errors scope="session"/></p>
				    <content:getAttribute attribute="introduction2" beanName="layoutPageBean"/>
				  <br>
                  <table width="495" border="0">
		<ps:linkAccessible path="/do/investment/contractFundsReport/">
				  <tr>
				    <td width="12" valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
				      <td width="128" valign="top">
				      	<content:getAttribute beanName="layoutPageBean" attribute="body1Header">
				      		<content:param>/do/investment/contractFundsReport/</content:param>	
				      	</content:getAttribute>
				      </td>
					<td width="20" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				      <td width="350" valign="top">				    <content:getAttribute attribute="body1" beanName="layoutPageBean"/></td>
				    </tr>
		</ps:linkAccessible>
		
		<ps:linkAccessible path="/do/investment/investmentAllocationReport/">
					<tr><td colspan="4" height="20"></td></tr>
				  <tr>
				    <td width="12" valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
				      <td width="128" valign="top">
				      	<content:getAttribute beanName="layoutPageBean" attribute="body2Header">
				      		<content:param>/do/investment/investmentAllocationReport/</content:param>	
				      	</content:getAttribute>

					  </td>
					<td width="20" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				      <td width="350" valign="top"><content:getAttribute attribute="body2" beanName="layoutPageBean"/></td>
                      </td>
				  </tr>
   				  <tr><td colspan="4" height="20"></td></tr>
		</ps:linkAccessible>
		<ps:linkAccessible path="/do/fundCheck">
				  <tr>
				    <td width="12" valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
				      <td width="128" valign="top">
				      	<content:getAttribute id="fundCheckLinkText" attribute="text">
				      		<content:param>/do/fundCheck</content:param>	
				      	</content:getAttribute>
				      </td>
					  <td width="20" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				      <td width="350" valign="top"><content:getAttribute id="fundCheckDesc" attribute="text"/></td>
				    </tr>
				    <tr><td colspan="4" height="20"></td></tr>
		</ps:linkAccessible>

	
		<c:if test="${isIPSAvailable == true}">
			<ps:linkAccessible path="/do/investment/ipsManager/">
				  <tr>
				    <td width="12" valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
				      <td width="128" valign="top">
				      	<content:getAttribute id="ipsAssisServiceLinkText" attribute="text">
				      		<content:param>/do/investment/ipsManager/</content:param>	
				      	</content:getAttribute>
				      </td>
					  <td width="20" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				      <td width="350" valign="top"><content:getAttribute id="ipsAssisServiceDesc" attribute="text"/></td>
				    </tr>
				    <tr><td colspan="4" height="20"></td></tr>
			</ps:linkAccessible>
</c:if>
		<c:if test="${isServiceProviderHasDocuemnts == true}">
		
		<ps:linkAccessible path="/do/investment/cofidQtrlyReview/">
				  <tr>
				    <td width="12" valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
				      <td width="128" valign="top">
				      	<content:getAttribute id="cofid321QtrlyReviewAssisServiceLinkText" attribute="text">
				      		<content:param>/do/investment/cofidQtrlyReview/</content:param>	
				      	</content:getAttribute>
				      </td>
					  <td width="20" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				      <td width="350" valign="top"><content:getAttribute id="cofid321QtrlyReviewAssisServiceDesc" attribute="text"/></td>
				    </tr>
				    <tr><td colspan="4" height="20"></td></tr>
		</ps:linkAccessible>
		</c:if>
		<c:if test="${isCoFiduciary==true}">
		<c:if test="${(isAutoexecuteOnAndFundRecommendationAvailable == 'true')}">
			<ps:linkAccessible path="/do/investment/coFiduciaryFundRecommendationReview/">
					<tr><td colspan="4" height="20"></td></tr>
				  <tr>
				    <td width="12" valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
				      <td width="128" valign="top">
				      	
				      	<content:getAttribute id="cofidFundRecommendationReviewLinkText" attribute="text">
				      		<content:param>/do/investment/coFiduciaryFundRecommendationReview/</content:param>	
				      	</content:getAttribute>

					  </td>
					<td width="20" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				      <td width="350" valign="top"><content:getAttribute id="cofidFundRecommendationReviewLinkDesc" attribute="text"/></td>
                      
				  </tr>
   				  <tr><td colspan="4" height="20"></td></tr>
		</ps:linkAccessible>
	 </c:if> 
</c:if>
		<ps:linkAccessible path="/do/investment/performanceChartInput/">
				  <tr>
				    <td width="12" valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
				      <td width="128" valign="top">
				      	<content:getAttribute beanName="layoutPageBean" attribute="body3Header">
				      		<content:param>/do/investment/performanceChartInput/</content:param>	
				      	</content:getAttribute>
				      </td>
					  <td width="20" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
				      <td width="350" valign="top"><content:getAttribute attribute="body3" beanName="layoutPageBean"/></td>
				    </tr>
		</ps:linkAccessible>
		
			    </table></td>
			  </tr>
			</table>

          </td>
          <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          <td valign="top">
		  <img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
            <table width="180" border="0" cellspacing="0" cellpadding="0" class="box">
              <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="178"><img src="/assets/unmanaged/images/s.gif" width="178" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              </tr>
              <tr>
                <td colspan="3">
                  <table width="180" border="0" cellspacing="0" cellpadding="0" class="box">
                    <tr>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td width="178"><img src="/assets/unmanaged/images/s.gif" width="178" height="1"></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
            <img src="/assets/unmanaged/images/s.gif" width="1" height="5"><br>
          </td>
        </tr>
        <tr>
           	<td colspan="3">
				<br><br>
 			</td>
		</tr>
        <tr>
           	<td colspan="3">
				<p><content:pageFooter beanName="layoutPageBean"/></p>
				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
		</tr>
      </table>
	  <img src="/assets/unmanaged/images/s.gif" width="1" height="20"></td>
    <td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@page import="org.owasp.encoder.Encode"%>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
 <%@ page import="com.manulife.pension.ps.web.tpabob.TPABlockOfBusinessEntryForm" %>  
<jsp:useBean id="tpaBlockOfBusinessForm" 
			scope="session" 
			class="com.manulife.pension.ps.web.tpabob.TPABlockOfBusinessForm" />
 			
<%TPABlockOfBusinessEntryForm tpaBlockOfBusinessEntryForm1 = (TPABlockOfBusinessEntryForm)session.getAttribute("tpaBlockOfBusinessEntryForm");
	pageContext.setAttribute("tpaBlockOfBusinessEntryForm1",tpaBlockOfBusinessEntryForm1,PageContext.PAGE_SCOPE); %>	 
 

  <content:contentBean
  contentId="<%=ContentConstants.TPA_BLOCK_OVERVIEW_LINK_TEXT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="tpaBlockOverview" />
  
  <content:contentBean
  contentId="<%=ContentConstants.TPA_BLOCK_OVERVIEW_DESCRIPTION_TEXT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="tpaBlockOverviewDescription" />
  
  <content:contentBean
  contentId="<%=ContentConstants._404A5_IPI_DATA_LINK_TEXT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="_404a5IpiData" />
  
  <content:contentBean
  contentId="<%=ContentConstants._404A5_IPI_DATA_DESCRIPTION_TEXT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="_404a5IpiDataDescription" />
  
  <content:contentBean
  contentId="<%=ContentConstants.TPA_FEES_LINK_TEXT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="tpaFees" />
  
  <content:contentBean
  contentId="<%=ContentConstants.TPA_FEES_DESCRIPTION_TEXT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="tpaFeesDescription" />
   
  <content:contentBean
  contentId="<%=ContentConstants.TPA_MISSED_LOAN_REPAYMENT_REPORT_LINK%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="tpaMissedLoanRepaymentReport" />
  
  <content:contentBean
  contentId="<%=ContentConstants.TPA_MISSED_LOAN_REPAYMENT_REPORT_DESCRIPTION%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="tpaMissedLoanRepaymentReportDescription" />
  
  <content:contentBean
  contentId="<%=ContentConstants.CRD_QBD_TRANSACTION_ACTIVITY_LINK_TEXT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="tpaCrdQbdReportLink" />
  
  <content:contentBean
  contentId="<%=ContentConstants.CRD_QBD_TRANSACTION_ACTIVITY_DESCRIPTION_TEXT%>"
  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="tpaCrdQbdReportDescription" />
 
 <SCRIPT type="text/javascript" >
 
  /**
 * This method is called when a user clicks on the Profile ID of the TPA user. 
 * This method takes the user to TPA BOB page.
 */
 
function gotoTPABOB(profileID) {
	tpaBobForm = document.tpaBlockOfBusinessForm;
	tpaBobForm.tpaUserIDSelected.value = profileID;
	tpaBobForm.submit();
}
</SCRIPT>

<div id="errordivcs"><content:errors scope="session"/></div>
<ps:form  modelAttribute="tpaBlockOfBusinessForm" name="tpaBlockOfBusinessForm" action="/do/tpabob/tpaBlockOfBusiness/" method="post">
	<form:hidden path="tpaUserIDSelected" />
	
</ps:form>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="23%" valign="top"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
		<td width="54%">
		  
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
			    	<td width="250" ><img src="/assets/unmanaged/images/s.gif" width="250" height="1"></td>
					<td width="300"><img src="/assets/unmanaged/images/s.gif" width="300" height="1"></td>
					<td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
			    </tr>
			    <tr height="50">
			    <td width="250" align="left" > 
				
					<ps:linkAccessible path="/do/tpabob/tpaBlockOfBusiness/">
	                      <content:getAttribute beanName="tpaBlockOverview" attribute="text" >
                           <content:param>javascript:gotoTPABOB('<%=Encode.forHtmlContent(tpaBlockOfBusinessEntryForm1.getTpaUserIDSelected())%>')</content:param>
	                  
	                      </content:getAttribute>
	                </ps:linkAccessible>
			    </td>
				<td width="300">
					<content:getAttribute beanName="tpaBlockOverviewDescription" attribute="text"/>
			    </td>
				<td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
			   </tr>
			   <tr height="50">
			    <td width="250" align="left" >
					<ps:linkAccessible path="/do/tpabob/tpaBlockOfBusinessHome/">
	                      <content:getAttribute beanName="_404a5IpiData" attribute="text" >
							<content:param>/do/tpabob/tpaBlockOfBusinessHome/?action=generateIPIBoBCsv</content:param>
	                      </content:getAttribute>
	                </ps:linkAccessible>
				</td>
				<td width="300">
					<content:getAttribute beanName="_404a5IpiDataDescription" attribute="text"/>
				</td>
				<td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
			   </tr>
			   <tr height="50">
			    <td width="250" align="left" >
	                <ps:linkAccessible path="/do/tpabob/tpaBlockOfBusinessHome/">
	                    <content:getAttribute beanName="tpaFees" attribute="text" >
							<content:param>/do/tpabob/tpaBlockOfBusinessHome/?action=generateTPAFeesCsv</content:param>
	                    </content:getAttribute>
	                </ps:linkAccessible>
			    </td>
				<td width="300">
					<content:getAttribute beanName="tpaFeesDescription" attribute="text"/>
			    </td>
				<td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1">
				       
				    </td>
			   </tr>
			   <tr height="50">
			    <td width="250" align="left" >
	                <ps:linkAccessible path="/do/tpabob/tpaBlockOfBusinessHome/">
	                    <content:getAttribute beanName="tpaMissedLoanRepaymentReport" attribute="text" >
							<content:param>/do/tpabob/tpaBlockOfBusinessHome/?action=generateTPAMissedLoanRepaymentReportCsv</content:param>
	                    </content:getAttribute>
	                </ps:linkAccessible>
			    </td>
				<td width="300">
					<content:getAttribute beanName="tpaMissedLoanRepaymentReportDescription" attribute="text"/>
			    </td>
				<td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1">
			    </td>
			   </tr>
			   <tr height="50">
			    <td width="250" align="left" >
	                <ps:linkAccessible path="/do/tpabob/tpaBlockOfBusinessHome/">
	                    <content:getAttribute beanName="tpaCrdQbdReportLink" attribute="text" >
							<content:param>/do/tpabob/tpaBlockOfBusinessHome/?action=generateTPACrdQbaTransactionReportCsv</content:param>
	                    </content:getAttribute>
	                </ps:linkAccessible>
			    </td>
				<td width="300">
					<content:getAttribute beanName="tpaCrdQbdReportDescription" attribute="text"/>
			    </td>
				<td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1">
			    </td>
			   </tr>
		    </table>
					 
		    <p class="footnote"><content:getAttribute attribute="pageFooter" beanName="layoutPageBean"/></p>
			<p class="footnote"><content:getAttribute attribute="pageFootnotes" beanName="layoutPageBean"/></p><br>
			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/><br><br></p>
		</td>
		<td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	</tr>
</table>

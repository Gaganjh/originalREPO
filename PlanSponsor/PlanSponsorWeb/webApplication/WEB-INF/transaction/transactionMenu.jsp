<%@page import="com.manulife.pension.content.valueobject.LayoutPage"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%
int testval = ContentConstants.PENDING_WDW_OF_ADDITIONAL_CONTRIBUTIONS_LINK;
int miscval=ContentConstants.MISCELLANEOUS_SYSYTEMATIC_WITHDRAWALS_LINK;
int miscdesc=ContentConstants.MISCELLANEOUS_SYSYTEMATIC_WITHDRAWALS_DESCRIPTION;
int miucl=ContentConstants.MISCELLANEOUS_UNCASHED_CHECKS_LINK;
int miucd=ContentConstants.MISCELLANEOUS_UNCASHED_CHECKS_DESCRIPTION;
int cqtrl=ContentConstants.CRD_QBD_TRANSACTION_REPORT_LINK_TEXT;
int cqtrd=ContentConstants.CRD_QBD_TRANSACTION_REPORT_DESCRIPTION_TEXT;
pageContext.setAttribute("testval",testval,PageContext.PAGE_SCOPE);
pageContext.setAttribute("miscval",miscval,PageContext.PAGE_SCOPE);
pageContext.setAttribute("miscdesc",miscdesc,PageContext.PAGE_SCOPE);
pageContext.setAttribute("miucl",miucl,PageContext.PAGE_SCOPE);
pageContext.setAttribute("miucd",miucd,PageContext.PAGE_SCOPE);
pageContext.setAttribute("cqtrl",cqtrl,PageContext.PAGE_SCOPE);
pageContext.setAttribute("cqtrd",cqtrd,PageContext.PAGE_SCOPE);
%>
<c:set var="testval" value="${testval}"/>
<c:set var="miscval" value="${miscval}"/>
<c:set var="miscdesc" value="${miscdesc}"/>
<c:set var="miucl" value="${miucl}"/>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
      <table width="700" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
          <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
        </tr>
        <tr>
          <td valign="top">
		  <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
		  <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
		  <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
				<img src='<content:pageImage type="pageTitle" beanName="layoutPageBean"/>'
alt="${layoutBean.getParam('titleImageAltText')}" width="224" height="35"><br>

 <table width="500" border="0" cellspacing="0" cellpadding="0">
			  <tr><td colspan="2"><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td></tr>
              <tr>
				<td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
				<td width="495" valign="top"><br>
	           <c:if test="${not empty layoutPageBean.introduction1}">
				  <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/><br>
</c:if>
				  <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/><br>
				<img src="/assets/unmanaged/images/s.gif" width="1" height="20">
				<div id="errordivcs"><content:errors scope="session"/></div>
				  <table width="495" border="0">
                    <tr>
                      <td width="12" valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
                      <td width="150" valign="top">
				      	<content:getAttribute beanName="layoutPageBean" attribute="body1Header">
				      		<content:param>/do/transaction/transactionHistoryReport/</content:param>	
				      	</content:getAttribute>
					  </td>
                      <td width="20" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
                      <td width="350" valign="top">
                      	<content:getAttribute beanName="layoutPageBean" attribute="body1"/>
					  </td>
                    </tr>
                    <tr>
                      <td colspan="4" height="20"></td>
                    </tr>
                    <tr>
			    		<td valign="top">
						</td>
			      		<td valign="top">
							<content:contentBean contentId="${testval}" type="Miscellaneous" id="pendingAddContribLink"/>
							<content:getAttribute id="pendingAddContribLink" attribute="text">
							<content:param>/do/transaction/pendingTransactionHistoryReport/</content:param>
							</content:getAttribute>
							
			      		</td>
							<td valign="top"><img src="/assets/unmanaged/images/s.gif" ></td>
			      			<td valign="top">
							<content:contentBean contentId="<%=ContentConstants.PENDING_WDW_OF_ADDITIONAL_CONTRIBUTIONS_DESC%>" type="Miscellaneous" id="pendingAddContribDesc"/>
			      			<content:getAttribute beanName="pendingAddContribDesc" attribute="text"/>
			      		</td>
			    	</tr>
			    	 <tr>
                      <td colspan="4" height="20"></td>
                    </tr>
                    
                  <% 
                		boolean showSysWdLink = (Boolean) request.getAttribute("showSysLink");
						if(showSysWdLink) {
							
					%>
			    	<ps:isInPilot name="systematic Withdrawal">
						<tr>
				    		<td valign="top">
							</td>
									<td valign="top"><content:contentBean
											contentId="${miscval}"
											type="Miscellaneous" id="systematicWithdrawalLink" /> <content:getAttribute
											id="systematicWithdrawalLink" attribute="text">
											<content:param>/do/transaction/systematicWithdrawalReport/</content:param>
										</content:getAttribute> 
									</td>
									<td valign="top"><img src="/assets/unmanaged/images/s.gif" ></td>
				      			<td valign="top">
				      			<!--Layout/body3-->
								<content:contentBean contentId="${miscdesc}" type="Miscellaneous" id="systematicWithdrawalDescription"/>
				      			<content:getAttribute beanName="systematicWithdrawalDescription" attribute="text"/>
				      		</td>
				    	</tr>
				    	<tr>
 				  			<td colspan="4" height="20"></td>
 				  		</tr>
					</ps:isInPilot> 
					
                    <%
						}
					%> 
                   
                    <tr>
                      <td valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
                      <td valign="top">
				      	<content:getAttribute beanName="layoutPageBean" attribute="body2Header">
				      		<content:param>/do/transaction/cashAccountReport/</content:param>	
				      	</content:getAttribute>
					  </td>
                      <td valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
                      <td valign="top">
                      	<content:getAttribute beanName="layoutPageBean" attribute="body2"/>
					  </td>
                    </tr>
                    <tr>
                      <td colspan="4" height="20"></td>
                    </tr>
					<c:if test="${showLoans==true}">
	                    <tr>
	                      <td valign="top"><img src="/assets/unmanaged/images/s.gif" width="12" height="2"></td>
	                      <td  valign="top">
				      	<content:getAttribute beanName="layoutPageBean" attribute="body3Header">
					      		<content:param>/do/transaction/loanSummaryReport/</content:param>	
					      	</content:getAttribute>
						  </td>
	                      <td valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
	                      <td valign="top">
	                      	<content:getAttribute beanName="layoutPageBean" attribute="body3"/>
						  </td>
	                    </tr>
</c:if>
                    <tr>
                      <td colspan="4" height="20"></td>
                    </tr>
					
					<%
                	//	boolean restricted = (Boolean) request.getAttribute("ISRESTRICTED");
						//if(!restricted) {
					%> 
					<ps:isInPilot name="Uncashed Checks">
						<tr>
				    		<td valign="top">
							</td>
				      		<td valign="top">
								<content:contentBean contentId="${miucl}" type="Miscellaneous" id="uncashedChecksLink"/>
								<content:getAttribute id="uncashedChecksLink" attribute="text">
								<content:param>/do/transaction/uncashedChecksReport/</content:param>
								</content:getAttribute>
								
				      		</td>
								<td valign="top"><img src="/assets/unmanaged/images/s.gif" ></td>
				      			<td valign="top">
				      			<!--Layout/body3-->
								<content:contentBean contentId="${miucd}" type="Miscellaneous" id="uncashedChecksDescription"/>
				      			<content:getAttribute beanName="uncashedChecksDescription" attribute="text"/>
				      		</td>
				    	</tr>
				    	<tr>
 				  			<td colspan="4" height="20"></td>
 				  		</tr>
 				  		</ps:isInPilot>


							<tr>
								<td valign="top"></td>
								<td valign="top"><content:contentBean contentId="${cqtrl}"
										type="Miscellaneous" id="crdQbdTransactionReportLink" /> <content:getAttribute
										id="crdQbdTransactionReportLink" attribute="text">
										<content:param>/do/transaction/transactionMenu/?action=generateCrdQbaTransactionReportCsv</content:param>
									</content:getAttribute></td>
								<td valign="top"><img src="/assets/unmanaged/images/s.gif"></td>
								<td valign="top">
									<!--Layout/body3--> <content:contentBean contentId="${cqtrd}"
										type="Miscellaneous" id="crdQbdTransactionReportDescription" />
									<content:getAttribute
										beanName="crdQbdTransactionReportDescription" attribute="text" />
								</td>
							</tr>
							<tr>
								<td colspan="4" height="20"></td>
							</tr>


							<%
								//	}
							%> 
					
                    <tr>
                      <td valign="top">&nbsp;</td>
                      <td valign="top">&nbsp;</td>
                      <td valign="top"><img src="/assets/unmanaged/images/s.gif" width="1"></td>
                      <td valign="top">&nbsp;</td>
                    </tr>
                  </table>
                </td>
			  </tr>
			  <tr>


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
            </table>
            <img src="/assets/unmanaged/images/s.gif" width="1" height="5"><br>
          </td>
        </tr>
      </table>  

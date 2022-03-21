<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<content:contentBean contentId="<%=ContentConstants.CSF_LINK%>"	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="csfLinkText" />
<content:contentBean contentId="<%=ContentConstants.CSF_LINK_BLURB%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="csfLinkBlurb"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PLAN_HEADING_INFO%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="PLAN_HEADING_INFO"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_DEFINED_BENEFIT_CONTRACT_SNAPSHOT_TEXT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="contractSnapshotText"/>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("hideCsfLink",(String)request.getAttribute("hideCsfLink"),PageContext.PAGE_SCOPE);
	pageContext.setAttribute("isContractDocPresent",(Boolean)request.getAttribute("isContractDocPresent"),PageContext.PAGE_SCOPE);
	
	
%>
<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}"/>


<td width="4%" valign="top">
	<img src="/assets/unmanaged/images/body_corner.gif" width="50%" height="8"><br>
	<img src="/assets/unmanaged/images/s.gif" width="100%" height="1">
</td>

<td width="82%">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
        	<td width="82%" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          	<td width="3%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
          	<td width="15%" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        </tr>
        <tr>
        	<td width="82%">
		  		<img src="/assets/unmanaged/images/s.gif" width="82%" height="23">
		  		<img src="/assets/unmanaged/images/s.gif" width="3%" height="1">
		  		<!--page title-->
		  		<img src="<content:pageImage type="pageTitle" beanName="layoutPageBean" path="/assets/unmanaged/images/head_plan_details.gif"/>" alt="Contract details" >
		  		<br>

		    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  		<tr>
			  			<td colspan="2"><img src="/assets/unmanaged/images/s.gif" height="5"></td>
			  		</tr>
              		<tr>
						<td width="1%"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
						<td width="99%" valign="top">
							<br>
							<!--Layout/Intro1-->
							<content:pageIntro beanName="layoutPageBean"/>
            				<br>
                 			<br>
                  			<!--Layout/intro2-->
                  			<content:getAttribute beanName="layoutPageBean" attribute="introduction2">
                  				<content:param>#</content:param>
                  			</content:getAttribute>
                     		<br><br>
                     	<div id="errordivcs"><content:errors scope="session"/></div>
                			<table width="100%" border="0">
				  				<tr>
				    				<td width="2%" valign="top">
									</td>
				      				<td width="25%" valign="top">
			                  			<content:getAttribute beanName="layoutPageBean" attribute="body1Header">
				      						<content:param>../contractProfile</content:param>
				      					</content:getAttribute>
				      				</td>
									<td width="4%" valign="top"><img src="/assets/unmanaged/images/s.gif" ></td>
				      				<td width="350" valign="top">
				      					<!--Layout/body1-->
				      					<content:pageBody beanName="layoutPageBean" />
				      				</td>
				      			</tr>
				      			<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
				      			<!--For Defined Benefit contract need to hide 'Contract service feature' link if contract effective date is older than launch date-->
						    		<c:if test="${empty hideCsfLink }" >
								<!--ps:notPermissionAccess permissions="SELA"-->
									<tr>
										<td colspan="4" height="20"></td>
									</tr>
									<tr>
										<td width="2%" valign="top">
										</td>
										<td width="25%" valign="top">
										    <a href="/do/contract/viewServiceFeatures/"><content:getAttribute beanName="csfLinkText" attribute="text"/></a>
										</td>
										<td width="4%" valign="top"><img src="/assets/unmanaged/images/s.gif" ></td>
										<td width="350" valign="top">
											<!--Layout/body1-->
											<content:getAttribute beanName="csfLinkBlurb" attribute="text"/>

										</td>
									</tr>
				      			<!--/ps:notPermissionAccess-->
				      				</c:if>
				      			</ps:isNotJhtc>
								<tr>
									<td colspan="4" height="20"></td>
								</tr>
								
				  				<tr>
				    				<td valign="top">
									</td>
				      				<td valign="top">
			                  			<content:getAttribute beanName="layoutPageBean" attribute="body2Header">
				      						<content:param>../contractSnapshot/</content:param>
				      					</content:getAttribute>
				      				</td>
									<td valign="top"><img src="/assets/unmanaged/images/s.gif"></td>
				      					<td valign="top">
									    <c:if test="${definedBenefit != true}" >
				      						<!--Layout/body2-->
				      						<content:getAttribute beanName="layoutPageBean" attribute="body2"/>
</c:if>
						    			    <c:if test="${definedBenefit == true}" >
										<content:getAttribute beanName="contractSnapshotText" attribute="text"/>
</c:if>
                      					</td>
				  					</tr>
   				  					<tr>
   				  						<td colspan="4" height="20"></td>
   				  					</tr>
   				  					
   				  					
<c:if test="${userProfile.allowedContractStatements ==true}" >
				  					<tr>
				    					<td valign="top">
										</td>

				      					<td valign="top">
			                  			<content:getAttribute beanName="layoutPageBean" attribute="body3Header">
				      							<content:param>../contractStatements/</content:param>
				      						</content:getAttribute>
				      					</td>
										<td valign="top"><img src="/assets/unmanaged/images/s.gif" ></td>
				      					<td valign="top">
				      						<!--Layout/body3-->
				      						<content:getAttribute beanName="layoutPageBean" attribute="body3"/>
				      					</td>
				    				</tr>
 				  					<tr>
 				  						<td colspan="4" height="20"></td>
 				  					</tr>
</c:if>
					<!--Code change for ContractDocuments  starts-->
<c:if test="${userProfile.allowedContractDocuments ==true}">
						<c:if test="${isContractDocPresent ==true}" >
												<tr>
				    			<td valign="top">
								</td>
				      			<td valign="top">
			          	         <content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CONTRACT_DOCUMENTS_LINK %>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="contractDocLink"/>
				      		    <a href="/do/contract/contractDocuments/"><content:getAttribute id="contractDocLink"  attribute="text"/></a>
				      			</td>
								<td valign="top"><img src="/assets/unmanaged/images/s.gif" ></td>
				      			<td valign="top">
				      			<!--Layout/body3-->
				      			 <content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CONTRACT_DOCUMENTS_DESCRIPTION %>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="contractDocDescription"/>
				      			 <content:getAttribute beanName="contractDocDescription" attribute="text"/>
				      			</td>
				    		</tr>
				    		<tr>
 				  				<td colspan="4" height="20"></td>
 				  			</tr>
</c:if>
</c:if>
					<!--Code change for ContractDocuments  ends-->

                  <%
                    Boolean isPlanAvailable = (Boolean)request.getAttribute("isPlanAvailable");
                    if (isPlanAvailable.booleanValue() == true) {
                  %>
                    <tr>
                      <td valign="top">
                      </td>
                          <td valign="top">
                              <a href="../planData/view/"><content:getAttribute beanName="PLAN_HEADING_INFO" attribute="text"/></a>
                          </td>
                      <td valign="top"><img src="/assets/unmanaged/images/s.gif" ></td>
                      <td valign="top">
                        <!--Layout/body3-->
                        An online view of plan information.
                      </td>
                    </tr>
			            <%}%>
				    			</table>
			    			</td>
			  			</tr>

				    	<tr>
				    		<td width="1%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
				    		<td colspan=3 width="99%">
								<br>
								<p><content:pageFooter beanName="layoutPageBean"/></p>
 								<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 								<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 							</td>
 						</tr>
 					</table>
 				</td>

			    <td width="3%"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
       			<td width="15%" ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>

       		</tr>

       		<tr>
  		    </tr>
	</table>

</td>
<td valign="top">
	<img src="/assets/unmanaged/images/s.gif"  height="59">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="box">
    	<tr>
        	<td ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
            <td ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
            <td ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
        </tr>
    </table>
	<img src="/assets/unmanaged/images/s.gif" height="20">
</td>

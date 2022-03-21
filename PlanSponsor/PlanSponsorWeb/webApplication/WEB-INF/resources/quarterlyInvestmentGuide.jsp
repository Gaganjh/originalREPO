<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>




<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_QIG_RETAIL_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="retailLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_QIG_VENTURE_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="ventureLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_QIG_BD_HYBRID_R_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="bdRetailLinkText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_QIG_BD_HYBRID_V_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="bdVentureLinkText"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_QIG_MULTICLASS_LINKTEXT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="multiclassLinkText"/>


<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_ADOBE_READER_LINK%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="AdobeLink"/>
                     
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<%
	String fundPackageRetail = (String)session.getAttribute(Constants.FUND_PACKAGE_RETAIL);
	String fundPackageHybrid = (String)session.getAttribute(Constants.FUND_PACKAGE_HYBRID);
	String fundPackageBroker = (String)session.getAttribute(Constants.FUND_PACKAGE_BROKER);
	String fundPackageVenture = (String)session.getAttribute(Constants.FUND_PACKAGE_VENTURE);
	String fundPackageMulticlass = (String)session.getAttribute(Constants.FUND_PACKAGE_MULTICLASS);
	
	String fundPackageVRS = (String)session.getAttribute(Constants.FUND_PACKAGE_VRS);
	String qigVentureBDProductId = (String)session.getAttribute(Constants.QIG_VENTURE_BD_PRODUCT_ID);
	String qigVentureBDProductNYId = (String)session.getAttribute(Constants.QIG_VENTURE_BD_PRODUCT_NY_ID);
	
	String bdProductId = (String)session.getAttribute(Constants.BD_PRODUCT_ID);
	
	pageContext.setAttribute("fundPackageRetail",fundPackageRetail,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("fundPackageHybrid",fundPackageHybrid,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("fundPackageBroker",fundPackageBroker,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("fundPackageVenture",fundPackageVenture,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("fundPackageMulticlass",fundPackageMulticlass,PageContext.PAGE_SCOPE);
	
	pageContext.setAttribute("fundPackageVRS",fundPackageVRS,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("qigVentureBDProductId",qigVentureBDProductId,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("qigVentureBDProductNYId",qigVentureBDProductNYId,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("bdProductId",bdProductId,PageContext.PAGE_SCOPE);
%>

	<td width="23%" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	<td width="54%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
          <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
        </tr>
        <tr>
          <td colspan="3" >
		  <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
		  <img src='<content:pageImage type="pageTitle" beanName="layoutPageBean"/>' alt="Investments" ><br>
		  <table width="500" border="0" cellspacing="0" cellpadding="0">
		  		<tr><td><img src="/assets/unmanaged/images/s.gif" width="500" height="5"></td></tr>
          		<tr>
					<td valign="top">
						<c:if test="${layoutPageBean.introduction1 != '' }">
				    		<br>
				    		<content:getAttribute attribute="introduction1" beanName="layoutPageBean"/>
</c:if>
				    	<c:if test="${layoutPageBean.introduction2 != '' }">
				    		<br>
				    		<content:getAttribute attribute="introduction2" beanName="layoutPageBean"/>
</c:if>
				    	<c:if test="${layoutPageBean.body1Header != '' }">	
				    		<br>
				  			<content:getAttribute attribute="body1Header" beanName="layoutPageBean"/>
</c:if>
				  		<br>
				  		<content:getAttribute attribute="body1" beanName="layoutPageBean"/>
						<br>
						

          			</td>
        		</tr>
        		<tr>
         			<td valign="top">
					<ul>



	<c:if test="${userProfile.currentContract.contractAllocated ==true}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageMulticlass}">



<c:if test="${applicationScope.environment.siteLocation == 'usa'}" >

						 <ps:qigUrl property ="userProfile.currentContract" location ="usa" /><content:getAttribute id="multiclassLinkText" attribute="text"/></a>
</c:if>
<c:if test="${applicationScope.environment.siteLocation == 'ny' }" >
						 <ps:qigUrl property ="userProfile.currentContract" location ="ny" /><content:getAttribute id="multiclassLinkText" attribute="text"/></a>
</c:if>
</c:if>
</c:if>


<c:if test="${applicationScope.environment.siteLocation=='usa'}" >

<c:if test="${userProfile.currentContract.contractAllocated ==true}">

	<c:if test="${userProfile.currentContract.nml ==false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageRetail}" >
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_RETAIL_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="text"/></a>
										</li>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='HYB'}">


<c:if test="${userProfile.currentContract.productId =='ARABD'}">
										<li>

													<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_HYBRID_R_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="bdRetailLinkText" attribute="text"/></a>
												</li>
										<li>

													<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_HYBRID_V_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="bdVentureLinkText" attribute="text"/></a>
												</li>
</c:if>

<c:if test="${userProfile.currentContract.productId !='ARABD'}">
										
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_RETAIL_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="text"/></a>
										</li>
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="text"/></a>
										</li>

</c:if>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageBroker}">
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_RETAIL_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="text"/></a>
										</li>
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="text"/></a>
										</li>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageVenture}">
<c:if test="${userProfile.currentContract.productId ==qigVentureBDProductId}">
											<li>	
												<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="text"/></a>
											</li>
</c:if>
<c:if test="${userProfile.currentContract.productId !=qigVentureBDProductId}">
											<li>	
												<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="text"/></a>
											</li>
</c:if>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.nml ==true}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageRetail}">
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_NML_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="text"/></a>
										</li>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='HYB'}">

										
<c:if test="${userProfile.currentContract.productId =='ARABD'}">
												<li>	
													<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_HYBRID_R_NML_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="bdRetailLinkText" attribute="text"/></a>
												</li>
												<li>	
													<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_HYBRID_V_NML_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="bdVentureLinkText" attribute="text"/></a>
												</li>
</c:if>
<c:if test="${userProfile.currentContract.productId !='ARABD'}">
				
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_NML_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="text"/></a>
										</li>
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="text"/></a>
										</li>

</c:if>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageBroker}">
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_NML_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="text"/></a>
										</li>
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="text"/></a>
										</li>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageVenture}">
<c:if test="${userProfile.currentContract.productId ==qigVentureBDProductId}">
											<li>	
												<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="text"/></a>
											</li>
</c:if>
<c:if test="${userProfile.currentContract.productId !=qigVentureBDProductId}">
											<li>	
												<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="text"/></a>
											</li>
</c:if>
</c:if>
</c:if>
</c:if>
</c:if>
						
<c:if test="${applicationScope.environment.siteLocation == 'ny'}">
<c:if test="${userProfile.currentContract.contractAllocated ==true}">
<c:if test="${userProfile.currentContract.nml ==false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageRetail}">
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_RETAIL_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="nyText"/></a>
										</li>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == 'HYB' }">
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_RETAIL_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="nyText"/></a>
										</li>
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="nyText"/></a>
										</li>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageBroker}">

<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='HYB'}">
										
<c:if test="${userProfile.currentContract.productId =='ARABD'}">
											<li>

											<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_HYBRID_NY_R_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="bdRetailLinkText" attribute="text"/></a>
											</li>
	                                                                         	<li>

											<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_HYBRID_NY_V_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="bdVentureLinkText" attribute="text"/></a>
										 	</li>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.productId !='ARABD'}">
										
										<li>

											<a href="#" onClick="PDFWindow('<%=Constants.QIG_RETAIL_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="nyText"/></a>
										</li>
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="nyText"/></a>
										</li>

</c:if>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageVenture}">

<c:if test="${userProfile.currentContract.productId =='ARABDY'}">
											<li>	
												<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="nyText"/></a>
											</li>
</c:if>

<c:if test="${userProfile.currentContract.productId !='ARABDY'}">
											<li>	
												<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="nyText"/></a>
											</li>
</c:if>
</c:if>
					
</c:if>
<c:if test="${userProfile.currentContract.nml ==true}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageRetail}">
										<li>												
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_NML_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="nyText"/></a>
										</li>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode =='HYB'}">

<c:if test="${userProfile.currentContract.productId =='ARABDY'}">
												<li>	
													<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_HYBRID_NY_R_NML_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="bdRetailLinkText" attribute="nyText"/></a>
												</li>
												<li>	
													<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_HYBRID_NY_V_NML_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="bdVentureLinkText" attribute="nyText"/></a>
												</li>
</c:if>
<c:if test="${userProfile.currentContract.productId !='ARABDY'}">

										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_NML_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="nyText"/></a>
										</li>
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="nyText"/></a>
										</li>

</c:if>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageBroker}">
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_NML_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="retailLinkText" attribute="nyText"/></a>
										</li>
										<li>
											<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="nyText"/></a>
										</li>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode ==fundPackageVenture}">

<c:if test="${userProfile.currentContract.productId =='ARABDY'}">
											<li>	
												<a href="#" onClick="PDFWindow('<%=Constants.QIG_BD_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="nyText"/></a>
											</li>
</c:if>

<c:if test="${userProfile.currentContract.productId !='ARABDY'}">
											<li>	
												<a href="#" onClick="PDFWindow('<%=Constants.QIG_VENTURE_NY_URL%>')" onMouseOver="self.status='Go to the PDF'; return true"><content:getAttribute id="ventureLinkText" attribute="nyText"/></a>
											</li>
</c:if>
</c:if>
</c:if>
</c:if>
</c:if>

					</ul>
           			</td>
          		</tr>
          		<tr>
          			<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          			<td valign="top"> <img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
        		</tr>
        		<tr>
        			<td>
        				<content:getAttribute id="AdobeLink" attribute="text"/>
      	  			</td>
      	  		</tr>
      	  		<tr>
          			<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
          			<td valign="top"> <img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
        		</tr>
      	  </table>



		  <table width="500" border="0" cellspacing="0" cellpadding="0">
		  		<tr>
				    		
				    <td colspan=3 width="100%">
						<p><content:pageFooter beanName="layoutPageBean"/></p>
 						<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
 						<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
 					</td>
 				</tr>		  	
 		  </table>
 		  </td>
 		</tr>
 	  </table>
 	
 	<td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>

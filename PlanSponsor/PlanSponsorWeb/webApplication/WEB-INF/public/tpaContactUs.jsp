<%-- Prevent the creation of a session --%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.Constants, com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>


<%-- This jsp includes the following CMA content --%>

<content:contentBean contentId="<%=ContentConstants.PS_CONTACTS_DEFAULT_EMAIL%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Contacts_Default_Email"/>

<c:set var="emailAddress" value="${Contacts_Default_Email.text}" /> 
<div id="errordivcs"><content:errors scope="session"/></div>
<%-- Logic to highlight Contact us on the navigation --%>
<c:if test = "${parm== 'contactUs'}">
<script type="text/javascript" >
	init(3, 1);
</script>

</c:if>

<!-- start contactUs.jsp area -->
<table width="765" border="0" cellspacing="0" cellpadding="0" />
  
  <tr>
  	<td>&nbsp;</td>
  </tr>
  
  <tr>
	<!-- column 1 -->	

   	<td width="90">
    	<img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0">&nbsp;
    </td>
    
    <!--// end menu (column 1) -->

    <!-- column 2 -->  
    <td width="435" valign="top" class="greyText"> 
		<table width="435" border="0" cellspacing="0" cellpadding="0" class="fixedTable">
			<tr>
				<td class="greyText" align="left">
				<p><img src='<content:pageImage beanName="layoutPageBean" type="pagetitle"/>' alt="Contact Us">
				</p>
				</td>
			</tr>
		 
			<tr>
				<td class="greyText" align="left">
				
	              <c:if test="${not empty layoutPageBean.introduction1}">
				  	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
</c:if>
					
				  <br>
				
				  <c:if test="${not empty layoutPageBean.introduction2}">
					<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
</c:if>
		<c:if test = "${layoutPageBean.body1 ne ''}">
					<p>
						<content:getAttribute attribute="body1" beanName="layoutPageBean">
							<content:param>
<a href='mailto:${emailAddress}?subject=${firstFirmId} <%-- scope="request" --%> '>${emailAddress}</a>
							</content:param>
						</content:getAttribute>
					</p>
</c:if>
		  
			</td>
			</tr>
		
			<tr>
				<td>
					<br>
					<br>
<input type="button" onclick="javascript:history.back();" name="button" class="button100Lg" value="previous"/>
					<br>
					<br>
				</td>
			</tr>	
		</table>
	</td>
	
    <!--// emd column 2 -->


    <!-- column 3 HELPFUL HINT -->

    <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
    <td width="180" valign="top" >
       <center>
			<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
	   </center>
    </td>

    <!--// end column 3 -->
    </tr>
</table>
<!-- end contactUs.jsp area -->



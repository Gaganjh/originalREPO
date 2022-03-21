<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td valign="top" align="left">
			<img src="/assets/unmanaged/images/s.gif" width="10" height="1">
		</td>
		<td width="350" valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td valign="top">
<c:if test="${not empty layoutBean.getParam(titleImageFallback)}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.param(titleImageFallback)}" /> 


<img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>"
alt="${layoutBean.getParam('titleImageAltText')}">
			     			<br>
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
							<img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
							<br>
</c:if>

						<c:if test="${empty param.printFriendly }" >
							<!--Layout/intro1-->
							<c:if test="${not empty layoutPageBean.introduction1}">
								<content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
								<br>
</c:if>
							<!--Layout/Intro2-->
<c:if test="${layoutBean.showSelectContractLink ==true}">
								<c:if test="${not empty layoutPageBean.introduction2}">
									<content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
									<br>
</c:if>
</c:if>
						</c:if>
					</td>
				</tr>
			</table>
		<br>
		</td>
	</tr>
</table>

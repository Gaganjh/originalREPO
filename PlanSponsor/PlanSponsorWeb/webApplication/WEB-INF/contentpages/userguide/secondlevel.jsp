<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%@ page import="com.manulife.pension.content.view.MutableLayoutPage" %>
<%@ page import="com.manulife.pension.delegate.ContractServiceDelegate" %> 
<%@ page import="com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility" %>
<%@page import="com.manulife.pension.exception.ContractDoesNotExistException" %>
<%@ page import="com.manulife.pension.service.security.role.PilotCAR" %>
<%@page import="com.manulife.pension.service.security.role.InternalUserManager"%>
<%@page import="com.manulife.pension.content.valueobject.ContentDescription"%>

<%

	//retrieve content id from the request
	String strContentKey = request.getParameter("contentKey");
	boolean noticeManagerflag = false;
	int contentKey = 0;
	if(strContentKey != null) {
		contentKey = Integer.parseInt(strContentKey);
	}
	else
		return;
//GIFL 1C- Start
	UserProfile userprofile = (UserProfile)request.getSession(false).getAttribute(Constants.USERPROFILE_KEY);
	boolean giflInd = userprofile.getCurrentContract().getHasContractGatewayInd();
	String giflVersion3Indicator = Constants.GIFL_VERSION_03.equals(userprofile.getCurrentContract().getGiflVersion())? "true":"false";
//GIFL 1C- End

// OB3 T3 Changes - Start 
	boolean deferralInd = false;
	String deferralMode = "";
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		deferralMode = contractServiceDelegate.determineSignUpMethod(userprofile.getCurrentContract().getContractNumber());
		if(	deferralMode != null){
			deferralInd = true;
		}
// OB3 T3 Changes - End 

// JIRA#131 Contact us Changes - Start 
	boolean isBundledIndicator  = userprofile.getCurrentContract().isBundledGaIndicator();
// JIRA#131 Contact us Changes  - End 

	
	try {
			if (!(NoticeManagerUtility.validateProductRestriction(userprofile
					.getCurrentContract())
					|| NoticeManagerUtility.validateContractRestriction(userprofile
							.getCurrentContract())
					|| NoticeManagerUtility.validateDIStatus(
							userprofile.getCurrentContract(), userprofile.getRole())
					|| userprofile.getRole() instanceof PilotCAR || userprofile.isInternalServicesCAR()|| userprofile.getRole()instanceof InternalUserManager) ) {
				noticeManagerflag = true;
			}
			
		} catch (ContractDoesNotExistException ex) {

		}

%>
<content:contentBean contentId="<%=contentKey%>" type="LayoutPage" beanName="SecondLevelBean"/> 

<%
	String title = "User Guides";
	Object obj = pageContext.getAttribute("SecondLevelBean", PageContext.REQUEST_SCOPE);	
	MutableLayoutPage layoutPage = (MutableLayoutPage)obj;
	title = layoutPage.getBrowserTitle();
	int parentId = layoutPage.getParentId();
	
%>

<c:if test="${SecondLevelBean.parentId ==73}">
	<script type="text/javascript" >
		init(2, 1);
	</script>
</c:if>
<c:if test="${SecondLevelBean.parentId ==75}">
	<script type="text/javascript" >
		init(2, 4);
	</script>
</c:if>
<c:if test="${SecondLevelBean.parentId !=73}">
<c:if test="${SecondLevelBean.parentId !=75}">
	<script type="text/javascript" >
		init(0, 0);
    </script>
</c:if>
</c:if>

<script type="text/javascript" >
	document.title = "<%=title%>";
</script>

<content:contentListByParent parentId="<%=parentId%>" collectionName="contentItems"/>

<c:forEach items="${contentItems}" var="item" >	
	<% ContentDescription item= (ContentDescription)pageContext.getAttribute("item"); %>

<%
	//Find the landing page bean
	if("0".equals(item.getCategory())) {
%>
<content:contentBean contentId="<%=item.getKey()%>" type="LayoutPage" beanName="LandingPageBean"/>
<%
	}
%>




</c:forEach>

 


<!-- column 1 (15 + 135 + 15 = 165) -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
	  <td width="10" valign="top"><img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><!-- <img src="/assets/unmanaged/images/s.gif" width="30" height="1">--></td>
	</tr>
</table>

<table width="760" cellpadding="0" cellspacing="0" border="0"> 
<tr>
    <td width="10" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td width="130"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td width="10" ><img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td width="395"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
    <td width="15"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
    <td width="180"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
    <td width="20"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
</tr>
<tr>	
  <td valign="top"><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
  <td  vAlign=top>

    <table id=column1 cellSpacing=0 cellPadding=0 border=0>
    <!--<tbody> -->
      
      <tr vAlign=top>
        <td >
          <img height=80 src="/assets/unmanaged/images/spacer.gif" width=1>
        </td>
      </tr>
      <tr>
      	
        <td class=greyText><table width="100%" border="0" cellpadding="0" cellspacing="0">
          
          <tr>
            <td > 
            	<strong><content:getAttribute attribute="body1Header" beanName="LandingPageBean"/></strong>
            	
            </td>
          <tr>
          <tr>
          	<td>
				<table>
            	<!-- Display the links -->
            	<content:secondLevelLHLinks contentKey="<%=contentKey%>" collectionName="contentItems" category="1" url="/do/contentpages/userguide/secondlevel/">
            	<content:param><%=String.valueOf(giflInd)%></content:param>
				    	<content:param><%=giflVersion3Indicator%></content:param>
				    	<!-- OB3 T3 Deferrals Link Changes - Start -->
						<content:param><%=String.valueOf(deferralInd)%></content:param>
				    	<content:param><%=deferralMode%></content:param>
				    	<!-- OB3 T3  Deferrals Link Changes - End -->	
				    	<content:param><%=String.valueOf(isBundledIndicator)%></content:param>
				    	
				    	<!-- This code needs to be added after CMA implementation -->
				    	<content:param><%=String.valueOf(parentId)%></content:param> 
				    	<content:param><%=String.valueOf(noticeManagerflag)%></content:param>     	
				</content:secondLevelLHLinks>
				
				
           		</table> 	
            </td>
          </tr>
          
           
<c:if test="${SecondLevelBean.parentId ne 75}">

          <tr>
            <td >
            	<br><br>
            	<strong><content:getAttribute attribute="body2Header" beanName="LandingPageBean"/></strong>
            	
            </td>
          </tr>
</c:if>
          <tr>
            <td>
              <table>
                <!-- Display the second set of links -->
                <content:secondLevelLHLinks contentKey="<%=contentKey%>" collectionName="contentItems" category="2" url="/do/contentpages/userguide/secondlevel/">
				    	<content:param><%=String.valueOf(giflInd)%></content:param>
				    	<content:param><%=giflVersion3Indicator%></content:param>
				    	<!-- OB3 T3 Deferrals Link Changes - Start -->
						<content:param><%=String.valueOf(deferralInd)%></content:param>
				    	<content:param><%=deferralMode%></content:param>
				    	<!-- OB3 T3  Deferrals Link Changes - End -->	
				    	<content:param><%=String.valueOf(isBundledIndicator)%></content:param>
				    	
				    	<!-- This code needs to be added after CMA implementation -->
				    	<content:param><%=String.valueOf(parentId)%></content:param>   
				    	<content:param><%=String.valueOf(noticeManagerflag)%></content:param>  	
				</content:secondLevelLHLinks>
				
              </table>
            </td>
          </tr>
		 
          
          <tr>
          	
            <td height="14" colspan="2">
              <br><br>
              <strong><content:getAttribute attribute="body3Header" beanName="LandingPageBean"/></strong>
              
            </td>
          </tr>
          <tr>
            <td>
              <table>
                <!-- Display the third set of links -->
          	    <content:secondLevelLHLinks contentKey="<%=contentKey%>" collectionName="contentItems" category="3" url="/do/contentpages/userguide/secondlevel/"/>
              </table>
            </td>
          </tr>          
          
                  
         </table></TD>
       </TR>
      
    <!--</tbody> -->
    </table>
  </td>


  <td><img height="1" src="/assets/unmanaged/images/spacer.gif" width="10" border="0"></td>
<!-- Now we move on to the main page -->
<!-- end column 1 -->
<!-- column 2 (375) -->
  <td vAlign="top" class="greyText"> 
  <!-- <img src="/assets/unmanaged/images/s.gif" width="383" height="23"><br> -->
  <!-- <img src="/assets/pagetitleimages/enrollments.gif" alt="Changing Participant Data" width="220" height="33"><br> -->
    <br>
    <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
     <table width="100%" border="0" cellspacing="0" cellpadding="0">      
      <td width="375" vAlign="top" class="greyText">
      	<img src="/assets/unmanaged/images/s.gif" width="373" height="0"><br>
      	<img src='<content:pageImage type="pageTitle" beanName="SecondLevelBean"/>'><br>
      	<img src="/assets/unmanaged/images/s.gif" width="1" height="20">
      	<div id="errordivcs"><content:errors scope="session"/></div>
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      	  <tr>
      	    <td><p>
      	    	<content:getAttribute attribute="introduction1" beanName="SecondLevelBean"/>
					<content:getAttribute attribute="introduction2" beanName="SecondLevelBean"/><br>
      	    	<!-- Display Second Level Main Links -->
      			<content:secondLevelMainLinks id="SecondLevelBean">
      				<content:param><%=String.valueOf(isBundledIndicator)%></content:param>
      			</content:secondLevelMainLinks>
      		</p></td>
      	  </tr>
   		  <tr>
			<td>
	   		<br>
		 	<p><content:pageFooter beanName="SecondLevelBean"/></p>
 		 	<p class="footnote"><content:pageFootnotes beanName="SecondLevelBean"/></p>
 		 	<p class="disclaimer"><content:pageDisclaimer beanName="SecondLevelBean" index="-1"/></p>
 	 		</td>
   		  </tr>
      	</table>
       </td>
      </table>
  </td> 
  <td valign="top" class="fixedTable"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1"></td>

  <td valign="top">
   <img src="/assets/unmanaged/images/s.gif" width="1" height="25">   <br>        
   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">     <br>
   <content:rightHandLayerDisplay layerName="layer1" beanName="SecondLevelBean" />   
		  
   <img src="/assets/unmanaged/images/s.gif" width="1" height="5"><br>
   <content:rightHandLayerDisplay layerName="layer2" beanName="SecondLevelBean" />   		  

  </td>
  <td><img src="/assets/unmanaged/images/s.gif" width="20" height="312"></td>

</tr>

</table>
<br>

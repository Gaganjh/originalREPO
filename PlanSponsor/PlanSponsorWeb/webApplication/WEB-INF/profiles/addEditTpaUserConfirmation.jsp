<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditUserForm" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);


AddEditUserForm theForm = (AddEditUserForm)session.getAttribute("addEditUserForm");
pageContext.setAttribute("theForm",theForm,PageContext.PAGE_SCOPE);




boolean SHOW_EDIT_EXT_USER_BUTTON=(Boolean) request.getSession(false).getAttribute(Constants.SHOW_EDIT_EXT_USER_BUTTON);
pageContext.setAttribute("SHOW_EDIT_EXT_USER_BUTTON",SHOW_EDIT_EXT_USER_BUTTON,PageContext.PAGE_SCOPE);
%>
 
<jsp:useBean id="addEditUserForm" scope="session"
	class="com.manulife.pension.ps.web.profiles.AddEditUserForm" />

  
<script type="text/javascript">
var submitted=false;

function doFinish() {
	if (!submitted) {
		submitted = true;
		var url = new URL(window.location.href);
		url.setParameter('action', 'finish');
		window.location.href = url.encodeURL();
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return;
	}
}

function editProfile(href){
	if (!submitted) {
		submitted = true;
		window.location.href=href;
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return;
	}
}

var prefsOpen = false;

function toggleSection() {
	var section = document.all["prefs"].style;
	if (prefsOpen == false) {
		section.display = "block";
		prefsOpen = true;
	} else {
	    section.display = "none";
		prefsOpen = false;
	}
}

var plusIcon = "/assets/unmanaged/images/plus_icon.gif";
var minusIcon = "/assets/unmanaged/images/minus_icon.gif";

function expandSection(cid) {
	document.getElementById(cid).style.display=(document.getElementById(cid).style.display!="block")? "block" : "none"
	document.getElementById(cid+"img").src=(document.getElementById(cid).style.display=="block")? minusIcon : plusIcon;
}

function expandAll() {
	expandAllSections();
	
<c:forEach items="${addEditUserForm.tpaFirms}" var="tpaFirm" varStatus="tpaIndex" >





<c:if test="${tpaFirm.removed !=true}">
setIconImage("${tpaFirm.id}img", minusIcon);
</c:if>
</c:forEach>
	
}

function contractAll() {
	contractAllSections();
	
<c:forEach items="${addEditUserForm.tpaFirms}" var="tpaFirm" varStatus="tpaIndex" >





<c:if test="${tpaFirm.removed !=true}">
setIconImage("${tpaFirm.id}img", plusIcon);
</c:if>
</c:forEach>
	
}

function setIconImage(imgId, iconImage) {
	imgField = document.getElementById(imgId);
	if (imgField != null) {
		imgField.src=iconImage;
	}
}

</script>

<%--
Defect 007287: Don't highlight changes in add user confirmation screen.
--%>
<logicext:if name="layoutBean" property="param(task)" op="equal" value="add">
  <logicext:then>
<style>
.highlightBold {
	color : #000000;
	font-weight: normal;
}
</style>
  </logicext:then>
</logicext:if>

<table border=0 cellSpacing=0 cellPadding=0>
  
      <TR class=tablehead>
        <TD class=tableheadTD1 colSpan=3><B>User profile </B></TD>
      </TR>
      <TR>
        <TD class=boxborder width=1><IMG height=1 
              src="/assets/unmanaged/images/s.gif" 
              width=1></TD>
        <TD>
        <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
            <TBODY>
              <TR class="datacell1">
                <td width="70"><strong>First name </strong></td>
                <TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
                <TD width="100">
                  <ps:highlightIfChanged name="addEditUserForm" property="firstName">
${addEditUserForm.firstName}
				  </ps:highlightIfChanged>
                </TD>
                <td width="70"><strong>Primary Email </strong></td>
                <TD class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
                <TD width="135">
                  <ps:highlightIfChanged name="addEditUserForm" property="email">
${addEditUserForm.email}
				  </ps:highlightIfChanged>                  
                </TD>                
              </TR>
              <TR class="datacell1">
                <td width="70"><strong>Last name </strong></td>
                <TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
                <TD width="100">
                  <ps:highlightIfChanged name="addEditUserForm" property="lastName">
${addEditUserForm.lastName}
				  </ps:highlightIfChanged>
                </TD>
                <td width="70"><strong>Secondary Email </strong></td>
                <TD class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
                <TD width="135">
<c:if test="${not empty addEditUserForm.secondaryEmail}">
${addEditUserForm.secondaryEmail}
</c:if>
                </TD> 
                              
              </TR>
              <TR class="datacell1">
                <td width="100"><strong>Social Security Number </strong></td>
                <TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
                <TD width="135">
                  <ps:highlightIfChanged name="addEditUserForm" property="ssn">
                  <%int index =  ((AddEditUserForm)pageContext.getAttribute("theForm")).getSsn().toString().length(); 
                  if (index > 0) { %>
                 					<c:if test="${userProfile.role.roleId ne 'ICC'}">
									<render:fullmaskSSN property="addEditUserForm.ssn" />
								</c:if>
								<c:if test="${userProfile.role.roleId eq 'ICC'}">
									<render:ssn property="addEditUserForm.ssn" />
								</c:if>
								<% } %>
                  </ps:highlightIfChanged>
                </TD>
                
                 
                
                <td width="100"><strong>Telephone number </strong></td>
                <TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
                <TD width="135">
				  	<ps:highlightIfChanged name="addEditUserForm" property="phone">
				  	<c:if test="${addEditUserForm.phone !='' }">
	                     <render:phone property="addEditUserForm.phone"/>
</c:if>
                  </ps:highlightIfChanged>
                    	<c:if test="${addEditUserForm.ext !='' }">
                   ext 
</c:if>
                  <ps:highlightIfChanged name="addEditUserForm" property="ext">                     
${addEditUserForm.ext}
                  </ps:highlightIfChanged>
                </TD>   
               
                </TR>
                <TR class="datacell1">
                <td width="70"><strong>Web access </strong></td>
                <TD class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
                <TD width="100">
                  <ps:highlightIfChanged name="addEditUserForm" property="webAccess">
	                  <logicext:if name="addEditUserForm" property="webAccess" op="equal" value="true">
	                  	<logicext:then>yes</logicext:then>
					    <logicext:else>no</logicext:else>
					  </logicext:if>
				  </ps:highlightIfChanged>                  
                </TD>
                 <td width="70"><strong>Fax number </strong></td>
                <TD class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
                <TD width="100">
                  <ps:highlightIfChanged name="addEditUserForm" property="fax">
                    	<c:if test="${addEditUserForm.fax !='' }">
                     <render:fax property="addEditUserForm.fax"/>
</c:if>
                  </ps:highlightIfChanged>
                </TD> 
                
              </TR>

				</TBODY>
        </TABLE></TD>
        <TD class=boxborder width=1><IMG height=1 
              src="/assets/unmanaged/images/s.gif" 
              width=1></TD>
      </TR>
      <TR>
        <TD colSpan=3>
        <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
            <TBODY>
              <TR>
                <TD width="1" class=boxborder><IMG height=1 
                    src="/assets/unmanaged/images/s.gif" 
                    width=1></TD>
                <TD class=boxborder><IMG height=1 
                    src="/assets/unmanaged/images/s.gif" 
                    width=1></TD>
              </TR>
            </TBODY>
        </TABLE>
        </TD>
      </TR>
</TABLE>
  
  <br/>
  
  <table>
  <c:if test="${addEditUserForm.webAccess}"> 
    <td align="right" onclick="toggleSection();">
       <a href="#">Email preferences</a>&nbsp;<img src="/assets/unmanaged/images/layer_icon.gif" width="17" height="9">
    </td>
   </c:if>
  </table>
  

<logicext:if name="layoutBean" property="param(task)" op="equal" value="add">
  <logicext:then>
<c:set var="actionPath" value="/do/profiles/addTpaUser/" />
  </logicext:then>
  <logicext:else>
<c:set var="actionPath" value="/do/profiles/editTpaUser/" />
  </logicext:else>
</logicext:if>

<%-- <jsp:useBean id="actionPath" type="java.lang.String"/> --%>


<%-- <ps:form cssClass="margin-bottom:0;" method="POST" action="${actionPath}" name="addEditUserForm" modelAttribute="addEditUserForm">--%>

   <jsp:include page="viewEmailPreferences.jsp" flush="true"/>     
   <br/>
   <c:if test="${SHOW_EDIT_EXT_USER_BUTTON ==true}">
	   <TABLE cellSpacing=0 cellPadding=0 width="412" border=0>
		 <TR>
		  <TD width="357" align=right>
			<div align="left">
			<STRONG>
			   <A href="javascript:expandAll()"><img src="/assets/unmanaged/images/plus_icon_all.gif" border="0"></A>/
			   <A href="javascript:contractAll()"><img src="/assets/unmanaged/images/minus_icon_all.gif" border="0"></A> All Sections 
			</STRONG>
			</div></TD>
		  </TR>
	   </TABLE>
	   <br/>
	
<c:if test="${userProfile.isInternalUser() ==true}">
			<jsp:include page="viewTpaUserAccessSectionInternal.jsp" flush="true"/>     
</c:if>
	   
<c:if test="${userProfile.isInternalUser() eq false}"> 

			<jsp:include page="viewTpaUserAccessSection.jsp" flush="true"/>     
			
</c:if>
</c:if>
   
   <br/>
   
   <table width=450 cellSpacing=0 cellPadding=0 border=0>
       <tr >
            <td>
            <%
            String name =  ((AddEditUserForm)pageContext.getAttribute("theForm")).getUserName(); 
            pageContext.setAttribute("name",name,PageContext.PAGE_SCOPE);
            %>
            
            <c:if test="${SHOW_EDIT_EXT_USER_BUTTON ==true}">
               			<input type=button class="button100Lg" value="edit this user"
   	 						   onclick="editProfile('/do/profiles/editTpaUser/?userName=${name}')" >
</c:if>
    	 	</td><td>			
<input type="submit" onclick="javascript:window.print()" name="print" class="button100Lg" value="print"/>


            </td><td>	
    	 				
<input type="submit" onclick="doFinish()" name="finish" class="button100Lg" value="finish"/>


            </td>
        </tr>
    </table>  
   
   <br/>

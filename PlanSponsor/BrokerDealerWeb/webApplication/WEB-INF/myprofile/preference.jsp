<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>


<%@page import="com.manulife.pension.bd.web.messagecenter.BDMessageCenterFacade"%>


<%@page import="com.manulife.pension.bd.web.messagecenter.BDMessageCenterUtils"%>
<%@page import="com.manulife.pension.bd.web.messagecenter.BDMessageCenterMessageTemplate" %>
<script type="text/javascript">
   function changeReceiveMessage(chkBox, tid) {
	   if (!chkBox.checked) {
		   name='recivemsgList['+tid+'].receiveMessage';
		   document.forms['myprofilePreferenceForm'].elements[name].checked = false;
	   }
   } 
   
   function changeEmailNotification(chkBox, tid) {
	   if (chkBox.checked) {
		   name='recivemsgList['+tid+'].emailNotification';
		   document.forms['myprofilePreferenceForm'].elements[name].checked = true;
	   }
   } 

</script>

<content:contentBean contentId="<%=BDContentConstants.MY_PROFILE_DEFAULT_FUND_LISTING_TITLE%>" 
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="fundListingTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_LICENSE_VERIFICATION_TEXT%>" 
 	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="licenseText" />
 	
<content:contentBean contentId="<%=BDContentConstants.MESSAGE_CENTER_PREF_SECTION_TITLE%>" 
   	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="messagePrefSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.MESSAGE_CENTER_PREF_SECTION_DESC%>" 
   	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="messagePrefSectionDesc" />
<content:contentBean contentId="<%=BDContentConstants.OTHER_PREF_SECTION_TITLE%>" 
   	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="otherPrefSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.MESSAGE_CENTER_PREF_EMAIL_TITLE%>" 
	type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="mcEmailSectionTitle" />

<c:set var="form" value="${requestScope.myprofilePreferenceForm}" scope="page"/>
<utils:cancelProtection name="myprofilePreferenceForm" changed="${form.changed}"
     exclusion="['action']"/>

<div id="contentFull">
	<layout:pageHeader nameStyle="h1"/>

<c:if test="${form.success}">
	<utils:info contentId="<%=BDContentConstants.MY_PROFILE_SAVE_SUCCESS_MESSAGE_TEXT%>"/> 
</c:if>

<%
    BDSessionHelper.moveMessageIntoRequest(request);
%>
<report:formatMessages scope="request"/>

<userprofile:myprofileTab/>

<bd:form action="/do/myprofile/preference" modelAttribute="myprofilePreferenceForm" name="myprofilePreferenceForm">

<input type="hidden" name="action"/>

<%-- If there is message center preference --%>
<c:if test="${myprofilePreferenceForm.hasMessagePreference}">

<c:set var="templates" value="<%=BDMessageCenterFacade.getInstance().getMessageTemplates()%>"/>

<div class="BottomBorder">
  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="messagePrefSectionTitle"/></div>
  <div align="right"><a href="/do/messagecenter/">Message Center</a></div>
</div>

<div style="margin-top:10px"><content:getAttribute attribute="text" beanName="messagePrefSectionDesc"/></div>
<br />
<table width="88%" class="report_table_content" >
  <thead>
    <tr>
      <th width="54%" valign="bottom" nowrap="nowrap" class="val_str"><h2>Message Type</h2></th>
      <th width="23%" valign="bottom" class="val_str"><h2>Receive these messages</h2></th>
      <th width="23%" valign="bottom" class="val_str"><h2>Also receive email notifications</h2></th>
    </tr>
  </thead>
  <tbody>
  
   <c:forEach var="t" items="${templates}" varStatus="indexVal">                  

<%

BDMessageCenterMessageTemplate t =(BDMessageCenterMessageTemplate)pageContext.getAttribute("t");


%> 
    <tr class="spec">
      <td class="date">
       <h2>
        <label>${t.shortText}</label>
       </h2>
      </td>
      <td>
	  <%boolean disableReceiveMessage = false;
	    boolean disableReceiveNotification = false;
	  if(BDMessageCenterUtils.isFundCheckMessage(t.getTemplateId())){
	  disableReceiveNotification=true;
	  }
	  if(BDMessageCenterUtils.isFundCheckMessage(t.getTemplateId())
	  || BDMessageCenterUtils.isGlobalMessage(t.getTemplateId())){
	    disableReceiveMessage=true;
	  }
	  pageContext.setAttribute("disableReceiveMessage", disableReceiveMessage);
	  pageContext.setAttribute("disableReceiveNotification", disableReceiveNotification);
	  %>
	  
	    
      <h2 align="center">
      	<form:checkbox path="recivemsgList[${indexVal.index}].receiveMessage" 
          disabled="${disableReceiveMessage}"
          onclick="changeReceiveMessage(this,${indexVal.index})"
          value="true" 
          />
        </h2>                 
      </td>
      <td>
       <h2 align="center">
          <form:checkbox path="recivemsgList[${indexVal.index}].emailNotification" 
             disabled="${disableReceiveNotification}" 
             onclick="changeEmailNotification(this, ${indexVal.index})"
             value="true"
             />
       </h2>

      </td>
    </tr>       
   </c:forEach>
  </tbody>
</table>
<div class="label"><content:getAttribute attribute="text" beanName="mcEmailSectionTitle"/></div>
<div class="inputText">
  <label></label>
  <p>
    <label>
<form:radiobutton path="summaryEmail" value="true"/>
	Yes 
	</label>
    <label>
<form:radiobutton path="summaryEmail" value="false"/>
	No</label>
 </p>
    <br />
</div>

<br/>
<div class="BottomBorder">
  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="otherPrefSectionTitle"/></div>
</div>
</c:if>

<p> <content:getAttribute attribute="text" beanName="fundListingTitle"/> </p>

<span class="RadioLabel">*&nbsp;Default Fund Listing:</span>
<span class="RadioValues">
  <label>
<form:radiobutton path="defaultSiteLocation" id="USA" value="USA"/>
	  USA
  </label>
  <label>
<form:radiobutton path="defaultSiteLocation" id="NY" value="NY"/>
	  New York
  </label>
</span>
<br />
<br />

   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Save"
	        onclick="return doProtectedSubmitBtn(document.myprofilePreferenceForm, 'save', this)">
        </div> 

    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Close"
             onclick="return doCancelBtn(document.myprofilePreferenceForm, this)"> 
    </div>
</bd:form>
</div>

<layout:pageFooter/>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>


<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@tag import="com.manulife.pension.bd.web.messagecenter.BDMessageCenterFacade"%>
<%@tag import="com.manulife.pension.bd.web.messagecenter.BDMessageCenterMessageTemplate" %>
<%@tag import="com.manulife.pension.service.security.bd.valueobject.BDMessagePreference" %>

<%@attribute name="preferences" type="com.manulife.pension.service.security.bd.valueobject.BDUserMessageCenterPreferences" 
   required="true"%>

<content:contentBean contentId="<%=BDContentConstants.MESSAGE_CENTER_PREF_SECTION_TITLE%>" 
                     type="<%=BDContentConstants.TYPE_MESSAGE%>" 
                     id="mcPrefSectionTitle" />
                     
<content:contentBean contentId="<%=BDContentConstants.MESSAGE_CENTER_PREF_EMAIL_TITLE%>" 
					type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" 
					id="mcEmailSectionTitle" />

<c:set var="templates" value="<%=BDMessageCenterFacade.getInstance().getMessageTemplates()%>"/>


<div class="BottomBorder">
	<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="mcPrefSectionTitle"/></div>
</div>	 

<br />

<table width="88%" class="report_table_content" >
      <thead>
        <tr>
          <th width="54%" valign="bottom" nowrap="nowrap" class="val_str"><h2>Message Type</h2></th>
          <th width="23%" valign="bottom" class="val_str"><h2>Receive these messages </h2></th>
          <th width="23%" valign="bottom" class="val_str"><h2>Also receive email notifications</h2></th>
        </tr>
      </thead>
      <tbody>
       <c:forEach var="t" items="${templates}">
              
               <% BDMessageCenterMessageTemplate t = (BDMessageCenterMessageTemplate)jspContext.getAttribute("t");
               BDMessagePreference preference =(BDMessagePreference) preferences.getMessagePreference(t.getTemplateId());
            	jspContext.setAttribute("preference", preference);
                %> 
        <tr class="spec">
          <td class="date">
           <h2>
            <label>${t.shortText}</label>
           </h2>
          </td>
          <td>
            <h2 align="center">
             <c:if test="${preference.receiveMessage}">
				<input type="checkbox" name ="receiveMessage"  value="${preference.receiveMessage}"  disabled="disabled"  checked />
			</c:if>
			<c:if test="${preference.receiveMessage!=true}">
				<input type="checkbox" name ="receiveMessage"  value="${preference.receiveMessage}"  disabled="disabled"  />
			</c:if>
            
            </h2>                 
          </td>
          <td>
           <h2 align="center">
           
            <c:if test="${preference.emailNotification==true}">
				<input type="checkbox" name ="receiveMessage" value="${preference.emailNotification}" disabled="disabled" checked/>
			</c:if >
			<c:if test="${preference.emailNotification!=true}">
				<input type="checkbox" name ="receiveMessage" value="${preference.emailNotification}" disabled="disabled" />
			</c:if>
              
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
  			 <c:if test="${preference.emailNotification==true}">
				<input type="radio" name ="preferences" value="${preferences.receiveSummaryEmail}" disabled="disabled" checked="checked"/>
			</c:if>
			<c:if test="${preference.emailNotification!=true}">
				<input type="radio" name ="preferences" value="${preferences.receiveSummaryEmail}" disabled="disabled" />
			</c:if>
     
	Yes 
	</label>
    <label>
  
    <input type="radio" name ="preferences"  disabled="disabled" value="false" />
	No
	</label>
 </p>
    <br />
</div>
                    
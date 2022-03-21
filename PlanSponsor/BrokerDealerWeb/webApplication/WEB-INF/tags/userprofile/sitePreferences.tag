
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- 
   This tag is used to display the USA/New York radio buttons for 
   Fund Listing Preferences.
--%>
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_PREFERENCES_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="preferencesText" />


<div class="inputTextFull">
	<content:getAttribute attribute="text" beanName="preferencesText"/>
</div>
<div class="label">*&nbsp;Default Fund Listing:<br /></div>
<div class="inputText">
  <label>
	  <form:radiobutton styleId="USA" path="defaultSiteLocation" value="USA"/>
	  USA
  </label>
  <label>
	 <form:radiobutton styleId="NY" path="defaultSiteLocation" value="NY"/>
	  New York
  </label>
   <br/>
</div>

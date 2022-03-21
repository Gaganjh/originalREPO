<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<content:contentBean
	contentId="80833"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="saveButtonHoverOverText" /> 
	
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
	
<ps:isExternal name="userProfile" property="role"> 
<div class="buttonGroup">         
<input type="submit" class="button100Lg" onclick="return handleCancelClicked();" value="cancel &amp; exit" name="actionLabel"/>



            
<input type="submit" class="button100Lg" onclick="return handleSaveClicked();" value="save &amp; exit" name="actionLabel"/>



         
<input type="submit" class="button100Lg" onclick="return handleSaveClicked();" value="save" name="actionLabel" />



           
<input type="submit" class="button100Lg" onclick="return handleSubmitClicked();" value="submit" name="actionLabel" />



<div>   
</ps:isExternal>
<ps:isInternalUser name="userProfile" property="role">
<div class="buttonGroup">          

         <c:set var="saveMouseOverText">
            Tip('<content:getAttribute beanName="saveButtonHoverOverText" attribute="text" escapeJavaScript="true"/>')
         </c:set>
<input type="submit" class="button110Lg" onclick="return handleCancelClicked();" value="cancel &amp; exit" name="actionLabel" />



                       
<input type="submit" class="button110Lg" onclick="return false;" onmouseout="UnTip()" onmouseover="${saveMouseOverText}" value="save &amp; exit" name="actionLabel"  />





            
<input type="submit" class="button110Lg" onclick="return false;" onmouseout="UnTip()" onmouseover="${saveMouseOverText}" value="save" name="actionLabel" />





<input type="submit" class="button110Lg" onclick="return false;" onmouseout="UnTip()" onmouseover="${saveMouseOverText}" value="submit" name="actionLabel" />





<div>  
</ps:isInternalUser> 

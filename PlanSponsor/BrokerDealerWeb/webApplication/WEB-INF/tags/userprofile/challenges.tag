<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- 
   This tag is used for displaying two challenge question
   The question/answer inputs are in the challengeInput tag
--%>

<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_CHALLENGE_QUESTIONS_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="challengeTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_CHALLENGE_QUESTIONS_SECTION_DESCRIPTION%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="challengeDescription" />

<%@attribute name="form" type="com.manulife.pension.ezk.web.ActionForm" required="true" rtexprvalue="true"%>

<script type="text/javascript">
<!--
<%-- This function is to show/hide the create your own question input field
     The question id '0' corresponds the the create your own question --%> 
function selectChallengeQuestion(selection, questionText, divElement) {
	if (selection.value=='0') {
		divElement.style.display="";
	} else {
		divElement.style.display="none";
		selection.form.elements[questionText].value='';
	}
}
<%-- Function to clear the answer fields when the question changes. --%>
function clearAnswers(selection, elementPrefix) {
	selection.form.elements[elementPrefix+'.answer'].value='';
	selection.form.elements[elementPrefix+'.confirmedAnswer'].value='';
}

//-->
</script>


<div class="BottomBorder">
  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="challengeTitle"/>
</div>
</div>
<div class="regSection">
        <div style="height:1px"> </div>    
<div class="inputTextFull">
  <label>
	  <content:getAttribute attribute="text" beanName="challengeDescription"/>
  </label>
</div>

<userprofile:challengeInput questionNum="1" value="${form.challenge1}"/>

<userprofile:challengeInput questionNum="2" value="${form.challenge2}"/>
</div>
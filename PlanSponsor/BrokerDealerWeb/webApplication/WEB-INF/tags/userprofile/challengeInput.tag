<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@ tag import="com.manulife.pension.bd.web.registration.RegisterRiaUserStep2Form"%>
<%-- 
   This tag is used for displaying challenge question/answers
   The questioNum attribute should be either '1' or '2'
--%>



<%@attribute name="questionNum" type="java.lang.String" required="true"%>
<%@attribute name="value" type="com.manulife.pension.bd.web.registration.util.PasswordChallengeInput" required="true"%>
<%@attribute name="update" type="java.lang.String" required="false"%>


<c:choose>
  <c:when test="${empty update || update=='false'}">
     <c:set var="mandatoryInd" value="*"/>
  </c:when>
  <c:otherwise>
     <c:set var="mandatoryInd" value=""/>
  </c:otherwise>
</c:choose>
<c:choose>

<c:when test="${questionNum =='1'}">
  <c:set var="questionLabel">1<sup>st</sup></c:set>
  <c:set var="challengeName" value="challenge1"/>
  <c:set var="challengeNameqa" value="${value.questionList}"/>
  <c:set var="createQName" value="creatOwnQuestion1"/>
  <c:set var="customQuestionId" value="0"/>
</c:when>
  <c:otherwise>
  <c:set var="questionLabel">2<sup>nd</sup></c:set>
  <c:set var="challengeName" value="challenge2"/>
   <c:set var="challengeNameqa" value="${value.questionList}"/>
  <c:set var="createQName" value="creatOwnQuestion2"/>
  <c:set var="customQuestionId" value="0"/>
  </c:otherwise>
</c:choose>
<c:choose>
 <c:when test="${value.questionId!='0'}">
   <c:set var="createOwnQuestionStyle" value="display:none"/>
 </c:when>
 <c:otherwise>
   <c:set var="createOwnQuestionStyle" value=""/>
 </c:otherwise>
</c:choose>

  <div class="label">${mandatoryInd} ${questionLabel} Challenge Question:</div>
  <div class="inputText">
    <label>
          <form:hidden path="${challengeName}.questionId"  value = "${customQuestionId}" />
	      <form:input path="${challengeName}.customizedQuestionText" cssClass="input" size="50" maxlength="100"/>
	</label>
  </div>
  <div class="label">${mandatoryInd} Answer:</div>
  <div class="inputText">
     <label>
     <form:password path="${challengeName}.answer" showPassword="true" cssClass="input" size="32" maxlength="32"/>
    </label>
  </div>
  
  <div class="label">${mandatoryInd} Confirm Answer:</div>
  <div class="inputText">
     <label>
     <form:password path="${challengeName}.confirmedAnswer" showPassword="true" cssClass="input" size="32" maxlength="32"/>
    </label>
  </div>

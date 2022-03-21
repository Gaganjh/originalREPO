<%@ attribute name="showLabel" type="java.lang.Boolean" rtexprvalue="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@tag import="com.manulife.pension.bd.web.BDConstants"%>

<%-- 
   This tag is used to display the username password section.
--%>

<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_USERNAME_PASSWORD_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="userNamePasswordSectionTitle" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_USERNAME_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="userNameText" />

<div class="BottomBorder">
	<div class="SubTitle Gold Left">
	     <content:getAttribute attribute="text" beanName="userNamePasswordSectionTitle"/>
	</div>
	<c:if test="${showLabel}">
		<div class="GrayLT Right">* = Required Field</div>
		<div id="deapistatusdiv" class="message message_error" style="display: none; color:red;">
         <content:getAttribute attribute="text"	beanName="DeapiErrorMessage">
		 </content:getAttribute>
         </div>
	</c:if>
</div>
<div class="regSection">

	<p>
		<content:getAttribute attribute="text" beanName="userNameText" />
	</p>
	<div class="label">* Username:</div>
	<div class="inputText">
		<label> <br /> <form:input id="userName" cssClass="input"
				path="userCredential.userId" maxlength="20" />
		</label>
	</div>
	<div class="label">* Password:</div>

	<div class="inputText">
		<label> <form:password cssClass="input"
				path="userCredential.password" id="newPassword" maxlength="64" />
		</label> <br />
		<!--  Your password must contain at least 5 characters. -->

		<div>
			</br>
			<div id="defaulttxt" style="display: none;color:black;"><span class ="defaulttxt"></span>&nbsp;Passwords MUST meet all of the following criteria before you can continue:</div>
			<div id="fstmsg" style="display: none;color:black;">
				<span class="one"></span>&nbsp;Be between 8-64 characters
			</div>
			<div id="secmsg" style="display: none;color:black;">
				<span class="two"></span>&nbsp;Have at least 1 uppercase letter
			</div>
			<div id="thmsg" style="display: none;color:black;">
				<span class="three"></span>&nbsp;Have at least 1 lowercase
				letter
			</div>
			<div id="formsg" style="display: none;color:black;">
				<span class="four"></span>&nbsp;Have at least 1 number
			</div>
			<div id="fifthmsg" style="display: none;color:black;">
				<span class="five"></span>&nbsp;Have at least 1 special
				character
			</div>
			<div id="seventhmsg" style="display: none;color:black;">
				<span class="seven"></span>&nbsp;Not contain industry related
				words
			</div>
			<div id="eithmsg" style="display: none;color:black;">
				<span class="eith"></span>&nbsp;Not contain username
			</div>
			<div id="ninethmsg" style="display: none;color:black;">
				<span class="nineth"></span>&nbsp;Not contain repeating or sequential characters
			</div>
		</div>
	</div>

	<div class="label">* Confirm Password:</div>

	<div class="inputText">
		<label> <form:password cssClass="input" id="confirmPassword"
				path="userCredential.confirmedPassword" maxlength="64" />
		</label> <br />
	</div>

	<div class="inputText">
		<p id="pwdvalidation" style="font-size: 11; color: red;"></p>
	</div>
</div>

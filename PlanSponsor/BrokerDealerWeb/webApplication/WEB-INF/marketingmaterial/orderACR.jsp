<%--  Imports  --%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<%-- Tab Libraries Used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>

<%-- Constant Files used--%>
<un:useConstants var="contentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />


<%-- Beans Used --%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<content:contentBean contentId="${contentConstants.ORDER_ACR_PAGE_NOTICE}"
                     type="${contentConstants.TYPE_MISCELLANEOUS}"
                     id="pageNotice"/>
<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_ACR_CONFIRMATION_TEXT%>"
        			 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        			 id="acrConfirmation"/> 
<content:contentBean contentId="<%=BDContentConstants.ORDER_ACR_CONFIRMATION%>"
        			 type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        			 id="acrConfirmationHeading"/>        			                       
<script type="text/javascript">

function doOnload(){
	var submitValue = "<%= request.getAttribute("orderSubmit") %>";
	if(submitValue=="submitted"){
		document.getElementById("content").style.visibility="hidden";
		document.getElementById("contentSubmit").style.visibility="visible";
	}
	else if(submitValue=="Notsubmitted"){
		document.getElementById("contentSubmit").style.visibility="hidden";
		document.getElementById("content").style.visibility="visible";
	}
}
function doRefresh() {
	  document.orderACRForm.action = "/marketingmaterial/orderACR.jsp/";
	  document.forms.orderACRForm.submit();
	  // submit done by fact input = default page   
	}

window.onload = doOnload;

</script>

<div id="rightColumn2">
<h2><content:getAttribute attribute="title" beanName="pageNotice"/></h2>
<p>
<content:getAttribute attribute="text" beanName="pageNotice"/>
</p>
</div>

<div id="content">

    <bd:form action="/orderACR/" method="post" modelAttribute="orderACRForm" neme="orderACRForm">

	<h1><content:getAttribute id="layoutPageBean" attribute="name" /></h1>
	<p>
	
	<%--Layout/intro1--%> 
	<c:if test="${not empty layoutPageBean.introduction1}">
		<content:getAttribute beanName="layoutPageBean" attribute="introduction1" />
</c:if>

	<%--Layout/Intro2--%> 
	<c:if test="${not empty layoutPageBean.introduction2}">
		<content:getAttribute beanName="layoutPageBean" attribute="introduction2" />
</c:if>

    </p>

	<%-- Error- message box --%>
	<%
    	BDSessionHelper.moveMessageIntoRequest(request);
	%>
	<report:formatMessages scope="request"/>

    <div class="BottomBorder">
    <div class="Red Right">* Required field</div>
	<div class="SubTitle Gold Left">
	<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
    </div>
	</div>

	<%-- Contract Name --%>
	<div class="label">*Contract Name:</div>
	<div class="inputText">
<form:input path="contractName" size="25" cssClass="input" id="contract22" />
    </div>

	<%-- Contract Number --%>
	<div class="label">*Contract Number:</div>
	<div class="inputText">
    <label> 
<form:input path="contractNumber" size="5" cssClass="input" id="contract2" />
    </label>
     <br />
	</div>

	<%-- Report Month End Date Drop Down --%>
	<div class="label">*Report Month-End Date:</div>
	<div class="inputText">
    <label> 
<form:select path="orderACRForm" cssClass="input" >
		<form:options name="orderACRForm" property="monthValueList" labelProperty="monthList" />
</form:select>
    </label> 
    
    <%-- Report Year Drop Down --%> 
    <label> 
<form:select path="orderACRForm" cssClass="input" >
		<form:options name="orderACRForm" property="yearList" />
</form:select>
    </label>
    <br />
	</div>

	<%-- Presenter Name --%>
	<div class="label">*Presenter's First/Last Name:</div>
	<div class="inputText">
    <span id="sprytextfield1">
    <label>
<form:input path="presenterFirstName" size="25" cssClass="input" id="contract5" />
    </label>
    </span> 
    <span id="sprytextfield2">
<form:input path="presenterLastName" size="25" cssClass="input" id="contract4" />
    </span>
    </div>

	<div class="BottomBorder">
	<div class="SubTitle Gold Left">
	<content:getAttribute beanName="layoutPageBean" attribute="body1"/>
    </div>
	</div>

	<%-- Number of Color Copies --%>
	<div class="label">*Color:</div>
	<div class="inputText">
    <label>
<form:input path="numberOfColorCopies" size="5" cssClass="input" id="contract2" />
    </label>
    </div>

    <%-- Number of B & W copies --%>
	<div class="label">*Black &amp; White:</div>
	<div class="inputText">
    <label>
<form:input path="numberOfBlackAndWhiteCopies" size="5" cssClass="input" id="contract2" />
    </label>
    </div>
    
    <div class="label">Output:</div>
	<div class="inputText">
    <label> 
<form:select path="orderACRForm" cssClass="input" >
		<form:options name="orderACRForm" itemValue="outputTypeList" itemLabel="outputTypeList" />
</form:select>
    </label>
    </div>

	<div class="BottomBorder">
	<div class="SubTitle Gold Left">
    <content:getAttribute beanName="layoutPageBean" attribute="body2Header"/>
    </div>
	</div>

	<%-- Recipient Name --%>
	<div class="label">*Recipient's First/Last Name:</div>
	<div class="inputText">
    <span id="sprytextfield3">
    <label>
<form:input path="recipientFirstName" size="25" cssClass="input" id="firstname2" />
    </label>
    </span>
    <span id="sprytextfield4">
    <label>
<form:input path="recipientLastName" size="25" cssClass="input" id="lastname2" />
    </label>
    </span>
    </div>

    <%-- Company  Name --%>
	<div class="label">*Company Name:</div>
	<div class="inputText">
    <label>
<form:input path="companyName" size="25" cssClass="input" id="firstname" />
    </label>
    </div>

    <%-- Street Address --%>
	<div class="label">*Street Address:</div>
	<div class="inputText">
    <label>
<form:input path="streetAddressName" size="30" cssClass="input" id="address" />
    </label>
    </div>

    <%-- City Name --%>
	<div class="label">*City:</div>
	<div class="inputText">
    <label> 
<form:input path="cityAddressName" cssClass="input" id="city" />
    </label>
    </div>

    <%-- State Name --%>
	<div class="label">*State:</div>
	<div class="inputText">
    <label> 
<form:input path="stateAddressName" cssClass="input" id="city" />
    </label>
    </div>

    <%-- Zip Code --%>
	<div class="label">*Zip Code:</div>
	<div class="inputText">
    <label> 
<form:input path="zipCodeAddress" size="10" cssClass="input" id="zip" />
    </label>
    </div>

	<div class="BottomBorder">
	<div class="SubTitle Gold Left">
    <content:getAttribute beanName="layoutPageBean" attribute="body3Header"/>
    </div>
	</div>

    <%-- Your Name --%>
	<div class="label">*Your First/Last Name:</div>
	<div class="inputText">
    <label>
<form:input path="yourFirstName" size="25" cssClass="input" id="firstname3" />
    </label>
    <label>
<form:input path="yourLastName" size="25" cssClass="input" id="lastname" />
    </label>
    </div>

    <%-- Telephone Number --%>
	<div class="label">*Your Telephone Number:</div>
	<div class="inputText">
<form:input path="areaCode" size="5" cssClass="input" id="email2" />
<form:input path="subscriberNumber1" size="5" cssClass="input" id="email2" />
<form:input path="subscriberNumber2" size="5" cssClass="input" id="email2" />
    <label>ext. 
<form:input path="phoneNumberExtension" size="7" cssClass="input" id="Extension" />
    </label>
    </div>

    <%-- Email Address --%>
	<div class="label">*EMail Address:</div>
	<div class="inputText">
    <label>
<form:input path="contactEmailAddress" size="25" cssClass="input" id="email" />
    </label>
    </div>

    <%-- Comments --%>
	<div class="label">Comments</div>
	<div class="inputText">
<form:textarea path="comments" cols="50" rows="5" ></form:textarea>
    </div>

<input type="hidden" name="action"/>

	<div class="nextButton">
	<div id="regButtons">
	<div id="regButtonsR">
    <a href="#submit" onclick="return doProtectedSubmit(document.orderACRForm, 'sendMail', this)">Submit</a>
    </div>
	</div>
	</div>
	</bd:form>
</div>
<div id="contentSubmit"  style="visibility: hidden;position: absolute; top: 240px; ">
	<div id="content">
		<h1>
			<content:getAttribute id="acrConfirmationHeading" attribute="text" />
		</h1>
		<%--Layout/Successful Message--%>
		<p>
			<content:getAttribute beanName="acrConfirmation" attribute="text"/>
		</p>	
			<div class="nextButton">
			<div id="regButtonsLong">
			<div id="regButtonsLongR">
		    <a href="" onclick="javascript:doRefresh();">Submit Another Order</a>
			</div>    
			</div>
			</div>
	</div>		
</div>


<!-- FootNotes and Disclaimer -->
<div class="footnotes">
		<dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
		<dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
		<div class="footnotes_footer"></div>
</div> 
<!--end of footnotes-->




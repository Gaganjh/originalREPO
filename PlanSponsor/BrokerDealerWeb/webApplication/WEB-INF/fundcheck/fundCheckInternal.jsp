<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<content:contentBean contentId="<%=BDContentConstants.FUNDCHECK_ADDITIONAL_RESOURCES_LAYER%>"
                     type="<%=BDContentConstants.TYPE_LAYER%>"
                     id="additionalResources"/>                
<content:contentBean contentId="<%=BDContentConstants.FUNDCHECK_NO_PDFS_EXISTS_MESSAGE%>"
						type="<%=BDContentConstants.TYPE_MESSAGE%>"
						id="NoPDFsExistMessage" />	
<content:contentBean contentId="<%=BDContentConstants.NO_SEARCH_RESULT_FOUND%>"
						type="<%=BDContentConstants.TYPE_MESSAGE%>"
						id="NosearchResult" />    
<content:contentBean contentId="<%=BDContentConstants.PLEASE_ADD_MORE_FILTERS%>"
						type="<%=BDContentConstants.TYPE_MESSAGE%>"
						id="PleaseAddMoreFilters" />						                 

<style type="text/css"> 
<!--
 
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td .siteMapTitle {
    font-family: Georgia, "Times New Roman", Times, serif;
    font-size: 16px;
    color: #000;
    font-weight: normal;
    margin-bottom: 0px;
    padding-bottom: 0px;   
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul li a {
    list-style-type: none;
    list-style-image: none;
    font-family: Verdana, Geneva, sans-serif;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul li a:hover {
    list-style-type: none;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul {
    list-style-type: none;
    padding-left: 0px;
    padding-top: 0px;
    margin-top: 0px;
    padding-bottom: 0px;
    margin-left: 10px;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul li {
    color: #666;
    margin: 0px;
    padding: 0px;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 {
    background-color: #f5f6f1;
    border-top-width: 4px;
    border-top-style: solid;
    border-top-color: #1F2B38;
    margin: 0px 0px 0px 0px;
    margin-top: 50px;
	padding: 20px;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 p {
    width:550px;
}
 
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul li a{
    text-decoration:underline !important;
	color:#005B80 !important;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn1 h5 .date {
    font-size: 11px;
    font-weight: normal;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn1 {
    float: right;
    padding: 10px; /* Sets the padding properties for an element using shorthand notation (top, right, bottom, left) */
    width: 230px;
    color: #FFFFFF;
    background-color: #455560;
    margin-top: 0px;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn1 h2 {
	font-family: Georgia, "Times New Roman", Times, serif;
	font-size: 1.1em;
	font-weight: normal;
	padding: 0px;
	color: #dcd087;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn1 a {
    font-size: 11px;
    color: #FFF;
    text-decoration: underline;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn1 a:hover {
    font-size: 11px;
    color: #dcd087;
    text-decoration: underline;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn1 .selected {
    font-family: Arial, Helvetica, sans-serif;
    font-size: 12px;
    font-weight: bold;
}
 
-->
	/*  Define the background color for all the ODD table columns  */
	.ipuTableColumn tr td:nth-child(odd){ 
		background-color: #bed5e6;
		border: 1px solid #005B80;
	}
	/*  Define the background color for all the EVEN table columns  */
	.ipuTableColumn tr td:nth-child(even){
		background-color: #eceae3;
		border: 1px solid #005B80;
	}
</style>

<script type="text/javascript">

	var targetOnLoad;
	
	function doPdfSearch(){

		e = document.getElementById("searchTypeInternal"); 
		selectedSearchType = e.options[e.selectedIndex].value;
		if((selectedSearchType == "contractName") || (selectedSearchType == "finRepName")){
			document.bdFundCheckInternalForm.searchNameList.value=true;
		}
		document.bdFundCheckInternalForm.action="?action=pdfSearch";
		document.getElementById("errorMsgs").style.display="none";
		document.bdFundCheckInternalForm.submit();
	}

	
	function doPdfSearchByName(){
		e = document.getElementById("nameList"); 
		selectedId = e.options[e.selectedIndex].value;
		selectedName = e.options[e.selectedIndex].text;
		selectedNameArray = selectedName.split("-");
		document.bdFundCheckInternalForm.searchNameList.value=false;
		document.bdFundCheckInternalForm.selectedId.value = selectedId;
		document.bdFundCheckInternalForm.selectedName.value = selectedName;
		document.getElementById("searchInputInternal").value = selectedNameArray[0];
		document.getElementById("nameListDiv").style.display="none"; 
		document.bdFundCheckInternalForm.action="?action=pdfSearch";
		document.getElementById("errorMsgs").style.display="none";
		document.bdFundCheckInternalForm.submit();
	}

	function fnSearchType(){
		document.getElementById("searchInputInternal").value="";
		if(document.getElementById("nameListDiv") != null){
			if(document.getElementById("nameListDiv").style.display=="block"){
				document.getElementById("nameListDiv").style.display="none";  
			}
		}
		
	}
	
	function fnCheckTextLength(){
		e = document.getElementById("searchTypeInternal"); 
		selectedSearchType = e.options[e.selectedIndex].value;
		if(selectedSearchType == "contractName"){
			textLength = document.getElementById("searchInputInternal").value.length;
			if(textLength > 30){
				document.getElementById("searchInputInternal").value = (document.getElementById("searchInputInternal").value).substring(0,textLength-1);
			}
		}else if(selectedSearchType == "finRepName"){
			textLength = document.getElementById("searchInputInternal").value.length;
			if(textLength > 60){
				document.getElementById("searchInputInternal").value = (document.getElementById("searchInputInternal").value).substring(0,textLength-1);
			}
		}else{
			textLength = document.getElementById("searchInputInternal").value.length;
			if(textLength > 7){
				document.getElementById("searchInputInternal").value = (document.getElementById("searchInputInternal").value).substring(0,textLength-1);
			}
		}
	}
	
	function submitRKey(evt) { 
	  var evt = (evt) ? evt : ((event) ? event : null); 
	  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	  if ((evt.keyCode == 13) && (node.type=="text"))  {doPdfSearch();} 
	} 

	document.onkeypress = submitRKey;

	// scripts for L2
	
	function fnAssign(){
		e = document.getElementById("searchTypeL2"); 
		selectedSearchType = e.options[e.selectedIndex].value;
		if(selectedSearchType == "contractName"){
			document.getElementById("searchNameList").value="1";
		}
		//doProtectedSubmitBtn(bdFundCheckInternalForm,'searchPDF',this)
		document.bdFundCheckInternalForm.action="?action=searchPDF";
		document.getElementById("errorMsgs").style.display="none";
		document.bdFundCheckInternalForm.submit();
	}

	
	function doPdfSearchByNameL2(){
		e = document.getElementById("nameListL2"); 
		selectedId = e.options[e.selectedIndex].value;
		selectedName = e.options[e.selectedIndex].text;
		selectedNameArray = selectedName.split("-");
		document.bdFundCheckInternalForm.searchNameList.value="0";
		document.bdFundCheckInternalForm.selectedId.value = selectedId;
		document.bdFundCheckInternalForm.selectedName.value = selectedName;
		document.bdFundCheckInternalForm.searchInput.value = selectedNameArray[0];
		document.getElementById("nameListDiv").style.display="none"; 
		document.bdFundCheckInternalForm.action.value="searchPDF";
		document.bdFundCheckInternalForm.submit();
	}
	
	function fnSearchTypeL2(){
		document.getElementById("searchInputInternalL2").value="";
		if(document.getElementById("nameListDivL2") != null){
			if(document.getElementById("nameListDivL2").style.display=="block"){
				document.getElementById("nameListDivL2").style.display="none";  
			}
		}
		
	}

	function fnCheckTextLengthL2(){
		e = document.getElementById("searchTypeL2"); 
		selectedSearchType = e.options[e.selectedIndex].value;
		if(selectedSearchType == "contractName"){
			textLength = document.getElementById("searchInputInternalL2").value.length;
			if(textLength > 30){
				document.getElementById("searchInputInternalL2").value = (document.getElementById("searchInputInternalL2").value).substring(0,textLength-1);
			}
		}else{
			textLength = document.getElementById("searchInputInternalL2").value.length;
			if(textLength > 7){
				document.getElementById("searchInputInternalL2").value = (document.getElementById("searchInputInternalL2").value).substring(0,textLength-1);
			}
		}
	}
	
	function doOpenPDFInternal(action, season, year, contractNumber, producerCode, companyID, language, participantNoticeInd, searchActionType) {
		
		var selectedPDF;
		
		//Let's remember the current form target first. 
		targetOnLoad = document.bdFundCheckInternalForm.target;
		
		if (contractNumber == "")
			selectedPDF = producerCode;
		else
			selectedPDF = contractNumber;
		
		document.bdFundCheckInternalForm.selectedSeason.value = season;
		document.bdFundCheckInternalForm.selectedYear.value = year;
		document.bdFundCheckInternalForm.selectedPDF.value = selectedPDF;
		document.bdFundCheckInternalForm.selectedCompanyId.value = companyID;
		document.bdFundCheckInternalForm.selectedLanguage.value = language;
		document.bdFundCheckInternalForm.participantNoticeInd.value = participantNoticeInd;
		document.bdFundCheckInternalForm.searchActionType.value = searchActionType;
		document.bdFundCheckInternalForm.action="?action="+action;
		//document.bdFundCheckInternalForm.action.value=action;
		//To open the PDF in a new tab.
		document.bdFundCheckInternalForm.target = "_blank";
		
		document.bdFundCheckInternalForm.submit();
		
		//If we don't assign this back, every request will be processed in
		//a new tab. So, back to normal. 
		document.bdFundCheckInternalForm.target = targetOnLoad;
	}
		
</script>

<bd:form action="/do/fundcheck/fundCheckInternal/" modelAttribute="bdFundCheckInternalForm" name="bdFundCheckInternalForm">
<script type="text/javascript" src="/assets/unmanaged/javascript/bdutils.js"></script>
<form:hidden path="selectedId" />
<form:hidden path="selectedName" />
<form:hidden path="searchNameList" id="searchNameList" value=false/>
<input type="hidden" name="selectedForm"/>

<form:hidden path="action"/>
<form:hidden path="selectedSeason"/>
<form:hidden path="selectedYear"/>
<form:hidden path="selectedPDF" />
<form:hidden path="selectedCompanyId"/>
<form:hidden path="selectedLanguage"/>
<form:hidden path="participantNoticeInd"/>
<form:hidden path="searchInternalIndicator"/>
<form:hidden path="searchActionType" />

<div id="contentOuterWrapper">
  <div id="contentWrapper">
  <report:formatMessages scope="request"/>
<c:set var="additionalResourcesText" value="${additionalResources.text}" />
	<table width="100%"><tr>
	<td style="vertical-align: top">
	<c:if test="${not empty layoutPageBean.name}">	
    <div id="contentTitle"><content:getAttribute id="layoutPageBean" attribute="name"/></div>
    <div id="content3">
    	<%-- Layout/Body 1  Header --%>
    	<c:if test="${not empty layoutPageBean.body1Header}">		
    	<div>
		<span style="font-family: Georgia,Times New Roman,Times,serif; font-size: 20px;color: #B45F04">
			<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
		</span>
		</div>		
</c:if>
    	<%-- Layout/Body 1  Text --%>
    	<c:if test="${not empty layoutPageBean.body1}">	
    	
			<content:getAttribute beanName="layoutPageBean" attribute="body1"/>
</c:if>
		<table>
			<tr>
				<td style="vertical-align: top">
				
				<%-- <form:select path="contractToAdd">
<form:options items="${contractList}" path="value" labelProperty="label" />

</form:select> --%>
	 				<form:select path="searchType" onchange="fnSearchType();" id="searchTypeInternal">
<form:options items="${bdFundCheckInternalForm.searchTypeMap}"  />
</form:select>
				</td>
				<td style="vertical-align: top">
					<form:input path="searchInput" onkeyup="fnCheckTextLength()" size="43" id="searchInputInternal"/><br>
<c:if test="${not empty bdFundCheckInternalForm.nameMap}">
						<div id="nameListDiv" style="DISPLAY:block";>
							<select id="nameList"  size="${bdFundCheckInternalForm.nameListSize}" onChange="doPdfSearchByName()" style="width:100%;">
								<c:forEach var="nameInfoMap" items="${bdFundCheckInternalForm.nameMap}">
									<option value="${nameInfoMap.key}" >${nameInfoMap.value} - ${nameInfoMap.key}</option>
								</c:forEach>
							</select>
						</div>
					</c:if>									
				</td>
				<td style="vertical-align: top">
					<div class="button_header" style="margin-left: 5px; margin-top: 2px; float: right; margin-right: 0px;">
						<span>
							<input type="button" id="customize_report" name="action" value="Search" style="margin-right: 0px; width: 65px;font-size:11px;" onclick="doPdfSearch()" />
						</span>
					</div>
				</td>	
				<td colspan="2"></td>
			</tr>
		</table>
		<table>
			<tr>
				<td style="vertical-align: top">
					<c:if test="${bdFundCheckInternalForm.showSearchResults ==true}">
					<table cellspacing="0px">
							<c:if test="${bdFundCheckInternalForm.searchInternalIndicator ==false}">
								<tr><td><h2>Results</h2></td></tr>	
					</table>
					<table class="ipuTableColumn" cellspacing="0px">
								<c:forEach var="infoRow" items="${bdFundCheckInternalForm.searchResultFundCheckInfo}" >						
								<tr>										
									<c:forEach var="info" items="${infoRow}">
										<td>
											<a href="javascript:doOpenPDFInternal('openPDF', '${info.season}', '${info.year}', '${info.contractNumber}', '${info.producerCode}', '${info.companyId}', '', '','fundCheckInfo');">
												<ul>
													<c:if test="${bdFundCheckInternalForm.springSeason ==fn:trim(info.season)}">
														<li>Spring&nbsp;<c:out value="${info.year}"/></li>
													</c:if>	
													<c:if test="${bdFundCheckInternalForm.fallSeason ==fn:trim(info.season)}">
														<li>Fall&nbsp;<c:out value="${info.year}"/></li>
													</c:if>
								<br/>
								<li style="text-decoration: underline;"> 
								<c:if test="${info.year eq 2016}" >
									<%=BDConstants.FUNDCHECK_LABEL%>
									</c:if>
									<c:if test="${info.year ge 2017}" >
									<%=BDConstants.INVESTMENT_PLATFORM_UPDATE%>
									<%--Investment Platform<br>
									Update--%>
									</c:if>	
									<c:if test="${bdFundCheckInternalForm.showCompanyId}">									
										<c:if test="${fn:trim(info.companyId)== 'NY'}">
												&nbsp;-&nbsp;New York
										</c:if>
										<c:if test="${fn:trim(info.companyId)!= 'NY'}">
											&nbsp;-&nbsp;<c:out value="${info.companyId}"/>
										</c:if>
									</c:if>
								</li>	
									</ul>
								</a>
							</td>
					</c:forEach>					
									</tr>
								</c:forEach>
							</c:if>
							
					</table>
					</c:if>			
				</td>
			</tr>
		</table>   
    </div>
	</c:if>
	
	
	
	<%-- Layout/intro1 --%>
	<c:if test="${not empty layoutPageBean.introduction1}">
		<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
	
	<%-- Layout/intro2 --%>
	<c:if test="${not empty layoutPageBean.introduction2}">
		<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
</c:if>
	</div>
	</td>
	<td style="vertical-align: top">	
	<c:if test="${not empty additionalResourcesText}">
		 <div id="rightColumn1">		
			<span style="font-family: Georgia,Times New Roman,Times,serif; font-size: 20px;color: #ECD672">
				<content:getAttribute attribute="title" beanName="additionalResources"/>
			</span>
			
			<!-- The following CMA content text should be modified to remove "Participant Notification Site" related contents -->
			<content:getAttribute attribute="text" beanName="additionalResources"/>
			<br><br>
			<h2 style="color: #ECD672; font-size: 19px;">Plan Sponsor Investment Platform Update</h2><br/>
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
			 		<tr>
			 			<td style="vertical-align: top">
			 				<form:select path="searchTypeL2" onchange="fnSearchTypeL2();" >
		  						<form:options items="${bdFundCheckInternalForm.searchTypeMapL2}" />
							</form:select>
						</td>
			 		</tr>
			 		<tr>
			 			<td style="vertical-align: top" width="100%">
			 				<form:input path="searchInputL2" maxlength="30" onkeyup="fnCheckTextLengthL2()" size="28" cssStyle="width: 215px;" id="searchInputInternalL2"/>
<c:if test="${not empty bdFundCheckInternalForm.nameMapL2}">
								<div id="nameListDivL2" style="width:100%;DISPLAY:block;">
									<select id="nameListL2"  size="${bdFundCheckInternalForm.nameListSizeL2}" onChange="doPdfSearchByNameL2()" style="width:100%;" >
										<c:forEach var="nameInfoMap" items="${bdFundCheckInternalForm.nameMapL2}">
											<option value="${nameInfoMap.key}" >${nameInfoMap.value} - ${nameInfoMap.key}</option>
										</c:forEach>
									</select>
								</div>
							</c:if>					
			 			</td>
					</tr>	
				
			 		
			 		<tr>
			 			<td>
							<div class="button_header" style="margin-left: 5px; margin-top: 2px; float: right; margin-right: 2px;">
								<span>
									<input type="submit" name="action" value="Search" style="margin-right: 0px; width: 65px;font-size:11px;" onclick="fnAssign();" />
								</span>
							</div>
			 			</td>
			 		</tr>
			 		
					<c:if test="${bdFundCheckInternalForm.showSearchResults ==true}">
<c:if test="${bdFundCheckInternalForm.searchInternalIndicator ==true}">
							<tr><td><h2 style="font-size: 1.1em">Results</h2></td></tr>
							<style>
								.fontsize {
									font-size: 12px;
									}
							</style>
							
							<c:forEach var="info1" items="${bdFundCheckInternalForm.searchResultFundCheckInfoL2}" >
									<tr>
										<td style="padding-left: 8px;">	
											<table>		
												<tbody>									
												<c:if test="${bdFundCheckInternalForm.springSeason ==fn:trim(info1.season)}">
													<tr><td width="33%" style="color: #dcd087; font-size: 10px;">Spring&nbsp;<c:out value="${info1.year}"/></td></tr>
												</c:if>
												<c:if test="${bdFundCheckInternalForm.fallSeason ==fn:trim(info1.season)}">
													<tr><td width="33%" style="color: #dcd087; font-size: 10px;">Fall&nbsp;<c:out value="${info1.year}"/></td></tr>
												</c:if>
													<tr><td>
													<a class="fontsize" href="javascript:doOpenPDFInternal('openPDF', '${info1.season}', '${info1.year}', '${info1.contractNumber}', '', '${info1.companyId}', '', '','fundCheckInfoL2');">
														PS Notice												
													</a>
													</td>
													<c:if test="${info1.participantNoticeInd}">
													<td>
													<a href="javascript:doOpenPDFInternal('openPDF', '${info1.season}', '${info1.year}', '${info1.contractNumber}', '', '${info1.companyId}', 'EN', '${info1.participantNoticeInd}', 'fundCheckInfoL2');">
																PPT Notice English
													</a>
													</td><td>
													<a href="javascript:doOpenPDFInternal('openPDF', '${info1.season}', '${info1.year}', '${info1.contractNumber}', '', '${info1.companyId}', 'SP', '${info1.participantNoticeInd}', 'fundCheckInfoL2');">
																PPT Notice Spanish
													</a>
													</td>
													</c:if>
													</tr>
												</tbody>
											</table>
										</td>
									</tr>		
							</c:forEach>							
						</c:if>
					</c:if>
			 	</table>
			 	
			 	<br><br>
			 	<%-- Layout/Body 2 Header --%>
				<span style="font-family: Georgia,Times New Roman,Times,serif; font-size: 20px;">
			 	<content:getAttribute beanName="layoutPageBean" attribute="body2Header"/>
				</span>
		 </div>  
	</c:if>
	</td>	
	</tr></table>	
	<table width="100%">
	<tr><td>
	<div id="errorMsgs" style="DISPLAY:block"; >
		
		<c:if test="${bdFundCheckInternalForm.noSearchResults ==true}">
			<div class='message message_info' id="infoMsgs" style="DISPLAY:block"; >
				<dl><dt>Information Message</dt><dd>1.&nbsp;&nbsp;
					<content:getAttribute id="NosearchResult" attribute="text" />
				</dd></dl>
			</div>
</c:if>
<c:if test="${bdFundCheckInternalForm.fundCheckPDFAvailable ==false}">
			<div class='message message_info' id="infoMsgs" style="DISPLAY:block"; >
				<dl><dt>Information Message</dt><dd>1.&nbsp;&nbsp;
					<content:getAttribute id="NoPDFsExistMessage" attribute="text" />
				</dd></dl>
			</div>
</c:if>
<c:if test="${bdFundCheckInternalForm.addMoreFilters ==true}">
			<div class='message message_info' id="infoMsgs" style="DISPLAY:block"; >
				<dl><dt>Information Message</dt><dd>1.&nbsp;&nbsp;
					<content:getAttribute id="PleaseAddMoreFilters" attribute="text" />
				</dd></dl>
			</div>
</c:if>
	</div>
	</td></tr></table>
	<table width="100%">
	<tr>
	<td style="vertical-align: top">
    
	</td>
	</tr>
	</table>	
  </div>
</div>

<div class="footnotes">
    <dl><dd><content:pageFooter beanName="layoutPageBean"/></dd></dl>
    <dl><dd><content:pageFootnotes beanName="layoutPageBean"/></dd></dl>
    <dl><dd><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></dd></dl>
    <div class="footnotes_footer"></div>
</div>
</bd:form>    

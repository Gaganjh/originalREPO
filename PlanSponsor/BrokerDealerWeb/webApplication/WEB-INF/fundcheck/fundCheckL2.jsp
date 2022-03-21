<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

					 
<content:contentBean contentId="<%=BDContentConstants.FUNDCHECK_ADDITIONAL_RESOURCES_LAYER%>"
                     type="<%=BDContentConstants.TYPE_LAYER%>"
                     id="additionalResources"/>
<content:contentBean contentId="<%=BDContentConstants.FUNDCHECK_NO_PDFS_EXISTS_MESSAGE_LEVEL2%>"
						type="<%=BDContentConstants.TYPE_MESSAGE%>"
						id="NoPDFsExistMessageLevel2" />	
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
    margin: 0px 30px 0px 0px;
    margin-top: 10px;
	padding: 10px;
}
 
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 table tbody tr td ul li a{
    text-decoration:underline !important;
	color:#005B80 !important;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn1 h5 .date {
    font-size: 11px;
    font-weight: normal;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #content3 p {
    width:590px;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn1 {
    float: right;
    padding: 10px; /* Sets the padding properties for an element using shorthand notation (top, right, bottom, left) */
    width: 243px;
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

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn2 {
    float: right;
    padding: 10px; /* Sets the padding properties for an element using shorthand notation (top, right, bottom, left) */
    width: 243px;
    color: #FFFFFF;
    background-color: #8b8878;
    margin-top: 10px;
    clear: right;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn2 h2 {
	font-family: Georgia, "Times New Roman", Times, serif;
	font-size: 12px;
	font-weight: normal;
	padding: 0px;
	color: #FFFFFF;
}

#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn2 a {
    font-size: 11px;
    color: #FFF;
    text-decoration: underline;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn2 a:hover {
    font-size: 11px;
    color: #dcd087;
    text-decoration: underline;
}
#contract_funds #page_wrapper #page_content #contentOuterWrapper #contentWrapper #rightColumn2 .selected {
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
	
	function fnAssign(){

		e = document.bdFundCheckLevel2Form.searchType; 
		selectedSearchType = e.options[e.selectedIndex].value;
		if(selectedSearchType == "contractName"){
			document.bdFundCheckLevel2Form.searchNameList.value="1";
		}
	}

	
	function doPdfSearchByName(){
		e = document.getElementById("nameList"); 
		selectedId = e.options[e.selectedIndex].value;
		selectedName = e.options[e.selectedIndex].text;
		selectedNameArray = selectedName.split("-");
		document.bdFundCheckLevel2Form.searchNameList.value="0";
		document.bdFundCheckLevel2Form.selectedId.value = selectedId;
		document.bdFundCheckLevel2Form.selectedName.value = selectedName;
		document.bdFundCheckLevel2Form.searchInput.value = selectedNameArray[0];
		document.getElementById("nameListDiv").style.display="none"; 
		document.bdFundCheckLevel2Form.action.value="searchPDF";
		document.bdFundCheckLevel2Form.submit();
	}
	
	function fnSearchType(){
		document.bdFundCheckLevel2Form.searchInput.value="";
		if(document.getElementById("nameListDiv") != null){
			if(document.getElementById("nameListDiv").style.display=="block"){
				document.getElementById("nameListDiv").style.display="none";  
			}
		}
		
	}

	function fnCheckTextLength(){
		e = document.bdFundCheckLevel2Form.searchType; 
		selectedSearchType = e.options[e.selectedIndex].value;
		if(selectedSearchType == "contractName"){
			textLength = document.bdFundCheckLevel2Form.searchInput.value.length;
			if(textLength > 30){
				document.bdFundCheckLevel2Form.searchInput.value = (document.bdFundCheckLevel2Form.searchInput.value).substring(0,textLength-1);
			}
		}else{
			textLength = document.bdFundCheckLevel2Form.searchInput.value.length;
			if(textLength > 7){
				document.bdFundCheckLevel2Form.searchInput.value = (document.bdFundCheckLevel2Form.searchInput.value).substring(0,textLength-1);
			}
		}
	}	
	
	function submitRKey(evt) { 
	  var evt = (evt) ? evt : ((event) ? event : null); 
	  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	  if ((evt.keyCode == 13) && (node.type=="text"))  {fnAssign();return doProtectedSubmitBtn(bdFundCheckLevel2Form,'searchPDF',this)} 
	} 

	document.onkeypress = submitRKey;
	
	function doOpenPDFL2(action, season, year, selectedPDF, companyID, PDFCode, language, participantNoticeInd, searchActionType) {
		
		//Let's remember the current form target first. 
		targetOnLoad = document.bdFundCheckLevel2Form.target;
		
		document.bdFundCheckLevel2Form.selectedSeason.value = season;
		document.bdFundCheckLevel2Form.selectedYear.value = year;
		document.bdFundCheckLevel2Form.selectedPDF.value = selectedPDF;
		document.bdFundCheckLevel2Form.selectedCompanyId.value = companyID;
		document.bdFundCheckLevel2Form.selectedPDFCode.value = PDFCode;
		document.bdFundCheckLevel2Form.selectedLanguage.value = language;
		document.bdFundCheckLevel2Form.participantNoticeInd.value = participantNoticeInd;
		document.bdFundCheckLevel2Form.searchActionType.value = searchActionType;
		document.bdFundCheckLevel2Form.action.value = action;
		
		//To open the PDF in a new tab.
		document.bdFundCheckLevel2Form.target = "_blank";
		
		document.bdFundCheckLevel2Form.submit();
		
		//If we don't assign this back, every request will be processed in
		//a new tab. So, back to normal. 
		document.bdFundCheckLevel2Form.target = targetOnLoad;
	}
	
</script>
<bd:form action="/do/fundcheck/fundCheckL2/" name="bdFundCheckLevel2Form" modelAttribute="bdFundCheckLevel2Form">
<script type="text/javascript" src="/assets/unmanaged/javascript/bdutils.js"></script>

<form:hidden path="selectedSeason"/>
<form:hidden path="selectedYear"/>
<form:hidden path="selectedPDF"/>
<form:hidden path="selectedPDFCode"/>
<form:hidden path="selectedCompanyId"/>
<form:hidden path="selectedLanguage"/>
<form:hidden path="participantNoticeInd"/>
<form:hidden path="searchActionType" />

<div id="contentOuterWrapper">
  <div id="contentWrapper">
  <report:formatMessages scope="request"/>
<c:set var="additionalResourcesText" value="${additionalResources.text}" />
	<table width="100%"><tr>
	<td>
	<c:if test="${not empty layoutPageBean.name}">
    <div id="contentTitle"><content:getAttribute id="layoutPageBean" attribute="name"/></div>
</c:if>

	<%-- Layout/intro1 --%>
	<c:if test="${not empty layoutPageBean.introduction1}">
		<p><content:getAttribute beanName="layoutPageBean" attribute="introduction1"/></p>
</c:if>
	
	<%-- Layout/intro2 --%>
	<c:if test="${not empty layoutPageBean.introduction2}">
		<p><content:getAttribute beanName="layoutPageBean" attribute="introduction2"/></p>
</c:if>
	<br></br>
	<div id="content3">
    	<%-- Layout/Body 1 Header --%>
		<c:if test="${ not empty layoutPageBean.body1Header} ">
    	<div>
			<span style="font-family: Georgia,Times New Roman,Times,serif; font-size: 20px;color: #B45F04">
				<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
			</span>
    	</div>
</c:if>
    	<%-- Layout/Body 1 Text --%>
    	<c:if test="${ not empty layoutPageBean.body1}">
			<content:getAttribute beanName="layoutPageBean" attribute="body1"/>
</c:if>
		<c:choose>
		<c:when test="${bdFundCheckLevel2Form.fundCheckPDFAvailable}">
		<table class="ipuTableColumn" cellspacing="0px">
			<c:forEach var="infoRow" items="${bdFundCheckLevel2Form.fundCheckInfo}" >
				<tr>
					<c:forEach var="info" items="${infoRow}">
							<td>
								<a href="javascript:doOpenPDFL2('openPDF', '${info.season}', '${info.year}', 'producerCode', '${info.companyId}', '${info.producerCode}', '', '', 'fundCheckInfo');">
									<ul>
										<c:if test="${bdFundCheckLevel2Form.springSeason ==fn:trim(info.season)}">
											<li>Spring&nbsp;<c:out value="${info.year}"/> </li>
										</c:if>
										<c:if test="${bdFundCheckLevel2Form.fallSeason ==fn:trim(info.season)}">
											<li>Fall&nbsp;<c:out value="${info.year}"/> </li>
										</c:if>
							<br/>
							<li style="text-decoration: underline;">
									<c:if test="${info.year eq 2016}" >								
									<%=BDConstants.FUNDCHECK_LABEL%>
									</c:if>
									<c:if test="${info.year ge 2017}" >								
									<%=BDConstants.INVESTMENT_PLATFORM_UPDATE%>
									</c:if>
									<c:if test="${bdFundCheckLevel2Form.showProducerCode}">
										Producer:&nbsp;<c:out value="${info.producerCode}"/>
									</c:if>		
									<c:if test="${bdFundCheckLevel2Form.showCompanyId}">									
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
			</table> 
			</c:when>
			<c:otherwise>
			<table cellspacing="0px">
				<tr>
       			<td style="font-size:11px;">
					<content:getAttribute id="NoPDFsExistMessageLevel2" attribute="text" />
				</td>
       			</tr>
			</table>
			</c:otherwise>
		</c:choose>
	</div>
	</td>
	<td style="vertical-align: top">
	<c:if test="${not empty  additionalResourcesText}" > 
		 <div id="rightColumn1">		
				<span style="font-family: Georgia,Times New Roman,Times,serif; font-size: 20px; color: #ECD672">
					<content:getAttribute attribute="title" beanName="additionalResources"/>
				</span>
				<!-- The following CMA content text should be modified to remove "Participant Notification Site" related contents -->
				<content:getAttribute attribute="text" beanName="additionalResources"/>
				<br><br>
				<h2 style="color: #ECD672; font-size: 19px;">Plan Sponsor Investment Platform Update</h2><br/>
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
			 		<tr>
			 			<td style="vertical-align: top">
			 			
			 		
			 			
			 		<form:select path="searchType"  onchange="fnSearchType();">
			 			<c:forEach
							items="${bdFundCheckLevel2Form.searchTypeMap}"
							var="searchTyp">
							<form:option value="${searchTyp.key}">${searchTyp.value}</form:option>
						</c:forEach>
					</form:select>
			 			</td>
			 		</tr>
			 		<tr>
			 			<td style="vertical-align: top" width="100%">
<form:input path="searchInput" maxlength="30" onkeyup="fnCheckTextLength()" size="28"/>
<c:if test="${not empty bdFundCheckLevel2Form.nameMap}">
								<div id="nameListDiv" style="width:100%;DISPLAY:block;">
									<select id="nameList"  size="${bdFundCheckLevel2Form.nameListSize}" onChange="doPdfSearchByName()" style="width:100%;" >
										<c:forEach var="nameInfoMap" items="${bdFundCheckLevel2Form.nameMap}">
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
<input type="submit" onclick="fnAssign(); return doProtectedSubmitBtn(bdFundCheckLevel2Form,'searchPDF',this)" value="Search" style="margin-right: 0px; width: 65px;font-size:11px;"/>
								</span>
							</div>
			 			</td>
			 		</tr>
				
<c:if test="${bdFundCheckLevel2Form.showSearchResults ==true}">
					<td><h2>Results</h2></td>
					<tr>
						<td>					
							<c:forEach var="info" items="${bdFundCheckLevel2Form.searchResultFundCheckInfo}" >
								<table>		
									<tr><td>
									<table>		
										<tbody>
										<c:if test="${bdFundCheckLevel2Form.springSeason ==fn:trim(info.season)}">
											<tr><td width="33%" style="color: #dcd087; font-size: 10px;">Spring&nbsp;<c:out value="${info.year}"/></td></tr>
										</c:if>
										<c:if test="${bdFundCheckLevel2Form.fallSeason ==fn:trim(info.season)}">		
											<tr><td width="33%" style="color: #dcd087; font-size: 10px;">Fall&nbsp;<c:out value="${info.year}"/></td></tr>
										</c:if>
										<tr><td>
										<a href="javascript:doOpenPDFL2('openPDF', '${info.season}', '${info.year}', 'contractNumber', '${info.companyId}', '${info.contractNumber}', '', '', 'fundCheckInfoL2');">
											PS Notice												
										</a>
										</td>
										<c:if test="${info.participantNoticeInd}">
											<td>
											<a class="fontsize" href="javascript:doOpenPDFL2('openPDF', '${info.season}', '${info.year}', 'contractNumber', '${info.companyId}', '${info.contractNumber}', 'EN', '${info.participantNoticeInd}', 'fundCheckInfoL2');">
												PPT Notice English
											</a>
											</td><td>
											<a class="fontsize" href="javascript:doOpenPDFL2('openPDF', '${info.season}', '${info.year}', 'contractNumber', '${info.companyId}', '${info.contractNumber}', 'SP', '${info.participantNoticeInd}', 'fundCheckInfoL2');">
												PPT Notice Spanish
											</a>
											</td>
										</c:if>
										</tr>
										</tbody>
									</table>	
									</td></tr>
								</table>
							</c:forEach>
						</td>
				</tr>
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
	<div>
	

<c:if test="${bdFundCheckLevel2Form.noSearchResults ==true}">
		<tr><td >
				<div  class='message message_info'  style="color:#000000">
					<dl><dt>Information Message</dt><dd>1.&nbsp;&nbsp;
					<content:getAttribute id="NosearchResult" attribute="text" />
					</dd></dl>
				</div>	
			</td></tr>
</c:if>
<c:if test="${bdFundCheckLevel2Form.fundCheckPDFAvailableForSearch ==false}">
		<tr><td >
				<div  class='message message_info'  style="color:#000000">
					<dl><dt>Information Message</dt><dd>1.&nbsp;&nbsp;
					<content:getAttribute id="NoPDFsExistMessage" attribute="text" />
					</dd></dl>
				</div>	
			</td></tr>
</c:if>
<c:if test="${bdFundCheckLevel2Form.addMoreFilters ==true}">
		<tr><td >
				<div  class='message message_info'  style="color:#000000">
					<dl><dt>Information Message</dt><dd>1.&nbsp;&nbsp;
					<content:getAttribute id="PleaseAddMoreFilters" attribute="text" />
					</dd></dl>
				</div>	
			</td></tr>
</c:if>
	</div>
	</td></tr></table>
	<table width="100%">
	<tr>
	<td style="vertical-align: top">
    
	</td>
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
<form:hidden path="action" value=""/>
<form:hidden path="selectedId" />
<form:hidden path="selectedName" />
<form:hidden path="searchNameList" value="0"/>

</bd:form>

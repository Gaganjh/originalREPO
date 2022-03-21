<%@	page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@	page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@	page import="com.manulife.pension.ps.web.Constants"	%>
<%@	page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%@	taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@	taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<script>

/**
* opens a window to show the PDF for the selected report
* by sending a request to the ContractDocumentsPDF action
*
*/
function showAmendmentPDF(theSelect) {
	var url = "/do/contract/contractDocumentsPDF/?contractDoc=";
	var key = theSelect.options[theSelect.selectedIndex].value;
	if (key.length > 0) {
	url = url+key;
	PDFWindow(url)
	}
}

/**
* opens a window to show the PDF for the selected report
* by sending a request to the ContractDocumentsPDF action
*
*/
function showContractPDF(fileName) {
	var url = "/do/contract/contractDocumentsPDF/?contractDoc=";
	url = url+fileName;
	PDFWindow(url)
}

</script>
 <jsp:useBean id="contractDocuments" scope="request" class="com.manulife.pension.ps.web.contract.ContractDocuments" />
  <c:set var="amendmentDocs" value="${contractDocuments.amendmentDocs}"/>
<table border="0">
<tr>
 <td width="5%" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8">
</td>
     
   
<td	width="82%">
      <p>
        <content:errors scope="request" />
      </p>
     <c:if test="${not empty contractDocuments }" >
	<table width="730" border="0" cellspacing="0" cellpadding="0">
  <tr>
 <%-- Beans used --%>





	<td	width="700">
				  <TABLE cellSpacing=0 cellPadding=0 width=495 border=0 align="left">
			  <TBODY>
			  <TR>

				<TD	width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" 
				  width=1></TD>
				<TD	width=80><IMG height=1 src="/assets/unmanaged/images/s.gif" 
				  width=80></TD>
				<TD	width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" 
				  width=1></TD>
				<TD	width=327><IMG height=1	
				  src="/assets/unmanaged/images/s.gif" width=327></TD>
				<TD	width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" 
				  width=1></TD>
				<TD	width=80><IMG height=1 src="/assets/unmanaged/images/s.gif" 
				  width=80></TD>
				<TD	width=5><IMG height=1 src="/assets/unmanaged/images/s.gif" 
				  width=4></TD>
				<TD	width=5><IMG height=1 src="/assets/unmanaged/images/s.gif" 
				  width=1></TD></TR>
			 			  
			  <tr>
		
		<td colspan="4">
			<input name="isAciSignupAuto" value="true" type="hidden">
		                   	<table border="0" cellpadding="0" cellspacing="0" width="495">
                    <tbody><tr>

                      <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                      <td width="327"><img src="/assets/unmanaged/images/s.gif" height="1" width="327"></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                      <td width="80"><img src="/assets/unmanaged/images/s.gif" height="1" width="80"></td>
                      <td width="5"><img src="/assets/unmanaged/images/s.gif" height="1" width="4"></td>
                      <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    </tr>
                    <tr class="tablehead">

                      <td class="tableheadTD1" colspan="3"><b><content:getAttribute beanName="layoutPageBean" attribute="subHeader"/>  </b>                      </td>
                      <td></td>
                      <td colspan="3" class="tableheadTD" align="center"><b>&nbsp;</b></td>
                    </tr>
                 	
                    <tr class="datacell1">
                    
                      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                      <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

					  <td colspan="4" align="left" valign="top">
					  
					   		
							<b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b>
<c:if test="${not empty contractDocuments.contractDocName}">
							<br><content:getAttribute beanName="layoutPageBean" attribute="body1"/> <br>	
							
<a href="javascript:showContractPDF('${contractDocuments.contractDocName}')">
							<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_DOWNLOAD_CONTRACT_LINK%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="downloadContract"/>
				      		<content:getAttribute beanName="downloadContract" attribute="text"/></a>	<Br>			 
</c:if>
<c:if test="${empty contractDocuments.contractDocName}">
							<br>
							<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CONTRACT_NOT_AVAILABLE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messagesNoContractDoc"/>
				      		<content:getAttribute beanName="messagesNoContractDoc" attribute="text"/></a>	<Br>
</c:if>
							
							</td>
                      <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                    </tr>
                          
                      
                           <tr class="datacell2">
                           
                           <td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
     					  <td colspan="4" align="left" valign="top"><b>
     					  <content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b>
     						<br>
     							<content:getAttribute beanName="layoutPageBean" attribute="body2"/><Br>
					<c:if test="${not empty amendmentDocs }" >
					
                	<ps:select name="contractDocuments" property="amendmentOption" onchange="showAmendmentPDF(this);" >
                		<ps:option value="">${contractDocuments.getAmendmentOption()}</ps:option>
                		<ps:options collection="amendmentDocs" property="value" labelProperty="label"/> 
                	</ps:select>
                 </c:if>
                 <c:if test="${empty amendmentDocs }" >
				<div class="highlightBold">
                 <content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_AMENDMENTS_NOT_AVAILABLE%>"
                           type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                          	id="messages"/><content:getAttribute beanName="messages" attribute="text"/>
                  </div>
                       </c:if>
							</td>
                           <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                           </tr>
   			  <TR>
				<TD	class=databorder colSpan=8><IMG	height=1 
				  src="/assets/unmanaged/images/s.gif" width=1></TD></TR>
			  <TR>
				<TD><IMG height=1 src="/assets/unmanaged/images/s.gif"></TD>
				<TD	colSpan=8>&nbsp; </TD></TR>

				<TR>
				<TD><IMG height=1 src="/assets/unmanaged/images/s.gif"></TD>
				<td colspan=4 width="99%">
							<br>
								<p><content:pageFooter beanName="layoutPageBean"/></p>
 								<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 								<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 							</td></TR></TBODY></TABLE><IMG 
			height=1 src="/assets/unmanaged/images/spacer.gif" width=15 border=0> 
			<IMG height=25 src="/assets/unmanaged/images/s.gif" width=1>	<IMG 
			height=5 src="/assets/unmanaged/images/s.gif" width=1> <IMG height=5	
			src="/assets/unmanaged/images/s.gif"	width=1> </TD></TABLE><IMG 
	  height=20	src="/assets/unmanaged/images/s.gif"	width=1> 

</TD></TABLE>
</c:if>
</td>

</tr>
</table>

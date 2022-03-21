<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ page import="com.manulife.pension.ps.web.ErrorCodes"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

  <content:contentBean
  contentId="<%=ContentConstants.CSV_FILE_CREATED_SUCCESSFULLY%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="csvFileCreated" />
  
  <content:contentBean
  contentId="<%=ContentConstants.CSV_FILE_NOT_CREATED%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="csvFileNotCreated" />
  
  <content:contentBean
  contentId="<%=ContentConstants.PPT_CHANGE_NOTIFICATION_DOC_CREATED%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="pcnDocCreated" />
  
  <content:contentBean
  contentId="<%=ContentConstants.PPT_CHANGE_NOTIFICATION_DOC_NOT_CREATED%>"
  type="<%=ContentConstants.TYPE_MESSAGE%>" id="pcnDocNotCreated" />
  
  <script type="text/javascript" >

$(document).ready(function() {
	var temp, errorMsg= '';
	var message = '<table id="bac_error_div"><tbody><tr><td valign="top"><ul> ';
			if(${statusCode.CSV_STATUS} == <%=ErrorCodes.CSV_FILE_CREATED_SUCCESSFULLY%>){
				message = message+"<li>"+ '<content:getAttribute beanName="csvFileCreated" attribute="text" filter="false" escapeJavaScript="true"/>'+'</li>';					
			}
			if (${statusCode.DOC_STATUS} == <%=ErrorCodes.PPT_CHANGE_NOTIFICATION_DOC_CREATED%>){
				message = message+"<li>"+ '<content:getAttribute beanName="pcnDocCreated" attribute="text" filter="false" escapeJavaScript="true"/>'+'</li>';
			}
			if (${statusCode.CSV_STATUS} == <%=ErrorCodes.CSV_FILE_NOT_CREATED%>){
				errorMsg = '<content:getAttribute beanName="csvFileNotCreated" attribute="text" filter="false" escapeJavaScript="true"/>';
				message = message+"<li>"+ errorMsg.replace('{0}',${errorCode.CSV_NOT_CREATED})+'</li>';
			}
			if (${statusCode.DOC_STATUS} == <%=ErrorCodes.PPT_CHANGE_NOTIFICATION_DOC_NOT_CREATED%>){
				errorMsg = '<content:getAttribute beanName="pcnDocNotCreated" attribute="text" filter="false" escapeJavaScript="true"/>';
				message = message+"<li>"+ errorMsg.replace('{0}',${errorCode.DOC_NOT_CREATED})+'</li>';
			}
		message = message+'</ul></td></tr></tbody></table>';
		$('#statusDiv').html(message);
});
</script>
<html>
<head>
<title></title></head>
<body>
<c:set var="form" value="${requestScope.ipiTool}"/>
<br/><br/>
	<div id = "confirmation">
		<table border="0" cellSpacing="0" cellPadding="0" width=760>
		<tbody >
		  <tr>
		    <td width=30>&nbsp;</td>
			  <td>
					<table border="0" cellSpacing="0" cellPadding="0" width="700">
							<tbody>
									<tr>
									<td>
										  <!--Retrieve from CMA -->
										<img src="<content:pageImage type="pageTitle" beanName="layoutPageBean"/>" alt="Participant Fee Change Notice Confirmation"/> 
										<br><br>
									</td>
								</tr>
								<tr>
									<td>
									<!--Layout/Intro1--> 
										<content:pageIntro beanName="layoutPageBean" />
										<br>
										<content:getAttribute beanName="layoutPageBean"
										  attribute="introduction2">

										</content:getAttribute> <br>
										<br>
									</td>
								</tr>
								<tr>
									<td vAlign="top" width="730">
										<table border="0" cellSpacing="0" cellPadding="0" width="730">
											<tbody>
												<tr>
													<td colspan="3">
														<div id="statusDiv">
															&nbsp;
														</div>
													<br>
													</td>
												</tr>
												<tr>
													<td colspan="3">
														&nbsp;
													</td>
												</tr>
												<tr>
													<ps:form action="/do/fee/disclosure/" modelAttribute="ipiToolForm" name="ipiToolForm">
<td colspan="3"><input type="submit" class="button134" value="Ok"/></td>
													</ps:form>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
								
							</tbody>
						</table>
						<br/>	
			 	 </td>
	     	</tr>
			<tr>
				<td width=30>
				  &nbsp;
				</td>
			   <td>
				 <p><content:pageFooter beanName="layoutPageBean"/></p>
				 <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
				 <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
			   </td>
			</tr>
		</tbody>
	</table>
	</div>
</body>
</html>

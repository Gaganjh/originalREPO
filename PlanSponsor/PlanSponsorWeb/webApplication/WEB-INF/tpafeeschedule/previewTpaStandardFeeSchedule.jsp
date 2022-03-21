<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String technicalDifficulties =(String)request.getAttribute(Constants.TECHNICAL_DIFFICULTIES);
pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);
%>


<content:contentBean
	contentId="<%=ContentConstants.WARNNING_MESSAGE_ON_CANCEL_DATA_LOSS_TO_TPA_USER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="warningMessage" />

<content:contentBean
	contentId="<%=ContentConstants.STANDARD_SCHEDULE_APPLIED_TO_ALL_CONTRACT_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="standardScheduleAppliedMessage" />
	
<c:if test="${empty  technicalDifficulties}">
	<c:if test="${empty param.printFriendly }" >
		<script language="javascript" type="text/javascript"
			src="/assets/unmanaged/javascript/calendar.js"></script>

		<script type="text/javascript" >
	
	function doBack() {
		
		document.forms['tpaStandardFeeScheduleForm'].action = "/do/editTpaStandardFeeSchedule/";
		document.tpaStandardFeeScheduleForm.submit();
	}

	function doSubmit() {
		
		var message;  
		message = '<content:getAttribute beanName="standardScheduleAppliedMessage" attribute="text" />';
	    var response = confirm(message);
	
		if (response == true) {
		
			document.forms['tpaStandardFeeScheduleForm'].action = "/do/previewTpaStandardFeeSchedule/?action=submit";
			document.tpaStandardFeeScheduleForm.submit();   	
		}else{
			return false;
		}
	}
	
	function isFormDirty(){
		return true;
	}
	
	function isLostChanges(){
		
		if(isFormDirty()){
			return confirm('<content:getAttribute beanName="warningMessage" attribute="text"/>');
		}
		return true;
	}

try {
	  $(document).ready(function() { 	
		  registerTrackChangesFunction(isFormDirty);
			var hrefs  = document.links;
			if (hrefs != null)
			{
				for (i=0; i<hrefs.length; i++) { 
					if(hrefs[i].href != undefined && 
						hrefs[i].href.indexOf("profiles/editMyProfile") != -1){
						hrefs[i].onclick = new Function ("return isLostChanges();");
					}
				
				}
			}

	// To alert user that the changes were not yet saved
			$("a:not(.signOutLayerStyle2)").on("click",function(event){
				return isLostChanges();
			});
	  });
	 } catch (e) {
	 	// If JQuery is not loaded, exception will be caught here
	 }
</script>
	</c:if>
</c:if>

<%-- technical difficulties --%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="30"
			height="1"></td>
		<td width="100%" valign="top"><%-- error line --%> &nbsp; <content:errors
			scope="request" /> &nbsp; <%-- technical difficulties --%> <br>

		<%-- Start TPA Standard Schedule Confirmation information --%> <br>
		<TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
			<TBODY>
				<TR>
					<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					<TD class=actions><IMG src="/assets/unmanaged/images/s.gif"
						width=203 height=1></TD>
					<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					<TD class=type><IMG src="/assets/unmanaged/images/s.gif"
						width=145 height=1></TD>
					<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					<TD class=paymentTotal><IMG
						src="/assets/unmanaged/images/s.gif" width=200 height=1></TD>
					<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					<TD><IMG src="/assets/unmanaged/images/s.gif" width=180
						height=1></TD>
					<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
				</TR>
				<TR class=tablehead>
					<TD class=tableheadTD1 height=25 vAlign=center colSpan=9><b>Individual
					Expenses</b></TD>
					<TD class=databorder><IMG src="/assets/unmanaged/images/s.gif"
						width=1 height=1></TD>
				</TR>
				<TR class=tablesubhead>
					<TD class=databorder height=25><IMG
						src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					<TD><b>Fee type</b></TD>
					
					<TD class=dataheaddivider height=25><IMG
						src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					<TD valign=middle align=right height=25><b>Value</b></TD>
					<TD class=dataheaddivider height=25><IMG
						src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					<TD vAlign=middle height=25 align=left colspan="4"><b>Special
					notes</b></TD>
					<TD class=databorder height=25><IMG
						src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
				</TR>
				
<c:forEach items="${tpaStandardFeeScheduleForm.previewFeeItemsList}" var="details" varStatus="theIndex" >




											
						<c:choose>
							<c:when test="${theIndex.index % 2 == 0}">
								<tr class="datacell2"> 
							</c:when>
							<c:otherwise>
								<tr class="datacell1">
							</c:otherwise>
						</c:choose> 					
											
						<TD class=databorder ><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD vAlign=top style="white-space: wrap;"><B>${details.feeDescription}</B></TD>

						
						<TD class=datadivider ><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD valign=middle align=right >${details.displayFormattedAmount}</TD>

						<TD class=datadivider ><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD vAlign=middle  align=left colspan="4" style="word-break:break-all;">${details.notes}</TD>

						<TD class=databorder ><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>

						</TR>
</c:forEach>
				<TR>
					<TD class=databorder colSpan=10><IMG
						src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
				</TR>
				<TR>
					<TD colSpan=5><IMG src="/assets/unmanaged/images/s.gif"
						width=1 height=1></TD>
					<TD class=dark_grey_color vAlign=center colSpan=4></TD>
					<TD class=dark_grey_color colSpan=4 align=right></TD>
					<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
				</TR>
			</TBODY>
		</TABLE>
		<br>
		<ps:form action="/do/viewTpaStandardFeeSchedule/" modelAttribute="tpaStandardFeeScheduleForm" name="tpaStandardFeeScheduleForm">
			<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
				<tr>

					<td width="410" align="right"></td>

<td width="160" align="right"><input type="submit" onclick="return doBack();" name="action" class="button134" value="back"/></td>



<td width="160" align="right"><input type="submit" onclick="return doSubmit();" name="action" class="button134" value="submit"/></td>


				</tr>
			</table>

		</ps:form> <%-- End TPA Standard Schedule Confirmation information --%> <%-- technical difficulties --%>
		</td>
	</tr>
</table>

<c:if test="${not empty param.printFriendly }" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute id="globalDisclosure"
				attribute="text" /></td>
		</tr>
	</table>
</c:if>
<c:if test="${empty param.printFriendly}">
		<table cellpadding="0" cellspacing="0" border="0" width="730" class="fixedTable" height="">
			<tr>
				<td width="30">&nbsp;</td>
				<td width="700"><content:pageFooter beanName="layoutPageBean" /></td>
			</tr>
		</table>
</c:if>

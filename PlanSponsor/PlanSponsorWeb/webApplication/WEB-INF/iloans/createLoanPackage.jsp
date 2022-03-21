<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.content.valueobject.ContentType"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>


<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render" prefix="render"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>


<script type="text/javascript" >
	function doOnload() {
		var displayPdf =
<%=session.getAttribute("pdf") != null%>
	;
		if (displayPdf)
			PDFWindow('/do/util/pdf/');
	}
</SCRIPT>


<ps:form method="POST" modelAttribute="createLoanPackageForm" name="createLoanPackageForm" action="/do/iloans/createLoanPackage/">

	

	<table width="600" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="8"><strong><font color="#CC6600">*</font></strong>
				Required Information<br> <content:errors scope="session" /></td>
		</tr>

		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="113"><img src="/assets/unmanaged/images/s.gif"
				width="80" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="463"><img src="/assets/unmanaged/images/s.gif"
				width="250" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="113"><img src="/assets/unmanaged/images/s.gif"
				width="80" height="1" /></td>
			<td width="4"><img src="/assets/unmanaged/images/s.gif"
				width="4" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="tablehead">
			<td class="tableheadTD1" colspan="8"><b><content:getAttribute
						beanName="layoutPageBean" attribute="body1Header" /></b></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td colspan="6" rowspan="13"><table width="100%" border="0"
					cellpadding="0">
					<tr>
						<td width="165" align="left"><ps:label fieldId="loanDate"
								mandatory="true">Date of loan</ps:label></td>
<td width="31%">&nbsp;&nbsp; <form:input path="loanDate" maxlength="10" size="10" cssClass="inputField"/> <a

							href="javascript:cal1.popup();"><img
								src="/assets/unmanaged/images/cal.gif" width="16" height="16"
								border="0" alt="Click Here to Pick up the date"></a>
							(mm/dd/yyyy)<br>
						</td>
					</tr>
					<tr>
						<td align="left"><ps:label fieldId="nextRepaymentDate"
								mandatory="true">Next repayment date</ps:label></td>
<td width="31%">&nbsp;&nbsp; <form:input path="nextRepaymentDate" maxlength="10" size="10" cssClass="inputField"/> <a href="javascript:cal2.popup();"><img


								src="/assets/unmanaged/images/cal.gif" width="16" height="16"
								border="0" alt="Click Here to Pick up the date"></a>
							(mm/dd/yyyy)<br>
						</td>
					</tr>

				</table></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>

			<ps:roundedCorner numberOfColumns="8" emptyRowColor="white" />
		</tr>

	</table>
	<br>
	<table width="600" border="0" cellspacing="1" cellpadding="1">
		<tr>
<td width="477" align="right"><input type="submit" class="button150" value="i:loans home" /> </td>

<td width="116" align="right"><input type="submit" class="button150" value="create package" /> </td>


		</tr>
	</table>
	</td>
	</tr>
	</table>
	</td>
	<td width="30"></td>
	</tr>
	</table>
</ps:form>
<script type="text/javascript" >
	var cal1 = new calendar(
			document.forms['createLoanPackageForm'].elements['loanDate']);
	cal1.year_scroll = true;
	cal1.time_comp = false;

	var cal2 = new calendar(
			document.forms['createLoanPackageForm'].elements['nextRepaymentDate']);
	cal1.year_scroll = true;
	cal1.time_comp = false;
</script>

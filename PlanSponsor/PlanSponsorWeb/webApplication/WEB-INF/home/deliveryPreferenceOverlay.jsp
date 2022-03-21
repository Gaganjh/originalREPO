<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<script language="JavaScript1.2" type="text/javascript">
	var submitted = false;

	function doSubmit(href) {
		if (!submitted) {
			submitted = true;
			window.location.href = href;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return;
		}
	}
</script>
<table width="520	" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>

			<td width="50%" valign="top" class="greyText"><img
				src="<content:pageImage type="pageTitle" beanName="layoutPageBean"/>"
				alt="<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>">
		
				</td>

		</tr>
		<tr>
			<td width="50%" valign="top"><content:errors scope="request" />
			</td>

		</tr>
		<tr>
			<td>&nbsp</td>
		</tr>
		<tr>
			<td><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></td>
		</tr>
		<tr>
			<td>&nbsp</td>
		</tr>

		<tr>

			<td align="right"><input type="button"
				onclick="doSubmit('/do/profiles/editMyProfile/?loginFlow=Y')"
				name="action" class="button100Lg" value="continue" /></td>
		</tr>
		<tr>
			<td>&nbsp</td>
		</tr>
		<tr>
			<td>&nbsp</td>
		</tr>
	</tbody>
</table>

<html>
	<body>
		<form name="autoform" action="${redirected_ISAM_MLC_URL}"	method="POST">
			<input name="mpjwt" type="hidden" value="${generatedAuthToken}" /> 
			<input name="authorization" type="hidden" value="${authorization}" /> 
		</form>
		
		<script type="text/javascript">
			document.autoform.submit();
		</script>
	</body>
</html>
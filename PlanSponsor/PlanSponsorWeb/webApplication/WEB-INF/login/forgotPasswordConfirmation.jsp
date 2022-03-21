<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<script language="JavaScript1.2" type="text/javascript">
<!--

var submitted=false;

function setButtonAndSubmit(button) {
	
	if (!submitted) {
		submitted=true;
		document.passwordResetAuthenticationForm.button.value = button;
		document.passwordResetAuthenticationForm.submit();
	} else {
		window.status = "Transaction already in progress.  Please wait.";
	}
}

-->
</script>


<TABLE width="550" border="0" cellPadding="0" cellSpacing="0" align="top">
  
  <TR> 
    <!-- column 1 (15 + 135 + 15 = 165) -->
    <TD width="10"><IMG height=8 
      src="/assets/unmanaged/images/s.gif" width=10 border=0></TD>
    <TD width="100" vAlign=top>
      <TABLE id=column1 cellSpacing=0 cellPadding=0 border=0>
          <TR vAlign=top>
            <TD><IMG src="/assets/unmanaged/images/s.gif" width=100 height=1></TD>
          </TR>
          <TR>
            <TD class=greyText>&nbsp;</TD>
          </TR>
      </TABLE>
    </TD>
    <TD width="15"><IMG src="/assets/unmanaged/images/s.gif" width=15 border=0 height=1></TD>
    <!-- end column 1 -->
    <!-- column 2 (375) -->
    <TD width="425" vAlign=top class=greyText> 
	  <content:errors scope="request"/> 
	  <ps:form method="POST" modelAttribute="passwordResetAuthenticationForm" name="passwordResetAuthenticationForm" action="/do/login/passwordResetConfirmation/" >
	   	  	
      <table width="425" border="0" cellpadding="0" cellspacing="0" align="top">
        <tr >
          <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
          <td ><content:getAttribute beanName="layoutPageBean" attribute="body1">
<content:param>${passwordResetAuthenticationForm.maskedEmailAddress}</content:param>
          		</content:getAttribute>
          </td>
        </tr>
      </table>
      <br>
      <table width="425" border="0" cellspacing="0" cellpadding="0">
        <tr align="center">
          <td width="112">&nbsp;</td>
          <td width="139">
<input type="button" onclick="javascript:window.print()" name="abc" class="button100Lg" value="print"/>


		  </td>
          <td width="144">
<input type="submit" class="button100Lg" onclick="javascript:setButtonAndSubmit('finished');return false;" value="finish" name= "fini"/>
<form:hidden path="button"/>
            <script type="text/javascript" >
				var onenter = new OnEnterSubmit('fini', 'finish');
				onenter.install();
			  </script>

        </tr>
      </table>
 	</ps:form>
      <br>
	  <p><content:pageFooter beanName="layoutPageBean"/></p>
 	  <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 	  <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
    </TD>
  </TR>
</TABLE>

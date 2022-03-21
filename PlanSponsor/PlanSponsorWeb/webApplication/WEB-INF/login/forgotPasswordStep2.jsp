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
<TABLE width="550" border="0" cellPadding="0" cellSpacing="0">
  
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
      <table width="425" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
          <td valign="top" width="495">
				<font color="#CC6600">*</font> Required Information
          </td>
        </tr>
      </table>
	  <content:errors scope="session"/> 
	  <ps:form method="POST" modelAttribute="passwordResetAuthenticationForm" name="passwordResetAuthenticationForm" action="/do/login/passwordResetChallenge/" >	
	    
      <table width="425" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="113"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="463"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="113"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="tablehead">
          <td colspan="7" class="tableheadTD1"><strong> <content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></strong></td>
          <td width="1" class="databorder" align="left"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

        <tr class="datacell1" >
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="6" align="center">
            <TABLE border="0" cellPadding="3" cellSpacing="0" class="datacell1">
              <TR vAlign=top class="datacell1">
                <TD width="168"><strong>Challenge question</strong></TD>
<TD width="250">${passwordResetAuthenticationForm.savedChallengeQuestion}</TD>
              </TR>
              <TR vAlign=top class="datacell1">
                <TD><strong>Answer</strong> <font color="#CC6600">*</font></TD>
                <TD><span class="bodytext"><font face="Verdana, Arial, Geneva, sans-serif" size="1">
                  <form:password path="challengeAnswer" cssClass="inputField" size="40" maxlength="32"/>
                  </font></span>
                </TD>
              </TR>
            </TABLE>             
          </td>
          <td width="1" class="databorder" ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

        <tr class="datacell1">
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="3" height="1"></td>
          <td colspan="2" rowspan="2" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif"  height="5"></td>
        </tr>
        <tr>
          <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" border="0" width="1" height="1"></td>
        </tr>
      </table>
      <br>
      <table width="425" border="0" cellspacing="0" cellpadding="0">
        <tr align="center">
          <td width="112">&nbsp;</td>
          <td width="139">            
<input type="submit" class="button100Lg" onclick="javascript:setButtonAndSubmit('cancel');return false;" value="cancel" name="cont"/>
          </td>
          <td width="144">
<input type="submit" class="button100Lg" onclick="javascript:setButtonAndSubmit('continue');return false;" value="continue" name="cont"/>
          </td>
<form:hidden path="button"/>
            <script type="text/javascript" >
				var onenter = new OnEnterSubmit('cont', 'continue');
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
<script>
	setFocusOnFirstInputField("passwordResetAuthenticationForm");
</script>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%@ page import="com.manulife.pension.ps.web.Constants" %>

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

<TABLE width=550 border="0" cellPadding="0" cellSpacing="0">  
  <TR>
    <TD width="10"><IMG height=1 
      src="/assets/unmanaged/images/s.gif" width=10 border=0></TD>
    <TD width="100" vAlign=top>
      <TABLE id=column1 cellSpacing=0 cellPadding=0 border=0>
        <TBODY>
          <TR vAlign=top>
            <TD><IMG src="/assets/unmanaged/images/s.gif" width=100 height=1></TD>
          </TR>
          <TR>
            <TD class=greyText>&nbsp;</TD>
          </TR>
        </TBODY>
      </TABLE>
    </TD>
    <TD width="15"><IMG src="/assets/unmanaged/images/s.gif" width=15 border=0 height=1></TD>
    <!-- end column 1 -->


    <!-- column 2 -->
	<ps:form method="POST" modelAttribute="passwordResetAuthenticationForm"  name="passwordResetAuthenticationForm"   action="/do/login/passwordResetAuthentication/" >

    <TD width="425" vAlign=top class=greyText> 
      <table width="425" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
          <td valign="top" width="495">
          		<font color="#CC6600">*</font> Required Information<br><br>
          </td>
        </tr>
      </table>
	  <content:errors scope="session"/> 
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

        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="6" align="center"><TABLE border="0" cellPadding="3" cellSpacing="0" class="datacell1">
            <TBODY>
              <TR vAlign=top class="datacell1">
                <TD width="168"><strong>Contract number<font color="#CC6600">*</font></strong></TD>
				<TD width="250">
				<form:input path="contractNumber" styleClass="inputField" size="40" maxlength="<%=Constants.STR_CONTRACT_NUMBER_MAX_LENGTH%>"/></TD>
              </TR>
              <TR vAlign=top class="datacell1">
                <TD><span ><strong>Social Security number</strong> </span> <font color="#CC6600">*</font></TD>
                <TD><span class="bodytext"><font face="Verdana, Arial, Geneva, sans-serif" size="1">
               <form:password path="ssn.digits[0]" id="ssn" showPassword="true" cssClass="inputField"  size="3" maxlength="3"/>
               <form:password path="ssn.digits[1]" id="ssn[1]" showPassword="true" cssClass="inputField"  size="3" maxlength="2"/>
               <form:password  path="ssn.digits[2]" id ="ssn[2]" showPassword="true" cssClass="inputField" size="4" maxlength="4"/>
                </font></span>
                </TD>
              </TR>
              <TR vAlign=top class="datacell1">
                <td><span ><strong>Email <font color="#CC6600">*</font></strong></span></td>
<TD><form:input path="emailAddress" size="40" cssClass="inputField"/>
                </TD>
              </TR>
            </TBODY>
          </TABLE>             
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="2" height="1"></td>
          <td  colspan="2" rowspan="2" align="left" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
        </tr>
        <tr>
          <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table>
      <br>
      <table width="425" border="0" cellspacing="0" cellpadding="0">
        <tr align="center">
          <td width="112">&nbsp;</td>
          <td width="139">            
<input type="submit" class="button100Lg" onclick="javascript:setButtonAndSubmit('cancel');return false;" value="cancel" name="cont" />
          </td>
          <td width="144">
<input type="submit" class="button100Lg" onclick="javascript:setButtonAndSubmit('continue');return false;" value="continue" name="cont" />
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

<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="loanContentConstants" className="com.manulife.pension.ps.web.onlineloans.LoanContentConstants" />

<content:contentBean
  contentId="${loanContentConstants.SECTION_TITLE_BANK_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  beanName="sectionTitleBankInformation"/>

<content:contentBean
  contentId="${loanContentConstants.BANK_DIALOG_FOOTER}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  beanName="bankDialogFooter"/>

<content:contentBean
  contentId="${loanContentConstants.BANK_LOOKUP_NO_DATA_FOUND}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  beanName="bankLookupNoDataFound"/>

<content:contentBean
  contentId="${loanContentConstants.BANK_LOOKUP_MORE_THAN_ONE_ENTRY_FOUND}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  beanName="bankLookupMoreThanOneEntryFound"/>

<script type="text/javascript">

function prepareBankLookupDialog(jsonString, selectBankName) {
  var banks = null;
  try { 
    banks = YAHOO.lang.JSON.parse(jsonString);
  } catch (e) {
    alert("Failed to lookup ABA number. Please contact the system administrator");
    return false;
  } 

  var foundMatch = false;
  $("#bankSelectionTable").empty();
  if (banks.length == 0) {
    $("#bankNameOtherRadio").prop("checked", true);
    $("#bankSelectionDiv").hide();
    $("#bankSelectionInitialMsg").html('<content:getAttribute escapeJavaScript="true" attribute="text" beanName="bankLookupNoDataFound"/>');
  } else {
    $("#bankSelectionInitialMsg").html('<content:getAttribute escapeJavaScript="true" attribute="text" beanName="bankLookupMoreThanOneEntryFound"/>');
    var html = '';
    for (var i = 0; i < 4 && i < banks.length; i++) {
      var bank = banks[i];
      html += '<tr><td valign="top" width="12%"><input type="radio" name="bank_hidden" value="1">';
      html += '</td><td valign="top" width="88%">' + bank.name + '</td></tr>';
    }
    $("#bankSelectionTableHidden").html(html);
    
    html = '';

    for (var i = 0; i < banks.length; i++) {
      var bank = banks[i];
      html += '<tr><td valign="top" width="12%"><input name="bank" type="radio" value="' + bank.name + '"';
      if (selectBankName == bank.name) {
        foundMatch = true;
        html += ' checked="checked" ';
      }
      html += '/></td><td valign="top" width="88%">' + bank.name + '</td></tr>';
    }
    $("#bankSelectionDivScrollView").height($("#bankSelectionTableHidden").height() + 5);
    $("#bankSelectionTable").html(html);
    $("#bankSelectionDiv").show();

    if (! foundMatch) {
      if (typeof(selectBankName) != "undefined" && selectBankName.length > 0) {
        $("#bankNameOtherInput").val(selectBankName);
      }
    } else {
      $("#bankNameOtherRadio").prop("checked", false);
      $("#bankNameOtherInput").val('');
    }
  }
  return true;
}

</script>

<div id="BankLookupDialog">
  <div class="hd" style="padding: 0px; background:none; background-color: #AFAFAF">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td style="padding: 5px">
        <font style="font-size: 15px; font-weight: bold;color: #ffffff">Bank Information</font>
      </td>
    </tr>
  </table>
  </div>

  <div class="bd" style="background-color:#ffffff; padding: 0px">  
  <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
      <td colspan="2">
        <table border="0" cellpadding="0" cellspacing="0" width="250">
          <tr>
            <td width="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="3" /></td>

            <td width="250">

              <div id="bankSelectionErrorMsg">
              &nbsp;
              </div>
               
              <div id="bankSelectionInitialMsg" style="color: red;">
              &nbsp;
              </div>

              <table style="display:none" id="bankSelectionTableHidden" border="0" cellpadding="0" cellspacing="0" width="230">
              </table>
              <div id="bankSelectionDiv">
                <div id="bankSelectionDivScrollView" style="width : 250px; height : 65px; overflow : auto;">
                  <table id="bankSelectionTable" border="0" cellpadding="0" cellspacing="0" width="230">
                  </table>
                </div>
              </div>

              <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                  <td><input id="bankNameOtherRadio" name="bank" type="radio" value="OTHER" /></td>
                  <td><input id="bankNameOtherInput" name="bankOther" type="text"
                             maxlength="70"
                             onkeyup="$('#bankNameOtherRadio').prop('checked', true)"/></td>
                </tr>

                <tr>
                  <td>&nbsp;</td>

                  <td>&nbsp;</td>
                </tr>

                <tr>
                  <td>&nbsp;</td>

                  <td>
                  <input id="bankLookupDialogCancel" type="button" class="button100Lg" value="cancel"/>
                  &nbsp;&nbsp;
                  <input id="bankLookupDialogSelect" type="button" class="button100Lg" value="select"/>
                  </td>
                </tr>

                <tr>
                  <td>&nbsp;</td>

                  <td>&nbsp;</td>
                </tr>
              </table>
            </td>

            <td width="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="3" /></td>
          </tr>

          <tr>
            <td colspan="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="1" /></td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  </div>
</div>

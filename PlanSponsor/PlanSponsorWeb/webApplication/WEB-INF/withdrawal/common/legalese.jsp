<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<style type="text/css">
    #theLayer {
        position: fixed;

        top: 35%;
        left: 130px;
        width: 500px;
        border: 0px;

        padding: 0px;
        margin: 0px;
        z-index: 101;
        display: none;

        position: fixed;
    }
    #theContentLayer {
        max-height: 260px;
        overflow: auto;
        padding: 10px;
    }
    #Err_Message_Frame {
        position: absolute;
        top: 0px;
        left: 0px;
        z-index: 100;
        background-color: black;
        opacity: 0.15;

        border: 0px;
        margin: 0px;
        padding: 0px;
        
        display: none;
    }
</style>

<!--[if IE]>
<style type="text/css">
    #theLayer {
        position: absolute;

        top: 35%;
        left: 130px;
        width: 500px;
        border: 0px;

        padding: 0px;
        margin: 0px;
        z-index: 101;
        display: none;
    }
    #theContentLayer {
        max-height: 260px;
        height: expression( this.scrollHeight > 260 ? '260' : 'auto');  /* sets max-height for IE */
        overflow: auto;
        padding: 10px;
    }
    #Err_Message_Frame {
        position: absolute;
        top: 0px;
        left: 0px;
        z-index: 100;
        background-color: black;
        filter: alpha(opacity=15); 

        border: 0px;
        margin: 0px;
        padding: 0px;
        
        display: none;
    }
</style>
<![endif]-->

<!--[if lt IE 7]>
<style type="text/css">
    #theLayer {
        position: absolute;

        top: 35%;
        left: 130px;
        width: 500px;
        border: 0px;

        padding: 0px;
        margin: 0px;
        z-index: 101;
        display: none;
    }
    #theContentLayer {
        height: expression( this.scrollHeight > 260 ? '260' : 'auto');  /* sets max-height for IE */
        overflow: auto;
        padding: 10px;
    }
</style>
<![endif]-->

<script language="JavaScript1.2" type="text/javascript">

function hideMe(){
        // hide IFRAME
        var iframe = document.getElementById('Err_Message_Frame');
        iframe.style.display = 'none';

        // hide layer
        var layer = document.getElementById('theLayer');
        layer.style.display = 'none';
}

function showMe(){
        // show IFRAME
        var iframe = document.getElementById('Err_Message_Frame');

        // Set it to the max value.        
        if (document.body.scrollHeight > document.body.offsetHeight) {
          iframe.style.height = document.body.scrollHeight;
        } else {
          iframe.style.height = document.body.offsetHeight;
        } // fi

        // Set it to the max value.        
        if (document.body.scrollWidth > document.body.offsetWidth) {
          iframe.style.width = document.body.scrollWidth;
        } else {
          iframe.style.width = document.body.offsetWidth;
        } // fi

        iframe.style.display = 'block';

        // show layer
        var layer = document.getElementById('theLayer');
        layer.style.display = 'block';
}

</script>
<content:contentBean
	contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_APPROVE_LEGALESE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" id="approveLegaleseText" />

<%-- Defines handlers for all editable fields --%>
<script type="text/javascript">
	function showLegalese() {
  		showMe();
   	}
   	
   	function hideLegalese() {
  		hideMe();
   	}
   	
<c:if test="${withdrawalForm.isLegaleseTextConfirmed}" >
	if (typeof(runOnLoad) == "function") {
	  runOnLoad( showLegalese );
	}
</c:if>
 
</script>


<!-- BEGIN FLOATING LAYER CODE //-->

<iframe 
  id="Err_Message_Frame" 
  src="/assets/unmanaged/images/s.gif"
  frameborder="0" 
  scrolling="no" 
  marginheight="0px" 
  marginwidth="0px"></iframe>


<div id="theLayer">
  <table class="box" border="0" width="100%" cellspacing="0"
    cellpadding="0">
    <tr>
      <td colspan="3">
        <table border="0" width="100%" cellspacing="0" cellpadding="0">
          <tr>
            <td
              style="padding-left: 6px; background : transparent url(/assets/unmanaged/images/clear_angle_blue_light.gif) no-repeat left top;" />
            <td class="tableheadTD1"
              style="background : #89A2B3; padding-left: 0px;" width="100%"
              colspan="2"><b>Certification</b></td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="boxborder" width="1"><img
        src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  
      <td colspan="1">
      <div id="theContentLayer">
        <table width="100%" cellspacing="0" cellpadding="0">
          <tr>
            <td width="100%">
              ${withdrawalForm.withdrawalRequestUi.withdrawalRequest.legaleseInfo.legaleseText}
            </td>
          </tr>
          <tr>
            <td width="100%">&nbsp;</td>
          </tr>
          <tr>
<td width="100%" align="center"><input type="submit" class="button100Lg" onclick="document.getElementById('action').value='approve';document.getElementById('legaleseConfirmFlagId').value='true'; processForm(); return false;" value="I agree"/>


&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit" class="button100Lg" onclick="document.getElementById('legaleseConfirmFlagId').value='false'; document.getElementById('withdrawalRequestUi.withdrawalRequest.ignoreWarnings').value='false'; document.getElementById('withdrawalRequestUi.withdrawalRequest.ignoreWarnings').disabled=false; hideLegalese(); return false;" value="I disagree"/>


            </td>
          </tr>
          <tr>
            <td width="100%">&nbsp;</td>
          </tr>
        </table>
      </div>
      <%-- End of "theContentLayer" --%>
      </td>
      <td class="boxborder" width="1"><img
        src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
  
    <%-- bottom of table --%>
    <tr class="boxborder">
      <td height="1" colspan="3" class="boxborder"><img height="1"
        src="/assets/unmanaged/images/s.gif" width="1"></td>
    </tr>
  </table>
</div>
<%-- End of "theLayer" --%>
<!-- END FLOATING LAYER CODE //-->

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentHelper" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%> 


<script
    language="javascript">
function doSubmit(button){

	document.forms.performanceChartInputForm.button.value=button;
	document.forms.performanceChartInputForm.submit();
}

</script>

<table border="0" cellspacing="3" cellpadding="2" width="700">
    <tr>
        <td width="715" valign="top">
        
<c:if test="${userProfile.currentContract.hasContractGatewayInd ==true}">
			<content:contentBean
				contentId="<%=ContentHelper.getContentIdByVersion(Constants.PSW_PC_GIFL_MESSAGE,userProfile)%>"
				type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="giflMessage" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="100%"></p><content:getAttribute id="giflMessage"
						attribute="text" /></p></td>
				</tr>
			</table>
</c:if>

        <table border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="100" rowspan="2"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
                <td valign="top" class="greyText" width="500"><content:errors scope="request"/><br>
                </td>
                <td width="100" rowspan="2"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
            </tr>
        </table>


<ps:form modelAttribute="performanceChartInputForm" name="performanceChartInputForm" method="GET" action="/do/investment/performanceChartInput/">
<form:hidden path="button" />
 <img src="/assets/unmanaged/images/s.gif" width="5" height="1"><br>
        <table width="715" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="301"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="242"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="164"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td class="tableheadTD1" colspan="8">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="tableheadTD"><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></td>
                    </tr>
                </table>
                </td>
            </tr>
            <tr>
                <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td valign="bottom" class="datacell1">&nbsp;</td>
                <td class="datadivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
                <td align="left" valign="bottom" class="datacell1"><b>Select Investment Option or Index</b></td>
                <td class="datadivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
                <td colspan="2" valign="bottom" class="datacell1"><b>Optional</b></td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td rowspan="7" valign="top" class="datacell1"><content:getAttribute attribute="body1" beanName="layoutPageBean"/></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datacell1">1. <form:select path="fundSelection1" cssClass="greyText" size="1" > <form:options items="${viewFunds}" itemValue="id" itemLabel="name"/> </form:select></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" class="datacell1">1. <form:input path="fundPercentage1" maxlength="3" size="3" cssClass="greyText" /> %</td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datacell1">2. <form:select path="fundSelection2" cssClass="greyText" size="1" > <form:options items="${viewFunds}" itemValue="id" itemLabel="name"/> </form:select></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" class="datacell1">2. <form:input path="fundPercentage2" maxlength="3" size="3" cssClass="greyText" /> %</td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datacell1">3. <form:select path="fundSelection3" cssClass="greyText" size="1" > <form:options items="${viewFunds}" itemValue="id" itemLabel="name"/> </form:select></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" class="datacell1">3. <form:input path="fundPercentage3" maxlength="3" size="3" cssClass="greyText" /> %</td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datacell1">4. <form:select path="fundSelection4" cssClass="greyText" size="1" > <form:options items="${viewFunds}" itemValue="id" itemLabel="name"/> </form:select></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" class="datacell1">4. <form:input path="fundPercentage4" maxlength="3" size="3" cssClass="greyText" /> %</td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datacell1">5. <form:select path="fundSelection5" cssClass="greyText" size="1" > <form:options items="${viewFunds}" itemValue="id" itemLabel="name"/> </form:select></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" class="datacell1">5. <form:input path="fundPercentage5" maxlength="3" size="3" cssClass="greyText" /> %</td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td class="datacell1">6. <form:select path="fundSelection6" cssClass="greyText" size="1" > <form:options items="${viewFunds}" itemValue="id" itemLabel="name"/> </form:select></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" class="datacell1">6. <form:input path="fundPercentage6" maxlength="3" size="3" cssClass="greyText" /> %</td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td height="15" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td align="right" class="datacell1"></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td colspan="2" align="right" class="datacell1"></td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td width="1" height="4" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                <td class="whiteborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="whiteborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="whiteborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td rowspan="2" colspan="2"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
            </tr>
            <tr align="top">
                <td colspan="6" class="databorder" align="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
        </table>


        <br>
        <br>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="297"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="117"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="278"><img src="/assets/unmanaged/images/s.gif"height="1"></td>
                <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
 
            <tr>
                <td class="tableheadTD1" colspan="8">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="tableheadTD"><content:getAttribute attribute="body2Header" beanName="layoutPageBean"/></td>
                    </tr>
                </table>
                </td>
            </tr>
           <tr>
                <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td valign="bottom" class="datacell1">&nbsp;</td>
                <td class="datadivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
                <td align="left" valign="bottom" class="datacell1"><b>Select Date Range</b></td>
                <td class="datadivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
                <td colspan="2" valign="bottom" class="datacell1">&nbsp;</td>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td rowspan="2" valign="top" class="datacell1"><content:getAttribute attribute="body2" beanName="layoutPageBean"/></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datacell1">Start Date (mm/yyyy)</td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" class="datacell1"><form:input path="startDate" maxlength="12" size="7" cssClass="greyText" />
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr>
                <td height="97" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td valign="top" class="datacell1">End Date (mm/yyyy)</td>
                <td valign="top" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" valign="top" class="datacell1"><form:input path="endDate" maxlength="12" size="7" cssClass="greyText" />
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr> 

            <tr >
                <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datadivider" ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td  colspan="2" rowspan="2" width="5" height="5"><img src="/assets/unmanaged/images/box_lr_corner.gif"></td>
            </tr>
            <tr class="lastrow">
                <td colspan="6" valign="top" class="databorder" ><img src="/assets/unmanaged/images/s.gif" height="1" width="1" ></td>
            </tr>
        </table>
        <br>
        <table  width="699" border="0" cellpadding="0" cellspacing="0">
            <tr align="right">
              <td width="553">           
              	<input name="submit1" type="submit" class="button100Lg" onClick="javascript:doSubmit('reset'); return false;" value="reset">
              </td>
              <td width="146">
              
			<input type="submit" class="button100Lg" onclick="doSubmit('view'); return false;" name="actionLabel" value="view chart" />
              </td>
              <script type="text/javascript" >
              //alert("1111");
				var onenter = new OnEnterSubmit('actionLabel', 'view chart');
				onenter.install();
			  </script>
            </tr>
        </table>
        </td>
        </ps:form>
    </tr>
</table>

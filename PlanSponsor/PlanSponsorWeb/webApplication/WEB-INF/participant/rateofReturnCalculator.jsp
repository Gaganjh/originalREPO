<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.content.valueobject.ContentType"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render" prefix="render"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/java-encoder.tld" prefix="e" %>

<content:contentBean
    contentId="<%=ContentConstants.FIXED_FOOTNOTE_PBA%>"
type="<%=ContentConstants.TYPE_PAGEFOOTNOTE%>" id="footnotePBA" />

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

    
<script type="text/javascript">

var isPageSubmitted = false;

window.onload = function() {
	//document.getElementById("radio99").checked=false;
}

function doBack() {
    document.rateOfReturnCalculatorForm.action.value = 'back';
    submitForm();
}

function doReset() {
    document.rateOfReturnCalculatorForm.action.value = 'reset';
    submitForm();
}

function doCalculate() {
    document.rateOfReturnCalculatorForm.action.value = 'calculate';
    submitForm();
}

function submitForm() {
    if (isPageSubmitted) {
        window.status = "Transaction already in progress.  Please wait.";
    } else {
        isPageSubmitted = true;
        document.rateOfReturnCalculatorForm.submit();
    }
}
    
function clearDateFields() {
    var frm = document.rateOfReturnCalculatorForm;

    document.getElementById("startMonth").value="";
    document.getElementById("startDay").value="";
    document.getElementById("startYear").value="";
    document.getElementById("endMonth").value="";
    document.getElementById("endDay").value="";
    document.getElementById("endYear").value="";

}

</script>

<table width="760" border="0" cellspacing="0" cellpadding="0">
    <tbody>
        <tr>
            <td width="30" valign="top"><img
                src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>
            <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
            </td>
            <td width="700" valign="top">
            <TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
                <TBODY>
                    <tr>
                        <td width="500"><img src="/assets/unmanaged/images/s.gif"
                            height="1"></td>
                        <td width="20"><img src="/assets/unmanaged/images/s.gif"
                            height="1"></td>
                        <td width="180"><img src="/assets/unmanaged/images/s.gif"
                            height="1"></td>
                    </tr>
                    <tr>
                        <TD vAlign="top"><IMG src="/assets/unmanaged/images/s.gif" width="500" height="23"><BR>
                            <IMG src="/assets/unmanaged/images/s.gif" width="5" height="1"> <img src='<content:pageImage type="pageTitle" beanName="layoutPageBean"/>' alt="Rate of Return"><BR>
                            <TABLE border="0" cellSpacing="0" cellPadding="0" width="500">
                                <TBODY>
                                    <TR>
                                         <TD width="5"><IMG src="/assets/unmanaged/images/s.gif" width="5" height="1"></TD>
                                         <TD vAlign="top"><BR>              
                                         <!--<content:getAttribute attribute="introduction1" beanName="layoutPageBean"/>
                                         <br><br>-->                    
                                         <content:getAttribute attribute="introduction2" beanName="layoutPageBean"/>
                                         </TD>
                                         <TD width="20"><IMG src="/assets/unmanaged/images/s.gif" width="20" height="1"></TD>
                                         <TD vAlign="top" width="180"></TD>
                                    </TR>
                                </TBODY>
                            </TABLE>
                        </TD>
                        <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
                        <td valign="top" class="right">
                              <img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
                              <jsp:include page="/WEB-INF/global/standardreportlistsection.jsp" flush="true" />
                              <jsp:include page="/WEB-INF/global/standardreporttoolssection.jsp" flush="true" />
                        </td>
                    </tr>
                </tbody>
            </table>
            <IMG src="/assets/unmanaged/images/s.gif" width=1 height=20><BR></TD>
            <TD vAlign=top width=15><IMG src="/assets/unmanaged/images/s.gif" width=15 height=1></TD>
        </tr>
    </tbody>
</table>

<ps:form method="post" action="/do/db/rateOfReturnCalculator/" modelAttribute="rateOfReturnCalculatorForm" name="rateOfReturnCalculatorForm">
<input type="hidden" name="action"/>
<TABLE border=0 cellSpacing=0 cellPadding=0 width=760>
    <TBODY>
        <TR>
            <TD width="30">&nbsp;</TD>
            <TD width="715">
	            <TABLE border=0 cellSpacing=3 cellPadding=2 width=715>
					<TBODY>
						<TR>
							<TD vAlign=top width=715>
								<div class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
						        <content:errors scope="session" />
						        </div>
								<TABLE border=0 cellSpacing=0 cellPadding=0>
									<TBODY>
										<TR>
											<TD rowSpan=2 width=100><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
											<TD class=greyText vAlign=top width=500><BR></TD>
											<TD rowSpan=2 width=100><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=15   height=1></TD>
										</TR>
									</TBODY>
								</TABLE>                                 
                                                                
				                <!-- begin main content table --><IMG src="/assets/unmanaged/images/s.gif" width="5" height="1"><BR>
				                <TABLE border="0" cellSpacing="0" cellPadding="0" width="715" style="table-layout:fixed;">
				                    <TBODY>
				                        <TR>
				                            <TD width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD width="267"><IMG src="/assets/unmanaged/images/s.gif" height="1"></TD>
				                            <TD width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD width="290"><IMG src="/assets/unmanaged/images/s.gif" height="1"></TD>
				                            <TD width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD width="150"><IMG src="/assets/unmanaged/images/s.gif" height="1"></TD>
				                            <TD width="4"><IMG src="/assets/unmanaged/images/s.gif" width="4" height="1"></TD>
				                            <TD width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                        </TR>
				                        <TR class="tablehead">
				                            <TD class="tableheadTD1" colSpan="8"><B><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></B></TD>
				                        </TR>
				                        <TR class="tablesubhead">
				                            <TD class="databorder" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD vAlign="bottom">&nbsp;</TD>
				                            <TD class="datadivider" vAlign="bottom" width="1"><B><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></B></TD>
				                            <TD vAlign="bottom" align="left"><B>Choose a time period</B></TD>
				                            <TD class="datadivider" vAlign="bottom" width="1"><B><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></B></TD>
				                            <TD vAlign="bottom" colSpan="2"><B>Rate of Return</B></TD>
				                            <TD class="databorder"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                        </TR>
				                        <TR>
				                            <TD class="databorder"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD class="datacell1" vAlign="top" style="padding: 4px;">
				                            <content:getAttribute beanName="layoutPageBean" attribute="body1"/></TD>
				                            <TD class="datadivider"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD class="datacell1" vAlign=top>
				                                <TABLE border="0" cellSpacing="0" cellPadding="0" width="290">
				                                    <tr valign="center">
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" value="1" /></td>
				                                        <td width="90%">1 month ago</td>
				                                    </tr>
				                                    <tr valign="center">
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" value="3" /></td>
				                                        <td width="90%">3 months ago</td>
				                                    </tr>
				                                    <tr valign="center">
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" value="6" /></td>
				                                        <td width="90%">6 months ago</td>
				                                    </tr>
				                                    <tr>
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" value="12" /></td>
				                                        <td width="90%">12 months ago</td>
				                                    </tr>
				                                    <tr>
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" value="0" /></td>
				                                        <td width="90%">Year to date</td>
				                                    </tr>
				                                    <tr>
<td><form:radiobutton onclick="clearDateFields();" path="timePeriodFromToday" value="99" id="radio99"/></td>
				                                        <td width="90%">Or enter a specific time period</td>
				                                    </tr>
				                                </table>
				                                <table border="0">
				                                    <tr>
				                                        <td width="23%">Start date:</td>
				                                        <td width="77%">
				                                            <input type="text" name="startMonth" id="startMonth" size="2" tabindex="9" maxlength="2"
				                                            value="${e:forHtmlAttribute(rateOfReturnCalculatorForm.startMonth)}"
				                                            onFocus="this.form.timePeriodFromToday[5].checked=true;">
				                                            <input type="text" name="startDay" id="startDay" size="2" tabindex="10" maxlength="2"
				                                            value="${e:forHtmlAttribute(rateOfReturnCalculatorForm.startDay)}"
				                                            onFocus="this.form.timePeriodFromToday[5].checked=true;">
				                                            <input type="text" name="startYear" id="startYear" size="4" tabindex="11" maxlength="4"
				                                            value="${e:forHtmlAttribute(rateOfReturnCalculatorForm.startYear)}"
				                                            onFocus="this.form.timePeriodFromToday[5].checked=true;">
				                                            mm/dd/yyyy
				                                        </td>
				                                    </tr>                                    
				                                    <tr>
				                                        <td>End date:</td>
				                                        <td>
				                                            <input type="text" name="endMonth" id="endMonth" size="2" tabindex="12" maxlength="2"
				                                            value="${e:forHtmlAttribute(rateOfReturnCalculatorForm.endMonth)}"
				                                            onFocus="this.form.timePeriodFromToday[5].checked=true;">
				                                            <input type="text" name="endDay" id="endDay" size="2" tabindex="13" maxlength="2"
				                                            value="${e:forHtmlAttribute(rateOfReturnCalculatorForm.endDay)}"
				                                            onFocus="this.form.timePeriodFromToday[5].checked=true;">
				                                            <input type="text" name="endYear" id="endYear" size="4" tabindex="14" maxlength="4"
				                                            value="${e:forHtmlAttribute(rateOfReturnCalculatorForm.endYear)}"
				                                            onFocus="this.form.timePeriodFromToday[5].checked=true;">
				                                            mm/dd/yyyy
				                                        </td>
				                                </tr>                                
				                                </table>
				                            </TD>
				                            <TD class="datadivider"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD class="datacell1" colSpan="2" vAlign="top" style="padding: 4px;">
<c:if test="${rateOfReturnCalculatorForm.resultsCalculatedFlag ==true}">
Rate of Return for the period beginning ${rateOfReturnCalculatorForm.resultStartDate} and
ending ${rateOfReturnCalculatorForm.resultEndDate} is
<b class="highlight">${rateOfReturnCalculatorForm.rateOfReturn}</b>%
</c:if>
<c:if test="${rateOfReturnCalculatorForm.resultsCalculatedFlag ==false}">
				                                	Please select the time period and click calculate button to determine the contract's rate of return
</c:if>
				                            </TD>
				                            <TD class="databorder"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                        </TR>
				                        <TR>
				                            <TD class="databorder" height="4" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="4"></TD>
				                            <TD class="whiteborder" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD class="datadivider" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD class="whiteborder" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD class="datadivider" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD class="whiteborder" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
				                            <TD rowspan="2" colspan="2"><IMG src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></TD>
				                        </TR>
				                        <TR valign="top">
				                            <TD class="databorder" colSpan="6" valign="top"><IMG src="/assets/unmanaged/images/s.gif" width="1"   height="1"></TD>
				                        </TR>
				                    </TBODY>
				                </TABLE>
				                <BR>
				                <BR>
				                <TABLE border="0" cellSpacing="0" cellPadding="0" width="699">
				                    <TBODY>
				                        <TR>
				                            <TD width="374">
				                                <DIV align="right">
<input type="button" onclick="return doBack();" name="button" class="button134" title="Return to DB Account" value="back"/>


				                                </DIV>
				                            </TD>
				                            <TD width="181">
				                                <DIV align="center">
<input type="button" onclick="return doReset();" name="button" class="button134" title="Reset the fields" value="reset"/>


				                                </DIV>
				                            </TD>
				                            <TD width="136">
				                                <DIV align="right">
<input type="button" onclick="return doCalculate();" name="button" class="button134" title="Calculate" value="calculate"/>


				                                </DIV>
				                            </TD>
				                        </TR>
				                    </TBODY>
				                </TABLE>
				            </TD>
				       </TR>
				    </TBODY>
				</TABLE>
			</TD>
		</TR>
	</TBODY>
</TABLE>
<!--page footer section-->
<table width="765"  border="0" cellspacing="0" cellpadding="0">
<tr>
<td width="30">&nbsp;</td>
  <td width="735">
  <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
  <br><br>
  <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
      </td>
  </tr>    
    </table>
</ps:form>              


<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/java-encoder.tld" prefix="e" %>

        

<link rel="stylesheet" href="/assets/unmanaged/stylesheet/manulife.css" type="text/css">	
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/plansponsor.css" type="text/css">	
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/buttons.css" type="text/css">		                   

<%-- Load IE 6 specific Styles to handle width issues --%>
<!--[if lt IE 7]>
  <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
<![endif]-->
<SCRIPT language=JavaScript1.2 type=text/javascript>
<!--
function doDelete(){
	
	document.getElementById('submissionId').value = '${deletePIFDataForm.submissionId}';
	document.getElementById('contractNumber').value = '${deletePIFDataForm.contractNumber}';
	document.deletePIFDataForm.action="/do/contract/pic/delete/?action=delete";
	document.deletePIFDataForm.submit();
}

function doCancel(){
    document.getElementById('submissionId').value = '${deletePIFDataForm.submissionId}';
	document.deletePIFDataForm.action="/do/contract/pic/delete/?action=cancel";
	document.deletePIFDataForm.submit();
		
}
-->
</SCRIPT>

<%-- Error Table --%>
<div id="messagesBox" class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
	<ps:messages scope="request" maxHeight="${param.printFriendly ? '1000px' : '100px'}" suppressDuplicateMessages="true" />
</div>

<ps:form method="POST" action="/do/contract/pic/delete/" modelAttribute="deletePIFDataForm" name="deletePIFDataForm">
	<form:hidden path="submissionId" />
	<form:hidden path="contractNumber" />
<%--------------  body  -----------------------%>
<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
  <TBODY>
  <TR>
    <TD width=20>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
    <TD width=710>
	    <TABLE border=0 cellSpacing=0 cellPadding=0 width=710>
              <TBODY>
              <TR>
                <TD>
                  <P><B></B></P></TD>
                <TD width=10><IMG border=0 
                  src="/assets/unmanaged/images/spacer.gif" 
                  width=15 height=1></TD>
                <TD>&nbsp;</TD></TR>
              <TR>
                <TD vAlign=top width=710>
                  <TABLE border=0 cellSpacing=0 cellPadding=0 width=710>
                    <TBODY>
                    <TR>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=actions><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=35 height=1></TD>
                      <TD class=submissionDate><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=40 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=type><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=130 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=payrollDate><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=130 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=0 height=1></TD>
                      <TD align="left" class=contributionTotal><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
                      <TD class=paymentTotal><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=180 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=0 height=1></TD>
                      <TD><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD></TR>
                    <TR class=tablehead>
                      <TD class=tableheadTD1 height=25 vAlign=center colSpan=7><B>Delete submission draft</B></TD>
                      <TD class=databorder><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD><TD class=databorder><IMG 
                        src="/assets/unmanaged/images/s.gif" 
                        width=1 height=1></TD>
						</TR>
					
					<TR class="n2">
						<TD class=databorder>
							<IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
						</TD>
						<TD colSpan=7>
							<TABLE border="0" width="100%" cellPadding=2>
							<TR class="n2">
									<TD vAlign=top >
										<B>Contract number</B>
									</TD>
									<TD span class="highlight">
<B> ${deletePIFDataForm.contractNumber}		</B>
									</TD> 
								</TR>
								<TR class="n2">
									<TD vAlign=top>
										<B>Contract name</B>
									</TD>
									<TD class="highlight">
<B>${deletePIFDataForm.contractName}</B>
									</TD> 
								</TR>
								<TR class="n2">
								    <TD vAlign=top>
									    <B>Date updated</B> 
								    </TD>
									<TD class="highlight">  
<B>${e:forHtmlContent(deletePIFDataForm.lastUpdatedDate)}</B>
									</TD>
									</TR>
									<TR class="n2">
									<TD vAlign=top>
										<b>Updated by</b>
									</TD>
									<td class="highlight">
<B>${e:forHtmlContent(deletePIFDataForm.userName)}</B>
									</td>	
									</TR>
									<TR class="n2"> 
										<TD >
										</TD>
										<TD vAlign=bottom> <BR>
										</TD>   
									</TR>
					
							</TABLE>
						</TD>
						<TD class=databorder>
							<IMG src="/assets/unmanaged/images/s.gif" width=1 height=1>
						</TD>
					</TR>
					<TR ><TD class=databorder colSpan=9><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD></TR>
				</TBODY>
				</TABLE>
			</TD>
			</TR>

			</TBODY>
		</TABLE>
	</TD>
	</TR>
	<TR class="yui-skin-sam"> 
	<TD width=20></TD>
		<TD vAlign=bottom> <BR>
		</TD>   
	</TR>
	<TR class="yui-skin-sam"> 
	  <TD width=20>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
	  <TD vAlign=bottom> 
			<input type="button" onclick="doCancel();" name="back" class="button100Lg" value="back">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input  type="button" onclick="doDelete();" class="button100Lg" name="delete" value="delete">
			
	  		</TD>      
	</TR>	
</TBODY>
</TABLE> 
</ps:form>

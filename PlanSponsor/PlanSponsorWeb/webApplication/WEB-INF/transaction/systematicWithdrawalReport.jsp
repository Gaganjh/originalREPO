<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib prefix="psreport" uri="manulife/tags/report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ page
	import="com.manulife.pension.ps.web.transaction.SystematicWithdrawReportForm"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page
	import="com.manulife.pension.platform.web.util.CommonEnvironment"%>
<%@ page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawReportData" %>
<%@ page import="com.manulife.pension.service.contract.report.valueobject.SystematicWithdrawDataItem" %>
<%@ page import="java.util.Collection"%>
<%@ page import="org.springframework.validation.BeanPropertyBindingResult" %>
<%
String ua = request.getHeader( "User-Agent" );
boolean isMSIE = (ua != null && ua.indexOf("MSIE") != -1);
request.setAttribute("isIE",isMSIE);
boolean isTrident = (ua != null && ua.indexOf("Trident") != -1);
request.setAttribute("isNewIE", isTrident);
boolean isFirefox = (ua != null && ua.indexOf("Firefox") != -1);
request.setAttribute("isMozilla", isFirefox);


BeanPropertyBindingResult errorBean = (BeanPropertyBindingResult)request.getAttribute(CommonEnvironment.getInstance().getErrorKey());
pageContext.setAttribute("errorBean", errorBean);
%>
<jsp:useBean id="systematicWithdrawReportForm" scope="session" type="com.manulife.pension.ps.web.transaction.SystematicWithdrawReportForm"/>
<c:set var="printFriendly" value="${param.printFriendly}" />

<%
SystematicWithdrawReportData theReport = (SystematicWithdrawReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>







<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="csvIcon" />
<content:contentBean
	contentId="<%=ContentConstants.MESSAGE_STMT_NO_PARTICIPANTS_ON_BASIC_SEARCH%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="noResultForSearch" />
<content:contentBean
	contentId="<%=ContentConstants.MESSAGE_INSTRUCTION_ABOVE_THE_SEARCH_FIELDS%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="instructionForSearch" />
<script language="JavaScript" type="text/javascript"
	src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript" >

var previousSelectionIndex = 0;

function setFilterFromSelect2(theSelect)
{
  var newValue = theSelect.options[theSelect.selectedIndex].value;
  if ( newValue != "##" ) {
	  filterMap[theSelect.name] = newValue;
  }
  else {
	  theSelect.selectedIndex = previousSelectionIndex;
  }
}

function setPreviousSelection(theSelect)
{
  previousSelectionIndex = theSelect.selectedIndex;
}

function clearName(evt){

		//IE or browsers that use the getElementById model
		if (document.systematicWithdrawReportForm.participantName) {
		document.systematicWithdrawReportForm.participantName = "";
	}

		
		//Netscape or browsers that use the document model
	  	evt = (evt) ? evt : (window.event) ? event : null;
	  	if (evt)
	  	{	
	    	var charCode = (evt.charCode) ? evt.charCode :
	                   ((evt.keyCode) ? evt.keyCode :
	                   ((evt.which) ? evt.which : 0));
	    	if (charCode == 9) {
	    		return false;
	    	}
	  	}    


		
		if (document.systematicWithdrawReportForm.ssnOne) {
				if (document.systematicWithdrawReportForm.ssnOne.value.length >= 0){
						document.systematicWithdrawReportForm.participantName.value = "";
				}
		}


		if (document.systematicWithdrawReportForm.ssnTwo) {
				if (document.systematicWithdrawReportForm.ssnTwo.value.length >= 0){	
					document.systematicWithdrawReportForm.participantName.value = "";
				}
		}

		if (document.systematicWithdrawReportForm.ssnThree) {
				if (document.systematicWithdrawReportForm.ssnThree.value.length >= 0){	
					document.systematicWithdrawReportForm.participantName.value = "";
				}
		}

	}
	
	
function clearSSN(evt){

	//IE or browsers that use the getElementById model
	
	if (document.systematicWithdrawReportForm.ssnOne) {
		
		document.systematicWithdrawReportForm.ssnOne = "";
	} 

	if (document.systematicWithdrawReportForm.ssnTwo) {
		document.systematicWithdrawReportForm.ssnTwo = "";
	}
	if (document.systematicWithdrawReportForm.ssnThree) {
		document.systematicWithdrawReportForm.ssnThree = "";
	}

	
	//Netscape or browsers that use the document model
	evt = (evt) ? evt : (window.event) ? event : null;
  	if (evt)
  	{	
    	var charCode = (evt.charCode) ? evt.charCode :
                   ((evt.keyCode) ? evt.keyCode :
                   ((evt.which) ? evt.which : 0));
    	if (charCode == 9) {
    		return false;
    	}
  	}    
	
	
	if (document.systematicWithdrawReportForm.participantName) {
		
			if (document.systematicWithdrawReportForm.participantName.value.length >= 0){
				document.systematicWithdrawReportForm.ssnOne.value = "";
				document.systematicWithdrawReportForm.ssnTwo.value = "";
				document.systematicWithdrawReportForm.ssnThree.value = "";
			}	
	}
		
}

//Called when reset is clicked
function doReset() {
	document.systematicWithdrawReportForm.task.value = "reset";
	return true;
}

</script>
 <p>
      <c:if test="${not empty sessionScope.psErrors}">
      <c:if test="${printFriendly == null }" >
<c:set var="errorsExist" value="${true}" scope="page" />
        <div id="errordivcs"><content:errors scope="session"/></div>
      </c:if>
      <c:if test="${printFriendly != null }" >
          <%pageContext.removeAttribute("psErrors") ;%>
      </c:if>
      </c:if>
      </p>
<%-- <report:formatMessages scope="request" /> --%>
  <content:errors scope="session"/> 
<ps:form modelAttribute="systematicWithdrawReportForm" name="systematicWithdrawReportForm" action="/do/transaction/systematicWithdrawalReport/" method="post">

<input type="hidden" name="task" value="filter"/>
	<TABLE width="100%" height="140" bgcolor="beige" cellspacing="0"
		cellpadding="2">
		<!-- Start of body title -->
		<TBODY>
			</tr>
			<tr class="tablehead">

				<TD class="tablesubhead" colspan="6"><B>Systematic
						Withdrawals</B> as of <render:date
						patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>"
						property="systematicWithdrawReportForm.asOfDate" /></TD>
			</tr>
			<!-- End of body title -->
			<tr class="filterSearch">
				<TD colspan="6"><content:getAttribute id="instructionForSearch"
						attribute="text" /></TD>
			</tr>
			
			<tr class="filterSearch">
			 <td width="180" valign="bottom" class="filterSearch"><DIV id="wdStatusDiv"><b>Withdrawal status</b><br>
			 <c:if test="${printFriendly != null }" >
<form:select path="wdStatus" disabled="true" onchange="setFilterFromSelect2(this);" >
					<%-- set the first value of the select --%>
					<form:option value="All">All</form:option>
<form:options items="${systematicWithdrawReportForm.statusList}" itemLabel="label" itemValue="value"/>
</form:select>
			    </c:if>
				<c:if test="${printFriendly == null }" >
					<form:select path="wdStatus" >
				    <%-- set the first value of the select --%>
					<form:option value="All">All</form:option>
<form:options items="${systematicWithdrawReportForm.statusList}"  itemLabel="label" itemValue="value"/>
					</form:select>
			    </c:if>
					</DIV>
	        </td>
	        <td width="180" valign="bottom" class="filterSearch"><DIV id="participantStatus"><b>Withdrawal type</b><br>
	        <c:if test="${printFriendly != null }" >
<form:select path="wdType" disabled="true" onchange="setFilterFromSelect2(this);" >
					<%-- set the first value of the select --%>
					<form:option value="All">All</form:option>
<form:options items="${systematicWithdrawReportForm.typeList}"  itemLabel="label" itemValue="value"/>
</form:select>
			    </c:if>
				<c:if test="${printFriendly == null }" >
					<form:select path="wdType" >
				    <%-- set the first value of the select --%>
					<form:option value="All">All</form:option>
<form:options items="${systematicWithdrawReportForm.typeList}"  itemLabel="label" itemValue="value"/>
					</form:select>
			    </c:if>
					</DIV>
	        </td>
						
			<td width="219" valign="bottom" class="filterSearch"><ps:label fieldId="lastName" mandatory="false"><b>Last Name</b></ps:label><br>
                <c:if test="${printFriendly != null }" >
<form:input path="participantName" maxlength="30" readonly="true" cssClass="inputField"/>
			    </c:if>
			    <c:if test="${printFriendly == null }" >
<form:input path="participantName" maxlength="30" onchange="setFilterFromInput(this);" onkeyup="clearSSN(event);" cssClass="inputField" tabindex="19"/>
			    </c:if>
            </td>
					
			 <td width="180" valign="bottom" class="filterSearch">
            	<ps:label fieldId="ssn" mandatory="false"><b>SSN</b></ps:label><br>
                <c:if test="${printFriendly != null }" >
				<form:password path="ssnOne" cssClass="inputField" readonly="true"
						onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
				<form:password path="ssnTwo" cssClass="inputField" readonly="true"
						onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2"/>
				<form:input path="ssnThree" autocomplete="off" cssClass="inputField" readonly="true"
						onkeyup="return autoTab(this, 4, event);"  size="4" maxlength="4"/>
				</c:if>
				<c:if test="${printFriendly == null }" >
				<form:password path="ssnOne"  cssClass="inputField" onkeypress = "clearName(event);" 
						onkeyup="clearName(event); return autoTab(this, 3, event);" size="3" maxlength="3" tabindex="30"/>
				<form:password path="ssnTwo"  cssClass="inputField" onkeypress = "clearName(event);" 
						onkeyup="clearName(event); return autoTab(this, 2, event);" size="2" maxlength="2" tabindex="31"/>
				<form:input path="ssnThree"  cssClass="off" styleClass="inputField"   onkeypress = "clearName(event);" 
						onkeyup="clearName(event); return autoTab(this, 4, event);" size="4" maxlength="4" tabindex="32"/>
				
			
				</c:if>
            </td></tr>
            <tr class="filterSearch">
            <td colspan="3"></td>
	        <td valign="middle" class="filterSearch"><div align="left">
	        <c:if test="${printFriendly == null }" >
	            <input type="submit" name="submit" value="Search" tabindex="70"/>
	            <input type="submit" name="reset" value="Reset" tabindex="70" onclick="return doReset();"/>
			</c:if>
	        </div></td>
            
			</tr>
		</TBODY>
	</table>
     
              	
   &nbsp;&nbsp;

<div class="report_table">
		<c:if test="${theReport != null}">
<c:if test="${not empty theReport.details}">

				<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
					<TBODY>
						<TR class="tablehead">
							<td class="tableheadTD1" colspan="19">
								<TABLE width="100%">
									<TBODY>
										<TR>
											<TD width="10%" class="tableheadTD">&nbsp;</TD>
											<TD width="80%" class="tableheadTDinfo" align="center"><div
													class="table_display_info">
													<strong><report:recordCounter report="theReport"
															label="Systematic Withdrawals" /></strong>
												</div></TD>
											<TD width="10%" class="tableheadTDinfo" align="right">
												<div class="table_pagination">
													<report:pageCounter formName="systematicWithdrawReportForm" report="theReport" arrowColor="black" />
												</div>
												<div class="table_controls_footer"></div>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
							</TD>

						</TR>
					</TBODY>
				</TABLE>
</c:if>
		</c:if>

		<c:if test="${theReport != null}">
<c:if test="${empty theReport.details}">

				<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
					<TBODY>
						<TR class="tablehead">
							<td class="tableheadTD1" colspan="19">
							
								<TABLE width="100%">
									<TBODY>
										<TR>

											<TD width="10%" class="tableheadTD">&nbsp;</TD>
											<TD width="80%" class="tableheadTDinfo" align="center"><div
													class="table_display_info">
													<strong><report:recordCounter report="theReport"
															label="Systematic Withdrawals" /></strong>
												</div></TD>
											<TD width="10%" class="tableheadTDinfo" align="right">
												<div class="table_pagination">
													<report:pageCounter formName="systematicWithdrawReportForm" report="theReport" arrowColor="black" />
												</div>
												<div class="table_controls_footer"></div>
											</TD>

										</TR>
									</TBODY>
								</TABLE>
							</TD>
						</TR>
					</TBODY>
				</TABLE>
</c:if>
		</c:if>

		<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
		
	<thead>
	  
			
				
				<TR class="tablesubhead">
			    <td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
                <TD valign="bottom" >
				<B><psreport:sort formName="systematicWithdrawReportForm" field="participantName">Name</psreport:sort></B>
				</TD>
				<td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      			
                <TD valign="bottom" >
				<B><psreport:sort formName="systematicWithdrawReportForm" field="wdStatus" >Withdrawal status</psreport:sort></B>
							
				</TD>
				 <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>  
               
                <TD valign="bottom"><B>
					<psreport:sort formName="systematicWithdrawReportForm" field="wdType">Withdrawal type</psreport:sort>
				</B></TD>
				 <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
               
                <TD valign="bottom">
				<report:sort formName="systematicWithdrawReportForm" field="setDate" direction="asc"><B>Setup date</B></report:sort>
				</TD>
				 <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                
                <TD valign="bottom">
				<B><report:sort formName="systematicWithdrawReportForm" field="calcMethod" direction="asc">Calculation method</report:sort></B>
				</TD>
				 <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                
                <TD valign="bottom">
				<B><report:sort formName="systematicWithdrawReportForm" field="freq" direction="asc">Frequency</report:sort></B>
				</TD>
				 <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
               
                <TD valign="bottom">
				<B><report:sort formName="systematicWithdrawReportForm" field="lastPayDate" direction="asc">Last payment date</report:sort></B>
				</TD>
				 <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
               
                <TD align="right" valign="bottom">
				<B><report:sort formName="systematicWithdrawReportForm" field="lastPayAmt" direction="asc">Last payment amount($)&nbsp;</report:sort></B>
				</TD>
				 <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                
                <TD align="right" valign="bottom">
				<B><report:sort formName="systematicWithdrawReportForm" field="totalAsset" direction="asc">Total current assets($)</report:sort></B>
				</TD>
               <td class="databorder" width="1"><img
					src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
															
              </TR>
              	
           
              </thead>
         
<c:if test="${empty theReport.details}">

					<tr class="datacell1">
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1">
						</td>
						<td colspan="17"><content:getAttribute id="noResultForSearch"
								attribute="text" /></td>
						<td width="1" class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="0" height="1">
						</td>
					</tr>
					
</c:if>
			<tbody>
				<c:if test="${theReport != null}">
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="ind" >

 <c:set var="ind" value="${ind.index}"/> 
 <% 
  String ind = pageContext.getAttribute("ind").toString();
 SystematicWithdrawDataItem theItem =(SystematicWithdrawDataItem)pageContext.getAttribute("theItem");

 %>

							<%
						String rowClass = "spec";
									if (Integer.parseInt(ind) % 2 == 1) {
										rowClass = "datacell1";
									} else {
										rowClass = "datacell2";
									}
						
				%>

							<tr class="<%=rowClass%>">
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td><ps:link
										action='<%="/do/participant/participantAccount/?profileId="
                                            + theItem.getParticipant().getProfileId()%>'>




${theItem.participant.lastName}, ${theItem.participant.firstName}

</ps:link> <br /> <c:if test="${not empty theItem.participant.ssn}">

										<render:ssn property="theItem.participant.ssn" />
</c:if> <br /></td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td>
									<!-- <div style="color: #000;"> -->
${theItem.wdStatus}

									<!-- 	<br /> <br />
									</div> -->
								</td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td>${theItem.wdType}</td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td><render:date property="theItem.wdSetupDate"
										defaultValue="-"
										patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td>${theItem.calculationMethod}
								</td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td>${theItem.wdfrequency}</td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td><render:date property="theItem.lastPayDate"
										defaultValue="-"
										patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" /></td>
								<td class="datadivider"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td align="right"><render:number
										property="theItem.lastPaymentAmount" defaultValue="0.0"
										type="c" sign="false" /></td>

								<td class="datadivider"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td align="right"><render:number
										property="theItem.totalAssets" defaultValue="0.0" type="c"
										sign="false" /></td>
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>

</c:forEach>
            
</c:if>
					<tr>
				<td colspan=19" width=" 1" class="databorder"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
					 
				</c:if>

			</tbody>
			
		</table>
		<c:if test="${not empty theReport}">
<c:if test="${not empty theReport.details}">
				<TABLE width="730" border="0" cellspacing="0" cellpadding="0">
					<TBODY>
						<TR>
							<TD colspan="19">
								<TABLE width="100%">
									<TBODY>
										<TR>
											<TD width="10%">&nbsp;</TD>
											<TD width="80%" align="center"><div
													class="table_display_info">
													<strong><report:recordCounter report="theReport"
															label="Systematic Withdrawals" /></strong>
												</div></TD>
											<TD width="10%" align="right">
												<div class="table_pagination">
													<report:pageCounter formName="systematicWithdrawReportForm" report="theReport" arrowColor="black" />
												</div>
												<div class="table_controls_footer"></div>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
							</TD>
						</TR>
					</TBODY>
				</TABLE>
</c:if>
<p><content:pageFooter beanName="layoutPageBean"/></p>
<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
		</c:if>
	</div>
</ps:form>
<%-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td colspan="19">
			<br>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
 			<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 		  	</td>
  		</tr>
	</table> --%>
<c:if test="${printFriendly != null }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>

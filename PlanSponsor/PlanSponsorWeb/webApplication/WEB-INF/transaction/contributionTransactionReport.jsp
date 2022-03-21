<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionItem" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.ParticipantVO" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData.MoneyTypeAmount" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

		 <p>
      		<content:errors scope="request" /><content:errors scope="session" />
    	</p>
<%
ContributionTransactionReportData theReport = (ContributionTransactionReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>
<c:if test="${not empty theReport}">
<%
	boolean showEE = theReport.isHasEmployeeContribution();
	boolean showER = theReport.isHasEmployerContribution();
	%>
	<c:if test="${empty param.printFriendly}">
	<style>
	<td width="30">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	#detailsTable{width : 715}
	#totals {width : 150}
	
	<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

		<logicext:then>
				<!-- EE && ER -->
				#itemName {width : 150}
		</logicext:then>

		<logicext:else>
				
				#itemName {width : 250}
		</logicext:else>
	</logicext:if>

	#itemSSN {width : 75}

   	<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="notEqual" value="true"/>

		<logicext:then>
			<!-- EE && !ER -->
			#itemEE {width : 239}
		</logicext:then>

		<logicext:else>
			#itemEE {width : 110}
		</logicext:else>
	</logicext:if>

   	<logicext:if name="theReport" property="hasEmployeeContribution" op="notEqual" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

		<logicext:then>
			<!-- !EE && ER -->
			#itemER {width : 239}
		</logicext:then>

		<logicext:else>
			#itemER {width : 110}
		</logicext:else>
	</logicext:if>

	#itemTotal {width : 93}

	#imgTotals {width : 150}

	<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

		<logicext:then>
				<!-- EE && ER -->
				#imgName {width : 150}
		</logicext:then>

		<logicext:else>
				#imgName {width : 250}
		</logicext:else>
	</logicext:if>

	#imgSSN {width : 75}

   	<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="notEqual" value="true"/>

		<logicext:then>
			<!-- EE && !ER -->
			#imgEE {width : 239}
		</logicext:then>

		<logicext:else>
			#imgEE {width : 110}
		</logicext:else>
	</logicext:if>

   	<logicext:if name="theReport" property="hasEmployeeContribution" op="notEqual" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

		<logicext:then>
			<!-- !EE && ER -->
			#imgER {width : 239}
		</logicext:then>

		<logicext:else>
			#imgER {width : 110}
		</logicext:else>
	</logicext:if>

	#imgTotal {width : 93}
	</style>
	</c:if>

	<c:if test="${not empty param.printFriendly}" >
	<style>
	#detailsTable{width : 600}
	#totals {width : 150}

	<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

		<logicext:then>
				<!-- EE && ER -->
				#itemName {width : 125}
		</logicext:then>

		<logicext:else>
				<!-- !( EE && ER) -->
				#itemName {width : 215}
		</logicext:else>
	</logicext:if>

	#itemSSN {width : 90}

   	<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="notEqual" value="true"/>

		<logicext:then>
			<!-- EE && !ER -->
			#itemEE {width : 124}
		</logicext:then>

		<logicext:else>
			#itemEE {width : 80}
		</logicext:else>
	</logicext:if>

   	<logicext:if name="theReport" property="hasEmployeeContribution" op="notEqual" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

		<logicext:then>
			<!-- !EE && ER -->
			#itemER {width : 124}
		</logicext:then>

		<logicext:else>
			#itemER {width : 80}
		</logicext:else>
	</logicext:if>

	#itemTotal {width : 54}

	#imgTotals {width : 150}

	<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

		<logicext:then>
				<!-- EE && ER -->
				#imgName {width : 125}
		</logicext:then>

		<logicext:else>
				<!-- !( EE && ER) -->
				#imgName {width : 215}
		</logicext:else>
	</logicext:if>

	#imgSSN {width : 100}

   	<logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="notEqual" value="true"/>

		<logicext:then>
			<!-- EE && !ER -->
			#imgEE {width : 124}
		</logicext:then>

		<logicext:else>
			#imgEE {width : 80}
		</logicext:else>
	</logicext:if>

   	<logicext:if name="theReport" property="hasEmployeeContribution" op="notEqual" value="true">
		<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

		<logicext:then>
			<!-- !EE && ER -->
			#imgER {width : 124}
		</logicext:then>

		<logicext:else>
			#imgER {width : 80}
		</logicext:else>
	</logicext:if>

	#imgTotal {width : 54}
	</style>
	</c:if>


	<ps:form cssStyle="margin-bottom:0;"  modelAttribute="contributionTransactionReportForm" name="contributionTransactionReportForm" method="POST" action="/do/transaction/contributionTransactionReport/">

      <table id="detailsTable" style="width: 715px;" cellspacing="0" cellpadding="0" border="0">
		<!-- top row -->
		<tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="totals"><img id="imgTotals" src="/assets/unmanaged/images/s.gif" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="itemName"><img id="imgName" src="/assets/unmanaged/images/s.gif"height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td id="itemSSN"><img id="imgSSN" src="/assets/unmanaged/images/s.gif" height="1"></td>
		  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

<c:if test="${theReport.hasEmployeeContribution ==true}">
			  <td id="itemEE"><img id="imgEE" src="/assets/unmanaged/images/s.gif" height="1"></td>
<c:if test="${theReport.hasEmployerContribution ==true}">
				  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployerContribution ==true}">
			  <td id="itemER"><img id="imgER" src="/assets/unmanaged/images/s.gif" height="1"></td>
<c:if test="${theReport.hasEmployeeContribution ==true}">
				  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${theReport.hasEmployerContribution ==true}">
					<td id="itemTotal"><img id="imgTotal" src="/assets/unmanaged/images/s.gif" height="1"></td>
</c:if>
</c:if>

		  <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>


		<!-- tablehead -->
		<!-- tablehead -->
        <tr class="tablehead">

          <td class="tableheadTD1" colspan="<%=(showEE && showER)?14:10%>">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="tableheadTD"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b> </td>
                <td class="tableheadTDinfo">
						<report:recordCounter report="theReport" label="Participants"/>
				</td>
                <td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="contributionTransactionReportForm"/></td>
              </tr>
            </table>
          </td>

		</tr>


		<tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		  <!-- totals on left -->
             <td rowspan="4" class="whiteBox" valign="top">

        <table border="0" cellspacing="0" cellpadding="0">
          <tr>
           <td>&nbsp;</td>
           <td>
            <b class="highlight">Transaction type</b><br>
            <b>Contribution</b><br>
            <b>Payroll ending <render:date patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" property="theReport.payrollEndingDate"/>
			</b>
			<br>
            <br>
			<b  class="highlight">Number of participants </b><b>
			<c:if test="${not empty theReport.numberOfParticipants}">
					<render:number property="theReport.numberOfParticipants" type="i" />
</c:if>
			    <%-- <logic:notEmpty name="theReport" property="numberOfParticipants">
					<render:number property="theReport.numberOfParticipants" type="i" />
				</logic:notEmpty> --%>
				</b>
			<br>
            <br>
            <b class="highlight">Invested date</b>
            <b><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theReport.transactionDate"/> </b>
            <br>
            <b class="highlight">Transaction number </b><b>${e:forHtmlContent(theReport.transactionNumber)}</b>
            </td>
           </tr>
         </table>
          </td>
		  <!-- end of totals on left -->


		  <!-- headers -->
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" colspan="3">&nbsp;</td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

<c:if test="${theReport.hasEmployeeContribution ==true}">
			  <td valign="top" align="right" colspan="<%=(showER)?1:2%>"><b>Employee contributions ($)</b></td>
<c:if test="${theReport.hasEmployerContribution ==true}">
				  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployerContribution ==true}">
			  <td valign="top" align="right" colspan="<%=(showEE)?1:2%>"><b>Employer contributions ($)</b></td>
<c:if test="${theReport.hasEmployeeContribution ==true}">
				  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${theReport.hasEmployerContribution ==true}">
				  <td valign="top" align="right" colspan="2"><b>Total contributions ($)</b></td>
</c:if>
</c:if>

		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		  <!-- end of headers -->
        </tr>

		<!-- regular -->
        <tr class="datacell2">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		  <td valign="top" colspan="3"><b>Contributions</b></td>
          <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

<c:if test="${theReport.hasEmployeeContribution ==true}">
			  <td valign="top" align="right" colspan="<%=(showER)?1:2%>">

<c:if test="${not empty theReport.totalEmployeeContribution}">
					<render:number property="theReport.totalEmployeeContribution" type="c" sign="false"/>
</c:if>

			  </td>
<c:if test="${theReport.hasEmployerContribution ==true}">
				  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployerContribution ==true}">
			  <td valign="top" align="right" colspan="<%=(showEE)?1:2%>">

<c:if test="${not empty theReport.totalEmployerContribution}">
					<render:number property="theReport.totalEmployerContribution" type="c" sign="false"/>
</c:if>

			  </td>
<c:if test="${theReport.hasEmployeeContribution ==true}">
				  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${theReport.hasEmployerContribution ==true}">
				  <td valign="top" align="right" colspan="2">

<c:if test="${not empty theReport.totalContribution}">
						<render:number property="theReport.totalContribution" type="c" sign="false"/>
</c:if>

				  </td>
</c:if>
</c:if>

		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

        </tr>

        <tr class="datacell1">
          <td rowspan="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		  <logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
			<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

			<logicext:then>
					<!-- EE && ER -->
					<td valign="top" colspan="10" class="tableheadTD">
			</logicext:then>

			<logicext:else>
					<!-- EE || ER -->
					<td valign="top" colspan="6" class="tableheadTD">
			</logicext:else>
 		  </logicext:if>

          <strong class="tableheadTD">Total
          contributions by money type </strong> 
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

		<!-- money types -->
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		  <logicext:if name="theReport" property="hasEmployeeContribution" op="equal" value="true">
			<logicext:and name="theReport" property="hasEmployerContribution" op="equal" value="true"/>

			<logicext:then>
					<!-- EE && ER -->
					<td valign="top" colspan="10">
			</logicext:then>

			<logicext:else>
					<!-- EE || ER -->
					<td valign="top" colspan="6">
			</logicext:else>
 		  </logicext:if>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr class="datacell1">
				<td><b>Money type</b></td>
				<td align="right"><b>Amount ($)</b></td>
				<td><b>Money type</b></td>
				<td align="right"><b>Amount ($)</b></td>
			</tr>

<c:if test="${not empty theReport.moneyTypes}">

				<%
				
					Object[] types = theReport.getMoneyTypes().toArray();
				
					//this array is always padded up to even number of elements
					for (int i = 0; i < types.length; i++) {
						
						pageContext.setAttribute("theItem", types[i]);
						boolean isLeft = i % 2 == 0;
						
						if (isLeft) {
							
							// if left side column, then open the table row
							
				%>
				
				<tr class="datacell1">
				
				<%
				
						}
				
				%>
				
<td>${theItem.longDescription}</td>
					<td align="right">

<c:if test="${not empty theItem.amount}">
							<render:number property="theItem.amount" type="c" sign="false" defaultValue=""/>
</c:if>
						
					</td>
				
				<%
				
						if (! isLeft) {
							
							// if right side column, then close the table row
							
				%>
				
				</tr>
				
				<%
				
						} else if (i == types.length - 1) {
							
							// if last money type and on left side, then placeholders on right side
							
				%>
					
					<td></td><td align="right"></td>
					
				</tr>
				
				<%
						
						}
					
					}
					
				%>
				
</c:if>

			</table>

		  </td>
		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<!-- end of money types -->

		<!-- span colspan="10" -->

        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		  <td colspan="<%=(showEE && showER)?10:6%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

		<tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"  class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		  <td class="greyborder" colspan="<%=(showEE && showER)?10:6%>" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<!-- end of span -->


		<!-- headers / column names  -->
		<tr class="tablesubhead">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td valign="top" class="whiteBox"><b class="highlight">Contribution details</b></td>
          <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <!-- %=LoanSummaryItem.SORT_NAME%  - TODO all sort field names !!!! -->
          <td valign="top"><report:sort field="<%=ContributionTransactionReportData.SORT_FIELD_NAME%>" direction="asc" formName="contributionTransactionReportForm"><b>Name</b></report:sort></td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><report:sort field="<%=ContributionTransactionReportData.SORT_FIELD_SSN%>" direction="asc" formName="contributionTransactionReportForm"><b>SSN</b></report:sort></td>
          <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

<c:if test="${theReport.hasEmployeeContribution ==true}">
			  <td valign="top" align="right" colspan="<%=(showER)?1:2%>"><b><report:sort field="<%=ContributionTransactionReportData.SORT_FIELD_EMPLOYEE_CONTRIBUTION%>" direction="desc" formName="contributionTransactionReportForm"><b>Employee contributions</b></report:sort>
				($) </b></td>
<c:if test="${theReport.hasEmployerContribution ==true}">
				  <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployerContribution ==true}">
			  <td valign="top" align="right" colspan="<%=(showEE)?1:2%>"><b><report:sort field="<%=ContributionTransactionReportData.SORT_FIELD_EMPLOYER_CONTRIBUTION%>" direction="desc" formName="contributionTransactionReportForm"><b>Employer contributions</b></report:sort>
				($) </b></td>
<c:if test="${theReport.hasEmployeeContribution ==true}">
				  <td class="dataheaddivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${theReport.hasEmployerContribution ==true}">
					<td valign="top" align="right" colspan="2"><b><report:sort field="<%=ContributionTransactionReportData.SORT_FIELD_TOTAL_CONTRIBUTION%>" direction="desc" formName="contributionTransactionReportForm"><b>Total contributions</b></report:sort>
					($)</b></td>
			   <!-- ($) </b></td> -->
</c:if>
</c:if>

		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

        </tr>

		<!-- report details -->
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/>
<%String temp = pageContext.getAttribute("indexValue").toString();%>
					<%-- <c:choose>
						<c:when test="${theIndex.index % 2 == 0}">
							<tr class="datacell1">
						</c:when>
						<c:otherwise>
							<tr class="datacell2">
						</c:otherwise>
					</c:choose --%>
								<% if (Integer.parseInt(temp) % 2 == 0) { %> 
 			  <tr class="datacell1">
 			<% } else { %> 
			  <tr class="datacell2"> 
			<% } %> 

			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			  <td valign="top" class="whiteBox"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			  <td class="greyborder" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

			  <td valign="top">
				    <ps:link action="/do/participant/participantAccount/" paramId="participantId" paramName="theItem" paramProperty="participant.id">
${theItem.participant.wholeName}
					</ps:link>
			  </td>


			  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
			  <td valign="top"><render:ssn property="theItem.participant.ssn"/></td>
			  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

<c:if test="${theReport.hasEmployeeContribution ==true}">
				  <td valign="top" align="right" colspan="<%=(showER)?1:2%>">
<c:if test="${not empty theItem.employeeContribution}">
						  <render:number property="theItem.employeeContribution" type="c" sign="false"/>
</c:if>
				  </td>
<c:if test="${theReport.hasEmployerContribution ==true}">
					  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployerContribution ==true}">
				  <td valign="top" align="right" colspan="<%=(showEE)?1:2%>">
<c:if test="${not empty theItem.employerContribution}">
						  <render:number property="theItem.employerContribution" type="c" sign="false"/>
</c:if>
				  </td>
<c:if test="${theReport.hasEmployeeContribution ==true}">
					  <td class="datadivider" valign="top"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${theReport.hasEmployerContribution ==true}">
					  <td valign="top" align="right" colspan="2">
<c:if test="${not empty theItem.totalContribution}">
							  <render:number property="theItem.totalContribution" type="c" sign="false"/>
</c:if>
					  </td>
</c:if>
</c:if>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

			</tr>

</c:forEach>
</c:if>
		<!-- end of report details -->

		<%
		String rowColor = (theReport.getDetails().size() % 2 == 1) ? "white" : "beige";
		%>

		<!-- last row -->
	    <tr class="<%= rowColor %>border">

		  <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

<c:if test="${theReport.hasEmployeeContribution ==true}">
			  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${theReport.hasEmployerContribution ==true}">
				<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployerContribution ==true}">
			  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:if test="${theReport.hasEmployeeContribution ==true}">
				<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

<c:if test="${theReport.hasEmployeeContribution ==true}">
<c:if test="${theReport.hasEmployerContribution ==true}">
				  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
</c:if>

		  <td rowspan="2"  colspan="2" width="5" class="lastrow" style="align:right"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
        </tr>

		<tr>
          <td class="databorder" colspan="<%=(showEE&&showER)?12:8%>"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>

		<tr>
          <td align="right" colspan="<%=(showEE&&showER)?14:10%>"><report:pageCounter report="theReport" arrowColor="black" formName="contributionTransactionReportForm"/></td>
        </tr>

 		<tr>
			<td colspan="14">
				<br>
				<p><content:pageFooter beanName="layoutPageBean"/></p>
 				<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 				<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 			</td>
		</tr>


      </table>

	  </ps:form>

      </c:if>
<c:if test="${not empty param.printFriendly}" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>


<%-- taglib used --%>
<%@page import="org.apache.taglibs.standard.tag.common.core.ChooseTag"%>
<%@page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.util.content.GenericException" %>
<%@ page import="com.manulife.pension.bd.web.BDErrorCodes" %>
<%@ page import="com.manulife.pension.bd.web.util.BDSessionHelper" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFTFReportData" %>
<un:useConstants var="bdConstants"
	className="com.manulife.pension.bd.web.BDConstants" />
<%-- Beans used --%>
<%-- <jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" /> --%> 
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<input type="hidden" name="pdfCapped" /><%--  input - name="fundToFundTransactionReportForm" --%>

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
%>
 <%
TransactionDetailsFTFReportData theReport = (TransactionDetailsFTFReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 


<c:if test="${not empty theReport}">

 

    <bd:form  method="post"
        action="/do/bob/transaction/fundToFundTransactionReport/" modelAttribute="fundToFundTransactionReportForm" name="fundToFundTransactionReportForm">
    
        <div id="summaryBox">
        <h1><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h1>
         <%
     if (bobContext.getCurrentContract().isDefinedBenefitContract()==false) {
%> <span class="name">Name: <strong>${theReport.participantName}</strong></span> <%

         } %> 
        <%
        if (bobContext.getCurrentContract().isDefinedBenefitContract()==false) {
            %><br />
            <span class="name">
SSN: <strong> ${theReport.participantSSN} </strong> </span><%

            } %> 
             <span
            class="name"><br />
           <br />
           Invested Date: <strong><render:date
            dateStyle="m" property="theReport.transactionDate" /></strong>
            <br /></span>
            <span class="name">
            Request Date: <strong><render:date
            dateStyle="m" property="theReport.requestDate" /></strong></span> <span
            class="name"><br />
           Total Amount:</span><span class="name"><strong><report:number
            property="theReport.totalAmount" type="c" sign="true" /></strong></span> <span
            class="name"><br />
          <br />
Transaction Number:</span><span class="name"><strong>${e:forHtmlContent(theReport.transactionNumber)}<br />

</strong></span> <span class="name"> Submission Method: </span><span class="name"><strong>${theReport.mediaCode}</strong></span> <span class="name"><br />

Source of Transfer:</span><span class="name"><strong>${theReport.sourceOfTransfer}</strong></span></div>

    </bd:form>
</c:if>

    <jsp:include page="/WEB-INF/global/displayContractInfo.jsp"/>
    
    <report:formatMessages scope="request"/>

    <navigation:contractReportsTab />
    
<c:if test="${not empty theReport}">
 


    <bd:form method="post"
        action="/do/bob/transaction/fundToFundTransactionReport/" modelAttribute="fundToFundTransactionReportForm" name="fundToFundTransactionReportForm">
    
        <div class="page_section_subheader controls">
    
        <h3><content:getAttribute id="layoutPageBean" attribute="body1Header"/></h3>
        <c:if test="${empty requestScope.isError}">
            <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
            <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
        </c:if>
        </div>
        <!--.message_info-->
        <div class="report_table"><!--.message_info-->
        <div class="clear_footer"></div>
    
        <table class="report_table_content">
            <thead>
                <tr>
                    <th width="35%" class="val_str"><strong>Investment Option</strong></th>
                    <th width="35%" class="val_str">
                    <% if (theReport.doFromMoneyTypesExist()) {
                    %> <strong>Money Type</strong> 
                    <% } %>
                    </th>
                    <%
                    if (theReport.showFromPercent()) {
                    %>
                    <th width="10%" class="val_str align_center"><strong>Amount($)</strong></th>
                    <th width="10%" class="val_str align_center"><strong>% Out</strong></th>
                    <%
                    } else {
                    %>
                    <th width="10%" class="val_str align_center" colspan="2"><strong>Amount($)</strong></th>
                    <th width="10%" class="val_str"><strong>&nbsp;</strong></th>
                    <%
                    }
                    %>
    
                </tr>
            </thead>
            
            <tbody>
<c:forEach items="${theReport.transferFroms}" var="category" >


                    
                    <tr class="spec">
<td class="name"><B>${category.groupName}</B></td>

                        <td class="name">&nbsp;</td>
                        <% if(theReport.showFromPercent()) {%>
                            <td class="name">&nbsp;</td>
                            <td class="name">&nbsp;</td>
                        <%} else { %>
                            <td class="name" colspan="2">&nbsp;</td>
                            <td class="name">&nbsp;</td>
                        <%} %>
                        
                    </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >


                        
                        <tr>
<td width="35%" class="name">${fund.name}</td>

<td width="35%" class="name">${fund.moneyTypeDescription}</td>

                            <%
                            TransactionDetailsFund fund= (TransactionDetailsFund)pageContext.getAttribute("fund");
                                if (fund.getAmount() != null
                                    && fund.getPercentage() != null) {
                            %>
                            <%
                            if (theReport.showFromPercent()) {
                            %>
                            <td width="10%" class="val_str">
                            <div align="right"><report:number property="fund.amount"
                                type="c" sign="false" /></div>
                            </td>
                            <td width="10%" class="cur">
                            <div align="right"><report:number property="fund.percentage" /></div>
                            </td>
                            <%
                            } else {
                            %>
                            <td width="10%" class="val_str" colspan="2">
                            <div align="right"><report:number property="fund.amount"
                                type="c" sign="false" /></div>
                            </td>
                            <td width="10%" class="cur">
                            <div align="right">&nbsp;</div>
                            </td>
                            <%
                            }
                            %>
                            <%
                            }
                            %>
                        
</c:forEach>
</c:forEach>
                </tr>
    
                <!-- before totals -->
                
                <tr class="spec">
                    <%
                            if (theReport.getTotalFromAmount() != null
                            && theReport.getTotalFromPct() != null && theReport.showFromPercent()) {
                    %>
    
                    <td width="35%" class="name">
                    <div align="right"><strong>Total:</strong></div>
                    </td>
                    <td width="35%" class="name">&nbsp;</td>
                    <td width="20%" class="val_str">
                    <div align="right"><report:number
                        property="theReport.totalFromAmount" type="c" sign="false" /></div>
                    </td>
                    <td width="10%" class="cur">&nbsp;</td>
    
                    <%
                    } else if (theReport.getTotalFromAmount() != null
                            && !theReport.showFromPercent()) {
                    %>
                        <td width="35%" class="name">
                        <div align="right"><strong>Total:</strong></div>
                        </td>
                        <td width="35%" class="name">&nbsp;</td>
                        <td width="10%" class="val_str" colspan="2">
                        <div align="right"><report:number
                            property="theReport.totalFromAmount" type="c" sign="false" /></div>
                        </td>
                    <td width="10%" class="cur">&nbsp;</td>
                    <%} %>
    
                </tr>
            </tbody>
        </table>
        </div>
        <div class="page_section_subheader controls">
    
        <h3><content:getAttribute id="layoutPageBean" attribute="body2Header"/></h3>
        </div>
        <div class="report_table"><!--.message_info-->
        <div class="clear_footer"></div>
        <table class="report_table_content">
            <thead>
                <tr>
                    <th class="val_str"><strong>Investment
                    Option</strong></th>
                    <th class="val_str">&nbsp;</th>
                    <th class="val_str align_center"><strong>Amount($) </strong></th>
                    <th class="val_str align_center"><strong>% In </strong></th>
                </tr>
            </thead>
            <tbody>
<c:forEach items="${theReport.transferTos}" var="category" >


                    
                    <tr class="spec">
<td class="name"><B>${category.groupName}</B></td>

                        <td class="name">&nbsp;</td>
                        <td class="name">&nbsp;</td>
                        <td class="name">&nbsp;</td>
                    </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >


                        <tr>
<td width="35%" class="name">${fund.name}</td>

                            <td width="35%" class="name">&nbsp;</td>
                            <% TransactionDetailsFund fund= (TransactionDetailsFund)pageContext.getAttribute("fund");
                                            if (fund.getAmount() != null
                                            && fund.getPercentage() != null) {
                            %>
                            <td width="10%" class="val_str">
                            <div align="right"><report:number property="fund.amount"
                                type="c" sign="false" /></div>
                            </td>
    
                            <td width="10%" class="cur">
                            <div align="right"><report:number property="fund.percentage" /></div>
                            </td>
    
                            <%
                            }
                            %>
                        </tr>
</c:forEach>
</c:forEach>
                <!-- before totals -->
                    <tr class="spec">
                    <td width="35%" class="name">
                    <div align="right"><strong>Total:</strong></div>
                    </td>
                    <td width="35%" class="name">&nbsp;</td>
                    <%
                            if (theReport.getTotalToAmount() != null
                            && theReport.getTotalToPct() != null) {
                    %>
                    <td width="10%" class="val_str">
                    <div align="right"><report:number
                        property="theReport.totalToAmount" type="c" sign="false" /></div>
                    </td>
    
                    <td width="10%" class="cur">
                    <div align="right"><report:number
                        property="theReport.totalToPct" /></div>
                    </td>
                    <%
                    }
                    %>
                </tr>
    
            </tbody>
        </table>
        </div>
    
        <%
            Boolean showMessage = false;
            if ((theReport.getRedemptionFees().doubleValue() != 0) || 
            (theReport.getMva().doubleValue() > (double)0.0)) {
                showMessage = true;
            }
    
            if (showMessage) {
        %>
            <div class="message message_info">
                    <dl>
                        <dt>Information Message</dt>
        <%  } %>
        <%
            int messageCount = 0;
            
            if (theReport.getRedemptionFees().doubleValue() > (double)0.0) {%>
                <dd><%=++messageCount%> .&nbsp;&nbsp;
                <content:contentBean contentId="<%=BDContentConstants.MESSAGE_REDEMPTION_FEE_APPLED%>"
                                           type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="redemptionMsg"/>
                <content:getAttribute beanName="redemptionMsg" attribute="text">
                    <content:param>
                        -<report:number property="theReport.redemptionFees" type="c" sign="true"/>
                    </content:param>
                </content:getAttribute>
                </dd>
        <%  } %>
    
        <%  if (theReport.getRedemptionFees().doubleValue() != 0) {%>
                <dd><%=++messageCount%> .&nbsp;&nbsp;
                <a href="#" onClick="javascript:PDFWindow('<%=BDConstants.REDEMPTION_FEE_PDF_URL%>')">View underlying mutual fund redemption fee information</a></dd>
        <%  } %>
                
    
        <%  if (theReport.getMva().doubleValue() > (double)0.0) { %>
                <dd><%=++messageCount%> .&nbsp;&nbsp;
                <content:contentBean contentId="<%=BDContentConstants.MESSAGE_MVA_APPLIED%>"
                                              type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="mvaMsg"/>
                <content:getAttribute beanName="mvaMsg" attribute="text">
                    <content:param>
                        -<report:number property="theReport.mva" type="c" sign="true"/>
                    </content:param>
                </content:getAttribute>
                </dd>
        <%  } %>    
        
        <%  if (showMessage) { %>
                    </dl>
                </div> <!--.message_info-->
        <%  } %>
    
    
        <div class="page_section_subheader controls">
    
        <h3><content:getAttribute id="layoutPageBean" attribute="body3Header"/></h3>
        </div>
    
    
        <div class="report_table"><!--.message_info-->
        <div class="clear_footer"></div>
    
    
        <table class="report_table_content">
            <thead>
                <tr>
                    <th width="30%" class="val_str"><strong>Investment Option</strong></th>
                    <th width="30%" class="val_str"><strong>Money Type </strong></th>
                    <th width="13%" class="val_str align_center"><strong>Amount($) </strong></th>
                    <th width="13%" class="val_str align_center"><strong>Unit Value </strong></th>
                    <th width="14%" class="val_str align_center"><strong>Number Of Units</strong></th>
                    <%  if (showMessage) { %>
                    <th width="10%" class="val_str"><strong>Comments</strong></th>
                    <%  } %>
                </tr>
            </thead>
            <tbody>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="category" >


                        <tr class="spec">
<td class="name"><B>${category.groupName}</B></td>

                            <td class="name">&nbsp;</td>
                            <td class="name">&nbsp;</td>
                            <td class="name">&nbsp;</td>
                            <td class="name">&nbsp;</td>
                            <%  if (showMessage) { %>
                                <td class="name">&nbsp;</td>
                            <%  } %>
                        </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >
<c:choose><c:when test="${fundIndex.index % 2 == 0}"><tr></c:when>
<c:otherwise><tr class="spec"></c:otherwise>
</c:choose>

                                <% TransactionDetailsFund fund= (TransactionDetailsFund)pageContext.getAttribute("fund"); %>
                               
<%--                                 if (fundIndex.index % 2 == 0) {
                                    <tr>
                                <% } else { %>
                                    <tr class="spec">
                                <% } %> --%>
<td class="name">${fund.name}</td>

                                <td class="name">
                                <c:if test="${fund.moneyTypeDescription ==' '}"> 
                                  -
</c:if>
                                <c:if test="${fund.moneyTypeDescription !=''}">
${fund.moneyTypeDescription}
</c:if>
                                </td>
                                <td class="cur"><report:number
                                    property="fund.amount" type="c" sign="false" /></td>
                                <td class="cur">
                                <%
                                if (fund.displayUnitValue()) {
                                %> <report:number
                                    defaultValue="-" property="fund.displayUnitValue" scale="2" sign="true" />
                                <%
                                                // If has no units, means we have interest rate here
                                                if (!fund.displayNumberOfUnits()) {
                                %>&#37; <%
                                                }
                                }else {
                                %> - <%} %>
                                </td>
                                <td class="cur">
                                <%
                                if (fund.displayNumberOfUnits()) {
                                %> <report:number
                                    defaultValue="-" property="fund.displayNumberOfUnits" scale="6" sign="false" />
                                <%
                                    }else {
                                %> - <%} %>
                                </td>
                                <%  if (showMessage) { %>
<td class="name">${fund.comments}
                                </td>
                                <%  } %>
                            </tr>
</c:forEach>
</c:forEach>
</c:if>
    
            </tbody>
        </table>
    
        </div>
        <!--.report_table-->
    </bd:form>
</c:if>

    <layout:pageFooter/>


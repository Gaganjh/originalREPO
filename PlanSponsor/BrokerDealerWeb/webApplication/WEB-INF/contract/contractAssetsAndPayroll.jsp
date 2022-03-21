<%--  Imports  --%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="java.util.GregorianCalendar"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%--  Tag Libraries  --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>






<%
ContractInformationReportData theReport = (ContractInformationReportData)request.getAttribute(BDConstants.REPORT_BEAN);
  pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
  BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
  pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);
  
  %>





<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_CONTRACT_ASSETS_SECTION_TITLE%>"
                            type="<%=BDContentConstants.TYPE_MESSAGE%>"
                            id="assetsTitle"/>

<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_ALLOCATION_DETAILS_SECTION_TITLE%>"
                            type="<%=BDContentConstants.TYPE_MESSAGE%>"
                            id="allocationTitle"/>

<content:contentBean contentId="<%=BDContentConstants.CONTRACT_INFO_PAGE_PAYROLL_ALLOCATION_DETAILS_SECTION_TITLE%>"
                            type="<%=BDContentConstants.TYPE_MESSAGE%>"
                            id="payrollAllocationTitle"/>

<h4><content:getAttribute beanName="assetsTitle" attribute="text"/></h4>
<table class="overview_table">
    <tbody>
        <tr>
            <th>Total Contract Assets:</th>
            <td>
               <report:number property="theReport.contractSnapshotVo.planAssets.totalPlanAssetsAmount"
                              type="c" />
            </td>
        </tr>
        <tr>
            <th class="subheading">Assets in Cash Account:</th>
            <td>
               <report:number property="theReport.contractSnapshotVo.planAssets.cashAccountAmount"
                              type="c" />
            </td>
        </tr>
<c:if test="${theReport.contractSnapshotVo.planAssets.uninvestedAssetsAmount !=0}">
            <tr>
                <th class="subheading">Pending Transaction:</th>
                <td>
                    <report:number property="theReport.contractSnapshotVo.planAssets.uninvestedAssetsAmount"
                                   type="c" />
                </td>
            </tr>
</c:if>
        <tr>
            <th class="subheading">Assets Allocated:</th>
            <td>
                <report:number property="theReport.contractSnapshotVo.planAssets.allocatedAssetsAmount"
                               type="c" />
            </td>
        </tr>
        <logicext:if name="bobContext" property="currentContract.loanFeature" op="equal" value="true">
            <logicext:and name="theReport" property="hasLoanAssets" op="equal" value="true" />
            <logicext:then>
                <tr>
                    <th class="subheading">Loan Assets:</th>
                    <td><report:number property="theReport.contractSnapshotVo.planAssets.loanAssets"
                                       type="c" defaultValue="" />
                    </td>
                </tr>
            </logicext:then>
        </logicext:if>
<c:if test="${bobContext.currentContract.PBA ==true}">
            <tr>
                <th class="subheading">PBA Assets:</th>
                <td>
                   <report:number property="theReport.contractSnapshotVo.planAssets.personalBrokerageAccountAmount"
                                  type="c" />
                </td>
            </tr>
</c:if>
<c:if test="${bobContext.currentContract.definedBenefitContract !=true}">
            <tr>
                <th>Participants:</th>
                <td>
${theReport.participantCount}
                    </td>
            </tr>
</c:if>
        <tr>
            <th>As of:</th>
            <td>
            	<render:date property="bobContext.currentContract.contractDates.asOfDate"
                         patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
                         defaultValue="" />
            </td>
        </tr>
        <tr>
        
        <%-- This has been commented out to suppress the display of Blended Asset Charge - CL 131387
            <%
            boolean isInvalidDate = false;
            Date dateWithYear9999 = new GregorianCalendar(9999, Calendar.DECEMBER, 31).getTime();
            Date dateWithYear0001 = new GregorianCalendar(1, Calendar.JANUARY, 1).getTime();

            if (theReport.getAssetChargeAsOfDate() != null) {
                if (dateWithYear9999.equals(theReport.getAssetChargeAsOfDate()) 
                        || dateWithYear0001.equals(theReport.getAssetChargeAsOfDate())) {
                    isInvalidDate = true;
                }
            }

            if (!isInvalidDate) {
            %>                                  
                <th>Blended Asset Charge (%):</th>
                <td>
                <report:number property="theReport.blendedAssetCharge" type="d" defaultValue="0.000" pattern="##0.000" scale="3" />
                as of
                <render:date property="theReport.assetChargeAsOfDate" patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>" />
            <%
            }
            %>
        --%> 
        </tr>   
    </tbody>
</table>

<c:if test="${bobContext.currentContract.definedBenefitContract ==true}">
    <h4><content:getAttribute beanName="allocationTitle" attribute="text"/></h4>
</c:if>
<c:if test="${bobContext.currentContract.definedBenefitContract !=true}">
    <h4><content:getAttribute beanName="payrollAllocationTitle" attribute="text"/></h4>
</c:if>
<table class="overview_table">
    <tbody>
        <tr>
<c:if test="${bobContext.currentContract.definedBenefitContract ==true}">
                <th>Last Allocation:</th>
</c:if>
<c:if test="${bobContext.currentContract.definedBenefitContract !=true}">
                <th>Last Payroll Allocation:</th>
</c:if>
            <td>
               <report:number property="theReport.contractSummaryVo.lastAllocationAmount" type="c"
                              defaultValue="0.00" />
           </td>
        </tr>
        <tr>
            <th>Invested Date:</th>
            <td>
                <render:date property="theReport.contractSummaryVo.lastSubmissionDate"
                             patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
                             defaultValue="" />
            </td>
        </tr>
        <tr>
            <th>Applicable as of:</th>
            <td>
               <render:date property="theReport.contractSummaryVo.lastPayrollDate"
                            patternOut="<%=RenderConstants.MEDIUM_MDY_SLASHED%>"
                            defaultValue="" />
           </td>
        </tr>
    </tbody>
</table>

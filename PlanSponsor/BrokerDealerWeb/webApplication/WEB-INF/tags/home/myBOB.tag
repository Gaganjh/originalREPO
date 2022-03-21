<%@ attribute name="myBOBSummary" type="com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessSummaryVO" rtexprvalue="true" required="true"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ tag import="com.manulife.pension.bd.web.BDConstants"%>
<%@ tag import="com.manulife.pension.bd.web.util.JspHelper"%>

<style type="text/css">
  #bobTitle td{
    color:#333;
    font-family: Georgia, "Times New Roman", Times, serif;
    font-size: 1.4em;
    font-style: normal;
    font-weight: normal;
    font-variant: normal;
    background: none;   
    align: left;
    padding-left: 0px;
    padding-right: 0px;
    padding-bottom: 0px;
    }
    
  #bobSummary td,th{
    color: #000000;
    font-size: 0.6875em;
    padding-top:4px;
    padding-right: 4px;
    padding-bottom: 4px;
    padding-left: 4px;
  }
  
  #bobSummary th{
    padding-top: 0px;
  }
</style>

<% try { %> 
<content:contentBean contentId="<%=BDContentConstants.MY_BOB_TITLE%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="myBOBTitle" />
<content:contentBean contentId="<%=BDContentConstants.MY_BOB_LINK%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="myBOBLink" />

<!-- My Block of Business title with image -->
<table id="bobTitle">
        <tr>
            <td width="10"><a href="/do/bob/blockOfBusiness/Active/"><content:image id="myBOBLink" contentfile="image"/></a>
            </td>
            <td valign="top" width="370">
                <a href="/do/bob/blockOfBusiness/Active/"><content:getAttribute attribute="title" beanName="myBOBTitle" /> &raquo; </a>
            </td>
        </tr>
</table>

<!-- Block of Business summary -->
<c:if test="${not empty myBOBSummary}">

    <table summary="Enter summary for this table" id="bobSummary">
        <thead>     
            <tr>
                <th width="80"></th>
                <th width="67" class="val_num_cnt"><strong>#</strong></th>
                <th width="65" class="val_num_cnt"><strong>Participants</strong></th>
                <th width="77" class="cur"><strong>Assets**</strong></th>
            </tr>
        </thead>
        <tbody> 
            <tr>
                <td nowrap="nowrap"><strong>Active Contracts</strong></td>
                <td class="val_num_cnt"><report:number property="myBOBSummary.numOfActiveContracts" pattern="<%=BDConstants.NUMBER_5_DIGITS%>"/></td>
                <td class="val_num_cnt"><report:number property="myBOBSummary.numOfActiveLives" pattern="<%=BDConstants.NUMBER_9_DIGITS%>"/></td>
                <td class="cur"><report:number property="myBOBSummary.activeContractAssets" type="c"/></td>
            </tr>
            <tr>
                <td nowrap="nowrap"><strong>Outstanding <br />
                        Proposals</strong></td>
                <td class="val_num_cnt"><report:number property="myBOBSummary.numOfOutstandingProposals" pattern="<%=BDConstants.NUMBER_5_DIGITS%>"/></td>
                <td class="val_num_cnt"><report:number property="myBOBSummary.numOfOutstandingProposalsLives" pattern="<%=BDConstants.NUMBER_9_DIGITS%>"/></td>
                <td class="cur"><report:number property="myBOBSummary.outstandingProposalsAssets" type="c"/></td>
            </tr>
            <tr>
                <td nowrap="nowrap"><strong>Pending Contracts</strong></td>
                <td class="val_num_cnt"><report:number property="myBOBSummary.numOfPendingContracts" pattern="<%=BDConstants.NUMBER_5_DIGITS%>"/></td>
                <td class="val_num_cnt"><report:number property="myBOBSummary.numOfPendingContractsLives" pattern="<%=BDConstants.NUMBER_9_DIGITS%>"/></td>
                <td class="cur"><report:number property="myBOBSummary.pendingContractsAssets" type="c"/></td>
            </tr>       
            <tr>
                <td colspan="4"><div align="right"><a href="/do/bob/blockOfBusiness/Active/"><content:getAttribute attribute="text" beanName="myBOBLink" /> &raquo;</a></div></td>
            </tr>
        </tbody>
    </table>    
</c:if>

    <%
       } catch (Exception e)  {
           JspHelper.log("myBOB.tag", e, "fails");
       }
    %>

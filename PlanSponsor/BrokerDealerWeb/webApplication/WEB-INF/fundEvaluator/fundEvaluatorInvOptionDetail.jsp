<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.platform.web.content.CommonContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.fundEvaluator.FundEvaluatorConstants" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
    
<%-- <c:set var="previewFunds" value="${.}" /> --%>
<content:contentBean contentId="<%=BDContentConstants.OVERLAY_TITLE%>"
                     type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
                     id="overlayTitle"/>
<content:contentBean contentId="<%=BDContentConstants.OVERLAY_PREVIEW_TITLE%>"
                     type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
                     id="overlayPreviewTitle"/>                  
<content:contentBean contentId="<%=BDContentConstants.ICON_LABEL_CLOSED_FUND%>"
                     type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
                     id="iconClosedFund"/>
<content:contentBean contentId="<%=BDContentConstants.ICON_LABEL_CONTRACT_SELECTED_FUND%>"
                     type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
                     id="iconContractSelectedFund"/>
<content:contentBean contentId="<%=BDContentConstants.ICON_LABEL_CALCULATED_FUND%>"
                     type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
                     id="calculatedFund"/>
<content:contentBean contentId="<%=BDContentConstants.ICON_LABEL_ADDED_FUND%>"
                     type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
                     id="fundAdded"/>
<content:contentBean contentId="<%=BDContentConstants.ICON_LABEL_REMOVED_FUND%>"
                     type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
                     id="fundRemoved"/>                     
<content:contentBean contentId="<%=BDContentConstants.ICON_LABEL_PBA_COMPETING%>"
 type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
 id="iconPBACompetingFund"/> 

<content:contentBean contentId="<%=BDContentConstants.COMPETING_FUNDS_LINK_LABLE%>"
 type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
 id="competingFundsPDFLinkLable"/>                      
                     
                     
                     
<content:contentBean contentId="<%=BDContentConstants.LABEL_NOT_APPLICABLE%>"
                     type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
                     id="labelNotApplicable"/>
<content:contentBean contentId="<%=BDContentConstants.HOW_TO_ADD_MORE_FUNDS_TO_FUND_EVALUATOR%>"
                     type="<%=CommonContentConstants.TYPE_MISCELLANEOUS%>"
                     id="howToAddMoreFundsToFundEvaluatorInfo"/>
<content:contentBean contentId="<%=BDContentConstants.PREVIEW_FUNDS_PRINT_DISCLAIMER%>"
                    type="<%=CommonContentConstants.TYPE_DISCLAIMER%>"
                    id="previewFundsPrintWindowDisclaimer"/>                 					 
                 
    <%-- Overlay Header --%>
    <DIV class="page_section_subsubheader">
        <%-- Content for overlay header --%>
        <c:if test="${fundEvaluatorForm.assetClassId ne 'previewFunds'}">
            <content:getAttribute beanName="overlayTitle" attribute="text" filter="true"/>
        </c:if>
        <c:if test="${fundEvaluatorForm.assetClassId eq 'previewFunds'}">
            <content:getAttribute beanName="overlayPreviewTitle" attribute="text" filter="true"/>
        </c:if>
    </DIV>
            
    <%-- Layer content section for detailed and Lifestyle & Lifecycle overlay --%>
    <c:if test="${fundEvaluatorForm.assetClassId ne 'previewFunds'}">
        <TABLE>
            <TBODY>
                <TR>
                    <TD colspan=2>
                        <div id="tableContainer" class="report_table">
                            <%-- Investment options --%>
                            <jsp:include page="fundEvaluatorInvOptions.jsp" >
                                <jsp:param name="fundEvaluatorForm" value="fundEvaluatorForm" />
                            </jsp:include>
                        </div>
                    </TD>
                </TR>
				<TR>
					<TD colspan="4">
						<img height=10 src="/assets/unmanaged/images/s.gif" width=1 border=0>
					</TD>
                <TR>
                <%-- Footer section for Detailed and Lifestyle & Lifecycle overlay --%>             
                <TR>
                    <TD colspan=2>  
                        <TABLE style="FONT-SIZE: 15px" >
                            <TBODY>
                                <TR>
                                    <TD WIDTH="5%">
                                        &nbsp;
                                    </TD>
                                    <TD WIDTH="40%" style="font-size:12px">
                                        <span style= "FONT-WEIGHT: normal; FONT-SIZE: 0.88em"><content:getAttribute beanName="howToAddMoreFundsToFundEvaluatorInfo" attribute="text" filter="true"/></span>
                                    </TD>
                                    <TD WIDTH="32%">
                                        <TABLE style="FONT-SIZE: 15px">
                                            <TBODY>
                                                <TR>
                                                    <TD class="footerLabelHeadings">
                                                        View
                                                    </TD>
                                                    <TD class="footerLabelOptions">
      <label>
<form:radiobutton onclick="javascript:getFundsForAssetClass()" path="fundEvaluatorForm.viewInvestmentOptionsBy" value="viewByRanking" />Rank
					
				</label>                                                    
                                                       <BR/>
                                                         <label> 
<form:radiobutton onclick="javascript:getFundsForAssetClass()" path="fundEvaluatorForm.viewInvestmentOptionsBy" value="viewByMeasurement" />Measurement
</label>
                                                    </TD>
                                                </TR>
                                            </TBODY>
                                        </TABLE>
                                    </TD>
                                    <TD rowSpan=2>
                                    	<DIV class="nextButton showScreen">
	                                        <div id="fundEvaluatorButtons">
												<input type="button" class="grey-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn next'" name="Cancel" value="Cancel" onclick="doCancel()">
											</div>
										</DIV>
                                    </TD>
                                    <TD>
                                    	<DIV class="nextButton showScreen">
	                                        <div id="fundEvaluatorButtons">
												<input type="button" class="grey-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn next'" name="SaveClose" value="Save &amp; Close" onclick="saveAndClosePanel()">
											</div>
										</DIV>
                                    </TD>
                                </TR>
                            </TBODY>
                        </TABLE>
                    </TD>
                </TR>
                <tr>
			<td colspan=1>
			</td>
				<td ><TABLE style="FONT-SIZE: 15px">
                                            <TBODY>
                                                <TR>
                                                    <TD class="footerLabelHeadings" style="padding-right: 245px;">
                                                        &nbsp;
                                                    </TD>
                                                    <TD class="footerLabelOptions">
                                                      <a href="#" onClick="openCompetingPDF();"><content:getAttribute beanName="competingFundsPDFLinkLable" attribute="text" filter="true"/></a>
														</TD>
                                                </TR>
                                            </TBODY>
                                        </TABLE>
				
				
				<%-- <a href="#" onClick="openCompetingPDF();"><content:getAttribute beanName="competingFundsPDFLinkLable" attribute="text" filter="true"/>Open PDF</a> --%>
				</td>
			</tr>
				<TR>
					<TD colspan="4">
						<img height=5 src="/assets/unmanaged/images/s.gif" width=1 border=0>
					</TD>
				</TR>
                <TR>
                    <TD width="5%">
                        &nbsp;
                    </TD>
                    <TD>
                    
                    
                        <img style="vertical-align:middle" src="/assets/unmanaged/images/calculatedFundIcon.png" alt="" width="15" height="15" /> 
						<img height="1" src="/assets/unmanaged/images/s.gif" width="5" border="0" />
						 <content:getAttribute beanName="calculatedFund" attribute="text" filter="true"/>
						<br/>
						
						<img style="vertical-align:middle" src="/assets/unmanaged/images/manuallyAddedFundIcon.png" alt="" width="15" height="15" /> 
						<img height="1" src="/assets/unmanaged/images/s.gif" width="5" border="0" />
						 <content:getAttribute beanName="fundAdded" attribute="text" filter="true"/>
						<br/>
						
						<img style="vertical-align:middle" src="/assets/unmanaged/images/manuallyRemovedFundIcon.png" alt="" width="15" height="15" /> 
						<img height="1" src="/assets/unmanaged/images/s.gif" width="5" border="0" />
						<content:getAttribute beanName="fundRemoved" attribute="text" filter="true"/>
						<br/>
								
                        <img style="vertical-align:middle" src="/assets/unmanaged/images/minusIcon.png" alt="" width="12" height="15" />
						<img height="1" src="/assets/unmanaged/images/s.gif" width="8" border="0" />
                        <content:getAttribute beanName="iconClosedFund" attribute="text" filter="true"/>
                        <br/>
                        <img style="vertical-align:middle" src="/assets/unmanaged/images/eIcon.png" alt="" width="12" height="15" />
						<img height="1" src="/assets/unmanaged/images/s.gif" width="8" border="0" />
                        <content:getAttribute beanName="iconContractSelectedFund" attribute="text" filter="true"/>
	                    <c:if test="${fundEvaluatorForm.PBAContrat}">
	                       <br/>
	                       <img style="vertical-align:middle" src="/assets/unmanaged/images/P_Icon.png" alt="" width="12" height="15" />
							<img height="1" src="/assets/unmanaged/images/s.gif" width="8" border="0" />
	                        <content:getAttribute beanName="iconPBACompetingFund" attribute="text" filter="true"/>
	                    </c:if>
						<br/>
                        <span style="font-size:10px">N/A</span>
						<img height="1" src="/assets/unmanaged/images/s.gif" width="4" border="0" />
                        <content:getAttribute beanName="labelNotApplicable" attribute="text" filter="true"/>
                       
                       
                        <%-- Competing funds selection warning message dynamic --%>
                        <DIV id="competingFundInfo" style="float:right;color:red">
                        </DIV>
                    </TD>
                </TR>
			

		</TBODY>
        </TABLE>
    </c:if>
    
    <%-- Preview funds overlay --%>
    
    <c:if test="${fundEvaluatorForm.assetClassId eq 'previewFunds'}">
        <table id="ieval_fundlist_tbl" >
            <TBODY>
                <TR>
                    <TD> 
                        <DIV id="tableContainerPreview" class="report_table" >
                            <%-- Investment options --%>
                            <jsp:include page="fundEvaluatorInvOptions.jsp" >
                                <jsp:param name="fundEvaluatorForm" value="fundEvaluatorForm" />
                            </jsp:include>
                        </DIV>
                    </TD>
                </TR>
				<TR>
					<TD colspan="4">
						<img height=10 src="/assets/unmanaged/images/s.gif" width=1 border=0>
					</TD>
                <TR>
                <%-- Footer section --%>
				<TR>
					<Table>
						<TR >
							<TD width="5%">
							</TD>
							<TD WIDTH="35%" >
								<div class="showScreen">
									<content:getAttribute beanName="howToAddMoreFundsToFundEvaluatorInfo" attribute="text" filter="true"/>
								</div>
							</TD>
							<TD WIDTH="40%" >
								<TABLE style="FONT-SIZE: 15px" width="100%">
									<TBODY>
										<TR>
											<TD width="40%" class="footerLabelHeadings">
												<div class="showScreen">
													View
												</div>
											</TD>
											<TD width="60%" class="footerLabelOptions">
												<div class="showScreen">
												<label>
<form:radiobutton onclick="javascript:displayPreviewFilter()" path="fundEvaluatorForm.viewInvOptionsByOnPreview" value="viewByRanking" />Rank </label>
													<BR/>
<label><form:radiobutton onclick="javascript:displayPreviewFilter()" path="fundEvaluatorForm.viewInvOptionsByOnPreview" value="viewByMeasurement" />Measurement</label>
												</div>
											</TD>
											
										</TR>
									</TBODY>
								</TABLE>
							</TD>
							<TD  valign="top">
								<DIV class="showScreen">
									<div id="fundEvaluatorButtons">
										<input type="button" class="grey-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn next'" name="Cancel" value="Cancel" onclick="doCancel()" />
									</div>
								</div>
							</TD>
							<TD rowspan="4" valign="top">
								<div class="showScreen">
									<div id="fundEvaluatorButtons">
										<input type="button" class="grey-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn next'" name="saveCloseFromPreview" value="Save &amp; Close" onclick="saveAndCloseFromPreview()">
									</div>
								</div>
								<br><br><br>
                                <DIV class="nextButton showScreen"> 
                                     <DIV id="fundEvaluatorButtons" class="showscreen" style=="vertical-align:top">
                                        <input type="button" class="grey-btn next" onmouseover="this.className +=' btn-hover'" onmouseout="this.className='grey-btn next'" name="Print List" value="Print List" onclick="printPreview('${_csrf.token}')">
                                    </DIV>
                                </DIV>
							</TD>
						</TR>
						<TR>
							<TD colspan="5">
								<img height=2 src="/assets/unmanaged/images/s.gif" width=1 border=0>
							</TD>
						<TR>
						<TR>
							<TD width="5%">
							</TD>					
							<TD nowrap>
							    
							    <img style="vertical-align:middle" src="/assets/unmanaged/images/calculatedFundIcon.png" alt="" width="15" height="15" /> 
								<img height="1" src="/assets/unmanaged/images/s.gif" width="5" border="0" />
								 <content:getAttribute beanName="calculatedFund" attribute="text" filter="true"/>
								<br/>
								
								<img style="vertical-align:middle" src="/assets/unmanaged/images/manuallyAddedFundIcon.png" alt="" width="15" height="15" /> 
								<img height="1" src="/assets/unmanaged/images/s.gif" width="5" border="0" />
								 <content:getAttribute beanName="fundAdded" attribute="text" filter="true"/>
								<br/>
								
								<img style="vertical-align:middle" src="/assets/unmanaged/images/manuallyRemovedFundIcon.png" alt="" width="15" height="15" /> 
								<img height="1" src="/assets/unmanaged/images/s.gif" width="5" border="0" />
								<content:getAttribute beanName="fundRemoved" attribute="text" filter="true"/>
								<br/>
								
								<img style="vertical-align:middle" src="/assets/unmanaged/images/minusIcon.png" alt="" width="12" height="15" /> 
								<img height="1" src="/assets/unmanaged/images/s.gif" width="8" border="0" />
								<content:getAttribute beanName="iconClosedFund" attribute="text" filter="true"/>
								<br/>
								<img style="vertical-align:middle" src="/assets/unmanaged/images/eIcon.png" alt="" width="12" height="15" />
								<img height="1" src="/assets/unmanaged/images/s.gif" width="8" border="0" />
								<content:getAttribute beanName="iconContractSelectedFund" attribute="text" filter="true"/>
								<c:if test="${fundEvaluatorForm.PBAContrat}">
			                       <br/>
			                       <img style="vertical-align:middle" src="/assets/unmanaged/images/P_Icon.png" alt="" width="12" height="15" />
									<img height="1" src="/assets/unmanaged/images/s.gif" width="8" border="0" />
			                        <content:getAttribute beanName="iconPBACompetingFund" attribute="text" filter="true"/>
			                     </c:if>
								<br/>
								<span style="font-size:10px">N/A</span>
								<img height="1" src="/assets/unmanaged/images/s.gif" width="4" border="0" />
								<content:getAttribute beanName="labelNotApplicable" attribute="text" filter="true"/>
							</TD>
							<TD nowrap>
								<TABLE style="FONT-SIZE: 15px" class="showScreen" width="100%">
									<TBODY>
										<TR>
											<TD width="40%" class="footerLabelHeadings">
												List
											</TD>
											<TD class="footerLabelOptions" widht="60%">
											<label>
<form:radiobutton onclick="javascript:displayPreviewFilter()" path="fundEvaluatorForm.listInvesmentOptionBy" id="invOptionByRanking" value="availableInvOptions" />
													All available funds
</label>
												<br/>
												<label>
<form:radiobutton onclick="displayPreviewFilter()" path="fundEvaluatorForm.listInvesmentOptionBy" id="invOptionByMeasurement" value="selectedInvOptions" />
													Only selected funds
</label>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
							</TD>
							<TD>
							</TD>
						</TR>
						<tr>
						<TD width="5%">
							</TD>
							<TD >
								&nbsp;
							</TD>
							
						<td  style="text-align: center;" >
						<a href="#" onClick="openCompetingPDF();"><content:getAttribute beanName="competingFundsPDFLinkLable" attribute="text" filter="true"/></a>
						 
						</td>
						<td> &nbsp;</td>
						</tr>
						
						<TR>
							<TD width="5%">
							</TD>
							<TD>
								&nbsp;
							</TD>
							<TD colspan=3 >
								<%-- Competing funds warning message dynamic --%>
								<DIV id="competingFundInfoPreview" class="showScreen" style="float:right;color:red" wrap height="20px">
								</DIV>
							</TD>
						</TR>
						<TR>
							<TD colspan="4">
								<%-- Preview funds print window disclaimer (Only for print window)--%>
								<DIV class="showPrint" >
									 <content:getAttribute beanName="previewFundsPrintWindowDisclaimer" attribute="text" 	filter="true"/>
								</DIV>
							</TD>
						</TR>
					</Table>
				</TR>
            </TBODY>
        </table>
    </c:if> 

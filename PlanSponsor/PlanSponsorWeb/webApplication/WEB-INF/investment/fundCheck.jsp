
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
Boolean technicalDifficulties =(Boolean)request.getAttribute(Constants.TECHNICAL_DIFFICULTIES);
pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_CURRENT_FUNDCHECK_CONTENT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="CurrentFundCheck"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_PREVIOUS_FUNDCHECK_CONTENT%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="PreviousFundCheck"/>     
                     
<content:contentBean contentId="<%=ContentConstants.FUNDCHECK_NO_PDFS_EXISTS_MESSAGE%>"
						type="<%=ContentConstants.TYPE_MESSAGE%>"
						id="NoPDFsExistMessage" />

<p>
	<content:errors scope="request" />
</p>

<c:if test="${empty technicalDifficulties}">

<c:set var="tableWidth" value="485" />
<c:choose>
<c:when test="${fundCheckForm.pdfCount == 2}">
<c:set var="tableWidth" value="485" />
</c:when>
<c:otherwise>
<c:set var="tableWidth" value="254" />
</c:otherwise>
</c:choose>
<table border="0" cellspacing="3" cellpadding="2" width="700">
    <tr>
        <td width="511" valign="top">        
	        <table  width="${tableWidth}" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td width="254"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
	                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	                <td width="254"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
	                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	            </tr>
	            <tr>
	                <td colspan="5">
		                <table width="100%" border="0" cellspacing="0" cellpadding="0">
		                    <tr class="tablehead">
							<!--<b><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></b>-->
		                        <td class="tableheadTD1"><b>Investment Platform Update</b></td>
		                    </tr>
		                </table>
	                </td>
	            </tr>
	            <c:choose>
		            <c:when test="${fundCheckForm.fundCheckPDFAvailable}">
		            	<c:choose>
			            	<c:when test="${fundCheckForm.pdfCount == 2}">
							
						        <tr class="whitebox" align='center' >
						          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td valign="center" height='80'><a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.currentFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.currentFundCheck.year}"/>
&selectedNotice=<c:out value="PS"/>' target="_blank"><b>${fundCheckForm.currentFundCheck.title} <%-- filter="false" --%></b></a></td>
						          <td class="whiteboxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td valign="center" height='80'><a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.previousFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.previousFundCheck.year}"/>
&selectedNotice=<c:out value="PS"/>' target="_blank"><b>${fundCheckForm.previousFundCheck.title} <%-- filter="false" --%></b></a></td>
						          <td class="whiteboxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

						        </tr>

						        <tr class="datacell2" align='center'>
						          	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td valign="center" height='60'>
										<b>
										<c:if test="${fundCheckForm.currentFundCheck.participantNoticeInd == 'N'}"> No </c:if>
											${fundCheckForm.currentFundCheck.participantNoticeTitle}
										</b>
									</td>
						          	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td valign="center" height='60'>
										<b>
										<c:if test="${fundCheckForm.previousFundCheck.participantNoticeInd == 'N'}"> No </c:if>
											${fundCheckForm.previousFundCheck.participantNoticeTitle}
										</b>
									</td>
						          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						        </tr>
								
								<tr class="datacell2" align='center'>
						          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:choose>
<c:when test="${fundCheckForm.currentFundCheck.participantNoticeInd == 'Y'}">
<td valign="top" height='30'><b>
<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.currentFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.currentFundCheck.year}"/>
&selectedNotice=<c:out value="PPT"/>&selectedLanguage=<c:out value="EN"/>&selectedParticipantNotice=<c:out value="${fundCheckForm.currentFundCheck.participantNoticeInd}"/>' target="_blank">English</a> | 
<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.currentFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.currentFundCheck.year}"/>
&selectedNotice=<c:out value="PPT"/>&selectedLanguage=<c:out value="SP"/>&selectedParticipantNotice=<c:out value="${fundCheckForm.currentFundCheck.participantNoticeInd}"/>' target="_blank">Spanish</a></b></td>
</c:when>
<c:otherwise><td valign="top" height='30'></td>
</c:otherwise>
</c:choose>

						          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<c:choose>
<c:when test="${fundCheckForm.previousFundCheck.participantNoticeInd == 'Y'}">
<td valign="top" height='30'><b>
<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.previousFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.previousFundCheck.year}"/>
&selectedNotice=<c:out value="PPT"/>&selectedLanguage=<c:out value="EN"/>&selectedParticipantNotice=<c:out value="${fundCheckForm.previousFundCheck.participantNoticeInd}"/>' target="_blank">English</a> | 
<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.previousFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.previousFundCheck.year}"/>
&selectedNotice=<c:out value="PPT"/>&selectedLanguage=<c:out value="SP"/>&selectedParticipantNotice=<c:out value="${fundCheckForm.previousFundCheck.participantNoticeInd}"/>' target="_blank">Spanish</a></b></td>
</c:when>
<c:otherwise><td valign="top" height='30'></td>
</c:otherwise>
</c:choose>
						          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								 
						        </tr>
						        <tr>
				          			<td colspan="5" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				        		</tr>
				        	</c:when>
				        	<c:otherwise>
				        		<c:choose>
				        			<c:when test="${fundCheckForm.currentFundCheck.title != null and fundCheckForm.currentFundCheck.title != ''}">
<c:set var="fundCheckTitle" value="${fundCheckForm.currentFundCheck.title}"/>
				        			</c:when>
				        			<c:otherwise>
<c:set var="fundCheckTitle" value="${fundCheckForm.previousFundCheck.title}"/>
				        			</c:otherwise>
				        		</c:choose>
						        <tr class="tablesubhead">
						          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						          <td></td>
						          <td valign="top"></td>
						          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						        </tr>
						        <tr class="datacell1">
						        	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						        	<td colspan="3" height='80'><center>
						        		<c:choose>
						        			<c:when test="${fundCheckForm.currentFundCheck.title != null and fundCheckForm.currentFundCheck.title != ''}">
						        				<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.currentFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.currentFundCheck.year}"/>
												&selectedNotice=<c:out value="PS"/>' target="_blank"><b>${fundCheckTitle}</b></a>
						        			</c:when>
						        			<c:otherwise>
						        				<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.previousFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.previousFundCheck.year}"/>
												&selectedNotice=<c:out value="PS"/>' target="_blank"><b>${fundCheckTitle}</b></a>
						        			</c:otherwise>
						        		</c:choose>
						        		</center>
						        	</td>
						        	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						        </tr>
						        <tr class="datacell2" align='center'>
						          	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
									<td valign="center" height='80' colspan="3">
										<c:choose>
						        			<c:when test="${fundCheckForm.currentFundCheck.title != null and fundCheckForm.currentFundCheck.title != ''}">
												<c:choose>
													<c:when test="${fundCheckForm.currentFundCheck.participantNoticeInd == 'N'}">
														<b>No ${fundCheckForm.currentFundCheck.participantNoticeTitle}</b>
													</c:when>
													<c:when test="${fundCheckForm.currentFundCheck.participantNoticeInd == 'Y'}">
														<b>
															${fundCheckForm.currentFundCheck.participantNoticeTitle}<br><br>
															<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.currentFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.currentFundCheck.year}"/>
															&selectedNotice=<c:out value="PPT"/>&selectedLanguage=<c:out value="EN"/>&selectedParticipantNotice=<c:out value="${fundCheckForm.currentFundCheck.participantNoticeInd}"/>' target="_blank">English</a> | 
															<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.currentFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.currentFundCheck.year}"/>
															&selectedNotice=<c:out value="PPT"/>&selectedLanguage=<c:out value="SP"/>&selectedParticipantNotice=<c:out value="${fundCheckForm.currentFundCheck.participantNoticeInd}"/>' target="_blank">Spanish</a>
														</b>
													</c:when>
												</c:choose>
											</c:when>
						        			<c:otherwise>
												<c:choose>
													<c:when test="${fundCheckForm.previousFundCheck.participantNoticeInd == 'N'}">
														<b>No ${fundCheckForm.previousFundCheck.participantNoticeTitle}</b>
													</c:when>
													<c:when test="${fundCheckForm.previousFundCheck.participantNoticeInd == 'Y'}">
														<b>
															${fundCheckForm.previousFundCheck.participantNoticeTitle}<br><br>
															<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.previousFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.previousFundCheck.year}"/>
															&selectedNotice=<c:out value="PPT"/>&selectedLanguage=<c:out value="EN"/>&selectedParticipantNotice=<c:out value="${fundCheckForm.previousFundCheck.participantNoticeInd}"/>' target="_blank">English</a> | 
															<a href='fundCheck?action=openPDF&selectedSeason=<c:out value="${fundCheckForm.previousFundCheck.season}"/>&selectedYear=<c:out value="${fundCheckForm.previousFundCheck.year}"/>
															&selectedNotice=<c:out value="PPT"/>&selectedLanguage=<c:out value="SP"/>&selectedParticipantNotice=<c:out value="${fundCheckForm.previousFundCheck.participantNoticeInd}"/>' target="_blank">Spanish</a>
														</b>
													</c:when>
												</c:choose>
						        			</c:otherwise>
						        		</c:choose>
									</td>
						          	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						        </tr>						        
						        <tr>
				          			<td colspan="5" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				        		</tr>
				        	</c:otherwise>
			        	</c:choose>
	        		</c:when>
	        		<c:otherwise>
	        			<tr class="datacell1">
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							<td colspan="3"><content:getAttribute
								id="NoPDFsExistMessage" attribute="text" /></td>
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						</tr>
						<tr>
		          			<td colspan="5" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		        		</tr>
	        		</c:otherwise>
	        	</c:choose>
			</table>
        </td>
        <style>
				.width0{
				width: 0px !important;
			}
			div.righthandlayer1content{
				height:292px;
				overflow: auto;
			}
			:root *> div.righthandlayer1content{
				height:286px;
				overflow: auto;
			}
		</style>
        <td valign="top">
        	<content:rightHandLayerDisplay layerName="layer1" beanName="layoutPageBean" />
        </td>
    </tr>
</table>
</c:if>				
                    

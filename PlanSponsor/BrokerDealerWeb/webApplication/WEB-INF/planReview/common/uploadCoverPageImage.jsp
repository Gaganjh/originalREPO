<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<content:contentBean
	contentId="<%=BDContentConstants.UPLOAD_COVER_INSTRUCTIONS%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>"
	id="uploadCoverInstructions" />
<content:contentBean contentId="<%=BDContentConstants.TERMS_OF_USE%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="termsOfUse" />
	
<content:contentBean
	contentId="<%=BDContentConstants.PLAN_REVIEW_STOCK_IMAGES_INTRODUCTION_TEXT%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="stockImagesIntroText" />
	

<style type="text/css">
.hover
{
 -moz-box-shadow: 0 0 15px orange; -webkit-box-shadow: 0 0 15px orange; box-shadow: 0 0 15px orange; 
/* border:5px solid lightskyblue; */
}
</style>
<script type="text/javascript">
$(document).ready(function(){
	$('.name').mouseover(function(){
		$(this)).addClass('hover');
	});
     $('.name').mouseout(function(){
    	 $(this)).removeClass('hover');
     });
});
</script>

<div id="uploadCoverImagediv"
	class="ui-helper-hidden ui-state-highlight">
	<div style="background-color: #455660; border-top: #febe10 4px solid;">
		<ul class="proposal_nav_menu">
			<LI><a id="Requested_Printed_copies_sec" class="selected_link">
					<SPaN style="padding-left: 10px"> <strong>Customize cover image</strong>
				</span>
			</a></LI>
		</ul>
	</div>
	<div style="text-align: right; padding-right: 15px; padding-top: 5px">
		<a href="javascript:doCoverCancel()"><img class="cancel" height="15"
			width="15" src="/assets/unmanaged/images/icon_hide.gif" /></a>
	</div>
	<div style="overflow-y: scroll; overflow-x: hidden; width: 100%; height: 460px;">
		<table class="coverImagediv" style="table-layout: fixed; width: 800px">
			<tr>
				<td width="2%"></td>
				<td>
					<table style="table-layout: fixed; width: 780px">
						<tr>
							<td width="350px" align="left"
								style="font-size: 18px; font-weight: 50px; color: black;">
								Customization options:</td>
								<td width="150px">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" 
								style="font-size: 14px; font-weight: 40px; color: #0E968B;">
								<strong>1) Upload your own</strong>
							</td>
							<td align="left" rowspan="2" style="padding-left: 5px">
								<a
									href="javascript:doSelectedCoverImage('${planReviewReportForm.defaultCoverPageImage.orderIndex}')"
									class="logoimageId"><img
										onmouseover="this.className +=' hover'"
										onmouseout="this.className='name'"
										src="${planReviewReportForm.defaultCoverPageImage.onlineCoverImagePath}"
										height="95px" width="170px" /> <!-- <div id="uploadPreview"></div> -->
										<br>
										<div tyle="font-size: 12px; font-weight: 50px; width: 170px; color: black; padding-top:5px;">
											
												${planReviewReportForm.defaultCoverPageImage.coverPageCaptionCmaValue}</center>
										</div>
								</a>
							</td>
						</tr>
						<tr>
							<td align="left" 
								style="font-size: 12px; font-weight: 50px; color: black;">
								<content:getAttribute
									id="uploadCoverInstructions" attribute="text" /><br />
							</td>

						</tr>
						<tr>

							<td align="left"
								style="font-size: 12px; font-weight: 50px; color: black;">
								<div class="errorSpan" id="error"
									style="color: red; display: none;"></div>
								<div align="left" id="errorValue"
									style="font-size: 12px; font-weight: 50px; color: red;"></div> <br/>
								Acceptable formats: jpg <br /> Orientation: Landscape<br />
								Dimensions: 2643(W)&nbsp;&times;&nbsp;1247(H)<br />Color type : RGB only<br />Resolution: High quality<br />
								Max file size | 2MB<br />
								<br /> 
								<input type="file" name="uploadCoverImage" 
									accept="image/*" id="uploadCoverImage" />
									
									<br /> Uploaded file:  <c:if
									test="${planReviewReportForm.uploadCoverImage ne null}">
									${planReviewReportForm.uploadCoverImage.originalFilename}</c:if>
								<br />
							<br /> 
							<input class="blue-btn cancel"
								onmouseover="this.className +=' btn-hover'"
								onmouseout="this.className='blue-btn next'" value=upload
								type="button" name=upload onclick="onUploadCoverImageClick()">
								
							</td>
							</tr>
						<tr>
							<td align="left"
								style="font-size: 12px; font-weight: 50px; color: black;" >
								<content:getAttribute
									id="termsOfUse" attribute="text" /><br />
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2"
								style="font-size: 14px; font-weight: 50px; color: black;">
								<strong>OR</strong>
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2"
								style="font-size: 14px; font-weight: 40px; color: #0E968B;">
								<strong>2) Stock images</strong>
							</td>
						</tr>
						<tr>
							<td align="left"
								style="font-size: 12px; font-weight: 50px; color: black;" >
								<content:getAttribute
									id="stockImagesIntroText" attribute="text" /><br />
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">
								<table style="table-layout: fixed; width: 750px">
<c:forEach items="${planReviewReportForm.stockCoverPageImages}" var="stockCoverPageImage" varStatus="indexValue" >

<c:set var="rowIndex"  value="${indexValue.index}"/>


										<c:if test="${rowIndex % 4 == 0}">
											<tr>
										</c:if>


										<td class=name width="150px" style="padding-left: 10px; padding-top:10px;" ><a
											href="javascript:doSelectedCoverImage('${stockCoverPageImage.orderIndex}')"
											cssClass="logoimageId"><img onmouseover="this.className +=' hover'"
												onmouseout="this.className='name'" 
												src="${stockCoverPageImage.onlineCoverImagePath}"
												height="95px" width="170px" /> <!-- <div id="uploadPreview"></div> -->
												<br/>
												<div style="padding-top:5px; font-size: 12px; font-weight: 50px; width: 170px; color: black;">
													
														${stockCoverPageImage.coverPageCaptionCmaValue} 
												</div>
												<br/> </a></td>

										<c:if test="${(rowIndex+1) % 4 == 0}">
											</tr>
										</c:if>
</c:forEach>
								</table>
							</td>
						</tr>

					</table>
				</td>
			</tr>
		</table>

	</div>
	<div align="right" style="color: white">&nbsp;</div>
</div>


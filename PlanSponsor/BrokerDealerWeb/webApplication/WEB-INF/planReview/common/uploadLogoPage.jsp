<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<content:contentBean
	contentId="<%=BDContentConstants.UPLOAD_LOGO_INSTRUCTIONS_CONTRACT_LEVEL%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>"
	id="contractUploadLogoInstructions" />
<content:contentBean
	contentId="<%=BDContentConstants.UPLOAD_LOGO_INSTRUCTIONS%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>"
	id="bobUploadLogoInstructions" />
<content:contentBean
	contentId="<%=BDContentConstants.UPLOAD_LOGO_COMPANY_NAME_MANDATORY%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="companyNameMandatory" />

<div id="updateLogoImage" class="ui-helper-hidden ui-state-highlight">
	<div style="background-color: #455660; border-top: #febe10 4px solid;">
		<ul class="proposal_nav_menu">
			<LI><a id="Requested_Printed_copies_sec" class="selected_link">
					<SPaN style="padding-left: 10px"> <Strong>Upload
							logo</strong>
				</span>
			</a></LI>
		</ul>
	</div>
	<div id="page_section_subheader">
		<div class="Requested_Printed_copies" style="display: block;">
			<div style=""></div>
		</div>
	</div>
	<div>
		<div align="left"
			style="font-size: 12px; padding-left: 25px; padding-right: 25px; padding-top: 20px; font-weight: 50px; color: black;">
			<!-- Upload an image that best represents the plan sponsor.Images will be resized to fit the space 
								accordingly, For best performance, please ensure the dimensions of your image are as close to
								those provided and it's a high resolution jpg. -->
			<c:if test="${sessionScope.bobResults!='contract'}">
				<content:getAttribute id="bobUploadLogoInstructions"
					attribute="text" />
				<br />
			</c:if>
			<c:if test="${sessionScope.bobResults=='contract'}">
				<content:getAttribute id="contractUploadLogoInstructions"
					attribute="text" />
				<br />
			</c:if>
		</div>
		<table style="padding-left: 25px; padding-top: 20px;">
			<tr>
				<td align="left"
					style="color: rgb(14, 150, 139); padding-top: 10px; font-size: 14px;">
					<strong>Upload your own:</strong>
				</td>
			</tr>
			<tr>
				<td style="padding-top: 0x; font-size: 12px; color: black;">Acceptable
					formats: jpg</td>
			</tr>
			<tr>
				<td style="padding-top: 0px; font-size: 12px; color: black;">Dimensions
					for best fit: 603(W)&times;248(H)</td>
			</tr>
			<tr>
				<td style="padding-top: 0px; font-size: 12px; color: black;">Color
					type: RGB only</td>
			</tr>
			<tr>
				<td style="padding-top: 0px; font-size: 12px; color: black;">Max
					file size: 1MB</td>
			</tr>
		</table>
	</div>
	<div>
		<table height="400px" width="100%" cellpadding=0 cellspacing=0
			align="left">
			<tr>
				<td width="50%" colspan="2">
					<table width="100%" cellpadding=0 cellspacing=0>
						<tr height="40px">
							<td colspan="2" style="padding-bottom: 15px; padding-left: 26px; vertical-align: bottom;">
								<div style="display: block; vertical-align: top;">
									<span id="imageError"
										style="font-size: 12px; font-weight: 50px; color: red;"></span>
								</div>
							</td>
						</tr>
						<tr></tr>
						<tr>
							<td align="left" colspan="2"
								style="font-size: 12px; font-weight: 50px; color: black; padding-bottom: 19px; padding-left: 26px;">
								<!-- <input type="file" name="planReviewReportForm.uploadImage"  id="choose" multiple="multiple" accept="image/*" /> -->
								<form:input type="file" path="planReviewReportForm.uploadImage"
									 styleId="chooseLogo"
									onchange="readURL(this);" value="Choose File" accept="image/*" cssClass="uploadImage"/>
								<br /> Uploaded file: <c:if
									test="${planReviewReportForm.uploadImage ne null}">
									${planReviewReportForm.uploadImage.originalFilename}</c:if>
							</td>
						<tr  height="20px">
							<td colspan="2" style="padding-bottom: 10px; padding-left: 26px; vertical-align: bottom;">
								<div style="display: block; vertical-align: top;">
									<span id="companyError"
										style="color: red; font-size: 12px; font-weight: 50px;"></span>
								</div>
							</td>
						</tr>
						<tr>
							<td
								style="font-size: 11px; padding-left: 25px; font-weight: 50px; color: black;">
								<span style="font-size: 12px; font-weight: 50px; color: red;">*</span>Required
								field: Company legal name associated with the logo
							</td>
						</tr>
						<tr>
<td class=name align="left" style="padding-left: 26px;">
<input type="text" name="companyName" maxlength="25" size="40" styleClass="companyName" >


</input> &nbsp;</td>
						</tr>
					</table>
				</td>
				<td rowspan="4" style="padding-bottom: 250px;">
					<div id="imagediv"
						style="display: block; width: 301px; text-align: right;">
						<img src="/assets/unmanaged/images/s.gif" id="fileChecker" alt="" height="120px" width="280px" /><br />
						<a style="color: red;" href="javascript://" onclick="doRemove()">remove</a>
					</div>
				</td>
			</tr>
			<tr>
				<td align=left
					style="vertical-align: top; padding-right: 40px; padding-bottom: 160px; padding-left: 26px">
					<span> <input class="blue-btn cancel"
						onmouseover="this.className +=' btn-hover'"
						onmouseout="this.className='blue-btn next'" value=Cancel
						type=button name=Back onclick="doLogoCancel()" style="float: left">
				</span> <span style="margin: 10px;" cssClass="nextButton"
					id="enabledBottomNextButton"> <input class="blue-btn submit"
						onmouseover="this.className +=' btn-hover'"
						onmouseout="this.className='blue-btn next'" value=Upload
						type=button name=Back onclick="onUploadLogoImage()">
				</span>
				</td>
			</tr>
		</table>
	</div>
	<div align="right" style="color: white">&nbsp;</div>
</div>

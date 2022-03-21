
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<div id="showShippingDetailsDiv"
	class="ui-helper-hidden ui-state-highlight">

	<div style="background-color: #455660; border-top: #febe10 4px solid;">
		<ul class="proposal_nav_menu">
			<LI><a id="Requested_Printed_copies_sec" class="selected_link">
					<Span style="padding-left: 185px"> <strong>Shipping
							details</strong>
				</span>
			</a></LI>
		</ul>
	</div>
	<div style="text-align: right; padding-right: 15px; padding-top: 5px">
		<a href="javascript:doClose()"><img class="cancel" height="15"
			width="15" src="/assets/unmanaged/images/icon_hide.gif" /></a>
	</div>
	<div>
		<table class="printConfirmationbox" cellspacing="0" cellpadding="0"
			width="100%" style="padding-top: 17px; padding-bottom: 7px;">
<c:if test="${not empty planReviewHistoryDetailsReportForm.shippingAddressDetails}">
				<tr>
					<td width="50%" class="addressLable">Selected Print Confirmation
						No:</td>

					<td width="50%" class="address">${planReviewHistoryDetailsReportForm.shippingAddressDetails.printConfirmationNumber}</td>
				</tr>

				<tr>
					<td class="addressLable">Recipient's Name:</td>
					<td class="address">${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientName}</td>
				</tr>
				<tr>
					<td class="addressLable">Phone:</td>
					<td class="address">${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientPhoneNumber}</td>
				</tr>
				<tr>
					<td  class="addressLable">Email:</td>
					<td class="address">${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientEmail}</td>
				</tr>
				<tr>
					<td class="addressLable">Company:</td>
					<td  class="address">${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientCompanyName}</td>
				</tr>
				<tr>
					<td  class="addressLable">Address Line 1:</td>
					<td  class="address">${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientAddressLine1}</td>
				</tr>
				<tr>
					<td class="addressLable">Address Line 2:</td>
					<td  class="address">
						${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientAddressLine2}</td>
				</tr>
				<tr>
					<td  class="addressLable">City:</td>
					<td class="address">${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientCity}</td>
				</tr>
				<tr>
					<td  class="addressLable">State:</td>
					<td  class="address">${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientState}</td>
				</tr>
				<tr>
					<td  class="addressLable">Zip:</td>
					<td  class="address">
						${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientZip}</td>
				</tr>
				<tr>
					<td  class="addressLable">Country:</td>
					<td class="address">
						${planReviewHistoryDetailsReportForm.shippingAddressDetails.recipientCountry}</td>
				</tr>
				<tr>
					<td class="addressLable">Number of Copies:</td>
					<td  class=address>${planReviewHistoryDetailsReportForm.shippingAddressDetails.numberOfCopies}</td>
				</tr>
				<tr>
					<td  class="addressLable">Shipping Method:</td>
					<td  class="address">${planReviewHistoryDetailsReportForm.shippingAddressDetails.shippingMethod}</td>
				</tr>
</c:if>
		</table>
	</div>
</div>

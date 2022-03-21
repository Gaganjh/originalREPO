<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bdweb"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<script type="text/javascript">
$(document).ready(function(){

	
});
</script>

<div class="report_height dymanicSCroll">
	<div id="loadingContent" class="ui-helper-hidden ui-state-highlight"
		style="height: 645px; overflow-y: auto; overflow-x: hidden;">
		<div style="background-color: #455660; border-top: #febe10 4px solid;">
			<ul class="proposal_nav_menu">
				<li><a id="Customize_Shipping_sec" class="selected_link"><span
						style="padding-left: 10px;"> <strong>Customize
								your shipping details</strong></span></a></li>
			</ul>
		</div>

		<!-- Report Erros/Warning Messages -->
		<report:formatMessages scope="session"
			suppressDuplicateMessages="true" />

<bd:form action="/do/bob/" modelAttribute="planReviewPrintForm" name="planReviewPrintForm">
		<!-- <div class="Customize_Shipping" style="display: block;" >
					<div  style=""></div> -->
		<c:if test="${not empty planReviewPrintForm.shippingVO}">
			<input type="hidden"  name="rvpUser" />
			<div align="left" class="val_num_cnt"
				style="width: 100%; text-align: left; padding-top: 10px; padding-left: 5px; font: 12px verdana, arial, helvetica, sans-serif;">

				<!-- Block to display Default Address shipping block as enabled -->
				<div align="left" styleId="ifRvpUser" style="padding-left: 40px;">
					<form:hidden id="defaultaddressHidden"
							path ="shippingVO.defaultAddressSelected" />

					<form:hidden path="defaultAddress" />
				<c:if test="${(userProfile.role.roleType.internal eq false)}">
					<form:checkbox id="defaultAddressCheckBox"
						path="shippingVO.defaultAddressSelected"
						onchange='defaultAddressCheckBoxClicked(this)'
						disabled="${planReviewPrintForm.shippingVO.disableDefaultAddressCheckbox}" />
					&nbsp; Apply the last shipping information you saved to this order.

				</c:if>
				</div>

				<div align="left" styleId="ifRvpUser"
					style="padding-left: 40px; padding-top: 15px; color: #000; font: 12px verdana, arial, helvetica, sans-serif;">
					<b> Note: It's important that you ensure the address you enter
						is appropriate for all contracts you select. If you need to
						deselect any contracts please click Cancel below.</b>
				</div>
				<div align="left" styleId="ifRvpUser"
					style="padding-left: 44px; padding-top: 15px; color: #000; font: 12px verdana, arial, helvetica, sans-serif;">
					<span style="color: #FF0000;">*</span>Indicates a required field
				</div>
			</div>
			<DIV id="default_Shipping_Addr" style="display: block;">

				<div id="defaultaddress">
					<table style="cellspacing: 0px; cellpadding: 0px; width: 100%;"padding-left:40px;">
						<tr>
							<td width="2%" valign="top"></td>
							<td width="48%">

								<table id="shippingAddressDetails"
									style="cellspacing: 0px; cellpadding: 0px; width: 100%; align: left;">

									<tr>
										<td width="15%" class=addressLable
											Style="height: 18px;"><span
											style="color: #FF0000;">*</span>Recipient's name:</td>
										<td width="70%" class=address Style="height: 18px;"><form:input
												path="shippingVO.recipientName"
												cssClass="shppingAddress" size="20" maxlength="80"
												id="recipientName" /> &nbsp; <bdweb:fieldHilight 
												name="RecipientName" singleDisplay="true" style="float:none"
												className="errorIcon" /></td>

									</tr>
									<tr>
										<td width="10%" class=addressLable
											Style="height: 18px; "><span
											style="color: #FF0000;">*</span>Phone:</td>
										<td width="70%" class=address Style="height: 18px;"><form:input 
												path="shippingVO.recipientPhoneNumber"
												cssClass="shppingAddress" size="16" maxlength="16"
												id="recipientPhoneNumber" /> &nbsp;<bdweb:fieldHilight 
												name="phoneNumber" singleDisplay="true" style="float:none"
												className="errorIcon" /></td>

									</tr>
									<tr>
										<td width="10%" class=addressLable
											Style="height: 18px; "><span
											style="color: #FF0000;">*</span>Email:</td>
										<td width="70%" id="shppingAddress" class=address
											Style="height: 18px;"><form:input
												path="shippingVO.recipientEmail"
												cssClass="shppingAddress" size="16" maxlength="255"
												id="recipientEmail" /> &nbsp;<bdweb:fieldHilight 
												name="emailAddress" singleDisplay="true" style="float:none"
												className="errorIcon" /></td>

									</tr>
									<tr>
										<td width="10%" class=addressLable
											Style="height: 18px;"><span
											style="color: #FF0000;">*</span>Company:</td>
										<td width="70%" class=address Style="height: 18px;"><form:input 
												path="shippingVO.recipientCompanyName"
												cssClass="shppingAddress" size="20" maxlength="100"
												id ="recipientCompany" /> &nbsp;<bdweb:fieldHilight 
												name="company" singleDisplay="true" style="float:none"
												className="errorIcon" /></td>
									</tr>
									<tr>
										<td width="10%" class=addressLable
											Style="height: 18px; "><span
											style="color: #FF0000;">*</span>Address Line 1:</td>
										<td width="70%" class=address Style="height: 18px;"><form:input
												path="shippingVO.recipientAddressLine1"
												cssClass="shppingAddress" size="20" maxlength="100"
												id="addressLine1" /> &nbsp;<bdweb:fieldHilight 
												name="addressLine1" singleDisplay="true" style="float:none"
												className="errorIcon" /></td>
									</tr>
									<tr>
										<td width="10%" class=addressLable >Address
											Line 2:</td>
										<td width="70%" class=address Style="height: 18px;"><form:input
												path="shippingVO.recipientAddressLine2"
												cssClass="shppingAddress" size="20" maxlength="100"
												id="addressLine2" /></td>
									</tr>
									<tr>
										<td width="10%" class=addressLable ><span
											style="color: #FF0000;">*</span>City:</td>
										<td width="70%" class=address><form:input
												path="shippingVO.recipientCity"
												cssClass="shppingAddress" size="20" maxlength="50"
												id="recipientCity" /> &nbsp;<bdweb:fieldHilight   name="city"
												singleDisplay="true" style="float:none"
												className="errorIcon" /></td>
									</tr>
									<tr>
										<td width="10%" class=addressLable ><span
											style="color: #FF0000;">*</span>State:</td>
										<td width="70%" class=address>

										<form:select id="stateCode"
												cssClass="shppingAddress"
												path="shippingVO.recipientState">
											<form:option value=""> </form:option>
											<c:if test="${planReviewPrintForm.shippingVO.recipientCountry eq 'USA'}">
												<c:forEach var="stateItem" items="${planReviewPrintForm.usaStates}">
													<c:if test="${stateItem.key eq planReviewPrintForm.shippingVO.recipientState}">
											        	<form:option value="${stateItem.key}" selected><c:out value="${stateItem.key}"/></form:option>
											        </c:if>
											        <c:if test="${stateItem.key ne planReviewPrintForm.shippingVO.recipientState}">
											        	<form:option value="${stateItem.key}"><c:out value="${stateItem.key}"/></form:option>
											        </c:if>
											    </c:forEach>
										     </c:if>
											<c:if test="${planReviewPrintForm.shippingVO.recipientCountry eq 'CAN'}">
												<c:forEach var="stateItem" items="${planReviewPrintForm.canadianProvinces}">
											        <c:if test="${stateItem.key eq planReviewPrintForm.shippingVO.recipientState}">
											        	<form:option value="${stateItem.key}" selected><c:out value="${stateItem.key}"/></form:option>
											        </c:if>
											        <c:if test="${stateItem.key ne planReviewPrintForm.shippingVO.recipientState}">
											        	<form:option value="${stateItem.key}"><c:out value="${stateItem.key}"/></form:option>
											        </c:if>
											    </c:forEach>
										    </c:if>
										</form:select> 
										&nbsp;<bdweb:fieldHilight name="state" singleDisplay="true"  
												style="float:none" className="errorIcon" /></td>
									</tr>
									<tr>
										<td width="10%" class=addressLable ><span
											style="color: #FF0000;">*</span>Zip:</td>
										<td width="70%" class=addressDisabled><form:input
												path="shippingVO.recipientZip"
												cssClass="shppingAddress" size="10" maxlength="10"
												id="recipientZipCode" /> &nbsp; <bdweb:fieldHilight    name="zipCode"
												singleDisplay="true" style="float:none"
												className="errorIcon" /></td>
									</tr>
									<tr>
										<td width="10%" class=addressLable><span
											style="color: #FF0000;">*</span>Country: &nbsp;</td>
										<td width="70%" id="shppingAddress"
											class="addressDisabled shppingAddress">
											<form:select  id="country"
												cssClass="shppingAddress"
												path="shippingVO.recipientCountry"
												onchange="populateStates(this.value, '');">
												
												<c:forEach var="countryItem" items="${planReviewPrintForm.countryMap}">
											        <c:if test="${countryItem.key eq planReviewPrintForm.shippingVO.recipientCountry}">
											        	<form:option value="${countryItem.key}" selected><c:out value="${countryItem.key}"/></form:option>
											        </c:if>
											        <c:if test="${countryItem.key ne planReviewPrintForm.shippingVO.recipientCountry}">
											        	<form:option value="${countryItem.key}"><c:out value="${countryItem.value}"/></form:option>
											        </c:if>
											    </c:forEach>
											</form:select>&nbsp;
											<bdweb:fieldHilight name="country" singleDisplay="true"  
												style="float:none" className="errorIcon" /></td>
									</tr>
									<tr>
										<td colspan="2" class="address makeDefaultSupress"
											id="defaultSupress"><form:checkbox
												id="makeDefaultAddressCheckBox"
												cssClass="shppingAddress"
												path="shippingVO.makeDefaultAddressSelected" 
												disabled ="${planReviewPrintForm.shippingVO.disbaleMakeDefaultAddressSelected}"/> &nbsp;
											Save this address as my shipping information for future plan
											review print orders</td>
									</tr>

								</table>

								<div align="left" styleId="ifRvpUser"
									style="padding-left: 15px; padding-top: 15px; color: #000; font: 12px verdana, arial, helvetica, sans-serif;">
									<b>Note: It's recommended you only save this if you send to
										this address often (i.e. your address).</b>
								</div>
								<div align="left" styleId="ifRvpUser"
									style="padding-left: 15px; padding-top: 15px; color: #000; font: 12px verdana, arial, helvetica, sans-serif;">
									Please allow approximately 3 business days for shipping. If you
									have any questions, please email jhplanreview@jhancock.com or
									call 1-877-346-8378.</div>

							</td>
						</tr>

					</table>
				</div>
			</DIV>

		</c:if>
		<!-- Block to display New shipping Address block as enabled -->

		<div class="nextButton" id="enabledBottomNextButton"
			style="padding-right: 20px; padding-bottom: 50px">
			<div align="left" style="padding-top: 15px; padding-left: 40px;">
				<input class="blue-btn cancel"
					onmouseover="this.className +=' btn-hover'"
					onmouseout="this.className='blue-btn next'" onclick="doCancel();"
					value="Cancel" type=button name="cancel"> <input
					class="blue-btn submit" onmouseover="this.className +=' btn-hover'"
					onmouseout="this.className='blue-btn next'" value="Submit"
					onclick="displayConfirm();" type=button name="submit">
			</div>
		</div>

</bd:form>
	</div>
</div>

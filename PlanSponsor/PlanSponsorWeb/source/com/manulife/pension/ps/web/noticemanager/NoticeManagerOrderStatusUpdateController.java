package com.manulife.pension.ps.web.noticemanager;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.event.Event;
import com.manulife.pension.event.NoticeManagerOrderStatusNotificationEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeMailingOrderVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.noticemanager.util.ComputeMD5Hash;
import com.manulife.pension.util.BaseEnvironment;

/**
 * Order Status Update provide by Merrill  application
 * @author krishta
 *
 */
@Controller
@RequestMapping( value ="/noticemanager")

public class NoticeManagerOrderStatusUpdateController extends PsAutoController {
	
	@ModelAttribute("noticeManagerOrderStatusUpdateForm") 
	public NoticeManagerOrderStatusUpdateForm populateForm()
	{
		return new NoticeManagerOrderStatusUpdateForm();
		}

	private static final Logger logger = Logger.getLogger(NoticeManagerOrderStatusUpdateController.class);


	private static SimpleDateFormat dateFormat = new SimpleDateFormat(	
			"MM-dd-yyyy", Locale.US);

		@RequestMapping(value ="/updatestatus/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault( @ModelAttribute("noticeManagerOrderStatusUpdateForm") NoticeManagerOrderStatusUpdateForm actionForm,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		logger.info("Merrill responded status for the order we raised from Build your page under NMC tool-->>Entry");
		if (logger.isDebugEnabled()) {
			logger.debug("Merrill responded status for the order!!!");
			logger.debug("requested URI:" + request.getRequestURI());
			if (Constants.DUMP_PARAMETERS) {
				logger.debug("Merrill responded status for the order-Dumping parameters:");
				Enumeration parameters = request.getParameterNames();
				while (parameters.hasMoreElements()) {
					String parameter = (String) parameters.nextElement();
					String[] values = request.getParameterValues(parameter);
					if (values == null) {
						logger.debug("Merrill responded status for the order -Parameter [" + parameter
								+ "] does not exist.");

					} else {
						StringBuffer valuesBuffer = new StringBuffer();
						for (int i = 0; i < values.length; i++) {
							valuesBuffer.append(values[i]);
							if (i != values.length - 1) {
								valuesBuffer.append(", ");
							}
						}
						logger.debug("Parameter [" + parameter + "] is ["
								+ valuesBuffer + "]");
					}
				}
			}
		}
		NoticeManagerOrderStatusUpdateForm orderStatusUpdateForm =  (NoticeManagerOrderStatusUpdateForm)actionForm;
		populateNoticeManagerOrderStatusUpdate(
				orderStatusUpdateForm, request);
		List<String> errors = new ArrayList<String>();
		boolean updatedStatusInd = false;

		String orderStatus = orderStatusUpdateForm.getStatus();
		String trackingNumber = orderStatusUpdateForm.getTrackingNumber();
		if(StringUtils.isBlank(orderStatus)){
				errors.add("Failed to get orderStatus");
		}else if(StringUtils.isBlank(trackingNumber)){
				errors.add("Failed to get trackingNumber");
		}else{
			
			PlanNoticeDocumentServiceDelegate planNoticeDocumentService = PlanNoticeDocumentServiceDelegate.getInstance();
			String orderNumber = orderStatusUpdateForm.getOrderNumber();
			String pageCount = orderStatusUpdateForm.getPageCount();
			String participantCount = orderStatusUpdateForm.getParticipantCount();
			String colorPrintInd = orderStatusUpdateForm.getColorInd();
			String mailDate = orderStatusUpdateForm.getMailDate();
			String vipInd = orderStatusUpdateForm.getVIP();
			String totalCost = orderStatusUpdateForm.getTotalCost();
			String orderStapledInd = orderStatusUpdateForm.getOrderStapledInd();
			String bulkOrderInd = orderStatusUpdateForm.getBulkOrderInd();
			String largeEnvelopeInd = orderStatusUpdateForm.getLargeEnvelopeInd();
			String orderSealedInd = orderStatusUpdateForm.getOrderSealedInd();
			String md5Hash = orderStatusUpdateForm.getMd5Hash();
			PlanNoticeMailingOrderVO contractNoticeMailingOrder =  new PlanNoticeMailingOrderVO();
			if (logger.isDebugEnabled()) {
				logger.debug("Merrill responded orderNumber :"+orderNumber);
				logger.debug("Merrill responded pageCount :"+pageCount);
				logger.debug("Merrill responded participantCount :"+participantCount);
				logger.debug("Merrill responded colorPrintInd :"+colorPrintInd);
				logger.debug("Merrill responded mailDate :"+mailDate);

				logger.debug("Merrill responded VIP :"+vipInd);
				logger.debug("Merrill responded totalCost :"+totalCost);
				logger.debug("Merrill responded orderStapledInd :"+orderStapledInd);
				logger.debug("Merrill responded bulkOrderInd :"+bulkOrderInd);
				logger.debug("Merrill responded largeEnvelopeInd :"+largeEnvelopeInd);
				logger.debug("Merrill responded orderSealedInd :"+orderSealedInd);
				logger.debug("Merrill responded Md5Hash :"+md5Hash);
			}
			try {
				String generatedMD5Hash = new ComputeMD5Hash().createHash(trackingNumber);
				if (logger.isDebugEnabled()) {
					logger.debug("JH generated MD5 hash code :"+generatedMD5Hash);
				}
				if(!generatedMD5Hash.equalsIgnoreCase(md5Hash)){
					errors.add("Merrill request order failed with its MD5 hash Check "+trackingNumber);
				} else{
					Integer orderTrackingNo = Integer.parseInt(trackingNumber);
					
					try {
						contractNoticeMailingOrder  = planNoticeDocumentService.getContractNoticeMailingOrder(orderTrackingNo);
					} catch (Exception e) {
						errors.add("Order trackinng number doesn't exist");
						logger.error("order Tracking No doesn't exist",e);
					}
					if( errors.isEmpty() && (contractNoticeMailingOrder == null || contractNoticeMailingOrder.getOrderNumber() == null)){
						errors.add("Failed to get trackingNumber " + trackingNumber);
						logger.error("tracking number recieved from Merill " + trackingNumber + " is not available in out database");
					}else if( errors.isEmpty()){
						contractNoticeMailingOrder.setOrderNumber(orderTrackingNo);
						if(StringUtils.equalsIgnoreCase(Constants.ORDER_NOT_COMPLETED_STATUS, orderStatus) ){
							contractNoticeMailingOrder.setOrderStatusCode("IC");
						}else if(StringUtils.equalsIgnoreCase(Constants.ORDER_INPROGRESS_STATUS, orderStatus)){
							contractNoticeMailingOrder.setOrderStatusCode("IP");
							if(StringUtils.isBlank(colorPrintInd)){
								errors.add("Failed to get Color Print Indicator");
							}
							contractNoticeMailingOrder.setColorPrintInd(colorPrintInd);
							if(StringUtils.isBlank(participantCount)){
								errors.add("Failed to get Participant Count");
							}
							int noOfParticipant = Integer.parseInt(participantCount);
							contractNoticeMailingOrder.setNoOfParticipant(noOfParticipant);
							if(StringUtils.isBlank(pageCount)){
								errors.add("Failed to get Page Count");
							}
							int totalPageCount = Integer.parseInt(pageCount);
							contractNoticeMailingOrder.setTotalPageCount(totalPageCount);
							if(StringUtils.isBlank(vipInd)){
								errors.add("Failed to get VIP Indicator");
							}
							contractNoticeMailingOrder.setVipInd(vipInd);
							
							if(StringUtils.isBlank(orderStapledInd)){
								errors.add("Failed to get orderStapled Indicator");
							}
							contractNoticeMailingOrder.setOrderStapledInd(orderStapledInd);
							
							if(StringUtils.isBlank(orderSealedInd)){
								errors.add("Failed to get orderSealed Indicator");
							}
							contractNoticeMailingOrder.setOrderSealedInd(orderSealedInd);
							
							if(StringUtils.isBlank(largeEnvelopeInd)){
								errors.add("Failed to get largeEnvelope Indicator");
							}
							contractNoticeMailingOrder.setLargeEnvelopeInd(largeEnvelopeInd);
							
							if(StringUtils.isBlank(bulkOrderInd)){
								errors.add("Failed to get bulkOrder Indicator");
							}
							contractNoticeMailingOrder.setBulkOrderInd(bulkOrderInd);
							Integer merrillOrderNumber = Integer.parseInt(orderNumber);
							contractNoticeMailingOrder.setMerrilOrderNumber(merrillOrderNumber);
	
						}else if(StringUtils.equalsIgnoreCase(Constants.ORDER_CANCELLED_STATUS, orderStatus)){
							contractNoticeMailingOrder.setOrderStatusCode("CN");
						}else if(StringUtils.equalsIgnoreCase(Constants.ORDER_COMPLETED_STATUS, orderStatus)){
							contractNoticeMailingOrder.setOrderStatusCode("CM");
							if(StringUtils.isBlank(totalCost)){
								errors.add("Failed to get total Cost");
							}
							BigDecimal totalMailingCost = new BigDecimal(totalCost);
							contractNoticeMailingOrder.setTotalMailingCost(totalMailingCost);
							if(StringUtils.isBlank(mailDate)){
								errors.add("Failed to get Mail date");
							}
							setOrderMailedDate(contractNoticeMailingOrder, mailDate,errors);
						}
					}

				}
			} catch (NoSuchAlgorithmException e1) {
				errors.add("Problem in generating MD5 hash");
				logger.error("Problem in generating MD5 hash",e1);
			}
			
			
			if(errors.isEmpty()){
				try {
					updatedStatusInd = planNoticeDocumentService.updateContractNoticeMailingOrder(contractNoticeMailingOrder);
				} catch (Exception e) {
					logger.error(
							"NoticeManagerOrderStatusUpdate: Failed to update order details ",
							 e);
				}
				if(!updatedStatusInd){
					response.sendError( response.SC_NOT_ACCEPTABLE,"Failure to update order status");
					response.setStatus(response.SC_NOT_ACCEPTABLE);
				}else if(StringUtils.equalsIgnoreCase(Constants.ORDER_INPROGRESS_STATUS, orderStatus)){
					try {

						triggerNoticeOrderStatusNotification(contractNoticeMailingOrder);
					} catch (SystemException e) {
						logger.error(
								"NoticeManagerOrderStatusUpdate: Failed to trigger event for order status Updated with IP ",
								e);
					} catch (Exception e) {
						logger.error(
								"NoticeManagerOrderStatusUpdate: Failed to trigger event for order status Updated with IP ",
								 e);
					}
					response.setStatus(response.SC_OK);
				}
			}
			else {
				
				logger.error("Merrill responded orderNumber :"+orderNumber);
				logger.error("Merrill responded pageCount :"+pageCount);
				logger.error("Merrill responded participantCount :"+participantCount);
				logger.error("Merrill responded colorPrintInd :"+colorPrintInd);
				logger.error("Merrill responded mailDate :"+mailDate);

				logger.error("Merrill responded VIP :"+vipInd);
				logger.error("Merrill responded totalCost :"+totalCost);
				logger.error("Merrill responded orderStapledInd :"+orderStapledInd);
				logger.error("Merrill responded bulkOrderInd :"+bulkOrderInd);
				logger.error("Merrill responded largeEnvelopeInd :"+largeEnvelopeInd);
				logger.error("Merrill responded orderSealedInd :"+orderSealedInd);
				logger.error("Merrill responded Md5Hash :"+md5Hash);
			}
		}
		
		
		if(!errors.isEmpty()){
			StringBuffer  errorMessage = new StringBuffer();
			for(String error:errors){
				errorMessage.append(error);
				errorMessage.append(";");
			}
			response.sendError( response.SC_EXPECTATION_FAILED,errorMessage.toString());
			response.setStatus(response.SC_EXPECTATION_FAILED);
		}
		logger.info("Merrill responded status for the order we raised from Build your page under NMC tool-->>Exit");
		return null;
	}
	

	private void populateNoticeManagerOrderStatusUpdate(
			NoticeManagerOrderStatusUpdateForm orderStatusUpdateForm, HttpServletRequest request) {
		Map<String,String> OrderStatusUpdate  = new HashMap<String,String>();
		Enumeration parameters = request.getParameterNames();
		while (parameters.hasMoreElements()) {
			String parameter = (String) parameters.nextElement();
			String[] values = request.getParameterValues(parameter);
			StringBuffer valuesBuffer = new StringBuffer();
			if (values == null) {
				logger.info("Merrill responded status for the order -Parameter [" + parameter
						+ "] does not exist.");

			} else {

				for (int i = 0; i < values.length; i++) {
					valuesBuffer.append(values[i]);
					if (i != values.length - 1) {
						valuesBuffer.append(", ");
					}
				}
				logger.debug("Parameter [" + parameter + "] is ["
						+ valuesBuffer + "]");
			}
			OrderStatusUpdate.put(parameter.replaceAll("=", "").trim(), valuesBuffer.toString());
		}
		
		orderStatusUpdateForm.setStatus( OrderStatusUpdate.get(Constants.STATUS));
		orderStatusUpdateForm.setTrackingNumber( OrderStatusUpdate.get(Constants.TRACKING_NUMBER));
		orderStatusUpdateForm.setOrderNumber( OrderStatusUpdate.get(Constants.ORDER_NUMBER));
		orderStatusUpdateForm.setPageCount( OrderStatusUpdate.get(Constants.PAGE_COUNT));
		orderStatusUpdateForm.setParticipantCount( OrderStatusUpdate.get(Constants.PARTICIPANT_COUNT));
		orderStatusUpdateForm.setColorInd( OrderStatusUpdate.get(Constants.COLOR_IND));
		orderStatusUpdateForm.setMailDate( OrderStatusUpdate.get(Constants.MAIL_DATE));
		orderStatusUpdateForm.setVIP( OrderStatusUpdate.get(Constants.VIP));
		orderStatusUpdateForm.setTotalCost( OrderStatusUpdate.get(Constants.TOTAL_COST));
		orderStatusUpdateForm.setOrderStapledInd( OrderStatusUpdate.get(Constants.ORDER_STAPLED_IND));
		orderStatusUpdateForm.setBulkOrderInd( OrderStatusUpdate.get(Constants.BULK_ORDER_IND));
		orderStatusUpdateForm.setLargeEnvelopeInd( OrderStatusUpdate.get(Constants.LARGE_ENVELOPE_IND));
		orderStatusUpdateForm.setOrderSealedInd( OrderStatusUpdate.get(Constants.ORDER_SEALED_IND));
		orderStatusUpdateForm.setMd5Hash( OrderStatusUpdate.get(Constants.MD5_HASH));
	}

	/**
	 * 
	 * @param contractNoticeMailingOrderVO
	 * @param mailedDate
	 */
	private void setOrderMailedDate(PlanNoticeMailingOrderVO contractNoticeMailingOrderVO,
			String mailedDate, List<String> errors) {
		try {
			synchronized (dateFormat) {
				contractNoticeMailingOrderVO.setOrderStatusDate(dateFormat.parse(mailedDate));	
			}
		} catch (ParseException e) {
			errors.add("Failed to Parse the mailed date passed by Merril");
			logger.error(
                    "NoticeManagerOrderStatusUpdate: Failed to parse the mailed date",
                    e);
		}
	}
	
	/**
	 * When the Order Status of a Notice Manager Order is updated to “In Progress”, this Event 218 will be triggered 
	 * @param contractNoticeMailingOrder
	 * @param termOfUseInd
	 * @throws SystemException
	 */
	public void triggerNoticeOrderStatusNotification(PlanNoticeMailingOrderVO contractNoticeMailingOrder )
					throws SystemException {
		logger.info("Event trigged for Notice Order status Update-->Entry");
		NoticeManagerOrderStatusNotificationEvent event = new NoticeManagerOrderStatusNotificationEvent();
		event.setInitiator(Event.SYSTEM_USER_PROFILE_ID);
		event.setContractId(contractNoticeMailingOrder.getContractId());
		event.setMailingName(contractNoticeMailingOrder.getMailingName());
		event.setOrderNumber(contractNoticeMailingOrder.getMerrilOrderNumber());
		event.setTermOfUseAccepted("Y");
		event.setProfileId(contractNoticeMailingOrder.getProfileId().longValue());
		// trigger the event
		EventClientUtility.getInstance(
				new BaseEnvironment().getAppId()).prepareAndSendJMSMessage(event); 
		logger.info("Event trigged for Notice Order status Update-->Exist");
	}
}

package com.manulife.pension.platform.web.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.delegate.EmailProcessingServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUShareDocumentsEmailNotificationVO;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUShareInformationVO;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmissionMetaDataVO;
import com.manulife.pension.ps.service.withdrawal.email.EmailGeneratorException;
import com.manulife.pension.service.notification.util.BrowseServiceHelper;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.StringUtility;
import com.manulife.pension.util.email.BodyPart;
import com.manulife.pension.util.email.EmailMessageException;
import com.manulife.pension.util.email.EmailMessageVO;

public class SDUEmailNotificationHelper {

	private static SDUEmailNotificationHelper instance = new SDUEmailNotificationHelper();

	public static SDUEmailNotificationHelper getInstance() {
		return instance;
	}
	protected static Logger logger;
	private static int psContentId = 96981;
	private static int bdContentId = 96982;	
	public static final String FROM_EMAIL_ADDRESS = "fromEmailAddressForSDU";
	public static final String HOST_URL_NAME = "siteDomain";
	public static final String SITE_PROTOCOL_NAME = "siteProtocol";

	protected static InternetAddress fromEmailAddress = null;
	protected String hostURL = null;
	protected String siteProtocol = null;
	static {
		logger = Logger.getLogger(SDUEmailNotificationHelper.class);
		try {
			// get from email address from Namespace bindings, SDU has same email address for 3 sites (default property in ps)
			BaseEnvironment environment = new BaseEnvironment();
			fromEmailAddress = new InternetAddress(environment.getNamingVariable(FROM_EMAIL_ADDRESS, "bd"));
		} catch (Exception e) {
			SystemException se = new SystemException(e, "Static block failed for ");
			throw ExceptionHandlerUtility.wrap(se);
		}
	}

	protected int getContentId(String siteDomain) {
		return StringUtils.equalsIgnoreCase("ps", siteDomain)?psContentId:bdContentId;		
	}
	
	/**
	 * Get the dynamic login URL to be replaced in email content
	 * @param siteDomain
	 * @return
	 * @throws SystemException
	 */
	protected String getUrl(String siteInfo) throws SystemException {
		try {
			// get SMTP server property
			BaseEnvironment environment = new BaseEnvironment();

			hostURL = environment.getNamingVariable(HOST_URL_NAME, siteInfo);
			siteProtocol = environment.getNamingVariable(SITE_PROTOCOL_NAME, null);
		} catch (Exception e) {
			SystemException se = new SystemException(e, "Static block failed for ");
			throw ExceptionHandlerUtility.wrap(se);
		}
		StringBuffer url = new StringBuffer(siteProtocol + "://" + hostURL);
		return url.toString();
	}

	/**
	 * build email content from CMA and send individual email to the users who are selected to be notified via email.
	 * @param sduSubmissionMetaDataVO
	 * @param siteDomain
	 * @return
	 * @throws EmailMessageException
	 * @throws SystemException
	 * @throws EmailGeneratorException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public List<SDUShareDocumentsEmailNotificationVO> sendEmail(String submissionId, SDUSubmissionMetaDataVO sduSubmissionMetaDataVO, String siteDomain, Location siteLocation)
			throws EmailMessageException, SystemException, EmailGeneratorException, JsonParseException, JsonMappingException, IOException {

		
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String shareInfoJson = sduSubmissionMetaDataVO.getShareInfo();
		
		List<SDUShareDocumentsEmailNotificationVO> sduShareDocumentsEmailNotificationVOList=null;
		List<SDUShareInformationVO> sduShareInformationVOList = mapper.readValue(shareInfoJson,
				new TypeReference<ArrayList<SDUShareInformationVO>>() {
				});

		if (null != sduShareInformationVOList && !sduShareInformationVOList.isEmpty()) {
			sduShareDocumentsEmailNotificationVOList = new ArrayList<SDUShareDocumentsEmailNotificationVO>();
			
			Miscellaneous content = getEmailContent(getContentId(siteDomain), siteLocation);
			
			String siteInfo = "";
			if(siteDomain.equalsIgnoreCase("ps")) {
				siteInfo = siteLocation==Location.USA?"usa":"ny";
			}else {
				siteInfo = siteDomain;
			}
			
			String siteURL = getUrl(siteInfo);
			String emailSubject = content.getTitle();
			Object subjectParams[] = new Object[] { sduSubmissionMetaDataVO.getClientUserName(),
					sduSubmissionMetaDataVO.getClientContract()};
			emailSubject = StringUtility.substituteParams(emailSubject, subjectParams);
			
			for (SDUShareInformationVO sduShareInformationVO : sduShareInformationVOList) {
				if (null != sduShareInformationVO && sduShareInformationVO.isSendEmail()) {

					EmailProcessingServiceDelegate processingDelegate = EmailProcessingServiceDelegate
							.getInstance(null);
					EmailMessageVO psMessage;
					try {
					Address[] emailRecipients = {new InternetAddress(sduShareInformationVO.getSharedWithEmailAddress())};
					Object bodyParams[] = new Object[] { sduShareInformationVO.getSharedWithUserName(),
							sduSubmissionMetaDataVO.getClientContract(), siteURL };
					String bodyText = StringUtility.substituteParams(content.getText(), bodyParams);

					
						ArrayList<BodyPart> bodyParts = new ArrayList<BodyPart>();

						bodyParts.add(new BodyPart("body", true, BodyPart.HTML_CONTENT, bodyText, null));

						psMessage = new EmailMessageVO(null, fromEmailAddress, null, emailRecipients, null, null, emailSubject,
								bodyParts);

					} catch (EmailMessageException emailMessageException) {
						logger.error(emailMessageException);
						throw new EmailGeneratorException("Failed to send notification", emailMessageException);
					} catch (AddressException addressException) {
						logger.error(addressException);
						throw new EmailGeneratorException("To/From email address is not valid", addressException);
					}
					try {
						processingDelegate.sendAndReceiveConfirmation(psMessage);
					} catch (SystemException systemException) {
						logger.error(systemException);
						throw new EmailGeneratorException(
								"System Exception trying to generate send notification message", systemException);
					}
					SDUShareDocumentsEmailNotificationVO sduShareDocumentsEmailNotificationVO = new SDUShareDocumentsEmailNotificationVO();
					sduShareDocumentsEmailNotificationVO.setSubmissionId(Long.parseLong(submissionId));
					sduShareDocumentsEmailNotificationVO.setSharedWithUserId(sduShareInformationVO.getSharedWithUserId());
					sduShareDocumentsEmailNotificationVO.setSharedWithEmailAddress(sduShareInformationVO.getSharedWithEmailAddress());
					sduShareDocumentsEmailNotificationVO.setSendEmail(sduShareInformationVO.isSendEmail());
					sduShareDocumentsEmailNotificationVO.setEmailSentTs(new Timestamp(System.currentTimeMillis()));
					sduShareDocumentsEmailNotificationVOList.add(sduShareDocumentsEmailNotificationVO);
				}
			}
		}
		return sduShareDocumentsEmailNotificationVOList;
	}

	protected Miscellaneous getEmailContent(int contentId, Location location) throws SystemException {
		BrowseServiceHelper bsh = BrowseServiceHelper.getInstance();
		ContentType miscellaneous = ContentTypeManager.instance().MISCELLANEOUS;
		Miscellaneous content = (Miscellaneous) bsh.getContentById(contentId, miscellaneous, location);
		content.setText(content.getText());
		return content;
	}
}

package com.manulife.pension.platform.web.secureDocumentUpload;

import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.PlanSponsorUser;

public final class SDUConstants implements com.manulife.pension.platform.web.CommonConstants {
	
	//Send Document Upload
	public static final String APIGEE_OAUTH_TOKEN_URL="apigee.oauth.token.url";
	public static final String APIGEE_EDGE_PROXY_URL="apigee.edge.proxy.url";
	public static final String FILE_UPLOAD_WIDGET_USER_ID="file.upload.widjet.username";
	public static final String FILE_UPLOAD_WIDGET_PASSWORD="file.upload.widjet.password";
	public static final String SDU_PS_CLIENT_ID = "rpspswsduext";
	public static final String SDU_BD_CLIENT_ID = "rpsbdwsduext";
	public static final String SDU_SUBMISSION_HISTORY_ENDPOINT = "/submissionHistory";
	public static final String SDU_SHARE_HISTORY_ENDPOINT = "/shareHistory";
	public static final String SDU_WIDGET_DROPZONE_ENDPOINT = "/widget/dropZone";
	public static final String SDU_DOWNLOAD_ENDPOINT = "/download";
	public static final String SDU_DELETE_ENDPOINT = "/deleteDocument";	
	public static final String SDU_UPDATE_EMAIL_NOTIFCATION_INFO = "/updateEmailNotificationInfo";
	public static final String INC_ROLE = IntermediaryContact.ID;
	public static final String PPY_ROLE = PayrollAdministrator.ID;
	public static final String PSU_ROLE = PlanSponsorUser.ID;
	public static final String TPA_ROLE = "TPA";
	public static final String SDU_PS_SUBMISSION_SOURCE = "SDUPSW";
	public static final String SDU_BD_SUBMISSION_SOURCE = "SDUFRW";
	public static final String SDU_PS_CLIENT_APP_NAME = "PSW";
	public static final String SDU_BD_CLIENT_APP_NAME = "FRW";
	//These 2 constants are used in contactsDAO, don't forget to update the constants in contactsDAO.
	public static final String SDU_PSW = "PSW";
	public static final String SDU_BDW = "BDW";
}

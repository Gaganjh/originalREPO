package com.manulife.pension.service.distribution.valueobject;

import java.sql.Timestamp;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class ManagedContent extends BaseSerializableCloneableObject {

	// PlanSponsor WebSite (PSW)
	public static final String CMA_SITE_CODE_PSW = "PS";

	// Participant website (EZk)
	public static final String CMA_SITE_CODE_PW = "PA";

	public static final String TRUTH_IN_LENDING_NOTICE_PDF = "LTL";

	public static final String TRUTH_IN_LENDING_NOTICE_HTML = "LTH";

	public static final String PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_PDF = "LPR";

	public static final String PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_HTML = "LPH";

	public static final String LOAN_PARTICIPANT_AUTHORIZATION = "LPP";

	public static final String LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_YES = "LA1";

	public static final String LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_NO = "LA2";

	public static final String LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_NULL = "LA3";

	public static final String LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_YES = "LA4";

	public static final String LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_NO = "LA5";

	public static final String LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_NULL = "LA6";

	public static final String LOAN_APPROVAL_GENERIC = "LAP";

	public static final String AMORTIZATION_SCHEDULE_PDF = "LAM";

	public static final String AMORTIZATION_SCHEDULE_HTML = "LAH";

	public static final String AT_RISK_TEXT = "LAR";
	
	public static final String LOAN_FORM_PDF = "LFM";

	public static final String LOAN_PACKAGE_INSTRUCTIONS_FOR_ADMIN = "LIA";

	public static final String LOAN_PACKAGE_INSTRUCTIONS_FOR_PARTICIPANT = "LIP";

	public static final String LOAN_INSTRUCTIONS_PDF = "LIF";

	private static final long serialVersionUID = 1L;
	private Integer contentKey;
	private Integer contentId;
	private String cmaSiteCode;
	private String contentTypeCode;
	private Integer createdById;
	private Timestamp created;

	public String getContentTypeCode() {
		return contentTypeCode;
	}

	public void setContentTypeCode(String contentTypeCode) {
		this.contentTypeCode = contentTypeCode;
	}

	public Integer getContentKey() {
		return contentKey;
	}

	public void setContentKey(Integer contentKey) {
		this.contentKey = contentKey;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public String getCmaSiteCode() {
		return cmaSiteCode;
	}

	public void setCmaSiteCode(String cmaSiteCode) {
		this.cmaSiteCode = cmaSiteCode;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

}

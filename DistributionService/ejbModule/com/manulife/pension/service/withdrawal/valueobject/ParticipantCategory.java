package com.manulife.pension.service.withdrawal.valueobject;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class ParticipantCategory extends BaseSerializableCloneableObject {
    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRestrictedCategory() {
		return RestrictedCategory;
	}
	public void setRestrictedCategory(String restrictedCategory) {
		RestrictedCategory = restrictedCategory;
	}
	private String description;
	private String RestrictedCategory;

}

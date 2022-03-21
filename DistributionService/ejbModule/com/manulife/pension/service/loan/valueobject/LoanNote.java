package com.manulife.pension.service.loan.valueobject;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.distribution.valueobject.Note;

public class LoanNote extends BaseSerializableCloneableObject implements Note {

	private static final long serialVersionUID = 1L;

	private Integer createdById;
	private Integer lastUpdatedById;
	private Timestamp created;
	private Timestamp lastUpdated;	
	private String noteTypeCode;
	private String note;
	
	public Integer getCreatedById() {
		return createdById;
	}
	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}
	public Integer getLastUpdatedById() {
		return lastUpdatedById;
	}
	public void setLastUpdatedById(Integer lastUpdatedById) {
		this.lastUpdatedById = lastUpdatedById;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public Timestamp getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getNoteTypeCode() {
		return noteTypeCode;
	}
	public void setNoteTypeCode(String noteTypeCode) {
		this.noteTypeCode = noteTypeCode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isBlank() {
		return StringUtils.isBlank(note);
	}

	/**
     * @return Returns the note in display format (escaped for HTML and JavaScript).
     */
    public String getNoteForDisplay() {
        return com.manulife.pension.util.StringUtils.escapeHtmlAndFormFeeds(note);
    }

}

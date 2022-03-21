package com.manulife.pension.platform.web.taglib;

import java.io.Serializable;
import java.sql.Timestamp;

public class ChangeHistoryVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Timestamp currentUpdatedTs;
	
	private Timestamp previousUpdatedTs;
	
	private String sourceChannelCode;
	
	private String previousSourceChannelCode;
	
	private ChangeUserInfo currentUser;
	
	private ChangeUserInfo previousUser;

    public class ChangeUserInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private String userId;
		private String userIdType;
		private String lastName;
		private String firstName;
	
		public ChangeUserInfo() {
		}
		
		public ChangeUserInfo(String userId, String userIdType, String lastName, String firstName) 
		{
			this.userId = userId;
			this.userIdType = userIdType;
			this.lastName = lastName;
			this.firstName = firstName;
		}
	
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getUserIdType() {
			return userIdType;
		}
		public void setUserIdType(String userIdType) {
			this.userIdType = userIdType;
		}		
	}
    
	public Timestamp getCurrentUpdatedTs() {
		return currentUpdatedTs;
	}

	public void setCurrentUpdatedTs(Timestamp currentUpdatedTs) {
		this.currentUpdatedTs = currentUpdatedTs;
	}

	public Timestamp getPreviousUpdatedTs() {
		return previousUpdatedTs;
	}

	public void setPreviousUpdatedTs(Timestamp previousUpdatedTs) {
		this.previousUpdatedTs = previousUpdatedTs;
	}

	public String getSourceChannelCode() {
		return sourceChannelCode;
	}

	public void setSourceChannelCode(String sourceChannelCode) {
		this.sourceChannelCode = sourceChannelCode;
	}

	public String getPreviousSourceChannelCode() {
		return previousSourceChannelCode;
	}

	public void setPreviousSourceChannelCode(String previousSourceChannelCode) {
		this.previousSourceChannelCode = previousSourceChannelCode;
	}

	public ChangeUserInfo getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(ChangeUserInfo currentUser) {
		this.currentUser = currentUser;
	}

	public ChangeUserInfo getPreviousUser() {
		return previousUser;
	}

	public void setPreviousUser(ChangeUserInfo previousUser) {
		this.previousUser = previousUser;
	}
}

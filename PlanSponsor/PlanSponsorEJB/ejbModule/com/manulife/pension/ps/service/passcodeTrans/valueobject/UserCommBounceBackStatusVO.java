package com.manulife.pension.ps.service.passcodeTrans.valueobject;

import java.io.Serializable;

public class UserCommBounceBackStatusVO implements Serializable {

		private static final long serialVersionUID = 1L;
		private String sId;
		private String commType;
		private int noOfAttempts;
		private String userProfileId;
		
		public String getUserProfileId() {
			return userProfileId;
		}
		public void setUserProfileId(String userProfileId) {
			this.userProfileId = userProfileId;
		}
		public String getsId() {
			return sId;
		}
		public void setsId(String sId) {
			this.sId = sId;
		}
		public String getCommType() {
			return commType;
		}
		public void setCommType(String commType) {
			this.commType = commType;
		}
		public int getNoOfAttempts() {
			return noOfAttempts;
		}
		public void setNoOfAttempts(int noOfAttempts) {
			this.noOfAttempts = noOfAttempts;
		}
}

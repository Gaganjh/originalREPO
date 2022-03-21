package com.manulife.pension.ps.administration;
/*
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


import java.util.StringTokenizer;

/**
 * @author antalro
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class User {



		private String userName= "";
		private String contractNumber= "";
		private String contractName= "";
		private String contractLocation ="";
		private String roleName= "NoAccess";
		private String email= "";;
		private String updatedByFirstName= "";
		private String updatedByLastName= "";
		private String timeStamp= "";
		private String firstName= "";
		private String lastName= "";
		private String profileId = "";



		/**
		 * @return Returns the contractLocation.
		 */
		public String getContractLocation() {
			return contractLocation;
		}
		/**
		 * @param contractLocation The contractLocation to set.
		 */
		public void setContractLocation(String contractLocation) {
			this.contractLocation = contractLocation;
		}
		/**
		 * @return Returns the profileId.
		 */
		public String getProfileId() {
			return profileId;
		}
		/**
		 * @param profileId The profileId to set.
		 */
		public void setProfileId(String profileId) {
			this.profileId = profileId;
		}
		/**
		 * @return Returns the roleName.
		 */
		public String getRoleName() {
			return roleName;
		}
		/**
		 * @param roleName The roleName to set.
		 */
		public void setRoleName(String roleName) {
			if(roleName != null)
			{
			StringTokenizer tokenizer = new StringTokenizer(roleName,"=");
			tokenizer.nextToken();
			if(tokenizer.hasMoreTokens())
			{
				String tmp = tokenizer.nextToken();
				if(tmp.trim().equalsIgnoreCase("NA"))
				{
					this.roleName = "NoAccess";
				}
				else if(tmp.trim().equalsIgnoreCase("PSEUM"))
				{
					this.roleName= "Plan Sponsor User Manager";
				}
				else if(tmp.trim().equalsIgnoreCase("PYA"))
				{
					this.roleName = "Payroll Administrator";
				}
				else if(tmp.trim().equalsIgnoreCase("PA"))
				{
					this.roleName = "Plan Adminstrator";
				}
				else
				{
					this.roleName = "NoAccess";
				}
			}
			}
		}
	
		public void setRoleNameNoParsing(String roleName)
		{
			this.roleName = roleName;
		}
		/**
		 * @return Returns the timeStamp.
		 */
		public String getTimeStamp() {
			return timeStamp;
		}
		/**
		 * @param timeStamp The timeStamp to set.
		 */
		public void setTimeStamp(String timeStamp) {

			StringTokenizer tokenizer = new StringTokenizer(timeStamp,":");
			//ignore the first one
			tokenizer.nextToken();
			StringBuffer tmp = new StringBuffer();
			while(tokenizer.hasMoreTokens())
			{
				tmp.append(tokenizer.nextToken());
			}
			this.timeStamp = tmp.toString();
		}
		/**
		 * @return Returns the updatedByFirstName.
		 */
		public String getUpdatedByFirstName() {
			return updatedByFirstName;
		}
		/**
		 * @param updatedByFirstName The updatedByFirstName to set.
		 */
		public void setUpdatedByFirstName(String updatedByFirstName) {
			StringTokenizer tokenizer = new StringTokenizer(updatedByFirstName,":");
			tokenizer.nextToken();
			if(tokenizer.hasMoreTokens())
			{
				this.updatedByFirstName = tokenizer.nextToken();
			}
		}
		/**
		 * @return Returns the updatedByLastName.
		 */
		public String getUpdatedByLastName() {
			return updatedByLastName;
		}
		/**
		 * @param updatedByLastName The updatedByLastName to set.
		 */
		public void setUpdatedByLastName(String updatedByLastName) {
			StringTokenizer tokenizer = new StringTokenizer(updatedByLastName,":");
			tokenizer.nextToken();
			if(tokenizer.hasMoreTokens())
			{
				this.updatedByLastName = tokenizer.nextToken();
			}
		}
		/**
		 * @return Returns the userName.
		 */
		public String getUserName() {
			return userName;
		}
		/**
		 * @param userName The userName to set.
		 */
		public void setUserName(String userName) {
			StringTokenizer tokenizer = new StringTokenizer(userName,":");
			tokenizer.nextToken();
			if(tokenizer.hasMoreTokens())
			{
				this.userName = tokenizer.nextToken();
			}
			// clean up if there are "''" in the username
			int specialCharsIndex = this.userName.indexOf("''"); 
			if(specialCharsIndex != -1)
			{
				StringBuffer tmp = new StringBuffer(this.userName);
				this.userName = tmp.substring(0,specialCharsIndex)+tmp.substring(specialCharsIndex+1,this.userName.length());
			}
		}
		/**
		 * @return Returns the contractName.
		 */
		public String getContractName() {
			return contractName;
		}
		/**
		 * @param contractName The contractName to set.
		 */
		public void setContractName(String contractName) {
			if(contractName != null)
			{
				StringTokenizer tokenizer = new StringTokenizer(contractName,"=");
				tokenizer.nextToken();
				if(tokenizer.hasMoreTokens())
				{
					this.contractName = tokenizer.nextToken();
				}
			}
		}
		/**
		 * @return Returns the contractNumber.
		 */
		public String getContractNumber() {
			return contractNumber;
		}
		/**
		 * @param contractNumber The contractNumber to set.
		 */
		public void setContractNumber(String contractNumber) {
			if(contractNumber != null)
			{
				StringTokenizer tokenizer = new StringTokenizer(contractNumber,"=");
				tokenizer.nextToken();
				if(tokenizer.hasMoreTokens()) tokenizer.nextToken();
				if(tokenizer.hasMoreTokens()) this.contractNumber = tokenizer.nextToken();
			}
		}


		/**
		 * @return Returns the email.
		 */
		public String getEmail() {
			return email;
		}
		/**
		 * @param email The email to set.
		 */
		public void setEmail(String email) {
			System.out.println("About to parse="+email);
			if(email != null)
			{
				StringTokenizer tokenizer = new StringTokenizer(email,":");
				tokenizer.nextToken();
				if(tokenizer.hasMoreTokens())
				{
					this.email = tokenizer.nextToken();
				}
			}
		}
		
		/**
		 * @return Returns the firstName.
		 */
		public String getFirstName() {
			return firstName;
		}
		/**
		 * @param firstName The firstName to set.
		 */
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		/**
		 * @return Returns the lastName.
		 */
		public String getLastName() {
			return lastName;
		}
		/**
		 * @param lastName The lastName to set.
		 */
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		public void setEmailNoParsing(String email)
		{
			this.email = email;
		}
		
		public void setContractNumberNoParsing(String contractNumber)
		{
			this.contractNumber = contractNumber;
		}
		public void setProfileIdNoParsing(String profileId)
		{
			this.profileId = profileId;
		}
		public void setUserNameNoParsing(String userName)
		{
			this.userName = userName;
		}
		public String toString()
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append(userName).append(",").append(firstName).append(",").append(lastName).append(",").append(contractNumber).append(",").append(contractName).append(",").append(roleName).append(",").append(email).append(",").append(updatedByFirstName).append(",").append(updatedByLastName).append(",").append(timeStamp).append("\n");
			return buffer.toString();
		}
}

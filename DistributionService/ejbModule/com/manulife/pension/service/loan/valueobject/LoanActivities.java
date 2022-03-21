package com.manulife.pension.service.loan.valueobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.withdrawal.valueobject.UserName;

public class LoanActivities {

	private String submissionId;

	private Integer contractId;

	private Integer participantUserProfileId;

	private String participantFirstName;

	private String participantLastName;

	private List<? extends ActivityDetail> detailsSystemOfRecords = new ArrayList<LoanActivityDetail>();

	private List<? extends ActivityDetail> details = new ArrayList<LoanActivityDetail>();

	private List<? extends ActivityDynamicDetail> dynamicDetails = new ArrayList<ActivityDynamicDetail>();

	private List<? extends ActivityDynamicDetail> dynamicDetailsSystemOfRecords = new ArrayList<ActivityDynamicDetail>();

	private List<? extends ActivitySummary> summaries = new ArrayList<LoanActivitySummary>();

	private List<LoanActivityRecord> activityRecords = new ArrayList<LoanActivityRecord>();

	private Map<Integer, UserName> userDataList = new HashMap<Integer, UserName>();
	
	// Indicate whether the country is USA for the different activity types.
	private boolean systemOfRecordCountryUSA = true;
    private boolean originalCountryUSA = true;
    private boolean savedCountryUSA = true;

	public LoanActivities(String submissionId, Integer contractId,
			Integer participantUserProfileId, String participantFirstName,
			String participantLastName) {
		super();
		this.submissionId = submissionId;
		this.contractId = contractId;
		this.participantUserProfileId = participantUserProfileId;
		this.participantFirstName = participantFirstName;
		this.participantLastName = participantLastName;
	}

	public boolean isCountryUSA(String country) {
	    country = StringUtils.trimToEmpty(country);
	    if (GlobalConstants.COUNTRY_CODE_USA.equals(country)
	            || StringUtils.isEmpty(country)) {
	        // A null/blank country code signifies USA
	        return true;
	    } else {
	        return false;
	    }
	}
	
	/**
	 * Adds the specified ActivityDetail or ActivityDynamicDetail entries
	 * retrieved from the SDB to the activityRecords list, which stores the data
	 * on a field basis.
	 * 
	 * @param details
	 *            ActivityDetail or ActivityDynamicDetail entries retrieved from
	 *            the SDB
	 * @return int Number of entries that were processed (added/updated) to the
	 *         activityRecords list
	 */
	public int addActivityDetails(List<? extends ActivityDetail> details) {
		int numberEntriesProcessed = 0;
		if (details.size() <= 0) {
			return numberEntriesProcessed;
		}

		LoanActivityRecord newActivityRecord;

		// Build a map of the userProfileId's we'll need to retrieve details
		// for.
		for (ActivityDetail detail : details) {
			if (!userDataList.containsKey(detail.getLastUpdatedById())) {
				userDataList.put(detail.getLastUpdatedById(), new UserName());
			}
		}
		// Retrieve and store user info for the userProfileId's in userDataList
		populateUserDataList();

		/*
		 * Process the ActivityDetail entries, which correspond to the entries
		 * on table stp100.ACTIVITY_DETAIL, and the ActivityDynamicDetail
		 * entries, which correspond to the entries on table
		 * stp100.ACTIVITY_DYNAMIC_DETAIL.
		 */
		for (ActivityDetail detail : details) {

			boolean found = false;
			UserName userData = userDataList.get(detail.getLastUpdatedById());

			/*
			 * Record whether the country value is USA for the different 
			 * activity types.  These are access from request.jsp to determine
			 * if the state name or state code should be displayed in the 
			 * activity history details.
			 */
            if (LoanField.COUNTRY.getActivityDetailItemNo().equals(
					detail.getItemNumber())) {
                if (ActivityDetail.TYPE_SAVED
                        .equals(detail.getTypeCode())) {
                    setSavedCountryUSA(isCountryUSA(detail.getValue()));
                } else if (ActivityDetail.TYPE_ORIGINAL
                        .equals(detail.getTypeCode())) {
                    setOriginalCountryUSA(isCountryUSA(detail.getValue()));
                } else {  
                    setSystemOfRecordCountryUSA(isCountryUSA(detail.getValue()));
                }
                // If the country is deemed to be USA, set the activity history
                // value to 'USA', so activity history shows a consistent value.
                if (isCountryUSA(detail.getValue())) {
                    detail.setValue(GlobalConstants.COUNTRY_CODE_USA);
                }
            }
			
			for (LoanActivityRecord activityRecord : activityRecords) {

				if (detail instanceof ActivityDynamicDetail) {
					ActivityDynamicDetail activityDetail = (ActivityDynamicDetail) detail;
					if (activityRecord.getItemNumber().equals(
							activityDetail.getItemNumber())
							&& activityRecord.getSubItemNumber().equals(
									activityDetail.getSecondaryNumber())
							&& activityRecord.getSubItemName().equals(
									activityDetail.getSecondaryName())) {
						found = true;
					}
				} else {
					if (activityRecord.getItemNumber().equals(
							detail.getItemNumber())) {
						found = true;
					}
				}

				if (found) {
					activityRecord.setValue(detail.getValue(), detail
							.getTypeCode());
					if (ActivityDetail.TYPE_ORIGINAL.equals(detail
							.getTypeCode())) {
					    if (userData.getFirstName() != null 
					            || userData.getLastName() != null) {
	                        activityRecord.setSubmittedByName(userData
	                                .getFirstName()
	                                + " " + userData.getLastName());
					    }
						activityRecord.setSubmittedByRole(userData.getRole());
						activityRecord.setSubmittedByTimestamp(detail
								.getLastUpdated());
					}
					if (ActivityDetail.TYPE_SAVED.equals(detail.getTypeCode())) {
                        if (userData.getFirstName() != null 
                                || userData.getLastName() != null) {
                            activityRecord.setChangedByName(userData.getFirstName()
                                    + " " + userData.getLastName());
                        }
						activityRecord.setChangedByRole(userData.getRole());
						activityRecord.setChangedByTimestamp(detail
								.getLastUpdated());
					}
                    if (ActivityDetail.TYPE_SYSTEM_OF_RECORD.equals(detail.getTypeCode())) {
                        activityRecord.setSystemOfRecordTimestamp(detail
                                .getLastUpdated());
                    }
					break;
				}
			}

			if (!found) {
				newActivityRecord = new LoanActivityRecord(detail
						.getItemNumber(), 0);
				if (detail instanceof ActivityDynamicDetail) {
					ActivityDynamicDetail activityDetail = (ActivityDynamicDetail) detail;
					newActivityRecord.setSubItemNumber(activityDetail
							.getSecondaryNumber());
					newActivityRecord.setSubItemName(activityDetail
							.getSecondaryName());
				}
				newActivityRecord.setValue(detail.getValue(), detail
						.getTypeCode());
				newActivityRecord.setFieldName(LoanField
						.getFieldFromItemNumber(detail.getItemNumber())
						.getFieldName());

				if (ActivityDetail.TYPE_ORIGINAL.equals(detail.getTypeCode())) {
                    if (userData.getFirstName() != null 
                            || userData.getLastName() != null) {
                        newActivityRecord.setSubmittedByName(userData
                                .getFirstName()
                                + " " + userData.getLastName());
                    }
					newActivityRecord.setSubmittedByRole(userData.getRole());
					newActivityRecord.setSubmittedByTimestamp(detail
							.getLastUpdated());
				} else if (ActivityDetail.TYPE_SAVED.equals(detail
						.getTypeCode())) {
                    if (userData.getFirstName() != null 
                            || userData.getLastName() != null) {
                        newActivityRecord.setChangedByName(userData.getFirstName()
                                + " " + userData.getLastName());
                    }
					newActivityRecord.setChangedByRole(userData.getRole());
					newActivityRecord.setChangedByTimestamp(detail
							.getLastUpdated());
                } else if (ActivityDetail.TYPE_SYSTEM_OF_RECORD.equals(detail
                        .getTypeCode())) {
                    newActivityRecord.setSystemOfRecordTimestamp(detail
                            .getLastUpdated());
				}

				activityRecords.add(newActivityRecord);
			}
			numberEntriesProcessed++;
		}

		return numberEntriesProcessed;
	}

	private void populateUserDataList() {
		UserInfo uinfo;

		for (Integer userProfileId : userDataList.keySet()) {
			if (userProfileId.equals(this.participantUserProfileId)) {
				// Implies the userProfileId is that of the participant,
				// so skip retrieval of user info data, and use the participant
				// user data already available.
				userDataList.get(userProfileId).setFirstName(
						this.participantFirstName);
				userDataList.get(userProfileId).setLastName(
						this.participantLastName);
				userDataList.get(userProfileId).setRole(
						LoanConstants.USER_ROLE_PARTICIPANT_DISPLAY_NAME);
			} else {
				try {
					uinfo = SecurityServiceDelegate.getInstance()
							.getUserProfileByProfileId(userProfileId.longValue());
					if (uinfo != null) {
	                    userDataList.get(userProfileId).setFirstName(
	                            uinfo.getFirstName());
	                    userDataList.get(userProfileId).setLastName(
	                            uinfo.getLastName());
	                    
						// Bundled GA Project 2012 - Modified section below to include a condition
						// for when the user activity was performed by a Bundled GA Rep (or other internals such as team lead)
	                    String displayRole = "";
	                    if (uinfo.getRole().isTPA()) {
	                    	displayRole = LoanConstants.USER_ROLE_TPA_DISPLAY_NAME;
	                    } else if (uinfo.getRole().isInternalUser()) {
	                        // This applies to ANY user who is internal. May be more than just BGA Rep.
	                    	displayRole = LoanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME;
	                    } else {
                            displayRole = LoanConstants.USER_ROLE_PLAN_SPONSOR_DISPLAY_NAME;
	                    }
	                    
	                    userDataList
	                            .get(userProfileId).setRole(displayRole);
					} else {
					    // No such user exists for the profile ID indicated.
                        userDataList.get(userProfileId).setFirstName(null);
                        userDataList.get(userProfileId).setLastName(null);
					    userDataList.get(userProfileId).setRole(null);
					}    
				} catch (Exception e) {
                    // Instead of crashing, assume no such user exists for
				    // the profile ID indicated.
				    userDataList.get(userProfileId).setFirstName(null);
                    userDataList.get(userProfileId).setLastName(null);
                    userDataList.get(userProfileId).setRole(null);
				}
			}
		}
	}

	public List<? extends ActivityDetail> getDetailsSystemOfRecords() {
		return detailsSystemOfRecords;
	}

	public void setDetailsSystemOfRecords(
			List<? extends ActivityDetail> detailsSystemOfRecords) {
		this.detailsSystemOfRecords = detailsSystemOfRecords;
	}

	public List<? extends ActivityDynamicDetail> getDynamicDetailsSystemOfRecords() {
		return dynamicDetailsSystemOfRecords;
	}

	public void setDynamicDetailsSystemOfRecords(
			List<? extends ActivityDynamicDetail> dynamicDetailsSystemOfRecords) {
		this.dynamicDetailsSystemOfRecords = dynamicDetailsSystemOfRecords;
	}

	public List<? extends ActivityDetail> getDetails() {
		return details;
	}

	public void setDetails(List<? extends ActivityDetail> details) {
		this.details = details;
	}

	public List<? extends ActivityDynamicDetail> getDynamicDetails() {
		return dynamicDetails;
	}

	public void setDynamicDetails(
			List<? extends ActivityDynamicDetail> dynamicDetails) {
		this.dynamicDetails = dynamicDetails;
	}

	public List<? extends ActivitySummary> getSummaries() {
		return summaries;
	}

	public void setSummaries(List<? extends ActivitySummary> summaries) {
		this.summaries = summaries;
	}
	
	/**
	 * Return the Activity Summary record corresponding to the statusCode
	 * supplied.  If it doesn't exist, return null.
	 * @param statusCode
	 * @return LoanActivitySummary
	 */
	public LoanActivitySummary getSummaryRecord(String statusCode) {
	    if (statusCode == null) {
	        return null;
	    }
	    for (ActivitySummary summary : this.summaries) {
	        if (statusCode.equals(summary.getStatusCode())) {
	            return (LoanActivitySummary) summary;
	        }
	    }
	    return null;
	}
	
	public LoanActivitySummary getSummaryForApprovedStatus() {
	    return getSummaryRecord(ActivitySummary.APPROVED);
	}

	public List<LoanActivityRecord> getActivityRecords() {
		return activityRecords;
	}

	private String getSubmissionId() {
		return submissionId;
	}

	private void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}

	private Integer getContractId() {
		return contractId;
	}

	private void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	private Integer getParticipantUserProfileId() {
		return participantUserProfileId;
	}

	private void setParticipantUserProfileId(Integer participantProfileId) {
		this.participantUserProfileId = participantProfileId;
	}

	private String getParticipantFirstName() {
		return participantFirstName;
	}

	private void setParticipantFirstName(String participantFirstName) {
		this.participantFirstName = participantFirstName;
	}

	private String getParticipantLastName() {
		return participantLastName;
	}

	private void setParticipantLastName(String participantLastName) {
		this.participantLastName = participantLastName;
	}

    public boolean isSystemOfRecordCountryUSA() {
        return systemOfRecordCountryUSA;
    }

    public void setSystemOfRecordCountryUSA(boolean systemOfRecordCountryUSA) {
        this.systemOfRecordCountryUSA = systemOfRecordCountryUSA;
    }

    public boolean isOriginalCountryUSA() {
        return originalCountryUSA;
    }

    public void setOriginalCountryUSA(boolean originalCountryUSA) {
        this.originalCountryUSA = originalCountryUSA;
    }

    public boolean isSavedCountryUSA() {
        return savedCountryUSA;
    }

    public void setSavedCountryUSA(boolean savedCountryUSA) {
        this.savedCountryUSA = savedCountryUSA;
    }
}

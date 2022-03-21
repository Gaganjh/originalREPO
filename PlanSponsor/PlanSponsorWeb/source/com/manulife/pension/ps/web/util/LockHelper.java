package com.manulife.pension.ps.web.util;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.security.valueobject.UserInfo;

public class LockHelper {
    public static final String CSF_LOCK_NAME = "csf";
    public static final String USER_PROFILE_LOCK_NAME = "userprofile";
    public static final String TPA_FIRM_LOCK_NAME = "tpafirm";
    public static final String PLAN_LOCK_NAME = "plan";
    public static final String CONTACT_ADDRESS_LOCK_NAME = "contactAddress";
    public static final String CONTACT_COMMENT_LOCK_NAME = "contactComment";
    public static final String EDIT_IPS_REVIEW_LOCK_NAME = "editIPSReview";
    public static final String TPA_PLAN_LOCK_NAME = "tpaplan";
    public static final String EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE = "tpaContractFee";
    public static final String EDIT_TPA_STANDARD_SCHEDULE_PAGE = "editTPAStandardFee";
    
    public static final String EDIT_PLAN_DATA_PAGE = "viewNoticePlanData";
    public static final String JH_REP_LABEL = "John Hancock Representative";

    public static String getLockOwnerDisplayName(UserProfile loggedInUser, UserInfo lockOwnerUserInfo) {
        String name = null;

        if (lockOwnerUserInfo != null) {
            if (lockOwnerUserInfo.getRole().isExternalUser()) {
                name = new StringBuffer(lockOwnerUserInfo.getFirstName()).append(" ")
                    .append(lockOwnerUserInfo.getLastName()).toString();
            } else {
                if (loggedInUser.getRole().isExternalUser()) {
                    name = JH_REP_LABEL;
                } else {
                    name = new StringBuffer(lockOwnerUserInfo.getFirstName()).append(" ")
                        .append(lockOwnerUserInfo.getLastName()).append(" ")
                        .append(JH_REP_LABEL).toString();
                }
            }
        } else {
            name = JH_REP_LABEL;
        }

        return name;
    }
}

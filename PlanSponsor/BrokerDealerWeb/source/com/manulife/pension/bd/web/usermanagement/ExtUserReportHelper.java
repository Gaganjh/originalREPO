package com.manulife.pension.bd.web.usermanagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.util.render.RenderConstants;

/**
 * This is a helper class for user report classes
 * 
 * @author Ilamparithi
 * 
 */
public class ExtUserReportHelper {

    private static Logger logger = Logger.getLogger(ExtUserReportHelper.class);

    private static String SelectLabel = "Select";

    private static SimpleDateFormat sdf = new SimpleDateFormat(RenderConstants.MEDIUM_MDY_SLASHED);
    
    /**
     * The method is to make the SimpleDateFormat synchronized in order to make it thread safe
     * @param value
     * @return string formatter
     * @throws ParseException
     */
    private static synchronized Date  DateParseMMDDYY(String value) throws ParseException{ 
        return sdf.parse(value); 
    }

    public static final String RESET_TASK = "reset";
    
    static {
        sdf.setLenient(false);
    }

    /**
     * This method returns a list of BDUserRoleType objects except the Level 1 Broker
     * 
     * @return List<LabelValueBean> a list of BDUserRoleType objects
     */
    public static List<LabelValueBean> getExtUserRoles() {
        List<BDUserRoleType> sortedExtUserRoles = BDUserRoleDisplayNameUtil.getInstance()
                .getSortedExtUserRoles(true);
        sortedExtUserRoles.remove(BDUserRoleType.BasicFinancialRep);
        sortedExtUserRoles.remove(BDUserRoleType.NoAccess);
        List<LabelValueBean> userRoleDisplayList = new ArrayList<LabelValueBean>(sortedExtUserRoles
                .size() + 1);
        userRoleDisplayList.add(new LabelValueBean("Select", ""));
        for (BDUserRoleType userRole : sortedExtUserRoles) {
            LabelValueBean labelValue = new LabelValueBean(ExtUserSearchReportHelper
                    .getUserRoleDisplay(userRole), userRole.getUserRoleCode());
            userRoleDisplayList.add(labelValue);
        }
        return userRoleDisplayList;
    }

    /**
     * Returns a list of states that belong to US.
     * 
     * @return List a List of LabelValueBean objects with state code as Label and Value since we
     *         have to show the state codes in the screen. But the DAO returns the state codes and
     *         state names.
     */
    @SuppressWarnings("unchecked")
    public static List<LabelValueBean> getUSStates() {
        List<LabelValueBean> tempList = new ArrayList<LabelValueBean>();
        List<LabelValueBean> statesList = new ArrayList<LabelValueBean>();
        statesList.add(new LabelValueBean(SelectLabel, ""));
        try {
            Map<String, String> states = EnvironmentServiceDelegate.getInstance(
                    BDConstants.BD_APPLICATION_ID).getUSAGeographicalStatesOnly();
            for (Iterator<String> it = states.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                // We have to show the state codes in the screen.
                tempList.add(new LabelValueBean(key.toString(), key.toString()));
            }
            Collections.sort(tempList, new Comparator<LabelValueBean>() {
                public int compare(LabelValueBean o1, LabelValueBean o2) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }
            });
            statesList.addAll(tempList);
        } catch (SystemException e) {
            logger.error("Fail to load the US states", e);
        }
        return statesList;
    }

    /**
     * This method is used for validating Date
     * 
     * @param dateString String
     * @return Date
     * @throws ParseException
     */
    public static Date validateDateFormat(String dateString) throws ParseException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validateDateFormat");
        }
        Date validDate = null;
        if (!((dateString.trim().length() == 10) && (dateString.substring(2, 3).equals("/")) && (dateString
                .substring(5, 6)).equals("/"))) {
            throw new ParseException("invalid date format", 0);
        }
        String month = dateString.substring(0, 2);
        String day = dateString.substring(3, 5);
        String year = dateString.substring(6, 10);
        try {
            Integer.parseInt(year);
            if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12)
                throw new ParseException("invalid month", 0);

            if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31)
                throw new ParseException("invalid day", 0);

            if (Integer.parseInt(day) == 29 && (Integer.parseInt(month) == 2)
                    && (Integer.parseInt(year) % 4 > 0))
                throw new ParseException("invalid day", 0);

        } catch (Exception e) {
            throw new ParseException("invalid date format", 0);
        }

        validDate = DateParseMMDDYY(dateString);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- validateDateFormat");
        }
        return validDate;
    }

}

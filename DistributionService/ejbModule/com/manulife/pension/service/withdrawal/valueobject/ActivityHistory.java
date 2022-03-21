package com.manulife.pension.service.withdrawal.valueobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * @author Dennis
 * 
 */
public class ActivityHistory extends BaseSerializableCloneableObject {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    private List<WithdrawalActivitySummary> summaries = new ArrayList<WithdrawalActivitySummary>();

    private Collection<Activity> activities = new ArrayList<Activity>();

    /**
     * @return the activity history activities
     */
    public Collection<Activity> getActivities() {
        return activities;
    }

    /**
     * @param activities the acitivty histor yactivities
     */
    public void setActivities(final Collection<Activity> activities) {
        this.activities = activities;
    }

    /**
     * @return the activity history summaries
     */
    public List<WithdrawalActivitySummary> getSummaries() {
        return summaries;
    }

    /**
     * @param summaries the activity history summaries
     */
    public void setSummaries(final List<WithdrawalActivitySummary> summaries) {
        this.summaries = summaries;
    }

}

package com.manulife.pension.ps.service.report.census.valueobject;

import java.io.Serializable;

public class StatisticsSummary implements Serializable {

    private static final long serialVersionUID = 7914024879062262042L;
    private int autoEnrolled;
    private int defaultEnrolled;
    private int paperEnrolled;
    private int internetEnrolled;    
    private int participants;
    private int optOut;
    private int pendingEligibility;
    private int pendingEnrollment;
    private int notEligible;
    
    public StatisticsSummary(int autoEnrolled, int defaultEnrolled, int paperEnrolled,
    		int internetEnrolled,  int participants, int optOut, int pendingEligibility, 
    		int pendingEnrollment, int notEligible) {
        super();
        this.autoEnrolled = autoEnrolled;
        this.defaultEnrolled = defaultEnrolled;
        this.paperEnrolled = paperEnrolled;
        this.internetEnrolled = internetEnrolled;
        this.participants = participants;
        this.optOut = optOut;
        this.pendingEligibility = pendingEligibility;
        this.pendingEnrollment = pendingEnrollment;
        this.notEligible = notEligible;
    }

    public StatisticsSummary() {
                
    }

    public int getAutoEnrolled() {
        return autoEnrolled;
    }

    public void setAutoEnrolled(int autoEnrolled) {
        this.autoEnrolled = autoEnrolled;
    }

    public int getDefaultEnrolled() {
        return defaultEnrolled;
    }

    public void setDefaultEnrolled(int defaultEnrolled) {
        this.defaultEnrolled = defaultEnrolled;
    }

    public int getInternetEnrolled() {
        return internetEnrolled;
    }

    public void setInternetEnrolled(int internetEnrolled) {
        this.internetEnrolled = internetEnrolled;
    }

    public int getNotEligible() {
        return notEligible;
    }

    public void setNotEligible(int notEligible) {
        this.notEligible = notEligible;
    }

    public int getOptOut() {
        return optOut;
    }

    public void setOptOut(int optOut) {
        this.optOut = optOut;
    }

    public int getPaperEnrolled() {
        return paperEnrolled;
    }

    public void setPaperEnrolled(int paperEnrolled) {
        this.paperEnrolled = paperEnrolled;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public int getPendingEligibility() {
        return pendingEligibility;
    }

    public void setPendingEligibility(int pendingEligibility) {
        this.pendingEligibility = pendingEligibility;
    }

    public int getPendingEnrollment() {
        return pendingEnrollment;
    }

    public void setPendingEnrollment(int pendingEnrollment) {
        this.pendingEnrollment = pendingEnrollment;
    }

}

package com.manulife.pension.ps.web.pilot;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * @author Steven
 */
public class PilotContractReportForm extends ReportForm {

    /**
     * 
     */
    private static final long serialVersionUID = 432199593608156930L;

    public static final String ALL = "All";

    private String contractNumber;

    private String teamCode;

    private List<LabelValueBean> teamCodeList = new ArrayList<LabelValueBean>();

    /**
     * Constructor.
     */
    public PilotContractReportForm() {
        super();
    }

    /**
     * @see ReportForm#clear()
     */
    public void clear() {
        super.clear();
        contractNumber = null;
        teamCode = null;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public List<LabelValueBean> getTeamCodeList() {
        return teamCodeList;
    }

    public void setTeamCodeList(List<LabelValueBean> teamCodeList) {
        this.teamCodeList = teamCodeList;
    }

}
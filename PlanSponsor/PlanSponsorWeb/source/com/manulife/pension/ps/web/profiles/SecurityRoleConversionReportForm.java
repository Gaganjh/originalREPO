package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.FastDateFormat;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * Security role conversion report action form
 * @author Steven Wang
 */
public class SecurityRoleConversionReportForm extends ReportForm {

    private static final long serialVersionUID = 5125174830998034434L;

    public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
    
    //SimpleDateFormat is converted to FastDateFormat to make it thread safe
    public static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("MM/dd/yyyy");
    
    public static final String ALL = "All";

    private String contractNumber;
    private String fromDate;
    private String toDate;
    private String teamCode;
    private List<LabelValueBean> teamCodeList = new ArrayList<LabelValueBean>();

	/**
	 * Constructor.
	 */
	public SecurityRoleConversionReportForm() {
		super();
	}

	/**
	 * @see ReportForm#clear()
	 */
 	public void clear() {
		super.clear();
        contractNumber = null;
        fromDate = null;
        toDate = null;
        teamCode = null;
	}

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
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

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
    private String getCurrentDay() {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());
        return DATE_FORMATTER.format(new Date(cl.getTimeInMillis()));
    }
    
}
package com.manulife.pension.ps.web.noticereports;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.report.ReportForm;

/**
 * Common Action form for notice reports.
 * 
 */
public class NoticeReportsCommonForm extends ReportForm {

    private static final long serialVersionUID = -9221334525642956197L;

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
    static {
        DATE_FORMATTER.setLenient(false);
    }

    //Date Formatter variable is added to a synchronized block to make it thread safe
    protected static synchronized String  dateFormatter(Date inputDate){ 
        return DATE_FORMATTER.format(inputDate); 
    }
    protected static synchronized Date  dateParser(String value) throws ParseException{ 
        return DATE_FORMATTER.parse(value); 
    }
    private String contractNumber;

    private String action;

    private String fromDate;

    private String toDate;

    private String task;

    private String defaultFromDate;

    private List<LabelValueBean> internalActionList = new ArrayList<LabelValueBean>();

    private List<LabelValueBean> externalActionList = new ArrayList<LabelValueBean>();

    /**
     * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    public void reset( HttpServletRequest httpservletrequest) {
        super.reset( httpservletrequest);
    }

    /**
     * @return the contractNumber
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * @param contractNumber the contractNumber to set
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the fromDate
     */
    public String getFromDate() {
        if (fromDate == null)
            return getDefaultFromDate();
        return fromDate;
    }

    /**
     * Returns the default from date from properties file If not found in the properties file,
     * returns hard coded default from date.
     * 
     * @return default from date
     */
    public String getDefaultFromDate() {

        defaultFromDate = NoticeManagerUtility.properties
                .getProperty("NOTICE_REPORTS_DEFAULT_FROM_DATE");
        if (defaultFromDate == null) {
            defaultFromDate = NoticeReportsConstants.NOTICE_REPORTS_DEFAULT_FROM_DATE;
        }
        return defaultFromDate;
    }

    /**
     * Wrapper method to support old naming convention for getDefaultFromDate.
     * 
     * @return default from date
     */
    public String getFromDefaultDate() {

        return getDefaultFromDate();
    }

    /**
     * Returns default date as current date.
     * 
     * @return default date as current date.
     */
    public String getDefaultDate() {
        GregorianCalendar defaultDate = new GregorianCalendar();
        return dateFormatter(defaultDate.getTime());
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        try {
            this.fromDate = dateFormatter(dateParser(fromDate));
        } catch (ParseException pe) {
            this.fromDate = fromDate;
        }
    }

    /**
     * @return the toDate
     */
    public String getToDate() {
        if (toDate == null)
            return getDefaultDate();
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(String toDate) {
        try {
            this.toDate = dateFormatter(dateParser(toDate));
        } catch (ParseException pe) {
            this.toDate = toDate;
        }
    }

    /**
     * @param externalActionList the externalActionList to set
     */
    public void setExternalActionList(List<LabelValueBean> externalActionList) {
        this.externalActionList = externalActionList;
    }

    /**
     * @param internalActionList the internalActionList to set
     */
    public void setInternalActionList(List<LabelValueBean> internalActionList) {
        this.internalActionList = internalActionList;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setDefaultFromDate(String defaultFromDate) {
        this.defaultFromDate = defaultFromDate;
    }

    public List<LabelValueBean> getInternalActionList() {
        return internalActionList;
    }

    public List<LabelValueBean> getExternalActionList() {
        return externalActionList;
    }

}

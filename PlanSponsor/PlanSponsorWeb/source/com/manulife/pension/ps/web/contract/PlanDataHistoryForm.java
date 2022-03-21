package com.manulife.pension.ps.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.service.report.contract.valueobject.PlanDataHistoryReportData;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.valueobject.PlanDataHistoryVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;

public class PlanDataHistoryForm extends ReportForm {
    private static final long serialVersionUID = -6808480077978840067L;

    private int planId = 0;
    private String fieldName = "";
    private String userName = "";
    private String fromDate = "";
    private String toDate = "";
    private PlanDataHistoryReportData report = null;
    
    private List fieldNames = new ArrayList();
    
    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    /**
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }
    
    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    
    /**
     * @return the report
     */
    public PlanDataHistoryReportData getReport() {
        return report;
    }
    
    /**
     * @param report the report to set
     */
    public void setReport(PlanDataHistoryReportData report) {
        this.report = report;
    }
    
    /**
     * @return the toDate
     */
    public String getToDate() {
        return toDate;
    }
    
    /**
     * @param toDate the toDate to set
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the planId
     */
    public int getPlanId() {
        return planId;
    }

    /**
     * @param planId the planId to set
     */
    public void setPlanId(int planId) {
        this.planId = planId;
    }

    /**
     * @return the fieldNames
     */
    public List getFieldNames() {
        return fieldNames;
    }

    /**
     * @param fieldNames the fieldNames to set
     */
    public void setFieldNames(List fieldNames) {
        this.fieldNames = fieldNames;
    }
    
    public void reset( HttpServletRequest request) {
        List<LabelValueBean> fieldNames = new ArrayList<LabelValueBean>();
        fieldNames.add(new LabelValueBean(PlanConstants.DROP_DOWN_DEFAULT_LABEL, ""));
        for (Object fName : PlanDataHistoryVO.fieldNames) {
            fieldNames.add(new LabelValueBean((String)fName, (String)fName));
        }
        setFieldNames(fieldNames);
    }
}

package com.manulife.pension.ps.service.report.investment.valueobject;

import java.util.ArrayList;

import com.manulife.pension.ps.service.report.investment.reporthandler.InvestmentAllocationDetailsReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 *
 * @author nvintila
 * @date Feb 8, 2004
 * @time 3:31:25 PM
 */
public class InvestmentAllocationDetailsReportData extends ReportData {

    public static final String REPORT_ID = InvestmentAllocationDetailsReportHandler.class.getName();
    public static final String REPORT_NAME = "investmentAllocationDetailsReport"; 
    
    public static final String CSV_REPORT_NAME = "ContractAllocationDetails"; 

    AllocationDetailsReportSummaryVO summary;

    //Map monthEndDates;
    private boolean hideOngoingContributions;
    private boolean jhiIndicatorFlg;

    public InvestmentAllocationDetailsReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);
        this.details = new ArrayList(0); // a graceful way to report "No matches"
    }

    public AllocationDetailsReportSummaryVO getSummary() {
        return summary;
    }

    public void setSummary(AllocationDetailsReportSummaryVO summary) {
        this.summary = summary;
    }

//    /**
//     * todo: this logic is duplicated from
//     * {@link com.manulife.pension.ps.web.investment.BaseInvestmentPageForm#setMonthEnds}
//     */
//    public void setDateInfo(String contractNumber, Date asOfDate) {
//
//        ContractDatesVO contractDatesVO = new InvestmentAllocationDAO()
//            .getContractDates(contractNumber);
//
//        this.monthEndDates = new HashMap();
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat formatlabel = new SimpleDateFormat("MMM d, yyyy");
//
//        this.monthEndDates.put(formatter.format(contractDatesVO.getAsOfDate()),
//            formatlabel.format(contractDatesVO.getAsOfDate()));
//
//        Iterator iter = contractDatesVO.getMonthEndDates().iterator();
//
//        while (iter.hasNext()) {
//            Date date = (Date) iter.next();
//            this.monthEndDates.put(formatter.format(date),
//                formatlabel.format(date));
//        }
//
//        //prepareForRendering(contractDatesVO, asOfDate);
//    }

//    public Map getMonthEndDates() {
//        return monthEndDates;
//    }

    /**
     * Prepare for conditional rendering:
     * 1) User messages "no matches"
     * 2) Hiding of the data inside "ongoing contributions" column if the
     * asOfDate used is not the "last business date".
     */
    public void prepareForRendering(boolean isCurrentBusinessDate) {
        //boolean showNoMatchesMessage = getDetails().isEmpty();
        this.hideOngoingContributions = !isCurrentBusinessDate;// && !showNoMatchesMessage;
    }

    /**
     * Presentation logic: controls the visibility of the data inside the
     * "Ongoing contributions" column.
     */
    public boolean isHideOngoingContributions() {
        return hideOngoingContributions;
    }

	/**
	 * @return the jhiIndicatorFlg
	 */
	public boolean isJhiIndicatorFlg() {
		return jhiIndicatorFlg;
	}

	/**
	 * @param jhiIndicatorFlg the jhiIndicatorFlg to set
	 */
	public void setJhiIndicatorFlg(boolean jhiIndicatorFlg) {
		this.jhiIndicatorFlg = jhiIndicatorFlg;
	}
}
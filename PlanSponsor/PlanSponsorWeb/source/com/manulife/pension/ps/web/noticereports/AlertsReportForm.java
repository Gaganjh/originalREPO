package com.manulife.pension.ps.web.noticereports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.service.report.noticereports.valueobject.UploadAndShareReportTopTenDocumentNamesVO;

public class AlertsReportForm extends NoticeReportsCommonForm {

    private static final long serialVersionUID = -4371610975506788986L;

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
    
    private List<UploadAndShareReportTopTenDocumentNamesVO> topFiveUploadedDocList ;
    private List<UploadAndShareReportTopTenDocumentNamesVO> sixToTenUploadedDocList ;
    
    private Boolean sourceOfShareListInd  = false;

    private Boolean sourceOfUploadListInd  = false;
    
    
    public Boolean getSourceOfShareListInd() {
		return sourceOfShareListInd;
	}

	public void setSourceOfShareListInd(Boolean sourceOfShareListInd) {
		this.sourceOfShareListInd = sourceOfShareListInd;
	}

	public Boolean getSourceOfUploadListInd() {
		return sourceOfUploadListInd;
	}

	public void setSourceOfUploadListInd(Boolean sourceOfUploadListInd) {
		this.sourceOfUploadListInd = sourceOfUploadListInd;
	}
    
    static {
        DATE_FORMATTER.setLenient(false);
    }

    public List<UploadAndShareReportTopTenDocumentNamesVO> getTopFiveUploadedDocList() {
		return topFiveUploadedDocList;
	}

	public void setTopFiveUploadedDocList(
			List<UploadAndShareReportTopTenDocumentNamesVO> topFiveUploadedDocList) {
		this.topFiveUploadedDocList = topFiveUploadedDocList;
	}

	public List<UploadAndShareReportTopTenDocumentNamesVO> getSixToTenUploadedDocList() {
		return sixToTenUploadedDocList;
	}

	public void setSixToTenUploadedDocList(
			List<UploadAndShareReportTopTenDocumentNamesVO> sixToTenUploadedDocList) {
		this.sixToTenUploadedDocList = sixToTenUploadedDocList;
	}

	public void reset( HttpServletRequest httpservletrequest) {
        super.reset( httpservletrequest);
    }

}

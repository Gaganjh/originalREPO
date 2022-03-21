package com.manulife.pension.bd.web.estatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;

/**
 * Form to hold the Estatement related Request and Response data
 * 
 * @author raoprer
 * 
 */
public class EstatementReportForm extends BaseReportForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1234274738810217814L;
	public static final String FIELD_FIRMS = "firms";
	private List<BrokerDealerFirm> firms = new ArrayList<BrokerDealerFirm>(0);
	private List<RiaStatementVO> riaStatementListVO = new ArrayList<RiaStatementVO>();
	private String selectedFirmId;
	private String selectedFirmName;
	private String docId=" ";
	private String fileType= " ";
	private String userType=" ";
	private String participantFilter = null;
	private List<Date> listDates = new ArrayList<Date>();
	private List<String> listDatesStr = new ArrayList<String>();
	private List<String> listFirms = new ArrayList<String>();
	private String statementFirmId = "";
	private String statementFirmName = "";
	private String statementGenDateStr = "";
	private String riaStmDateFilter = "";
	private String riaFirmFilter ="";
	
	public String getSelectedFirmId() {
		return selectedFirmId;
	}

	public void setSelectedFirmId(String selectedFirmId) {
		this.selectedFirmId = selectedFirmId;
	}

	public String getSelectedFirmName() {
		return selectedFirmName;
	}

	public void setSelectedFirmName(String selectedFirmName) {
		this.selectedFirmName = selectedFirmName;
	}

	public List<BrokerDealerFirm> getFirms() {
		return firms;
	}

	public void setFirms(List<BrokerDealerFirm> firms) {
		this.firms = firms;
	}

	public List<RiaStatementVO> getRiaStatementListVO() {
		return riaStatementListVO;
	}

	public void setRiaStatementListVO(
			List<RiaStatementVO> riaStatementListVO) {
		this.riaStatementListVO = riaStatementListVO;
	}

	

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getParticipantFilter() {
		return participantFilter;
	}

	public void setParticipantFilter(String participantFilter) {
		this.participantFilter = participantFilter;
	}

	public List<Date> getListDates() {
		return listDates;
	}

	public void setListDates(List<Date> listDates) {
		this.listDates = listDates;
	}

	public List<String> getListDatesStr() {
		return listDatesStr;
	}

	public void setListDatesStr(List<String> listDatesStr) {
		this.listDatesStr = listDatesStr;
	}

	public String getStatementFirmId() {
		return statementFirmId;
	}

	public void setStatementFirmId(String statementFirmId) {
		this.statementFirmId = statementFirmId;
	}

	public String getStatementFirmName() {
		return statementFirmName;
	}

	public void setStatementFirmName(String statementFirmName) {
		this.statementFirmName = statementFirmName;
	}

	public String getStatementGenDateStr() {
		return statementGenDateStr;
	}

	public void setStatementGenDateStr(String statementGenDateStr) {
		this.statementGenDateStr = statementGenDateStr;
	}

	public List<String> getListFirms() {
		return listFirms;
	}

	public void setListFirms(List<String> listFirms) {
		this.listFirms = listFirms;
	}

	public String getRiaStmDateFilter() {
		return riaStmDateFilter;
	}

	public void setRiaStmDateFilter(String riaStmDateFilter) {
		this.riaStmDateFilter = riaStmDateFilter;
	}

	public String getRiaFirmFilter() {
		return riaFirmFilter;
	}

	public void setRiaFirmFilter(String riaFirmFilter) {
		this.riaFirmFilter = riaFirmFilter;
	}

}
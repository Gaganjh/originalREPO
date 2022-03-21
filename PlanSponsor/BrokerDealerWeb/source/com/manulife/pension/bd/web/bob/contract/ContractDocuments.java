package com.manulife.pension.bd.web.bob.contract;

import java.util.ArrayList;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;



/**
 * ContractDocuments class 
 * This class is used as a value object for 
 * ContractDocuments page
 * 
 */

public class ContractDocuments {
	
	private String contractDocName;

	private ArrayList amendmentDocs;

	private String amendmentOption;

	/**
	 * This Constructor sets the default value for ContractAmendment dropdown.
	 *
	 */
	public ContractDocuments() {

		StringBuffer prompt = new StringBuffer(BDConstants.SELECT_AMENDMENT);
		amendmentDocs = new ArrayList();
		amendmentOption = prompt.toString();
	}

	/**
	 * To get amendment option.
	 * @return
	 */
	public String getAmendmentOption() {
		return this.amendmentOption;
	}

	/**
	 * This method adds the ContractAmendment doc to amedment list or
	 * sets the ContractDocument name.
	 * @param documentType
	 * @param key
	 * @param label
	 */
	public void addDocuments(String documentType, String key, String label) {
		if (documentType != null) {
			if ((RequestConstants.DOCUMENT_TYPE_CONTRACT).equals(documentType
					.trim())) {
				this.setContractDocName(key);
			} else if ((RequestConstants.DOCUMENT_TYPE_AMENDMENT)
					.equals(documentType.trim())) {
				amendmentDocs.add(new LabelValueBean(label, key));
			}
		}
	}

	/**
	 * To get AmendmentDocuments.
	 * @return
	 */
	public ArrayList getAmendmentDocs() {
		return amendmentDocs;
	}

	/**
	 * To set AmendmentDocuments.
	 * @param amendmentDocs
	 */
	public void setAmendmentDocs(ArrayList amendmentDocs) {
		this.amendmentDocs = amendmentDocs;
	}

	/**
	 * To set amendment option
	 * @param amendmentOption
	 */
	public void setAmendmentOption(String amendmentOption) {
		this.amendmentOption = amendmentOption;
	}

	/**
	 * To get ContractDocName
	 * @return contractDocName
	 */
	public String getContractDocName() {
		return contractDocName;
	}

	/**
	 * To set ContractDocName
	 * @param contractDocName
	 */
	public void setContractDocName(String contractDocName) {
		this.contractDocName = contractDocName;
	}

}
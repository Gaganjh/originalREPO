package com.manulife.pension.platform.web.investment.valueobject;

import java.io.Serializable;

/**
 * Value Object for Criteria And Weightings
 * 
 * @author arthik
 *
 */
public class CriteriaAndWeightingPresentation implements Serializable{

	private static final long serialVersionUID = 1L;

	private String criteriaDesc;
	
	private String weighting;
	
	private String colorCode;
	
	private String criteriaCode;

	public String getCriteriaDesc() {
		return criteriaDesc;
	}

	public void setCriteriaDesc(String criteriaDesc) {
		this.criteriaDesc = criteriaDesc;
	}

	public String getWeighting() {
		return weighting;
	}

	public void setWeighting(String weighting) {
		this.weighting = weighting;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getCriteriaCode() {
		return criteriaCode;
	}

	public void setCriteriaCode(String criteriaCode) {
		this.criteriaCode = criteriaCode;
	}
}

package com.manulife.pension.bd.web.pagelayout;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.util.BDSessionHelper;

/**
 * The Layout store for the BDW web application. It stores the
 * system configuration of each Page's layout. And it provides the methods
 * to get a BDLayoutBean that tailed to whether the page needs 
 * DefinedBenefit and non-default content location in the request
 * @author guweigu
 *
 */
public class LayoutStore {
	private Map<String, BDLayoutBeanConfig> layouts;

	/**
	 * Returns LayoutBean for page. If the request has set the
	 * content location, use it. Otherwise use the site location (null)
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	public BDLayoutBean getLayoutBean(String id, HttpServletRequest request) {
		boolean isDefinedBenefit = false;
		boolean isGiflSelect = false;

		if ((BDSessionHelper.getBobContext(request) != null)
				&& (BDSessionHelper.getBobContext(request).getCurrentContract() != null)
				&& BDSessionHelper.getBobContext(request).getCurrentContract()
						.isDefinedBenefitContract()) {
			isDefinedBenefit = true;
		}
		if ((BDSessionHelper.getBobContext(request) != null)
				&& (BDSessionHelper.getBobContext(request).getCurrentContract() != null)
				&& (BDSessionHelper.getBobContext(request).getCurrentContract()
						.getHasContractGatewayInd())
				&& (BDConstants.GIFL_VERSION_03).equals(BDSessionHelper
						.getBobContext(request).getCurrentContract()
						.getGiflVersion())) {
			isGiflSelect = true;
		}		
		BDLayoutBeanConfig config = layouts.get(id);
		if (config == null) {
			return null;
		}
		return new BDLayoutBeanImpl(config, isDefinedBenefit, isGiflSelect,
				ApplicationHelper.getRequestContentLocation(request));
	}

	public void setLayouts(Map<String, BDLayoutBeanConfig> layouts) {
		this.layouts = layouts;
	}
}

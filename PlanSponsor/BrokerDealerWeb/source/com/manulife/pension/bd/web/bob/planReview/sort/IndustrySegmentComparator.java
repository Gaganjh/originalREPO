package com.manulife.pension.bd.web.bob.planReview.sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;

/**
 * This is Comparator class for IndustrySegement used for Plan Review report
 * pages.
 * 
 * @author NagaRaju
 * 
 */

public class IndustrySegmentComparator extends
		BaseContractReviewReportComparator {
	
	private static final Logger logger = Logger.getLogger(IndustrySegmentComparator.class.getName());
	
	private static Map<String, String> magazineIndustryDropdownMap = null;
	
	static {

		if (magazineIndustryDropdownMap == null) {

			try {
				List<LabelValueBean> magazineIndustryDropdownList = PlanReviewServiceDelegate
						.getInstance(
								Environment.getInstance().getApplicationId())
						.getAllMagazineIndustryDropdownValues();

				magazineIndustryDropdownMap = new HashMap<String, String>();

				for (LabelValueBean labelValueBean : magazineIndustryDropdownList) {
					magazineIndustryDropdownMap.put(labelValueBean.getLabel(),
							labelValueBean.getValue());
				}
			} catch (SystemException exception) {
				logger.error(
						"Exception occured while loading magazine Industry Dropdown Map",
						exception);
				
				throw new IllegalArgumentException("Exception occured while loading magazine Industry Dropdown Map",
						exception);
			}
		}
	}

	public IndustrySegmentComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0,
			PlanReviewReportUIHolder arg1) {

		String industrysgmt0 = arg0.getSelectedIndustrySegment();
		String industrysgmt1 = arg1.getSelectedIndustrySegment();

		String industrysgmtName0 = StringUtils.EMPTY;
		String industrysgmtName1 = StringUtils.EMPTY;

		if (StringUtils.isNotEmpty(industrysgmt0)) {
			industrysgmtName0 = magazineIndustryDropdownMap.get(industrysgmt0);
		}

		if (StringUtils.isNotEmpty(industrysgmt1)) {
			industrysgmtName1 = magazineIndustryDropdownMap.get(industrysgmt1);
		}

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier
				* industrysgmtName0.compareToIgnoreCase(industrysgmtName1);

		if (result == 0) {
			result = super.compare(arg0, arg1);
		}
		return result;
	}
}

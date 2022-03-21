package com.manulife.pension.bd.web.bob.blockOfBusiness.util;

import java.util.Map;

import com.manulife.pension.platform.web.util.LabelInfoBean;

/**
 * This class will hold information retrieved from XML file.
 * 
 * The filter information will be discretely stored in two maps, corresponding to advance filters,
 * quick filters.
 * 
 * @author harlomte
 * 
 */
public class BOBFilterMap {
    private Map<String, LabelInfoBean> applicableAdvFilters;

    private Map<String, LabelInfoBean> applicableQuickFilters;

    public Map<String, LabelInfoBean> getApplicableAdvFilters() {
        return applicableAdvFilters;
    }

    public void setApplicableAdvFilters(Map<String, LabelInfoBean> applicableAdvFilters) {
        this.applicableAdvFilters = applicableAdvFilters;
    }

    public Map<String, LabelInfoBean> getApplicableQuickFilters() {
        return applicableQuickFilters;
    }

    public void setApplicableQuickFilters(Map<String, LabelInfoBean> applicableQuickFilters) {
        this.applicableQuickFilters = applicableQuickFilters;
    }
}

package com.manulife.pension.ps.web.profiles;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;

/**
 * @author Charles Chan
 */
public class ManageUsersReportForm extends ReportForm {

	public static final String FIELD_FILTER_VALUE = "filterValue";

	private String filter;

	private String filterValue;
	
    private boolean searchButtonHit;

    private boolean showAllButtonHit;

    
	public String getFilterTextValue() {
		if (StringUtils.equals(filter, ManageUsersReportData.FILTER_EMPLOYEE_NUMBER) || 
				StringUtils.equals(filter, ManageUsersReportData.FILTER_INTERNAL_USER_ID) ||
				StringUtils.equals(filter, ManageUsersReportData.FILTER_EMPLOYEE_LAST_NAME)) {
			return filterValue;
		} else {
			return "";
		}
	}

	public void setFilterTextValue(String filterTextValue) {
	}

	public boolean isSearchButtonHit() {
		return searchButtonHit;
	}

	public void setSearchButtonHit(boolean searchButtonHit) {
		this.searchButtonHit = searchButtonHit;
		if (searchButtonHit) {
		    showAllButtonHit = false;
		}
	}

	/**
	 * Constructor.
	 */
	public ManageUsersReportForm() {
		super();
	}

	/**
	 * @return Returns the filterType.
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filterType
	 *            The filterType to set.
	 */
	public void setFilter(String filter) {
		this.filter = trim(filter);
	}

	/**
	 * @return Returns the filterValue.
	 */
	public String getFilterValue() {
		return filterValue;
	}

	/**
	 * @param filterValue
	 *            The filterValue to set.
	 */
	public void setFilterValue(String filterValue) {
		this.filterValue = trim(filterValue);
	}
	/**
	 * @see ReportForm#clear()
	 */
 	public void clear() {
		super.clear();
		setFilter(null);
		setFilterValue(null);
	}

    /**
     * @return the showAllButtonHit
     */
    public boolean isShowAllButtonHit() {
        return showAllButtonHit;
    }

    /**
     * @param showAllButtonHit the showAllButtonHit to set
     */
    public void setShowAllButtonHit(boolean showAllButtonHit) {
        this.showAllButtonHit = showAllButtonHit;
        if (showAllButtonHit) {
            searchButtonHit = false;
        }
    }
    
    public List<LabelValueBean> getPSWRoles() {
    	return ManageInternalUserHelper.getPSWRoles();
    }

    public List<LabelValueBean> getBDWRoles() {
    	return ManageInternalUserHelper.getBDWRoles();
    }
    
	public String getPSWRole() {
		if (StringUtils.equals(filter, ManageUsersReportData.FILTER_PSW_ROLE)) {
			return filterValue;
		} else {
			return ManageUsersReportData.ALL_ROLE;
		}
	}

	public void setPSWRole(String role) {
	}

	public String getBDWRole() {
		if (StringUtils.equals(filter, ManageUsersReportData.FILTER_BDW_ROLE)) {
			return filterValue;
		} else {
			return ManageUsersReportData.ALL_ROLE;
		}
	}

	public void setBDWRole(String role) {
	}
	
	public boolean isFilterOnPSWRole() {
		return StringUtils.equals(filter, ManageUsersReportData.FILTER_PSW_ROLE);
	}


	public boolean isFilterOnBDWRole() {
		return StringUtils.equals(filter, ManageUsersReportData.FILTER_BDW_ROLE);		
	}
}
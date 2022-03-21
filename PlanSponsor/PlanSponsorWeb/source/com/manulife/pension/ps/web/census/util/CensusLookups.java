/**
 * 
 */
package com.manulife.pension.ps.web.census.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.eligibility.EligibilityDataHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.vesting.util.PlanYear;
import com.manulife.util.render.RenderConstants;
import com.manulife.pension.cache.CodeLookupCache;

/**
 * The utility class encapsulating the Lookups for census
 * 
 * @author guweigu
 * 
 */
public class CensusLookups {
    private static final Logger log = Logger.getLogger(CensusLookups.class);

    /**
     * Supported lookup types
     */
    public static final String YesNo = "yesNo";
    public static final String YesNoWithoutBlank = "yesNoWithoutBlank";
    
    public static final String State = "state";

    public static final String StateOfResidence = "stateOfResidence";

    public static final String Country = "country";

    public static final String EmploymentStatus = "employmentStatus";
    
    public static final String EmploymentStatusWithoutC = "employmentStatusWithoutC";

    public static final String BooleanString = "boolean";

    public static final String BooleanStringWithoutBlank = "booleanWithoutBlank";

    public static final String NamePrefix = "namePrefix";
    
    public static final String Segment = "segment";
    
    public static final String SourceChannelCode = "sourceChannelCode";
    
    public static final String PeriodOfServiceUnit = "periodOfServiceUnit";
    
    public static final String PlanEntryFrequency = "planEntryFrequency";
    
    public static final String ServiceCreditingMethod = "serviceCreditingMethod";
    
    public static final String ComputationPeriod = "computationPeriod";
    
    public static final String IntegerValues = "integer";
    
    /**
     * supoorted lookup lists
     */
    private List<LabelValueBean> statesList = new ArrayList<LabelValueBean>();

    private List<LabelValueBean> stateOfResidenceList = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> countryList = new ArrayList<LabelValueBean>();

    private List<LabelValueBean> employmentStatusList = new ArrayList<LabelValueBean>();

    private List<LabelValueBean> employmentStatusListWithoutC = new ArrayList<LabelValueBean>();

    private List<LabelValueBean> booleanStringList = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> booleanStringListWithoutBlank = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> segmentList = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> namePrefixList = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> optOutIndList = new ArrayList<LabelValueBean>();

    private Set<String>  countrySet = new HashSet<String>();
    
    private List<LabelValueBean> sourceChannelCodeList = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> periodOfServiceUnitList = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> planEntryFrequencyList = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> serviceCreditingMethodList = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> computationPeriodList = new ArrayList<LabelValueBean>();
    
    private List<LabelValueBean> integerValueList = new ArrayList<LabelValueBean>();
    
    private static CensusLookups instance = new CensusLookups();

    /*
     * The map key = lookup type name value = lookup list for the type
     */
    private Map<String, List<LabelValueBean>> lookupMap = new HashMap<String, List<LabelValueBean>>();

    /**
     * Singleton instance
     * 
     * @return singleton instance
     */
    public static CensusLookups getInstance() {
        return instance;
    }

    /**
     * Construtor.
     * 
     */
    private CensusLookups() {

        booleanStringList.add(new LabelValueBean("", ""));
        booleanStringList.add(new LabelValueBean("Yes", "Y"));
        booleanStringList.add(new LabelValueBean("No", "N"));
        
        booleanStringList = Collections.unmodifiableList(booleanStringList);
        
        integerValueList.add(new LabelValueBean("1", "1"));
        integerValueList.add(new LabelValueBean("2", "2"));
        integerValueList.add(new LabelValueBean("3", "3"));
        
        integerValueList = Collections.unmodifiableList(integerValueList);
        
        booleanStringListWithoutBlank.add(new LabelValueBean("Yes", "Y"));
        booleanStringListWithoutBlank.add(new LabelValueBean("No", "N"));
        
        booleanStringListWithoutBlank = Collections.unmodifiableList(booleanStringListWithoutBlank);
        
        namePrefixList.add(new LabelValueBean("", ""));
        namePrefixList.add(new LabelValueBean("DR.", "DR."));
        namePrefixList.add(new LabelValueBean("MR.", "MR."));
        namePrefixList.add(new LabelValueBean("MRS.", "MRS."));
        namePrefixList.add(new LabelValueBean("MS.", "MS."));
        
        namePrefixList = Collections.unmodifiableList(namePrefixList);
        
        optOutIndList.add(new LabelValueBean("", ""));
        optOutIndList.add(new LabelValueBean("Yes", "Y"));
        optOutIndList.add(new LabelValueBean("No", "N"));
        
        optOutIndList = Collections.unmodifiableList(optOutIndList);
        
        segmentList.add(new LabelValueBean("All Employees", ""));
        segmentList.add(new LabelValueBean("Account Holders", "1"));
        segmentList.add(new LabelValueBean("Non-Account Holders", "0"));

        segmentList = Collections.unmodifiableList(segmentList);
        
        lookupMap.put(YesNo, booleanStringList);
        lookupMap.put(YesNoWithoutBlank, booleanStringListWithoutBlank);

        // load lookups from database
        try {
            loadCountries();
            loadStates();
            loadStateOfResidenceList();
            loadEmployeeStatuses();
            loadSourceChannelCodes();
            loadComputationPeriodCodes();
            loadServiceCreditingMethodCodes();
            loadPlanEntryFrequencyListCodes();
            loadPeriodOfServiceUnitCodes();
        } catch (SystemException e) {
            log.error("Fail to load lookups", e);
            throw new RuntimeException("Fail to load lookups", e);
        }
        
        // put the lookups into the lookupMap
        lookupMap.put(State, getStates());
        lookupMap.put(StateOfResidence, getStateOfResidence());
        lookupMap.put(Country, getCountries());
        lookupMap.put(EmploymentStatus, getEmploymentStatuses());
        lookupMap.put(EmploymentStatusWithoutC, getEmploymentStatusesWithoutC());
        lookupMap.put(BooleanString, booleanStringList);
        lookupMap.put(NamePrefix, namePrefixList);
        lookupMap.put(Segment, segmentList);
        lookupMap.put(SourceChannelCode, sourceChannelCodeList);
        lookupMap.put(ComputationPeriod, computationPeriodList);
        lookupMap.put(ServiceCreditingMethod, serviceCreditingMethodList);
        lookupMap.put(PlanEntryFrequency, planEntryFrequencyList);
        lookupMap.put(PeriodOfServiceUnit, periodOfServiceUnitList);
        lookupMap.put(IntegerValues, integerValueList);
    }


    /**
     * Load the countries from Environment Service.
     * 
     * @throws SystemException
     */
    private void loadCountries() throws SystemException {
        countryList.add(new LabelValueBean("", ""));
        countryList.add(new LabelValueBean("USA", "USA"));
        countryList.add(new LabelValueBean("CANADA", "CANADA"));
        countryList.add(new LabelValueBean("MEXICO", "MEXICO"));
        countrySet.add("USA");
        countrySet.add("CANADA");
        countrySet.add("MEXICO");
        Map countryMap = EnvironmentServiceDelegate.getInstance(Constants.PS_APPLICATION_ID)
                .getCountries();
        TreeSet sortedCountries = new TreeSet(countryMap.values());
        Iterator iter = sortedCountries.iterator();
        while (iter.hasNext()) {
            String value = (String) iter.next();
            if (value.equals("USA") || value.equals("CANADA") || value.equals("CAN") || value.equals("MEXICO") || value.equals("MEX")) {
                continue;
            }
            countryList.add(new LabelValueBean(value, value));
            countrySet.add(value);
        }
    }

    /**
     * Load the states from Environment Service.
     * 
     * @throws SystemException
     */
    private void loadStates() throws SystemException {
        statesList.add(new LabelValueBean("Select", ""));
        Map usaStates = EnvironmentServiceDelegate.getInstance(Constants.PS_APPLICATION_ID)
                .getUSAStates();
        TreeSet sortedStates = new TreeSet(usaStates.keySet());
        Iterator iter = sortedStates.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            statesList.add(new LabelValueBean(key, key));
        }
        statesList = Collections.unmodifiableList(statesList);
    }
    
    /**
     * Load the states from Environment Service.
     * 
     * @throws SystemException
     */
    private void loadStateOfResidenceList() throws SystemException {
        stateOfResidenceList.add(new LabelValueBean("Select", ""));
        Map usaStates = EnvironmentServiceDelegate.getInstance(Constants.PS_APPLICATION_ID)
                .getUSAGeographicalStatesOnly();
        TreeSet sortedStates = new TreeSet(usaStates.keySet());
        Iterator iter = sortedStates.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            stateOfResidenceList.add(new LabelValueBean(key, key));
        }
        stateOfResidenceList = Collections.unmodifiableList(stateOfResidenceList);
    }


    /**
     * Load the employee statuses from Employee Service.
     * 
     * @throws SystemException
     */
    private void loadEmployeeStatuses() throws SystemException {
        Map empStatus = EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID)
                .getEmployeeStatusList();

        TreeSet sortedSet = new TreeSet(empStatus.keySet());
        Iterator iter = sortedSet.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = (String) empStatus.get(key);
            employmentStatusList.add(new LabelValueBean(value, key));
        }
        employmentStatusList = Collections.unmodifiableList(employmentStatusList);

        List<LabelValueBean> statuses = employmentStatusList;
        for (LabelValueBean bean : statuses) {
            if (!"C".equals(bean.getValue())) {
                employmentStatusListWithoutC.add(bean);
            }
        }
        employmentStatusList = Collections.unmodifiableList(employmentStatusList);
    }

    public List<LabelValueBean> getEmploymentStatusesWithoutC() {
        return employmentStatusListWithoutC;
    }
    
    /**
     * Returns list of counntries lookup.
     * 
     * @return The list of countries lookups.
     */
    public List<LabelValueBean> getCountries() {
        return countryList;
    }

    public Set<String> getCountrySet() {
        return countrySet;        
    }
    
    /**
     * Returns the list of states lookup.
     * 
     * @return The states lookups.
     */
    public List<LabelValueBean> getStates() {
        return statesList;
    }

    public List<LabelValueBean> getStateOfResidence() {
        return stateOfResidenceList;
    }
    
    /**
     * Returns a list of employee status lookups
     * 
     * @return A list of employee status
     */
    public List<LabelValueBean> getEmploymentStatuses() {
        return employmentStatusList;
    }

    public List<LabelValueBean> getNamePrefix() {
        return namePrefixList;
    }
    
    public List<LabelValueBean> getOptOutInd() {
        return optOutIndList;
    }

    public List<LabelValueBean> getSegments() {
        return segmentList;
    }

    public List<LabelValueBean> getPlanYearEnds(Integer contractNumber) throws SystemException {
        final PlanData planData = ContractServiceDelegate.getInstance()
                .readPlanData(contractNumber);
        PlanYear year = new PlanYear(planData.getPlanYearEnd().getAsDate(), new Date());
        FastDateFormat dateFormatter = FastDateFormat
                .getInstance(RenderConstants.MEDIUM_MDY_SLASHED);
        List<LabelValueBean> planYearEnds = new ArrayList<LabelValueBean>();
        String dValue = dateFormatter.format(year.getLastPlanYearEndDate());
        planYearEnds.add(new LabelValueBean(dValue, dValue));
        year.previousYear();
        dValue = dateFormatter.format(year.getLastPlanYearEndDate());
        planYearEnds.add(new LabelValueBean(dValue, dValue));
        return planYearEnds;
    }
    
    public List<LabelValueBean> getLookup(String type) {
        return lookupMap.get(type);
    }
    
    /**
     * Given a lookup type and lookup key, returns the Lookup description value.
     * 
     * @param type The lookup type.
     * @param key The lookup key under the type.
     * @return The lookup description value.
     */
    public String getLookupValue(String type, String key) {
        List lookups = (List) lookupMap.get(type);
        if (lookups == null) {
            throw new IllegalArgumentException("No such type supported : " + type);
        }
        return getLookupValue(lookups, key);
    }

    /**
     * Given a list of lookups and lookup key, returns the Lookup description value.
     * 
     * @param lookups The list of lookups
     * @param key The lookup key under the type.
     * @return The lookup description value.
     */
    protected String getLookupValue(List lookups, String key) {
        // for blank keey return a blank value to
        // aovid return sth like 'please select....'
        String keyValue = (key == null) ? "" : key.trim();
        if (keyValue.length() == 0)
            return "";

        for (int i = lookups.size() - 1; i >= 0; i--) {
            LabelValueBean bean = (LabelValueBean) lookups.get(i);
            if (bean.getValue().equals(keyValue)) {
                return bean.getLabel();
            }
        }
        return "";
    }
    
    /**
     * Load the Source Channel Codes from the lookup table.
     * 
     * @throws SystemException
     */
    private void loadSourceChannelCodes() throws SystemException {
        
        final Collection<String> lookupKeys = new ArrayList<String>();
        lookupKeys.add(CodeLookupCache.SOURCE_CHANNEL_CODE);
        
        Map data = EnvironmentServiceDelegate.getInstance(Constants.PS_APPLICATION_ID)
        .getLookupData(lookupKeys);
                     
        Map sourceMap = (Map)data.get(CodeLookupCache.SOURCE_CHANNEL_CODE);
        Iterator iter = sourceMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String label = (String) sourceMap.get(key);
            sourceChannelCodeList.add(new LabelValueBean(label, key));
        }
        
        sourceChannelCodeList = Collections.unmodifiableList(sourceChannelCodeList);
    }

    /**
     * Load the service crediting method Codes from the lookup table.
     * 
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
	private void loadServiceCreditingMethodCodes() throws SystemException {

		final Collection<String> lookupKeys = new ArrayList<String>();
		lookupKeys.add(CodeLookupCache.SERVICE_CREDITING_METHOD);

		Map<String, Collection<DeCodeVO>> data = EnvironmentServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID).getLookupData(
						lookupKeys);

		for (DeCodeVO vo : data.get(CodeLookupCache.SERVICE_CREDITING_METHOD)) {
			serviceCreditingMethodList.add(new LabelValueBean(vo
					.getDescription(), vo.getCode()));
		}

		serviceCreditingMethodList = Collections
				.unmodifiableList(serviceCreditingMethodList);

	}
    
    /**
     * Load the Period Of Service Codes from the lookup table.
     * 
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
	private void loadPeriodOfServiceUnitCodes() throws SystemException {

		final Collection<String> lookupKeys = new ArrayList<String>();
		lookupKeys.add(CodeLookupCache.PERIOD_OF_SERVICE_UNIT);

		Map<String, Collection<DeCodeVO>> data = EnvironmentServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID).getLookupData(
						lookupKeys);

		for (DeCodeVO vo : data.get(CodeLookupCache.PERIOD_OF_SERVICE_UNIT)) {
			periodOfServiceUnitList.add(new LabelValueBean(vo.getDescription(),
					vo.getCode()));
		}

		periodOfServiceUnitList = Collections
				.unmodifiableList(periodOfServiceUnitList);

	}
    
    /**
     * Load the Plan Frequency Codes from the lookup table.
     * 
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
	private void loadPlanEntryFrequencyListCodes() throws SystemException {

		final Collection<String> lookupKeys = new ArrayList<String>();
		lookupKeys.add(CodeLookupCache.PLAN_FREQUENCY);

		Map<String, Collection<DeCodeVO>> data = EnvironmentServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID).getLookupData(
						lookupKeys);

		for (DeCodeVO vo : data.get(CodeLookupCache.PLAN_FREQUENCY)) {
			planEntryFrequencyList.add(new LabelValueBean(vo.getDescription(),
					vo.getCode()));
		}

		planEntryFrequencyList = Collections
				.unmodifiableList(planEntryFrequencyList);
	}
    
    /**
     * TODO the codes are not there in LOOKUP table
     * Hence hard coding here
     * 
     * Load the computation Period Codes from the lookup table.
     * 
     * @throws SystemException
     */
    private void loadComputationPeriodCodes() throws SystemException {

		// final Collection<String> lookupKeys = new ArrayList<String>();
		// lookupKeys.add(CodeLookupCache.SOURCE_CHANNEL_CODE);

		// Map<String, String> data =
		// EnvironmentServiceDelegate.getInstance(Constants.PS_APPLICATION_ID)
		// .getLookupData(lookupKeys);

		//Map<String, String> data = new HashMap<String, String>();
		//data.put("I", "Initial");
		//data.put("S", "Subsequent");

		//for (Map.Entry<String, String> entrySet : data.entrySet()) {
		//	String key = entrySet.getKey();
		//	String label = entrySet.getValue();
		//	computationPeriodList.add(new LabelValueBean(label, key));
		//}
    	
    	computationPeriodList.add(new LabelValueBean("Initial", "I"));
    	computationPeriodList.add(new LabelValueBean("Subsequent", "S"));

		computationPeriodList = Collections
				.unmodifiableList(computationPeriodList);
	}
}

package com.manulife.pension.bd.web.registration.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;

/**
 * This is an utility class for address.
 * 
 * @author Ilamparithi
 * 
 */
public class AddressUtil {

    private static String SelectLabel = "- Select -";

    private static final AddressUtil instance = new AddressUtil();

    private List<LabelValueBean> statesList;

    private List<LabelValueBean> countriesList;

    private static Logger log = Logger.getLogger(AddressUtil.class);

    public static AddressUtil getInstance() {
        return instance;
    }

    private AddressUtil() {
        List<LabelValueBean> tempList = new ArrayList<LabelValueBean>();
        tempList.add(new LabelValueBean(SelectLabel, ""));
        tempList.addAll(getUSStates());
        statesList = Collections.unmodifiableList(tempList);
        countriesList = Collections.unmodifiableList(getAllCountries());
    }

    public List<LabelValueBean> getStatesList() {
        return statesList;
    }

    public List<LabelValueBean> getCountriesList() {
        return countriesList;
    }

    /**
     * Returns a list of states that belong to US.
     * 
     * @return List a List of LabelValueBean objects with state code as Label and Value since we
     *         have to show the state codes in the screen. But the DAO returns the state codes and
     *         state names.
     */
    @SuppressWarnings("unchecked")
    private List<LabelValueBean> getUSStates() {
        List<LabelValueBean> tempList = new ArrayList<LabelValueBean>();
        try {
            Map<String, String> states = EnvironmentServiceDelegate.getInstance(
                    BDConstants.BD_APPLICATION_ID).getUSAGeographicalStatesOnly();
            for (Iterator<String> it = states.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                // We have to show the state codes in the screen.
                tempList.add(new LabelValueBean(key.toString(), key.toString()));
            }
            Collections.sort(tempList, new Comparator<LabelValueBean>() {
                public int compare(LabelValueBean o1, LabelValueBean o2) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }
            });
        } catch (SystemException e) {
            log.error("Fail to load the US states", e);
        }
        return tempList;
    }

    /**
     * Returns a list of countries.
     * 
     * @return List a List of LabelValueBean objects with country name as Label and country code as
     *         value.
     */
    private List<LabelValueBean> getAllCountries() {
        List<LabelValueBean> tempList = new ArrayList<LabelValueBean>();
        try {
            Collection<com.manulife.pension.service.environment.valueobject.LabelValueBean> countries = EnvironmentServiceDelegate
                    .getInstance(BDConstants.BD_APPLICATION_ID).getCountriesForLookup();
            for (Iterator<com.manulife.pension.service.environment.valueobject.LabelValueBean> it = countries
                    .iterator(); it.hasNext();) {
                com.manulife.pension.service.environment.valueobject.LabelValueBean country = it
                        .next();
                tempList.add(new LabelValueBean(country.getLabel(), country.getValue()));
            }

        } catch (SystemException e) {
            log.error("Fail to load the countries", e);
        }
        return tempList;
    }

}

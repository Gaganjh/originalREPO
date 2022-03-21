package com.manulife.pension.platform.web.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.FundClassVO;

/**
 * This class is the Utility class for the web tier to handle data for the fund
 * classes.
 * 
 * @author kuthiha
 * 
 */
public class FundClassUtility {

    private static LinkedHashMap<String, FundClassVO> fundClassCollection;

    private static FundClassUtility instance = new FundClassUtility();

    private static final Logger logger = Logger
            .getLogger(FundClassUtility.class);

    /**
     * Default constructor
     * 
     */
    private FundClassUtility() {

    }

    /**
     * Loads the all the Fund class data from the service.
     * 
     * @throws SystemException
     */
    private static void loadFundClassData() throws SystemException {
        if (fundClassCollection == null) {
            try {
                fundClassCollection = FundServiceDelegate.getInstance()
                        .getAllFundClasses();
            } catch (SystemException e) {
                logger
                        .error("Class: FundClassUtility :: - unable to get the Fund Class Collection from the service");
                throw e;
            }
        }
    }

    /**
     * Returns the fully loaded instance of FundClassUtility
     * 
     * @return FundClassUtility
     * @throws SystemException
     */
    public static FundClassUtility getInstance() throws SystemException {
        loadFundClassData();
        return instance;
    }

    /**
     * Returns the short name for the fund class
     * 
     * @param rateType
     * @return
     * @deprecated Use getFundClassNumber(String rateType)
     */
    public String getFundClassShortName(String rateType) {
        String shortName = null;
        Set<String> keys = fundClassCollection.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            FundClassVO fundVo = fundClassCollection.get(key);
            if (StringUtils.equals(rateType, fundVo.getFundClassId())) {
                shortName = fundVo.getFundClassShortName();
                break;
            }
        }

        return shortName;
    }

    /**
     * Returns the class number this is supposed to be the way we display class
     * in columns on Broker Dealer, and in the future anywhere that we wish to
     * display the shortest form of the class. This new method is designed to
     * replace the deprecated getClassShortName method.
     * 
     * @param rateType
     * @return String - the integer part of the class with no leading 0: C01 ...
     *         C10...C99. We no longer use the C01 versions on the web anymore.
     *         We are to use only an integer representation of the class. So a
     *         fund with a fund class short name of "C01" will return String =
     *         "1" A fund with a fund class short name of "C09" will return
     *         String = "9"
     * 
     */
    public String getFundClassNumber(String rateType) {
        String shortName = null;
        String classNumber = "";

        Set<String> keys = fundClassCollection.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            FundClassVO fundVo = fundClassCollection.get(key);
            if (StringUtils.equals(rateType, fundVo.getFundClassId())) {
                shortName = fundVo.getFundClassShortName();
                if ((shortName != null) && (shortName.indexOf("C") == 0)) {
                	if(shortName.equals("C0P")){
                    	String jhiFundclass ="";
                    	 shortName = shortName.substring(1);
                    	 String[] shortNames = shortName.split("P");
                    	 for (String newShortName :shortNames)
                    		  jhiFundclass = newShortName;
                    	 classNumber = new Integer(jhiFundclass).toString();
                    }
                	else if(shortName.equals("CY1")||shortName.equals("CY2")){
                		 classNumber = new Integer(0).toString();
                	}
                	else{
                	classNumber = new Integer(shortName.substring(1))
                            .toString();
                    }
                }
                break;
            }
        }

        return classNumber;
    }
    
    public String getBaseClass(int classOrderNo) {
        String classId = null;

        Set<String> keys = fundClassCollection.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            classId = it.next();
            FundClassVO fundVo = fundClassCollection.get(classId);
            if (classOrderNo == fundVo.getSortOrder()) {
                break;
            }
        }
        return classId;
    }
    
    /**
     * Method to retrieve the fund class name by providing the rate type.
     * 
     * @param rateType
     * @return String
     */
    public String getFundClassName(String rateType) {
        String fundClassName = null;
        Set<String> keys = fundClassCollection.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            FundClassVO fundVo = fundClassCollection.get(key);
            if (StringUtils.equals(rateType, fundVo.getFundClassId())) {
            	fundClassName = fundVo.getFundClassName();
                break;
            }
        }

        return fundClassName;
    }
    
    /**
     * Method to retrieve the fund class display name by providing the rate type.
     * 
     * @param rateType
     * @return String
     */
    public String getFundClassMediumName(String rateType) {
        String classMediumName = null;
        Set<String> keys = fundClassCollection.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            FundClassVO fundVo = fundClassCollection.get(key);
            if (StringUtils.equals(rateType, fundVo.getFundClassId())) {
            	classMediumName = fundVo.getClassMediumName();
                break;
            }
        }

        return classMediumName;
    }
    
}

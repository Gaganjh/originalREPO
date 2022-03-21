package com.manulife.pension.bd.web.navigation;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.delegate.BrokerServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.broker.valueobject.FirmLandingPages;

public class BDFirmLandingPages {
    public static final Logger logger = Logger.getLogger(BDFirmLandingPages.class);

    private static BDFirmLandingPages instance = null;

    private HashMap<String, FirmLandingPages> cmaKeyMap = null;

    // CMA Landing Page Keys.
    private final static Integer INDEPENDENT_PRIME_LANDING_PAGE_CMA_KEY = new Integer(66309);

    private final static Integer INDEPENDENT_PARTNERING_LANDING_PAGE_CMA_KEY = new Integer(66310);

    private final static Integer INDEPENDENT_LITERATURE_LANDING_PAGE_CMA_KEY = new Integer(66311);

    private BDFirmLandingPages() {
        // Do not allow external instantiation.
    }

    public static BDFirmLandingPages getInstance() {
        if (instance == null) {
            instance = new BDFirmLandingPages();
            initialize(instance);
        }
        return instance;
    }

    private static void initialize(BDFirmLandingPages anInstance) {
        BrokerServiceDelegate delegate = BrokerServiceDelegate
                .getInstance(BDConstants.BD_APPLICATION_ID);
        try {
            anInstance.setCmaKeyMap((HashMap<String, FirmLandingPages>) delegate
                    .getAllFirmLandingPageCMAKeys());
        } catch (SystemException e) {
            logger
                    .error("Exception attempting to initialize the BDFirmLandingPages Map (loaded from CSDB): "
                            + e.getMessage());
        }
    }

    public int getPrimeLandingPageCMAKey(String approvingFirmCode) {
        int cmaKey = INDEPENDENT_PRIME_LANDING_PAGE_CMA_KEY;
        try {
            if (this.cmaKeyMap.containsKey(approvingFirmCode.toUpperCase().trim())) {
                cmaKey = this.cmaKeyMap.get(approvingFirmCode).getPrimeLandingPageCMAKey();
            }
        } catch (Exception e) {
            logger
                    .error("Exception attempting obtain PRIME Landing Page CMA Key for National Account: "
                            + approvingFirmCode + " - Details: " + e.getMessage());
            cmaKey = INDEPENDENT_PRIME_LANDING_PAGE_CMA_KEY;
        }
        return cmaKey;
    }

    public int getPartneringLandingPageCMAKey(String approvingFirmCode) {
        int cmaKey = INDEPENDENT_PARTNERING_LANDING_PAGE_CMA_KEY;
        try {
            if (this.cmaKeyMap.containsKey(approvingFirmCode.toUpperCase().trim())) {
                cmaKey = this.cmaKeyMap.get(approvingFirmCode).getPartneringLandingPageCMAKey();
            }
        } catch (Exception e) {
            logger
                    .error("Exception attempting obtain PARTNERING Landing Page CMA Key for National Account: "
                            + approvingFirmCode + " - Details: " + e.getMessage());
            cmaKey = INDEPENDENT_PARTNERING_LANDING_PAGE_CMA_KEY;
        }
        return cmaKey;
    }

    public int getLiteratureLandingPageCMAKey(String approvingFirmCode) {
        int cmaKey = INDEPENDENT_LITERATURE_LANDING_PAGE_CMA_KEY;
        try {
            if (this.cmaKeyMap.containsKey(approvingFirmCode.toUpperCase().trim())) {
                cmaKey = this.cmaKeyMap.get(approvingFirmCode).getLiteratureLandingPageCMAKey();
            }
        } catch (Exception e) {
            logger
                    .error("Exception attempting obtain LITERATURE Landing Page CMA Key for National Account: "
                            + approvingFirmCode + " - Details: " + e.getMessage());
            cmaKey = INDEPENDENT_LITERATURE_LANDING_PAGE_CMA_KEY;
        }
        return cmaKey;
    }

    public void setCmaKeyMap(HashMap<String, FirmLandingPages> cmaKeyMap) {
        this.cmaKeyMap = cmaKeyMap;
    }
}

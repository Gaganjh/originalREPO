package com.manulife.pension.ps.web.util;

import java.util.Collection;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.ContractPilotList;
import com.manulife.pension.util.log.LogUtility;

/**
 * Utilities and helpers for the Pilot.
 * 
 * @author celarro
 */
public class PilotHelper {

    public PilotHelper() {
    }

    /**
     * Detetermines if the named pilot is active for the given contract
     * 
     * @param contractId
     * @param name
     * @return
     */
    public static boolean isInPilot(int contractId, String name) {
        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        boolean isInPilot = false;
        try {
            ContractPilotList pilotList = service.getContractPilot(contractId, name);
            isInPilot = pilotList.isPilotEnabled(name).booleanValue();
        } catch (ApplicationException ae) {
            LogUtility.log(ae);
        } catch (SystemException se) {
            LogUtility.log(se);
        }
        return isInPilot;
    }
    
    /**
     * Detetermines if any pilot projects are available
     *
     * @return
     */
    public static boolean isAnyPilotAvailable() {
        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        boolean isAnyPilot = false;
        try {
            Collection<String> pilotList = service.getPilotList();
            isAnyPilot = pilotList.size() > 0 ? true : false;
        } catch (SystemException se) {
            LogUtility.log(se);
        }
        return isAnyPilot;
    }
}

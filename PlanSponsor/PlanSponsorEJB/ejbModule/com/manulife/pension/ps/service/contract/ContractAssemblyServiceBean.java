package com.manulife.pension.ps.service.contract;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.exception.ApolloBackEndException;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ParticipantACIFeatureUpdateVO;
import com.manulife.pension.service.contract.valueobject.PlanData;

public class ContractAssemblyServiceBean implements SessionBean {
    private static final long serialVersionUID = -2929989432575277461L;

    private Logger logger = Logger.getLogger(ContractAssemblyServiceBean.class);

    private SessionContext mySessionCtx;

    /**
     * getSessionContext
     */
    public SessionContext getSessionContext() {
        return mySessionCtx;
    }

    /**
     * setSessionContext
     */
    public void setSessionContext(SessionContext ctx) {
        mySessionCtx = ctx;
    }

    /**
     * ejbActivate
     */
    public void ejbActivate() {
    }

    /**
     * ejbCreate
     */
    public void ejbCreate() throws CreateException {
    }

    /**
     * ejbPassivate
     */
    public void ejbPassivate() {
    }

    /**
     * ejbRemove
     */
    public void ejbRemove() {
    }

    /**
     * Saves the plan data and triggers the eligibility calculation request
     * 
     * @param planData
     * @throws SystemException
     */
	public void savePlanDataAndTriggerECRequest(final PlanData planData)
			throws SystemException {
		
		try{
		// Get the existing eligibility plan data
		PlanData existingData = ContractServiceDelegate.getInstance()
				.loadPlanEligibilityData(planData.getContractId());

		//Save the plan data
		ContractServiceDelegate.getInstance().savePlanData(planData);

		// Trigger the ED and PED calculation
		EligibilityServiceDelegate.getInstance(
				GlobalConstants.PSW_APPLICATION_ID)
				.triggerPlanEDOrPEDCalculation(existingData, planData);
		} catch (SystemException ex) {
			//If any exception occurs, then rollback the entire transaction
			getSessionContext().setRollbackOnly();
			throw ex;
		}
	}
	
	/**
	 * Saves the CSF data and triggers the eligibility calculation request 
	 * 
	 * @param changedCsfCollection
	 * @param paf
	 * @throws SystemException
	 * @throws ApolloBackEndException 
	 */
	public void saveCSFAndTriggerECRequest(
			Collection<ContractServiceFeature> changedCsfCollection,
			ParticipantACIFeatureUpdateVO paf) throws SystemException, ApolloBackEndException {
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> saveCSFAndTriggerECRequest");
		
		ContractServiceDelegate service = ContractServiceDelegate.getInstance();
		
		try {
			if (paf.isNotEmpty()) {
				service.updateCSFAndParticipantAciFields(paf,
						changedCsfCollection);
			} else {
				service.updateContractServiceFeatures(changedCsfCollection);
			}

			EligibilityServiceDelegate.getInstance(
					GlobalConstants.PSW_APPLICATION_ID)
					.triggerCSFEDOrPEDCalculation(changedCsfCollection);
		} catch (ApolloBackEndException ae) {
			//If any exception occurs, then rollback the entire transaction
			getSessionContext().setRollbackOnly();
            throw ae;
        } catch (ApplicationException ae) {
			//If any exception occurs, then rollback the entire transaction
			getSessionContext().setRollbackOnly();
			throw new SystemException(ae, "saveContractServiceFeatureData"
					+ ae.getDisplayMessage());
		} catch (SystemException ex) {
			//If any exception occurs, then rollback the entire transaction
			getSessionContext().setRollbackOnly();
			throw ex;
		}
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> saveCSFAndTriggerECRequest");
	}
}

package com.manulife.pension.ps.service.delegate;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.ApolloBackEndException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.contract.ContractAssemblyService;
import com.manulife.pension.ps.service.contract.ContractAssemblyServiceHome;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ParticipantACIFeatureUpdateVO;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.delegate.AbstractServiceDelegate;

public class ContractAssemblyServiceDelegate extends
		AbstractServiceDelegate {

	private String applicationId;

	private static ContractAssemblyServiceDelegate instance;

	private static final Logger logger = Logger
			.getLogger(ContractAssemblyServiceDelegate.class);

	/**
	 * @param applicationId
	 */
	protected ContractAssemblyServiceDelegate(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return
	 */
	public static ContractAssemblyServiceDelegate getInstance(
			String applicationId) {
		if (instance == null) {
			instance = new ContractAssemblyServiceDelegate(
					applicationId);
		}

		return instance;
	}

	@Override
	protected EJBObject create() throws SystemException, RemoteException,
			CreateException {
		return ((ContractAssemblyServiceHome) getHome()).create();

	}

	@Override
	protected String getApplicationId() {
		return applicationId;
	}

	@Override
	protected String getHomeClassName() {
		return ContractAssemblyServiceHome.class.getName();
	}

	/**
	 * Saves the plan data and triggers the eligibility calculation request
	 * 
	 * @param planData
	 * @throws SystemException
	 */
	public void savePlanDataAndTriggerECRequest(final PlanData planData)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> savePlanDataAndTriggerECRequest");

		try {
			ContractAssemblyService service = (ContractAssemblyService) getService();
			service.savePlanDataAndTriggerECRequest(planData);
		} catch (RemoteException e) {
			throw new SystemException(e,
					"Problem occured savePlanDataAndTriggerECRequest() method call with Exception "
							+ e.getMessage());
		}

		if (logger.isDebugEnabled())
			logger.debug("exit -> savePlanDataAndTriggerECRequest");
	}
	
	/**
	 * Saves the CSF data and triggers the eligibility calculation request
	 * 
	 * @param changedCsfCollection
	 * @param paf
	 * @throws SystemException
	 */
	public void saveCSFAndTriggerECRequest(
			Collection<ContractServiceFeature> changedCsfCollection,
			ParticipantACIFeatureUpdateVO paf) throws ApolloBackEndException, SystemException {
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> saveCSFAndTriggerECRequest");
		
		try {
			ContractAssemblyService service = (ContractAssemblyService) getService();
			service.saveCSFAndTriggerECRequest(changedCsfCollection, paf);
		} catch (RemoteException e) {
			throw new SystemException(e,
					"Problem occured savePlanDataAndTriggerECRequest() method call with Exception "
							+ e.getMessage());
		}
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> saveCSFAndTriggerECRequest");
	}
}

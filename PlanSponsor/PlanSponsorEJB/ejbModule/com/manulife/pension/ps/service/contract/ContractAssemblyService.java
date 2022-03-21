package com.manulife.pension.ps.service.contract;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.manulife.pension.exception.ApolloBackEndException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ParticipantACIFeatureUpdateVO;
import com.manulife.pension.service.contract.valueobject.PlanData;

public interface ContractAssemblyService extends EJBObject {
	public void savePlanDataAndTriggerECRequest(final PlanData planData)
			throws RemoteException, SystemException;

	public void saveCSFAndTriggerECRequest(
			Collection<ContractServiceFeature> changedCsfCollection,
			ParticipantACIFeatureUpdateVO paf) throws ApolloBackEndException, RemoteException, SystemException;
}

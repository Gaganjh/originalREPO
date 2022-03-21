package com.manulife.pension.service.distribution;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.loan.dao.DistributionAtRiskDetailsDAO;


/**
 * 
 * @author Vasanth Balaji G.
 * 
 */

public class AtRiskHandler {

	private static AtRiskHandler instance = new AtRiskHandler();

	public static AtRiskHandler getInstance() {
		return instance;
	}

	private AtRiskHandler() {

	}

	/**
     * This method determine  registration at risk period or not
     * 
     * @param atRiskDetils
     * @return
     * @throws SystemException
     */
    public boolean isRegistrationAtRiskPeriod(AtRiskDetailsInputVO atRiskDetils) throws SystemException {
    	return SecurityServiceDelegate.getInstance().isRegistrationAtRiskPeriod(atRiskDetils);
    }
	
	/**
	 * This method will inserts the {@link DistributionAtRisk} if it does not
	 * exist and update it if it does exist.
	 * 
	 * @param submissionId
	 *            The Submission Id
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public void insertUpdateDistributionAtRiskActivities(List<AtRiskDetailsVO> objects)
			throws DistributionServiceException {
		DistributionAtRiskDetailsDAO.insertOrUpdate(objects);
	}
	
	/**
	 * This method used to Retrieve Participant's forgot wserName information
	 * 
	 * @param atRiskDetils
	 * @return
	 * @throws SystemException
	 */
	public AtRiskForgetUserName getAtRiskActivitiesForgotUserName(AtRiskDetailsInputVO atRiskDetils) 
			throws SystemException {
		return SecurityServiceDelegate.getInstance().getAtRiskActivitiesForgotUserName(atRiskDetils);
	}
	
	/**
     * This method retrieves the
     * Participant's Web registration information from DB (various table).
     * 
     * @param profileId
     * @param contractId
     * @return AtRiskWebRegistrationVO
     */
	public AtRiskWebRegistrationVO getWebRigistrationInfo(AtRiskDetailsInputVO atRiskDetils) throws  SystemException {

		return SecurityServiceDelegate.getInstance().getWebRigistrationInfo(atRiskDetils);			
	}
	
	
	/**
	 * This method will retrieve Participant current Address.
	 * 
	 * @param atRiskDetils
	 * @return
	 * @throws SystemException
	 */
	public AtRiskAddressChangeVO getCurrentAddress(AtRiskDetailsInputVO atRiskDetils) throws  SystemException {

		return SecurityServiceDelegate.getInstance().getCurrentAddress(atRiskDetils);	
	}

	/**
	 * This method retrieves the forgot userName  and password function information.
	 * 
	 * @param atRiskDetils
	 * @return
	 * @throws SystemException
	 */
	public ArrayList<AtRiskPasswordResetVO> getForgotUserNameAndPassowordFunction(AtRiskDetailsInputVO atRiskDetils)throws SystemException {

		return SecurityServiceDelegate.getInstance().getForgotUserNameAndPassowordFunction(atRiskDetils);	
	}
	
	/**
	 * This method retrieves the forgot password information.
	 * 
	 * @param atRiskDetils
	 * @return
	 * @throws RemoteException
	 */
	public AtRiskPasswordResetVO getForgotPasswordFunction(AtRiskDetailsInputVO atRiskDetils) throws SystemException {
		return SecurityServiceDelegate.getInstance().getForgotPasswordFunction(atRiskDetils);
	}
	

}

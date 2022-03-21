package com.manulife.pension.service.withdrawal;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.DistributionContainerEnvironment;
import com.manulife.pension.service.testutility.MockContainerEnvironment;

public class BaseWithdrawalServiceTestCase extends DistributionContainerEnvironment {

    public BaseWithdrawalServiceTestCase() {
        super();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        MockContainerEnvironment.registerMockService(
				"java:comp/env/ejb/WithdrawalServiceLocal",
				WithdrawalServiceLocalHome.class, WithdrawalService.class,
				WithdrawalServiceBean.class);
        
        registerMockService(WithdrawalServiceLocalHome.class, WithdrawalService.class,
                WithdrawalServiceBean.class);

        registerMockService(WithdrawalServiceHome.class, WithdrawalServiceRemote.class,
                WithdrawalServiceBean.class);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    protected WithdrawalService getLocalWithdrawalService() throws SystemException {
        try {
            return WithdrawalServiceUtil.getLocalHome().create();
        } catch (final NamingException namingException) {
            throw new SystemException(namingException, this.getClass().getName(),
                    "getWithdrawalService()", "Lookup failed for "
                            + WithdrawalServiceLocalHome.class.getName() + ".");
        } catch (final CreateException createException) {
            throw new SystemException(createException, this.getClass().getName(),
                    "getWithdrawalService()", "Create failed for "
                            + WithdrawalServiceLocalHome.class.getName() + ".");
        }
    }

    protected WithdrawalServiceRemote getRemoteWithdrawalService() throws SystemException {
        try {
            return WithdrawalServiceUtil.getHome().create();
        } catch (final NamingException namingException) {
            throw new SystemException(namingException, this.getClass().getName(),
                    "getWithdrawalService()", "Lookup failed for "
                            + WithdrawalServiceHome.class.getName() + ".");
        } catch (final CreateException createException) {
            throw new SystemException(createException, this.getClass().getName(),
                    "getWithdrawalService()", "Create failed for "
                            + WithdrawalServiceHome.class.getName() + ".");
        } catch (final RemoteException remoteException) {
            throw new SystemException(remoteException, this.getClass().getName(),
                    "getWithdrawalService()", "Remote exception for "
                            + WithdrawalServiceHome.class.getName() + ".");
        }
    }

}

package com.manulife.pension.ps.web.fee;


import javax.naming.NamingException;

import org.mockejb.SessionBeanDescriptor;

import com.manulife.pension.service.contract.ContractService;
import com.manulife.pension.service.contract.ContractServiceBean;
import com.manulife.pension.service.contract.ContractServiceHome;
import com.manulife.pension.service.environment.EnvironmentService;
import com.manulife.pension.service.environment.EnvironmentServiceBean;
import com.manulife.pension.service.environment.EnvironmentServiceHome;
import com.manulife.pension.service.fee.FeeServiceBean;
import com.manulife.pension.service.fee.FeeServiceHome;
import com.manulife.pension.service.fee.FeeServiceRemote;
import com.manulife.pension.service.testutility.MockContainerEnvironment;
import com.manulife.pension.util.log.MrlLogger;
import com.manulife.pension.util.log.MrlLoggerBean;
import com.manulife.pension.util.log.MrlLoggerHome;

public class EcrMockContainer {
    
    public void setUp()
    throws NamingException {
        
        MockContainerEnvironment.initialize();
        
        SessionBeanDescriptor feeServiceDescriptor =
                new SessionBeanDescriptor(
                    FeeServiceHome.class.getName(),
                    FeeServiceHome.class,
                    FeeServiceRemote.class,
                    FeeServiceBean.class);
        MockContainerEnvironment.getMockContainer().deploy(feeServiceDescriptor);

        SessionBeanDescriptor mrlLoggerServiceDescriptor =
            new SessionBeanDescriptor(
                MrlLoggerHome.class.getName(),
                MrlLoggerHome.class,
                MrlLogger.class,
                MrlLoggerBean.class);
        MockContainerEnvironment.getMockContainer().deploy(mrlLoggerServiceDescriptor);
        
        SessionBeanDescriptor envServiceDescriptor =
                new SessionBeanDescriptor(
                        EnvironmentServiceHome.class.getName(),
                        EnvironmentServiceHome.class,
                        EnvironmentService.class,
                        EnvironmentServiceBean.class);
        MockContainerEnvironment.getMockContainer().deploy(envServiceDescriptor);
        
        SessionBeanDescriptor contractServiceDescriptor =
                new SessionBeanDescriptor(
                        ContractServiceHome.class.getName(),
                        ContractServiceHome.class,
                        ContractService.class,
                        ContractServiceBean.class);
        MockContainerEnvironment.getMockContainer().deploy(contractServiceDescriptor);
        
    }
    
}

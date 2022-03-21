package com.manulife.pension.service.distribution;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.mockejb.SessionBeanDescriptor;
import org.mockejb.TransactionManager;
import org.mockejb.TransactionPolicy;
import org.mockejb.interceptor.ClassPatternPointcut;

import com.manulife.pension.content.service.BrowseService;
import com.manulife.pension.content.service.BrowseServiceBean;
import com.manulife.pension.content.service.BrowseServiceHome;
import com.manulife.pension.service.account.AccountService;
import com.manulife.pension.service.account.AccountServiceBean;
import com.manulife.pension.service.account.AccountServiceHome;
import com.manulife.pension.service.account.entity.AvailabilityScheduleEntity;
import com.manulife.pension.service.account.entity.AvailabilityScheduleEntityBean;
import com.manulife.pension.service.account.entity.AvailabilityScheduleEntityHome;
import com.manulife.pension.service.account.entity.ParticipationEntity;
import com.manulife.pension.service.account.entity.ParticipationEntityBean;
import com.manulife.pension.service.account.entity.ParticipationEntityHome;
import com.manulife.pension.service.account.entity.PortfolioEntity;
import com.manulife.pension.service.account.entity.PortfolioEntityBean;
import com.manulife.pension.service.account.entity.PortfolioEntityHome;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransaction;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransactionBean;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransactionHome;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransaction;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransactionBean;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransactionHome;
import com.manulife.pension.service.account.transaction.ParticipantContractTransaction;
import com.manulife.pension.service.account.transaction.ParticipantContractTransactionBean;
import com.manulife.pension.service.account.transaction.ParticipantContractTransactionHome;
import com.manulife.pension.service.contract.ContractService;
import com.manulife.pension.service.contract.ContractServiceBean;
import com.manulife.pension.service.contract.ContractServiceHome;
import com.manulife.pension.service.email.EmailProcessingService;
import com.manulife.pension.service.email.EmailProcessingServiceBean;
import com.manulife.pension.service.email.EmailProcessingServiceHome;
import com.manulife.pension.service.employee.EmployeeService;
import com.manulife.pension.service.employee.EmployeeServiceBean;
import com.manulife.pension.service.employee.EmployeeServiceHome;
import com.manulife.pension.service.environment.EnvironmentService;
import com.manulife.pension.service.environment.EnvironmentServiceBean;
import com.manulife.pension.service.environment.EnvironmentServiceHome;
import com.manulife.pension.service.loan.LoanDocumentService;
import com.manulife.pension.service.loan.LoanDocumentServiceBean;
import com.manulife.pension.service.loan.LoanDocumentServiceLocalHome;
import com.manulife.pension.service.loan.LoanService;
import com.manulife.pension.service.loan.LoanServiceBean;
import com.manulife.pension.service.loan.LoanServiceHome;
import com.manulife.pension.service.loan.LoanServiceLocalHome;
import com.manulife.pension.service.loan.LoanServiceRemote;
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.SecurityServiceBean;
import com.manulife.pension.service.security.SecurityServiceHome;
import com.manulife.pension.service.security.tpa.TPAService;
import com.manulife.pension.service.security.tpa.TPAServiceBean;
import com.manulife.pension.service.security.tpa.TPAServiceHome;
import com.manulife.pension.service.testutility.MockContainerEnvironment;
import com.manulife.pension.service.testutility.MockContainerEnvironmentTestCase;
import com.manulife.pension.service.vesting.VestingService;
import com.manulife.pension.service.vesting.VestingServiceBean;
import com.manulife.pension.service.vesting.VestingServiceHome;
import com.manulife.pension.util.log.MrlLogger;
import com.manulife.pension.util.log.MrlLoggerBean;
import com.manulife.pension.util.log.MrlLoggerHome;

public class DistributionContainerEnvironment extends
		MockContainerEnvironmentTestCase {

	protected Context context = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();

		// Here is where we create mock entries for each of the services that we
		// use
		// in withdrawals.

		registerMockService(EmailProcessingServiceHome.class,
				EmailProcessingService.class, EmailProcessingServiceBean.class);

		SessionBeanDescriptor browseServiceBeanDescriptor = new SessionBeanDescriptor(
				"java:comp/env/ejb/BrowseServiceHome", BrowseServiceHome.class,
				BrowseService.class, BrowseServiceBean.class);
		MockContainerEnvironment.getMockContainer().deploy(
				browseServiceBeanDescriptor);
		MockContainerEnvironment.getAspectSystem().add(
				new ClassPatternPointcut(BrowseServiceBean.class.getName()),
				new TransactionManager(TransactionPolicy.REQUIRED));

		registerMockService(BrowseServiceHome.class, BrowseService.class,
				BrowseServiceBean.class);

		registerMockService(AccountServiceHome.class, AccountService.class,
				AccountServiceBean.class);

		registerMockService(EmployeeServiceHome.class, EmployeeService.class,
				EmployeeServiceBean.class);

		registerMockService(SecurityServiceHome.class, SecurityService.class,
				SecurityServiceBean.class);

		registerMockService(EnvironmentServiceHome.class,
				EnvironmentService.class, EnvironmentServiceBean.class);

		registerMockService(MrlLoggerHome.class, MrlLogger.class,
				MrlLoggerBean.class);

		registerMockService(ContractServiceHome.class, ContractService.class,
				ContractServiceBean.class);

		registerMockService(TPAServiceHome.class, TPAService.class,
				TPAServiceBean.class);

		registerMockService(ParticipantContractTransactionHome.class,
				ParticipantContractTransaction.class,
				ParticipantContractTransactionBean.class);

		registerMockService(ParticipationEntityHome.class,
				ParticipationEntity.class, ParticipationEntityBean.class);

		registerMockService(PortfolioEntityHome.class, PortfolioEntity.class,
				PortfolioEntityBean.class);

		registerMockService(AvailabilityScheduleEntityHome.class,
				AvailabilityScheduleEntity.class,
				AvailabilityScheduleEntityBean.class);

		registerMockService(VestingServiceHome.class, VestingService.class,
				VestingServiceBean.class);

		registerMockService(LoanServiceLocalHome.class, LoanService.class,
				LoanServiceBean.class);

		registerMockService(LoanDocumentServiceLocalHome.class,
				LoanDocumentService.class, LoanDocumentServiceBean.class);
		SessionBeanDescriptor loanDocumentServiceBeanDescriptor = new SessionBeanDescriptor(
				"java:comp/env/ejb/LoanDocumentServiceLocal",
				LoanDocumentServiceLocalHome.class, LoanDocumentService.class,
				LoanDocumentServiceBean.class);
		MockContainerEnvironment.getMockContainer().deploy(
				loanDocumentServiceBeanDescriptor);
		MockContainerEnvironment.getAspectSystem().add(
				new ClassPatternPointcut(LoanDocumentServiceBean.class
						.getName()),
				new TransactionManager(TransactionPolicy.REQUIRED));

		registerMockService(LoanServiceHome.class, LoanServiceRemote.class,
				LoanServiceBean.class);

		SessionBeanDescriptor loanServiceLocalBeanDescriptor = new SessionBeanDescriptor(
				"java:comp/env/ejb/LoanServiceLocal",
				LoanServiceLocalHome.class, LoanService.class,
				LoanServiceBean.class);

		MockContainerEnvironment.getMockContainer().deploy(
				loanServiceLocalBeanDescriptor);
		MockContainerEnvironment.getAspectSystem().add(
				new ClassPatternPointcut(LoanServiceBean.class.getName()),
				new TransactionManager(TransactionPolicy.REQUIRED));

		registerMockService(LoanAmortizationTransactionHome.class,
				LoanAmortizationTransaction.class,
				LoanAmortizationTransactionBean.class);

		registerMockService(LoanRequestPackageTransactionHome.class,
				LoanRequestPackageTransaction.class,
				LoanRequestPackageTransactionBean.class);

		context = new InitialContext();
		// context.bind(ParticipationEntityHome.class.getName(),
		// new MockParticipationEntityHome());
		// // context.bind(PortfolioEntityHome.class.getName(),
		// // new MockPortfolioEntityHome());
		// context.bind(AvailabilityScheduleEntityHome.class.getName(),
		// new MockAvailabilityScheduleEntityHome());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		context.unbind(ParticipationEntityHome.class.getName());
		context.unbind(PortfolioEntityHome.class.getName());
		context.unbind(AvailabilityScheduleEntityHome.class.getName());

	}

}

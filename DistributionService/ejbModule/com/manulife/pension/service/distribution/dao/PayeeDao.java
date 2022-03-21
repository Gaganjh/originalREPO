package com.manulife.pension.service.distribution.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * Data Access Object for srstd1d1.stp100.payee.
 */
public class PayeeDao extends BaseDatabaseDAO {

	private static final int FIRST_PAYEE_NUMBER = 1;

	/**
	 * selects an {@link ArrayList} of {@link Payee} objects that are contained
	 * in the given submission.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            The submission Id
	 * @param userProfileId
	 *            The user profile Id
	 * @return A list of payee value objects
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public List<? extends Payee> select(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			Class<? extends Payee> payeeType,
			Class<? extends DistributionAddress> distributionAddressType,
			Class<? extends PaymentInstruction> paymentInstructionType)
			throws DistributionServiceException {
		// The method name.
		List<Payee> returnList = new ArrayList<Payee>();
		List tempList = null;

		// The insert handler.
		SelectBeanListQueryHandler selectBeanListQueryHandler = new SelectBeanListQueryHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				PayeeDaoSql.SELECT_ALL_PAYEES_SQL,
				PayeeDaoSql.SELECT_ALL_PAYEES_PARAMETER_TYPES, payeeType,
				PayeeDaoSql.SELECT_ALL_PAYEES_RESULT_FIELDS);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				PayeeDaoSql.SELECT_ALL_PAYEES_PARAMETER_TYPES.length);

		// The order here must match the query order.
		parameters.add(submissionId);

		try {
			tempList = (List) selectBeanListQueryHandler.select(parameters
					.toArray());
		} catch (DAOException daoException) {
			throw new DistributionServiceDaoException("Submission ID: "
					+ submissionId + " contract ID: " + contractId
					+ " userProfileId:" + userProfileId + " type:"
					+ payeeType.getName(), daoException);
		}

		List<? extends DistributionAddress> distributionAddresses = new DistributionAddressDao()
				.select(submissionId, contractId, userProfileId,
						DistributionAddress.PAYEE_TYPE_CODE,
						distributionAddressType);

		List<? extends PaymentInstruction> paymentInstructions = new PaymentInstructionDao()
				.select(submissionId, contractId, userProfileId,
						paymentInstructionType);

		for (Object o : tempList) {
			Payee payee = (Payee) o;

			for (DistributionAddress address : distributionAddresses) {
				if (new Integer(payee.getPayeeNo())
						.equals(address.getPayeeNo())) {
					payee.setAddress(address);
					/*
					 * Only 1 address is allowed per payee.
					 */
					break;
				}
			}

			for (PaymentInstruction paymentInstruction : paymentInstructions) {
				if (new Integer(payee.getPayeeNo()).equals(paymentInstruction
						.getPayeeNo())
						&& payee.getRecipientNo().equals(
								paymentInstruction.getRecipientNo())) {
					payee.setPaymentInstruction(paymentInstruction);
				}
			}

			returnList.add(payee);
		}

		return returnList;

	}

	/**
	 * Inserts a collection of payees into the database.
	 * 
	 * @param contractId
	 *            The Contract id
	 * @param submissionId
	 *            The submisssion id
	 * @param userProfileId
	 *            The user profile id
	 * @param recipientNo
	 *            The payee's recipient Number
	 * @param payees
	 *            the collection of payees to insert
	 * @throws DistributionServiceException
	 *             thrown if there is an exception
	 */
	public void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Integer recipientNo,
			final Collection<Payee> payees) throws DistributionServiceException {

		if (payees == null) {
			return;
		}
		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				PayeeDaoSql.SQL_INSERT_PAYEE, PayeeDaoSql.INSERT_TYPES);
		DistributionAddressDao addrDao = new DistributionAddressDao();
		PaymentInstructionDao paymentDao = new PaymentInstructionDao();

		int payeeCount = FIRST_PAYEE_NUMBER;
		for (Payee payee : payees) {
			try {
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(recipientNo);
				params.add(payeeCount);
				params.add(StringUtils.upperCase(StringUtils.trim(payee
						.getFirstName())));
				params.add(StringUtils.upperCase(StringUtils.trim(payee
						.getLastName())));
				params.add(payee.getOrganizationName());
				params.add(payee.getTypeCode());
				params.add(payee.getReasonCode());
				params.add(payee.getPaymentMethodCode());
				params.add(payee.getShareTypeCode());
				params.add(payee.getShareValue());
				params.add(payee.getRolloverAccountNo());
				// Don't save default for this: <enter name of plan>
				final String rolloverPlanName = payee.getRolloverPlanName();
				params.add(StringUtils.equals(Payee.DEFAULT_ROLLOVER_PLAN_NAME,
						rolloverPlanName) ? StringUtils.EMPTY
						: rolloverPlanName);
				params.add(payee.getIrsDistCode());
				params.add(payee.getMailCheckToAddress());
				params.add(payee.getSendCheckByCourier());
				params.add(payee.getCourierCompanyCode());
				params.add(payee.getCourierNo());
				params.add(userProfileId);
				params.add(userProfileId);
				params.add(payee.getTaxes());
				params.add(payee.getParticipant());
				handler.insert(params.toArray());
				addrDao.insertPayeeAddress(submissionId, contractId,
						userProfileId, recipientNo, payeeCount, payee
								.getAddress());
				paymentDao.insert(submissionId, contractId, userProfileId,
						recipientNo, payeeCount, payee.getPaymentInstruction());
			} catch (DAOException e) {
				throw new DistributionServiceDaoException(
						"Problem occurred in prepared call: "
								+ PayeeDaoSql.SQL_INSERT_PAYEE
								+ " for contract ID " + contractId
								+ " and SubmissionId " + submissionId, e);
			}
			payeeCount++;
		}
	}

	/**
	 * Does a cascade delete based on submission id. Deletes 1. All Payee's 2.
	 * All Payee Payment Instructions 3. All Payee Addresses
	 * 
	 * @param contractId
	 *            The Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The user profile id
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying exception
	 */
	public void deleteAll(final Integer submissionId, final Integer contractId,
			final Integer userProfileId) throws DistributionServiceException {

		new PaymentInstructionDao().deleteAll(submissionId, contractId,
				userProfileId);
		new DistributionAddressDao().deleteAll(submissionId, contractId,
				userProfileId, DistributionAddress.PAYEE_TYPE_CODE);

		List<Object> params = null;

		SQLDeleteHandler handler = new SQLDeleteHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				PayeeDaoSql.SQL_DELETE_PAYEE);
		try {
			params = new ArrayList<Object>(1);
			params.add(submissionId);
			handler.delete(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ PayeeDaoSql.SQL_DELETE_PAYEE
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}

	}
	
	
}

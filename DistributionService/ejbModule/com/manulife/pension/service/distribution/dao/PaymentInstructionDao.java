package com.manulife.pension.service.distribution.dao;

import java.util.ArrayList;
import java.util.List;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;

/**
 * Data Access Object for srstd1d1.stp100.payment_instruction.
 * 
 */
public class PaymentInstructionDao extends BaseDatabaseDAO {

	/**
	 * selects an {@link ArrayList} of {@link PaymentInstruction} objects that are
	 * contained in the given submission.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            The submission Id
	 * @param userProfileId
	 *            The user profile Id
	 * @return A list of payment instruction value objects
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public List<? extends PaymentInstruction> select(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			Class<? extends PaymentInstruction> paymentInstructionType)
			throws DistributionServiceException {
		List<PaymentInstruction> returnList = new ArrayList<PaymentInstruction>();
		List tempList = null;

		// The insert handler.
		SelectBeanListQueryHandler selectBeanListQueryHandler = new SelectBeanListQueryHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				PaymentInstructionDaoSql.SELECT_ALL_PAYMENT_INSTRUCTIONS_SQL,
				PaymentInstructionDaoSql.SELECT_ALL_PAYMENT_INSTRUCTIONS_PARAMETER_TYPES, paymentInstructionType,
				PaymentInstructionDaoSql.SELECT_ALL_PAYMENT_INSTRUCTIONS_RESULT_FIELDS);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				PaymentInstructionDaoSql.SELECT_ALL_PAYMENT_INSTRUCTIONS_PARAMETER_TYPES.length);

		// The order here must match the query order.
		parameters.add(submissionId);

		try {
			tempList = (List) selectBeanListQueryHandler.select(parameters
					.toArray());
		} catch (DAOException daoException) {
			throw new DistributionServiceDaoException("Submission ID: "
					+ submissionId + " contract ID: " + contractId
					+ " userProfileId:" + userProfileId + " type:"
					+ paymentInstructionType.getName(), daoException);
		}

		for (Object o : tempList) {
			PaymentInstruction paymentInstruction = (PaymentInstruction) o;
			returnList.add(paymentInstruction);
		}

		return returnList;

	}
	
	/**
	 * Delete's all payment instructions for a submission id.
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

		List<Object> params = null;
		SQLDeleteHandler handler = new SQLDeleteHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				PaymentInstructionDaoSql.SQL_DELETE_PAYMENT_INSTRUCTION);
		try {
			params = new ArrayList<Object>(1);
			params.add(submissionId);
			handler.delete(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ PaymentInstructionDaoSql.SQL_DELETE_PAYMENT_INSTRUCTION
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}
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
	 *            The recipient Number
	 * @param payeeNo
	 *            The payee number
	 * @param payment
	 *            the payment instruction to insert
	 * @throws DistributionServiceException
	 *             thrown if there is an exception
	 */
	public void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Integer recipientNo,
			final Integer payeeNo, final PaymentInstruction payment)
			throws DistributionServiceException {
		boolean paymentflag = false;
		
		if (payment.getPaymentAmount() != null ) {
			paymentflag = true;
		}
		if (!paymentflag && (payment == null || payment.isBlank())) {
			return;
		}
		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				PaymentInstructionDaoSql.SQL_INSERT_PAYMENT_INSTRUCTION,
				PaymentInstructionDaoSql.INSERT_TYPES);
		try {
			params = new ArrayList<Object>();
			params.add(submissionId);
			params.add(recipientNo);
			params.add(payeeNo);
			params.add(payment.getBankAccountTypeCode());
			params.add(payment.getBankTransitNumber());
			params.add(payment.getBankAccountNumber());
			params.add(payment.getBankName());
			params.add(payment.getCreditPartyName());
			params.add(userProfileId);
			params.add(userProfileId);
			params.add(payment.getPaymentAmount());
			handler.insert(params.toArray());
		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ PaymentInstructionDaoSql.SQL_INSERT_PAYMENT_INSTRUCTION
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}
	}
}

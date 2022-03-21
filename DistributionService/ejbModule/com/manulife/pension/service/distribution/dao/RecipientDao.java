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
import com.manulife.pension.service.distribution.valueobject.Recipient;

/**
 * @author Dennis
 * 
 */
public class RecipientDao extends BaseDatabaseDAO {

	private static final int FIRST_RECIPIENT_NUMBER = 1;

	/**
	 * selects an {@link ArrayList} of {@link Recipient} objects that are
	 * contained in the given submission.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            The submission Id
	 * @param userProfileId
	 *            The user profile Id
	 * @return A list of recipient value objects
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public List<? extends Recipient> select(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			Class<? extends Recipient> recipientType,
			Class<? extends Payee> payeeType,
			Class<? extends DistributionAddress> distributionAddressType, Class<? extends PaymentInstruction> paymentInstructionType)
			throws DistributionServiceException {
		List<Recipient> returnList = new ArrayList<Recipient>();
		List tempList = null;

		// The insert handler.
		SelectBeanListQueryHandler selectBeanListQueryHandler = new SelectBeanListQueryHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				RecipientDaoSql.SELECT_ALL_RECIPIENTS_SQL,
				RecipientDaoSql.SELECT_ALL_RECIPIENTS_PARAMETER_TYPES,
				recipientType,
				RecipientDaoSql.SELECT_ALL_RECIPIENTS_RESULT_FIELDS);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				RecipientDaoSql.SELECT_ALL_RECIPIENTS_PARAMETER_TYPES.length);

		// The order here must match the query order.
		parameters.add(submissionId);

		try {
			tempList = (List) selectBeanListQueryHandler.select(parameters
					.toArray());
		} catch (DAOException daoException) {
			throw new DistributionServiceDaoException("Submission ID: "
					+ submissionId + " contract ID: " + contractId
					+ " userProfileId:" + userProfileId + " type:"
					+ recipientType.getName(), daoException);
		}

		List<? extends Payee> payees = new PayeeDao().select(submissionId,
				contractId, userProfileId, payeeType, distributionAddressType,
				paymentInstructionType);

		List<? extends DistributionAddress> distributionAddresses = new DistributionAddressDao()
				.select(submissionId, contractId, userProfileId,
						DistributionAddress.RECIPIENT_TYPE_CODE,
						distributionAddressType);

		for (Object o : tempList) {
			Recipient recipient = (Recipient) o;

			for (DistributionAddress address: distributionAddresses) {
				if (new Integer(recipient.getRecipientNo()).equals(address
						.getRecipientNo())) {
					recipient.setAddress(address);
					/*
					 * Only 1 address is allowed per recipient.
					 */
					break;
				}
			}

			for (Payee payee : payees) {
				if (new Integer(recipient.getRecipientNo()).equals(payee
						.getRecipientNo())) {
					recipient.getPayees().add(payee);
				}
			}
			returnList.add(recipient);
		}

		return returnList;

	}

	/**
	 * Does a cascade delete based on submission id. Deletes 1. All Recipients
	 * 2. All Recipient Addresses 3. All Recipient Payee's --> 4. All Recipient
	 * Payee Payment Instructions --> 5. All Recipient Payee Addresses
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

		new DistributionAddressDao().deleteAll(submissionId, contractId,
				userProfileId, DistributionAddress.RECIPIENT_TYPE_CODE);
		new PayeeDao().deleteAll(submissionId, contractId, userProfileId);

		List<Object> params = null;
		SQLDeleteHandler handler = new SQLDeleteHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				RecipientDaoSql.SQL_DELETE_RECIPIENT);
		try {
			params = new ArrayList<Object>(1);
			params.add(submissionId);
			handler.delete(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ RecipientDaoSql.SQL_DELETE_RECIPIENT
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}
	}

	/**
	 * 
	 * 
	 * @param contractId
	 *            The Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The user profile id
	 * @param recipients
	 *            The collectio of recipients to be deleted
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying exception
	 */
	public void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Collection<Recipient> recipients)
			throws DistributionServiceException {

		if (recipients == null) {
			return;
		}

		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				RecipientDaoSql.SQL_INSERT_RECIPIENT,
				RecipientDaoSql.INSERT_TYPES);
		DistributionAddressDao addrDao = new DistributionAddressDao();
		PayeeDao payeeDao = new PayeeDao();

		int recipientCount = FIRST_RECIPIENT_NUMBER;
		for (Recipient recipient : recipients) {
			try {
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(recipientCount);
				params.add(StringUtils.upperCase(StringUtils.trim(recipient
						.getFirstName())));
				params.add(StringUtils.upperCase(StringUtils.trim(recipient
						.getLastName())));
				params.add(recipient.getOrganizationName());
				params.add(recipient.getUsCitizenInd());
				params.add(recipient.getStateOfResidenceCode());
				params.add(recipient.getShareTypeCode());
				params.add(recipient.getShareValue());
				params.add(recipient.getFederalTaxPercent());
				params.add(recipient.getStateTaxPercent());
				params.add(recipient.getStateTaxTypeCode());
				params.add(recipient.getTaxpayerIdentTypeCode());
				params.add(recipient.getTaxpayerIdentNo());
				params.add(userProfileId);
				params.add(userProfileId);
				handler.insert(params.toArray());
				addrDao.insertRecipientAddress(submissionId, contractId,
						userProfileId, recipientCount, recipient.getAddress());
				payeeDao.insert(submissionId, contractId, userProfileId,
						recipientCount, recipient.getPayees());
			} catch (DAOException e) {
				throw new DistributionServiceDaoException(
						"Problem occurred in prepared call: "
								+ RecipientDaoSql.SQL_INSERT_RECIPIENT
								+ " for contract ID " + contractId
								+ " and SubmissionId " + submissionId, e);
			}
			recipientCount++;
		}
	}
}

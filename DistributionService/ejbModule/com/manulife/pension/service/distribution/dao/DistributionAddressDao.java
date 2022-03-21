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
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;

/**
 * Address Data Access Object.
 */
public class DistributionAddressDao extends BaseDatabaseDAO {

	public List<? extends DistributionAddress> select(
			final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final String distributionTypeCode,
			Class<? extends DistributionAddress> addressType)
			throws DistributionServiceException {
		List<DistributionAddress> returnList = new ArrayList<DistributionAddress>();
		List tempList = null;

		// The insert handler.
		SelectBeanListQueryHandler selectBeanListQueryHandler = new SelectBeanListQueryHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				DistributionAddressDaoSql.SELECT_ALL_ADDRESSES_SQL,
				DistributionAddressDaoSql.SELECT_ALL_ADDRESSES_PARAMETER_TYPES,
				addressType,
				DistributionAddressDaoSql.SELECT_ALL_ADDRESSES_RESULT_FIELDS);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				DistributionAddressDaoSql.SELECT_ALL_ADDRESSES_PARAMETER_TYPES.length);

		// The order here must match the query order.
		parameters.add(submissionId);
		parameters.add(distributionTypeCode);

		try {
			tempList = (List) selectBeanListQueryHandler.select(parameters
					.toArray());
		} catch (DAOException daoException) {
			throw new DistributionServiceDaoException("Submission ID: "
					+ submissionId + " contract ID: " + contractId
					+ " userProfileId:" + userProfileId + " type:"
					+ addressType.getName(), daoException);
		}
		for (Object o : tempList) {
			DistributionAddress address = (DistributionAddress) o;
			returnList.add(address);
		}

		return returnList;

	}

	/**
	 * Inserts a collection of address into the database.
	 * 
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param recipientNo
	 *            The Recipient number for the address.
	 * @param addresses
	 *            The collection of address to insert
	 * @throws DistributionServiceException
	 *             Thrown if there's an exception
	 */
	public void insertRecipientAddress(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			final Integer recipientNo, final DistributionAddress addresses)
			throws DistributionServiceException {

		insert(submissionId, contractId, userProfileId, recipientNo, null,
				DistributionAddress.RECIPIENT_TYPE_CODE, addresses);

	}

	/**
	 * Inserts a collection of address into the database.
	 * 
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param recipientNo
	 *            The recipient No ( Recipient is the parent of payee )
	 * @param payeeNo
	 *            The Payee number for the address.
	 * @param addresses
	 *            The collection of address to insert
	 * @throws DistributionServiceException
	 *             Thrown if there's an exception
	 */
	public void insertPayeeAddress(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			final Integer recipientNo, final Integer payeeNo,
			final DistributionAddress addresses)
			throws DistributionServiceException {

		insert(submissionId, contractId, userProfileId, recipientNo, payeeNo,
				DistributionAddress.PAYEE_TYPE_CODE, addresses);

	}

	/**
	 * Inserts an address into the database.
	 * 
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param recipientNo
	 *            The Recipient number
	 * @param payeeNo
	 *            The Payee number
	 * @param distributionTypeCode
	 *            The distribution type code
	 * @param address
	 *            The address to insert
	 * @throws DistributionServiceException
	 *             Thrown if there's an exception
	 */
	private void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Integer recipientNo,
			final Integer payeeNo, final String distributionTypeCode,
			final DistributionAddress address)
			throws DistributionServiceException {

		if (address == null || address.isBlank()) {
			return;
		}
		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				DistributionAddressDaoSql.SQL_INSERT_ADDRESS,
				DistributionAddressDaoSql.INSERT_TYPES);

		try {
			params = new ArrayList<Object>();
			params.add(submissionId);
			params.add(submissionId);
			params.add(distributionTypeCode);
			params.add(recipientNo);
			params.add(payeeNo);
			params.add(address.getAddressLine1());
			params.add(address.getAddressLine2());
			params.add(address.getCity());
			params.add(address.getStateCode());
			params.add(address.getZipCode());
			params.add(address.getCountryCode());
			params.add(userProfileId);
			params.add(userProfileId);
			handler.insert(params.toArray());
		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ DistributionAddressDaoSql.SQL_INSERT_ADDRESS
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}
	}

	/**
	 * Deletes all address for a given submission id and distribution code.
	 * 
	 * 
	 * @param contractId
	 *            The currect Contract Id
	 * @param submissionId
	 *            The Submission Id
	 * @param userProfileId
	 *            The users profile Id
	 * @param distributionTypeCode
	 *            The distribution type code. Must be valid or an exception will
	 *            be thrown
	 * @throws DistributionServiceException
	 *             thrown if an error is encountered
	 */
	public void deleteAll(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final String distributionTypeCode)
			throws DistributionServiceException {

		List<Object> params = null;

		SQLDeleteHandler handler = new SQLDeleteHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				DistributionAddressDaoSql.SQL_DELETE_ADDRESSES);

		if (!distributionTypeCode.equals(DistributionAddress.PAYEE_TYPE_CODE)
				&& !distributionTypeCode.equals(DistributionAddress.RECIPIENT_TYPE_CODE)
				&& !distributionTypeCode.equals(DistributionAddress.LOAN_TYPE_CODE)) {
			throw new DistributionServiceDaoException(
					"WithdrawalAddressDAO:deleteAll - unknown distribution code"
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId);
		}
		try {
			params = new ArrayList<Object>(1);
			params.add(submissionId);
			params.add(distributionTypeCode);
			handler.delete(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ DistributionAddressDaoSql.SQL_DELETE_ADDRESSES
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}
	}
}

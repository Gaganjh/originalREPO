package com.manulife.pension.service.distribution.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Declaration;

/**
 * The DeclarationDao performs database access operations for
 * {@link Declaration} objects.
 */
public class DeclarationDao extends BaseDatabaseDAO {

	public List<? extends Declaration> select(final Integer submissionId,
			final Integer contractId, final Integer userProfileId,
			Class<? extends Declaration> declarationType)
			throws DistributionServiceException {
		List<Declaration> returnList = new ArrayList<Declaration>();
		List tempList = null;

		// The insert handler.
		SelectBeanListQueryHandler selectBeanListQueryHandler = new SelectBeanListQueryHandler(
				DataSourceJndiName.SUBMISSION,
				DeclarationDaoSql.SELECT_ALL_DECLARATIONS_SQL,
				DeclarationDaoSql.SELECT_ALL_DECLARATIONS_PARAMETER_TYPES,
				declarationType,
				DeclarationDaoSql.SELECT_ALL_DECLARATIONS_RESULT_FIELDS);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				DeclarationDaoSql.SELECT_ALL_DECLARATIONS_PARAMETER_TYPES.length);

		// The order here must match the query order.
		parameters.add(submissionId);

		try {
			tempList = (List) selectBeanListQueryHandler.select(parameters
					.toArray());
		} catch (DAOException daoException) {
			throw new DistributionServiceDaoException("Submission ID: "
					+ submissionId + " contract ID: " + contractId
					+ " userProfileId:" + userProfileId + " type:"
					+ declarationType.getName(), daoException);
		}
		for (Object o : tempList) {
			Declaration declaration = (Declaration) o;
			returnList.add(declaration);
		}

		return returnList;

	}

	/**
	 * deletes all of the declarations related to the given submission id.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            the submission id
	 * @param userProfileId
	 *            the user profile id
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public void deleteAll(final Integer submissionId, final Integer contractId,
			final Integer userProfileId) throws DistributionServiceException {

		List<Object> params = new ArrayList<Object>(1);
		params.add(submissionId);
		SQLDeleteHandler handler = new SQLDeleteHandler(
				DataSourceJndiName.SUBMISSION,
				DeclarationDaoSql.SQL_DELETE_ALL_DECLARATIONS);
		try {
			handler.delete(params.toArray());
		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ DeclarationDaoSql.SQL_DELETE_ALL_DECLARATIONS
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}

	}

	/**
	 * deletes all of the declarations related to the given submission id, that
	 * match the declaration_type_code provided.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            the submission id
	 * @param TypeCodes
	 *            the list of declaration_type_code values to delete
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public void delete(final Integer submissionId, final Integer contractId,
			final List<String> typeCodes) throws DistributionServiceException {
		// TODO:tm1 need to create a test for this method.
		// Below corresponds to maximum entries in the "in" clause in
		// DeclarationDaoSql.SQL_DELETE_SPECIFIC_DECLARATION_TYPES.
		int typeCodesMaxSize = 2;

		if (typeCodes.size() == 0) {
			return; // no type codes, so nothing to delete.
		}
		if (typeCodes.size() > typeCodesMaxSize) {
			throw new IllegalArgumentException("TypeCodes list contains too "
					+ "many entries.  It can support a maximum of "
					+ typeCodesMaxSize + " but contains: "
					+ typeCodes.toString());
		}

		try {
			List<Object> params = new ArrayList<Object>(3);
			params.add(submissionId);
			for (int i = 0; i < typeCodesMaxSize; i++) {
				if (i < typeCodes.size()) {
					params.add(typeCodes.get(i));
				} else {
					params.add(typeCodes.get(0));
				}
			}
			new SQLDeleteHandler(
					DataSourceJndiName.SUBMISSION,
					DeclarationDaoSql.SQL_DELETE_SPECIFIC_DECLARATION_TYPES,
					DeclarationDaoSql.DELETE_SPECIFIC_DECLARATION_TYPES_PARAMETER_TYPES)
					.delete(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ DeclarationDaoSql.SQL_DELETE_SPECIFIC_DECLARATION_TYPES
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}

	}

	/**
	 * Inserts a collection of declarations into the database.
	 * 
	 * @param contractId
	 *            The contract Id
	 * @param submissionId
	 *            The submission Id
	 * @param userProfileId
	 *            the userProfile Id
	 * @param declarations
	 *            the collection of delcarations to insert
	 * @throws DistributionServiceException
	 *             thrown if there is an underlying error
	 */
	public void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId,
			final Collection<? extends Declaration> declarations)
			throws DistributionServiceException {

		if (declarations == null) {
			return;
		}
		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				DataSourceJndiName.SUBMISSION,
				DeclarationDaoSql.SQL_INSERT_DECLARATIONS);

		for (Declaration declaration : declarations) {
			try {
				params = new ArrayList<Object>();
				params.add(submissionId);
				params.add(declaration.getTypeCode());
				params.add(userProfileId);
				handler.insert(params.toArray());

			} catch (DAOException e) {
				throw new DistributionServiceDaoException(
						"Problem occurred in prepared call: "
								+ DeclarationDaoSql.SQL_INSERT_DECLARATIONS
								+ " for contract ID " + contractId
								+ " and SubmissionId " + submissionId, e);
			}
		}
	}
}

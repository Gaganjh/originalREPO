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
import com.manulife.pension.service.distribution.valueobject.Note;

/**
 * The NoteDao performs database access operations for
 * {@link Note} objects.
 */
public class NoteDao extends BaseDatabaseDAO {

	/**
	 * getNotes gets an {@link ArrayList} of {@link Note}
	 * objects that are contained in the given submission.
	 * 
	 * @param withdrawalRequest
	 *            The request to lookup the notes with.
	 * @return List of WithdrawalRequestNote objects.
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public List<? extends Note> select(final Integer submissionId, Class<? extends Note> noteType)
			throws DistributionServiceException {
		List tempList = null;
		List<Note> returnList = new ArrayList<Note>();

		// The insert handler.
		SelectBeanListQueryHandler selectBeanListQueryHandler = new SelectBeanListQueryHandler(
				BaseDatabaseDAO.STP_DATA_SOURCE_NAME,
				NoteDaoSql.SELECT_ALL_NOTES_SQL,
				NoteDaoSql.SELECT_ALL_NOTES_PARAMETER_TYPES, noteType,
				NoteDaoSql.SELECT_ALL_NOTES_RESULT_FIELDS);

		final ArrayList<Object> parameters = new ArrayList<Object>(
				NoteDaoSql.SELECT_ALL_NOTES_PARAMETER_TYPES.length);

		parameters.add(submissionId);

		try {
			tempList = (List) selectBeanListQueryHandler.select(parameters
					.toArray());
		} catch (DAOException daoException) {

			throw new DistributionServiceDaoException("Submission ID ["
					+ submissionId + "]", daoException);
		}
		for (Object o : tempList) {
			returnList.add((Note) o);
		}
		return returnList;

	}

	
	/**
	 * deleteAll deletes all of the notes related to the given
	 * submission.
	 * 
	 * @param contractId
	 *            The contract id
	 * @param submissionId
	 *            the submission id
	 * @param userProfileId
	 *            the user profile id
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public void deleteAll(final Integer submissionId, final Integer contractId,
			final Integer userProfileId) throws DistributionServiceException {

		try {
			List<Object> params = new ArrayList<Object>(1);
			params.add(submissionId);
			new SQLDeleteHandler(BaseDatabaseDAO.STP_DATA_SOURCE_NAME,
					NoteDaoSql.SQL_DELETE_ALL_NOTES).delete(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ NoteDaoSql.SQL_DELETE_ALL_NOTES
							+ " for contract ID " + contractId
							+ " and SubmissionId " + submissionId, e);
		}

	}

	/**
	 * Inserts a collection of notes into the database.
	 * 
	 * @param contractId
	 *            The contract id
	 * @param submissionId
	 *            the submission id
	 * @param userProfileId
	 *            the user profile id
	 * @param note
	 *            the note to insert
	 * @throws DistributionServiceException
	 *             If there is a data tier exception.
	 */
	public void insert(final Integer submissionId, final Integer contractId,
			final Integer userProfileId, final Note note)
			throws DistributionServiceException {

		if (note == null || note.isBlank()) {
			return;
		}

		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
				NoteDaoSql.SQL_INSERT_NOTES);

		try {
			params = new ArrayList<Object>();
			params.add(submissionId);
			params.add(note.getNoteTypeCode());
			params.add(note.getNote());
			params.add(userProfileId);
			handler.insert(params.toArray());

		} catch (DAOException e) {
			throw new DistributionServiceDaoException(
					"Problem occurred in prepared call: "
							+ NoteDaoSql.SQL_INSERT_NOTES + " for contract ID "
							+ contractId + " and SubmissionId " + submissionId,
					e);
		}
	}
	
	/**
     * deleteNoteType deletes notes related to the given
     * submission and note type code.
     * 
     * @param contractId
     *            The contract id
     * @param submissionId
     *            the submission id
     * @param userProfileId
     *            the user profile id
     * @param noteTypeCode
     *            the note type code
     * @throws DistributionServiceException
     *             If there is a data tier exception.
     */
    public void deleteNoteType(final Integer submissionId, final Integer contractId,
            final Integer userProfileId, final String noteTypeCode)
            throws DistributionServiceException {

        try {
            List<Object> params = new ArrayList<Object>(1);
            params.add(submissionId);
            params.add(noteTypeCode);
            new SQLDeleteHandler(BaseDatabaseDAO.STP_DATA_SOURCE_NAME,
                    NoteDaoSql.SQL_DELETE_NOTE_TYPE).delete(params.toArray());

        } catch (DAOException e) {
            throw new DistributionServiceDaoException(
                    "Problem occurred in prepared call: "
                            + NoteDaoSql.SQL_DELETE_NOTE_TYPE
                            + " for contract ID " + contractId
                            + " and SubmissionId " + submissionId
                            + " and noteTypeCode " + noteTypeCode, e);
        }
    }

}

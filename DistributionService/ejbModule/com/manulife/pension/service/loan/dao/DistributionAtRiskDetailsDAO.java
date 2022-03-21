package com.manulife.pension.service.loan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.withdrawal.valueobject.Address;

/**
 * Class used to make insertion / update / retrieve participant risk information
 * to database.
 * 
 * @author Vasanth Balaji
 * 
 */
public class DistributionAtRiskDetailsDAO extends BaseDatabaseDAO {

	private static final String CLASS_NAME = DistributionAtRiskDetailsDAO.class
			.getName();

	/**
	 * This method inserts a set of {@link AtRisk Parameters}s into the
	 * database.
	 * 
	 * 
	 */
	public static void insert(AtRiskDetailsVO objects)
			throws DistributionServiceException {

		if (objects == null) {
			return;
		}

		List<Object> params = null;
		SQLInsertHandler handler = new SQLInsertHandler(
				BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME,
				DistributionAtRiskDaoSql.SQL_INSERT_DISTRIBUTION_ATRISK_DATA,
				DistributionAtRiskDaoSql.INSERT_TYPES);

		try {
			params = new ArrayList<Object>();

			// AtRiskWebRegistrationVO

			if (objects.getSubmissionId() != null)
				params.add(objects.getSubmissionId());
			else
				throw new LoanDaoException(
						"DistributionAtRiskDetailsDAO.insert failed as SubmissionId is null");

			if (objects.getWebRegistration() != null) {
				params.add(objects.getWebRegistration()
						.getWebRegistrationDate());
				params.add(objects.getWebRegistration()
						.getWebRegConfirmationMailedDate());
			} else {
				params.add(null);
				params.add(null);
			}
			if (objects.getWebRegistration() != null
					&& objects.getWebRegistration().getAddress() != null) {
				params.add(objects.getWebRegistration().getAddress()
						.getAddressLine1());
				params.add(objects.getWebRegistration().getAddress()
						.getAddressLine2());
				params.add(objects.getWebRegistration().getAddress().getCity());
				params.add(objects.getWebRegistration().getAddress()
						.getStateCode());
				params.add(objects.getWebRegistration().getAddress()
						.getZipCode());
				params.add(objects.getWebRegistration().getAddress()
						.getCountryCode());
			} else {
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
			}

			if (objects.getWebRegistration() != null) {
				params.add(objects.getWebRegistration()
						.getConfirmUpdatedProfileId());
				params.add(objects.getWebRegistration()
						.getConfirmUpdatedUserIdType());
			} else {
				params.add(null);
				params.add(null);
			}

			if (objects.getWebRegistration() != null
					&& objects.getWebRegistration() != null) {
				params.add(objects.getWebRegistration().getConfirmUpdatedUserFirstName());
				params.add(objects.getWebRegistration().getConfirmUpdatedUserLastName());
			} else {
				params.add(null);
				params.add(null);
			}

			// AtRiskAddressChangeVO
			if (objects.getAddresschange() != null
					&& objects.getAddresschange().getApprovalAddress() != null) {
				params.add(objects.getAddresschange().getApprovalAddress()
						.getAddressLine1());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getAddressLine2());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getCity());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getStateCode());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getZipCode());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getCountryCode());
			} else {
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
			}

			if (objects.getAddresschange() != null) {
				params.add(objects.getAddresschange()
						.getApprovalUpdatedProfileId());
				params.add(objects.getAddresschange()
						.getApprovalUpdatedUserIdType());
			} else {
				params.add(null);
				params.add(null);
			}

			if (objects.getAddresschange() != null
					&& objects.getAddresschange().getApprovalAddress() != null) {
				params.add(objects.getAddresschange().getCreatedUserFistName());
				params.add(objects.getAddresschange().getCreatedUserLastName());
			} else {
				params.add(null);
				params.add(null);
			}

			// AtRiskPasswordResetVO
			if (objects.getPasswordReset() != null) {
				params.add(objects.getPasswordReset()
						.getEmailPasswordResetDate());
				params.add(objects.getPasswordReset()
						.getEmailPasswordResetEmailAddress());
				params.add(objects.getPasswordReset()
						.getEmailPasswordResetInitiatedProfileId());
				params.add(objects.getPasswordReset()
						.getEmailAddressLastUpdatedProfileId());
				params.add(objects.getPasswordReset()
						.getEmailAddressLastUpdatedUserIdType());
			} else {
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
			}

			// AtRiskForgetUserName
			if (objects.getForgetUserName() != null) {
				params.add(objects.getForgetUserName()
						.getForgotPasswordRequestedDate());
				params.add(objects.getForgetUserName()
						.getForgotPasswordEmailAddress());
				params.add(objects.getForgetUserName()
						.getForgotPasswordUpdatedProfileId());
				params.add(objects.getForgetUserName()
						.getForgotPasswordUpdatedUserIdType());
				params.add(objects.getForgetUserName()
						.getForgotPasswordUpdatedUserFirstName());
				params.add(objects.getForgetUserName()
						.getForgotPasswordUpdatedUserLastName());
			} else {
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
			}

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp curentTimestamp = new java.sql.Timestamp(
					calendar.getTime().getTime());
			params.add(curentTimestamp);
			params.add(curentTimestamp);
			handler.insert(params.toArray());

		} catch (DAOException e) {
			throw new LoanDaoException(
					"Problem occurred in prepared call: "
							+ DistributionAtRiskDaoSql.SQL_INSERT_DISTRIBUTION_ATRISK_DATA
							+ " for newSubmissionId "
							+ objects.getSubmissionId(), e);
		}

	}

	/**
	 * 
	 * @param submissionId
	 * @return
	 * @throws DistributionServiceException
	 */
	public static AtRiskDetailsVO retrieve(AtRiskDetailsInputVO atRiskDetils)
			throws DistributionServiceException {

		if(atRiskDetils == null) return null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AtRiskDetailsVO voObj = null;
		
		Integer submissionId = atRiskDetils.getSubmissionId();
		Timestamp currentTS= null;
		
		try {
			conn = BaseDatabaseDAO.getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(DistributionAtRiskDaoSql.SQL_SELECT_DISTRIBUTION_ATRISK_DATA);
			if (submissionId != null && !"".equals(submissionId)) {
				stmt.setInt(1, submissionId);
				rs = stmt.executeQuery();
				while (rs.next()) {

					voObj = new AtRiskDetailsVO();
					voObj.setSubmissionId(submissionId);

					AtRiskWebRegistrationVO riskWebRegistrationVO = new AtRiskWebRegistrationVO();

					Address confirmMailAddressvo = new Address();
					confirmMailAddressvo.setAddressLine1(rs
							.getString("CONFIRM_MAIL_ADDR_LINE1"));
					confirmMailAddressvo.setAddressLine2(rs
							.getString("CONFIRM_MAIL_ADDR_LINE2"));
					confirmMailAddressvo.setCity(rs
							.getString("CONFIRM_MAIL_CITY_NAME"));
					confirmMailAddressvo.setCountryCode(rs
							.getString("CONFIRM_MAIL_COUNTRY_NAME"));
					confirmMailAddressvo.setStateCode(rs
							.getString("CONFIRM_MAIL_STATE_CODE"));
					confirmMailAddressvo.setZipCode(rs
							.getString("CONFIRM_MAIL_ZIP_CODE"));

					riskWebRegistrationVO.setAddress(confirmMailAddressvo);
					riskWebRegistrationVO.setConfirmUpdatedUserFirstName(rs
							.getString("CONFIRM_UPDATED_USER_FIRST_NAME"));
					riskWebRegistrationVO.setConfirmUpdatedUserLastName(rs
							.getString("CONFIRM_UPDATED_USER_LAST_NAME"));

					Address approvalMailAddressvo = new Address();
					approvalMailAddressvo.setAddressLine1(rs
							.getString("APPROVAL_MAIL_ADDR_LINE1"));
					approvalMailAddressvo.setAddressLine2(rs
							.getString("APPROVAL_MAIL_ADDR_LINE2"));
					approvalMailAddressvo.setCity(rs
							.getString("APPROVAL_MAIL_CITY_NAME"));
					approvalMailAddressvo.setCountryCode(rs
							.getString("APPROVAL_MAIL_COUNTRY_NAME"));
					approvalMailAddressvo.setStateCode(rs
							.getString("APPROVAL_MAIL_STATE_CODE"));
					approvalMailAddressvo.setZipCode(rs
							.getString("APPROVAL_MAIL_ZIP_CODE"));
					
					riskWebRegistrationVO.setConfirmUpdatedUserFirstName(rs
							.getString("CONFIRM_UPDATED_USER_FIRST_NAME"));
					riskWebRegistrationVO.setConfirmUpdatedUserLastName(rs
							.getString("CONFIRM_UPDATED_USER_LAST_NAME"));
					
					riskWebRegistrationVO.setConfirmUpdatedProfileId(rs
							.getInt("CONFIRM_UPDATED_PROFILE_ID"));
					riskWebRegistrationVO.setConfirmUpdatedUserIdType(rs
							.getString("CONFIRM_UPDATED_USER_ID_TYPE"));
					riskWebRegistrationVO.setWebRegConfirmationMailedDate(rs
							.getDate("WEB_REG_CONFIRMATION_MAILED_DATE"));
					riskWebRegistrationVO.setWebRegistrationDate(rs
							.getDate("WEB_REGISTRATION_DATE"));

					AtRiskAddressChangeVO riskAddressChangevo = new AtRiskAddressChangeVO();
					riskAddressChangevo
							.setApprovalAddress(approvalMailAddressvo);
					riskAddressChangevo.setApprovalUpdatedProfileId(rs
							.getInt("APPROVAL_UPDATED_PROFILE_ID"));
					riskAddressChangevo.setApprovalUpdatedUserIdType(rs
							.getString("APPROVAL_UPDATED_USER_ID_TYPE"));
					
					riskAddressChangevo.setCreatedUserFistName(rs
							.getString("APPROVAL_UPDATED_USER_FIRST_NAME"));

					riskAddressChangevo.setCreatedUserLastName(rs
							.getString("APPROVAL_UPDATED_USER_LAST_NAME"));
					
					AtRiskPasswordResetVO riskPasswordResetvo = new AtRiskPasswordResetVO();
					riskPasswordResetvo.setEmailPasswordResetDate(rs
							.getDate("EMAIL_PASSWORD_RESET_DATE"));
					riskPasswordResetvo
							.setEmailPasswordResetEmailAddress(rs
									.getString("EMAIL_PASSWORD_RESET_USED_EMAIL_ADDRESS"));
					riskPasswordResetvo
							.setEmailPasswordResetInitiatedProfileId(rs
									.getInt("EMAIL_PASSWORD_RESET_INITIATED_PROFILE_ID"));
					riskPasswordResetvo
							.setEmailAddressLastUpdatedUserIdType(rs
									.getString("EMAIL_ADDRESS_LAST_UPDATED_USER_ID_TYPE"));
					riskPasswordResetvo.setEmailAddressLastUpdatedProfileId(rs
							.getInt("EMAIL_ADDRESS_LAST_UPDATED_PROFILE_ID"));

					AtRiskForgetUserName riskforgetUsername = new AtRiskForgetUserName();
					riskforgetUsername.setForgotPasswordRequestedDate(rs
							.getDate("FORGOT_PASSWORD_REQUESTED_DATE"));
					riskforgetUsername.setForgotPasswordEmailAddress(rs
							.getString("FORGOT_PASSWORD_EMAIL_ADDRESS"));
					riskforgetUsername.setForgotPasswordUpdatedProfileId(rs
							.getInt("FORGOT_PASSWORD_UPDATED_PROFILE_ID"));
					riskforgetUsername.setForgotPasswordUpdatedUserIdType(rs
							.getString("FORGOT_PASSWORD_UPDATED_USER_ID_TYPE"));
					riskforgetUsername
							.setForgotPasswordUpdatedUserFirstName(rs
									.getString("FORGOT_PASSWORD_UPDATED_USER_FIRST_NAME"));
					riskforgetUsername
							.setForgotPasswordUpdatedUserFirstName(rs
									.getString("FORGOT_PASSWORD_UPDATED_USER_LAST_NAME"));
					
					// Set all risk indicator in assumption that if it's in risk period then only data will be 
					//available in this table
					if(riskWebRegistrationVO.getWebRegistrationDate()!= null)
						riskWebRegistrationVO.setWebRegAtRiskPeriod(true);
					
					if(riskPasswordResetvo.getEmailPasswordResetDate() != null)
						riskPasswordResetvo.setAtRiskPeriod(true);
					
					if(riskforgetUsername.getForgotPasswordRequestedDate()!= null)
						riskforgetUsername.setAtRiskPeriod(true);
					
					voObj.setWebRegistration(riskWebRegistrationVO);
					voObj.setAddresschange(riskAddressChangevo);
					voObj.setPasswordReset(riskPasswordResetvo);
					voObj.setForgetUserName(riskforgetUsername);
					
					currentTS= rs.getTimestamp("CREATED_TS");

				}
				rs.close();
				
				// To check the confirmation letter available or not
				if(voObj != null && voObj.getWebRegistration() != null){
					voObj.getWebRegistration().setWebConfirmationLetterAvailable(
							isWebConfirmationLetterAvailable(atRiskDetils, currentTS));
				}
			} else {
				throw new LoanDaoException(
						"Problem occurred in prepared call: "
								+ DistributionAtRiskDaoSql.SQL_SELECT_DISTRIBUTION_ATRISK_DATA
								+ " for newSubmissionId " + submissionId);
			}

		} catch (SQLException e) {
			throw new LoanDaoException(
					"Problem occurred in prepared call: "
							+ DistributionAtRiskDaoSql.SQL_SELECT_DISTRIBUTION_ATRISK_DATA
							+ " for newSubmissionId " + submissionId, e);
		} catch (SystemException e) {
			throw new LoanDaoException(
					"Problem occurred in prepared call: "
							+ DistributionAtRiskDaoSql.SQL_SELECT_DISTRIBUTION_ATRISK_DATA
							+ " for newSubmissionId " + submissionId, e);
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		return voObj;
	}

	/**
	 * Updates a record in the DISTRIBUTION_AT_RISK_DETAIL.
	 * 
	 * @param submissionId
	 *            The Submission Id
	 * @throws DistributionServiceException
	 *             Thrown if there is an underlying error
	 */
	public static void update(AtRiskDetailsVO objects)
			throws DistributionServiceException {

		if (objects == null) {
			return;
		}

		List<Object> params = null;
		SQLUpdateHandler handler = new SQLUpdateHandler(
				BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME,
				DistributionAtRiskDaoSql.SQL_UPDATE_DISTRIBUTION_ATRISK_DATA,
				DistributionAtRiskDaoSql.UPDATE_TYPES);

		try {
			params = new ArrayList<Object>();

			if (objects.getWebRegistration() != null) {
				params.add(objects.getWebRegistration()
						.getWebRegistrationDate());
				params.add(objects.getWebRegistration()
						.getWebRegConfirmationMailedDate());
			} else {
				params.add(null);
				params.add(null);
			}

			if (objects.getWebRegistration() != null
					&& objects.getWebRegistration().getAddress() != null) {
				params.add(objects.getWebRegistration().getAddress()
						.getAddressLine1());
				params.add(objects.getWebRegistration().getAddress()
						.getAddressLine2());
				params.add(objects.getWebRegistration().getAddress().getCity());
				params.add(objects.getWebRegistration().getAddress()
						.getStateCode());
				params.add(objects.getWebRegistration().getAddress()
						.getZipCode());
				params.add(objects.getWebRegistration().getAddress()
						.getCountryCode());
			} else {
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
			}
			if (objects.getWebRegistration() != null) {
				params.add(objects.getWebRegistration()
						.getConfirmUpdatedProfileId());
				params.add(objects.getWebRegistration()
						.getConfirmUpdatedUserIdType());
			} else {
				params.add(null);
				params.add(null);
			}
			if (objects.getWebRegistration() != null
					&& objects.getWebRegistration() != null) {
				params.add(objects.getWebRegistration().getConfirmUpdatedUserFirstName());
				params.add(objects.getWebRegistration().getConfirmUpdatedUserLastName());
			} else {
				params.add(null);
				params.add(null);
			}

			// AtRiskAddressChangeVO
			if (objects.getAddresschange() != null
					&& objects.getAddresschange().getApprovalAddress() != null) {
				params.add(objects.getAddresschange().getApprovalAddress()
						.getAddressLine1());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getAddressLine2());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getCity());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getStateCode());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getZipCode());
				params.add(objects.getAddresschange().getApprovalAddress()
						.getCountryCode());
			} else {
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
			}

			if (objects.getAddresschange() != null) {
				params.add(objects.getAddresschange()
						.getApprovalUpdatedProfileId());
				params.add(objects.getAddresschange()
						.getApprovalUpdatedUserIdType());
			} else {
				params.add(null);
				params.add(null);
			}

			if (objects.getAddresschange() != null
					&& objects.getAddresschange().getApprovalAddress() != null) {
				params.add(objects.getAddresschange().getCreatedUserFistName());
				params.add(objects.getAddresschange().getCreatedUserLastName());
			} else {
				params.add(null);
				params.add(null);
			}

			// AtRiskPasswordResetVO
			if (objects.getPasswordReset() != null) {
				params.add(objects.getPasswordReset()
						.getEmailPasswordResetDate());
				params.add(objects.getPasswordReset()
						.getEmailPasswordResetEmailAddress());
				params.add(objects.getPasswordReset()
						.getEmailPasswordResetInitiatedProfileId());
				params.add(objects.getPasswordReset()
						.getEmailAddressLastUpdatedProfileId());
				params.add(objects.getPasswordReset()
						.getEmailAddressLastUpdatedUserIdType());
			} else {
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
			}

			// AtRiskForgetUserName
			if (objects.getForgetUserName() != null) {
				params.add(objects.getForgetUserName()
						.getForgotPasswordRequestedDate());
				params.add(objects.getForgetUserName()
						.getForgotPasswordEmailAddress());
				params.add(objects.getForgetUserName()
						.getForgotPasswordUpdatedProfileId());
				params.add(objects.getForgetUserName()
						.getForgotPasswordUpdatedUserIdType());
				params.add(objects.getForgetUserName()
						.getForgotPasswordUpdatedUserFirstName());
				params.add(objects.getForgetUserName()
						.getForgotPasswordUpdatedUserLastName());
			} else {
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
				params.add(null);
			}

			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp curentTimestamp = new java.sql.Timestamp(
					calendar.getTime().getTime());
			params.add(curentTimestamp);

			if (objects.getSubmissionId() != null) {
				params.add(objects.getSubmissionId());
			} else {
				throw new LoanDaoException(
						"DistributionAtRiskDetailsDAO.insert failed as SubmissionId is null");
			}

			handler.update(params.toArray());

		} catch (DAOException e) {
			throw new LoanDaoException(
					"Problem occurred in prepared call: "
							+ DistributionAtRiskDaoSql.SQL_UPDATE_DISTRIBUTION_ATRISK_DATA
							+ " for SubmissionId "
							+ objects.getSubmissionId(), e);
		}

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
	public static void insertOrUpdate(List<AtRiskDetailsVO> objects)
			throws DistributionServiceException {

		if (objects == null) {
			throw new LoanDaoException(
					"DistributionAtRiskDetailsDAO.insert/Update failed as AtRiskDetailsVO object is null");
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer submissionId = null;
		List<Object> params = null;

		try {
			conn = getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);

			for (AtRiskDetailsVO atRiskVO : objects) {

				params = new ArrayList<Object>(1);

				if (atRiskVO.getSubmissionId() != null) {
					params.add(atRiskVO.getSubmissionId());
				} else {
					throw new LoanDaoException(
							"DistributionAtRiskDetailsDAO.insertOrUpdate failed as SubmissionId is null");
				}

				stmt = conn
						.prepareStatement(DistributionAtRiskDaoSql.SQL_CHECK_EXISTS);
				if (atRiskVO.getSubmissionId() != null) {
					stmt.setInt(1, atRiskVO.getSubmissionId());
					stmt.execute();
					rs = stmt.getResultSet();

				//	while (rs != null) {

						if (rs.next()) {
							update(atRiskVO);
							
						} else {
							insert(atRiskVO);
							
						}
						rs.close();
						
					//}
					
					//stmt.close();
				}
				else
				{
					throw new LoanDaoException(
							"DistributionAtRiskDetailsDAO.insertOrUpdate failed as SubmissionId is null");
				}
			}// for loop end.
		} 
		catch (SQLException sqlE) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ DistributionAtRiskDaoSql.SQL_CHECK_EXISTS
					+ "SubmissionId " + submissionId, new DAOException(sqlE));
		} catch (SystemException sysE) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ DistributionAtRiskDaoSql.SQL_CHECK_EXISTS
					+ "SubmissionId " + submissionId, new DAOException(sysE));
		} finally {
			
			close(stmt, conn);
		}
	}
	/**
	 * 
	 * @param atRiskDetils
	 * @param createdTs
	 * @return
	 * @throws DistributionServiceException
	 */
	public static boolean isWebConfirmationLetterAvailable(AtRiskDetailsInputVO atRiskDetils, 
			Timestamp createdTs )throws DistributionServiceException {
		boolean isWebConfirmationLetterAvailable = false;
		
		if (createdTs == null) return false;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		Integer profileId = atRiskDetils.getProfileId();
		Integer	contractId = atRiskDetils.getContractId();
		
		try {
			conn = BaseDatabaseDAO.getDefaultConnection(CLASS_NAME,
					BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);
			
			stmt = conn.prepareStatement(DistributionAtRiskDaoSql.SELECT_WEB_REG_ADDRESS);
			
			
			if (profileId != null && contractId != null ) {
				stmt.setInt(1, profileId);
				stmt.setInt(2, contractId);
				stmt.setTimestamp(3, createdTs);
				rs = stmt.executeQuery();

				if (rs.next()) {
					isWebConfirmationLetterAvailable = true;
				}
				rs.close();
			}

		} catch (SQLException e) {
			throw new LoanDaoException(
					"Problem occurred in prepared call: "
							+ DistributionAtRiskDaoSql.SELECT_WEB_REG_ADDRESS
							+ " for profileId " + profileId + " contractId : "+contractId, e);
		} catch (SystemException e) {
			throw new LoanDaoException(
					"Problem occurred in prepared call: "
							+ DistributionAtRiskDaoSql.SELECT_WEB_REG_ADDRESS
							+ " for profileId " + profileId + " contractId: "+contractId, e);
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		
		
		return isWebConfirmationLetterAvailable;
	}
	
	
}
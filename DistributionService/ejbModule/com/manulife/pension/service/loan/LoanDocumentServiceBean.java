package com.manulife.pension.service.loan;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.loan.dao.LoanDao;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.util.LoanContentConstants;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.EjbLoanSupportDataRetriever;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.document.AmortizationSchedule;
import com.manulife.pension.service.loan.valueobject.document.Instructions;
import com.manulife.pension.service.loan.valueobject.document.LoanDocumentBundle;
import com.manulife.pension.service.loan.valueobject.document.LoanForm;
import com.manulife.pension.service.loan.valueobject.document.PromissoryNote;
import com.manulife.pension.service.loan.valueobject.document.TruthInLendingNotice;
import com.manulife.pension.service.loan.valueobject.document.XMLValueObject;
import com.manulife.util.xml.TransformationHelper;

/**
 * Bean implementation class for Enterprise Bean: LoanService.
 * 
 * @ejb.bean name="LoanDocumentService" display-name="Loan Document Service"
 *           type="Stateless" view-type="both" transaction-type="Container"
 *           jndi-name="com.manulife.pension.service.loan.LoanDocumentServiceHome"
 *           local-jndi-name="com.manulife.pension.service.loan.LoanDocumentServiceLocalHome"
 * 
 * @ejb.interface generate="local,remote"
 * 
 * @ejb.transaction type="Required"
 * 
 * @ejb.util generate="logical"
 * 
 * @websphere.bean
 */
public class LoanDocumentServiceBean implements javax.ejb.SessionBean {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger
			.getLogger(LoanDocumentServiceBean.class);

	private javax.ejb.SessionContext mySessionCtx;

	private LoanDao loanDao = null;

	private LoanSupportDao loanSupportDao = null;

	private LoanService loanService = null;

	private LoanDocumentFactory loanDocumentFactory = null;

	private TransformationHelper transform = null;
	
	/**
	 * The initial output stream size: 50K
	 */
	private static final int OUTPUT_STREAM_SIZE = 1024 * 50;

	/**
	 * {@inheritDoc}
	 */
	public void setSessionContext(final javax.ejb.SessionContext ctx) {
		mySessionCtx = ctx;
		loanDao = new LoanDao();
		loanDocumentFactory = new LoanDocumentFactory();
		transform = new TransformationHelper();
		try {
			loanService = LoanServiceUtil.getLocalHome().create();
			loanSupportDao = new LoanSupportDao(
					BaseDatabaseDAO
							.getDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void ejbActivate() {
	}

	/**
	 * ejbCreate is called as part of the EJB lifecycle.
	 * 
	 * @throws javax.ejb.CreateException
	 *             If an create exception occurs.
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void ejbPassivate() {
		loanDao = null;
		loanSupportDao = null;
		loanService = null;
		loanDocumentFactory = null;
		transform = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void ejbRemove() {
		loanDao = null;
		loanSupportDao = null;
		loanService = null;
		loanDocumentFactory = null;
		transform = null;
	}

	/**
	 * Gets the SessionContext.
	 * 
	 * @return SessionContext The session context.
	 */
	public javax.ejb.SessionContext getSessionContext() {
		return mySessionCtx;
	}

	/**
	 * Generate the HTML version of the truth in lending notice using the loan
	 * object retrieved from the given submission ID
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public String getTruthInLendingNoticeHtml(final Integer userProfileId,
			final Integer contractId, final Integer submissionId) {
		Loan loan = loanService.read(userProfileId, contractId, submissionId);
		
        // Set the effectiveDate and maturityDate back 
        // to their SDB value as we don't want to show the recalculated 
        // values.
        loan.setEffectiveDate(loan.getEffectiveDateOriginalDBValue());
        loan.setMaturityDate(loan.getMaturityDateOriginalDBValue());

		return getTruthInLendingNoticeHtml(loan);
	}

	/**
	 * Generate the HTML version of the truth in lending notice using the given
	 * loan object. The method will be invoked by the Participant website during
	 * acceptance when the loan data is not yet saved to the database.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public String getTruthInLendingNoticeHtml(Loan loan) {
		/*
		 * Create a document bundle that has the truth in lending document
		 * object inside.
		 */
		loan.setDataRetriever(new EjbLoanSupportDataRetriever());
		LoanDocumentBundle documentBundle = loanDocumentFactory
				.getDocumentBundle(loan, TruthInLendingNotice.class);
		ByteArrayOutputStream out = new ByteArrayOutputStream(OUTPUT_STREAM_SIZE);

		TruthInLendingNotice truthInLendingNotice = documentBundle
				.getTruthInLendingNotice();
		String truthInLendingNoticeXslt = getXslt(loan,
				ManagedContent.TRUTH_IN_LENDING_NOTICE_HTML,
				LoanContentConstants.TRUTH_IN_LENDING_NOTICE_HTML);
		String html = getHtml(out, truthInLendingNoticeXslt, truthInLendingNotice);
		return html;
	}

	/**
	 * Generate the HTML version of the amortization schedule using the loan
	 * object retrieved from the given submission ID
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public String getAmortizationScheduleHtml(final Integer userProfileId,
			final Integer contractId, final Integer submissionId) {
		Loan loan = loanService.read(userProfileId, contractId, submissionId);
		return getAmortizationScheduleHtml(loan);
	}

	/**
	 * Generate the HTML version of the amortization schedule using the given
	 * loan object. The method will be invoked by the Participant website during
	 * acceptance when the loan data is not yet saved to the database.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public String getAmortizationScheduleHtml(Loan loan) {
		loan.setDataRetriever(new EjbLoanSupportDataRetriever());
		LoanDocumentBundle documentBundle = loanDocumentFactory
				.getDocumentBundle(loan, AmortizationSchedule.class);
		ByteArrayOutputStream out = new ByteArrayOutputStream(OUTPUT_STREAM_SIZE);

		AmortizationSchedule amortizationSchedule = documentBundle
				.getAmortizationSchedule();
		String amortizationScheduleXslt = getXslt(loan,
				ManagedContent.AMORTIZATION_SCHEDULE_HTML,
				LoanContentConstants.AMORTIZATION_SCHEDULE_HTML);
		String html = getHtml(out, amortizationScheduleXslt, amortizationSchedule);
		return html;
	}

	/**
	 * Generate the HTML version of the promissory note using the loan object
	 * retrieved from the given submission ID
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public String getPromissoryNoteAndIrrevocablePledgeHtml(
			final Integer userProfileId, final Integer contractId,
			final Integer submissionId) {
		Loan loan = loanService.read(userProfileId, contractId, submissionId);
        
        // Set the effectiveDate and maturityDate back 
        // to their SDB value as we don't want to show the recalculated 
        // values.
        loan.setEffectiveDate(loan.getEffectiveDateOriginalDBValue());
        loan.setMaturityDate(loan.getMaturityDateOriginalDBValue());

		return getPromissoryNoteAndIrrevocablePledgeHtml(loan);
	}

	/**
	 * Generate the HTML version of the promissory note using the given loan
	 * object. The method will be invoked by the Participant website during
	 * acceptance when the loan data is not yet saved to the database.
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public String getPromissoryNoteAndIrrevocablePledgeHtml(Loan loan) {
		/*
		 * Create a document bundle that has the promissory note document object
		 * inside.
		 */
		ByteArrayOutputStream out = new ByteArrayOutputStream(OUTPUT_STREAM_SIZE);
		loan.setDataRetriever(new EjbLoanSupportDataRetriever());

		LoanDocumentBundle documentBundle = loanDocumentFactory
				.getDocumentBundle(loan, PromissoryNote.class);
		PromissoryNote promissoryNote = documentBundle.getPromissoryNote();
		String promissoryNoteXslt = getXslt(loan,
				ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_HTML,
				LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_HTML);
		String html = getHtml(out, promissoryNoteXslt, promissoryNote);
		return html;
	}

	/**
	 * Generates Loan Package
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public byte[] getLoanPackage(final Integer userProfileId,
			final Integer contractId, final Integer submissionId) {

		ByteArrayOutputStream out = new ByteArrayOutputStream(OUTPUT_STREAM_SIZE);
		List<byte[]> loanPackage = new ArrayList<byte[]>();
		byte[] loanForm;
		byte[] promissoryNote;
		byte[] truthInLendingNotice;
		byte[] amortizationSchedule;
		byte[] instructionsForParticipant = null;
		byte[] instructionsForPlanAdmin = null;
		byte[] loanRequestInstructions = null;

		Loan loan = loanService.read(userProfileId, contractId, submissionId);
		LoanDocumentBundle documentBundle = loanDocumentFactory
				.getDocumentBundle(loan, LoanForm.class, PromissoryNote.class,
						TruthInLendingNotice.class, AmortizationSchedule.class,
						Instructions.class);

		// Create all the forms
		String promissoryNoteXslt = getXslt(loan,
				ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_PDF,
				LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_PDF);
		String truthInLendingNoticeXslt = getXslt(loan,
				ManagedContent.TRUTH_IN_LENDING_NOTICE_PDF,
				LoanContentConstants.TRUTH_IN_LENDING_NOTICE_PDF);
		String loanFormXslt = getXslt(loan, ManagedContent.LOAN_FORM_PDF,
				LoanContentConstants.LOAN_FORM_PDF);
		String amortizationScheduleXslt = getXslt(loan,
				ManagedContent.AMORTIZATION_SCHEDULE_PDF,
				LoanContentConstants.AMORTIZATION_SCHEDULE_PDF);
		
		boolean isBundledContract = documentBundle.getLoanForm().isBundledGaIndicator();
		
		if(!isBundledContract){
			String instructionsForParticipantXslt = getXslt(loan,
					ManagedContent.LOAN_PACKAGE_INSTRUCTIONS_FOR_PARTICIPANT,
					LoanContentConstants.LOAN_PACKAGE_INSTRUCTIONS_FOR_PARTICIPANT);
			String instructionsForPlanAdminXslt = getXslt(loan,
					ManagedContent.LOAN_PACKAGE_INSTRUCTIONS_FOR_ADMIN,
					LoanContentConstants.LOAN_PACKAGE_INSTRUCTIONS_FOR_ADMIN);
			
			instructionsForParticipant = getPdf(out, instructionsForParticipantXslt,
					documentBundle.getInstructionsForParticipant());
			out.reset();			
			instructionsForPlanAdmin = getPdf(out, instructionsForPlanAdminXslt,
					documentBundle.getInstructionsForAdministrator());
			out.reset();			
		}

		promissoryNote = getPdf(out, promissoryNoteXslt, documentBundle.getPromissoryNote());
		out.reset();
		truthInLendingNotice = getPdf(out, truthInLendingNoticeXslt, documentBundle.getTruthInLendingNotice());
		out.reset();
		loanForm = getPdf(out, loanFormXslt, documentBundle.getLoanForm());
		out.reset();
		amortizationSchedule = getPdf(out, amortizationScheduleXslt, documentBundle.getAmortizationSchedule());
		out.reset();
		
		// Add all the forms to the loan package list to get the final pdf
		if(!isBundledContract){
			loanPackage.add(instructionsForParticipant);
			loanPackage.add(instructionsForPlanAdmin);
		}
		loanPackage.add(promissoryNote);
		loanPackage.add(truthInLendingNotice);
		loanPackage.add(amortizationSchedule);
		
		loanPackage.add(loanForm);
		transform.concatPDF(loanPackage, out);

		return ((ByteArrayOutputStream) out).toByteArray();
	}

	/**
	 * Generates Loan Document
	 * 
	 * @ejb.interface-method view-type="local"
	 */
	public byte[] getLoanDocuments(final Integer userProfileId,
			final Integer contractId, final Integer submissionId,
			final boolean useEffectiveDateFromDB) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(OUTPUT_STREAM_SIZE);
		List<byte[]> loanDocument = new ArrayList<byte[]>();
		byte[] promissoryNote;
		byte[] truthInLendingNotice;
		byte[] amortizationSchedule;

		// Create all the forms
		Loan loan = loanService.read(userProfileId, contractId, submissionId);
		if (useEffectiveDateFromDB) {
		    // Override the recalculated effectiveDate with the one from the SDB.
		    loan.setEffectiveDate(loan.getEffectiveDateOriginalDBValue());
		}
		LoanDocumentBundle documentBundle = loanDocumentFactory
				.getDocumentBundle(loan, PromissoryNote.class,
						TruthInLendingNotice.class, AmortizationSchedule.class);

		String promissoryNoteXslt = getXslt(loan,
				ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_PDF,
				LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_PDF);
		String truthInLendingNoticeXslt = getXslt(loan,
				ManagedContent.TRUTH_IN_LENDING_NOTICE_PDF,
				LoanContentConstants.TRUTH_IN_LENDING_NOTICE_PDF);
		String amortizationScheduleXslt = getXslt(loan,
				ManagedContent.AMORTIZATION_SCHEDULE_PDF,
				LoanContentConstants.AMORTIZATION_SCHEDULE_PDF);

		promissoryNote = getPdf(out, promissoryNoteXslt, documentBundle
				.getPromissoryNote());
		out.reset();
		
		truthInLendingNotice = getPdf(out, truthInLendingNoticeXslt, documentBundle
				.getTruthInLendingNotice());
		out.reset();
		
		amortizationSchedule = getPdf(out, amortizationScheduleXslt, documentBundle
				.getAmortizationSchedule());
		out.reset();

		loanDocument.add(promissoryNote);
		loanDocument.add(truthInLendingNotice);
		loanDocument.add(amortizationSchedule);
		transform.concatPDF(loanDocument, out);

		// Adds DRAFT water mark to the PDF
		if (loanDocumentFactory.isDraftDocument(loan)) {
			transform.addDraftWaterMark(out);
		}

		return ((ByteArrayOutputStream) out).toByteArray();
	}

	/**
	 * Returns the latest content retrieved from the CMA database.
	 * 
	 * @ejb.interface-method view-type="local"
	 * @param contentId
	 * @return
	 */
	public ContentText getContentTextByKey(int contentKey) {
		LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		try {
			return loanDataHelper
					.getContentTextByKey(GlobalConstants.PSW_APPLICATION_ID,
							Location.US, contentKey);
		} catch (LoanDaoException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Returns the latest content retrieved from the CMA database.
	 * 
	 * @ejb.interface-method view-type="local"
	 * @param contentId
	 * @return
	 */
	public ContentText getContentTextById(int contentId) {
		LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
				.getLoanDataHelper();
		try {
			return loanDataHelper.getContentTextById(
					GlobalConstants.PSW_APPLICATION_ID, Location.US, contentId);
		} catch (LoanDaoException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	private String getXslt(Loan loan, String managedContentTypeCode,
			Integer cmaContentKey) {
		ManagedContent content = loan.getManagedContent(managedContentTypeCode);
		String xsltSource = null;
		if (content == null || content.getContentId() == null) {
			/*
			 * Have to check if the content ID is null as well because the UI
			 * may set the ManagedContent object into the loan.
			 */
			xsltSource = getContentTextByKey(cmaContentKey).getText();
		} else {
			xsltSource = getContentTextById(content.getContentId()).getText();
		}
		return xsltSource;
	}

	/**
	 * Generates PDF version of a document.
	 * 
	 * @param promissoryNote
	 * @return
	 */
	private byte[] getPdf(OutputStream out, String xslSource,
			XMLValueObject document) {
		String xmlSource = document.toXML();
		transform.transformFopPdf(xmlSource, xslSource, out);
		return ((ByteArrayOutputStream) out).toByteArray();
	}

	/**
	 * Generates PDF version of a document.
	 * 
	 * @param promissoryNote
	 * @return
	 */
	private String getHtml(OutputStream out, String xslSource,
			XMLValueObject document) {
		String xmlSource = document.toXML();
		transform.transform(xmlSource, xslSource, out);
		return out.toString();
	}

}

package com.manulife.pension.ps.web.withdrawal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.ps.web.controller.PsAutoActionLabelForm;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.fee.valueobject.WithdrawalTransactionalFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * WithdrawalForm is the action form for the withdrawals entry steps.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.3 2006/08/23 17:03:44
 */
public class WithdrawalForm extends PsAutoActionLabelForm implements
        WithdrawalStepOneProperties {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(WithdrawalForm.class);

    private static final String STEP_1_PAGE_KEY = "S1";

    private static final String STEP_2_PAGE_KEY = "S2";

    // The value object - use fields directly for types that don't
    // need conversion.
    private WithdrawalRequestUi withdrawalRequestUi;

    // This contains the data for the dropdown lists.
    // Each list in the map is keyed from constants in CodeLookupCache.
    private Map lookupData;

    // Contains a map of user names keyed by profile ID.
    private Map userNames;

    // Contains a collection of state tax options
    private Collection<StateTaxVO> stateTaxOptions;

    private String dirty = "false";

    private boolean errorsExist = false;

    private String pageAllowed = StringUtils.EMPTY;

    private String actionInvoked = StringUtils.EMPTY;

    private String APPROVED = "Approved";
    
    private WithdrawalTransactionalFee withdrawalTransactionalFee;

    /**
     * 
     */
    public WithdrawalForm() {
        super();
    }

    /**
     * @return the withdrawalRequestUi
     */
    public WithdrawalRequestUi getWithdrawalRequestUi() {
        if (withdrawalRequestUi == null) {
            withdrawalRequestUi = getWithdrawalRequestUiStub();
        } // fi
        return withdrawalRequestUi;
    }

    /**
     * Gets a WithdrawalRequestUi object to fill in form values (struts populated), which happens
     * before out action kicks in. Then if the stub was required, it's likely a bookmark.
     * 
     * @return WithdrawalRequestUi A blank object, initialized big enough to hold most requests.
     */
    private WithdrawalRequestUi getWithdrawalRequestUiStub() {
        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());
        withdrawalRequest.getMoneyTypes().add(new WithdrawalRequestMoneyType());

        withdrawalRequest.getFees().add(new WithdrawalRequestFee());

        withdrawalRequest.getDeclarations().add(new WithdrawalRequestDeclaration());
        withdrawalRequest.getDeclarations().add(new WithdrawalRequestDeclaration());
        withdrawalRequest.getDeclarations().add(new WithdrawalRequestDeclaration());
        withdrawalRequest.getDeclarations().add(new WithdrawalRequestDeclaration());
        withdrawalRequest.getDeclarations().add(new WithdrawalRequestDeclaration());

        final WithdrawalRequestRecipient withdrawalRequestRecipient = new WithdrawalRequestRecipient();

        withdrawalRequestRecipient.getPayees().add(new WithdrawalRequestPayee());
        withdrawalRequestRecipient.getPayees().add(new WithdrawalRequestPayee());

        withdrawalRequest.getRecipients().add(withdrawalRequestRecipient);

        final WithdrawalRequestUi withdrawalRequestUiResult = new WithdrawalRequestUi(
                withdrawalRequest);

        return withdrawalRequestUiResult;
    }

    /**
     * @param withdrawalRequestUi the withdrawalRequestUi to set
     */
    public void setWithdrawalRequestUi(final WithdrawalRequestUi withdrawalRequestUi) {
        this.withdrawalRequestUi = withdrawalRequestUi;
    }

    /**
     * @return the lookupData
     */
    public Map getLookupData() {
        return lookupData;
    }

    /**
     * @param lookupData the lookupData to set
     */
    public void setLookupData(final Map lookupData) {
        this.lookupData = lookupData;
    }

    /**
     * @return the user names lookup data
     */
    public Map getUserNames() {
        return userNames;
    }

    /**
     * @param userNames - The user names lookup data to set.
     */
    public void setUserNames(Map userNames) {
        this.userNames = userNames;
    }

    /**
     * Returns true if the Wit hdrawal Type is Simple
     * 
     * @param code
     * @return
     */
    protected boolean isWithdrawalTypeSimple(String code) {
        boolean isSimple = true;
        if (WithdrawalReason.RETIREMENT.equals(code) || WithdrawalReason.TERMINATION.equals(code)
                || WithdrawalReason.MANDATORY_DISTRIBUTION_TERM.equals(code)) {
            isSimple = false;
        }
        return isSimple;

    }

    /**
     * Read Only form property: Description of the current Payment To setting
     * 
     * @return Description of the current PaymentTo code
     */
    public String getPaymentToDescription() {
        for (DeCodeVO item : (Collection<DeCodeVO>) lookupData.get("PAYMENT_TO_TYPE")) {
            if (item.getCode().equals(withdrawalRequestUi.getWithdrawalRequest().getPaymentTo())) {
                return item.getDescription();
            } // fi
        } // end for
        return "";
    }

    /**
     * Read Only form property: Description of the current Withdrawal Reason code
     * 
     * @return Description of the current withdrawal reason code
     */
    public String getWithdrawalReasonDescription() {

        for (DeCodeVO item : (Collection<DeCodeVO>) lookupData
                .get(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS)) {
            if (item.getCode().equals(withdrawalRequestUi.getWithdrawalRequest().getReasonCode())) {
                return item.getDescription();
            } // fi
        } // end for
        return StringUtils.EMPTY;
    }

    /**
     * @return returns "true" if the form is dirty
     */
    public String getDirty() {
        return dirty;
    }

    /**
     * @param dirty
     */
    public void setDirty(final String dirty) {
        this.dirty = Boolean.parseBoolean(dirty) ? Boolean.TRUE.toString() : Boolean.FALSE
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset( final HttpServletRequest httpServletRequest) {

        // Reset the declarations
        if (withdrawalRequestUi != null) {
            withdrawalRequestUi.setSelectedDeclarations(ArrayUtils.EMPTY_STRING_ARRAY);
        }

        super.reset( httpServletRequest);
    }

    public Collection<StateTaxVO> getStateTaxOptions() {
        return stateTaxOptions;
    }

    public void setStateTaxOptions(final Collection<StateTaxVO> stateTaxOptions) {
        this.stateTaxOptions = stateTaxOptions;
    }

    /**
     * Determines if the request is ready for processing, or if errors/warnings exist to halt
     * processing.
     * 
     * @return boolean - True if processing is halted, false otherwise.
     */
    public boolean getErrorsExist() {
        return (!(getWithdrawalRequestUi().getWithdrawalRequest().isValidToProcess()));
    }

    /**
     * Sets that step 1 page is allowed.
     */
    public void setStep1Allowed() {
        pageAllowed = STEP_1_PAGE_KEY;
    }

    /**
     * Sets that step 2 page is allowed.
     */
    public void setStep2Allowed() {
        pageAllowed = STEP_2_PAGE_KEY;
    }

    /**
     * Sets that neither step 1 or step 2 page is allowed.
     */
    public void clearPageAllowed() {
        pageAllowed = StringUtils.EMPTY;
    }

    /**
     * Queries if step 1 page is allowed.
     */
    public boolean isStep1Allowed() {
        return StringUtils.equals(pageAllowed, STEP_1_PAGE_KEY);
    }

    /**
     * Queries if step 2 page is allowed.
     */
    public boolean isStep2Allowed() {
        return StringUtils.equals(pageAllowed, STEP_2_PAGE_KEY);
    }

    /**
     * Queries page allowed value.
     */
    public String getPageAllowed() {
        return pageAllowed;
    }

    /**
     * Returns true if no errors exist, the action is approve and the legalese text has been shown
     * else false
     * 
     * @return true if the legalse text is shown
     */
    public boolean getIsLegaleseTextConfirmed() {
        boolean flag = false;
        if (!getErrorsExist()
                && getAction().equals("approve")
                && BooleanUtils.isTrue(withdrawalRequestUi.getWithdrawalRequest()
                        .getIsLegaleseConfirmed())) {
            flag = true;
        }
        return flag;
    }

    public String getActionInvoked() {
        return actionInvoked;
    }

    public void setActionInvoked(final String actionInvoked) {
        this.actionInvoked = actionInvoked;
    }

    /**
     * Cleans the form of data we don't want staying in the session. Should be called when we exit
     * from the initiates or review pages through normal page flow logic.
     */
    public void clean() {

        withdrawalRequestUi = null;
        lookupData = null;
        userNames = null;
        stateTaxOptions = null;
        clearPageAllowed();
        actionLabel = null;
    }

    /**
     * Parses the Request staus collection in the Lookup data to display proper status description
     * on the view page.
     * 
     */
    public void parseRequestStatusCollection() {
        Collection<DeCodeVO> newRequestStatus = new ArrayList<DeCodeVO>();
        Collection<DeCodeVO> coll = (Collection<DeCodeVO>) lookupData
                .get(CodeLookupCache.WITHDRAWAL_REQUEST_STATUS_ORDERED);
        for (Iterator iter = coll.iterator(); iter.hasNext();) {
            DeCodeVO element = (DeCodeVO) iter.next();
            if (element.getCode().length() == 2) {
                newRequestStatus.add(element);
            }
            if (StringUtils.equals(APPROVED, element.getDescription())) {
                StringTokenizer st = new StringTokenizer(element.getCode(), ",");
                while (st.hasMoreTokens()) {
                	DeCodeVO dvo = new DeCodeVO(StringUtils.EMPTY, StringUtils.EMPTY);
                    String code = st.nextToken();
                    dvo.setCode(code.trim());
                    dvo.setDescription(APPROVED);
                    newRequestStatus.add(dvo);
                }

            }

        }
        lookupData.put(CodeLookupCache.WITHDRAWAL_REQUEST_STATUS_ORDERED, newRequestStatus);
    }
    
	public WithdrawalTransactionalFee getWithdrawalTransactionalFee() {
		return withdrawalTransactionalFee;
	}

	public void setWithdrawalTransactionalFee(
			WithdrawalTransactionalFee withdrawalTransactionalFee) {
		this.withdrawalTransactionalFee = withdrawalTransactionalFee;
	}
}

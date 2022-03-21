package com.manulife.pension.ps.web.withdrawal;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.VestingHelper;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.LoanAndWithdrawalDisplayHelper;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.distribution.valueobject.RequestType;
import com.manulife.pension.service.environment.util.LookupDataHelper;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.environment.valueobject.StateTaxType;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.SecurityHelper;
import com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.BaseWithdrawal;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.PayeePaymentInstruction;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.SSNRender;

/**
 * WithdrawalRequestUi provides String fields for non-String fields in the {@link WithdrawalRequest}
 * object. To access String fields, just access the {@link WithdrawalRequest} object directly, as
 * it's a field of this object.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.6 2006/09/05 15:32:19
 */
public class WithdrawalRequestUi extends BaseWithdrawalUiObject {

    /**
     * LINK_EXPIRY_DURATION_IN_MONTHS.
     */
    private static final int LINK_EXPIRY_DURATION_IN_MONTHS = 6;

    /**
     * USER_ROLE_CODE_PLAN_SPONSOR.
     */
    public static final String USER_ROLE_CODE_PLAN_SPONSOR = "PS";

    /**
     * USER_ROLE_CODE_TPA.
     */
    public static final String USER_ROLE_CODE_TPA = "TP";

    /**
     * USER_ROLE_CODE_BUNDLED_GA
     */
    public static final String USER_ROLE_CODE_BUNDLED_GA = "JH";
    
    
    private static final String TAX_PERCENT_PATTERN = "#0.0000";

    private static final DecimalFormat TAX_FORMATTER = new DecimalFormat(TAX_PERCENT_PATTERN);

    /**
     * Currency formatter.
     */
    public static final String CURRENCY_FORMAT_PATTERN = "#,##0.00";

    /**
     * The TAX_PERCENTAGE_ZERO_OPTION.
     */
    public static final String TAX_PERCENTAGE_ZERO_OPTION = formatDecimalFormatter(BigDecimal.ZERO);

    public static final Logger logger = Logger.getLogger(WithdrawalRequestUi.class);

    public static final String US_SITE_ID = "019";

    public static final String NY_SITE_ID = "020";
    
    //synchronizes this method to avoid race condition.
    public static synchronized String formatDecimalFormatter(BigDecimal value) { 
        return TAX_FORMATTER.format(value); 
    }

    private static final Collection SHOW_DELETE_STATUS_CODES = new ArrayList<String>() {
        /**
         * Default Serial Version UID.
         */
        private static final long serialVersionUID = 1L;
        {
            add(WithdrawalStateEnum.DRAFT.getStatusCode());
            add(WithdrawalStateEnum.PENDING_REVIEW.getStatusCode());
            add(WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode());
        }
    };

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private WithdrawalRequest withdrawalRequest;
  
    private String atRiskApprovalText;
    
    private ArrayList<String> atRiskdetailText;

    private static final String VO_BEAN_NAME = "withdrawalRequest";

    private static final String[] UI_FIELDS = { "birthDate", "contractId", "createdProfileId",
            "deathDate", "disabilityDate", "expirationDate", "finalContributionDate",
            "mostRecentPriorContributionDate", "participantId", "participantLeavingPlanInd",
            "participantUSCitizenInd", "employeeProfileId", "requestDate", "terminationDate",
           "retirementDate", "withdrawalAmount", "legallyMarriedInd" };

    // We create String fields for any input types that aren't String.
    private String birthDate;

    private String contractId;

    private String createdProfileId;

    private String deathDate;

    private String disabilityDate;

    private String expirationDate;

    private String finalContributionDate;

    private String mostRecentPriorContributionDate;

    private String participantId;

    private String participantLeavingPlanInd;

    private String participantUSCitizenInd;

    private String employeeProfileId;

    private String requestDate;

    private String terminationDate;

    private String retirementDate;

    private String withdrawalAmount;
    
    private Boolean contractHasNonFullyVestedMoneyTypes;

    private boolean isViewAction = false;

    private boolean isConfirmAction = false;

    // We create Collections to hold UI classes for coll.
    private String[] selectedDeclarations = ArrayUtils.EMPTY_STRING_ARRAY;

    private Collection<WithdrawalRequestFeeUi> fees;

    private Collection<WithdrawalRequestMoneyTypeUi> moneyTypes;

    private Collection<WithdrawalRequestRecipientUi> recipients;

    // attributes defined for PDF
    boolean isRequestSendForReview;
    boolean isRequestSendForApprove;
    boolean showIrsLoanDistribution;
    boolean isRequestApproved;
    boolean showExpectedProcessingDate;
    boolean showStaticContent;
    boolean showHardshipReasonAndHardshipDescription;
    boolean showWmsiPenchecks;
    boolean showDisabilityDate;
    boolean showRetirementDate;
    boolean showTerminationDate;
    String reasonDescriptionForDisplay = StringUtils.EMPTY;
    String participantMaskedSSN = StringUtils.EMPTY;
    boolean hasPbaOrLoans;
    boolean showFinalContributionDate;
    boolean isParticipantInitiated;
    boolean wmsiOrPenchecksSelected;
    boolean showAvailableWithdrawalAmountColumn;
    boolean showAvailableforHardshipColumn;
    boolean showRequestedWithdrawalAmountPercentColumn;
    boolean showTotalRequestedWithdrawalAmount;
    boolean showTotalAvailableWithdrawalAmount;
    boolean participantHasPartialStatus;
    boolean hasPre1987MoneyTypes;
    boolean showReasonIsFullyVestedFromPlanSpecialMessage;
    boolean showOptionForUnvestedAmount;
    boolean showTaxWitholdingSection;
    boolean hasTaxNoticeDeclaration;
    boolean isIRSSpecialTaxNotice;
    boolean hasWaitPeriodWaveDeclaration;
    boolean showParticipantAgreedLegaleseContent;
    boolean showItWasCertifiedLabel;
    boolean hasIraProviderDeclaration;
    boolean hasBothPbaAndLoans;
    boolean hasPbaOnly;
    boolean hasLoansOnly;
    boolean printParticipant;
    String stateTaxType;
    BigDecimal federalTaxPercent;
    BigDecimal stateTaxPercent;
    BigDecimal totalRequestedWithdrawalAmount;
    BigDecimal totalAvailableWithdrawalAmount;
    BigDecimal totalOutstandingLoanAmt;
    WithdrawalCmaContent cmaContent;
    boolean showAtRiskDeclaration;
    boolean hasRiskIndicatorDeclaration;
    boolean isAtRiskStateCodeEmpty;
    boolean isAtRiskZipCodeEmpty;
    boolean showAtRiskAddressCountry;
    boolean isParticipantGIFLEnabled;
    boolean showLegallyMarriedAsEditable;
    
    boolean nonTaxablePayeeFlag;
    boolean rothPayeeFlag;
    
    
    

    String amount;    
	
	String legallyMarriedInd;
    
	 private String botInd;
	    
	   private String earInd;
	    
	    private String rothInd;
	    
	    private String nonRothInd;    
	 

		private String Tb;
	    private String Pa;
	    private String Paat;
	    private String Par;
	    private String Rb;
	    private String Nrat;
	    /**
		 * @return the payDirectlyTome
		 */
		public String getPayDirectlyTome() {
			return payDirectlyTome;
		}

		/**
		 * @param payDirectlyTome the payDirectlyTome to set
		 */
		public void setPayDirectlyTome(String payDirectlyTome) {
			this.payDirectlyTome = payDirectlyTome;
		}

		/**
		 * @return the payDirectlyTomeAmount
		 */
		public String getPayDirectlyTomeAmount() {
			return payDirectlyTomeAmount;
		}

		/**
		 * @param payDirectlyTomeAmount the payDirectlyTomeAmount to set
		 */
		public void setPayDirectlyTomeAmount(String payDirectlyTomeAmount) {
			this.payDirectlyTomeAmount = payDirectlyTomeAmount;
		}

		private String payDirectlyTome;
		private String payDirectlyTomeAmount;
		private String validatePA;
		private String validatePAAT;
		private String validatePAR;
		
		/**
		 * @return the validatePA
		 */
		public String getValidatePA() {
			return validatePA;
		}

		/**
		 * @param validatePA the validatePA to set
		 */
		public void setValidatePA(String validatePA) {
			this.validatePA = validatePA;
		}

		/**
		 * @return the validatePAAT
		 */
		public String getValidatePAAT() {
			return validatePAAT;
		}

		/**
		 * @param validatePAAT the validatePAAT to set
		 */
		public void setValidatePAAT(String validatePAAT) {
			this.validatePAAT = validatePAAT;
		}

		/**
		 * @return the validatePAR
		 */
		public String getValidatePAR() {
			return validatePAR;
		}

		/**
		 * @param validatePAR the validatePAR to set
		 */
		public void setValidatePAR(String validatePAR) {
			this.validatePAR = validatePAR;
		}

		
	    /**
		 * @return the nratCategory
		 */
		public String getNratCategory() {
			return nratCategory;
		}

		/**
		 * @param nratCategory the nratCategory to set
		 */
		public void setNratCategory(String nratCategory) {
			this.nratCategory = nratCategory;
		}

		/**
		 * @return the rbCategory
		 */
		public String getRbCategory() {
			return rbCategory;
		}

		/**
		 * @param rbCategory the rbCategory to set
		 */
		public void setRbCategory(String rbCategory) {
			this.rbCategory = rbCategory;
		}

		/**
		 * @return the tbCategory
		 */
		public String getTbCategory() {
			return tbCategory;
		}

		/**
		 * @param tbCategory the tbCategory to set
		 */
		public void setTbCategory(String tbCategory) {
			this.tbCategory = tbCategory;
		}

		String nratCategory;
	    String rbCategory;
	    String tbCategory;
	    // for error mark multipayee
	    String missingRollover;
	    String selectOnePayee;
	    /**
		 * @return the missingRollover
		 */
		public String getMissingRollover() {
			return missingRollover;
		}

		/**
		 * @param missingRollover the missingRollover to set
		 */
		public void setMissingRollover(String missingRollover) {
			this.missingRollover = missingRollover;
		}

		/**
		 * @return the selectOnePayee
		 */
		public String getSelectOnePayee() {
			return selectOnePayee;
		}

		/**
		 * @param selectOnePayee the selectOnePayee to set
		 */
		public void setSelectOnePayee(String selectOnePayee) {
			this.selectOnePayee = selectOnePayee;
		}
	  
    final String unmangedImagePath = String.valueOf(PdfHelper.class.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX));
    
    /**
     * Default Constructor.
     * 
     * @param withdrawalRequest The request to load the data from.
     */
    public WithdrawalRequestUi(final WithdrawalRequest withdrawalRequest) {
        super(UI_FIELDS, VO_BEAN_NAME);
        setWithdrawalRequest(withdrawalRequest);

        convertFromBean();
    }

    /**
     * Default Constructor.
     */
    public WithdrawalRequestUi() {
        super(UI_FIELDS, VO_BEAN_NAME);
        // Load the blank bean.
        this.withdrawalRequest = new WithdrawalRequest();
        // Create empty lists.
        setSelectedDeclarations(new String[0]);
        setFees(new ArrayList<WithdrawalRequestFeeUi>());
        setMoneyTypes(new ArrayList<WithdrawalRequestMoneyTypeUi>());
        setRecipients(new ArrayList<WithdrawalRequestRecipientUi>());
    }

    /**
     * Converts the matching fields from the WithdrawalRequest bean, to this object.
     */
    public final void convertFromBean() {
        try {
            BeanUtils.copyProperties(this, withdrawalRequest);

            // Handle withdrawal amount formatting
            if (withdrawalRequest.getWithdrawalAmount() == null) {
                withdrawalAmount = StringUtils.EMPTY;
            } else {
                final DecimalFormat formatter = new DecimalFormat(CURRENCY_FORMAT_PATTERN);
                withdrawalAmount = formatter.format(withdrawalRequest.getWithdrawalAmount());
            } // fi
        } catch (IllegalAccessException illegalAccessException) {
            throw new NestableRuntimeException(illegalAccessException);
        } catch (InvocationTargetException invocationTargetException) {
            throw new NestableRuntimeException(invocationTargetException);
        } // end try/catch

        // Load the MoneyTypes
        if (CollectionUtils.isEmpty(withdrawalRequest.getMoneyTypes())) {
            setMoneyTypes(new ArrayList<WithdrawalRequestMoneyTypeUi>(0));
        } else {
            setMoneyTypes(new ArrayList<WithdrawalRequestMoneyTypeUi>(withdrawalRequest
                    .getMoneyTypes().size()));

            for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                    .getMoneyTypes()) {

                getMoneyTypes().add(
                        new WithdrawalRequestMoneyTypeUi(withdrawalRequestMoneyType, this));
            } // end for
        } // fi

        // Load selectedDeclarations
        if (CollectionUtils.isEmpty(withdrawalRequest.getDeclarations())) {
            setSelectedDeclarations(ArrayUtils.EMPTY_STRING_ARRAY);
        } else {

            final String[] array = new String[withdrawalRequest.getDeclarations().size()];

            int i = 0;
            for (Declaration withdrawalRequestDeclaration : withdrawalRequest
                    .getDeclarations()) {

                array[i] = withdrawalRequestDeclaration.getTypeCode();
                i++;
            } // end for

            setSelectedDeclarations(array);
        } // fi
        
      //PAR: get At Risk Text
        try {
        	if(getWithdrawalRequest().getIsParticipantCreated() 
        			&& getWithdrawalRequest().getRequestRiskIndicator()
        				&& getWithdrawalRequest().getAtRiskDetailsVO() != null){

        		AtRiskDetailsInputVO atRiskDetils = new AtRiskDetailsInputVO();

        		atRiskDetils.setSubmissionId(getWithdrawalRequest().getSubmissionId());
        		atRiskDetils.setLoanOrWithdrawalReq(RequestType.WITHDRAWAL);
        		atRiskDetils.setProfileId((int) getWithdrawalRequest().getEmployeeProfileId());
        		atRiskDetils.setContractId(getWithdrawalRequest().getContractId());
        		atRiskDetils.setParticipantInitiated(getWithdrawalRequest().getIsParticipantCreated());

        		List<Object>  atRiskTextList = LoanAndWithdrawalDisplayHelper.atRiskDisplayText(atRiskDetils,getWithdrawalRequest().getAtRiskDetailsVO());
            	
        		if(atRiskTextList != null && atRiskTextList.size() > 0){
        		setAtRiskApprovalText(((ArrayList<String>)atRiskTextList.get(0)).get(0));
        		setAtRiskdetailText((ArrayList<String>)atRiskTextList.get(1));
        		}
        	}

        } catch (SystemException e) {
        	throw new RuntimeException(e);
		}

        // Load fees
        if (CollectionUtils.isEmpty(withdrawalRequest.getFees())) {
            setFees(new ArrayList<WithdrawalRequestFeeUi>(0));
        } else {
            setFees(new ArrayList<WithdrawalRequestFeeUi>(withdrawalRequest.getFees().size()));

            for (Fee withdrawalRequestFee : withdrawalRequest.getFees()) {

                getFees().add(new WithdrawalRequestFeeUi((WithdrawalRequestFee)withdrawalRequestFee));
            } // end for
        } // fi

        // Load recipients
        if (CollectionUtils.isEmpty(withdrawalRequest.getRecipients())) {
            setRecipients(new ArrayList<WithdrawalRequestRecipientUi>(0));
        } else {
            setRecipients(new ArrayList<WithdrawalRequestRecipientUi>(withdrawalRequest
                    .getRecipients().size()));

            for (Recipient withdrawalRequestRecipient : withdrawalRequest
                    .getRecipients()) {

                getRecipients().add(
                        new WithdrawalRequestRecipientUi((WithdrawalRequestRecipient)withdrawalRequestRecipient, this));
            } // end for
        } // fi

        // Load the federal tax.
        if (getWithdrawalRequest().getFederalTaxVo() != null) {
            try {
                BeanUtils.copyProperty(this, "federalTaxPercentage", getWithdrawalRequest()
                        .getFederalTaxVo().getTaxPercentage());
            } catch (IllegalAccessException illegalAccessException) {
                throw new NestableRuntimeException(illegalAccessException);
            } catch (InvocationTargetException invocationTargetException) {
                throw new NestableRuntimeException(invocationTargetException);
            } // end try/catch
        } // fi
    }

    /**
     * Converts the matching fields from this object, to the WithdrawalRequest bean.
     * 
     * @param ignoreDeclarations True if the converison should ignore declarations.
     */
    public final void convertToBean(final boolean ignoreDeclarations) {

        try {
            BeanUtils.copyProperties(withdrawalRequest, this);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }

        // Load the MoneyTypes
        if (CollectionUtils.isEmpty(getMoneyTypes())) {
            withdrawalRequest.setMoneyTypes(new ArrayList<WithdrawalRequestMoneyType>(0));
        } else {
            final Collection<WithdrawalRequestMoneyType> convertedMoneyTypes = new ArrayList<WithdrawalRequestMoneyType>(
                    getMoneyTypes().size());

            for (WithdrawalRequestMoneyTypeUi withdrawalRequestMoneyTypeUi : getMoneyTypes()) {

                withdrawalRequestMoneyTypeUi.convertToBean();

                convertedMoneyTypes.add(withdrawalRequestMoneyTypeUi
                        .getWithdrawalRequestMoneyType());
            } // end for
            withdrawalRequest.setMoneyTypes(convertedMoneyTypes);
        } // fi

        // Load the fees
        if (CollectionUtils.isEmpty(getFees())) {
            withdrawalRequest.setFees(new ArrayList<Fee>(0));
        } else {
            final Collection<Fee> convertedFees = new ArrayList<Fee>(
                    getFees().size());

            for (WithdrawalRequestFeeUi withdrawalRequestFeeUi : getFees()) {

                withdrawalRequestFeeUi.convertToBean();

                convertedFees.add(withdrawalRequestFeeUi.getWithdrawalRequestFee());
            } // end for
            withdrawalRequest.setFees(convertedFees);
        } // fi

        // Added for POW. This ensures that the declarations are not overwritten
        // if the WithdrawalRequestUi object does not have declarations initialised

        if (ArrayUtils.isEmpty(getSelectedDeclarations())
                && CollectionUtils.isNotEmpty(withdrawalRequest.getDeclarations())) {
            String[] array = ArrayUtils.EMPTY_STRING_ARRAY;
            if (withdrawalRequest.getIsParticipantCreated() || isViewOnly()) {
                Collection<Declaration> declarations = cleanUpDeclarations(withdrawalRequest
                        .getDeclarations());
                withdrawalRequest.setDeclarations(declarations);

                array = new String[withdrawalRequest.getDeclarations().size()];
                int i = 0;
	            for (Declaration withdrawalRequestDeclaration : withdrawalRequest
	                    .getDeclarations()) {
	
	                array[i] = withdrawalRequestDeclaration.getTypeCode();
	                i++;
	            } // end for
            } 
            setSelectedDeclarations(array);
        }
        if (!ignoreDeclarations) {
            // // Load the selectedDeclarations
            if (ArrayUtils.isEmpty(getSelectedDeclarations())) {
                withdrawalRequest.setDeclarations(new ArrayList<Declaration>(0));
            } else {
                final Collection<Declaration> convertedDeclarations = new ArrayList<Declaration>(
                        getSelectedDeclarations().length);

                WithdrawalRequestDeclaration declaration;
                for (int i = 0; i < getSelectedDeclarations().length; i++) {

                    declaration = new WithdrawalRequestDeclaration();
                    declaration.setTypeCode(getSelectedDeclarations()[i]);
                    declaration.setSubmissionId(getWithdrawalRequest().getSubmissionId());

                    convertedDeclarations.add(declaration);
                } // end for

                if (withdrawalRequest.getIsParticipantCreated()|| isViewOnly()) {
                    Iterator<Declaration> it = withdrawalRequest.getDeclarations()
                            .iterator();
                    while (it.hasNext()) {
                    	Declaration decl = it.next();
                        Iterator<Declaration> its = convertedDeclarations
                                .iterator();
                        boolean isAlreadyPresent = false;
                        while (its.hasNext()) {
                        	Declaration convertedDecl = its.next();
                            if (StringUtils.equals(decl.getTypeCode(), convertedDecl.getTypeCode())) {
                                isAlreadyPresent = true;
                                break;
                            }

                        }
                        if (!isAlreadyPresent) {
                            convertedDeclarations.add(decl);
                        }

                    }
                }
                withdrawalRequest.setDeclarations(convertedDeclarations);

            } // fi
        }

        // Load the recipients
        if (CollectionUtils.isEmpty(getRecipients())) {
            withdrawalRequest.setRecipients(new ArrayList<Recipient>(0));
        } else {
            final Collection<Recipient> convertedRecipients = new ArrayList<Recipient>(
                    getRecipients().size());

            for (WithdrawalRequestRecipientUi withdrawalRequestRecipientUi : getRecipients()) {

                withdrawalRequestRecipientUi.convertToBean();

                convertedRecipients.add(withdrawalRequestRecipientUi
                        .getWithdrawalRequestRecipient());
            } // end for
            withdrawalRequest.setRecipients(convertedRecipients);
        } // fi
    }

    /**
     * this method cleans up the declarations to avoid having duplicates.
     * 
     * @param declarations
     * @return Collection <WithdrawalRequestDeclaration>
     */
    private Collection<Declaration> cleanUpDeclarations(
            Collection<Declaration> declarations) {
        Collection<Declaration> newColl = new ArrayList<Declaration>();
        if (CollectionUtils.isNotEmpty(declarations)) {
            Iterator<Declaration> it = declarations.iterator();
            while (it.hasNext()) {
            	Declaration dec = it.next();
                if (!StringUtils.equals(Declaration.AT_RISK_TRANSACTION_TYPE_CODE,
                        dec.getTypeCode())) {
                    newColl.add(dec);
                }
            }
        }
        return newColl;
    }

    /**
     * Converts the matching fields from this object, to the WithdrawalRequest bean.
     */
    public final void convertToBean() {
        convertToBean(false);
    }

    /**
     * @return the birthDate
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(final String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return the contractId
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(final String contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the createdProfileId
     */
    public String getCreatedProfileId() {
        return createdProfileId;
    }

    /**
     * @param createdProfileId the createdProfileId to set
     */
    public void setCreatedProfileId(final String createdProfileId) {
        this.createdProfileId = createdProfileId;
    }

    /**
     * @return the deathDate
     */
    public String getDeathDate() {
        return deathDate;
    }

    /**
     * @param deathDate the deathDate to set
     */
    public void setDeathDate(final String deathDate) {
        this.deathDate = deathDate;
    }

    /**
     * @return the selectedDeclarations
     */
    public String[] getSelectedDeclarations() {
        return selectedDeclarations;
    }

    /**
     * @param selectedDeclarations the selectedDeclarations to set
     */
    public void setSelectedDeclarations(final String[] selectedDeclarations) {
        this.selectedDeclarations = selectedDeclarations;
    }

    /**
     * @return the disabilityDate
     */
    public String getDisabilityDate() {
        return disabilityDate;
    }

    /**
     * @return the disabilityDate
     */
    public String getDefaultDisabilityDate() {

        // Use existing date if one exists
        if (StringUtils.isNotBlank(getDisabilityDate())) {
            return getDisabilityDate();
        } else if (StringUtils.equals(WithdrawalRequest.EMPLOYEE_STATUS_DISABLED_CODE,
                getWithdrawalRequest().getEmplStatusCode())) {

            // Use employee status date if status is d
            try {
                // Use the employee status effective date - must convert from date
                return BeanUtils.getSimpleProperty(getWithdrawalRequest(),
                        "emplStatusEffectiveDate");

            } catch (IllegalAccessException illegalAccessException) {
                throw new NestableRuntimeException(illegalAccessException);
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new NestableRuntimeException(noSuchMethodException);
            } catch (InvocationTargetException invocationTargetException) {
                throw new NestableRuntimeException(invocationTargetException);
            } // end try/catch
        } else {

            // Else no default
            return null;
        }
    }

    /**
     * @param disabilityDate the disabilityDate to set
     */
    public void setDisabilityDate(final String disabilityDate) {
        this.disabilityDate = disabilityDate;
    }

    /**
     * @return the expirationDate
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(final String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the fees
     */
    public Collection<WithdrawalRequestFeeUi> getFees() {
        return fees;
    }

    /**
     * @param fees the fees to set
     */
    public void setFees(final Collection<WithdrawalRequestFeeUi> fees) {
        this.fees = fees;
    }

    /**
     * @return the finalContributionDate
     */
    public String getFinalContributionDate() {
        return finalContributionDate;
    }

    /**
     * @return the finalContributionDate
     */
    public String getDefaultFinalContributionDateForWmsiPenChecks() {

        // Use most recent prior contribution if existing
        if (StringUtils.isNotBlank(getMostRecentPriorContributionDate())) {
            return getMostRecentPriorContributionDate();
        } else {

            // Use last contribution payroll ending date if existing
            try {
                // Use the contract effective date - must convert from date
                return BeanUtils.getSimpleProperty(getWithdrawalRequest().getParticipantInfo(),
                        "contractEffectiveDate");

            } catch (IllegalAccessException illegalAccessException) {
                throw new NestableRuntimeException(illegalAccessException);
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new NestableRuntimeException(noSuchMethodException);
            } catch (InvocationTargetException invocationTargetException) {
                throw new NestableRuntimeException(invocationTargetException);
            } // end try/catch
        }
    }

    /**
     * @param finalContributionDate the finalContributionDate to set
     */
    public void setFinalContributionDate(final String finalContributionDate) {
        this.finalContributionDate = finalContributionDate;
    }

    /**
     * @return the moneyTypes
     */
    public Collection<WithdrawalRequestMoneyTypeUi> getMoneyTypes() {
        return moneyTypes;
    }

    /**
     * @param moneyTypes the moneyTypes to set
     */
    public void setMoneyTypes(final Collection<WithdrawalRequestMoneyTypeUi> moneyTypes) {
        this.moneyTypes = moneyTypes;
    }

    /**
     * Retrieves the total balance. If one of the total balances is not set, the total returns as
     * null.
     * 
     * @return BigDecimal - The total balance of all money types.
     */
    public BigDecimal getTotalBalance() {

        BigDecimal total = Constants.BIG_DECIMAL_ZERO;

        for (WithdrawalRequestMoneyTypeUi moneyType : getMoneyTypes()) {

            if (moneyType.getWithdrawalRequestMoneyType().getTotalBalance() != null) {
                total = total.add(moneyType.getWithdrawalRequestMoneyType().getTotalBalance());
            } else {
                return null;
            }
        }

        return total;
    }
    // method for getting total balance of EEDEF money type for Hardship withdrawal type
    public Boolean getEEDEFMoneyTypeTotalBalance() {
    
    	 BigDecimal total = Constants.BIG_DECIMAL_ZERO;
    	for (WithdrawalRequestMoneyTypeUi moneyType : getMoneyTypes()) {  		
            
         if(moneyType.getWithdrawalRequestMoneyType().getMoneyTypeId() != null && 
        		 moneyType.getWithdrawalRequestMoneyType().getMoneyTypeId().equals("EEDEF") && 
        		 moneyType.getWithdrawalRequestMoneyType().getTotalBalance() != null
        		 && BigDecimal.ZERO.compareTo(moneyType.getWithdrawalRequestMoneyType().getTotalBalance()) != 0) {      	 
        	     total = total.add(moneyType.getWithdrawalRequestMoneyType().getTotalBalance());                
                 return true;
             }
        	 
         }       
        return false;    	
    }

    /**
     * Retrieves the total available withdrawal amount. If one of the available amounts is not set,
     * the total returns as null.
     * 
     * @return BigDecimal - The total available withdrawal amount.
     */
    public BigDecimal getTotalAvailableWithdrawalAmount() {

        BigDecimal total = Constants.BIG_DECIMAL_ZERO;

        for (WithdrawalRequestMoneyTypeUi moneyType : getMoneyTypes()) {

            if (moneyType.getWithdrawalRequestMoneyType().getAvailableWithdrawalAmount() != null) {
                total = total.add(moneyType.getWithdrawalRequestMoneyType()
                        .getAvailableWithdrawalAmount());
            } else {
                return null;
            }
        }

        return total;
    }

    /**
     * @return the mostRecentPriorContributionDate
     */
    public String getMostRecentPriorContributionDate() {
        return mostRecentPriorContributionDate;
    }

    /**
     * @param mostRecentPriorContributionDate the mostRecentPriorContributionDate to set
     */
    public void setMostRecentPriorContributionDate(final String mostRecentPriorContributionDate) {
        this.mostRecentPriorContributionDate = mostRecentPriorContributionDate;
    }

    /**
     * @return the participantId
     */
    public String getParticipantId() {
        return participantId;
    }

    /**
     * @param participantId the participantId to set
     */
    public void setParticipantId(final String participantId) {
        this.participantId = participantId;
    }

    /**
     * @return the participantLeavingPlanInd
     */
    public String getParticipantLeavingPlanInd() {
        return participantLeavingPlanInd;
    }

    /**
     * @param participantLeavingPlanInd the participantLeavingPlanInd to set
     */
    public void setParticipantLeavingPlanInd(final String participantLeavingPlanInd) {
        this.participantLeavingPlanInd = participantLeavingPlanInd;
    }

    /**
     * @return the participantUSCitizenInd
     */
    public String getParticipantUSCitizenInd() {
        return participantUSCitizenInd;
    }

    /**
     * @param participantUSCitizenInd the participantUSCitizenInd to set
     */
    public void setParticipantUSCitizenInd(final String participantUSCitizenInd) {
        this.participantUSCitizenInd = participantUSCitizenInd;
    }

    /**
     * @return the employeeProfileId
     */
    public String getEmployeeProfileId() {
        return employeeProfileId;
    }

    /**
     * @param employeeProfileId the employeeProfileId to set
     */
    public void setEmployeeProfileId(final String employeeProfileId) {
        this.employeeProfileId = employeeProfileId;
    }

    /**
     * @return the recipient
     */
    public Collection<WithdrawalRequestRecipientUi> getRecipients() {
        return recipients;
    }

    /**
     * @param recipients the recipient to set
     */
    public void setRecipients(final Collection<WithdrawalRequestRecipientUi> recipients) {
        this.recipients = recipients;
    }

    /**
     * @return the requestDate
     */
    public String getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate the requestDate to set
     */
    public void setRequestDate(final String requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @return the withdrawalAmount
     */
    public String getWithdrawalAmount() {
        return withdrawalAmount;
    }

    /**
     * @param withdrawalAmount the withdrawalAmount to set
     */
    public void setWithdrawalAmount(final String withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    /**
     * @return the withdrawalRequest
     */
    public WithdrawalRequest getWithdrawalRequest() {
        return withdrawalRequest;
    }

    /**
     * @param withdrawalRequest the withdrawalRequest to set
     */
    public void setWithdrawalRequest(final WithdrawalRequest withdrawalRequest) {
        this.withdrawalRequest = withdrawalRequest;
    }

    /**
     * @return the retirementDate
     */
    public String getRetirementDate() {
        return retirementDate;
    }

    /**
     * @return the retirementDate
     */
    public String getDefaultRetirementDate() {
        // Use existing date if one exists
        if (StringUtils.isNotBlank(getRetirementDate())) {
            return getRetirementDate();
        } else if (StringUtils.equals(WithdrawalRequest.EMPLOYEE_STATUS_RETIRED_CODE,
                getWithdrawalRequest().getEmplStatusCode())) {

            // Use employee status date if status is retired
            try {
                // Use the employee status effective date - must convert from date
                return BeanUtils.getSimpleProperty(getWithdrawalRequest(),
                        "emplStatusEffectiveDate");

            } catch (IllegalAccessException illegalAccessException) {
                throw new NestableRuntimeException(illegalAccessException);
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new NestableRuntimeException(noSuchMethodException);
            } catch (InvocationTargetException invocationTargetException) {
                throw new NestableRuntimeException(invocationTargetException);
            } // end try/catch
        } else {

            // Else no default
            return null;
        }
    }

    /**
     * @param retirementDate the retirementDate to set
     */
    public void setRetirementDate(final String retirementDate) {
        this.retirementDate = retirementDate;
    }

    /**
     * @return the terminationDate
     */
    public String getTerminationDate() {
        return terminationDate;
    }

    /**
     * @return the terminationDate
     */
    public String getDefaultTerminationDate() {
        // Use existing date if one exists
        if (StringUtils.isNotBlank(getTerminationDate())) {
            return getTerminationDate();
        } else if (StringUtils.equals(WithdrawalRequest.EMPLOYEE_STATUS_TERMINATION_CODE,
                getWithdrawalRequest().getEmplStatusCode())) {

            try {
                // Use the employee status effective date - must convert from date
                return BeanUtils.getSimpleProperty(getWithdrawalRequest(),
                        "emplStatusEffectiveDate");

            } catch (IllegalAccessException illegalAccessException) {
                throw new NestableRuntimeException(illegalAccessException);
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new NestableRuntimeException(noSuchMethodException);
            } catch (InvocationTargetException invocationTargetException) {
                throw new NestableRuntimeException(invocationTargetException);
            } // end try/catch
        } else {

            // Else no default
            return null;
        }
    }

    /**
     * @return the default reason code
     */
    public String getDefaultReasonCode() {
        // Use existing reason if one exists
        final String reasonCode = getWithdrawalRequest().getReasonCode();
        if (StringUtils.isNotBlank(reasonCode)) {
            return reasonCode;
        }

        // Determine if we should default based on employee status code
        final String employeeStatusCode = getWithdrawalRequest().getEmplStatusCode();
        if (StringUtils.equals(WithdrawalRequest.EMPLOYEE_STATUS_TERMINATION_CODE,
                employeeStatusCode)) {
            return WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE;
        } else if (StringUtils.equals(WithdrawalRequest.EMPLOYEE_STATUS_DISABLED_CODE,
                employeeStatusCode)) {
            return WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE;
        } else if (StringUtils.equals(WithdrawalRequest.EMPLOYEE_STATUS_RETIRED_CODE,
                employeeStatusCode)) {
            return WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE;
        } else {

            // Else no default
            return StringUtils.EMPTY;
        }
    }

    /**
     * This looks up the mapping for vesting based of the withdrawal request reason code.
     * 
     * @return String - The mapped reason code for vesting.
     */
    public String getMappedReasonCodeForVesting() {
        return WithdrawalVestingEngine
                .lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(getWithdrawalRequest()
                        .getReasonCode());
    }

    /**
     * @param terminationDate the terminationDate to set
     */
    public void setTerminationDate(final String terminationDate) {
        this.terminationDate = terminationDate;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {

        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, getWithdrawalRequest()));

        // Check notes
        if (withdrawalRequest.getCurrentAdminToAdminNote() != null) {
            messages.addAll(getValidationMessages(graphLocation, withdrawalRequest
                    .getCurrentAdminToAdminNote()));
        } // fi
        if (withdrawalRequest.getCurrentAdminToParticipantNote() != null) {
            messages.addAll(getValidationMessages(graphLocation, withdrawalRequest
                    .getCurrentAdminToParticipantNote()));
        } // fi

        int i = 0;
        for (WithdrawalRequestMoneyTypeUi withdrawalRequestMoneyTypeUi : getMoneyTypes()) {
            messages.addAll(withdrawalRequestMoneyTypeUi.getValidationMessages(new GraphLocation(
                    graphLocation, "moneyTypes", i)));
            i++;
        } // end for

        i = 0;
        for (WithdrawalRequestFeeUi withdrawalRequestFeeUi : getFees()) {
            messages.addAll(withdrawalRequestFeeUi.getValidationMessages(new GraphLocation(
                    graphLocation, "fees", i)));
            i++;
        } // end for

        i = 0;
        for (WithdrawalRequestRecipientUi withdrawalRequestRecipientUi : getRecipients()) {
            messages.addAll(withdrawalRequestRecipientUi.getValidationMessages(new GraphLocation(
                    graphLocation, "recipients", i)));
            i++;
        } // end for

        return messages;
    }

    /**
     * Performs any default setup steps common to all withdrawal screens.
     * 
     * @param userProfile The user profile to use.
     * @throws SystemException
     */
    public void performDefaultSetup(final UserProfile userProfile) throws SystemException {

        // Any common setup steps should be moved here

        // TODO: This needs work on type conversion.
        final Integer currentUserId = new Integer(String.valueOf(userProfile.getPrincipal()
                .getProfileId()));

        // Load contract information
        ContractInfo contractInfo;
        try {
            contractInfo = WithdrawalServiceDelegate.getInstance().getContractInfo(
                    getWithdrawalRequest().getContractId(),
                    WithdrawalWebUtil.getPrincipalFromUserProfile(userProfile));
        } catch (SystemException systemException) {
            throw new RuntimeException(systemException);
        } // end try/catch

        // If the user is a TPA, and they haven't selected a contract, we have to load the userRole
        // differently for the given contract. Otherwise it's already all been loaded for us.
        final UserRole userRole;
        if (userProfile.getCurrentContract() == null) {
            try {
                userRole = SecurityHelper.getUserRoleForContract(userProfile.getPrincipal(),
                        userProfile.getRole(), contractInfo.getContractId());
            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            } // end try/catch
        } else {
            userRole = userProfile.getRole();
        } // fi

        if (userRole.isTPA()) {
            withdrawalRequest.setUserRoleCode(USER_ROLE_CODE_TPA);
        } // fi
        if (userRole.isPlanSponsor()) {
            withdrawalRequest.setUserRoleCode(USER_ROLE_CODE_PLAN_SPONSOR);
        } // fi
        if (userRole.isInternalUser()) {
            withdrawalRequest.setUserRoleCode(USER_ROLE_CODE_BUNDLED_GA);
        } // fi

        withdrawalRequest.setCmaSiteCode(WithdrawalRequest.CMA_SITE_CODE_PSW);

        // Set user permissions
        WithdrawalRequestUi.populatePermissions(getWithdrawalRequest().getContractInfo(),
                userProfile.getPrincipal());
        withdrawalRequest.setPrincipal(userProfile.getPrincipal());

        setUserInformation(currentUserId, withdrawalRequest);

        for (Recipient withdrawalRequestRecipient : withdrawalRequest
                .getRecipients()) {
            setUserInformation(currentUserId, (WithdrawalRequestRecipient)withdrawalRequestRecipient);
            setUserInformation(currentUserId, (Address)withdrawalRequestRecipient.getAddress());

            if (CollectionUtils.isNotEmpty(withdrawalRequestRecipient.getPayees())) {
                for (Payee withdrawalRequestPayee : withdrawalRequestRecipient
                        .getPayees()) {
                    setUserInformation(currentUserId, (WithdrawalRequestPayee)withdrawalRequestPayee);
                    setUserInformation(currentUserId, (PayeePaymentInstruction) withdrawalRequestPayee
                            .getPaymentInstruction());
                    setUserInformation(currentUserId, (Address)withdrawalRequestPayee.getAddress());
                } // end for
            } // fi
        } // end for

        for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                .getMoneyTypes()) {
            setUserInformation(currentUserId, withdrawalRequestMoneyType);
        } // end for
        for (Fee withdrawalRequestFee : withdrawalRequest.getFees()) {
            setUserInformation(currentUserId, (WithdrawalRequestFee)withdrawalRequestFee);
        } // end for
        for (WithdrawalRequestLoan withdrawalRequestLoan : withdrawalRequest.getLoans()) {
            setUserInformation(currentUserId, withdrawalRequestLoan);
        } // end for
        setUserInformation(currentUserId, withdrawalRequest.getCurrentAdminToAdminNote());
        setUserInformation(currentUserId, withdrawalRequest.getCurrentAdminToParticipantNote());

        setContractHasNonFullyVestedMoneyTypes(WithdrawalWebUtil
                .getContractHasNonFullyVestedMoneyTypes(new Integer(contractId)));
        if(withdrawalRequest.getContractPermission() == null){
			withdrawalRequest.setContractPermission(SecurityServiceDelegate
					.getInstance().getTpaFirmContractPermission(
							Integer.parseInt(contractId))); 
        }
    }

    /**
     * setUserInformation populates the createdById field if it is not already set, and fills in the
     * lastUpdateById field with the current user's ID.
     * 
     * @param currentUserId The ID of the current user.
     * @param baseWithdrawal The withdrawal object to set the data on.
     */
    private void setUserInformation(final Integer currentUserId, final BaseWithdrawal baseWithdrawal) {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("Current user id [").append(currentUserId).append(
                    "] and current created by is [").append(baseWithdrawal.getCreatedById())
                    .append("] and withdrawal type is [").append(
                            baseWithdrawal.getClass().getName()).append("]").toString());
        }

        if (baseWithdrawal != null) {
            if (baseWithdrawal.getCreatedById() == null) {
                baseWithdrawal.setCreatedById(currentUserId);
            } // fi
            baseWithdrawal.setLastUpdatedById(currentUserId);
        } // fi
    }

    /**
     * Performs any default setup steps specific to Step 1.
     * 
     * @param userProfile The user profile to use.
     * @throws SystemException
     */
    public void performStep1DefaultSetup(final UserProfile userProfile) throws SystemException {

        // Perform any common setup steps
        performDefaultSetup(userProfile);

        // Set type of withdrawal
        getWithdrawalRequest().setReasonCode(getDefaultReasonCode());

        // Set default termination date
        setTerminationDate(getDefaultTerminationDate());

        // Set default retirement date
        setRetirementDate(getDefaultRetirementDate());

        // Set default disability date
        setDisabilityDate(getDefaultDisabilityDate());

        // Set default loan option
        withdrawalRequest.setLoanOption(withdrawalRequest.getDefaultLoanOption());
    }

    /**
     * Performs any default setup steps specific to Step 2.
     * 
     * @param userProfile The user profile to use.
     * @throws SystemException
     */
    public void performStep2DefaultSetup(final UserProfile userProfile) throws SystemException {

        // Perform any common setup steps
        performDefaultSetup(userProfile);

        // Validate the number of recipients and payees
        verifyRecipientAndPayeeCount();

        // Check for valid WMSI - Penchecks and withdrawal reason
        verifyWmsiPenChecks();

        // Set default check payee name
        setDefaultCheckPayeeDisplayName();
        
        // Security Enhancements - set default eft payee name
        setDefaultEftPayeeDisplayName();

        // Set the payee type and reason
        setDefaultPayeeTypeAndReason();

        // Set default amount type
        setDefaultAmountType();

        // Set IRS Distribution code
        setDefaultIrsDistributionCode();

        // Set rollover plan name
        setDefaultRolloverPlanName();

        // Set the default federal tax.
        setDefaultFederalTax();
        updateStateTaxOnStateOfResidenceChange();

        // Set the default option for unvested money.
        setDefaultOptionForUnvestedMoney();

        // set the default fees
        setDefaultFees();
    }

    /**
     * Sets a default fee if none in the withdrawalRequest
     * 
     */
    private void setDefaultFees() {
        if (CollectionUtils.isEmpty(withdrawalRequest.getFees())) {
            // Add default fee
            final WithdrawalRequestFee withdrawalRequestFee = new WithdrawalRequestFee();
            getWithdrawalRequest().setFees(new ArrayList<Fee>(1));
            getWithdrawalRequest().getFees().add(withdrawalRequestFee);

            // Add default fee ui
            setFees(new ArrayList<WithdrawalRequestFeeUi>(1));
            getFees().add(new WithdrawalRequestFeeUi(withdrawalRequestFee));
        } else {
            setFees(new ArrayList<WithdrawalRequestFeeUi>(withdrawalRequest.getFees().size()));
            for (Fee withdrawalRequestFee : withdrawalRequest.getFees()) {
                getFees().add(new WithdrawalRequestFeeUi((WithdrawalRequestFee)withdrawalRequestFee));
            }
        }
    }

    /**
     * Performs any default setup steps specific to Review.
     * 
     * @param userProfile The user profile to use.
     * @throws SystemException
     */
    public void performReviewDefaultSetup(final UserProfile userProfile) throws SystemException {

        // Perform common default set up
        performDefaultSetup(userProfile);

        // Set default termination date
        setTerminationDate(getDefaultTerminationDate());

        // Set default retirement date
        setRetirementDate(getDefaultRetirementDate());

        // Set default disability date
        setDisabilityDate(getDefaultDisabilityDate());

        // set the default fees
        setDefaultFees();

        // Set the default option for unvested money.
        setDefaultOptionForUnvestedMoney();

        // Set default check payee name
        setDefaultCheckPayeeDisplayName();

        // Keep track of original step 1 fields for warning
        getWithdrawalRequest().updateOriginalStep1DriverFields();
    }

    /**
     * Sets the default Option For Unvested Money.
     */
    protected void setDefaultOptionForUnvestedMoney() {

        if (StringUtils.isBlank(withdrawalRequest.getUnvestedAmountOptionCode())) {
            withdrawalRequest.setUnvestedAmountOptionCode(withdrawalRequest.getContractInfo()
                    .getEffectiveDefaultUnvestedMoneyOptionCode());

        }
    }

    /**
     * Sets the default state tax values.
     */
    private void setDefaultStateTax() {
        for (WithdrawalRequestRecipientUi withdrawalRequestRecipientUi : this.getRecipients()) {
            withdrawalRequestRecipientUi.setDefaultStateTax();
        } // end for
    }

    /**
     * Sets the default FederalTax values.
     */
    private void setDefaultFederalTax() {
        final FederalTaxVO federalTaxVo = withdrawalRequest.getFederalTaxVo();

        if (federalTaxVo == null) {
            throw new RuntimeException("FederalTaxVO was null.");
        } // fi
        
        for (WithdrawalRequestRecipientUi withdrawalRequestRecipientUi : this.getRecipients()) {
            if(withdrawalRequestRecipientUi.getWithdrawalRequestRecipient().getFederalTaxPercent()!=null){
            	withdrawalRequest.getRecipients().iterator().next().setFederalTaxPercent
            	(withdrawalRequestRecipientUi.getWithdrawalRequestRecipient().getFederalTaxPercent());
            	return;
            }
        }
        

        // Defaults to the value object's percentage.
        withdrawalRequest.getRecipients().iterator().next().setFederalTaxPercent(
                federalTaxVo.getTaxPercentage());
    }

    /**
     * Sets the payee type and payee reason to default values.
     */
    private void setDefaultPayeeTypeAndReason() {
        // TODO: Find the values that these should be set to.
        for (WithdrawalRequestRecipientUi recipient : getRecipients()) {
            final Collection<WithdrawalRequestPayeeUi> payees = recipient.getPayees();
            int i = 0;
            for (WithdrawalRequestPayeeUi payee : payees) {
                // Set all payee types to participant.
                payee.getWithdrawalRequestPayee().setTypeCode(
                        WithdrawalRequestPayee.TYPE_CODE_PARTICIPANT);
                // Set the reason for the payees.
                if (i == 0) {
                    payee.getWithdrawalRequestPayee().setReasonCode(
                            WithdrawalRequestPayee.REASON_CODE_PAYMENT);
                } else { // if (i == 1) {
                    payee.getWithdrawalRequestPayee().setReasonCode(
                            WithdrawalRequestPayee.REASON_CODE_ROLLOVER);
                } // fi
            } // end for
        } // end for
    }

    /**
     * Verifies that the number of recipients and payees is correct based on the payment to field.
     */
    private void verifyRecipientAndPayeeCount() {

        // Verify that we have only one recipient
        if (!WithdrawalRequest.RECIPIENT_RANGE.containsInteger(getRecipients().size())) {
            throw new NestableRuntimeException(new StringBuffer(
                    "WithdrawalRequest for participant [").append(getParticipantId()).append(
                    "] has [").append(getRecipients().size()).append("] recipients.").toString());
        }

        final String paymentTo = getWithdrawalRequest().getPaymentTo();
        for (WithdrawalRequestRecipientUi recipient : getRecipients()) {
            if (!WithdrawalRequestRecipient.PAYEE_RANGE.containsInteger(recipient.getPayees()
                    .size())) {
                throw new NestableRuntimeException(new StringBuffer(
                        "WithdrawalRequest for participant [").append(getParticipantId()).append(
                        "] has [").append(recipient.getPayees().size()).append("] payees.")
                        .toString());
            }

            // Single payee - corresponds to direct to participant, rollover to... and plan
            // trustee
            if (recipient.getPayees().size() == 1) {
                if (StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                        || StringUtils
                                .equals(
                                        paymentTo,
                                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {
                    throw new NestableRuntimeException(new StringBuffer("WithdrawalRequest [")
                            .append(this).append("] has 1 payees with payment to [").append(
                                    paymentTo).append("].").toString());
                }
            } else {
                if (StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)
                        || StringUtils.equals(paymentTo,
                                WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                        || StringUtils.equals(paymentTo,
                                WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)
                        || StringUtils.equals(paymentTo,
                                WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
                    throw new NestableRuntimeException(new StringBuffer("WithdrawalRequest [")
                            .append(this).append("] has 2 payees with payment to [").append(
                                    paymentTo).append("].").toString());

                }
            }
        }
    }

    /**
     * Sets the default value for the payee check name display field if the field is display only.
     */
    protected void setDefaultCheckPayeeDisplayName() {

        // Set payment to field for payees
        final String paymentTo = getWithdrawalRequest().getPaymentTo();
        final WithdrawalRequestRecipientUi withdrawalRequestRecipientUi = getRecipients()
                .iterator().next();
        final Iterator<WithdrawalRequestPayeeUi> iterator = withdrawalRequestRecipientUi
                .getPayees().iterator();

        final WithdrawalRequestPayeeUi firstPayee = iterator.next();

        // Ensure the name fields are set on the payee.
        setParticipantNameIntoPayeeNameFields(withdrawalRequestRecipientUi, firstPayee
                .getWithdrawalRequestPayee());

        if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)) {
            // Set payee to participant name
            firstPayee.setCheckOrganizationName(withdrawalRequestRecipientUi.getParticipantName());
        } else if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            // Set payee to participant name
            firstPayee.setCheckOrganizationName(getWithdrawalRequest().getParticipantInfo()
                    .getTrusteeName());
        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {

            // Check if first payee should be reset if payment to has changed
            if (getWithdrawalRequest().hasPaymentToChanged()) {
                firstPayee.setCheckOrganizationName(StringUtils.EMPTY);
            } // fi

            // Default the display name
            final WithdrawalRequestPayeeUi secondPayee = iterator.next();

            setParticipantNameIntoPayeeNameFields(withdrawalRequestRecipientUi, secondPayee
                    .getWithdrawalRequestPayee());
            secondPayee.setCheckOrganizationName(withdrawalRequestRecipientUi.getParticipantName());
            // Security Enhancements
            secondPayee.setEftOrganizationName(withdrawalRequestRecipientUi.getParticipantName());

        } else {
            // Roll-over - check if the payment to has changed to see if we should reset field
            if (getWithdrawalRequest().hasPaymentToChanged()) {

                // Payment to changed - reset the field
                firstPayee.setCheckOrganizationName(StringUtils.EMPTY);
            }
        }
    }

    /**
     * Updates the payee with the appropritate name information for this participant.
     * 
     * @param withdrawalRequestRecipientUi The data used to set the name.
     * @param withdrawalRequestPayee The object to set the name information into.
     */
    private void setParticipantNameIntoPayeeNameFields(
            final WithdrawalRequestRecipientUi withdrawalRequestRecipientUi,
            final WithdrawalRequestPayee withdrawalRequestPayee) {
        withdrawalRequestPayee.setFirstName(withdrawalRequestRecipientUi
                .getWithdrawalRequestRecipient().getFirstName());
        withdrawalRequestPayee.setLastName(withdrawalRequestRecipientUi
                .getWithdrawalRequestRecipient().getLastName());
    }

    /**
     * Sets the default value for the payee credit party name display field if the field is display
     * only.
     */
/*    protected void setDefaultCreditPartyDisplayName() {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("setDefaultCreditPartyDisplayName> Entry.").toString());
        }

        // Set payment to field for payees
        final String paymentTo = getWithdrawalRequest().getPaymentTo();
        final Iterator<Payee> iterator = getWithdrawalRequest().getRecipients()
                .iterator().next().getPayees().iterator();
        final Payee payee1 = iterator.next();
        if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)) {
            // Set payee to participant name
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "setDefaultCreditPartyDisplayName> Direct to participant - setting credit party name from [")
                                .append(payee1.getPaymentInstruction().getCreditPartyName())
                                .append("] to participant name [").append(
                                        getWithdrawalRequest().getParticipantName()).append("].")
                                .toString());
            }
            payee1.getPaymentInstruction().setCreditPartyName(
                    getWithdrawalRequest().getParticipantName());
        } else if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            // Set payee to trustee name
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "setDefaultCreditPartyDisplayName> Direct to participant - setting credit party name from [")
                                .append(payee1.getPaymentInstruction().getCreditPartyName())
                                .append("] to trustee name [").append(
                                        getWithdrawalRequest().getParticipantInfo()
                                                .getTrusteeName()).append("].").toString());
            }
            payee1.getPaymentInstruction().setCreditPartyName(
                    getWithdrawalRequest().getParticipantInfo().getTrusteeName());
        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {

            // Check if first payee should be reset if payment to has changed
            if (getWithdrawalRequest().hasPaymentToChanged()) {

                if (logger.isDebugEnabled()) {
                    logger
                            .debug(new StringBuffer(
                                    "setDefaultCreditPartyDisplayName> After tax - setting payee 1 credit party name from [")
                                    .append(payee1.getPaymentInstruction().getCreditPartyName())
                                    .append("] to blank.").toString());
                }
                payee1.getPaymentInstruction().setCreditPartyName(StringUtils.EMPTY);
            }
            // Leave first payee as input - set second payee to participant name
            // Default the display name
            final Payee payee2 = iterator.next();
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "setDefaultCreditPartyDisplayName> After tax - setting payee 2 credit party name from [")
                                .append(payee2.getPaymentInstruction().getCreditPartyName())
                                .append("] to participant name [").append(
                                        getWithdrawalRequest().getParticipantName()).append("].")
                                .toString());
            }
            payee2.getPaymentInstruction().setCreditPartyName(
                    getWithdrawalRequest().getParticipantName());
        } else {
            // Roll-over - check if the payment to has changed to see if we should reset field
            if (getWithdrawalRequest().hasPaymentToChanged()) {

                // Payment to changed - reset the field
                if (logger.isDebugEnabled()) {
                    logger
                            .debug(new StringBuffer(
                                    "setDefaultCreditPartyDisplayName> Rollover - setting credit party name from [")
                                    .append(payee1.getPaymentInstruction().getCreditPartyName())
                                    .append("] to blank.").toString());
                }
                payee1.getPaymentInstruction().setCreditPartyName(StringUtils.EMPTY);
            }
        }
    }
*/
    /**
     * Determines whether the tax withholding section should be displayed or suppressed. Currently
     * the tax withholding section is suppressed if:
     * <ul>
     * <li> Payment To is Plan trustee.
     * <li> Payment To is Rollover to IRA.
     * <li> Payment To is Rollover to plan.
     * <li> Payment To is After-tax contributions direct to participant, remainder to IRA.
     * <li> Payment To is After-tax contributions direct to participant, remainder to plan.
     * <li> Total requested amount is less than $200.00.
     * </ul>
     * 
     * @return the showTaxWitholdingSection
     */
    public boolean getShowTaxWitholdingSection() {
        return withdrawalRequest.getShowTaxWitholdingSection();
    }

    /**
     * Determines whether the available withdrawal amount column should be displayed or suppressed.
     * Currently the available withdrawal amount column is suppressed if:
     * <ul>
     * <li> Withdrawal Reason is Hardship
     * <li> Withdrawal Reason is Voluntary Contribution Money
     * <li> Withdrawal Reason is Employee Rollover Money
     * </ul>
     * 
     * @return boolean - True if the available withdrawal amount column should be shown.
     */
    public boolean getShowAvailableWithdrawalAmountColumn() {
        return !(StringUtils.equals(getWithdrawalRequest().getReasonCode(),
                WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE)
                || StringUtils.equals(getWithdrawalRequest().getReasonCode(),
                        WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE) || StringUtils
                .equals(getWithdrawalRequest().getReasonCode(),
                        WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE));
    }

    /**
     * TODO getShowTotalAvailableWithdrawalAmount Description.
     * 
     * @return boolean - True if the total availalbe withdrawal amount field should be shown.
     */
    public boolean getShowTotalAvailableWithdrawalAmount() {
        if (CollectionUtils.isEmpty(getMoneyTypes())) {
            return false;
        } // fi

        for (WithdrawalRequestMoneyType moneyType : getWithdrawalRequest().getMoneyTypes()) {
            // Check for blank available amount
            if (moneyType.getAvailableWithdrawalAmount() == null) {
                return false;
            } // fi
        } // end for
        return true;
    }

    /**
     * Determines whether the total requested withdrawal amount field should be displayed or
     * suppressed. Currently the total requested withdrawal amount field is suppressed if:
     * <ul>
     * <li> Any available withdrawal amount column is blank.
     * <li> Any requested withdrawal amount column is blank if display only.
     * </ul>
     * 
     * @return boolean - True if the total requested withdrawal amount field should be shown.
     */
    public boolean getShowTotalRequestedWithdrawalAmount() {
        if (CollectionUtils.isEmpty(getMoneyTypes())) {
            return false;
        }

        for (WithdrawalRequestMoneyType moneyType : getWithdrawalRequest().getMoneyTypes()) {
            // Check for blank available amount
            if (moneyType.getAvailableWithdrawalAmount() == null) {
                return false;
            }
            // Check for display only blank requested amount
            if ((moneyType.getWithdrawalAmount() == null)
                    && (!getIsRequestedAmountColumnEditable())) {
                return false;
            }
        }

        // Total should be displayed
        return true;
    }

    /**
     * Determines whether the requested withdrawal amount percent column should be displayed or
     * suppressed. Currently the requested withdrawal amount percent column is suppressed if:
     * <ul>
     * <li> Amount type is specific.
     * </ul>
     * 
     * @return boolean - True if the requested withdrawal amount percent column should be shown.
     */
    public boolean getShowRequestedWithdrawalAmountPercentColumn() {
        return !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE,
                getWithdrawalRequest().getAmountTypeCode());
    }

    /**
     * Determines if the SpecificAmount field is shown or not.
     * 
     * @return boolean - True if the field is shown, false otherwise.
     */
    public boolean getShowSpecificAmount() {
        return StringUtils.equals(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE,
                withdrawalRequest.getAmountTypeCode());
    }

    /**
     * Determines if the Show Option For Unvested Amount field is shown or not.
     * 
     * @return boolean - True if the field is shown, false otherwise.
     */
    public boolean getShowOptionForUnvestedAmount() {
        return getWithdrawalRequest().getShowOptionForUnvestedAmount();
    }

    /**
     * Sets the default value for the amount type field.
     */
    protected void setDefaultAmountType() {

        if (StringUtils.isBlank(getWithdrawalRequest().getAmountTypeCode())) {

            if (!getWithdrawalRequest().getWithdrawalReasonSimple()
                    && (Boolean.TRUE.equals(getWithdrawalRequest().getParticipantLeavingPlanInd()))) {

                getWithdrawalRequest().setAmountTypeCode(
                        WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE);
            } else {

                getWithdrawalRequest().setAmountTypeCode(StringUtils.EMPTY);
            }
        }
    }

    /**
     * Retrieves the total requested withdrawal amount. If one of the requested amounts is not set,
     * the total returns as null.
     * 
     * @return BigDecimal - The total requested withdrawal amount.
     */
    public BigDecimal getTotalRequestedWithdrawalAmount() {
        return getWithdrawalRequest().getTotalRequestedWithdrawalAmount();
    }

    /**
     * Filters the specified lookup data based on data in the withdrawal request.
     * 
     * @param lookupDataMap The data map used for lookups (see {@link CodeLookupCache}).
     */
    public void filterStep2LookupData(final Map lookupDataMap) {

        // Filter amount type
        if (lookupDataMap.containsKey(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE)) {

            if (getWithdrawalRequest().getWithdrawalReasonSimple()) {
                lookupDataMap
                        .put(
                                CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                                LookupDataHelper
                                        .filterLookupData(
                                                (Collection) lookupDataMap
                                                        .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                                new String[] { WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE },
                                                LookupDataHelper.REMOVE_INDICATOR));
            } else {
                if (Boolean.TRUE.equals(getWithdrawalRequest().getParticipantLeavingPlanInd())) {
                    lookupDataMap
                            .put(
                                    CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                                    LookupDataHelper
                                            .filterLookupData(
                                                    (Collection) lookupDataMap
                                                            .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                                    new String[] { WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE },
                                                    LookupDataHelper.RETAIN_INDICATOR));
                }
            }
 // hide maximum elective money type except Hardship
            
            if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE,
                    withdrawalRequest.getReasonCode())){
              	 lookupDataMap
                   .put(
                           CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                           LookupDataHelper
                                   .filterLookupData(
                                           (Collection) lookupDataMap
                                                   .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                           new String[] {WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE},
                                           LookupDataHelper.REMOVE_INDICATOR));
              
              }
            // check the money type balance total"EEDEF" Bal and show the  maxium elective money type for hardship withdrawaltype
            if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE,
                    withdrawalRequest.getReasonCode())){
            	  Boolean totalEEDEFBal = true;
            	  totalEEDEFBal = getEEDEFMoneyTypeTotalBalance();                
                  if(totalEEDEFBal) {
                	  lookupDataMap
                      .put(
                              CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                              LookupDataHelper
                                      .filterLookupData(
                                              (Collection) lookupDataMap
                                                      .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                              new String[] {WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE,WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE},
                                              LookupDataHelper.RETAIN_INDICATOR)); 
                  }
                  else {
                	  lookupDataMap
                      .put(
                              CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                              LookupDataHelper
                                      .filterLookupData(
                                              (Collection) lookupDataMap
                                                      .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                              new String[] {WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE},
                                              LookupDataHelper.RETAIN_INDICATOR)); 
                  }
            	
              	 lookupDataMap
                   .put(
                           CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                           LookupDataHelper
                                   .filterLookupData(
                                           (Collection) lookupDataMap
                                                   .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                           new String[] { WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE},
                                           LookupDataHelper.REMOVE_INDICATOR));
              
              }
        }
    }

    /**
     * Filters the specified lookup data based on data in the withdrawal request.
     */
    public void filterReviewLookupData(final Map lookupDataMap) {

        // Filter amount type
        if (lookupDataMap.containsKey(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE)) {

            if (getWithdrawalRequest().getWithdrawalReasonSimple()) {
                lookupDataMap
                        .put(
                                CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                                LookupDataHelper
                                        .filterLookupData(
                                                (Collection) lookupDataMap
                                                        .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                                new String[] { WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE },
                                                LookupDataHelper.REMOVE_INDICATOR));
            } else {
                if (Boolean.TRUE.equals(getWithdrawalRequest().getParticipantLeavingPlanInd())) {
                    lookupDataMap
                            .put(
                                    CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                                    LookupDataHelper
                                            .filterLookupData(
                                                    (Collection) lookupDataMap
                                                            .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                                    new String[] { WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE },
                                                    LookupDataHelper.RETAIN_INDICATOR));
                }
            }
  // hide maximum elective money type except Hardship
            
            if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE,
                    withdrawalRequest.getReasonCode())){
              	 lookupDataMap
                   .put(
                           CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                           LookupDataHelper
                                   .filterLookupData(
                                           (Collection) lookupDataMap
                                                   .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                           new String[] {WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE},
                                           LookupDataHelper.REMOVE_INDICATOR));
              
              }
            // check the money type balance total"EEDEF" Bal and show the  maxium elective money type for hardship withdrawaltype
            if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE,
                    withdrawalRequest.getReasonCode())){
            	  Boolean totalEEDEFBal = true;
            	  totalEEDEFBal = getEEDEFMoneyTypeTotalBalance();                              
                  if(totalEEDEFBal) {
                	  lookupDataMap
                      .put(
                              CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                              LookupDataHelper
                                      .filterLookupData(
                                              (Collection) lookupDataMap
                                                      .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                              new String[] {WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE,WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE},
                                              LookupDataHelper.RETAIN_INDICATOR)); 
                  }
                  else {
                	  lookupDataMap
                      .put(
                              CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                              LookupDataHelper
                                      .filterLookupData(
                                              (Collection) lookupDataMap
                                                      .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                              new String[] {WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE},
                                              LookupDataHelper.RETAIN_INDICATOR)); 
                  }
            	
              	 lookupDataMap
                   .put(
                           CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE,
                           LookupDataHelper
                                   .filterLookupData(
                                           (Collection) lookupDataMap
                                                   .get(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE),
                                           new String[] { WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE},
                                           LookupDataHelper.REMOVE_INDICATOR));
              
              }
        }

        // Filter loan option type
        if (lookupDataMap.containsKey(CodeLookupCache.LOAN_OPTION_TYPE)) {

            if (!getWithdrawalRequest().getWithdrawalReasonSimple()) {

                final Collection<String> filterKeys = new ArrayList<String>();
                filterKeys.add(WithdrawalRequest.LOAN_KEEP_OPTION);

                if (BooleanUtils.isTrue(getWithdrawalRequest().getParticipantLeavingPlanInd())) {

                    if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE,
                            getWithdrawalRequest().getPaymentTo())
                            || StringUtils.equals(
                                    WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE,
                                    getWithdrawalRequest().getPaymentTo())
                            || StringUtils
                                    .equals(
                                            WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                                            getWithdrawalRequest().getPaymentTo())
                            || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE,
                                    getWithdrawalRequest().getPaymentTo())) {
                        filterKeys.add(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
                    }

                } else {
                    filterKeys.add(WithdrawalRequest.LOAN_ROLLOVER_OPTION);
                }

                lookupDataMap.put(CodeLookupCache.LOAN_OPTION_TYPE, LookupDataHelper
                        .filterLookupData((Collection) lookupDataMap
                                .get(CodeLookupCache.LOAN_OPTION_TYPE), filterKeys
                                .toArray(new String[0]), LookupDataHelper.REMOVE_INDICATOR));
            }
        }
    }

    /**
     * Queries if the Final Contribution Date field should be shown or suppressed.
     * 
     * @return boolean - True if the field should be shown.
     */
    public boolean getShowFinalContributionDate() {

        // Show field if participant is leaving plan
        if (BooleanUtils.isTrue(withdrawalRequest.getParticipantLeavingPlanInd())) {
            return true;
        }

        // Otherwise show based on reason
        if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE,
                withdrawalRequest.getReasonCode())

                || StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE,
                        withdrawalRequest.getReasonCode())

                || StringUtils.equals(
                        WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE,
                        withdrawalRequest.getReasonCode())

                || StringUtils.equals(
                        WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE,
                        withdrawalRequest.getReasonCode())) {

            return true;
        }

        return false;
    }

    /**
     * Determines if the loan section should be shown or not.
     * 
     * @return boolean True if the loan section should be shown, false otherwise.
     */
    public boolean getShowLoanSection() {
        return CollectionUtils.isNotEmpty(getWithdrawalRequest().getLoans());
    }

    /**
     * Queries if the Loan IRS Distribution Code field should be shown or suppressed.
     * 
     * @return boolean - True if the field should be shown.
     */
    public boolean getShowLoanIrsDistributionCode() {
        return !StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE,
                withdrawalRequest.getPaymentTo());
    }

    /**
     * Checks to see if Irs Loan Closure Distribution label needs to be shown or suppressed.
     * 
     * @return boolean - True if Irs Loan Closure Distribution label should be shown.
     */
    public boolean getShowIrsLoanDistribution() {
        return !((WithdrawalRequest.LOAN_KEEP_OPTION.equals(withdrawalRequest.getLoanOption())));
    }

    // View Notes / Declarations sections
    /**
     * Read Only form property: Checks if the withdrawal request has a tax notice declaration.
     * 
     * @return true if the current WithdrawalRequest has a tax notice declaration.
     */
    public boolean getHasTaxNoticeDeclaration() {
        return hasDeclaration(WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE);
    }

    /**
     * Read Only form property: Checks if the withdrawal request has a waiting period wave
     * declaration.
     * 
     * @return true if the current WithdrawalRequest has a waiting period wave declaration.
     */
    public boolean getHasWaitPeriodWaveDeclaration() {
        return hasDeclaration(WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE);
    }

    /**
     * Read Only form property: Checks if the withdrawal request has an IRA provider account setup.
     * 
     * @return true if the current WithdrawalRequest has a an IRA provider account setup.
     */
    public boolean getHasIraProviderDeclaration() {
        return hasDeclaration(WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE);
    }

    /**
     * Checks if a declaration of a given type has been selected.
     * 
     * @param typeCode The declaration type code.
     * @return boolean - True if the declartation of the given type is checked.
     */
    protected boolean hasDeclaration(final String typeCode) {
        for (Declaration item : withdrawalRequest.getDeclarations()) {
            if (item.getTypeCode().equals(typeCode)) {
                return true;
            } // fi
        } // end for
        return false;
    }

    /**
     * Checks if the request state is draft
     */

    public boolean getIsRequestInDraft() {
        return WithdrawalStateEnum.DRAFT.getStatusCode().equals(withdrawalRequest.getStatusCode());
    }

    /**
     * Read Only form property: Checks if the withdrawal request has been approved.
     * 
     * @return true if the current WithdrawalRequest has been approved.
     */
    public boolean getIsRequestApproved() {
        return WithdrawalStateEnum.APPROVED.getStatusCode().equals(
                withdrawalRequest.getStatusCode())
                || WithdrawalStateEnum.READY_FOR_ENTRY.getStatusCode().equals(
                        withdrawalRequest.getStatusCode());
    }

    /**
     * Read Only form property: Checks if the withdrawal request has been send for Review.
     * 
     * @return true if the current WithdrawalRequest has been send for review
     */
    public boolean getIsRequestSendForReview() {
        return WithdrawalStateEnum.PENDING_REVIEW.getStatusCode().equals(
                withdrawalRequest.getStatusCode());
    }

    /**
     * Read Only form property: Checks if the withdrawal request has been send for approval
     * 
     * @return true if the current WithdrawalRequest has been send for approval.
     */
    public boolean getIsRequestSendForApprove() { 
        return WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode().equals(
                withdrawalRequest.getStatusCode());
    }

    /**
     * Checks to see if the hardship reason and hardship description needs to be displayed.
     * 
     * @return boolean - True if reason code is hardship.
     */
    public boolean getShowHardshipReasonAndHardshipDescription() {
        return (WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE
                .equals(withdrawalRequest.getReasonCode()));
    }

    /**
     * Determines if the WmsiPenchecks (IRA service provider) is shown or suppressed.
     * 
     * @return boolean - True if shown, otherwise suppressed.
     */
    public boolean getShowWmsiPenchecks() {
        return (WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE
                .equals(withdrawalRequest.getReasonCode()));
    }

    /**
     * Determines if the DisabiltiyDate is shown or suppressed.
     * 
     * @return boolean - True if shown, otherwise suppressed.
     */
    public boolean getShowDisabilityDate() {
        return getWithdrawalRequest().getShowDisabilityDate();
    }

    /**
     * getShowRetirementDate determines if the element should be shown or not.
     * 
     * @return boolean - true if the element should be shown, false otherwise.
     */
    public boolean getShowRetirementDate() {
        return getWithdrawalRequest().getShowRetirementDate();
    }

    /**
     * getShowTerminationDate determines if the element should be shown or not.
     * 
     * @return boolean - true if the element should be shown, false otherwise.
     */
    public boolean getShowTerminationDate() {
        return getWithdrawalRequest().getShowTerminationDate();
    }

    /**
     * Returns true if the Withdrawal Type is Simple.
     * 
     * @param code The withdrawal type to check.
     * @return boolean - True if the withdrawal type is simple, false otherwise.
     */
    protected boolean isWithdrawalTypeSimple(final String code) {
        boolean isSimple = true;
        if (WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE.equals(code)
                || WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE.equals(code)
                || WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE
                        .equals(code)) {
            isSimple = false;
        }
        return isSimple;

    }

    /**
     * Determines whether the requested amount column should be editable or not. Currently the
     * requested amount column is editable if:
     * <ul>
     * <li> Amount Type is Specific Amount.
     * </ul>
     * 
     * @return the requestedAmountColumnEditable
     */
    public boolean getIsRequestedAmountColumnEditable() {

        return StringUtils.equals(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE,
                getWithdrawalRequest().getAmountTypeCode());
    }

    /**
     * Determines whether the requested amount percentage column should be editable or not.
     * Currently the requested amount column percentage is editable if:
     * <ul>
     * <li> Amount Type is Percent by MoneyType.
     * </ul>
     * 
     * @return the requestedAmountPercentageColumnEditable
     */
    public boolean getIsRequestedAmountPercentageColumnEditable() {

        return StringUtils.equals(WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE,
                getWithdrawalRequest().getAmountTypeCode());
    }

    /**
     * returns true if TPA firm exists for the contract.
     * 
     * @return boolean - true if there exists a TPA, false otherwise.
     */

    public boolean getIsTpaFirmExistForContract() {
        return withdrawalRequest.getParticipantInfo().getThirdPartyAdminId().booleanValue();
    }

    /**
     * R/O property: returns the Federal Tax % for the first recipient.
     * 
     * @return {@link BigDecimal} The federal tax rate chosen.
     */
    public BigDecimal getFederalTaxPercent() {
        WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient) withdrawalRequest
                .getRecipients().toArray()[0];
        return (recipient != null) ? recipient.getFederalTaxPercent() : null;
    }

    /**
     * R/O property: returns the State Tax % for the first recipient.
     * 
     * @return {@link BigDecimal} The state tax rate chosen.
     */
    public BigDecimal getStateTaxPercent() {
        WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient) withdrawalRequest
                .getRecipients().toArray()[0];
        return (recipient != null) ? recipient.getStateTaxPercent() : null;
    }

    /**
     * R/O property: returns the State Tax Type for the first recipient.
     * 
     * @return {@link String} The state tax type chosen.
     */
    public String getStateTaxType() {
        WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient) withdrawalRequest
                .getRecipients().toArray()[0];
        return (recipient != null) ? recipient.getStateTaxTypeCode() : null;
    }

    /**
     * Read only property: check if the WD request is in a "Pending..." state.
     * 
     * @return boolean - True if the request is in a pending state, false otherwise.
     */
    public boolean getIsRequestPending() {
        final String statusCode = withdrawalRequest.getStatusCode();
        final boolean result = ((StringUtils.equals(statusCode, WithdrawalStateEnum.PENDING_REVIEW
                .getStatusCode())) || (StringUtils.equals(statusCode,
                WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode())));
        return result;
    }

    /**
     * Check if the request has both PBA money and loans.
     * 
     * @return boolean - True if the participant has both PBA and Loans.
     */
    public boolean getHasBothPbaAndLoans() {
        return (BooleanUtils.toBooleanDefaultIfNull(getWithdrawalRequest().getParticipantInfo()
                .getParticipantHasPbaMoney(), false) && CollectionUtils
                .isNotEmpty(getWithdrawalRequest().getLoans()));
    }

    /**
     * Check if the request has either PBA money or loans.
     * 
     * @return boolean - True if the participant has PBA or Loans.
     */
    public boolean getHasPbaOrLoans() {
        return (BooleanUtils.toBooleanDefaultIfNull(getWithdrawalRequest().getParticipantInfo()
                .getParticipantHasPbaMoney(), false) || CollectionUtils
                .isNotEmpty(getWithdrawalRequest().getLoans()));
    }

    /**
     * Check if the request has PBA money only with no loans.
     * 
     * @return boolean - True if the participant has only PBA.
     */
    public boolean getHasPbaOnly() {
        return (BooleanUtils.toBooleanDefaultIfNull(getWithdrawalRequest().getParticipantInfo()
                .getParticipantHasPbaMoney(), false) && CollectionUtils
                .isEmpty(getWithdrawalRequest().getLoans()));
    }

    /**
     * Check if the request has Loans only with no PBA.
     * 
     * @return boolean - True if the participant has only Loans.
     */
    public boolean getHasLoansOnly() {
        return (!BooleanUtils.toBooleanDefaultIfNull(getWithdrawalRequest().getParticipantInfo()
                .getParticipantHasPbaMoney(), false) && CollectionUtils
                .isNotEmpty(getWithdrawalRequest().getLoans()));
    }

    /**
     * Check if the participant has any pre-1987 money types.
     * 
     * @return boolean - True if the participant has Pre 1987 Money Types.
     */
    public boolean getHasPre1987MoneyTypes() {

        boolean result = false;
        if (CollectionUtils.isNotEmpty(getMoneyTypes())) {
            for (WithdrawalRequestMoneyType moneyType : getWithdrawalRequest().getMoneyTypes()) {
                if (BooleanUtils.toBooleanDefaultIfNull(moneyType.getIsPre1987MoneyType(), false)) {
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * Check if the participant has partial status.
     * 
     * @return boolean - True if the participant has partial status.
     */
    public boolean getParticipantHasPartialStatus() {
        return ArrayUtils.contains(WithdrawalRequest.PARTIAL_PARTICIPANT_STATUS_CODES,
                getWithdrawalRequest().getParticipantInfo().getParticipantStatusCode());
    }

    /**
     * Check if the participant is considered fully vested based off of their status.
     * 
     * @return boolean - True if the participant is considered fully vested.
     */
    public boolean getParticipantIsConsideredFullyVested() {
        return ArrayUtils.contains(WithdrawalRequest.FULLY_VESTED_PARTICIPANT_STATUS_CODES,
                getWithdrawalRequest().getParticipantInfo().getParticipantStatusCode());
    }

    /**
     * Used to show/hide the Activity History link.
     * 
     * See View WD (WVI-31).
     * 
     * @return boolean - True if the activity history should be shown.
     */
    public boolean getShowActivityHistory() {
        // FIXME: use a parameter to set the link expiry interval
        // TODO: What are the units for the expiry interval? Months, days, etc?
        int linkExpiresAfter = LINK_EXPIRY_DURATION_IN_MONTHS; // months

        if (WithdrawalStateEnum.READY_FOR_ENTRY.getStatusCode().equals(
                getWithdrawalRequest().getStatusCode())
                || WithdrawalStateEnum.DENIED.getStatusCode().equals(
                        getWithdrawalRequest().getStatusCode())
                || WithdrawalStateEnum.DELETED.getStatusCode().equals(
                        getWithdrawalRequest().getStatusCode())
                || WithdrawalStateEnum.EXPIRED.getStatusCode().equals(
                        getWithdrawalRequest().getStatusCode())
                || WithdrawalStateEnum.APPROVED.getStatusCode().equals(
                        getWithdrawalRequest().getStatusCode())) {
            Calendar linkExpiresOn = Calendar.getInstance();
            linkExpiresOn.setTime(getWithdrawalRequest().getLastUpdated());
            linkExpiresOn.add(Calendar.MONTH, linkExpiresAfter); // add expiryPeriod months

            Calendar now = Calendar.getInstance();

            if (now.after(linkExpiresOn)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Used to show/hide the Expected Processing date field.
     * 
     * See View WD (WVI-15).
     * 
     * @return boolean - True if the expected processing date should be shown.
     */
    public boolean getShowExpectedProcessingDate() {
        if (WithdrawalStateEnum.READY_FOR_ENTRY.getStatusCode().equals(
                getWithdrawalRequest().getStatusCode())
                || WithdrawalStateEnum.APPROVED.getStatusCode().equals(
                        getWithdrawalRequest().getStatusCode())) {
            return true;
        } // fi
        return false;
    }

    /**
     * Determines if the Delete button is shown or supressed.
     * 
     * @return boolean - True if the button is shown, false otherwise.
     */
    public boolean getShowDeleteButton() {

        // Suppress if the request is participant initiated and the state tax chosen is invalid
        if (getWithdrawalRequest().getIsParticipantCreated()
                && getWithdrawalRequest().getIsNoLongerValid()) {
            return false;
        }

        if (ObjectUtils.equals(new Integer((int) getWithdrawalRequest().getPrincipal()
                .getProfileId()), getWithdrawalRequest().getCreatedById())) {
            // We are the initiator.

            // If 'new' status, don't show the delete button.
            if (getWithdrawalRequest().getSubmissionId() == null) {
                logger
                        .debug("getShowDeleteButton> Suppressing delete button because request is new.");
                return false;
            } // fi

            // Only if the request matches one of the show delete status codes, we show it.
            return SHOW_DELETE_STATUS_CODES.contains(getWithdrawalRequest().getStatusCode());

        } else {
            // The user is not the initiator. Never show the delete button in this case.
            logger
                    .debug("getShowDeleteButton> Suppressing delete button because user is not initiator.");
            return false;
        } // fi
    }

    /**
     * Determines if the Send for Approval button is shown or supressed.
     * 
     * @return boolean - True if the button is shown, false otherwise.
     */
    public boolean getShowSendForApprovalButton() {

        // Suppress if the request is no longer valid
        if (getWithdrawalRequest().getIsNoLongerValid()) {
            logger
                    .debug("getShowSendForApprovalButton> Suppressing send for approval button because request is not longer valid.");
            return false;
        }

        // Suppress if user does not have either initiate or review permission
        if (BooleanUtils.isNotTrue(getWithdrawalRequest().getContractInfo()
                .getHasInitiatePermission())
                && BooleanUtils.isNotTrue(getWithdrawalRequest().getContractInfo()
                        .getHasReviewPermission())) {
            logger
                    .debug("getShowSendForApprovalButton> Suppressing send for approval button because user does not have initiate permission or review permission.");
            return false;
        }

        // Suppress if an existing request exists
        if (BooleanUtils.isTrue(getWithdrawalRequest().getParticipantInfo()
                .getHasExistingWithdrawalRequest())) {
            logger
                    .debug("getShowSendForApprovalButton> Suppressing send for approval button because there is an existing request.");
            return false;
        }

        // Suppress if participant has PBA or Roth money
        if (BooleanUtils.isTrue(getWithdrawalRequest().getParticipantInfo()
                .getParticipantHasPbaMoney())) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getShowSendForApprovalButton> Suppressing send for approval button because participant has PBA [")
                                .append(
                                        getWithdrawalRequest().getParticipantInfo()
                                                .getParticipantHasPbaMoney()).append("]").toString());
            }
            return false;
        }

        // Suppress if participant status is total
        if (getWithdrawalRequest().getParticipantInfo().isParticipantStatusTotal()) {
            logger
                    .debug("getShowSendForApprovalButton> Suppressing send for approval button because participant status is total.");
            return false;
        }

        // Suppress if not new, draft or pending review
        final String statusCode = getWithdrawalRequest().getStatusCode();
        if (StringUtils.isEmpty(statusCode)
                || StringUtils.equals(statusCode, WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE)
                || StringUtils.equals(statusCode,
                        WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE)) {

            // Suppress if two step approval and initiate permission (and not review permission)
            if (BooleanUtils.isTrue(getWithdrawalRequest().getContractInfo()
                    .getTwoStepApprovalRequired())
                    && BooleanUtils.isTrue(getWithdrawalRequest().getContractInfo()
                            .getHasInitiatePermission())
                    && BooleanUtils.isNotTrue(getWithdrawalRequest().getContractInfo()
                            .getHasReviewPermission())) {

                logger
                        .debug("getShowSendForApprovalButton> Suppressing send for approval button because contract is two step approval and user has initiate but not review permission.");
                return false;

            } else {
                return true;
            }
        } else {

            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getShowSendForApprovalButton> Suppressing send for approval button because request status [")
                                .append(statusCode).append("] is not permitted.").toString());
            }
            return false;
        }
    }

    public boolean getShowReasonIsFullyVestedFromPlanSpecialMessage() {

        if (getParticipantIsConsideredFullyVested()) {
            return false;
        }

        // These are the fully vested reasons that are selected on the plan page.
        final Collection<String> fullyVestedWithdrawalReasons = getWithdrawalRequest()
                .getContractInfo().getFullyVestedWithdrawalReasons();

        if (fullyVestedWithdrawalReasons.contains(getWithdrawalRequest().getReasonCode())) {
            return true;
        } // fi
        return false;
    }

    public boolean getShowVestingProvidedSpecialMessage() {

        if (getParticipantIsConsideredFullyVested() || getShowReasonIsFullyVestedFromPlanSpecialMessage()) {
            return false;
        } // fi

        // The latest vesting effective date is set, only if we have an editable value.
        if (getWithdrawalRequest().getLatestVestingEffectiveDate() != null) {
            return true;
        } // fi
        
        return false;

    }

    /**
     * Determines if the Send for Review button is shown or supressed.
     * 
     * @return boolean - True if the button is shown, false otherwise.
     */
    public boolean getShowSendForReviewButton() {

        // Suppress if the request is participant initiated and the state tax chosen is invalid
        if (getWithdrawalRequest().getIsParticipantCreated()
                && getWithdrawalRequest().getIsNoLongerValid()) {
            return false;
        }

        // Suppress if the request is no longer valid
        if (getWithdrawalRequest().getIsNoLongerValid()) {
            logger
                    .debug("getShowSendForReviewButton> Suppressing send for review button because request is no longer valid.");
            return false;
        }

        // Suppress if user does not have initiate permission
        if (BooleanUtils.isNotTrue(getWithdrawalRequest().getContractInfo()
                .getHasInitiatePermission())) {
            logger
                    .debug("getShowSendForReviewButton> Suppressing send for review button because user does not have initiate permission.");
            return false;
        }

        // Suppress if an existing request exists
        if (BooleanUtils.isTrue(getWithdrawalRequest().getParticipantInfo()
                .getHasExistingWithdrawalRequest())) {
            logger
                    .debug("getShowSendForReviewButton> Suppressing send for review button because participant has existing request.");
            return false;
        }

        // Suppress if participant has PBA or Roth money
        if (BooleanUtils.isTrue(getWithdrawalRequest().getParticipantInfo()
                .getParticipantHasPbaMoney())) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getShowSendForReviewButton> Suppressing send for review button because participant has PBA [")
                                .append(
                                        getWithdrawalRequest().getParticipantInfo()
                                                .getParticipantHasPbaMoney()).append("] ") .toString());
            }
            return false;
        }

        // Suppress if participant status is total
        if (getWithdrawalRequest().getParticipantInfo().isParticipantStatusTotal()) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug("getShowSendForReviewButton> Suppressing send for review button because participant status is total.");
            }
            return false;
        }

        // Suppress if not new or draft
        final String statusCode = getWithdrawalRequest().getStatusCode();
        if (StringUtils.isEmpty(statusCode)
                || StringUtils.equals(statusCode, WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE)) {

            // Suppress if one step approval
            if (BooleanUtils.isFalse(getWithdrawalRequest().getContractInfo()
                    .getTwoStepApprovalRequired())) {

                logger
                        .debug("getShowSendForReviewButton> Suppressing send for review button because contract does not have two step approval process.");
                return false;

            } else {
                return true;
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getShowSendForReviewButton> Suppressing send for review button because request status [")
                                .append(statusCode).append("] is not permitted.").toString());
            }

            return false;
        }
    }

    /**
     * Determines if the Approve button is shown or supressed.
     * 
     * @return boolean - True if the button is shown, false otherwise.
     */
    public boolean getShowApproveButton() {

        // Suppress if the request is no longer valid
        if (getWithdrawalRequest().getIsNoLongerValid()) {
            logger
                    .debug("getShowApproveButton> Suppressing approve button because request is no longer valid.");
            return false;
        }

        // Suppress if user does not have approve permission
        if (BooleanUtils.isNotTrue(getWithdrawalRequest().getContractInfo()
                .getHasApprovePermission())) {
            logger
                    .debug("getShowApproveButton> Suppressing approve button because user does not have approve permission.");
            return false;
        }

        // Suppress if an existing request exists
        if (BooleanUtils.isTrue(getWithdrawalRequest().getParticipantInfo()
                .getHasExistingWithdrawalRequest())) {
            logger
                    .debug("getShowApproveButton> Suppressing approve button because participant has existing request.");
            return false;
        }

        final BigDecimal totalBalance = getTotalBalance();
        if ((totalBalance != null) && (BigDecimal.ZERO.equals(totalBalance))) {
            logger
                    .debug("getShowApproveButton> Suppressing approve button because participant has zero money.");
            return false;
        }

        // Suppress if participant has PBA or Roth money
        if (BooleanUtils.isTrue(getWithdrawalRequest().getParticipantInfo()
                .getParticipantHasPbaMoney())) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getShowApproveButton> Suppressing send for review button because participant has PBA [")
                                .append(
                                        getWithdrawalRequest().getParticipantInfo()
                                                .getParticipantHasPbaMoney()).append("]").toString());
            }
            return false;
        }

        // Suppress if participant status is total
        if (getWithdrawalRequest().getParticipantInfo().isParticipantStatusTotal()) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug("getShowApproveButton> Suppressing approve button because participant status is total.");
            }
            return false;
        }

        // Suppress if not new, draft, pending review or pending approval
        final String statusCode = getWithdrawalRequest().getStatusCode();
        if (StringUtils.isEmpty(statusCode)
                || StringUtils.equals(statusCode, WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE)
                || StringUtils.equals(statusCode,
                        WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE)
                || StringUtils.equals(statusCode,
                        WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE)) {

            // Suppress if initiator may not approve and user is initiator
            if (BooleanUtils.isFalse(getWithdrawalRequest().getContractInfo()
                    .getInitiatorMayApprove())
                    && (ObjectUtils.equals(getWithdrawalRequest().getCreatedById(), new Integer(
                            (int) getWithdrawalRequest().getPrincipal().getProfileId())))) {
                logger
                        .debug("getShowApproveButton> Suppressing approve button because user may not approve a request they initiated.");
                return false;

            } else {
                return true;
            }
        } else {

            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "getShowApproveButton> Suppressing approve button because request status [")
                                .append(statusCode).append("] is not permitted.").toString());
            }
            return false;
        }
    }

    // To remove this method - moved to Withdrawal.java

    public Integer getLegaleseContentId(Boolean spousalConsent) {
        Integer contentId = null;
        if (WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE
                .equals(getWithdrawalRequest().getReasonCode())) {
            contentId = new Integer(ContentConstants.LEGALESE_DYNAMIC_MANDATORY_TERMINATION_TEXT);
        } else {
            if (BooleanUtils.isTrue(spousalConsent)) {
                contentId = new Integer(
                        ContentConstants.LEGALESE_DYNAMIC_SPOUSAL_CONSENT_REQUIRED_TEXT);
            } else if (BooleanUtils.isFalse(spousalConsent)) {
                contentId = new Integer(ContentConstants.LEGALESE_DYNAMIC_NO_SPOUSAL_CONSENT_TEXT);
            } else if (spousalConsent == null) {
                contentId = new Integer(
                        ContentConstants.LEGALESE_DYNAMIC_SPOUSAL_CONSENT_BLANK_TEXT);
            }

        }
        return contentId;
    }

    public Collection<ValidationError> getValidationMessagesStepOne(
            final GraphLocation graphLocation) {

        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, getWithdrawalRequest()));

        return messages;
    }

    /**
     * Verifies that WMSI PenChecks are only present with the appropriate withdrawal reason.
     */
    protected void verifyWmsiPenChecks() {

        if (!StringUtils.equals(
                WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE,
                getWithdrawalRequest().getReasonCode())) {
            getWithdrawalRequest().setIraServiceProviderCode(null);
        }
    }

    /**
     * Queries the specified role and populates the withdrawal related contract information
     * permissions.
     * 
     * @param contractInfo The contract information object to populate.
     * @param principal The principal.
     * @throws SystemException If a system exception occurs.
     */
    public static void populatePermissions(final ContractInfo contractInfo,
            final Principal principal) throws SystemException {

        UserRole userRole = principal.getRole();

        // Add contract specific privileges
        userRole = SecurityHelper.getUserRoleForContract(principal, userRole, contractInfo.getContractId());

        populatePermissions(contractInfo, userRole);

    }

    /**
     * Queries the specified role and populates the withdrawal related contract information
     * permissions.
     * 
     * @param contractInfo The contract information object to populate.
     * @param userRole The user's role.
     * @param principal The principal.
     * @throws SystemException If a system exception occurs.
     */
    public static void populatePermissions(final ContractInfo contractInfo,
            final UserRole userRole) throws SystemException {

        contractInfo.setHasInitiatePermission(userRole
                .hasPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE));
        contractInfo.setHasReviewPermission(userRole
                .hasPermission(PermissionType.REVIEW_WITHDRAWALS));
        // Loans Project: Changing APPROVE_WITHDRAWALS permission to SIGNING AUTHORITY
        contractInfo.setHasApprovePermission(userRole.hasPermission(PermissionType.SIGNING_AUTHORITY));

        contractInfo.setHasViewAllPermission(userRole
                .hasPermission(PermissionType.VIEW_ALL_WITHDRAWALS));
        contractInfo.setHasSelectedAccessPermission(userRole
                .hasPermission(PermissionType.SELECTED_ACCESS));
        
        //for loans: 
        contractInfo.setHasIntitiateLoanPermission(userRole
                .hasPermission(PermissionType.INITIATE_LOANS));
        contractInfo.setHasReviewLoanPermission(userRole
                .hasPermission(PermissionType.REVIEW_LOANS));       
        contractInfo.setHasViewAllLoansPermission(userRole
                .hasPermission(PermissionType.VIEW_ALL_LOANS));
        

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("populatePermissions> Initialized contract information ")
                    .append("with permissions for userRole [").append(userRole).append("] to [")
                    .append(contractInfo).append("]").toString());
        }
    }

    /**
     * Re-converts some fields from the request object if they have been overridden.
     */
    public void updateForcedData() {

        // Currently termination date and retirement date can be overridden
        try {
            BeanUtils.copyProperty(this, WithdrawalRequestProperty.TERMINATION_DATE,
                    withdrawalRequest.getTerminationDate());
            BeanUtils.copyProperty(this, WithdrawalRequestProperty.RETIREMENT_DATE,
                    withdrawalRequest.getRetirementDate());

        } catch (IllegalAccessException illegalAccessException) {
            throw new NestableRuntimeException(illegalAccessException);
        } catch (InvocationTargetException invocationTargetException) {
            throw new NestableRuntimeException(invocationTargetException);
        } // end try/catch
    }

    /**
     * This method contains logic to update the state tax details if the state of residence is
     * changed.
     * 
     */
    public void updateStateTaxOnStateOfResidenceChange() {

        // Extract state tax (recipient list can be empty; state tax VO can be null)
        final StateTaxVO stateTax = CollectionUtils.isEmpty(getWithdrawalRequest().getRecipients()) ? null
                : getWithdrawalRequest().getRecipients().iterator().next().getStateTaxVo();
        final StateTaxType stateTaxType = stateTax == null ? StateTaxType.NONE : stateTax
                .getStateTaxType();
        if (logger.isDebugEnabled()) {
            logger
                    .debug(new StringBuffer(
                            "WithdrawalRequestUi.performStep2DefaultSetup> Checking whether to default state of tax with original state of residence [")
                            .append(getWithdrawalRequest().getOriginalParticipantStateOfResidence())
                            .append("], current state of residence [").append(
                                    getWithdrawalRequest().getParticipantStateOfResidence())
                            .append("], original state tax type [").append(
                                    getWithdrawalRequest().getOriginalStateTaxType()).append(
                                    "], and state tax type [").append(stateTaxType).append("].")
                            .toString());
        }
        // Set the default state tax if the state of residence changed or state tax type has changed
        if (!StringUtils.equals(getWithdrawalRequest().getParticipantStateOfResidence(),
                getWithdrawalRequest().getOriginalParticipantStateOfResidence())
                || !ObjectUtils.equals(getWithdrawalRequest().getOriginalStateTaxType(),
                        stateTaxType)) {
            setDefaultStateTax();
        }
    }

    /**
     * Determines whether the vesting information link should be displayed or suppressed.
     * 
     * @return boolean - True if the link should be displayed, otherwise false.
     * @see com.manulife.pension.ps.web.census.util.VestingHelper.isVestingInformationAvailable
     * @see #getContractHasNonFullyVestedMoneyTypes()
     */
    public boolean getShowVestingInformationLink() {
        boolean vestingInformationAvailable = false;
        try {
            vestingInformationAvailable = VestingHelper
                    .isVestingInformationAvailable(withdrawalRequest.getContractInfo()
                    .getContractId());
        } catch (final SystemException systemException) {
            logger
                    .debug(new StringBuffer(
                            "getShowVestingInformationLink> Could not determine if vesting "
                            + "information should be displayed.  Exception was [")
                            .append(systemException).append("].").toString());
            // Assume in this case we should suppress the link, the default 'false' value applies.
        } // end try/catch

        // Show the link if we have the info and the contract has non-fully vested money types.
        return vestingInformationAvailable && BooleanUtils.isTrue(getContractHasNonFullyVestedMoneyTypes());
    }

    /**
     * Returns the event date to be used with the vesting information link. Returns the event
     * specific date for certain withdrawal reasons (termination, disability, mandatory
     * distribution, retirement), otherwise returns the current date.
     * 
     * @return Date - The vesting information event date.
     */
    public Date getVestingInformationEventDate() {
        return getWithdrawalRequest().getVestingEventDate();
    }

    /**
     * Sets the default IRS Distribution Code for each payee.
     */
    protected void setDefaultIrsDistributionCode() {

        // Set IRS Distribution code for each payee
        for (WithdrawalRequestPayeeUi payee : getRecipients().iterator().next().getPayees()) {
            payee.setDefaultIrsDistributionCode();
        }
    }

    /**
     * Sets the default Rollover Plan Name for each payee.
     */
    protected void setDefaultRolloverPlanName() {
        for (WithdrawalRequestPayeeUi payee : getRecipients().iterator().next().getPayees()) {

            // Set the default rollover plan name.
            payee.setDefaultRolloverPlanName();
        }
    }

    /**
     * @return the isViewAction
     */
    public boolean isViewAction() {
        return isViewAction;
    }

    /**
     * @param isViewAction the isViewAction to set
     */
    public void setViewAction(final boolean isViewAction) {
        this.isViewAction = isViewAction;
    }

    /**
     * @return the isConfirmAction
     */
    public boolean isConfirmAction() {
        return isConfirmAction;
    }

    /**
     * @param isConfirmAction the isConfirmAction to set
     */
    public void setConfirmAction(final boolean isConfirmAction) {
        this.isConfirmAction = isConfirmAction;
    }

    /**
     * Queries if the static content should be shown.
     * 
     * @return boolean - True if the static content should be shown, false otherwise.
     */
    public boolean getShowStaticContent() {

        if (isViewAction()) {
            // View is only show for pending review and pending approval
            return getIsRequestPending();

        } else if (isConfirmAction()) {
            // Confirm should always be shown
            return true;
        } else {
            // This query is only used on the shared view and confirm JSPs, so we will default to
            // true elsewhere.
            return true;
        }
    }

    /**
     * Used to show/hide the view participant agreed legalese content.
     * 
     * @return boolean - True if the participant legalese content is present.
     */
    public boolean getShowParticipantAgreedLegaleseContent() {

        final LegaleseInfo participantLegaleseInfo = getWithdrawalRequest()
                .getParticipantLegaleseInfo();
        if (participantLegaleseInfo != null) {
            if (StringUtils.isNotEmpty(participantLegaleseInfo.getLegaleseText())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to determine if contract has elected to use the JH generic IRS Special Tax Notice
     * 
     * @return boolean - True if the special tax notice is used
     */
    public boolean getIsIRSSpecialTaxNotice() {
        
        return getWithdrawalRequest().getContractInfo().getCsfIRSSpecialTaxNotice().booleanValue();
    }

    /**
     * Used to determine if request if participant initiated.
     * 
     * @return boolean - True if the participant initiated request
     */
    public boolean getIsParticipantInitiated() {

        return getWithdrawalRequest().getIsParticipantCreated();
    }
    
    /**
     * Used to determine if vesting called indicator has been set.
     * 
     * @return boolean - True if vesting called indicator has been set to Y
     */
    public boolean getHasVestingCalled() {

        return getWithdrawalRequest().getVestingCalledInd();

    }

    public boolean getIsPOWFirstTimeForReviewAndApproveForMT() {
        boolean isFirstTime = false;
        if (getIsParticipantInitiated() && !getHasVestingCalled()) {
            isFirstTime = true;
        }

        return isFirstTime;
    }

    /**
     * This method checks if the It was certified label needs to be displayed on the jsp.
     * 
     * @return
     */
    public boolean getShowItWasCertifiedLabel() {
        if (StringUtils.equals(
                WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE,
                getWithdrawalRequest().getReasonCode())
                || (getShowAtRiskDeclaration())) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the user is allowed to see the At Risk Indicator declaration.
     * 
     * @return
     */
    public boolean getShowAtRiskDeclaration() {
    	if (getWithdrawalRequest().isAtRiskDeclarationPermittedForUser()) {
        return true;
    }
    return false;
    }

    /**
     * Read Only form property: Checks if the withdrawal request has an IRA provider account setup.
     * 
     * @return true if the current WithdrawalRequest has a an IRA provider account setup.
     */
    public boolean getHasRiskIndicatorDeclaration() {
        return hasDeclaration(WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE);
    }

    /**
     * @return Boolean - The contractHasNonFullyVestedMoneyTypes.
     */
    public Boolean getContractHasNonFullyVestedMoneyTypes() {
        return contractHasNonFullyVestedMoneyTypes;
    }

    /**
     * @param contractHasNonFullyVestedMoneyTypes - The contractHasNonFullyVestedMoneyTypes to set.
     */
    public void setContractHasNonFullyVestedMoneyTypes(final Boolean contractHasNonFullyVestedMoneyTypes) {
        this.contractHasNonFullyVestedMoneyTypes = contractHasNonFullyVestedMoneyTypes;
    }

	/**
	 * @return the reasonDescriptionForDisplay
	 */
	public String getReasonDescriptionForDisplay() {
		return reasonDescriptionForDisplay = withdrawalRequest.getReasonDescriptionForDisplay();
	}

	/**
	 * @return the participantMaskedSSN
	 */
	public String getParticipantMaskedSSN() {
		return participantMaskedSSN = SSNRender.format(withdrawalRequest.getParticipantSSN(), StringUtils.EMPTY, true);
	}
    
	/**
	 * @return the wmsiOrPenchecksSelected
	 */
	public boolean isWmsiOrPenchecksSelected() {
		return wmsiOrPenchecksSelected = this.withdrawalRequest.isWmsiOrPenchecksSelected();
	}

	/**
	 * @return the cmaContent
	 */
	public WithdrawalCmaContent getCmaContent() {
		return cmaContent;
	}

	/**
	 * @param cmaContent the cmaContent to set
	 */
	public void setCmaContent(WithdrawalCmaContent cmaContent) {
		this.cmaContent = cmaContent;
	}

	/**
     * Sets the required values for PDF
     */
	private void setRecipientAndPayeeUiValuesForPDF() {
	
		for (WithdrawalRequestRecipientUi recipientUi :recipients) {
			recipientUi.setValuesForPDF();
			
			for (WithdrawalRequestPayeeUi payeeUi : recipientUi.getPayees()) {
				payeeUi.setValuesForPDF();
			}
		}
	}
	
	/**
     * Sets the required values for PDF
     */
	public void setValuesForPDF(WithdrawalCmaContent cmaContent){
		setRecipientAndPayeeUiValuesForPDF();
		setCmaContent(cmaContent);
		getReasonDescriptionForDisplay();
    	getParticipantMaskedSSN();
    	isWmsiOrPenchecksSelected();
    	totalOutstandingLoanAmt = getWithdrawalRequest().getTotalOutstandingLoanAmt();
    	showIrsLoanDistribution = getShowIrsLoanDistribution();
    	showFinalContributionDate = getWithdrawalRequest().getShowFinalContributionDate();
		isRequestApproved = getIsRequestApproved();
		showExpectedProcessingDate = getShowExpectedProcessingDate();
		showStaticContent = getShowStaticContent();
    	showHardshipReasonAndHardshipDescription = getShowHardshipReasonAndHardshipDescription();
    	showWmsiPenchecks = getShowWmsiPenchecks();
    	showDisabilityDate = getShowDisabilityDate();
    	showRetirementDate = getShowRetirementDate();
    	showTerminationDate = getShowTerminationDate();
    	isParticipantInitiated = getIsParticipantInitiated();
    	showAvailableWithdrawalAmountColumn = getShowAvailableWithdrawalAmountColumn();
    	showAvailableforHardshipColumn = getshowAvailableforHardshipColumn();
    	showRequestedWithdrawalAmountPercentColumn = getShowRequestedWithdrawalAmountPercentColumn();
    	totalAvailableWithdrawalAmount = getTotalAvailableWithdrawalAmount();
    	showTotalRequestedWithdrawalAmount = getShowTotalRequestedWithdrawalAmount();
    	hasPbaOrLoans = getHasPbaOrLoans();
    	showTotalAvailableWithdrawalAmount = getShowTotalAvailableWithdrawalAmount();
    	participantHasPartialStatus = getParticipantHasPartialStatus();
    	hasPre1987MoneyTypes = getHasPre1987MoneyTypes();
    	showReasonIsFullyVestedFromPlanSpecialMessage = getShowReasonIsFullyVestedFromPlanSpecialMessage();
    	showOptionForUnvestedAmount = getShowOptionForUnvestedAmount();
    	showTaxWitholdingSection = getShowTaxWitholdingSection();
    	stateTaxType = getStateTaxType();
        federalTaxPercent = getFederalTaxPercent();
        stateTaxPercent = getStateTaxPercent();
        totalRequestedWithdrawalAmount = getTotalRequestedWithdrawalAmount();
        hasTaxNoticeDeclaration = getHasTaxNoticeDeclaration();
        isIRSSpecialTaxNotice = getIsIRSSpecialTaxNotice();
        hasWaitPeriodWaveDeclaration = getHasWaitPeriodWaveDeclaration();
        showParticipantAgreedLegaleseContent = getShowParticipantAgreedLegaleseContent();
        showItWasCertifiedLabel = getShowItWasCertifiedLabel();
        hasIraProviderDeclaration = getHasIraProviderDeclaration();
        hasBothPbaAndLoans = getHasBothPbaAndLoans();
        hasPbaOnly = getHasPbaOnly();
        hasLoansOnly = getHasLoansOnly();
        isRequestSendForReview = getIsRequestSendForReview();
        isRequestSendForApprove = getIsRequestSendForApprove();
        
        showAtRiskDeclaration = getShowAtRiskDeclaration();
        if(showAtRiskDeclaration){
        	hasRiskIndicatorDeclaration = getHasRiskIndicatorDeclaration();
        }
        
    }
	
	public boolean getshowAvailableforHardshipColumn() {
		 boolean hardshipcolum = false;
	        if(StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE,
	                withdrawalRequest.getReasonCode())) {
	        	hardshipcolum = true;
	        }

	        return hardshipcolum;
		
		
		
	}

	/**
     * A check for displaying the field as edit mode or not.
     * @return Boolean
     */
    public boolean isViewOnly() { 
        boolean bgaInd = getWithdrawalRequest().getContractInfo() 
                        .isBundledGaIndicator(); 
        if (bgaInd	&& getIsRequestSendForApprove()
        		&& getWithdrawalRequest().getContractPermission().isSigningAuthority()) { 
                return true; 
        } 
        return false; 
    } 
    
    /**
     * A check for the field is mandatory or not 
	 * when the contract is a bundled and before sending for Approval.
     * @return Boolean
     */
    public boolean isMandatoryForBundledContract() { 
        boolean bgaInd = getWithdrawalRequest().getContractInfo() 
                        .isBundledGaIndicator(); 
        if (bgaInd && (getIsRequestInDraft() || getIsRequestSendForReview())
        		&& getWithdrawalRequest().getContractPermission().isSigningAuthority()) { 
                return true; 
        } 
        return false; 
    }
    
    /**
     * A check for displaying the field as edit mode or not.
     * @return Boolean
     */
    public boolean isShowDenyButton() { 
        boolean bgaInd = getWithdrawalRequest().getContractInfo() 
                        .isBundledGaIndicator(); 
        if (bgaInd	&& getIsRequestSendForApprove() 
        		&& getWithdrawalRequest().getContractPermission().isSigningAuthority()) { 
                return false; 
        } 
        return true; 
    }

	/**
	 * @return the printParticipant
	 */
	public boolean isPrintParticipant() {
		return printParticipant;
	}

	/**
	 * @param printParticipant the printParticipant to set
	 */
	public void setPrintParticipant(boolean printParticipant) {
		this.printParticipant = printParticipant;
	}

	/**
	 * @return the isParticipantGIFLEnabled
	 */
	public boolean isParticipantGIFLEnabled() {
		return isParticipantGIFLEnabled;
	}

	/**
	 * @param isParticipantGIFLEnabled the isParticipantGIFLEnabled to set
	 */
	public void setParticipantGIFLEnabled(boolean isParticipantGIFLEnabled) {
		this.isParticipantGIFLEnabled = isParticipantGIFLEnabled;
	}

	/**
	 * @return the atRiskApprovalText
	 */
	public String getAtRiskApprovalText() {
		return atRiskApprovalText;
	}

	/**
	 * @param atRiskApprovalText the atRiskApprovalText to set
	 */
	public void setAtRiskApprovalText(String atRiskApprovalText) {
		this.atRiskApprovalText = atRiskApprovalText;
	}

	/**
	 * @return the atRiskdetailText
	 */
	public ArrayList<String> getAtRiskdetailText() {
		return atRiskdetailText;
	}

	/**
	 * @param atRiskdetailText the atRiskdetailText to set
	 */
	public void setAtRiskdetailText(ArrayList<String> atRiskdetailText) {
		this.atRiskdetailText = atRiskdetailText;
	}
	

    public void setShowLegallyMarriedAsEditable(boolean showLegallyMarriedAsEditable) {
        this.showLegallyMarriedAsEditable = showLegallyMarriedAsEditable;
    }

    public boolean isShowLegallyMarriedAsEditable() throws SystemException {

        final String withdrawalStatusCode = getWithdrawalRequest().getStatusCode();
        if (!isParticipantInitiated()
                && (isWithdrawalNewStatus(withdrawalStatusCode)
                        || isWithdrawalDraftStatus(withdrawalStatusCode)
                        || isWithdrawalPendingReviewStatus(withdrawalStatusCode) || (isWithdrawalPendingApprovalStatus(withdrawalStatusCode) && (!getWithdrawalRequest()
                        .getContractInfo().isBundledGaIndicator() || !hasContractTPASigningAuthorityPermission(getWithdrawalRequest()
                        .getContractId()))))) {
            return true;
        }
        return false;
    }

    private boolean isParticipantInitiated() {
        return "PA".equals(getWithdrawalRequest().getUserRoleCode());
    }

    private boolean isWithdrawalDraftStatus(String withdrawalStatusCode) {
        return WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE.equals(withdrawalStatusCode);
    }

    private boolean isWithdrawalNewStatus(String withdrawalStatusCode) {
        if (WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE.equals(withdrawalStatusCode)
                && getWithdrawalRequest().getSubmissionId() == null) {
            return true;
        }
        return false;
    }

    private boolean isWithdrawalPendingReviewStatus(String withdrawalStatusCode) {
        return WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE.equals(withdrawalStatusCode);
    }

    private boolean isWithdrawalPendingApprovalStatus(String withdrawalStatusCode) {
        return WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE
                .equals(withdrawalStatusCode);
    }

    private boolean hasContractTPASigningAuthorityPermission(Integer contractId)
            throws SystemException {
        return SecurityServiceDelegate.getInstance().getTpaFirmContractPermission(contractId)
                .isSigningAuthority();
    }

    public String getLegallyMarriedInd() {
        return legallyMarriedInd;
    }

    public void setLegallyMarriedInd(String legallyMarriedInd) {
        this.legallyMarriedInd = legallyMarriedInd;
    }

    /**
     * Sets the default value for the payee name display field if the field is display
     * only.
     */
    protected void setDefaultEftPayeeDisplayName() {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("setDefaultEftPayeeDisplayName> Entry.").toString());
        }

        // Set payment to field for payees
        final String paymentTo = getWithdrawalRequest().getPaymentTo();
        final Iterator<Payee> iterator = getWithdrawalRequest().getRecipients()
                .iterator().next().getPayees().iterator();
        final Payee payee1 = iterator.next();
        if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)) {
            // Set payee to participant name
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "setDefaultEftPayeeDisplayName> Direct to participant - setting EFT payee name from [")
                                .append(payee1.getOrganizationName())
                                .append("] to participant name [").append(
                                        getWithdrawalRequest().getParticipantName()).append("].")
                                .toString());
            }
            payee1.setOrganizationName(
                    getWithdrawalRequest().getParticipantName());
        } else if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            // Set payee to trustee name
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "setDefaultEftPayeeDisplayName> Direct to participant - setting EFT payee name from [")
                                .append(payee1.getOrganizationName())
                                .append("] to trustee name [").append(
                                        getWithdrawalRequest().getParticipantInfo()
                                                .getTrusteeName()).append("].").toString());
            }
            payee1.setOrganizationName(
                    getWithdrawalRequest().getParticipantInfo().getTrusteeName());
        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {

            // Check if first payee should be reset if payment to has changed
            if (getWithdrawalRequest().hasPaymentToChanged()) {

                if (logger.isDebugEnabled()) {
                    logger
                            .debug(new StringBuffer(
                                    "setDefaultEftPayeeDisplayName> After tax - setting payee 1 name from [")
                                    .append(payee1.getOrganizationName())
                                    .append("] to blank.").toString());
                }
                payee1.setOrganizationName(StringUtils.EMPTY);
            }
            // Leave first payee as input - set second payee to participant name
            // Default the display name
            final Payee payee2 = iterator.next();
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "setDefaultEftPayeeDisplayName> After tax - setting payee 2 name from [")
                                .append(payee2.getOrganizationName())
                                .append("] to participant name [").append(
                                        getWithdrawalRequest().getParticipantName()).append("].")
                                .toString());
            }
            payee2.setOrganizationName(
                    getWithdrawalRequest().getParticipantName());
        } else {
            // Roll-over - check if the payment to has changed to see if we should reset field
            if (getWithdrawalRequest().hasPaymentToChanged()) {

                // Payment to changed - reset the field
                if (logger.isDebugEnabled()) {
                    logger
                            .debug(new StringBuffer(
                                    "setDefaultEftPayeeDisplayName> Rollover - setting payee party name from [")
                                    .append(payee1.getOrganizationName())
                                    .append("] to blank.").toString());
                }
                payee1.setOrganizationName(StringUtils.EMPTY);
            }
        }
    } 
	

	
	
	public boolean isNonTaxablePayeeFlag() {
		return nonTaxablePayeeFlag;
	}

	public void setNonTaxablePayeeFlag(boolean nonTaxablePayeeFlag) {
		this.nonTaxablePayeeFlag = nonTaxablePayeeFlag;
	}

	public boolean isRothPayeeFlag() {
		return rothPayeeFlag;
	}

	public void setRothPayeeFlag(boolean rothPayeeFlag) {
		this.rothPayeeFlag = rothPayeeFlag;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBotInd() {
		return botInd;
	}

	public void setBotInd(String botInd) {
		this.botInd = botInd;
	}

	public String getEarInd() {
		return earInd;
	}

	public void setEarInd(String earInd) {
		this.earInd = earInd;
	}

	public String getRothInd() {
		return rothInd;
	}

	public void setRothInd(String rothInd) {
		this.rothInd = rothInd;
	}

	public String getNonRothInd() {
		return nonRothInd;
	}

	public void setNonRothInd(String nonRothInd) {
		this.nonRothInd = nonRothInd;
	}
	   public String getTb() {
				return Tb;
			}

			public void setTb(String tb) {
				Tb = tb;
			}

			public String getPa() {
				return Pa;
			}

			public void setPa(String pa) {
				Pa = pa;
			}

			public String getPaat() {
				return Paat;
			}

			public void setPaat(String paat) {
				Paat = paat;
			}

			public String getPar() {
				return Par;
			}

			public void setPar(String par) {
				Par = par;
			}

			public String getRb() {
				return Rb;
			}

			public void setRb(String rb) {
				Rb = rb;
			}

			public String getNrat() {
				return Nrat;
			}

			public void setNrat(String nrat) {
				Nrat = nrat;
			}
	

}

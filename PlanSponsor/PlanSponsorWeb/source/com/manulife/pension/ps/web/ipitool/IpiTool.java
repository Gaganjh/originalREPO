package com.manulife.pension.ps.web.ipitool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.collections.list.GrowthList;
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolJhChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolRecoveryMethodField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSalesAndServiceChargeField;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.fee.util.DiscontinuanceContractYearCalcuator;
import com.manulife.pension.service.fee.util.FeeUtil;
import com.manulife.pension.service.fee.util.Constants.ContractAdminFeeType;
import com.manulife.pension.service.fee.util.Constants.PaymentTypes;
import com.manulife.pension.service.fee.valueobject.AdministrativeExpense;
import com.manulife.pension.service.fee.valueobject.BasicAssetChargeRate;
import com.manulife.pension.service.fee.valueobject.DiscontinuaceFee;
import com.manulife.pension.service.fee.valueobject.DiscontuanceContractYearParam;
import com.manulife.pension.service.fee.valueobject.IpiHypotheticalVO;
import com.manulife.pension.util.ArrayUtility;

enum IpiTool {
    
    INSTANCE;
    
    private static final Logger logger = Logger.getLogger(IpiTool.class);
    
    private enum Forward {
        
        SUBMISSION("submission"),
        SUMMARY("summary"),
        CONFIRMATION("confirmation");
        
        private final String url;
        private Forward(String url) { this.url = url; }
        
    }
    
    static String appId = Environment.getInstance().getAppId();
    
    private static final Set<ContractAdminFeeType> adminFeeTypes =
        ArrayUtility.toUnsortedSet(ContractAdminFeeType.values());
    
    private static final Set<String> PREACTIVE_STATUS =
        Collections.unmodifiableSet(
                ArrayUtility.toUnsortedSet(
                        "PS",
                        "DC",
                        "PC",
                        "CA"));
    
    enum IpiHypotheticalToolState {
        
        ENTER {

            IpiToolView validate(
                    Map<IpiToolField<?>, String> currentFieldMap,
                    Map<IpiToolField<?>, String> inputFieldMap,
                    List<BasicAssetChargeLine> currentBac,
                    List<BasicAssetChargeLine> inputBac,
                    List<String> currentDiScale,
                    List<String> inputDiScale)
            throws SystemException {
                
                return currentFieldMap == null ? null
                        : super.validate(currentFieldMap, inputFieldMap, currentBac, inputBac, currentDiScale, inputDiScale, false, false);
                
            }

            public IpiToolView process(
            		UserProfile userProfile,
                    Map<IpiToolField<?>, String> currentFieldMap,
                    Map<IpiToolField<?>, String> inputFieldMap,
                    List<BasicAssetChargeLine> currentBacScale,
                    List<BasicAssetChargeLine> inputBacScale,
                    List<String> currentDiScale,
                    List<String> inputDiScale,
                    BigDecimal totalAssets)
            throws SystemException {
                
            	 List<SectionView> fieldAttributeValue;
            	 Integer contractId = userProfile.getCurrentContract().getContractNumber() ;
                
                 if (currentFieldMap.isEmpty()) {
                     
                     Map<ContractAdminFeeType, Object> fees =
                             FeeServiceDelegate.getInstance(appId) 
                             .getAdminFees(
                            		 contractId,
                            		 new Date(), 
                             		adminFeeTypes); 
                     
                  // Calculate current year DI chanrge
                  final List<DiscontinuaceFee> discontinuanceChargeScale;
               	  BigDecimal diCharge = BigDecimal.ZERO;
                     
               	 if(fees.get(ContractAdminFeeType.DISCONTINUANCE_CHARGE_SCALE) != null ){
               		 List<DiscontinuaceFee> workingScale = (List<DiscontinuaceFee>) fees.get(ContractAdminFeeType.DISCONTINUANCE_CHARGE_SCALE);
                     if (workingScale == null) {
                         workingScale = Collections.emptyList();
                     }
                     List<String> currentDi = new ArrayList<String>();
                     for (DiscontinuaceFee discontinuaceFee : workingScale) {
                    	 int minYear= Integer.parseInt(discontinuaceFee.getMinimumYear());
                    	 int maxYear= Integer.parseInt(discontinuaceFee.getMaximumYear());
                    	 String amount = "0.0"; 
                    	 if(discontinuaceFee.getAmountValue() != null){
                    		 amount = discontinuaceFee.getAmountValue().setScale(3, BigDecimal.ROUND_DOWN).toString();
                    	}
                    	
                    	 if(!(maxYear == 999)){
                    		 for(int i= minYear; i <= maxYear ;i++  ){
                        		 currentDi.add(amount);
                        	 } 
                    	 }
                    	 
					}
                    currentDiScale = currentDi;
                    if(!(currentDiScale.size() >= 7)){
                    	while (currentDiScale.size() < 7) {
                    		currentDiScale.add("0.000");
                    	}
                    }
                     
                     DiscontuanceContractYearParam discParam =   FeeServiceDelegate.getInstance(appId).
                     											getContractDiscontinuanceParameters(contractId);
                     
                     Integer dcYear = new Integer(
                             DiscontinuanceContractYearCalcuator.getDiscontinunaceContractYear(
                            		 discParam,
                            		 new Date()));
                     
                     discontinuanceChargeScale =
                             Collections.unmodifiableList(
                                     FeeUtil.filterScaleByContractYear(
                                             workingScale,
                                             dcYear));
                     
                     if (! discontinuanceChargeScale.isEmpty()) {
                         
                         DiscontinuaceFee firstScaleItem = discontinuanceChargeScale.get(0);
                         
                         if (Integer.parseInt(firstScaleItem.getMinimumYear()) <= dcYear
                                 && Integer.parseInt(firstScaleItem.getMaximumYear()) >= dcYear) {
                        	 diCharge = firstScaleItem.getAmountValue();
                         }
                         
                     } 
               	 }
               	  
                 fees.put(ContractAdminFeeType.DISCONTINUANCE_CHARGE_SCALE, diCharge);   
                                  
                 //Populate current year BAC and DI Temp work around
                 
                 
               // call apollo stored proc to get BAC scale 
				List<BasicAssetChargeRate> bacRate = FeeServiceDelegate
						.getInstance(appId).getBaseAssetChargeScale(
								contractId, new Date());
				currentBacScale = BacScaleRetrievalViewTransformer.INSTANCE
						.transform(bacRate);
				// Set Monthly BAC for IPI tool
				
				BigDecimal currentBac = BigDecimal.ZERO ;
					if (PREACTIVE_STATUS.contains(userProfile.getCurrentContract().getStatus())) {
						
						currentBac = FeeServiceDelegate.getInstance(appId).
							calculateSpaBAC(contractId, bacRate, userProfile.getCurrentContract().getTotalAssets(), new Date());
					} else {
						currentBac = (BigDecimal) fees.get(ContractAdminFeeType.MONTHLY_BASIC_ASSET_CHARGE);
					}
				
				fees.put(ContractAdminFeeType.BLENDED_ASSET_CHARGE, currentBac.setScale(3, RoundingMode.HALF_UP));
                                     
                 fieldAttributeValue = CurrentFieldRetrievalViewTransformer.INSTANCE.transform(fees);    
                
                 } else {
                	SectionViewListBuilder builder = new SectionViewListBuilder(); 
                	fieldAttributeValue = BypassViewTransformer.INSTANCE.transform(currentFieldMap, inputFieldMap, builder).build();
                     
                }
                
                return createView(Forward.SUBMISSION, fieldAttributeValue, currentBacScale, inputBacScale, currentDiScale, inputDiScale);
                
            }
        },
        SUBMIT {

            @Override
            public IpiToolView process(
            		UserProfile userProfile,
                    Map<IpiToolField<?>, String> currentFieldMap,
                    Map<IpiToolField<?>, String> inputFieldMap,
                    List<BasicAssetChargeLine> currentBacScale,
                    List<BasicAssetChargeLine> inputBacScale,
                    List<String> currentDiScale,
                    List<String> inputDiScale,
                    BigDecimal totalAssets)
            throws SystemException {
            	
            	Integer contractId = userProfile.getCurrentContract().getContractNumber();
            	String contractStatus = userProfile.getCurrentContract().getStatus();
            	
                Map<AdministrativeExpense, BigDecimal> currentAdminExpenses =
                        FeeServiceDelegate.getInstance(appId) 
                        .calculateAdministrativeExpenses( 
                        		contractId,
                                SummarizeProcessTransformer.INSTANCE.transform(currentFieldMap), contractStatus, totalAssets);
                
                Map<AdministrativeExpense, BigDecimal> changedAdminExpenses =
                        FeeServiceDelegate.getInstance(appId) 
                        .calculateAdministrativeExpenses( 
                        		contractId,
                                SummarizeProcessTransformer.INSTANCE.transform(
                                     MergeFieldTransformer.INSTANCE.transform(currentFieldMap, inputFieldMap)), contractStatus, totalAssets);
                
                SectionViewListBuilder builder = new SectionViewListBuilder();
                
                builder = BypassViewTransformer.INSTANCE.transform(currentFieldMap, inputFieldMap, builder);
                List<SectionView> fieldAttributeValue = SummarizeViewTransformer.INSTANCE.transform(currentAdminExpenses, changedAdminExpenses, 
                											currentFieldMap, inputFieldMap, builder);
                
                return createView(Forward.SUMMARY, fieldAttributeValue, currentBacScale, inputBacScale, currentDiScale, inputDiScale);
                
            }
           
        },
        GENERATE {

            @SuppressWarnings("unchecked")
			@Override
            public IpiToolView process(
            		UserProfile userProfile,
                    Map<IpiToolField<?>, String> currentFieldMap,
                    Map<IpiToolField<?>, String> inputFieldMap,
                    List<BasicAssetChargeLine> currentBacScale,
                    List<BasicAssetChargeLine> inputBacScale,
                    List<String> currentDiScale,
                    List<String> inputDiScale,
                    BigDecimal totalAssets)
            throws SystemException {
                    
 	            IpiHypotheticalVO ipiHypotheticalVO = new IpiHypotheticalVO();
 	            
 	            	ipiHypotheticalVO.setContractId(userProfile.getCurrentContract().getContractNumber());
 	            	ipiHypotheticalVO.setContractEffectiveDate(userProfile.getCurrentContract().getEffectiveDate());
 	            	ipiHypotheticalVO.setContractStatus(userProfile.getCurrentContract().getStatus());
 	            	ipiHypotheticalVO.setTotalAssets(totalAssets);
 	            	ipiHypotheticalVO.setContractName(userProfile.getCurrentContract().getContractLegalName());
 	            	ipiHypotheticalVO.setCarName(userProfile.getCurrentContract().getContractCarName());
 	            	ipiHypotheticalVO.setProfileId(String.valueOf(userProfile.getPrincipal().getUserName()));
 	            	//Transform fees
 	             	ipiHypotheticalVO.setCurrentFees(SummarizeProcessTransformer.INSTANCE.transform(currentFieldMap));
 	            	if(!inputFieldMap.isEmpty()&& inputFieldMap != null){
 	            	ipiHypotheticalVO.setNewFees(SummarizeProcessTransformer.INSTANCE.transform(
 	            	MergeFieldTransformer.INSTANCE.transform(currentFieldMap, inputFieldMap)));
 	            	ipiHypotheticalVO.setNewPricingFees(SummarizeProcessTransformer.INSTANCE.transform(inputFieldMap));
 	            	} 
 	            	int index = 0;
 					
 	            	List<BasicAssetChargeLine> finalBacScale =  
 	            						LazyList.decorate(new GrowthList(), new BacFactory());
 	       		
	 	       		List<BasicAssetChargeLine> bacScale = currentBacScale;
	 	       		BasicAssetChargeLine basicAssetChargeScale;
	 	       		
	 	       		if(inputBacScale.get(0).getBandEnd().equals(String.valueOf(BigDecimal.ZERO)) 
	 	       					&& inputBacScale.get(0).getCharge().equals(String.valueOf(BigDecimal.ZERO))){
	 	       			finalBacScale = currentBacScale;
	 	       			
	 	       		} else if(inputBacScale.get(0).getBandEnd().equals(String.valueOf(BigDecimal.ZERO)) 
	 	       					&& !inputBacScale.get(0).getCharge().equals(String.valueOf(BigDecimal.ZERO))){
	 	       			for (BasicAssetChargeLine basicAssetCharge : bacScale) {
	 	       				basicAssetChargeScale = new BasicAssetChargeLine();
	 	       				basicAssetChargeScale.setBandStart(basicAssetCharge.getBandStart());
	 	       				basicAssetChargeScale.setBandEnd(basicAssetCharge.getBandEnd());
	 	       				basicAssetChargeScale.setCharge(inputBacScale.get(index).getCharge());
	 	       				finalBacScale.add(index, basicAssetChargeScale);
	 	       				index++;
	 	       				
	 	       			}
	 	       		} else if(!inputBacScale.get(0).getBandEnd().equals(String.valueOf(BigDecimal.ZERO)) 
	 	       					&& inputBacScale.get(0).getCharge().equals(String.valueOf(BigDecimal.ZERO))) {
	 	       			for (BasicAssetChargeLine basicAssetCharge : bacScale) {
	 	       				basicAssetChargeScale = new BasicAssetChargeLine();
	 	       				basicAssetChargeScale.setBandStart(inputBacScale.get(index).getBandStart());
	 	       				basicAssetChargeScale.setBandEnd(inputBacScale.get(index).getBandEnd());
	 	       				basicAssetChargeScale.setCharge(basicAssetCharge.getCharge());
	 	       				finalBacScale.add(index, basicAssetChargeScale);
	 	       				index++;
	 	       				
	 	       			}
	 	       			
	 	       		} else {
	 	       			finalBacScale = inputBacScale;
	 	       			
	 	       		}
	 	       		ipiHypotheticalVO.setCurrentBacScale(BaseAssetTransformer.INSTANCE.transform(currentBacScale));
	            	ipiHypotheticalVO.setNewBacScale(BaseAssetTransformer.INSTANCE.transform(finalBacScale));
	            	
 	            	//DI charges
 	            	ipiHypotheticalVO.setCurrentDiScale(currentDiScale);	
 	             	if(inputDiScale.size() >0 && StringUtils.isNotBlank((String) inputDiScale.get(0))){
 	             		while (StringUtils.isBlank(inputDiScale.get(inputDiScale.size()-1))){
 	             			inputDiScale.remove(inputDiScale.size()-1);
 	                     }
 	             		ipiHypotheticalVO.setNewDiScale(inputDiScale);
 	             	}else{
 	             		ipiHypotheticalVO.setNewDiScale(currentDiScale);
 	           	    }
 	            	// Generate Change notification document to pricing shared drive
 	             	Map<String,Map<String,String>> notificationStatus = new HashMap<String, Map<String,String>>();
 	             	try {
 	             			notificationStatus = FeeServiceDelegate.getInstance(appId) 
 	            								.persistHypotheticalParticipantChangeNotificationBinary(ipiHypotheticalVO);
 	            			 
 	            	} catch (Exception e) {
 	            		// If change notification fails while calculating fee or fee service call.
 	            		Map<String,String> status = new HashMap<String,String>();
	            		Map<String,String> errors = new HashMap<String,String>();
 	            		String ststusCode = new SystemException(e, e.getMessage()).getUniqueId();
 	            		if(notificationStatus.isEmpty()){
 	            			logger.error("Problem occured while generating IPI tool participant change notification: contractId: "+ userProfile.getCurrentContract().getContractNumber(), e);
 	            			status.put("CSV_STATUS", "1619"); // CSV failure error code.
 	            			status.put("DOC_STATUS", "1620"); // Document Failure code.
 	            			errors.put("CSV_NOT_CREATED", ststusCode); 	//CSV failure error code
 	            			errors.put("DOC_NOT_CREATED", ststusCode);	//Document failure error code	
 	            			notificationStatus.put("statusCode", status);
 	            			notificationStatus.put("errorCode", errors);
 	            		}
 	            	}
            	return createView(Forward.CONFIRMATION, null, null, null, null, null, null, null, notificationStatus );
            }
            
        };
        
        private static final List<IpiToolField<?>> INPUT_FIELDS_ALL;
        static {
            
            ArrayList<IpiToolField<?>> fields = new ArrayList<IpiToolField<?>>();
            fields.addAll(Arrays.asList(IpiToolRecoveryMethodField.values()));
            fields.addAll(Arrays.asList(IpiToolJhChargeField.values()));
            fields.addAll(Arrays.asList(IpiToolSalesAndServiceChargeField.values()));
            fields.trimToSize();
            INPUT_FIELDS_ALL = Collections.unmodifiableList(fields);
            
        }
        
        abstract IpiToolView process(
        		UserProfile userProfile,
                Map<IpiToolField<?>, String> currentFieldMap,
                Map<IpiToolField<?>, String> inputFieldMap,
                List<BasicAssetChargeLine> currentBacScale,
                List<BasicAssetChargeLine> inputBacScale,
                List<String> currentDiScale,
                List<String> inputDiScale,
                BigDecimal totalAssets)
        throws SystemException;

        
        private String determineFinalValue(
                IpiToolField<?> key,
                Map<IpiToolField<?>, String> currentFieldMap,
                Map<IpiToolField<?>, String> inputFieldMap) {
            
            return
                    StringUtils.isNotBlank(inputFieldMap.get(key))
                    ? inputFieldMap.get(key)
                    : currentFieldMap.get(key);
            
        }
        
        private static final FieldLevelFormValidator CURRENT_FIELD_VALIDATOR =
                FieldLevelFormValidator.getInstance(
                        FieldLevelFormValidator.FIELD_TYPE_VALIDATION,
                        FieldLevelFormValidator.ALLOW_CURRENT_NEGATIVE_VALIDATION,
                        FieldLevelFormValidator.MAX_INT_DIGITS_VALIDATION,
                        FieldLevelFormValidator.CODE_VALIDATION);
        
        private static final FieldLevelFormValidator INPUT_FIELD_VALIDATOR =
                FieldLevelFormValidator.getInstance(
                        FieldLevelFormValidator.FIELD_TYPE_VALIDATION,
                        FieldLevelFormValidator.ALLOW_INPUT_NEGATIVE_VALIDATION,
                        FieldLevelFormValidator.MAX_INT_DIGITS_VALIDATION,
                        FieldLevelFormValidator.SCALE_VALIDATION,
                        FieldLevelFormValidator.CODE_VALIDATION);
        
        @SuppressWarnings("unchecked")
		IpiToolView validate(
                Map<IpiToolField<?>, String> currentFieldMap,
                Map<IpiToolField<?>, String> inputFieldMap,
                List<BasicAssetChargeLine> currentBacScale,
                List<BasicAssetChargeLine> inputBacScale,
                List<String> currentDiScale,
                List<String> inputDiScale,
                boolean isBacChanged,
                boolean isDiChanged)
        throws SystemException {
            
            try {
                
                CURRENT_FIELD_VALIDATOR.validate(
                        currentFieldMap.entrySet().iterator(),
                        ErrorDefinitionBuilder.UNSUPPORTED_BUILDER);
                
            } catch (UnsupportedOperationException uoe) {
                throw new SystemException("Current field values from web layer invalid: " + uoe.getMessage());
            }
            
            ErrorDefinitionBuilder builder = new ErrorDefinitionBuilder();
            INPUT_FIELD_VALIDATOR.validate(
                    new FilterIterator(
                            inputFieldMap.entrySet().iterator(),
                            new Predicate() {
                                @Override
                                public boolean evaluate(Object arg0) { return ! StringUtils.isBlank(((Map.Entry<IpiToolField, String>) arg0).getValue()); }
                            }),
                            builder);
            
            if (! builder.isFieldInError(IpiToolRecoveryMethodField.ASSET_CHARGE_RECOVERY_METHOD)
                    && ! builder.isFieldInError(IpiToolRecoveryMethodField.OTHER_CHARGE_RECOVERY_METHOD)
                    && PaymentTypes.BILLED_TO_CLIENT.getCode().equals(determineFinalValue(IpiToolRecoveryMethodField.ASSET_CHARGE_RECOVERY_METHOD, currentFieldMap, inputFieldMap))
                    && ! PaymentTypes.BILLED_TO_CLIENT.getCode().equals(determineFinalValue(IpiToolRecoveryMethodField.OTHER_CHARGE_RECOVERY_METHOD, currentFieldMap, inputFieldMap))) {
                
                builder.add(IpiToolRecoveryMethodField.ASSET_CHARGE_RECOVERY_METHOD, ValidationMessage.ASSET_AND_OTHER_RM);
                builder.add(IpiToolRecoveryMethodField.OTHER_CHARGE_RECOVERY_METHOD, ValidationMessage.ASSET_AND_OTHER_RM);
                
            }

    		if(isBacChanged){
    			builder.add(IpiToolJhChargeField.BLENDED_ASSET_CHARGE, ValidationMessage.BAC_CALCULATION);
    		}
    		
    		if(isDiChanged){
    			builder.add(IpiToolJhChargeField.DISCONTINUANCE_FEE, ValidationMessage.DI_CALCULATION);
    		} 
    		
            if (! builder.isFieldInError(IpiToolSalesAndServiceChargeField.ANNUALIZED_TPA_ASSET_BASED_FEE)
                    && StringUtils.isNotBlank(inputFieldMap.get(IpiToolSalesAndServiceChargeField.ANNUALIZED_TPA_ASSET_BASED_FEE))
                    && new BigDecimal(inputFieldMap.get(IpiToolSalesAndServiceChargeField.ANNUALIZED_TPA_ASSET_BASED_FEE)).signum() != 0) {
                
                builder.add(IpiToolSalesAndServiceChargeField.ANNUALIZED_TPA_ASSET_BASED_FEE, ValidationMessage.NONZERO_ANNUALIZED_TPA_ABF);
                
            }
            
            IpiToolView view =
                    builder.isEmpty()
                    ? null
                    : createView(
                            currentFieldMap,
                            inputFieldMap,
                            currentBacScale,
                            inputBacScale,
                            currentDiScale,
                            inputDiScale,
                            builder.getErrorKeys(),
                            builder.getErrorFields());
            
            return view;
            
        }
        
        static IpiHypotheticalToolState valueFor(
                Map<IpiToolField<?>, String> currentFieldMap,
                Map<IpiToolField<?>, String> inputFieldMap,
                String autoActionMethod)
        throws AccessControlException {
            
            IpiHypotheticalToolState state;
            
           if (autoActionMethod.equals("generate")) {
        	   
                state = GENERATE;
                
            } else if (currentFieldMap.keySet().containsAll(INPUT_FIELDS_ALL) && StringUtils.isNotBlank(autoActionMethod) ) {
                
                state = SUBMIT;
                
            } else {
                
                state = ENTER;
                
            }
            
            return state;
            
        }
        
        private static IpiToolView createView(
                final Forward forward,
                final List<SectionView> fieldAttribute,
                final List<BasicAssetChargeLine> currentBacScale,
                final List<BasicAssetChargeLine> inputBacScale,
                final List<String> currentDiScale,
                final List<String> inputDiScale,
                final List<Integer> errorKeys,
                final Set<String> errorFields,
                final Map<String,Map<String,String>> errorCode) {
            
            final String forwardUrl = forward.url;
            return new IpiToolView() {
                
                private static final long serialVersionUID = 1L;
                @Override
                public String getForwardUrl() { return forwardUrl; }
                @Override
                public List<SectionView> getFieldAttributeValue() { return fieldAttribute; }
                @Override
                public List<Integer> getErrorKeys() { return errorKeys; }
                @Override
                public Set<String> getErrorFields() { return errorFields; }
                @Override
                public List<BasicAssetChargeLine> getCurrentBacScale() { return currentBacScale; }
                @Override
                public List<BasicAssetChargeLine> getInputBacScale() { return inputBacScale; }
                @Override
                public List<String> getCurrentDiScale() { return currentDiScale; }
                @Override
                public List<String> getInputDiScale() { return inputDiScale; }
				@Override
				public Map<String, Map<String, String>> getErrorCode() { return errorCode; } 
            };
            
        }
        
        private static IpiToolView createView(
                final Forward forward,
                final List<SectionView> fieldAttribute,
                final List<BasicAssetChargeLine> currentBacScale,
                final List<BasicAssetChargeLine> inputBacScale,
                final List<String> currentDiScale,
                final List<String> inputDiScale) {
            
            return createView(
                    forward,
                    fieldAttribute,
                    currentBacScale,
                    inputBacScale,
                    currentDiScale,
                    inputDiScale,
                    null,
                    null,
                    null);
            
        }
        
        private static IpiToolView createView(
                Map<IpiToolField<?>, String> currentFieldMap,
                Map<IpiToolField<?>, String> inputFieldMap,
                List<BasicAssetChargeLine> currentBacScale,
                List<BasicAssetChargeLine> inputBacScale,
                List<String> currentDiScale,
                List<String> inputDiScale,
                List<Integer> errorKeys,
                Set<String> errorFields) {
            
            return createView(
                    Forward.SUBMISSION,
                    BypassViewTransformer.INSTANCE.transform(
                            currentFieldMap,
                            inputFieldMap,
                            new SectionViewListBuilder())
                    .build(),
                    currentBacScale,
                    inputBacScale,
                    currentDiScale,
                    inputDiScale,
                    errorKeys,
                    errorFields,
                    null);
            
        }
        
    }
    
    IpiToolView process(
    		UserProfile userProfile,
            Map<IpiToolField<?>, String> currentFieldMap,
            Map<IpiToolField<?>, String> inputFieldMap,
            List<BasicAssetChargeLine> currentBacScale,
            List<BasicAssetChargeLine> inputBacScale,
            List<String> currentDiScale,
            List<String> inputDiScale,
            String autoActionMethod,
            boolean isBacChanged,
            boolean isDiChanged)
    throws SystemException {
        
        IpiHypotheticalToolState state =
                IpiHypotheticalToolState.valueFor(
                        currentFieldMap,
                        inputFieldMap,
                        autoActionMethod);
        
        IpiToolView view = state.validate(currentFieldMap, inputFieldMap, currentBacScale, inputBacScale, currentDiScale, inputDiScale, isBacChanged, isDiChanged);
        
        if (view == null) {
            view = state.process(userProfile, currentFieldMap, inputFieldMap, currentBacScale, inputBacScale, currentDiScale, inputDiScale, userProfile.getCurrentContract().getTotalAssets());
        }
        
        return view;
        
    }
    
}

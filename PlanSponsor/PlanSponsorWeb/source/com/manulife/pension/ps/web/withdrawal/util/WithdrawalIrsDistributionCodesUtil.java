package com.manulife.pension.ps.web.withdrawal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.ps.web.withdrawal.WithdrawalForm;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.MultiPayeeTaxes;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;


/***
 * 
 * @author meripra
 *
 */
public class WithdrawalIrsDistributionCodesUtil {
	
	public static String PARTICIPANT ="Participant";

	/**
	 * 
	 * @param lookupData
	 * @param form
	 * @param withdrawalRequest
	 */
	public void getIrsDistributionCodeList(Map lookupData, WithdrawalForm form, WithdrawalRequest withdrawalRequest) {
		if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE"))
				&& (withdrawalRequest.getPaymentTo().equals("TR") || withdrawalRequest.getPaymentTo().equals("PA") || 
						("M".equals(withdrawalRequest.getPaymentTo())&& (withdrawalRequest.getParticipantDetails() !=null && withdrawalRequest.getParticipantDetails().indexOf(PARTICIPANT) !=-1)))
				&& (CollectionUtils.isNotEmpty(withdrawalRequest.getLoans())
						&& withdrawalRequest.LOAN_CLOSURE_OPTION.equals(withdrawalRequest.getLoanOption()))) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			if (lookupData.containsKey("IRS_CODE_WD")) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if ((!code.toString().equals("G")) && (!code.toString().equals("3"))) {
						if (WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate()) < 59.5
								&& (code.toString().equals("1") || code.toString().equals("2"))) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						}else if ((WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate())) >= 59.5 
								&& code.toString().equals("7")) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						}
						

					}

				}

			}
			lookupData.remove("IRS_CODE_WD");
			lookupData.put("IRS_CODE_WD", codeList);
			form.setLookupData(lookupData);
		} else if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE")|| withdrawalRequest.getReasonCode().equals("PD") || withdrawalRequest.getReasonCode().equals("IR"))
				&& (withdrawalRequest.getPaymentTo().equals("PA") )) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			if (lookupData.containsKey("IRS_CODE_WD")) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if ((!code.toString().equals("G")) && (!code.toString().equals("3"))) {
						if (WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate()) < 59.5
								&& (code.toString().equals("1") || code.toString().equals("2"))) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						}else if (WithdrawalWebUtil.checkAgePre1936(withdrawalRequest) 
								&& (code.toString().equals("7A") || code.toString().equals("7"))) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						}else if ((!WithdrawalWebUtil.checkAgePre1936(withdrawalRequest) &&(WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate())) >= 59.5) 
								&& code.toString().equals("7")) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						}
						

					}

				}

			}
			lookupData.remove("IRS_CODE_WD");
			lookupData.put("IRS_CODE_WD", codeList);
			form.setLookupData(lookupData);
		}else if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE")) && "M".equals(withdrawalRequest.getPaymentTo())) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			if (lookupData.containsKey("IRS_CODE_WD")) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if ((!code.toString().equals("G")) && (!code.toString().equals("3"))) {
						if (WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate()) < 59.5
								&& (code.toString().equals("1") || code.toString().equals("2"))) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						} else if (WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate()) >=59.5
								&& (code.toString().equals("7A") || code.toString().equals("7"))) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						}
						

					}

				}

			}
			lookupData.remove("IRS_CODE_WD");
			lookupData.put("IRS_CODE_WD", codeList);
			form.setLookupData(lookupData);
		}
		else if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE")|| withdrawalRequest.getReasonCode().equals("PD")|| withdrawalRequest.getReasonCode().equals("IR"))|| withdrawalRequest.getReasonCode().equals("DI")
				&& (withdrawalRequest.getPaymentTo().equals("RI") || withdrawalRequest.getPaymentTo().equals("RP"))) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			final Map lookupDataMap = new HashMap();
			if (lookupData.containsKey("IRS_CODE_WD")) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if (code.toString().equals("G")) {
						codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
					}

				}
			}
			lookupData.remove("IRS_CODE_WD");
			lookupData.put("IRS_CODE_WD", codeList);
			form.setLookupData(lookupData);
		 }else if("MD".equals(withdrawalRequest.getReasonCode()) && "PA".equals(withdrawalRequest.getPaymentTo())){
			 Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			 Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
					if ( !WithdrawalWebUtil.checkAgePre1936(withdrawalRequest) && WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate()) >= 72){
					if (lookupData.containsKey("IRS_CODE_WD")) {
						for (DeCodeVO irs : irsCode) {
							if ("7".toString().equals(irs.getCode().trim())) {					
									codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
								}
							}
						}
					lookupData.remove("IRS_CODE_WD");
					lookupData.put("IRS_CODE_WD", codeList);
					form.setLookupData(lookupData);
			}else if (WithdrawalWebUtil.checkAgePre1936(withdrawalRequest)){
				if (lookupData.containsKey("IRS_CODE_WD")) {
					for (DeCodeVO irs : irsCode) {
						if ("7A".toString().equals(irs.getCode().trim()) ||"7".toString().equals(irs.getCode().trim())) {					
								codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
							}
						}
					}
				lookupData.remove("IRS_CODE_WD");
				lookupData.put("IRS_CODE_WD", codeList);
				form.setLookupData(lookupData);
			}
		}else if(withdrawalRequest.getReasonCode().equals("HA") && withdrawalRequest.getPaymentTo().equals("PA")){
				Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
				if (lookupData.containsKey("IRS_CODE_WD")) {
					DeCodeVO vo = null;
					String code = "";
					String desc = "";
					
					Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
					for (DeCodeVO irs : irsCode) {
						code = irs.getCode().trim();
						desc = irs.getDescription();
						if ((!code.toString().equals("G")) && (!code.toString().equals("3"))) {
							if (WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate()) < 59.5
									&& (code.toString().equals("1") || code.toString().equals("2"))) {
								codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
							}else if (WithdrawalWebUtil.checkAgePre1936(withdrawalRequest) 
									&& (code.toString().equals("7A") || code.toString().equals("7"))) {
								codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
							}else if ((!WithdrawalWebUtil.checkAgePre1936(withdrawalRequest) &&(WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate())) >= 59.5) 
									&& code.toString().equals("7")) {
								codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
							}

						}

					}

				}
				lookupData.remove("IRS_CODE_WD");
				lookupData.put("IRS_CODE_WD", codeList);
				form.setLookupData(lookupData);
				} else if (withdrawalRequest.getReasonCode().equals("DI") && withdrawalRequest.getPaymentTo().equals("PA")) {
					Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
					final Map lookupDataMap = new HashMap();
					if (lookupData.containsKey("IRS_CODE_WD")) {
						DeCodeVO vo = null;
						String code = "";
						String desc = "";
						Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
						for (DeCodeVO irs : irsCode) {
							code = irs.getCode().trim();
							desc = irs.getDescription();
							if (code.toString().equals("3")) {
								codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
							}

						}
					}
					lookupData.remove("IRS_CODE_WD");
					lookupData.put("IRS_CODE_WD", codeList);
					form.setLookupData(lookupData);
				}  else {
			  form.setLookupData(lookupData);
		}
	}
	
	public void getIrsDistributionCodeListForReview(Map lookupData, WithdrawalForm form, WithdrawalRequest withdrawalRequest) {
		if(form.getWithdrawalRequestUi().isViewOnly() || withdrawalRequest.getIsParticipantCreated()) {
			getIrsDistributionCodeList(lookupData,  form,  withdrawalRequest);
			getIrsDistributionCodeListForLoans(lookupData,  form,  withdrawalRequest);
		}	
		else if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE"))
				&& (withdrawalRequest.getPaymentTo().equals("TR") || withdrawalRequest.getPaymentTo().equals("PA") || "M".equals(withdrawalRequest.getPaymentTo()))
				&& (CollectionUtils.isNotEmpty(withdrawalRequest.getLoans())
						&& withdrawalRequest.LOAN_CLOSURE_OPTION.equals(withdrawalRequest.getLoanOption()))) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			if (lookupData.containsKey("IRS_CODE_WD")) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if ((!code.toString().equals("G")) && (!code.toString().equals("3"))) {
						if (code.toString().equals("1") || code.toString().equals("2") || code.toString().equals("7")) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						}
						
					}

				}

			}
			lookupData.remove("IRS_CODE_WD");
			lookupData.put("IRS_CODE_WD", codeList);
			form.setLookupData(lookupData);
		} else if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE") || withdrawalRequest.getReasonCode().equals("PD")|| withdrawalRequest.getReasonCode().equals("IR"))
				&& (withdrawalRequest.getPaymentTo().equals("PA") || "M".equals(withdrawalRequest.getPaymentTo()))) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			if (lookupData.containsKey("IRS_CODE_WD")) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if ((!code.toString().equals("G")) && (!code.toString().equals("3"))) {
						if (code.toString().equals("1") || code.toString().equals("2") || code.toString().equals("7A") ||code.toString().equals("7")) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						}
					}

				}

			}
			lookupData.remove("IRS_CODE_WD");
			lookupData.put("IRS_CODE_WD", codeList);
			form.setLookupData(lookupData);
		} else if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE") || withdrawalRequest.getReasonCode().equals("PD")|| withdrawalRequest.getReasonCode().equals("IR")|| withdrawalRequest.getReasonCode().equals("DI"))
				&& (withdrawalRequest.getPaymentTo().equals("RI") || withdrawalRequest.getPaymentTo().equals("RP"))) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			final Map lookupDataMap = new HashMap();
			if (lookupData.containsKey("IRS_CODE_WD")) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if (code.toString().equals("G")) {
						codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
					}

				}
			}
			lookupData.remove("IRS_CODE_WD");
			lookupData.put("IRS_CODE_WD", codeList);
			form.setLookupData(lookupData);
			}else if("MD".equals(withdrawalRequest.getReasonCode()) && "PA".equals(withdrawalRequest.getPaymentTo())){
				Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
				if (lookupData.containsKey("IRS_CODE_WD")) {
					DeCodeVO vo = null;
					String code = "";
					String desc = "";
					Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
					for (DeCodeVO irs : irsCode) {
						code = irs.getCode().trim();
						desc = irs.getDescription();
						if ((!code.toString().equals("G")) && (!code.toString().equals("3")) && (!code.toString().equals("1")) && (!code.toString().equals("2"))) {
							if ( code.toString().equals("7A") ||code.toString().equals("7")) {
								codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
							}
						}

					}

				}
				lookupData.remove("IRS_CODE_WD");
				lookupData.put("IRS_CODE_WD", codeList);
				form.setLookupData(lookupData);
				
			}else if((withdrawalRequest.getReasonCode().equals("HA")  || (withdrawalRequest.getReasonCode().equals("IR")))
					&& withdrawalRequest.getPaymentTo().equals("PA")){
				Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
				if (lookupData.containsKey("IRS_CODE_WD")) {
					DeCodeVO vo = null;
					String code = "";
					String desc = "";
					Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
					for (DeCodeVO irs : irsCode) {
						code = irs.getCode().trim();
						desc = irs.getDescription();
						if ((!code.toString().equals("G")) && (!code.toString().equals("3"))) {
							if (code.toString().equals("1") || code.toString().equals("2") || code.toString().equals("7A") ||code.toString().equals("7")) {
								codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
							}
						}

					}

				}
				lookupData.remove("IRS_CODE_WD");
				lookupData.put("IRS_CODE_WD", codeList);
				form.setLookupData(lookupData);
				}else if (withdrawalRequest.getReasonCode().equals("DI") && withdrawalRequest.getPaymentTo().equals("PA")) {
					Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
					final Map lookupDataMap = new HashMap();
					if (lookupData.containsKey("IRS_CODE_WD")) {
						DeCodeVO vo = null;
						String code = "";
						String desc = "";
						Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_WD");
						for (DeCodeVO irs : irsCode) {
							code = irs.getCode().trim();
							desc = irs.getDescription();
							if (code.toString().equals("3")) {
								codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
							}

						}
					}
					lookupData.remove("IRS_CODE_WD");
					lookupData.put("IRS_CODE_WD", codeList);
					form.setLookupData(lookupData);
				}   else {
			form.setLookupData(lookupData);
		}
	}
	
	public void getIrsDistributionCodeListForLoans(Map lookupData, WithdrawalForm form, WithdrawalRequest withdrawalRequest) {
		if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE"))
				&& (withdrawalRequest.getPaymentTo().equals("TR") || withdrawalRequest.getPaymentTo().equals("PA") 
						|| withdrawalRequest.getPaymentTo().equals("RI") ||  withdrawalRequest.getPaymentTo().equals("RP"))
				&& (CollectionUtils.isNotEmpty(withdrawalRequest.getLoans())
						&& withdrawalRequest.LOAN_CLOSURE_OPTION.equals(withdrawalRequest.getLoanOption()))) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			if (lookupData.containsKey("IRS_CODE_LOAN")) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_LOAN");
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if ((!code.toString().equals("G")) && (!code.toString().equals("3"))) {
						if (WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate()) < 59.5
								&& (code.toString().equals("1M") || code.toString().equals("2M"))) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						} else if (WithdrawalWebUtil.getAgeCalculate(withdrawalRequest.getBirthDate()) >= 59.5
								&& (code.toString().equals("7M"))) {
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
						}

					}

				}

			}
			lookupData.remove("IRS_CODE_LOAN");
			lookupData.put("IRS_CODE_LOAN", codeList);
			form.setLookupData(lookupData);
		}  else if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE"))
				&&  (CollectionUtils.isNotEmpty(withdrawalRequest.getLoans()) && withdrawalRequest.getPaymentTo().equals("RP") 
						&&  withdrawalRequest.LOAN_ROLLOVER_OPTION.equals(withdrawalRequest.getLoanOption()))) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			final Map lookupDataMap = new HashMap();
			if (lookupData.containsKey("IRS_CODE_LOAN")) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get("IRS_CODE_LOAN");
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if (code.toString().equals("G")) {
						codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
					}

				}
			}
			lookupData.remove("IRS_CODE_LOAN");
			lookupData.put("IRS_CODE_LOAN", codeList);
			form.setLookupData(lookupData);
       } 
		else {
			form.setLookupData(lookupData);
		}
	}
	
	
	 public String getIrsCode( WithdrawalRequest withdrawalRequest) {
	        String irsCode = "";
	        Collection<Recipient> recipients = withdrawalRequest.getRecipients();
	        for(Recipient recipient: recipients) {
	            Collection<Payee> payees  = recipient.getPayees();
	            for(Payee payee: payees) {
	            	if(StringUtils.isNotBlank(payee.getIrsDistCode()))
	            		irsCode = payee.getIrsDistCode(); 
	            }
	        }
	        return irsCode;
	    }
	 
	 public boolean validateMultipleDestinations(WithdrawalRequest withdrawalRequest) {
		 //check for multidestion based on the contract No
		 boolean checkMultidestination = false;
		 try {
			 checkMultidestination = WithdrawalServiceDelegate.getInstance().validateMD(withdrawalRequest.getContractId());
		 }catch(Exception Ex) {
			 Ex.printStackTrace();
		 }
			   return checkMultidestination;
	    }
	 
	
	 
	 public void getPaymentToList(Map lookupData, WithdrawalForm form, WithdrawalRequest withdrawalRequest) {
	
	 if (!validateMultipleDestinations(withdrawalRequest)) {
			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			if (lookupData.containsKey(CodeLookupCache.PAYMENT_TO_TYPE)) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get(CodeLookupCache.PAYMENT_TO_TYPE);
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if ((!code.toString().equals("PI")) && (!code.toString().equals("PP"))) {					
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
					} 
					if(code.toString().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)) {
						withdrawalRequest.setRolloverTypeEligible(true);
					}
				}

			}
			lookupData.remove(CodeLookupCache.PAYMENT_TO_TYPE);
			lookupData.put(CodeLookupCache.PAYMENT_TO_TYPE, codeList);
			form.setLookupData(lookupData);
			
	 	}else {

			Collection<DeCodeVO> codeList = new ArrayList<DeCodeVO>();
			if (lookupData.containsKey(CodeLookupCache.PAYMENT_TO_TYPE)) {
				DeCodeVO vo = null;
				String code = "";
				String desc = "";
				Collection<DeCodeVO> irsCode = (Collection<DeCodeVO>) lookupData.get(CodeLookupCache.PAYMENT_TO_TYPE);
				for (DeCodeVO irs : irsCode) {
					code = irs.getCode().trim();
					desc = irs.getDescription();
					if ((!code.toString().equals("PI")) && (!code.toString().equals("PP")) && (!code.toString().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION))) {					
							codeList.add(new DeCodeVO(irs.getCode(), irs.getDescription()));
					} 

				}

			}
			lookupData.remove(CodeLookupCache.PAYMENT_TO_TYPE);
			lookupData.put(CodeLookupCache.PAYMENT_TO_TYPE, codeList);
			form.setLookupData(lookupData);
			
	 	
	 	}
	 }
public void getSavedValuesForMultipayee(WithdrawalRequest request){
		 
		 String taxesFlag ;
		 String payeeDetails;
		 List <MultiPayeeTaxes> payeeList = new ArrayList();
		 MultiPayeeTaxes multipayee = null;
		 for(final Recipient recipient: request.getRecipients()){
	    		
	    		for (final Payee payee : recipient.getPayees()){
	    			
	    			taxesFlag = payee.getTaxes();
	    			payeeDetails = payee.getParticipant();
	    			if(payee.getPaymentInstruction().getPaymentAmount() != null){
	    				request.setPayDirectlyTomeAmount(payee.getPaymentInstruction().getPaymentAmount());
	    			}
	    			multipayee = new MultiPayeeTaxes(); 
	    			 if((payeeDetails != null && payeeDetails.trim().length()>0 ) && (taxesFlag != null && taxesFlag.trim().length()>0)){
	    		        	payeeDetails = payeeDetails.substring(1,payeeDetails.length() - 1);
	    		      		 String payeeDetail = payeeDetails.replaceAll("\"", "");
	    		      		 String newPayee[] = payeeDetail.split(":");
	    		      		 multipayee.setPayee(newPayee[0]);
	    		      		 taxesFlag = taxesFlag.substring(1,taxesFlag.length() - 1);
	    		    		 String newtaxFlag = taxesFlag.replaceAll("\"", ""); 
	    		    		 String newFlag[] = newtaxFlag.split(",");  
	    		    		 for( String tax1 :newFlag) 
	    		    		 { 
	    		    			 if(tax1.trim().equals("Non_Taxable:Y")) {
	    		    				 multipayee.setNonTaxable("Y");
	    		    			 }
	    		    			 if(tax1.trim().equals("Taxable:Y")) {
	    		    				 multipayee.setTaxable("Y");
	    		    			 }
	    		    			 if(tax1.trim().equals("Roth_Non_Tax:Y")) {
	    		    				 multipayee.setRothNonTax("Y");
	    		    			 }
	    		    			 if(tax1.trim().equals("Roth_Taxable:Y")) {
	    		    				 multipayee.setRothTaxable("Y");
	    		    			 }
	    		    			 if(tax1.trim().equals("Roth_IRA:Y")) {
	    		    				 multipayee.setRothIRA("Y");
	    		    			 }
	    		    		 }
	    		        }
	    			 payeeList.add(multipayee);
	    		}
	    	
	    	}
		 if(payeeList != null){
		 for(MultiPayeeTaxes mpayee : payeeList){

			 
				if( mpayee.getPayee() != null && mpayee.getPayee().equals("RI")) {
					if(mpayee.getTaxable().equals("Y") && mpayee.getNonTaxable().equals("Y") ){
						request.setTaxableParticipantInfo("TIRA");
						request.setNonTaxableParticipantInfo("TIRA");
					}
					else if (mpayee.getTaxable().equals("Y")) {
						request.setTaxableParticipantInfo("TIRA");
					}else if (mpayee.getNonTaxable().equals("Y")) {
						request.setNonTaxableParticipantInfo("TIRA");
					}
				}else if (mpayee.getPayee() != null && mpayee.getPayee().equals("RR")) {
					if(mpayee.getRothIRA().equals("Y") && mpayee.getRothTaxable().equals("Y") && mpayee.getTaxable().equals("Y") && mpayee.getNonTaxable().equals("Y") && mpayee.getRothNonTax().equals("Y"))
					{
						request.setRothParticaipantInfo("RIRA");
						request.setTaxableParticipantInfo("RIRA");
						request.setNonTaxableParticipantInfo("RIRA");
					}else if(mpayee.getRothIRA().equals("Y") && mpayee.getRothTaxable().equals("Y") && mpayee.getTaxable().equals("Y") ){
						request.setRothParticaipantInfo("RIRA");
						request.setTaxableParticipantInfo("RIRA");
					}else if (mpayee.getNonTaxable().equals("Y") && mpayee.getRothIRA().equals("Y")  && mpayee.getRothNonTax().equals("Y")){
						request.setRothParticaipantInfo("RIRA");
						request.setNonTaxableParticipantInfo("RIRA");
					}
					else if (mpayee.getNonTaxable().equals("Y") && mpayee.getTaxable().equals("Y") && mpayee.getRothIRA().equals("Y") ){
						request.setTaxableParticipantInfo("RIRA");
						request.setNonTaxableParticipantInfo("RIRA");
					}
					else if(mpayee.getRothIRA().equals("Y") && mpayee.getRothTaxable().equals("Y") ) {
						request.setRothParticaipantInfo("RIRA");
					}else if (mpayee.getTaxable().equals("Y") && mpayee.getRothIRA().equals("Y")) {
						
						request.setTaxableParticipantInfo("RIRA");
					}else {
						request.setNonTaxableParticipantInfo("RIRA");
					}
					
				}else if (mpayee.getPayee() != null && mpayee.getPayee().equals("EP")) {
					if(mpayee.getTaxable().equals("Y") && mpayee.getRothTaxable().equals("Y")&& mpayee.getRothNonTax().equals("Y") && mpayee.getNonTaxable().equals("Y")){
						request.setTaxableParticipantInfo("EQP");
						request.setRothParticaipantInfo("EQP");
						request.setNonTaxableParticipantInfo("EQP");
					} else if(mpayee.getTaxable().equals("Y") && mpayee.getRothTaxable().equals("Y")){
						request.setTaxableParticipantInfo("EQP");
						request.setRothParticaipantInfo("EQP");
					} else if(mpayee.getRothTaxable().equals("Y")&& mpayee.getNonTaxable().equals("Y")){
						request.setRothParticaipantInfo("EQP");
						request.setNonTaxableParticipantInfo("EQP");
					} else if(mpayee.getTaxable().equals("Y") && mpayee.getNonTaxable().equals("Y") ){
						request.setTaxableParticipantInfo("EQP");
						request.setNonTaxableParticipantInfo("EQP");
					}
					else if(mpayee.getTaxable().equals("Y"))
					{
						request.setTaxableParticipantInfo("EQP");
					}else if (mpayee.getRothTaxable().equals("Y")|| mpayee.getRothNonTax().equals("Y")) {
						request.setRothParticaipantInfo("EQP");
					}else {
						request.setNonTaxableParticipantInfo("EQP");
					}
				}else if(mpayee.getPayee() != null && mpayee.getPayee().equals("PA")){
					if (mpayee.getNonTaxable().equals("N") && mpayee.getTaxable().equals("N")&& mpayee.getRothIRA().equals("N") && mpayee.getRothTaxable().equals("N") && mpayee.getRothNonTax().equals("N") ) {
						request.setParticipantDetails("PA");;
						}
						else if(mpayee.getRothTaxable().equals("Y") && mpayee.getRothNonTax().equals("Y")){
						request.setParticipantDetails("PAR");
						request.setTotalRothBalFlag(true);
						}else if((mpayee.getNonTaxable().equals("Y") || mpayee.getNonTaxable().equals("N")) && (mpayee.getRothNonTax().equals("Y") || mpayee.getRothNonTax().equals("N"))){
						request.setParticipantDetails("PAAT");
						request.setNonTaxableFlag(true);
						}
						}
			 
		 }
		 }
	 }

}
	




	


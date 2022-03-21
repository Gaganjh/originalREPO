package com.manulife.pension.bd.web.fundEvaluator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.service.fund.valueobject.LookupDescription;
import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.AssetClassForInvOption;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolGlobalData;
import com.manulife.pension.bd.web.fundEvaluator.common.FundForInvOption;
import com.manulife.pension.bd.web.fundEvaluator.processor.CoreToolHelper;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.BrokerEntityExtendedProfile;
import com.manulife.pension.service.broker.valueobject.NFACodeConstants;
import com.manulife.pension.service.broker.valueobject.impl.BrokerDealerFirmImpl;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.ContractFund;
import com.manulife.pension.service.fund.valueobject.Fund;
import com.manulife.pension.service.fund.valueobject.FundClassVO;
import com.manulife.pension.service.fund.valueobject.FundFamilyVO;
import com.manulife.pension.service.fund.valueobject.SvgifFund;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerAssistantUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.BDFirmRep;

public class FundEvaluatorActionHelper {
    
    /**
     * Updates total selected funds from the all asset classes.
     * @param request
     * @return
     */
    public static void updateTotalFundsSelected(HttpServletRequest request, FundEvaluatorForm ievaluatorForm) {
        int totalFundsSelected = 0;
        
        Hashtable<String,Collection<String>> selectedAssetClsFunds = FundEvaluatorActionHelper.getUserSelectedAssetClsFundsFromSession(request, false);
       
            if(selectedAssetClsFunds != null) {
                Iterator<String> iteratorAssetClassId = selectedAssetClsFunds.keySet().iterator();
                for( ;iteratorAssetClassId.hasNext(); ) {
                    String assetClsId = iteratorAssetClassId.next();
                    Collection<String> fundsSelected = (Collection<String>)selectedAssetClsFunds.get(assetClsId);
                    int totalAssetClassFundsSelected = 0;
                    if(fundsSelected != null) {
                        totalAssetClassFundsSelected = fundsSelected.size();
                    }
                    totalFundsSelected +=  totalAssetClassFundsSelected;
                    ievaluatorForm.getAssetClassDetails().get(assetClsId).setTotalFundsSelected(totalAssetClassFundsSelected);
                }
             }
       
         ievaluatorForm.setTotalSelectedFunds(totalFundsSelected);
    }
    
    /**
     * Returns the user selected fund id's from the user session.
     * @param request
     * @return
     */
    public static Hashtable<String, Collection<String>> getUserSelectedAssetClsFundsFromSession(HttpServletRequest request, boolean resetSelectedFunds) {
        
        HttpSession session = request.getSession();
        Hashtable<String, Collection<String>> selectedAssetClsFunds;
        
        if (session.getAttribute("SELECTED_ASSET_FUNDS") == null || resetSelectedFunds) {
            selectedAssetClsFunds = new Hashtable<String, Collection<String>>();
            session.setAttribute("SELECTED_ASSET_FUNDS", selectedAssetClsFunds);
        } else {
            selectedAssetClsFunds = (Hashtable<String,Collection<String>>) session
                    .getAttribute("SELECTED_ASSET_FUNDS");
        }
        
        return selectedAssetClsFunds;
    }
    
    /**
     * Updates the user selected funds information in the asset class list to display on overlay 
     * @param assetClassForInvOptionList
     * @param fundEvaluatorForm
     * @param request
     */
    public static void updateUserSelectedFundsInAssetClsFunds(ArrayList<AssetClassForInvOption> assetClassForInvOptionList, FundEvaluatorForm fundEvaluatorForm, HttpServletRequest request) {
        
        // If resetFilters is "N", requesting for filter options and user selected/de-selected funds should not wipe out. 
        String resetFilters = (String)request.getParameter(FundEvaluatorConstants.RESET_FILTERS);
        
        if(StringUtils.equalsIgnoreCase(resetFilters, BDConstants.NO)) {
            // Get user selected funds from the form which is not yet updated in the session
            String[] userSelectedAssetClsFunds = (String[])fundEvaluatorForm.getSelectedFunds(); // value format will be "AssetclassId-FundId-boolean(isOptinalFund)". eg: LCV-REV-true for a optional fund
            MultiValueMap userSelectedAssetClsFundsMap = getSelectedFundsMap(userSelectedAssetClsFunds);
            // If user selected any filter option from the overlay
            // Update the funds from the recently selected/de-selceted but not updated in the session
            for(AssetClassForInvOption assetClassForInvOption:assetClassForInvOptionList) {    
                Collection<String> selectedFundIds = userSelectedAssetClsFundsMap.getCollection(assetClassForInvOption.getId());
                updateFundSelectionStatus(assetClassForInvOption, selectedFundIds);
            }           
        } else {
            Hashtable<String, Collection<String>> selectedAssetClsFundsInSession = getUserSelectedAssetClsFundsFromSession(request, false);
            // if resetFilters value is NOT 'N', displaying the overlay from the step4, then will selecte the funds from the session.
          for(AssetClassForInvOption assetClassForInvOption:assetClassForInvOptionList) {   
                Collection<String> selectedFundIdsInSession = selectedAssetClsFundsInSession.get(assetClassForInvOption.getId());
                updateFundSelectionStatus(assetClassForInvOption, selectedFundIdsInSession);
            }
        }
    }
    
    /**
     * Updates the user selected funds information in the asset class
     * @param assetClassForInvOption
     * @param selectedFundIds
     */
    public static void updateFundSelectionStatus(AssetClassForInvOption assetClassForInvOption, Collection<String> selectedFundIds ) {
        for(FundForInvOption fundForInvOption : assetClassForInvOption.getFundForInvOptionList()) {
            if(selectedFundIds != null && selectedFundIds.contains(fundForInvOption.getFund().getFundId())) {
                fundForInvOption.setChecked(true);
            } else {
                fundForInvOption.setChecked(false);
            }
        }
    }
    
    /**
     * Update the tool selected funds in user session context.
     * @param assetClassForInvOptionList
     * @param selectedAssetClsFundIds
     */
    public static void updateToolSelectedFundsInSession(ArrayList<AssetClassForInvOption> assetClassForInvOptionList, Hashtable<String, Collection<String>> selectedAssetClsFundIds) {
        // Update the baseline up fund Id's in the session context of the user. 
        for(AssetClassForInvOption assetClsInvOption:assetClassForInvOptionList) {
            Collection<String> fundIdList = null;
            if(!selectedAssetClsFundIds.contains(assetClsInvOption.getId())) {
                // If the specified asset class Id is not added yet to the session context object 
                fundIdList = new ArrayList<String>();
                selectedAssetClsFundIds.put(assetClsInvOption.getId(), fundIdList);
            } else {
                // If already the asset class funds are included in the session context object.
                fundIdList = selectedAssetClsFundIds.get(assetClsInvOption.getId());
            }
            
            for(FundForInvOption fundForInvOption : assetClsInvOption.getFundForInvOptionList()) {
                if(fundForInvOption.isChecked()) {
                    // If fund is checked, It will be added to the user session context.
                    fundIdList.add(fundForInvOption.getFund().getFundId());
                }
            }
        }
    }
   
    /**
     * Returns user selected funds in Map object with key as asset class Id and value as fund Id list.
     * @param selectedAssetFunds
     * @return
     */
    public static MultiValueMap getSelectedFundsMap(String[] selectedAssetFunds) {
        
        MultiValueMap selectedFunds = new MultiValueMap(); 
        if (selectedAssetFunds != null) {
            for (String assetClassFundName :selectedAssetFunds) {
                String[] tokens = StringUtils.split(assetClassFundName, BDConstants.HYPHON_SYMBOL);
                selectedFunds.put(tokens[0], tokens[1]);
            }
        }
        
        return selectedFunds;
    }

    /**
     * Returns true if user requested for preview page.
     * @param assetClassName
     * @return
     */
    public static boolean isPreview(String assetClassName) {
        boolean preview = false;
        
        if(FundEvaluatorConstants.PREVIEW_FUNDS.equalsIgnoreCase(assetClassName) ) {
            preview = true;
        }
        
        return preview; 
    }
    
    /**
     * To update the funds selected by the user for a selected asset class. It will keep refresh on
     * every update
     * 
     * @param ievaluatorForm
     * @param request
     */
     public static void updateUserSelectedFundsInSession(
             FundEvaluatorForm ievaluatorForm, HttpServletRequest request) throws SystemException {

         Hashtable<String, Collection<String>> selectedAssetClsFunds = getUserSelectedAssetClsFundsFromSession(request, false);
         
         String[] userUpdatedAssetClsFunds = (String[])ievaluatorForm.getSelectedFunds(); // value format will be "AssetclassId-FundId-boolean(isOptinalFund)". eg: LCV-REV-true for a optional fund
         MultiValueMap updatedAssetClsFundsMap = getSelectedFundsMap(userUpdatedAssetClsFunds);
         
         if(FundEvaluatorActionHelper.isPreview(ievaluatorForm.getAssetClassId()) ) {
             
             if (selectedAssetClsFunds != null) {
                 Iterator<String> assetClassIdsIterator = selectedAssetClsFunds.keySet().iterator();
                 for ( ;assetClassIdsIterator.hasNext(); ) {
                     String assetClsId = assetClassIdsIterator.next();
                     Collection<String> fundsSelected = (Collection<String>)selectedAssetClsFunds.get(assetClsId);
                     if (updatedAssetClsFundsMap.containsKey(assetClsId)) {
                         Collection<String> updatedFundIds = updatedAssetClsFundsMap.getCollection(assetClsId);
                         selectedAssetClsFunds.put(assetClsId, updatedFundIds);
                    } else {
                        fundsSelected.clear();
                    }
                  }
             }
                 
         } else {
              String assetClassId = ievaluatorForm.getAssetClassId();
             
              if(isSvfFundSelected(updatedAssetClsFundsMap)) {
                  // if SVF fund selected in the asset class, deselect all the competing funds from other asset classes.
                  // Note: SVF competing funds will be deselected from the same asset class from front end(through Javascript)
                  deselectAllSvfCompetingFunds(selectedAssetClsFunds);
              } else if(isSvfCompetingFundSelected(updatedAssetClsFundsMap)){
                  // if SVF competing fund selected in the asset class, deselect all the SVF funds from other asset classes
                  // Note: SVF funds will be deselected from the same asset class from front end(through Javascript)
                  deselectAllSvfFunds(selectedAssetClsFunds);
              }
              // LSF & LCF asset class funds updated.
              if(FundEvaluatorConstants.ASSET_CLASS_ID_LIFECYCLE_AND_LIFESTYLE.equalsIgnoreCase(assetClassId)) {
                  if( updatedAssetClsFundsMap.getCollection(FundEvaluatorConstants.ASSET_CLASS_ID_LIFECYCLE) != null) {
                      selectedAssetClsFunds.put(FundEvaluatorConstants.ASSET_CLASS_ID_LIFECYCLE, updatedAssetClsFundsMap.getCollection(FundEvaluatorConstants.ASSET_CLASS_ID_LIFECYCLE));
                  } else {
                      selectedAssetClsFunds.get(FundEvaluatorConstants.ASSET_CLASS_ID_LIFECYCLE).clear();
                  }
                  if(updatedAssetClsFundsMap.getCollection(FundEvaluatorConstants.ASSET_CLASS_ID_LIFESTYLE) != null){
                      selectedAssetClsFunds.put(FundEvaluatorConstants.ASSET_CLASS_ID_LIFESTYLE, updatedAssetClsFundsMap.getCollection(FundEvaluatorConstants.ASSET_CLASS_ID_LIFESTYLE));
                  } else {
                      selectedAssetClsFunds.get(FundEvaluatorConstants.ASSET_CLASS_ID_LIFESTYLE).clear();
                  }
                  if(updatedAssetClsFundsMap.getCollection(FundEvaluatorConstants.ASSET_CLASS_ID_GIFL_SELECT) != null){
                      selectedAssetClsFunds.put(FundEvaluatorConstants.ASSET_CLASS_ID_GIFL_SELECT, updatedAssetClsFundsMap.getCollection(FundEvaluatorConstants.ASSET_CLASS_ID_GIFL_SELECT));
                  } else if(selectedAssetClsFunds.get(FundEvaluatorConstants.ASSET_CLASS_ID_GIFL_SELECT) != null){
                      selectedAssetClsFunds.get(FundEvaluatorConstants.ASSET_CLASS_ID_GIFL_SELECT).clear();
                  }
              } else {
                  if(updatedAssetClsFundsMap.getCollection(assetClassId) != null){
                      selectedAssetClsFunds.put(assetClassId, updatedAssetClsFundsMap.getCollection(assetClassId));
                  } else {
                      selectedAssetClsFunds.get(assetClassId).clear();
                  }
              }
        }
     }
     
     /**
      * Returns true, if atleast one SVF fund has selected from the detailed overlay 
      * @param selectedFundIds
      * @return
      */
     public static boolean isSvfFundSelected(MultiValueMap updatedAssetClsFundsMap) {
         boolean svfFundSelected = false;
         
         Collection<String> stableValueFunds = CoreToolGlobalData.defaultSelectedStableValueFunds;
         Collection<String> selectedFundIds = updatedAssetClsFundsMap.values();
         
         for(String fundId : stableValueFunds){
             if (selectedFundIds != null && selectedFundIds.contains(fundId)) {
                 svfFundSelected = true;
                 break;
             }
         }
         
         return svfFundSelected;
     }
         
     /**
      * Checks any SVF or SVF competing fund selected by user or in base line funds and updates the form fields
      * 
      * @param request
      * @param fundEvaluatorForm
      * @throws SystemException
      */
     public static void checkStableValueAndCompetingFundRule(HttpServletRequest request, FundEvaluatorForm fundEvaluatorForm) throws SystemException {
         boolean svfFundSelected = false;
         boolean svfCompetingSelected = false;
         
         // Stable value funds
         Collection<String> stableValueFunds = CoreToolGlobalData.defaultSelectedStableValueFunds;
         // Stable value competing funds
         Collection<String> svfCompetingFunds = FundServiceDelegate.getInstance().getSVFCompetingFunds(Calendar.getInstance().getTime());         // so far selected funds by user
         Hashtable<String, Collection<String>> userSelectedAssetClassFundsInSession = getUserSelectedAssetClsFundsFromSession(request, false);
          
         // set default to false for the form bean properties
         fundEvaluatorForm.setSelectedAnySVF(false);
         fundEvaluatorForm.setSelectedAnySVFCompeting(false);
         
         for(Collection<String> assetClass : userSelectedAssetClassFundsInSession.values()){
             for(String fundId: assetClass){
                 if(stableValueFunds.contains(fundId)) {
                     svfFundSelected = true;
                     fundEvaluatorForm.setSelectedAnySVF(true);
                     break; // break for the inner loop
                 } else if(svfCompetingFunds.contains(fundId)){
                     svfCompetingSelected = true;
                     fundEvaluatorForm.setSelectedAnySVFCompeting(true);
                     break; // break for the inner loop
                 }
             }
             if(svfFundSelected || svfCompetingSelected) {
                 break; // break for the outer loop
             } 
         }
     }
     
     /**
      * Returns true, if atleast one SVF competing fund has selected from the detailed overlay.
      * @param selectedFundIds
      * @return
      */
     public static boolean isSvfCompetingFundSelected(MultiValueMap updatedAssetClsFundsMap) throws SystemException {
         boolean svfCompetingFundSelected = false;
         
             Collection<String> svfCompetingFunds = FundServiceDelegate.getInstance().getSVFCompetingFunds(Calendar.getInstance().getTime());
             Collection<String> selectedFundIds = updatedAssetClsFundsMap.values();
             for(String fundId: svfCompetingFunds) {
                 if (selectedFundIds != null && selectedFundIds.contains(fundId)) {
                     svfCompetingFundSelected = true;
                     break;
                 }
             }
         
         return svfCompetingFundSelected;
     }
     
     /**
      * Deselect SVF funds from the user selected funds (user session object)
      * @param selectedAssetClsFunds
      */
     public static void deselectAllSvfFunds(Hashtable<String, Collection<String>>selectedAssetClsFunds){
         Collection<String> stableValueFunds = CoreToolGlobalData.defaultSelectedStableValueFunds;

         if (stableValueFunds != null && stableValueFunds.size() > 0){
             for (String assetClassId : selectedAssetClsFunds.keySet()) {
                 selectedAssetClsFunds.get(assetClassId).removeAll(stableValueFunds);
             }
         }
    }
    
     /**
      * Deselect SVF competing funds from the user selected funds (user session object)
      * @param selectedAssetClsFunds
      */
     public static void deselectAllSvfCompetingFunds(Hashtable<String, Collection<String>>selectedAssetClsFunds) throws SystemException {
         
             Collection<String> svfCompetingFunds = FundServiceDelegate.getInstance().getStableValueFunds().keySet();
             if (svfCompetingFunds != null && svfCompetingFunds.size() > 0) {
                 for (String assetClassId : selectedAssetClsFunds.keySet()) {
                     selectedAssetClsFunds.get(assetClassId).removeAll(svfCompetingFunds);
                 }
             }
     }
     
     /**
      * Returns the Asset class details for specified asset class.
      * @param assetClassId
      * @param assetClassForInvOptionList
      */
     public static AssetClassForInvOption getAssetClassForInvOption(String assetClassId,
            ArrayList<AssetClassForInvOption> assetClassForInvOptionList) {
         
        AssetClassForInvOption assetClassForInvOption = null;
        if (assetClassForInvOptionList != null && !assetClassForInvOptionList.isEmpty()) {
            for (AssetClassForInvOption assetClass : assetClassForInvOptionList) {
                if (assetClass.getId().equalsIgnoreCase(assetClassId)) {
                    assetClassForInvOption = assetClass;
                    break;
                }
            }
        }

        return assetClassForInvOption;
    }
     
     /**
      * Returns only the selected funds investment options.
      * Generates the selected investment options using the user selected funds from the form
      * The user request for filter option 'only selected funds', funds information will be taken from the form.
      * @param ievaluatorForm
      * @param request
      * @return ArrayList<AssetClassForInvOption>
      */
     public static ArrayList<AssetClassForInvOption> getSelectedAssetClsFundsInvOptionList(FundEvaluatorForm fundEvaluatorForm, HttpServletRequest request) throws SystemException {
         
          // Asset class base line funds.
          ArrayList<AssetClassForInvOption> assetClsForInvOptionList = FundEvaluatorUtility.getInvestmentOptionDetails(fundEvaluatorForm, request);
          
          String resetFilters = (String)request.getParameter(FundEvaluatorConstants.RESET_FILTERS);
          if(StringUtils.equalsIgnoreCase(resetFilters, BDConstants.NO)) {
              // if user selected any filter options from the overlay like radio buttons or sort option, select the user selected funds from the Form
              MultiValueMap userSelectedAssetClsFundsMap = getSelectedFundsMap(fundEvaluatorForm.getSelectedFunds());

              if(userSelectedAssetClsFundsMap != null && userSelectedAssetClsFundsMap.size() > 0) {
                 // If user has already selected funds and selected the filter options
                  for(AssetClassForInvOption assetClsForInvOption : assetClsForInvOptionList ) {
                  	List<FundForInvOption> fundForInvOptionList = assetClsForInvOption.getFundForInvOptionList();
                      if(fundForInvOptionList != null && fundForInvOptionList.size() > 0) {
                          // If the asset class funds list not null and has at least one fund in the base line up funds
                          Collection<String> userSelectedFundIds = (Collection<String>)userSelectedAssetClsFundsMap.get(assetClsForInvOption.getId());
                          if(userSelectedFundIds != null && userSelectedFundIds.size() > 0){
                           // If the asset class funds list not null and has at least one fund was selected 
                              ArrayList<FundForInvOption> selectedFundforInvOptionList = new ArrayList<FundForInvOption>();
                              for(FundForInvOption fundForInvOption : assetClsForInvOption.getFundForInvOptionList()){
                                  if(userSelectedFundIds.contains(fundForInvOption.getFund().getFundId())) {
                                      // If fund id is available in the user selected fund list, add to the asset class fund
                                      // investment also as checked
                                      fundForInvOption.setChecked(true);
                                      selectedFundforInvOptionList.add(fundForInvOption);
                                  }
                              }
                              assetClsForInvOption.setFundForInvOptionList(selectedFundforInvOptionList);
                          } else {
                              fundForInvOptionList.clear();
                          }
                      }
                  }
               } else {
                   // if there is NO fund selected or all funds unselected and user selected the "Only selected funds" option list. 
                   for(AssetClassForInvOption assetClsForInvOption : assetClsForInvOptionList ) {
                       List<FundForInvOption> fundForInvOptionList = assetClsForInvOption.getFundForInvOptionList();
                       if(fundForInvOptionList != null && fundForInvOptionList.size() > 0) {
                           fundForInvOptionList.clear();
                       }
                   }
               }
          } 
          
          return assetClsForInvOptionList;
     }
     
    /**This method is used to determine whether to display firm filter dropdown or not 
     * @param ievaluatorForm
     * @param request
     * @return boolean
     */
    public static boolean isShowFirmFilter(FundEvaluatorForm ievaluatorForm,HttpServletRequest request){
        
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        boolean showFirmFilter = false;
        
        if(!userProfile.isInMimic()){
            if(BDUserRoleType.InternalBasic.equals(userProfile.getBDPrincipal().getBDUserRole().getRoleType())
                    ||BDUserRoleType.Administrator.equals(userProfile.getBDPrincipal().getBDUserRole().getRoleType())
                            ||BDUserRoleType.NationalAccounts.equals(userProfile.getBDPrincipal().getBDUserRole().getRoleType())
                            ||BDUserRoleType.ContentManager.equals(userProfile.getBDPrincipal().getBDUserRole().getRoleType())
                            ||BDUserRoleType.RVP.equals(userProfile.getBDPrincipal().getBDUserRole().getRoleType())){
                
                showFirmFilter = true; 
            }
        }
        return showFirmFilter;
    }
    
    /**
     * This method gives back a list of uniquely named Firm Names for a given User. The user should
     * be a BDFirmRep user.
     * 
     * @param userProfile - BDFirmRepUserProfile.
     * @return - Arraylist of Firm Names associated to the user profile.
     */
    public static ArrayList<String> getAssociatedFirmNamesForFirmRep(BDUserProfile userProfile) {
        List<BrokerDealerFirm> bdFirms = ((BDFirmRep) userProfile.getRole())
                .getBrokerDealerFirmEntities();
        ArrayList<String> firmNames = new ArrayList<String>();
        if (bdFirms != null) {
            for (BrokerDealerFirm bdFirm : bdFirms) {
                if (!firmNames.contains(bdFirm.getFirmName())) {
                    firmNames.add(bdFirm.getFirmName());
                }
            }
        }

        return firmNames;
    }
    
    /**This method gives the first firm name from the list of firm names associated to
     * a given user. 
     * @param userProfile
     * @return
     */
    public static String getAssociatedFirstFirmNameForFirmRep(BDUserProfile userProfile){    
    	String firmName = "";
    	List firmNames = getAssociatedFirmNamesForFirmRep(userProfile);    	
    	if(firmNames != null && firmNames.size()!= 0){
    		firmName = (String)firmNames.get(0);    		
    	}
    	return firmName;
    }
    
    /**This method is used to populate the default values while loading the 
     * create report page initially.
     * @param ievaluatorForm
     * @param request
     * @throws SystemException 
     * @throws SecurityServiceException 
     */
    public static void populateDefaultValuesForCustomizeReport(FundEvaluatorForm ievaluatorForm,HttpServletRequest request) throws SecurityServiceException, SystemException{
    	
    	BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
    	if(StringUtils.isBlank(ievaluatorForm.getFundListRiskOrderOrAssetClass())) {
    		ievaluatorForm.setFundListRiskOrderOrAssetClass(FundEvaluatorConstants.FUNDLIST_ASSET_CLASS);
    	}
    	
    	// always include the ranking section
    	ievaluatorForm.setIncludedOptItem4("true");
    	if(StringUtils.isBlank(ievaluatorForm.getFundRankingMethodology())) {
    		ievaluatorForm.setFundRankingMethodology("selected");
    	}
    	
    	
    	String firmName = "";
    	//If the user had selected existing client button
    	if(StringUtils.isNotEmpty(ievaluatorForm.getContractNumber())){
    		ievaluatorForm.setCompanyName(ievaluatorForm.getCompanyName());
    	}    	
    	String firstName = userProfile.getBDPrincipal().getFirstName();
    	String lastName = userProfile.getBDPrincipal().getLastName();
    	
    	//populate "Your Name Text box" with first and last name combination at very first time. After that
    	// the updated value from step 5 will be populated in the yourName field.
    	if(ievaluatorForm.getYourName()== null){
    		ievaluatorForm.setYourName(firstName+" "+lastName);
    	}
    	
    	//populate "Your Firm Text box" only on first time.
    	if(ievaluatorForm.getYourFirm()==null){ 
	    	if(BDUserProfileHelper.isInternalUser(userProfile)){
	    		//set Blank.
	    		ievaluatorForm.setYourFirm("");    		
	    	}
	    	else if(BDUserProfileHelper.isFirmRep(userProfile)){	    		    		
	    		ievaluatorForm.setYourFirm(FundEvaluatorActionHelper.
	    				getAssociatedFirstFirmNameForFirmRep(userProfile)); 
	    	}
	    	else if(BDUserProfileHelper.isFinancialRep(userProfile)){  
				ExtendedBrokerUserProfile broker = 
					(ExtendedBrokerUserProfile) BDUserSecurityServiceDelegate
					.getInstance().getExtendedBDExtUserProfile(userProfile.getBDPrincipal());					
	    		
	    		List<BrokerDealerFirm> firms = broker.getBrokerDealerFirms();
	    		if(firms != null && firms.size()>0){
	    			BrokerDealerFirm firm = (BrokerDealerFirm)firms.get(0);
	    			firmName = firm.getFirmName() ;
	    			ievaluatorForm.setYourFirm(firmName);
	    		}
	    		//if no firm name populate with company name
	    		else {
	    			BrokerEntityExtendedProfile brokerEntityExtendedProfile =
						(BrokerEntityExtendedProfile)broker.getPrimaryBrokerEntityProfile();						
	    			ievaluatorForm.setYourFirm(brokerEntityExtendedProfile.getOrgName());	    			
	    		}
	    		
	    	}
	    	//any assistant role.    	
	    	else if(BDUserProfileHelper.isFinancialRepAssistant(userProfile)){  		
				ExtendedBrokerAssistantUserProfile brokerAssistant = 
					(ExtendedBrokerAssistantUserProfile) BDUserSecurityServiceDelegate
					.getInstance().getExtendedBDExtUserProfile(userProfile.getBDPrincipal());
				
				List assistantList = brokerAssistant.getParentBroker().getBrokerDealerFirms();	
				if(assistantList != null && assistantList.size()>0){
						firmName = ((BrokerDealerFirmImpl) assistantList.get(0)).getFirmName();		
			    		ievaluatorForm.setYourFirm(firmName);
				}
	    	}
    	}
    	
    }
    /**
     * This method restore the default values for the criteria selection page.
     * @param ievaluatorForm
     * @param request
     */
    public static void criteriaSelectionRestoreDefaultValues(FundEvaluatorForm ievaluatorForm,HttpServletRequest request){
    	ArrayList<CriteriaVO> criteriaVO = (ArrayList<CriteriaVO>) ievaluatorForm.getTheItemList();
        ArrayList<CriteriaVO> criteriaVONew = new ArrayList<CriteriaVO>();

        if (criteriaVO.isEmpty()) {
            ArrayList<CriteriaVO> criteriaInformation = FundEvaluatorUtility.createCriteriaInfo(); 
            criteriaVO.addAll(criteriaInformation);
        }
        request.setAttribute(BDConstants.CRITERIA_DETAILS, criteriaVO);
        
        ArrayList<CriteriaVO> criteriaInformation = FundEvaluatorUtility.createCriteriaInfo();
        criteriaVONew.addAll(criteriaInformation);
        ievaluatorForm.setTheItemList(criteriaVONew);
    }
    
    /**
     * This method helps to populate the default values for Narrow your list page.
     * @param ievaluatorForm
     * @throws SystemException 
     * @throws RemoteException 
     */
    public static void populateNarrowYourListWithDefaultValues(FundEvaluatorForm ievaluatorForm) throws SystemException, RemoteException{
    	
    	if(StringUtils.isNotBlank(ievaluatorForm.getContractNumber())){ 
    		String fundInvestmentId = StringUtils.EMPTY;
    		CoreToolHelper coreToolHelper = new CoreToolHelper();        	
    		Hashtable<String, ContractFund> fundHashTable = coreToolHelper.getContractFunds(Integer.parseInt(ievaluatorForm.getContractNumber()));
    		
    		boolean lifeStyleFundSelected = coreToolHelper.isContractHavingAnyLifeStyleFunds(fundHashTable);
    		
    		// Fetch Lifecycle& Lifestyle family suites from lookup
    		
    		Map<String, ArrayList<FundFamilyVO>> assetAndFundFamily= FundServiceDelegate.getInstance().getAssetAllocationOptions(Integer.valueOf(ievaluatorForm.getContractNumber()));
    		
    		ArrayList<FundFamilyVO> lifecycleFamilyList = assetAndFundFamily.get(FundEvaluatorConstants.ASSET_CLASS_ID_LIFECYCLE);
    		ArrayList<FundFamilyVO> lifestyleFamilyList = assetAndFundFamily.get(FundEvaluatorConstants.ASSET_CLASS_ID_LIFESTYLE);
    		
    		Set<String> merrillCoveredFunds = FundServiceDelegate.getInstance().getMerrillCoveredFundInvestmentIds();
        	Set<String> merrillCoveredFundFamilyGroups = FundServiceDelegate.getInstance().getMerrillCoveredFundFamilyCodes();
        	
        	lifecycleFamilyList = filterByMerrillCoveredFundFamilyList(ievaluatorForm, merrillCoveredFundFamilyGroups, lifecycleFamilyList);
        	lifestyleFamilyList = filterByMerrillCoveredFundFamilyList(ievaluatorForm, merrillCoveredFundFamilyGroups, lifestyleFamilyList);
    		
    		ievaluatorForm.setFundFamilyList(lifecycleFamilyList);
    		ievaluatorForm.setLifestyleFamilyList(lifestyleFamilyList);
    		
    		
			// contains MMf fund as key and value as True if contract has selected.
			Map<String, Boolean> contractSelectedMMFunds = new HashMap<String, Boolean>();
			// contains all the contract selected SVF and MMF funds that need to be displayed as selected or checked.
			Map<String, Boolean> contractSelectedSVFAndMMFFunds = new HashMap<String, Boolean>();

			List<String> selectSVFFunds = new ArrayList<String>();
			String[] selectMMFFunds = new String[] {};

			boolean svfSelected = coreToolHelper.isContractHavingAnyStableValueFunds(fundHashTable, selectSVFFunds);
			boolean mmfSelected = coreToolHelper.isContractHavingAnyMoneyMarketFunds(fundHashTable, contractSelectedMMFunds);
			// load contract selected MMF funds
			contractSelectedSVFAndMMFFunds.putAll(contractSelectedMMFunds);
   		
    		// collect Stable value funds
    		List<String> svfFundList = FundServiceDelegate.getInstance().getStableFundsByCompany(ievaluatorForm.getContractLocationId());
			svfFundList = filterByMerrillCoveredFundList(ievaluatorForm, merrillCoveredFunds, svfFundList, new HashSet<String>(selectSVFFunds));
    		
    		// retrieve the Fund details for the collected SVF funds
			Hashtable<String, Fund> svfFundTable = FundServiceDelegate.getInstance().getFundObjTableForFundList(svfFundList);
			
			ievaluatorForm.setSvfFundList(svfFundTable);
			ievaluatorForm.setSvfFundCodeList(svfFundList);
			// collect Money market funds
			List<String> mmfFundList = FundServiceDelegate.getInstance().getMoneyMarketFundsByCompany(ievaluatorForm.getContractLocationId());
			mmfFundList = filterByMerrillCoveredFundList(ievaluatorForm, merrillCoveredFunds, mmfFundList, contractSelectedMMFunds.keySet());
			// retrieve the Fund details for the collected MMF funds
			Hashtable<String, Fund> mmfFundTable = FundServiceDelegate.getInstance().getFundObjTableForFundList(mmfFundList);
			ievaluatorForm.setMmfFundList(mmfFundTable);
			ievaluatorForm.setMmfFundCodeList(mmfFundList);
			
    		String[] checkedFundSuites = ievaluatorForm.getLifecycleFundSuites();
    		ArrayList<String> selectedFundSuites = new ArrayList<String>();

    		// To add Contract selected funds suites
    		for(FundFamilyVO fundFamilyVO : lifecycleFamilyList){
    			if(fundFamilyVO.isContractSelected()){ 
    				selectedFundSuites.add(fundFamilyVO.getFamilyCode());
					
    			}
    		}
    		
    		// To add user checked funds suites, during back & forth navigation
    		if(checkedFundSuites != null){
	    		for(String familyCode : checkedFundSuites){
	    			if(!selectedFundSuites.contains(familyCode)){
	    				selectedFundSuites.add(familyCode);
	    			}
	    		}
    		}
    		
    		if(selectedFundSuites.size() > 0){
				ievaluatorForm.setLifecycleFundSuites(selectedFundSuites
								.toArray(new String[selectedFundSuites.size()]));
	    		
    		}
    		
    		String[] userSelectedLifeStyleSuites = ievaluatorForm.getLifestyleFundSuites();
    		ArrayList<String> selectLifeStyleSuites = new ArrayList<String>();

    		// To add Contract selected funds suites
    		for(FundFamilyVO fundFamilyVO : lifestyleFamilyList){
    			if(fundFamilyVO.isContractSelected()){ 
    				selectLifeStyleSuites.add(fundFamilyVO.getFamilyCode());
					
    			}
    		}
    		// To add user checked funds suites, during back & forth navigation
    		if(userSelectedLifeStyleSuites != null){
	    		for(String familyCode : userSelectedLifeStyleSuites){
	    			if(!selectLifeStyleSuites.contains(familyCode)){
	    				selectLifeStyleSuites.add(familyCode);
	    			}
	    		}
    		}
    		if(selectLifeStyleSuites.size() > 0){
				ievaluatorForm.setLifestyleFundSuites(selectLifeStyleSuites
								.toArray(new String[selectLifeStyleSuites.size()]));
    		}
    		
    		
    		//to load the user selected MMF funds while navigating to and forth
    		ArrayList<String> selectMmfFunds = new ArrayList<String>();
    		selectMmfFunds.addAll(contractSelectedMMFunds.keySet());
    		if(ievaluatorForm.getMoneyMarketFunds() != null ){
    			for(String mmfFund : ievaluatorForm.getMoneyMarketFunds()){
    				if(!selectMmfFunds.contains(mmfFund)){
    					selectMmfFunds.add(mmfFund);
    				}
    			}
    		}
    		
    		//if contract has selected any SVF fund.
    		if(svfSelected && selectSVFFunds != null  ){
    			if(selectSVFFunds.size() > 1){
    				fundInvestmentId = FundServiceDelegate.getInstance().getMostRecentStableValueFund(Integer.parseInt(ievaluatorForm.getContractNumber()));
    			}else{
    				fundInvestmentId = selectSVFFunds.get(0);
    			}
    			ievaluatorForm.setSVFSelected(svfSelected);
        		ievaluatorForm.setStableValueFunds(fundInvestmentId);
        		//for validation purpose.
        		contractSelectedSVFAndMMFFunds.put(fundInvestmentId, true);
        		ievaluatorForm.setCompulsoryFunds(FundEvaluatorConstants.COMPULSORY_FUND_SVF);
    		}
    		     
        	//if contract has selected any MMF fund.
        	if(mmfSelected){
        		ievaluatorForm.setMMFSelected(mmfSelected);
        		ievaluatorForm.setMoneyMarketFunds(selectMmfFunds.toArray(selectMMFFunds));
        		ievaluatorForm.setCompulsoryFunds(FundEvaluatorConstants.COMPULSORY_FUND_MMF);        	
        	} 
        	
        	if(svfSelected || mmfSelected){
        		ievaluatorForm.setContractSelectedSVFAndMMFFunds(contractSelectedSVFAndMMFFunds);
        	}
        	
        	if(!svfSelected && !mmfSelected){
        		ievaluatorForm.setCompulsoryFunds(FundEvaluatorConstants.EMPTY_STRING);
        	}
        }
        else{
        	// Fetch Lifecycle&Lifestyle family suites from lookup
        	// getAssetAllocationMap
        	Map<String, ArrayList<FundFamilyVO>> assetAndFundFamily= FundServiceDelegate.getInstance().getAssetAllocationOptions(0);
        	
        	Set<String> merrillCoveredFunds = FundServiceDelegate.getInstance().getMerrillCoveredFundInvestmentIds();
        	Set<String> merrillCoveredFundFamilyGroups = FundServiceDelegate.getInstance().getMerrillCoveredFundFamilyCodes();
    		
    		ArrayList<FundFamilyVO> lifecycleFamilyList = assetAndFundFamily.get(FundEvaluatorConstants.ASSET_CLASS_ID_LIFECYCLE);
    		ArrayList<FundFamilyVO> lifestyleFamilyList = assetAndFundFamily.get(FundEvaluatorConstants.ASSET_CLASS_ID_LIFESTYLE);
    		
    		lifecycleFamilyList = filterByMerrillCoveredFundFamilyList(ievaluatorForm, merrillCoveredFundFamilyGroups, lifecycleFamilyList);
    		lifestyleFamilyList = filterByMerrillCoveredFundFamilyList(ievaluatorForm, merrillCoveredFundFamilyGroups, lifestyleFamilyList);
        
    		ievaluatorForm.setFundFamilyList(lifecycleFamilyList);
    		ievaluatorForm.setLifestyleFamilyList(lifestyleFamilyList);
    		// Fetch SVF & MMG  Funds 
    		List<String> svfFundList = FundServiceDelegate.getInstance().getStableFundsByCompany(
					StringUtils.equals("USA", StringUtils.isNotBlank(ievaluatorForm.getFundUsa())?ievaluatorForm.getFundUsa().trim():StringUtils.EMPTY)? "019" : "094");
    		
    		if(BDConstants.CLASS_ZERO.equals(ievaluatorForm.getFundClassSelected()) || BDConstants.SIGNATURE_PLUS.equals(ievaluatorForm.getFundClassSelected())) {
    			List<String> svgiFunds = FundServiceDelegate.getInstance().getSVGIFFunds();
    			for (String svgiFund : svgiFunds) {
					if(svfFundList.contains(svgiFund)) {
						svfFundList.remove(svgiFund);
					}
				}
    		}
    		// fetch funds that are pending for state approval 
			ArrayList<String> statePendingApprovalfunds = FundServiceDelegate.getInstance().getStateApprovalPendingFunds(
					StringUtils.isNotBlank(ievaluatorForm.getStateSelected())?ievaluatorForm.getStateSelected().trim():StringUtils.EMPTY);
			// remove the funds that are pending for state approval 
    		svfFundList.removeAll(statePendingApprovalfunds);    		
			svfFundList = filterByMerrillCoveredFundList(ievaluatorForm, merrillCoveredFunds, svfFundList, null);					
            
			//US47378-SVF SIG - Fund Evaluator(New Plan - Generic)-If Class = Sig0 or Sig+ for both USA and NY Funds and state != "MD", 		
			if(!BDConstants.CLASS_ZERO.equals(ievaluatorForm.getFundClassSelected()) && !BDConstants.SIGNATURE_PLUS.equals(ievaluatorForm.getFundClassSelected()) || BDConstants.STATE_MARYLAND.equals(ievaluatorForm.getStateSelected()))
			{
				List<SvgifFund> defaultSvgifFunds = FundServiceDelegate.getInstance().getSVGIFDefaultFunds();
				
				for (SvgifFund defaultSvgifFund : defaultSvgifFunds) {
					String defaultSvgifFundId = defaultSvgifFund.getFundId();
					if(svfFundList.contains(defaultSvgifFundId))
						svfFundList.remove(defaultSvgifFundId);
				}
			}
    			
			Hashtable<String, Fund> svfFundTable = FundServiceDelegate.getInstance().getFundObjTableForFundList(svfFundList);
			ievaluatorForm.setSvfFundList(svfFundTable);
			ievaluatorForm.setSvfFundCodeList(svfFundList);
			
			List<String> mmfFundList = FundServiceDelegate.getInstance().getMoneyMarketFundsByCompany(
					StringUtils.equals("USA", StringUtils.isNotBlank(ievaluatorForm.getFundUsa())?ievaluatorForm.getFundUsa().trim():StringUtils.EMPTY) ? "019" : "094");
			mmfFundList = filterByMerrillCoveredFundList(ievaluatorForm, merrillCoveredFunds, mmfFundList, null);
			Hashtable<String, Fund> mmfFundTable = FundServiceDelegate.getInstance().getFundObjTableForFundList(mmfFundList);
			ievaluatorForm.setMmfFundList(mmfFundTable);
			ievaluatorForm.setMmfFundCodeList(mmfFundList);
			ievaluatorForm.setMMFSelected(false);
			ievaluatorForm.setSVFSelected(false);
	        	        	
        }
     }
    /**loads the drop downs  (fund class and fund menu)
     * @param ievaluatorForm
     * @throws SystemException
     */
    public static void loadFundClassAndFundMenu(FundEvaluatorForm ievaluatorForm) throws SystemException{
        if (ievaluatorForm.getFundClassesList().isEmpty()) {
            FundServiceDelegate delegate = FundServiceDelegate.getInstance();
            Map<String, FundClassVO> fundClassesMap = delegate.getAllFundClasses();
            ArrayList fundClassesList = FundEvaluatorUtility.getFundClasses(fundClassesMap);
            ievaluatorForm.setFundClassesList(fundClassesList);
        }
        ievaluatorForm.setFundMenuList(FundEvaluatorUtility.getFundMenuList());
            
    
    }

    /**loads the drop downs  (states)
     * @param ievaluatorForm
     * @throws SystemException
     */
    public static void loadStatesMenu(FundEvaluatorForm ievaluatorForm) throws SystemException{

        if (ievaluatorForm.getStatesList().isEmpty()) {
            EnvironmentServiceDelegate delegate = EnvironmentServiceDelegate.getInstance();
            Collection<DeCodeVO> statesCollection = delegate.getUsaIssueStates();
            ArrayList<LabelValueBean> statesList = FundEvaluatorUtility.getStatesMenuList(statesCollection);
            ievaluatorForm.setStatesList(statesList);
        }  
    }
    
    
    /**This method populates the list with a number of status 
     * @return
     */
    public static List<String> getContractStatusVerificationList(){
    	
    	List<String> contractStatusVerificationList = new ArrayList<String>();
    	
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_AC);
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_AP);
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_CA);
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_CP);
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_PS);    	
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_DC);
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_IP);
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_PA);
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_PC);
    	contractStatusVerificationList.add(FundEvaluatorConstants.CONTRACT_STATUS_PP);
    	
    	return contractStatusVerificationList;
    	
    }
    
    /** loads required data for select your client page.
	 * @param ievaluatorForm
	 * @param request
	 * @throws SystemException
	 */
	public static void populateNewClientInfromation(
			FundEvaluatorForm ievaluatorForm, HttpServletRequest request)
			throws SystemException {
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		
		// set as fund menu all
		ievaluatorForm.setFundMenuSelected(CoreToolConstants.FUND_MENU_ALL);
		
		//load fund class and fund menu list.
		loadFundClassAndFundMenu(ievaluatorForm);
		
		// load states list
		loadStatesMenu(ievaluatorForm);
		
		//check if we need to show firm filer drop down (for non-mimic internal users.)       
        ievaluatorForm.setShowFirmFilter(FundEvaluatorActionHelper.isShowFirmFilter(
                ievaluatorForm, request));
		
		//set nml/EDJ true or false for external users.
		if(!userProfile.isInternalUser()){
			if (BDUserProfileHelper.associatedWithApprovingFirm(userProfile, NFACodeConstants.NML)) {
				ievaluatorForm.setNml(true);
			} else if (BDUserProfileHelper.associatedWithApprovingFirm(userProfile, NFACodeConstants.EdwardJones)) {
	            // NML and EDJ are mutually exclusive 
			    // set edwardJones true or false for external users
			    ievaluatorForm.setEdwardJones(true);
			}
			if (userProfile.isMerrillAdvisor()) {
				ievaluatorForm.setMerrillFirmFilter(true);
			}
			if (BDUserProfileHelper.associatedWithApprovingFirm(userProfile, NFACodeConstants.SmithBarney)) {
                ievaluatorForm.setSmithBarneyAssociated(true);
            }
		}
		
		if (StringUtils.isEmpty(ievaluatorForm.getFundUsa())) {			
			if(userProfile.getDefaultFundListing() != null){
				if ("us".equalsIgnoreCase(userProfile.getDefaultFundListing().getCode())) {						
					ievaluatorForm.setFundUsa(FundEvaluatorConstants.US_FUNDS);
					ievaluatorForm
							.setDefaultSiteLocation(FundEvaluatorConstants.US_FUNDS);
				} else if ("ny".equalsIgnoreCase(userProfile
						.getDefaultFundListing().getCode())) {
					ievaluatorForm.setFundUsa(FundEvaluatorConstants.NY_FUNDS);
					ievaluatorForm
							.setDefaultSiteLocation(FundEvaluatorConstants.NY_FUNDS);
				} else {
					ievaluatorForm.setFundUsa(FundEvaluatorConstants.US_FUNDS);
				}
			}
			else{
				ievaluatorForm.setFundUsa(FundEvaluatorConstants.US_FUNDS);
			}
		}

	}
    
    /**
     * Set required conent location for a particular request.
     * @param form
     * @param request
     */    
    public static void setContentLocation(FundEvaluatorForm fundEvaluatorForm, HttpServletRequest request) {        
        ApplicationHelper.setRequestContentLocation(request, getFundEvaluationLocation(fundEvaluatorForm, request));        
    }
    
    /**
     * Returns the location of the funds evaluating.
     * @param form
     * @param request
     * @return
     */
    public static Location getFundEvaluationLocation(FundEvaluatorForm fundEvaluatorForm, HttpServletRequest request) {
        
        Location fundEvaluationLocation;
        String companyFundsEvaluated = fundEvaluatorForm.getFundUsa();

        // Changing the CMA content location if funds evaluating for NY, default always for US
        if (StringUtils.equalsIgnoreCase(FundEvaluatorConstants.CLIENT_EXISTING, fundEvaluatorForm
                .getClientType())) {
            // if contract is entered, contract location specific CMA will be retreived
            if (StringUtils.equalsIgnoreCase(fundEvaluatorForm.getContractLocationId(), CommonConstants.COMPANY_ID_NY)) {
                fundEvaluationLocation = Location.NEW_YORK;
            } else {
                fundEvaluationLocation = Location.USA;
            }
        } else {
            // if new client, based on user selected fund evaluation the CMA content will be retreived
            if (StringUtils.isNotBlank(companyFundsEvaluated)
                    && StringUtils.equals(companyFundsEvaluated, BDConstants.COMPANY_NAME_NY)) {
                fundEvaluationLocation = Location.NEW_YORK;
            } else {
                fundEvaluationLocation = Location.USA;
            }
        }

        return fundEvaluationLocation;
    }
    
    /**
     * Reset the overlay filter values to default filter values.
     * @param fundEvaluatorForm
     */
    public static void resetOverlayFilterValues(FundEvaluatorForm fundEvaluatorForm) {
        fundEvaluatorForm.setRankingOrder(FundEvaluatorConstants.SORT_ASCENDING);
        fundEvaluatorForm.setViewInvestmentOptionsBy(FundEvaluatorConstants.VIEW_INVESTMENT_OPTIONS_BY_RANKING);
        fundEvaluatorForm.setListInvesmentOptionBy(FundEvaluatorConstants.LIST_AVAILABLE_INVESTMENT_OPTIONS);
        fundEvaluatorForm.setViewInvOptionsByOnPreview(FundEvaluatorConstants.VIEW_INVESTMENT_OPTIONS_BY_RANKING);
    }
    
    /**
     * filter fund family VO based on Merrill Firm Filter
     * 
     * @param ievaluatorForm
     * @param merrillCoveredFundFamilyGroups
     * @param baseList
     * @return ArrayList<FundFamilyVO>
     */
    public static final String RP_FUND_FAMILY_CODE = "RP";
    private static ArrayList<FundFamilyVO> filterByMerrillCoveredFundFamilyList(FundEvaluatorForm ievaluatorForm,
    		Set<String> merrillCoveredFundFamilyGroups, ArrayList<FundFamilyVO> baseList ) throws SystemException, RemoteException {
    	ArrayList<FundFamilyVO> filteredList = new ArrayList<FundFamilyVO>();
    	LookupDescription familyFundHideToCloseNB = FundServiceDelegate.getInstance().familyFundHideToCloseNB();
    	
    	if((null != familyFundHideToCloseNB && ievaluatorForm.isNewPlanClosedFund() && StringUtils.isNotEmpty(familyFundHideToCloseNB.getLookupCode()) &&
    			familyFundHideToCloseNB.getLookupCode().equals(RP_FUND_FAMILY_CODE)) || (!ievaluatorForm.isExistingClientClosedFund() && StringUtils.isNotBlank(ievaluatorForm.getContractNumber()) ||
    					( ievaluatorForm.isExistingClientClosedFund() && StringUtils.isNotBlank(ievaluatorForm.getContractNumber()))) || 
    			(!ievaluatorForm.isNewPlanClosedFund() && StringUtils.isBlank(ievaluatorForm.getContractNumber()))){
			for(FundFamilyVO fundFamilyVO : baseList) {
				if((fundFamilyVO.isContractSelected() && merrillCoveredFundFamilyGroups.contains(fundFamilyVO.getFamilyCode()))
						|| !(fundFamilyVO.getFamilyCode().equalsIgnoreCase(RP_FUND_FAMILY_CODE))) {
					filteredList.add(fundFamilyVO);
				}
			}
    	}
    	else if(!ievaluatorForm.isMerrillFirmFilter()) {
    		return baseList;
    	}
    	else
    		for(FundFamilyVO fundFamilyVO : baseList) {
    			if(fundFamilyVO.isContractSelected() || merrillCoveredFundFamilyGroups.contains(fundFamilyVO.getFamilyCode())) {
    				filteredList.add(fundFamilyVO);
    			}
    		}
    	return filteredList;
    }
    
    /**
     * filter investment id based on Merrill Firm Filter
     * 
     * @param ievaluatorForm
     * @param merrillCoveredFunds
     * @param baseFundList
     * @param contractSelected
     * @return List<String>
     */
	private static List<String> filterByMerrillCoveredFundList(FundEvaluatorForm ievaluatorForm,
			Set<String> merrillCoveredFunds, List<String> baseFundList, Set<String> contractSelected) {
		if(!ievaluatorForm.isMerrillFirmFilter()) {
    		return baseFundList;
    	}
		List<String> filteredList = new ArrayList<String>();
		for (String fundId : baseFundList) {
			if ((contractSelected != null && contractSelected.contains(fundId)) || merrillCoveredFunds.contains(fundId)) {
				filteredList.add(fundId);
			}
		}
		return filteredList;
	}


}
    
    

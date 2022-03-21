package com.manulife.pension.ps.web.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.util.CloneableAutoForm;

/**
 * Action form for the business conversion page (Security Role Conversion)
 * 
 * @author Steven Wang
 * 
 */
public class BusinessConversionForm extends CloneableAutoForm {

    private static final long serialVersionUID = 5291428493734844586L;
    
    private static final String SAVE_ACTION = "save";
    private static final String CANCEL_ACTION = "cancel";
    private static final String SEARCH_ACTION = "search";
    private static final String BACK_ACTION = "back";
    public static final String FIELD_CONTRACT_NUMBER = "contractNumber";

    private String contractNumber;

    private String contractName;

    private List<BusinessConvertionItem> relatedProfiles = new ArrayList<BusinessConvertionItem>();
    // keep users
    private Collection users;
    
    private boolean noData;
    /**
     * A string keep previous request page 
     */
    private String targetAction;
    
    /**
     * Get contract name
     * @return
     */
    public String getContractName() {
        return contractName;
    }
    /**
     * Set contract name
     * @param contractName
     */
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    /**
     * Get contract number
     * @return
     */
    public String getContractNumber() {
        return contractNumber;
    }
    /**
     * Set contract number
     * @param contractNumber
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
    /**
     * Get profiles relate to certain contract
     * @return
     */
    public List<BusinessConvertionItem> getRelatedProfiles() {
        return relatedProfiles;
    }
    /**
     * Set profiles relate to certain contract
     * @param relatedProfiles
     */
    public void setRelatedProfiles(List<BusinessConvertionItem> relatedProfiles) {
        this.relatedProfiles = relatedProfiles;
    }
    /**
     * Help method to get related profile size of
     */
    public int getItemsSize(){
        return relatedProfiles.size();
    }
    /**
     * Help method to check if action is searching action
     * @return
     */
    public boolean isSearchAction(){
       if (getAction()!=null && getAction().equalsIgnoreCase(SEARCH_ACTION))
       {
           return true;
       }
       return false;
    }
    /**
     * Help method to check if action is save action
     * @return
     */
    public boolean isSaveAction(){
       if (getAction()!=null && getAction().equalsIgnoreCase(SAVE_ACTION))
       {
           return true;
       }
       return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    public void clear( HttpServletRequest request) {
        super.clear( request);
        //keep contract number and target action value
        contractName = null;
        noData = false;
        relatedProfiles = new ArrayList<BusinessConvertionItem>();
    }
    public Object clone() {
            BusinessConversionForm myClone = (BusinessConversionForm) super.clone();
            myClone.relatedProfiles = new ArrayList<BusinessConvertionItem>();

            for (Iterator it = relatedProfiles.iterator(); it.hasNext();) {
                BusinessConvertionItem item = (BusinessConvertionItem) it.next();
                myClone.relatedProfiles.add((BusinessConvertionItem) item.clone());
            }
            return myClone;
    }
    /**
     * Get target action
     * @return
     */
    public String getTargetAction() {
        return targetAction;
    }
    /**
     * Set target action
     * @param targetAction
     */
    public void setTargetAction(String targetAction) {
        this.targetAction = targetAction;
    }
    public boolean isNoData() {
        return noData;
    }
    public void setNoData(boolean noData) {
        this.noData = noData;
    }
    public Collection getUsers() {
        return users;
    }
    public void setUsers(Collection users) {
        this.users = users;
    }


}

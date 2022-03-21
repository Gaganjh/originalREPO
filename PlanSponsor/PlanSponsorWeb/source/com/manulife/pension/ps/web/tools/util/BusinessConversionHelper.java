package com.manulife.pension.ps.web.tools.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.profiles.FormChanges;
import com.manulife.pension.ps.web.tools.BusinessConvertionItem;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.UserRoleFactory;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.role.type.RoleType;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.DefaultRolePermissions;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * A helper class for populating user permissions.
 * 
 * @author Steven Wang
 */
public class BusinessConversionHelper {

    /**
     * Constructor.
     */
    private BusinessConversionHelper() {
        super();
    }
    /**
     * Help method to populate a given contract permission with a new user selected role
     * @param contract
     * @param profileItem
     * @throws SystemException
     */
    public static void populateUserContractPermission(ContractPermission contract,BusinessConvertionItem profileItem) throws SystemException {
        //set new primary contact
        contract.setPrimaryContact(profileItem.isPrimaryContact());
        //set new mail recipient
        contract.setMailRecipient(profileItem.isMailRecipient());
        UserRole newRole = null;
        newRole = UserRoleFactory.getUserRole(profileItem.getRole().getValue());
        if (newRole instanceof IntermediaryContact)
        {
            contract.setRoleType(RoleType.getRoleTypeById(profileItem.getContactType()));
        }
        contract.setTrusteeMailRecepient(profileItem.isTrusteeMailRecipient());
        contract.setStatementIndicator(profileItem.isPptStatementConsultant());
        contract.setSignatureReceivedTrustee(profileItem.isSignReceivedTrustee());
        contract.setSignatureReceivedAuthSigner(profileItem.isSignReceivedAuthorizedSignor());
        
        //populate permission base on new role
        populateUserRolePermissions(newRole, contract.getRole());
        contract.setRole(newRole);
    }  
    /**
     * Help method to get changes after business conversion for event log
     * @param newMap
     * @param oldMap
     * @return String contain changes
     */
    public static String getChanges(Map newMap, Map oldMap){
        FormChanges formChanges = new FormChanges();
        formChanges.computeChanges(newMap, oldMap);
        return formChanges.toString();
        
    }
    /**
     * Help method to convert contract permission to flat map for compute changes
     * @param contract
     * @return
     */
    public static Map getContractPermissionAsMap(ContractPermission contract){
        Map map = new HashMap();
        String prefix = new StringBuffer("contractAccess[").append(contract.getContractNumber()).append("].").toString();
        map.put(prefix + "planSponsorRole", contract.getRole().getDisplayName());
        if (contract.getRoleType() != null) {
            map.put(prefix + "roleType", contract.getRoleType().getDisplayName());
        }
        map.put(prefix + "primaryContact", contract.isPrimaryContact());
        map.put(prefix + "mailRecipient", contract.isMailRecipient());
        for (Iterator i = contract.getRole().getPermissions().iterator(); i.hasNext();){
            PermissionType permission = (PermissionType) i.next();
            map.put(prefix + PermissionType.getPermissionCode(permission), "true");
        }
        return map;
        
    }
    /**
     * Populate user role permission
     * 
     * @param newRole
     * @param planSponsorUserRole
     * @throws SystemException
     */
    public static void populateUserRolePermissions(UserRole newRole, UserRole planSponsorUserRole) throws SystemException {
        //manage users
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.MANAGE_EXTERNAL_USERS);
        //selected access
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.SELECTED_ACCESS);
        //Edit Services Features
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.EDIT_SERVICE_FEATURES);
        //Down load report
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.REPORT_DOWNLOAD);
        //Employer statements
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.EMPLOYER_STATEMENT_ACCESS);
        //view submissions
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.SUBMISSION_ACCESS);
        //create/upload submissions
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.UPLOAD_SUBMISSIONS);
        //view all user's submissions
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.VIEW_ALL_SUBMISSIONS);
        //cash account
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.CASH_ACCOUNT_ACCESS);
        //update census data
        mapExistingPermissions(newRole, planSponsorUserRole, PermissionType.UPDATE_CENSUS_DATA);
        //direct debit & account
        mapDirectDebitPermission(newRole, planSponsorUserRole);
        //view salar
        mapViewSalaryPermission(newRole, planSponsorUserRole);
        //new permission
        populateNewPermissions(newRole);
        // loans permissions
        populateLoanPermissions(newRole);
    }

    /**
     * Help method to convert existing permissions excluding direct debit and View Salary permissions
     * @param currentRole
     * @param newRole
     * @param permissionType
     * @throws SystemException
     */
    private static void mapExistingPermissions(UserRole newRole, UserRole oldRole, PermissionType permissionType) throws SystemException {

        DefaultRolePermissions newRoleDefaultPermissions = getDefaultRolePermissions(newRole);

        // BCN37- i new role default is true add this permission
        if (newRoleDefaultPermissions.getDefaultPermissionValue(permissionType).equals(DefaultRolePermissions.TRUE)) 
        {
            newRole.addPermission(permissionType);
            return;
        }
        // BCN37 - ii new role default is not applicable. Remove permission
        if (newRoleDefaultPermissions.getDefaultPermissionValue(permissionType).equals(DefaultRolePermissions.NOT_APPLICABLE)) 
        {
            if (newRole.hasPermission(permissionType)) 
            {
                newRole.removePermission(permissionType);
            }
            return;
        }
        // BCN37 - iii new role default is no. current role has this permission remove it.
        if (newRoleDefaultPermissions.getDefaultPermissionValue(permissionType).equals(DefaultRolePermissions.NO)) 
        {
            if (newRole.hasPermission(permissionType)) 
            {
                newRole.removePermission(permissionType);
            }
            return;
        }
        // BCN37 - new role default is yes. current role does not has this permission add it.
        if (newRoleDefaultPermissions.getDefaultPermissionValue(permissionType).equals(DefaultRolePermissions.YES)) {
            if (!newRole.hasPermission(permissionType)) 
            {
                newRole.addPermission(permissionType);
            }
            return;
        }
    }
    
    /**
     * Help method to map direct debit permission and direct debit accounts
     * @param newRole
     * @param oldRole
     */
    private static void mapDirectDebitPermission(UserRole newRole, UserRole oldRole){
        //map the existing direct debit permission and accounts DSF31 BCN38
        if (oldRole.hasPermission(PermissionType.DIRECT_DEBIT_ACCOUNT))
        {
            newRole.addPermission(PermissionType.DIRECT_DEBIT_ACCOUNT);
            newRole.setDirectDebitAccounts(oldRole.getDirectDebitAccounts());
        }
    }
    
    /**
     * map view salary permission. DFS31 and BCN38a 
     * @param newRole
     * @param oldRole
     * @throws SystemException
     */
    private static void mapViewSalaryPermission(UserRole newRole, UserRole oldRole) throws SystemException{
        DefaultRolePermissions newRoleDefaultPermissions = getDefaultRolePermissions(newRole);
        if (newRoleDefaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_SALARY).equals(DefaultRolePermissions.TRUE))
        {
            if (!oldRole.hasPermission(PermissionType.VIEW_SALARY))
            {    
                newRole.addPermission(PermissionType.VIEW_SALARY);
                return;
            }
        }
        if (newRoleDefaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_SALARY).equals(DefaultRolePermissions.NOT_APPLICABLE))
        {
            if (newRole.hasPermission(PermissionType.VIEW_SALARY))
            {
                newRole.removePermission(PermissionType.VIEW_SALARY);
            }
            return;
        }
        if (newRoleDefaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_SALARY).equals(DefaultRolePermissions.YES) || newRoleDefaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_SALARY).equals(DefaultRolePermissions.NO))
        {
            if (oldRole.hasPermission(PermissionType.VIEW_SALARY))
            {
                newRole.addPermission(PermissionType.VIEW_SALARY);
            } else {
                if (newRole.hasPermission(PermissionType.VIEW_SALARY))
                {
                    newRole.removePermission(PermissionType.VIEW_SALARY);
                }
            }
            return;
        }

        
    }
    
    /**
     * Populate new permissions. DSF31 BCN39
     * @param newRole
     */
    private static void populateNewPermissions(UserRole newRole){
        //trustee
        if (newRole instanceof Trustee)
        {
            newRole.addPermission(PermissionType.VIEW_ALL_WITHDRAWALS);
            newRole.addPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE);
            newRole.addPermission(PermissionType.REVIEW_WITHDRAWALS);
            // Changing APPROVE_WITHDRAWALS to SIGNING_AUTHORITY
            newRole.addPermission(PermissionType.SIGNING_AUTHORITY);
            return;
        }
        //authorized signor
        if (newRole instanceof AuthorizedSignor)
        {
            newRole.addPermission(PermissionType.VIEW_ALL_WITHDRAWALS);
            newRole.addPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE);
            newRole.addPermission(PermissionType.REVIEW_WITHDRAWALS);
            // Changing APPROVE_WITHDRAWALS to SIGNING_AUTHORITY
            newRole.addPermission(PermissionType.SIGNING_AUTHORITY);
            return;
        }
        //administrative contact
        if (newRole instanceof AdministrativeContact)
        {
            newRole.addPermission(PermissionType.VIEW_ALL_WITHDRAWALS);
            newRole.addPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE);
            newRole.addPermission(PermissionType.REVIEW_WITHDRAWALS);
            return;
        }
        //intermediary contact
        if (newRole instanceof IntermediaryContact)
        {
            newRole.addPermission(PermissionType.VIEW_ALL_WITHDRAWALS);
            return;
        }
        //payroll administrator
        if (newRole instanceof PayrollAdministrator)
        {
            return;
        }
        
    }
    


/**
 * Populate new loans permissions based on the role
 * @param newRole
 */
private static void populateLoanPermissions(UserRole newRole){
    //trustee
    if (newRole instanceof Trustee)
    {
        newRole.addPermission(PermissionType.VIEW_ALL_LOANS);
        newRole.addPermission(PermissionType.INITIATE_LOANS);
        newRole.addPermission(PermissionType.REVIEW_LOANS);
        return;
    }
    //authorized signer
    if (newRole instanceof AuthorizedSignor)
    {
        newRole.addPermission(PermissionType.VIEW_ALL_LOANS);
        newRole.addPermission(PermissionType.INITIATE_LOANS);
        newRole.addPermission(PermissionType.REVIEW_LOANS);

        return;
    }
    //administrative contact
    if (newRole instanceof AdministrativeContact)
    {
        newRole.addPermission(PermissionType.VIEW_ALL_LOANS);
        newRole.addPermission(PermissionType.INITIATE_LOANS);
        newRole.addPermission(PermissionType.REVIEW_LOANS);
        return;
    }
    //intermediary contact
    if (newRole instanceof IntermediaryContact)
    {
        newRole.addPermission(PermissionType.VIEW_ALL_LOANS);
        return;
    }
    //payroll administrator
    if (newRole instanceof PayrollAdministrator)
    {
        return;
    }
    
}
   
    /**
     * Get the DefaultRolePermissions for the UserRole
     * 
     * @param userRole
     * @return DefaultRolePermissions
     * @throws SystemException
     */
    public static DefaultRolePermissions getDefaultRolePermissions(UserRole userRole) throws SystemException {
        if (userRole.getDefaultRolePermissions() == null) {
            userRole.setDefaultRolePermissions(SecurityServiceDelegate.getInstance().getDefaultRolePermissions(userRole.toString()));
        }
        return userRole.getDefaultRolePermissions();
    }

    /**
     * This method will be used to update the user.
     * 
     * @param user
     * @param profileItem
     */
    public static void updateUserInfo(UserInfo user, BusinessConvertionItem profileItem) {
    	user.setWebAccessInd(profileItem.isWebAccess());
    }
}
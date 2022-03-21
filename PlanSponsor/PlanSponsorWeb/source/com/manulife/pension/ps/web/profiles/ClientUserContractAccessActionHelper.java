package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.fee.util.FeeDisclosureUtility;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractContact;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.PlanSponsorUser;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.UserRoleFactory;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.AccessLevelUtility;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;

/**
 * A helper class for add/edit client user action. The goal is to cut down the amount of non-workflow related codes in the actual action.
 * 
 * @author Charles Chan
 * @author Steven Wang
 */
public class ClientUserContractAccessActionHelper {

    private static final Logger logger = Logger.getLogger(ClientUserContractAccessActionHelper.class);

    /**
     * slect one constants for role drop down list
     */
    public static final String SELECT_ONE_CONSTANTS = "Select one";
    public static final String Empty_String = "";

    /**
     * Constructor.
     */
    private ClientUserContractAccessActionHelper() {
        super();
    }

    /**
     * This method populates a new contract access view object from a contract object. This method is only called when a new contract is added to the user (either the first default
     * one or subsequent ones).
     * 
     * @param accessForm
     * @param contract
     * @throws SystemException
     */
    static void populateNewContractAccess(UserRole loginUserRole, ClientUserContractAccess contractAccess, Contract contract) throws SystemException {
        contractAccess.setContractNumber(new Integer(contract.getContractNumber()));
        contractAccess.setContractName(contract.getCompanyName());
        contractAccess.setCbcIndicator(contract.isBusinessConverted());
        contractAccess.setNewContract(true);

        UserPermissions userPermissions = null;
        // role DSF9 SCC40 & S
        if (contract.isBusinessConverted()) {
            // no role is selected permission & email preferrence should be null
            contractAccess.setPlanSponsorSiteRole(new RoleValueLabelBean());
            userPermissions = new UserPermissions();
        } else {
            UserRole planSponsorUserRole = new PlanSponsorUser();
            contractAccess.setPlanSponsorSiteRole(new RoleValueLabelBean(PlanSponsorUser.ID, planSponsorUserRole.getDisplayName()));
            userPermissions = UserPermissionsHelper.getNewUserPermissions(loginUserRole, planSponsorUserRole, contract);
        }
        contractAccess.setUserPermissions(userPermissions);
        contractAccess.setOriginalUserPermissions((UserPermissions) userPermissions.clone());

        contractAccess.setIccDesignate(false);
        contractAccess.setPrimaryContact(false);
        contractAccess.setMailRecepient(false);
        contractAccess.setTrusteeMailRecepient(false);
        contractAccess.setStatementIndicator(false);
        contractAccess.setSignatureReceivedTrustee(false);
        contractAccess.setSignatureReceivedAuthSigner(false);
        contractAccess.setIccDesignateEligible(isICCDesignateEligible(contract.getContractNumber()));
        contractAccess.setSendServiceDesignate(false);
        
        loadSendServiceFeatures(contractAccess, contract.getContractNumber(), contract);

        if (contract.getIccDesignate() != null) {
            contractAccess.setIccDesignateProfileId(contract.getIccDesignate().getProfileId());
            contractAccess.setIccDesignateName(contract.getIccDesignate().getFirstName() + " " + contract.getIccDesignate().getLastName());
        }
        if (contract.getSendServiceDesignate() != null) {
            contractAccess.setSendServiceDesignateProfileId(contract.getSendServiceDesignate().getProfileId());
            contractAccess.setSendServiceDesignateName(contract.getSendServiceDesignate().getFirstName() + " " + contract.getSendServiceDesignate().getLastName());
        }
        // Get current primary contact and mail recipient for this contract
        if (contract.getPrimaryContact() != null) {
            contractAccess.setPrimaryContactProfileId(contract.getPrimaryContact().getProfileId());
            contractAccess.setPrimaryContactName(contract.getPrimaryContact().getFirstName() + " " + contract.getPrimaryContact().getLastName());
        }
        if (contract.getMailRecipient() != null) {
            contractAccess.setMailRecipientProfileId(contract.getMailRecipient().getProfileId());
            contractAccess.setMailRecipientName(contract.getMailRecipient().getFirstName() + " " + contract.getMailRecipient().getLastName());
        }
        
		if (contract.getTrusteeMailRecipient() != null) {
			ContractContact trusteeMailRecipientContact = contract
					.getTrusteeMailRecipient();

			contractAccess
					.setTrusteeMailRecipientProfileId(trusteeMailRecipientContact
							.getProfileId());
			contractAccess
					.setTrusteeMailRecipientName(trusteeMailRecipientContact
							.getFirstName()
							+ " " + trusteeMailRecipientContact.getLastName());
		}
        
		if (contract.getParticipantStatementConsultant() != null) {
			ContractContact participantStatementConsultant = contract
					.getParticipantStatementConsultant();
			contractAccess
					.setParticipantStatementConsultantProfileId(participantStatementConsultant
							.getProfileId());
			contractAccess
					.setParticipantStatementConsultantName(participantStatementConsultant
							.getFirstName()
							+ " "
							+ participantStatementConsultant.getLastName());
		}
        
        if (contract.isBusinessConverted()) {
            contractAccess.setRoleList(buildRoleList(loginUserRole, null));
        } else {
            // populate flags for warning messages
            setLastPermissionFlags(contractAccess, 0, contract);
        }
    }

    /**
     * Populates a contract access view object with the given contract permission & contract
     * 
     * @param accessForm
     * @param contractPermission
     * @param Contract contract
     * @throws SystemException
     */
    static void populateContractAccess(ClientUserContractAccess contractAccess, UserRole loggedInRole, ContractPermission contractPermission, UserInfo manageUser, Contract contract)
            throws SystemException {
        contractAccess.setContractNumber(new Integer(contract.getContractNumber()));
        contractAccess.setContractName(contract.getCompanyName());
        contractAccess.setCbcIndicator(contract.isBusinessConverted());
        // role id
        contractAccess.setPlanSponsorSiteRole(new RoleValueLabelBean(contractPermission.getRole().toString(), contractPermission.getRole().getDisplayName()));
        if (contractPermission.getRoleType() != null) {
            contractAccess.setRoleType(contractPermission.getRoleType().getId());
        }
        contractAccess.setPrimaryContact(contractPermission.isPrimaryContact());
        contractAccess.setMailRecepient(contractPermission.isMailRecipient());
        contractAccess.setTrusteeMailRecepient(contractPermission.isTrusteeMailRecepient());
        contractAccess.setStatementIndicator(contractPermission.isStatementIndicator());
        contractAccess.setSignatureReceivedTrustee(contractPermission.isSignatureReceivedTrustee());
        contractAccess.setSignatureReceivedAuthSigner(contractPermission.isSignatureReceivedAuthSigner());
        contractAccess.setIccDesignate(contractPermission.isIccDesignate());
        contractAccess.setIccDesignateEligible(isICCDesignateEligible(contract.getContractNumber()));
        contractAccess.setSendServiceDesignate(contractPermission.isSendServiceDesignate());
        
        loadSendServiceFeatures(contractAccess, contract.getContractNumber(), contract);
        
        if(contract.getIccDesignate()!= null){
        	contractAccess.setIccDesignateProfileId(contract.getIccDesignate().getProfileId());
        	contractAccess.setIccDesignateName(contract.getIccDesignate().getFirstName()+" "+contract.getIccDesignate().getLastName());
        }
        if(contract.getSendServiceDesignate()!= null){
        	contractAccess.setSendServiceDesignateProfileId(contract.getSendServiceDesignate().getProfileId());
        	contractAccess.setSendServiceDesignateName(contract.getSendServiceDesignate().getFirstName()+" "+contract.getSendServiceDesignate().getLastName());
        }
        // Get current primary contact and mail recipient for this contract
        if (contract.getPrimaryContact() != null) {
            contractAccess.setPrimaryContactProfileId(contract.getPrimaryContact().getProfileId());
            contractAccess.setPrimaryContactName(contract.getPrimaryContact().getFirstName() + " " + contract.getPrimaryContact().getLastName());
        }
        if (contract.getMailRecipient() != null) {
            contractAccess.setMailRecipientProfileId(contract.getMailRecipient().getProfileId());
            contractAccess.setMailRecipientName(contract.getMailRecipient().getFirstName() + " " + contract.getMailRecipient().getLastName());
        }
        
        if (contract.getTrusteeMailRecipient() != null) {
			ContractContact trusteeMailRecipientContact = contract
					.getTrusteeMailRecipient();

			contractAccess
					.setTrusteeMailRecipientProfileId(trusteeMailRecipientContact
							.getProfileId());
			contractAccess
					.setTrusteeMailRecipientName(trusteeMailRecipientContact
							.getFirstName()
							+ " " + trusteeMailRecipientContact.getLastName());
		}
        
		if (contract.getParticipantStatementConsultant() != null) {
			ContractContact participantStatementConsultant = contract
					.getParticipantStatementConsultant();
			contractAccess
					.setParticipantStatementConsultantProfileId(participantStatementConsultant
							.getProfileId());
			contractAccess
					.setParticipantStatementConsultantName(participantStatementConsultant
							.getFirstName()
							+ " "
							+ participantStatementConsultant.getLastName());
		}

        // populate permissions
        UserPermissions userPermissions = UserPermissionsHelper.getUserPermissions(loggedInRole, contractPermission.getRole(), contract);
        contractAccess.setUserPermissions(userPermissions);
        contractAccess.setOriginalUserPermissions((UserPermissions) userPermissions.clone());

        // populate flag for warning messages
        setLastPermissionFlags(contractAccess, manageUser.getProfileId(), contract);

        if (contract.isBusinessConverted()) {
            contractAccess.setRoleList(buildRoleList(loggedInRole, contractPermission.getRole()));
        }
    }

    /**
     * Repopulate a contract access view object when role is changed
     * 
     * @param accessForm
     * @param loggedInRole
     * @param Contract contract
     * @throws SystemException
     */
    static void reloadContractAccessForRoleChange(ClientUserContractAccess contractAccess, UserRole loggedInRole, Contract contract) throws SystemException {
        UserRole newRole = UserRoleFactory.getUserRole(contractAccess.getPlanSponsorSiteRole().getValue());
        // populate permissions
        UserPermissions userPermissions = UserPermissionsHelper.getNewUserPermissions(loggedInRole, newRole, contract);
        contractAccess.setUserPermissions(userPermissions);
    }

    public static UserInfo getManagedUserInfo(Principal loggedInPrincipal, String managedUserName) throws SystemException {
        UserInfo userInfo = null;
        try {
            // Pass in null for the Principal to bypass some security checks. It is up to the calling class to filter out any contracts the logged in user doesn't have access to.
            userInfo = SecurityServiceDelegate.getInstance().searchByUserName(AbstractAddEditUserController.newPrincipal(new Long(0), null, null), managedUserName);
        } catch (SecurityServiceException e) {
            SystemException se = new SystemException(e, ClientUserContractAccessActionHelper.class.toString(), "getManagedUserInfo", e.toString());
            throw se;
        }
        return userInfo;
    }
    
    //This method will return true if the given contractId is not MTA , RA457 and DB
    private static boolean isICCDesignateEligible(int contractId)throws SystemException  {
    	return SecurityServiceDelegate.getInstance().isICCDesignateEligible(contractId);
    }
    /**
     * Build a contract accesses list base on login user's role & permission
     * 
     * @param logged in user profile
     * @param logged in user info
     * @param manageed user info
     * @return a list contain login user can managed.
     * @throws SystemException
     */
    static List<ClientUserContractAccess> buildContractAccesses(UserProfile loggedInUser, UserInfo loggedInUserInfo, UserInfo managedUserInfo) throws SystemException {
        return buildContractAccesses(loggedInUser, loggedInUserInfo, managedUserInfo, new ArrayList<Integer>());
    }

    /**
     * Build a contract accesses list base on login user's role & permission
     * 
     * @param loggedInUser
     * @param loggedInUserInfo
     * @param managedUserInfo
     * @param cannotManageRoleContracts
     * @return
     * @throws SystemException
     */
    public static List<ClientUserContractAccess> buildContractAccesses(UserProfile loggedInUser, UserInfo loggedInUserInfo, UserInfo managedUserInfo, List<Integer> cannotManageRoleContracts) throws SystemException {
        List<ClientUserContractAccess> contractAccesses = new ArrayList<ClientUserContractAccess>();
        // managed user contract permissions
        ContractPermission[] permissions = managedUserInfo.getContractPermissions();
        /*
         * Sort managed user's contracts by contract number.
         */
        Arrays.sort(permissions, new Comparator() {
            public int compare(Object o1, Object o2) {
                ContractPermission bean1 = (ContractPermission) o1;
                ContractPermission bean2 = (ContractPermission) o2;
                Integer contractNumber1 = new Integer(bean1.getContractNumber());
                Integer contractNumber2 = new Integer(bean2.getContractNumber());
                return contractNumber1.compareTo(contractNumber2);
            }
        });
        // build contract access list
        for (int i = 0; i < permissions.length; i++) {
            if (logger.isDebugEnabled()) {
                logger.debug("permission:" + permissions[i].toString());
            }

            ClientUserContractAccess contractAccess = new ClientUserContractAccess();

            if (loggedInUser.getRole().isInternalUser()) {
                // Internal user display all managed user's contracts. DFS11 SVC 58 & DFS 9 MPR 206
                populateContractAccess(contractAccess, loggedInUser.getRole(), permissions[i], managedUserInfo, getContract(managedUserInfo.getRole(), permissions[i]
                        .getContractNumber()));
            } else {
                if (loggedInUser.getRole().isExternalUser()) {
                    ContractPermission loggedInUserPermission = loggedInUserInfo.getContractPermission(permissions[i].getContractNumber());
                    if (loggedInUserPermission != null) {
                        UserRole loggedInContractRole = loggedInUserPermission.getRole();
                        Contract contract = getContract(managedUserInfo.getRole(), permissions[i].getContractNumber());
                        if (!contract.isBusinessConverted()) {
                            // login contract is not business converted
                            if (AccessLevelUtility.canManageExternal(loggedInContractRole) && !AccessLevelUtility.canManageExternal(permissions[i].getRole())) {
                                // add contract if login user can manage external & managed user can not
                                // manage user DFS11 SVC59 & DFS 9 SCC2
                                populateContractAccess(contractAccess, loggedInContractRole, permissions[i], managedUserInfo, contract);
                            } else {
                                // do not add contract
                                continue;
                            }

                        } else {
                            // login contract is business ovnverted
                            // DFS 11 SVC 60 & DFS9 scc 3
                            if (canAccessBusinessConvertedContract(loggedInContractRole, permissions[i])) {
                                populateContractAccess(contractAccess, loggedInContractRole, permissions[i], managedUserInfo, contract);
                            } else {
                                cannotManageRoleContracts.add(contract.getContractNumber());
                                // don't add contract
                                continue;
                            }
                        }
                    } else {
                        // logged in user has no access to this contract
                        continue;
                    }
                } else {
                    continue;
                }
            }
            contractAccesses.add(contractAccess);
        }

        return contractAccesses;

    }

    /**
     * Obtains the list of contracts (in the form of Integer objects) that the user is an external user manager of.
     * 
     * @param userName The user name to check.
     * @return
     */
    static List getExternalUserManagerContracts(String userName) {
        List contracts = null;
        try {
            UserInfo userInfo = SecurityServiceDelegate.getInstance().searchByUserName(AbstractAddEditUserController.newPrincipal(new Long(0), null, null), userName);
            ContractPermission[] editUserContracts = userInfo.getContractPermissions();
            contracts = new ArrayList(editUserContracts.length);
            for (int i = 0; i < editUserContracts.length; i++) {
                UserRole role = editUserContracts[i].getRole();
                if (role.isExternalUser() && role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)) {
                    contracts.add(new Integer(editUserContracts[i].getContractNumber()));
                }
            }
        } catch (Exception e) {
            /*
             * This should not happen because we are only retrieving the user info object using a generic principal.
             */
            throw new NestableRuntimeException(e);
        }
        return contracts;
    }

    /**
     * Prepares the request. Specifically, it sets up the contract drop down list if the user is a PSEUM and has multiple contracts.
     * 
     * @param actionForm
     * @param request
     */
    static void populateContractDropDown( AddEditClientUserForm actionForm, HttpServletRequest request) throws SystemException {
        // get login user profile
        UserProfile userProfile = PsController.getUserProfile(request);
       // AddEditClientUserForm form = (AddEditClientUserForm) actionForm;
        UserRole userRole = userProfile.getRole();

        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				PsController.getUserProfile(request).getPrincipal());

        /*
         * MPR 20 & MPR 21. Show drop down list of contracts if logged in user is an PSEUM and has multiple contracts.
         */
        if (userRole.isExternalUser() && userRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)) {
            // && userProfile.isMultipleContracts()) {

            ContractPermission[] contracts = loginUserInfo.getContractPermissions();

            /*
             * The labels are sorted by contract IDs in ascending order.
             */
            Set contractLabelValueBeans = new TreeSet(new Comparator() {
                public int compare(Object o1, Object o2) {
                    LabelValueBean bean1 = (LabelValueBean) o1;
                    LabelValueBean bean2 = (LabelValueBean) o2;
                    Integer contractNumber1 = new Integer(bean1.getValue());
                    Integer contractNumber2 = new Integer(bean2.getValue());
                    return contractNumber1.compareTo(contractNumber2);
                }
            });

            /*
             * Obtains the list of contracts that the edited user is a PSEUM of.
             */
            List editUserPseumContracts;
         //TODO Spring changes.. need to fix it
         /*  if (!AbstractAddEditUserAction.isAddUser(mapping)) {
                editUserPseumContracts = getExternalUserManagerContracts(actionForm.getUserName());
            } else {*/
                editUserPseumContracts = new ArrayList();
            /*}*/

            for (int i = 0; i < contracts.length; i++) {

                /*
                 * Drop down should only contains contracts that the login user is a PSEUM of.
                 */
                UserRole contractUserRole = contracts[i].getRole();

                if (!(contractUserRole.isExternalUser() && contractUserRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS))) {
                    continue;
                }

                int contractId = contracts[i].getContractNumber();

                /*
                 * Also, if the edited user is a PSEUM of any contract, the drop down should not show the contract.
                 */
                if (editUserPseumContracts.contains(new Integer(contractId)) 
                		|| (userProfile.getCurrentContract() != null 
                				&& userProfile.getCurrentContract().getContractNumber() == contractId)) {
                    continue;
                }
                /*
                 * Check contract status skill PS, DC, PC
                 */
                try{
                    Contract contract =  ContractServiceDelegate.getInstance().getContractDetails(contracts[i].getContractNumber(), 6);
                    if (contract.getStatus().equals(Contract.STATUS_PENDING_CONTRACT_APPROVAL) || contract.getStatus().equals(Contract.STATUS_DETAILS_COMPLETED)||
                            contract.getStatus().equals(Contract.STATUS_PROPOSAL_SIGNED)){
                        continue;
                    }

                } catch (ContractNotExistException e){
                    throw new SystemException(e.getMessage());
                }
                
                // Skip any contracts that the logged in user doesn't have access to that the managed user already has
                if (actionForm.getCannotManageRoleContracts() != null && actionForm.getCannotManageRoleContracts().contains(contractId)) {
                    continue;
                }
                
                /*
                 * Skip all contracts that is already in the form.
                 */
                boolean contractFound = false;

                for (Iterator it2 = actionForm.getContractAccesses().iterator(); it2.hasNext() && !contractFound;) {
                    ClientUserContractAccess accessForm = (ClientUserContractAccess) it2.next();
                    if (accessForm.getContractNumber().intValue() == contractId) {
                        contractFound = true;
                    }
                }

                if (contractFound) {
                    continue;
                }

                String label = contractId + " " + contracts[i].getCompanyName();

                contractLabelValueBeans.add(new LabelValueBean(label, String.valueOf(contractId)));
            }

            /*
             * Add an empty label.
             */
            List contractList = new ArrayList();
            if (contractLabelValueBeans.size() > 0) {
                contractList.add(new LabelValueBean(SELECT_ONE_CONSTANTS,Empty_String));
                contractList.addAll(contractLabelValueBeans);
                request.setAttribute(Constants.ADD_EDIT_USER_CONTRACT_LIST, contractList);
            }
        }
    }

    static void populateContractPermission(ContractPermission permission, ClientUserContractAccess access) throws SystemException {

        permission.setContractNumber(access.getContractNumber().intValue());
        permission.setPrimaryContact(access.isPrimaryContact());
        permission.setIccDesignate(access.isIccDesignate());
        permission.setSendServiceDesignate(access.isSendServiceDesignate());
        permission.setMailRecipient(access.isMailRecepient());
        permission.setStatementIndicator(access.isStatementIndicator());
        permission.setTrusteeMailRecepient(access.isTrusteeMailRecepient());
        permission.setSignatureReceivedTrustee(access.isSignatureReceivedTrustee());
        permission.setSignatureReceivedAuthSigner(access.isSignatureReceivedAuthSigner());
        UserPermissionsHelper.populateRolePermissions(permission.getRole(), access.getUserPermissions());
    }

    /**
     * Located client user contract access view object in contract access list
     * 
     * @param contractAccesses
     * @param contractNumber
     * @return a client user contract access.
     */
    static ClientUserContractAccess findContractAccess(List contractAccesses, String contractNumber) {
        for (Iterator it = contractAccesses.iterator(); it.hasNext();) {
            ClientUserContractAccess access = (ClientUserContractAccess) it.next();
            Integer itContractNumber = access.getContractNumber();
            /*
             * The contract number can be null because we're adding a dummy contract access in the cloned form to maintain the order of contracts.
             */
            if (itContractNumber != null && itContractNumber.toString().equals(contractNumber)) {
                return access;
            }
        }
        return null;

    }

    /**
     * Check user can access business converted contract
     * 
     * @param user
     * @param contractpermission
     * @return
     */
    static boolean canAccessBusinessConvertedContract(UserRole loggedInRole, ContractPermission contractpermission) {
        // internal user can access all contract
        if (loggedInRole.isInternalUser()) {
            if (contractpermission.getRole() instanceof Trustee || contractpermission.getRole() instanceof AuthorizedSignor) {
                return loggedInRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS_TRUSTEE_AND_AUTH_SIGNOR);
            } else {
                return loggedInRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS);
        }
        } else if (loggedInRole.isExternalUser() && AccessLevelUtility.canManageExternal(loggedInRole)) {
        // external user DFS9 SCC 3 & DFS 11 SVC60
            // trustee role
            if (loggedInRole instanceof Trustee) {
                if (contractpermission.getRole() instanceof Trustee)
                    return true;
                if (contractpermission.getRole() instanceof AuthorizedSignor)
                    return true;
                if (contractpermission.getRole() instanceof AdministrativeContact)
                    return true;
                if (contractpermission.getRole() instanceof IntermediaryContact)
                    return true;
                if (contractpermission.getRole() instanceof PayrollAdministrator)
                    return true;
            }
            // authorized signor or administrative role
            if (loggedInRole instanceof AuthorizedSignor || loggedInRole instanceof AdministrativeContact) {
                if (contractpermission.getRole() instanceof AdministrativeContact)
                    return true;
                if (contractpermission.getRole() instanceof PayrollAdministrator)
                    return true;
            }
        } else {
            return false;
        }
        return false;

    }

    /**
     * Retrieves a contract object.
     * 
     * @param userRole
     * @param contractNumber
     * @return
     * @throws SystemException
     */
    static Contract getContract(UserRole userRole, Integer contractNumber) throws SystemException {

        Contract contract = null;
        try{
            contract = ContractServiceDelegate.getInstance().getContractDetails(contractNumber.intValue(), 6);
        } catch (ContractNotExistException ce){
            throw new SystemException(ce, ClientUserContractAccessActionHelper.class.getName(), "getContract",
            "Contract does not exist.");
        }

        if (contract != null) {

            if (contract.getCompanyName() == null) {
                return null;
            }

            /*
             * MPR.63 - If the user is a CAR or Team Leader, then pushing the Add contracts link will display an error message to user if contract number enterted is a Manulife
             * Staff contract.
             */
            if (contract.isManulifeStaffContract()) {
                if (!userRole.hasPermission(PermissionType.STAFF_ACCOUNT)) {
                    // need add internal services/Conversion Car and pilot CAR
                    return null;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(contract);
        }
        return contract;
    }

    /**
     * build role drop down list for contract access if logged in contract is business converted
     */
    static List<LabelValueBean> buildRoleList(UserRole loggedInRole, UserRole managedRole) {
        List<LabelValueBean> roleList = new ArrayList<LabelValueBean>();

        // internal user
        if (loggedInRole.isInternalUser()) {
            if (loggedInRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS_TRUSTEE_AND_AUTH_SIGNOR)) {
                // scc 25
                roleList.add(new LabelValueBean(new Trustee().getDisplayName(), Trustee.ID));
                roleList.add(new LabelValueBean(new AuthorizedSignor().getDisplayName(), AuthorizedSignor.ID));
                roleList.add(new LabelValueBean(new AdministrativeContact().getDisplayName(), AdministrativeContact.stringID));
                roleList.add(new LabelValueBean(new PayrollAdministrator().getDisplayName(), PayrollAdministrator.ID));
                roleList.add(new LabelValueBean(new IntermediaryContact().getDisplayName(), IntermediaryContact.ID));
            } else {
                // If the role is a Trustee or Auth Signor add it to the list for read-only purposes
                if (managedRole != null) {
                    if (managedRole instanceof Trustee) {
                        roleList.add(new LabelValueBean(new Trustee().getDisplayName(), Trustee.ID));
                    }
                    if (managedRole instanceof AuthorizedSignor) {
                        roleList.add(new LabelValueBean(new AuthorizedSignor().getDisplayName(), AuthorizedSignor.ID));
                    }
                }
                // scc 24
                roleList.add(new LabelValueBean(new AdministrativeContact().getDisplayName(), AdministrativeContact.stringID));
                roleList.add(new LabelValueBean(new PayrollAdministrator().getDisplayName(), PayrollAdministrator.ID));
                roleList.add(new LabelValueBean(new IntermediaryContact().getDisplayName(), IntermediaryContact.ID));
            }
        } else {
            // scc 22
            if (loggedInRole instanceof Trustee) {
                roleList.add(new LabelValueBean(new Trustee().getDisplayName(), Trustee.ID));
                roleList.add(new LabelValueBean(new AuthorizedSignor().getDisplayName(), AuthorizedSignor.ID));
                roleList.add(new LabelValueBean(new AdministrativeContact().getDisplayName(), AdministrativeContact.stringID));
                roleList.add(new LabelValueBean(new PayrollAdministrator().getDisplayName(), PayrollAdministrator.ID));
                roleList.add(new LabelValueBean(new IntermediaryContact().getDisplayName(), IntermediaryContact.ID));
            }
            // scc 23
            if (loggedInRole instanceof AuthorizedSignor || loggedInRole instanceof AdministrativeContact) {
                roleList.add(new LabelValueBean(new AdministrativeContact().getDisplayName(), AdministrativeContact.stringID));
                roleList.add(new LabelValueBean(new PayrollAdministrator().getDisplayName(), PayrollAdministrator.ID));
            }

        }
        
        return roleList;
    }

    /**
     * Checking has at least one contract is business converted contract in the contract access list
     * 
     * @param contractAccesses
     * @return
     */
    static boolean hasBusinessConvertedContract(List contractAccesses) {

        for (Iterator i = contractAccesses.iterator(); i.hasNext();) {
            ClientUserContractAccess contract = (ClientUserContractAccess) i.next();
            if (contract.isCbcIndicator()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checking has at least not business converted contract in the contract access list
     * 
     * @param contractAccesses
     * @return
     */
    static boolean hasNonBusinessConvertedContract(List contractAccesses) {

        for (Iterator i = contractAccesses.iterator(); i.hasNext();) {
            ClientUserContractAccess contract = (ClientUserContractAccess) i.next();

            if (!contract.isCbcIndicator() || !contract.isSelectedAccess()) {
                return true;
            }

        }
        return false;
    }

    public static void setLastPermissionFlags(ClientUserContractAccess contractAccess, long managedUserProfileId, Contract contract) throws SystemException {
        String selectedAccessCode = PermissionType.getPermissionCode(PermissionType.SELECTED_ACCESS);

        // Check only client users for these permissions
        String manageExternalUsersCode = PermissionType.getPermissionCode(PermissionType.MANAGE_EXTERNAL_USERS);
        String submissionsAccessCode = PermissionType.getPermissionCode(PermissionType.SUBMISSION_ACCESS);

        // Check client and TPA users for these permissions, in case of who will review CSF is "TPA"
        // Changing the approveWithdrawal to signingAuthority: part of LOANS project
        String signingAuthorityCode = PermissionType.getPermissionCode(PermissionType.SIGNING_AUTHORITY);
        String reviewWithdrawalsCode = PermissionType.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS);
        
        // Checking the client users for the last review loans permission
        String reviewLoansCode = PermissionType.getPermissionCode(PermissionType.REVIEW_LOANS);

        boolean checkSigningAuthority = false;
        boolean checkReviewWithdrawals = false;
        boolean checkReviewLoans = false;
        String whoWillReviewWithdrawals = "";
        String whoWillReviewLoans = "";

        boolean checkManageExternalUsers = (contractAccess.isNewContract() && contractAccess.getUserPermissions().isShowManageUsers())
        || (!contractAccess.isNewContract() && contractAccess.getOriginalUserPermissions().isManageUsers());
        
        boolean checkSubmissionsAccess = (contractAccess.isNewContract() && contractAccess.getUserPermissions().isShowViewSubmissions())
        || (!contractAccess.isNewContract() && contractAccess.getOriginalUserPermissions().isViewSubmissions());


        // clientPermissionList to be built to be checked only for the permissions need to be checked on client users 
        List<String> clientPermissionList = new ArrayList<String>();
        if (checkManageExternalUsers) {
        	clientPermissionList.add(manageExternalUsersCode);
        }
        if (checkSubmissionsAccess) {
        	clientPermissionList.add(submissionsAccessCode);
        }        

        // checking the client users for if any one is the last user with manageExternalUsers or submissions access permission
        if (!clientPermissionList.isEmpty()) {
            clientPermissionList.add(selectedAccessCode);
        }

        // Map that holds the client users only data
        Map clientPermissionMap = null;
        
        List<String> clientTPAPermissionList = null;
        
        if (contract.isBusinessConverted()) {
        	
        	// building the permission list that needs to be checked for both client and TPA users
	        clientTPAPermissionList = new ArrayList<String>();
	                    
        	checkSigningAuthority = (contractAccess.isNewContract() && contractAccess.getUserPermissions().isShowSigningAuthority())
                || (!contractAccess.isNewContract() && contractAccess.getOriginalUserPermissions().isSigningAuthority());

	        if (checkSigningAuthority) {
	            clientTPAPermissionList.add(signingAuthorityCode);
	        }

	        ContractServiceFeature withdrawalsCSF = contract.getServiceFeatureMap().get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
            if (withdrawalsCSF != null) {
                if (ContractServiceFeature.internalToBoolean(withdrawalsCSF.getValue())) {
                    if (ContractServiceFeature.internalToBoolean(withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_REVIEW))) {
                        checkReviewWithdrawals = (contractAccess.isNewContract() && contractAccess.getUserPermissions().isShowReviewWithdrawals())
                                || (!contractAccess.isNewContract() && contractAccess.getOriginalUserPermissions().isReviewWithdrawals());
                        whoWillReviewWithdrawals = withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS);
                    }
                }
            }
	        if (checkReviewWithdrawals) {
	            clientTPAPermissionList.add(reviewWithdrawalsCode);
	            clientPermissionList.add(reviewWithdrawalsCode);
	        }

            // determine if we need to check for the last user with review loans permission
            // For loans, there is always a review stage. so if the whoWillReviewLoanRequestsPriorToApproval is not null then
            // we need to check for the last user with review loans permission
            ContractServiceFeature loansCSF = contract.getServiceFeatureMap().get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
            if (loansCSF != null) {
                if (ContractServiceFeature.internalToBoolean(loansCSF.getValue())) {
                	if (loansCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS) != null) {
                        checkReviewLoans = (contractAccess.isNewContract() && contractAccess.getUserPermissions().isShowReviewLoans())
                                || (!contractAccess.isNewContract() && contractAccess.getOriginalUserPermissions().isReviewLoans());
                        whoWillReviewLoans = loansCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS);
                	}
                }	
            }
            		
	        if (checkReviewLoans) {
	            clientTPAPermissionList.add(reviewLoansCode);
	            clientPermissionList.add(reviewLoansCode);
	        }
        
        }
        
        // checking the client users for if any one is the last user with
		// manageExternalUsers or submissions access permission
		if (!clientPermissionList.isEmpty()) {
			clientPermissionMap = SecurityServiceDelegate.getInstance()
					.getExternalUsersWithRolePermission(
							contractAccess.getContractNumber(),
							clientPermissionList, null);
		}

		if (checkManageExternalUsers) {
			filterUserList(clientPermissionMap, manageExternalUsersCode,
					selectedAccessCode);
			contractAccess.setLastUserWithManageUsers(isLastUserWithPermission(
					clientPermissionMap, manageExternalUsersCode,
					managedUserProfileId, contractAccess.isNewContract()));
		} else {
			contractAccess.setLastUserWithManageUsers(false);
		}

		if (checkSubmissionsAccess) {
			filterUserList(clientPermissionMap, submissionsAccessCode,
					selectedAccessCode);
			contractAccess
					.setLastUserWithSubmissionsAccess(isLastUserWithPermission(
							clientPermissionMap, submissionsAccessCode,
							managedUserProfileId, contractAccess
									.isNewContract()));
		} else {
			contractAccess.setLastUserWithSubmissionsAccess(false);
		}
	        
		if (contract.isBusinessConverted()) {
			// retrieving the client and TPA users for the other permissions as
			// we need to look for both for last user
			Map clientTPAPermissionMap = null;

			if (!clientTPAPermissionList.isEmpty()) {
				clientTPAPermissionList.add(selectedAccessCode);
				clientTPAPermissionMap = SecurityServiceDelegate.getInstance()
						.getUsersWithRolePermission(
								contractAccess.getContractNumber(),
								clientTPAPermissionList, null);
			}

			if (checkSigningAuthority) {
				filterUserList(clientTPAPermissionMap, signingAuthorityCode,
						selectedAccessCode);
				contractAccess
						.setLastUserWithSigningAuthority(isLastUserWithPermission(
								clientTPAPermissionMap, signingAuthorityCode,
								managedUserProfileId, contractAccess
										.isNewContract()));
			} else {
				contractAccess.setLastUserWithSigningAuthority(false);
			}

			// Need to check both external users and TPA users to check for
			// WHO_WILL_REVIEW loans or withdrawals is "TPA"
			if (checkReviewWithdrawals) {
				// for withdrawals
				if ((ServiceFeatureConstants.WHO_WILL_REVIEW_TPA).equals(whoWillReviewWithdrawals)) {
					filterUserList(clientTPAPermissionMap,
							reviewWithdrawalsCode, selectedAccessCode);
					contractAccess
							.setLastUserWithReviewIWithdrawals(isLastUserWithPermission(
									clientTPAPermissionMap,
									reviewWithdrawalsCode,
									managedUserProfileId, contractAccess
											.isNewContract()));
				} else if ((ServiceFeatureConstants.WHO_WILL_REVIEW_PS).equals(whoWillReviewWithdrawals)) {
					filterUserList(clientPermissionMap, reviewWithdrawalsCode,
							selectedAccessCode);
					contractAccess
							.setLastClientUserWithReviewIWithdrawals((isLastUserWithPermission(
									clientPermissionMap, reviewWithdrawalsCode,
									managedUserProfileId, contractAccess
											.isNewContract())));
				}
			} else {
				contractAccess.setLastUserWithReviewIWithdrawals(false);
				contractAccess.setLastClientUserWithReviewIWithdrawals(false);
			}

			// check if it is last client or TPA with review loans permission
			if (checkReviewLoans) {
				// for LOANS
				if ((ServiceFeatureConstants.WHO_WILL_REVIEW_TPA).equals(whoWillReviewLoans)) {
					filterUserList(clientTPAPermissionMap, reviewLoansCode,
							selectedAccessCode);
					contractAccess
							.setLastUserWithReviewLoansPermission(isLastUserWithPermission(
									clientTPAPermissionMap, reviewLoansCode,
									managedUserProfileId, contractAccess
											.isNewContract()));
				} else if ((ServiceFeatureConstants.WHO_WILL_REVIEW_PS).equals(whoWillReviewLoans)) {
					filterUserList(clientPermissionMap, reviewLoansCode,
							selectedAccessCode);
					contractAccess
							.setLastClientUserWithReviewLoans((isLastUserWithPermission(
									clientPermissionMap, reviewLoansCode,
									managedUserProfileId, contractAccess
											.isNewContract())));
				}
			} else {
				contractAccess.setLastUserWithReviewLoansPermission(false);
				contractAccess.setLastClientUserWithReviewLoans(false);
			}
        }
    }
    
    private static void filterUserList(Map permissionMap, String permissionCode, String filterCode) {
        List userList = (List) permissionMap.get(permissionCode);
        List filterList = (List) permissionMap.get(filterCode);
        if (userList != null && filterList != null) {
            userList.removeAll(filterList);
        }
    }
    
    private static boolean isLastUserWithPermission(Map permissionMap, String permissionCode, long profileId, boolean newContract) throws SystemException {
        boolean isLast = false;
        List userList = (List) permissionMap.get(permissionCode);
        if (userList == null || userList.isEmpty()) {
            isLast = newContract;
        } else {
            if (newContract) {
                isLast = !SecurityServiceDelegate.getInstance().checkUserActive(userList);
            } else {
                Long userProfileId = new Long(profileId);
                if (userList.contains(userProfileId)) {
                    userList.remove(userProfileId);
                    if (userList.isEmpty()) {
                        isLast = true;
                    } else {
                        // Ensure that at least one of the users in the list is active
                        isLast = !SecurityServiceDelegate.getInstance().checkUserActive(userList);
                    }
                }
            }
        }
        return isLast;
    }

    private static boolean isLastUserWithPermission(Map permissionMap, String permissionCode, long profileId) throws SystemException {
        boolean isLast = false;
        List userList = (List) permissionMap.get(permissionCode);
        if (userList == null || userList.isEmpty()) {
            isLast = true;
        } else {
            Long userProfileId = new Long(profileId);
            if (userList.contains(userProfileId)) {
                userList.remove(userProfileId);
            }
            if (userList.isEmpty()) {
                isLast = true;
            } else {
                // Ensure that at least one of the users in the list is active
                isLast = !SecurityServiceDelegate.getInstance().checkUserActive(userList);
            }
        }
        return isLast;
    }

    public static Map<String, Boolean> getLastUserFlags(int contractId, long profileId, boolean newUser) {
        Map<String, Boolean> lastUserFlagMap = new HashMap<String, Boolean>();

        List<String> permissionList = new ArrayList<String>(0);

        String trusteeRoleCode = Trustee.ID;
        List<String> roleList = new ArrayList<String>();
        roleList.add(trusteeRoleCode);
        
        Map permissionMap = null;
        try {
            permissionMap = SecurityServiceDelegate.getInstance().getUsersWithRolePermission(contractId, permissionList, roleList);
            lastUserFlagMap.put(trusteeRoleCode, Boolean.valueOf(isLastUserWithPermission(permissionMap, trusteeRoleCode, profileId, newUser)));
        } catch (SystemException e) {
            logger.warn("Error searching for users on contract " + contractId + " with role/permission.");
        }
        
        return lastUserFlagMap;
    }
    
    public static Collection<GenericException> checkForLockOrDelete(UserProfile loggedInUserProfile, long managedUserProfileId, String managedUserName) throws SystemException {
        Collection<GenericException> errors = new ArrayList<GenericException>();

        if (managedUserName != null && !"".equals(managedUserName)) {
            Long deletedByProfileId = SecurityServiceDelegate.getInstance().getDeletedByProfileId(managedUserName);
            if (deletedByProfileId != null) {
                try {
                    UserInfo deletedByUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(loggedInUserProfile.getPrincipal(), deletedByProfileId.longValue());
                    if (deletedByUserInfo != null) {
                        errors.add(new GenericException(1058, new String[] { deletedByUserInfo.getFirstName(),
                                deletedByUserInfo.getLastName() + (deletedByUserInfo.getRole().isInternalUser() ? " John Hancock Representative" : "") }));
                    } else {
                        errors.add(new GenericException(1058, new String[] { "John Hancock", "Representative" }));
                    }
                } catch (SecurityServiceException e) {
                    throw new SystemException(e, ClientUserContractAccessActionHelper.class.getName(), "checkForLockOrDelete", "Failed to get user info of deleted by. "
                            + e.toString());
                }
            }
        }

        if (errors.isEmpty() && managedUserProfileId != 0) {
            if (!LockServiceDelegate.getInstance().lock(LockHelper.USER_PROFILE_LOCK_NAME, LockHelper.USER_PROFILE_LOCK_NAME + managedUserProfileId,
                    loggedInUserProfile.getPrincipal().getProfileId())) {
                try {
                    Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(LockHelper.USER_PROFILE_LOCK_NAME, LockHelper.USER_PROFILE_LOCK_NAME + managedUserProfileId);

                    UserInfo lockOwnerUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(loggedInUserProfile.getPrincipal(), lockInfo.getLockUserProfileId());

                    String lockOwnerDisplayName = LockHelper.getLockOwnerDisplayName(loggedInUserProfile, lockOwnerUserInfo);
                    errors.add(new GenericException(1057, new String[] { lockOwnerDisplayName }));
                } catch (SecurityServiceException e) {
                    throw new SystemException(e, ClientUserContractAccessActionHelper.class.getName(), "checkForLockOrDelete", "Failed to get user info of lock own. "
                            + e.toString());
                }
            }
        }

        return errors;
    }
    
    /**
     * Helper method to determine if the logged in user can manage all of the contracts on the managed user profile
     * 
     * @param loggedInUserInfo
     * @param managedUserInfo
     * @return
     */
    public static boolean canManageAllContracts(UserInfo loggedInUserInfo, UserInfo managedUserInfo) {
            boolean canManageContract = true;
            for (ContractPermission managedContractPermission : managedUserInfo.getContractPermissions()) {
            if (loggedInUserInfo.getRole().isExternalUser()) {
                ContractPermission loggedInContractPermission = loggedInUserInfo.getContractPermission(managedContractPermission.getContractNumber());
                // Cannot manage if managed user has a contract that logged in user doesn't have access to or has selected access to
                if (loggedInContractPermission == null || loggedInContractPermission.isSelectedAccess()) {
                    canManageContract = false;
                } else {
                    if (loggedInContractPermission.getRole() instanceof PlanSponsorUser) {
                        // Cannot manage a user if logged in user doesn't have manage users permission or managed user does have manage users permission
                        if (!loggedInContractPermission.isManageExternalUsers() || managedContractPermission.isManageExternalUsers()) {
                            canManageContract = false;
                        }
                    } else {
                        // Cannot manage if managed user has a role the logged in user can't manage
                        canManageContract = canAccessBusinessConvertedContract(loggedInContractPermission.getRole(), managedContractPermission);
                        }
                    }
            } else if (loggedInUserInfo.getRole().isInternalUser()) {
                if (managedContractPermission.getRole() instanceof PlanSponsorUser) {
                    canManageContract = loggedInUserInfo.getRole().hasPermission(PermissionType.MANAGE_EXTERNAL_USERS);
                } else {
                    canManageContract = canAccessBusinessConvertedContract(loggedInUserInfo.getRole(), managedContractPermission);
                }
                }

                if (!canManageContract) {
                    break;
                }
            }
        return canManageContract;
    }

	public static List<ClientUserContractAccess> buildContractAccessesForLoggedInContract(UserProfile loggedInUser, 
			UserInfo loggedInUserInfo, UserInfo managedUserInfo, List<Integer> cannotManageRoleContracts, int contractNumber) throws SystemException {
		// TODO Auto-generated method stub

        List<ClientUserContractAccess> contractAccesses = new ArrayList<ClientUserContractAccess>();
        // managed user contract permissions
        ContractPermission[] permissions = managedUserInfo.getContractPermissions();
        /*
         * Sort managed user's contracts by contract number.
         */
        Arrays.sort(permissions, new Comparator() {
            public int compare(Object o1, Object o2) {
                ContractPermission bean1 = (ContractPermission) o1;
                ContractPermission bean2 = (ContractPermission) o2;
                Integer contractNumber1 = new Integer(bean1.getContractNumber());
                Integer contractNumber2 = new Integer(bean2.getContractNumber());
                return contractNumber1.compareTo(contractNumber2);
            }
        });
        // build contract access list
        for (int i = 0; i < permissions.length; i++) {
            if (logger.isDebugEnabled()) {
                logger.debug("permission:" + permissions[i].toString());
            }
            
            ClientUserContractAccess contractAccess = new ClientUserContractAccess();
            
            if ((contractNumber == 0) && (permissions[i].getContractNumber() == loggedInUser.getCurrentContract().getContractNumber())
            		|| ((contractNumber != 0) && (permissions[i].getContractNumber() == contractNumber))) {

            if (loggedInUser.getRole().isInternalUser()) {
                // Internal user display all managed user's contracts. DFS11 SVC 58 & DFS 9 MPR 206
                populateContractAccess(contractAccess, loggedInUser.getRole(), permissions[i], managedUserInfo, getContract(managedUserInfo.getRole(), permissions[i]
                        .getContractNumber()));
            } else {
                if (loggedInUser.getRole().isExternalUser()) {
                    ContractPermission loggedInUserPermission = loggedInUserInfo.getContractPermission(permissions[i].getContractNumber());
                    if (loggedInUserPermission != null) {
                        UserRole loggedInContractRole = loggedInUserPermission.getRole();
                        Contract contract = getContract(managedUserInfo.getRole(), permissions[i].getContractNumber());
                        if (!contract.isBusinessConverted()) {
                            // login contract is not business converted
                            if (AccessLevelUtility.canManageExternal(loggedInContractRole) && !AccessLevelUtility.canManageExternal(permissions[i].getRole())) {
                                // add contract if login user can manage external & managed user can not
                                // manage user DFS11 SVC59 & DFS 9 SCC2
                                populateContractAccess(contractAccess, loggedInContractRole, permissions[i], managedUserInfo, contract);
                            } else {
                                // do not add contract
                                continue;
                            }

                        } else {
                            // login contract is business ovnverted
                            // DFS 11 SVC 60 & DFS9 scc 3
                            if (canAccessBusinessConvertedContract(loggedInContractRole, permissions[i])) {
                                populateContractAccess(contractAccess, loggedInContractRole, permissions[i], managedUserInfo, contract);
                            } else {
                                cannotManageRoleContracts.add(contract.getContractNumber());
                                // don't add contract
                                continue;
                            }
                        }
                    } else {
                        // logged in user has no access to this contract
                        continue;
                    }
                } else {
                    continue;
                }
            }
            contractAccesses.add(contractAccess);
            break;
        }
        }

        return contractAccesses;

    
	}

	public static ClientUserContractAccess findContractAccess(int contractNumber, UserProfile userProfile,
			UserInfo loginUserInfo, UserInfo managedUserInfo) throws SystemException {
		// TODO Auto-generated method stub
		
		ClientUserContractAccess access = new ClientUserContractAccess();
		
		// managed user contract permissions
        ContractPermission[] permissions = managedUserInfo.getContractPermissions();
        
        /*
         * Sort managed user's contracts by contract number.
         */
        Arrays.sort(permissions, new Comparator() {
            public int compare(Object o1, Object o2) {
                ContractPermission bean1 = (ContractPermission) o1;
                ContractPermission bean2 = (ContractPermission) o2;
                Integer contractNumber1 = new Integer(bean1.getContractNumber());
                Integer contractNumber2 = new Integer(bean2.getContractNumber());
                return contractNumber1.compareTo(contractNumber2);
            }
        });
        
        // build contract access list
        for (int i = 0; i < permissions.length; i++) {
            
        	if (logger.isDebugEnabled()) {
                logger.debug("permission:" + permissions[i].toString());
            }
            
        	if(contractNumber == permissions[i].getContractNumber()) {

                if (userProfile.getRole().isInternalUser()) {
                    // Internal user display all managed user's contracts. DFS11 SVC 58 & DFS 9 MPR 206
                    populateContractAccess(access, userProfile.getRole(), permissions[i], managedUserInfo, getContract(managedUserInfo.getRole(), permissions[i]
                            .getContractNumber()));
                } else {
                    if (userProfile.getRole().isExternalUser()) {
                        ContractPermission loggedInUserPermission = loginUserInfo.getContractPermission(permissions[i].getContractNumber());
                        if (loggedInUserPermission != null) {
                            UserRole loggedInContractRole = loggedInUserPermission.getRole();
                            Contract contract = getContract(managedUserInfo.getRole(), permissions[i].getContractNumber());
                            if (!contract.isBusinessConverted()) {
                                // login contract is not business converted
                                if (AccessLevelUtility.canManageExternal(loggedInContractRole) && !AccessLevelUtility.canManageExternal(permissions[i].getRole())) {
                                    // add contract if login user can manage external & managed user can not
                                    // manage user DFS11 SVC59 & DFS 9 SCC2
                                    populateContractAccess(access, loggedInContractRole, permissions[i], managedUserInfo, contract);
                                } else {
                                    // do not add contract
                                    continue;
                                }

                            } else {
                                // Login contract is business converted
                                // DFS 11 SVC 60 & DFS9 SCC 3
                                if (canAccessBusinessConvertedContract(loggedInContractRole, permissions[i])) {
                                    populateContractAccess(access, loggedInContractRole, permissions[i], managedUserInfo, contract);
                                } else {
                                    // don't add contract
                                    continue;
                                }
                            }
                        } else {
                            // logged in user has no access to this contract
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                break;
            }
        }
		return access;
	}

	public static void populateContractDropDown( AutoForm actionForm,
			HttpServletRequest request, List<Integer> contractIdList) throws SystemException {
        // get login user profile
        UserProfile userProfile = PsController.getUserProfile(request);
        AddEditClientUserForm form = (AddEditClientUserForm) actionForm;
        UserRole userRole = userProfile.getRole();

        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				PsController.getUserProfile(request).getPrincipal());

        /*
         * MPR 20 & MPR 21. Show drop down list of contracts if logged in user is an PSEUM and has multiple contracts.
         */
        if (userRole.isExternalUser() && userRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)) {
            // && userProfile.isMultipleContracts()) {

            ContractPermission[] contracts = loginUserInfo.getContractPermissions();

            /*
             * The labels are sorted by contract IDs in ascending order.
             */
            Set contractLabelValueBeans = new TreeSet(new Comparator() {
                public int compare(Object o1, Object o2) {
                    LabelValueBean bean1 = (LabelValueBean) o1;
                    LabelValueBean bean2 = (LabelValueBean) o2;
                    Integer contractNumber1 = new Integer(bean1.getValue());
                    Integer contractNumber2 = new Integer(bean2.getValue());
                    return contractNumber1.compareTo(contractNumber2);
                }
            });

            /*
             * Obtains the list of contracts that the edited user is a PSEUM of.
             */
            List editUserPseumContracts;
           //TODO need to fix it
          /*  if (!AbstractAddEditUserAction.isAddUser(mapping)) {
                editUserPseumContracts = getExternalUserManagerContracts(form.getUserName());
            } else {*/
                editUserPseumContracts = new ArrayList();
           /* }*/

            for (int i = 0; i < contracts.length; i++) {

                /*
                 * Drop down should only contains contracts that the login user is a PSEUM of.
                 */
                UserRole contractUserRole = contracts[i].getRole();

                if (!(contractUserRole.isExternalUser() && contractUserRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS))) {
                    continue;
                }

                int contractId = contracts[i].getContractNumber();

                /*
                 * Also, if the edited user is a PSEUM of any contract, the drop down should not show the contract.
                 */
                if (editUserPseumContracts.contains(new Integer(contractId)) 
                		|| (userProfile.getCurrentContract() != null 
                				&& userProfile.getCurrentContract().getContractNumber() == contractId)) {
                    continue;
                }
                /*
                 * Check contract status skill PS, DC, PC
                 */
                try{
                    Contract contract =  ContractServiceDelegate.getInstance().getContractDetails(contracts[i].getContractNumber(), 6);
                    if (contract.getStatus().equals(Contract.STATUS_PENDING_CONTRACT_APPROVAL) || contract.getStatus().equals(Contract.STATUS_DETAILS_COMPLETED)||
                            contract.getStatus().equals(Contract.STATUS_PROPOSAL_SIGNED)){
                        continue;
                    }

                } catch (ContractNotExistException e){
                    throw new SystemException(e.getMessage());
                }
                
                // Skip any contracts that the logged in user doesn't have access to that the managed user already has
                if (form.getCannotManageRoleContracts() != null && form.getCannotManageRoleContracts().contains(contractId)) {
                    continue;
                }
                
                /*
                 * Skip all contracts that is already in the form.
                 */
                boolean contractFound = false;

                for (Iterator<Integer> it2 = contractIdList.iterator(); it2.hasNext() && !contractFound;) {
                    Integer contractNumber = (Integer) it2.next();
                    if (contractNumber.intValue() == contractId) {
                        contractFound = true;
                    }
                }

                if (contractFound) {
                    continue;
                }

                String label = contractId + " " + contracts[i].getCompanyName();

                contractLabelValueBeans.add(new LabelValueBean(label, String.valueOf(contractId)));
            }

            /*
             * Add an empty label.
             */
            List contractList = new ArrayList();
            if (contractLabelValueBeans.size() > 0) {
                contractList.add(new LabelValueBean(SELECT_ONE_CONSTANTS,Empty_String));
                contractList.addAll(contractLabelValueBeans);
                request.setAttribute(Constants.ADD_EDIT_USER_CONTRACT_LIST, contractList);
            }
        }
    }

	public static List<Integer> getContractIdList( AddEditClientUserForm form, 
			HttpServletRequest request, UserProfile userProfile, ContractPermission[] permissions) {
		// TODO Auto-generated method stub
		// Get user name by using the profile id
  		
		List<Integer> contractIdList = new ArrayList<Integer>();
  		
        // build contract access list
        for (int i = 0; i < permissions.length; i++) {
        	contractIdList.add(permissions[i].getContractNumber());
        }
  		
  		return contractIdList;
	}
	
	/**
	 * This method is used to check the SEND Service is ON or not in Contract Service Features page
	 * @param contractAccess
	 * @param contractId
	 * @throws SystemException 
	 */
	public static void loadSendServiceFeatures(ClientUserContractAccess contractAccess, int contractId, Contract contract) throws SystemException{

		NoticePlanCommonVO noticePlanCommonVO = null;
		try {
			noticePlanCommonVO = ContractServiceDelegate.getInstance().readNoticePlanCommonData(contractId);
		} catch (SystemException e) {
			logger.error(e);
		}
		if(noticePlanCommonVO != null){
			Map csfMap = null;
			ContractServiceDelegate service = ContractServiceDelegate.getInstance();
			try {
				csfMap = service.getContractServiceFeatures(contractId);
			} catch (ApplicationException e) {
				logger.error(e);
			}
			//Notice Generation service - To load the CSF page with selected values
			if(csfMap != null && csfMap.size() != 0){
				if(isICCDesignateEligible(contractId) && FeeDisclosureUtility.isPinpoinContract(contractId) && !contract.isBundledGaIndicator()){
					contractAccess.setSendServiceDesignateEligible(true);
				}
				else{
					contractAccess.setSendServiceDesignateEligible(false);
				}
			} else {
				contractAccess.setSendServiceDesignateEligible(false);
			}
		}
	}

}

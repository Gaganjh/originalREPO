package com.manulife.pension.service.security;

import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.CAR;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.PlanSponsorUser;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.SuperCAR;
import com.manulife.pension.service.security.role.TPAUserManager;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.UserRoleFactory;

/**
 * MockPrincipalFactory is used for unit testing, where a principal is required.
 */
public class MockPrincipalFactory {

    private static MockPrincipalFactory testPrincipalFactory = null;

    /**
     * TEST_USER_ROLE_CODE_CAR
     */
    public static final String TEST_USER_ROLE_CODE_CAR = new CAR().toString();

    /**
     * TEST_USER_ROLE_CODE_PAYROLL_ADMINSTRATOR
     */
    public static final String TEST_USER_ROLE_CODE_PAYROLL_ADMINSTRATOR = new PayrollAdministrator()
            .toString();

    /**
     * TEST_USER_ROLE_CODE_PLAN_ADMINSTRATOR
     */
    public static final String TEST_USER_ROLE_CODE_PLAN_SPONSOR_USER = new PlanSponsorUser()
            .toString();

    /**
     * TEST_USER_ROLE_CODE_RELATIONSHIP_MANAGER
     */
    public static final String TEST_USER_ROLE_CODE_RELATIONSHIP_MANAGER = new RelationshipManager()
            .toString();

    /**
     * TEST_USER_ROLE_CODE_TEAM_LEAD
     */
    public static final String TEST_USER_ROLE_CODE_TEAM_LEAD = new TeamLead().toString();

    /**
     * TEST_USER_ROLE_CODE_SUPER_CAR
     */
    public static final String TEST_USER_ROLE_CODE_SUPER_CAR = new SuperCAR().toString();

    /**
     * TEST_USER_ROLE_CODE_TPA
     */
    public static final String TEST_USER_ROLE_CODE_TPA = new ThirdPartyAdministrator().toString();

    /**
     * TEST_USER_ROLE_CODE_INTERNAL_USER_MANAGER
     */
    public static final String TEST_USER_ROLE_CODE_INTERNAL_USER_MANAGER = new InternalUserManager()
            .toString();

    /**
     * TEST_USER_ROLE_CODE_TPA_USER_MANAGER
     */
    public static final String TEST_USER_ROLE_CODE_TPA_USER_MANAGER = new TPAUserManager()
            .toString();

    /**
     * getInstance provides a singleton to the factory.
     * 
     * @return MockPrincipalFactory The factory.
     */
    public static MockPrincipalFactory getInstance() {
        if (testPrincipalFactory == null) {
            testPrincipalFactory = new MockPrincipalFactory();
        } // fi
        return testPrincipalFactory;
    }

    /**
     * Default Constructor.
     */
    private MockPrincipalFactory() {
        super();
    }

    /**
     * getPrincipal is used to get a principal for testing.
     * 
     * @param profileId The profile ID.
     * @param userRole The {@link UserRole}.
     * @param userName The user name.
     * @param firstName The first name.
     * @param lastName The last name.
     * @return {@link Principal} The principal.
     */
    public Principal getPrincipal(final long profileId, final UserRole userRole,
            final String userName, final String firstName, final String lastName) {
        return new Principal(profileId, userRole, userName, firstName, lastName);
    }

    /**
     * getPrincipal is used to get a principal for testing.
     * 
     * @param profileId The profile ID.
     * @param userRoleCode The key used to lookup a {@link UserRole} with {@link UserRoleFactory}.
     * @param userName The user name.
     * @param firstName The first name.
     * @param lastName The last name.
     * @return {@link Principal} The principal.
     */
    public Principal getPrincipal(final long profileId, final String userRoleCode,
            final String userName, final String firstName, final String lastName) {
        return new Principal(profileId, UserRoleFactory.getUserRole(userRoleCode), userName,
                firstName, lastName);
    }

}

package com.manulife.pension.ps.service.report.profiles.reporthandler;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.dao.SecurityReportDAO;

/**
 * Business team code lookup
 * 
 * @author Steven Wang
 */
public class BusinessTeamCodeLookup {

    private static final Logger logger = Logger.getLogger(BusinessTeamCodeLookup.class);

    public static Collection<String> getBusinessTeamCodes() throws SystemException {
        return SecurityReportDAO.getBusinessTeamCodes();

    }
}
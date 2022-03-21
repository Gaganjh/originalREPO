package com.manulife.pension.ireports.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;

public class FundDAO extends BaseDatabaseDAO {

    private static final Logger logger = Logger.getLogger(FundDAO.class);

    private static final String SQL_SELECT_CONTRACT_FUNDS_FOR_IREPORT = " select "
    	+ "     RTRIM(f.investmentid) as investmentid "
    	+ "   , RTRIM(f.assetcls) as asset_class "
    	+ "   , cf.ratetype" 
    	+ "   , fm.nml_fund_ind "
    	+ "   , fm.fund_family_cd "    	
    	+ "   , f.fund_family_category_cd as fundFamilyCategoryCode " 
    	+ " from "
    	+ "     PSW100.fund_base f "
    	+ "   , PSW100.contractfunds cf "
    	+ "   , PSW100.UNDERLYING_FUND fm"
    	+ " where	cf.contractnumber = ? "
    	+ " and		f.investmentid = cf.fund_investmentid "
    	+ " and      f.fund_cd = fm.fund_cd";
    

     FundDAO() {
    }

    public Collection<Fund> retrieveFundsByContractNumber(final String contractNumber)
    {
        Collection<Fund> funds = new ArrayList<Fund>();
        logger.info("Start of retrieveFundsByContractNumber().");
        StoredProcedureHandler handler = new StoredProcedureHandler(
                VIEW_FUNDS_DATA_SOURCE_NAME,
                SQL_SELECT_CONTRACT_FUNDS_FOR_IREPORT,
                new StoredProcedureHandler.OutputDefinition[] { new StoredProcedureHandler.BeanOutputDefinition(
                        Fund.class, new String[] { "investmentid", "assetcls", "ratetype",
                        "nmlFundInd", "fundFamilyCd", "fundFamilyCategoryCode" }, StoredProcedureHandler.MANY) });

        Object[] objects = null;
        try {
            objects = handler.execute(new Object[] { getContractId(contractNumber) });
            funds = Arrays.asList((Fund[]) objects[0]);
        } catch (DAOException e) {
			logger.error("error fetching the Funds for a Contract Number.");
			throw new ReportDAOException("DAO exception occured", e);
        }
        return funds;
    }
}

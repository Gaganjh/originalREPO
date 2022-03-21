package com.manulife.pension.ireports.dao;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.ireports.model.Contract;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;

public class ContractDAO extends BaseDatabaseDAO {

    private static final Logger logger = Logger.getLogger(ContractDAO.class);

    private static final String SQL_SELECT_CONTRACT_BY_CONTRACT_NUMBER = " call "
            + "VF100.SELECT_CONTRACT_BY_CONTRACTNUMBER(?)";
    
    ContractDAO() {
    }

    public Contract retrieveContractByContractNumber(final String contractNumber) {

        logger.info("Start of retrieveContractByContractNumber().");

        Contract result = null;

        if (StringUtils.isEmpty(contractNumber)) {
            throw new IllegalArgumentException("Empty contract number");
        }

        StoredProcedureHandler handler = new StoredProcedureHandler(
                VIEW_FUNDS_DATA_SOURCE_NAME,
                SQL_SELECT_CONTRACT_BY_CONTRACT_NUMBER,
                new StoredProcedureHandler.OutputDefinition[] { new StoredProcedureHandler.BeanOutputDefinition(
                        Contract.class, new String[] { "contractNumber", "productId",
                                "subsidiaryCode", "fundPackageSeries" }, StoredProcedureHandler.ONE) });

        Object[] objects = null;
        try {
            objects = handler.execute(new Object[] { getContractId(contractNumber) });
            result = (Contract) objects[0];
        } catch (DAOException e) {
           logger.error("error executing strored procedure SELECT_CONTRACT_BY_CONTRACTNUMBER ");
           throw new ReportDAOException("DAO exception occured", e);
        }
        if (result != null) {
            Collection<Fund> packagedFunds = new FundDAO()
            .retrieveFundsByContractNumber(contractNumber);
            result.setFunds(packagedFunds);
        }
        return result;
    }
}

package com.manulife.pension.ps.service.report.investment.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationTotals;
import com.manulife.pension.ps.service.report.investment.valueobject.ContractFundDetails;
import com.manulife.pension.ps.service.report.investment.valueobject.FundCategory;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.fund.valueobject.AssetClassVO;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class InvestmentAllocationDAO extends BaseDatabaseDAO{

	private static final String className = InvestmentAllocationDAO.class.getName();
	static {
		try {
			Class.forName(FundInfoCache.class.getName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private enum ORGANIZING_OPTION {
        AssetClass, RiskCategory
     };
	
	private static final String GET_INVESTMENTS_ALLOCATION_DETAILS =
        "call " + CUSTOMER_SCHEMA_NAME +"GET_INVESTMENT_DTL(?,?,?,?)";
	
    private static final String GET_FUNDS_BY_CATAGORY =
        "call " + "VF100." +"SELECT_CONTRACT_FUNDS_BY_CATEGORY(?,?)";

    private static final String GET_FUNDS_FOR_CONTRACT_HISTORY =
        "call " + "VF100." +"SELECT_CONTRACT_FUNDS_HISTORY(?,?)";
    
    private static final String SQL_GET_FUNDS_COUNT_FOR_AS_OF_DATE = "SELECT  COUNT(SELECTEDFLAG) FROM "
            + "PSW100"
            + "."
            + "CONTRACT_FUND_HISTORY WHERE CONTRACTNUMBER = ? "
            + "AND START_DATE <= ? AND END_DATE >= ? AND SELECTEDFLAG='Y'";

	private java.sql.Date asOfDate = null;
	private	java.sql.Date currentDate = null;

    public ReportData getReportData(ReportCriteria criteria) throws SystemException  {

    	//Date
    	String s_asOfDate = (String)criteria.getFilterValue(InvestmentAllocationReportData.FILTER_ASOFDATE_REPORT);
    	if (s_asOfDate != null)
    		asOfDate = new java.sql.Date(Long.valueOf(s_asOfDate).longValue());

		String s_currentDate = (String)criteria.getFilterValue(InvestmentAllocationReportData.FILTER_CURRENTDATE);
    	if (s_currentDate != null)
    		currentDate = new java.sql.Date(Long.valueOf(s_currentDate).longValue());

        //Contract Number
        Integer contractNumber = (Integer)criteria.getFilterValue(InvestmentAllocationReportData.FILTER_CONTRACT_NO);

		//PBA
		Boolean isPBA = (Boolean)criteria.getFilterValue(InvestmentAllocationReportData.FILTER_ISPBA);

		//Site location
		String siteLocation = (String)criteria.getFilterValue(InvestmentAllocationReportData.FILTER_SITE);

		//view option defaults to asset view
		String viewOption = (String)criteria.getFilterValue(InvestmentAllocationReportData.FILTER_VIEW_OPTION);
		if (viewOption == null)
			viewOption = "0";

		ORGANIZING_OPTION organizeByOption ;
		String organizedOption = (String)criteria.getFilterValue(InvestmentAllocationReportData.FILTER_ORGANIZING_OPTION);
		if (organizedOption != null
                && Constants.ORGANIZE_BY_ASSET_CLASS.equals(organizedOption.trim())) {
			organizeByOption = ORGANIZING_OPTION.AssetClass;
		}  else {
			organizeByOption = ORGANIZING_OPTION.RiskCategory;
		}
		
        int totalCount = 1; //since paging is not required for this report the count is hard coded as 1

        InvestmentAllocationReportData reportData = new InvestmentAllocationReportData(criteria, totalCount);

        Connection psconnection = null;
        Connection vfconnection = null;
        CallableStatement stmt_investmentAllocation = null;
        CallableStatement stmt_fundCatagories = null;
        CallableStatement stmt_fundContract = null;
		ResultSet investmentTotalsRs = null;
		ResultSet investmentDetailsRs = null;
		ResultSet fundsByCatagoryRs0 = null;
		ResultSet fundsByCatagoryRs1 = null;
		ResultSet fundsByCatagoryRs2 = null;
		ResultSet fundsByContractRs = null;

		StringBuffer lifecycle = new StringBuffer();
		StringBuffer lifestyle = new StringBuffer();
		StringBuffer pba = new StringBuffer();
		StringBuffer all = new StringBuffer();
		Hashtable fullFundList = new Hashtable();

        try {
            psconnection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            vfconnection = getReadUncommittedConnection(className, FUNDS_DATA_SOURCE_NAME);
            
            reportData.setNumberOfFundsSelected(getNumberOfFundsSelected(vfconnection,
                    contractNumber, asOfDate));

            //funds in a particular catagory
            stmt_fundCatagories = vfconnection.prepareCall(GET_FUNDS_BY_CATAGORY);
            stmt_fundCatagories.setBigDecimal(1, new BigDecimal(contractNumber.intValue()));
            stmt_fundCatagories.registerOutParameter(2, Types.DECIMAL);
            stmt_fundCatagories.execute();

        	fundsByCatagoryRs0 = stmt_fundCatagories.getResultSet();
        	
        	if (fundsByCatagoryRs0 != null) {
        	    
            	while (fundsByCatagoryRs0.next()) {
            		lifecycle.append("'"+fundsByCatagoryRs0.getString(1).trim()+"',");
                }
            	
        	}
        	
            stmt_fundCatagories.getMoreResults();
        	fundsByCatagoryRs1 = stmt_fundCatagories.getResultSet();
        	
        	if (fundsByCatagoryRs1 != null) {
        	    
            	while (fundsByCatagoryRs1.next()) {
            		lifestyle.append("'"+fundsByCatagoryRs1.getString(1).trim()+"',");
                }
            	
        	}
        	
        	stmt_fundCatagories.getMoreResults();
        	fundsByCatagoryRs2 = stmt_fundCatagories.getResultSet();
        	
        	if (fundsByCatagoryRs2 != null) {
        	    
            	while (fundsByCatagoryRs2.next()) {
            		pba.append("'"+fundsByCatagoryRs2.getString(1).trim()+"',");
            	}
            	
        	}
        	

            //funds for the contract
            // if the report is for current date - default option - then
            // quiery funds from historical data, so the we can have ratetypes for the funds
            // which are no longer selected by the contract
            //
           // if (currentDate.equals(asOfDate))
            stmt_fundContract = vfconnection.prepareCall(GET_FUNDS_FOR_CONTRACT_HISTORY);
           // else // funds can be taken from current contract_funds
            //	stmt_fundContract = vfconnection.prepareCall(GET_FUNDS_FOR_CONTRACT);
            stmt_fundContract.setBigDecimal(1, new BigDecimal(contractNumber.intValue()));
            stmt_fundContract.registerOutParameter(2, Types.DECIMAL);
            stmt_fundContract.execute();

        	fundsByContractRs = stmt_fundContract.getResultSet();
        	
        	if (fundsByContractRs != null) {
        	    
    			while (fundsByContractRs.next()){
    
            		all.append("'"+fundsByContractRs.getString(1).trim()+"',");
            		String fundID = fundsByContractRs.getString(1).trim();
            		String marketingOrder = fundsByContractRs.getString(3).trim();
            		String rateType = fundsByContractRs.getString(2).trim();
            		String selectedFlag =fundsByContractRs.getString(4).trim();
            		Date endDate =fundsByContractRs.getDate(5);
            		ContractFundDetails contractFundDetails =new ContractFundDetails(fundID, marketingOrder, rateType, selectedFlag, endDate);
            		fullFundList.put(fundID,contractFundDetails);
            	}
    			
        	}
        	

			//allocation details
            stmt_investmentAllocation = psconnection.prepareCall(GET_INVESTMENTS_ALLOCATION_DETAILS);
        	stmt_investmentAllocation.setBigDecimal(1, new BigDecimal(contractNumber.doubleValue())); 	//contract number
        	stmt_investmentAllocation.setDate(2, asOfDate );								//as of date								//pba fund ids
			stmt_investmentAllocation.setString(3, viewOption);
			stmt_investmentAllocation.registerOutParameter(4, Types.DECIMAL);
        	stmt_investmentAllocation.execute();


        	stmt_investmentAllocation.getMoreResults();
        	investmentTotalsRs = stmt_investmentAllocation.getResultSet();
        	AllocationTotals[] allocTotals = populateTotals(investmentTotalsRs, isPBA);

        	stmt_investmentAllocation.getMoreResults();
        	investmentDetailsRs = stmt_investmentAllocation.getResultSet();
        	populateDetails(reportData,investmentDetailsRs, fullFundList, isPBA, siteLocation, organizeByOption);

        	//Add the allocation totals in proper order
    		if(allocTotals[0] != null)
    			reportData.addAllocationTotal(allocTotals[0]);	//NON-Lifecycle-lifestyle
    		if(allocTotals[1] != null) {
    			//This is hack because the stored proc is returning an row with zeros,
    			//even if there are no lifecycle funds associated with the contract.
    			java.util.Map allocDetailsMap = reportData.getAllocationDetails();
    			java.util.Iterator it = allocDetailsMap.keySet().iterator();
    			boolean hasLC = false;
    			while(it.hasNext()) {
    				FundCategory fc = (FundCategory)it.next();
    				if ("LC".equals(fc.getCategoryCode())
                            || Constants.ASSET_CLASS_LIFECYCLE.equals(fc.getCategoryCode())) {
    					hasLC = true;
    					break;
    				}
    			}
    				//System.out.println("Category = " + ((FundCategory)it.next()).toString());

    			if(hasLC)
    				reportData.addAllocationTotal(allocTotals[1]);	//Lifecycle
    		}
 
    		if(allocTotals[2] != null) {
    			//This is hack because the stored proc is returning an row with zeros,
    			//even if there are no lifestyle funds associated with the contract.
    			java.util.Map allocDetailsMap = reportData.getAllocationDetails();
    			java.util.Iterator it = allocDetailsMap.keySet().iterator();
    			boolean hasLS = false;
    			while(it.hasNext()) {
    				FundCategory fc = (FundCategory)it.next();
    				if ("LS".equals(fc.getCategoryCode())
                            || Constants.ASSET_CLASS_LIFESTYLE.equals(fc.getCategoryCode())) {
    					hasLS = true;
    					break;
    				}
    			}
    				//System.out.println("Category = " + ((FundCategory)it.next()).toString());

    			if(hasLS)
    				reportData.addAllocationTotal(allocTotals[2]);	//Lifestyle
    		}

       		if(allocTotals[3] != null)
       		{
       		java.util.Map allocDetailsMap = reportData.getAllocationDetails();
			java.util.Iterator it = allocDetailsMap.keySet().iterator();
			boolean hasGW = false;
			while(it.hasNext()) {
				FundCategory fc = (FundCategory)it.next();
				if ("GW".equals(fc.getCategoryCode())
                            || Constants.ASSET_CLASS_GIFL.equals(fc.getCategoryCode())) {
					hasGW = true;
					break;
				}
			}
			if(hasGW)
				reportData.addAllocationTotal(allocTotals[3]);	//GIFL
       		}
			if(allocTotals[4] != null) {
    			if(	isPBA != null && isPBA.booleanValue())
    				reportData.addAllocationTotal(allocTotals[4]);	//PBA
    			else if(isPBA != null && isPBA.booleanValue() && !currentDate.equals(asOfDate)) {
    				//Only display if values are not zero
    				if(!(allocTotals[4].getEmployeeAssets() == 0 && allocTotals[4].getEmployerAssets() == 0 &&
    					allocTotals[4].getNumberOfOptions() == 0 && allocTotals[4].getParticipantsInvested() == 0 &&
    					allocTotals[4].getTotalAssets() == 0))
    					reportData.addAllocationTotal(allocTotals[4]);
    			}
    		}

        	/*stmt_investmentAllocation.getMoreResults();
        	investmentTotalsRs = stmt_investmentAllocation.getResultSet();
        	populateTotals(reportData,investmentTotalsRs, isPBA);*/

        } catch (SQLException e) {
        	e.printStackTrace();
            throw new SystemException(e, this.getClass().getName(), "getReportData",
           		"Problem occurred during one of the stored proc call. " +
           			"Input parameters are " +
           			"contractNumber:" + contractNumber +
           			", asOfDate:" + asOfDate +
          			", lifecycle:" + lifecycle.toString()+
           			", lifestyle:" + lifestyle.toString()+
           			", viewOption:" + viewOption +
           			", pba:" + pba);
        } finally {
        	close(stmt_fundCatagories, null);
        	close(stmt_fundContract, vfconnection);
        	close(stmt_investmentAllocation, psconnection);
        }
        return reportData;
    }




    private AllocationTotals[] populateTotals(ResultSet rs, Boolean isPBA) throws SQLException{
    	AllocationTotals[] allocTotals = new AllocationTotals[5];
    	
    	if (rs != null) {
    	    
        	while (rs.next()){
        		AllocationTotals totals = new AllocationTotals();
        		String category = rs.getString("CATEGORY").trim();
    
    			totals.setEmployeeAssets(new Double(rs.getString("EE_TOTAL").trim()).doubleValue());
    			totals.setEmployerAssets(new Double(rs.getString("ER_TOTAL").trim()).doubleValue());
    			totals.setNumberOfOptions(new Integer(rs.getString("NUM_OPTIONS").trim()).intValue());
    			totals.setParticipantsInvested(new Integer(rs.getString("NUM_PARTICIPANTS").trim()).intValue());
    			if(rs.getString("PCT") != null) {
    				totals.setPercentageOfTotal(new Double(rs.getString("PCT").trim()).doubleValue());
    			}
    			totals.setTotalAssets(new Double(rs.getString("TOTAL").trim()).doubleValue());
    			//data.addAllocationTotal(totals);
    
    			//Note: We are expecting a maximum of five items from the result set.
    			//The following code defines the order.
    			if (category.equals("NON-LIFESTYLE-LIFECYCLE")) {
        			totals.setFundCategoryType(FundCategory.NON_LIFESTYLE_LIFECYCLE);
        			allocTotals[0] = totals;
    			}
        		else if (category.equals("LIFECYCLE")) {
        			totals.setFundCategoryType(FundCategory.LIFECYCLE);
        			allocTotals[1] = totals;
        		}
        		else if (category.equals("LIFESTYLE")) {
        			totals.setFundCategoryType(FundCategory.LIFESTYLE);
        			allocTotals[2] = totals;
        		}
        		else if (category.equals("GIFL")) {
        			totals.setFundCategoryType(FundCategory.GIFL);
        			allocTotals[3] = totals;
        		}
        		else if (category.equals("PBA")) {
        			totals.setFundCategoryType(FundCategory.PBA);
        			allocTotals[4] = totals;
        		}
        	}
        	
    	}
    	
    	//Add the allocation totals in proper order
    	/*if(allocTotals[0] != null)
    		data.addAllocationTotal(allocTotals[0]);
    	if(allocTotals[1] != null)
    		data.addAllocationTotal(allocTotals[1]);
    	if(allocTotals[2] != null) {
    		if(	isPBA != null && isPBA.booleanValue())
    			data.addAllocationTotal(allocTotals[2]);
    	}*/
    	return allocTotals;
    }

    private void populateDetails(InvestmentAllocationReportData data, ResultSet rs,
    	Hashtable allFunds, Boolean isPBA, String siteLocation, ORGANIZING_OPTION organizeByOption) throws SQLException, SystemException {
		
    	List<AssetClassVO> assetClasses = FundInfoCache.getAllAssetClasses();
    	SimpleDateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date  activeFundDate = null;
		try{
			activeFundDate =dFormat.parse(new String("12/31/9999"));
		}
		catch(ParseException pe)
		{
			System.out.println("Could not parse date");
		}
		boolean returnedPBA = false;
		
		if (rs != null) {
		    
    		while (rs.next()) {
    			AllocationDetails details = new AllocationDetails();
    			String fundId = rs.getString("INVESTMENT_OPTION_ID").trim();
    			String percentageOfTotal = rs.getString("PCT");
    			String marketingOrder =null;
    			ContractFundDetails contractFundDetails =(ContractFundDetails)allFunds.get(fundId);
    			if(contractFundDetails !=null) {
    	        	marketingOrder = contractFundDetails.getMarketingOrder();
    			}
    			if (allFunds.containsKey(fundId))
    				allFunds.remove(fundId);
    
    			boolean fundIsPBA = false;
    			//AAARGGGG this is ugly. Check if the PBA fund was returned.
                if ("NPB".equals(fundId) || "PBA".equals(fundId)) {
    				returnedPBA = true;
    				fundIsPBA = true;
                }
    
                FundVO fundInfo = FundInfoCache.getFundData(fundId);
    			FundCategory category = null;
    			
    			switch(organizeByOption) {
    			case AssetClass :
    				 AssetClassVO vo = getAssetClass(assetClasses, fundInfo.getAssetClass(),
    				         fundIsPBA);
    				 category = new FundCategory(vo.getAssetClass(), vo.getAssetClassDesc(), vo.getSortOrder());
    				 break;
    			case RiskCategory:	
    				 category = new FundCategory(fundInfo.getRiskCategoryCodeWithLS(), fundInfo.getFontColor(), fundInfo.getBgColor());
    				 break;
    			}
    			
    			details.setFundName(fundInfo.getName());
    			details.setFundId(fundId);
    			if (marketingOrder != null) {
    				details.setMarketingOrder(new Integer(marketingOrder).intValue());
    			}
    			if(contractFundDetails !=null) {
    				details.setRateType(contractFundDetails.getRateType());
    			} else { //to do: need to get historical rate type
    				details.setRateType("");
    			}
    			details.setFundType(fundInfo.getType());
    			details.setTickerSymbol(fundInfo.getTickerSymbol());
    			details.setParticipantsInvestedCurrent( new Integer(rs.getString("NUM_PART_CURRENT").trim()).intValue() );
    			details.setParticipantsInvestedFuture(new Integer(rs.getString("NUM_PART_ONGOING").trim()).intValue() );
    			details.setEmployeeAssets(new Double(rs.getString("EE_TOTAL").trim()).doubleValue() );
    			details.setEmployerAssets(new Double(rs.getString("ER_TOTAL").trim()).doubleValue() );
    			details.setTotalAssets(new Double(rs.getString("TOTAL").trim()).doubleValue() );
    			if(percentageOfTotal != null) {
    				details.setPercentageOfTotal(new Double(percentageOfTotal.trim()).doubleValue() );
    			}
    			details.setFundCategory(category);
    			data.addAllocationDetails(category, details);
    		}
    		
		}
		
		if (asOfDate.equals(currentDate)){
			Iterator remainingFunds = allFunds.keySet().iterator();

			while (remainingFunds.hasNext())
			{

				String fundId = (String)remainingFunds.next();
				ContractFundDetails contractFundDetails =(ContractFundDetails)allFunds.get(fundId);
				if(contractFundDetails.getEndDate().equals(activeFundDate)&& contractFundDetails.isSelected())
				{
					AllocationDetails details = new AllocationDetails();

					FundVO fundInfo = FundInfoCache.getFundData(fundId);
					FundCategory category = null;
					
					switch(organizeByOption) {
					case AssetClass :
						 AssetClassVO vo = getAssetClass(assetClasses, fundInfo.getAssetClass(),
                                    false);
						 category = new FundCategory(vo.getAssetClass(), vo.getAssetClassDesc(), vo.getSortOrder());
						 break;
					case RiskCategory:	
						 category = new FundCategory(fundInfo.getRiskCategoryCodeWithLS(), fundInfo.getFontColor(), fundInfo.getBgColor());
						 break;
					}
					
					details.setFundName(fundInfo.getName());
					details.setFundId(fundId);
					details.setTickerSymbol(fundInfo.getTickerSymbol());
					String marketingOrder =contractFundDetails.getMarketingOrder();
					if (marketingOrder != null)
						details.setMarketingOrder(new Integer(marketingOrder).intValue());
					if(contractFundDetails !=null)
					  details.setRateType(contractFundDetails.getRateType());
					else //to do: need to get historical rate type
						details.setRateType("");
					details.setFundType(fundInfo.getType());
					details.setParticipantsInvestedCurrent(0);
					details.setParticipantsInvestedFuture(0);
					details.setEmployeeAssets(0);
					details.setEmployerAssets(0);
					details.setTotalAssets(0);
					details.setPercentageOfTotal(0);
					details.setFundCategory(category);
					data.addAllocationDetails(category, details);
				}
			}
		}

		//Handle the situation where the PBA indicator is true, and the PBA values are zero.
		if(!returnedPBA && isPBA.booleanValue() && asOfDate.equals(currentDate)) {
			AllocationDetails details = new AllocationDetails();
			FundVO fundInfo = null;
			String marketingOrder = null;
			String rateType ="";
			if("usa".equalsIgnoreCase(siteLocation)) {
				fundInfo = FundInfoCache.getFundData("PBA");
				details.setFundId("PBA");

				//marketingOrder = (String)allFunds.get("PBA");
				ContractFundDetails contractFundDetails =(ContractFundDetails)allFunds.get("PBA");
				if(contractFundDetails ==null)
					System.out.println("no contractFundDetails for PBA");
				else
				{
					marketingOrder = contractFundDetails.getMarketingOrder();
					rateType =contractFundDetails.getRateType();
				}
			} else {
				fundInfo = FundInfoCache.getFundData("NPB");
				details.setFundId("NPB");

				//marketingOrder = (String)allFunds.get("NPB");
				ContractFundDetails contractFundDetails =(ContractFundDetails)allFunds.get("NPB");
				if(contractFundDetails ==null) {
					System.out.println("no contractFundDetails for NPB");
				} else {
					marketingOrder = contractFundDetails.getMarketingOrder();
					rateType =contractFundDetails.getRateType();
				}
			}
			FundCategory category = null;
			
			switch(organizeByOption) {
			case AssetClass :
			    AssetClassVO vo = getAssetClass(null, null, true);
                    category = new FundCategory(vo.getAssetClass(), vo.getAssetClassDesc(), vo
                            .getSortOrder());
				break;
			case RiskCategory:	
				category = new FundCategory(fundInfo.getRiskCategoryCodeWithLS(), fundInfo.getFontColor(), fundInfo.getBgColor());
				break;
			}
			
			details.setFundName(fundInfo.getName());
			details.setFundType(fundInfo.getType());
			details.setTickerSymbol(fundInfo.getTickerSymbol());
			if (marketingOrder != null)
				details.setMarketingOrder(new Integer(marketingOrder).intValue());
			details.setParticipantsInvestedCurrent(0);
			details.setParticipantsInvestedFuture(0);
			details.setEmployeeAssets(0);
			details.setEmployerAssets(0);
			details.setTotalAssets(0);
			details.setPercentageOfTotal(0);
			details.setFundCategory(category);
			details.setRateType(rateType);
			data.addAllocationDetails(category, details);
		}
    }

    /**
     * This class returns the AssetClassVO for a given category code
     * 
     * @param assetClasses List<AssetClassVO>
     * @param categoryCode String
     * 
     * @return AssetClassVO
     */
    private AssetClassVO getAssetClass(List<AssetClassVO> assetClasses, String categoryCode,
            boolean fundIsPBA) {
        /*
         * if fund is PBA; return back PBA description
         */
        if (fundIsPBA) {
            return new AssetClassVO(Constants.PBA_CATEGORY_CODE, Constants.PBA_ORDER,
                    Constants.PBA_DESC);
        }
        /*
         * else need to find the asset class desc from database
         */
    	for (AssetClassVO assetClass : assetClasses) {
    		if (categoryCode.equalsIgnoreCase(assetClass.getAssetClass())) {
    			return assetClass;
    		}
    	}
    	/*
         * this will never happen
         */
        AssetClassVO vo = new AssetClassVO("", 0, "");
    	return vo;
    }

    /**
     * The method returns the number of funds selected by the contract as of report date
     * 
     * @param vfconnection
     * @param contractNumber
     * @param asOfDate
     * 
     * @return count Integer
     * @throws SystemException
     */
    private int getNumberOfFundsSelected(Connection vfconnection, Integer contractNumber,
            java.sql.Date asOfDate) throws SystemException {
        int count = 0;
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        try {
            stmnt = vfconnection.prepareStatement(SQL_GET_FUNDS_COUNT_FOR_AS_OF_DATE);
            int i = 0;
            stmnt.setInt(++i, contractNumber);
            stmnt.setDate(++i, asOfDate);
            stmnt.setDate(++i, asOfDate);
            rs = stmnt.executeQuery();
            i = 0;
            while (rs.next()) {
                count = rs.getInt(++i);
            }
        } catch (SQLException e) {
            throw new SystemException(e, this.getClass().getName(), "getNumberOfFundsSelected",
                    "Problem occurred during the execution of querry. " + "Input parameters are "
                            + "contractNumber:" + contractNumber + ", asOfDate:" + asOfDate);
        } finally {
            close(stmnt, null);
        }
        return count;
    }
}
package com.manulife.pension.ireports.report.util;

import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

import com.manulife.pension.ireports.StandardReportsConstants;


public class AssetHouseFundMapping implements StandardReportsConstants{

	public static final String SECTION_TITLE_EQUITY_FUNDS = "Equity Funds";
	public static final String SECTION_TITLE_SPE_SEC_BAL_FUNDS = "Specialty/Sector/Balanced";
	public static final String SECTION_TITLE_ASSET_ALLOCATION_FUNDS = "Asset Allocation";
	public static final String SECTION_TITLE_FIXED_INCOME = "Fixed Income Funds";
	public static final String SECTION_TITLE_GUARANTEED_ACCOUNTS = "Guaranteed Accounts";
	
	private final Map /* <AssetHouseSubGroup>, <AssetHouseMainGroup> */ mainGroupMapping = new LinkedMap();
	private final Map /* <String assetClassId>, <AssetHouseSubGroup> */ subGroupMapping = new LinkedMap();

	private static final AssetHouseFundMapping INSTANCE = new AssetHouseFundMapping();

	public static AssetHouseFundMapping getInstance() {
		return INSTANCE;
	}
	  
	// Constructor
	/////////////////

	private AssetHouseFundMapping() {
		// MB - can we drop this and use the assetclsgroup table instead?
		//               - the database does not exactly match the descriptions below
		//                 which would make this refactoring difficult
		// Harcoding it for March 2017 DOL changes, no time to fix it for now
		
		AssetHouseMainGroup equityFundsMainGroup = new AssetHouseMainGroup(SECTION_TITLE_EQUITY_FUNDS, 0);

		// subgroup is known as Style Box by business
		addSubGroupAndAssetClass(equityFundsMainGroup, "Large Cap Value", 0, "LCV");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Large Cap Blend", 1, "LCB");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Large Cap Growth", 2, "LCG");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Mid Cap Value", 3, "MCV");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Mid Cap Blend", 4, "MCB");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Mid Cap Growth", 5, "MCG");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Small Cap Value", 6, "SCV");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Small Cap Blend", 7, "SCB");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Small Cap Growth", 8, "SCG");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Multi Cap Value", 9, "MCF");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Multi Cap Blend", 10, "MBC");
		addSubGroupAndAssetClass(equityFundsMainGroup, "Multi Cap Growth", 11, "MGC");
		addSubGroupAndAssetClass(equityFundsMainGroup, "International/Global Value", 12, "IGV");
		addSubGroupAndAssetClass(equityFundsMainGroup, "International/Global Blend", 13, "IGB");
		addSubGroupAndAssetClass(equityFundsMainGroup, "International/Global Growth", 14, "IGG");

		AssetHouseMainGroup specSectorBalancedMainGroup = new AssetHouseMainGroup(SECTION_TITLE_SPE_SEC_BAL_FUNDS, 1);
		addSubGroupAndAssetClass(specSectorBalancedMainGroup, "Specialty", 15, "SPE");
		addSubGroupAndAssetClass(specSectorBalancedMainGroup, "Sector", 16, "SEC");
		addSubGroupAndAssetClass(specSectorBalancedMainGroup, "Balanced", 17, "BAL");
		addSubGroupAndAssetClass(specSectorBalancedMainGroup, "Index", 18, "IDX");

		AssetHouseMainGroup assetAllocationMainGroup = new AssetHouseMainGroup(SECTION_TITLE_ASSET_ALLOCATION_FUNDS, 2);
		addSubGroupAndAssetClass(assetAllocationMainGroup, "Target Date", 19, "LCF");
        addSubGroupAndAssetClass(assetAllocationMainGroup, "Target Risk", 20, "LSF");
		addSubGroupAndAssetClass(assetAllocationMainGroup, "Guaranteed Income Feature", 21, "LSG");
		
		AssetHouseMainGroup fixedIncomeMainGroup = new AssetHouseMainGroup(SECTION_TITLE_FIXED_INCOME, 3);
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "High Quality Short Term", 22, "FXS");
        addSubGroupAndAssetClass(fixedIncomeMainGroup, "High Quality Short Term", 23, "HQS");
        addSubGroupAndAssetClass(fixedIncomeMainGroup, "Medium Quality Short Term", 24, "MQI");
        addSubGroupAndAssetClass(fixedIncomeMainGroup, "Low Quality Short Term", 25, "LQS");
        addSubGroupAndAssetClass(fixedIncomeMainGroup, "Global Short Term", 26, "GLS");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "High Quality Intermediate Term", 27, "FXI");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "Medium Quality Intermediate Term", 28, "FXM");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "Low Quality Intermediate Term", 29, "HYF");
        addSubGroupAndAssetClass(fixedIncomeMainGroup, "Low Quality Intermediate Term", 30, "LQI");
        addSubGroupAndAssetClass(fixedIncomeMainGroup, "Global Intermediate Term", 31, "GLI");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "High Quality Long Term", 32, "FXL");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "Medium Quality Long Term", 33, "MQL");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "Low Quality Long Term", 34, "LQL");
        addSubGroupAndAssetClass(fixedIncomeMainGroup, "Global Long Term", 35, "GLL");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "Global Bond", 36, "GLB");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "Year 3 Guaranteed", 37, "GA3");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "Year 5 Guaranteed", 38, "GA5");
		addSubGroupAndAssetClass(fixedIncomeMainGroup, "Year 10 Guaranteed", 39, "G10");
	}

	// Private Constructor support methods
	/////////////////

	private void addSubGroupAndAssetClass(AssetHouseMainGroup mainGroup, String subGroupName, int subGroupOrder, String assetClass) {
		AssetHouseSubGroup subGroup = new AssetHouseSubGroup(subGroupName, subGroupOrder);
		this.mainGroupMapping.put(subGroup, mainGroup);
		this.subGroupMapping.put(assetClass, subGroup);
	}

	private void addSubGroupAndAssetClasses(AssetHouseMainGroup mainGroup, String subGroupName, int subGroupOrder, String[] assetClasses) {
		AssetHouseSubGroup subGroup = new AssetHouseSubGroup(subGroupName, subGroupOrder);
		this.mainGroupMapping.put(subGroup, mainGroup);
		for (int i = 0; i < assetClasses.length; i++) {
			this.subGroupMapping.put(assetClasses[i], subGroup);
		}
	}
	
	// Public methods
	/////////////////
	
	public AssetHouseSubGroup getSubGroup(String assetClassId) {
		return (AssetHouseSubGroup) this.subGroupMapping.get(assetClassId);
	}
	
	public int getNumberOfSubGroups() {
		return this.subGroupMapping.size();
	}
	
	public AssetHouseMainGroup lookupMainGroup(AssetHouseSubGroup subGroup) {
		return (AssetHouseMainGroup) this.mainGroupMapping.get(subGroup);
	}

}

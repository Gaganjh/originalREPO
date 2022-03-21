package com.manulife.pension.ireports;

/**
 * Contains global data for the whole application, as opposed to CoreToolBOSData that contains session data
 * Creation date: (10/15/01 3:38:35 PM)
 * @author: Marcos Rogovsky
 *
 * Revision History:
 * April 30 2004	RH	Added new Stable Value Fund hashtable to accomidate funds that will be considered
 * 						to be part of this product.
 * 						They will be treated like a mandatory fund with 3 asterisks
 * August 23, 2004	RH	Added new Market Timing hashtable to add funds that require a
 * 						chevron on the PDF
 * March 28 2005	RH	Added additional entries for fundsNotToBeSelected
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.manulife.pension.ireports.util.propertymanager.PropertyManager;

public abstract class CoreToolGlobalData {
	/*
	Some funds should never be picked up by the tool, even if they have a high
	percentile ranking score, the business will tell us what funds these are
	TM - These are funds that are closed to new business, and some additional funds
	*/
	public static final Map additionalFundsNotToBeSelected = new HashMap();
	public static final Set definedBenefitContractProductIds = new HashSet();	
	/*
	Added Jan 06 as part of the i-evaluator base enhancements.
	*/
	public static final Map assetClassForcedIndexFunds = new HashMap();
	public static final Map assetClassProxyIndexFunds = new HashMap();
	private static final List lifestyleFundPairs = new ArrayList();

	static {
		//debugLevel = Integer.parseInt(System.getProperty("MF.debugLevel"));
		loadProperties();
	}

	private static void loadProperties() {
		buildDefinedBenefitContractProductIds();
		buildAdditionalFundsNotToBeSelected();
		buildAssetClassIndexFundAssociations();
		buildLifestyleFundPairs();
	}

	private static void buildDefinedBenefitContractProductIds() {
		String definedBenefitContractProductIdsString = PropertyManager.getString("MF.std.definedBenefitContractProductIds");
		System.out.println("Defined Benefit Contract Product Ids: " + definedBenefitContractProductIdsString + "\n");
		StringTokenizer tokenizer = new StringTokenizer(definedBenefitContractProductIdsString, ",");
		while (tokenizer.hasMoreTokens()) {
			String productId = tokenizer.nextToken();
			definedBenefitContractProductIds.add(productId);
		}
	}

	public static void reloadProperties() {
		additionalFundsNotToBeSelected.clear();
		assetClassForcedIndexFunds.clear();
		assetClassProxyIndexFunds.clear();
		lifestyleFundPairs.clear();
		
		loadProperties();
	}
	
	public static void buildAdditionalFundsNotToBeSelected() {
		loadPropertyIntoMap("MF.std.additionalFundsNotToBeSelected", additionalFundsNotToBeSelected);
	}

	private static void loadPropertyIntoMap(String property, Map propertyMap) {
		String properties = PropertyManager.getString(property);
		System.out.println(property + ": " + properties + "\n");
		StringTokenizer tokenizer = new StringTokenizer(properties, ",");
		while (tokenizer.hasMoreTokens()) {
			String id = tokenizer.nextToken().trim();
			propertyMap.put(id, "");
		}
	}
	
	/**
	 * Asset Classes that have associated Index Funds
	 * Reads in the values from the property file and puts then in the assetClassIndexFundAssociations variable
	 */
	private static void buildAssetClassIndexFundAssociations() {
		String associationsString = PropertyManager.getString("MF.std.assetClassForcedIndexFunds");
		System.out.println("Asset Class Forced Index Fund Associations: " + associationsString + "\n");
		StringTokenizer tokenizer = new StringTokenizer(associationsString, ",");
		while (tokenizer.hasMoreTokens()) {
			String associationInfo = tokenizer.nextToken();
			
			StringTokenizer tokenizer2 = new StringTokenizer(associationInfo," ");
			String assetClassId = tokenizer2.nextToken();
			
			List fundList = new ArrayList();
			while(tokenizer2.hasMoreTokens()) {
				String indexFundId = tokenizer2.nextToken();
				fundList.add(indexFundId);
			}

			assetClassForcedIndexFunds.put(assetClassId, fundList);
		}
		
		associationsString = PropertyManager.getString("MF.std.assetClassProxyIndexFunds");
		System.out.println("Asset Class Proxy Index Fund Associations: " + associationsString + "\n");
		tokenizer = new StringTokenizer(associationsString, ",");
		while (tokenizer.hasMoreTokens()) {
			String associationInfo = tokenizer.nextToken();
			
			StringTokenizer tokenizer2 = new StringTokenizer(associationInfo," ");
			String assetClassId = tokenizer2.nextToken();
			
			List fundList = new ArrayList();
			while(tokenizer2.hasMoreTokens()) {
				String indexFundId = tokenizer2.nextToken();
				fundList.add(indexFundId);
			}

			assetClassProxyIndexFunds.put(assetClassId, fundList);
		}
	}

	private static void buildLifestyleFundPairs() {
		String lifestylePropertyString;
		lifestylePropertyString = PropertyManager.getString("MF.std.lifestyleFundPairs");
		System.out.println("Lifestyle fund pairs: " + lifestylePropertyString + "\n");
		if (lifestylePropertyString != null) {
			StringTokenizer tokenizer = new StringTokenizer(lifestylePropertyString, ",");
			while (tokenizer.hasMoreTokens()) {
				String pairInfo = tokenizer.nextToken();
				StringTokenizer tokenizer2 = new StringTokenizer(pairInfo," ");
				List fundList = new ArrayList();
				while(tokenizer2.hasMoreTokens()) {
					String fundId = tokenizer2.nextToken();
					fundList.add(fundId);
				}
				lifestyleFundPairs.add(Collections.unmodifiableList(fundList));
			}
		}
	}

	public static List getLifestyleFundPairs() {
		return Collections.unmodifiableList(lifestyleFundPairs);
	}

	/** Return true iff Collection c1 contains any of the elements in Collection c2 */
	public static boolean containsAny(Collection c1, Collection c2) {
		if (c1 == null || c2 == null) {
			return false;
		}
		
		boolean result = false;
		for (Iterator iter = c2.iterator(); !result && iter.hasNext(); ) {
			result = c1.contains(iter.next());
		}
		
		return result;
	}

}
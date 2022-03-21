package com.manulife.pension.ps.web.fee.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.util.log.LogUtility;

/**
 * This is utility class for Participant Statement Fees Tool content 
 * 
 * @author Eswar
 *
 */
public class PartStmtFeesContent {
	
	private static PartStmtFeesContent instance = null;
	private static List<String> participantSummaryGac = null;
	private static List<String> participantPartyGac = null;
	private static List<String> participantDetailGac = null;
	private static List<String> contractSummaryGac = null;
	private static List<String> contractPartyGac = null;
	private static List<String> contractDetailGac = null;
	private static List<String> summaryLegend = null;
	private static List<String> detailLegend = null;
	private static List<String> individualCharges = null;
	private static List<String> otherCharges = null;
	private static List<String> opsList = null;
	
	private static final String FEE = "fee";
	private static final String KEY = "key";
	private static final String SHORTTEXT = "shortText";
	private static final String LONGTEXT = "longText";
	
	private static final String TPA_SERVICE_FEE = "tpaServiceFee";
	private static final String DETAILED_STMT_FEE = "detailedStmtFee";
	private static final String GIFLFEATURE_FEE = "giflFeatureFee";
	private static final String GIFLFEATURE_FEE_ADJ = "giflFeatureFeeAdj";
	private static final String GIA_MVA = "giaMva";
	private static final String LOAN_FEE = "loanFee";
	private static final String WITHDRAWL_FEE = "withdrawlFee";
	private static final String TPA_LOAN_FEE = "tpaLoanFee";
	private static final String TPA_WITHDRAWAL_FEE = "tpaWithdrawalFee";
	private static final String REDEMPTION_FEE = "redemptionFee";
	private static final String RIA_FEE = "riaFee";
	private static final String STABLE_VALUEFUND_MVA = "stableValueFundMva";
	private static final String WITHDRAWAL_CHARGE = "withdrawalCharge";
	private static final String CREDIT = "credit";
	private static final String PLAN_CREDIT = "planCredit";

	private static final String JH_GAC_SUMMARY_TOTAL = "jhGacSummaryTotal";
	private static final String TRUSTEE_GAC_SUMMARY_TOTAL = "trusteeGacSummaryTotal";
	private static final String BROKER_GAC_SUMMARY_TOTAL = "brokerGacSummaryTotal";
	private static final String TPA_GAC_SUMMARY_TOTAL = "tpaGacSummaryTotal";
	private static final String RIA_GAC_SUMMARY_TOTAL = "riaGacSummaryTotal";
	
	private static final String PARTY_RECORDKEEPING_GAC_SUMMARY_TOTAL = "partyRecordkeepingGacSummaryTotal";
	private static final String PARTY_SALESSERVICE_GAC_SUMMARY_TOTAL = "partySalesServiceGacSummaryTotal";
	private static final String PARTY_TRUSTEE_GAC_SUMMARY_TOTAL = "partyTrusteeGacSummaryTotal";

	private static final String JH_GAC_ASSETCHARGES = "jhGacAssetCharges";
	private static final String JH_GAC_PARTICIPANT_FEES = "jhGacParticipantFees";
	private static final String JH_GAC_RECORDKEEPING_FEES = "jhGacRecordKeepingFees";
	private static final String JH_GAC_AUTO_DEDUNPAID_CHARGES = "jhGacAutoDedUnpaidCharges";
	private static final String JH_GAC_DISCONTINUANCE_CHARGES = "jhGacDiscontinuanceCharges";
	private static final String TRUSTEE_GAC = "trusteeGac";
	private static final String BROKER_GAC = "brokerGac";
	private static final String TPA_GAC_PLAN_ADMIN_SERVICE_FEES = "tpaGacPlanAdminServiceFees";
	private static final String TPA_GAC_ADMIN_FEES = "tpaGacAdminFees";
	private static final String TPA_GAC_PARTICIPANT_FEES = "tpaGacParticipantFees";
	private static final String RIA_GAC = "riaGac";
	private static final String RIA_FLAT_FEE_PER_HEAD = "riaFlatFeePerHead";
	private static final String TOTAL_GAC = "totalGac";					
	private static final String TOTAL_PARTICIPANT_FEES = "totalParticipantFees";
	
	private static final String MARKET_VALUE_EQUALIZER = "marketValueEqualizer";
	private static final String PARTY_MARKET_VALUE_EQUALIZER = "partyMarketValueEqualizer";
	
	private Logger logger = logger = Logger.getLogger(PartStmtFeesContent.class);
	
	private Map<String,PartStmtFeesDescription> content = new HashMap<String,PartStmtFeesDescription>();
	
	private InputStream xmlFileStream = getClass().getClassLoader().getResourceAsStream(
    "./ParticipantStatementFeesTool.xml");
	
	static {
        try {
            instance = new PartStmtFeesContent();
        } catch (SystemException e) {
        	LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);
        }
    }
	/**
     * Constructor for ParticipantStatementContent
     */
    private PartStmtFeesContent() throws SystemException {
        loadContent();
        otherCharges = populateOtherCharges();
        individualCharges = populateIndividualCharges();
        participantSummaryGac = populateSummaryList();
        participantPartyGac = populatePartyList();
        participantDetailGac = populateDetailList();
        contractSummaryGac = contractSummaryList();
        contractPartyGac = contractPartyList();
        contractDetailGac = contractDetailList();
        summaryLegend = populateSummaryLegends();
        detailLegend = populateDetailedLegends();
        opsList = populateOpsInvestigation();
    }
    
    /**
     * @return instance
     */
    public static PartStmtFeesContent getInstance() {
        return instance;
    }
    
    /**
     * @return content
     */
    public Map<String,PartStmtFeesDescription> getFeeContent() {
        return content;
    }
    
    /**
     * It loads content XML file and and parses the same. 
     * @throws SystemException
     */
    private void loadContent() throws SystemException {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(xmlFileStream));
            Document document = parser.getDocument();
            NodeList list = document.getElementsByTagName(FEE);
            int pageLen = list.getLength();
            
            PartStmtFeesDescription bean = null;
            for (int i = 0; i < pageLen; i++) {
            	Element fee = (Element) list.item(i);
                String key = fee.getAttribute(KEY);
                String shortText = fee.getAttribute(SHORTTEXT);
                String longText = fee.getAttribute(LONGTEXT);
                bean = new PartStmtFeesDescription(shortText,longText);
                content.put(key,bean);
            	
            }
        } catch (IOException e) {
        	logger.debug("Exception occured while loading ParticipantStatementFeesTool.xml"+e);
        	throw new SystemException("Exception occured while loading ParticipantStatementFeesTool.xml"+e);
        } catch (SAXException e) {
        	logger.debug("Exception occured while parsing ParticipantStatementFeesTool.xml"+e);
        	throw new SystemException("Exception occured while parsing ParticipantStatementFeesTool.xml"+e);
        }
    }
    
    /**
     * Individual Charges
     * @return List
     */
    private static List<String> populateIndividualCharges() {
    	return Arrays.asList(
    			 TPA_SERVICE_FEE
    			,DETAILED_STMT_FEE 
    			,GIFLFEATURE_FEE
    			,GIFLFEATURE_FEE_ADJ
    			,GIA_MVA
    			,LOAN_FEE
    			,WITHDRAWL_FEE
    			,TPA_LOAN_FEE
    			,TPA_WITHDRAWAL_FEE
    			,REDEMPTION_FEE
    			,RIA_FEE
    			,STABLE_VALUEFUND_MVA
    			,WITHDRAWAL_CHARGE
    			);     	
    }
    
    /**
     * Other Charges
     * @return List
     */
    private static List<String> populateOtherCharges() {
    	return Arrays.asList(
    			 CREDIT
    			,PLAN_CREDIT
    			);     	
    }

    
    /**
     * Summary GAC List
     * @return List
     */
    private static List<String> populateSummaryList() {
    	return Arrays.asList(
    			 JH_GAC_SUMMARY_TOTAL
    			,TRUSTEE_GAC_SUMMARY_TOTAL
    			,BROKER_GAC_SUMMARY_TOTAL
    			,MARKET_VALUE_EQUALIZER
    			,TPA_GAC_SUMMARY_TOTAL
    			,RIA_GAC_SUMMARY_TOTAL
    			);     	
    }
    
    /**
     * Summary GAC List
     * @return List
     */
    private static List<String> populatePartyList() {
    	return Arrays.asList(
    		PARTY_RECORDKEEPING_GAC_SUMMARY_TOTAL
   			,PARTY_SALESSERVICE_GAC_SUMMARY_TOTAL
   			,MARKET_VALUE_EQUALIZER
   			,PARTY_TRUSTEE_GAC_SUMMARY_TOTAL
   			);     	
   }
    
    /**
     * Detail GAC List
     * @return List
     */
    private static List<String> populateDetailList() {
    	return Arrays.asList(
    			 JH_GAC_ASSETCHARGES
    			,JH_GAC_PARTICIPANT_FEES
    			,JH_GAC_RECORDKEEPING_FEES
    			,JH_GAC_AUTO_DEDUNPAID_CHARGES
    			,JH_GAC_DISCONTINUANCE_CHARGES
    			,TRUSTEE_GAC
    			,BROKER_GAC
    			,MARKET_VALUE_EQUALIZER
    			,TPA_GAC_PLAN_ADMIN_SERVICE_FEES
    			,TPA_GAC_ADMIN_FEES
    			,TPA_GAC_PARTICIPANT_FEES
    			,RIA_GAC
    			,RIA_FLAT_FEE_PER_HEAD
    			); 
    }
    
    /**
     * Contract Summary GAC List
     * @return List
     */
    private static List<String> contractSummaryList() {
    	return Arrays.asList(
    			TOTAL_PARTICIPANT_FEES
    			,TOTAL_GAC
    			,JH_GAC_SUMMARY_TOTAL
    			,TRUSTEE_GAC_SUMMARY_TOTAL
    			,BROKER_GAC_SUMMARY_TOTAL
    			,MARKET_VALUE_EQUALIZER
    			,TPA_GAC_SUMMARY_TOTAL
    			,RIA_GAC_SUMMARY_TOTAL
    			);     	
    }
    
    /**
     * Contract Party GAC List
     * @return List
     */
    private static List<String> contractPartyList() {
    	return Arrays.asList(
    			TOTAL_PARTICIPANT_FEES
    			,TOTAL_GAC
    			,PARTY_RECORDKEEPING_GAC_SUMMARY_TOTAL
    			,PARTY_SALESSERVICE_GAC_SUMMARY_TOTAL
    			,PARTY_MARKET_VALUE_EQUALIZER
    			,PARTY_TRUSTEE_GAC_SUMMARY_TOTAL
    			);     	
    }

    /**
     * Contract Detail GAC List
     * @return List
     */
    private static List<String> contractDetailList() {
    	return Arrays.asList(
    			TOTAL_PARTICIPANT_FEES
    			,TOTAL_GAC
    			,JH_GAC_ASSETCHARGES
    			,JH_GAC_PARTICIPANT_FEES
    			,JH_GAC_RECORDKEEPING_FEES
    			,JH_GAC_AUTO_DEDUNPAID_CHARGES
    			,JH_GAC_DISCONTINUANCE_CHARGES
    			,TRUSTEE_GAC
    			,BROKER_GAC
    			,MARKET_VALUE_EQUALIZER
    			,TPA_GAC_PLAN_ADMIN_SERVICE_FEES
    			,TPA_GAC_ADMIN_FEES
    			,TPA_GAC_PARTICIPANT_FEES
    			,RIA_GAC
    			,RIA_FLAT_FEE_PER_HEAD
    			); 
    }
    
    /**
     * Summary Legends
     * @return List
     */
    private static List<String> populateSummaryLegends() {
    	return Arrays.asList(
    			TOTAL_PARTICIPANT_FEES
    			,TOTAL_GAC
    			,JH_GAC_SUMMARY_TOTAL
    			,TRUSTEE_GAC_SUMMARY_TOTAL
    			,BROKER_GAC_SUMMARY_TOTAL
    			,MARKET_VALUE_EQUALIZER
    			,TPA_GAC_SUMMARY_TOTAL
    			,RIA_GAC_SUMMARY_TOTAL
    			);     	
    }
    
    /**
     * Detailed Legends
     * @return List
     */
    private static List<String> populateDetailedLegends() {
    	return Arrays.asList(
    			TOTAL_PARTICIPANT_FEES
    			,TOTAL_GAC
    			,JH_GAC_ASSETCHARGES
    			,JH_GAC_PARTICIPANT_FEES
    			,JH_GAC_RECORDKEEPING_FEES
    			,JH_GAC_AUTO_DEDUNPAID_CHARGES
    			,JH_GAC_DISCONTINUANCE_CHARGES
    			,TRUSTEE_GAC
    			,BROKER_GAC
    			,MARKET_VALUE_EQUALIZER
    			,TPA_GAC_PLAN_ADMIN_SERVICE_FEES
    			,TPA_GAC_ADMIN_FEES
    			,TPA_GAC_PARTICIPANT_FEES
    			,RIA_GAC
    			,RIA_FLAT_FEE_PER_HEAD
    			);     	
    }
    
    /**
     * @return List
     */
    private static List<String> populateOpsInvestigation() {
    	return Arrays.asList(
    			 JH_GAC_ASSETCHARGES
    			,JH_GAC_SUMMARY_TOTAL
    			,TRUSTEE_GAC
    			,BROKER_GAC
    			,MARKET_VALUE_EQUALIZER
    			,PARTY_MARKET_VALUE_EQUALIZER
    			,TRUSTEE_GAC_SUMMARY_TOTAL
    			,BROKER_GAC_SUMMARY_TOTAL
    			,PARTY_RECORDKEEPING_GAC_SUMMARY_TOTAL
    			,PARTY_SALESSERVICE_GAC_SUMMARY_TOTAL
    			,PARTY_TRUSTEE_GAC_SUMMARY_TOTAL
    			);     	
    }
    
    /**
     * @return OtherCharges
     */
    public List<String> getOtherChargesList() {
    	return otherCharges;
    }
    
    /**
     * @return individualCharges
     */
    public List<String> getIndividualChargesList() {
    	return individualCharges;
    }
    
    /**
     * @return participantSummaryGac
     */
    public List<String> getParticipantSummaryGACList() {
    	return participantSummaryGac;
    }
    
    /**
     * @return participantSummaryGac
     */
    public List<String> getParticipantPartyGACList() {
    	return participantPartyGac;
    }
    
    /**
     * @return participantDetailGac
     */
    public List<String> getParticipantDetailGACList() {
    	return participantDetailGac;
    }
    
    /**
     * @return contractSummaryGac
     */
    public List<String> getContractSummaryGACList() {
    	return contractSummaryGac;
    }
    
    /**
     * @return contractPartyGac
     */
    public List<String> getContractPartyGACList() {
    	return contractPartyGac;
    }
    
    /**
     * @return contractDetailGac
     */
    public List<String> getContractDetailGACList() {
    	return contractDetailGac;
    }
    
    /**
     * @return summaryLegend
     */
    public List<String> getSummaryReportLegend() {
    	return summaryLegend;
    }
    
    /**
     * @return detailLegend
     */
    public List<String> getDetailReportLegend() {
    	return detailLegend;
    }
    
    /**
     * @return opsList
     */
    public List<String> getOpsInvestigation() {
    	return opsList;
    }
    
}

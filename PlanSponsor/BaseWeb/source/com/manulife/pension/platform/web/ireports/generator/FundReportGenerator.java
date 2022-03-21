package com.manulife.pension.platform.web.ireports.generator;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.dao.ContractDAO;
import com.manulife.pension.ireports.dao.DAOFactory;
import com.manulife.pension.ireports.model.FundOffering;
import com.manulife.pension.ireports.model.ShortlistKey;
import com.manulife.pension.ireports.report.ContractShortlistOptions;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.report.streamingreport.StreamingReport;
import com.manulife.pension.ireports.util.ContractFundOfferingDeterminator;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.utility.ireports.FundReportConstants;
import com.manulife.pension.platform.web.ireports.utilities.CIAFormUtil;
import com.manulife.pension.platform.utility.ireports.utilities.FundReportUtil;
import com.manulife.pension.platform.utility.ireports.valueobject.FundReportParamsHolderVO;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.dao.HistoricalIreportDAO;
import com.manulife.pension.service.fund.fandp.valueobject.FandpFilterCriteria;
import com.manulife.pension.service.fund.fandp.valueobject.FundsAndPerformance;

/**
 * This is a Utility class for i:reports. This class is responsible to get all the parameters to be
 * sent to i:reports PDF generator, and then, trigger the i:report PDF generation.
 * 
 * @author harlomte
 * 
 */
public class FundReportGenerator {

    /**
     * This method populates the FundReportParamsHolderVO from FapForm. The information in
     * this object will be later passed while triggering the i:reports PDF generation.
     * 
     * @param fapForm
     * @param request
     * @return
     * @throws SystemException
     */
    public static FundReportParamsHolderVO populateFundReportParams(FapForm fapForm,
            HttpServletRequest request) throws SystemException {
        
        FundReportParamsHolderVO fundReportParams = new FundReportParamsHolderVO();
        
        FundsAndPerformance fundsAndPerformance = (FundsAndPerformance) request.getSession()
                .getAttribute(FapConstants.VO_FUNDS_AND_PERFORMANCE);


        FandpFilterCriteria fandPFilterCriteria = fundsAndPerformance.getFilterCriteriaUsed();
        
        // Latest as of Date
        Date asOfDate = fundsAndPerformance.getAsOfDates().get(
                StandardReportsConstants.REPORT_CONTEXT_UNIT_VALUE);
        fundReportParams.setAsOfDate(asOfDate);
        //This parameter holds month end Date to generate Historical IReport
        fundReportParams.setPeriodendingDate(fapForm.getMonthEndDate());
        // Group By
        fundReportParams.setGroupSelected(fandPFilterCriteria.getGroupBy()); // fapForm.getGroupbySelect();
        // Getting the Selected value from Dropdown
        fundReportParams.setSeletctedGroup(fapForm.getGroupBySelect());
        // This parameter holds the vale when request is coming from web-page to generate i:report
        fundReportParams.setSelectedFromHistoricalReport(fapForm.isSelectedFromWebPage());
        
        // Report
        fundReportParams.setReportSelected(fapForm.getSelectedReport()); 
       	fundReportParams.setReportClassName(FundReportUtil.getReportClassName(fandPFilterCriteria
                    .getGroupBy(), fapForm.getSelectedReport()));	
       	fundReportParams.setMerrillAdvisor(fandPFilterCriteria.isIncludeOnlyMerrillCoveredFunds());

        // Sub Report
        fundReportParams.setSubReportSelected(FundReportConstants.EMPTY_STRING);
        
        // Funds Chosen.
        fundReportParams.setSelectedFundsList(FundReportUtil.getFundsChosen(fundsAndPerformance));
        
        // Contract Number
        if (isContractSelected(fandPFilterCriteria)) { 
            Integer contractNumber = fandPFilterCriteria.getContractNumber();
            fundReportParams.setContractNumberSelected(String.valueOf(contractNumber));

            Contract contract;
            try {
                    contract = ContractServiceDelegate.getInstance().getContractDetails(contractNumber, 6);
            } catch (ContractNotExistException e) {
                throw new SystemException(e, "Contract #" + contractNumber + " not found.");
            } catch (NumberFormatException e) {
                throw new SystemException(e, "NumberFormatException occurred for contract Number: "
                        + contractNumber);
            }

            fundReportParams.setContractSelected(contract);
        }
        
        // US / NY Indicator
        String companyId = fandPFilterCriteria.getCompanyName();
        fundReportParams.setCompanyName(companyId);
        
        String usNyIndicator = GlobalConstants.MANULIFE_CONTRACT_ID_FOR_USA.equals(companyId) ? CommonConstants.SITEMODE_USA
                : CommonConstants.SITEMODE_NY;
        fundReportParams.setUsNyIndicator(usNyIndicator); // fapForm.getSiteLocation()

        // Class Menu - extra parameter.
        fundReportParams.setClassSelected(fandPFilterCriteria.getFundClass()); // fapForm.getClassSelect()

        // Set the Additional Parameters.
        
        // ISF Indicator
        fundReportParams.setIsfIndicator(fapForm.getIsfSelected()); 

        // NML Indicator - used when creating the FAP Action Form.
        fundReportParams.setIncludeNml(fundsAndPerformance.isNmlFundsIncluded());  
        
        // The Subtitle.. This is the Client Name.
        fundReportParams.setSubTitle(fapForm.getClientName());
        
        fundReportParams.setAdvisorName(fapForm.getAdvisorName());
        
        boolean fundListMatch = FundReportUtil.isFundListMatch(request, fundsAndPerformance);
        fundReportParams.setFundListMatch(fundListMatch);
        
        // Get Title 1 thru Title 5 info
        Map<String, String> reportTitleList = FundReportUtil.getReportTitle(fundReportParams, request);
        fundReportParams.setReportTitleList(reportTitleList);
        
        return fundReportParams;
    }

    /**
     * This method checks if we are contract mode or generic mode of F&P page.
     * 
     * @param fapForm
     * @return - true if we are in contract mode of F&P page, else returns false.
     */
    private static boolean isContractSelected(FandpFilterCriteria fandPFilterCriteria) {
        return fandPFilterCriteria.getContractNumber() > 0;
    }

    /**
     * This method triggers the i:report PDF generation.
     * 
     * @param fundReportParams
     * @param request
     * @param environment
     * @return - ByteArrayOutputStream object containing the PDF to be sent back to the user.
     * @throws SystemException
     */
	public static ByteArrayOutputStream triggerIreport(FundReportParamsHolderVO fundReportParams,
            HttpServletRequest request) throws SystemException {

        ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();

        // Create Report Options. This represents the options selected by the user in online page.
        // These options are passed along to PDF generation part.
        ReportOptions options = makeReportOptions(fundReportParams, request);  
        
        // Get the report class which will be called for PDF generation part.
        StreamingReport report = getReport(fundReportParams.getReportClassName(), options);
	
        // Build the PDF report.
        report.buildReport();

        report.writeTo(pdfOutStream);
        //Appending Contract investment administration form instead if ISF;ME:127244 change
        if (options.isIncludeISF()) {
			int cmaKeyOfCIAPdf;
			//Checking for the user role is TPA or not.  
			if(StringUtils.equalsIgnoreCase(BaseSessionHelper.getBaseUserProfile(request).getAbstractPrincipal().getAbstractUserRole().getRoleId(), "TPA")){
				cmaKeyOfCIAPdf = CommonContentConstants.CIA_FORM_FOR_TPA;
			} else {
				cmaKeyOfCIAPdf = (fundReportParams.getContractSelected() != null && fundReportParams.getContractSelected().isNml()) ?  CommonContentConstants.CIA_FRW_IS_NML : CommonContentConstants.CIA_FRW_IS_NOT_NML;
			}
			String context = StringUtils.equalsIgnoreCase(fundReportParams.getUsNyIndicator(), "USA") ?  PdfConstants.US_CIA_FORM : PdfConstants.NY_CIA_FORM;		
	        pdfOutStream = CIAFormUtil.appendCIAForm(pdfOutStream, cmaKeyOfCIAPdf, context, request );
        }
        return pdfOutStream;
        
    }
    
    /**
     * This method gets a instance of the Report class which will be used to create the PDF.
     * 
     * @param selectedReportName
     * @param options
     * @param hostLocation
     * @return
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    private static StreamingReport getReport(String selectedReportName, ReportOptions options)
            throws SystemException {
        StreamingReport report = null;
        try {
            Class reportClass = FundReportGenerator.class.getClassLoader().loadClass(
                    selectedReportName);
            Constructor declaredConstructor = reportClass.getDeclaredConstructor(new Class[] {
                    ReportOptions.class });
            report = (StreamingReport) declaredConstructor.newInstance(new Object[] { options });
        } catch (ClassNotFoundException e) {
            throw new SystemException(e, "unable to load report class [" + selectedReportName + "]");
        } catch (SecurityException e) {
            throw new SystemException("unable to construct report class [" + selectedReportName
                    + "]");
        } catch (NoSuchMethodException e) {
            throw new SystemException(e, "unable to construct report class [" + selectedReportName
                    + "]");
        } catch (IllegalArgumentException e) {
            throw new SystemException(e, "unable to construct report class [" + selectedReportName
                    + "]");
        } catch (InstantiationException e) {
            throw new SystemException(e, "unable to construct report class [" + selectedReportName
                    + "]");
        } catch (IllegalAccessException e) {
            throw new SystemException(e, "unable to construct report class [" + selectedReportName
                    + "]");
        } catch (InvocationTargetException e) {
            throw new SystemException(e, "unable to construct report class [" + selectedReportName
                    + "]");
        }
        return report;
    }

    /**
     * This method creates the Report Options object. This object holds the information selected by
     * the user in the online page. This information will be passed for PDf generation part.
     * 
     * @param fundReportParams
     * @param companyId
     * @param request
     * @return - ReportOptions object.
     * @throws SystemException
     */
    public static ReportOptions makeReportOptions(FundReportParamsHolderVO fundReportParams,
            HttpServletRequest request) throws SystemException {
        
        String companyId = fundReportParams.getCompanyName();
        
        List<String> fundsChosenList = fundReportParams.getSelectedFundsList();
        String[] fundsChosen = new String[fundsChosenList.size()];
        fundsChosenList.toArray(fundsChosen);
        
        FundOffering fundOffering = FundReportUtil.createFundOffering(fundReportParams, request);
    
		// If contract is selected, get the ContractFundofferingDeterminator.
        ContractFundOfferingDeterminator determinator = null;
        if (!StringUtils.isBlank(fundReportParams.getContractNumberSelected())) {
            // Contract Selected.
            com.manulife.pension.ireports.model.Contract contract = ((ContractDAO) DAOFactory
                    .create(ContractDAO.class.getName()))
                    .retrieveContractByContractNumber(fundReportParams.getContractNumberSelected());

            determinator = new ContractFundOfferingDeterminator(companyId, contract);
        }
        
        ContractShortlistOptions contractShortlistOptions = null;
        contractShortlistOptions = ContractShortlistOptions.createContractOrShortlistOptions(
        		getContractOrShortlistUsed(fundReportParams, request), !fundReportParams.isFundListMatch());
    
        Map<String, String> reportTitle = fundReportParams.getReportTitleList(); 
        
        ReportOptions options = new ReportOptions(fundOffering, fundReportParams.getReportSelected(),
        		fundReportParams.getSeletctedGroup(),new HistoricalIreportDAO(),
        		fundReportParams.isSelectedFromHistoricalReport(),
        		getClassMenu(fundReportParams, companyId, determinator),
        		fundReportParams.getPeriodendingDate(),
				fundsChosen, fundReportParams.getLifecyclePortfolio(), 
				fundReportParams.getLifestylePortfolio(), contractShortlistOptions, 
				fundReportParams.getIsfIndicator(), reportTitle, 
				determinator == null ? null : determinator.getContractFundsMap());
        
        options.setMerrillAdvisor(fundReportParams.isMerrillAdvisor());
        return options;
    }
    
    /**
     * This method gives the class Menu selected by the user.
     * 
     * @param fundReportParams
     * @return
     */
	private static String getClassMenu(FundReportParamsHolderVO fundReportParams, String companyId,
			ContractFundOfferingDeterminator determinator) {
        if (!StringUtils.isBlank(fundReportParams.getContractNumberSelected())) {
            return determinator.getClassMenuCode();
        } else {
            return fundReportParams.getClassSelected();
        }
    }

    /**
     * This method checks if a Contract was selected or a ShortList options were selected or none of
     * them was selected. Based on this, it creates the Key that will be passed to ReportOptions
     * object.
     * 
     * @param fundReportParams
     * @param request
     * @return
     */
    private static String getContractOrShortlistUsed(FundReportParamsHolderVO fundReportParams,
            HttpServletRequest request) {
        
        FandpFilterCriteria fandpFilterCriteria = FundReportUtil
                .getLastExecutedFilterCriteria(request);
        
        if (!StringUtils.isBlank(fundReportParams.getContractNumberSelected())) {
            return ContractShortlistOptions
                    .createContractOrShortlistContractString(fundReportParams
                            .getContractNumberSelected());
        } else if (fandpFilterCriteria != null
                && FapConstants.SHORTLIST_FILTER_KEY.equals(fandpFilterCriteria
                        .getAdvanceFilterOption())) {
            Map<String, String> fundShortListOptions = fandpFilterCriteria.getFundShortListOptions();
            if (fundShortListOptions != null) {
                String shortListType = fundShortListOptions.get(FapConstants.SHORT_LIST_TYPE);
                String fundMenuSelect = fundShortListOptions.get(FapConstants.FUND_MENU_PACKAGE_SERIES);
                String allocationGroupSelect = fundShortListOptions.get(FapConstants.ASSET_ALLOCATION_GROUP);

                ShortlistKey shortlistKey = new ShortlistKey(shortListType, fundMenuSelect,
                        allocationGroupSelect);
                return ContractShortlistOptions.createContractOrShortlistString(shortlistKey);
            } else {
                return FundReportConstants.EMPTY_STRING;
            }
        } else {
            return FundReportConstants.EMPTY_STRING;
        }
	}

}

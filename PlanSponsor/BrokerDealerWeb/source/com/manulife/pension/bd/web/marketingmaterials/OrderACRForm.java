package com.manulife.pension.bd.web.marketingmaterials;

import static java.util.Calendar.YEAR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * The form bean for OrderACR page
 * 
 * @author Siby Thomas
 * 
 */
public class OrderACRForm extends AutoForm {
    
    private static final long serialVersionUID = 1L;
    
    public static final String FIELD_CONTRACT_NAME = "contractName";
    public static final String FIELD_CONTRACT_NUMBER = "contractNumber";
    public static final String FIELD_PRESENTER_FIRST_NAME = "presenterFirstName";
    public static final String FIELD_PRESENTER_LAST_NAME = "presenterLastName";
    public static final String FIELD_NUMBER__OF_COLOR_COPIES = "numberOfColorCopies";
    public static final String FIELD_NUMBER__OF_BLACK_AND_WHITE_COPIES = "numberOfBlackAndWhiteCopies";
    public static final String FIELD_OUTPUT_TYPE = "outputType";
    public static final String FIELD_RECIPIENT_FIRST_NAME = "recipientFirstName";
    public static final String FIELD_RECIPIENT_LAST_NAME = "recipientLastName";
    public static final String FIELD_COMPANY_NAME = "companyName";
    public static final String FIELD_STREET_NAME = "streetAddressName";
    public static final String FIELD_CITY_NAME = "cityAddressName";
    public static final String FIELD_STATE_NAME = "stateAddressName";
    public static final String FIELD_ZIPCODE = "zipCodeAddress";
    public static final String FIELD_YOUR_FIRST_NAME = "yourFirstName";
    public static final String FIELD_YOUR_LAST_NAME = "yourLastName";
    public static final String FIELD_PHONE_AREA_CODE = "areaCode";
    public static final String FIELD_SUBSCRIBER_NUMBER_1 = "subscriberNumber1";
    public static final String FIELD_SUBSCRIBER_NUMBER_2 = "subscriberNumber2";
    public static final String FIELD_PHONE_EXTENSION = "phoneNumberExtension";
    public static final String FIELD_EMAIL = "contactEmailAddress";
    
    public static final String YEAR_DEFAULT_VALUE = "Year";
    public static final String MONTH_DEFAULT_VALUE = "Month";
    
    public static final String OUTPUT_TYPE_PDF_ONLY = "PDF Only";
    public static final String OUTPUT_TYPE_PRINT_ONLY = "Print Only";
    public static final String OUTPUT_TYPE_PRINT_PDF = "Print & PDF";
    
    public static enum Months {
        Month("0"), January("01"), February("02"), March("03"), April("04"), May("05"), June("06"),
        July("07"), August("08"), September("09"), October("10"), November("11"), December("12");

        private String month;

        Months(String month) {
            this.month = month;
        }

        public String getMonth() {
            return this.month;
        }
    };

    // request details
    private String contractName = "";
    private String contractNumber = "";
    private String reportMonth = YEAR_DEFAULT_VALUE;
    private String reportYear = YEAR_DEFAULT_VALUE;
    private String presenterFirstName = "";
    private String presenterLastName = "";
    private String numberOfColorCopies = "";
    private String numberOfBlackAndWhiteCopies = "";
    private String outputType = "";
    
    // shipping address details
    private String recipientFirstName = "";
    private String recipientLastName = "";
    private String companyName = "";
    private String streetAddressName = "";
    private String cityAddressName = "";
    private String stateAddressName = "";
    private String zipCodeAddress = "";
    
    // contact information details
    private String yourFirstName = "";
    private String yourLastName = "";
    private String areaCode = "";
    private String subscriberNumber1 = "";
    private String subscriberNumber2 = "";
    private String phoneNumberExtension = "";
    private String contactEmailAddress = "";
    private String comments = "";
    
    private Collection<String> yearList;
    private Collection<Months> monthList;
    private Collection<String> monthValueList;
    private Collection<String> outputTypeList;
    
    /**
     * returns the contract name
     * 
     * @return String
     */
    public String getContractName() {
        return contractName;
    }
    /**
     * sets the contract name
     * 
     * @param contractName
     */
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    /**
     * returns the contract number
     * 
     * @return String
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * sets the contract number
     * 
     * @param contractNumber
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**
     * returns the report month
     * 
     * @return String
     */
    public String getReportMonth() {
        return reportMonth;
    }

    /**
     * sets the report month
     * 
     * @param reportMonth
     */
    public void setReportMonth(String reportMonth) {
        this.reportMonth = reportMonth;
    }

    /**
     * returns the report year
     * 
     * @return String
     */
    public String getReportYear() {
        return reportYear;
    }

    /**
     * sets the report year
     * 
     * @param reportYear
     */
    public void setReportYear(String reportYear) {
        this.reportYear = reportYear;
    }

    /**
     * returns the presenter first name
     * 
     * @return String
     */
    public String getPresenterFirstName() {
        return presenterFirstName;
    }

    /**
     * sets the presenter first name
     * 
     * @param presenterFirstName
     */
    public void setPresenterFirstName(String presenterFirstName) {
        this.presenterFirstName = presenterFirstName;
    }

    /**
     * returns the presenter last name
     * 
     * @return String
     */
    public String getPresenterLastName() {
        return presenterLastName;
    }

    /**
     * sets the presenter last name
     * 
     * @param presenterLastName
     */
    public void setPresenterLastName(String presenterLastName) {
        this.presenterLastName = presenterLastName;
    }

    /**
     * returns the number of color copies
     * 
     * @return String
     */
    public String getNumberOfColorCopies() {
        return numberOfColorCopies;
    }

    /**
     * sets the number of color copies
     * 
     * @param numberOfColorCopies
     */
    public void setNumberOfColorCopies(String numberOfColorCopies) {
        this.numberOfColorCopies = numberOfColorCopies;
    }

    /**
     * returns the number of B&W copies
     * 
     * @return String
     */
    public String getNumberOfBlackAndWhiteCopies() {
        return numberOfBlackAndWhiteCopies;
    }

    /**
     * sets the number of B&W copies
     * 
     * @param numberOfBlackAndWhiteCopies
     */
    public void setNumberOfBlackAndWhiteCopies(String numberOfBlackAndWhiteCopies) {
        this.numberOfBlackAndWhiteCopies = numberOfBlackAndWhiteCopies;
    }
    
    /**
     * returns the output type.
     * 
     * @return String outputType
     */
    public String getOutputType() {
        return outputType;
    }

    /**
     * sets the output type.
     * 
     * @param outputType
     */
    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    /**
     * returns the recipient first name
     * 
     * @return String
     */
    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    /**
     * sets the recipient first name
     * 
     * @param recipientFirstName
     */
    public void setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
    }

    /**
     * returns the recipient last name
     * 
     * @return String
     */
    public String getRecipientLastName() {
        return recipientLastName;
    }

    /**
     * sets the recipient last name
     * 
     * @param recipientLastName
     */
    public void setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
    }

    /**
     * returns the company name
     * 
     * @return String
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * sets the company name
     * 
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * returns the street address name
     * 
     * @return String
     */
    public String getStreetAddressName() {
        return streetAddressName;
    }

    /**
     * sets the street address name
     * 
     * @param streetAddressName
     */
    public void setStreetAddressName(String streetAddressName) {
        this.streetAddressName = streetAddressName;
    }

    /**
     * returns the city address name
     * 
     * @return String
     */
    public String getCityAddressName() {
        return cityAddressName;
    }

    /**
     * sets the city address name
     * 
     * @param cityAddressName
     */
    public void setCityAddressName(String cityAddressName) {
        this.cityAddressName = cityAddressName;
    }

    /**
     * returns the state address name
     * 
     * @return String
     */
    public String getStateAddressName() {
        return stateAddressName;
    }

    /**
     * sets the state address name
     * 
     * @param stateAddressName
     */
    public void setStateAddressName(String stateAddressName) {
        this.stateAddressName = stateAddressName;
    }

    /**
     * returns the zip code address
     * 
     * @return String
     */
    public String getZipCodeAddress() {
        return zipCodeAddress;
    }

    /**
     * sets the zip code address
     * 
     * @param zipCodeAddress
     */
    public void setZipCodeAddress(String zipCodeAddress) {
        this.zipCodeAddress = zipCodeAddress;
    }

    /**
     * returns the your first name
     * 
     * @return String
     */
    public String getYourFirstName() {
        return yourFirstName;
    }

    /**
     * sets the your first name
     * 
     * @param yourFirstName
     */
    public void setYourFirstName(String yourFirstName) {
        this.yourFirstName = yourFirstName;
    }

    /**
     * returns the your last name
     * 
     * @return String
     */
    public String getYourLastName() {
        return yourLastName;
    }

    /**
     * sets the your last name
     * 
     * @param yourLastName
     */
    public void setYourLastName(String yourLastName) {
        this.yourLastName = yourLastName;
    }

    /**
     * returns the area code
     * 
     * @return String
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * sets the area code
     * 
     * @param areaCode
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * returns the subscriber number1
     * 
     * @return String
     */
    public String getSubscriberNumber1() {
        return subscriberNumber1;
    }

    /**
     * sets the subscriber number1
     * 
     * @param subscriberNumber1
     */
    public void setSubscriberNumber1(String subscriberNumber1) {
        this.subscriberNumber1 = subscriberNumber1;
    }

    /**
     * returns the subscriber number2
     * 
     * @return String
     */
    public String getSubscriberNumber2() {
        return subscriberNumber2;
    }

    /**
     * sets the subscriber number2
     * 
     * @param subscriberNumber2
     */
    public void setSubscriberNumber2(String subscriberNumber2) {
        this.subscriberNumber2 = subscriberNumber2;
    }

    /**
     * returns the phone extension
     * 
     * @return String
     */
    public String getPhoneNumberExtension() {
        return phoneNumberExtension;
    }

    /**
     * sets the phone extension
     * 
     * @param phoneNumberExtension
     */
    public void setPhoneNumberExtension(String phoneNumberExtension) {
        this.phoneNumberExtension = phoneNumberExtension;
    }

    /**
     * returns the contact email address
     * 
     * @return String
     */
    public String getContactEmailAddress() {
        return contactEmailAddress;
    }

    /**
     * sets the contact email address
     * 
     * @param contactEmailAddress
     */
    public void setContactEmailAddress(String contactEmailAddress) {
        this.contactEmailAddress = contactEmailAddress;
    }

    /**
     * returns the comments
     * 
     * @return String
     */
    public String getComments() {
        return comments;
    }

    /**
     * sets the comments
     * 
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * returns the list of months in digits( 01, 02, ..)
     * 
     * @return Collection<String>
     */
    public Collection<String> getMonthValueList() {
        if (monthValueList != null) {
            return monthValueList;
        } else {
            monthValueList = new ArrayList<String>();
            monthValueList.add(Months.Month.getMonth());
            monthValueList.add(Months.January.getMonth());
            monthValueList.add(Months.February.getMonth());
            monthValueList.add(Months.March.getMonth());
            monthValueList.add(Months.April.getMonth());
            monthValueList.add(Months.May.getMonth());
            monthValueList.add(Months.June.getMonth());
            monthValueList.add(Months.July.getMonth());
            monthValueList.add(Months.August.getMonth());
            monthValueList.add(Months.September.getMonth());
            monthValueList.add(Months.October.getMonth());
            monthValueList.add(Months.November.getMonth());
            monthValueList.add(Months.December.getMonth());
            return monthValueList;
        }
    }

    /**
     * returns the list of months in Words(e.g January, February, ..)
     * 
     * @return Collection<Months>
     */
    public Collection<Months> getMonthList() {
        if (monthList != null) {
            return monthList;
        } else {
            monthList = new ArrayList<Months>();
            monthList.add(Months.Month);
            monthList.add(Months.January);
            monthList.add(Months.February);
            monthList.add(Months.March);
            monthList.add(Months.April);
            monthList.add(Months.May);
            monthList.add(Months.June);
            monthList.add(Months.July);
            monthList.add(Months.August);
            monthList.add(Months.September);
            monthList.add(Months.October);
            monthList.add(Months.November);
            monthList.add(Months.December);
            return monthList;
        }
    }

    /**
     * returns the list of years( current and previous two years)
     * 
     * @return Collection<String>
     */
    public Collection<String> getYearList() {
        if (yearList != null) {
            return yearList;
        } else {
            Calendar calendar = Calendar.getInstance();
            yearList = new ArrayList<String>();
            yearList.add(YEAR_DEFAULT_VALUE);
            yearList.add(String.valueOf(calendar.get(YEAR) - 2));
            yearList.add(String.valueOf(calendar.get(YEAR) - 1));
            yearList.add(String.valueOf(calendar.get(YEAR)));
            return yearList;
        }
    }
    
    /**
     * returns the list of output type
     * 
     * @return Collection<String> outputTypeList
     */
    public Collection<String> getOutputTypeList() {
        if (outputTypeList != null) {
            return outputTypeList;
        } else {
            outputTypeList = new ArrayList<String>();
            outputTypeList.add(OUTPUT_TYPE_PRINT_ONLY);
            outputTypeList.add(OUTPUT_TYPE_PDF_ONLY);
            outputTypeList.add(OUTPUT_TYPE_PRINT_PDF);
            return outputTypeList;
        }
    }

    /**
     * reset the form bean
     * 
     * @see AutoForm#reset(ActionMapping, HttpServletRequest)
     */
    @Override
    public void reset( HttpServletRequest request) {
        super.reset( request);
        
        contractName = "";
        contractNumber = "";
        reportMonth = YEAR_DEFAULT_VALUE;
        reportYear = YEAR_DEFAULT_VALUE;
        presenterFirstName = "";
        presenterLastName = "";
        numberOfColorCopies = "";
        numberOfBlackAndWhiteCopies = "";
        outputType = OUTPUT_TYPE_PRINT_ONLY;

        recipientFirstName = "";
        recipientLastName = "";
        companyName = "";
        streetAddressName = "";
        cityAddressName = "";
        stateAddressName = "";
        zipCodeAddress = "";

        yourFirstName = "";
        yourLastName = "";
        areaCode = "";
        subscriberNumber1 = "";
        subscriberNumber2 = "";
        phoneNumberExtension = "";
        contactEmailAddress = "";
        comments = "";
    }
}

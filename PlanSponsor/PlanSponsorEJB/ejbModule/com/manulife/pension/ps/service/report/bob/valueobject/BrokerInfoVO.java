package com.manulife.pension.ps.service.report.bob.valueobject;

/**
 * This class will hold the Broker related Information to be shown in BOB Summary.
 * 
 * This will be used when the Broker Info is taken from DB.
 * 
 * @author HArlomte
 * 
 */
public class BrokerInfoVO implements java.io.Serializable {

    private static final long serialVersionUID = -8549239459101455436L;

    private String brokerFullName;

    private String producerCode;

    private String bdFirmName;
    
    private String brokerFirstName;
    
    private String brokerLastName;
    
	private String frAddressLine1;
    
    private String frAddressLine2;
    
    private String frAddressLine3;
    
    private String cityName;
    
    private String stateCode;
    
    private String zipCode;

    public String getBrokerFullName() {
        return brokerFullName;
    }

    public void setBrokerFullName(String brokerFullName) {
        this.brokerFullName = brokerFullName;
    }

    public String getProducerCode() {
        return producerCode;
    }

    public void setProducerCode(String producerCode) {
        this.producerCode = producerCode;
    }

    public String getBdFirmName() {
        return bdFirmName;
    }

    public void setBdFirmName(String bdFirmName) {
        this.bdFirmName = bdFirmName;
    }

	public String getFrAddressLine1() {
		return frAddressLine1;
	}

	public void setFrAddressLine1(String frAddressLine1) {
		this.frAddressLine1 = frAddressLine1;
	}

	public String getFrAddressLine2() {
		return frAddressLine2;
	}

	public void setFrAddressLine2(String frAddressLine2) {
		this.frAddressLine2 = frAddressLine2;
	}

	public String getFrAddressLine3() {
		return frAddressLine3;
	}

	public void setFrAddressLine3(String frAddressLine3) {
		this.frAddressLine3 = frAddressLine3;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
   public String getBrokerLastName() {
			return brokerLastName;
		}

	public void setBrokerLastName(String brokerLastName) {
			this.brokerLastName = brokerLastName;
	}

	public String getBrokerFirstName() {
		return brokerFirstName;
	}

	public void setBrokerFirstName(String brokerFirstName) {
		this.brokerFirstName = brokerFirstName;
	}
}

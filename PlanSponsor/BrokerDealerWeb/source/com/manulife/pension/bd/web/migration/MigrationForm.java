package com.manulife.pension.bd.web.migration;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.myprofile.BrokerEntitiesForm;

public class MigrationForm extends BrokerEntitiesForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	private String password;
    private HashMap<String, String> addressFlagMap;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = StringUtils.trimToEmpty(userName);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = StringUtils.trimToEmpty(password);
	}
    
    /**
     * Returns a map for profile entitys and the address flags (to indicate which address is
     * used.(home/mailing))
     * 
     * @return
     */
    public HashMap<String, String> getAddressFlagMap() {
        return addressFlagMap;
    }

    /**
     * Sets the address flag map
     * 
     * @param addressFlagMap
     */
    public void setAddressFlagMap(HashMap<String, String> addressFlagMap) {
        this.addressFlagMap = addressFlagMap;
    }    
}

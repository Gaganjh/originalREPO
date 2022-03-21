package com.manulife.pension.platform.web.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.AbstractServiceDelegate;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.ps.service.report.fandp.FandpService;
import com.manulife.pension.ps.service.report.fandp.FandpServiceHome;
import com.manulife.pension.ps.service.report.fandp.valueobject.CustomQueryRow;
import com.manulife.pension.ps.service.report.fandp.valueobject.CustomQuerySavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.FundListSavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedDataLists;

/**
 * 
 * @author ayyalsa
 *
 */
public class FandpServiceDelegate extends PsAbstractServiceDelegate {
	
	private static FandpServiceDelegate instance = new FandpServiceDelegate();

	/**
	 * constructor
	 */
	public FandpServiceDelegate() {
	}

	/**
	 * @return FandpServiceDelegate
	 */
	public static FandpServiceDelegate getInstance() {
		return instance;
	}

    /**
     * @see AbstractServiceDelegate#getHomeClassName()
     */
    protected String getHomeClassName() {
        return FandpServiceHome.class.getName();
    }

    /**
     * @see AbstractServiceDelegate#getRemote(EJBHome)
     */
    protected EJBObject create() throws SystemException, RemoteException, CreateException {
        return ((FandpServiceHome)getHome()).create();
    }
    
    /**
     * 
     * @param profileId
     * @param savedDataType
     * @return
     * @throws SystemException
     */
    public UserSavedDataLists retrieveUserSavedDataLists(long profileId, String savedDataType) 
	throws SystemException {

    	FandpService service = (FandpService) getService();
    	UserSavedDataLists userSavedDataLists = null;

    	try {
    		userSavedDataLists = service.retrieveUserSavedDataLists(profileId, savedDataType);
        } catch (RemoteException e) {
        	logAndCreateSystemException(e);
        }	

        return userSavedDataLists;
	}

    /**
     * 
     * @param profileId
     * @param savedDataType
     * @param savedDataName
     * @return
     * @throws SystemException
     */
    public List<CustomQueryRow> retrieveCustomQuerySavedDataByName (
    		long profileId, 
    		String savedDataType,
    		String savedDataName) 
	throws SystemException {

    	FandpService service = (FandpService) getService();
    	List<CustomQueryRow> customQueryRowList = new ArrayList<CustomQueryRow>();
    	
    	try {
    		UserSavedDataLists userSavedDataLists = 
    			service.retrieveUserSavedDataLists(profileId, savedDataType);
    		
    		if (userSavedDataLists != null) {
    			CustomQuerySavedData customQuerySavedData = 
    				userSavedDataLists.getCustomQuery(savedDataName);
    			
    			if (customQuerySavedData != null && 
    					customQuerySavedData.getDataArrayList() != null) {
    				
    				for (Object obj : customQuerySavedData.getDataArrayList()) {
    					CustomQueryRow customQueryRow = (CustomQueryRow) obj;
    					customQueryRowList.add(customQueryRow);
    				}
    			}
    		}
        } catch (RemoteException e) {
        	logAndCreateSystemException(e);
        }	

        return customQueryRowList;
	}
    
    /**
     * 
     * @param profileId
     * @param savedDataType
     * @param savedDataName
     * @return
     * @throws SystemException
     */
    public List<String> retrieveFundListSavedDataByName (
    		long profileId, String savedDataType, String savedDataName) throws SystemException {

    	FandpService service = (FandpService) getService();
    	List<String> fundIdList = new ArrayList<String>();
    	
    	try {
    		UserSavedDataLists userSavedDataLists = 
    			service.retrieveUserSavedDataLists(profileId, savedDataType);
    		
    		if (userSavedDataLists != null) {
    			FundListSavedData fundListSavedData = 
    				userSavedDataLists.getFundList(savedDataName);
    			
    			if (fundListSavedData != null && 
    					fundListSavedData.getDataArrayList() != null) {
    				
    				for (Object obj : fundListSavedData.getDataArrayList()) {
    					String fundId = (String) obj;
    					fundIdList.add(fundId);
    				}
    			}
    		}
        } catch (RemoteException e) {
        	logAndCreateSystemException(e);
        }	

        return fundIdList;
	}
    
    /**
     * 
     * @param profileId
     * @param userSavedData
     * @param overwriteEisting
     * @return
     * @throws SystemException
     */
    public String insertUserData(long profileId, UserSavedData userSavedData, boolean overwriteEisting) 
	throws SystemException {
    	FandpService service = (FandpService) getService();
    	String duplicateInd = "";
    	
    	try {
    		duplicateInd = service.insertUserData(profileId, userSavedData, overwriteEisting);
        } catch (RemoteException e) {
        	logAndCreateSystemException(e);
        }	
        
        return duplicateInd;
	}
    
    /**
     * 
     * @param profileId
     * @param savedDataName
     * @param savedDataType
     * @throws RemoteException
     * @throws SystemException
     */
    public void deleteUserSavedDataLists(
            long profileId, String savedDataName, String savedDataType) 
	throws RemoteException, SystemException {
    	FandpService service = (FandpService) getService();
    	
    	try {
    		service.deleteUserSavedDataLists(profileId, savedDataName, savedDataType);
        } catch (RemoteException e) {
        	logAndCreateSystemException(e);
        }	
        
    }
}

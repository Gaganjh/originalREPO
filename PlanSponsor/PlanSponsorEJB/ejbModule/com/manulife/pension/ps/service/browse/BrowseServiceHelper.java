package com.manulife.pension.ps.service.browse;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.manulife.pension.content.RecoverableException;
import com.manulife.pension.content.service.BrowseService;
import com.manulife.pension.content.service.BrowseServiceHome;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.BaseServiceHelper;

/**
 * This class is the BrowseSErvice Helpser used by MailMessage.
 * 
 * @author Ilker Celikyilmaz
 */
public class BrowseServiceHelper extends BaseServiceHelper {
	private static BrowseServiceHelper instance = new BrowseServiceHelper();

	private BrowseServiceHelper() {
		super();
	}

	public static BrowseServiceHelper getInstance() {
		return instance;
	}
	
	public Content getContentById(int contentId, ContentType contentType, String siteLocation)
			throws RecoverableException, SystemException {
		Content content = null;
		try {

			BrowseServiceHome home = (BrowseServiceHome) getHome(BrowseServiceHome.class);
			BrowseService service = home.create();
			content = service.findContent(contentId, contentType, getLocation(siteLocation));
			service.remove();
		} catch (CreateException e) {
			throw new SystemException(e, getClass().getName(),
					"getContentById", "ContentId:" + contentId
							+ ", siteLocation:"+siteLocation);
		} catch (RemoveException e) {
			throw new SystemException(e, getClass().getName(),
					"getContentById", "ContentId:" + contentId
							+ ", siteLocation:"+siteLocation);
		} catch (RemoteException e) {
			throw new SystemException(e, getClass().getName(),
					"getContentById", "ContentId:" + contentId
							+ ", siteLocation:"+siteLocation);
		}

		return content;
	}	

	private static Location getLocation(String siteLocation)
	{
		Location returnLocation = null;
		if ("ny".equalsIgnoreCase(siteLocation))
			returnLocation = Location.NEW_YORK;
		else
			returnLocation = Location.USA;
		
		return returnLocation;
	}
}


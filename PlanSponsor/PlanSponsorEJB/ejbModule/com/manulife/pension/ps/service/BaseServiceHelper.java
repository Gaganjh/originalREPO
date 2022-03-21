package com.manulife.pension.ps.service;

import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.manulife.pension.service.cache.Cache;

/**
 * This is the base class for any service helper.
 * 
 * @author Ilker Celikyilmaz
 */
public class BaseServiceHelper {

	protected EJBHome getHome(Class homeClass) {

		EJBHome home = (EJBHome)Cache.getFromCache(
				EJBHome.class.getName(), homeClass.getName());

		if (home == null) {
			try {
				Context context = new InitialContext();
				home = (EJBHome) PortableRemoteObject.narrow(context.lookup(
				        homeClass.getName()), homeClass);
				Cache.cacheObject(EJBHome.class.getName(), homeClass.getName(),
						home, Cache.EXPIRES_NEVER);
			} catch (ClassCastException e) {
				//this.generalLog.error(e);
				throw new EJBException(e.toString());
			} catch (NamingException e) {
				//this.generalLog.error(e);
				throw new EJBException(e.toString());
			}

		}
		return home;
	}

}


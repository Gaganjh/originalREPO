package com.manulife.pension.platform.web.fap.customquery.util.propertymanager;

import java.util.Set;

public interface PropertyType {
	
	public Object getObjectValue(String key);

	public Set getKeySet(String key);

}


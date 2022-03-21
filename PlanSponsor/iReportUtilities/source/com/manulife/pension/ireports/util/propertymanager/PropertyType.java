package com.manulife.pension.ireports.util.propertymanager;

import java.util.Set;

public interface PropertyType {
	
	public Object getObjectValue(String key);

	public Set getKeySet(String key);

}


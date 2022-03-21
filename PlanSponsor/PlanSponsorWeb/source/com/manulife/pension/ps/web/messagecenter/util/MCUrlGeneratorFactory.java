package com.manulife.pension.ps.web.messagecenter.util;

public class MCUrlGeneratorFactory {
	private static MCUrlGenerator instance = new MCUrlGeneratorImpl();
	
	static public MCUrlGenerator getInstance() {
		return instance;
	}
}

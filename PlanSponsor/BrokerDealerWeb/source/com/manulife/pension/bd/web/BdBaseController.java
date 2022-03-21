package com.manulife.pension.bd.web;

import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.bd.web.util.Environment;

public class BdBaseController {
	
	
	static {
		CommonEnvironment.initialize(Environment.getInstance());
	}
	
	
	public static final String ERROR_KEY = CommonEnvironment.getInstance().getErrorKey();
}

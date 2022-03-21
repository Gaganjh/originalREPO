package com.manulife.pension.ps.web;

import com.manulife.pension.platform.web.util.CommonEnvironment;import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.Environment;

public class PsBaseUtil {
	
	
	static {
		CommonEnvironment.initialize(Environment.getInstance());
	}
	
	
	public static final String ERROR_KEY = "psErrors";
}

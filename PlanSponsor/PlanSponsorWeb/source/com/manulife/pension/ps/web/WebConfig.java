/**
 * 
 */
package com.manulife.pension.ps.web;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.manulife.pension.ps.web.participant.payrollSelfService.PayrollSelfServiceChangesInterceptor;

/**
 * @author natarde
 *
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	private static final Logger logger = Logger.getLogger(WebConfig.class);
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("adding interceptors of path patterns to handle the request");
		}
		registry.addInterceptor(new PayrollSelfServiceChangesInterceptor()).addPathPatterns("/participant/payrollSelfService");

	}
}

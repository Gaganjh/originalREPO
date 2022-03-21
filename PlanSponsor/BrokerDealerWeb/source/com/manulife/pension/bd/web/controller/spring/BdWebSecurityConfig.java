/**
 * 
 */
package com.manulife.pension.bd.web.controller.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.manulife.pension.bd.web.controller.spring.BdAccessDeniedHandler;

/**
 *  Used to handle CSRF security issue.
 * 
 * @author ganesja
 *
 */
@Configuration
@EnableWebSecurity
public class BdWebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*RequestMatcher matcher = new AntPathRequestMatcher("/do/bob/planReview/Customize/");
	    DelegatingRequestMatcherHeaderWriter headerWriter =
	        new DelegatingRequestMatcherHeaderWriter(matcher,new XFrameOptionsHeaderWriter(
	        		XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
	    http.headers().addHeaderWriter(headerWriter);*/
		http.headers().frameOptions().sameOrigin();
			http.csrf().ignoringAntMatchers("/login/loginServlet","/ViewRIAStatements","/ClearContentCacheServlet","/ClearCacheServlet","/PeriodicProcessServlet","/HeartBeatServlet","/activation/activationServlet","/unallocated/*","/PieChartServlet","/LineChartServlet","/BarChartServlet","/PingServlet","/do/iKit/getFundInfo/");
			http.exceptionHandling().accessDeniedHandler( new BdAccessDeniedHandler());
	}
	
	
}

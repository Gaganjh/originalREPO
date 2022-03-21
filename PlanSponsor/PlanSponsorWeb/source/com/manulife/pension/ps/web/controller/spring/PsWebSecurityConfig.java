/**
 * 
 */
package com.manulife.pension.ps.web.controller.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *  Used to handle CSRF security issue.
 * 
 * @author ganesja
 *
 */
@EnableWebSecurity
public class PsWebSecurityConfig  {
	
	@Configuration
    @Order(value = 2)
    public static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
           @Override
           protected void configure(HttpSecurity http) throws Exception {

		
			http.csrf().ignoringAntMatchers("/login/loginServlet","/StoredProcInvocationServlet","/servlet/DeleteIUMUser","/servlet/DeleteBIUser","/servlet/DeleteRMUser","/unallocated/*","/servlet/ResetInternalUserPassword","/servlet/AddBIUser","/servlet/AddRMUser","/servlet/AddIUMUser","/HeartBeatServlet","/PeriodicProcessServlet","/LineChartServlet","/BarChartServlet","/PieChartServlet","/tpadownload/tedFileDownloadServlet","/ClearContentCacheServlet","/ClearCacheServlet","/do/tools/sendDocumentUpload/","/do/tools/secureDocumentUpload/submit/","/do/noticemanager/updatestatus/");
			http.exceptionHandling().accessDeniedHandler( new PsAccessDeniedHandler());
	}
}
    @Configuration
    @Order(value = 1)
    public static class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {

           @Override
           protected void configure(HttpSecurity http) throws Exception {
                  http.antMatcher("/do/fap/fapFilterAction/**").headers().frameOptions().sameOrigin();
           }

}
    @Configuration
    @Order(value = 0)
    public static class RestSecurityConfiguration2 extends WebSecurityConfigurerAdapter {

           @Override
           protected void configure(HttpSecurity http) throws Exception {
                  http.antMatcher("/do/preferences/setReportPageSize/**").headers().frameOptions().sameOrigin();
           }

    }
}

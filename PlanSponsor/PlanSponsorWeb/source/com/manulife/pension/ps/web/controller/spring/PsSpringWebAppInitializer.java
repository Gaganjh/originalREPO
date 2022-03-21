package com.manulife.pension.ps.web.controller.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
 



import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import com.manulife.pension.ps.web.PsApplicationContextInitializer;
 
/**
 * Used as replacement of Web.xml
 * 
 */
@Configuration 
public class PsSpringWebAppInitializer implements WebApplicationInitializer {
 
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(PsContextConfig.class);
        // Dispatcher Servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher",
                new DispatcherServlet(appContext));
        dispatcher.setLoadOnStartup(2);
        dispatcher.addMapping("/WEB-INF/do/*");
         
        dispatcher.setInitParameter("contextClass", appContext.getClass().getName());
 
        servletContext.addListener(new ContextLoaderListener(appContext));
        servletContext.addListener(new PsApplicationContextInitializer());
        // Included Commons Multipart Resolver to fix Spring CSRF issue with Multipart forms.
        /**
         * IMPORTANT  Do not move springMultipartFilter below springSecurityFilterChain.
         * 
         * */
        servletContext.addFilter("springMultipartFilter", org.springframework.web.multipart.support.MultipartFilter.class)
        .addMappingForUrlPatterns(null, true, "/*");
        // UTF8 Charactor Filter.
        FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
      
        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");  
        FilterRegistration.Dynamic paramFr = servletContext.addFilter("ParamWrapperFilter", com.manulife.pension.ps.web.filter.ParamWrapperFilter.class);
        		paramFr.addMappingForUrlPatterns(null, true, "/*");
        paramFr.setInitParameter("excludeParams", "(.*\\.|^|.*|\\[('|\"))(c|C)lass(\\.|('|\")]|\\[).*");
            
        servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class)
        .addMappingForUrlPatterns(null, true, "/*");
        
    }
    
}
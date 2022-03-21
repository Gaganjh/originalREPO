package com.manulife.pension.bd.web.controller.spring;

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

import com.manulife.pension.bd.web.BDApplicationContextInitializer;
 
/**
 * Used as replacement of Web.xml
 * 
 */
@Configuration 
public class BdSpringWebAppInitializer implements WebApplicationInitializer {
 
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(BdContextConfig.class);
        
        System.out.println("Spring Webapp initializer for FRW");
        // Dispatcher Servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher",
                new DispatcherServlet(appContext));
        dispatcher.setLoadOnStartup(2);
        dispatcher.addMapping("/WEB-INF/do/*");
         
        dispatcher.setInitParameter("contextClass", appContext.getClass().getName());
 
        servletContext.addListener(new ContextLoaderListener(appContext));
        servletContext.addListener(new BDApplicationContextInitializer());
        
        servletContext.addFilter("springMultipartFilter", org.springframework.web.multipart.support.MultipartFilter.class)
        .addMappingForUrlPatterns(null, true, "/*");
        // UTF8 Charactor Filter.
        
        FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        fr.addMappingForUrlPatterns(null, true, "/*");  
        
      // FilterRegistration.Dynamic fapf = servletContext.addFilter("ShallowEtagHeaderFilter", ShallowFAPFilter.class);
        //fapf.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
       // fapf.setInitParameter("Content-Encoding", "gzip");
       // fapf.setInitParameter("forceEncoding", "true");
      // fapf.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*"); 
        FilterRegistration.Dynamic paramFr = servletContext.addFilter("ParamWrapperFilter", com.manulife.pension.bd.web.filter.ParamWrapperFilter.class);
        		paramFr.addMappingForUrlPatterns(null, true, "/*");
        paramFr.setInitParameter("excludeParams", "(.*\\.|^|.*|\\[('|\"))(c|C)lass(\\.|('|\")]|\\[).*");
       // Included Commons Multipart Resolver to fix Spring CSRF issue with Multipart forms.
       /**
        * IMPORTANT  Do not move springMultipartFilter below springSecurityFilterChain.
        * 
        * */
      
    servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class)
        .addMappingForUrlPatterns(null, true, "/*");
        
    }
    
}
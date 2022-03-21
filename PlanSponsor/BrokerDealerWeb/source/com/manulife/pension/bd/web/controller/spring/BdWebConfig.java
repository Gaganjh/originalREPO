package com.manulife.pension.bd.web.controller.spring;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
/**
 *  
 * @author ganesja
 *
 */

@Configuration
@EnableWebMvc
public class BdWebConfig implements WebMvcConfigurer {
	
	@Autowired
	ServletContext context;
 
    // Static Resource Config 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       // registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
     //   registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
       // registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
    }
     
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BdLoggingInterceptor());
    }
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
     
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("ApplicationResources");
        return messageSource;
    }
    @Bean
    public MessageSource messageResource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("pdf_reports");
        return messageSource;
    }
   
}
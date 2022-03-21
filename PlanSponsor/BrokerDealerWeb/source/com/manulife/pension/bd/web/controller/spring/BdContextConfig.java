package com.manulife.pension.bd.web.controller.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
 
/**
 * 
 * @author ganesja
 *
 */
@EnableWebMvc
@Configuration
@ComponentScan("com.manulife.pension.bd.web") 
public class BdContextConfig implements WebMvcConfigurer { 
    
	
	@Bean  
	public InternalResourceViewResolver viewResolver() {
		BdViewResolver resolver = new BdViewResolver();
		return resolver;
	}

    
    
    /**
     * Included Commons Multipart Resolver to fix Spring CSRF issue with Multipart forms.
     * 
     * @return
     */
  @Bean(name = "filterMultipartResolver")
    public CommonsMultipartResolver getMultipartResolver() {
    	CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    	multipartResolver.setMaxUploadSize(100000000);
        return multipartResolver;
    }
    

   
    
}
package com.manulife.pension.platform.web.exception;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.manulife.pension.exception.SystemException;


//@ControllerAdvice("com.manulife.pension.platform.web")
//@ControllerAdvice(value = "com.manulife.pension.platform.web")
//@ControllerAdvice(basePackages = "com.manulife.pension.platform.web")
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	public static final String DEFAULT_ERROR_VIEW = "systemErrorPage";
	@ExceptionHandler(value = SystemException.class)
	  public ModelAndView
	  defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
	    // If the exception is annotated with @ResponseStatus rethrow it and let
	    // the framework handle it - like the OrderNotFoundException example
	    // at the start of this post.
	    // AnnotationUtils is a Spring Framework utility class.
	    if (AnnotationUtils.findAnnotation
	                (e.getClass(), ResponseStatus.class) != null)
	      throw e;

	    // Otherwise setup and send the user to a default error-view.
	    ModelAndView mav = new ModelAndView();
	    mav.addObject("datetime", new Date());
	    mav.addObject("exception", e);
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName(DEFAULT_ERROR_VIEW);
	    return mav;
	  }
}

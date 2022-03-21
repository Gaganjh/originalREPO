package com.manulife.pension.platform.web.controller;

import java.io.Serializable;

public class ControllerForward implements Serializable {
	protected String name = null;
	 protected boolean configured = false;
	 protected String path = null;
	 protected boolean redirect = false;
	 
	    public String getName() {
	        return (this.name);
	    }
	
	    public void setName(String name) {
	        if (configured) {
	            throw new IllegalStateException("Configuration is frozen");
	        }
	        this.name = name;
	    }
	   
	    
	        public String getPath() {
	            return (this.path);
	        }
	    
	        public void setPath(String path) {
	            if (configured) {
	                throw new IllegalStateException("Configuration is frozen");
	            }
	            this.path = path;
	        }
	        public boolean getRedirect() {
	        	        return (this.redirect);
	        	    }
	        	
	        	    public void setRedirect(boolean redirect) {
	        	        if (configured) {
	        	            throw new IllegalStateException("Configuration is frozen");
	        	        }
	        	        this.redirect = redirect;
	        	    }
	        	    
	 public ControllerForward(String name, String path, boolean redirect) {
		 
		         super();
		         setName(name);
		         setPath(path);
		         setRedirect(redirect);
		 
		     }
	 public ControllerForward(String path, boolean redirect) {
		 
		         super();
		         setName(null);
		         setPath(path);
		         setRedirect(redirect);
		 
		     }

}

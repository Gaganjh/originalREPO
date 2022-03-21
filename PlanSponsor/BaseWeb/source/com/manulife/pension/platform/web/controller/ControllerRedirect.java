package com.manulife.pension.platform.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class  ControllerRedirect  {
	protected Logger logger = null;
	public ControllerRedirect(Class clazz) {
		logger = Logger.getLogger(clazz);
	}
	  protected Map parameterValues = null;
	  protected boolean redirect = false;
	  protected boolean configured = false;
	  private StringBuffer buf;
    protected String path = null;

    public String getPath() {
        if (buf == null) {
            return this.path;
        } else {
            return this.path +"?" + buf.toString();
        }
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
	      private void initializeParameters() {
	    	          parameterValues = new HashMap();
	    	      }
	  public ControllerRedirect() {
		           setRedirect(true);
		           initializeParameters();
		      }
	  public ControllerRedirect(String path) {
		  			setPath(path);
		           setRedirect(true);
		          initializeParameters();
		      }
	  public void addParameter(String fieldName, Object valueObj) {
		          
		  
		  if (buf == null) {
	            buf = new StringBuffer();
	        } else {
	            buf.append("&");
	        }
	        buf.append(fieldName);
	        buf.append("=");
	        String value = (valueObj != null) ? valueObj.toString() : "";
	        buf.append(value);
		         /* String value = (valueObj != null) ? valueObj.toString() : "";
		          if (parameterValues == null) {
		              initializeParameters();
		          }
		  
		          //try {
		              value = ResponseUtils.encodeURL(value);
		          //} catch (UnsupportedEncodingException uce) {
		              // this shouldn't happen since UTF-8 is the W3C Recommendation
		         //     String errorMsg = "UTF-8 Character Encoding not supported";
		         //     log.error(errorMsg, uce);
		         //     throw new RuntimeException(errorMsg, uce);
		         // }
		          
		          Object currentValue = parameterValues.get(fieldName);        
		          if (currentValue == null) {
		              // there's no value for this param yet; add it to the map
		              parameterValues.put(fieldName, value);
		              
		          } else if (currentValue instanceof String) {
		              // there's already a value; let's use an array for these parameters
		              String[] newValue = new String[2];
		              newValue[0] = (String) currentValue;
		              newValue[1] = value;
		              parameterValues.put(fieldName, newValue);
		              
		          } else if (currentValue instanceof String[]) {
		              // add the value to the list of existing values
		              List newValues = new ArrayList(Arrays.asList((Object[]) currentValue));
		              newValues.add(value);
		              parameterValues.put(fieldName, (String[]) newValues.toArray(new String[newValues.size()]));
		          }*/
		      }


}

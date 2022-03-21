package com.manulife.pension.platform.web.taglib.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class TagUtils extends TagSupport {

	private static final Logger log = Logger.getLogger(TagUtils.class);
	private static Method encode = null;
	private static final Map<String, Integer> scopes = new HashMap<String, Integer>();
	private static TagUtils instance = new TagUtils();

	static {
		try {
			Class[] args = new Class[] { String.class, String.class };

			encode = URLEncoder.class.getMethod("encode", args);
		} catch (NoSuchMethodException e) {
			log.debug("Could not find Java 1.4 encode method.  Using deprecated version.", e);
		}
		scopes.put("page", PageContext.PAGE_SCOPE);
		scopes.put("request", PageContext.REQUEST_SCOPE);
		scopes.put("session", PageContext.SESSION_SCOPE);
		scopes.put("application", PageContext.APPLICATION_SCOPE);
	}

	protected TagUtils() {
		super();
	}

	public static TagUtils getInstance() {
		return instance;
	}

	public static void setInstance(TagUtils instance) {
		TagUtils.instance = instance;
	}

	public void write(PageContext pageContext, String text) throws JspException {

		JspWriter writer = pageContext.getOut();

		try {
			writer.print(text);
		} catch (IOException e) {
			saveException(pageContext, e);
			throw new JspException(BaseBundle.getMessage("write.io"));
		}
	}

	public Object lookup(PageContext pageContext, String name, String scopeName) throws JspException {
		if (scopeName == null) {
			return pageContext.findAttribute(name);
		}

		try {
			return pageContext.getAttribute(name, instance.getScope(scopeName));
		} catch (JspException e) {
			saveException(pageContext, e);
			throw e;
		}
	}

	public void saveException(PageContext pageContext, Throwable exception) {
		pageContext.setAttribute(Constants.EXCEPTION_KEY, exception, PageContext.REQUEST_SCOPE);
	}

	public int getScope(String scopeName) throws JspException {
		Integer scope = (Integer) scopes.get(scopeName.toLowerCase());

		if (scope == null) {
			throw new JspException(BaseBundle.getMessage("lookup.scope", scope + ""));
		}

		return scope.intValue();
	}

	public Map computeParameters(PageContext pageContext, String paramId, String paramName, String paramProperty,
			String paramScope, String name, String property, String scope, boolean transaction) throws JspException {
		if ((paramId == null) && (name == null) && !transaction) {
			return (null);
		}
		
		Map map = null;
		try {
			if (name != null) {
				map = (Map) getInstance().lookup(pageContext, name, property, scope);
			}

		} catch (JspException e) {
			saveException(pageContext, e);
			throw e;
		}

		Map results = null;
		if (map != null) {
			results = new HashMap(map);
		} else {
			results = new HashMap();
		}

		if ((paramId != null) && (paramName != null)) {
			Object paramValue = null;

			try {
				paramValue = TagUtils.getInstance().lookup(pageContext, paramName, paramProperty, paramScope);
			} catch (JspException e) {
				saveException(pageContext, e);
				throw e;
			}

			if (paramValue != null) {
				String paramString = null;

				if (paramValue instanceof String) {
					paramString = (String) paramValue;
				} else {
					paramString = paramValue.toString();
				}

				Object mapValue = results.get(paramId);

				if (mapValue == null) {
					results.put(paramId, paramString);
				} else if (mapValue instanceof String[]) {
					String[] oldValues = (String[]) mapValue;
					String[] newValues = new String[oldValues.length + 1];

					System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
					newValues[oldValues.length] = paramString;
					results.put(paramId, newValues);
				} else {
					String[] newValues = new String[2];
					newValues[0] = mapValue.toString();
					newValues[1] = paramString;
					results.put(paramId, newValues);
				}
			}
		}

		if (transaction) {
			HttpSession session = pageContext.getSession();
			String token = null;

			if (session != null) {
				token = (String) session.getAttribute(Constants.TRANSACTION_TOKEN_KEY);
			}

			if (token != null) {
				results.put(Constants.TOKEN_KEY, token);
			}
		}

		return (results);
	}
public Object lookup(PageContext pageContext, String name, String property, String scope) throws JspException {
		
		Object bean = lookup(pageContext, name, scope);

		if (bean == null) {
			JspException e = null;

			if (scope == null) {
				e = new JspException(BaseBundle.getMessage("lookup.bean.any", name));
			} else {
				e = new JspException(BaseBundle.getMessage("lookup.bean", name, scope));
			}

			saveException(pageContext, e);
			throw e;
		}

		if (property == null) {
			return bean;
		}

		try {
			return PropertyUtils.getProperty(bean, property);
		} catch (IllegalAccessException e) {
			saveException(pageContext, e);
			throw new JspException(BaseBundle.getMessage("lookup.access", property, name));
		} catch (IllegalArgumentException e) {
			saveException(pageContext, e);
			throw new JspException(BaseBundle.getMessage("lookup.argument", property, name));
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();

			if (t == null) {
				t = e;
			}

			saveException(pageContext, t);
			throw new JspException(BaseBundle.getMessage("lookup.target", property, name));
		} catch (NoSuchMethodException e) {
			saveException(pageContext, e);

			String beanName = name;

			if (Constants.BEAN_KEY.equals(name)) {
				Object obj = pageContext.findAttribute(Constants.BEAN_KEY);

				if (obj != null) {
					beanName = obj.getClass().getName();
				}
			}

			throw new JspException(BaseBundle.getMessage("lookup.method", property, beanName));
		}
	}

	public boolean isXhtml(PageContext pageContext) {
		String xhtml = (String) pageContext.getAttribute(Constants.XHTML_KEY, PageContext.PAGE_SCOPE);

		return "true".equalsIgnoreCase(xhtml);
	}
	
	public static String computeURLWithCharEncoding(PageContext pageContext,
            String forward, String href, String page, String action, String module,
            Map params, String anchor, boolean redirect, boolean encodeSeparator,
            boolean useLocalEncoding)
            throws MalformedURLException {
            String charEncoding = "UTF-8";

            if (useLocalEncoding) {
                charEncoding = pageContext.getResponse().getCharacterEncoding();
            }

            // TODO All the computeURL() methods need refactoring!
            // Validate that exactly one specifier was included
            int n = 0;

            if (forward != null) {
                n++;
            }

            if (href != null) {
                n++;
            }

            if (page != null) {
                n++;
            }

            if (action != null) {
                n++;
            }

            if (n != 1) {
              /*  throw new MalformedURLException(messages.getMessage(
                        "computeURL.specifier"));*/
            }

            // Look up the module configuration for this request
           // ModuleConfig moduleConfig = getModuleConfig(module, pageContext);

            // Calculate the appropriate URL
            StringBuffer url = new StringBuffer();
            HttpServletRequest request =
                (HttpServletRequest) pageContext.getRequest();

            if (forward != null) {
               // ForwardConfig forwardConfig =
                //    moduleConfig.findForwardConfig(forward);

              //  if (forwardConfig == null) {
                //    throw new MalformedURLException(messages.getMessage(
               //             "computeURL.forward", forward));
               // }

                // **** removed - see bug 37817 ****
                //  if (forwardConfig.getRedirect()) {
                //      redirect = true;
                //  }

                if (forward.startsWith("/")) {
                    url.append(request.getContextPath());
                   /* url.append(RequestUtils.forwardURL(request, forwardConfig,
                            moduleConfig));*/
                } else {
                    url.append(forward);
                }
            } else if (href != null) {
                url.append(href);
            } else if (action != null) {
               // ActionServlet servlet = (ActionServlet) pageContext.getServletContext().getAttribute(Globals.ACTION_SERVLET_KEY);
              //  String actionIdPath = request.actionIdURL(action, moduleConfig, servlet);
               // if (actionIdPath != null) {
                   // action = actionIdPath;
                    //url.append(request.getContextPath());
                    url.append(action);
              //  } 
            //else {
                    //url.append(instance.getActionMappingURL(action, module,
                      //      pageContext, false));
               // }
            } else /* if (page != null) */
             {
                url.append(request.getContextPath());
             //   url.append(this.pageURL(request, page, moduleConfig));
            }

            // Add anchor if requested (replacing any existing anchor)
            if (anchor != null) {
                String temp = url.toString();
                int hash = temp.indexOf('#');

                if (hash >= 0) {
                    url.setLength(hash);
                }

                url.append('#');
                url.append(encodeURL(anchor, charEncoding));
            }

            // Add dynamic parameters if requested
            if ((params != null) && (params.size() > 0)) {
                // Save any existing anchor
                String temp = url.toString();
                int hash = temp.indexOf('#');

                if (hash >= 0) {
                    anchor = temp.substring(hash + 1);
                    url.setLength(hash);
                    temp = url.toString();
                } else {
                    anchor = null;
                }

                // Define the parameter separator
                String separator = null;

                if (redirect) {
                    separator = "&";
                } else if (encodeSeparator) {
                    separator = "&amp;";
                } else {
                    separator = "&";
                }

                // Add the required request parameters
                boolean question = temp.indexOf('?') >= 0;
                Iterator keys = params.keySet().iterator();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Object value = params.get(key);

                    if (value == null) {
                        if (!question) {
                            url.append('?');
                            question = true;
                        } else {
                            url.append(separator);
                        }

                        url.append(encodeURL(key, charEncoding));
                        url.append('='); // Interpret null as "no value"
                    } else if (value instanceof String) {
                        if (!question) {
                            url.append('?');
                            question = true;
                        } else {
                            url.append(separator);
                        }

                        url.append(encodeURL(key, charEncoding));
                        url.append('=');
                        url.append(encodeURL((String) value, charEncoding));
                    } else if (value instanceof String[]) {
                        String[] values = (String[]) value;

                        for (int i = 0; i < values.length; i++) {
                            if (!question) {
                                url.append('?');
                                question = true;
                            } else {
                                url.append(separator);
                            }

                            url.append(encodeURL(key, charEncoding));
                            url.append('=');
                            url.append(encodeURL(values[i], charEncoding));
                        }
                    } else /* Convert other objects to a string */
                     {
                        if (!question) {
                            url.append('?');
                            question = true;
                        } else {
                            url.append(separator);
                        }

                        url.append(encodeURL(key, charEncoding));
                        url.append('=');
                        url.append(encodeURL(value.toString(), charEncoding));
                    }
                }

                // Re-add the saved anchor (if any)
                if (anchor != null) {
                    url.append('#');
                    url.append(encodeURL(anchor, charEncoding));
                }
            }

            // Perform URL rewriting to include our session ID (if any)
            // but only if url is not an external URL
            if ((href == null) && (pageContext.getSession() != null)) {
                HttpServletResponse response =
                    (HttpServletResponse) pageContext.getResponse();

                if (redirect) {
                    return (response.encodeRedirectURL(url.toString()));
                }

                return (response.encodeURL(url.toString()));
            }

            return (url.toString());
        }
        public String encodeURL(String url) {
            return encodeURL(url, "UTF-8");
        }
        
        public static String encodeURL(String url, String enc) {
        	        try {
        	            if ((enc == null) || (enc.length() == 0)) {
        	                enc = "UTF-8";
        	            }
        	
        	            // encode url with new 1.4 method and UTF-8 encoding
        	            if (encode != null) {
        	                return (String) encode.invoke(null, new Object[] { url, enc });
        	            }
        	        } catch (Exception e) {
        	            
        	        }
        	
        	        return URLEncoder.encode(url);
        	    }
        
        
        public String computeURL(PageContext pageContext, String forward,
        		         String href, String page, String action, String module, Map params,
        		         String anchor, boolean redirect)
        		         throws MalformedURLException {
        		         return this.computeURLWithCharEncoding(pageContext, forward, href,
        		             page, action, module, params, anchor, redirect, false);
        		     }
        
        public String computeURLWithCharEncoding(PageContext pageContext,
        		         String forward, String href, String page, String action, String module,
        		         Map params, String anchor, boolean redirect, boolean useLocalEncoding)
        		         throws MalformedURLException {
        		         return computeURLWithCharEncoding(pageContext, forward, href, page,
        		             action, module, params, anchor, redirect, true, useLocalEncoding);
        		     }

		public String message(PageContext pageContext, String bundle, String locale, String key) {
			return key;
		}

		public String filter(String value) {
				         if ((value == null) || (value.length() == 0)) {
				             return value;
				         }
				 
				         StringBuffer result = null;
				         String filtered = null;
				 
				         for (int i = 0; i < value.length(); i++) {
				             filtered = null;
				 
				             switch (value.charAt(i)) {
				             case '<':
				                 filtered = "&lt;";
				 
				                 break;
				 
				            case '>':
				                filtered = "&gt;";
				
				                break;
				
				            case '&':
				                filtered = "&amp;";
				
				                break;
				
				            case '"':
				                filtered = "&quot;";
				
				                break;
				
				            case '\'':
				                filtered = "&#39;";
				
				                break;
				            }
				
				            if (result == null) {
				                if (filtered != null) {
				                    result = new StringBuffer(value.length() + 50);
				
				                    if (i > 0) {
				                        result.append(value.substring(0, i));
				                    }
				
				                    result.append(filtered);
				                }
				            } else {
				                if (filtered == null) {
				                    result.append(value.charAt(i));
				                } else {
				                    result.append(filtered);
				                }
				            }
				        }
				
				        return (result == null) ? value : result.toString();
		}
        
        
        	}



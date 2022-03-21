package com.manulife.pension.bd.web.filter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;


public class ParamWrapperFilter implements Filter {

	private static final Logger logger = Logger.getLogger(ParamWrapperFilter.class);
	
	private static final String DEFAULT_BLACKLIST_PATTERN = "(.*\\.|^|.*|\\[('|\"))(c|C)lass(\\.|('|\")]|\\[).*";
	private static final String INIT_PARAM_NAME = "excludeParams";

	private Pattern pattern;

	public void init( FilterConfig filterConfig ) throws ServletException {
		final String toCompile;
		final String initParameter = filterConfig.getInitParameter(INIT_PARAM_NAME);
		logger.debug("initParameter");
		if (initParameter != null && initParameter.trim().length()>0) {
			toCompile = initParameter;
		} else {
			toCompile = DEFAULT_BLACKLIST_PATTERN;
			if (logger.isDebugEnabled()) {
	        	logger.debug("body does not match blacklisted parameter pattern");
			}
		}
		this.pattern = Pattern.compile(toCompile, Pattern.DOTALL);		
	}

	public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
			throws IOException, ServletException {
		chain.doFilter(new ParamFilteredRequest(request, pattern), response);
		if (logger.isDebugEnabled()) {
        	logger.debug("[ParamFilteredRequest]:body does not match blacklisted parameter pattern");
		}
		}

	public void destroy() {
	}
	
	static class ParamFilteredRequest extends HttpServletRequestWrapper {

		private static final int BUFFER_SIZE = 128;

		private final String body;
		private final Pattern pattern;
		private final List requestParameterNames;
		private boolean read_stream = false;

		public ParamFilteredRequest( ServletRequest request, Pattern pattern ) {
			super((HttpServletRequest) request);
			this.pattern = pattern;

			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader bufferedReader = null;
			requestParameterNames = Collections.list((Enumeration) super.getParameterNames());
			InputStream inputStream = null;
			try {
				inputStream = request.getInputStream();

				if (inputStream != null) {
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

					char[] charBuffer = new char[BUFFER_SIZE];
					int bytesRead = -1;
					while ( (bytesRead = bufferedReader.read(charBuffer)) > 0 ) {
						stringBuilder.append(charBuffer, 0, bytesRead);
					}
				} else {
					stringBuilder.append("");
				}
			} catch ( IOException ex ) {
				logCatchedException(ex);
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch ( IOException ex ) {
						logCatchedException(ex);
					}
				}if (inputStream != null) {
					try {
						inputStream.close();
					} catch ( IOException ex ) {
						logCatchedException(ex);
					}
				}
			}
			body = stringBuilder.toString();

		}

		public Enumeration getParameterNames() {
			List finalParameterNames = new ArrayList();
			List parameterNames = Collections.list((Enumeration) super.getParameterNames());
			final Iterator iterator = parameterNames.iterator();
			while ( iterator.hasNext() ) {
				String parameterName = (String) iterator.next();
				if (!pattern.matcher(parameterName).matches()) {
					finalParameterNames.add(parameterName);
				}
			}
			return Collections.enumeration(finalParameterNames);
		}

		public ServletInputStream getInputStream() throws IOException {		    
		    final ByteArrayInputStream byteArrayInputStream;
		    if (pattern.matcher(body).matches()) {		        
		        byteArrayInputStream = new ByteArrayInputStream("".getBytes());
		    } else if (read_stream) {
		        byteArrayInputStream = new ByteArrayInputStream("".getBytes());
		    } else {
		        if (logger.isDebugEnabled()) {
		        	logger.debug("[getInputStream]: OK - body does not match blacklisted parameter pattern");
		        }
		        byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
		        read_stream = true;
		    }
		    return new ServletInputStream() {
		        public int read() throws IOException {
		            return byteArrayInputStream.read();
		        }
		    };
		}

		private void logCatchedException( IOException ex ) {
			logger.error("[ParamFilteredRequest]: Exception catched: ", ex);
		}


	}

	}


	 
	 
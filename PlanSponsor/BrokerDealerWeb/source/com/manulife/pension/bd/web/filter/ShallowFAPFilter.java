package com.manulife.pension.bd.web.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPOutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.http.HttpMethod;
import org.springframework.util.ClassUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

public class ShallowFAPFilter extends  OncePerRequestFilter {

	private static final String HEADER_ETAG = "ETag";
	 
    private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
 
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
 
    private static final String DIRECTIVE_NO_STORE = "no-store";
 
    private static final String STREAMING_ATTRIBUTE = ShallowEtagHeaderFilter.class.getName() + ".STREAMING";
 
 
    /** Checking for Servlet 3.0+ HttpServletResponse.getHeader(String) */
    private static final boolean servlet3Present =
            ClassUtils.hasMethod(HttpServletResponse.class, "getHeader", String.class);
 
 
    /**
     * The default value is "false" so that the filter may delay the generation of
     * an ETag until the last asynchronously dispatched thread.
     */
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }
 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	  HttpServletRequest httpRequest = (HttpServletRequest) request;
          HttpServletResponse httpResponse = (HttpServletResponse) response;
          String acceptEncoding = httpRequest.getHeader(HttpHeaders.ACCEPT_ENCODING);
          if (acceptEncoding != null) {
              if (acceptEncoding.indexOf("gzip") >= 0) {
                  GZIPHttpServletResponseWrapper gzipResponse = new GZIPHttpServletResponseWrapper(httpResponse);
                  filterChain.doFilter(request, gzipResponse);
                  gzipResponse.finish();
                  return;
              }
          }
          filterChain.doFilter(request, response);
 
      /*  HttpServletResponse responseToUse = response;
        if (!isAsyncDispatch(request) && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new HttpStreamingAwareContentCachingResponseWrapper(response, request);
        }
 
        filterChain.doFilter(request, responseToUse);
 
        if (!isAsyncStarted(request) && !isContentCachingDisabled(request)) {
            updateResponse(request, responseToUse);
        }*/
    }
 
    private void updateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
       // Assert.notNull(responseWrapper, "ContentCachingResponseWrapper not found");
        HttpServletResponse rawResponse = (HttpServletResponse) responseWrapper.getResponse();
        int statusCode = responseWrapper.getStatusCode();
     
        if (rawResponse.isCommitted()) {
        	   responseWrapper.setBufferSize(1719244);
            responseWrapper.copyBodyToResponse();
        }
        else if (isEligibleForEtag(request, responseWrapper, statusCode, responseWrapper.getContentInputStream())) {
            String responseETag = generateETagHeaderValue(responseWrapper.getContentInputStream());
            rawResponse.setHeader(HEADER_ETAG, responseETag);
            String requestETag = request.getHeader(HEADER_IF_NONE_MATCH);
            if (responseETag.equals(requestETag)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("ETag [" + responseETag + "] equal to If-None-Match, sending 304");
                }
                rawResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
            else {
                if (logger.isTraceEnabled()) {
                    logger.trace("ETag [" + responseETag + "] not equal to If-None-Match [" + requestETag +
                            "], sending normal response");
                }
                responseWrapper.copyBodyToResponse();
            }
        }
        else {
            if (logger.isTraceEnabled()) {
                logger.trace("Response with status code [" + statusCode + "] not eligible for ETag");
            }
            responseWrapper.setBufferSize(1719244);
            responseWrapper.copyBodyToResponse();
        }
    }
 
    /**
     * Indicates whether the given request and response are eligible for ETag generation.
     * <p>The default implementation returns {@code true} if all conditions match:
     * <ul>
     * <li>response status codes in the {@code 2xx} series</li>
     * <li>request method is a GET</li>
     * <li>response Cache-Control header is not set or does not contain a "no-store" directive</li>
     * </ul>
     * @param request the HTTP request
     * @param response the HTTP response
     * @param responseStatusCode the HTTP response status code
     * @param inputStream the response body
     * @return {@code true} if eligible for ETag generation; {@code false} otherwise
     */
    protected boolean isEligibleForEtag(HttpServletRequest request, HttpServletResponse response,
            int responseStatusCode, InputStream inputStream) {
 
        if (responseStatusCode >= 200 && responseStatusCode < 300 && HttpMethod.GET.matches(request.getMethod())) {
            String cacheControl = null;
            if (servlet3Present) {
                cacheControl = response.getHeader(HEADER_CACHE_CONTROL);
            }
            if (cacheControl == null || !cacheControl.contains(DIRECTIVE_NO_STORE)) {
                return true;
            }
        }
        return false;
    }
 
    /**
     * Generate the ETag header value from the given response body byte array.
     * <p>The default implementation generates an MD5 hash.
     * @param inputStream the response body as an InputStream
     * @return the ETag header value
     * @see org.frameworkset.util.DigestUtils
     */
    protected String generateETagHeaderValue(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder("\"0");
        DigestUtils.appendMd5DigestAsHex(inputStream, builder);
        builder.append('"');
        return builder.toString();
    }
 
 
    /**
     * This method can be used to disable the content caching response wrapper
     * of the ShallowEtagHeaderFilter. This can be done before the start of HTTP
     * streaming for example where the response will be written to asynchronously
     * and not in the context of a Servlet container thread.
     * @since 4.2
     */
    public static void disableContentCaching(ServletRequest request) {
       // Assert.notNull(request, "ServletRequest must not be null");
        request.setAttribute(STREAMING_ATTRIBUTE, true);
    }
 
    private static boolean isContentCachingDisabled(HttpServletRequest request) {
        return (request.getAttribute(STREAMING_ATTRIBUTE) != null);
    }
 
 
    private static class HttpStreamingAwareContentCachingResponseWrapper extends ContentCachingResponseWrapper {
 
        private final HttpServletRequest request;
 
        public HttpStreamingAwareContentCachingResponseWrapper(HttpServletResponse response, HttpServletRequest request) {
            super(response);
            this.request = request;
        }
 
       @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return (useRawResponse() ? getResponse().getOutputStream() : super.getOutputStream());
        }
 
    	private boolean useRawResponse() {
    		return isContentCachingDisabled(this.request);
    	}


		@Override
        public PrintWriter getWriter() throws IOException {
            return (useRawResponse() ? getResponse().getWriter() : super.getWriter());
        }
    }
	/*@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, responseWrapper);

        responseWrapper.copyBodyToResponse();
		
	}*/
    public class GZIPHttpServletResponseWrapper extends HttpServletResponseWrapper {

        private ServletResponseGZIPOutputStream gzipStream;
        private ServletOutputStream outputStream;
        private PrintWriter printWriter;

        public GZIPHttpServletResponseWrapper(HttpServletResponse response) throws IOException {
            super(response);
            response.addHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
        }

        public void finish() throws IOException {
            if (printWriter != null) {
                printWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (gzipStream != null) {
                gzipStream.close();
            }
        }

        @Override
        public void flushBuffer() throws IOException {
            if (printWriter != null) {
                printWriter.flush();
            }
            if (outputStream != null) {
                outputStream.flush();
            }
            super.flushBuffer();
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (printWriter != null) {
                throw new IllegalStateException("printWriter already defined");
            }
            if (outputStream == null) {
                initGzip();
                outputStream = gzipStream;
            }
            return outputStream;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (outputStream != null) {
                throw new IllegalStateException("printWriter already defined");
            }
            if (printWriter == null) {
                initGzip();
                printWriter = new PrintWriter(new OutputStreamWriter(gzipStream, getResponse().getCharacterEncoding()));
            }
            return printWriter;
        }

        @Override
        public void setContentLength(int len) {
        }

        private void initGzip() throws IOException {
            gzipStream = new ServletResponseGZIPOutputStream(getResponse().getOutputStream());
        }

    }

    public class ServletResponseGZIPOutputStream extends ServletOutputStream {

        GZIPOutputStream gzipStream;
        final AtomicBoolean open = new AtomicBoolean(true);
        OutputStream output;

        public ServletResponseGZIPOutputStream(OutputStream output) throws IOException {
            this.output = output;
            gzipStream = new GZIPOutputStream(output);
        }

        @Override
        public void close() throws IOException {
            if (open.compareAndSet(true, false)) {
                gzipStream.close();
            }
        }

        @Override
        public void flush() throws IOException {
            gzipStream.flush();
        }

        @Override
        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (!open.get()) {
                throw new IOException("Stream closed!");
            }
            gzipStream.write(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
            if (!open.get()) {
                throw new IOException("Stream closed!");
            }
            gzipStream.write(b);
        }

    }
}

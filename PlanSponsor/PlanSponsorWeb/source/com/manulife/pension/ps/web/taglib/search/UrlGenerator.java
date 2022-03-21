package com.manulife.pension.ps.web.taglib.search;



import java.io.Serializable;
import java.net.URL;
import java.util.Properties;

import com.manulife.pension.reference.Reference;
import com.manulife.pension.service.searchengine.SearchResult;

/**
 * Define the interface that a Url Generator must implement. Used with the
 * {@link UrlGeneratorFactory UrlGeneratorFactory},
 * it allow one to generate Url that will link to {@link Reference references}.
 * <p>
 * See {@link UrlGeneratorFactory UrlGeneratorFactory} for more information
 * on how to create and configure UrlGenerator that will act on different kind
 * of {@link Reference Reference} objects.
 * <p>
 * Class that implements this interface must be thread-safe.
 * <p>
 * Class that implements this interface <strong>must</strong> provide a no-arg
 * <strong>public</strong> constructor.
 *
 * @author Emmanuel Pirsch
 */
public interface UrlGenerator extends Serializable {

    /**
     * Generate a url for the specified reference.
     *
     * @param reference the {@link Reference Reference} object that will be used
     * to generate the url.
     * @param proeprties a {@link Properties Properties} object that contains
     * properties that may be used by the UrlGenerator to customize the
     * generated URLs. <code>null</code> value is allowed.
     */
    String generateUrl(Reference reference, Properties properties);

    /**
     * Generate a url for the specified reference. Make the generated url relative
     * to <code>baseUrl</code>.
     *
     * @param reference the {@link Reference Reference} object that will be used
     * to generate the url.
     * @param baseUrl the base url to use when generating the url.
     * @param proeprties a {@link Properties Properties} object that contains
     * properties that may be used by the UrlGenerator to customize the
     * generated URLs. <code>null</code> value is allowed.
     *
     * @return the generate url
     */
    String generateUrl(Reference reference, String baseUrl, Properties properties);

	/**
     * Generate a url for the specified reference. Make the generated url relative
     * to <code>baseUrl</code>.
     *
     * @param reference the {@link Reference Reference} object that will be used
     * to generate the url.
     * @param baseUrl the base url to use when generating the url.
     * @param proeprties a {@link Properties Properties} object that contains
     * properties that may be used by the UrlGenerator to customize the
     * generated URLs. <code>null</code> value is allowed.
     *
     * @return the generate url
     */
    String generateUrl(Reference reference, Properties properties, SearchResult result);

    /**
     * Generate a url for the specified reference. Make the generated url relative
     * to <code>baseUrl</code>.
     *
     * @param reference the {@link Reference Reference} object that will be used
     * to generate the url.
     * @param baseUrl the base url to use when generating the url.
     * @param proeprties a {@link Properties Properties} object that contains
     * properties that may be used by the UrlGenerator to customize the
     * generated URLs. <code>null</code> value is allowed.
     *
     * @return the generated url
     */
    URL generateUrl(Reference reference, URL url, Properties properties);
}


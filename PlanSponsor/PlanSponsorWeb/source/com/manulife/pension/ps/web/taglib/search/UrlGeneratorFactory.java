package com.manulife.pension.ps.web.taglib.search;


import java.util.Map;
import java.util.WeakHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.manulife.pension.reference.Reference;
import com.manulife.pension.ps.web.taglib.search.UrlGeneratorNotFoundException;



/**
 * The UrlGeneratorFactory class is used to retrieve the appropriate
 * {@link UrlGenerator UrlGenerator} for a specified {@link Reference Reference}
 * object.
 * <p>
 * {@link UrlGenerator UrlGenerator} return by this class will be cached for
 * faster retrieval at a later time. The cache uses weak references so the
 * memory it used may be reclaimed on low memory condition.
 * <p>
 * Configuration used to match {@link Reference Reference} class with
 * {@link UrlGenerator UrlGenerator} is done withing the web.xml deployement
 * descriptors. The logic used to retrieve the proper generator for a particular
 * reference type is as follow :
 * <p>
 * <ol>
 * <li>Obtain the fully qualified name of the {@link Reference Reference} object
 *     passed as an argument to {@link #getGenerator(Reference) getGenerator},
 * <li>Replace all the dots "." with forward slash "/". (we refer to this as
 *     <code>slashedClassname</code>.)
 * <li>Lookup in the default JNDI content for
 *     java:comp/env/UrlGeneratorFactory/[<code>slashedClassname</code>].
 * <li>If no context entry is found, obtain the {@link Reference Reference} object
 *     superclass and go back to step 2. Note that implemented interface will
 *     <strong>not</strong> be searched (to prevent to many search).
 * <li>If after traversing the class hierachy, no {@link UrlGenerator UrlGenerator}
 *     is found, then an {@link UrlGeneratorNotFoundException UrlGeneratorNotFoundException}
 *     will be thrown.
 * </ol>
 * <p>
 * Note that before taking the steps enumerated above, the cache will be searched
 * to prevent unecessary lookups.
 *
 * @author Emmanuel Pirsch
 */
public class UrlGeneratorFactory {
    private Logger logger= Logger.getLogger(UrlGeneratorFactory.class.getName());

    private static UrlGeneratorFactory instance;

    public static String BASE_ENVIRONMENT_CONTEXT = "java:comp/env/UrlGeneratorFactory/";

    private Map urlGeneratorCache= new WeakHashMap();

    private UrlGeneratorFactory() {
    }

    /**
     * Get an instance of the {@link UrlGeneratorFactory UrlGeneratorFactory}
     *
     * @return an instance of a {@link UrlGeneratorFactory UrlGeneratorFactory}
     */
    public static UrlGeneratorFactory getInstance() {
        // we are not thread safe, but the only side effect would be that a little
        // bit more memory (which will soon be reclaimed by the garbage collector)
        // will be used if many thread access this method and "instance" is null.
        if (instance == null) {
            instance= new UrlGeneratorFactory();
        }

        return instance;
    }

    /**
     * Obtain a {@link UrlGenerator UrlGenerator} that can be used to generate
     * url for the specified {@link Reference Reference}.
     * <p>
     * See {@link UrlGeneratorFaqctory UrlGeneratorFactory} class comment for
     * a more detailled description on how the appropriate {@link UrlGenerator UrlGenerator}
     * is located.
     */
    public UrlGenerator getGenerator(Reference reference) throws UrlGeneratorNotFoundException {
         String referenceClassname= reference.getClass().getName();
        UrlGenerator urlGenerator= (UrlGenerator)urlGeneratorCache.get(referenceClassname);

        if (urlGenerator == null) {
            urlGenerator= findGenerator(reference);
            urlGeneratorCache.put(referenceClassname, urlGenerator);
        }

        return urlGenerator;
    }

    /**
     * Search in the {@link Context Context} for a {@link UrlGenerator UrlGenerator}
     * that match the specified {@link Reference Reference}.
     * <p>
     * See {@link UrlGeneratorFaqctory UrlGeneratorFactory} class comment for
     * a more detailled description on how the appropriate {@link UrlGenerator UrlGenerator}
     * is located.
     *
     * @param reference
     *
     * @return a {@link UrlGenerator UrlGenerator} that match the {@link Reference Reference}.
     *
     * @throws UrlGeneratorNotFoundException if no matching {@link UrlGenerator UrlGenerator} are found.
     */
    private UrlGenerator findGenerator(Reference reference) throws UrlGeneratorNotFoundException {
        try {
            Context context= new InitialContext();
            boolean found= false;
            Class referenceClass= reference.getClass();
            StringBuffer contextEntry= new StringBuffer();
            String generatorClassname= null;

            searchInContext : do {
                String classname= referenceClass.getName();
                contextEntry.setLength(0);
                contextEntry.append(BASE_ENVIRONMENT_CONTEXT);
                contextEntry.append(classname.replace('.', '/'));

                try {
                    generatorClassname= (String)context.lookup(contextEntry.toString());
                    found= true;
                } catch (NameNotFoundException nameNotFound) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Did not find "+contextEntry.toString()+" in context", nameNotFound);
                    }
                    referenceClass= referenceClass.getSuperclass();
                    if (referenceClass == null) {
                        break searchInContext;
                    }
                }
            } while (!found);

            if (!found) {
                throw new UrlGeneratorNotFoundException(this.getClass().getName(), "findGenerator", "Unable to find a UrlGenerator for "+reference.getClass().getName()+" in context!");
            }

            return (UrlGenerator) Class.forName(generatorClassname).newInstance();
        } catch (NamingException namingException) {
            throw new UrlGeneratorNotFoundException(this.getClass().getName(), "findGenerator", namingException.toString());
        } catch (ClassNotFoundException urlGeneratorClassDoesNotExist) {
            throw new UrlGeneratorNotFoundException(this.getClass().getName(), "findGenerator", urlGeneratorClassDoesNotExist.toString());
        } catch (IllegalAccessException urlGeneratorClassIsNotAccessible) {
            throw new UrlGeneratorNotFoundException(this.getClass().getName(), "findGenerator", urlGeneratorClassIsNotAccessible.toString());
        } catch (InstantiationException unableToInstanciateUrlGenerator) {
            throw new UrlGeneratorNotFoundException(this.getClass().getName(), "findGenerator", unableToInstanciateUrlGenerator.toString());
        }
    }
}


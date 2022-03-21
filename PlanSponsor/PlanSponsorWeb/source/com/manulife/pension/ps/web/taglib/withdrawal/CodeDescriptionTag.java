/**
 * 
 * @ author kuthiha
 * Dec 12, 2006
 */
package com.manulife.pension.ps.web.taglib.withdrawal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Iterates over a collection and outputs the description for the specified key.
 * 
 * @author kuthiha
 */
public class CodeDescriptionTag extends SimpleTagSupport {

    /**
     * Represents the collection that needs to be iterated on to get the description.
     */
    private Collection collection;

    /**
     * The property name of the field that contains the value to search for.
     */
    private String keyName;

    /**
     * The property name of the field that contains the description.
     */
    private String keyValue;

    /**
     * The value to search for.
     */
    private String key;

    private static final Logger logger = Logger.getLogger(CodeDescriptionTag.class);

    /**
     * {@inheritDoc}
     */
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        String val = null;

        if (collection == null) {
            return;
        } // fi

        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            Object element = (Object) iter.next();
            String keyStr;
            try {
                keyStr = (String) BeanUtils.getProperty(element, keyName);
                if (StringUtils.equals(key, keyStr)) {
                    val = BeanUtils.getProperty(element, keyValue);
                    try {
                        out.print(val);
                        return;
                    } catch (IOException e) {
                        logger
                                .error("Got an IOException while processing [CodeDescriptionTag] ",
                                        e);
                        throw new JspException(e.toString());
                    } // end try/catch
                } // fi
            } catch (IllegalAccessException e) {
                logger.error(
                        "Got an IllegalAccessException while processing [CodeDescriptionTag] ", e);
                throw new JspException(e.toString());
            } catch (InvocationTargetException e) {
                logger.error(
                        "Got an InvocationTargetException while processing [CodeDescriptionTag] ",
                        e);
                throw new JspException(e.toString());
            } catch (NoSuchMethodException e) {
                logger.error("Got an NoSuchMethodException while processing [CodeDescriptionTag] ",
                        e);
                throw new JspException(e.toString());
            } // end try/catch
        } // end for
    }

    /**
     * @return the collection
     */
    public Collection getCollection() {
        return collection;
    }

    /**
     * @param collection the collection to set
     */
    public void setCollection(final Collection collection) {
        this.collection = collection;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * @return the keyName
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * @param keyName the keyName to set
     */
    public void setKeyName(final String keyName) {
        this.keyName = keyName;
    }

    /**
     * @return the keyValue
     */
    public String getKeyValue() {
        return keyValue;
    }

    /**
     * @param keyValue the keyValue to set
     */
    public void setKeyValue(final String keyValue) {
        this.keyValue = keyValue;
    }

}

package com.manulife.pension.ps.web.taglib.search;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.manulife.pension.reference.CategoryReference;
import com.manulife.pension.reference.ContentReference;
import com.manulife.pension.reference.FundReference;
import com.manulife.pension.reference.Reference;
import com.manulife.pension.service.searchengine.SearchResult;
import com.manulife.pension.util.content.helper.ContentUtility;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>@version 1.0</p>
 * <p> </p>
 * @author unascribed
 * @version 1.0
 */
public class TitleGeneratorTag extends TagSupport {
    private static Logger logger= Logger.getLogger(TitleGeneratorTag.class);

    /** @see #getBeanId() */
    private String beanId;

    /** @see #getProperty() */
    private String property;

    /** @see #getTitle() */
    private String title;

    /** @see #getTitleproperty */
    private String titleProperty;

    public TitleGeneratorTag() {
    }

    /**
     * The <code>beanId</code> property contain the name of a JavaBean in
     * some scope from which the {@link Reference Reference} object will be
     * retrieved.
     *
     * @return the name of the bean in some scope from which the
     * {@link Reference Reference} object will be returned.
     */
    public String getBeanId() {
        return beanId;
    }

    /**
     * Change the {@link #getBeanId beanId} property.
     *
     * @param beanId the new value of the property.
     */
    public void setBeanId(String beanId) {
        this.beanId= beanId;
    }


    public int doEndTag() throws javax.servlet.jsp.JspException {
        try {
            Object bean= pageContext.findAttribute(beanId);
            SearchResult searchResult= (SearchResult)bean;

            Reference reference= searchResult.getReference();
            String contentType;
            if (reference instanceof FundReference) {
                contentType= "ViewFunds";
            } else if (reference instanceof ContentReference) {
                contentType= ((ContentReference)reference).getContentType();
            } else if (reference instanceof CategoryReference) {
                contentType= ((CategoryReference)reference).getCategory();
            } else {
                if (logger.isDebugEnabled()) {
                	logger.debug("Unknown Reference type : "+reference.getClass());
            	}
                contentType= "unknown";
            }

            JspWriter out= pageContext.getOut();

            TitleSpecification titleSpecification=
                    TitleGeneratorConfig.getInstance().getSpecification(contentType, reference, UrlGeneratorTagConfig.getInstance().getProperties(pageContext.getSession(), (HttpServletRequest)pageContext.getRequest()));
            if (titleSpecification == null) {
                if (logger.isDebugEnabled()) {
                	logger.debug("Cannot find a TitleSpecification for contentType: "+contentType+", reference class: "+reference.getClass().getName());
            	}
                out.print("no title");
            } else {
                String title= titleSpecification.getTitle(searchResult, reference, ContentUtility.getRequestUrl(pageContext));
                out.print(title);
            }
        } catch (IOException unableToWriteToOut) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot write to JSP out", unableToWriteToOut);
            }
        }
        return super.doEndTag();
    }
}
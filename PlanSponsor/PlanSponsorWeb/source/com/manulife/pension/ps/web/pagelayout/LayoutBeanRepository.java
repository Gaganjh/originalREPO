package com.manulife.pension.ps.web.pagelayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.util.log.LogUtility;

/**
 * This singleton class loads all the LayoutBeans.
 * 
 */
public class LayoutBeanRepository {

    public final static String PAGE_ELEMENT_NAME = "page";

    public final static String PAGE_ID_ATTRIBUTE = "id";

    public final static String LAYOUTPAGE_ELEMENT_NAME = "layoutPage";

    public final static String STYLESHEET_ELEMENT_NAME = "styleSheet";

    public final static String JAVASCRIPT_ELEMENT_NAME = "javascript";

    public final static String HEADERPAGE_ELEMENT_NAME = "headerPage";

    public final static String MENUPAGE_ELEMENT_NAME = "menuPage";

    public final static String BODYPAGE_ELEMENT_NAME = "bodyPage";

    public final static String FOOTERPAGE_ELEMENT_NAME = "footerPage";

    public final static String CONTENTID_ELEMENT_NAME = "contentId";

    public final static String DEFINED_BENEFIT_CONTENTID_ELEMENT_NAME = "definedBenefitContentId";
    
    public final static String GIFL_SELECT_CONTENTID_ELEMENT_NAME = "giflSelectContentId";

    public final static String PARAMS_ELEMENT_NAME = "params";

    public final static String PARAM_ELEMENT_NAME = "param";

    public final static String PARAM_KEY_ATTRIBUTE = "key";

    public final static String PARAM_VALUE_ATTRIBUTE = "value";

    public final static String DEFAULTS_ELEMENT_NAME = "defaults";

    public final static String TPA_HEADERUSED_ELEMENT_NAME = "tpaHeaderUsed";

    public final static String SHOW_SELECT_CONTRACT_LINK_ELEMENT_NAME = "showSelectContractLink";
    
    public final static String EXCLUDE_DEFAULT_LAYOUT_JAVASCRIPTS_ELEMENT_NAME = "excludeDefaultLayoutJavascripts";
    

    private final static Logger logger = Logger
            .getLogger("com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository");

    private InputStream xmlFileStream = getClass().getClassLoader().getResourceAsStream(
            "./pagelayoutrepository.xml");

    private static LayoutBeanRepository instance = null;

    private Map<String, LayoutBean> pages;

    static {
        try {
            instance = new LayoutBeanRepository();
        } catch (SystemException e) {
            // This is already been logged.
        }
    }

    /**
     * Constructor for LayoutBeanRepository
     */
    private LayoutBeanRepository() throws SystemException {
        loadRepository();
    }

    public static LayoutBeanRepository getInstance() {
        return instance;
    }

    public LayoutBean getPageBean(final String id) {
        return pages.get(id);
    }

    /**
     * This method loads the security data from securityinfo.xml
     * 
     * @throws IOException If secuirtyinfo.xml does not exist.
     * @throws SAXException If the xml is not well formatted.
     */
    private void loadRepository() throws SystemException {
        DOMParser parser = new DOMParser();

        try {
            parser.parse(new InputSource(xmlFileStream));
            Document document = parser.getDocument();

            pages = new HashMap<String, LayoutBean>();

            // Set defaults values
            Element defaults = (Element) document.getElementsByTagName(DEFAULTS_ELEMENT_NAME).item(
                    0);

            Element defaultLayoutPageElement = (Element) defaults.getElementsByTagName(
                    LAYOUTPAGE_ELEMENT_NAME).item(0);
            String defaultLayoutPage = defaultLayoutPageElement.getFirstChild().getNodeValue();

            Element defaultHeaderPageElement = (Element) defaults.getElementsByTagName(
                    HEADERPAGE_ELEMENT_NAME).item(0);
            String defaultHeaderPage = defaultHeaderPageElement.getFirstChild().getNodeValue();

            Element defaultMenuPageElement = (Element) defaults.getElementsByTagName(
                    MENUPAGE_ELEMENT_NAME).item(0);
            String defaultMenuPage = defaultMenuPageElement.getFirstChild().getNodeValue();

            Element defaultTpaHeaderUsedElement = (Element) defaults.getElementsByTagName(
                    TPA_HEADERUSED_ELEMENT_NAME).item(0);
            String defaultTpaHeaderUsed = defaultTpaHeaderUsedElement.getFirstChild()
                    .getNodeValue();

            Element defaultShowSelectContractLinkElement = (Element) defaults.getElementsByTagName(
                    SHOW_SELECT_CONTRACT_LINK_ELEMENT_NAME).item(0);
            String defaultShowSelectContractLink = defaultShowSelectContractLinkElement
                    .getFirstChild().getNodeValue();

            Element defaultFooterPageElement = (Element) defaults.getElementsByTagName(
                    FOOTERPAGE_ELEMENT_NAME).item(0);
            String defaultFooterPage = defaultFooterPageElement.getFirstChild().getNodeValue();

            List styleSheets = new ArrayList();
            styleSheets.addAll(parseCollection(defaults, STYLESHEET_ELEMENT_NAME));

            List javascripts = new ArrayList();
            javascripts.addAll(parseCollection(defaults, JAVASCRIPT_ELEMENT_NAME));

            Element defaultContenIdElement = (Element) defaults.getElementsByTagName(
                    CONTENTID_ELEMENT_NAME).item(0);
            String defaultContentId = defaultContenIdElement.getFirstChild().getNodeValue();

            Element defaultParamsElement = (Element) defaults.getElementsByTagName(
                    PARAMS_ELEMENT_NAME).item(0);

            Map defaultParams = getParams(defaults, new HashMap());

            // Load page datas
            NodeList list = document.getElementsByTagName(PAGE_ELEMENT_NAME);
            int pageLen = list.getLength();

            for (int i = 0; i < pageLen; i++) {
                Element page = (Element) list.item(i);
                String id = page.getAttribute(PAGE_ID_ATTRIBUTE);

                String menu = page.getAttribute("menu");
                String submenu = page.getAttribute("submenu");
                String tpaMenu = page.getAttribute("tpaMenu");
                String tpaSubmenu = page.getAttribute("tpaSubmenu");

                String definedBenefitMenu = page.getAttribute("definedBenefitMenu");
                String definedBenefitSubmenu = page.getAttribute("definedBenefitSubmenu");
                // default to menu if definedBenefitMenu is not set
                if (definedBenefitMenu == null || definedBenefitMenu.length() == 0) {
                    definedBenefitMenu = menu;
                }
                // default to submenu if definedBenefitSummenu is not set
                if (definedBenefitSubmenu == null || definedBenefitSubmenu.length() == 0) {
                    definedBenefitSubmenu = submenu;
                }
                
                boolean isPBA = Boolean.valueOf(
                        page.getAttribute("isPBA") != null ? page.getAttribute("isPBA") : "false")
                        .booleanValue();

                String layoutPage = getTextValue(page, LAYOUTPAGE_ELEMENT_NAME, defaultLayoutPage);

                List styleSheetsForPage = new ArrayList(styleSheets);
                styleSheetsForPage.addAll(parseCollection(page, STYLESHEET_ELEMENT_NAME));
                
                boolean isExcludeDefaultLayoutJavascripts = Boolean.valueOf( getTextValue(page,EXCLUDE_DEFAULT_LAYOUT_JAVASCRIPTS_ELEMENT_NAME,"false"));
                List javascriptsForPage = new ArrayList();
                if(!isExcludeDefaultLayoutJavascripts) {
                	javascriptsForPage.addAll(javascripts);
                }                
                javascriptsForPage.addAll(parseCollection(page, JAVASCRIPT_ELEMENT_NAME));

                String headerPage = getTextValue(page, HEADERPAGE_ELEMENT_NAME, defaultHeaderPage);

                String tpaHeaderUsed = getTextValue(page, TPA_HEADERUSED_ELEMENT_NAME,
                        defaultTpaHeaderUsed);
                boolean isTpaHeaderUsed = Boolean.valueOf(tpaHeaderUsed).booleanValue();

                String showSelectContractLink = getTextValue(page,
                        SHOW_SELECT_CONTRACT_LINK_ELEMENT_NAME, defaultShowSelectContractLink);
                boolean boolShowSelectContractLink = Boolean.valueOf(showSelectContractLink)
                        .booleanValue();

                String menuPage = getTextValue(page, MENUPAGE_ELEMENT_NAME, defaultMenuPage);

                Element bodyPageElement = (Element) page
                        .getElementsByTagName(BODYPAGE_ELEMENT_NAME).item(0);
                String bodyPage = bodyPageElement.getFirstChild().getNodeValue();

                String footerPage = getTextValue(page, FOOTERPAGE_ELEMENT_NAME, defaultFooterPage);

                int contentId = Integer.parseInt(getTextValue(page, CONTENTID_ELEMENT_NAME,
                        defaultContentId));

                int definedBenefitContentId = Integer.parseInt(getTextValue(page, DEFINED_BENEFIT_CONTENTID_ELEMENT_NAME,
                        "0"));

                int giflSelectContentId = Integer.parseInt(getTextValue(page, GIFL_SELECT_CONTENTID_ELEMENT_NAME,
                "0"));

                LayoutBean bean = new LayoutBean(id, layoutPage, styleSheetsForPage,
                        javascriptsForPage, headerPage, menuPage, bodyPage, footerPage, contentId, definedBenefitContentId,
                        getParams(page, defaultParams), menu, submenu, isPBA, isTpaHeaderUsed,
                        boolShowSelectContractLink, tpaMenu, tpaSubmenu, definedBenefitMenu, definedBenefitSubmenu,giflSelectContentId);

                pages.put(id, bean);
            }

            if (logger.isDebugEnabled())
                logger.debug("pages=" + pages);

        } catch (IOException e) {
            SystemException se = new SystemException(e, this.getClass().getName(),
                    "loadRepository", "IOException");
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
            throw se;
        } catch (SAXException e) {
            SystemException se = new SystemException(e, this.getClass().getName(),
                    "loadRepository", "SAXException");
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
            throw se;
        }

    }

    private final Collection parseCollection(Element document, String elementName) {

        NodeList list = document.getElementsByTagName(elementName);
        int len = list.getLength();
        List retVals = new ArrayList();
        for (int j = 0; j < len; j++) {
            Element element = (Element) list.item(j);
            String value = element.getFirstChild().getNodeValue();
            retVals.add(value);
        }

        return retVals;
    }

    private final String getTextValue(Element document, String elementName, String defaultValue) {
        String retValue = defaultValue;
        NodeList list = document.getElementsByTagName(elementName);
        if (list != null && list.getLength() > 0) {
            Element element = (Element) list.item(0);
            if (element != null) {
                Node node = element.getFirstChild();
                retValue = node == null ? defaultValue : node.getNodeValue();
            }
        }

        return retValue;
    }

    private final Map getParams(Element rootElement, Map defaultParams) {
        Element paramsElement = (Element) rootElement.getElementsByTagName(PARAMS_ELEMENT_NAME)
                .item(0);

        Map params = new HashMap(defaultParams);

        if (paramsElement != null) {
            NodeList list = paramsElement.getElementsByTagName(PARAM_ELEMENT_NAME);
            int paramsCount = list.getLength();
            for (int i = 0; i < paramsCount; i++) {
                Element param = (Element) list.item(i);
                String key = param.getAttribute(PARAM_KEY_ATTRIBUTE);
                String value = param.getAttribute(PARAM_VALUE_ATTRIBUTE);
                params.put(key, value);
            }
        }
        return params;
    }

}

package com.manulife.pension.ireports.content;

import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentDescription;
import com.manulife.pension.content.valueobject.Disclaimer;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.content.valueobject.PageFootnote;
import com.manulife.pension.content.view.ContentFile;
import com.manulife.pension.util.content.taglib.ContentPageImage;

public class ContentUtility {

    private static final Logger logger = Logger.getLogger(ContentUtility.class);

    // Used for converting content to a cmaContent DOM node. (Used in NBDW PDF
    // Generation)
    private static String CMA_CONTENT_START_TAG = "<cmaContent>";

    private static String CMA_CONTENT_END_TAG = "</cmaContent>";

    // For removing some tags from content for use in XSL-FO PDFs:
    private static final Pattern PATTERN_LINE_BREAK = Pattern.compile("<br[^>]*>", Pattern.CASE_INSENSITIVE);

    private static final Pattern PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_EMPTY = Pattern.compile(
            "(<h4[^>]*>|</h4>|<P[^>]*>|<p[^>]*>|<ul[^>]*>|</ul>|<img[^>]*>|<div[^>]*>|</div>|<span[^>]*>|</span>|<font[^>]*>|</font>|<![^>]*>)",
            Pattern.CASE_INSENSITIVE);
    
    private static final Pattern PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_SPACE = Pattern.compile(
            "(<li[^>]*>|</li>|<a[^>]*>|</a>|&nbsp;|[\\x00-\\x1F\\x7F-\\xA8\\xAA-\\xAD\\xAF-\\xFF])", Pattern.CASE_INSENSITIVE);

	private static final Pattern PATTERN_PARAGRAPH_END_TAGS = Pattern
			.compile("</p[^>]*>|</P[^>]*>");

    // For removing many common tags from content for use in CSV files:
    private static final Pattern PATTERN_CSV_CONTENT_TAGS_TO_REPLACE_WITH_EMPTY = Pattern
            .compile(
                    "(<ul[^>]*>|</ul>|<a[^>]*>|</a>|<img[^>]*>|<div[^>]*>|</div>|<span[^>]*>|</span>|<font[^>]*>|</font>|<![^>]*>|<b[^>]*>|</b>|<strong[^>]*>|</strong>|<i[^>]*>|</i>|<u[^>]*>|</u>|<sup[^>]*>|</sup>|<sub[^>]*>|</sub>|<nobr[^>]*>|</nobr>)",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern PATTERN_CSV_CONTENT_TAGS_TO_REPLACE_WITH_SPACE = Pattern
            .compile("(<li[^>]*>|</li>|<p[^>]*>|</p>|<br/>|<br[^>]*>|&nbsp;|[\\x00-\\x1F\\x7F-\\xA8\\xAA-\\xAD\\xAF-\\xFF])",
                    Pattern.CASE_INSENSITIVE);

	private static final Pattern HTML_ENTITY_PATTERN_CONSECUTIVE_BR_TAGS = Pattern.compile(
			"&lt;br[^&gt;]*&gt;\\s*&lt;br[^&gt;]*&gt;", Pattern.CASE_INSENSITIVE);
	private static final Pattern HTML_ENTITY_PATTERN_LINE_BREAK = Pattern.compile(
			"&lt;br[^&gt;]*&gt;", Pattern.CASE_INSENSITIVE);
	private static final Pattern HTML_ENTITY_PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_EMPTY = Pattern.compile(
            "(&lt;P[^&gt;]*&gt;|&lt;p[^&gt;]*&gt;|&lt;ul[^&gt;]*&gt;|&lt;/ul&gt;|&lt;img[^&gt;]*&gt;|&lt;div[^&gt;]*&gt;|&lt;/div&gt;|&lt;span[^&gt;]*&gt;|&lt;/span&gt;|&lt;font[^&gt;]*&gt;|&lt;/font&gt;|&lt;![^&gt;]*&gt;)",
            Pattern.CASE_INSENSITIVE);	
	private static final Pattern HTML_ENTITY_PATTERN_CENTER_TAG = Pattern.compile("&lt;center[^&gt;]*&gt;|&lt;CENTER[^&gt;]*&gt;");
	private static final Pattern HTML_ENTITY_PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_SPACE = Pattern.compile(
            "(&lt;li[^&gt;]*&gt;|&lt;/li&gt;|&lt;a[^&gt;]*&gt;|&lt;/a&gt;|&nbsp;|[\\x00-\\x1F\\x7F-\\xA8\\xAA-\\xAD\\xAF-\\xFF])", Pattern.CASE_INSENSITIVE);
	private static final Pattern HTML_ENTITY_PATTERN_PARAGRAPH_TAG = Pattern.compile("&lt;p[^&gt;]*&gt;|&lt;P[^&gt;]*&gt;");
	private static final Pattern HTML_ENTITY_PATTERN_BLOCK_END_TAGS = Pattern
	            .compile("&lt;/p[^&gt;]*&gt;|&lt;/P[^&gt;]*&gt;|&lt;/center[^&gt;]*&gt;|&lt;/CENTER[^&gt;]*&gt;");
	private static final Pattern HTML_ENTITY_PATTERN_SUB_TAG = Pattern.compile("&lt;sub[^&gt;]*&gt;|&lt;SUB[^&gt;]*&gt;");
	private static final Pattern HTML_ENTITY_PATTERN_SUP_TAG = Pattern.compile("&lt;sup[^&gt;]*&gt;|&lt;SUP[^&gt;]*&gt;");
	private static final Pattern HTML_ENTITY_PATTERN_SUP_TAG_FRW = Pattern.compile("&lt;frwsup[^&gt;]*&gt;|&lt;FRWSUP[^&gt;]*&gt;");
	private static final Pattern HTML_ENTITY_PATTERN_BOLD_TAG = Pattern.compile("&lt;b[^&gt;]*&gt;|&lt;B[^&gt;]*&gt;|&lt;strong[^&gt;]*&gt;|&lt;STRONG[^&gt;]*&gt;");
	private static final Pattern HTML_ENTITY_PATTERN_ITALICS_TAG = Pattern.compile("&lt;i[^&gt;]*&gt;|&lt;I[^&gt;]*&gt;");
	private static final Pattern HTML_ENTITY_PATTERN_UNDERLINE_TAG = Pattern.compile("&lt;u[^&gt;]*&gt;|&lt;U[^&gt;]*&gt;");
	private static final Pattern HTML_ENTITY_PATTERN_INLINE_END_TAGS = Pattern
            .compile("&lt;/sub[^&gt;]*&gt;|&lt;/SUB[^&gt;]*&gt;|&lt;/sup[^&gt;]*&gt;|&lt;/SUP[^&gt;]*&gt;|&lt;/b[^&gt;]*&gt;|&lt;/B[^&gt;]*&gt;|&lt;/strong[^&gt;]*&gt;|&lt;/STRONG[^&gt;]*&gt;|&lt;/i[^&gt;]*&gt;|&lt;/I[^&gt;]*&gt;|&lt;/u[^&gt;]*&gt;|&lt;/U[^&gt;]*&gt;");
	
    /*
     * Replacement strings for the above patterns.
     */
    private static final String EMPTY_STRING = "";

    private static final String SPACE = " ";

    private static final String BREAK = "<br/>";

    // For replacing ampersand character with ampersand amp semi-colon for use
    // in XSL-FO PDFs:
    private static final String AMPERSAND = "& ";

    private static final String AMPERSAND_AMP = "&amp; ";

    // XSL-FO Conversion Patterns

    private static final Pattern PATTERN_CENTER_TAG = Pattern.compile("<center[^>]*>|<CENTER[^>]*>");

    private static final Pattern PATTERN_PARAGRAPH_TAG = Pattern.compile("<p[^>]*>|<P[^>]*>");

    private static final Pattern PATTERN_BLOCK_END_TAGS = Pattern
            .compile("</p[^>]*>|</P[^>]*>|</center[^>]*>|</CENTER[^>]*>");

    private static final Pattern PATTERN_SUB_TAG = Pattern.compile("<sub[^>]*>|<SUB[^>]*>");

    private static final Pattern PATTERN_SUP_TAG = Pattern.compile("<sup[^>]*>|<SUP[^>]*>");

    private static final Pattern PATTERN_BOLD_TAG = Pattern.compile("<b[^>]*>|<B[^>]*>|<strong[^>]*>|<STRONG[^>]*>");

    private static final Pattern PATTERN_ITALICS_TAG = Pattern.compile("<i[^>]*>|<I[^>]*>");

    private static final Pattern PATTERN_UNDERLINE_TAG = Pattern.compile("<u[^>]*>|<U[^>]*>");

    private static final Pattern PATTERN_INLINE_END_TAGS = Pattern
            .compile("</sub[^>]*>|</SUB[^>]*>|</sup[^>]*>|</SUP[^>]*>|</b[^>]*>|</B[^>]*>|</strong[^>]*>|</STRONG[^>]*>|</i[^>]*>|</I[^>]*>|</u[^>]*>|</U[^>]*>");

	private static final Pattern PATTERN_CONSECUTIVE_BR_TAGS = Pattern.compile("<br[^>]*>\\s*<br[^>]*>", Pattern.CASE_INSENSITIVE);

    // XSL-FO Tag Replacement Strings

    // replace all <br...> tags with <fo:block></fo:block>
    // replace all <center> with <fo:block text-align="center">
    // replace all <p> tags with <fo:block>
    // replace all </p> </center> tags with </fo:block>
    // replace all <sub> with <fo:inline baseline-shift="sub">
    // replace all <sup> with <fo:inline baseline-shift="super">
    // replace all <b> with <fo:inline font-weight="bold">
    // replace all <i> with <fo:inline font-style="italic">
    // replace all <u> with <fo:inline text-decoration="underline">
    // replace all </b> </i> </u> </sub> </sup> with </fo:inline>

    private static final String FO_INLINE_CENTER = "<fo:inline text-align=\"center\">";

    private static final String FO_BLOCK_START = "<fo:block>";
    private static final String FO_BLOCK_START_P_TAG = "<fo:block space-after=\"12pt\">";
    private static final String FO_BLOCK_PADDING_AFTER_TAG = "<fo:block padding-after=\"6pt\">";

    private static final String FO_BLOCK_END = "</fo:block>";

    private static final String FO_WRAPPER_START = "<fo:wrapper xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">";

    private static final String FO_WRAPPER_END = "</fo:wrapper>";

    private static final String FO_INLINE_SUBSCRIPT = "<fo:inline baseline-shift=\"sub\">";

    private static final String FO_INLINE_SUPERSCRIPT = "<fo:inline baseline-shift=\"super\">";
    
    private static final String FO_INLINE_SUPERSCRIPT_FRW = "<fo:inline baseline-shift=\"super\" font-size=\"8pt\">";

    private static final String FO_INLINE_BOLD = "<fo:inline font-weight=\"bold\">";

    private static final String FO_INLINE_ITALICS = "<fo:inline font-style=\"italic\">";

    private static final String FO_INLINE_UNDERLINE = "<fo:inline text-decoration=\"underline\">";

    private static final String FO_END_INLINE = "</fo:inline>";
    
	private static Integer ADMIN_GIFL_LINK_ID=59817;
	private static Integer ADMIN_GIFL_SELECT_LINK_ID=66718;
	
	//OB3 T3 Changes start
	private static Integer ADMIN_AUTO_DEF_LINK_ID=72485;
	private static Integer ADMIN_SIGN_DEF_LINK_ID=72486;
	//OB3 T3 Changes End
	
	private static Map<Integer,Integer> adminGiflLinksMap= new HashMap<Integer,Integer> ();
	//OB3 T3 Changes start
	private static Map<Integer,Integer> adminDefLinksMap= new HashMap<Integer,Integer> ();
	//OB3 T3 Changes End
	static 
	{
		adminGiflLinksMap.put(ADMIN_GIFL_LINK_ID, ADMIN_GIFL_LINK_ID);
		adminGiflLinksMap.put(ADMIN_GIFL_SELECT_LINK_ID, ADMIN_GIFL_SELECT_LINK_ID);
		
	}
	//OB3 T3 Changes start
	static 
	{
		adminDefLinksMap.put(ADMIN_AUTO_DEF_LINK_ID, ADMIN_AUTO_DEF_LINK_ID);
		adminDefLinksMap.put(ADMIN_SIGN_DEF_LINK_ID, ADMIN_SIGN_DEF_LINK_ID);
	}
	//OB3 T3 Changes End

	//JIRA#131 Bundled or Non-Bundled Content Ids - Start
	private static Integer MAINTENANCE_WEBSITE_LINK_ID =51723;
	private static Integer BUNDLED_WEBSITE_MAINTENANCE_LINK_ID =56565;
	private static Integer MAINTENANCE_CONTACT_LINK_ID=53707;
	private static Integer BUNDLED_MAINTENANCE_CONTACT_LINK_ID=82413;
	private static Integer MAINTENANCE_MONEY_LINK_ID=51726;
	private static Integer BUNDLED_MAINTENANCE_MONEY_LINK_ID=82417;
	private static Integer MAINTENANCE_ROTH_LINK_ID=55265;
	private static Integer BUNDLED_MAINTENANCE_ROTH_LINK_ID=82428;
	private static Integer WHO_DO_I_CALL_LINK_ID=51538;
	private static Integer BUNDLED_WHO_DO_I_CALL_LINK_ID=82325;
	private static Integer LOAN_LINK_ID =51651;
	private static Integer BUNDLED_LOAN_LINK_ID=82452;
	private static Integer FIDUCIARY_SUMMARY_ID=51473;
	private static Integer BUNDLED_FIDUCIARY_SUMMARY_ID=82426;
	private static Integer CONTACT_US_ID=51346;
	private static Integer BUNDLED_CONTACT_US_ID=82556;
	private static Integer RIGHT_HAND_DISPLAY_ID=52644;
	private static Integer BUNDLED_RIGHT_HAND_DISPLAY_ID=82414;
	private static Integer NOTICE_MANAG_LINK_ID = 88380;
	private static Map<Integer,Boolean> bundledLinksMap = new HashMap<Integer,Boolean> ();
	static 
	{ 
		bundledLinksMap.put(WHO_DO_I_CALL_LINK_ID, Boolean.FALSE); 
		bundledLinksMap.put(BUNDLED_WHO_DO_I_CALL_LINK_ID, Boolean.TRUE); 
		bundledLinksMap.put(LOAN_LINK_ID, Boolean.FALSE); 
		bundledLinksMap.put(BUNDLED_LOAN_LINK_ID, Boolean.TRUE); 
		bundledLinksMap.put(FIDUCIARY_SUMMARY_ID, Boolean.FALSE); 
		bundledLinksMap.put(BUNDLED_FIDUCIARY_SUMMARY_ID, Boolean.TRUE); 
		bundledLinksMap.put(MAINTENANCE_WEBSITE_LINK_ID, Boolean.FALSE); 
		bundledLinksMap.put(BUNDLED_WEBSITE_MAINTENANCE_LINK_ID, Boolean.TRUE); 
		bundledLinksMap.put(MAINTENANCE_CONTACT_LINK_ID, Boolean.FALSE); 
		bundledLinksMap.put(BUNDLED_MAINTENANCE_CONTACT_LINK_ID, Boolean.TRUE); 
		bundledLinksMap.put(MAINTENANCE_MONEY_LINK_ID, Boolean.FALSE); 
		bundledLinksMap.put(BUNDLED_MAINTENANCE_MONEY_LINK_ID, Boolean.TRUE); 
		bundledLinksMap.put(MAINTENANCE_ROTH_LINK_ID, Boolean.FALSE); 
		bundledLinksMap.put(BUNDLED_MAINTENANCE_ROTH_LINK_ID, Boolean.TRUE); 
		bundledLinksMap.put(CONTACT_US_ID, Boolean.FALSE); 
		bundledLinksMap.put(BUNDLED_CONTACT_US_ID, Boolean.TRUE); 
		bundledLinksMap.put(RIGHT_HAND_DISPLAY_ID, Boolean.FALSE); 
		bundledLinksMap.put(BUNDLED_RIGHT_HAND_DISPLAY_ID, Boolean.TRUE);
	} 
	//JIRA#131 Bundled or Non-Bundled Content Ids - End
	
	private static Map<Integer,Boolean> nmcLinksMap = new HashMap<Integer,Boolean> ();
	static 
	{ 
		nmcLinksMap.put(NOTICE_MANAG_LINK_ID, Boolean.FALSE); 
	}
    /** types of image */

    public static String getPageIntroduction(LayoutPage layoutPageBean) {
        return substituteParams(layoutPageBean.getIntroduction1(), null);
    }

    /**
     * A convenient method for the more generic getContentAttribute method.
     *
     * @see getContentAttribute(Content, String, String, String[])
     */
    public static String getContentAttribute(Content content, String attribute) {
        return getContentAttribute(content, attribute, null, null);
    }

    /**
     * Get an attribute of the given content bean and subsitute parameters if they exist.
     *
     * @param content The content bean.
     * @param attribute The attribute of the content bean.
     * @param member The member element of the attribute. If null, "text" is assumed.
     * @param params The text substitution parameters. This method can take a null params.
     * @return The text associated with the attribute.
     */
    public static String getContentAttribute(Content content, String attribute, String member, String[] params) {

        String text = getContentAttributeText(content, attribute, member);

        return substituteParams(text, params);
    }

    /**
     * Get an attribute of the given content bean with no string substitution.
     *
     * @param content The content bean.
     * @param attribute The attribute of the content bean.
     * @param member The member element of the attribute. If null, "text" is assumed.
     * @return The text associated with the attribute.
     */
    public static String getContentAttributeText(Object content, String attribute, String member) {

        if (member == null) {
            member = "text";
        }

        String text = null;

        try {
            Object obj = PropertyUtils.getProperty(content, attribute);
            if (obj instanceof Content || obj instanceof ContentDescription) {
                // get the sub-attribute
                obj = PropertyUtils.getProperty(obj, member);
            }
            if (obj != null) {
                text = obj.toString();
            }
        } catch (Exception e) {
            // ignore it, we'll return null text in this case.
        }

        return text;
    }

    /**
     * Returns the page image path.
     *
     * @param type One of the three types of images defined in ContentPageImage.
     * @param defaultImagePath Default image path when no image is found in CMA.
     * @return The page image path.
     * @see com.manulife.pension.util.content.taglib.ContentPageImage
     */
    public static String getPageImage(LayoutPage content, String type, String defaultImagePath) {

        ContentFile cfile = null;
        if (type != null && type.equalsIgnoreCase(ContentPageImage.TYPE_TOP)) {
            cfile = content.getTopImage();
        } else if (type != null && type.equalsIgnoreCase(ContentPageImage.TYPE_BOTTOM)) {
            cfile = content.getBottomImage();
        } else {
            cfile = content.getPageTitleImage();
        }

        String imagePath = defaultImagePath;
        if (cfile != null) {
            imagePath = cfile.getPath();
            if (logger.isDebugEnabled()) {
                logger.debug("Page image [" + type + "] is taken from CMA and is [" + imagePath + "]");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Page image [" + type + "] is taken from default");
            }
        }
        return imagePath;
    }

    /**
     * Substitute parameters in the given string.
     *
     * @param s The string to perform substitution.
     * @param params The list of parameters. If param is null, the original text is returned.
     * @return The substituted string.
     */
    public static String substituteParams(String s, String[] params) {

        if (params == null) {
            return s;
        }

        if (s == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int index = s.indexOf("'");
        while (index != -1) {
            sb.append(s.substring(0, index + 1));
            if (index < s.length() - 2 && (s.charAt(index + 1) == '{' || s.charAt(index + 1) == '}')
                    && s.charAt(index + 2) == '\'') {
                sb.append(s.substring(index + 1, index + 2));
                index += 2;
            }

            sb.append("'");

            s = s.substring(index + 1);
            index = s.indexOf("'");
        }
        sb.append(s);
        return MessageFormat.format(sb.toString(), (Object[]) params);
    }

    /**
     * Get a collection of how to links associated with a layout page
     *
     * @param content The content bean.
     * @return The collection of How To links
     */
    public static Collection getHowToLinks(Content bean) {

        Collection howTo = new ArrayList();

        if (bean != null) {
            if (bean instanceof LayoutPage) {
                LayoutPage layoutPage = (LayoutPage) bean;

                setHowTo(howTo, layoutPage.getLayer5());
                setHowTo(howTo, layoutPage.getLayer6());
                setHowTo(howTo, layoutPage.getLayer7());
                setHowTo(howTo, layoutPage.getLayer8());

            } else {
                logger.error("Not a page layout " + bean.getId());
                howTo = null;
            }
        } else {
            logger.error("Unable to find content bean in request scope for id " + bean.getId());
            howTo = null;
        }

        return howTo;
    }

    private static void setHowTo(Collection howTo, Content bean) {

        if (bean != null) {
            if (bean instanceof Miscellaneous) {
                howTo.add(bean);
            }
        }

    }

    /**
     * Get the page Footnotes of a given LayoutPagebean and subsitute parameters if they exist.
     *
     * @param content The content bean.
     * @param params The text substitution parameters. This method can take a null params.
     * @param index The footnote to be displayed or -1 for all footnotes
     * @return The text associated with the footnote.
     */
    public static String getPageFootnotes(Content content, String[] params, int index) {

        String text = "";

        if (content instanceof LayoutPage) {

            Content[] cts = ((LayoutPage) content).getFootnotes();
            if (cts != null) {
                if (index == -1) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < cts.length; i++) {
                        if (i > 0) {
                            sb.append("<br/>");
                        }
                        sb.append(((PageFootnote) cts[i]).getText());
                    }

                    text = sb.toString();
                } else if (cts.length > index) {
                    text = ((PageFootnote) cts[index]).getText();
                }
            }
        }

        return substituteParams(text, params);
    }

    /**
     * Get the page Footer of a given LayoutPagebean and subsitute parameters if they exist.
     *
     * @param content The content bean.
     * @param params The text substitution parameters. This method can take a null params.
     * @return The text associated with the footnote.
     */
    public static String getPageFooter(Content content, String[] params) {

        String text = "";

        if (content instanceof LayoutPage) {
            text = ((LayoutPage) content).getFooter1();
        }

        return substituteParams(text, params);
    }

    /**
     * Get the page Disclaimers of a given LayoutPagebean and subsitute parameters if they exist.
     *
     * @param content The content bean.
     * @param params The text substitution parameters. This method can take a null params.
     * @param index The disclaimer to be displayed or -1 for all disclaimers
     * @return The text associated with the disclaimer.
     */
    public static String getPageDisclaimer(Content content, String[] params, int index) {

        String text = "";

        if (content instanceof LayoutPage) {

            Content[] cts = ((LayoutPage) content).getDisclaimer();
            if (cts != null) {
                if (index == -1) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < cts.length; i++) {
                        if (i > 0) {
                            sb.append("<br/><br/>");
                        }
                        sb.append(((Disclaimer) cts[i]).getText());
                    }

                    text = sb.toString();
                } else if (cts.length > index) {
                    text = ((Disclaimer) cts[index]).getText();
                }
            }
        }

        return substituteParams(text, params);
    }

    public static String getRequestUrl(PageContext pageContext) {
        return getRequestUrl((HttpServletRequest) pageContext.getRequest());
    }

    public static String getRequestUrl(HttpServletRequest request) {
        java.util.Enumeration enumeration = request.getHeaders("host");
        String requestUrl = null;
        while (enumeration.hasMoreElements())
            requestUrl = (String) enumeration.nextElement();
        return requestUrl;
    }

    /**
     * Sort a given list of ContentDescription objects by pageId.
     *
     * @param list The list of ContentDescription objects. This list is modified for the calling program.
     *
     */
    public static void sortOnPageId(List list) {

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                ContentDescription firstContent = (ContentDescription) list.get(i);
                String first = firstContent.getPageId();
                ContentDescription secondContent = (ContentDescription) list.get(j);
                String second = secondContent.getPageId();
                if (first.compareTo(second) > 0) {
                    // switch positions
                    list.set(i, secondContent);
                    list.set(j, firstContent);
                }

            }
        }
    }

    /**
     * Stolen from VPS's utility library, this method will remove the special characters that are embedded in a String
     * (carriage return, newline, and formfeed)
     *
     * @param text The text that will be parsed.
     *
     * @return The text without the special characters
     */
    public static String jsEsc(String text) {

        char[] chars = text.toCharArray();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\r') {
                // carriage return
                sb.append(" ");
            } else if (chars[i] == '\n') {
                sb.append(" ");
                // newline
            } else if (chars[i] == '\f') {
                sb.append(" ");
                // formfeed
            } else {
                sb.append(chars[i]);
            }
        }

        return sb.toString();
    }

    /**
     * Strip unfriendly tags for use in FOP(PDF) files. This method assumes the user used the eWebEditPro editor which
     * is pretty good at ending most tags except the <br>
     * tag.
     *
     * @author Mark Eldridge
     * @param content - the content to be stripped of tags not available in FOP conversions
     * @return String - the content missing the tags we strip.
     */
    public static String filterCMAContentForPDF(String content) {
        // Fix all of the <br> by making them <br/>, and then
        if (content == null) {
            // Return empty string to avoid displaying null in PDF.
            return EMPTY_STRING;
        }
        content = PATTERN_LINE_BREAK.matcher(content).replaceAll(BREAK);
        content = PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_EMPTY.matcher(content).replaceAll(EMPTY_STRING);
        content = PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_SPACE.matcher(content).replaceAll(SPACE);
        content = PATTERN_PARAGRAPH_END_TAGS.matcher(content).replaceAll(BREAK+BREAK);
        
        // To avoid exception and collapse of displaying the content in PDF if
        // it contains '& ' char and
        // to display '& ' char, '& ' is replaced with '&amp; '
        content = content.replaceAll(AMPERSAND, AMPERSAND_AMP);
        return content;
    }

    /**
     * Strip a bunch of common tags that may show up. We can't account for everything, but this is a good start
     *
     * @author Mark Eldridge
     *
     */
    public static String filterCMAContentForCSV(String content) {
        // We replace line breaks and open paragraphs with just a space, then
        // remove all the other tags.
        content = PATTERN_CSV_CONTENT_TAGS_TO_REPLACE_WITH_SPACE.matcher(content).replaceAll(SPACE);
        content = PATTERN_CSV_CONTENT_TAGS_TO_REPLACE_WITH_EMPTY.matcher(content).replaceAll(EMPTY_STRING);
        return content;
    }

    /**
     * Creates a w3c DOM Node called cmaContent containing a DOM version of the provided CMA content string.
     *
     * @author eldrima
     * @param contentFromCMA A String containing content directly obtained from the CMA
     * @return A Node called cmaContent to be inserted into another DOM used to generate a PDF using FOP. null if there
     *         is a problem creating the node.
     *
     * @see convertCMAContentToXMLString
     * @see createContentDocument
     */
    public static Element createContentDOMNodeForPDF(String contentString) {
        Element contentElement = null;
        try {
            // Convert the content string to XML.
            String contentXMLString = convertCMAContentToXMLString(contentString);
            // Create a DOM document from the XML.
            Document contentXMLDocument = createContentDocument(contentXMLString);
            // Grab the first child node from that DOM and return it.
            contentElement = contentXMLDocument.getDocumentElement();
        } catch (Exception e) {
            logger
                    .error("CMA Content converted to XML could not then be rendered to w3c Document Node for use in PDF Generation :"  + e.getMessage());
        }
        return contentElement;
    }

    public static Element createContentDOMNodeFromXslFOString(String xslFOString) {
        Element contentElement = null;
        try {
            // Create a DOM document from the XSL FO String.
            Document xslFODocument = createContentDocumentNamespaceAware(xslFOString);
            // Grab the first child node from that DOM and return it.
            contentElement = xslFODocument.getDocumentElement();
        } catch (Exception e) {
            logger.error("XSL FO String could not then be rendered to w3c Document Node for use in PDF Generation: "
                    + e.getMessage());
        }
        return contentElement;
    }

    /**
     * Converts a string of XHTML CMA Content into XML contained in <cmaContent> </cmaContent> tags, and filters out
     * certain html tags from the content within the Sting passed using filterCMAContentForPDF.
     *
     * @author eldrima
     * @param cmaContent
     * @return String containing filtered XML enclosed in <cmaContent> </cmaContent> tags
     *
     * @see filterCMAContentForPDF
     */
    private static String convertCMAContentToXMLString(String cmaContent) {
        StringBuffer xmlStringBuffer = new StringBuffer();
        xmlStringBuffer.append("<!DOCTYPE entity SYSTEM \"EntityResolverDTD.dtd\">");
        xmlStringBuffer.append(CMA_CONTENT_START_TAG);
        xmlStringBuffer.append(ContentUtility.filterCMAContentForPDF(cmaContent));
        xmlStringBuffer.append(CMA_CONTENT_END_TAG);
        return xmlStringBuffer.toString();
    }

    public static String convertCMAContentToXSLFO(String content) {
    	return convertCMAContentToXSLFO(content, true, false);
    }
    
    public static String convertCMAContentToXSLFO(String content, boolean wrapContent, boolean lookForHTMLEntityTags) {

    	// replace all <br...> <br...> tags with <fo:block space-after="6pt" ></fo:block>
    	content = PATTERN_CONSECUTIVE_BR_TAGS.matcher(content).replaceAll(FO_BLOCK_PADDING_AFTER_TAG.concat(FO_BLOCK_END));
    	// replace all <br...> tags with <fo:block></fo:block>
        content = PATTERN_LINE_BREAK.matcher(content).replaceAll(FO_BLOCK_START.concat(FO_BLOCK_END));
        // replace all <center> with <fo:block text-align="center">
        content = PATTERN_CENTER_TAG.matcher(content).replaceAll(FO_INLINE_CENTER);
        // replace all <p> tags with <fo:block>
        content = PATTERN_PARAGRAPH_TAG.matcher(content).replaceAll(FO_BLOCK_START_P_TAG);

        // replace all </p> </center> tags with </fo:block>
        content = PATTERN_BLOCK_END_TAGS.matcher(content).replaceAll(FO_BLOCK_END);

        // strip unfriendly tags to avoid collapse of PDF display
        content = PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_EMPTY.matcher(content).replaceAll(EMPTY_STRING);
        content = PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_SPACE.matcher(content).replaceAll(SPACE);
        
        // replace all <sub> with <fo:inline baseline-shift="sub">
        content = PATTERN_SUB_TAG.matcher(content).replaceAll(FO_INLINE_SUBSCRIPT);
        // replace all <sup> with <fo:inline baseline-shift="super">
        content = PATTERN_SUP_TAG.matcher(content).replaceAll(FO_INLINE_SUPERSCRIPT);
        // replace all <b> with <fo:inline font-weight="bold">
        content = PATTERN_BOLD_TAG.matcher(content).replaceAll(FO_INLINE_BOLD);
        // replace all <i> with <fo:inline font-style="italic">
        content = PATTERN_ITALICS_TAG.matcher(content).replaceAll(FO_INLINE_ITALICS);
        // replace all <u> with <fo:inline text-decoration="underline">
        content = PATTERN_UNDERLINE_TAG.matcher(content).replaceAll(FO_INLINE_UNDERLINE);
        // replace all </b> </i> </u> </sub> </sup> with </fo:inline>
        content = PATTERN_INLINE_END_TAGS.matcher(content).replaceAll(FO_END_INLINE);

        // To avoid exception and collapse of displaying the content in PDF if
        // it contains '& ' char and
        // to display '& ' char, '& ' is replaced with '&amp; '
        content = content.replaceAll(AMPERSAND, AMPERSAND_AMP);
        
        if(lookForHTMLEntityTags) {
        	content = HTML_ENTITY_PATTERN_CONSECUTIVE_BR_TAGS.matcher(content).replaceAll(FO_BLOCK_PADDING_AFTER_TAG.concat(FO_BLOCK_END));
        	content = HTML_ENTITY_PATTERN_LINE_BREAK.matcher(content).replaceAll(FO_BLOCK_START.concat(FO_BLOCK_END));
            content = HTML_ENTITY_PATTERN_CENTER_TAG.matcher(content).replaceAll(FO_INLINE_CENTER);
            // replace all <p> tags with <fo:block>
            content = HTML_ENTITY_PATTERN_PARAGRAPH_TAG.matcher(content).replaceAll(FO_BLOCK_START_P_TAG);
            // replace all </p> </center> tags with </fo:block>
            content = HTML_ENTITY_PATTERN_BLOCK_END_TAGS.matcher(content).replaceAll(FO_BLOCK_END);
            // strip unfriendly tags to avoid collapse of PDF display
            content = HTML_ENTITY_PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_EMPTY.matcher(content).replaceAll(EMPTY_STRING);
            content = HTML_ENTITY_PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_SPACE.matcher(content).replaceAll(SPACE);
            // replace all <sub> with <fo:inline baseline-shift="sub">
            content = HTML_ENTITY_PATTERN_SUB_TAG.matcher(content).replaceAll(FO_INLINE_SUBSCRIPT);
            // replace all <sup> with <fo:inline baseline-shift="super">
            content = HTML_ENTITY_PATTERN_SUP_TAG.matcher(content).replaceAll(FO_INLINE_SUPERSCRIPT);
         // replace all <FRWSUP> with <fo:inline baseline-shift="super"> Exclusively for FRW PDF
            content = HTML_ENTITY_PATTERN_SUP_TAG_FRW.matcher(content).replaceAll(FO_INLINE_SUPERSCRIPT_FRW);
            // replace all <b> with <fo:inline font-weight="bold">
            content = HTML_ENTITY_PATTERN_BOLD_TAG.matcher(content).replaceAll(FO_INLINE_BOLD);
            // replace all <i> with <fo:inline font-style="italic">
            content = HTML_ENTITY_PATTERN_ITALICS_TAG.matcher(content).replaceAll(FO_INLINE_ITALICS);
            // replace all <u> with <fo:inline text-decoration="underline">
            content = HTML_ENTITY_PATTERN_UNDERLINE_TAG.matcher(content).replaceAll(FO_INLINE_UNDERLINE);
            // replace all </b> </i> </u> </sub> </sup> with </fo:inline>
            content = HTML_ENTITY_PATTERN_INLINE_END_TAGS.matcher(content).replaceAll(FO_END_INLINE);
        }
        
        if (wrapContent) {
        	return FO_WRAPPER_START.concat(content).concat(FO_WRAPPER_END);
    	} else {
    		return content;
    	}
    }

    /**
     * Creates a w3c Document from a given piece of XML.
     *
     * @author eldrima
     * @param contentXMLString
     * @return the Document created, or null if an exception occurs.
     *
     *
     */
    private static Document createContentDocument(String contentXMLString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(new CustomEntityResolver());
        document = builder.parse(new InputSource(new StringReader(contentXMLString)));
        return document;
    }

    /**
     * Creates a w3c namespace aware Document from a given piece of XML.
     *
     * @author HArlomte
     * @param contentXMLString
     * @return the name space aware Document created, or null if an exception occurs.
     *
     *
     */
    private static Document createContentDocumentNamespaceAware(String contentXMLString)
            throws Exception {
        StringBuffer xmlStringBuffer = new StringBuffer();
        xmlStringBuffer.append("<!DOCTYPE entity SYSTEM \"EntityResolverDTD.dtd\">");
        xmlStringBuffer.append(contentXMLString);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document document = null;
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(new CustomEntityResolver());
        document = builder.parse(new InputSource(new StringReader(xmlStringBuffer.toString())));
        return document;
    }
    public static boolean isAdminGuideGiflLink(int contentId){
    	return adminGiflLinksMap.containsKey(contentId);
    }
	//OB3 T3 Changes Start
    public static boolean isAdminGuideDefLink(int contentId){
    	return adminDefLinksMap.containsKey(contentId);
    }
	//OB3 T3 Changes End
    
  //JIRA#131 Bundled or Non-Bundled Content Ids - Start
    public static boolean isAdminGuideBundledLink(int contentId){
    	return bundledLinksMap.containsKey(contentId);
    }
    
  //NMC - Start
    public static boolean isAdminGuideLink(int contentId){
    	return nmcLinksMap.containsKey(contentId);
    }
	//NMC - End
    /**
     * This method returns GIFL select or GIFL content id needs to be skipped to display inadmin guide page 
     * @param giflVersion3Indicator
     * @param contentId
     * @return true or false 
     */
	public static boolean isAdminGuideGiflContentId(String giflVersion3Indicator, int contentId){
		boolean isGiflContentId = false;
		//Skip one of the two values GIFl or GIFL select based on the GIFL version
		if(("true".equals(giflVersion3Indicator) && 
				!ADMIN_GIFL_SELECT_LINK_ID.equals(contentId)) || 
					(!"true".equals(giflVersion3Indicator)&& 
							!ADMIN_GIFL_LINK_ID.equals(contentId)))
		{
			isGiflContentId = true;
		}
		return isGiflContentId;
	}	
	//JIRA#131 Changes Start
    /**
     * This method returns which content ids to be skipped to display in admin guide page, based on Bundled Indicator 
     * @param deferralMode
     * @param contentId
     * @return true or false 
     */
	public static boolean skipAdminGuideBundledContent(String isContractBundledInd, int contentId) {
		if (!bundledLinksMap.containsKey(contentId)) {
			// this content isn't affected by bundling, so display as usual
			return false;
		}
		Boolean isBundledContent = bundledLinksMap.get(contentId);
		boolean isContractBundled = "true".equals(isContractBundledInd);
		if (((isContractBundled && !isBundledContent)) 
				|| (!isContractBundled && isBundledContent)) { 
			// contract and content bundled indicator don't match, so skip this content return true; 
			return true; 
		} else	{ 
			return false;
		} 
	}
	
	//NMC Changes Start
    /**
     * This method returns which content ids to be skipped to display in admin guide page, based on Bundled Indicator 
     * @param deferralMode
     * @param contentId
     * @return true or false 
     */
	public static boolean skipAdminGuideContent(String isNmcInd, int contentId) {
		if (!nmcLinksMap.containsKey(contentId)) {
			// this content isn't affected by bundling, so display as usual
			return false;
		}
		Boolean isNmcContent = nmcLinksMap.get(contentId);
		boolean isNmcContractInd = "false".equals(isNmcInd);
		if (((isNmcContractInd && !isNmcContent)) 
				|| (!isNmcContractInd && isNmcContent)) { 
			// contract and content  indicator don't match, so skip this content return true; 
			return true; 
		} else	{ 
			return false;
		} 
	}
	//NMC Changes End

	//OB3 T3 Changes Start
    /**
     * This method returns Auto select or Deferral content id needs to be skipped to display in admin guide page 
     * @param deferralMode
     * @param contentId
     * @return true or false 
     */
	public static boolean isAdminGuideDefContentId(String deferralMode, int contentId){
		boolean isDefContentId = false;
		//Skip one of the two values GIFl or GIFL select based on the GIFL version
		if(("A".equals(deferralMode) && 
				!ADMIN_AUTO_DEF_LINK_ID.equals(contentId)) || 
					("S".equals(deferralMode)&& 
							!ADMIN_SIGN_DEF_LINK_ID.equals(contentId)))
		{
			isDefContentId = true;
		}
		return isDefContentId;
	}	
	//OB3 T3 Changes End

	public static class CustomEntityResolver implements EntityResolver {

		public InputSource resolveEntity(String publicId, String systemId) {
			if (systemId.endsWith("EntityResolverDTD.dtd")) {
				// Return local copy of the EntityResolverDTD.dtd file
				return new InputSource(this.getClass().getClassLoader()
						.getResource("EntityResolverDTD.dtd").toString());
			} else {
				// Return null to make process resume normally
				return null;
			}
		}
	
	}
}

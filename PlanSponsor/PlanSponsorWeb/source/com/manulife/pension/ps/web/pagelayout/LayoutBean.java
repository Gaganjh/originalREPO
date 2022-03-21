package com.manulife.pension.ps.web.pagelayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;

/**
 * This bean is used to keep all the required info to build the page. Layout
 * jsps is basically using them. This is a mutable bean.
 *
 * @author Ilker Celikyilmaz
 */
public class LayoutBean implements Cloneable{
	private String id;
	private String layoutURL;
	private List styleSheets;
	private List javascripts;
	private String headerPage;
	private String menuPage;
	private String bodyPage;
	private String footerPage;
	private int contentId;
    private int definedBenefitContentId;
	private Map params;
	private String menu = "0";
	private String submenu = "0";
    private String tpaMenu = "0";
    private String tpaSubmenu = "0";
    private String definedBenefitMenu = "0";
    private String definedBenefitSubmenu = "0";
    private boolean pba = false;
	private boolean tpaHeaderUsed;
	private boolean showSelectContractLink;
	private int giflSelectContentId;

	public LayoutBean(String id, String layoutURL, Collection styleSheets,
			Collection javascripts, String headerPage, String menuPage,
			String bodyPage, String footerPage, int contentId, int definedBenefitContentId, Map params,
			String menu, String subMenu, boolean isPBA, boolean tpaHeaderUsed,
			boolean showSelectContractLink, String tpaMenu, String tpaSubmenu,
            String definedBenefitMenu, String definedBenefitSubmenu, int giflSelectContentId)  
	{
		this.id = id;
		this.layoutURL = layoutURL;
		this.styleSheets = new ArrayList();
		this.styleSheets.addAll(styleSheets);
		this.javascripts = new ArrayList();
		this.javascripts.addAll(javascripts);
		this.headerPage = headerPage;
		this.menuPage = menuPage;
		this.bodyPage = bodyPage;
		this.footerPage = footerPage;
		this.contentId = contentId;
        this.definedBenefitContentId = definedBenefitContentId;
		this.params = params;
		this.menu = menu;
		this.submenu = subMenu;
		this.pba = isPBA;
		this.tpaHeaderUsed = tpaHeaderUsed;
		this.showSelectContractLink = showSelectContractLink;
        this.tpaMenu = tpaMenu;
        this.tpaSubmenu = tpaSubmenu;
        this.definedBenefitMenu = definedBenefitMenu;
        this.definedBenefitSubmenu = definedBenefitSubmenu;
        this.giflSelectContentId= giflSelectContentId; 

    }

	/**
	 * This is a number describing a menu. ie "1" for the first menu
	 */
	public String getMenu()
	{
		return menu;
	}



	/**
	 * This is a number describing a submenu. ie "1" for the first submenu
	 */
	public String getSubmenu()
	{
		return submenu;
	}


	/**
	 * This is a number describing a submenu. ie "1" for the first submenu
	 */
	public void setSubmenu(String value)
	{
		this.submenu = value;
	}

	/**
	 * Gets the getId
	 *
	 * @return Returns a Stirng
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the layoutPage
	 *
	 * @return Returns a String
	 */
	public String getLayoutURL() {
		return layoutURL;
	}

	/**
	 * Gets the styleSheets
	 *
	 * @return Returns a Collection
	 */
	public Collection getStyleSheets() {
		return styleSheets;
	}

	/**
	 * Gets the javascripts
	 *
	 * @return Returns a Collection
	 */
	public Collection getJavascripts() {
		return javascripts;
	}

	/**
	 * Gets the menuPage
	 *
	 * @return Returns a String
	 */
	public String getMenuPage() {
		return menuPage;
	}

	/**
	 * Gets the bodyPage
	 *
	 * @return Returns a String
	 */
	public String getBodyPage() {
		return bodyPage;
	}

	/**
	 * Gets the footerPage
	 *
	 * @return Returns a String
	 */
	public String getFooterPage() {
		return footerPage;
	}

	/**
	 * Gets the headerPage
	 *
	 * @return Returns a String
	 */
	public String getHeaderPage() {
		return headerPage;
	}

	/**
	 * Gets the value for the parameter.
	 *
	 * @param key The key to the parameter.
	 * @return The value of the parameter.
	 */
	public String getParam(String key) {
		return (String)params.get(key);
	}

	/**
	 * Gets the value for the parameter as a list of IDs. The
	 * value of the parameter is delimited by ","
	 *
	 * @param key The key to the parameter.
	 * @return The value of the parameter as a list of IDs
	 */
	public List getParamAsIds(String key) {
		StringTokenizer st = new StringTokenizer(getParam(key), ",");
		List result = new ArrayList();
		while (st.hasMoreTokens()) {
			result.add(Integer.valueOf(st.nextToken().trim()));
		}
		return result;
	}

	/**
	 * Gets the parameters
	 *
	 * @return Returns a Map
	 */
	public Map getParams() {
		return params;
	}

	/**
	 * This methos returns the LayoutPage associated with the page
	 *
	 * @return LayoutPage
	 */
	public LayoutPage getLayoutPageBean() {
		LayoutPage layoutPage = null;
		Content bean = null;
			try {
				bean = ContentCacheManager.getInstance().getContentById(
						this.contentId, ContentTypeManager.instance().LAYOUT_PAGE);

			} catch (Exception e) {
				SystemException se = new SystemException(e, this.getClass().getName(),
						"getLayoutPageBean", "Content id:"+this.contentId);
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			}
	

		if (bean != null) {
			if (bean instanceof LayoutPage) layoutPage = (LayoutPage) bean;
		}

		// This is modified to return at least empty LayoutPage object.
		// Otherwise we won't be able to set the
		// nul object in the jsp. This is helpful during the development. We
		// can take it out when we
		// finished development or have all hte content ready. (IC)
		return layoutPage == null ? (LayoutPage) new com.manulife.pension.content.view.MutableLayoutPage()
				: layoutPage;

	}

	public String toString() {
		return StaticHelperClass.toString(this);
	}
	/**
	 * Gets the isPBA
	 * @return Returns a boolean
	 */
	public boolean isPba() {
		return pba;
	}

	public Object clone(){
       
       try {
       		return super.clone();
       }
       //This should not happen because we have made this class Cloneable.
       //Object.clone checks whether a class 
	   //implements it, and if not, throws a CloneNotSupportedException
	   //similar code in CloneableAutoForm()
       catch (CloneNotSupportedException e) {
       		throw new NestableRuntimeException(e);
       }
       
    }

	/**
	 * @return Returns the tpaHeaderUsed.
	 */
	public boolean isTpaHeaderUsed() {
		return tpaHeaderUsed;
	}
	/**
	 * @return Returns the showSelectContractLink.
	 */
	public boolean isShowSelectContractLink() {
		return showSelectContractLink;
	}

    /**
     * @return the tpaMenu
     */
    public String getTpaMenu() {
        return tpaMenu;
    }

    /**
     * @param tpaMenu the tpaMenu to set
     */
    public void setTpaMenu(String tpaMenu) {
        this.tpaMenu = tpaMenu;
    }

    /**
     * @return the tpaSubmenu
     */
    public String getTpaSubmenu() {
        return tpaSubmenu;
    }

    /**
     * @param tpaSubmenu the tpaSubmenu to set
     */
    public void setTpaSubmenu(String tpaSubmenu) {
        this.tpaSubmenu = tpaSubmenu;
    }

    public int getDefinedBenefitContentId() {
        return definedBenefitContentId;
    }

    public void setDefinedBenefitContentId(int definedBenefitContentId) {
        this.definedBenefitContentId = definedBenefitContentId;
    }

    public String getDefinedBenefitMenu() {
        return definedBenefitMenu;
    }

    public void setDefinedBenefitMenu(String definedBenefitMenu) {
        this.definedBenefitMenu = definedBenefitMenu;
    }

    public String getDefinedBenefitSubmenu() {
        return definedBenefitSubmenu;
    }

    public void setDefinedBenefitSubmenu(String definedBenefitSubmenu) {
        this.definedBenefitSubmenu = definedBenefitSubmenu;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

	/** To store Gifl version 3 content id 
	 * @return the giflSelectContentId
	 */
	public int getGiflSelectContentId() {
		return giflSelectContentId;
	}
}

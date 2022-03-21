package com.manulife.pension.platform.web.navigation;

/**
 * This class is inherited by UserMenuItem class. This class provides the common instance
 * variables needed in a UserMenuItem object.
 * 
 * @author HArlomte
 *
 */
public class BaseMenuItem implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String actionURL;
	private Boolean isEnabled;
	
	private boolean largerTitle;
	
	protected BaseMenuItem() {		
	}
	
	public BaseMenuItem(String id, String title, String actionURL) {
		this.id = id;
		this.title = title;
		this.actionURL = actionURL;
		this.isEnabled = true;
	}
	
    public BaseMenuItem(String id, String title, String actionURL, Boolean isEnabled) {
        this.id = id;
        this.title = title;
        this.actionURL = actionURL;
        this.isEnabled = isEnabled;
    }
	
    public BaseMenuItem(String id, String title, String actionURL, 
    		boolean largerTitle) {
		this.id = id;
		this.title = title;
		this.actionURL = actionURL;
		this.largerTitle = largerTitle;
	}
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getActionURL() {
		return actionURL;
	}
	
	public void setActionURL(String actionURL) {
		this.actionURL = actionURL;
	}

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

	/**
	 * @return the largerTitle
	 */
	public boolean isLargerTitle() {
		return largerTitle;
	}

	/**
	 * @param largerTitle the largerTitle to set
	 */
	public void setLargerTitle(boolean largerTitle) {
		this.largerTitle = largerTitle;
	}


}

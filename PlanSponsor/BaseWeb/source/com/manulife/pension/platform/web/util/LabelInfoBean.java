package com.manulife.pension.platform.web.util;

/**
 * This bean will be used to hold simple information such as id, title, enabled.
 * 
 * This will be used to store Filter, Column Information.
 * 
 * @author harlomte
 * 
 */
public class LabelInfoBean implements java.io.Serializable {

    public LabelInfoBean() {

    }

    public LabelInfoBean(String id, String title, Boolean enabled) {
        this.id = id;
        this.title = title;
        this.enabled = enabled;
    }
    
    private static final long serialVersionUID = -6235065909959804209L;

    private String id;

    private String title;

    private Boolean enabled;
    
    private Boolean displayInPdfAndCsv;
    
    private boolean overRideEnable = false;
    
    private String PDFTitle;
    
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

    public Boolean getEnabled() {
        return enabled && !overRideEnable;
    }
    
    public Boolean getDefaultEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public Boolean getDisplayInPdfAndCsv() {
		return displayInPdfAndCsv;
	}

	public void setDisplayInPdfAndCsv(Boolean displayInPdfAndCsv) {
		this.displayInPdfAndCsv = displayInPdfAndCsv;
	}
	
	public boolean getOverRideEnable() {
		return overRideEnable;
	}

	public void setOverRideEnable(boolean overRideEnable) {
		this.overRideEnable = overRideEnable;
	}
	
	public LabelInfoBean createCopy() {
	    
	    final LabelInfoBean copy = new LabelInfoBean(id, title, enabled);
	    copy.displayInPdfAndCsv = displayInPdfAndCsv;
	    copy.overRideEnable = overRideEnable;
	    copy.PDFTitle = PDFTitle;
	    return copy;
	    
	}
	
	public String getPDFTitle() {
		return PDFTitle;
	}

	public void setPDFTitle(String PDFTitle) {
		this.PDFTitle = PDFTitle;
	}
	
}

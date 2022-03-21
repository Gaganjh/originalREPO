package com.manulife.pension.platform.web.taglib;


/**
 * @author Charles Chan
 */
public class MapTag extends ParamSupportTag {

	private String id;
	
	/**
	 * Constructor.
	 */
	public MapTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.taglib.ParamSupportTag#getParameterMapKey()
	 */
	protected String getParameterMapKey() {
		return id;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
}

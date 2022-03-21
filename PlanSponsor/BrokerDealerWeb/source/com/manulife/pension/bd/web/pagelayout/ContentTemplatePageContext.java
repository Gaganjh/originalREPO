package com.manulife.pension.bd.web.pagelayout;

import java.io.Serializable;

/**
 * The bean to hold content id, menu id, body and body id details.
 * Used for Dynamic content page
 * 
 * @author AAmbrose
 * 
 */
public class ContentTemplatePageContext implements Serializable {

	private static final long serialVersionUID = 1L;

	private int contentId;

	private String menuId;

	private String bodyId;

	private String body;

	public ContentTemplatePageContext(int contentId, String menuId,
			String bodyId, String body) {
		setContentId(contentId);
		setMenuId(menuId);
		setBodyId(bodyId);
		setBody(body);
	}

	/**
	 * @return the int contentId
	 */
	public int getContentId() {
		return contentId;
	}

	/**
	 * @param int
	 *            contentId the contentId to set
	 */
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	/**
	 * @return the String menuId
	 */
	public String getMenuId() {
		return menuId;
	}

	/**
	 * @param String
	 *            menuId the menuId to set
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return String bodyId
	 */
	public String getBodyId() {
		return bodyId;
	}

	/**
	 * @param String
	 *            bodyId the bodyId to set
	 */
	public void setBodyId(String bodyId) {
		this.bodyId = bodyId;
	}

	/**
	 * @return the String body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param String body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

}

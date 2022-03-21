package com.manulife.pension.ps.web.noticemanager;

/**
 * 
 * Order Status page to show order placed from Build your package page
 * @author Tamilarasu k
 *
 */
public class OrderStatusNoticeManagerForm extends UploadSharedNoticeManagerForm {

	private static final long serialVersionUID = 1L;
	private boolean noticeManagerAccessPermissions;
	/**
	 * @return the noticeManagerAccessPermissions
	 */
	public boolean isNoticeManagerAccessPermissions() {
		return noticeManagerAccessPermissions;
	}
	/**
	 * @param noticeManagerAccessPermissions the noticeManagerAccessPermissions to set
	 */
	public void setNoticeManagerAccessPermissions(
			boolean noticeManagerAccessPermissions) {
		this.noticeManagerAccessPermissions = noticeManagerAccessPermissions;
	}
	

}

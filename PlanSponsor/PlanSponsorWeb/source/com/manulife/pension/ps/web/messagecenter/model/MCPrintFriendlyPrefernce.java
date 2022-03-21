package com.manulife.pension.ps.web.messagecenter.model;

import com.manulife.pension.service.message.valueobject.MessageCenterComponent;

/**
 * MCPrintFriendly preference is a decorate class of MCPrefernce. It decorate
 * the underlying MCPreference for specific print friendly behavior. For
 * example, all section is expanded, and all messages is displayed. While the
 * other preferences uses the underlying MCPreference object
 * 
 * This class is readonly. The set/update methods do not change the values.
 * 
 * @author guweigu
 * 
 */
public class MCPrintFriendlyPrefernce extends MCPreference {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MCPreference delegate;

	public MCPrintFriendlyPrefernce(MCPreference delegate) {
		this.delegate = delegate;
	}

	@Override
	public MCSectionPreference getSectionPreference(
			MessageCenterComponent section) {
		return new PrintFriendlySectionPreference(delegate
				.getSectionPreference(section));
	}

	@Override
	public void collapseAllSectionButOne(MessageCenterComponent tab,
			MessageCenterComponent section) {
		return;
	}

	@Override
	public void expandSection(MessageCenterComponent section, boolean expand) {
		return;
	}


	@Override
	public void setSectionSort(MessageCenterComponent section, String key,
			boolean ascending) {
		return;
	}	

	@Override
	public void updateSectionPreference(MessageCenterComponent section,
			MCSectionPreference pref) {
		return;
	}

	private static class PrintFriendlySectionPreference extends
			MCSectionPreference {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private MCSectionPreference delegate;

		public PrintFriendlySectionPreference(MCSectionPreference delegate) {
			this.delegate = delegate;
		}

		@Override
		public String getPrimarySortAttribute() {
			return delegate.getPrimarySortAttribute();
		}

		@Override
		public boolean isAscending() {
			return delegate.isAscending();
		}

		@Override
		public boolean isExpand() {
			return true;
		}

		@Override
		public boolean isShowAll() {
			return true;
		}
	}
}

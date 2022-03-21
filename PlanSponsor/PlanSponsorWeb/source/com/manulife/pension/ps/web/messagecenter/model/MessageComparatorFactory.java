package com.manulife.pension.ps.web.messagecenter.model;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;

/**
 * The Message comparator factory returns different comparator array based on the
 * the PrimarySortingKey (including the attribute name and sorting order)
 * 
 * @author guweigu
 *
 */
public class MessageComparatorFactory {

	private static MessageComparatorFactory factory = new MessageComparatorFactory();

	private Map<PrimarySortKey, MessageComparator> comparators;

	@SuppressWarnings ("unchecked") 
	private MessageComparatorFactory() {
		comparators = new HashMap<PrimarySortKey, MessageComparator>();
		comparators.put(new PrimarySortKey(
				MCConstants.PriorityAttrName, false),
				new MessageComparator(new Comparator[] {
						new PriorityAttrComparator(false),
						new PostedTsAttrComparator(false),
						new ShortTextAttrComparator(true) }));
		comparators.put(new PrimarySortKey(
				MCConstants.PriorityAttrName, true),
				new MessageComparator(new Comparator[] {
						new PriorityAttrComparator(true),
						new PostedTsAttrComparator(false),
						new ShortTextAttrComparator(true) }));
		comparators.put(new PrimarySortKey(
				MCConstants.PostedTsAttrName, false),
				new MessageComparator(new Comparator[] {
						new PostedTsAttrComparator(false),
						new PriorityAttrComparator(false),						
						new ShortTextAttrComparator(true) }));
		comparators.put(new PrimarySortKey(
				MCConstants.PostedTsAttrName, true),
				new MessageComparator(new Comparator[] {
						new PostedTsAttrComparator(true),
						new PriorityAttrComparator(false),						
						new ShortTextAttrComparator(true) }));
		comparators.put(new PrimarySortKey(
				MCConstants.ShortTextAttrName, false),
				new MessageComparator(new Comparator[] {
						new ShortTextAttrComparator(false),
						new PriorityAttrComparator(false),
						new PostedTsAttrComparator(false)
												
						}));
		comparators.put(new PrimarySortKey(
				MCConstants.ShortTextAttrName, true),
				new MessageComparator(new Comparator[] {
						new ShortTextAttrComparator(true),
						new PriorityAttrComparator(false),
						new PostedTsAttrComparator(false)
												
						}));
	}

	public static MessageComparatorFactory getInstance() {
		return factory;
	}
	
	public MessageComparator getComparator(String attr,
			boolean ascending) {
		return comparators.get(new PrimarySortKey(attr, ascending));
	}

	/**
	 * Comparator uses Priority as Primary sorting key, then it is
	 * CreateTs(descending) and ShortText (Ascending)
	 * 
	 * @author guweigu
	 * 
	 */
	static public class MessageComparator implements
			Comparator<RecipientMessageDetail> {
		private Comparator<RecipientMessageDetail>[] comparators;

		public MessageComparator(Comparator<RecipientMessageDetail>[] comparators) {
			this.comparators = comparators;
		}

		public int compare(RecipientMessageDetail m1, RecipientMessageDetail m2) {
			for (Comparator<RecipientMessageDetail> c : comparators) {
				if (c != null) {
					int result = c.compare(m1, m2);
					if (result != 0) {
						return result;
					}
				}
			}
			return 0;
		}
	}
	static public class PriorityAttrComparator implements
			Comparator<RecipientMessageDetail> {
		private boolean ascending;

		public PriorityAttrComparator(boolean ascending) {
			this.ascending = ascending;
		}

		public int compare(RecipientMessageDetail m1, RecipientMessageDetail m2) {
			int result = m1.getPriority().getValue()
					- m2.getPriority().getValue();
			return ascending ? -result : result;
		}
	}

	static public class PostedTsAttrComparator implements
			Comparator<RecipientMessageDetail> {
		private boolean ascending;

		public PostedTsAttrComparator(boolean ascending) {
			this.ascending = ascending;
		}

		public int compare(RecipientMessageDetail m1, RecipientMessageDetail m2) {
			Date d1 = m1.getEffectiveTs();
			Date d2 = m2.getEffectiveTs();
			int result = 0;
			if (d1 != null && d2 != null) {
				result = d1.compareTo(d2);
			} else if (d1 == null) {
				result = -1;
			} else if (d2 == null) {
				result = 1;
			}
			return ascending ? result : -result;
		}
	}

	static public class ShortTextAttrComparator implements
			Comparator<RecipientMessageDetail> {
		private boolean ascending;

		public ShortTextAttrComparator(boolean ascending) {
			this.ascending = ascending;
		}

		public int compare(RecipientMessageDetail m1, RecipientMessageDetail m2) {
			String s1 = m1.getShortText();
			String s2 = m2.getShortText();
			int result = 0;
			if (s1 != null && s2 != null) {
				result = s1.compareTo(s2);
			} else if (s1 == null) {
				result = -1;
			} else if (s2 == null) {
				result = 1;
			}
			return ascending ? result : -result;
		}
	}

	public static class PrimarySortKey extends BaseSerializableCloneableObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String attr;
		private boolean ascending;

		public PrimarySortKey(String attr, boolean ascending) {
			this.attr = attr;
			this.ascending = ascending;
		}

		public String getAttr() {
			return attr;
		}

		public void setAttr(String attr) {
			this.attr = attr;
		}

		public boolean isAscending() {
			return ascending;
		}

		public void setAscending(boolean ascending) {
			this.ascending = ascending;
		}
	}
}

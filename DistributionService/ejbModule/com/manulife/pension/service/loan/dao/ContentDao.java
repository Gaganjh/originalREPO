package com.manulife.pension.service.loan.dao;

import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SelectBeanArrayQueryHandler;
import com.intware.dao.jdbc.SelectSingleOrNoValueQueryHandler;
import com.manulife.pension.content.IrrecoverableException;
import com.manulife.pension.content.util.ContentTextComparator;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.ContentText;

public class ContentDao {

	private static final String CONTENT_TEXT_LARGE_TABLE_NAME = "content_text_large";
	private static final String CONTENT_TEXT_SMALL_TABLE_NAME = "content_text_small";

	/**
	 * Retrieves the USA CMA content from the given set of ContentText objects.
	 * 
	 * @param contentTextSet
	 * @return
	 */
	public Set<ContentText> getUsContentText(Set<ContentText> contentTextSet) {
		Set<ContentText> result = new HashSet<ContentText>();
		for (ContentText text : contentTextSet) {
			if (Location.US.equals(text.getLocation())
					|| Location.USA.equals(text.getLocation())) {
				result.add(text);
			}
		}
		return result;
	}

	/**
	 * Retrieves the New York CMA content from the given set of ContentText
	 * objects. If none is found, it will return the USA CMA content.
	 * 
	 * @param contentTextSet
	 * @return
	 */
	public Set<ContentText> getNyContentText(Set<ContentText> contentTextSet) {
		Set<ContentText> result = new HashSet<ContentText>();
		for (ContentText text : contentTextSet) {
			if (Location.NEW_YORK.equals(text.getLocation())) {
				result.add(text);
			}
		}
		if (result.size() == 0) {
			result = getUsContentText(contentTextSet);
		}
		return result;
	}

	public Set<ContentText> getContentTextByKey(String cmaJndiName,
			String cmaSchemaName, int key, boolean isLive, boolean isEnabled)
			throws DAOException {
		String sql = "SELECT id FROM " + cmaSchemaName
				+ "content WHERE key=? AND is_live=? AND is_enabled=?";
		SelectSingleOrNoValueQueryHandler selectByKeyAndIsEnabledHandler = new SelectSingleOrNoValueQueryHandler(
				cmaJndiName, sql, new int[] { Types.INTEGER, Types.CHAR,
						Types.CHAR }, Integer.class);
		Integer contentId = (Integer) selectByKeyAndIsEnabledHandler
				.select(new Object[] { new Integer(key), new Boolean(isLive),
						new Boolean(isEnabled) });
		if (contentId == null) {
			return new HashSet<ContentText>();
		}
		return getContentTextById(cmaJndiName, cmaSchemaName, contentId);
	}

	/**
	 * Returns all content text for the given content id.
	 * 
	 * @param int
	 *            contentId
	 * 
	 * @return ContentText[]
	 * 
	 * @exception IrrecoverableException
	 *                If any error occurs while querying database.
	 */
	public Set<ContentText> getContentTextById(String cmaJndiName,
			String cmaSchemaName, int contentId) throws DAOException {
		List<ContentText> large = Arrays.asList(getContentText(cmaJndiName,
				cmaSchemaName, CONTENT_TEXT_LARGE_TABLE_NAME, contentId));
		List<ContentText> small = Arrays.asList(getContentText(cmaJndiName,
				cmaSchemaName, CONTENT_TEXT_SMALL_TABLE_NAME, contentId));

		Set<ContentText> mostRecentCollidingItems = new HashSet<ContentText>();
		Set<ContentText> answer = new HashSet<ContentText>();

		TreeSet<ContentText> collidingItems = new TreeSet<ContentText>(
				new ContentTextComparator());
		collidingItems.addAll(large);
		collidingItems.retainAll(small);

		Iterator<ContentText> iterator = collidingItems.iterator();
		for (; iterator.hasNext();) {
			ContentText collidingLarge = (ContentText) iterator.next();
			int indexOfSmall = small.indexOf(collidingLarge);
			ContentText collidingSmall = (ContentText) small
					.remove(indexOfSmall);
			if (ContentTextComparator.compare(collidingLarge, collidingSmall) < 0) {
				mostRecentCollidingItems.add(collidingSmall);
			} else {
				mostRecentCollidingItems.add(collidingLarge);
			}
		}

		answer.addAll(large);
		answer.addAll(small);
		answer.addAll(mostRecentCollidingItems);
		return answer;
	}

	private ContentText[] getContentText(String cmaJndiName,
			String cmaSchemaName, String tableName, Integer contentId)
			throws DAOException {
		String sql = "SELECT id, context, location, text, last_updated_ts FROM "
				+ cmaSchemaName + tableName + " WHERE id = ?";
		SelectBeanArrayQueryHandler getAllByContentIdHandler = new SelectBeanArrayQueryHandler(
				cmaJndiName, sql, new int[] { Types.INTEGER },
				ContentText.class, new String[] { "Id", "Context",
						"LocationForAbbreviation", "Text", "UpdatedAt" });
		ContentText[] contentText = (ContentText[]) getAllByContentIdHandler
				.select(new Object[] { new Integer(contentId) });
		return contentText;
	}

}

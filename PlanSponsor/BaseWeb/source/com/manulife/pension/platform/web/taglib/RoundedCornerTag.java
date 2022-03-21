package com.manulife.pension.platform.web.taglib;

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

/**
 * @author Charles Chan
 */
public class RoundedCornerTag extends TagSupport {

	private String numberOfColumns;
	private String name;
	private String property;
	private String scope;
	private String oddRowColor;
	private String evenRowColor;
	private String emptyRowColor;
	private String columnDividerClass = "datadivider";
	private static final String lineBreak = System
			.getProperty("line.separator");

	/**
	 * Constructor.
	 */
	public RoundedCornerTag() {
		super();
	}

	public int doStartTag() throws JspException {

		int columns = Integer.valueOf(numberOfColumns).intValue();
		Collection collection = null;

		if (name != null) {
			collection = (Collection) TagUtils.getInstance().lookup(pageContext, name,
					property, scope);
		}

		int collectionSize = collection == null ? 0 : collection.size();

		String rowColor = collectionSize % 2 == 0 ? evenRowColor : oddRowColor;

		if (collectionSize == 0) {
			rowColor = emptyRowColor;
		}

		StringBuffer sb = new StringBuffer("<tr class=\"").append(rowColor)
				.append("border\">").append(lineBreak);

		sb.append("<td class=\"databorder\">").append(
				"<img src=\"/assets/unmanaged/images/s.gif\"").append(
				" width=\"1\" height=\"4\"></td>").append(lineBreak);

		sb.append("<td");
		if (collectionSize == 0) {
			sb.append(" colspan=\"").append(columns - 3).append("\"");
		}

		sb.append("><img src=\"/assets/unmanaged/images/s.gif\"").append(
				" width=\"1\" height=\"1\"></td>").append(lineBreak);

		if (collectionSize > 0) {
			for (int i = 0; i < columns - 4; i += 2) {
				sb.append("<td class=\"").append(columnDividerClass).append(
						"\">").append(
						"<img src=\"/assets/unmanaged/images/s.gif\"").append(
						" width=\"1\" height=\"1\"></td>").append(lineBreak);
				sb.append("<td><img src=\"/assets/unmanaged/images/s.gif\"")
						.append(" width=\"1\" height=\"1\"></td>").append(
								lineBreak);
			}
		}

		sb.append("<td colspan=\"2\" rowspan=\"2\" class=\"").append(rowColor)
				.append("Box\">").append("<img ").append(
						"src=\"/assets/unmanaged/images/box_lr_corner.gif\"")
				.append(" width=\"5\" height=\"5\"></td>").append(lineBreak);
		sb.append("</tr>").append("<tr>").append(
				"<td class=\"databorder\" colspan=\"").append(columns - 2)
				.append("\"><img src=\"/assets/unmanaged/images/s.gif\"")
				.append(" width=\"1\" height=\"1\"></td>").append(lineBreak);

		TagUtils.getInstance().write(pageContext, sb.toString());

		return SKIP_BODY;
	}

	/**
	 * @return Returns the evenNumberColor.
	 */
	public String getEvenRowColor() {
		return evenRowColor;
	}

	/**
	 * @param evenNumberColor
	 *            The evenNumberColor to set.
	 */
	public void setEvenRowColor(String evenNumberColor) {
		this.evenRowColor = evenNumberColor;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the numberOfColumns.
	 */
	public String getNumberOfColumns() {
		return numberOfColumns;
	}

	/**
	 * @param numberOfColumns
	 *            The numberOfColumns to set.
	 */
	public void setNumberOfColumns(String numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	/**
	 * @return Returns the oddNumberColor.
	 */
	public String getOddRowColor() {
		return oddRowColor;
	}

	/**
	 * @param oddNumberColor
	 *            The oddNumberColor to set.
	 */
	public void setOddRowColor(String oldNumberColor) {
		this.oddRowColor = oldNumberColor;
	}

	/**
	 * @return Returns the emptyRowColor.
	 */
	public String getEmptyRowColor() {
		return emptyRowColor;
	}

	/**
	 * @return Returns the columnDividerClass.
	 */
	public String getColumnDividerClass() {
		return columnDividerClass;
	}

	/**
	 * @param columnDividerClass
	 *            The columnDividerClass to set.
	 */
	public void setColumnDividerClass(String columnDividerClass) {
		this.columnDividerClass = columnDividerClass;
	}

	/**
	 * @param emptyRowColor
	 *            The emptyRowColor to set.
	 */
	public void setEmptyRowColor(String emptyRowColor) {
		this.emptyRowColor = emptyRowColor;
	}

	/**
	 * @return Returns the property.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return Returns the scope.
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope
	 *            The scope to set.
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
}
package com.manulife.pension.platform.web.fap.tabs.util;

import org.apache.commons.lang3.StringUtils;

/**
 * ColumnsInfoBean holds the information for a specific column 
 * header for the F&P tabs. The column headers are render based on these
 * values only. Each column is differentiated by the 'key' attribute.
 * This 'key' attribute should match the attribute name of the value object.
 * 
 * @author ayyalsa
 *
 */
public class ColumnsInfoBean implements java.io.Serializable {
	
	/**
	 * a default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String secondaryKey;
	private String keyForCsv;

    private String name;
	private String format;
	private String secondaryKeyFormat;

	private boolean nowrap;
	private String columnClass;
	private String sortClass;
	private boolean sort;
	private int sortOrder;
	
	private String colSpan;
	private String rowSpan;
	private String colSpanForPdf;
	
	private boolean toggleOption;
	private ColumnsToggleInfoBean[] toggleInfoBeans; 
	
	private String valueRenderingType = "TEXT";
	private int dataRowSpan;
	
	/**
	 * TRUE, only for the RORs, Standard Deviations and Fund Metrics columns  
	 */
	private boolean hypoLogicApplicable;
	
	private boolean showHeader = true;

	/**
	 * Constructor
	 *  
	 * @param key
	 * @param name
	 * @param format
	 * @param nowrap
	 * @param columnClass
	 * @param sort
	 * @param sortOrder
	 * @param rowSpan
	 */
	public ColumnsInfoBean(String key, String name, String format, 
			boolean nowrap, String columnClass, boolean sort, int sortOrder, 
			String rowSpan) {
		this.key = key;
		this.name = name;
		this.format = format;
		this.nowrap = nowrap;
		this.columnClass = columnClass;
		this.sortOrder = sortOrder;
		this.rowSpan = rowSpan;
		this.sort = sort;
	}
	
	/**
	 * Constructor
	 *  
	 * @param key
	 * @param name
	 * @param format
	 * @param nowrap
	 * @param columnClass
	 * @param sort
	 * @param sortOrder
	 * @param rowSpan
	 */
	public ColumnsInfoBean(String key, String secondaryKey, String name, String format, 
			boolean nowrap, String columnClass, String valueRenderingType, boolean sort, int sortOrder, 
			String rowSpan) {
		this.key = key;
		this.name = name;
		this.format = format;
		this.nowrap = nowrap;
		this.columnClass = columnClass;
		this.sortOrder = sortOrder;
		this.rowSpan = rowSpan;
		this.sort = sort;
		this.secondaryKey = secondaryKey;
		this.valueRenderingType = valueRenderingType;
	}
	
	/**
	 * Constructor
	 *  
	 * @param key
	 * @param name
	 * @param format
	 * @param nowrap
	 * @param columnClass
	 * @param sort
	 * @param sortOrder
	 * @param rowSpan
	 */
	public ColumnsInfoBean(String key, String secondaryKey, String name, String format, String secondaryKeyFormat,
			boolean nowrap, String columnClass, boolean sort, int sortOrder, 
			String rowSpan) {
		this.key = key;
		this.name = name;
		this.format = format;
		this.nowrap = nowrap;
		this.columnClass = columnClass;
		this.sortOrder = sortOrder;
		this.rowSpan = rowSpan;
		this.sort = sort;
		this.secondaryKey = secondaryKey;
		this.secondaryKeyFormat = secondaryKeyFormat;
	}
	
	/**
	 * Constructor
	 * 
	 * @param key
	 * @param secondaryKey
	 * @param name
	 * @param format
	 * @param nowrap
	 * @param columnClass
	 * @param sort
	 * @param sortOrder
	 * @param showHeader
	 * @param valueRenderingType
	 */
	public ColumnsInfoBean(String key, String secondaryKey, String name, String format, 
			boolean nowrap, String columnClass, boolean sort, int sortOrder, 
			boolean showHeader, String valueRenderingType) {
		this.key = key;
		this.name = name;
		this.format = format;
		this.nowrap = nowrap;
		this.columnClass = columnClass;
		this.sortOrder = sortOrder;
		this.sort = sort;
		this.showHeader = showHeader;
		this.secondaryKey = secondaryKey;
		this.valueRenderingType = valueRenderingType;
	}
	
	/**
	 * Constructor
	 * 
	 * Used only by FundCheck tab for the rendering  type
	 * 
	 * @param key
	 * @param name
	 * @param format
	 * @param nowrap
	 * @param columnClass
	 * @param sort
	 * @param sortOrder
	 * @param rowSpan
	 */
	public ColumnsInfoBean(String key, String name, String format, 
			boolean nowrap, String columnClass, boolean sort, int sortOrder, 
			String rowSpan, String valueRenderingType) {
		this(key, name, format, nowrap, columnClass, sort, sortOrder, rowSpan);
		this.valueRenderingType = valueRenderingType;
	}
	
	/**
     * Constructor
     * 
     * Used by Morningstar only for the CSV key
     * 
     * @param key
     * @param name
     * @param format
     * @param nowrap
     * @param columnClass
     * @param sort
     * @param sortOrder
     * @param rowSpan
     */
    public ColumnsInfoBean(String key, String keyForCsv, String name, String format,
            boolean nowrap, String columnClass, boolean sort, int sortOrder, String rowSpan,
            String valueRenderingType) {
        this(key, name, format, nowrap, columnClass, sort, sortOrder, rowSpan);
        this.valueRenderingType = valueRenderingType;
        this.keyForCsv = keyForCsv;
    }
    
    /**
     * Constructor 
     * 
     * @param key
     * @param name
     * @param format
     * @param nowrap
     * @param columnClass
     * @param sort
     * @param sortOrder
     * @param rowSpan
     * @param dataRowSpan
     */
	public ColumnsInfoBean(String key, String name, String format, 
			boolean nowrap, String columnClass, boolean sort, int sortOrder, 
			String rowSpan, int dataRowSpan) {
		this(key, name, format, nowrap, columnClass, sort, sortOrder, rowSpan);
		this.dataRowSpan = dataRowSpan;
	}
	
	/**
	 * Constructor
	 * 
	 * @param key
	 * @param name
	 * @param format
	 * @param nowrap
	 * @param columnClass
	 * @param sort
	 * @param sortOrder
	 * @param rowSpan
	 * @param dataRowSpan
	 * @param valueRenderingType
	 */
	public ColumnsInfoBean(String key, String name, String format, 
			boolean nowrap, String columnClass, boolean sort, int sortOrder, 
			String rowSpan, int dataRowSpan, String valueRenderingType) {
		this(key, name, format, nowrap, columnClass, sort, sortOrder, rowSpan);
		this.dataRowSpan = dataRowSpan;
		this.valueRenderingType = valueRenderingType;
	}

	/**
     * Constructor
     * 
     * Used in FapTabUtility static block
     * 
     * @param key
     * @param name
     * @param format
     * @param nowrap
     * @param columnClass
     * @param sortClass
     * @param rowSpan
     */
    public ColumnsInfoBean(String key, String name, String format, 
            boolean nowrap, String columnClass, String sortClass, String rowSpan) {
        this.key = key;
        this.name = name;
        this.format = format;
        this.nowrap = nowrap;
        this.columnClass = columnClass;
        this.sortClass = sortClass;
        this.rowSpan = rowSpan;
    }
	
	/**
	 * Constructor
	 * 
	 * Used in FapTabUtility static block
	 * 
	 * @param key
	 * @param name
	 * @param format
	 * @param nowrap
	 * @param columnClass
	 * @param sortClass
	 * @param rowSpan
	 */
	public ColumnsInfoBean(String key, String name, String format, 
			boolean nowrap, String columnClass, String sortClass, String rowSpan, boolean hypoLogicApplicable) {
		this.key = key;
		this.name = name;
		this.format = format;
		this.nowrap = nowrap;
		this.columnClass = columnClass;
		this.sortClass = sortClass;
		this.rowSpan = rowSpan;
		this.hypoLogicApplicable = hypoLogicApplicable;
	}
	
	/**
	 * Constructor for Level1 columns
	 * 
	 * @param name
	 * @param columnClass
	 * @param colSpan
	 * @param rowSpan
	 */
	public ColumnsInfoBean(String name, String columnClass, String colSpan, String rowSpan) {
		this.name = name;
		this.columnClass = columnClass;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
	}
	
	/**
	 * Constructor for Level1 columns
	 * 
	 * @param name
	 * @param columnClass
	 * @param colSpan
	 * @param rowSpan
	 */
	public ColumnsInfoBean(String name, String columnClass, String colSpan, String rowSpan, String colSpanForPdf) {
		this.name = name;
		this.columnClass = columnClass;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		this.colSpanForPdf = colSpanForPdf;
	}

	
	/**
	 * Constructor
	 *  
	 * Used for Hypothetical logical columns only
	 *  
	 * @param key
	 * @param name
	 * @param format
	 * @param nowrap
	 * @param columnClass
	 * @param sort
	 * @param sortOrder
	 * @param rowSpan
	 */
	public ColumnsInfoBean(String key, String name, String format, 
			boolean nowrap, String columnClass, boolean sort, int sortOrder, 
			String rowSpan, boolean hypoLogicApplicable) {
		this.key = key;
		this.name = name;
		this.format = format;
		this.nowrap = nowrap;
		this.columnClass = columnClass;
		this.sortOrder = sortOrder;
		this.rowSpan = rowSpan;
		this.sort = sort;
		this.hypoLogicApplicable = hypoLogicApplicable;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the nowrap
	 */
	public boolean isNowrap() {
		return nowrap;
	}

	/**
	 * @param nowrap the nowrap to set
	 */
	public void setNowrap(boolean nowrap) {
		this.nowrap = nowrap;
	}

	/**
	 * @return the columnClass
	 */
	public String getColumnClass() {
		return columnClass;
	}

	/**
	 * @param columnClass the columnClass to set
	 */
	public void setColumnClass(String columnClass) {
		this.columnClass = columnClass;
	}

	/**
	 * @return the sortClass
	 */
	public String getSortClass() {
		return sortClass;
	}

	/**
	 * @param sortClass the sortClass to set
	 */
	public void setSortClass(String sortClass) {
		this.sortClass = sortClass;
	}

	/**
	 * @return the sort
	 */
	public boolean isSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(boolean sort) {
		this.sort = sort;
	}

	/**
	 * @return the sortOrder
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the colSpan
	 */
	public String getColSpan() {
		return colSpan;
	}

	/**
	 * @param colSpan the colSpan to set
	 */
	public void setColSpan(String colSpan) {
		this.colSpan = colSpan;
	}

	/**
	 * @return the rowSpan
	 */
	public String getRowSpan() {
		return rowSpan;
	}

	/**
	 * @param rowSpan the rowSpan to set
	 */
	public void setRowSpan(String rowSpan) {
		this.rowSpan = rowSpan;
	}

	/**
	 * @return the toggleOption
	 */
	public boolean isToggleOption() {
		return toggleOption;
	}

	/**
	 * @param toggleOption the toggleOption to set
	 */
	public void setToggleOption(boolean toggleOption) {
		this.toggleOption = toggleOption;
	}

	/**
	 * @return the toggleInfoBeans
	 */
	public ColumnsToggleInfoBean[] getToggleInfoBeans() {
		return toggleInfoBeans;
	}

	/**
	 * @param toggleInfoBeans the toggleInfoBeans to set
	 */
	public void setToggleInfoBeans(ColumnsToggleInfoBean[] toggleInfoBeans) {
		this.toggleInfoBeans = toggleInfoBeans;
	}

	/**
	 * @return the dataRowSpan
	 */
	public int getDataRowSpan() {
		return dataRowSpan;
	}

	/**
	 * @param dataRowSpan the dataRowSpan to set
	 */
	public void setDataRowSpan(int dataRowSpan) {
		this.dataRowSpan = dataRowSpan;
	}

	/**
	 * @return the valueRenderingType
	 */
	public String getValueRenderingType() {
		return valueRenderingType;
	}

	/**
	 * @param valueRenderingType the valueRenderingType to set
	 */
	public void setValueRenderingType(String valueRenderingType) {
		this.valueRenderingType = valueRenderingType;
	}
	
	/**
     * @return the keyForCsv
     */
    public String getKeyForCsv() {
        return keyForCsv;
    }

    /**
     * @param keyForCsv the keyForCsv to set
     */
    public void setKeyForCsv(String keyForCsv) {
        this.keyForCsv = keyForCsv;
    }

	/**
	 * @return the hypoLogicApplicable
	 */
	public boolean isHypoLogicApplicable() {
		return hypoLogicApplicable;
	}

	/**
	 * @param hypoLogicApplicable the hypoLogicApplicable to set
	 */
	public void setHypoLogicApplicable(boolean hypoLogicApplicable) {
		this.hypoLogicApplicable = hypoLogicApplicable;
	}
	
	public String getColSpanForPdf() {
		if(StringUtils.isEmpty(colSpanForPdf)) {
			return colSpan;
		}
		return colSpanForPdf;
	}

	public void setColSpanForPdf(String colSpanForPdf) {
		this.colSpanForPdf = colSpanForPdf;
	}
	
	/**
	 * 
	 * @return boolean
	 */
	public boolean isShowHeader() {
		return showHeader;
	}
	
	public String getSecondaryKey() {
		return secondaryKey;
	}

	public void setSecondaryKey(String secondaryKey) {
		this.secondaryKey = secondaryKey;
	}
	
	public String getSecondaryKeyFormat() {
		if(this.secondaryKeyFormat != null) {
			return secondaryKeyFormat;
		}
		return format;
	}

	public void setSecondaryKeyFormat(String secondaryKeyFormat) {
		this.secondaryKeyFormat = secondaryKeyFormat;
	}
}

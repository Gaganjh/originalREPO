package com.manulife.pension.ps.service.report.fandp.valueobject;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

public class SavedUserDataFactory {
    private static final String USER_SAVED_LIST_NAME = "SAVED_DATA_NAME";

    private static final String USER_SAVED_LIST_ID = "SAVED_DATA_SEQ_NO";

    private static final String USER_SAVED_LIST_TYPE = "SAVED_DATA_TYPE_CODE";

    private static final String USER_SAVED_LIST_DATA = "SAVED_DATA_TEXT";

    private static final String TYPE_SAVED_CUSTOM_QUERY = "CQ";

    private static final String TYPE_SAVED_FUND_LIST = "FL";

    private static SavedUserDataFactory instance = null;

    public static SavedUserDataFactory getInstance() {
        if (instance == null) {
            instance = new SavedUserDataFactory();
        }
        return instance;
    }

    public UserSavedData getUserSavedDataObject(ResultSet resultSet)
            throws SQLException {
        UserSavedData list = null;
        String listName = "";
        Integer listId = new Integer(-1);
        String listType = "";
        String delimitedData = "";

        listName = resultSet.getString(USER_SAVED_LIST_NAME);
        listId = resultSet.getInt(USER_SAVED_LIST_ID);
        listType = resultSet.getString(USER_SAVED_LIST_TYPE);
        delimitedData = resultSet.getString(USER_SAVED_LIST_DATA);

        if (StringUtils.equals(listType, TYPE_SAVED_CUSTOM_QUERY)) {
            list = new CustomQuerySavedData();
        } else if (StringUtils.equals(listType, TYPE_SAVED_FUND_LIST)) {
        	list = new FundListSavedData();
        }

        list.setId(listId);
        list.setListType(listType);
        list.setName(listName);
        list.setDelimtedData(delimitedData);

        return list;
    }

}

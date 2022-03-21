package com.manulife.pension.ps.web.census.util;

import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * Mapper class that maps the old sourceCode to new code that is used by caller.
 * 
 * @author chancha
 */
public abstract class BaseEmployeeVestingParamSourceCodeMapper extends
        BaseSerializableCloneableObject {

    /**
     * Default serial version UID
     */
    private static final long serialVersionUID = 1L;

    private final Map<String, String> sourceCodeMap = new HashMap<String, String>();

    protected abstract void populateMap();

    protected Map<String, String> getSourceCodeMap() {
        return sourceCodeMap;
    }

    public BaseEmployeeVestingParamSourceCodeMapper() {
        populateMap();

        // Put common source codes
        sourceCodeMap.put("PE", "WS");
        sourceCodeMap.put("PC", "WS");
        sourceCodeMap.put("PS", "WS");

        sourceCodeMap.put("EL", "AD");
        sourceCodeMap.put("LP", "AD");
    }

    public String getConvertedValue(String sourceName) {
        return sourceCodeMap.get(sourceName);
    }
}

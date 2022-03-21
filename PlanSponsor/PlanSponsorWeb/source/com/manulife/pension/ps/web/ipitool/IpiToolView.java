package com.manulife.pension.ps.web.ipitool;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IpiToolView
extends Serializable {
    
    String getForwardUrl();
    List<SectionView> getFieldAttributeValue();
    List<BasicAssetChargeLine> getCurrentBacScale();
    List<BasicAssetChargeLine> getInputBacScale();
    List<String> getCurrentDiScale();
    List<String> getInputDiScale();
    List<Integer> getErrorKeys();
    Set<String> getErrorFields();
    Map<String, Map<String, String>> getErrorCode();
    
}
package com.manulife.pension.ireports.report.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.map.LinkedMap;

import com.manulife.pension.service.fund.standardreports.valueobject.AssetCategory;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetCategoryGroup;

/**
 * Groups Asset Categories into their Asset Category Groups
 * @author Greg Mckhool
 *
 */
public class AssetCategoriesCollator {

	private Collection assetCategories;
	private Map assetCategoryGroups;

	public AssetCategoriesCollator(Collection assetCategories, Map assetCategoryGroups) {
		this.assetCategories = assetCategories;
		this.assetCategoryGroups = assetCategoryGroups; //assumed to be sorted in asc sort number order
	}

	public Map collate() {
		if (assetCategories == null || assetCategoryGroups == null) {
			return Collections.EMPTY_MAP;
		}
        
      
        BeanComparator comparator = new BeanComparator("assetCategoryGroupOrderNo");
        List values =new ArrayList(assetCategoryGroups.values());
        Collections.sort(values, comparator);
        
		Map assetCategoriesByGroup = new LinkedMap();
		for (Iterator iter = values.iterator(); iter.hasNext();) {
			AssetCategoryGroup assetCategoryGroup = (AssetCategoryGroup) iter.next();
			assetCategoriesByGroup.put(assetCategoryGroup.getAssetCategoryGroupId(), new ArrayList());
		}	
		
		for (Iterator iter = assetCategories.iterator(); iter.hasNext();) {
			AssetCategory assetCategory = (AssetCategory) iter.next();
			String assetCategoryGroupId = assetCategory.getAssetCategoryGroupId();

			List assetCategoriesForGroup = (List)assetCategoriesByGroup.get(assetCategoryGroupId);
			
			if(assetCategoriesForGroup != null) {
				assetCategoriesForGroup.add(assetCategory);
			}
		}
		
		for (Iterator iter = assetCategoriesByGroup.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			List assetCategoriesForGroup = (List) entry.getValue();
			if (!assetCategoriesForGroup.isEmpty()) {
				Collections.sort(assetCategoriesForGroup, new AssetCategoryComparator());
			}
		}	
		
		return assetCategoriesByGroup;
	}
	
	private static class AssetCategoryComparator implements Comparator {
		public int compare (Object o1, Object o2){
			AssetCategory assetCategory1 = (AssetCategory)o1;
			AssetCategory assetCategory2 = (AssetCategory)o2;
			int order1 = assetCategory1.getAssetCategoryOrderNo();
			int order2 = assetCategory2.getAssetCategoryOrderNo();
			return order1 - order2;
		}
	}
}

package com.manulife.pension.ps.web.profiles;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Charles Chan
 */
public class FormChanges implements Serializable {
	
	private static boolean roleChanged; 

 	public static boolean isRoleChanged() {
		return roleChanged;
	}

	public static void setRoleChanged(boolean roleChanged) {
		FormChanges.roleChanged = roleChanged;
	}

	private Map modifications = new HashMap();

    private Map additions = new HashMap();

    private Map removals = new HashMap();

    /**
     * Constructor.
     */
    public FormChanges() {
        super();
    }

    public boolean isChanged(String fieldName) {
        return modifications.containsKey(fieldName)
                || additions.containsKey(fieldName)
                || removals.containsKey(fieldName);
    }

    public boolean isChanged() {
        return modifications.size() > 0 || additions.size() > 0
                || removals.size() > 0;           //|| isRoleChanged(); -- Commented for CL # 129254
    }

    /**
     * @return Returns the additions.
     */
    public Map getAdditions() {
        return additions;
    }

    /**
     * @param additions
     *            The additions to set.
     */
    public void setAdditions(Map additions) {
        this.additions = additions;
    }

    /**
     * @return Returns the changes.
     */
    public Map getModifications() {
        return modifications;
    }

    /**
     * @param changes
     *            The changes to set.
     */
    public void setModifications(Map changes) {
        this.modifications = changes;
    }

    /**
     * @return Returns the removals.
     */
    public Map getRemovals() {
        return removals;
    }

    /**
     * @param removals
     *            The removals to set.
     */
    public void setRemovals(Map removals) {
        this.removals = removals;
    }

    private String getMapAsString(Map map) {
        StringBuffer sb = new StringBuffer();
        Set sortedEntries = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                Map.Entry entry1 = (Map.Entry) o1;
                Map.Entry entry2 = (Map.Entry) o2;
                return ((String) entry1.getKey()).compareTo((String) entry2.getKey());
            }
        });

        sortedEntries.addAll(map.entrySet());

        for (Iterator it = sortedEntries.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sb.append("[").append(entry.getKey()).append("=").append(
                    entry.getValue()).append("]");
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        if (additions.size() > 0) {
            sb.append(" Additions: ").append(getMapAsString(additions));
        }
        if (removals.size() > 0) {
            sb.append(" Removals: ").append(getMapAsString(removals));
        }
        if (modifications.size() > 0) {
            sb.append(" Modifications: ").append(getMapAsString(modifications));
        }
        return sb.toString();
    }

    /**
     * Compute the difference between the two given maps. Changes are stored in
     * the FormChanges object. The FormChanges object remembers additions,
     * removals, and modifications.
     * 
     * @return A FormChanges object that captures all the changes made in the
     *         form.
     */
    public void computeChanges(Map newFormMap, Map oldFormMap) {
        /*
         * Iterates over the old form and see if anything is removed or changed.
         */
        for (Iterator it = oldFormMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();

            Object key = entry.getKey();
            Object oldValue = entry.getValue();

            /*
             * Skip comparison if the value is null.
             */
            if (oldValue != null) {
                Object newValue = newFormMap.get(key);
                if (newValue == null) {
                    getRemovals().put(key, oldValue);
                } else if (!newValue.equals(oldValue)) {
                    getModifications().put(key, newValue);
                }
            }
        }

        /*
         * Iterates the new forms and check if anything is added.
         */
        for (Iterator it = newFormMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object newValue = entry.getValue();

            if (newValue != null) {
                Object oldValue = oldFormMap.get(key);
                if (oldValue == null) {
                    getAdditions().put(key, newValue);
                }
            }
        }
    }
}


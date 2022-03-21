/*
 * Created on Sep 20, 2004
 *
 */
package com.manulife.pension.ps.service.submission.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.util.log.LogUtility;

/**
 * @author parkand
 *
 */
public class StatusGroupHelper {
	// singleton
	private static StatusGroupHelper instance = null;

	// static initializer for singleton
	static {
		try {
			instance = new StatusGroupHelper();
		} catch (SystemException e) {
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);
		}
	}

	private InputStream xmlFileStream = getClass().getClassLoader().getResourceAsStream("./statusGroups.xml");

	// string constants used in XML descriptor
	private static final String GROUP_ELEMENT_NAME = "group";
	private static final String GROUP_CODE_ATTRIBUTE = "code";
	private static final String GROUP_DESC_ATTRIBUTE = "desc"; // not used
	private static final String GROUP_STATES_ATTRIBUTE = "states";
	private static final String APOSTROPHE="'";
	// collection
	private Map groups;
	private SortedSet groupDescriptions;
	private Map statusToGroupMap;

	public class StatusGroup implements Comparable {
		private String code;
		private String desc;
		StatusGroup(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
		public int compareTo(Object obj) {
			return desc.compareTo(((StatusGroup)obj).getDesc());
		}
	}

	private Logger logger = null;

	private StatusGroupHelper() throws SystemException {
		logger = Logger.getLogger(this.getClass());
		DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(xmlFileStream));
            Document document = parser.getDocument();

            loadGroups(document);

        } catch (IOException e) {
        	e.printStackTrace();
  			throw new SystemException(e, this.getClass().getName(), "constructor" , "IOException occurred.");
        } catch (SAXException e) {
        	e.printStackTrace();
  			throw new SystemException(e, this.getClass().getName(), "constructor" , "SAXException occurred.");
        }
	}

	public static StatusGroupHelper getInstance() {
		return instance;
	}


	private void loadGroups(Document document) {
        NodeList list = document.getElementsByTagName(GROUP_ELEMENT_NAME);
        int length = list.getLength();
        groups = new HashMap(length+1);
        groupDescriptions = new TreeSet();
        statusToGroupMap = new HashMap(length+1);
        for (int i=0; i < length; i++)
        {
        	Element group = (Element)list.item(i);
        	String code = group.getAttribute(GROUP_CODE_ATTRIBUTE);
        	String desc = group.getAttribute(GROUP_DESC_ATTRIBUTE);
        	String states = group.getAttribute(GROUP_STATES_ATTRIBUTE);
       		StringTokenizer st = new StringTokenizer(states,",");
       		ArrayList stateList = new ArrayList(st.countTokens()+1);
        	while (st.hasMoreTokens()) {
        		String stateId = st.nextToken();
            	stateList.add(stateId);
            	statusToGroupMap.put(stateId, code);
        	}
    		groups.put(code,stateList);
    		StatusGroup sg = new StatusGroup(code,desc);
    		groupDescriptions.add(sg);
        }
	}
	//This method is specifically used to get the input string for submission history.
	public String getAllStatusesByValue(String statusval) {
		String temp = getKeysFromValue(groups,statusval);
		return temp.substring(0, temp.length()-1);
		
	}
	//This method is specifically used to get the values for selected 
	//submission status in the advanced search.
	public String getKeysFromValue(Map<?, ?> hm, Object value) {
		List<Object> list = new ArrayList<Object>();
		String mapValues = null;
		StringBuffer stringBuff = new StringBuffer();
		for (Object object : hm.keySet()) {
			if (object.equals(value)) {
				ArrayList allValues = (ArrayList) hm.get(object);
				Iterator iterator = allValues.iterator();
				while (iterator.hasNext()) {
					stringBuff.append(APOSTROPHE);
					stringBuff.append(iterator.next());
					stringBuff.append(APOSTROPHE);
					stringBuff.append(",");
				}
			}
		}
		return stringBuff.toString();
	}

	public Map getAllStatusGroups() {
		return groups;
	}

	public SortedSet getAllStatusGroupDescriptions() {
		return groupDescriptions;
	}

	public Collection getStatusesByGroup(String group) {
		return (Collection)groups.get(group);
	}

	public String getGroupByStatus(String status) {
		return (String)statusToGroupMap.get(status);
	}
}

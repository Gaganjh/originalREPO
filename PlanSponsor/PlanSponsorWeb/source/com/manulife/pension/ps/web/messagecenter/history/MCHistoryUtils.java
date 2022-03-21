package com.manulife.pension.ps.web.messagecenter.history;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.model.MessageTemplateRepository;
import com.manulife.pension.ps.web.messagecenter.personalization.DisplayableMessageTemplate;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacade;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.message.report.valueobject.MessageHistoryDetails;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentImpl;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent.Type;
import com.manulife.pension.service.security.role.ExternalClientUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;

public class MCHistoryUtils {
	
	public enum MessageStatus {
		ALL("ALL", "All"),
		ESCALATED("ESCALATED", "Escalated"),
		EXPIRED("EXPIRED", "Expired"),
		REPLACED("REPLACED", "Replaced"),
		OBSOLETED("OBSOLETED", "Obsoleted"),
		COMPLETEDONLINE("COMPONLINE", "Completed Online"),
		DECLAREDCOMPLETE("DECCOMP", "Declared Complete"),
		REMOVEDFROMVIEW("REMFROMVIEW", "Removed"),
		REMOVEDBYSTILLACTIVE("REMSTILLACTIVE", "Removed But Still Active");
		
	    private static final Map<String,MessageStatus> lookup 
     	= new HashMap<String,MessageStatus>();

	    static {
	    	for(MessageStatus s : EnumSet.allOf(MessageStatus.class))
	    		lookup.put(s.getValue(), s);
	    }
		
		private String value;
		private String text;
	
		private MessageStatus(String value, String text) {
			this.value = value;
			this.text = text;
		}
		
		public String getValue() {
			return value;
		}
		
		public static MessageStatus get(String value) {
			return lookup.get(value);
		}

		public String getText() {
			
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
	/**
	 * List of tabs of the message center, the value is the tab's id, the label
	 * is the tab's name
	 */
	private static List<LabelValueBean> tabs;

	/**
	 * The map of section list. Key of the map is the tab's id The value is the
	 * a list of LabelValueBean that corresponds to the section under the tab
	 */
	private static Map<String, List<LabelValueBean>> sectionMap;

	/**
	 * The map of types key to the tabs
	 */
	private static Map<String, List<LabelValueBean>> typesByTab;

	/**
	 * The map of the types key to the sections
	 */
	private static Map<String, List<LabelValueBean>> typesBySection;

	/**
	 * An empty selection
	 */
	private static final LabelValueBean EmptySelection = new LabelValueBean("All",
			"");

	private static final LabelValueBeanComparator LabelComparator = new LabelValueBeanComparator();

	/**
	 * Get the List of the tabs as LabelValueBean value is the tab's id label is
	 * the tab's name
	 * 
	 * @param servlet
	 * @return
	 * @throws SystemException
	 */
	static public List<LabelValueBean> getTabs(ServletContext servlet)
			throws SystemException {
		initTabsSectionMap(servlet);
		return tabs;
	}

	/**
	 * Initialize the tabs and sectionMap and cache it
	 * 
	 * @param servlet
	 * @throws SystemException
	 */
	static synchronized private void initTabsSectionMap(ServletContext servlet)
			throws SystemException {
		if (tabs == null) {
			MessageCenterComponent top = MCUtils.getMessageCenterTree(servlet);
			tabs = new ArrayList<LabelValueBean>(top.getChildrenSize() + 1);
			tabs.add(EmptySelection);
			sectionMap = new HashMap<String, List<LabelValueBean>>();
			List<LabelValueBean> fullSectionsList = new ArrayList<LabelValueBean>();
			fullSectionsList.add(EmptySelection);
			sectionMap.put("", fullSectionsList);
			for (MessageCenterComponent tab : top.getChildren()) {
				String value = tab.getId().getValue().toString();
				tabs.add(new LabelValueBean(tab.getName(), value));
				List<LabelValueBean> sectionsList = new ArrayList<LabelValueBean>(
						tab.getChildrenSize() + 1);
				sectionsList.add(EmptySelection);
				sectionMap.put(value, sectionsList);
				for (MessageCenterComponent section : tab.getChildren()) {
					LabelValueBean lb = new LabelValueBean(section.getName(),
							section.getId().getValue().toString());
					fullSectionsList.add(lb);
					sectionsList.add(lb);
				}
			}
			sort(tabs);
			sort(sectionMap);
		}
	}

	/**
	 * Get the list of LabelValueBeans for sections under a tab
	 * 
	 * @param servlet
	 * @param tab
	 *            The string of tab's id.
	 * @return
	 * @throws SystemException
	 */
	static public List<LabelValueBean> getSections(ServletContext servlet,
			String tab) throws SystemException {
		initTabsSectionMap(servlet);
		return sectionMap.get(tab);
	}

	/**
	 * Get the typs according to selected tab and/or selected section
	 * 
	 * @param servlet
	 * @param tab
	 * @param section
	 * @return
	 * @throws SystemException
	 */
	static public List<LabelValueBean> getTypes(HttpServlet servlet,
			String tab, String section) throws SystemException {
		List<LabelValueBean> returnTypes = new ArrayList<LabelValueBean>();
		return returnTypes;
	}

	private static String tabsAsJavaScript = null;
	private static String sectionMapAsJavaScript = null;
	private static String typesByTabAsJavaScript = null;
	private static String typesBySectionAsJavaScript = null;

	/**
	 * Get the tabs LabelValueBean as a javascript array
	 * 
	 * @param servlet
	 * @return
	 * @throws SystemException
	 */
//	 replaced by getTabsAndSectionMapAsJavaScript
	/* static public String getTabsAsJavaScript(HttpServlet servlet)
			throws SystemException {
		// initialize
		if (tabsAsJavaScript == null) {
			initTabsSectionMap(servlet);
			StringBuffer buf = new StringBuffer("var tabs = new Array(");
			for (LabelValueBean lb : tabs) {
				addOptionToArray(buf, lb);
			}
			buf.setLength(buf.length() - 1);
			buf.append(");");
			tabsAsJavaScript = buf.toString();
		}
		return tabsAsJavaScript;
	} */
	
	/**
	 * Get the section map as a two-dimensional javascript array
	 * 
	 * @param servlet
	 * @return
	 * @throws SystemException
	 */
	// replaced by getTabsAndSectionMapAsJavaScript
	/* static public String getSectionMapAsJavaScript(HttpServlet servlet)
			throws SystemException {
		if (sectionMapAsJavaScript == null) {
			initTabsSectionMap(servlet);
			StringBuffer buf = new StringBuffer("var sections=");
			addMapToArray(buf, sectionMap);
			sectionMapAsJavaScript = buf.toString();
		}
		return sectionMapAsJavaScript;
	} */

	/**
	 * Get the tabs and sections as a javascript arrays
	 * 
	 * @param servlet
	 * @return
	 * @throws SystemException
	 */
	static public String getTabsAndSectionMapAsJavaScript(ServletContext servlet, HttpServletRequest request)
	throws SystemException {
		
		initTabsSectionMap(servlet);
		Map <String, List<LabelValueBean>>  accessibleSections = filterForAccessibleSections(servlet, request);
		List<LabelValueBean> accessibleTabs = filterForAccessibleTabs(request,accessibleSections);
		sort(accessibleTabs);
		StringBuffer buf = new StringBuffer("var tabs = new Array(");
		for (LabelValueBean lb : accessibleTabs) {
			addOptionToArray(buf, lb);
		}
		buf.setLength(buf.length() - 1);
		buf.append(");");
		buf.append("\n var sections=");
		addMapToArray(buf, accessibleSections);
			
		return buf.toString();
	}
	
	static Map<String, List<LabelValueBean>> filterTypesByAccessibleTemplates(ServletContext servlet,
			HttpServletRequest request, Map<String, List<LabelValueBean>> inputMap) throws SystemException {
		Map<String, List<LabelValueBean>> filteredList = new HashMap<String, List<LabelValueBean>>();

		if ( ! SessionHelper.getUserProfile(request).getRole().isInternalUser() ) {
			//need to filter the types.
			MessageServiceFacade facade = MessageServiceFacadeFactory.getInstance(servlet);
			Set<Integer> accessibleTemplates = facade.getActiveMessageTemplatesForUser(SessionHelper.getUserProfile(request));
			for ( String key : inputMap.keySet()) {
				for ( LabelValueBean bean : inputMap.get(key)) {
					if ( StringUtils.isEmpty(bean.getValue())) {
						if ( filteredList.get(key) == null ) {
							filteredList.put(key, new ArrayList<LabelValueBean>());
						}
						filteredList.get(key).add(bean);
						continue;
					}
					Integer id = new Integer(bean.getValue());
					if ( accessibleTemplates.contains(id)) {
						if ( filteredList.get(key) == null ) {
							filteredList.put(key, new ArrayList<LabelValueBean>());
						}
						filteredList.get(key).add(bean);
					}
				}
			}
			
		} else {
			filteredList = inputMap;
		}
		return filteredList;
	}
	
	static Map<String, List<LabelValueBean>> filterForAccessibleSections(ServletContext servlet,
			HttpServletRequest request) throws SystemException {
			Map<String, List<LabelValueBean>> filteredSectionList = new HashMap<String, List<LabelValueBean>>();

		if (! SessionHelper.getUserProfile(request).getRole().isInternalUser() ) {
			
			MessageCenterComponent top = MCUtils.getMessageCenterTree(servlet);
			MessageTemplateRepository templateRepository = MCUtils.getMessageTemplateRepository(servlet, top.getId());
			MessageServiceFacade facade = MessageServiceFacadeFactory.getInstance(servlet);
			Set<Integer> accessibleTemplates = facade.getActiveMessageTemplatesForUser(SessionHelper.getUserProfile(request));
			
			for ( String key : sectionMap.keySet()) {
				for ( LabelValueBean bean : sectionMap.get(key)) {
					if ( StringUtils.isEmpty(bean.getValue())) {
						if ( filteredSectionList.get(key) == null ) {
							filteredSectionList.put(key, new ArrayList<LabelValueBean>());
						}
						filteredSectionList.get(key).add(bean);
						continue;
					}
					Integer id = new Integer(bean.getValue());
					boolean addSection = false;
					DisplayableMessageTemplate[] templatesForSection = templateRepository.getMessageTemplates(new MessageCenterComponentImpl(new MessageCenterComponentId(Type.SECTION, id)));
					for (DisplayableMessageTemplate t : templatesForSection) {
						if (accessibleTemplates.contains(t.getId())) {
							addSection = true;
						}
					}
					if(addSection) {
						if ( filteredSectionList.get(key) == null ) {
							filteredSectionList.put(key, new ArrayList<LabelValueBean>());
						}
						filteredSectionList.get(key).add(bean);
					}
				}
				if ( filteredSectionList.get(key) != null  && filteredSectionList.get(key).size() == 1 && StringUtils.isNotBlank(key))
					filteredSectionList.remove(key);
			}
			
		} else {
			filteredSectionList = sectionMap;
		}
		return filteredSectionList;
	}

	static List<LabelValueBean> filterForAccessibleTabs(HttpServletRequest request,Map<String, List<LabelValueBean>> accesibleSectionMap) {
		List<LabelValueBean> filteredTabList = new ArrayList<LabelValueBean>();
		
		if (! SessionHelper.getUserProfile(request).getRole().isInternalUser() ) {
			
			for ( String key : accesibleSectionMap.keySet()) {
				for(LabelValueBean bean : tabs){
					if(bean.getValue() != null && key != null && bean.getValue().equals(key))
						filteredTabList.add(bean);
				}
			}
		}else {
			filteredTabList = tabs;
		}
		return filteredTabList;
	}
	/**
	 * Get the types javascript array indexed by tab's list order
	 * 
	 * @param servlet
	 * @return
	 * @throws SystemException
	 */
	static public String getTypesByTabAsJavaScript(ServletContext servlet, HttpServletRequest request) throws SystemException {
		
		initTypes(servlet);
		StringBuffer buf = new StringBuffer("var typesByTab=");
		addMapToArray(buf, filterTypesByAccessibleTemplates(servlet, request, typesByTab));
		typesByTabAsJavaScript = buf.toString();
		return typesByTabAsJavaScript;
	}

	static public String getTypesBySectionAsJavaScript(ServletContext servlet, HttpServletRequest request)
			throws SystemException {
		initTypes(servlet);
		StringBuffer buf = new StringBuffer("var typesBySection=");
		addMapToArray(buf, filterTypesByAccessibleTemplates(servlet, request, typesBySection));
		typesBySectionAsJavaScript = buf.toString();
		return typesBySectionAsJavaScript;
	}

	static private void initTypes(ServletContext servlet) throws SystemException {
		if (typesByTab == null) {
			MessageCenterComponent top = MCUtils.getMessageCenterTree(servlet);
			MessageTemplateRepository templateRepository = MCUtils
					.getMessageTemplateRepository(servlet, top.getId());
			List<LabelValueBean> fullTypes = new ArrayList<LabelValueBean>();
			fullTypes.add(EmptySelection);
			typesByTab = new HashMap<String, List<LabelValueBean>>();
			typesBySection = new HashMap<String, List<LabelValueBean>>();
			typesByTab.put(EmptySelection.getValue(), fullTypes);
			for (MessageCenterComponent tab : top.getChildren()) {
				List<LabelValueBean> tabTypes = new ArrayList<LabelValueBean>();
				typesByTab.put(tab.getId().getValue().toString(), tabTypes);
				tabTypes.add(EmptySelection);
				for (MessageCenterComponent section : tab.getChildren()) {
					DisplayableMessageTemplate[] templates = templateRepository
							.getMessageTemplates(section);
					List<LabelValueBean> sectionTypes = new ArrayList<LabelValueBean>();
					sectionTypes.add(EmptySelection);
					typesBySection.put(section.getId().getValue().toString(),
							sectionTypes);
					for (DisplayableMessageTemplate template : templates) {
						LabelValueBean typeOption = new LabelValueBean(template
								.getShortText(), Integer.toString(template
								.getId()));
						sectionTypes.add(typeOption);
						tabTypes.add(typeOption);
						fullTypes.add(typeOption);
					}
				}
			}
			sort(typesByTab);
			sort(typesBySection);
		}
	}

	static private void addMapToArray(StringBuffer buf,
			Map<String, List<LabelValueBean>> map) {
		buf.append("new Array(");
		for (String key : map.keySet()) {
			buf.append("new optionMap(");
			buf.append("'");
			buf.append(key);
			buf.append("', new Array(");
			List<LabelValueBean> types = map.get(key);
			for (LabelValueBean lb : types) {
				addOptionToArray(buf, lb);
			}
			buf.setLength(buf.length() - 1);
			buf.append(")),");
		}
		buf.setLength(buf.length() - 1);
		buf.append(");");
	}

	static private void addOptionToArray(StringBuffer buf, LabelValueBean lb) {
		buf.append("new option('");
		buf.append(lb.getValue());
		buf.append("','");
		buf.append(StringEscapeUtils.escapeJavaScript(lb.getLabel()));
		buf.append("'),");
	}

	private static void sort(List<LabelValueBean> list) {
		Collections.sort(list, LabelComparator);
	}

	private static void sort(Map<String, List<LabelValueBean>> map) {
		for (List<LabelValueBean> list : map.values()) {
			sort(list);
		}
	}

	static private class LabelValueBeanComparator implements
			Comparator<LabelValueBean> {
		public int compare(LabelValueBean o1, LabelValueBean o2) {
			Collator collator = Collator.getInstance();
        	if(o1.getLabel() == o2.getLabel()){
        		return 0;
    		}
        	if(o1.getLabel().equals("All")){
        		return -1;
    		}
        	if(o2.getLabel().equals("All")){
        		return 1;
    		}
            return collator.compare(o1.getLabel(),o2.getLabel());
		}
	}

	static public String getMessageStatusText(HttpServletRequest request,
			MessageHistoryDetails detail) {
		UserProfile user = SessionHelper.getUserProfile(request);
		StringBuffer buf = new StringBuffer("Message id: ");
		buf.append(detail.getMessageId());

		buf.append("<br>Last modified: ");
		buf.append(new SimpleDateFormat(MCConstants.LastModifiedFormat).format(detail
				.getStatusTimestamp()));
		buf.append("<br>");
		buf.append("By: ");
		String role = detail.getStatusUserRole();
		if (!user.isInternalUser()
				&& !(StringUtils.equals(role, ExternalClientUser.ID) || StringUtils
						.equals(role, ThirdPartyAdministrator.ID))) {
			buf.append("Message Center");
		} else {
			buf.append(detail.getStatusUserLastName());
			buf.append(", ");
			buf.append(detail.getStatusUserFirstName());
		}
		return buf.toString();
	}
}

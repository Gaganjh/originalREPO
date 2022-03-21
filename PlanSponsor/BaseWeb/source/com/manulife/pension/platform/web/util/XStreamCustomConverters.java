package com.manulife.pension.platform.web.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.util.render.RenderConstants;
import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimestampConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XStreamCustomConverters {

	private static XStreamCustomConverters converters = new XStreamCustomConverters();
	public static XStreamCustomConverters getInstance() {
		return converters;
	}
/*	private XStream xStream;
	
	public static MapConverter MAP_CONVERTER = converters.new MapConverter();
	
	
	
	public XStreamCustomConverters getInstance(XStream xStream) {
		this.xStream = xStream;
		return converters;
	}
	*/
	 /**
   	 * This class is an handler for all Date objects.
   	 */
   	public class DateConverterX extends DateConverter {
   		protected String toString(Object obj) {
   			Date date = (Date) obj;
   			return DateFormatUtils.format(date, RenderConstants.MEDIUM_MDY_SLASHED);
       }
   	}
   	
   	/**
   	 * This class is an handler for all Timestamp objects.
   	 */
   	public class TimestampConverterX extends SqlTimestampConverter {
   		protected String toString(Object obj) {
   			Timestamp ts = (Timestamp) obj;
   			SimpleDateFormat dateFormat = new SimpleDateFormat(RenderConstants.LONG_MDY_SLASHED);		
   			return dateFormat.format(ts);
   		}
   	}
   	
   	/**
   	 * This class is an handler for view page Timestamp objects.
   	 */
   	public class ViewPageTimestampConverterX extends SqlTimestampConverter {
   		protected String toString(Object obj) {
   			Timestamp ts = (Timestamp) obj;
   			SimpleDateFormat dateFormat = new SimpleDateFormat(RenderConstants.LONG_TIMESTAMP_MDY_SLASHED);		
   			return dateFormat.format(ts);
   		}
   	}
   	
	/**
   	 * This class is an handler for all BigDecimal objects.
   	 */
   	public class BigDecimalConverterX extends BigDecimalConverter {
   		
   		public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
   			writer.toString();
   			writer.setValue(toString(source));
   		}
   	 
   		protected String toString(Object obj) {
   			BigDecimal bd = (BigDecimal) obj;
   			
   			NumberFormat currencyNumberFormat = NumberFormat.getCurrencyInstance();
   			DecimalFormat df = (DecimalFormat)currencyNumberFormat;
   			DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
   			dfs.setCurrencySymbol(StringUtils.EMPTY);
   			df.setDecimalFormatSymbols(dfs);
   			currencyNumberFormat.setMaximumFractionDigits(2);
   		    currencyNumberFormat.setMinimumFractionDigits(2);
   			return currencyNumberFormat.format(bd);
   		}
   	}
   	
	 /**
   	 * This class is an handler for all Map objects.
   	 */
   	public class MapConverter implements Converter {
   		
   		@SuppressWarnings("rawtypes")
   		public boolean canConvert(Class type) {
   			return type.equals(HashMap.class) || type.equals(Hashtable.class)
   					|| type.equals(java.util.LinkedHashMap.class);
   		}

   		@SuppressWarnings("rawtypes")
   		public void marshal(Object value, HierarchicalStreamWriter writer,
   				MarshallingContext context) {

   			AbstractMap map = (AbstractMap) value;
   			for (Object obj : map.entrySet()) {
   			
   				Entry entry = (Entry) obj;
   				
   				Object valueObj = entry.getValue();
   				if (!isValue(valueObj)) {
   					
   					if (isCollectionObject(valueObj)){
   						writer.startNode(getValidKey(entry.getKey(), false));
   					} else {
   						writer.startNode("item");
   						writer.addAttribute("key", getValidKey(entry.getKey(), false));
   					}
   					context.convertAnother(entry.getValue() == null ? StringUtils.EMPTY : entry.getValue());
   					writer.endNode();

   				} else {
   					writer.startNode("item");
   					writer.addAttribute("key", getValidKey(entry.getKey(), false));
   					writer.addAttribute("value", StringEscapeUtils.escapeXml(entry.getValue().toString()));
   					writer.endNode();
   				}
   			}
   		}

   		public Object unmarshal(HierarchicalStreamReader arg0,
   				UnmarshallingContext arg1) {
   			return null;
   		}
   		
   		/**
   		 * used to check the whether the class is a primitive data.
   		 */
   		private boolean isValue(Object obj) {

   			if (obj instanceof Integer || obj instanceof Float
   					|| obj instanceof String || obj instanceof Character) {
   				return true;
   			}
   			return false;

   		}
   		
   		/**
   		 * Checks for whether the value is an collection object or not
   		 * @param obj
   		 * @return
   		 */
   		private boolean isCollectionObject(Object obj) {
   			if (obj == null) return false;
   			Class type = obj.getClass();
   			return type.equals(ArrayList.class)
   	                || type.equals(HashSet.class)
   	                || type.equals(LinkedList.class)
   	                || type.equals(Vector.class);
   		}
   	}
   	
   	/**
	 * This class is an handler for all list objects.
	 */
	public class CollectionConverterX extends CollectionConverter {

		public CollectionConverterX(ClassMapper classMapper) {
			super(classMapper, "class");
		}

		protected void writeItem(Object paramObject, MarshallingContext paramMarshallingContext, 
				HierarchicalStreamWriter paramHierarchicalStreamWriter) {
			
		    if (paramObject == null) {
		    	paramHierarchicalStreamWriter.startNode(this.classMapper.lookupName(ClassMapper.Null.class));
		    	paramHierarchicalStreamWriter.endNode();
		    } else {
		    	if (paramObject instanceof DeCodeVO) {
		    		DeCodeVO deCodeVo = (DeCodeVO)paramObject;
		    		paramHierarchicalStreamWriter.startNode("item");
		    		paramHierarchicalStreamWriter.addAttribute("key", getValidKey(deCodeVo.getCode(), false));
		    		paramHierarchicalStreamWriter.addAttribute("value", StringEscapeUtils.escapeXml(deCodeVo.getDescription()));
		    		paramHierarchicalStreamWriter.endNode();
		    	} else if (paramObject instanceof LabelValueBean) {
		    		LabelValueBean labelValueBean = (LabelValueBean)paramObject;
		    		paramHierarchicalStreamWriter.startNode("item");
		    		paramHierarchicalStreamWriter.addAttribute("key", getValidKey(labelValueBean.getValue(), false));
		    		paramHierarchicalStreamWriter.addAttribute("value", StringEscapeUtils.escapeXml(labelValueBean.getLabel()));
		    		paramHierarchicalStreamWriter.endNode();
		    	} else {
			    	paramHierarchicalStreamWriter.startNode(this.classMapper.lookupName(paramObject.getClass()));
				    paramMarshallingContext.convertAnother(paramObject);
				    paramHierarchicalStreamWriter.endNode();
		    	}
		    }
		}
	}
	
	/*public class CustomReflectionConverter extends ReflectionConverter {

		public CustomReflectionConverter(ClassMapper classMapper,
		        ReflectionProvider reflectionProvider, DefaultConverterLookup defaultConverterLookup) {
		    super(classMapper, "class", "defined-in", reflectionProvider, defaultConverterLookup);
		}

		@Override
		public void marshal(Object obj, HierarchicalStreamWriter writer,
		        MarshallingContext context) {
		    super.marshal(obj,writer,context);

		    writer.startNode("node4");
		    writer.setValue("test");
		    writer.endNode();
		}

		@Override
		public Object unmarshal(HierarchicalStreamReader reader,
		        UnmarshallingContext context) {
		    return super.unmarshal(reader,context);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean canConvert(Class clazz) {
		    return clazz.equals(LoanParameter.class);
		}
	}
	
*/	
	private String getValidKey(Object obj, boolean addPrefix) {

			String key = obj.toString();
			if (obj instanceof PermissionType){
				key = obj.getClass().getName();
			} 
			
			/*else {
	   			boolean isNumberic = (obj instanceof Integer || obj instanceof Float ||
	   					obj instanceof Double || obj instanceof BigDecimal ||
	   					obj instanceof BigInteger || obj instanceof Short ||
	   					obj instanceof Long);
	   				
	   			if (isNumberic || StringUtils.isNumeric(StringUtils.substring(StringUtils.trim(key), 0, 1))) {
	   				return "_" + key;
	   			}
			}*/
			return addPrefix ? "_" + key : key;

	}
}

package com.manulife.pension.ps.web.onlineloans;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.TreeMarshaller;
import com.thoughtworks.xstream.core.util.ObjectIdDictionary;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
/**
 * Custom marshaller used by XStream to convert value object to XML
 * 
 * @author thangjo
 */
public class CustomTreeMarshaller extends TreeMarshaller {

	private static final Logger logger = Logger.getLogger(CustomTreeMarshaller.class);
	private ObjectIdDictionary parentObjects = new ObjectIdDictionary();

    public CustomTreeMarshaller(HierarchicalStreamWriter writer, 
    		ConverterLookup converterLookup, ClassMapper classMapper) {
    	super(writer, converterLookup, classMapper);
    }

    public void convertAnother(Object item) {
        if (this.parentObjects.containsId(item)) {
        	// Ignore and return if there is a circular reference
        	if (logger.isDebugEnabled()) {
    			logger.debug("Circular Reference found for Object:: " 
    					+ item.getClass().getName()
    					+ ". Skipping this object from XML conversion");
    		}
        	return;
        }
        parentObjects.associateId(item, "");
        Converter converter = converterLookup.lookupConverterForType(item.getClass());
        converter.marshal(item, writer, this);
        parentObjects.removeId(item);
    }
}

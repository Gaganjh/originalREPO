package com.manulife.pension.ps.web.onlineloans;

import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.core.DefaultConverterLookup;
import com.thoughtworks.xstream.core.TreeMarshallingStrategy;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
/**
 * Custom implementation of TreeMarshallingStrategy
 * 
 * @author thangjo
 */
public class CustomTreeMarshallingStrategy extends TreeMarshallingStrategy {

    public void marshal(HierarchicalStreamWriter writer, Object obj, 
    		DefaultConverterLookup converterLookup, ClassMapper classMapper) {
        new CustomTreeMarshaller(
                writer, converterLookup, classMapper).start(obj);
    }

}

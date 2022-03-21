package com.manulife.pension.platform.web.cache;

import java.util.Map;

import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.content.view.MutableFootnote;

public interface FootnoteCache {
    public Map<String, Map<String, MutableFootnote>> getFootnotes();
    
    /**
     * Gets the CMA content for each of the symbols matching the specifed company id.
     * Returns a array of Footnote object sorted by the symbols
     * 
     * @param symbols
     * @param companyId
     * @return returns a array of Footnote object sorted by the symbols
     */
    public Footnote[] sortFootnotes(String[] symbols, String companyId);
    
}

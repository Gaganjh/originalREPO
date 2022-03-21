
package com.manulife.pension.bd.web.fundEvaluator.util;

import org.apache.log4j.Logger;

import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.MutableFootnote;
import com.manulife.pension.exception.SystemException;
/**
 * Retrieve Footnotes from the CMA service by location
 * @author PWakode
 */
public class FootnoteLookupHelper {
    
    private static final Logger logger = Logger.getLogger(FootnoteLookupHelper.class);
	
    public static MutableFootnote[] retrieveFootnotesFromCMAService(boolean isNY) throws SystemException{
        MutableFootnote[] footnotes = null;

        try{
            footnotes = (MutableFootnote[]) BrowseServiceDelegate.getInstance().findContent(ContentTypeManager.instance().FOOTNOTE,
                    (isNY) ? Location.NEW_YORK : Location.USA);
        }
        /*try{
            footnotes = buildMockFootnotes();
        }*/
        catch(Throwable e){
            logger.debug("Problem in retrieving footnotes from CMAService");
            throw new SystemException(e, "FootnoteLookupHelper.retrieveFootnotesFromCMAService"  +
            "Problem in method retrieveFootnotesFromCMAService() of FootnoteLookupHelper");
        }    
        return footnotes;
    }

	@SuppressWarnings("unused")
    private static MutableFootnote[] buildMockFootnotes() {
        String[][] mockFootnotes = {
				{ "4",	"This sub-account was introduced July 31, 1999.", "190" },
				{ "6",	"This sub-account was introduced October 3, 1997." , "190" },
				{ "14",	"The underlying fund concentrates its investments in a sector of the market. A portfolio of this type may be riskier or more volatile in price than one that invests in more market sectors." , "270" },
				{ "#",	"These sub-accounts were introduced November 27, 1995.", "20" },
		};

		MutableFootnote[] returnValue = new MutableFootnote[mockFootnotes.length];

		for (int i = 0; i < mockFootnotes.length; i++) {
			MutableFootnote mockFootnote = new MutableFootnote();
			mockFootnote.setSymbol(mockFootnotes[i][0]);
			mockFootnote.setText(mockFootnotes[i][1]);
			int orderNumber = Integer.parseInt(mockFootnotes[i][2]);
			mockFootnote.setOrderNumber(orderNumber);
			returnValue[i] = mockFootnote;
		}

		return returnValue;
	}
}

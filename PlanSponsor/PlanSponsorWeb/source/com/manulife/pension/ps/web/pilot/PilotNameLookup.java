package com.manulife.pension.ps.web.pilot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * pilot code view bean for dynamic column
 * @author Steven Wang
 *
 */
public class PilotNameLookup implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //contain pilot code
    private List<String> pilotNames = new ArrayList<String>();

    public List<String> getPilotNames() {
        return pilotNames;
    }

    public void setPilotNames(List<String> pilotNames) {
        this.pilotNames = pilotNames;
    } 
    
    public int getPilotNamesSize(){
        return pilotNames.size();
    }

}

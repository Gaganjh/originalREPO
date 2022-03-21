package com.manulife.pension.ps.web.pilot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ps.web.controller.PsForm;

public class PilotContractForm extends PsForm {

    private static final long serialVersionUID = -2259065910344016638L;

    private String contractNumber;
    
    private String goodContractNumber=null;

    private String contractName;
    

    private Map<String, Boolean> pilotTable = new HashMap<String, Boolean>();

    public PilotContractForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Map<String, Boolean> getPilotTable() {
        return pilotTable;
    }

    public void setPilotTable(Map<String, Boolean> pilotTable) {
        this.pilotTable = pilotTable;
    }

    public void addPilotTableItem(String key, Boolean value) {
        this.pilotTable.put(key, value);

    }

    public Collection<String> getPilotNames() {

        return this.pilotTable.keySet();
    }

    public int getPilotTableSize() {

        return this.pilotTable.size();
    }
    
    public void resetPilotTable(){
        this.pilotTable = null;
        
    }

    public String getGoodContractNumber() {
        return goodContractNumber;
    }

    public void setGoodContractNumber(String goodContractNumber) {
        this.goodContractNumber = goodContractNumber;
    }



}

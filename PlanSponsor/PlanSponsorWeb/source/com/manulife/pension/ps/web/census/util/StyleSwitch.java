package com.manulife.pension.ps.web.census.util;

/**
 * A utility class to support switch the row style
 * in a table, where the row is not generated in
 * a loop.   The getNext method will return a 
 * switched row style, while increasing the 
 * row index in the mean time.
 * 
 * @author guweigu
 *
 */
public class StyleSwitch {
    private int start;
    private String prefix;
    private int index;
    public StyleSwitch() {
        start = 1;
        prefix="dataCell";
        index = start;
    }

    public String getNext() {
        String next = prefix + (index % 2 + 1);
        index++;
        return next;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setStart(int start) {
        this.start = start;
        index = start;
    }
}

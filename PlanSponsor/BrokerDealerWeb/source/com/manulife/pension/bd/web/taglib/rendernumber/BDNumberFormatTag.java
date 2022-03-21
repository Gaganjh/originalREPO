package com.manulife.pension.bd.web.taglib.rendernumber;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.manulife.util.render.NumberRender;
import com.manulife.util.web.taglib.render.FormatTag;

public class BDNumberFormatTag extends FormatTag {

    private static final long serialVersionUID = 1L;
    
    private String pattern;
    private String type;
    private int scale = 2; // deafult
    private int intDigits = 1; // default
    private boolean sign = true;
    private int roundingMode = BigDecimal.ROUND_HALF_DOWN; // default

    public void setPattern(String pattern) {
        this.pattern = pattern.trim();
    }

    public void setType(String type) {
        this.type = type.trim();
    }

    public void setScale(int scale) {

        this.scale = scale;
    }

    public void setSign(boolean sign) {

        this.sign = sign;
    }

    public void setIntDigits(int intDigits) {
        this.intDigits = intDigits;
    }

    public void setRoundingMode(int roundingMode) {
        this.roundingMode = roundingMode;
    }

    @Override
    protected String doFormat(Object oValue) {
        String value = NumberRender.format(oValue, defaultValue, pattern, type, scale,
                roundingMode,
                intDigits, sign);
        value = StringUtils.trimToEmpty(value);
        StringBuffer newValue = new StringBuffer();
        int signPosStart = value.indexOf("(");
        int signPosEnd = value.indexOf(")");
        if (signPosStart == 0 && signPosEnd == value.length() - 1) {
            value = value.replace("(", "-");
            value = value.substring(0, value.length() - 1);
            newValue.append(value);
        } else {
            newValue.append(value);
        }
        return newValue.toString();
    }

}

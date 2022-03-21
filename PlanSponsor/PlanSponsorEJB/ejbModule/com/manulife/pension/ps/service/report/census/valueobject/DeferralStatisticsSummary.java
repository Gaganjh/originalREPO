package com.manulife.pension.ps.service.report.census.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

public class DeferralStatisticsSummary implements Serializable {

    private static final long serialVersionUID = 7914024879062262042L;
    
    private static final BigDecimal bd50 = new BigDecimal(50);
    private static final BigDecimal bd100 = new BigDecimal(100);
    private static final BigDecimal bd200 = new BigDecimal(200);
    private static final BigDecimal bd300 = new BigDecimal(300);
    private static final BigDecimal bd400 = new BigDecimal(400);
    private static final BigDecimal bd500 = new BigDecimal(500);
    private static final BigDecimal bd600 = new BigDecimal(600);
    private static final BigDecimal bd700 = new BigDecimal(700);
    private static final BigDecimal bd800 = new BigDecimal(800);
    private static final BigDecimal bd2 = new BigDecimal(2);
    private static final BigDecimal bd4 = new BigDecimal(4);
    private static final BigDecimal bd6 = new BigDecimal(6);
    private static final BigDecimal bd8 = new BigDecimal(8);
    private static final BigDecimal bd10 = new BigDecimal(10);
    private static final BigDecimal bd12 = new BigDecimal(12);
    
    private int defaultSettings;
    private int activelyManaged;
    private int enrolledButNotSignedUp;
    private int notEnrolled;
    
    int unknown = 0;
    
    int traditionalLessThan50$ = 0;
    int traditionalLessThan100$ = 0;
    int traditionalLessThan200$ = 0;
    int traditionalLessThan300$ = 0;
    int traditionalLessThan400$ = 0;
    int traditionalLessThan500$ = 0;
    int traditionalLessThan600$ = 0;
    int traditionalLessThan700$ = 0;
    int traditionalLessThan800$ = 0;
    int traditionalMoreThan800$ = 0;
    
    int traditionalLessThan2Pct = 0;
    int traditionalLessThan4Pct = 0;
    int traditionalLessThan6Pct = 0;
    int traditionalLessThan8Pct = 0;
    int traditionalLessThan10Pct = 0;
    int traditionalLessThan12Pct = 0;
    int traditionalMoreThan12Pct = 0;
    
    int rothLessThan50$ = 0;
    int rothLessThan100$ = 0;
    int rothLessThan200$ = 0;
    int rothLessThan300$ = 0;
    int rothLessThan400$ = 0;
    int rothLessThan500$ = 0;
    int rothLessThan600$ = 0;
    int rothLessThan700$ = 0;
    int rothLessThan800$ = 0;
    int rothMoreThan800$ = 0;
    
    int rothLessThan2Pct = 0;
    int rothLessThan4Pct = 0;
    int rothLessThan6Pct = 0;
    int rothLessThan8Pct = 0;
    int rothLessThan10Pct = 0;
    int rothLessThan12Pct = 0;
    int rothMoreThan12Pct = 0;

    public DeferralStatisticsSummary() {
        this.activelyManaged = 0;
        this.defaultSettings = 0;
        this.enrolledButNotSignedUp = 0;
        this.notEnrolled = 0;                
    }

    public int getActivelyManaged() {
        return activelyManaged;
    }

    public void setActivelyManaged(int activelyManaged) {
        this.activelyManaged = activelyManaged;
    }

    public int getDefaultSettings() {
        return defaultSettings;
    }

    public void setDefaultSettings(int defaultSettings) {
        this.defaultSettings = defaultSettings;
    }

    public int getNotEnrolled() {
        return notEnrolled;
    }

    public void setNotEnrolled(int notEnrolled) {
        this.notEnrolled = notEnrolled;
    }
    
    public int getEnrolledButNotSignedUp() {
        return enrolledButNotSignedUp;
    }

    public void setEnrolledButNotSignedUp(int enrolled) {
        this.enrolledButNotSignedUp = enrolled;
    }

    public int getRothLessThan100$() {
        return rothLessThan100$;
    }

    public void setRothLessThan100$(int rothLessThan100$) {
        this.rothLessThan100$ = rothLessThan100$;
    }

    public int getRothLessThan10Pct() {
        return rothLessThan10Pct;
    }

    public void setRothLessThan10Pct(int rothLessThan10Pct) {
        this.rothLessThan10Pct = rothLessThan10Pct;
    }

    public int getRothLessThan12Pct() {
        return rothLessThan12Pct;
    }

    public void setRothLessThan12Pct(int rothLessThan12Pct) {
        this.rothLessThan12Pct = rothLessThan12Pct;
    }

    public int getRothLessThan200$() {
        return rothLessThan200$;
    }

    public void setRothLessThan200$(int rothLessThan200$) {
        this.rothLessThan200$ = rothLessThan200$;
    }

    public int getRothLessThan2Pct() {
        return rothLessThan2Pct;
    }

    public void setRothLessThan2Pct(int rothLessThan2Pct) {
        this.rothLessThan2Pct = rothLessThan2Pct;
    }

    public int getRothLessThan300$() {
        return rothLessThan300$;
    }

    public void setRothLessThan300$(int rothLessThan300$) {
        this.rothLessThan300$ = rothLessThan300$;
    }

    public int getRothLessThan400$() {
        return rothLessThan400$;
    }

    public void setRothLessThan400$(int rothLessThan400$) {
        this.rothLessThan400$ = rothLessThan400$;
    }

    public int getRothLessThan4Pct() {
        return rothLessThan4Pct;
    }

    public void setRothLessThan4Pct(int rothLessThan4Pct) {
        this.rothLessThan4Pct = rothLessThan4Pct;
    }

    public int getRothLessThan50$() {
        return rothLessThan50$;
    }

    public void setRothLessThan50$(int rothLessThan50$) {
        this.rothLessThan50$ = rothLessThan50$;
    }

    public int getRothLessThan500$() {
        return rothLessThan500$;
    }

    public void setRothLessThan500$(int rothLessThan500$) {
        this.rothLessThan500$ = rothLessThan500$;
    }

    public int getRothLessThan600$() {
        return rothLessThan600$;
    }

    public void setRothLessThan600$(int rothLessThan600$) {
        this.rothLessThan600$ = rothLessThan600$;
    }

    public int getRothLessThan6Pct() {
        return rothLessThan6Pct;
    }

    public void setRothLessThan6Pct(int rothLessThan6Pct) {
        this.rothLessThan6Pct = rothLessThan6Pct;
    }

    public int getRothLessThan700$() {
        return rothLessThan700$;
    }

    public void setRothLessThan700$(int rothLessThan700$) {
        this.rothLessThan700$ = rothLessThan700$;
    }

    public int getRothLessThan800$() {
        return rothLessThan800$;
    }

    public void setRothLessThan800$(int rothLessThan800$) {
        this.rothLessThan800$ = rothLessThan800$;
    }

    public int getRothLessThan8Pct() {
        return rothLessThan8Pct;
    }

    public void setRothLessThan8Pct(int rothLessThan8Pct) {
        this.rothLessThan8Pct = rothLessThan8Pct;
    }

    public int getRothMoreThan12Pct() {
        return rothMoreThan12Pct;
    }

    public void setRothMoreThan12Pct(int rothMoreThan12Pct) {
        this.rothMoreThan12Pct = rothMoreThan12Pct;
    }

    public int getRothMoreThan800$() {
        return rothMoreThan800$;
    }

    public void setRothMoreThan800$(int rothMoreThan800$) {
        this.rothMoreThan800$ = rothMoreThan800$;
    }

    public int getTraditionalLessThan100$() {
        return traditionalLessThan100$;
    }

    public void setTraditionalLessThan100$(int traditionalLessThan100$) {
        this.traditionalLessThan100$ = traditionalLessThan100$;
    }

    public int getTraditionalLessThan10Pct() {
        return traditionalLessThan10Pct;
    }

    public void setTraditionalLessThan10Pct(int traditionalLessThan10Pct) {
        this.traditionalLessThan10Pct = traditionalLessThan10Pct;
    }

    public int getTraditionalLessThan12Pct() {
        return traditionalLessThan12Pct;
    }

    public void setTraditionalLessThan12Pct(int traditionalLessThan12Pct) {
        this.traditionalLessThan12Pct = traditionalLessThan12Pct;
    }

    public int getTraditionalLessThan200$() {
        return traditionalLessThan200$;
    }

    public void setTraditionalLessThan200$(int traditionalLessThan200$) {
        this.traditionalLessThan200$ = traditionalLessThan200$;
    }

    public int getTraditionalLessThan2Pct() {
        return traditionalLessThan2Pct;
    }

    public void setTraditionalLessThan2Pct(int traditionalLessThan2Pct) {
        this.traditionalLessThan2Pct = traditionalLessThan2Pct;
    }

    public int getTraditionalLessThan300$() {
        return traditionalLessThan300$;
    }

    public void setTraditionalLessThan300$(int traditionalLessThan300$) {
        this.traditionalLessThan300$ = traditionalLessThan300$;
    }

    public int getTraditionalLessThan400$() {
        return traditionalLessThan400$;
    }

    public void setTraditionalLessThan400$(int traditionalLessThan400$) {
        this.traditionalLessThan400$ = traditionalLessThan400$;
    }

    public int getTraditionalLessThan4Pct() {
        return traditionalLessThan4Pct;
    }

    public void setTraditionalLessThan4Pct(int traditionalLessThan4Pct) {
        this.traditionalLessThan4Pct = traditionalLessThan4Pct;
    }

    public int getTraditionalLessThan50$() {
        return traditionalLessThan50$;
    }

    public void setTraditionalLessThan50$(int traditionalLessThan50$) {
        this.traditionalLessThan50$ = traditionalLessThan50$;
    }

    public int getTraditionalLessThan500$() {
        return traditionalLessThan500$;
    }

    public void setTraditionalLessThan500$(int traditionalLessThan500$) {
        this.traditionalLessThan500$ = traditionalLessThan500$;
    }

    public int getTraditionalLessThan600$() {
        return traditionalLessThan600$;
    }

    public void setTraditionalLessThan600$(int traditionalLessThan600$) {
        this.traditionalLessThan600$ = traditionalLessThan600$;
    }

    public int getTraditionalLessThan6Pct() {
        return traditionalLessThan6Pct;
    }

    public void setTraditionalLessThan6Pct(int traditionalLessThan6Pct) {
        this.traditionalLessThan6Pct = traditionalLessThan6Pct;
    }

    public int getTraditionalLessThan700$() {
        return traditionalLessThan700$;
    }

    public void setTraditionalLessThan700$(int traditionalLessThan700$) {
        this.traditionalLessThan700$ = traditionalLessThan700$;
    }

    public int getTraditionalLessThan800$() {
        return traditionalLessThan800$;
    }

    public void setTraditionalLessThan800$(int traditionalLessThan800$) {
        this.traditionalLessThan800$ = traditionalLessThan800$;
    }

    public int getTraditionalLessThan8Pct() {
        return traditionalLessThan8Pct;
    }

    public void setTraditionalLessThan8Pct(int traditionalLessThan8Pct) {
        this.traditionalLessThan8Pct = traditionalLessThan8Pct;
    }

    public int getTraditionalMoreThan12Pct() {
        return traditionalMoreThan12Pct;
    }

    public void setTraditionalMoreThan12Pct(int traditionalMoreThan12Pct) {
        this.traditionalMoreThan12Pct = traditionalMoreThan12Pct;
    }

    public int getTraditionalMoreThan800$() {
        return traditionalMoreThan800$;
    }

    public void setTraditionalMoreThan800$(int traditionalMoreThan800$) {
        this.traditionalMoreThan800$ = traditionalMoreThan800$;
    }

    public int getUnknown() {
        return unknown;
    }

    public void setUnknown(int unknown) {
        this.unknown = unknown;
    }
    
    /**
     * 
     * @param pct
     */
    public void processTraditionalPct(BigDecimal pct) {
        if(pct.compareTo(bd2) < 1) {
            this.setTraditionalLessThan2Pct(this.getTraditionalLessThan2Pct() + 1);
        } else if(pct.compareTo(bd4) < 1) {
            this.setTraditionalLessThan4Pct(this.getTraditionalLessThan4Pct() + 1);
        } else if(pct.compareTo(bd6) < 1) {
            this.setTraditionalLessThan6Pct(this.getTraditionalLessThan6Pct() + 1);
        } else if(pct.compareTo(bd8) < 1) {
            this.setTraditionalLessThan8Pct(this.getTraditionalLessThan8Pct() + 1);
        } else if(pct.compareTo(bd10) < 1) {
            this.setTraditionalLessThan10Pct(this.getTraditionalLessThan10Pct() + 1);
        } else if(pct.compareTo(bd12) < 1) {
            this.setTraditionalLessThan12Pct(this.getTraditionalLessThan12Pct() + 1);
        } else {
            this.setTraditionalMoreThan12Pct(this.getTraditionalMoreThan12Pct() + 1);
        } 
    }

    /**
     * 
     * @param pct
     */
    public void processRothPct(BigDecimal pct) {
        if(pct.compareTo(bd2) < 1) {
            this.setRothLessThan2Pct(this.getRothLessThan2Pct() + 1);
        } else if(pct.compareTo(bd4) < 1) {
            this.setRothLessThan4Pct(this.getRothLessThan4Pct() + 1);
        } else if(pct.compareTo(bd6) < 1) {
            this.setRothLessThan6Pct(this.getRothLessThan6Pct() + 1);
        } else if(pct.compareTo(bd8) < 1) {
            this.setRothLessThan8Pct(this.getRothLessThan8Pct() + 1);
        } else if(pct.compareTo(bd10) < 1) {
            this.setRothLessThan10Pct(this.getRothLessThan10Pct() + 1);
        } else if(pct.compareTo(bd12) < 1) {
            this.setRothLessThan12Pct(this.getRothLessThan12Pct() + 1);
        } else {
            this.setRothMoreThan12Pct(this.getRothMoreThan12Pct() + 1);
        } 
    }
    
    /**
     * 
     * @param amount
     */
    public void processTraditionalAmt(BigDecimal amount) {
        if(amount.compareTo(bd50) < 1) {
            this.setTraditionalLessThan50$(this.getTraditionalLessThan50$() + 1);
        } else if(amount.compareTo(bd100) < 1) {
            this.setTraditionalLessThan100$(this.getTraditionalLessThan100$() + 1);
        } else if(amount.compareTo(bd200) < 1) {
            this.setTraditionalLessThan200$(this.getTraditionalLessThan200$() + 1);
        } else if(amount.compareTo(bd300) < 1) {
            this.setTraditionalLessThan300$(this.getTraditionalLessThan300$() + 1);
        } else if(amount.compareTo(bd400) < 1) {
            this.setTraditionalLessThan400$(this.getTraditionalLessThan400$() + 1);
        } else if(amount.compareTo(bd500) < 1) {
            this.setTraditionalLessThan500$(this.getTraditionalLessThan500$() + 1);
        } else if(amount.compareTo(bd600) < 1) {
            this.setTraditionalLessThan600$(this.getTraditionalLessThan600$() + 1);
        } else if(amount.compareTo(bd700) < 1) {
            this.setTraditionalLessThan700$(this.getTraditionalLessThan700$() + 1);
        } else if(amount.compareTo(bd800) < 1) {
            this.setTraditionalLessThan800$(this.getTraditionalLessThan800$() + 1);
        } else {
            this.setTraditionalMoreThan800$(this.getTraditionalMoreThan800$() + 1);
        }
    }
    
    /**
     * 
     * @param amount
     */
    public void processRothAmt(BigDecimal amount) {
        if(amount.compareTo(bd50) < 1) {
            this.setRothLessThan50$(this.getRothLessThan50$() + 1);
        } else if(amount.compareTo(bd100) < 1) {
            this.setRothLessThan100$(this.getRothLessThan100$() + 1);
        } else if(amount.compareTo(bd200) < 1) {
            this.setRothLessThan200$(this.getRothLessThan200$() + 1);
        } else if(amount.compareTo(bd300) < 1) {
            this.setRothLessThan300$(this.getRothLessThan300$() + 1);
        } else if(amount.compareTo(bd400) < 1) {
            this.setRothLessThan400$(this.getRothLessThan400$() + 1);
        } else if(amount.compareTo(bd500) < 1) {
            this.setRothLessThan500$(this.getRothLessThan500$() + 1);
        } else if(amount.compareTo(bd600) < 1) {
            this.setRothLessThan600$(this.getRothLessThan600$() + 1);
        } else if(amount.compareTo(bd700) < 1) {
            this.setRothLessThan700$(this.getRothLessThan700$() + 1);
        } else if(amount.compareTo(bd800) < 1) {
            this.setRothLessThan800$(this.getRothLessThan800$() + 1);
        } else {
            this.setRothMoreThan800$(this.getRothMoreThan800$() + 1);
        }
    }
}

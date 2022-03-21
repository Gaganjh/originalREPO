package com.manulife.pension.bd.web.fundEvaluator;

import java.io.Serializable;

/**
 * It holds the details of the page navigation actions. 
 * Using for page irregular navigation
 * 
 * @author Ranjith Kumar
 *
 */
public class FundEvaluatorPageNavigationInfo  implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -3604558727748668782L;
    
    private String pageName;        // page name, eg - Step1      
    private String pageAction;      // page action name
    private String nextAction;      // Page next action
    private String previousAction;  // Page back action 
  
    public FundEvaluatorPageNavigationInfo(String pageName, String pageAction, String nextAction, String previousAction) {
        this.pageName = pageName;
        this.pageAction = pageAction;
        this.nextAction = nextAction;
        this.previousAction = previousAction;
    }

    /**
     * @return the pageAction
     */
    public String getPageAction() {
        return pageAction;
    }

    /**
     * @param pageAction the pageAction to set
     */
    public void setPageAction(String pageAction) {
        this.pageAction = pageAction;
    }

    /**
     * @return the nextAction
     */
    public String getNextAction() {
        return nextAction;
    }

    /**
     * @param nextAction the nextAction to set
     */
    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    /**
     * @return the previousAction
     */
    public String getPreviousAction() {
        return previousAction;
    }

    /**
     * @param previousAction the previousAction to set
     */
    public void setPreviousAction(String previousAction) {
        this.previousAction = previousAction;
    }

    /**
     * @return the pageName
     */
    public String getPageName() {
        return pageName;
    }

    /**
     * @param pageName the pageName to set
     */
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

   
}

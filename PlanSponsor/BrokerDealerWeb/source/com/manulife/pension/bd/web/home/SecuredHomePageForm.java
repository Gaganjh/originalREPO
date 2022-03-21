package com.manulife.pension.bd.web.home;

import java.util.List;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessSummaryVO;
import com.manulife.pension.service.message.report.valueobject.BDMessageCenterSummary;

/**
 * This is the form bean for secured home page action
 * 
 * @author Ilamparithi
 * 
 */
public class SecuredHomePageForm extends BaseForm {

    private static final long serialVersionUID = 1L;

    private int layerId;

    private boolean showIeval;

    private boolean showMyBOB;

    private boolean showMessageCenter;

    private boolean showBOBSetup;

    private boolean showMarketingCommentary;

    private List<Content> whatsNewContents;

    private List<Content> marketingCommentaryContents;

    private boolean allWhatsNewContents;

    private boolean showWhatsNewLink;
    
    private BDMessageCenterSummary messageCenterSummary;
    
    private boolean enableMCPreferencesLink;
    
    private BlockOfBusinessSummaryVO myBOBSummary;

    /**
     * Returns a flag to indicate whether to show all the What's New contents
     * 
     * @return the allWhatsNewContents
     */
    public boolean isAllWhatsNewContents() {
        return allWhatsNewContents;
    }

    /**
     * Sets a flag to indicate whether to show all the What's New contents
     * 
     * @param allWhatsNewContents the allWhatsNewContents to set
     */
    public void setAllWhatsNewContents(boolean allWhatsNewContents) {
        this.allWhatsNewContents = allWhatsNewContents;
    }

    /**
     * Returns the layer id for quick links section
     * 
     * @return the layerId
     */
    public int getLayerId() {
        return layerId;
    }

    /**
     * Sets the layer id for quick links section
     * 
     * @param layerId the layerId to set
     */
    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }

    /**
     * Returns the marketing commentary contents
     * 
     * @return the marketingCommentaryContents
     */
    public List<Content> getMarketingCommentaryContents() {
        return marketingCommentaryContents;
    }

    /**
     * Sets the marketing commentary contents
     * 
     * @param marketingCommentaryContents the marketingCommentaryContents to set
     */
    public void setMarketingCommentaryContents(List<Content> marketingCommentaryContents) {
        this.marketingCommentaryContents = marketingCommentaryContents;
    }

    /**
     * Returns a flag to indicate whether to show the BOB setup section
     * 
     * @return the showBOBSetup
     */
    public boolean isShowBOBSetup() {
        return showBOBSetup;
    }

    /**
     * Sets a flag to indicate whether to show the BOB setup section
     * 
     * @param showBOBSetup the showBOBSetup to set
     */
    public void setShowBOBSetup(boolean showBOBSetup) {
        this.showBOBSetup = showBOBSetup;
    }

    /**
     * Returns a flag to indicate whether to show the I:Evaluator link of Quick Links section
     * 
     * @return the showIeval
     */
    public boolean isShowIeval() {
        return showIeval;
    }

    /**
     * Sets a flag to indicate whether to show the I:Evaluator link of Quick Links section
     * 
     * @param showIeval the showIeval to set
     */
    public void setShowIeval(boolean showIeval) {
        this.showIeval = showIeval;
    }

    /**
     * Returns a flag to indicate whehter to show the Marketing Commentary section
     * 
     * @return the showMarketingCommentary
     */
    public boolean isShowMarketingCommentary() {
        return showMarketingCommentary;
    }

    /**
     * Sets a flag to indicate whehter to show the Marketing Commentary section
     * 
     * @param showMarketingCommentary the showMarketingCommentary to set
     */
    public void setShowMarketingCommentary(boolean showMarketingCommentary) {
        this.showMarketingCommentary = showMarketingCommentary;
    }

    /**
     * Returns a flag to indicate whether to show the message center section
     * 
     * @return the showMessageCenter
     */
    public boolean isShowMessageCenter() {
        return showMessageCenter;
    }

    /**
     * Sets a flag to indicate whether to show the message center section
     * 
     * @param showMessageCenter the showMessageCenter to set
     */
    public void setShowMessageCenter(boolean showMessageCenter) {
        this.showMessageCenter = showMessageCenter;
    }

    /**
     * Returns a flag to indicate whether to show the My BOB section
     * 
     * @return the showMyBOB
     */
    public boolean isShowMyBOB() {
        return showMyBOB;
    }

    /**
     * Sets a flag to indicate whether to show the My BOB section
     * 
     * @param showMyBOB the showMyBOB to set
     */
    public void setShowMyBOB(boolean showMyBOB) {
        this.showMyBOB = showMyBOB;
    }

    /**
     * Returns a flag to indicate whether to show a link in the What's New section
     * 
     * @return the showWhatsNewLink
     */
    public boolean isShowWhatsNewLink() {
        return showWhatsNewLink;
    }

    /**
     * Sets a flag to indicate whether to show a link in the What's New section
     * 
     * @param showWhatsNewLink the showWhatsNewLink to set
     */
    public void setShowWhatsNewLink(boolean showWhatsNewLink) {
        this.showWhatsNewLink = showWhatsNewLink;
    }

    /**
     * Returns the contents for What's New section
     * 
     * @return the whatsNewContents
     */
    public List<Content> getWhatsNewContents() {
        return whatsNewContents;
    }

    /**
     * Sets the contents for What's New section
     * 
     * @param whatsNewContents the whatsNewContents to set
     */
    public void setWhatsNewContents(List<Content> whatsNewContents) {
        this.whatsNewContents = whatsNewContents;
    }

    /**
     * Returns a flag to indicate whether to show the link in Message Center section.
     * 
     * @return the enableMCPreferencesLink
     */
    public boolean isEnableMCPreferencesLink() {
        return enableMCPreferencesLink;
    }

    /**
     * Sets a flag to indicate whether to show the link in Message Center section.
     * 
     * @param enableMCPreferencesLink the enableMCPreferencesLink to set
     */
    public void setEnableMCPreferencesLink(boolean enableMCPreferencesLink) {
        this.enableMCPreferencesLink = enableMCPreferencesLink;
    }

    /**
     * Returns the message center section info
     * 
     * @return the messageCenterSummary
     */
    public BDMessageCenterSummary getMessageCenterSummary() {
        return messageCenterSummary;
    }

    /**
     * Sets message center section info
     * 
     * @param messageCenterSummary the messageCenterSummary to set
     */
    public void setMessageCenterSummary(BDMessageCenterSummary messageCenterSummary) {
        this.messageCenterSummary = messageCenterSummary;
    }

    /**
     * returns the BOB Summary VO
     * 
     * @return the myBOBSummary
     */
    public BlockOfBusinessSummaryVO getMyBOBSummary() {
        return myBOBSummary;
    }

    /**
     * Sets the BOB Summary VO
     * 
     * @param myBOBSummary the myBOBSummary to set
     */
    public void setMyBOBSummary(BlockOfBusinessSummaryVO myBOBSummary) {
        this.myBOBSummary = myBOBSummary;
    }

}

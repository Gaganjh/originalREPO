package com.manulife.pension.bd.web.registration.util;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

/**
 * Store the information for the challenge question and answer
 * 
 * @author guweigu
 * 
 */
public class PasswordChallengeInput implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String questionId = "";

    private String customizedQuestionText;

    private String answer;

    private String confirmedAnswer;

    /**
     * Get the whole list of question label value bean
     * 
     * @return
     */
    public List<LabelValueBean> getQuestionList() {
        return ChallengeQuestionListUtil.getInstance().getQuestionsList();
    }

    /**
     * Depending on the questionId, either return the customized question or the selected question
     * 
     * @return
     */
    public String getQuestionText() {
        if (StringUtils.isEmpty(questionId)) {
            return "";
        } else if (StringUtils.equals(ChallengeQuestionListUtil.CreationQuestionId, questionId)) {
            return StringUtils.trimToEmpty(customizedQuestionText);
        } else {
            return ChallengeQuestionListUtil.getInstance().getSystemDefinedQuestionMap().get(
                    Integer.parseInt(questionId));
        }
    }

    /**
     * Returns the answer
     * 
     * @return String
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the answer
     * 
     * @param answer
     */
    public void setAnswer(String answer) {
        this.answer = StringUtils.trimToEmpty(answer);
    }

    /**
     * Returns the confirmedAnswer
     * 
     * @return String
     */
    public String getConfirmedAnswer() {
        return confirmedAnswer;
    }

    /**
     * Sets the confirmedAnswer
     * 
     * @param confirmedAnswer
     */
    public void setConfirmedAnswer(String confirmedAnswer) {
        this.confirmedAnswer = StringUtils.trimToEmpty(confirmedAnswer);
    }

    /**
     * Returns the questionId
     * 
     * @return String
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * Sets the questionId
     * 
     * @param questionId
     */
    public void setQuestionId(String questionId) {
        this.questionId = StringUtils.trimToEmpty(questionId);
    }

    /**
     * Copy data from one form to another
     * 
     * @param src
     */
    public void copyFrom(PasswordChallengeInput src) {
        this.answer = src.answer;
        this.confirmedAnswer = src.confirmedAnswer;
        this.questionId = src.questionId;
        this.customizedQuestionText = src.customizedQuestionText;
    }

    /**
     * Returns the customizedQuestionText
     * 
     * @return String
     */
    public String getCustomizedQuestionText() {
        return customizedQuestionText;
    }

    /**
     * Sets the customizedQuestionText
     * 
     * @param customizedQuestionText
     */
    public void setCustomizedQuestionText(String customizedQuestionText) {
        this.customizedQuestionText = StringUtils.trimToEmpty(customizedQuestionText);
    }

    /**
     * Returns true if all field is empty
     * 
     * @return boolean
     */
    public boolean isEmpty() {
    	return  StringUtils.isEmpty(answer)
                && StringUtils.isEmpty(confirmedAnswer)
                && StringUtils.isEmpty(customizedQuestionText);
        
    }
    
    /**
     * Returns true if all values are present
     * 
     * @return boolean
     */
    public boolean allValuesPresent() {
    	boolean flag = false;
        if (StringUtils.equals(ChallengeQuestionListUtil.CreationQuestionId, questionId)) {
            flag = StringUtils.isNotEmpty(answer) && StringUtils.isNotEmpty(confirmedAnswer)
                    && StringUtils.isNotEmpty(customizedQuestionText);
        } else {
            flag = StringUtils.isNotEmpty(answer) && StringUtils.isNotEmpty(confirmedAnswer);
        }
       return flag;
    }
    

    /**
     * This method clears all fields
     */
    public void clear() {
        questionId = "";
        customizedQuestionText = "";
        answer = "";
        confirmedAnswer = "";
    }
}

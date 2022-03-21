package com.manulife.pension.bd.web.validation.rules;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.registration.util.ChallengeQuestionListUtil;
import com.manulife.pension.bd.web.registration.util.PasswordChallengeInput;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.MatchRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationRule;

/**
 * This is a rule class to validate the challenge questions and answers.
 * 
 * @author Ilamparithi
 * 
 */
public class PasswordChallengeInputRule extends ValidationRule {

    private static final PasswordChallengeInputRule instance = new PasswordChallengeInputRule();

    private static final MandatoryRule firstChallengeQMandatoryRule = new MandatoryRule(
            BDErrorCodes.FIRST_CHALLENGE_QUESTION_NOT_SELECTED);

    private static final MandatoryRule firstChallengeQAMandatoryRule = new MandatoryRule(
            BDErrorCodes.FIRST_CHALLENGE_QUESTION_ANSWER_NOT_ENTERED);

    private static final MandatoryRule firstChallengeQConfirmAMandatoryRule = new MandatoryRule(
            BDErrorCodes.FIRST_CHALLENGE_QUESTION_CONFIRM_ANSWER_NOT_ENTERED);

    private static final MandatoryRule firstCreateChallengeQMandatoryRule = new MandatoryRule(
            BDErrorCodes.FIRST_CREATE_CHALLENGE_QUESTION_NOT_ENTERED);

    private static final MandatoryRule secondChallengeQMandatoryRule = new MandatoryRule(
            BDErrorCodes.SECOND_CHALLENGE_QUESTION_NOT_SELECTED);

    private static final MandatoryRule secondCreateChallengeQMandatoryRule = new MandatoryRule(
            BDErrorCodes.SECOND_CREATE_CHALLENGE_QUESTION_NOT_ENTERED);

    private static final MandatoryRule secondChallengeQAMandatoryRule = new MandatoryRule(
            BDErrorCodes.SECOND_CHALLENGE_QUESTION_ANSWER_NOT_ENTERED);

    private static final MandatoryRule secondChallengeQConfirmAMandatoryRule = new MandatoryRule(
            BDErrorCodes.SECOND_CHALLENGE_QUESTION_CONFIRM_ANSWER_NOT_ENTERED);

    private static final RegularExpressionRule invalidFirstCreateChallengeQRErule = new RegularExpressionRule(
            BDErrorCodes.FIRST_CREATE_CHALLENGE_QUESTION_INVALID,
            BDRuleConstants.CREATE_CHALLENGE_QUESTION_RE);

    private static final RegularExpressionRule invalidSecondCreateChallengeQRErule = new RegularExpressionRule(
            BDErrorCodes.SECOND_CREATE_CHALLENGE_QUESTION_INVALID,
            BDRuleConstants.CREATE_CHALLENGE_QUESTION_RE);

    private static final RegularExpressionRule invalidFirstChallengeQARErule = new RegularExpressionRule(
            BDErrorCodes.FIRST_CHALLENGE_QUESTION_ANSWER_INVALID,
            BDRuleConstants.CHALLENGE_QUESTION_ANSWER_RE);

    private static final RegularExpressionRule invalidSecondChallengeQARErule = new RegularExpressionRule(
            BDErrorCodes.SECOND_CHALLENGE_QUESTION_ANSWER_INVALID,
            BDRuleConstants.CHALLENGE_QUESTION_ANSWER_RE);

    MatchRule firstChallengeAnswersMatchRule = new MatchRule(
            BDErrorCodes.FIRST_CHALLENGE_QUESTION_ANSWERS_MISMATCH);

    MatchRule secondChallengeAnswersMatchRule = new MatchRule(
            BDErrorCodes.SECOND_CHALLENGE_QUESTION_ANSWERS_MISMATCH);

    NoMatchRule challengeQuestionsNoMatchRule = new NoMatchRule(
            BDErrorCodes.SELECTED_CHALLENGE_QUESTIONS_SAME);

    NoMatchRule createdChallengeQuestionsNoMatchRule = new NoMatchRule(
            BDErrorCodes.CREATED_CHALLENGE_QUESTIONS_SAME);

    NoMatchRule challengeQAnswersNoMatchRule = new NoMatchRule(
            BDErrorCodes.CHALLENGE_QUESTIONS_ANSWERS_SAME);

    /**
     * Constructor
     */
    public PasswordChallengeInputRule() {
        super(0);
    }

    /**
     * Returns a PasswordChallengeInputRule instance
     * 
     * @return PasswordChallengeInputRule
     */
    public static final PasswordChallengeInputRule getInstance() {
        return instance;
    }

    /**
     * This method validates two PasswordChallengeInput objects which represent challenge question 1
     * and challenge question 2 respectively. It then populates the collection object with errors if
     * any
     * 
     * @return boolean a boolean value to indicate whether the validation is passed or not
     */
    @SuppressWarnings("unchecked")
    public boolean validate(String[] fieldIds, Collection validationErrors, Object objectToValidate) {
        boolean isValid = false;
        if (objectToValidate != null && objectToValidate instanceof Pair) {
            Pair<PasswordChallengeInput, PasswordChallengeInput> pair = (Pair<PasswordChallengeInput, PasswordChallengeInput>) objectToValidate;
            PasswordChallengeInput first = (PasswordChallengeInput) pair.getFirst();
            PasswordChallengeInput second = (PasswordChallengeInput) pair.getSecond();

            // Validating First Challenge Question
            if (!ChallengeQuestionListUtil.CreationQuestionId.equals(first.getQuestionId())) {
                firstChallengeQMandatoryRule.validate(fieldIds, validationErrors, first
                        .getQuestionText());
            } else {
                isValid = firstCreateChallengeQMandatoryRule.validate(fieldIds, validationErrors,
                        first.getQuestionText());
                if (isValid) {
                    isValid = invalidFirstCreateChallengeQRErule.validate(fieldIds,
                            validationErrors, first.getQuestionText());
                }
            }
            // Validating First Challenge Question Answer

            isValid = firstChallengeQAMandatoryRule.validate(fieldIds, validationErrors, first
                    .getAnswer());
            if (isValid) {
                invalidFirstChallengeQARErule.validate(fieldIds, validationErrors, first
                        .getAnswer());
            }
            isValid = firstChallengeQConfirmAMandatoryRule.validate(fieldIds, validationErrors,
                    first.getConfirmedAnswer());
            if (isValid) {
                firstChallengeAnswersMatchRule.validate(fieldIds, validationErrors,
                        new Pair<String, String>(StringUtils.upperCase(first.getAnswer()),
                                StringUtils.upperCase(first.getConfirmedAnswer())));
            }

            // Validating Second Challenge Question
            if (!ChallengeQuestionListUtil.CreationQuestionId.equals(second.getQuestionId())) {
                secondChallengeQMandatoryRule.validate(fieldIds, validationErrors, second
                        .getQuestionText());
            } else {
                isValid = secondCreateChallengeQMandatoryRule.validate(fieldIds, validationErrors,
                        second.getQuestionText());
                if (isValid) {
                    isValid = invalidSecondCreateChallengeQRErule.validate(fieldIds,
                            validationErrors, second.getQuestionText());
                }
            }
            // Validating First and Second Challenge Questions

            // both questions are selected from drop-down
            if (!ChallengeQuestionListUtil.CreationQuestionId.equals(first.getQuestionId())
                    && !ChallengeQuestionListUtil.CreationQuestionId.equals(second.getQuestionId())) {
                // Not necessary to match if both questions are not selected or only one is selected
                if (StringUtils.isNotEmpty(first.getQuestionId())
                        && StringUtils.isNotEmpty(second.getQuestionId())) {
                    challengeQuestionsNoMatchRule.validate(fieldIds, validationErrors,
                            new Pair<String, String>(
                                    StringUtils.upperCase(first.getQuestionText()), StringUtils
                                            .upperCase(second.getQuestionText())));
                }
                // both questions are manually entered
            } else if (ChallengeQuestionListUtil.CreationQuestionId.equals(first.getQuestionId())
                    && ChallengeQuestionListUtil.CreationQuestionId.equals(second.getQuestionId())) {
                // Not necessary to match if both questions are not entered or only one is entered
                if (StringUtils.isNotEmpty(first.getQuestionText())
                        && StringUtils.isNotEmpty(second.getQuestionText())) {
                    createdChallengeQuestionsNoMatchRule.validate(fieldIds, validationErrors,
                            new Pair<String, String>(
                                    StringUtils.upperCase(first.getQuestionText()), StringUtils
                                            .upperCase(second.getQuestionText())));
                }
                // one question is entered and one is selected from drop-down
            } else {
                // Not necessary to match if both questions are not entered or only one is entered
                if (StringUtils.isNotEmpty(first.getQuestionText())
                        && StringUtils.isNotEmpty(second.getQuestionText())) {
                    challengeQuestionsNoMatchRule.validate(fieldIds, validationErrors,
                            new Pair<String, String>(
                                    StringUtils.upperCase(first.getQuestionText()), StringUtils
                                            .upperCase(second.getQuestionText())));
                }
            }

            // Validating Second Challenge Question Answer and First and Second
            // Challenge Question Answers

            isValid = secondChallengeQAMandatoryRule.validate(fieldIds, validationErrors, second
                    .getAnswer());
            if (isValid) {
                invalidSecondChallengeQARErule.validate(fieldIds, validationErrors, second
                        .getAnswer());
            }

            // Not necessary to match if both answers are not entered or only one is entered
            if (StringUtils.isNotEmpty(first.getAnswer())
                    && StringUtils.isNotEmpty(second.getAnswer())) {
                challengeQAnswersNoMatchRule.validate(fieldIds, validationErrors,
                        new Pair<String, String>(StringUtils.upperCase(first.getAnswer()),
                                StringUtils.upperCase(second.getAnswer())));
            }
            isValid = secondChallengeQConfirmAMandatoryRule.validate(fieldIds, validationErrors,
                    second.getConfirmedAnswer());
            if (isValid) {
                isValid = secondChallengeAnswersMatchRule.validate(fieldIds, validationErrors,
                        new Pair<String, String>(StringUtils.upperCase(second.getAnswer()),
                                StringUtils.upperCase(second.getConfirmedAnswer())));
            }

        } else {
            return false;
        }
        return isValid;
    }

}

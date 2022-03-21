package com.manulife.pension.bd.web.registration.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.BDSystemSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;

/**
 * The utility class to construct Challenge question list
 * 
 * @author guweigu
 * 
 */
public class ChallengeQuestionListUtil {
	private static String SelectQuestionLabel = "Please select a question ...";
	private static String CreationQuestionLabel = "Create your own question";
	public static String CreationQuestionId = "0";

	private static final ChallengeQuestionListUtil instance = new ChallengeQuestionListUtil();

	private static Logger log = Logger
			.getLogger(ChallengeQuestionListUtil.class);
	private List<LabelValueBean> questionList;
	private Map<Integer, String> systemDefinedQuestionMap = Collections.emptyMap();

	public static ChallengeQuestionListUtil getInstance() {
		return instance;
	}

	/**
	 * Construct the question list: 1. please select question, (value is "")
	 * 2-n: system defined questions (value is "1" - "n") last one: create own
	 * question (value is "0")
	 */
	private ChallengeQuestionListUtil() {
		List<LabelValueBean> tempList = new ArrayList<LabelValueBean>();
		tempList.add(new LabelValueBean(SelectQuestionLabel, ""));
        // US44936
		//tempList.addAll(getSystemDefinedQuestions());
		tempList.add(new LabelValueBean(CreationQuestionLabel,
				CreationQuestionId));
		questionList = Collections.unmodifiableList(tempList);
	}

	/**
	 * Return the constructed question list
	 * @return
	 */
	public List<LabelValueBean> getQuestionsList() {
		return questionList;
	}

	/**
	 * Returns the system defined question map, the key is the id
	 * the value is the text of the question
	 * @return
	 */
	public Map<Integer, String> getSystemDefinedQuestionMap() {
		return systemDefinedQuestionMap;
	}

	/**
	 * Load the system defined challenged questions
	 * 
	 * @return
	 */
	private List<LabelValueBean> getSystemDefinedQuestions() {
		List<LabelValueBean> tempList = new ArrayList<LabelValueBean>();
		try {
			Map<Integer, String> systemQuestions = BDSystemSecurityServiceDelegate
					.getInstance().getChallengeQuestions();
			systemDefinedQuestionMap = Collections
					.unmodifiableMap(systemQuestions);
			for (Iterator<Integer> it = systemQuestions.keySet().iterator(); it
					.hasNext();) {
				Integer key = it.next();
				tempList.add(new LabelValueBean(systemQuestions.get(key), key
						.toString()));
			}
			Collections.sort(tempList, new Comparator<LabelValueBean>() {

				public int compare(LabelValueBean o1, LabelValueBean o2) {
					return o1.getLabel().compareTo(o2.getLabel());
				}
			});
		} catch (SystemException e) {
			log.error("Fail to load the system defined questions", e);
		}
		return tempList;
	}
}

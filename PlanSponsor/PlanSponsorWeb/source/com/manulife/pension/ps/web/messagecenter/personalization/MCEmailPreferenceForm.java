package com.manulife.pension.ps.web.messagecenter.personalization;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;


import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.message.valueobject.EmailPreference;
import com.manulife.pension.service.message.valueobject.EmailPreference.EmailFormat;
import com.manulife.pension.service.message.valueobject.EmailPreference.NormalMessageFrequency;
import com.manulife.pension.service.message.valueobject.EmailPreference.UrgentMessageFrequency;
import com.manulife.pension.service.message.valueobject.EmailPreference.WeeklyDay;
import com.manulife.pension.validator.ValidationError;

public class MCEmailPreferenceForm extends AutoForm implements
		CloneableForm, MCConstants {

	private String urgentMessageFrequency = "";
	private String normalMessageFrequency = "";
	private String weekDay;
	private String startingDate;
	private String emailFormat;
	private String firstName;
	private String lastName;
	private Long userProfileId;
	private Integer contractId;
	// this is only used for Internal user editing
	// external user's preference.  For external user
	// editing, it will get from UserProfile
	private boolean tpa;

	private Date biweeklyStartingDate;
	private Date originalStartingDate;

	private String emailWithNoMessage;

	public static final String UrgentImmediately = "I";
	public static final String UrgentDaily = "D";

	public static final String NormalDaily = "D";
	public static final String NormalWeekly = "W";
	public static final String NormalBiWeekly = "B";
	public static final String NormalNever = "N";
	

	private static final int AllowedStartingDays = 14;

	private MCEmailPreferenceForm clonedForm;

	private static EnumMap<UrgentMessageFrequency, String> UrgentMessageFrequencyMap = new EnumMap<UrgentMessageFrequency, String>(
			UrgentMessageFrequency.class);
	private static Map<String, UrgentMessageFrequency> ReverseUrgentFrequencyMap = new HashMap<String, UrgentMessageFrequency>();
	private static EnumMap<NormalMessageFrequency, String> NormalMessageFrequencyMap = new EnumMap<NormalMessageFrequency, String>(
			NormalMessageFrequency.class);
	private static Map<String, NormalMessageFrequency> ReverseNormalFrequencyMap = new HashMap<String, NormalMessageFrequency>();

	private boolean userCanEdit = false;
	
	static {
		UrgentMessageFrequencyMap.put(UrgentMessageFrequency.IMMEDIATELY,
				UrgentImmediately);
		UrgentMessageFrequencyMap.put(UrgentMessageFrequency.ONCE_A_DAY,
				UrgentDaily);
		ReverseUrgentFrequencyMap.put(UrgentImmediately,
				UrgentMessageFrequency.IMMEDIATELY);
		ReverseUrgentFrequencyMap.put(UrgentDaily,
				UrgentMessageFrequency.ONCE_A_DAY);
		NormalMessageFrequencyMap
				.put(NormalMessageFrequency.DAILY, NormalDaily);
		NormalMessageFrequencyMap.put(NormalMessageFrequency.WEEKLY,
				NormalWeekly);
		NormalMessageFrequencyMap.put(NormalMessageFrequency.BIWEEKLY,
				NormalBiWeekly);
		NormalMessageFrequencyMap
				.put(NormalMessageFrequency.Never, NormalNever);

		ReverseNormalFrequencyMap
				.put(NormalDaily, NormalMessageFrequency.DAILY);
		ReverseNormalFrequencyMap.put(NormalWeekly,
				NormalMessageFrequency.WEEKLY);
		ReverseNormalFrequencyMap.put(NormalBiWeekly,
				NormalMessageFrequency.BIWEEKLY);
		ReverseNormalFrequencyMap
				.put(NormalNever, NormalMessageFrequency.Never);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void update(EmailPreference preference) {
		if (preference == null) {
			return;
		}
		setUrgentMessageFrequency(UrgentMessageFrequencyMap.get(preference
				.getUrgentMessageFrequency()));
		setNormalMessageFrequency(NormalMessageFrequencyMap.get(preference
				.getNormalMessageFrequency()));
		emailWithNoMessage = preference.isEmailWithNoMessage() ? "Y" : "N";
		setEmailFormat(preference.getFormatPreference().getValue());
		switch (preference.getNormalMessageFrequency()) {
		case WEEKLY:
			WeeklyDay day = preference.getWeeklyDay();
			if (day == null) {
				setWeekDay("");
			} else {
				setWeekDay(day.getValue());
			}
			break;
		case BIWEEKLY:
			originalStartingDate = preference.getStartingDateForBiweekly();

			if (originalStartingDate != null) {
				this.startingDate = new SimpleDateFormat(DateFormat)
						.format(originalStartingDate);
			}
			break;
		}
	}

	public EmailPreference getEmailPreference() {
		EmailPreference preference = null;

		UrgentMessageFrequency uf = ReverseUrgentFrequencyMap
				.get(getUrgentMessageFrequency());
		if ( uf == null ) {
			uf = UrgentMessageFrequency.NEVER;
		}
		NormalMessageFrequency nf = ReverseNormalFrequencyMap
				.get(getNormalMessageFrequency());
		EmailFormat ef = EmailFormat.get(getEmailFormat());
		switch (nf) {
		case DAILY:
			preference = new EmailPreference(uf, true, StringUtils.equals("Y",
					emailWithNoMessage),ef);
			break;
		case WEEKLY:
			preference = new EmailPreference(uf, WeeklyDay.get(getWeekDay()),
					StringUtils.equals("Y", emailWithNoMessage),ef);
			break;
		case BIWEEKLY:
			preference = new EmailPreference(uf, biweeklyStartingDate,
					StringUtils.equals("Y", emailWithNoMessage),ef);
			break;
		case Never:
			preference = new EmailPreference(uf, false, StringUtils.equals("Y",
					emailWithNoMessage),ef);
		}
		return preference;
	}

	public void clear( HttpServletRequest request) {
	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public void storeClonedForm() {
		clonedForm = new MCEmailPreferenceForm();
		clonedForm.setUrgentMessageFrequency(getUrgentMessageFrequency());
		clonedForm.setNormalMessageFrequency(getNormalMessageFrequency());
		clonedForm.setWeekDay(getWeekDay());
		clonedForm.setEmailWithNoMessage(getEmailWithNoMessage());
		clonedForm.setStartingDate(getStartingDate());
		clonedForm.setEmailFormat(getEmailFormat());
		clonedForm.setTpa(isTpa());
	}

	public Object clone() {
		storeClonedForm();
		return clonedForm;
	}

	public String getUrgentMessageFrequency() {
		return urgentMessageFrequency;
	}

	public void setUrgentMessageFrequency(String urgentMessageFrequency) {
		this.urgentMessageFrequency = StringUtils
				.trimToEmpty(urgentMessageFrequency);
	}

	public String getNormalMessageFrequency() {
		return normalMessageFrequency;
	}

	public void setNormalMessageFrequency(String normalMessageFrequency) {
		this.normalMessageFrequency = StringUtils
				.trimToEmpty(normalMessageFrequency);
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = StringUtils.trimToEmpty(weekDay);
	}

	public String getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(String startingDate) {
		this.startingDate = StringUtils.trimToEmpty(startingDate);
		this.biweeklyStartingDate = MCUtils.convertDate(startingDate);
	}

	public void validate(List<ValidationError> errors) {
		if (NormalBiWeekly.equals(getNormalMessageFrequency())) {
			if ("".equals(getStartingDate())) {
				errors.add(new ValidationError("", ErrorMissingStartingDate));
			} else if (biweeklyStartingDate == null) {
				// wrong format
				errors.add(new ValidationError("", ErrorInvalidStartingDate));
			} else if (originalStartingDate == null
					|| !DateUtils.isSameDay(biweeklyStartingDate,
							originalStartingDate)) {
				// check if it is within two weeks
				Calendar today = DateUtils.truncate(new GregorianCalendar(),
						Calendar.DATE);
				today.add(Calendar.DATE, 1);
				Calendar twoWeek = DateUtils.truncate(new GregorianCalendar(),
						Calendar.DATE);
				twoWeek.add(Calendar.DATE, AllowedStartingDays);
				if (twoWeek.getTime().before(biweeklyStartingDate)
						|| today.getTime().after(biweeklyStartingDate)) {
					errors.add(new ValidationError("",
							ErrorStartingDateNotInRange));
				}
			}
		}
	}

	public Date getBiweeklyStartingDate() {
		return biweeklyStartingDate;
	}

	public void setBiweeklyStartingDate(Date biweeklyStartingDate) {
		this.biweeklyStartingDate = biweeklyStartingDate;
	}

	public String getValidDatesForJavaScript() {
		StringBuffer buffer = new StringBuffer("[");
		GregorianCalendar today = new GregorianCalendar();
		today.add(Calendar.DATE, 1);
		for (int i = 0; i < AllowedStartingDays; i++) {
			buffer.append("new Date(");
			buffer.append(today.get(Calendar.YEAR) + ","
					+ today.get(Calendar.MONTH) + ","
					+ today.get(Calendar.DATE));
			buffer.append("),");
			today.add(Calendar.DATE, 1);
		}
		buffer.setCharAt(buffer.length() - 1, ']');
		return buffer.toString();
	}

	public Date getDefaultStartingBiweeklyDate() {
		return DateUtils.add(new Date(), Calendar.DATE, 1);
	}

	public String getEmailWithNoMessage() {
		return emailWithNoMessage;
	}

	public void setEmailWithNoMessage(String emailWithNoMessage) {
		this.emailWithNoMessage = emailWithNoMessage;
	}

	public String getEmailFormat() {
		return emailFormat;
	}

	public void setEmailFormat(String emailFormat) {
		this.emailFormat = emailFormat;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	

	public Long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}	

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the userCanEdit
	 */
	public boolean isUserCanEdit() {
		return userCanEdit;
	}

	/**
	 * @param userCanEdit the userCanEdit to set
	 */
	public void setUserCanEdit(boolean userCanEdit) {
		this.userCanEdit = userCanEdit;
	}

	
	public boolean isTpa() {
		return tpa;
	}

	public void setTpa(boolean tpa) {
		this.tpa = tpa;
	}
}

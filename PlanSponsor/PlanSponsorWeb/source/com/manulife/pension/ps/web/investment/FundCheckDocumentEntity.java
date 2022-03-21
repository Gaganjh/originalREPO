package com.manulife.pension.ps.web.investment;

/**
 * This is the bean class to represent the pdf related info for online display
 * 
 * @author Ilamparithi
 * 
 */
public class FundCheckDocumentEntity {

	private String season;
	private String year;
	private String title;
	private String participantNoticeInd; 
	private String participantNoticeTitle; 
	private String language;

	/**
	 * Returns the season of the corresponding pdf
	 * 
	 * @return the season
	 */
	public String getSeason() {
		return season;
	}

	/**
	 * Sets the season of the corresponding pdf
	 * 
	 * @param season
	 *            the season to set
	 */
	public void setSeason(String season) {
		this.season = season;
	}

	/**
	 * Returns the year of the corresponding pdf
	 * 
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Sets the year of the corresponding pdf
	 * 
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * Returns the title of the corresponding pdf
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the corresponding pdf
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getParticipantNoticeInd() {
		return participantNoticeInd;
	}

	public void setParticipantNoticeInd(String participantNoticeInd) {
		this.participantNoticeInd = participantNoticeInd;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getParticipantNoticeTitle() {
		return participantNoticeTitle;
	}

	public void setParticipantNoticeTitle(String participantNoticeTitle) {
		this.participantNoticeTitle = participantNoticeTitle;
	}

}

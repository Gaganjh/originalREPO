package com.manulife.pension.bd.web.news;

import java.util.Date;
import java.util.List;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This is the form bean for News Action
 * 
 * @author Ilamparithi
 *
 */
public class NewsForm extends AutoForm {

	private static final long serialVersionUID = 1L;

	private int articleId;
	private String month;
	private String year;
	private List<Date> archiveDatesList;
	private List<Content> archivedNews;
	private Date selectedArchiveDate;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public List<Date> getArchiveDatesList() {
		return archiveDatesList;
	}
	public void setArchiveDatesList(List<Date> archiveDatesList) {
		this.archiveDatesList = archiveDatesList;
	}
	public List<Content> getArchivedNews() {
		return archivedNews;
	}
	public void setArchivedNews(List<Content> archivedNews) {
		this.archivedNews = archivedNews;
	}
	public Date getSelectedArchiveDate() {
		return selectedArchiveDate;
	}
	public void setSelectedArchiveDate(Date selectedArchiveDate) {
		this.selectedArchiveDate = selectedArchiveDate;
	}
}

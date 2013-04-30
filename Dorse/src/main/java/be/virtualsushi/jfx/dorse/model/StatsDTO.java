package be.virtualsushi.jfx.dorse.model;

import java.util.Date;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 29/04/13
 * Time: 12:05
 */
public class StatsDTO {
	                                        
	private Date fromDate;
	private Date toDate;
	private String article_id;

	public StatsDTO(String article_id, Date fromDate, Date toDate) {
		this.article_id = article_id;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public String getArticle() {
		return article_id;
	}

	public void setArticle(String article_id) {
		this.article_id = article_id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}

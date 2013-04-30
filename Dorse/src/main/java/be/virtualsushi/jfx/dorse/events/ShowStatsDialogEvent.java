package be.virtualsushi.jfx.dorse.events;


import be.virtualsushi.jfx.dorse.model.Article;

public class ShowStatsDialogEvent {

	private final String dialogTitle;
	private final Article article;

	public ShowStatsDialogEvent(String title, Article article) {
		this.dialogTitle = title;
		this.article = article;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public Article getArticle() {
		return article;
	}
}

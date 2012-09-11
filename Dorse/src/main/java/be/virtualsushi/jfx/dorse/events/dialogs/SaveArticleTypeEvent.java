package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.ArticleType;

public class SaveArticleTypeEvent extends SaveEntityEvent<ArticleType> {

	public SaveArticleTypeEvent(ArticleType entity) {
		super(entity);
	}

}

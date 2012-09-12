package be.virtualsushi.jfx.dorse.dialogs;

import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveArticleTypeEvent;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.ArticleType;

@Component
@FxmlFile("IdNamePairEditDialog.fxml")
public class ArticleTypeEditDialog extends IdNamePairEditDialog<ArticleType> {

	@Override
	protected ArticleType createNewEntityInstance() {
		return new ArticleType();
	}

	@Override
	protected SaveEntityEvent<ArticleType> createSaveEvent(ArticleType entity) {
		return new SaveArticleTypeEvent(entity);
	}

}

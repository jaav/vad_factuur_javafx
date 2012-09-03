package be.virtualsushi.jfx.dorse.dialogs;

import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.ArticleType;

@Component
@FxmlFile("IdNamePairEditDialog.fxml")
public class ArticleTypeEditDialog extends IdNamePairEditDialog<ArticleType> {

}

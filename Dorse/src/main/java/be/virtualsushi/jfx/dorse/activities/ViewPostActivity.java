package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.model.Post;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Scope("prototype")
public class ViewPostActivity extends AbstractViewEntityActivity<VBox, Post> {

	@FXML
	private Label idField, nameLabel, bottomLabel, topLabel;

	@FXML
	private Label nameField, priceField, bottomField, topField;

	@Override
	protected void mapFields(Post post) {
    idField.setText(String.valueOf(post.getId()));
    nameField.setText(post.getName());
    bottomField.setText(post.getBottom()+" g");
    topField.setText(post.getTop()+" g");
    priceField.setText("€ "+post.getPrice());
	}

  @Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization(Post entity) {
	}

}

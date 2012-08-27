package be.virtualsushi.jfx.dorse.control;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ImageButton extends Button {

	public ImageButton() {
		super();
		setGraphic(new ImageView(getImage()));
		getStyleClass().add("image-button");
	}

	protected abstract Image getImage();

}

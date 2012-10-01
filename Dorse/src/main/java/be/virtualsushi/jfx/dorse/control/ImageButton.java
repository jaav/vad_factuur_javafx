package be.virtualsushi.jfx.dorse.control;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ImageButton extends Button {

	public ImageButton() {
		super();
    init();
	}

  public ImageButton(String text){
    super(text);
    init();
  }

  public void init(){
    setGraphic(new ImageView(getImage()));
    setGraphicTextGap(3);
    setContentDisplay(ContentDisplay.LEFT);
    getStyleClass().add("image-button");
  }

	protected abstract Image getImage();

}

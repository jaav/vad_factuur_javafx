package be.virtualsushi.jfx.dorse.control;

import java.util.ResourceBundle;

import javafx.scene.control.Label;

public class LoadingMask extends DialogPopup {

	public static final String LOADING_CONSTANT_NAME = "loading";

	public LoadingMask(ResourceBundle resources) {
		super(false, false);
		setContent(new Label(resources.getString(LOADING_CONSTANT_NAME)));
	}

}

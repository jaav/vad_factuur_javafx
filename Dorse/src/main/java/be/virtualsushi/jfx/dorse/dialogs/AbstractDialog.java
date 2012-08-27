package be.virtualsushi.jfx.dorse.dialogs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.fxml.AppUiComponent;
import be.virtualsushi.jfx.dorse.fxml.DorseFxmlLoader;

import com.zenjava.jfxflow.dialog.Dialog;

public class AbstractDialog extends Dialog implements AppUiComponent {

	private DorseFxmlLoader dorseFxmlLoader;

	@Override
	@PostConstruct
	public void bindUi() {
		setContent(dorseFxmlLoader.bindView(this));
	}

	@Autowired
	public DorseFxmlLoader getDorseFxmlLoader() {
		return dorseFxmlLoader;
	}

	public void setDorseFxmlLoader(DorseFxmlLoader dorseFxmlLoader) {
		this.dorseFxmlLoader = dorseFxmlLoader;
	}

}

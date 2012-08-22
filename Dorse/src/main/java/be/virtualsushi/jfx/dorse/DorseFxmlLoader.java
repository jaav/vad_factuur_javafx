package be.virtualsushi.jfx.dorse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import org.apache.commons.io.IOUtils;

import com.zenjava.jfxflow.actvity.Activity;
import com.zenjava.jfxflow.actvity.FxmlLoadException;
import com.zenjava.jfxflow.actvity.FxmlLoader;

public class DorseFxmlLoader extends FxmlLoader {

	private ResourceBundle resuorces;

	private FXMLLoader nativeFxmlLoader = new FXMLLoader();

	public DorseFxmlLoader(ResourceBundle resources) {
		super();
		this.resuorces = resources;
		nativeFxmlLoader.setResources(resuorces);
	}

	public <Type extends Activity<?>> Type load(Class<?> clazz) throws FxmlLoadException {
		return super.load(parseClassToFxmlName(clazz), resuorces);
	}

	public Node loadView(Class<?> clazz) {
		InputStream fxmlStream = null;
		try {
			fxmlStream = getClass().getResourceAsStream(parseClassToFxmlName(clazz));
			return (Node) nativeFxmlLoader.load(fxmlStream);
		} catch (IOException e) {
			throw new FxmlLoadException(String.format("Unable to load FXML from '%s': %s", clazz.getName(), e.getMessage()), e);
		} finally {
			IOUtils.closeQuietly(fxmlStream);
		}
	}

	private String parseClassToFxmlName(Class<?> clazz) {
		return clazz.getName().replaceAll("\\.", "/") + ".fxml";
	}
}

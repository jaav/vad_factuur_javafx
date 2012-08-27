package be.virtualsushi.jfx.dorse.fxml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.zenjava.jfxflow.actvity.Activity;
import com.zenjava.jfxflow.actvity.FxmlLoadException;
import com.zenjava.jfxflow.actvity.FxmlLoader;

@Component
public class DorseFxmlLoader extends FxmlLoader {

	@Autowired
	private ResourceBundle resources;

	@Autowired
	private ApplicationContext applicationContext;

	public DorseFxmlLoader() {
		super();
	}

	public <Type extends Activity<?>> Type loadActivity(Class<?> clazz) throws FxmlLoadException {
		Type result = super.load(parseClassToFxmlName(clazz), resources);
		if (result != null) {
			applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(result, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
		}
		return result;
	}

	public Node bindView(AppUiComponent component) {
		InputStream fxmlStream = null;
		Class<?> clazz = component.getClass();
		try {
			fxmlStream = getClass().getResourceAsStream(parseClassToFxmlName(clazz));
			FXMLLoader loader = new FXMLLoader();
			if (resources != null) {
				loader.setResources(resources);
			}
			loader.setController(component);
			return (Node) loader.load(fxmlStream);
		} catch (IOException e) {
			throw new FxmlLoadException(String.format("Unable to load FXML from '%s': %s", clazz.getName(), e.getMessage()), e);
		} finally {
			IOUtils.closeQuietly(fxmlStream);
		}
	}

	private String parseClassToFxmlName(Class<?> clazz) {
		return "/" + clazz.getName().replaceAll("\\.", "/") + ".fxml";
	}

}

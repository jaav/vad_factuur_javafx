package be.virtualsushi.jfx.dorse.fxml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.events.EventHandler;

import com.google.common.eventbus.EventBus;
import com.zenjava.jfxflow.actvity.FxmlLoadException;

/**
 * Loads ui markup from Fxml.
 * 
 * @author Pavel Sitnikov (van.frga@gmail.com)
 * 
 */
public class PackageBasedUiBinder implements UiBinder {

	@Autowired
	private EventBus eventBus;

	@Autowired
	private ResourceBundle resources;

	@Override
	public Node bind(IUiComponent component) {
		if (component.getClass().getAnnotation(EventHandler.class) != null) {
			eventBus.register(component);
		}
		InputStream fxmlStream = null;
		Class<?> clazz = component.getClass();
		try {
			String fxmlFileName = clazz.getAnnotation(FxmlFile.class) != null ? clazz.getAnnotation(FxmlFile.class).value() : classToFxmlFileName(clazz);
			fxmlStream = clazz.getResourceAsStream(fxmlFileName);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(clazz.getResource(fxmlFileName));
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

	protected String classToFxmlFileName(Class<?> clazz) {
		return clazz.getSimpleName() + ".fxml";
	}

}

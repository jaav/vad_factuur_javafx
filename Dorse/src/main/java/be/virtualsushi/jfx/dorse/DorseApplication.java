package be.virtualsushi.jfx.dorse;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.zenjava.jfxflow.control.Browser;

public class DorseApplication extends Application {

	private AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(DorseApplicationFactory.class);

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		BorderPane root = new BorderPane();
		root.setCenter(applicationContext.getBean(Browser.class));
		Scene scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		scene.getStylesheets().add("style.css");
		stage.show();
		
		RestTemplate r = new RestTemplate();

	}

}

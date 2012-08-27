package be.virtualsushi.jfx.dorse;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_ARTICLES;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_CUSTOMERS;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_INVOICES;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.NEW_ARTICLE;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.NEW_CUSTOMER;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.NEW_INVOICE;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import be.virtualsushi.jfx.dorse.activities.EditCustomerActivity;
import be.virtualsushi.jfx.dorse.fxml.DorseFxmlLoader;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.navigation.AppRegexPlaceResolver;

import com.zenjava.jfxflow.control.Browser;
import com.zenjava.jfxflow.navigation.PlaceResolver;

public class DorseApplication extends Application {

	public static final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(DorseApplicationFactory.class);

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Browser browser = new Browser(applicationContext.getBean(ActivityNavigator.class), "VAD Factuur");
		browser.setHeader(createMenu(applicationContext.getBean(MenuFactory.class)));
		mapActivities(browser.getPlaceResolvers(), applicationContext.getBean(DorseFxmlLoader.class));
		BorderPane root = new BorderPane();
		root.setCenter(browser);
		Scene scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		scene.getStylesheets().add("style.css");
		stage.show();

	}

	private Node createMenu(MenuFactory factory) {
		MenuBar menuBar = new MenuBar();

		Menu menuVadFactuur = factory.createMenu("vad.factuur", "about", null);

		Menu menuObject = factory.createMenu("object", factory.createMenu("new", "invoice", NEW_INVOICE, "customer", NEW_CUSTOMER, "article", NEW_ARTICLE),
				factory.createMenu("list", "invoice", LIST_INVOICES, "customer", LIST_CUSTOMERS, "article", LIST_ARTICLES));

		menuBar.getMenus().addAll(menuVadFactuur, menuObject);

		return menuBar;
	}

	private void mapActivities(ObservableList<PlaceResolver> placeResolvers, DorseFxmlLoader loader) {
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.NEW_CUSTOMER, loader.loadActivity(EditCustomerActivity.class)));
	}

}

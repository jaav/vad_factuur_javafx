package be.virtualsushi.jfx.dorse;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.zenjava.jfxflow.control.Browser;
import com.zenjava.jfxflow.navigation.DefaultNavigationManager;
import com.zenjava.jfxflow.navigation.PlaceResolver;

public class DorseApplication extends Application {

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		DefaultNavigationManager navigationManager = new DefaultNavigationManager();

		ResourceBundle resources = ResourceBundle.getBundle("messages");

		Browser browser = new Browser(navigationManager);
		browser.setHeader(createMenu(resources));
		mapActivities(browser.getPlaceResolvers());

		BorderPane root = new BorderPane();
		root.setCenter(browser);
		Scene scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		scene.getStylesheets().add("style.css");
		stage.show();

	}

	private void mapActivities(ObservableList<PlaceResolver> placeResolvers) {

	}

	private Node createMenu(ResourceBundle resources) {

		MenuBar menuBar = new MenuBar();

		Menu menuVadFactuur = new Menu(resources.getString("vad.factuur"));

		MenuItem aboutItem = new MenuItem(resources.getString("about"));
		menuVadFactuur.getItems().add(aboutItem);

		Menu menuObject = new Menu(resources.getString("object"));

		Menu newItem = new Menu(resources.getString("new"));
		MenuItem newInvoiceItem = new MenuItem(resources.getString("invoice"));
		MenuItem newCustomer = new MenuItem(resources.getString("customer"));
		MenuItem newArticle = new MenuItem(resources.getString("article"));
		newItem.getItems().addAll(newInvoiceItem, newCustomer, newArticle);

		Menu listItem = new Menu(resources.getString("list"));
		MenuItem listInvoiceItem = new MenuItem(resources.getString("invoice"));
		MenuItem listCustomer = new MenuItem(resources.getString("customer"));
		MenuItem listArticle = new MenuItem(resources.getString("article"));
		listItem.getItems().addAll(listInvoiceItem, listCustomer, listArticle);

		menuObject.getItems().addAll(newItem, listItem);

		menuBar.getMenus().addAll(menuVadFactuur, menuObject);

		return menuBar;
	}

}

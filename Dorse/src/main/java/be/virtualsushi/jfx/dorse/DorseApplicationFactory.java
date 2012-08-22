package be.virtualsushi.jfx.dorse;

import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import be.virtualsushi.jfx.dorse.dialogs.LoginDialog;

import com.zenjava.jfxflow.control.Browser;
import com.zenjava.jfxflow.navigation.DefaultNavigationManager;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.PlaceResolver;

@Configuration
public class DorseApplicationFactory {

	private DorseFxmlLoader fxmlLoader = new DorseFxmlLoader(ResourceBundle.getBundle("messages"));

	@Bean
	public Browser getBrowser() {
		Browser browser = new Browser(getNavigationManager(), "VAD Factuur");
		browser.setHeader(createMenu(getResources()));
		mapActivities(browser.getPlaceResolvers());
		return browser;
	}

	private Node createMenu(ResourceBundle resources) {
		MenuBar menuBar = new MenuBar();

		Menu menuVadFactuur = new Menu(resources.getString("vad.factuur"));

		MenuItem aboutItem = new MenuItem(resources.getString("about"));
		menuVadFactuur.getItems().add(aboutItem);

		Menu menuObject = new Menu(resources.getString("object"));

		Menu newItem = new Menu(resources.getString("new"));
		MenuItem newInvoiceItem = new MenuItem(resources.getString("invoice"));
		newInvoiceItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

			}
		});

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

	private void mapActivities(ObservableList<PlaceResolver> placeResolvers) {

	}

	@Bean
	public NavigationManager getNavigationManager() {
		return new DefaultNavigationManager();
	}

	@Bean
	public ResourceBundle getResources() {
		return ResourceBundle.getBundle("messages");
	}

	@Bean
	public LoginDialog getLoginDialog() {
		return new LoginDialog(fxmlLoader, getResources());
	}

}

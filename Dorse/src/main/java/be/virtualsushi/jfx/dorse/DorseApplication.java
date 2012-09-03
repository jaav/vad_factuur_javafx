package be.virtualsushi.jfx.dorse;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_ARTICLES;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_CUSTOMERS;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_INVOICES;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.EDIT_ARTICLE;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.EDIT_CUSTOMER;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.EDIT_INVOICE;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import be.virtualsushi.jfx.dorse.activities.EditArticleActivity;
import be.virtualsushi.jfx.dorse.activities.EditCustomerActivity;
import be.virtualsushi.jfx.dorse.dialogs.LoginDialog;
import be.virtualsushi.jfx.dorse.events.authentication.LoginEvent;
import be.virtualsushi.jfx.dorse.events.authentication.SessionExpiredEvent;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.navigation.AppRegexPlaceResolver;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zenjava.jfxflow.control.Browser;
import com.zenjava.jfxflow.dialog.Dialog;
import com.zenjava.jfxflow.navigation.PlaceResolver;

public class DorseApplication extends Application {

	public static final String LOGIN_DIALOG_TITLE_KEY = "login.dialog.title";

	public static final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(DorseApplicationFactory.class);

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	private Dialog loginDialog;
	private Browser browser;
	private RestApiAccessor restApiAccessor;

	@Override
	public void start(Stage stage) throws Exception {

		browser = new Browser(applicationContext.getBean(ActivityNavigator.class), "VAD Factuur");
		browser.setHeader(createMenu(applicationContext.getBean(MenuFactory.class)));
		mapActivities(browser.getPlaceResolvers());

		BorderPane rootNode = new BorderPane();

		initializeAuthenticationManagement();

		rootNode.setCenter(browser);
		Scene scene = new Scene(rootNode, 800, 600);
		stage.setScene(scene);
		scene.getStylesheets().add("style.css");
		stage.show();

	}

	private void initializeAuthenticationManagement() {
		applicationContext.getBean(EventBus.class).register(this);
		restApiAccessor = applicationContext.getBean(RestApiAccessor.class);
		loginDialog = new Dialog(applicationContext.getBean(ResourceBundle.class).getString(LOGIN_DIALOG_TITLE_KEY));
		loginDialog.setContent(applicationContext.getBean(LoginDialog.class).asNode());
	}

	private Node createMenu(MenuFactory factory) {
		MenuBar menuBar = new MenuBar();

		Menu menuVadFactuur = factory.createMenu("vad.factuur", "about", null);

		Menu menuObject = factory.createMenu("object", factory.createMenu("new", "invoice", EDIT_INVOICE, "customer", EDIT_CUSTOMER, "article", EDIT_ARTICLE),
				factory.createMenu("list", "invoice", LIST_INVOICES, "customer", LIST_CUSTOMERS, "article", LIST_ARTICLES));

		menuBar.getMenus().addAll(menuVadFactuur, menuObject);

		return menuBar;
	}

	private void mapActivities(ObservableList<PlaceResolver> placeResolvers) {
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.EDIT_CUSTOMER, applicationContext.getBean(EditCustomerActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.EDIT_ARTICLE, applicationContext.getBean(EditArticleActivity.class)));
	}

	@Subscribe
	public void onSessionExpired(SessionExpiredEvent event) {
		loginDialog.show(browser);
	}

	@Subscribe
	public void onLogin(LoginEvent event) {
		if (StringUtils.isNotBlank(restApiAccessor.login(event.getUsername(), event.getPassword()))) {
			loginDialog.hide();
		}
	}

}

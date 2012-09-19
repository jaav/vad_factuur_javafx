package be.virtualsushi.jfx.dorse;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.EDIT_ARTICLE;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.EDIT_CUSTOMER;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.EDIT_INVOICE;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_ARTICLES;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_CUSTOMERS;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_INVOICES;

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

import be.virtualsushi.jfx.dorse.activities.CustomerListActivity;
import be.virtualsushi.jfx.dorse.activities.EditArticleActivity;
import be.virtualsushi.jfx.dorse.activities.EditCustomerActivity;
import be.virtualsushi.jfx.dorse.activities.EditInvoiceActivity;
import be.virtualsushi.jfx.dorse.activities.ViewArticleActivity;
import be.virtualsushi.jfx.dorse.activities.ViewCustomerActivity;
import be.virtualsushi.jfx.dorse.activities.ViewInvoiceActivity;
import be.virtualsushi.jfx.dorse.control.DialogPopup;
import be.virtualsushi.jfx.dorse.control.LoadingMask;
import be.virtualsushi.jfx.dorse.dialogs.AbstractDialog;
import be.virtualsushi.jfx.dorse.dialogs.LoginDialog;
import be.virtualsushi.jfx.dorse.events.HideDialogEvent;
import be.virtualsushi.jfx.dorse.events.ShowDialogEvent;
import be.virtualsushi.jfx.dorse.events.ShowLoadingMaskEvent;
import be.virtualsushi.jfx.dorse.events.authentication.AuthorizationRequiredEvent;
import be.virtualsushi.jfx.dorse.events.authentication.LoginSuccessfulEvent;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.navigation.AppRegexPlaceResolver;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zenjava.jfxflow.control.Browser;
import com.zenjava.jfxflow.dialog.Dialog;
import com.zenjava.jfxflow.navigation.PlaceResolver;

public class DorseApplication extends Application {

	public static final String LOGIN_DIALOG_TITLE_KEY = "login.dialog.title";

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	private Dialog loginDialog;
	private Dialog customDialog;
	private LoadingMask loadingMask;
	private Dialog currentlyShowingDialog;
	private Browser browser;
	private AnnotationConfigApplicationContext applicationContext;

	@Override
	public void start(Stage stage) throws Exception {

		applicationContext = new AnnotationConfigApplicationContext(DorseApplicationFactory.class);
		browser = new Browser(applicationContext.getBean(ActivityNavigator.class), "VAD Factuur");
		browser.setHeader(createMenu(applicationContext.getBean(MenuFactory.class)));
		mapActivities(browser.getPlaceResolvers());

		BorderPane rootNode = new BorderPane();

		initializeDialogs();

		rootNode.setCenter(browser);
		Scene scene = new Scene(rootNode, 800, 600);
		stage.setScene(scene);
		scene.getStylesheets().add("style.css");
		stage.show();

	}

	private void initializeDialogs() {
		applicationContext.getBean(EventBus.class).register(this);
		ResourceBundle resources = applicationContext.getBean(ResourceBundle.class);
		loginDialog = new DialogPopup(resources.getString(LOGIN_DIALOG_TITLE_KEY), false, true);
		loginDialog.setContent(applicationContext.getBean(LoginDialog.class).asNode());
		loadingMask = new LoadingMask(resources);
		customDialog = new DialogPopup();
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
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.VIEW_ARTICLE, applicationContext.getBean(ViewArticleActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.EDIT_INVOICE, applicationContext.getBean(EditInvoiceActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.VIEW_CUSTOMER, applicationContext.getBean(ViewCustomerActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.VIEW_INVOICE, applicationContext.getBean(ViewInvoiceActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.LIST_CUSTOMERS, applicationContext.getBean(CustomerListActivity.class)));
	}

	@Subscribe
	public void onAuthorizationRequired(AuthorizationRequiredEvent event) {
		showDialog(loginDialog);
	}

	@Subscribe
	public void onLogin(LoginSuccessfulEvent event) {
		if (StringUtils.isNotBlank(event.getAuthToken())) {
			hideCurrentlyShowingDialog();
		}
	}

	@Subscribe
	public void onShowDialog(ShowDialogEvent event) {
		customDialog.setTitle(event.getDialogTitle());
		AbstractDialog dialogContent = applicationContext.getBean(event.getDialogControllerClass());
		customDialog.setContent(dialogContent.asNode());
		showDialog(customDialog);
		dialogContent.onShow(event.getParameters());
	}

	@Subscribe
	public void onShowLoadingMask(ShowLoadingMaskEvent event) {
		showDialog(loadingMask);
	}

	@Subscribe
	public void onHideDiloag(HideDialogEvent event) {
		hideCurrentlyShowingDialog();
	}

	private void showDialog(Dialog dialogToShow) {
		if (currentlyShowingDialog != null) {
			currentlyShowingDialog.hide();
		}
		dialogToShow.show(browser);
		currentlyShowingDialog = dialogToShow;
	}

	private void hideCurrentlyShowingDialog() {
		if (currentlyShowingDialog != null) {
			currentlyShowingDialog.hide();
			currentlyShowingDialog = null;
		}
	}
}

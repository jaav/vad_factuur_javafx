package be.virtualsushi.jfx.dorse;

import java.util.ResourceBundle;

import be.virtualsushi.jfx.dorse.activities.*;
import be.virtualsushi.jfx.dorse.dialogs.AbstractFilterDialog;
import be.virtualsushi.jfx.dorse.events.ShowFilterDialogEvent;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.ServerResponse;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;
import be.virtualsushi.jfx.dorse.utils.AppVariables;
import com.zenjava.jfxflow.navigation.DefaultNavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import be.virtualsushi.jfx.dorse.control.DialogPopup;
import be.virtualsushi.jfx.dorse.control.LoadingMask;
import be.virtualsushi.jfx.dorse.dialogs.AbstractDialog;
import be.virtualsushi.jfx.dorse.dialogs.LoginDialog;
import be.virtualsushi.jfx.dorse.events.HideDialogEvent;
import be.virtualsushi.jfx.dorse.events.ShowDialogEvent;
import be.virtualsushi.jfx.dorse.events.ShowLoadingMaskEvent;
import be.virtualsushi.jfx.dorse.events.authentication.AuthorizationRequiredEvent;
import be.virtualsushi.jfx.dorse.events.authentication.LoginSuccessfulEvent;
import be.virtualsushi.jfx.dorse.events.report.ShowReportEvent;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.navigation.AppRegexPlaceResolver;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zenjava.jfxflow.control.Browser;
import com.zenjava.jfxflow.dialog.Dialog;
import com.zenjava.jfxflow.navigation.PlaceResolver;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.*;

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
    ActivityNavigator activityNavigator = (ActivityNavigator)applicationContext.getBean(ActivityNavigator.class);
		browser = new Browser(activityNavigator, "VAD Factuur");
		browser.setHeader(createMenu(applicationContext.getBean(MenuFactory.class)));
		mapActivities(browser.getPlaceResolvers());
		BorderPane rootNode = new BorderPane();
    activityNavigator.goTo(HOME);
		initializeDialogs();
		rootNode.setCenter(browser);
		Scene scene = new Scene(rootNode, 1200, 800);
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

		Menu menuVadFactuur = factory.createMenu("VADFactuur", "about", HOME, "logout", LOGOUT);

		Menu invoiceObject = factory.createMenu("Invoices", "new", EDIT_INVOICE, "list", LIST_INVOICES);

    Menu customerObject = factory.createMenu("Customers", "new", EDIT_CUSTOMER, "list", LIST_CUSTOMERS);

    Menu articleObject = factory.createMenu("Articles", "new", EDIT_ARTICLE, "list", LIST_ARTICLES);

		menuBar.getMenus().addAll(menuVadFactuur, invoiceObject, customerObject, articleObject);

		return menuBar;
	}

	private void mapActivities(ObservableList<PlaceResolver> placeResolvers) {
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.HOME, applicationContext.getBean(HomeActivity.class)));
    placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.TEST, applicationContext.getBean(TestActivity.class)));
    placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.LOGOUT, applicationContext.getBean(LogoutActivity.class)));
    placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.EDIT_CUSTOMER, applicationContext.getBean(EditCustomerActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.EDIT_ARTICLE, applicationContext.getBean(EditArticleActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.VIEW_ARTICLE, applicationContext.getBean(ViewArticleActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.EDIT_INVOICE, applicationContext.getBean(EditInvoiceActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.VIEW_CUSTOMER, applicationContext.getBean(ViewCustomerActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.VIEW_INVOICE, applicationContext.getBean(ViewInvoiceActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.LIST_CUSTOMERS, applicationContext.getBean(CustomerListActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.LIST_ARTICLES, applicationContext.getBean(ArticleListActivity.class)));
		placeResolvers.add(new AppRegexPlaceResolver(AppActivitiesNames.LIST_INVOICES, applicationContext.getBean(InvoiceListActivity.class)));
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
 	public void onShowFilterDialog(ShowFilterDialogEvent event) {
 		customDialog.setTitle(event.getDialogTitle());
 		AbstractFilterDialog dialogContent = applicationContext.getBean(event.getFilterDialogControllerClass());
 		customDialog.setContent(dialogContent.asNode());
 		showDialog(customDialog);
 		dialogContent.onShow();
 	}

	@Subscribe
	public void onShowLoadingMask(ShowLoadingMaskEvent event) {
		showDialog(loadingMask);
	}

	@Subscribe
	public void onHideDiloag(HideDialogEvent event) {
		hideCurrentlyShowingDialog();
	}

	@Subscribe
	public void onShowReport(ShowReportEvent event) {
		getHostServices().showDocument("file://" + event.getReportUri());
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

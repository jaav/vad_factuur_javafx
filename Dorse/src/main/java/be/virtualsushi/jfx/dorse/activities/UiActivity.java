package be.virtualsushi.jfx.dorse.activities;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.util.Duration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import be.virtualsushi.jfx.dorse.control.DialogPopup;
import be.virtualsushi.jfx.dorse.control.LoadingMask;
import be.virtualsushi.jfx.dorse.dialogs.AbstractDialog;
import be.virtualsushi.jfx.dorse.events.CancelDialogEvent;
import be.virtualsushi.jfx.dorse.fxml.IUiComponent;
import be.virtualsushi.jfx.dorse.fxml.UiBinder;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zenjava.jfxflow.actvity.AbstractActivity;
import com.zenjava.jfxflow.actvity.SimpleView;
import com.zenjava.jfxflow.dialog.Dialog;
import com.zenjava.jfxflow.transition.FlyTransition;
import com.zenjava.jfxflow.transition.HasEntryTransition;
import com.zenjava.jfxflow.transition.HasExitTransition;
import com.zenjava.jfxflow.transition.HorizontalPosition;
import com.zenjava.jfxflow.transition.ViewTransition;

/**
 * Basic class for all app's activities. Manages some activity lifecycle like if
 * activity have been just created or have came from stack.
 * 
 * @author Pavel Stinikov (van.frag@gmail.com)
 * 
 * @param <V>
 *            - Root node type.
 */
public class UiActivity<V extends Node> extends AbstractActivity<SimpleView<V>> implements HasEntryTransition, HasExitTransition, IUiComponent {

	private ActivityNavigator activityNavigator;

	private RestApiAccessor restApiAccessor;

	private EventBus eventBus;

	private ResourceBundle resources;

	private ApplicationContext applicationContext;

	private UiBinder uiBinder;

	private boolean isNew = true;

	private Dialog dialog = new DialogPopup();

	private LoadingMask loadingMask;

	private Class<? extends AbstractDialog> currentlyShowingComponent;

	/**
	 * Called any time activity get active.
	 */
	@Override
	public void activated() {
		super.activated();

		if (isNew()) {
			initialize();
			setNew(false);
		}

		getEventBus().register(this);
	}

	@Override
	protected void deactivated() {
		super.deactivated();
		getEventBus().unregister(this);
	}

	/**
	 * Called when activity get active for the first time.
	 */
	public void initialize() {
		loadingMask = new LoadingMask(getResources());
	}

	/**
	 * Additional method to handle Activity state. Called when activity attached
	 * to the {@link Scene}.
	 */
	protected void started() {

	}

	/**
	 * Called when activity detached from the {@link Scene}
	 */
	protected void stopped() {

	}

	/**
	 * Servers getting parameter with specified name from parameters map passed
	 * to activity. If no parameters have been passed or parameter with
	 * specified name could not be found will return default value.
	 * 
	 * @param parameterName
	 *            - name of the parameter to look for.
	 * @param valueClass
	 *            - parameter type.
	 * @param defaultValue
	 *            - default value if nothing were found.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <P extends Object> P getParameter(String parameterName, Class<P> valueClass, P defaultValue) {
		Map<String, Object> parameters = getActivityNavigator().getCurrentPlace().getParameters();
		if (parameters != null && parameters.containsKey(parameterName)) {
			Object result = parameters.get(parameterName);
			if (valueClass.isInstance(result)) {
				return (P) result;
			}
			return defaultValue;
		}
		return defaultValue;
	}

	public ViewTransition getExitTransition() {
		return FlyTransition.createFlyOut(getView().toNode(), Duration.millis(500), getExitSide());
	}

	public ViewTransition getEntryTransition() {
		return FlyTransition.createFlyIn(getView().toNode(), Duration.millis(500), getEntrySide());
	}

	public HorizontalPosition getExitSide() {
		return HorizontalPosition.left;
	}

	public HorizontalPosition getEntrySide() {
		return HorizontalPosition.right;
	}

	public boolean isSequentialTransition() {
		return false;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	protected void showDialog(String dialogTitle, Class<? extends AbstractDialog> componentClass) {
		currentlyShowingComponent = componentClass;
		dialog.setTitle(dialogTitle);
		AbstractDialog dialogContent = applicationContext.getBean(componentClass);
		dialog.setContent(dialogContent.asNode());
		dialog.show(getView().toNode());
		dialogContent.onShow();
	}

	protected void hideDialog(Class<? extends AbstractDialog> componentClass) {
		if (currentlyShowingComponent.equals(componentClass)) {
			dialog.hide();
		}
	}

	protected void showLoadingMask() {
		loadingMask.show(getView().toNode());
	}

	protected void hideLoadingMask() {
		loadingMask.hide();
	}

	public ResourceBundle getResources() {
		return resources;
	}

	@Autowired
	public void setResources(ResourceBundle resources) {
		this.resources = resources;
	}

	/**
	 * Quick jump to another activity.
	 * 
	 * @param name
	 */
	public void goTo(AppActivitiesNames name) {
		getActivityNavigator().goTo(name);
	}

	/**
	 * Quick jump to another activity with parameters provided.
	 * 
	 * 
	 * @param name
	 * @param parameters
	 *            - list of parameters to pass to the activity. format -
	 *            {param1Name, param1Value, param2Name, param2Valus...}.
	 */
	public void goTo(AppActivitiesNames name, Object... parameters) {
		getActivityNavigator().goTo(name, parameters);
	}

	public ActivityNavigator getActivityNavigator() {
		return activityNavigator;
	}

	@Autowired
	public void setActivityNavigator(ActivityNavigator activityNavigator) {
		this.activityNavigator = activityNavigator;
	}

	public RestApiAccessor getRestApiAccessor() {
		return restApiAccessor;
	}

	@Autowired
	public void setRestApiAccessor(RestApiAccessor restApiAccessor) {
		this.restApiAccessor = restApiAccessor;
	}

	@SuppressWarnings("unchecked")
	@Override
	@PostConstruct
	public void bindUi() {
		V node = (V) uiBinder.bind(this);
		node.sceneProperty().addListener(new ChangeListener<Scene>() {

			@Override
			public void changed(ObservableValue<? extends Scene> source, Scene oldScene, Scene newScene) {
				if (oldScene != null) {
					stopped();
				} else {
					started();
				}
			}
		});
		setView(new SimpleView<V>(node));
	}

	@Override
	public Node asNode() {
		throw new IllegalStateException("Activity can't be presented as node.");
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	@Autowired
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public UiBinder getUiBinder() {
		return uiBinder;
	}

	@Autowired
	public void setUiBinder(UiBinder uiBinder) {
		this.uiBinder = uiBinder;
	}

	@Subscribe
	public void onCancelDialog(CancelDialogEvent event) {
		hideDialog(event.getDialogClass());
	}
}

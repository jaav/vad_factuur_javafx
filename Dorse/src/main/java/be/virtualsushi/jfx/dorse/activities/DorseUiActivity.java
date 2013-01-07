package be.virtualsushi.jfx.dorse.activities;

import java.util.*;

import be.virtualsushi.jfx.dorse.dialogs.AbstractFilterDialog;
import be.virtualsushi.jfx.dorse.events.*;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.model.Status;
import be.virtualsushi.jfx.dorse.utils.AppVariables;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.util.Duration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import be.virtualsushi.jfx.dorse.dialogs.AbstractDialog;
import be.virtualsushi.jfx.dorse.events.authentication.LoginSuccessfulEvent;
import be.virtualsushi.jfx.dorse.fxml.IUiComponent;
import be.virtualsushi.jfx.dorse.fxml.UiBinder;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.zenjava.jfxflow.actvity.AbstractActivity;
import com.zenjava.jfxflow.actvity.SimpleView;
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
public abstract class DorseUiActivity<V extends Node> extends AbstractActivity<SimpleView<V>> implements HasEntryTransition, HasExitTransition, IUiComponent {

	private ActivityNavigator activityNavigator;

	private RestApiAccessor restApiAccessor;

	private EventBus eventBus;

	private ResourceBundle resources;

	private ApplicationContext applicationContext;

	private UiBinder uiBinder;

	private boolean isNew = true;

	private TaskCreator<?> pendingTaskCreator;

  private AppVariables appVariables;

  public static final String AUTHTOKEN_KEY = "authToken";

  public static final String USERNAME_KEY = "username";

  public static final String USERNAME_ID = "user_id";

  public static final String SHOULDREFRESHARTICLES = "should_refresh_articles";

  public static final String SHOULDREFRESHCUSTOMERS = "should_refresh_customers";

  public List<Status> statuses;

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

	protected void showDialog(String dialogTitle, Class<? extends AbstractDialog> componentClass, Object... parameters) {
		getEventBus().post(new ShowDialogEvent(dialogTitle, componentClass, parameters));
	}

  protected void showFilterDialog(String dialogTitle, Class<? extends AbstractFilterDialog> componentClass, Object... parameters) {
 		getEventBus().post(new ShowFilterDialogEvent(dialogTitle, componentClass, parameters));
 	}

	protected void hideDialog(Class<? extends AbstractDialog> componentClass) {
		getEventBus().post(new HideDialogEvent());
	}

  protected void hideFilterDialog() {
 		getEventBus().post(new HideFilterDialogEvent());
 	}

	protected void showLoadingMask() {
		getEventBus().post(new ShowLoadingMaskEvent());
	}

	protected void hideLoadingMask() {
		getEventBus().post(new HideDialogEvent());
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

  public AppVariables getAppVariables() {
 		return appVariables;
 	}

 	@Autowired
 	public void setAppVariables(AppVariables appVariables) {
 		this.appVariables = appVariables;
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

	@Subscribe
	public void onLoginSuccessful(LoginSuccessfulEvent event) {
		if (pendingTaskCreator != null) {
			doTaskExecution(pendingTaskCreator.createTask());
		}
	}

	protected void doInBackground(final TaskCreator<?> taskCreator) {
		pendingTaskCreator = taskCreator;
		doTaskExecution(pendingTaskCreator.createTask());
	}

	private void doTaskExecution(DorseBackgroundTask<?> task) {
		task.setEventBus(eventBus);
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				pendingTaskCreator = null;
			}
		});
		executeTask(task);
	}

  public List<Status> getStatuses(){
    hasStatuses();
    return statuses;
  }

  public Status getStatus(int id){
    hasStatuses();
    for (Status status : statuses) {
      if(status.getId()==id) return status;
    }
    return null;
  }

  private void hasStatuses(){
    if(statuses==null){
      statuses = new ArrayList<Status>();
      statuses.add(Status.getEmptyStatus());
      statuses.add(new Status(1, Status.PREPARED));
      statuses.add(new Status(2, Status.SENT));
      statuses.add(new Status(3, Status.INVOICED));
      statuses.add(new Status(4, Status.REMINDED));
      statuses.add(new Status(5, Status.PAID));
    }
  }

  public boolean shouldReloadCustomers() {
    return (Boolean)getAppVariables().get(SHOULDREFRESHCUSTOMERS);
  }

  public void setReloadCustomers(boolean reloadCustomers) {
    getAppVariables().put(SHOULDREFRESHCUSTOMERS, reloadCustomers);
  }

  public boolean shouldReloadArticles() {
    return (Boolean)getAppVariables().get(SHOULDREFRESHARTICLES);
  }

  public void setReloadArticles(boolean reloadArticles) {
    getAppVariables().put(SHOULDREFRESHARTICLES, reloadArticles);
  }
}

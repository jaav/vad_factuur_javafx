package be.virtualsushi.jfx.dorse.activities;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.util.Duration;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.dialogs.LoginDialog;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;

import com.zenjava.jfxflow.actvity.AbstractActivity;
import com.zenjava.jfxflow.actvity.SimpleView;
import com.zenjava.jfxflow.transition.FlyTransition;
import com.zenjava.jfxflow.transition.HasEntryTransition;
import com.zenjava.jfxflow.transition.HasExitTransition;
import com.zenjava.jfxflow.transition.HorizontalPosition;
import com.zenjava.jfxflow.transition.ViewTransition;

/**
 * Basic class for all app's activities. Stores {@link DaoManager} instance to
 * access data layer and {@link ResourceBundle} for resources, also manages some
 * activity lifecycle like if activity have been just created or have came from
 * stack.
 * 
 * @author Pavel Stinikov (van.frag@gmail.com)
 * 
 * @param <V>
 *            - JFX Flow stuff seems that it's needed to construct activity's
 *            view.
 */
public class BasicAppActivity<V extends Node> extends AbstractActivity<SimpleView<V>> implements HasEntryTransition, HasExitTransition {

	private ActivityNavigator activityNavigator;

	private RestApiAccessor restApiAccessor;

	private LoginDialog loginDialog;

	private ResourceBundle resources;

	private boolean isNew = true;

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
	}

	/**
	 * Called when activity get active for the first time.
	 */
	public void initialize() {

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
		return HorizontalPosition.right;
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

	public void showLoginDialog() {
		loginDialog.show(getView().toNode());
	}

	@Autowired
	public ResourceBundle getResources() {
		return resources;
	}

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

	@Autowired
	public ActivityNavigator getActivityNavigator() {
		return activityNavigator;
	}

	public void setActivityNavigator(ActivityNavigator activityNavigator) {
		this.activityNavigator = activityNavigator;
	}

	@Autowired
	public RestApiAccessor getRestApiAccessor() {
		return restApiAccessor;
	}

	public void setRestApiAccessor(RestApiAccessor restApiAccessor) {
		this.restApiAccessor = restApiAccessor;
	}

	@Autowired
	public LoginDialog getLoginDialog() {
		return loginDialog;
	}

	public void setLoginDialog(LoginDialog loginDialog) {
		this.loginDialog = loginDialog;
	}
}

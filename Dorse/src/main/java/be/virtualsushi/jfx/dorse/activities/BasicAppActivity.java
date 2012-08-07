package be.virtualsushi.jfx.dorse.activities;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.util.Duration;
import be.virtualsushi.jfx.dorse.AppActivitiesNames;

import com.zenjava.jfxflow.actvity.AbstractActivity;
import com.zenjava.jfxflow.actvity.View;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.PlaceBuilder;
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
public class BasicAppActivity<V extends View<?>> extends AbstractActivity<V> implements HasEntryTransition, HasExitTransition {

	private NavigationManager navigationManager;
	private ResourceBundle resources;
	private boolean isNew = true;

	/**
	 * {@link NavigationManager} serves navigation between app's activities.
	 * 
	 * @return
	 */
	public NavigationManager getNavigationManager() {
		return navigationManager;
	}

	public void setNavigationManager(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
	}

	/**
	 * Called any time activity get active.
	 */
	@Override
	protected void activated() {
		super.activated();

		if (isNew()) {
			initialize();
			setNew(false);
		}
	}

	/**
	 * Called when activity get active for the first time.
	 */
	protected void initialize() {

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
	protected <P extends Object> P getParameter(String parameterName, Class<P> valueClass, P defaultValue) {
		Map<String, Object> parameters = navigationManager.getCurrentPlace().getParameters();
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

	protected HorizontalPosition getExitSide() {
		return HorizontalPosition.right;
	}

	protected HorizontalPosition getEntrySide() {
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
	protected void goTo(AppActivitiesNames name) {
		getNavigationManager().goTo(new Place(name.name()));
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
	protected void goTo(AppActivitiesNames name, Object... parameters) {
		assert (parameters.length % 2 == 0) : "parameters count should be even.";
		PlaceBuilder placeBuilder = PlaceBuilder.create().name(name.name());
		int index = 0;
		while (index < parameters.length) {
			placeBuilder.parameter((String) parameters[index], parameters[index + 1]);
			index += 2;
		}
		getNavigationManager().goTo(placeBuilder.build());
	}
}

package be.virtualsushi.jfx.dorse.navigation;

import org.springframework.stereotype.Service;

import com.zenjava.jfxflow.navigation.DefaultNavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.PlaceBuilder;

@Service("navigationManager")
public class ActivityNavigatorImpl extends DefaultNavigationManager implements ActivityNavigator {

	/**
	 * Quick jump to another activity.
	 * 
	 * @param name
	 */
	@Override
	public void goTo(AppActivitiesNames name) {
		  goTo(new Place(name.name()));
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
	@Override
	public void goTo(AppActivitiesNames name, Object... parameters) {
		assert (parameters.length % 2 == 0) : "parameters count should be even.";
		PlaceBuilder placeBuilder = new PlaceBuilder(name.name());
		int index = 0;
		while (index < parameters.length) {
			placeBuilder.parameter((String) parameters[index], parameters[index + 1]);
			index += 2;
		}
		goTo(placeBuilder.build());
	}

}

package be.virtualsushi.jfx.dorse.navigation;


import com.zenjava.jfxflow.actvity.Activity;
import com.zenjava.jfxflow.navigation.RegexPlaceResolver;

public class AppRegexPlaceResolver extends RegexPlaceResolver {

	public AppRegexPlaceResolver(AppActivitiesNames name, Activity<?> activity) {
		super(name.name(), activity);
	}

}

package be.virtualsushi.jfx.dorse.navigation;


import com.zenjava.jfxflow.navigation.NavigationManager;

public interface ActivityNavigator extends NavigationManager {
	
	public void goTo(AppActivitiesNames name);
	
	public void goTo(AppActivitiesNames name, Object... parameters);
	
}

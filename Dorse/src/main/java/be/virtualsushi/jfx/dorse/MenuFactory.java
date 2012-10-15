package be.virtualsushi.jfx.dorse;

import java.util.ResourceBundle;

import be.virtualsushi.jfx.dorse.activities.DorseUiActivity;
import be.virtualsushi.jfx.dorse.utils.AppVariables;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;

@Component
public class MenuFactory {

	@Autowired
	private ActivityNavigator activityNavigator;

	@Autowired
  private AppVariables appVariables;

	@Autowired
	private ResourceBundle resources;

	public Menu createMenu(String name, MenuItem... subItems) {
		Menu result = new Menu(getTitle(name));
		if (ArrayUtils.isNotEmpty(subItems)) {
			result.getItems().addAll(subItems);
		}
		return result;
	}

	private String getTitle(String name) {
		return resources.containsKey(name) ? resources.getString(name) : name;
	}

	public MenuItem createMenuItem(String name, final AppActivitiesNames activityName) {
		MenuItem result = new MenuItem(getTitle(name));
		if (activityName != null) {
			result.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
          if(appVariables.get(DorseUiActivity.AUTHTOKEN_KEY)==null || appVariables.get(DorseUiActivity.USERNAME_KEY)==null)
            activityNavigator.goTo(AppActivitiesNames.HOME);
          else
          activityNavigator.goTo(activityName);
				}
			});
		}
		return result;
	}

	public MenuItem createMenuItem(String name) {
		return createMenuItem(name, null);
	}

	public Menu createMenu(String name, Object... subItemsParameters) {
		assert (subItemsParameters == null || subItemsParameters.length % 2 == 0) : "no parameters or parameters count should be even.";
		Menu result = new Menu(getTitle(name));
		if (subItemsParameters == null) {
			return result;
		}
		int i = 0;
		while (i < subItemsParameters.length) {
			String subItemName = (String) subItemsParameters[i];
			if (subItemsParameters[i + 1] != null) {
				result.getItems().add(createMenuItem(subItemName, (AppActivitiesNames) subItemsParameters[i + 1]));
			} else {
				result.getItems().add(createMenuItem(subItemName));
			}
			i += 2;
		}
		return result;
	}
}

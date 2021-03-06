package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 02/10/12
 * Time: 10:27
 */

@Component
@Scope("prototype")
@FxmlFile("TestActivity.fxml")
public class TestActivity extends AbstractBrowserActivity {

  @Override
  protected void doCustomBackgroundInitialization() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  protected void started() {
    super.started();    //To change body of overridden methods use File | Settings | File Templates.
    String authToken = (String)getAppVariables().get("authToken");
    System.out.println("authToken = " + authToken);
  }
}

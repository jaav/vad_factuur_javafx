package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
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
@FxmlFile("HomeActivity.fxml")
public class LogoutActivity extends AbstractBrowserActivity {


  @FXML
 	private TextField usernameField, passwordField;

  @Override
  protected void doCustomBackgroundInitialization() {
    //To change body of implemented methods use File | Settings | File Templates.
  }


  @FXML
 	protected void startTest(ActionEvent event) {
 		goTo(AppActivitiesNames.TEST);
 	}

  @FXML
 	protected void handleLoginAction  (ActionEvent event) {
 		String authToken = getRestApiAccessor().login(usernameField.getText(), passwordField.getText());
 		if (StringUtils.isNotBlank(authToken)) {
       getAppVariables().put(AUTHTOKEN_KEY, authToken);
       getAppVariables().put(USERNAME_KEY, usernameField.getText());
       goTo(AppActivitiesNames.TEST);
 		} else {
 			// TODO display error message
 		}
 	}

  @Override
  public void initialize() {
    super.initialize();    //To change body of overridden methods use File | Settings | File Templates.
    getAppVariables().clear();
  }
}

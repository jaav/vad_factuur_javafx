package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.events.authentication.LoginSuccessfulEvent;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 02/10/12
 * Time: 10:27
 */

@Component
@Scope("prototype")
@FxmlFile("HomeActivity.fxml")
public class HomeActivity extends AbstractBrowserActivity {


  @FXML
 	private TextField usernameField, passwordField;

  @Override
  protected void doCustomBackgroundInitialization() {
//    if(currentArticles==null)
//      currentCustomers = (List<Customer>)getRestApiAccessor().getResponse(Customer.class, Customer.DEFAULT_COLUMN, false, Customer.DEFAULT_ASC).getData();
//    if(currentArticles==null)
//      currentArticles = (List<Article>) getRestApiAccessor().getResponse(Article.class, Article.DEFAULT_COLUMN, true, Article.DEFAULT_ASC).getData();
  }

  @FXML
 	protected void handleLoginAction  (ActionEvent event) {
 		String authToken = getRestApiAccessor().login(usernameField.getText(), passwordField.getText());
 		if (StringUtils.isNotBlank(authToken)) {
       getAppVariables().put(AUTHTOKEN_KEY, authToken.substring(authToken.indexOf("___")+3));
       getAppVariables().put(USERNAME_ID, Long.parseLong(authToken.substring(0, authToken.indexOf("___"))));
       getAppVariables().put(USERNAME_KEY, usernameField.getText());
       getAppVariables().put(SHOULDREFRESHARTICLES, true);
       getAppVariables().put(SHOULDREFRESHCUSTOMERS, true);
       goTo(AppActivitiesNames.TEST);
 		} else {
 			// TODO display error message
 		}
 	}
}

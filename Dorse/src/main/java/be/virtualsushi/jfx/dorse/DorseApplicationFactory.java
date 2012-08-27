package be.virtualsushi.jfx.dorse;

import java.util.ResourceBundle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import be.virtualsushi.jfx.dorse.dialogs.LoginDialog;
import be.virtualsushi.jfx.dorse.restapi.CustomerJsonHttpMessageConverter;
import be.virtualsushi.jfx.dorse.restapi.CustomersListJsonHttpMessageConverter;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;

@Configuration
@ComponentScan(basePackages = { "be.virtualsushi.jfx.dorse" })
public class DorseApplicationFactory {

	@Bean(name = "resources")
	public ResourceBundle getResources() {
		return ResourceBundle.getBundle("messages");
	}

	@Bean
	@Scope("prototype")
	public LoginDialog getLoginDialog() {
		return new LoginDialog(getResources());
	}

	@Bean(name = "restApiAccessor")
	public RestApiAccessor getRestApiAccessor() {
		RestApiAccessor accessor = new RestApiAccessor();
		accessor.getMessageConverters().add(new CustomerJsonHttpMessageConverter());
		accessor.getMessageConverters().add(new CustomersListJsonHttpMessageConverter());
		return new RestApiAccessor();
	}

}

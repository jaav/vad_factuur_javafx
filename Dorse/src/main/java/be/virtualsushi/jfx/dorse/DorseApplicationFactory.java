package be.virtualsushi.jfx.dorse;

import java.util.ResourceBundle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import be.virtualsushi.jfx.dorse.fxml.PackageBasedUiBinder;
import be.virtualsushi.jfx.dorse.fxml.UiBinder;
import be.virtualsushi.jfx.dorse.restapi.CustomerJsonHttpMessageConverter;
import be.virtualsushi.jfx.dorse.restapi.CustomersListJsonHttpMessageConverter;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;

import com.google.common.eventbus.EventBus;

@Configuration
@ComponentScan(basePackages = { "be.virtualsushi.jfx.dorse" })
public class DorseApplicationFactory {

	@Bean(name = "resources")
	public ResourceBundle getResources() {
		return ResourceBundle.getBundle("messages");
	}

	@Bean(name = "restApiAccessor")
	public RestApiAccessor getRestApiAccessor() {
		RestApiAccessor accessor = new RestApiAccessor();
		accessor.getMessageConverters().add(new CustomerJsonHttpMessageConverter());
		accessor.getMessageConverters().add(new CustomersListJsonHttpMessageConverter());
		return new RestApiAccessor();
	}

	@Bean(name = "eventBus")
	public EventBus getEventBus() {
		return new EventBus();
	}

	@Bean(name = "uiBinder")
	public UiBinder getUiBinder() {
		return new PackageBasedUiBinder();
	}

}

package be.virtualsushi.jfx.dorse;

import java.util.ResourceBundle;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import be.virtualsushi.jfx.dorse.fxml.PackageBasedUiBinder;
import be.virtualsushi.jfx.dorse.fxml.UiBinder;
import be.virtualsushi.jfx.dorse.restapi.DorseMappingJacksonHttpMessageConverter;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;

import com.google.common.eventbus.EventBus;

@Configuration
@ComponentScan(basePackages = { "be.virtualsushi.jfx.dorse" })
@PropertySource("classpath:application.properties")
public class DorseApplicationFactory {

	@Bean(name = "resources")
	public ResourceBundle getResources() {
		return ResourceBundle.getBundle("messages");
	}

	@Bean(name = "restApiAccessor")
	public RestApiAccessor getRestApiAccessor() {
		RestApiAccessor accessor = new RestApiAccessor();
		accessor.getMessageConverters().add(new DorseMappingJacksonHttpMessageConverter());
		return accessor;
	}

	@Bean(name = "eventBus")
	public EventBus getEventBus() {
		return new EventBus();
	}

	@Bean(name = "uiBinder")
	public UiBinder getUiBinder() {
		return new PackageBasedUiBinder();
	}

	@Bean(name = "validator")
	public Validator getValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Bean(name = "propertyConfigurer")
	public PropertySourcesPlaceholderConfigurer getPropertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}

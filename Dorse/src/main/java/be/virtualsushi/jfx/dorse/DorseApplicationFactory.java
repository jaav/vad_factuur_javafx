package be.virtualsushi.jfx.dorse;

import java.util.ResourceBundle;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import be.virtualsushi.jfx.dorse.fxml.PackageBasedUiBinder;
import be.virtualsushi.jfx.dorse.fxml.UiBinder;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.ArticleType;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Sector;
import be.virtualsushi.jfx.dorse.model.Supplier;
import be.virtualsushi.jfx.dorse.model.Unit;
import be.virtualsushi.jfx.dorse.restapi.JsonHttpMessageConverter;
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
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Customer>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Customer[]>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Sector>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Sector[]>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Article>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Article[]>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<ArticleType>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<ArticleType[]>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Unit>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Unit[]>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Supplier>());
		accessor.getMessageConverters().add(new JsonHttpMessageConverter<Supplier[]>());
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

	@Bean(name = "validator")
	public Validator getValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

}

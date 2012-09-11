package be.virtualsushi.jfx.dorse.restapi;

import java.util.Collections;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

public class DorseMappingJacksonHttpMessageConverter extends MappingJacksonHttpMessageConverter {

	public DorseMappingJacksonHttpMessageConverter() {
		setSupportedMediaTypes(Collections.singletonList(new MediaType("text", "html", DEFAULT_CHARSET)));
	}

}

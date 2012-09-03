package be.virtualsushi.jfx.dorse.restapi;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class JsonHttpMessageConverter<M> implements HttpMessageConverter<M> {

	public static final MediaType JSON_MEDIA_TYPE = new MediaType("application", "json");

	private ObjectMapper objectMapper;
	private JsonFactory jsonFactory;

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return JSON_MEDIA_TYPE.equals(mediaType) && getSupportedModelClass().equals(clazz);
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return JSON_MEDIA_TYPE.equals(mediaType) && getSupportedModelClass().equals(clazz);
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return Collections.singletonList(JSON_MEDIA_TYPE);
	}

	@Override
	public M read(Class<? extends M> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		return getJsonFactory().createJsonParser(inputMessage.getBody()).readValueAs(getSupportedModelClass());
	}

	@Override
	public void write(M t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		getJsonFactory().createJsonGenerator(outputMessage.getBody(), JsonEncoding.UTF8).writeObject(t);
	}

	@SuppressWarnings("unchecked")
	private Class<M> getSupportedModelClass() {
		return (Class<M>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
		}
		return objectMapper;
	}

	private JsonFactory getJsonFactory() {
		if (jsonFactory == null) {
			jsonFactory = getObjectMapper().getJsonFactory();
		}
		return jsonFactory;
	}

}

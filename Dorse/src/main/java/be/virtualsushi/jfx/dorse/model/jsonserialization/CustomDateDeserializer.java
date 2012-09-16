package be.virtualsushi.jfx.dorse.model.jsonserialization;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class CustomDateDeserializer extends JsonDeserializer<Date> {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyy");

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonToken token = jp.getCurrentToken();
		if (token == JsonToken.VALUE_STRING) {
			try {
				return DATE_FORMAT.parse(jp.getText());
			} catch (ParseException e) {
				throw ctxt.mappingException(Date.class);
			}
		}
		throw ctxt.mappingException(Date.class);
	}

}

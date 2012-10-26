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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomDateDeserializer extends JsonDeserializer<Date> {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd");
	public static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

  private static final Logger log = LoggerFactory.getLogger(CustomDateDeserializer.class);

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonToken token = jp.getCurrentToken();
		if (token == JsonToken.VALUE_STRING) {
			try {
				return DATE_FORMAT.parse(jp.getText());
			} catch (ParseException e) {
				try {
					return FULL_DATE_FORMAT.parse(jp.getText());
				} catch (ParseException e1) {
					return null;
				}
			}
		}
		throw ctxt.mappingException(Date.class);
	}

}

package be.virtualsushi.jfx.dorse.model.jsonserialization;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomFloatDeserializer extends JsonDeserializer<Float> {

	@Override
	public Float deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonToken token = jp.getCurrentToken();
		if (token == JsonToken.VALUE_STRING) return 0F;
		else return Float.parseFloat(jp.getText());
	}

}

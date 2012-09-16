package be.virtualsushi.jfx.dorse.model.jsonserialization;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class CustomDateSerializer extends JsonSerializer<Date> {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyy");

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(DATE_FORMAT.format(value));
	}

}

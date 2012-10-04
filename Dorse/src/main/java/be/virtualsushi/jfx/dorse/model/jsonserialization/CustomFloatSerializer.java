package be.virtualsushi.jfx.dorse.model.jsonserialization;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomFloatSerializer extends JsonSerializer<Float> {

	@Override
	public void serialize(Float value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString("  "+value.toString()+"  ");
	}

}

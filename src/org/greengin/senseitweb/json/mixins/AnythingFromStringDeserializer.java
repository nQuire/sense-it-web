package org.greengin.senseitweb.json.mixins;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;


public class AnythingFromStringDeserializer extends JsonSerializer<String> {
	
	public AnythingFromStringDeserializer() {
		
	}
	
	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException,
			JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory(); // since 2.1 use mapper.getFactory() instead
		JsonParser jp = factory.createJsonParser(value);
		JsonNode json = mapper.readTree(jp);
		
		jsonGenerator.writeTree(json);
	}
	
}
package org.greengin.senseitweb.json.mixins;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;


public class AnythingToStringDeserializer extends JsonDeserializer<String> {
	
	public AnythingToStringDeserializer() {
		
	}
	
	@Override
	public String deserialize(JsonParser jp, DeserializationContext context) throws IOException,
			JsonProcessingException {
		JsonNode node = jp.readValueAsTree();
		return node.toString();
	}
	
}
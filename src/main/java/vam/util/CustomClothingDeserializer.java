package vam.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.extern.slf4j.Slf4j;
import vam.dto.scene.Clothing;

@Slf4j
public class CustomClothingDeserializer extends StdDeserializer<Clothing> {

	public CustomClothingDeserializer() {
		this(null);
	}

	public CustomClothingDeserializer(Class<?> t) {
		super(t);
	}

	@Override
	public Clothing deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String id;
		Boolean enabled;
		try {
			JsonNode node = jp.getCodec().readTree(jp);
			id = node.get("id").asText();
			enabled = node.get("enabled").asBoolean();
			return new Clothing(id, enabled);
		} catch (NullPointerException ex) {
			return new Clothing("", true);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
}
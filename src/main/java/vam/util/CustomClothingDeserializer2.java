package vam.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.extern.slf4j.Slf4j;
import vam.dto.meta.Dependence;

@Slf4j
public class CustomClothingDeserializer2 extends StdDeserializer<Map<String, Dependence>> {

	public CustomClothingDeserializer2() {
		this(null);
	}

	public CustomClothingDeserializer2(Class<?> t) {
		super(t);
	}

	@Override
	public Map<String, Dependence> deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		try {
			JsonNode node = jp.getCodec().readTree(jp);
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Dependence> result = mapper.convertValue(node, new TypeReference<Map<String, Dependence>>() {
			});
			return result;
		} catch (NullPointerException ex) {
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
}
package vam.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;
import vam.dto.meta.Dependence;

@Slf4j
public class MyCustomDeserializer2 extends JsonDeserializer<Map<String, Dependence>> {

	@Override
	public Map<String, Dependence> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException {
		String ss = "";
		JsonToken jsonToken = null;
		JsonToken nextToken = null;
		jsonToken = jsonParser.currentToken();
		if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
			List<String> histories = new ArrayList<>();
			nextToken = jsonParser.nextToken();
			while (jsonParser.hasCurrentToken() && jsonParser.currentToken() != JsonToken.END_ARRAY) {
				histories.add(jsonParser.getValueAsString());
				try {
					nextToken = jsonParser.nextToken();
				} catch (JsonParseException ex) {
					String acceptError = "Unexpected character (']' (code 93)):";
					if (StringUtils.startsWith(ex.getMessage(), acceptError)) {
						break;
					}
					System.out.println(ex.getMessage());
				} catch (Exception ex) {

				}
			}
			return null;
		} else {
			System.out.println(jsonParser.nextToken());
		}
		return null;
	}
}
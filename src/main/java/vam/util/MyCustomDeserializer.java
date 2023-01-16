package vam.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyCustomDeserializer extends JsonDeserializer<List<String>> {

//	public MyCustomDeserializer(Class<?> vc) {
//		super(vc);
//	}
//
//	public MyCustomDeserializer(Class<?> vc) {
//		super(vc);
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public List<String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException {
		String ss = "";
		JsonToken jsonToken = null;
		JsonToken nextToken = null;
		jsonToken = jsonParser.currentToken();
		if (jsonParser.currentToken() == JsonToken.START_ARRAY) {
			List<String> histories = new ArrayList<>();
			nextToken = jsonParser.nextToken();
			while (jsonParser.hasCurrentToken() && jsonParser.currentToken() != JsonToken.END_ARRAY) {
				histories.add(jsonParser.getValueAsString());
				try {
					jsonParser.nextToken();
				} catch (JsonParseException ex) {
					String acceptError = "Unexpected character ('";
					if (StringUtils.startsWith(ex.getMessage(), acceptError)) {
						System.out.println(ex.getMessage());
						break;
					}
					System.out.println(ex.getMessage());
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
			return histories;
		} else {
			System.out.println(jsonParser.nextToken());
		}
		return null;
	}
}
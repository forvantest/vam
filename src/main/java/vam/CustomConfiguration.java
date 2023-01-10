package vam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import vam.dto.scene.Clothing;
import vam.util.CustomClothingDeserializer;

@Configuration
public class CustomConfiguration {

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("CustomClothingSerializer", new Version(1, 0, 0, null, null, null));
		module.addDeserializer(Clothing.class, new CustomClothingDeserializer());

//		JavaType customClassCollection = objectMapper.getTypeFactory().constructCollectionType(List.class,
//				String.class);
//		module.addDeserializer(customClassCollections, new MyCustomDeserializer());

//		module.addDeserializer(List.class, new MyCustomDeserializer2(String.class));

		objectMapper.registerModule(module);
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		return objectMapper;
	}
}
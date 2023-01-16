package vam.dto.meta;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dependence {
	String licenseType;
	String missing;
	Map<String, Dependence> dependencies;

}

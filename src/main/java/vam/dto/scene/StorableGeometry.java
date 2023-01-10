package vam.dto.scene;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StorableGeometry {
	String id;
	String character;
	List<Clothing> clothing;
	List<Hair> hair;
	List<Morph> morphs;
}

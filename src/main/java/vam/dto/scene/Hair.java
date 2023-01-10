package vam.dto.scene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hair extends Clothing {

	public Hair() {
		super();
	}

	public Hair(String str) {
		super("", true);
	}

	public Hair(String id, Boolean enabled) {
		super(id, enabled);
	}
}

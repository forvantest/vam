package vam.dto.scene;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Clothing {
	String id;

//	@JsonIgnore
//	String internalId;

	Boolean enabled;

	public String getVarName() {
		int endIdx = StringUtils.indexOf(id, ":");
		if (0 < endIdx) {
			String varName = StringUtils.substring(id, 0, endIdx);
			return varName;
		} else
			return null;
	}

	public Clothing() {
	}

	public Clothing(String id, Boolean enabled) {
		this.id = id;
		this.enabled = enabled;
	}
}

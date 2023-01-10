package vam.dto.scene;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Morph {
	String uid;
	String name;
	Double value;

	public String getVarName() {
		int endIdx = StringUtils.indexOf(uid, "/");
		if (0 < endIdx) {
			String varName = StringUtils.substring(uid, 0, endIdx);
			return varName;
		} else
			return null;
	}
}

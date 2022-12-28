package vam.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;
import lombok.NoArgsConstructor;
import vam.dto.meta.CustomOption;
import vam.dto.meta.ReferenceIssue;

@Data
@NoArgsConstructor
public class MetaJson {
	String licenseType;
	String creatorName;
	String packageName;
	String standardReferenceVersionOption;
	String scriptReferenceVersionOption;
	String description;
	String credits;
	String instructions;
	String promotionalLink;
	String programVersion;
	List<String> contentList;

	@JsonProperty("EAEndYear")
	String EAEndYear;
	@JsonProperty("EAEndMonth")
	String EAEndMonth;
	@JsonProperty("EAEndDay")
	String EAEndDay;

	String secondaryLicenseType;

	@JsonProperty("dependencies")
	ObjectNode dependencies;

	CustomOption customOptions;

	Boolean hadReferenceIssues;
	Boolean includeVersionsInReferences;

	List<ReferenceIssue> referenceIssues;

	public MetaJson(String varKey, String licenseType, JsonNode jf) {
		this.licenseType = licenseType;
		this.dependencies = jf.deepCopy();
	}

	public Map<String, MetaJson> getDependenciesMap() {
		Map<String, MetaJson> metaJsonMap = new LinkedHashMap<>();
		if(Objects.isNull(dependencies))
			return metaJsonMap;
		
		dependencies.fields().forEachRemaining(e -> {
			String varKey = e.getKey();
			String licenseType = e.getValue().get("licenseType").asText();
			JsonNode jf = e.getValue().get("dependencies");
			metaJsonMap.put(varKey, new MetaJson(varKey, licenseType, jf));
		});
		return metaJsonMap;
	}

}

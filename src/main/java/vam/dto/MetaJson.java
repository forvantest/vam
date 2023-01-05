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
	private String licenseType;
	private String creatorName;
	private String packageName;
	private String standardReferenceVersionOption;
	private String scriptReferenceVersionOption;
	private String description;
	private String credits;
	private String instructions;
	private String promotionalLink;
	private String programVersion;
	private List<String> contentList;

	@JsonProperty("EAEndYear")
	private String EAEndYear;
	@JsonProperty("EAEndMonth")
	private String EAEndMonth;
	@JsonProperty("EAEndDay")
	private String EAEndDay;

	private String secondaryLicenseType;

	@JsonProperty("dependencies")
	private ObjectNode dependencies;

	private CustomOption customOptions;

	private Boolean hadReferenceIssues;
	private Boolean includeVersionsInReferences;

	private List<ReferenceIssue> referenceIssues;

	private String version;
	private String patreonLink;

	public MetaJson(String varKey, String licenseType, JsonNode jf) {
		this.licenseType = licenseType;
		this.dependencies = jf.deepCopy();
	}

//	public Map<String, MetaJson> getDependenciesMap() {
//		Map<String, MetaJson> metaJsonMap = new LinkedHashMap<>();
//		if (Objects.isNull(dependencies))
//			return metaJsonMap;
//
//		dependencies.fields().forEachRemaining(e -> {
//			String varKey = e.getKey();
//			String licenseType = e.getValue().get("licenseType").asText();
//			JsonNode jf = e.getValue().get("dependencies");
//			if (Objects.nonNull(jf))
//				metaJsonMap.put(varKey, new MetaJson(varKey, licenseType, jf));
//		});
//		return metaJsonMap;
//	}

	public Map<String, String> getDependenciesAll(String parent) {
		Map<String, String> metaJsonMap = new LinkedHashMap<>();
		if (Objects.isNull(dependencies))
			return metaJsonMap;

		dependencies.fields().forEachRemaining(e -> {
			String varKey = e.getKey();
			String licenseType = "";
			if (Objects.nonNull(e.getValue().get("licenseType")))
				licenseType = e.getValue().get("licenseType").asText();

			JsonNode jf = e.getValue().get("dependencies");
			if (Objects.nonNull(jf)) {
				MetaJson metaJson = new MetaJson(varKey, licenseType, jf);
				metaJsonMap.put(varKey, parent);
				metaJsonMap.putAll(metaJson.getDependenciesAll(varKey));
			} else {
				metaJsonMap.put(varKey, parent);
			}
		});
		return metaJsonMap;
	}

}

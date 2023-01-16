package vam.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.NoArgsConstructor;
import vam.dto.meta.CustomOption;
import vam.dto.meta.Dependence;
import vam.dto.meta.ReferenceIssue;
import vam.util.CustomClothingDeserializer2;
import vam.util.MyCustomDeserializer;

@Data
@JsonInclude(Include.NON_NULL)
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

	@JsonDeserialize(using = MyCustomDeserializer.class)
	private List<String> contentList;

	@JsonProperty("EAEndYear")
	private String EAEndYear;
	@JsonProperty("EAEndMonth")
	private String EAEndMonth;
	@JsonProperty("EAEndDay")
	private String EAEndDay;

	private String secondaryLicenseType;

	@JsonDeserialize(using = CustomClothingDeserializer2.class)
	private Map<String, Dependence> dependencies;

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
		fetchDependenciesAll(metaJsonMap, dependencies, parent);
		return metaJsonMap;
	}

	private void fetchDependenciesAll(Map<String, String> metaJsonMap, Map<String, Dependence> dependencies,
			String parent) {
		dependencies.forEach((varKey, dependence) -> {
//			String licenseType = "";
//			if (Objects.nonNull(e.getValue().get("licenseType")))
//				licenseType = value.get("licenseType").asText();
//			JsonNode jf = e.getValue().get("dependencies");
//			if (Objects.nonNull(jf)) {
			metaJsonMap.put(varKey, parent);
			if (!CollectionUtils.isEmpty(dependence.getDependencies())) {
				fetchDependenciesAll(metaJsonMap, dependence.getDependencies(), varKey);
			}
		});
	}

	@JsonDeserialize(using = MyCustomDeserializer.class)
	public void setContentList(List<String> contentList) {
		this.contentList = contentList;
	}

	public List<String> getContentList() {
		return contentList;
	}

}

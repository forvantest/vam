package vam.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;
import vam.dto.meta.CustomOption;
import vam.dto.meta.ReferenceIssue;

@Data
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

}

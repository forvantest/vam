package vam.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class VarFileDTO {
	public VarFileDTO(String fullPath, String varFileName) {
		String[] varNameArray = StringUtils.split(varFileName, ".");
		if (varNameArray.length >= 1)
			creatorName = varNameArray[0];
		if (varNameArray.length >= 2)
			packageName = varNameArray[1];
		if (varNameArray.length >= 3)
			version = varNameArray[2];
		this.fullPath = fullPath;
		this.varFileName = varFileName;
	}

	// file name rule
	private String creatorName;

	private String packageName;

	private String version;

	private String fullPath;
	private String varFileName;

	// Saves
	// scene
	private SceneJson sceneJson;
//	Person\pose\
	private List<PoseJson> poseJsons = new ArrayList();

	// Custom

	private MetaJson metaJson;

	public String makeTitle() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(creatorName);
		stringBuilder.append(".");
		stringBuilder.append(packageName);
		stringBuilder.append(".");
		stringBuilder.append(version);
		return stringBuilder.toString();
	}

	public String makeHidePath() {
		if (Objects.isNull(sceneJson))
			return "";

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(makeTitle());
		stringBuilder.append(File.separator);
		stringBuilder.append(sceneJson.makeHidePath());
		return stringBuilder.toString();
	}

}

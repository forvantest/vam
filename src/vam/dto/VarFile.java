package vam.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class VarFile {
	public VarFile(String varFileName) {
		String[] varNameArray = StringUtils.split(varFileName, ".");
		if (varNameArray.length >= 1)
			authorName = varNameArray[0];
		if (varNameArray.length >= 2)
			projectName = varNameArray[1];
		if (varNameArray.length >= 3)
			version = varNameArray[2];
	}

	// file name rule
	private String authorName;

	private String projectName;

	private String version;

	// Saves
	// scene
	private SceneJson sceneJson;
//	Person\pose\
	private List<PoseJson> poseJsons = new ArrayList();

	// Custom

	private MetaJson metaJson;

	public String makeTitle() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(authorName);
		stringBuilder.append(".");
		stringBuilder.append(projectName);
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

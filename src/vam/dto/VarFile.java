package vam.dto;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class VarFile {
	public VarFile(String varFileName) {
		String[] varNameArray = StringUtils.split(varFileName, ".");
		authorName = varNameArray[0];
		projectName = varNameArray[1];
		version = varNameArray[2];
	}

	// file name rule
	String authorName;

	String projectName;

	String version;

	// Saves
	// scene
	SceneJson sceneJson;

	// Custom

	MetaJson metaJson;
}

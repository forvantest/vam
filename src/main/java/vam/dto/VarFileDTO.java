package vam.dto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import vam.entity.VarFile;

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

	public VarFileDTO(VarFile varFile) {
		super();
		this.creatorName = varFile.getCreatorName();
		this.packageName = varFile.getPackageName();
		this.version = varFile.getVersion();
		this.fullPath = varFile.getFullPath();
		this.varFileName = varFile.getVarFileName();
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

	public void realHide(String VAM_FILE_PREFS) {
		tagVarFile(VAM_FILE_PREFS, ".hide");
	}

	public void favorite(String VAM_FILE_PREFS) {
		tagVarFile(VAM_FILE_PREFS, ".fav");
	}

	private void tagVarFile(String VAM_FILE_PREFS, String extension) {
		try {
			if (Objects.nonNull(this.getSceneJson())) {
				String PATH_HIDE_PATH = VAM_FILE_PREFS + this.makeHidePath();
				File hidePath = new File(PATH_HIDE_PATH);
				if (!hidePath.exists()) {
					hidePath.mkdirs();
				}
				String PATH_HIDE_FILE = PATH_HIDE_PATH + this.getSceneJson().makeEmptyFile(extension);
				File hideFile = new File(PATH_HIDE_FILE);
				if (!hideFile.exists()) {
					hideFile.createNewFile();
					System.out.println("+++" + extension + ": " + hideFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void unHide(String VAM_FILE_PREFS) {
		String PATH_HIDE_PATH = VAM_FILE_PREFS + this.makeTitle();
		File hidePath = new File(PATH_HIDE_PATH);
		if (hidePath.exists()) {
			try {
				FileUtils.forceDelete(hidePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("---unhide:" + hidePath);
		}
	}

}

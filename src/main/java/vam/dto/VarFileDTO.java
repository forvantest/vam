package vam.dto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import vam.entity.VarFile;

@Slf4j
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

		try {
			String str = varFile.getMetaDependencies();
			if (Objects.nonNull(str)) {
				ObjectMapper objectMapper = new ObjectMapper();
				this.setMetaJson(new MetaJson());
				this.getMetaJson().setDependencies(objectMapper.readTree(str).deepCopy());
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	private Integer femaleCount;
	private Integer femaleGenitaliaCount;
	private Integer maleCount;
	private Integer maleGenitaliaCount;

	// file name rule
	private String creatorName;

	private String packageName;

	private String version;

	private String fullPath;
	private String varFileName;

	// Saves
	// scene
	private List<SceneJson> sceneJsonList = new ArrayList();
//	Person\pose\
	private List<PoseJson> poseJsons = new ArrayList();

	// Custom

	private MetaJson metaJson;
	private Exception exception;

	public String makeTitle() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(creatorName);
		stringBuilder.append(".");
		stringBuilder.append(packageName);
		stringBuilder.append(".");
		stringBuilder.append(version);
		return stringBuilder.toString();
	}

	private String makeHidePath(SceneJson sceneJson) {
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

		this.getSceneJsonList().forEach(s -> {
			try {
				String PATH_HIDE_PATH = VAM_FILE_PREFS + this.makeHidePath(s);
				File hidePath = new File(PATH_HIDE_PATH);
				if (!hidePath.exists()) {
					hidePath.mkdirs();
				}
				String PATH_HIDE_FILE = PATH_HIDE_PATH + s.makeEmptyFile(extension);
				File hideFile = new File(PATH_HIDE_FILE);
				if (!hideFile.exists()) {
					hideFile.createNewFile();
					log.info("+++" + extension + ": " + hideFile);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
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
			log.info("---unhide: " + hidePath);
		}
	}

	public void increaseFemale() {
		if (Objects.isNull(femaleCount))
			femaleCount = 0;
		femaleCount++;
	}

	public void increaseFemaleGenitalia() {
		if (Objects.isNull(femaleGenitaliaCount))
			femaleGenitaliaCount = 0;
		femaleGenitaliaCount++;
	}

	public void increaseMale() {
		if (Objects.isNull(maleCount))
			maleCount = 0;
		maleCount++;
	}

	public void increaseMaleGenitalia() {
		if (Objects.isNull(maleGenitaliaCount))
			maleGenitaliaCount = 0;
		maleGenitaliaCount++;
	}

	public void setException(Exception ex) {
		this.exception = ex;
	}

	public String makeKey() {
		StringBuffer sb = new StringBuffer();
		sb.append(creatorName);
		sb.append(".");
		sb.append(packageName);
		sb.append(".");
		sb.append(version);
		return sb.toString();
	}

	public void moveVarFileTo(String VAM_SOME_PATH) {
		Path sDir = Paths.get(fullPath + varFileName);
		String targetPath = VAM_SOME_PATH + creatorName + "\\";
		File f = new File(targetPath);
		if (!f.exists())
			f.mkdirs();
		Path tDir = Paths.get(targetPath, varFileName);
		try {
			System.out.println("\n---moving Duplicate: " + sDir);
			Files.move(sDir, tDir, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

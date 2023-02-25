package vam.dto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import vam.util.FileUtil;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class VarFileDTO implements Comparable {

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

//	public VarFileDTO(VarFile varFile) {
//		super();
//		this.creatorName = varFile.getCreatorName();
//		this.packageName = varFile.getPackageName();
//		this.version = varFile.getVersion();
//		this.fullPath = varFile.getFullPath();
//		this.varFileName = varFile.getVarFileName();
//		this.favorite = varFile.getFavorite();
//		this.referencesJson = varFile.getReferencesJson();
//		try {
////			String str = varFile.getMetaDependencies();
////			if (Objects.nonNull(str)) {
////				this.setMetaJson(new MetaJson());
////				this.getMetaJson().setDependencies(objectMapper.readTree(str).deepCopy());
////			}
//			String str2 = varFile.getScenesJson();
//			if (Objects.nonNull(str2)) {
//				List<SceneJson> myObjects = objectMapper.readValue(str2, new TypeReference<List<SceneJson>>() {
//				});
//				this.setSceneJsonList(myObjects);
//			}
//			String str3 = varFile.getDependencies();
//			if (Objects.nonNull(str3)) {
//				this.dependencies = objectMapper.readValue(str3, new TypeReference<Set<String>>() {
//				});
//			}
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//
//	}

	public VarFileDTO() {
		super();
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

	private Set<String> dependencies = new HashSet<>();
	// Saves
	// scene
	private List<SceneJson> sceneJsonList = new ArrayList();

	private List<String> soundList = new ArrayList();

//	Person\pose\
	private List<PoseJson> poseJsons = new ArrayList();

	// Custom

	private MetaJson metaJson;
	private Exception exception;
	private Integer favorite;
	private String referencesJson;
	private Boolean bFavorite;
	private Boolean bHide;

	public String makeTitle() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(creatorName);
		stringBuilder.append(".");
		stringBuilder.append(packageName);
		stringBuilder.append(".");
		stringBuilder.append(version);
		return stringBuilder.toString();
	}

	public void easySceneJson() {
		try {
			List<SceneJson> easyList = new ArrayList<>();
			sceneJsonList.forEach(s -> easyList.add(new SceneJson(s)));
			sceneJsonList = easyList;
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		bHide = true;
	}

	public void favorite(String VAM_FILE_PREFS) {
		tagVarFile(VAM_FILE_PREFS, ".fav");
		bFavorite = true;
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

	public void moveVarFileTo(String VAM_SOME_PATH, String reason) {
		String srcPath = fullPath + varFileName;
		Path sDir = Paths.get(srcPath);
		String targetPath = VAM_SOME_PATH + creatorName + "\\";
		FileUtil.checkFolderExist(targetPath);
		Path tDir = Paths.get(targetPath, varFileName);
		if (!FileUtil.checkFileExist(srcPath)) {
			System.out.println("\n--X--moving failed src not exist " + reason + ": " + srcPath);
		} else if (!sDir.endsWith(tDir)) {
			try {
				System.out.println("\n---moving " + reason + ": " + sDir);
				Files.move(sDir, tDir, StandardCopyOption.REPLACE_EXISTING);
				FileUtil.deleteFolderIfEmpty(fullPath);
				this.setFullPath(targetPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (FileUtil.checkFileExist(targetPath + varFileName)) {
			log.debug("\n--X--moving failed target exist " + reason + ": " + targetPath + varFileName);
		} else {
			log.error("\n---moving failed " + reason + ": " + sDir);
		}
	}

	public void increaseFavorite() {
		if (Objects.nonNull(favorite))
			favorite++;
		else
			favorite = 1;
	}

	@Override
	public int compareTo(Object o) {
		if (!StringUtils.isNumeric(this.version))
			return -1;

		VarFileDTO varFileDTO = (VarFileDTO) o;
		if (!StringUtils.isNumeric(varFileDTO.version))
			return 1;

		return Integer.parseInt(varFileDTO.getVersion()) - Integer.parseInt(this.version);
	}

//	public void increaseReference(String parent) {
//		if (Objects.isNull(referencesJson))
//			referencesJson = "[]";
//		Set<String> reference;
//		try {
//			reference = objectMapper.readValue(referencesJson, HashSet.class);
//			String key = parent;
//			reference.add(key);
//			referencesJson = objectMapper.writeValueAsString(reference);
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//	}
}

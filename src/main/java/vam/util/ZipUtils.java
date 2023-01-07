package vam.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import vam.dto.MetaJson;
import vam.dto.PoseJson;
import vam.dto.SceneJson;
import vam.dto.VarFileDTO;
import vam.dto.enumration.VarFieldType;

@Slf4j
@Component
public class ZipUtils {

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 解壓縮
	 * 
	 * @param zipfile    zip檔位置
	 * @param extractDir 解壓縮資料夾
	 * @return
	 */
	public boolean unzipToVarFile(File zipfile, VarFileDTO varFileDTO) {

		try {
			unZip(zipfile, varFileDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 解壓縮主程式
	 * 
	 * @param zipFileName
	 * @param outputDirectory
	 * @throws Exception
	 */
	private void unZip(File ZIPFile, VarFileDTO varFileDTO) throws Exception {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(ZIPFile);
			Enumeration e = zipFile.getEntries();
			ZipEntry zipEntry = null;
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				// System.out.println("unziping " + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					// File f = new File(outputDirectory + File.separator + name);
					// f.mkdir();
					// System.out.println("創建立目錄：" + outputDirectory + File.separator + name);
				} else {
					VarFieldType varFieldType = checkTypeEnum(varFileDTO.makeTitle(), zipEntry);
					convertField(varFileDTO, varFieldType, zipFile, zipEntry);
				}
			}
			if (Objects.nonNull(zipFile))
				zipFile.close();
		} catch (Exception ex) {
			System.out.println("\n" + ZIPFile);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	static List<String> skipResourceExtension = Arrays.asList("sln", "vmb", "vab", "vaj", "vap", "vac", "dsf", "duf",
			"vam", "cs", "cslist", "txt", "scene", "assetbundle", "manifest", "Assetbundle", "gif", "jpeg", "jpg",
			"png", "tif", "tga", "psd", "dll", "mp3", "wav", "ogg", "jsondb", "voicebundle", "mtl", "obj", "fav",
			"colliders", "hide", "vapb");

	static List<String> skipSaveExtension = Arrays.asList("embodyprofile", "vmi", "hide");

	private VarFieldType checkTypeEnum(String title, ZipEntry zipEntry) {
		if ("meta.json".equals(zipEntry.getName())) {
			return VarFieldType.META;
		} else if (StringUtils.startsWith(zipEntry.getName(), "Custom")) {
			String atomPath = zipEntry.getName().toLowerCase();
			String atomExtension = getExtension(atomPath);
			if (skipResourceExtension.contains(atomExtension)) {
			} else if ("vmi".equals(atomExtension)) {
				if (StringUtils.startsWith(atomPath, "custom/atom/person/morphs/female/"))
					return VarFieldType.CUSTOM_ATOM_PERSON_MORPHS_FEMALE;
				if (StringUtils.startsWith(atomPath, "custom/atom/person/morphs/female_genitalia/"))
					return VarFieldType.CUSTOM_ATOM_PERSON_MORPHS_FEMALE_GENITALIA;
				if (StringUtils.startsWith(atomPath, "custom/atom/person/morphs/male/"))
					return VarFieldType.CUSTOM_ATOM_PERSON_MORPHS_MALE;
				if (StringUtils.startsWith(atomPath, "custom/atom/person/morphs/male_genitalia/"))
					return VarFieldType.CUSTOM_ATOM_PERSON_MORPHS_MALE_GENITALIA;
			} else if (StringUtils.startsWith(atomPath, "custom/scripts/")) {
				log.debug("Scripts.var...");
			} else if ("json".equals(atomExtension) && StringUtils.startsWith(atomPath, "custom/scripts/")) {
				log.debug("Scripts.json...");
			} else if ("json".equals(atomExtension) && StringUtils.startsWith(atomPath, "custom/subscene/")) {
				log.debug("SubScene.json...");
			} else if ("json".equals(atomExtension) && StringUtils.startsWith(atomPath, "custom/assets/")) {
				log.debug("Assets.json...");
			} else {
				log.debug("warn1: " + zipEntry.getName());
			}
		} else if (StringUtils.startsWith(zipEntry.getName(), "Saves")) {
			String atomPath = zipEntry.getName().toLowerCase();
			String atomExtension = getExtension(atomPath);
			String[] nameArray = StringUtils.split(atomPath, "/");
			if (skipResourceExtension.contains(atomExtension)) {
			} else if ("json".equals(atomExtension) && StringUtils.startsWith(atomPath, "saves/person/")) {
				//System.out.print("Saves.Person.json...");
			} else if ("person".equals(nameArray[1])) {
				if ("pose".equals(nameArray[2])) {
					return VarFieldType.SAVES_PERSON_POSE_DOT_JSON;
				} else {
					System.out.println("warn2: " + title + " : " + zipEntry.getName());
				}
			} else if ("scene".equals(nameArray[1])) {

				if (nameArray[nameArray.length - 1].endsWith(".json")) {
					return VarFieldType.SAVES_SCENE_DOT_JSON;
				} else if (skipSaveExtension.contains(atomExtension)) {

				} else {
					log.debug("warn12: wrong meta.json: " + nameArray[nameArray.length - 1]);
				}
			} else {
				log.debug("warn3: " + title + " : " + zipEntry.getName());
			}
		} else if (skipResourceExtension.contains(getExtension(zipEntry.getName()))) {
		} else {
			System.out.println("warn4: " + zipEntry.getName());
		}
		return null;
	}

	private void convertField(VarFileDTO varFileDTO, VarFieldType varFieldType, ZipFile zipFile, ZipEntry zipEntry) {
		if (VarFieldType.META == varFieldType) {
			String jsonText = null;
			String fixedString = null;
			try {
				jsonText = unZipFile(zipFile, zipEntry);
				fixedString = StringUtils.replace(jsonText, "\uFEFF", "");
				if (Objects.nonNull(fixedString) || StringUtils.isEmpty(fixedString)) {
					MetaJson metaJson = objectMapper.readValue(fixedString, MetaJson.class);
				//	if(StringUtils.contains(fixedString, "Alter3go.Studio_Poses_collection_1_3.latest"))
					//	System.out.println("debug2: " + varFileDTO.getVarFileName());
					varFileDTO.setMetaJson(metaJson);
				}
			} catch (JsonMappingException ex) {
				varFileDTO.setException(ex);
			} catch (JsonEOFException ex) {
				varFileDTO.setException(ex);
			} catch (JsonParseException ex) {
				varFileDTO.setException(ex);
			} catch (Exception ex) {
				System.out.println("\n" + varFileDTO.getFullPath() + varFileDTO.getVarFileName());
				System.out.println(jsonText);
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				System.out.println("...");
			}
		} else if (VarFieldType.CUSTOM_ATOM_PERSON_MORPHS_FEMALE == varFieldType) {
			varFileDTO.increaseFemale();
		} else if (VarFieldType.CUSTOM_ATOM_PERSON_MORPHS_FEMALE_GENITALIA == varFieldType) {
			varFileDTO.increaseFemaleGenitalia();
		} else if (VarFieldType.CUSTOM_ATOM_PERSON_MORPHS_MALE == varFieldType) {
			varFileDTO.increaseMale();
		} else if (VarFieldType.CUSTOM_ATOM_PERSON_MORPHS_MALE_GENITALIA == varFieldType) {
			varFileDTO.increaseMaleGenitalia();
		} else if (VarFieldType.SAVES_SCENE_DOT_JSON == varFieldType) {
			String jsonText = null;
			String fixedString = null;
			try {
				jsonText = unZipFile(zipFile, zipEntry);
				fixedString = StringUtils.replace(jsonText, "\uFEFF", "");
				if (Objects.isNull(fixedString) || StringUtils.isEmpty(fixedString)) {
					System.out.println("warn11: empty json:" + varFileDTO);
				} else {
					SceneJson sceneJson = objectMapper.readValue(fixedString, SceneJson.class);
					sceneJson.setScenePath(zipEntry.getName());
					varFileDTO.getSceneJsonList().add(sceneJson);
				}
			} catch (JsonParseException ex) {
				varFileDTO.setException(ex);
			} catch (Exception ex) {
				varFileDTO.setException(ex);
				System.out.println("\n" + varFileDTO.getFullPath() + varFileDTO.getVarFileName());
				System.out.println(jsonText);
				ex.printStackTrace();
				System.out.println(ex.getMessage());
			}
		} else if (VarFieldType.SAVES_PERSON_POSE_DOT_JSON == varFieldType) {
			String jsonText = null;
			try {
				jsonText = unZipFile(zipFile, zipEntry);
				if (Objects.nonNull(jsonText)) {
					PoseJson poseJson = objectMapper.readValue(jsonText, PoseJson.class);
					varFileDTO.getPoseJsons().add(poseJson);
				}
			} catch (JsonParseException ex) {
				varFileDTO.setException(ex);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	private String unZipFile(ZipFile zipFile, ZipEntry zipEntry) {
		try {
			String fileName = zipEntry.getName();
			fileName = fileName.replace('\\', '/');
			if (fileName.indexOf("/") != -1) {
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
			}
			InputStream inputStream = zipFile.getInputStream(zipEntry);
			String jsonText = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
					.collect(Collectors.joining(""));

			inputStream.close();
			return jsonText;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	private String getExtension(String fileName) {
		try {
			int index = StringUtils.lastIndexOf(fileName, ".");
			if (index >= 0)
				return StringUtils.substring(fileName, index + 1);
			return "";
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
}

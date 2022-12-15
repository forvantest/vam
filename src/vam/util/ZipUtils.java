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

import com.fasterxml.jackson.databind.ObjectMapper;

import vam.dto.MetaJson;
import vam.dto.SceneJson;
import vam.dto.VarFile;
import vam.dto.enumration.VarFieldType;

public class ZipUtils {

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 解壓縮
	 * 
	 * @param zipfile    zip檔位置
	 * @param extractDir 解壓縮資料夾
	 * @return
	 */
	public boolean unzipToVarFile(File zipfile, VarFile varFile) {

		try {
			unZip(zipfile, varFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	private void unZip(File ZIPFile, VarFile varFile) throws Exception {
		try {
			ZipFile zipFile = new ZipFile(ZIPFile);
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
					VarFieldType varFieldType = checkTypeEnum(zipEntry);
					convertField(varFile, varFieldType, zipFile, zipEntry);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	static List<String> skipResourceExtension = Arrays.asList("vmb", "vmi", "vab", "vaj", "vam", "jpg", "png");

	private VarFieldType checkTypeEnum(ZipEntry zipEntry) {
		if ("meta.json".equals(zipEntry.getName())) {
			return VarFieldType.META;
		} else if (StringUtils.startsWith(zipEntry.getName(), "Custom")) {
			if (skipResourceExtension.contains(getExtension(zipEntry.getName()))) {
				// System.out.println(zipEntry.getName());
			} else {
				System.out.println(zipEntry.getName());
			}
		} else if (StringUtils.startsWith(zipEntry.getName(), "Saves")) {
			String[] nameArray = StringUtils.split(zipEntry.getName(), "/");
			if (skipResourceExtension.contains(getExtension(zipEntry.getName()))) {
				// System.out.println(zipEntry.getName());
			} else if ("scene".equals(nameArray[1])) {
				return VarFieldType.SAVES_SCENE_DOT_JSON;
			} else {
				System.out.println(zipEntry.getName());
			}
		} else
			System.out.println(zipEntry.getName());
		return null;
	}

	private void convertField(VarFile varFile, VarFieldType varFieldType, ZipFile zipFile, ZipEntry zipEntry) {
		try {
			if (VarFieldType.META == varFieldType) {
				String jsonText = unZipFile(zipFile, zipEntry);
				if (Objects.nonNull(jsonText)) {
					MetaJson metaJson = objectMapper.readValue(jsonText, MetaJson.class);
					varFile.setMetaJson(metaJson);
				}
			}else if (VarFieldType.SAVES_SCENE_DOT_JSON == varFieldType) {
				String jsonText = unZipFile(zipFile, zipEntry);
				if (Objects.nonNull(jsonText)) {
					SceneJson sceneJson = objectMapper.readValue(jsonText, SceneJson.class);
					varFile.setSceneJson(sceneJson);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
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

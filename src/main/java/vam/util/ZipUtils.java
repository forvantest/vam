package vam.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import lombok.extern.slf4j.Slf4j;
import vam.dto.MetaJson;
import vam.dto.PoseJson;
import vam.dto.SceneJson;
import vam.dto.VarFileDTO;
import vam.dto.enumration.AtomType;
import vam.dto.enumration.SoundType;
import vam.dto.enumration.VarFieldType;
import vam.dto.meta.CustomOption;
import vam.dto.scene.Atom;
import vam.dto.scene.Clothing;
import vam.dto.scene.Hair;
import vam.dto.scene.Morph;
import vam.dto.scene.Storable;
import vam.dto.scene.StorableGeometry;

@Slf4j
@Component
public class ZipUtils {

	@Autowired
	public ObjectMapper objectMapper;

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
			varFileDTO.setException(ex);
			System.out.println("\n" + ZIPFile);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	static List<String> skipResourceExtension = Arrays.asList("sln", "vmb", "vab", "vaj", "vap", "vac", "dsf", "duf",
			"vam", "cs", "cslist", "txt", "scene", "assetbundle", "manifest", "gif", "jpeg", "jpg", "png", "tif", "tga",
			"psd", "dll", "jsondb", "voicebundle", "mtl", "obj", "fav", "colliders", "hide", "vapb");

	static List<String> skipSaveExtension = Arrays.asList("embodyprofile", "vmi", "hide");

	private VarFieldType checkTypeEnum(String title, ZipEntry zipEntry) {
		String atomPath = zipEntry.getName().toLowerCase();
		String atomExtension = getExtension(atomPath);
		if ("meta.json".equals(zipEntry.getName())) {
			return VarFieldType.META;
		} else if (StringUtils.startsWith(zipEntry.getName(), "Custom")) {
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
			} else if ("mp3".equals(atomExtension) || "ogg".equals(atomExtension) || "wav".equals(atomExtension)) {
				if (StringUtils.startsWith(atomPath, "custom/sound"))
					return VarFieldType.CUSTOM_SOUND;
				else if (StringUtils.startsWith(atomPath, "custom/script"))
					return VarFieldType.CUSTOM_SOUND;
				log.error("Strange sound path:{}", atomPath);
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
			String[] nameArray = StringUtils.split(atomPath, "/");
			if (skipResourceExtension.contains(atomExtension)) {
			} else if ("json".equals(atomExtension) && StringUtils.startsWith(atomPath, "saves/person/")) {
				// System.out.print("Saves.Person.json...");
			} else if ("person".equals(nameArray[1])) {
				if ("pose".equals(nameArray[2]) && "json".equals(atomExtension)) {
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
		} else if (skipResourceExtension.contains(atomExtension)) {
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

//					JavaType customClassCollection = objectMapper.getTypeFactory().constructCollectionType(List.class,
//							String.class);
//					List<String> beanList = (List<String>) objectMapper.readValue(fixedString, customClassCollection);

					MetaJson metaJson = objectMapper.readValue(fixedString, MetaJson.class);
					Map<String, String> mapDependencies = metaJson.getDependenciesAll("");
					varFileDTO.getDependencies().addAll(mapDependencies.keySet());
					// if(StringUtils.contains(fixedString,
					// "Alter3go.Studio_Poses_collection_1_3.latest"))
					// System.out.println("debug2: " + varFileDTO.getVarFileName());
					varFileDTO.setMetaJson(metaJson);
				}
			} catch (JsonMappingException ex) {
				ex.printStackTrace();
				log.debug(ex.getMessage());
				log.debug("\n" + varFileDTO.getFullPath() + varFileDTO.getVarFileName());
				log.debug(jsonText);
				// varFileDTO.setException(ex);
			} catch (JsonEOFException ex) {
				ex.printStackTrace();
				varFileDTO.setException(ex);
			} catch (JsonParseException ex) {
				ex.printStackTrace();
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

//					Clothing car = new Clothing("yellow", "renault");
//					String carJson = objectMapper.writeValueAsString(car);

					SceneJson sceneJson = objectMapper.readValue(fixedString, SceneJson.class);
					sceneJson.setScenePath(zipEntry.getName());
					varFileDTO.getSceneJsonList().add(sceneJson);
					if (Objects.nonNull(sceneJson.getAtoms()))
						varFileDTO.getDependencies().addAll(convertDependencies(sceneJson.getAtoms()));
				}
			} catch (JsonParseException ex) {
				varFileDTO.setException(ex);
			} catch (InvalidFormatException ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			} catch (UnrecognizedPropertyException ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			} catch (MismatchedInputException ex) {
				System.out.println(ex.getMessage());
				// ex.printStackTrace();
			} catch (NullPointerException ex) {
				System.out.println(ex.getMessage());
				// ex.printStackTrace();
			} catch (Exception ex) {
				// varFileDTO.setException(ex);
				System.out.println("\n" + varFileDTO.getFullPath() + varFileDTO.getVarFileName());
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
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		} else if (VarFieldType.CUSTOM_SOUND == varFieldType) {
			try {
				log.warn("\n{} have sound:{}", varFileDTO.getVarFileName(), zipEntry);
				varFileDTO.getSoundList().add(zipEntry.getName());
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	private Set<String> convertDependencies(List<Atom> atoms) {
		Set<String> dependencies = new HashSet<>();
		for (Atom atom : atoms) {
			if (atom.getType() == AtomType.Person) {
				for (Storable storable : atom.getStorables()) {
					if ("geometry".equals(storable.getId())) {
						StorableGeometry storableGeometry = storable;
						for (Clothing clothing : storableGeometry.getClothing()) {
							if (Objects.nonNull(clothing.getVarName()))
								dependencies.add(clothing.getVarName());
						}
						for (Hair hair : storableGeometry.getHair()) {
							if (Objects.nonNull(hair.getVarName()))
								dependencies.add(hair.getVarName());
						}
						for (Morph morph : storableGeometry.getMorphs()) {
							if (Objects.nonNull(morph.getVarName()))
								dependencies.add(morph.getVarName());
						}
					}
				}
			}
		}
		return dependencies;
	}

	private String takeVarName(String fullRef) {
		int startIdx = StringUtils.indexOf(fullRef, "=");
		int endIdx = StringUtils.indexOf(fullRef, ":");
		if (0 <= startIdx && startIdx < endIdx) {
			String varName = StringUtils.substring(fullRef, startIdx, endIdx);
			return varName;
		} else
			return null;
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

	public String getExtension(String fileName) {
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

	public String getFileName(String fileName) {
		try {
			int index = StringUtils.lastIndexOf(fileName, "/");
			if (index >= 0)
				return StringUtils.substring(fileName, index + 1);
			return "";
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public String getFolder(String fileName) {
		try {
			int index = StringUtils.lastIndexOf(fileName, ".");
			if (index >= 0)
				return StringUtils.substring(fileName, 0, index) + "\\";
			return "";
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public File translateChinese(VarFileDTO mergedVarFileDTO, VarFileDTO chineseVarFileDTO,
			VarFileDTO varFileDTOEnglish, String modifiedMetaJson, String path) {
		File odsTempFile = new File(varFileDTOEnglish.getFullPath() + varFileDTOEnglish.getVarFileName());
//				"C:\\VAM\\AllPackages\\___VarTidied___\\best_scene\\SlamT\\SlamT.Spooged-A_Tiny_Vam_Xmas-FULL.1.var");
		File result = null;
		ZipFile inZip = null;
		ZipOutputStream outZip = null;
		try {

			List<String> englishSoundList = varFileDTOEnglish.getSoundList();
			result = new File(path + mergedVarFileDTO.getVarFileName());
			inZip = new ZipFile(odsTempFile);
			outZip = new ZipOutputStream(new FileOutputStream(result));

			List<String> chineseSoundList = chineseVarFileDTO.getSoundList();

			for (Enumeration entries = inZip.getEntries(); entries.hasMoreElements();) {
				org.apache.tools.zip.ZipEntry in = (org.apache.tools.zip.ZipEntry) entries.nextElement();
				org.apache.tools.zip.ZipEntry out;
				InputStream source;// /scene/__PATREON/Spooged-A Tiny VAM Christmas\\Spooged-A Tiny Vam Xmas
									// FULL.json
				if (StringUtils.startsWith(in.getName(),
						"Saves/scene/__PATREON/Spooged-A Tiny VAM Christmas/Spooged-A Tiny Vam Xmas FULL.json")) {
					String jsonText = null;
					try {
						jsonText = unZipFile(inZip, in);
						for (int i = 0; i < englishSoundList.size(); i++) {
							int index = i % chineseSoundList.size();
							String chineseSound = chineseVarFileDTO.makeKey() + ":/" + chineseSoundList.get(index);
							String englishSound = "SELF:/" + englishSoundList.get(i);
							jsonText = StringUtils.replace(jsonText, englishSound, chineseSound);

							String chineseSound1 = getFileName(chineseSound);
							String englishSound1 = getFileName(englishSound);
							jsonText = StringUtils.replace(jsonText, englishSound1, chineseSound1);
							log.warn("{} replace sound:{} -----> {}", i, englishSound, chineseSound);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					byte[] contentAsBytes = jsonText.getBytes();
					out = new org.apache.tools.zip.ZipEntry(in);
					out.setSize(contentAsBytes.length);
					source = new ByteArrayInputStream(contentAsBytes);
				} else if (StringUtils.startsWith(in.getName(), "meta.json") && Objects.nonNull(modifiedMetaJson)) {
					byte[] contentAsBytes = modifiedMetaJson.getBytes();
					out = new org.apache.tools.zip.ZipEntry(in);
					out.setSize(contentAsBytes.length);
					source = new ByteArrayInputStream(contentAsBytes);
				} else {
					out = in;
					source = inZip.getInputStream(in);
				}
				outZip.putNextEntry(out);
				IOUtils.copy(source, outZip); // Apache's Commons-IO
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(outZip); // Apache's Commons-IO
		}
		odsTempFile = result;
		log.warn("done:{}", odsTempFile.getPath());
		return result;
	}

	private String fetchSoundType(SoundType soundType) {
		// TODO Auto-generated method stub
		return null;
	}

	private String translate(String jsonText, String englishSound, String chineseSound) {
//		String chineseSound = chineseVarFileDTO.makeKey() + ":/" + chineseSoundList.get(index);
		String englishSound2 = "SELF:/" + englishSound;
		jsonText = StringUtils.replace(jsonText, englishSound2, chineseSound);

		String chineseSound1 = getFileName(chineseSound);
		String englishSound1 = getFileName(englishSound);
		jsonText = StringUtils.replace(jsonText, englishSound1, chineseSound1);
		log.warn("replace sound:{} -----> {}", englishSound, chineseSound);
		return jsonText;
	}

	// 压缩文件夹内的文件
	public void doZip(String outDir, String zipFileName) {// zipDirectoryPath:需要压缩的文件夹名
		File zipDir = new File(outDir);
		try {
			ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
			handleDir(StringUtils.length(outDir), zipDir, zipOut);
			zipOut.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// 由doZip调用,递归完成目录文件读取
	private void handleDir(int rootPathLength, File dir, ZipOutputStream zipOut) throws IOException {
		FileInputStream fileIn;
		File[] files;
		int bufSize = 1024;
		byte[] buf = new byte[bufSize];
		int readedBytes = 0;

		files = dir.listFiles();

		if (files.length == 0) {// 如果目录为空,则单独创建之.
			// ZipEntry的isDirectory()方法中,目录以"/"结尾.
			String entryName = StringUtils.substring(dir.getPath(), rootPathLength) + "/";
			log.warn("entryName:{}", entryName);
			zipOut.putNextEntry(new ZipEntry(entryName));
			zipOut.closeEntry();
		} else {// 如果目录不为空,则分别处理目录和文件.
			for (File infile : files) {
				// System.out.println(fileName);

				if (infile.isDirectory()) {
					handleDir(rootPathLength, infile, zipOut);
				} else {
					fileIn = new FileInputStream(infile);
					String entryName = StringUtils.substring(infile.getPath(), rootPathLength);
//					log.warn("entryName:{}", entryName);
					zipOut.putNextEntry(new ZipEntry(entryName));
					while ((readedBytes = fileIn.read(buf)) > 0) {
						zipOut.write(buf, 0, readedBytes);
					}

					zipOut.closeEntry();
				}
			}
		}
	}

	// 解压指定zip文件
	public void unZip(String unZipfileName, String outDir) {// unZipfileName需要解压的zip文件名
		FileOutputStream fileOut;
		// File file;
		InputStream inputStream;
		int bufSize = 512;
		byte[] buf = new byte[bufSize];
		int readedBytes = 0;

		try {
//			String outDir = getFolder(unZipfileName);
			ZipFile zipFile = new ZipFile(unZipfileName);

			for (Enumeration entries = zipFile.getEntries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				File outfile = new File(outDir + entry.getName());

				if (entry.isDirectory()) {
					outfile.mkdirs();
				} else {
					// 如果指定文件的目录不存在,则创建之.
					File parent = outfile.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}

					inputStream = zipFile.getInputStream(entry);

					fileOut = new FileOutputStream(outfile);
					while ((readedBytes = inputStream.read(buf)) > 0) {
						fileOut.write(buf, 0, readedBytes);
					}
					fileOut.close();

					inputStream.close();
				}
			}
			zipFile.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void removeUseLessSound(String unZipfileName, List<String> soundList) {
		for (String filename : soundList) {
			File tempfile = new File(unZipfileName + filename);
			if (tempfile.exists()) {
				tempfile.delete();
				log.warn("--- remove useless sound: {}", tempfile.getAbsolutePath());
			}
		}
	}

	public Set<String> copyDirectory(int rootPathLength, String source, String destination) throws IOException {
		File srcDir = new File(source);
		File destDir = new File(destination);
		try {
			FileUtils.copyDirectory(srcDir, destDir);

		} catch (IOException e) {
			e.printStackTrace();
		}

		Set<String> fileList = new HashSet<>();
		Files.walkFileTree(Paths.get(destination), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				if (!Files.isDirectory(file)) {
					String fullPath = file.toAbsolutePath().toString();
					String entryName = StringUtils.substring(fullPath, rootPathLength);
					entryName = StringUtils.replace(entryName, "\\", "/");
					fileList.add(entryName);
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return fileList;
	}

	public void makeMetaJson(String unZipfileName, VarFileDTO mergedVarFileDTO) {
		try {
			MetaJson metaJson = new MetaJson();
			metaJson.setLicenseType("CC BY-NC-ND");
			metaJson.setCreatorName(mergedVarFileDTO.getCreatorName());
			metaJson.setPackageName(mergedVarFileDTO.getPackageName());
			metaJson.setStandardReferenceVersionOption("Latest");
			metaJson.setScriptReferenceVersionOption("Exact");
			metaJson.setDescription("");
			metaJson.setCredits("");
			metaJson.setInstructions("");
			metaJson.setPromotionalLink("");
			metaJson.setProgramVersion("1.20.77.13");
			metaJson.setContentList(mergedVarFileDTO.getSoundList());
			metaJson.setDependencies(new HashMap());
			metaJson.setCustomOptions(new CustomOption());
			metaJson.setHadReferenceIssues(false);
			metaJson.setReferenceIssues(new ArrayList());
			// Create new file
			String content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(metaJson);
			String path = unZipfileName + "\\meta.json";
			File file = new File(path);

			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}

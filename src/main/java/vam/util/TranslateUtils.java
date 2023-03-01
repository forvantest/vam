package vam.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import vam.dto.SceneJson;
import vam.dto.SoundDTO;
import vam.dto.TranslateDTO;
import vam.dto.VarFileDTO;
import vam.dto.enumration.SoundType;

@Slf4j
@Component
public class TranslateUtils {

	public List<String> translateChinese2(String unZipfileName, VarFileDTO mergedVarFileDTO,
			List<VarFileDTO> varFileDTOChineseList, VarFileDTO varFileDTOEnglish) {
		List<String> uselesssSoundList = new ArrayList<>();
		try {
			TranslateDTO translateDTO = new TranslateDTO(varFileDTOChineseList);

//			String content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(metaJson);
			SceneJson sceneJson = varFileDTOEnglish.getSceneJsonList().get(0);
			String path = unZipfileName + sceneJson.getScenePath();

			FileInputStream fis = new FileInputStream(path);
			String content = IOUtils.toString(fis, "UTF-8");
			fis.close();

			// If file doesn't exists, then create it
//			if (!file.exists()) {
//				file.createNewFile();
//			}
			String content2 = translate(content, varFileDTOEnglish.getSoundList(), translateDTO, uselesssSoundList);

			File file = new File(path);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// Write in file
			bw.write(content2);

			// Close connection
			bw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return uselesssSoundList;
	}

	Map<String, String> specialSoundMap = new HashMap<>();

	private void fillSpecialSoundList() {
		try {
			String rootPath = "C:\\VAM-resource\\中文語音可替換素材包\\";
			List<String> soundTypeList = Arrays.asList("-ACT-", "-BCUM-", "-CUM-", "PORN_WORDS_SPANK");

			for (int i = 0; i < soundTypeList.size(); i++) {
				String soundType = soundTypeList.get(i);
				String childPath = rootPath + soundType;
				File sfile = new File(childPath);
				for (File cFile : sfile.listFiles()) {
					String key = StringUtils.replace(cFile.getAbsolutePath(), rootPath, "");
					key = StringUtils.replace(key, "\\", "/");
//					int index = StringUtils.indexOf(key, "\\");
//					String value = StringUtils.substring(key, index);
					specialSoundMap.put(key, cFile.getName());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String specialSound(String content) {
		String jsonText = content;
		for (Entry<String, String> entrySet : specialSoundMap.entrySet()) {
			if (!StringUtils.contains(jsonText, entrySet.getKey())
					&& StringUtils.contains(jsonText, entrySet.getValue())) {
				jsonText = StringUtils.replace(jsonText, "jacky.sound.1:/Custom/Sounds/淫語/" + entrySet.getValue(),
						"jacky.sound.1:/Custom/Sounds/" + entrySet.getKey());
				log.error("replace special sound:{} ---> {}", entrySet.getValue(), entrySet.getKey());
			}
		}
		return jsonText;
	}

	private String translate(String content, List<String> englishSoundList, TranslateDTO translateDTO,
			List<String> uselesssSoundList) {
		String jsonText = content;
		try {
			fillSpecialSoundList();
			jsonText = specialSound(content);
			for (int i = 0; i < englishSoundList.size(); i++) {
				String englishSound = englishSoundList.get(i);
				SoundType soundType = identifyEnglishSoundType1(englishSound);
				if (Objects.nonNull(soundType)) {
					SoundDTO chineseSoundDTO = translateDTO.fetchSound(soundType);
					if (Objects.nonNull(chineseSoundDTO)) {
						jsonText = translate(jsonText, englishSound, chineseSoundDTO);
						uselesssSoundList.add(englishSound);
					} else {
						log.error("no match sound:{}", englishSound);
					}
				} else {
					log.warn("question sound:{} ", englishSound);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonText;
	}

	private SoundType identifyEnglishSoundType1(String englishSound) {
		String upperCase = englishSound.toUpperCase();
		if (StringUtils.contains(upperCase, "BGO-CM-"))
			return SoundType.FAKE_REJECT;
		if (StringUtils.contains(upperCase, "-ACT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "-AFT-"))
			return SoundType.PORN_WORDS;
		if (StringUtils.contains(upperCase, "-AT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "-CM-"))
			return SoundType.PORN_WORDS_CUM;
		if (StringUtils.contains(upperCase, "-CS-"))
			return SoundType.PORN_WORDS_CUM;
		if (StringUtils.contains(upperCase, "-FS-"))
			return SoundType.MOAN;
		if (StringUtils.contains(upperCase, "-FT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "-GFT-"))
			return SoundType.PORN_WORDS;
		if (StringUtils.contains(upperCase, "-PT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "-RT-"))
			return SoundType.PORN_WORDS_SPANK;
		if (StringUtils.contains(upperCase, "-SPK-"))
			return SoundType.SPANK;
		if (StringUtils.contains(upperCase, "-SS-"))
			return SoundType.BLOW;
		if (StringUtils.contains(upperCase, "-SSD-"))
			return SoundType.BLOW;
		if (StringUtils.contains(upperCase, "-ST-"))
			return SoundType.PORN_WORDS;
		if (StringUtils.contains(upperCase, "-TFT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "TIT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "-TT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "-O-"))
			return SoundType.CUM;
		if (StringUtils.contains(upperCase, "-T-"))
			return SoundType.MOAN;
		if (StringUtils.contains(upperCase, "-CMF-"))
			return SoundType.PORN_WORDS_CUM;
		if (StringUtils.contains(upperCase, "KS-"))
			return SoundType.KISS;
		if (StringUtils.contains(upperCase, "-SSB-"))
			return SoundType.BLOW;
		if (StringUtils.contains(upperCase, "_CMF-"))
			return SoundType.PORN_WORDS_CUM;
		if (StringUtils.contains(upperCase, "SEXSLAP"))
			return SoundType.BREATH;
		if (StringUtils.contains(upperCase, "SSD-"))
			return SoundType.BLOW;
		if (StringUtils.contains(upperCase, "WET"))
			return SoundType.WET;
		if (StringUtils.contains(upperCase, "-NFT-"))
			return SoundType.PORN_WORDS;
		if (StringUtils.contains(upperCase, "-CUM-"))
			return SoundType.CUM;

		log.error("no match sound:{} ", upperCase);
		return null;
	}

	private String translate(String jsonText, String englishSound, SoundDTO chineseSoundDTO) {
		String chineseSound = chineseSoundDTO.getVarFileKey() + ":/" + chineseSoundDTO.getSoundName();
		String englishSound2 = "SELF:/" + englishSound;
		jsonText = StringUtils.replace(jsonText, englishSound2, chineseSound);

		String chineseSound1 = getFileName(chineseSound);
		String englishSound1 = getFileName(englishSound);
		jsonText = StringUtils.replace(jsonText, englishSound1, chineseSound1);
		log.warn("replace sound:{} -----> {}", englishSound, chineseSound);
		return jsonText;
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
}

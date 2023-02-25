package vam.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
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

	private String translate(String content, List<String> englishSoundList, TranslateDTO translateDTO,
			List<String> uselesssSoundList) {
		String jsonText = content;
		try {
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
		if (StringUtils.contains(upperCase, "DG-ACT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "DG-AFT-"))
			return SoundType.PORN_WORDS;
		if (StringUtils.contains(upperCase, "DG-AT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "DG-CM-"))
			return SoundType.PORN_WORDS_CUM;
		if (StringUtils.contains(upperCase, "DG-CS-"))
			return SoundType.PORN_WORDS_CUM;
		if (StringUtils.contains(upperCase, "DG-FS-"))
			return SoundType.BREATH;
		if (StringUtils.contains(upperCase, "DG-FT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "DG-GFT-"))
			return SoundType.PORN_WORDS;
		if (StringUtils.contains(upperCase, "DG-O-"))
			return SoundType.PORN_WORDS_CUM;
		if (StringUtils.contains(upperCase, "DG-PT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "DG-RT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "DG-SPK-"))
			return SoundType.BREATH;
		if (StringUtils.contains(upperCase, "DG-SS-"))
			return SoundType.BREATH;
		if (StringUtils.contains(upperCase, "DG-SS-"))
			return SoundType.BREATH;
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

package vam.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import vam.dto.enumration.SoundType;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class TranslateDTO {

	Map<SoundType, Integer> sequence = new HashMap<>();
	Map<SoundType, List<SoundDTO>> soundData = new HashMap<>();

	public TranslateDTO(List<VarFileDTO> varFileDTOChineseList) {
		for (int i = 0; i < varFileDTOChineseList.size(); i++) {
			VarFileDTO varFileDTOChinese = varFileDTOChineseList.get(i);
			doTranslateDTO(varFileDTOChinese);
		}
	}

	private void doTranslateDTO(VarFileDTO varFileDTOChinese) {
		List<String> soundList = varFileDTOChinese.getSoundList();
		for (int i = 0; i < soundList.size(); i++) {
			String chineseSound = soundList.get(i);
			SoundType soundType = identifyChineseSoundType1(chineseSound);
			SoundDTO soundDTO = new SoundDTO(soundType, chineseSound, varFileDTOChinese.makeKey());
			List<SoundDTO> soundDTOList = soundData.get(soundType);
			if (soundDTOList == null) {
				soundDTOList = new ArrayList<>();
				soundData.put(soundType, soundDTOList);
				sequence.put(soundType, 0);
			}
			soundDTOList.add(soundDTO);
		}
	}

	public SoundDTO fetchSound(SoundType soundType) {
		if (sequence.get(soundType) != null) {
			int seq = sequence.get(soundType);
			List<SoundDTO> soundDTOList = soundData.get(soundType);
			int index = seq % soundDTOList.size();
			sequence.put(soundType, index + 1);
			return soundDTOList.get(index);
		} else {
			return null;
		}
	}

	private SoundType identifyChineseSoundType1(String chineseSound) {
		String upperCase = chineseSound.toUpperCase();
		if (StringUtils.contains(upperCase, "不要"))
			return SoundType.FAKE_REJECT;
		if (StringUtils.contains(upperCase, "呻吟聲"))
			return SoundType.MOAN;
		if (StringUtils.contains(upperCase, "MOAN"))
			return SoundType.MOAN;
		if (StringUtils.contains(upperCase, "BLOW"))
			return SoundType.BLOW;
		if (StringUtils.contains(upperCase, "DEEPTHROAT"))
			return SoundType.BLOW;
		if (StringUtils.contains(upperCase, "KISS"))
			return SoundType.KISS;
		if (StringUtils.contains(upperCase, "BREATH"))
			return SoundType.BREATH;
		if (StringUtils.contains(upperCase, "PHONERING"))
			return SoundType.PHONERING;
		if (StringUtils.contains(upperCase, "PAPACUM"))
			return SoundType.PAPACUM;
		if (StringUtils.contains(upperCase, "-BCUM-"))
			return SoundType.PORN_WORDS_CUM;
		if (StringUtils.contains(upperCase, "-ACT-"))
			return SoundType.PORN_WORDS_ACT;
		if (StringUtils.contains(upperCase, "-CUM-"))
			return SoundType.CUM;
		if (StringUtils.contains(upperCase, "FLUID"))
			return SoundType.WET;
		if (StringUtils.contains(upperCase, "SLAP"))
			return SoundType.SPANK;
		if (StringUtils.contains(upperCase, "BUSINESS"))
			return SoundType.NORMAL;
		if (StringUtils.contains(upperCase, "PORN_WORDS_SPANK"))
			return SoundType.PORN_WORDS_SPANK;
		if (StringUtils.contains(upperCase, "淫語"))
			return SoundType.PORN_WORDS;
		return SoundType.NORMAL;
	}

}

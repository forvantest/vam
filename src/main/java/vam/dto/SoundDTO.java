package vam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import vam.dto.enumration.SoundType;

@Slf4j
@JsonInclude(Include.NON_NULL)
@Data
public class SoundDTO {

	private SoundType soundType;

	String varFileKey;

	String soundName;

//	String nickName;

	public SoundDTO(SoundType soundType, String soundName, String varFileKey) {
		this.soundType = soundType;
		this.soundName = soundName;
		this.varFileKey = varFileKey;
	}

}

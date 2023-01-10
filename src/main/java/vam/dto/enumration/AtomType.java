package vam.dto.enumration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum AtomType {
	WindowCamera("WindowCamera"), PlayerNavigationPanel("PlayerNavigationPanel"), CoreControl("CoreControl"),
	Person("Person"), VRController("VRController"), SubScene("SubScene"), InvisibleLight("InvisibleLight"),
	CustomUnityAsset("CustomUnityAsset"), UISlider("UISlider"), UIToggle("UIToggle"), Empty("Empty"),
	UIButton("UIButton"), ImagePanelEmissive("ImagePanelEmissive"), Wall("Wall"), AudioSource("AudioSource"),
	Slate("Slate"), CyberpunkBed("CyberpunkBed"), CyberpunkComputerChair("CyberpunkComputerChair"),
	ReflectiveSlate("ReflectiveSlate");

	private final String description;

	AtomType(String description) {
		this.description = description;
	}

	@JsonCreator
	public static AtomType from(String description) {
		for (AtomType atomType : values()) {
			if (atomType.description.equals(description)) {
				return atomType;
			}
		}
		log.debug("\n---failed match AtomType: " + description);
		return null;
	}

	@JsonValue
	public String getValue() {
		return description;
	}
}

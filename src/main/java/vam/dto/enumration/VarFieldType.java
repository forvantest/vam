package vam.dto.enumration;

public enum VarFieldType {
	META("meta.json"), SAVES_SCENE_DOT_JSON("Saves/scene/Angela_HD_V1.json"),
	CUSTOM_ATOM_PERSON_MORPHS_FEMALE("custom/atom/person/morphs/female/"),
	CUSTOM_ATOM_PERSON_MORPHS_FEMALE_GENITALIA("custom/atom/person/morphs/female_genitalia/"),
	CUSTOM_ATOM_PERSON_MORPHS_MALE("custom/atom/person/morphs/male/"),
	CUSTOM_ATOM_PERSON_MORPHS_MALE_GENITALIA("custom/atom/person/morphs/male_genitalia/"),
	SAVES_PERSON_POSE_DOT_JSON("Saves\\Person\\pose\\SupaRioAmateur\\Scarlet.json"), CUSTOM_SOUND("custom/sound/"),;

	private String description;

	VarFieldType(String description) {
		this.description = description;
	}
}

package vam.dto.enumration;

public enum VarFieldType {
	META("meta.json"), SAVES_SCENE_DOT_JSON("Saves/scene/Angela_HD_V1.json"),
	SAVES_PERSON_POSE_DOT_JSON("Saves\\Person\\pose\\SupaRioAmateur\\Scarlet.json");

	private String description;

	VarFieldType(String description) {
		this.description = description;
	}
}

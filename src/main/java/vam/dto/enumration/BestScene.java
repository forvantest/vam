package vam.dto.enumration;

public enum BestScene {
	HotChicksVR("HotChicksVR"),

	;

	private String description;

	BestScene(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	static public boolean contains(String creatorName) {
		BestScene[] ea = BestScene.values();
		for (int i = 0; i < ea.length; i++) {
			if (ea[i].getDescription().equals(creatorName))
				return true;
		}
		return false;
	}

}

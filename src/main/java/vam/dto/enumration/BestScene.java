package vam.dto.enumration;

public enum BestScene {
	HotChicksVR("HotChicksVR"), androinz("androinz"), abcTits("abc tits"), 
	Chill_PopRun("Chill_PopRun"), TGC("TGC");

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

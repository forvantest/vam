package vam.dto.enumration;

public enum BestScene {
	HotChicksVR("HotChicksVR"), androinz("androinz"), abcTits("abc tits"), Chill_PopRun("Chill_PopRun"), TGC("TGC"),
	noheadnoleg("noheadnoleg"), KittyMocap("KittyMocap"), LDR("LDR"), jyy("jyy"), SlamT("SlamT"),
	ReignMocap("ReignMocap"), Zam55555("Zam55555"), AlpacaLaps("AlpacaLaps"), ZenMocap("ZenMocap"), Tiseb("Tiseb"),
	flyroxy("flyroxy"), Vihper("Vihper"), Universens("Universens"), xinxiu("xinxiu"), UJVAM("UJVAM"),
	FeiSama("FeiSama"), Nial("Nial"), IAmAFox("IAmAFox");

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

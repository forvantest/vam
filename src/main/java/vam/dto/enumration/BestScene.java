package vam.dto.enumration;

public enum BestScene {
	// @formatter:off
	HotChicksVR("HotChicksVR"), 
	Androinz("androinz"), 
	abcTits("abc tits"), 
	Chill_PopRun("ChilliPopRun"), 
	TGC("TGC"),
	noheadnoleg("noheadnoleg"), 
	KittyMocap("KittyMocap"), 
	LDR("LDR"), 
	jyy("Jyy"), 
	SlamT("SlamT"),
	ReignMocap("ReignMocap"), 
	Zam55555("Zam55555"), 
	AlpacaLaps("AlpacaLaps"), 
	ZenMocap("猛 禅"), 
	Tiseb("Tiseb"),
	FlyRoxy("Flyroxy"), 
	Vihper("Vihper"), 
	Universens("universens"), 
	xinxiu("xinxiu"), 
	UJVAM("UJVAM"),
	FeiSama("FeiSama"), 
	Nial("xNial"), 
	IAmAFox("IAmAFox"), 
	NisVamX("NisVamX"), 
	MK_47("MK_47"), 
	Ispinox("Ispinox"),
	HiphopJin("Hiphopjin"), 
	AndiFang("AndiFang"), 
	Romolas("Romolas"), 
	sxs4("sxs4"), 
	wunderwise("wunderwise"),
	Errors69("Errors69"), 
	XRWizard("xrwizard"), 
	Giovni("Giovni"), 
	uMKAxc62SI("uMKAxc62SI"), 
	VamTimbo("VamTimbo"),
	Zeeko("Zeeko834"),
	EyeCreated("Eye Created");

// @formatter:on

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

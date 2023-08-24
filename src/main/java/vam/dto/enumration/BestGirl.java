package vam.dto.enumration;

public enum BestGirl {
	// @formatter:off
	Dnaddr("Dnaddr"),
	realclone("realclone"),
	Archer("Archer"),
	VAMYJ("vamyj"),
	lv("lv"),
	CMA("Ccmmaa"),
	Eros("Eros"),
	Hcg("Hcg"),
	Solerrain("Solerrain"),	
	passerby("VAMpasserby"),
	callimohu("callimohu"),
	ye666("ye666"),
	zzzat16h("Zzzat16h"),
	VKStyle("VKStyle"),
	VAM_GS("VAM_GS"),
	sortof("Sortof"),
	MRdong("MRdong"),
	ReAcg("ReAcg"),
	BIGDOG("BIGDOG"),
	yesmola("yesmola"),
	FRK("FRK"),
	mai("mai"),
	Thorn("Thorn"),
	mio("mio"),
	HT("HT"),
	Anom("Anom"),
	VAMDoll("VAMDoll"),
	KDollMASTA("KDollMASTA"),
	Bamair1984("bamair1984"),
	qingfeng("qingfeng"),
	Qing("Qing"),
	rose1("rose1"),	
	rose11("rose11"),
	Xspada("Xspada"),
	ADADE("ADADE"),
	Wolverine("Wolverine"),
	Neiro("Neiro"),
	QWERTY("QWERTY"),
	MK47("MK47"),
	Keiaono("Keiaono"),
	hero774("hero774"),
	ILoveDolls("ILoveDolls"),
	starlu("starlu"),
	VR_Addict("VR_Addict"),
	Vr_Addict("Vr_Addict"),
	ABCgirls("ABCgirls870"),
	MonsterShinkai("MonsterShinkai"),
	Riccio("Riccio"),
	Fantasia3DArt("Fantasia3DArt")
	;
	// @formatter:on
	private String description;

	BestGirl(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	static public boolean contains(String creatorName) {
		BestGirl[] ea = BestGirl.values();
		for (int i = 0; i < ea.length; i++) {
			if (ea[i].getDescription().equals(creatorName))
				return true;
		}
		return false;
	}

}

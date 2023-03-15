package vam.dto.enumration;

public enum BestGirl {
	Dnaddr("Dnaddr"),
	realclone("realclone"),
	Archer("Archer"),
	VAMYJ("VAM-YJ"),
	lv("lv"),
	CMA("CMA"),
	Eros("Eros"),
	Hcg("Hcg"),
	Solerrain("Solerrain"),	
	passerby("passerby"),
	callimohu("callimohu"),
	ye666("ye666"),
	zzzat16h("zzzat16h"),
	VKStyle("VKStyle"),
	VAM_GS("VAM_GS"),
	sortof("sortof"),
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
	Bamair1984("Bamair1984"),
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
	VR_Addict("VR_Addict"),Vr_Addict("Vr_Addict"),
	;

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
			if( ea[i].getDescription().equals(creatorName))
				return true;
		}
		return false;
	}
	
}


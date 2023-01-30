package vam.dto.enumration;

public enum BestGirl {
	Dnaddr("Dnaddr"),realclone("realclone"),
	Archer("Archer"),VAMYJ("VAM-YJ"),
	lv("lv"),CMA("CMA"),
	VAMDoll("VAMDoll"),Anom("Anom"),
	Hcg("Hcg"),HT("HT"),
	zzzat16h("zzzat16h"),mio("mio"),
	callimohu("callimohu"),ye666("ye666"),
	Xspada("Xspada"),VKStyle("VKStyle"),
	VAM_GS("VAM_GS"),sortof("sortof"),
	Solerrain("Solerrain"),
	rose1("rose1"),	rose11("rose11"),
	qingfeng("qingfeng"),Qing("Qing"),
	passerby("passerby"),MRdong("MRdong"),
	Neiro("Neiro"),Wolverine("Wolverine"),
	yesmola("yesmola"),Eros("Eros"),
	KDollMASTA("KDollMASTA"),Bamair1984("Bamair1984"),
	BIGDOG("BIGDOG"),Keiaono("Keiaono"),
	MK47("MK47"),ReAcg("ReAcg"),
	QWERTY("QWERTY"),mai("mai"),
	Thorn("Thorn"),ADADE("ADADE"),
	FRK("FRK"),hero774("hero774"),
	
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


package vam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application extends SpringBootServletInitializer {

	@SuppressWarnings("resource")
	public static void main(final String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		long time1, time2;

		time1 = System.currentTimeMillis();
		mession(context.getBean(Work.class));
		time2 = System.currentTimeMillis();
		System.out.println("mission 花了：" + (time2 - time1) / 1000 + "秒");

		System.out.println("\n\nok");
		System.exit(0);
	}

	private static void mession(Work work) {
//		work.clearUseLessDB();
//		work.loadVarFileIntoDB("AllPackages/");
//		work.moveReference(null);
//		work.moveReference(BestScene.Ispinox.getDescription());
//		work.deploy("AA_ALL/");
//		work.deployBestGirl(BestGirl.Archer);
//		work.deployBestSceneGirl(BestScene.HotChicksVR, BestGirl.Dnaddr, "AA2_HotChicksVR_Dnaddr\\");
//		work.deployBestSceneGirl(BestScene.Androinz, BestGirl.realclone, "AA2_androinz_realclone\\");
//		work.deployBestSceneGirl(BestScene.abcTits, BestGirl.Archer, "AA2_abcTits_Archer\\");
//		work.deployBestSceneGirl(BestScene.Chill_PopRun, BestGirl.VAMYJ, "AA2_Chill_PopRun_VAMYJ\\");
//		work.deployBestSceneGirl(BestScene.TGC, BestGirl.lv, "AA2_TGC_lv\\");
//		work.deployBestSceneGirl(BestScene.noheadnoleg, BestGirl.Eros, "AA2_noheadnoleg_Eros\\");
//		work.deployBestSceneGirl(BestScene.KittyMocap, BestGirl.Hcg, "AA2_KittyMocap_Hcg\\");
//		work.deployBestSceneGirl(BestScene.LDR, BestGirl.Solerrain, "AA2_LDR_Solerrain\\");
//		work.deployBestSceneGirl(BestScene.jyy, BestGirl.passerby, "AA2_jyy_passerby\\");
//		work.deployBestSceneGirl(BestScene.SlamT, BestGirl.callimohu, "AA2_SlamT_callimohu\\");
//		work.deployBestSceneGirl(BestScene.ReignMocap, BestGirl.ye666, "AA2_ReignMocap_ye666\\");
//		work.deployBestSceneGirl(BestScene.Zam55555, BestGirl.zzzat16h, "AA2_Zam55555_zzzat16h\\");
//		work.deployBestSceneGirl(BestScene.Zam55555, BestGirl.ADADE, "AA2_Zam55555_ADADE\\");
//		work.deployBestSceneGirl(BestScene.AlpacaLaps, BestGirl.VKStyle, "AA2_AlpacaLaps_VKStyle\\");
//		work.deployBestSceneGirl(BestScene.ZenMocap, BestGirl.VAM_GS, "AA2_ZenMocap_VAM_GS\\");
//		work.deployBestSceneGirl(BestScene.Tiseb, BestGirl.sortof, "AA2_Tiseb_sortof\\");
//		work.deployBestSceneGirl(BestScene.flyroxy, BestGirl.MRdong, "AA2_flyroxy_MRdong\\");
//		work.deployBestSceneGirl(BestScene.Vihper, BestGirl.ReAcg, "AA2_Vihper_ReAcg\\");
//		work.deployBestSceneGirl(BestScene.Universens, BestGirl.CMA, "AA2_Universens_CMA\\");
//		work.deployBestSceneGirl(BestScene.xinxiu, BestGirl.yesmola, "AA2_xinxiu_yesmola\\");
//		work.deployBestSceneGirl(BestScene.UJVAM, BestGirl.mai, "AA2_UJVAM_mai\\");
//		work.deployBestSceneGirl(BestScene.SlamT, null, 10, "AA2_SlamT_10girls\\");

//		work.deployBestSceneGirl_GK("SlamT.Spooged-A_Tiny_Vam_Xmas-FULL.1.var", null, 3, "AAX_VAN_3girls\\",
//				"Zam55555.ZamS001SE_BusinessReception.latest");

		work.deployBestSceneGirl_GK("SlamT.Spooged-A_Tiny_Vam_Xmas-FULL.1.var", null, 3, "AAX_VAN_3girls\\",
				"jacky.sound.latest");

//		work.makeVarPack();

//		work.deployBestSceneGirl(BestScene.xinxiu, null, 10, "AA2_xinxiu_10girls\\");

//		work.deployOneSceneOneGirl("SlamT.TinyVam-TheCastingCouch.latest", "realclone.ELSA.latest",
//				"AA2_SlamTCouch_realcloneElsa\\");

//		work.deployBestSceneGirl(BestScene.Nial, BestGirl.Thorn, "AA2_Nial_Thorn\\");
		// work.deployBestSceneGirl(BestScene.FeiSama, BestGirl.FRK,
		// "AA2_FeiSama_FRK\\");

//		work.deployBestSceneGirl(BestScene.NisVamX, BestGirl.starlu, "AA2_NisVamX_starlu\\");
//		work.deployBestSceneGirl(BestScene.MK_47, BestGirl.MK47, "AA2_MK_47_MK47\\");
//		work.deployBestSceneGirl(BestScene.Vr_Addict, BestGirl.VR_Addict, "AA2_VR_Addict\\");

		// work.deployBestSceneGirl(BestScene.Ispinox, BestGirl.Thorn,
		// "AA2_Ispinox_Thorn\\");

//		BIGDOG("BIGDOG"),
//		Thorn("Thorn"),
//		mio("mio"),
//		HT("HT"),
//		Anom("Anom"),
//		VAMDoll("VAMDoll"),
//		KDollMASTA("KDollMASTA"),
//		Bamair1984("Bamair1984"),
//		qingfeng("qingfeng"),
//		Qing("Qing"),
//		rose1("rose1"),	
//		rose11("rose11"),
//		Xspada("Xspada"),
//		ADADE("ADADE"),
//		Wolverine("Wolverine"),
//		Neiro("Neiro"),
//		QWERTY("QWERTY"),
//		MK47("MK47"),
//		Keiaono("Keiaono"),
//		hero774("hero774"),

		// work.deployBestSceneGirl(BestScene.LDR, BestGirl.callimohu,
		// "AA_LDRcallimohu\\");

//		work.switchAuthor(BestScene.KittyMocap, BestGirl.Dnaddr);
//		work.deployBestScene(BestScene.Universens);
//		work.switchAuthor(BestScene.Universens);
//		work.deployBestScene(BestScene.HotChicksVR);
//		work.deployBestScene(BestScene.androinz);
//		work.deployBestGirl(BestGirl.CMA);
//		work.switchAuthor(BestGirl.mai);
//		work.switchAuthor(BestScene.TGC);
//		work.switchAuthor(BestScene.Chill_PopRun);
//		work.deployBestScene(BestScene.abcTits);
//		work.switchAuthor(BestScene.HotChicksVR);
//		work.switchAuthor(BestScene.androinz);
//		work.switchAuthor("FRK");
//		work.switchAuthor("Eros");

		// work.createLinkFile();
//		work.unDeploy(BestGirl.Archer);
//		work.unDeploy("Dnaddr");
//		work.unDeploy("VAMDoll");
//		work.unDeploy("VAM-YJ");
//		work.unDeploy("lv");
//		work.unDeploy("realclone");
//		work.unDeploy("uugg");
//		work.unDeploy("Anom");
//		work.unDeploy("callimohu");
//		work.unDeploy("CMA");
//		work.unDeploy("");
//		work.unDeploy("yesmola");
//		work.unDeploy("Solerrain");

//		 work.unDeploy("zzzat16h");

//		work.deploy("");
//		work.deploy("Archer");
//		work.deploy("Dnaddr");
//		work.deploy("VAMDoll");
//		work.deploy("VAM-YJ");
//		work.deploy("lv");
//		work.deploy("realclone");
		// work.deploy("zzzat16h");
//		work.deploy("uugg");
//		work.deploy("Anom");
//		work.deploy("callimohu");
//		work.deploy("CMA");
//		work.deploy("Solerrain");

//		work.deploy("yesmola");

//		work.allUnHide("girl/realclone-support");
	}
}
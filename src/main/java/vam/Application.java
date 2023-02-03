package vam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import vam.dto.enumration.BestGirl;

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
//		work.moveReference();
		work.deploy();
//		work.deployBestScene(BestScene.TGC);

//		work.deployBestScene(BestScene.Chill_PopRun);
//		work.deployBestScene(BestScene.abcTits);
//		work.deployBestScene(BestScene.HotChicksVR);
//		work.deployBestScene(BestScene.androinz);
//		work.deployBestGirl(BestGirl.mai);
		work.switchAuthor(BestGirl.mai);
//		work.switchAuthor(BestScene.TGC);
//		work.switchAuthor(BestScene.Chill_PopRun);
//		work.deployBestScene(BestScene.abcTits);
//		work.switchAuthor(BestScene.HotChicksVR);
//		work.switchAuthor(BestScene.androinz);
//		work.switchAuthor("FRK");
//		work.switchAuthor("Eros");

		// work.createLinkFile();
//		work.unDeploy("Archer");
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
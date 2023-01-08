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
		System.out.println("mession 花了：" + (time2 - time1) / 1000 + "秒");

		System.out.println("\n\nok");
		System.exit(0);
	}

	private static void mession(Work work) {

		work.loadVarFileIntoDB("AllPackages/");
//		work.moveReference();
		
		// work.createLinkFile();
//		work.unDeploy("AllPackages/girl/Archer/");
//		work.unDeploy("AllPackages/girl/Dnaddr/");
//		work.unDeploy("AllPackages/girl/VAMDoll/");
//		work.unDeploy("AllPackages/girl/VAM-YJ/");
//		work.unDeploy("AllPackages/girl/lv/");
//		work.unDeploy("AllPackages/girl/realclone/");
//		work.unDeploy("AllPackages/girl/uugg/");
//		work.unDeploy("AllPackages/girl/Anom/");
//		work.unDeploy("AllPackages/girl/callimohu/");
//		work.unDeploy("AllPackages/girl/CMA/");
//		work.unDeploy("AllPackages/girl/");
//		work.unDeploy("AllPackages/girl/yesmola/");
		
//		 work.unDeploy("AllPackages/girl/zzzat16h/");

//			work.deploy("AllPackages/girl/");
//		work.deploy("AllPackages/girl/Archer/");
//		work.deploy("AllPackages/girl/Dnaddr/");
//		work.deploy("AllPackages/girl/VAMDoll/");
//		work.deploy("AllPackages/girl/VAM-YJ/");
//		work.deploy("AllPackages/girl/lv/");
//		work.deploy("AllPackages/girl/realclone/");
		// work.deploy("AllPackages/girl/zzzat16h/");
//		work.deploy("AllPackages/girl/uugg/");
//		work.deploy("AllPackages/girl/Anom/");
//		work.deploy("AllPackages/girl/callimohu/");
//		work.deploy("AllPackages/girl/CMA/");

		work.deploy("AllPackages/girl/yesmola/");
		
//		work.allUnHide("girl/realclone-support/");
	}
}
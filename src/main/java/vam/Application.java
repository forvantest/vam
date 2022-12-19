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

		context.getBean(Work.class).loadVarFileIntoDB("girl/DnaAddr-support/"); // <-- here

		// context.getBean(Work.class).mainHide("girl/realclone-support/");

	}
}
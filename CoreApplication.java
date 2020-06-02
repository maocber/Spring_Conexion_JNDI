package co.com.periferia.alfa.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import co.com.periferia.alfa.core.exception.UtilException;

@SpringBootApplication
@EnableScheduling
public class CoreApplication {
	
	@Autowired 
	UtilException utilException;

	static Logger LOGGER = LoggerFactory.getLogger(CoreApplication.class);
	
	public static void main(String[] args) {
		//SpringApplication.run(CoreApplication.class, args);
		new SpringApplicationBuilder(CoreApplication.class).sources(AppConfigurationJNDI.class)
		   .run(args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() throws Exception {
		utilException.inicializar();
		LOGGER.info("Inicia aplicacion");
	}
}

package springBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"springBoot", "baseClasses", "busAppCore", "dataSources", "routeSchedule"})
public class BusAppSpringWeb {

	public static void main(String[] args) {
		SpringApplication.run(BusAppSpringWeb.class, args);
	}

}
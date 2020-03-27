package pro.antonshu.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import pro.antonshu.client.configs.RabbitConfig;

@SpringBootApplication
public class ConsoleClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsoleClientApplication.class, args);
	}

}

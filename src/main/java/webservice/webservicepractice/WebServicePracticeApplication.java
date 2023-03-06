package webservice.webservicepractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Auditing 활성화
@SpringBootApplication
public class WebServicePracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebServicePracticeApplication.class, args);
	}

}

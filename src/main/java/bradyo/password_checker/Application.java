package bradyo.password_checker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackages = "bradyo.password_checker", basePackageClasses = Jsr310JpaConverters.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

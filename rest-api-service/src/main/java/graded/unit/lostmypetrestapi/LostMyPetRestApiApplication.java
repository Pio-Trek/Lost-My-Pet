package graded.unit.lostmypetrestapi;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Start application configuration class with {@link SpringBootApplication} annotation
 * that use auto-configuration and component scan.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@SpringBootApplication
@EnableJSONDoc
public class LostMyPetRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LostMyPetRestApiApplication.class, args);
    }

}

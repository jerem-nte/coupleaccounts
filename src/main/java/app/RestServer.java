package app;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"services, config"})
public class RestServer {
	
    public static void main(String[] args) throws Exception {

    	 SpringApplication.run(RestServer.class, args);
    }
}

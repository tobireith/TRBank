package de.othr.sw.TRBank;

import de.othr.sw.TRBank.service.KundeServiceIF;
import de.othr.sw.TRBank.setup.SetupExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TRBankApplication {
	public static void main(String[] args) {
		SpringApplication.run(TRBankApplication.class, args);
	}
}

package de.othr.sw.TRBank;

import de.othr.sw.TRBank.service.InitData;
import de.othr.sw.TRBank.service.KundeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TRBankApplication implements ApplicationRunner {

	@Autowired
	InitData initData;

	@Autowired
	KundeServiceIF kundeService;

	public static void main(String[] args) {
		SpringApplication.run(TRBankApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//TODO: Init data
		if(kundeService.getAllKunden().size() <= 0) {
			System.out.println("Initializing data");
			initData.initData();

		}
	}

	@GetMapping("/hello")
	//public String hello(@RequestParam(value = "kundenNr", defaultValue = "0") int kundenNr) {
	public String hello() {
		return String.format("Hello!");
	}
}

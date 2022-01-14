package de.othr.sw.TRBank.setup;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SetupFactory {

    @Bean @Qualifier("privatkunde")
    public KontoSetupAbstract createKontoPrivatkunde() {
        return new KontoSetupPrivatkunde();
    }

    @Bean @Qualifier("firmenkunde")
    public KontoSetupAbstract createKontoFirmenkunde() {
        return new KontoSetupFirmenkunde();
    }

    @Bean @Qualifier("privatkunde")
    public KundeSetupAbstract createKundePrivatkunde() {
        return new KundeSetupPrivatkunde();
    }

    @Bean @Qualifier("firmenkunde")
    public KundeSetupAbstract createKundeFirmenkunde() {
        return new KundeSetupFirmenkunde();
    }

    @Bean
    public TransaktionSetup createTransaktion() {
        return new TransaktionSetup();
    }
}

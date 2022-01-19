package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.service.exception.TRBankException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class SetupExecutor {

    private final List<SetupComponentAbstract> setupComponents = new ArrayList<>();

    @Autowired @Qualifier("firmenkunde")
    KundeSetupAbstract kundeSetupFirmenkunde;

    @Autowired @Qualifier("privatkunde")
    KundeSetupAbstract kundeSetupPrivatkunde;

    @Autowired @Qualifier("firmenkunde")
    KontoSetupAbstract kontoSetupFirmenkunde;

    @Autowired @Qualifier("privatkunde")
    KontoSetupAbstract kontoSetupPrivatkunde;

    @Autowired
    TransaktionSetup transaktionSetup;

    private final Logger logger = LoggerFactory.getLogger(SetupExecutor.class);

    @PostConstruct
    public void initSetups() {
        setupComponents.addAll(List.of(kundeSetupFirmenkunde, kundeSetupPrivatkunde, kontoSetupFirmenkunde, kontoSetupPrivatkunde, transaktionSetup));

    }

    public void executeSetup() {
        try {
           logger.info("Setup wird gestartet...");
            for(int i = 0; i < setupComponents.size(); i++) {
                SetupComponentAbstract setupComponent = setupComponents.get(i);
                logger.info("Setup... Schritt[" + (i+1) + " / " + setupComponents.size() + "]" );
                if (setupComponent.setup()) {
                    logger.info(setupComponent.getClass().getSimpleName() + " erfolgreich!");
                } else {
                    logger.info(setupComponent.getClass().getSimpleName() + " übersprungen!");
                }
            }
            logger.info("Setup erfolgreich abgeschlossen!");
        } catch (TRBankException e) {
            logger.error("Setup fehlgeschlagen! " + e.getNachricht());
        } catch (Exception e) {
            logger.error("Setup fehlgeschlagen! " + e.getMessage());
            e.printStackTrace();
        }
    }
}

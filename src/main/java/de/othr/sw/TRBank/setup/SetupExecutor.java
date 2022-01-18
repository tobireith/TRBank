package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.service.exception.TRBankException;
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

    @PostConstruct
    public void initSetups() {
        setupComponents.addAll(List.of(kundeSetupFirmenkunde, kundeSetupPrivatkunde, kontoSetupFirmenkunde, kontoSetupPrivatkunde, transaktionSetup));

    }

    public void executeSetup() {
        try {
            System.out.println("Setup wird gestartet...");
            for(int i = 0; i < setupComponents.size(); i++) {
                SetupComponentAbstract setupComponent = setupComponents.get(i);
                System.out.println("Setup... Schritt[" + (i+1) + " / " + setupComponents.size() + "]" );
                if (setupComponent.setup()) {
                    System.out.println(setupComponent.getClass().getSimpleName() + " erfolgreich!");
                } else {
                    System.out.println(setupComponent.getClass().getSimpleName() + " übersprungen!");
                }
            }
            System.out.println("Setup erfolgreich abgeschlossen!");
        } catch (TRBankException e) {
            System.out.println("Setup fehlgeschlagen! " + e.getNachricht());
        } catch (Exception e) {
            System.out.println("Setup fehlgeschlagen! " + e.getMessage());
            e.printStackTrace();
        }
    }
}

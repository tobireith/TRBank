package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.service.KundeServiceIF;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class KundeSetupAbstract extends SetupComponentAbstract {
    @Autowired
    KundeServiceIF kundeService;
}

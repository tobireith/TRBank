package de.othr.sw.TRBank.setup;

import de.othr.sw.TRBank.service.exception.TRBankException;

public abstract class SetupComponentAbstract {
    abstract boolean setup() throws TRBankException;
}

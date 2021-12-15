package de.othr.sw.TRBank.service.exception;

public class TRBankException extends Exception {
    public String fehlerNachricht;

    public TRBankException() {
        super();
    }

    public TRBankException(String fehlerNachricht) {
        super(fehlerNachricht);
    }

}

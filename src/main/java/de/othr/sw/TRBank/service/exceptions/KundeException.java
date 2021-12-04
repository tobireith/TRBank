package de.othr.sw.TRBank.service.exceptions;

public class KundeException extends Exception{
    public long fehlerNr;
    public String fehlerNachricht;

    public KundeException() {
        super();
    }

    public KundeException(String fehlerNachricht) {
        super(fehlerNachricht);
    }

    public KundeException(long fehlerNr, String fehlerNachricht) {
        this.fehlerNr = fehlerNr;
        this.fehlerNachricht = fehlerNachricht;
    }
}

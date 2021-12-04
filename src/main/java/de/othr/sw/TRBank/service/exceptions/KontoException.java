package de.othr.sw.TRBank.service.exceptions;

import de.othr.sw.TRBank.entity.Konto;
import de.othr.sw.TRBank.entity.Kontoauszug;

public class KontoException extends Exception{
    public long fehlerNr;
    public String fehlerNachricht;
    public Konto konto;
    public Kontoauszug kontoauszug;

    public KontoException() {
        super();
    }

    public KontoException(String fehlerNachricht) {
        super(fehlerNachricht);
    }

    public KontoException(long fehlerNr, String fehlerNachricht) {
        super(fehlerNachricht);
        this.fehlerNr = fehlerNr;
        this.fehlerNachricht = fehlerNachricht;
    }

    public KontoException(long fehlerNr, String fehlerNachricht, Konto konto) {
        super(fehlerNachricht);
        this.fehlerNr = fehlerNr;
        this.fehlerNachricht = fehlerNachricht;
        this.konto = konto;
    }

    public KontoException(long fehlerNr, String fehlerNachricht, Konto konto, Kontoauszug kontoauszug) {
        super(fehlerNachricht);
        this.fehlerNr = fehlerNr;
        this.fehlerNachricht = fehlerNachricht;
        this.konto = konto;
        this.kontoauszug = kontoauszug;
    }
}

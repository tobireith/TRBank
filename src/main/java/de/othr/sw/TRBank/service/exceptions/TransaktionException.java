package de.othr.sw.TRBank.service.exceptions;

import de.othr.sw.TRBank.entity.Transaktion;

public class TransaktionException extends Exception{
    public Transaktion transaktion;
    public long fehlerNr;
    public String fehlerNachricht;

    public TransaktionException() {
        super();
    }

    public TransaktionException(String fehlerNachricht) {
        super(fehlerNachricht);
    }

    public TransaktionException(Transaktion transaktion, long fehlerNr, String fehlerNachricht) {
        this.transaktion = transaktion;
        this.fehlerNr = fehlerNr;
        this.fehlerNachricht = fehlerNachricht;
    }
}

package de.othr.sw.TRBank.service.exception;

public class TRBankException extends Exception {
    public String titel;
    public String nachricht;
    public String vorschlag;

    public TRBankException() {
        super();
    }

    public TRBankException(String nachricht) {
        super(nachricht);
        this.nachricht = nachricht;
    }

    public TRBankException(String titel, String nachricht) {
        super(nachricht);
        this.titel = titel;
        this.nachricht = nachricht;
    }

    public TRBankException(String titel, String nachricht, String vorschlag) {
        super(nachricht);
        this.titel = titel;
        this.nachricht = nachricht;
        this.vorschlag = vorschlag;
    }

    public String getTitel() {
        return titel;
    }

    public String getNachricht() {
        return nachricht;
    }

    public String getVorschlag() {
        return vorschlag;
    }

    @Override
    public String toString() {
        return "TRBankException{" +
                "titel='" + titel + '\'' +
                ", nachricht='" + nachricht + '\'' +
                ", vorschlag='" + vorschlag + '\'' +
                '}';
    }

}

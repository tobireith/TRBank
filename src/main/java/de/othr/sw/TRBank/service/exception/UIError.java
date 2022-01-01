package de.othr.sw.TRBank.service.exception;

public class UIError{
    public String titel;
    public String nachricht;
    public String vorschlag;

    public UIError(String nachricht) {
        this.nachricht = nachricht;
    }

    public UIError(String titel, String nachricht) {
        this.titel = titel;
        this.nachricht = nachricht;
    }

    public UIError(String titel, String nachricht, String vorschlag) {
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
        return "UIError{" +
                "titel='" + titel + '\'' +
                ", nachricht='" + nachricht + '\'' +
                ", vorschlag='" + vorschlag + '\'' +
                '}';
    }
}

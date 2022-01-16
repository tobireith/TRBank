package de.othr.sw.TRBank.entity.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class RestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String passwort;

    @Valid
    TransaktionDTO transaktionDTO;

    public RestDTO(String username, String passwort, TransaktionDTO transaktionDTO) {
        this.username = username;
        this.passwort = passwort;
        this.transaktionDTO = transaktionDTO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public TransaktionDTO getTransaktionDTO() {
        return transaktionDTO;
    }

    public void setTransaktionDTO(TransaktionDTO transaktionDTO) {
        this.transaktionDTO = transaktionDTO;
    }

    @Override
    public String toString() {
        return "restDTO{" +
                "username='" + username + '\'' +
                ", passwort='" + passwort + '\'' +
                ", transaktionDTO=" + transaktionDTO +
                '}';
    }
}

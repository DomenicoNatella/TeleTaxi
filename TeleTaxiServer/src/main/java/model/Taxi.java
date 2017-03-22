package model;

import java.util.Arrays;

/**
 * Created by dn on 21/03/17.
 */
public class Taxi {
    private int codice;
    private String stato, posizione, destinazione, serviziSpeciali[];

    public Taxi(int codice, String stato, String posizione, String denominazione, String[] serviziSpeciali) {
        this.codice = codice;
        this.stato = stato;
        this.posizione = posizione;
        this.destinazione = denominazione;
        this.serviziSpeciali = serviziSpeciali;
    }

    public int getCodice() {
        return codice;
    }

    public Taxi setCodice(int codice) {
        this.codice = codice;
        return this;
    }

    public String getStato() {
        return stato;
    }

    public Taxi impostaStato(String stato) {
        this.stato = stato;
        return this;
    }

    public String getPosizione() {
        return posizione;
    }

    public Taxi setPosizione(String posizione) {
        this.posizione = posizione;
        return this;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public Taxi setDestinazione(String destinazione) {
        this.destinazione = destinazione;
        return this;
    }

    public String[] getServiziSpeciali() {
        return serviziSpeciali;
    }

    public Taxi setServiziSpeciali(String[] serviziSpeciali) {
        this.serviziSpeciali = serviziSpeciali;
        return this;
    }

    @Override
    public String toString() {
        return "Taxi: " +
                "codice: " + codice +
                ", stato: '" + stato + '\'' +
                ", serviziSpeciali: " + Arrays.toString(serviziSpeciali);
    }
}

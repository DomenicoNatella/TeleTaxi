package model;

/**
 * Created by dn on 26/03/17.
 */
public class Notifica {

    private String stato;
    private int tempoNotifica;

    public Notifica(String stato, int tempoNotifica) {
        this.stato = stato;
        this.tempoNotifica = tempoNotifica;
    }

    public String getStato() {
        return stato;
    }

    public Notifica setStato(String stato) {
        this.stato = stato;
        return this;
    }

    public int getTempoNotifica() {
        return tempoNotifica;
    }

    public Notifica setTempoNotifica(int tempoNotifica) {
        this.tempoNotifica = tempoNotifica;
        return this;
    }

}

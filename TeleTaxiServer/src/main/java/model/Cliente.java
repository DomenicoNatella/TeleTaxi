package model;

import java.util.Date;

/**
 * Created by dn on 21/03/17.
 */
public class Cliente extends Persona {

    private int telefono;
    private String posizioneCorrente;

    public Cliente(String nome, String cognome, Date dataDiNascita, int telefono, String posizioneCorrente) {
        super(nome, cognome, dataDiNascita);
        this.telefono = telefono;
        this.posizioneCorrente = posizioneCorrente;
    }

    public int getTelefono() {
        return telefono;
    }

    public Cliente setTelefono(int telefono) {
        this.telefono = telefono;
        return this;
    }

    public String getPosizioneCorrente() {
        return posizioneCorrente;
    }

    public Cliente setPosizioneCorrente(String posizioneCorrente) {
        this.posizioneCorrente = posizioneCorrente;
        return this;
    }

    @Override
    public String toString() {
        return "Cliente: " +
                "telefono:" + telefono +
                ", posizione corrente:'" + posizioneCorrente + '\'';
    }
}

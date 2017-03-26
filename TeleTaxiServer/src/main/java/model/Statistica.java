package model;

/**
 * Created by dn on 26/03/17.
 */
public class Statistica {
    private String tipo;
    private String[] informazioni;

    public Statistica(String tipo, String[] informazioni) {
        this.tipo = tipo;
        this.informazioni = informazioni;
    }

    public String getTipo() {
        return tipo;
    }

    public Statistica setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public String[] getInformazioni() {
        return informazioni;
    }

    public Statistica setInformazioni(String[] informazioni) {
        this.informazioni = informazioni;
        return this;
    }

    private void invioStatistica(){
        // da implementare
    }
}

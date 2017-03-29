package model;

/**
 * Created by dn on 29/03/17.
 */
public class Statistica {
    private Class tipo;
    private Object[] informazioni;

    public Statistica(Class tipo, Object[] informazioni) {
        this.tipo = tipo;
        this.informazioni = informazioni;
    }

    public Class getTipo() {return tipo;}

    public Statistica setTipo(Class tipo) {this.tipo = tipo; return this;}

    public Object getInformazioni() {return informazioni;}

    public Statistica setInformazioni(Object[] informazioni) {this.informazioni = informazioni; return this;}
}

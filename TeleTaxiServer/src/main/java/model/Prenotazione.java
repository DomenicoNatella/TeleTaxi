package model;

/**
 * Created by dn on 26/03/17.
 */
public class Prenotazione {

    private Cliente cliente;
    private Taxi taxi;
    private String serviziSpeciali[], progressivo;
    private int tempoAttesa;

    public Prenotazione(Cliente cliente, Taxi taxi, String[] serviziSpeciali, String progressivo, int tempoAttesa) {
        this.cliente = cliente;
        this.taxi = taxi;
        this.serviziSpeciali = serviziSpeciali;
        this.progressivo = progressivo;
        this.tempoAttesa = tempoAttesa;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Prenotazione setCliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public Prenotazione setTaxi(Taxi taxi) {
        this.taxi = taxi;
        return this;
    }

    public String[] getServiziSpeciali() {
        return serviziSpeciali;
    }

    public Prenotazione setServiziSpeciali(String[] serviziSpeciali) {
        this.serviziSpeciali = serviziSpeciali;
        return this;
    }

    public String getProgressivo() {
        return progressivo;
    }

    public Prenotazione setProgressivo(String progressivo) {
        this.progressivo = progressivo;
        return this;
    }

    public int getTempoAttesa() {
        return tempoAttesa;
    }

    public Prenotazione setTempoAttesa(int tempoAttesa) {
        this.tempoAttesa = tempoAttesa;
        return this;
    }

    public boolean configuraNotifica(){
        // da implementare
        return false;
    }

    public boolean aggiornaInformazioni(){
        // da implementare
        return false;
    }

}

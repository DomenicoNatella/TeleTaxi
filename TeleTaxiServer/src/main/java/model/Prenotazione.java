package model;

/**
 * Created by dn on 26/03/17.
 */
public class Prenotazione {

    private Cliente cliente;
    private OperatoreTelefonico operatoreTelefonico;
    private Taxi taxi;
    private String serviziSpeciali[], progressivo;
    private int tempoAttesa;

    public Prenotazione(String progressivo, Cliente cliente,OperatoreTelefonico operatoreTelefonico, Taxi taxi,String destinazione, String[] serviziSpeciali, int tempoAttesa) {
        this.cliente = cliente;
        this.operatoreTelefonico = operatoreTelefonico;
        this.taxi = taxi;
        this.serviziSpeciali = serviziSpeciali;
        this.progressivo = progressivo;
        this.tempoAttesa = tempoAttesa;
    }

    public Cliente getCliente() {return cliente;}

    public Prenotazione setCliente(Cliente cliente) {this.cliente = cliente; return this;}

    public OperatoreTelefonico getOperatoreTelefonico() {return operatoreTelefonico;}

    public Prenotazione setOperatoreTelefonico(OperatoreTelefonico operatoreTelefonico) {this.operatoreTelefonico = operatoreTelefonico; return this;}

    public Taxi getTaxi() {
        return taxi;
    }

    public Prenotazione setTaxi(Taxi taxi) {this.taxi = taxi; return this;}

    public String[] getServiziSpeciali() {
        return serviziSpeciali;
    }

    public Prenotazione setServiziSpeciali(String[] serviziSpeciali) {this.serviziSpeciali = serviziSpeciali; return this;}

    public String getProgressivo() {
        return progressivo;
    }

    public Prenotazione setProgressivo(String progressivo) {this.progressivo = progressivo; return this;}

    public int getTempoAttesa() {
        return tempoAttesa;
    }

    public Prenotazione setTempoAttesa(int tempoAttesa) {this.tempoAttesa = tempoAttesa; return this;}

    public boolean configuraNotifica(){
        // da implementare
        return false;
    }

    public boolean aggiornaInformazioni(){
        // da implementare
        return false;
    }

}

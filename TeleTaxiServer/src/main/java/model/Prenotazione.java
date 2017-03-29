package model;

import java.util.Date;

/**
 * Created by dn on 26/03/17.
 */
public class Prenotazione {

    private Cliente cliente;
    private OperatoreTelefonico operatoreTelefonico;
    private Taxi taxi;
    private String serviziSpeciali[], progressivo;
    private double tempoAttesa;
    private Date data;

    public Prenotazione(String progressivo, Cliente cliente,OperatoreTelefonico operatoreTelefonico, Taxi taxi,String destinazione, String[] serviziSpeciali,
                        double tempoAttesa, Date data) {
        this.cliente = cliente;
        this.operatoreTelefonico = operatoreTelefonico;
        this.taxi = taxi;
        this.serviziSpeciali = serviziSpeciali;
        this.progressivo = progressivo;
        this.tempoAttesa = tempoAttesa;
        this.data = data;
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

    public double getTempoAttesa() {
        return tempoAttesa;
    }

    public Prenotazione setTempoAttesa(double tempoAttesa) {this.tempoAttesa = tempoAttesa; return this;}

    public Date getData() {return data;}

    public Prenotazione setData(Date data) {this.data = data; return this;}

    public boolean configuraNotifica(){
        // da implementare
        return false;
    }

    public boolean aggiornaInformazioni(){
        // da implementare
        return false;
    }

}

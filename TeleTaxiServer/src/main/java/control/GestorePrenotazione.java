package control;

import model.Prenotazione;
import model.Taxi;
import resources.BaseColumns;
import websource.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dn on 26/03/17.
 */
public class GestorePrenotazione {
    private static GestorePrenotazione instance = null;
    private Connection connection;
    private Statement statement;
    private CopyOnWriteArrayList<Prenotazione> prenotazioni;
    private CopyOnWriteArrayList<Taxi> taxi;

    public GestorePrenotazione(){
        DatabaseManager db = DatabaseManager.getInstance();
        connection = db.getConnection();
        try {
           if(connection!=null) statement = (Statement) connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Errore nella creazione della connessione in GestorePrenotazione");
        }catch (NullPointerException e){
            System.err.println("Nessuna connessione");
        }
    }

    //singleton
    public static synchronized GestorePrenotazione getInstance(){
        if(instance==null) instance=new GestorePrenotazione();
        return instance;
    }

    public synchronized Prenotazione inserisciPrenotazione(Prenotazione pr){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        prenotazioni.add(pr);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO "+ BaseColumns.TAB_PRENOTAZIONI+
                    "("+BaseColumns.PROGRESSIVO_PRENOTAZIONE+","+BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+","+BaseColumns.IDENTIFICATIVO_TAXI+","+
                    BaseColumns.IDENTIFICATIVO_CLIENTE+","+BaseColumns.POSIZIONE_CORRENTE+"," +BaseColumns.SERVIZI_SPECIALI+")"+" VALUES(?,?,?,?,?,?)");
            statement.setString(1,pr.getProgressivo());
            if(pr.getOperatoreTelefonico()!=null) statement.setString(2,pr.getOperatoreTelefonico().getIdentificativo());
            statement.setString(3, Integer.toString(pr.getTaxi().getCodice()));
            statement.setString(4, pr.getCliente().getCodiceCliente());
            statement.setString(5,pr.getCliente().getPosizioneCorrente());
            statement.setString(6, Arrays.toString(pr.getServiziSpeciali()));
            statement.executeUpdate();
           return pr;
        } catch (SQLException e) {
            System.err.print("Exception of SQL");
            return null;
        }
    }

    public synchronized void eliminaPrenotazione(Prenotazione pr){
        try {
            String sql = "DELETE FROM "+BaseColumns.TAB_PRENOTAZIONI+" WHERE "+BaseColumns.PROGRESSIVO_PRENOTAZIONE+" = \""+pr.getProgressivo()+"\" ;";
            statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            System.err.println("Exception of SQL");
        }
    }

    public double richiediPosizioniETempiDiAttesa(){
        return 0.0;
    }





}
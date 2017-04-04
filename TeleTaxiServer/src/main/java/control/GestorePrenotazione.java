package control;

import com.google.gson.Gson;
import model.*;
import resources.BaseColumns;
import websource.DatabaseManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dn on 26/03/17.
 */
public class GestorePrenotazione {
    private Gson gs;
    private static GestorePrenotazione instance = null;
    private Connection connection;
    private Statement statement;
    private CopyOnWriteArrayList<Prenotazione> prenotazioni;


    public GestorePrenotazione(){
        gs = new Gson();
        prenotazioni = new CopyOnWriteArrayList<Prenotazione>();
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


    public Prenotazione[] getAllPrenotazioni() {
        ArrayList<Prenotazione> prenotazionesTmp = new ArrayList<Prenotazione>();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_PRENOTAZIONI);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String progressivoPrenotazione = rs.getString(BaseColumns.PROGRESSIVO_PRENOTAZIONE);
                String identificativoOperatore = rs.getString(BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO);
                OperatoreTelefonico operatoreTelefonico =(OperatoreTelefonico) GestoreStatistica.getInstance().findOperatoreById(identificativoOperatore).getInformazioni();
                int identificativoTaxi = rs.getInt(BaseColumns.IDENTIFICATIVO_TAXI);
                Taxi taxi = (Taxi) GestoreStatistica.getInstance().findTaxiByCodice(identificativoTaxi).getInformazioni();
                String identificativoCliente = rs.getString(BaseColumns.IDENTIFICATIVO_CLIENTE);
                Cliente cliente =(Cliente) GestoreStatistica.getInstance().findClienteByID(identificativoCliente).getInformazioni();
                String posizioneCorrente = rs.getString(BaseColumns.POSIZIONE_CORRENTE);
                String[] serviziSpeciali = gs.fromJson(rs.getString(BaseColumns.SERVIZI_SPECIALI), String[].class);
                java.util.Date dataPrenotazione = new java.util.Date(rs.getTimestamp(BaseColumns.DATA_PRENOTAZIONE).getTime());
                prenotazionesTmp.add(new Prenotazione(progressivoPrenotazione,cliente,operatoreTelefonico,taxi,posizioneCorrente,serviziSpeciali,0.0,dataPrenotazione));
            }
            return (Prenotazione[]) prenotazionesTmp.toArray(new Prenotazione[prenotazionesTmp.size()]);
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }catch(NullPointerException e){
            System.err.println("Errore durante l'esecuzione della query "+e.getMessage());
        }
        return null;
    }



    public synchronized Prenotazione inserisciPrenotazione(Prenotazione pr){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        prenotazioni.add(pr);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO "+ BaseColumns.TAB_PRENOTAZIONI+
                    "("+BaseColumns.PROGRESSIVO_PRENOTAZIONE+","+BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+","+BaseColumns.IDENTIFICATIVO_TAXI+","+
                    BaseColumns.IDENTIFICATIVO_CLIENTE+","+BaseColumns.POSIZIONE_CORRENTE+","+BaseColumns.SERVIZI_SPECIALI+"," +BaseColumns.DATA_PRENOTAZIONE+")"+" VALUES(?,?,?,?,?,?,?)");
            statement.setString(1,pr.getProgressivo());
            if(pr.getOperatoreTelefonico()!=null) statement.setString(2,pr.getOperatoreTelefonico().getIdentificativo());
            statement.setInt(3, pr.getTaxi().getCodice());
            statement.setString(4, pr.getCliente().getCodiceCliente());
            statement.setString(5,pr.getCliente().getPosizioneCorrente());
            statement.setString(6, gs.toJson(pr.getServiziSpeciali()));
            statement.setTimestamp(7,  new Timestamp(pr.getData().getTime()));
            statement.executeUpdate();
           return pr;
        } catch (SQLException e) {
            System.err.print("Exception of SQL");
            return null;
        }
    }

    public synchronized void eliminaPrenotazione(Prenotazione pr){
        try {
            prenotazioni.remove(pr);
            String sql = "DELETE FROM "+BaseColumns.TAB_PRENOTAZIONI+" WHERE "+BaseColumns.PROGRESSIVO_PRENOTAZIONE+" = \""+pr.getProgressivo()+"\" ;";
            statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            System.err.println("Exception of SQL in eliminazione prenotazione");
        }
    }

    public double richiediPosizioniETempiDiAttesa(Prenotazione pr){
        return 0.0;
    }

}

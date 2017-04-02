package control;

import com.google.gson.Gson;
import model.*;
import resources.BaseColumns;
import websource.DatabaseManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dn on 28/03/17.
 */
public class GestoreStatistica {

    private static GestoreStatistica instance = null;
    private Connection connection;
    private Statement statement;
    private CopyOnWriteArrayList<Statistica> statistiche;
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private Gson gs = new Gson();


    public GestoreStatistica(){
        statistiche = new CopyOnWriteArrayList<Statistica>();
        DatabaseManager db = DatabaseManager.getInstance();
        connection = db.getConnection();
    }

    //singleton
    public static synchronized GestoreStatistica getInstance(){
        if(instance==null) instance=new GestoreStatistica();
        return instance;
    }

    public synchronized Statistica findOperatoreById(String identificatoreOperatore){
        ArrayList<OperatoreTelefonico> operatoriTmp = new ArrayList<OperatoreTelefonico>();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_OPERATORI_TELEFONICI+" WHERE "
                    +BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+" = "+identificatoreOperatore);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = rs.getDate(BaseColumns.DATA_DI_NASCITA_PERSONA);
                String password = rs.getString(BaseColumns.PASSWORD);
                operatoriTmp.add(new OperatoreTelefonico(identificatoreOperatore, nome, cognome, dataDiNascita, password));
            }
            return new Statistica(OperatoreTelefonico.class, (OperatoreTelefonico[]) operatoriTmp.toArray());
        }catch (SQLException e){
            System.err.println(e.getErrorCode());
        }
        return null;
    }

    public synchronized Statistica findPrenotazioneByOperatoreEData(String identificatoreOperatore, Date data){
        PreparedStatement statement;
        ArrayList<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_PRENOTAZIONI+" WHERE "
                    +BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+" = "+identificatoreOperatore+" AND "+BaseColumns.DATA_PRENOTAZIONE+" = "+data);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String progressivo = rs.getString(BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO);
                int codiceTaxi = rs.getInt(BaseColumns.IDENTIFICATIVO_TAXI);
                String codiceCliente = rs.getString(BaseColumns.IDENTIFICATIVO_CLIENTE);
                String posizioneCorrente = rs.getString(BaseColumns.POSIZIONE_CORRENTE);
                String serviziSpeciali[] = gs.fromJson(rs.getString(BaseColumns.SERVIZI_SPECIALI), String[].class);
                prenotazioni.add(new Prenotazione(progressivo,((Cliente[]) findClienteByID(codiceCliente).getInformazioni())[0],((OperatoreTelefonico[]) findOperatoreById(identificatoreOperatore).getInformazioni())[0],
                        ((Taxi[]) (findTaxiByCodice(codiceTaxi).getInformazioni()))[0], posizioneCorrente, serviziSpeciali, 0.0,data));
            }
            return new Statistica(Prenotazione.class, (Prenotazione[]) prenotazioni.toArray());
        }catch (SQLException e){
            System.err.println(e.getErrorCode());
        }
        return null;
    }

    private Statistica findClienteByID(String codiceCliente) {
        ArrayList<Cliente> clientiTmp = new ArrayList<Cliente>();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_CLIENTE+" WHERE "
                    +BaseColumns.IDENTIFICATIVO_CLIENTE+" = "+codiceCliente);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = rs.getDate(BaseColumns.DATA_DI_NASCITA_PERSONA);
                int telefono = rs.getInt(BaseColumns.TELEFONO);
                clientiTmp.add(new Cliente(codiceCliente, nome, cognome,dataDiNascita, telefono,"N.P."));
            }
            return new Statistica(Cliente.class, (Cliente[]) clientiTmp.toArray());
        }catch (SQLException e){
            System.err.println(e.getErrorCode());
        }
        return null;
    }


    private Statistica findTaxiByCodice(int codiceTaxi) {
        ArrayList<Taxi> taxiTmp = new ArrayList<Taxi>();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_TAXI+" WHERE "
                    +BaseColumns.IDENTIFICATIVO_TAXI+" = "+codiceTaxi);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String stato = rs.getString(BaseColumns.STATO_TAXI);
                String destinazione = rs.getString(BaseColumns.DESTINAZIONE);
                String[] serviziSpeciali = gs.fromJson(rs.getString(BaseColumns.SERVIZI_SPECIALI), String[].class);
                String password = rs.getString(BaseColumns.PASSWORD);
                taxiTmp.add(new Taxi(codiceTaxi, stato, "N.P.",destinazione, serviziSpeciali));
            }
            return new Statistica(Taxi.class, (Taxi[]) taxiTmp.toArray());
        }catch (SQLException e){
            System.err.println(e.getErrorCode());
        }
        return null;
    }
}

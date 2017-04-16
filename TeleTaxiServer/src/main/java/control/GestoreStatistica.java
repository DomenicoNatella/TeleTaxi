package control;

import com.google.gson.Gson;
import model.*;
import resources.*;
import resources.exception.*;
import websource.DatabaseManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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


    public GestoreStatistica() throws ConnectionSQLFailException {
        statistiche = new CopyOnWriteArrayList<Statistica>();
        DatabaseManager db = DatabaseManager.getInstance();
        connection = db.getConnection();
    }

    //singleton
    public static synchronized GestoreStatistica getInstance() throws ConnectionSQLFailException {
        if(instance==null) instance=new GestoreStatistica();
        return instance;
    }


    public synchronized Statistica findOperatoreById(String identificatoreOperatore) throws FindOperatoreFailException {
        List<OperatoreTelefonico> operatoriTmp = new ArrayList<OperatoreTelefonico>();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_OPERATORI_TELEFONICI+" WHERE "
                    +BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+" = '"+identificatoreOperatore+"'");
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = rs.getTimestamp(BaseColumns.DATA_DI_NASCITA_PERSONA);
                String password = rs.getString(BaseColumns.PASSWORD);
                operatoriTmp.add(new OperatoreTelefonico(identificatoreOperatore, nome, cognome, dataDiNascita, password));
            }
            return new Statistica(OperatoreTelefonico.class, operatoriTmp);
        }catch (SQLException e){
            throw new FindOperatoreFailException(Integer.toString(e.getErrorCode()));
        }
    }

    public synchronized Statistica findTaxiBetterWaiting(Prenotazione prenotazione) throws ConnectionSQLFailException, GetTaxiFailException, FindPrenotazioneFailException {
        Taxi[] taxis = GestoreFlottaTaxi.getInstance().getAllTaxi();
        Taxi min = taxis[0];
        for(Taxi t: taxis){
            if(GestorePrenotazione.getInstance().richiediTempiDiAttesa(prenotazione.getPosizioneCliente(), t.getPosizioneCorrente()) <
                    GestorePrenotazione.getInstance().richiediTempiDiAttesa(prenotazione.getPosizioneCliente(), min.getPosizioneCorrente())
                    && t != prenotazione.getTaxi() && t.getStato().equalsIgnoreCase("libero"))
                min = t;
        }
        List<Taxi> toReturn = new ArrayList<Taxi>();
        toReturn.add(min);
        return new Statistica(Taxi.class, toReturn);
    }

    public synchronized Statistica findPrenotazioneByOperatoreEData(String identificatoreOperatore, Date data)
            throws FindPrenotazioneFailException, FindTaxiFailException, FindClienteFailException, FindOperatoreFailException {
        PreparedStatement statement;
        ArrayList<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_PRENOTAZIONI+" WHERE "
                    +BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+" = '"+identificatoreOperatore+"' AND "+BaseColumns.DATA_PRENOTAZIONE+" = "+data);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String progressivo = rs.getString(BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO);
                int codiceTaxi = rs.getInt(BaseColumns.IDENTIFICATIVO_TAXI);
                String codiceCliente = rs.getString(BaseColumns.IDENTIFICATIVO_CLIENTE);
                String posizioneCorrente = rs.getString(BaseColumns.POSIZIONE_CLIENTE);
                String destinazione = rs.getString(BaseColumns.DESTINAZIONE);
                String serviziSpeciali[] = gs.fromJson(rs.getString(BaseColumns.SERVIZI_SPECIALI), String[].class);
                boolean assegnata = Boolean.getBoolean(rs.getString(BaseColumns.PRENOTAZIONE_ASSEGNATA));
                prenotazioni.add(new Prenotazione(progressivo,(Cliente) findClienteByID(codiceCliente).getValues().get(0),
                        (OperatoreTelefonico) findOperatoreById(identificatoreOperatore).getValues().get(0),
                        (Taxi) findTaxiByCodice(codiceTaxi).getValues().get(0),destinazione,serviziSpeciali,posizioneCorrente, 0.0,data,assegnata));
            }
            return new Statistica(Prenotazione.class, prenotazioni);
        }catch (SQLException e){
            throw new FindPrenotazioneFailException(Integer.toString(e.getErrorCode()));
        }
    }

    public Statistica findClienteByID(String codiceCliente) throws FindClienteFailException {
        ArrayList<Cliente> clientiTmp = new ArrayList<Cliente>();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_CLIENTE+" WHERE "
                    +BaseColumns.IDENTIFICATIVO_CLIENTE+" = '"+codiceCliente+"'");
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = rs.getTimestamp(BaseColumns.DATA_DI_NASCITA_PERSONA);
                int telefono = rs.getInt(BaseColumns.TELEFONO);
                clientiTmp.add(new Cliente(codiceCliente, nome, cognome,dataDiNascita, telefono));
            }
            return new Statistica(Cliente.class, clientiTmp);
        }catch (SQLException e){
            throw new FindClienteFailException(Integer.toString(e.getErrorCode()));
        }
    }


    public Statistica findTaxiByCodice(int codiceTaxi) throws FindTaxiFailException {
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
                String posizioneCorrente = rs.getString(BaseColumns.POSIZIONE_CORRENTE);
                taxiTmp.add(new Taxi(codiceTaxi, stato, posizioneCorrente,destinazione, serviziSpeciali));
            }
            return new Statistica(Taxi.class, taxiTmp);
        }catch (SQLException e){
            throw new FindTaxiFailException(Integer.toString(e.getErrorCode()));
        }
    }
}

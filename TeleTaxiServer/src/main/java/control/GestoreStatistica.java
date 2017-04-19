package control;

import com.google.gson.Gson;
import model.*;
import resources.BaseColumns;
import resources.exception.*;
import websource.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dn on 28/03/17.
 */
public class GestoreStatistica {

    private static GestoreStatistica instance = null;
    private Connection connection;
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private Gson gs = new Gson();


    public GestoreStatistica() throws ConnectionSQLFailException {
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

    public synchronized Statistica findPrenotazioneByProgressivo(String progressivo)
            throws FindPrenotazioneFailException, FindTaxiFailException, FindOperatoreFailException, ConnectionSQLFailException, GetTaxiFailException, FindClienteFailException {
        List<Prenotazione> toReturn = new ArrayList<Prenotazione>();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM " + BaseColumns.TAB_PRENOTAZIONI + " WHERE "
                    + BaseColumns.PROGRESSIVO_PRENOTAZIONE + " = '" + progressivo + "'");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                OperatoreTelefonico op = (OperatoreTelefonico) findOperatoreById(rs.getString(BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO)).getValues().get(0);
                Taxi taxi = (Taxi) findTaxiByCodice(rs.getInt(BaseColumns.IDENTIFICATIVO_TAXI)).getValues().get(0);
                Cliente cliente = (Cliente) findClienteByID(rs.getString(BaseColumns.IDENTIFICATIVO_CLIENTE)).getValues().get(0);
                String posizioneCliente = rs.getString(BaseColumns.POSIZIONE_CLIENTE);
                String destinazione = rs.getString(BaseColumns.DESTINAZIONE);
                String[] serviziSpeciali = gs.fromJson(rs.getString(BaseColumns.SERVIZI_SPECIALI), String[].class);
                boolean assegnata = rs.getBoolean(BaseColumns.PRENOTAZIONE_ASSEGNATA);
                Date dataPrenotazione = new Date(rs.getTimestamp(BaseColumns.DATA_PRENOTAZIONE).getTime());
                if (taxi.getPrenotazione().getProgressivo().equalsIgnoreCase(progressivo) && taxi.getDestinazione().equalsIgnoreCase(posizioneCliente)) {
                    double attesa = GestorePrenotazione.getInstance().richiediTempiDiAttesa(taxi.getPosizioneCorrente(), posizioneCliente);
                    toReturn.add(new Prenotazione(progressivo, cliente, op, taxi, destinazione, serviziSpeciali, posizioneCliente, attesa, dataPrenotazione, assegnata));
                } else
                    toReturn.add(new Prenotazione(progressivo, cliente, op, taxi, destinazione, serviziSpeciali, posizioneCliente, -1.0, dataPrenotazione, assegnata));
            }
            return new Statistica(Prenotazione.class, toReturn);
        } catch (SQLException e) {
            throw new FindPrenotazioneFailException(Integer.toString(e.getErrorCode()));
        }

    }

    public synchronized Statistica findTaxiBetterWaiting(Prenotazione prenotazione) throws ConnectionSQLFailException, GetTaxiFailException, FindPrenotazioneFailException, FindTaxiFailException, FindOperatoreFailException, FindClienteFailException {
        Taxi[] taxis = GestoreFlottaTaxi.getInstance().getAllTaxi();
        List<Taxi> toReturn = new ArrayList<Taxi>();
        Taxi min = null;

        for(Taxi t: taxis){
            if (min == null && t.getStato().equalsIgnoreCase("libero")) min = t;
            else if (GestorePrenotazione.getInstance().richiediTempiDiAttesa(prenotazione.getPosizioneCliente(), t.getPosizioneCorrente()) <
                    GestorePrenotazione.getInstance().richiediTempiDiAttesa(prenotazione.getPosizioneCliente(), min.getPosizioneCorrente())
                    && t != prenotazione.getTaxi() && t.getStato().equalsIgnoreCase("libero"))
                min = t;
        }
        if (min == null) {
            for (Taxi t : taxis) {
                if (min == null && t.getStato().equalsIgnoreCase("occupato")) min = t;
                else if (GestorePrenotazione.getInstance().richiediTempiDiAttesa(prenotazione.getPosizioneCliente(), t.getPosizioneCorrente()) <
                        GestorePrenotazione.getInstance().richiediTempiDiAttesa(prenotazione.getPosizioneCliente(), min.getPosizioneCorrente())
                        && t != prenotazione.getTaxi() && t.getStato().equalsIgnoreCase("occupato"))
                    min = t;
            }
        }
        toReturn.add(min);
        return new Statistica(Taxi.class, toReturn);
    }

    public synchronized Statistica findPrenotazioneByOperatoreEData(String identificatoreOperatore, Date data)
            throws FindPrenotazioneFailException, FindTaxiFailException, FindClienteFailException, FindOperatoreFailException, GetTaxiFailException, ConnectionSQLFailException {
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


    public Statistica findTaxiByCodice(int codiceTaxi) throws FindTaxiFailException, GetTaxiFailException, FindClienteFailException, FindOperatoreFailException, FindPrenotazioneFailException, ConnectionSQLFailException {
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
                Prenotazione prenotazione = (Prenotazione) findPrenotazioneByProgressivo(rs.getString(BaseColumns.PROGRESSIVO_PRENOTAZIONE)).getValues().get(0);
                taxiTmp.add(new Taxi(codiceTaxi, stato, posizioneCorrente, destinazione, serviziSpeciali, prenotazione));
            }
            return new Statistica(Taxi.class, taxiTmp);
        }catch (SQLException e){
            throw new FindTaxiFailException(Integer.toString(e.getErrorCode()));
        }
    }
}

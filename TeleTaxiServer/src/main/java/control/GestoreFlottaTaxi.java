package control;

import com.google.gson.Gson;
import model.OperatoreTelefonico;
import model.Taxi;
import resources.*;
import websource.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dn on 28/03/17.
 */
public class GestoreFlottaTaxi {

    private CopyOnWriteArrayList<Taxi> taxi;
    private static GestoreFlottaTaxi instance = null;
    private Connection connection;
    private Statement statement;
    private Gson gson = new Gson();

    public GestoreFlottaTaxi() throws ConnectionSQLFailException {
        taxi = new CopyOnWriteArrayList<Taxi>();
        DatabaseManager db = DatabaseManager.getInstance();
        connection = db.getConnection();
        try {
            if(connection!=null) statement = (Statement) connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Errore nella creazione della connessione in GestorePrenotazione");
            throw new ConnectionSQLFailException(Integer.toString(e.getErrorCode()));
        }catch (NullPointerException e){
            System.err.println("Nessuna connessione");
        }
    }

    //singleton
    public static synchronized GestoreFlottaTaxi getInstance() throws ConnectionSQLFailException {
        if(instance==null) instance=new GestoreFlottaTaxi();
        return instance;
    }

    public synchronized Taxi[] getAllTaxi() throws GetTaxiFailException {
        ArrayList<Taxi> taxiTmp = new ArrayList<Taxi>();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_TAXI);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                int identificativoTaxi = rs.getInt(BaseColumns.IDENTIFICATIVO_TAXI);
                String[] serviziSpeciali = gson.fromJson(rs.getString(BaseColumns.SERVIZI_SPECIALI), String[].class);
                String destinazione = rs.getString(BaseColumns.DESTINAZIONE);
                String posizioneCorrente = rs.getString(BaseColumns.POSIZIONE_CORRENTE);
                String statoTaxi = rs.getString(BaseColumns.STATO_TAXI);
                taxiTmp.add(new Taxi(identificativoTaxi,statoTaxi,posizioneCorrente,destinazione,serviziSpeciali));
            }
            return taxiTmp.toArray(new Taxi[taxiTmp.size()]);
        }catch (SQLException e){
           throw new GetTaxiFailException(Integer.toString(e.getErrorCode()));
        }
    }

    public synchronized Taxi inserisciTaxi(Taxi tx) throws InserisciTaxiFailException {
        taxi.add(tx);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO "+ BaseColumns.TAB_TAXI+
                    "("+BaseColumns.IDENTIFICATIVO_TAXI+","+BaseColumns.SERVIZI_SPECIALI+","+BaseColumns.DESTINAZIONE+","+
                    BaseColumns.STATO_TAXI+")"+" VALUES(?,?,?,?)");
            statement.setInt(1, tx.getCodice());
            statement.setString(2, gson.toJson(tx.getServiziSpeciali(), String[].class));
            statement.setString(3, tx.getDestinazione());
            statement.setString(4, tx.getStato());
            statement.executeUpdate();
            return tx;
        } catch (SQLException e) {
            throw new InserisciTaxiFailException(tx.toString());
        }
    }

    public synchronized void eliminaTaxi(Taxi tx) throws EliminaTaxiFailException {
        try {
            taxi.remove(tx);
            String sql = "DELETE FROM "+BaseColumns.TAB_TAXI+" WHERE "+BaseColumns.IDENTIFICATIVO_TAXI+" = \""+tx.getCodice()+"\" ;";
            statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            throw new EliminaTaxiFailException(tx.toString());
        }
    }

    public synchronized Taxi updateTaxi(Taxi tx) throws UpdateTaxiFailException {
        try{
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE " + BaseColumns.TAB_TAXI + " SET "+ BaseColumns.STATO_TAXI + " = ?," + BaseColumns.DESTINAZIONE + " = ?," + BaseColumns.POSIZIONE_CORRENTE + " = ?,"
                            + BaseColumns.SERVIZI_SPECIALI + " = ?," + "WHERE" + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + " = ?");
            ps.setString(1, tx.getStato());
            ps.setString(2, tx.getDestinazione());
            ps.setString(3, tx.getPosizioneCorrente());
            ps.setString(4, gson.toJson(tx.getServiziSpeciali(), String[].class));
            ps.setInt(5, tx.getCodice());
            return tx;
        }  catch (SQLException e) {
            throw new UpdateTaxiFailException(Integer.toString(e.getErrorCode()));
        }
    }

}

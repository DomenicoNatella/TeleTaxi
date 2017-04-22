package control;

import com.google.gson.Gson;
import model.Taxi;
import resources.BaseColumns;
import resources.exception.*;
import websource.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by dn on 28/03/17.
 */
public class GestoreFlottaTaxi {

    private static GestoreFlottaTaxi instance = null;
    private Connection connection;
    private Statement statement;
    private Gson gson = new Gson();

    public GestoreFlottaTaxi() throws ConnectionSQLFailException {
        DatabaseManager db = DatabaseManager.getInstance();
        connection = db.getConnection();
        try {
            if(connection!=null) statement = (Statement) connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Errore nella creazione della connessione in gestore prenotazione");
            throw new ConnectionSQLFailException(Integer.toString(e.getErrorCode()));
        }catch (NullPointerException e){
            System.err.println("Nessuna connessione");
            throw new ConnectionSQLFailException(e.getMessage());
        } catch (Exception e) {
            throw new ConnectionSQLFailException(e.getMessage());
        }
    }

    //singleton
    public static synchronized GestoreFlottaTaxi getInstance() throws ConnectionSQLFailException {
        if(instance==null) instance=new GestoreFlottaTaxi();
        return instance;
    }

    public synchronized Taxi[] getAllTaxi() throws GetTaxiFailException, ConnectionSQLFailException, FindPrenotazioneFailException, FindOperatoreFailException, FindTaxiFailException, FindClienteFailException {
        ArrayList<Taxi> taxiTmp = new ArrayList<Taxi>();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT * FROM "+BaseColumns.TAB_TAXI);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int identificativoTaxi = rs.getInt(BaseColumns.IDENTIFICATIVO_TAXI);
                String[] serviziSpeciali = gson.fromJson(rs.getString(BaseColumns.SERVIZI_SPECIALI), String[].class);
                String destinazione = rs.getString(BaseColumns.DESTINAZIONE);
                String posizioneCorrente = rs.getString(BaseColumns.POSIZIONE_CORRENTE);
                String statoTaxi = rs.getString(BaseColumns.STATO_TAXI);
                String prenotazione = rs.getString(BaseColumns.PROGRESSIVO_PRENOTAZIONE);
                taxiTmp.add(new Taxi(identificativoTaxi, statoTaxi, posizioneCorrente, destinazione, serviziSpeciali, prenotazione));
            }
            return taxiTmp.toArray(new Taxi[taxiTmp.size()]);
        }catch (SQLException e){
            throw new GetTaxiFailException(Integer.toString(e.getErrorCode()));
        } catch (Exception e) {
            throw new GetTaxiFailException(e.getMessage());
        }
    }

    public synchronized Taxi inserisciTaxi(Taxi tx) throws InserisciTaxiFailException {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO "+ BaseColumns.TAB_TAXI+
                    "("+BaseColumns.IDENTIFICATIVO_TAXI+","+BaseColumns.SERVIZI_SPECIALI+","+BaseColumns.DESTINAZIONE+","+ BaseColumns.POSIZIONE_CORRENTE+","+
                    BaseColumns.STATO_TAXI + "," + BaseColumns.PROGRESSIVO_PRENOTAZIONE + ")" + " VALUES(?,?,?,?,?,?)");
            statement.setInt(1, tx.getCodice());
            statement.setString(2, gson.toJson(tx.getServiziSpeciali(), String[].class));
            statement.setString(3, tx.getDestinazione());
            statement.setString(4, tx.getPosizioneCorrente());
            statement.setString(5, tx.getStato());
            statement.setString(6, tx.getPrenotazione());
            statement.executeUpdate();
            return tx;
        } catch (SQLException e) {
            throw new InserisciTaxiFailException(Integer.toString(e.getErrorCode()));
        } catch (Exception e) {
            throw new InserisciTaxiFailException(e.getMessage());
        }
    }

    public synchronized void eliminaTaxi(Taxi tx) throws EliminaTaxiFailException {
        try {
            String sql = "DELETE FROM "+BaseColumns.TAB_TAXI+" WHERE "+BaseColumns.IDENTIFICATIVO_TAXI+" = "+tx.getCodice()+" ;";
            statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            throw new EliminaTaxiFailException(tx.toString());
        } catch (Exception e) {
            throw new EliminaTaxiFailException(e.getMessage());
        }
    }

    public synchronized Taxi updateTaxi(Taxi tx) throws UpdateTaxiFailException {
        try{
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE " + BaseColumns.TAB_TAXI + " SET " + BaseColumns.STATO_TAXI + " = ?, " + BaseColumns.DESTINAZIONE + " = ?, "
                            + BaseColumns.POSIZIONE_CORRENTE + " = ?, "
                            + BaseColumns.SERVIZI_SPECIALI + " = ?, " + BaseColumns.PROGRESSIVO_PRENOTAZIONE + " = ? " + " WHERE " + BaseColumns.IDENTIFICATIVO_TAXI + " = ?");
            ps.setString(1, tx.getStato());
            ps.setString(2, tx.getDestinazione());
            ps.setString(3, tx.getPosizioneCorrente());
            ps.setString(4, gson.toJson(tx.getServiziSpeciali(), String[].class));
            ps.setString(5, tx.getPrenotazione());
            ps.setInt(6, tx.getCodice());
            ps.executeUpdate();
            return tx;
        }  catch (SQLException e) {
            throw new UpdateTaxiFailException(Integer.toString(e.getErrorCode()));
        } catch (Exception e) {
            throw new UpdateTaxiFailException(e.getMessage());
        }
    }

}

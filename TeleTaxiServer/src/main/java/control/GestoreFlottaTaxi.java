package control;

import model.Taxi;
import resources.BaseColumns;
import websource.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dn on 28/03/17.
 */
public class GestoreFlottaTaxi {

    private CopyOnWriteArrayList<Taxi> taxi;
    private static GestoreFlottaTaxi instance = null;
    private Connection connection;
    private Statement statement;

    public GestoreFlottaTaxi(){
        taxi = new CopyOnWriteArrayList<Taxi>();
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
    public static synchronized GestoreFlottaTaxi getInstance(){
        if(instance==null) instance=new GestoreFlottaTaxi();
        return instance;
    }

    public synchronized Taxi inserisciTaxi(Taxi tx){
        taxi.add(tx);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO "+ BaseColumns.TAB_TAXI+
                    "("+BaseColumns.IDENTIFICATIVO_TAXI+","+BaseColumns.SERVIZI_SPECIALI+","+BaseColumns.DESTINAZIONE+","+
                    BaseColumns.STATO_TAXI+")"+" VALUES(?,?,?,?)");
            statement.setString(1, Integer.toString(tx.getCodice()));
            statement.setString(2, Arrays.toString(tx.getServiziSpeciali()));
            statement.setString(3, tx.getDestinazione());
            statement.setString(4, tx.getStato());
            statement.executeUpdate();
            return tx;
        } catch (SQLException e) {
            System.err.print("Exception of SQL");
            return null;
        }
    }

    public synchronized void eliminaTaxi(Taxi tx){
        try {
            taxi.remove(tx);
            String sql = "DELETE FROM "+BaseColumns.TAB_TAXI+" WHERE "+BaseColumns.IDENTIFICATIVO_TAXI+" = \""+tx.getCodice()+"\" ;";
            statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            System.err.println("Exception of SQL");
        }
    }


}

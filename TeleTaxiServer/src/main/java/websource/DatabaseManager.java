package websource;

import resources.BaseColumns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dn on 26/03/17.
 */
public class DatabaseManager {

    private static DatabaseManager instance = null;
    private Connection connect;
    private Statement stmt;
    private String strConnect = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&INTtech?useSSL=true";

    private DatabaseManager(){
        createDatabase();
    }

    //singleton
    public static synchronized DatabaseManager getInstance(){
        if(instance==null) instance=new DatabaseManager();
        return instance;
    }

    public synchronized Connection getConnection(){
        return connect;
    }

    //private method
    private void createDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            String username = "root";
            String password = "t3l3t4x1";
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+BaseColumns.DB_NAME+strConnect, username, password);
            stmt = (Statement) connect.createStatement();
            stmt.executeUpdate("DROP DATABASE IF EXISTS"+BaseColumns.DB_NAME+";");
            stmt.executeUpdate("CREATE DATABASE "+ BaseColumns.DB_NAME+";");
            createTable();
        } catch (ClassNotFoundException e) {} catch (SQLException e) {} catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }finally{
            if(stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTable() {
        try {
            String sql_area = "CREATE TABLE "+BaseColumns.TAB_OPERATORI_TELEFONICI+
                    " ("+ BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+" PRIMARY KEY VARCHAR(255)," +
                    BaseColumns.NOME_PERSONA+" VARCHAR(255)," +
                    BaseColumns.COGNOME_PERSONA+" VARCHAR(255)," +
                    BaseColumns.DATA_DI_NASCITA_PERSONA+" DATE," +
                    BaseColumns.PASSWORD+" VARCHAR(255) ) ";
            stmt.executeUpdate(sql_area);

            String sql_cliente = "CREATE TABLE "+BaseColumns.TAB_CLIENTE+
                    " ("+BaseColumns.IDENTIFICATIVO_CLIENTE+" PRIMARY KEY VARCHAR(255),"+
                    BaseColumns.NOME_PERSONA+" VARCHAR(255),"+
                    BaseColumns.COGNOME_PERSONA+" VARCHAR(255),"+
                    BaseColumns.DATA_DI_NASCITA_PERSONA+" DATE,"+
                    BaseColumns.TELEFONO+" VARCHAR(255)) ";
            stmt.executeUpdate(sql_cliente);

            String sql_bike = "CREATE TABLE "+BaseColumns.TAB_PRENOTAZIONI+
                    " ("+ BaseColumns.PROGRESSIVO_PRENOTAZIONE+" PRIMARY KEY VARCHAR(255)," +
                    BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+" FOREIGN KEY VARCHAR(255)," +
                    BaseColumns.IDENTIFICATIVO_TAXI+" VARCHAR(255)," +
                    BaseColumns.IDENTIFICATIVO_CLIENTE+" FOREIGN KEY VARCHAR(255)," +
                    BaseColumns.POSIZIONE_CORRENTE+" VARCHAR(255)," +
                    BaseColumns.SERVIZI_SPECIALI+" VARCHAR(255)," +
                    BaseColumns.DATA_PRENOTAZIONE+" DATE )";
            stmt.executeUpdate(sql_bike);

            String sql_car = "CREATE TABLE "+BaseColumns.TAB_TAXI+
                    " ("+ BaseColumns.IDENTIFICATIVO_TAXI+" PRIMARY KEY VARCHAR(255)," +
                    BaseColumns.STATO_TAXI+" VARCHAR(255)," +
                    BaseColumns.DESTINAZIONE+" VARCHAR(255)," +
                    BaseColumns.SERVIZI_SPECIALI+" VARCHAR(255) ) ";
            stmt.executeUpdate(sql_car);

            String sql_cintea = "CREATE TABLE "+BaseColumns.TAB_MANAGER+
                    " ("+BaseColumns.USERNAME_MANAGER+" PRIMARY KEY VARCHAR(255)," +
                    BaseColumns.NOME_PERSONA+" VARCHAR(255)," +
                    BaseColumns.COGNOME_PERSONA+" VARCHAR(255)," +
                    BaseColumns.DATA_DI_NASCITA_PERSONA+" DATE ) ";
            stmt.executeUpdate(sql_cintea);

        } catch ( Exception e ) {
            System.err.println(  "Errore creazione tabella DataBaseSystem: " + e.getMessage() );
            System.exit(0);
        }finally {
            try {
                if(stmt!=null)   stmt.close();
                if(connect!=null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}

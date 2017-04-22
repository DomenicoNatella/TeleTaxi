package websource;

import resources.BaseColumns;
import resources.exception.ConnectionSQLFailException;

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
    private String strConnect = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&INTtech&useSSL=true";

    private DatabaseManager() throws ConnectionSQLFailException {
        createDatabase();
    }

    //singleton
    public static synchronized DatabaseManager getInstance() throws ConnectionSQLFailException {
        if(instance==null) instance=new DatabaseManager();
        return instance;
    }

    public synchronized Connection getConnection(){
        return connect;
    }

    //private method
    private void createDatabase() throws ConnectionSQLFailException {
        String username = "root";
        String password = "t3l3t4x1";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+strConnect, username, password);
            if(connect != null) System.err.println("Creating database...");
            stmt = (Statement) connect.createStatement();
            stmt.executeUpdate("CREATE DATABASE "+ BaseColumns.DB_NAME);
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + BaseColumns.DB_NAME + strConnect, username, password);
            stmt = connect.createStatement();
            createTable();

        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
            if (e.getErrorCode() == 1007) {
                try {
                    connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + BaseColumns.DB_NAME + strConnect, username, password);
                    stmt = connect.createStatement();
                } catch (SQLException exception) {
                    throw new ConnectionSQLFailException(exception.getSQLState());
                }
            }
        } catch (IllegalAccessException e) {
            throw new ConnectionSQLFailException(e.getMessage());
        } catch (InstantiationException e) {
            throw new ConnectionSQLFailException(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void createTable() throws ConnectionSQLFailException {
        try {
            String sql_area = "CREATE TABLE "+BaseColumns.TAB_OPERATORI_TELEFONICI+
                    " ("+ BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+" VARCHAR(255) PRIMARY KEY ," +
                    BaseColumns.NOME_PERSONA+" VARCHAR(255)," +
                    BaseColumns.COGNOME_PERSONA+" VARCHAR(255)," +
                    BaseColumns.DATA_DI_NASCITA_PERSONA+" TIMESTAMP ," +
                    BaseColumns.PASSWORD+" VARCHAR(255) ) ";
            stmt.executeUpdate(sql_area);

            String sql_cliente = "CREATE TABLE "+BaseColumns.TAB_CLIENTE+
                    " ("+BaseColumns.IDENTIFICATIVO_CLIENTE+" VARCHAR(255) PRIMARY KEY ,"+
                    BaseColumns.NOME_PERSONA+" VARCHAR(255),"+
                    BaseColumns.COGNOME_PERSONA+" VARCHAR(255),"+
                    BaseColumns.DATA_DI_NASCITA_PERSONA+" TIMESTAMP ,"+
                    BaseColumns.TELEFONO+" INTEGER ) ";
            stmt.executeUpdate(sql_cliente);

            String sql_cintea = "CREATE TABLE "+BaseColumns.TAB_MANAGER+
                    " ("+BaseColumns.USERNAME_MANAGER+" VARCHAR(255) PRIMARY KEY ," +
                    BaseColumns.NOME_PERSONA+" VARCHAR(255)," +
                    BaseColumns.COGNOME_PERSONA+" VARCHAR(255)," +
                    BaseColumns.PASSWORD+" VARCHAR(255)," +
                    BaseColumns.DATA_DI_NASCITA_PERSONA+" TIMESTAMP ) ";
            stmt.executeUpdate(sql_cintea);

            String sql_bike = "CREATE TABLE "+BaseColumns.TAB_PRENOTAZIONI+
                    " ("+ BaseColumns.PROGRESSIVO_PRENOTAZIONE+" VARCHAR(255) PRIMARY KEY ," +
                    BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + " VARCHAR(255) NULL ," +
                    BaseColumns.IDENTIFICATIVO_TAXI + " INTEGER NULL," +
                    BaseColumns.IDENTIFICATIVO_CLIENTE+" VARCHAR(255) ," +
                    BaseColumns.POSIZIONE_CLIENTE+" VARCHAR(255)," +
                    BaseColumns.DESTINAZIONE+" VARCHAR(255)," +
                    BaseColumns.SERVIZI_SPECIALI+" VARCHAR(255)," +
                    BaseColumns.PRENOTAZIONE_ASSEGNATA+" VARCHAR(6),"+
                    BaseColumns.DATA_PRENOTAZIONE + " TIMESTAMP )";
            stmt.executeUpdate(sql_bike);

            String sql_car = "CREATE TABLE " + BaseColumns.TAB_TAXI +
                    " (" + BaseColumns.IDENTIFICATIVO_TAXI + " INTEGER PRIMARY KEY ," +
                    BaseColumns.STATO_TAXI + " VARCHAR(255)," +
                    BaseColumns.DESTINAZIONE + " VARCHAR(255)," +
                    BaseColumns.POSIZIONE_CORRENTE + " VARCHAR(255)," +
                    BaseColumns.SERVIZI_SPECIALI + " VARCHAR(1000)," +
                    BaseColumns.PROGRESSIVO_PRENOTAZIONE + " VARCHAR(255) NULL )";
            stmt.executeUpdate(sql_car);

            String sql_alter_prenotazioni = "ALTER TABLE " + BaseColumns.TAB_PRENOTAZIONI + " ADD FOREIGN KEY(" + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + ")" +
                    " REFERENCES " + BaseColumns.TAB_OPERATORI_TELEFONICI +
                    "(" + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + ") ON DELETE SET NULL;";
            stmt.executeUpdate(sql_alter_prenotazioni);
            String sql_alter_prenotazioni_taxi = "ALTER TABLE " + BaseColumns.TAB_PRENOTAZIONI + " ADD FOREIGN KEY(" + BaseColumns.IDENTIFICATIVO_TAXI + ") REFERENCES " + BaseColumns.TAB_TAXI
                    + "(" + BaseColumns.IDENTIFICATIVO_TAXI + ") ON DELETE SET NULL;";
            stmt.executeUpdate(sql_alter_prenotazioni_taxi);
            String sql_alter_prenotazioni_cliente = "ALTER TABLE " + BaseColumns.TAB_PRENOTAZIONI + " ADD FOREIGN KEY(" + BaseColumns.IDENTIFICATIVO_CLIENTE + ") REFERENCES " + BaseColumns.TAB_CLIENTE
                    + "(" + BaseColumns.IDENTIFICATIVO_CLIENTE + ") ON DELETE SET NULL; ";
            stmt.executeUpdate(sql_alter_prenotazioni_cliente);
            String sql_alter_taxi = "ALTER TABLE " + BaseColumns.TAB_TAXI + " ADD FOREIGN KEY(" + BaseColumns.PROGRESSIVO_PRENOTAZIONE + ")" +
                    " REFERENCES " + BaseColumns.TAB_PRENOTAZIONI
                    + "(" + BaseColumns.PROGRESSIVO_PRENOTAZIONE + ") ON DELETE SET NULL;";
            stmt.executeUpdate(sql_alter_taxi);
        } catch (Exception e) {
            throw new ConnectionSQLFailException(e.getMessage());
        }
    }
}

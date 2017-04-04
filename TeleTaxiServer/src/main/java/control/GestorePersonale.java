package
control;

import com.fasterxml.jackson.databind.ser.Serializers;
import model.Manager;
import model.OperatoreTelefonico;
import resources.BaseColumns;
import websource.DatabaseManager;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dn on 28/03/17.
 */
public class GestorePersonale {

    private static GestorePersonale instance = null;
    private Connection connection;
    private Statement statement;
    private Manager manager;
    private CopyOnWriteArrayList<OperatoreTelefonico> operatoriTelefonici;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    public GestorePersonale() {
        DatabaseManager db = DatabaseManager.getInstance();
        connection = db.getConnection();
        try {
            if (connection != null) statement = (Statement) connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Errore nella creazione della connessione in GestorePrenotazione");
        } catch (NullPointerException e) {
            System.err.println("Nessuna connessione");
        }
        manager = Manager.getInstance();
    }

    public static GestorePersonale getInstance() {
        if (instance == null) return instance = new GestorePersonale();
        else return instance;
    }

    public synchronized OperatoreTelefonico[] getAllOperatoriTelefonici() {
        ArrayList<OperatoreTelefonico> operatoreTelefonicoTmp = new ArrayList<OperatoreTelefonico>();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM " + BaseColumns.TAB_OPERATORI_TELEFONICI);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String identificativoOperatore = rs.getString(BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO);
                String nomeOperatore = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = rs.getDate(BaseColumns.DATA_DI_NASCITA_PERSONA);
                String password = rs.getString(BaseColumns.PASSWORD);
                operatoreTelefonicoTmp.add(new OperatoreTelefonico(identificativoOperatore, nomeOperatore, cognome, dataDiNascita, password));
            }
            return (OperatoreTelefonico[]) operatoreTelefonicoTmp.toArray(new OperatoreTelefonico[operatoreTelefonicoTmp.size()]);
        } catch (SQLException e) {
            System.err.println(e.getErrorCode());
        }
        return null;
    }

    public synchronized OperatoreTelefonico findOperatore(String identificativoOperatore) {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM " + BaseColumns.TAB_OPERATORI_TELEFONICI + " WHERE " + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + " = " + identificativoOperatore);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = new Date();
                dataDiNascita.setTime(rs.getTimestamp(BaseColumns.DATA_DI_NASCITA_PERSONA).getTime());
                String password = BaseColumns.PASSWORD;
                return new OperatoreTelefonico(identificativoOperatore, nome, cognome, dataDiNascita, password);
            }
        } catch (SQLException e) {
            System.err.println("Exception of SQL in WHERE Clause");
        }
        return null;
    }


    public synchronized Manager findManager(String usernameManager) {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM " + BaseColumns.TAB_MANAGER + " WHERE " + BaseColumns.USERNAME_MANAGER + " = " + usernameManager);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = new Date();
                dataDiNascita.setTime(rs.getTimestamp(BaseColumns.DATA_DI_NASCITA_PERSONA).getTime());
                String password = BaseColumns.PASSWORD;
                return new Manager(nome, cognome, dataDiNascita, usernameManager, password);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public synchronized OperatoreTelefonico inserisciOperatoreTelefonico(OperatoreTelefonico op) {
        return manager.addOperatoreTelefonico(op, connection);
    }
}
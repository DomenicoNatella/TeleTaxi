package
control;

import model.Manager;
import model.OperatoreTelefonico;
import resources.*;
import websource.DatabaseManager;

import java.sql.*;
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


    public GestorePersonale() throws ConnectionSQLFailException {
        DatabaseManager db = DatabaseManager.getInstance();
        connection = db.getConnection();
        try {
            if (connection != null) statement = (Statement) connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Errore nella creazione della connessione in GestorePrenotazione");
            throw new ConnectionSQLFailException(Integer.toString(e.getErrorCode()));
        } catch (NullPointerException e) {
            System.err.println("Nessuna connessione");
        }
        manager = Manager.getInstance();
    }

    public static GestorePersonale getInstance() throws ConnectionSQLFailException {
        if (instance == null) return instance = new GestorePersonale();
        else return instance;
    }

    public synchronized OperatoreTelefonico[] getAllOperatoriTelefonici() throws GetOperatoriTelefoniciFailException {
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
            throw new GetOperatoriTelefoniciFailException(Integer.toString(e.getErrorCode()));
        }
    }

    public synchronized OperatoreTelefonico findOperatore(String identificativoOperatore) throws FindOperatoreFailException {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM " + BaseColumns.TAB_OPERATORI_TELEFONICI + " WHERE " + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + " = " + identificativoOperatore);
            ResultSet rs = statement.executeQuery();
            OperatoreTelefonico op = null;
            while (rs.next()) {
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = new Date();
                dataDiNascita.setTime(rs.getTimestamp(BaseColumns.DATA_DI_NASCITA_PERSONA).getTime());
                String password = BaseColumns.PASSWORD;
                op =  new OperatoreTelefonico(identificativoOperatore, nome, cognome, dataDiNascita, password);
            }
            return op;
        } catch (SQLException e) {throw new FindOperatoreFailException(Integer.toString(e.getErrorCode()));
        } catch (NullPointerException e){throw new FindOperatoreFailException(e.getMessage());}
    }


    public synchronized Manager findManager(String usernameManager) throws FindManagerFailException {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM " + BaseColumns.TAB_MANAGER + " WHERE " + BaseColumns.USERNAME_MANAGER + " = " + usernameManager);
            ResultSet rs = statement.executeQuery();
            Manager manager = null;
            while (rs.next()) {
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = new Date();
                dataDiNascita.setTime(rs.getTimestamp(BaseColumns.DATA_DI_NASCITA_PERSONA).getTime());
                String password = BaseColumns.PASSWORD;
                manager = new Manager(nome, cognome, dataDiNascita, usernameManager, password);
            }
            return manager;
        } catch (SQLException e) {
            throw new FindManagerFailException(usernameManager);
        }

    }

    public synchronized OperatoreTelefonico inserisciOperatoreTelefonico(OperatoreTelefonico op) throws InserisciOperatoreFailException {
        return manager.addOperatoreTelefonico(op, connection);
    }
}
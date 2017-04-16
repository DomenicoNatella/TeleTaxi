package
control;

import model.Manager;
import model.OperatoreTelefonico;
import org.restlet.engine.util.Base64;
import resources.*;
import resources.exception.*;
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
    private CopyOnWriteArrayList<OperatoreTelefonico> operatoriTelefonici;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    public GestorePersonale() throws ConnectionSQLFailException, FindManagerFailException, InserisciManagerFailException {
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
    }

    public static GestorePersonale getInstance() throws ConnectionSQLFailException, FindManagerFailException, InserisciManagerFailException {
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
            statement = connection.prepareStatement("SELECT * FROM " + BaseColumns.TAB_OPERATORI_TELEFONICI + " WHERE " +
                    BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + " = '" + identificativoOperatore+"'");
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


    public synchronized Manager findManager(String usernameManager) throws FindManagerFailException, ConnectionSQLFailException, InserisciManagerFailException {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM " + BaseColumns.TAB_MANAGER + " WHERE " + BaseColumns.USERNAME_MANAGER + " = '" + usernameManager+"'");
            ResultSet rs = statement.executeQuery();
            Manager manager = new Manager();
            while (rs.next()) {
                manager.setUsername(usernameManager);
                manager.setNome(rs.getString(BaseColumns.NOME_PERSONA));
                manager.setCognome(rs.getString(BaseColumns.COGNOME_PERSONA));
                Date dataDiNascita = new Date();
                dataDiNascita.setTime(rs.getTimestamp(BaseColumns.DATA_DI_NASCITA_PERSONA).getTime());
                manager.setDataDiNascita(dataDiNascita);
                manager.setPassword(new String(Base64.decode(rs.getString(BaseColumns.PASSWORD))));
            }
            return manager;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new FindManagerFailException(usernameManager);
        }

    }

    public synchronized OperatoreTelefonico eliminaOperatoreTelefonico(String identificativo)
            throws FindOperatoreFailException, EliminaOperatoreTelefonicoFailException, InserisciManagerFailException, FindManagerFailException, ConnectionSQLFailException {
        OperatoreTelefonico op = findOperatore(identificativo);
        OperatoreTelefonico deleted = Manager.getInstance().eliminaOperatoreTelefonico(op);
        return deleted;
    }

    public synchronized OperatoreTelefonico inserisciOperatoreTelefonico(OperatoreTelefonico op)
            throws InserisciOperatoreFailException, InserisciManagerFailException, FindManagerFailException, ConnectionSQLFailException {
        return Manager.getInstance().addOperatoreTelefonico(op);
    }

    public synchronized OperatoreTelefonico updateOperatoreTelefonico(OperatoreTelefonico op) throws UpdateOperatoreTelefonicoFailException {
        try{
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE " + BaseColumns.TAB_OPERATORI_TELEFONICI + " SET "+ BaseColumns.NOME_PERSONA + " = ?," +
                            BaseColumns.COGNOME_PERSONA + " = ?," + BaseColumns.DATA_DI_NASCITA_PERSONA + " = ?,"
                            + BaseColumns.PASSWORD + " = ?," + "WHERE" + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + " = ?");
            ps.setString(1, op.getNome());
            ps.setString(2, op.getCognome());
            ps.setTimestamp(3, new java.sql.Timestamp(op.getDataDiNascita().getTime()));
            ps.setString(4, op.getPassword());
            ps.setString(5, op.getIdentificativo());
            for(OperatoreTelefonico operatoreTelefonico: operatoriTelefonici)
                if(op.getIdentificativo().equalsIgnoreCase(operatoreTelefonico.getIdentificativo()))
                    operatoriTelefonici.set(operatoriTelefonici.indexOf(operatoreTelefonico), op);
            return op;
        }  catch (SQLException e) {
            throw new UpdateOperatoreTelefonicoFailException(Integer.toString(e.getErrorCode()));
        }
    }

    public synchronized Manager updateManager(Manager manager) throws UpdateManagerFailException {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE " + BaseColumns.TAB_MANAGER + " SET "+ BaseColumns.NOME_PERSONA + " = ?," + BaseColumns.COGNOME_PERSONA
                            + " = ?," + BaseColumns.DATA_DI_NASCITA_PERSONA + " = ?,"
                            + BaseColumns.PASSWORD + " = ?," + " WHERE " + BaseColumns.USERNAME_MANAGER + " = ?");
            ps.setString(1, manager.getNome());
            ps.setString(2, manager.getCognome());
            ps.setTimestamp(3, new java.sql.Timestamp(manager.getDataDiNascita().getTime()));
            ps.setString(4, manager.getPassword());
            ps.setString(5, manager.getUsername());
            return manager;
        }  catch (SQLException e) {
            throw new UpdateManagerFailException(Integer.toString(e.getErrorCode()));
        }
    }
}
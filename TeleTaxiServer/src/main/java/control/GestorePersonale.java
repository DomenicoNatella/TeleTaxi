package control;

import model.*;
import resources.BaseColumns;
import websource.DatabaseManager;

import java.util.Arrays;
import java.util.Date;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    public GestorePersonale(){
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

    public synchronized OperatoreTelefonico findOperatore(String identificativoOperatore){
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT *"+" FROM "+ BaseColumns.TAB_OPERATORI_TELEFONICI+" WHERE "+BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+" = "+identificativoOperatore);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = null;
                try {
                    dataDiNascita = df.parse(rs.getString(BaseColumns.DATA_DI_NASCITA_PERSONA));
                } catch (ParseException e) {
                    System.err.println("Errore nel parsing della data");
                }
                String password = BaseColumns.PASSWORD;
                return new OperatoreTelefonico(identificativoOperatore, nome, cognome, dataDiNascita, password);
            }
        }catch (SQLException e){
            System.err.println("Exception of SQL in WHERE Clause");
        }
        return null;
    }


    public synchronized Manager findManager(String usernameManager){
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("SELECT *"+" FROM "+ BaseColumns.TAB_MANAGER+" WHERE "+BaseColumns.USERNAME_MANAGER+" = "+usernameManager);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                String nome = rs.getString(BaseColumns.NOME_PERSONA);
                String cognome = rs.getString(BaseColumns.COGNOME_PERSONA);
                Date dataDiNascita = null;
                try {
                    dataDiNascita = df.parse(rs.getString(BaseColumns.DATA_DI_NASCITA_PERSONA));
                } catch (ParseException e) {
                    System.err.println("Errore nel parsing della data");
                }
                String password = BaseColumns.PASSWORD;
                return new Manager(nome, cognome, dataDiNascita, usernameManager ,password);
            }
        }catch (SQLException e){
            System.err.println("Exception of SQL in WHERE Clause");
        }
        return null;
    }

    public synchronized OperatoreTelefonico inserisciOperatoreTelefonico(OperatoreTelefonico op) {
        OperatoreTelefonico tmp = findOperatore(op.getIdentificativo());
        if (tmp != null) {
           tmp = manager.addOperatoreTelefonico(op, connection);
           if(tmp != null) return tmp;
           else return null;
        }else return tmp;
    }

}

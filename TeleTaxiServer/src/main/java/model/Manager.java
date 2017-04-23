package model;

import control.GestorePersonale;
import org.restlet.engine.util.Base64;
import resources.BaseColumns;
import resources.exception.*;
import websource.DatabaseManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by dn on 21/03/17.
 */
public class Manager extends Persona {
    private String username, password;
    private static Manager instance;
    private static Connection connection;

    public Manager() {
    }

    public Manager(String nome, String cognome, Date dataDiNascita, String username, String password) throws ConnectionSQLFailException, InserisciManagerFailException {
        super(nome, cognome, dataDiNascita);
        this.username = username;
        this.password = Base64.encode(password.getBytes(), true);
        DatabaseManager db = DatabaseManager.getInstance();
            try {
                connection = db.getConnection();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                PreparedStatement statement;
                try {
                    statement = connection.prepareStatement("INSERT INTO " + BaseColumns.TAB_MANAGER +
                            "(" + BaseColumns.USERNAME_MANAGER + "," + BaseColumns.NOME_PERSONA + "," + BaseColumns.COGNOME_PERSONA + "," +
                            BaseColumns.PASSWORD + "," + BaseColumns.DATA_DI_NASCITA_PERSONA + ")" + " VALUES(?,?,?,?,?)");
                    statement.setString(1, username);
                    statement.setString(2, nome);
                    statement.setString(3, cognome);
                    statement.setString(4, Base64.encode(password.getBytes(), true));
                    statement.setTimestamp(5, new java.sql.Timestamp(dataDiNascita.getTime()));
                    statement.executeUpdate();
                } catch (SQLException e) {
                    throw new InserisciManagerFailException(Integer.toString(e.getErrorCode()));
                } catch (Exception e) {
                    throw new InserisciManagerFailException(e.getMessage());
                }
            } catch (NullPointerException e) {
                throw new ConnectionSQLFailException(e.getMessage());
            } catch (Exception e) {
                throw new InserisciManagerFailException(e.getMessage());
            }
    }


    public String getUsername() {
        return username;
    }

    public Manager setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Manager setPassword(String password) {
        this.password = Base64.encode(password.getBytes(), true);
        return this;
    }

    public synchronized static Manager getInstance() throws ConnectionSQLFailException, FindManagerFailException, InserisciManagerFailException {
        if (instance == null) {
            try {
                File f = new File("./TeleTaxiServer/conf.dati");
                Scanner s = new Scanner(f);
                String initialKey = s.nextLine();
                if (initialKey.equalsIgnoreCase("true")) {
                    instance = new Manager(s.nextLine(), s.nextLine(), new SimpleDateFormat("dd/MM/yyyy").parse(s.nextLine()), "admin", s.nextLine());
                    f.delete();
                    f.createNewFile();
                    PrintWriter printStream = new PrintWriter(f);
                    printStream.println("false");
                    printStream.print(instance.getUsername());
                    printStream.close();
                    return instance;
                } else if (initialKey.equalsIgnoreCase("false")) {
                    Manager toReturn = GestorePersonale.getInstance().findManager(s.nextLine());
                    if (toReturn != null) {
                        connection = DatabaseManager.getInstance().getConnection();
                        return instance = toReturn;
                    }
                    else {
                        return instance = new Manager("Amministratore", "", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2017"), "admin", "admin");
                    }
                } else
                    return instance = new Manager("Amministratore", "", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2017"), "admin", "admin");
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
                return null;
            } catch (ParseException e) {
                System.err.println("Data di nascita non riconosciuta");
                System.exit(-1);
                return null;
            } catch (IOException e) {
                System.err.println("Errore I0");
                System.exit(-1);
                return null;
            } catch (Exception e) {
                System.err.println("Errore irreversibile " + e.getMessage());
                System.exit(-1);
                return null;
            }
        } else return instance;
    }

    public synchronized OperatoreTelefonico addOperatoreTelefonico(OperatoreTelefonico o) throws InserisciOperatoreFailException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO " + BaseColumns.TAB_OPERATORI_TELEFONICI +
                    "(" + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + "," + BaseColumns.NOME_PERSONA + "," + BaseColumns.COGNOME_PERSONA + "," +
                    BaseColumns.DATA_DI_NASCITA_PERSONA + "," + BaseColumns.PASSWORD + ")" + " VALUES(?,?,?,?,?)");
            statement.setString(1, o.getIdentificativo());
            statement.setString(2, o.getNome());
            statement.setString(3, o.getCognome());
            statement.setTimestamp(4, new java.sql.Timestamp(o.getDataDiNascita().getTime()));
            statement.setString(5, o.getPassword());
            statement.executeUpdate();
            return o;
        } catch (SQLException e) {
            throw new InserisciOperatoreFailException(Integer.toString(e.getErrorCode()));
        } catch (Exception e) {
            throw new InserisciOperatoreFailException(e.getMessage());
        }
    }

    public synchronized OperatoreTelefonico eliminaOperatoreTelefonico(OperatoreTelefonico op) throws EliminaOperatoreTelefonicoFailException {
        Statement statement;
        try {
            String sql = "DELETE FROM " + BaseColumns.TAB_OPERATORI_TELEFONICI + " WHERE " + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + " = '" + op.getIdentificativo() + "'";
            statement = connection.createStatement();
            statement.execute(sql);
            return op;
        } catch (SQLException e) {
            throw new EliminaOperatoreTelefonicoFailException(Integer.toString(e.getErrorCode()));
        } catch (Exception e) {
            throw new EliminaOperatoreTelefonicoFailException(e.getMessage());
        }
    }

    public synchronized OperatoreTelefonico updateOperatoreTelefonico(OperatoreTelefonico op) throws UpdateOperatoreTelefonicoFailException {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE " + BaseColumns.TAB_OPERATORI_TELEFONICI + " SET " + BaseColumns.NOME_PERSONA + " = ?," +
                            BaseColumns.COGNOME_PERSONA + " = ?," + BaseColumns.DATA_DI_NASCITA_PERSONA + " = ?,"
                            + BaseColumns.PASSWORD + " = ? " + " WHERE " + BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO + " = ?");
            ps.setString(1, op.getNome());
            ps.setString(2, op.getCognome());
            ps.setTimestamp(3, new java.sql.Timestamp(op.getDataDiNascita().getTime()));
            ps.setString(4, op.getPassword());
            ps.setString(5, op.getIdentificativo());
            ps.executeUpdate();
            return op;
        } catch (SQLException e) {
            throw new UpdateOperatoreTelefonicoFailException(Integer.toString(e.getErrorCode()));
        } catch (Exception e) {
            throw new UpdateOperatoreTelefonicoFailException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Manager: " + "username: '" + username + '\'';
    }

}

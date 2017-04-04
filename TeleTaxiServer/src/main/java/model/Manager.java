package model;

import resources.BaseColumns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dn on 21/03/17.
 */
public class Manager extends Persona{
    private String username, password;
    private static Manager instance;

    public Manager(String nome, String cognome, Date dataDiNascita, String username, String password) {
        super(nome, cognome, dataDiNascita);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public Manager setUsername(String username) {this.username = username; return this;}

    public String getPassword() {
        return password;
    }

    public Manager setPassword(String password) {this.password = password; return this;}

    public static Manager getInstance(){
        if(instance==null) return new Manager("Domenico","Natella", null, "admin","admin");
        else return instance;
    }

    public OperatoreTelefonico addOperatoreTelefonico(OperatoreTelefonico o, Connection connect){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        PreparedStatement statement;
        try {
            statement = connect.prepareStatement("INSERT INTO "+ BaseColumns.TAB_OPERATORI_TELEFONICI+
                    "("+BaseColumns.IDENTIFICATIVO_OPERATORE_TELEFONICO+","+BaseColumns.NOME_PERSONA+","+BaseColumns.COGNOME_PERSONA+","+
                    BaseColumns.DATA_DI_NASCITA_PERSONA+","+BaseColumns.PASSWORD+")"+" VALUES(?,?,?,?,?)");
            statement.setString(1,o.getIdentificativo());
            statement.setString(2,o.getNome());
            statement.setString(3, o.getCognome());
            statement.setTimestamp(4, new java.sql.Timestamp(o.getDataDiNascita().getTime()));
            statement.setString(5, o.getPassword());
            statement.executeUpdate();
            return o;
        } catch (SQLException e) {
            System.err.print("Exception of SQL"+e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {return "Manager: " + "username: '" + username + '\'';}
}

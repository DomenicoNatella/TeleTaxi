package model;

import resources.BaseColumns;
import resources.exception.ConnectionSQLFailException;
import resources.exception.InserisciClienteFailException;
import websource.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dn on 21/03/17.
 */
public class Cliente extends Persona {

    private int telefono;
    private String codiceCliente;
    private Connection connection = null;

    public Cliente(String codiceCliente, String nome, String cognome, Date dataDiNascita, int telefono) {
        super(nome, cognome, dataDiNascita);
        this.codiceCliente = codiceCliente;
        this.telefono = telefono;
    }

    public String getCodiceCliente() {
        return codiceCliente;
    }

    public Cliente setCodiceCliente(String codiceCliente) {
        this.codiceCliente = codiceCliente;
        return this;
    }

    public int getTelefono() {
        return telefono;
    }

    public Cliente setTelefono(int telefono) {
        this.telefono = telefono;
        return this;
    }

    public synchronized void inserisciCliente() throws InserisciClienteFailException, ConnectionSQLFailException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            DatabaseManager db = DatabaseManager.getInstance();
            connection = db.getConnection();
            PreparedStatement statement;
            statement = connection.prepareStatement("INSERT INTO " + BaseColumns.TAB_CLIENTE +
                    "(" + BaseColumns.IDENTIFICATIVO_CLIENTE + "," + BaseColumns.NOME_PERSONA + "," + BaseColumns.COGNOME_PERSONA + "," +
                    BaseColumns.DATA_DI_NASCITA_PERSONA + "," + BaseColumns.TELEFONO + ")" + " VALUES(?,?,?,?,?)");
            statement.setString(1, this.getCodiceCliente());
            statement.setString(2, this.getNome());
            statement.setString(3, this.getCognome());
            df.format(this.getDataDiNascita());
            statement.setTimestamp(4, new java.sql.Timestamp(this.getDataDiNascita().getTime()+3600000));
            statement.setInt(5, this.getTelefono());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InserisciClienteFailException(Integer.toString(e.getErrorCode()));
        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());
            throw new InserisciClienteFailException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Cliente: " + "telefono:" + telefono;
    }
}

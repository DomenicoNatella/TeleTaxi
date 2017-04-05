package resources;

/**
 * Created by dn on 05/04/17.
 */
public class ConnectionSQLFailException extends Exception {
    public ConnectionSQLFailException(String s) {
        super("Connessione al database fallita: "+s);
    }
}

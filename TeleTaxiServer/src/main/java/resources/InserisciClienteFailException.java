package resources;

/**
 * Created by dn on 05/04/17.
 */
public class InserisciClienteFailException extends Exception {
    public InserisciClienteFailException(String s) {
        super("Errore nell'inserimento del cliente: "+s);

    }
}

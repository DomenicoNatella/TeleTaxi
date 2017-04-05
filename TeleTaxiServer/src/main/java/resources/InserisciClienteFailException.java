package resources;

/**
 * Created by dn on 05/04/17.
 */
public class InserisciClienteFailException extends Throwable {
    public InserisciClienteFailException(String s) {
        super("Errore nell'inserimento del cliente: "+s);

    }
}

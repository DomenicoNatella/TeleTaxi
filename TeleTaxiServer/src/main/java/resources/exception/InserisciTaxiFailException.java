package resources.exception;

/**
 * Created by dn on 05/04/17.
 */
public class InserisciTaxiFailException extends Exception {
    public InserisciTaxiFailException(String s) {
        super("Errore nell'inserimento del taxi: "+s);
    }
}

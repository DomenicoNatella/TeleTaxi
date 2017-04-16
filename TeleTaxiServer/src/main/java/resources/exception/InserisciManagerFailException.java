package resources.exception;

/**
 * Created by dn on 09/04/17.
 */
public class InserisciManagerFailException extends Exception {
    public InserisciManagerFailException(String s) {
        super("Errore nell'inserimento del manager: "+s);
    }
}

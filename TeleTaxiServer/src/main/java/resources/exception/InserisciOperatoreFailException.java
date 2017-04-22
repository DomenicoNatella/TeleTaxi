package resources.exception;

/**
 * Created by dn on 05/04/17.
 */
public class InserisciOperatoreFailException extends Exception {
    public InserisciOperatoreFailException(String s) {
        super("Errore nell'inserimento dell'operatore: "+s);
    }
}

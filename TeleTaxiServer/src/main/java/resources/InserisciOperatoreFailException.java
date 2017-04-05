package resources;

/**
 * Created by dn on 05/04/17.
 */
public class InserisciOperatoreFailException extends Throwable {
    public InserisciOperatoreFailException(String s) {
        super("Errore nell'inserimento della prenotazione: "+s);
    }
}

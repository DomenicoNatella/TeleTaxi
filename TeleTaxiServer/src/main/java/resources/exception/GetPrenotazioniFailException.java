package resources.exception;

/**
 * Created by dn on 05/04/17.
 */
public class GetPrenotazioniFailException extends Exception {
    public GetPrenotazioniFailException(String s) {
        super("Si e' verificato un errore durante l'esecuzione della richiesta: "+s);
    }
}

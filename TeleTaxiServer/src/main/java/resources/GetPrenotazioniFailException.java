package resources;

/**
 * Created by dn on 05/04/17.
 */
public class GetPrenotazioniFailException extends Throwable {
    public GetPrenotazioniFailException(String s) {
        super("Si e' verificato un errore durante l'esecuzione della richiesta: "+s);
    }
}

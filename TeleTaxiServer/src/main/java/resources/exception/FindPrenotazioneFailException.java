package resources.exception;

/**
 * Created by dn on 05/04/17.
 */
public class FindPrenotazioneFailException extends Exception {
    public FindPrenotazioneFailException(String s) {
        super("La prenotazione non e' stata trovata: "+s);
    }
}

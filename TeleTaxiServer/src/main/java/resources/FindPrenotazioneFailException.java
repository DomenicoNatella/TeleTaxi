package resources;

/**
 * Created by dn on 05/04/17.
 */
public class FindPrenotazioneFailException extends Throwable {
    public FindPrenotazioneFailException(String s) {
        super("La prenotazione non e' stata trovata: "+s);
    }
}

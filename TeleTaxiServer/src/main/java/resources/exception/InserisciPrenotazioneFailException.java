package resources.exception;

/**
 * Created by dn on 05/04/17.
 */
public class InserisciPrenotazioneFailException extends Exception {
    public InserisciPrenotazioneFailException(String s) {
        super("Errore nell'inserimento della prenotazione: "+s);

    }
}

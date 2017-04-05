package resources;

/**
 * Created by dn on 05/04/17.
 */
public class InserisciPrenotazioneFailException extends Throwable {
    public InserisciPrenotazioneFailException(String s) {
        super("Errore nell'inserimento della prenotazione: "+s);

    }
}

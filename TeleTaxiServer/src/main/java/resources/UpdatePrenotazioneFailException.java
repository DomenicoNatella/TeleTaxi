package resources;

/**
 * Created by dn on 12/04/17.
 */
public class UpdatePrenotazioneFailException extends Exception {
    public UpdatePrenotazioneFailException(String s) {
        super("Errore nell'update della prenotazione: "+s);
    }
}

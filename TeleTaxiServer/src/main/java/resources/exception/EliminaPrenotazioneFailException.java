package resources.exception;

/**
 * Created by dn on 05/04/17.
 */
public class EliminaPrenotazioneFailException extends Exception {
    public EliminaPrenotazioneFailException(String s) {
        super("Errore nell'eliminazione della prenotazione: "+s);
    }
}
